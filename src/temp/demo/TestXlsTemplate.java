package temp.demo;

import io.github.benas.jpopulator.api.Populator;
import io.github.benas.jpopulator.impl.PopulatorBuilder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

import test.bean.People;
/**
 * 
 * 创建时间:2016-10-10上午9:22:24<br/>
 * 创建者:sanri<br/>
 * 功能:excel 模板生成,借助包 jxls;
 * 依赖于 commons-jexl ,commons-digester,commons-lang3<br/>
 */
public class TestXlsTemplate {
	public static void main(String[] args) throws ParsePropertyException, InvalidFormatException, IOException {
		String tplFile = "D:/testjxls/test.xlsx";
		String destFile = "d:/testjxls/generate.xlsx";
		
		XLSTransformer formatter = new XLSTransformer();
		
		Map<String,Object> beanParams = new HashMap<String, Object>();
		
		Populator populator = new PopulatorBuilder().build();
		List<People> populateBeans = populator.populateBeans(People.class, 10);
		
		InputStream tplFileInputStream= new FileInputStream(tplFile);
		OutputStream destFileOutputStream = new FileOutputStream(destFile);
		
		beanParams.put("students", populateBeans);
		Workbook transformXLS = formatter.transformXLS(tplFileInputStream, beanParams);
		transformXLS.write(destFileOutputStream);
		
		tplFileInputStream.close();
		destFileOutputStream.close();
	}
}
