package com.sanri.game.greens;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import sanri.utils.PathUtil;
import sanri.utils.StringUtil;
import sanri.utils.Validate;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 
 * 创建时间:2016-11-11下午9:09:40<br/>
 * 创建者:sanri<br/>
 * 功能:数据存取,主要是食物和菜<br/>
 */
public class DataCRUD {
	private Log logger = LogFactory.getLog(DataCRUD.class);
	private XStream xStream = new XStream(new DomDriver("utf-8"));
	private SAXReader saxReader = new SAXReader();
	private String basePath;
	public final static String foodsXmlName = "foods.xml";
	public final static String greensXmlName = "greenes.xml";
	private Charset charset = Charset.forName("utf-8");
	
	private static DataCRUD dataCRUD = new DataCRUD();
	public static DataCRUD getInstance(){
		return dataCRUD;
	}
	
	private DataCRUD(){
		xStream.alias("food", Food.class);
		xStream.alias("greens", Greens.class);
		xStream.addImplicitCollection(String.class, "id");
//		basePath = PathUtil.pkgPath("com.sanri.game.greens");
		basePath = PathUtil.webAppsPath()+File.separator+"greens";
	}
	
	/**
	 * 
	 * 功能:这个只是娱乐性质的程序,不能对同一 xml 文件获取多次连接,同时操作同一文件可能存在问题<br/>
	 * 创建时间:2016-11-12上午9:53:44<br/>
	 * 作者：sanri<br/>
	 * @param basePath
	 * @param xmlFileName
	 * @return<br/>
	 */
	public Document getConnection(String xmlFileName){
		Document document = null;
		try {
			File fileDir = new File(basePath);
			if(!fileDir.exists()){
				fileDir.mkdirs();
			}
			File xmlFile = new File(fileDir,xmlFileName);
			if(!xmlFile.exists()){
				xmlFile.createNewFile();
				FileUtils.writeStringToFile(xmlFile,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				String rootElement = StringUtil.prefix(xmlFileName);
				FileUtils.writeStringToFile(xmlFile, "<"+rootElement+"></"+rootElement+">");
			}
			Reader reader = new InputStreamReader(new FileInputStream(xmlFile), charset);
			document = saxReader.read(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	protected void add(BaseEntity obj,String xmlFileName){
		Document document = getConnection(xmlFileName);
		String xml = xStream.toXML(obj);
		try {
			Document parseText = DocumentHelper.parseText(xml);
			Element rootElement = parseText.getRootElement();
			document.getRootElement().add(rootElement);
			rootElement.addAttribute("id", obj.getId());
			writer(document, xmlFileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	protected boolean delete(String xmlFileName,String xpath){
		Document document = getConnection(xmlFileName);
		Node element =  document.selectSingleNode(xpath);
//		boolean remove = document.remove(element);	//这样删除不掉
		if(element == null){
			return false;
		}
		boolean delete = element.getParent().remove(element);
		try {
			writer(document, xmlFileName);
			return delete;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	protected void update(BaseEntity obj,String xmlFileName,String xpathQuery){
		delete(xmlFileName,xpathQuery);
		add(obj,xmlFileName);
	}
	
	 /** 
     * 把document对象写入新的文件 
     *  
     * @param document 
	 * @throws IOException 
     * @throws Exception 
     */  
	protected void writer(Document document,String xmlFileName) throws IOException {  
        // 紧凑的格式  
        // OutputFormat format = OutputFormat.createCompactFormat();  
        // 排版缩进的格式  
        OutputFormat format = OutputFormat.createPrettyPrint();  
        // 设置编码  
        format.setEncoding(charset.displayName());
        // 创建XMLWriter对象,指定了写出文件及编码格式  
        File xmlFile = new File(basePath+File.separator+xmlFileName);
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(xmlFile), charset), format);  
        writer.write(document);  
        writer.flush();  
        writer.close();  
    }  
	
	public void addFood(Food food){
		add(food, foodsXmlName);
	}
	public void addGreens(Greens greens){
		add(greens, greensXmlName);
	}
	public void deleteFood(String id){
		delete(foodsXmlName, "/foods/food[@id='"+id+"']");
	}
	public Food selectFood(String id){
		Document document = getConnection(foodsXmlName);
		Node element =  document.selectSingleNode("/foods/food[@id='"+id+"']");
		return (Food) xStream.fromXML(element.asXML());
	}
	public void deleteGreens(String id){
		delete(greensXmlName, "/greenes/greens[@id='"+id+"']");
	}
	public void updateFood(Food food){
		update(food, foodsXmlName, "/foods/food[@id='"+food.getId()+"']");
	}
	public void updateGreens(Greens greens){
		update(greens, greensXmlName, "/greenes/greens[@id='"+greens.getId()+"']");
	}
	
	public List<Food> filterFood(){
		Document document = getConnection(foodsXmlName);
		List<Node> selectNodes = document.selectNodes("//food");
		List<Food> foods = new ArrayList<Food>();
		if(!Validate.isEmpty(selectNodes)){
			for (Node node : selectNodes) {
				String xmlString = node.asXML();
//				System.out.println(xmlString);
				foods.add((Food) xStream.fromXML(xmlString));
			}
		}
		return foods;
	}

	public List<Greens> filterGreens() {
		Document document = getConnection(greensXmlName);
		List<Node> selectNodes = document.selectNodes("//greens");
		List<Greens> greens = new ArrayList<Greens>();
		if(!Validate.isEmpty(selectNodes)){
			for (Node node : selectNodes) {
				String xmlString = node.asXML();
				greens.add((Greens) xStream.fromXML(xmlString));
			}
		}
		return greens;
	}
	
	public static void main(String[] args) {
		DataCRUD instance = getInstance();
//		Populator populator = new PopulatorBuilder().build();
//		Food populateBean = populator.populateBean(Food.class);
//		Food food = new Food();
//		food.setId("lizhong");
//		food.setName("李仲");
//		food.setPrice(new Price(1500,"斤(500g)"));
//		food.setFoodType("meat");
//		food.setGrowthTime(new GrowthTime("any",-1));
//		food.setQuality("unknow");
//		food.setSingleScore(5);
//		food.setTaste("sweet");
//		food.addCookingMethod("炒")
//			.addCookingMethod("蒸")
//			.addCookingMethod("炸");
//		instance.addFood(food);
		
//		instance.deleteFood("lajiao");
		instance.filterFood();
	}
}
