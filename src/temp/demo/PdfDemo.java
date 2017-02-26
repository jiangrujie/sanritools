package temp.demo;

import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;



public class PdfDemo {
	public static void main(String[] args) {
		writerPDF();
	}
	
	public static void writerPDF(){
		Rectangle rectangle = new Rectangle(PageSize.A4);
		//设置背景色
		rectangle.setBackgroundColor(new BaseColor(0xdd,0xdd,0xdd));
		//设置 border 类型
		rectangle.setBorder(Rectangle.BOX);
		//设置 border 颜色
		rectangle.setBorderColor(BaseColor.RED);
		Document document =  new Document(rectangle);
		String fileName = null;
		try {
			fileName =  String.valueOf(System.currentTimeMillis())+".pdf";
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("d:/test/"+fileName,true));
			writer.setEncryption(null, null,PdfWriter.ALLOW_FILL_IN,PdfWriter.STANDARD_ENCRYPTION_128);
			writer.setViewerPreferences(PdfWriter.HideMenubar | PdfWriter.HideToolbar);
			writer.setViewerPreferences(PdfWriter.HideWindowUI);
			document.open();
			
			document.addTitle("title");
			document.addAuthor("sanri1993");
			document.addSubject("test");
			document.addKeywords("program");
			document.addCreator("creator sanri");
			document.addHeader("header", "header detail");
			document.addProducer();
			document.add(new Paragraph(new Chunk("..........ssssssssssssss")));
			
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("文件名:"+fileName);
	}
}
