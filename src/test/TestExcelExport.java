package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

import test.bean.Data;
import test.bean.NoFatherBean;

public class TestExcelExport {
	public static void main(String[] args) {
		List<NoFatherBean> list = Data.loadNormalData();
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		CellStyle cellStyle = workbook.createCellStyle();
		
		/******************************设置背景色************************/
		cellStyle.setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		cellStyle.setFillBackgroundColor(HSSFColor.CORNFLOWER_BLUE.index);
		/******************************设置居中***************************/
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);				//设置水平居中
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);	//设置垂直居中
		/*****************************设置自动换行***********************/
		cellStyle.setWrapText(true);									//设置自动换行
		/****************************设置边框***************************/
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);			//设置 下 细
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);				//设置 左
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);			//设置 右
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);				//设置 上
//		cellStyle.setTopBorderColor(HSSFColor.BLUE);				//设置 上边框 颜色
		/***************************设置字体*****************************/
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short)12);
		cellStyle.setFont(font);
		
		HSSFRow row = null;
		HSSFCell cell_0 = null;HSSFCell cell_1 = null;HSSFCell cell_2 = null;HSSFCell cell_3 = null;
		HSSFCell cell_4 = null;HSSFCell cell_5 = null;HSSFCell cell_6 = null;HSSFCell cell_7 = null;

		row = sheet.createRow(0);
		
		cell_0 = row.createCell(0);
		cell_0.setCellStyle(cellStyle);
		cell_0.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell_0.setCellValue("姓名");
		
		cell_1 = row.createCell(1);
		cell_1.setCellStyle(cellStyle);
		cell_1.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell_1.setCellValue("性别");
		
		cell_2 = row.createCell(2);
		cell_2.setCellStyle(cellStyle);
		cell_2.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell_2.setCellValue("年龄");
		
		cell_3 = row.createCell(3);
		cell_3.setCellStyle(cellStyle);
		cell_3.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell_3.setCellValue("生日");

		
		cell_4 = row.createCell(4);
		cell_4.setCellStyle(cellStyle);
		cell_4.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell_4.setCellValue("爱好");
		
		cell_5 = row.createCell(5);
		cell_5.setCellStyle(cellStyle);
		cell_5.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell_5.setCellValue("所需商品");
		
		cell_6 = row.createCell(6);
		cell_6.setCellStyle(cellStyle);
		cell_6.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell_6.setCellValue("商品价格");
		
		cell_7 = row.createCell(7);
		cell_7.setCellStyle(cellStyle);
		cell_7.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell_7.setCellValue("是否工作");
		
		for(int i=0;i<list.size();i++){
			NoFatherBean bean = list.get(i);
			row = sheet.createRow(i+1);
			cell_0 = row.createCell(0);
			cell_0.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell_0.setCellValue(bean.getName());
			cell_1 = row.createCell(1);
			cell_1.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell_1.setCellValue(bean.getSex());
			cell_2 = row.createCell(2);
			cell_2.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell_2.setCellValue(bean.getAge());
			cell_3 = row.createCell(3);
			cell_3.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell_3.setCellValue(bean.getBirthday());			//日期也是数字类型
			cell_4 = row.createCell(4);
			cell_4.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell_4.setCellValue(bean.getFavorite());
			cell_5 = row.createCell(5);
			cell_5.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell_5.setCellValue(bean.getGoods());
			cell_6 = row.createCell(6);
			cell_6.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell_6.setCellValue(bean.getPrice());
			cell_7 = row.createCell(7);
			cell_7.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
			cell_7.setCellValue(bean.isWork());
			
		}
		/**************************设置列宽*****************************/
		for(int i=0;i<8;i++){
			row = sheet.getRow(0);
			HSSFCell cell = row.getCell(i);
			int len = cell.getRichStringCellValue().getString().length();
			sheet.setColumnWidth(i,(len+2)*2*256 );
		}
		try {
			workbook.write(new FileOutputStream(new File("d:/test.xls")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
