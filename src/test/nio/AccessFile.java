package test.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class AccessFile {
    static Charset charset = Charset.forName("utf-8");
    public static void main(String[] args) {
        try {
            RandomAccessFile randomFile = new RandomAccessFile(new File("d:/b.txt"), "rw");
            FileChannel channel = randomFile.getChannel();
            ByteBuffer buff = ByteBuffer.allocate(1024);
            String content = "";
            while(channel.read(buff) > 0)
            {
                buff.flip();
                content = charset.decode(buff).toString();
                if("Intel".equals(content)){
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
