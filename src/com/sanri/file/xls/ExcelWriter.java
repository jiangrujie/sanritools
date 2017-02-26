package com.sanri.file.xls;

import java.beans.Introspector;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sanri.utils.BeanUtil;
import sanri.utils.StringUtil;
import sanri.utils.Validate;

public class ExcelWriter extends File{
	public ExcelWriter(String pathname,int version) {
		super(pathname);
		init(version);
	}
	public ExcelWriter(File parent,String child,int version) {
		super(parent,child);
		init(version);
	}
	private void init(int version) {
		this.version = version;
		if(version == EXCEL_2003){
			workbook = new HSSFWorkbook();
		}else if(version == EXCEL_2007){
			workbook = new XSSFWorkbook();
		}else{
			throw new IllegalArgumentException("不支持的 excel 版本");
		}
	}
	private Workbook workbook;
	private List<SheetConfig> configs = new ArrayList<SheetConfig>();
	private int version = EXCEL_2003;
			
	public final static int EXCEL_2007 = 2007;
	public final static int EXCEL_2003 = 2003;
	
	public final static float BASE_WIDTH_1_PX = 35.7f;
	public final static float BASE_HEIGHT_1_PX = 15.625f;
	public final static float BASE_CHINESE = 2 * 256;
	
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-28下午3:38:52</br>
	 * 功能:添加一个 sheet ,已经包装成 sheetConfig </br>
	 */
	public Sheet sheet(SheetConfig sheetConfig) {
		this.configs.add(sheetConfig);
		if (sheetConfig == null) {return null;}
		
		//设置 index 当前 sheet 的索引 
		sheetConfig.setIndex(configs.size());
		//设置 sheetName 没有配置 name 则取索引为 name
		String sheetName = sheetConfig.getName();
		if(StringUtil.isBlank(sheetName)){
			sheetName = String.valueOf(sheetConfig.getIndex());
		}
		//设置 sheet 
		Sheet currentSheet = workbook.createSheet(sheetName);
		sheetConfig.setSheet(currentSheet);
		//数据为空,则为空 sheet
		List<? extends Object> data = sheetConfig.getData();
		if (Validate.isEmpty(data)) {return sheetConfig.getSheet();}
		
		//配置信息 begin
		Map<String, String> chinese = sheetConfig.getChinese();			// 中文配置
		Map<String, Integer> widthConfig = sheetConfig.getWidthConfig();// 宽度配置
		Map<String,Integer> heightConfg = sheetConfig.getHeightConfig();// 高度配置
		List<String> hiddens = sheetConfig.getHiddens();				// 隐藏列配置
		String hiddensStr = ","+StringUtil.join(hiddens,",")+",";
		Map<String, CellStyle> styles = sheetConfig.getStyles();
		sheetConfig.setIndex(workbook.getSheetIndex(sheetName));		//创建 sheet 索引
		chinese = chinese == null ? new HashMap<String, String>() : chinese;
		widthConfig = widthConfig == null ? new HashMap<String, Integer>() : widthConfig;
		
		Class<?> dataClass = data.get(0).getClass();
		List<Method> findMethod = BeanUtil.findMethod(dataClass, BeanUtil.GET_METHOD_FILTER);
		
		if(!Validate.isEmpty(findMethod)){
			String title,chineseTitle;
			Method method = null;
			Row headRow = currentSheet.createRow(0);
			Cell cell = null;
			
			//标题生成
			for (int k=0;k<findMethod.size();k++) {
				method = findMethod.get(k);
				String methodName = method.getName();
				title = Introspector.decapitalize(methodName.substring(3));
				chineseTitle = chinese.get(title);
				chineseTitle = chineseTitle == null ? title : chineseTitle;
				
				//宽度配置 
				if(widthConfig.get(title) == null){
					currentSheet.autoSizeColumn(k);
				}else{
					currentSheet.setColumnWidth(k, widthConfig.get(title));
				}
				//隐藏列配置 
				if(hiddensStr.indexOf(","+title+",") != -1){
					currentSheet.setColumnHidden(k, true);
				}
				cell = headRow.createCell(k,Cell.CELL_TYPE_STRING);
				cell.setCellValue(chineseTitle);
			}//end for 标题生成
			
			try{
				//内容生成
				Object object = null;
				Object propertyValue = null;Row row = null;
				Class<? extends Object> valueType = null;
				for (int j = 0; j < data.size(); j++) {
					object = data.get(j);
					row = currentSheet.createRow(j + 1);
					// 遍历对象的所有属性
					for (int i = 0; i < findMethod.size(); i++) {
						method = findMethod.get(i);
						propertyValue = method.invoke(object);
						valueType = method.getReturnType();
						if (isPrimitive(valueType) && valueType != String.class) {
							String tempValue = ObjectUtils.toString(propertyValue);
							if (StringUtil.isBlank(tempValue)) {
								tempValue = "0";
							}
							if (valueType == boolean.class || valueType == Boolean.class) {
								cell = row.createCell(i, Cell.CELL_TYPE_BOOLEAN);
								cell.setCellValue(Boolean.parseBoolean(tempValue));
							} else if (valueType == char.class || valueType == Character.class) {
								cell = row.createCell(i, Cell.CELL_TYPE_STRING);
								cell.setCellValue(tempValue.charAt(0));
							} else {
								// 除了 boolean char 其它的都是 数字型
								cell = row.createCell(i, Cell.CELL_TYPE_NUMERIC);
								cell.setCellValue(Double.parseDouble(tempValue));
							}
						} else if (valueType == Calendar.class) {
							cell = row.createCell(i, Cell.CELL_TYPE_STRING);
							cell.setCellValue((Calendar) propertyValue);
						} else if (valueType == Date.class) {
							cell = row.createCell(i, Cell.CELL_TYPE_STRING);
							cell.setCellValue((Date) propertyValue);
						} else {
							// 最后所有的都认为是字符串型
							cell = row.createCell(i, Cell.CELL_TYPE_STRING);
							cell.setCellValue(ObjectUtils.toString(propertyValue));
						}
					}
				}
				//设置行高
				configHeight(currentSheet,heightConfg);
				//加上样式
				addStyle(currentSheet,styles);
			}catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} 
		}
		return sheetConfig.getSheet();
	}
	
	private void configHeight(Sheet sheet, Map<String, Integer> heightConfg) {
		if(sheet == null || Validate.isEmpty(heightConfg)){return ;}
		Row row = null;
		Entry<String, Integer> next = null;
		String expression = "";
		Integer height = null;
		Integer [] cols = null;
		int countRow = sheet.getPhysicalNumberOfRows();
		for(Iterator<Entry<String, Integer>> it = heightConfg.entrySet().iterator();it.hasNext();){
			next = it.next();
			expression = next.getKey();
			height = next.getValue();
			cols = handleHeight(expression,countRow);
			if(cols == null || cols[0] > cols[1]){break;}
			for(int i = cols[0];i<=cols[1];i++){
				row = sheet.getRow(i);			
				row.setHeight((short)(height.shortValue()));
			}
		}
	}
	Pattern expression1 = Pattern.compile("^\\d+~([0-9]+|max)$");
	Pattern expression2 = Pattern.compile("^\\d+~\\d+$");
	//TODO 没有考虑边界值问题
	private Integer[] handleHeight(String expression,int countRow) {
		if(StringUtil.isBlank(expression) || !Validate.validate(expression,expression1 )){return null;}
		Integer[] cols = new Integer [2] ;
		String[] split = expression.split("~");
		cols[0] = Integer.parseInt(split[0]) - 1;
		if(Validate.validate(expression,expression2 )){
			cols[1] = Integer.parseInt(split[1]) - 1; 
		}else{
			cols[1] = countRow - 1;
		}
		return cols;
	}
	
	/**
	 * 字符型,原始型,原始型包装型 都认为是原始型
	 * @param clazz
	 * @return
	 */
	private boolean isPrimitive(Class<?> clazz){
		return clazz.isPrimitive() || clazz == String.class
				|| clazz == Integer.class || clazz == Short.class
				|| clazz == Long.class || clazz == Float.class
				|| clazz == Double.class || clazz == Character.class
				|| clazz == Boolean.class || clazz == Byte.class;
	}
	
	/**
	 * 解析表达式,表达式规则 @see SheetConfig
	 * @param expression
	 * @param countRow
	 * @param countColumn
	 * @return
	 */
	private final static Pattern AREA_SELECT1 = Pattern.compile("\\$[a-zA-Z]+\\$[a-zA-Z0-9]+:\\$[a-zA-Z]+\\$[a-zA-Z0-9]+");
	private final static Pattern AREA_SELECT2 = Pattern.compile("\\$[a-zA-Z]+\\$[a-zA-Z0-9]+:max");
	private final static Pattern AREA_SELECT3 = Pattern.compile("\\$[a-zA-Z]+\\$[a-zA-Z0-9]+");
	
	/**
	 * 遍历所有样式把需要的区域的单元格加上样式
	 * @param sheet
	 * @param styles
	 */
	private void addStyle(Sheet sheet, Map<String, CellStyle> styles) {
		if(sheet == null || Validate.isEmpty(styles)){return ;}
		int countRow = sheet.getPhysicalNumberOfRows();
		Row row = sheet.getRow(0);
		int countColumn = row.getPhysicalNumberOfCells();
		Entry<String, CellStyle> next = null;
		CellRangeAddress address = null;
		Cell cell = null;
		String expression = "";
		CellStyle style = null;
		for(Iterator<Entry<String, CellStyle>> it = styles.entrySet().iterator();it.hasNext();){
			next = it.next();
			expression = next.getKey();
			style = next.getValue();
			address = parseExp(expression,countRow,countColumn);	//解析表达式,得到地址
			for(int i = address.getFirstRow();i <= address.getLastRow();i++){
				row = sheet.getRow(i);
				for(int j = address.getFirstColumn();j <= address.getLastColumn();j++){
					cell = row.getCell(j);
					cell.setCellStyle(style);
				}
			}
		}
	}
	
	private CellRangeAddress parseExp(String expression, int countRow, int countColumn) {
		if("head".equalsIgnoreCase(expression)){
			return new CellRangeAddress(0,0,0,countColumn - 1);
		}
		if("body".equalsIgnoreCase(expression)){
			return new CellRangeAddress(1,countRow - 1,0,countColumn - 1);
		}
		if(Validate.validate(expression, AREA_SELECT1)){
			String[] leftTop$rightBottom = expression.split(":");
			String top = ObjectUtils.toString(leftTop$rightBottom[0].split("\\$")[1], "0");
			String left = ObjectUtils.toString(leftTop$rightBottom[0].split("\\$")[2],"0");
			String bottom = ObjectUtils.toString(leftTop$rightBottom[1].split("\\$")[1],"0");
			String right = ObjectUtils.toString(leftTop$rightBottom[1].split("\\$")[2],"0");
			
			return new CellRangeAddress(handle(left,countRow),handle(right,countRow),handle(top,countColumn),handle(bottom,countColumn));
		}
		if(Validate.validate(expression, AREA_SELECT2)){
			String[] leftTop = expression.split(":");
			String top = ObjectUtils.toString(leftTop[0].split("\\$")[1], "0");
			String left = ObjectUtils.toString(leftTop[0].split("\\$")[2],"0");
			
			return new CellRangeAddress(handle(left, countRow), countRow - 1, handle(top, countColumn), countColumn - 1);
		}
		if(Validate.validate(expression, AREA_SELECT3)){
			String top = ObjectUtils.toString(expression.split("\\$")[1], "0");
			String left = ObjectUtils.toString(expression.split("\\$")[2],"0");
			
			return new CellRangeAddress(handle(left, countRow), handle(left, countRow), handle(top, countRow), handle(top, countRow));
		}
		throw new RuntimeException("您的表达式暂未实现");
	}
	
	private int handle(String area, int max) {
		int intValue = 0;
		if(area.equalsIgnoreCase("max")){
			intValue = max;
		}else{
			if(Validate.validate(area, Validate.ALPHABETIC)){
				intValue = getColumnNumber(area);
			}else{
				intValue = Integer.parseInt(area);
			}
		}
		intValue -= 1;
		return intValue >= max ? max - 1 : intValue;
	}
	
	/**
	 * Change excel column letter to integer number
	 * 
	 * @param column
	 *            column letter of excel file, like A,B,AA,AB
	 * @return 从 1 开始
	 */
	private int getColumnNumber(String column) {
		int length = column.length();
		short result = 0;
		for (int i = 0; i < length; i++) {
			char letter = column.toUpperCase().charAt(i);
			int value = letter - 'A' + 1;
			result += value * Math.pow(26, length - i - 1);
		}
//		return result - 1;
		return result;
	}
	
	/**
	 * 获取 sheet 配置
	 * @param index
	 * @return
	 */
	public SheetConfig getSheetConf(int index){
		for (SheetConfig sheetConf : configs) {
			if(sheetConf.getIndex() == index){
				return sheetConf;
			}
		}
		return null;
	}
	/**
	 * 获取 sheet 配置
	 * @param sheetName
	 * @return
	 */
	public SheetConfig getSheetConf(String sheetName){
		if(StringUtil.isBlank(sheetName)){return null;}
		for (SheetConfig sheetConf : configs) {
			if(sheetName.equals(sheetConf.getName())){
				return sheetConf;
			}
		}
		return null;
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-28下午3:56:40</br>
	 * 功能:获取 excel 版本 2003,2007</br>
	 */
	public int getVersion() {
		return version;
	}
	
	public CellStyle createCellStyle() {
		return this.workbook.createCellStyle();
	}

	public Font createFont() {
		return this.workbook.createFont();
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-28下午3:54:07</br>
	 * 功能:生成 excel 到文件 </br>
	 */
	public File generateFile(){
		File parentFile = this.getParentFile();
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		if(workbook != null){
			OutputStream fileOut = null;
			try {
				fileOut = new FileOutputStream(this);
				workbook.write(fileOut);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this;
	}
}
