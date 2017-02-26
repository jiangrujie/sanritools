package test.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class BufferTest {
    static Charset charset = Charset.forName("utf-8");
    public static void main(String[] args) {
        try {
            FileInputStream in = new FileInputStream(new File("d:/b.txt"));
            FileChannel cchChannel = in.getChannel();
            
            ByteBuffer buff = ByteBuffer.allocate(1024);
            String content = "";
            while(cchChannel.read(buff) > 0)
            {
                buff.flip();
                content = charset.decode(buff).toString();
                if("Intel".equals(content)){
                    
                }
            }
            System.out.println(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
