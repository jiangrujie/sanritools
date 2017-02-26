package com.sanri.app.servlet;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.lang.math.NumberUtils;

import sanri.utils.PathUtil;

import com.alibaba.fastjson.JSONObject;
import com.sanri.game.greens.DataCRUD;
import com.sanri.game.greens.Food;
import com.sanri.game.greens.Greens;
import com.sanri.game.greens.GrowthTime;
import com.sanri.game.greens.Price;

public class GreensServlet extends BaseServlet{
	private static Map<String,Map<String,String>> dicts = new HashMap<String, Map<String,String>>();
	private static Map<String,String> dictNames = new HashMap<String, String>();
	static{
		/**
		 * 字典项加载 
		 */
		String pkgPath = PathUtil.pkgPath("com.sanri.game.greens");
		try {
			File dictFile = new File(pkgPath+File.separator+"dict.xml");
			XMLConfiguration xmlConfiguration = new XMLConfiguration(dictFile);
			ConfigurationNode rootNode = xmlConfiguration.getRootNode();
			int dictCount = rootNode.getChildrenCount();
			for(int i=0;i<dictCount;i++){
				ConfigurationNode dict = rootNode.getChild(i);
				String currentName = dict.getName();
				String dictName = String.valueOf(dict.getAttribute(0).getValue());
				HashMap<String, String> currentDictMap = new HashMap<String, String>();
				dicts.put(currentName, currentDictMap);
				dictNames.put(currentName, dictName);
				
				int currentCount = dict.getChildrenCount();
				for(int j=0;j<currentCount;j++){
					ConfigurationNode current = dict.getChild(j);
					ConfigurationNode attribute = current.getAttribute(0);
					String value = String.valueOf(attribute.getValue());
					String text = String.valueOf(current.getValue());
					currentDictMap.put(value, text);
				}
			}
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	public GreensServlet(){
		this.setServletClass(GreensServlet.class);
	}
	
	@Override
	protected String getRootPath() {
		return "/greens/";
	}
	
	private DataCRUD dataCRUD = DataCRUD.getInstance();
	
	/**
	 * methods
	 */
	public void dicts(HttpServletRequest request,HttpServletResponse response){
		writeString(JSONObject.toJSONString(dicts), response);
	}
	public void dictsItems(HttpServletRequest request,HttpServletResponse response){
		String dictCode = request.getParameter("dictCode");
		Map<String, String> dict = dicts.get(dictCode);
		writeString(JSONObject.toJSONString(dict), response);
	}
	public void dictNames(HttpServletRequest request,HttpServletResponse response){
		writeString(JSONObject.toJSONString(dictNames), response);
	}
	public void listAllFood(HttpServletRequest request,HttpServletResponse response){
		List<Food> filterFood = dataCRUD.filterFood();
		writeString(JSONObject.toJSONString(filterFood), response);
	}
	public void listAllGreens(HttpServletRequest request,HttpServletResponse response){
		List<Greens> filterGreens = dataCRUD.filterGreens();
		writeString(JSONObject.toJSONString(filterGreens), response);
	}
	public void addFood(HttpServletRequest request,HttpServletResponse response){
		Food food = new Food();
		food.setId(UUID.randomUUID().toString());
		food.setName(request.getParameter("name"));
		food.setPrice(new Price(NumberUtils.toInt(request.getParameter("money")),"斤(500g)"));
		food.setFoodType(request.getParameter("foodType"));
		food.setGrowthTime(new GrowthTime("any",-1));
		food.setQuality(request.getParameter("quality"));
		food.setSingleScore(5);
		food.setTaste("sweet");
		food.addCookingMethod("炒")
			.addCookingMethod("蒸")
			.addCookingMethod("炸");
		dataCRUD.addFood(food);
	}
	
	public void addGreens(HttpServletRequest request,HttpServletResponse response){
//		Greens greens = new Greens();
//		greens.setId("rou01");
//		greens.setName("肉打汤");
//		greens.addFood(dataCRUD.selectFood("lizhong"));
//		greens.setCuisines("hunan");
//		greens.addCookingMethod("油煎")
//			.addCookingMethod("放水")
//			.addCookingMethod("放盐")
//			.addCookingMethod("出锅");
//		dataCRUD.addGreens(greens);
	}
}
