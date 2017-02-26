/**
 * 实现对编码的增删改查
 */
package com.sanri.game.jframe.querywubi.model;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.sanri.game.jframe.querywubi.file.FileHelper;

public class CodeModel extends AbstractTableModel{

	Vector rowData=new Vector();
	Vector columnName=new Vector();
	
	/*构造时把列名加上*/
	public CodeModel(){
		columnName.add("编号");
		columnName.add("拼音");
		columnName.add("汉字");
		columnName.add("五笔");
		columnName.add("拆  字  法");
		columnName.add("时间");
	}

	/*查询编码*/
	public void queryCode(String pingyingCode){
		FileHelper fh=new FileHelper();
		Vector commonLineData=fh.queryCode(pingyingCode);
		
		for(int i=0;i<commonLineData.size();i++){
			Vector col=new Vector();
			
			String s=(String)commonLineData.get(i);
			String []s2=s.split("@@");
			
			for(int j=0;j<s2.length;j++){
				col.add(s2[j]);
			}
			this.rowData.add(col);
		}
	}
	
	/*添加编码*/
	public boolean addCode(String s){
		FileHelper fh=new FileHelper();
		return fh.addCode(s);
	}
	
	/*删除编码*/
	public boolean delCode(CodeModel cm,int rowNum) {
		boolean b=true;
		FileHelper fh=new FileHelper();
		if(!fh.delCode((String)cm.getValueAt(rowNum,0))){
			b=false;
		}
		return b;
	}
	
	/*更改一个编码*/
	public boolean updateCode(CodeModel cm,String info,int rowNum){
		boolean b=true;
		FileHelper fh=new FileHelper();
		fh.updateCode(info, (String)cm.getValueAt(rowNum, 0));
		
		return b;
	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return this.columnName.size();
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return this.rowData.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return ((Vector)(this.rowData.get(rowIndex))).get(columnIndex);
	}

	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return (String)this.columnName.get(column);
	}


}
