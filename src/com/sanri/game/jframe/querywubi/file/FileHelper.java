package com.sanri.game.jframe.querywubi.file;

import java.util.Vector;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FileHelper {
	
	File f;
	
	public FileHelper(){
		f=new File("c:\\windows\\system32\\difficultSplit.sanri");
		if(!f.exists()){
			try {
				//新建文件
				f.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//查询编码
	public Vector queryCode(String pingyingCode){
		Vector commLineData=new Vector();		
		BufferedReader br=null;
		try {
			FileReader fr=new FileReader(f);
			br=new BufferedReader(fr);
			String s="";
			
			if(pingyingCode.equals("")){
				while((s=br.readLine())!=null){
					commLineData.add(s);
				}
			}else{
				while((s=br.readLine())!=null){
					String[]temp=s.split("@@");
					if(temp[1].equals(pingyingCode)){
						commLineData.add(s);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null!=br){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		}
		
		return commLineData;
		
	}
	
	//添加一个编码
	public boolean addCode(String s){
		boolean b=true;
		BufferedWriter bw=null;
		try {
			FileWriter fw=new FileWriter(f, true);
			bw=new BufferedWriter(fw);
			bw.write(s+"\n");
		} catch (Exception e) {
			b=false;
			e.printStackTrace();
			// TODO: handle exception
		}finally{
			if(null!=bw){
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return b;
	}
	
	//删除一个编码
	public boolean delCode(String sNo){
		boolean b=true;
		
		BufferedReader br=null;
		BufferedWriter bw=null;
		
		try {
			/*先读取所有的记录,过滤掉编号为sNo的记录*/
			FileReader fr=new FileReader(f);
			br=new BufferedReader(fr);
			String s="";
			Vector allData=new Vector();
			while((s=br.readLine())!=null){
				String []temp=s.split("@@");
				if(!(temp[0].equals(sNo))){
					allData.add(s);
				}
			}
			
			/*从向量中取出数据,再写回去*/
			FileWriter fw=new FileWriter(f);
			bw=new BufferedWriter(fw);
			for(int i=0;i<allData.size();i++){
				bw.write(((String)allData.get(i))+"\n");
			}
			
		} catch (Exception e) {
			b=false;
			e.printStackTrace();
			// TODO: handle exception
		}finally{
			try {
				if(null!=br){
					br.close();
				}
				if(null!=bw){
					bw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
		}
		
		return b;
	}
	
	/*更改一个编码*/
	public boolean updateCode(String adds,String delNo){
		boolean b=true;
	
		this.addCode(adds);
		this.delCode(delNo);
	
		return b;
	}
	
}
