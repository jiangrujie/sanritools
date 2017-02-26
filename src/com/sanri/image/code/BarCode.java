package com.sanri.image.code;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jbarcode.JBarcode;
import org.jbarcode.JBarcodeFactory;
import org.jbarcode.encode.InvalidAtributeException;
import org.jbarcode.util.ImageUtil;

/**
 * 
 * 创建时间:2016-10-2下午12:20:26<br/>
 * 创建者:sanri<br/>
 * 功能:条形码生成工具<br/>
 */
public class BarCode {
	public enum Type{
		CODABAR,CODE11,UOM_PIXEL,CODE2OF5,CODE39,CODE39EX,CODE93,EAN8,EAN8_2,EAN8_5,
		EAN13,EAN13_2,EAN13_5,ISBN,ISBN_5,ISSN,ISSN_2,ITF14,INTERLEAVED25,IDENTCODE,
		LEITCODE,MSI,ONECODE,PLANET,POSTNET,RM4SCC,UPCA,UPCA_2,UPCA_5,UPCE,UPCE_2,
		UPCE_5,CODE128,EAN128
	}
	
	public static void main(String[] args) throws InvalidAtributeException, FileNotFoundException, IOException {
		JBarcodeFactory factory = JBarcodeFactory.getInstance();
		JBarcode createCodabar = factory.createCode39();
		
//		JBarcode createCodabar = new JBarcode(Code39Encoder.getInstance(), WideRatioCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
		
		//
		createCodabar.setXDimension(1);//这里可以控制宽度 设置成 1 为 601 像素,不知道这个参数干嘛的
		createCodabar.setBarHeight(80);//条形码高度 80->302 像素; 这里是 80 毫米,96 dpi 分辨率,算出 302 像素 
		createCodabar.setWideRatio(30); //每个条码之间的间隙,2 毫米, 96 dpi 下是 7 像素
		
		//是否校验 
		createCodabar.setCheckDigit(false);
		//是否显示文件
		createCodabar.setShowText(true);
		//是否显示校验位
		createCodabar.setShowCheckDigit(false);
//		createCodabar.setEncoder(Code39Encoder.getInstance());
//		createCodabar.setPainter(WideRatioCodedPainter.getInstance());
//		createCodabar.setTextPainter(BaseLineTextPainter.getInstance());
		BufferedImage createBarcode = createCodabar.createBarcode("788515004012");
		
		FileOutputStream fileOutputStream = new FileOutputStream("d:/generate2.png");
//		ImageIO.write(createBarcode, "png", fileOutputStream);
		ImageUtil.encodeAndWrite(createBarcode, "png", fileOutputStream,96,96);
		fileOutputStream.flush();
		fileOutputStream.close();
	}
}
