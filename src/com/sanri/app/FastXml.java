package com.sanri.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sanri.utils.StringUtil;
import sanri.utils.Validate;

/**
 * 
 * 创建时间:2016-10-8下午5:44:02<br/>
 * 创建者:sanri<br/>
 * 功能:本类从文件系统读取 xml 结构生成 xml ,用于需要快速配置 xml 的情况<br/>
 * 注:只能生成一级<br/>
 */
public class FastXml {
	private static DocumentBuilderFactory factory;
	private static DocumentBuilder builder;
	private static Log logger = LogFactory.getLog(FastXml.class);
	static{
		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static Document startXml(String dirPath){
		return startXml(new File(dirPath));
	}
	/**
	 * 
	 * 功能:<br/>
	 * 创建时间:2016-10-9下午8:55:33<br/>
	 * 作者：sanri<br/>
	 * @param dir 需要生成 xml 的文件系统模板
	 * @param destDir 生成后的文件路径,文件名会取目录名
	 * @return Document 目录下将有多个文件,每个文件名是一个属性,这个属性又对应多个值<br/>
	 * 文件中一行为一列,如果此列属性没有值,留空行
	 */
	public static Document startXml(File dir){
		if(Validate.isEmpty(dir) || dir.isFile()){
			return null;
		}
		//下面开始正式生成
		Document doc = builder.newDocument();
		String nodeName = dir.getName();
		Element root = doc.createElement(nodeName+"s");
		doc.appendChild(root);
		
		File[] listFiles = dir.listFiles();
		if(Validate.isEmpty(listFiles)){
			return doc;
		}
		Map<String,List<String>> propertyDatas = new HashMap<String, List<String>>();
		try{
			//读取文本值,处理两边空白
			for (File file : listFiles) {
				String propertyName = StringUtil.prefix(file.getName());
				Reader input = new InputStreamReader(new FileInputStream(file),"utf-8");
				List<String> readLines = IOUtils.readLines(input);
				readLines = handlerLines(readLines);
				propertyDatas.put(propertyName, readLines);
			}
			//生成 document 树,思路是先生成一遍结点,然后再回过头来加属性
			int propertyCount = 0;boolean isCount = false;
			Iterator<Entry<String, List<String>>> propertyIterator = propertyDatas.entrySet().iterator();
			while(propertyIterator.hasNext()){
				Entry<String, List<String>> propertyEntry = propertyIterator.next();
				String propertyName = propertyEntry.getKey();
				List<String> propertyValues = propertyEntry.getValue();
				if(Validate.isEmpty(propertyValues)){
					logger.warn(propertyName+" 属性没有数据");
					break;
				}
				if(!isCount){
					for (String propertyValue : propertyValues) {
						Element element = doc.createElement(nodeName);
						element.setAttribute(propertyName, propertyValue);
						root.appendChild(element);
						
						propertyCount ++;
					}
					isCount = true;
					continue;
				}
				if(propertyValues.size() != propertyCount){
					throw new IllegalArgumentException("属性参数个数不匹配 "+propertyName);
				}
				//否则就是添加属性,而不需要追加元素了
				NodeList childNodes = root.getChildNodes();
				for (int i = 0; i < propertyCount; i++) {
					Element element = (Element) childNodes.item(i);
					element.setAttribute(propertyName, propertyValues.get(i));
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return doc;
	}
	/**
	 * 用于去掉空行和两边空白
	 */
	private static List<String> handlerLines(List<String> lines){
		List<String> handlerLines = new ArrayList<String>();
		if(!Validate.isEmpty(lines)){
			for (String line : lines) {
				handlerLines.add(line.trim());
			}
		}
		return handlerLines;
	}
	
	public static File generateFile(Document doc,String destDirPath){
		return generateFile(doc, new File(destDirPath));
	}
	/**
	 * 
	 * 功能:生成目标文件<br/>
	 * 创建时间:2016-10-9下午9:43:13<br/>
	 * 作者：sanri<br/>
	 * @param doc 文档对象
	 * @param destDir 目标目录
	 * @return<br/>
	 */
	public static File generateFile(Document doc,File destDir){
		if(Validate.isEmpty(destDir)){
			destDir.mkdirs();
		}
		Transformer transformer = null;
		Node firstChild = doc.getFirstChild();
		String fileName = String.valueOf(System.currentTimeMillis());
		if(firstChild != null){
			fileName = firstChild.getNodeName();
		}
		File destFile = new File(destDir,fileName+".xml");
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			//设置换行和缩进
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.transform(new DOMSource(doc), new StreamResult(destFile));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return destFile;
	}
	
	public static void main(String[] args) {
		Document startXml = FastXml.startXml("D:/test/xmlgenerate/field");
		File generateFile = FastXml.generateFile(startXml, "D:\\test\\xmlgenerate");
		System.out.println(generateFile);
	}
}
