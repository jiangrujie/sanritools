package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import test.bean.ExcelTestBean;

import com.sanri.file.xls.ExcelWriter;
import com.sanri.file.xls.SheetConfig;

public class TestXls {
	public static void main(String[] args) {
		List<ExcelTestBean> beans = new ArrayList<ExcelTestBean>();
		ExcelTestBean bean = new ExcelTestBean();
		bean.setAa((byte)12);
		bean.setGg('b');
		beans.add(bean);
		
		bean = new ExcelTestBean();
		bean.setAa((byte)21);
		bean.setBb((short)7);
		bean.setPeople("430124199309179596");
		bean.setName("sanri");
		beans.add(bean);
		
		bean = new ExcelTestBean();
		bean.setAa((byte)22);
		bean.setBb((short)4);
		bean.setDd(23233333333333l);
		bean.setHh(true);
		beans.add(bean);
		
		ExcelWriter excelWriter = new ExcelWriter("C:/Users/rjb-/Desktop/joinproject/test.xls",2003);
		SheetConfig sheetConfig = new SheetConfig("测试", beans);
		Map<String,String> chinese = new HashMap<String, String>();
		chinese.put("aa", "字节");
		chinese.put("bb", "短整型");
		chinese.put("cc", "整型");
		chinese.put("dd", "长整型");
		chinese.put("ee", "浮点型");
		chinese.put("ff", "double 型");
		chinese.put("gg", "字符型");
		chinese.put("hh", "布尔型");
		chinese.put("name", "名字");
		chinese.put("people", "人物");
		sheetConfig.setChinese(chinese);
		List<String> hiddens = new ArrayList<String>();
		hiddens.add("hh");
		sheetConfig.setHiddens(hiddens);
		Map<String,Integer> width = new HashMap<String, Integer>();
		width.put("dd", Math.round(20 * ExcelWriter.BASE_CHINESE));
		sheetConfig.setWidthConfig(width);
		Map<String,Integer> height = new HashMap<String, Integer>();
		height.put("1~3", Math.round(25 * ExcelWriter.BASE_HEIGHT_1_PX));
		sheetConfig.setHeightConfig(height);
//		CellStyle cellStyle = excelWriter.createCellStyle();
//		Font headFont = excelWriter.createFont();
//		headFont.setBoldweight((short)300);
//		headFont.setColor(HSSFColor.BLUE.index);
//		headFont.setFontHeightInPoints((short)12);
//		cellStyle.setFont(headFont);
//		/******************************设置背景色************************/
//		cellStyle.setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);
//		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
////		cellStyle.setFillBackgroundColor(HSSFColor.CORNFLOWER_BLUE.index);
//		/******************************设置居中***************************/
//		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);				//设置水平居中
//		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);	//设置垂直居中
//		/*****************************设置自动换行***********************/
//		cellStyle.setWrapText(true);									//设置自动换行
//		/****************************设置边框***************************/
//		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);			//设置 下 细
//		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);				//设置 左
//		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);			//设置 右
//		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);				//设置 上
		Map<String,CellStyle> styles = new HashMap<String, CellStyle>();
		
		CellStyle headStyle =  excelWriter.createCellStyle();
		Font headFont = excelWriter.createFont();
		headFont.setFontHeightInPoints((short)15);
		headFont.setFontName("华文新魏");
		headFont.setBoldweight((short)500);
		headStyle.setFont(headFont);
		headStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		styles.put("head", headStyle);
		
		CellStyle bodyStyle = excelWriter.createCellStyle();
		Font bodyFont = excelWriter.createFont();
		bodyFont.setFontHeightInPoints((short)12);
		bodyFont.setFontName("宋体");
		bodyFont.setBoldweight((short)400);
		bodyStyle.setFont(bodyFont);
		bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
		bodyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		bodyStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		bodyStyle.setFillForegroundColor(Color.BLUE.index);
		styles.put("body", bodyStyle);
		
		sheetConfig.setStyles(styles);
		excelWriter.sheet(sheetConfig);
		excelWriter.generateFile();
	}
}
