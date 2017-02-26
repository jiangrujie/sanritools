package com.sanri.file.xls;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

public class SheetConfig {
	private Sheet sheet;
	private int index;
	private String name;
	private List<? extends Object> data;
	/**
	 * 将某个区域定义为 某种样式 key 区域 
	 * key: head body 优先常量值
	 * key: $E$5:$G$9 excel 的表示形式 只选中一个格子就为 $E$5 大小写不区分 数字从0 开始字母从 A开始超过 26 用AA
	 * key: $A$0:$max$0 	max 指数据最大量 例:数据有 15列的话 max=15 也即 head
	 * key: $A$1:max		指第一行的 0 列 到最大行的最大列 即 body
	 */
	private Map<String,CellStyle> styles;
	/**
	 * 每个属性的中文意思,不传的话就以英文做标题
	 */
	private Map<String,String> chinese;
	/**
	 * 每个字段的宽度,像素为单位 String 传 字段名
	 */
	private Map<String,Integer> widthConfig;
	/**
	 * 行设置高度,像素为单位 String 传 n~m m 可为 max
	 */
	private Map<String,Integer> heightConfig;
	/**
	 * 需要隐藏的列
	 */
	private List<String> hiddens;
	
	public SheetConfig(){}
	public SheetConfig(String name,List<? extends Object> data){
		this.name = name;
		this.data = data;
	}
	//set & get
	public Sheet getSheet() {
		return sheet;
	}
	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name; 
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<? extends Object> getData() {
		return data;
	}
	public void setData(List<? extends Object> data) {
		this.data = data;
	}
	public Map<String, String> getChinese() {
		return chinese;
	}
	public void setChinese(Map<String, String> chinese) {
		this.chinese = chinese;
	}
	public List<String> getHiddens() {
		return hiddens;
	}
	public void setHiddens(List<String> hiddens) {
		this.hiddens = hiddens;
	}
	public Map<String, CellStyle> getStyles() {
		return styles;
	}
	public void setStyles(Map<String, CellStyle> styles) {
		this.styles = styles;
	}
	public Map<String, Integer> getWidthConfig() {
		return widthConfig;
	}
	public void setWidthConfig(Map<String, Integer> widthConfig) {
		this.widthConfig = widthConfig;
	}
	public Map<String, Integer> getHeightConfig() {
		return heightConfig;
	}
	public void setHeightConfig(Map<String, Integer> heightConfig) {
		this.heightConfig = heightConfig;
	}
}
