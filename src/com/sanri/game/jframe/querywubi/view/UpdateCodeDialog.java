package com.sanri.game.jframe.querywubi.view;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sanri.game.jframe.querywubi.model.CodeModel;

public class UpdateCodeDialog extends JDialog implements ActionListener{

	JPanel jpLabel=null;
	JPanel jpTextField=null;
	JPanel totalPanel=null;
	
	JLabel jlCodeNo=null,jlCodepingying=null,jlCodeCharacter=null,jlCodewubi=null,jlCodeDetach=null,jlCodeRemark=null;
	JTextField jtfCodeNo=null,jtfCodepingying=null,jtfCodeCharacter=null,jtfCodewubi=null,jtfCodeDetach=null,jtfCodeRemark=null;
	
	JButton jbOk=null;
	JButton jbCancel=null;
	
	int selectRowNum;
	CodeModel cm;
	
	public UpdateCodeDialog(Frame owner,String title,boolean model,CodeModel cm,int selectRowNum){
		
		super(owner,title,model); 
		this.cm=cm;
		this.selectRowNum=selectRowNum;
		//标签
		this.jlCodeNo=new JLabel("序    号",JLabel.CENTER);
		this.jlCodepingying=new JLabel("拼音编码",JLabel.CENTER);
		this.jlCodeCharacter=new JLabel("汉    字",JLabel.CENTER);
		this.jlCodewubi=new JLabel("五笔编码",JLabel.CENTER);
		this.jlCodeDetach=new JLabel("拆字法",JLabel.CENTER);
		this.jlCodeRemark=new JLabel("备注",JLabel.CENTER);
		
		//文本域
		this.jtfCodeNo=new JTextField(10);
		this.jtfCodepingying=new JTextField(10);
		this.jtfCodeCharacter=new JTextField(10);
		this.jtfCodewubi=new JTextField(10);
		this.jtfCodeDetach=new JTextField(10);
		this.jtfCodeRemark=new JTextField(10);
		
		this.jtfCodeNo.setText((String)cm.getValueAt(selectRowNum, 0));
		this.jtfCodeNo.setEditable(false);
		this.jtfCodepingying.setText((String)cm.getValueAt(selectRowNum, 1));
		this.jtfCodeCharacter.setText((String)cm.getValueAt(selectRowNum, 2));
		this.jtfCodewubi.setText((String)cm.getValueAt(selectRowNum, 3));
		this.jtfCodeDetach.setText((String)cm.getValueAt(selectRowNum, 4));
		this.jtfCodeRemark.setText((String)cm.getValueAt(selectRowNum, 5));
		
		//按扭区
		this.jbOk=new JButton("确认修改");
		this.jbCancel=new JButton("取消操作");
		
		this.jbOk.addActionListener(this);
		this.jbCancel.addActionListener(this);
		
		this.jbOk.setActionCommand("ok");
		this.jbCancel.setActionCommand("cancel");
		
		//面板区
		this.jpLabel=new JPanel();
		this.jpTextField=new JPanel();
		this.totalPanel=new JPanel();
		
		//设置布局
		this.jpLabel.setLayout(new GridLayout(7,1));
		this.jpTextField.setLayout(new GridLayout(7,1));
		this.totalPanel.setLayout(new GridLayout(1,2));
		
		//添加
		this.jpLabel.add(this.jlCodeNo);
		this.jpLabel.add(this.jlCodepingying);
		this.jpLabel.add(this.jlCodeCharacter);
		this.jpLabel.add(this.jlCodewubi);
		this.jpLabel.add(this.jlCodeDetach);
		this.jpLabel.add(this.jlCodeRemark);
		this.jpLabel.add(this.jbOk);
		
		this.jpTextField.add(this.jtfCodeNo);
		this.jpTextField.add(this.jtfCodepingying);
		this.jpTextField.add(this.jtfCodeCharacter);
		this.jpTextField.add(this.jtfCodewubi);
		this.jpTextField.add(this.jtfCodeDetach);
		this.jpTextField.add(this.jtfCodeRemark);
		this.jpTextField.add(this.jbCancel);
		
		this.totalPanel.add(this.jpLabel);
		this.totalPanel.add(this.jpTextField);
		
		this.add(this.totalPanel);
		
		//设置
		this.setSize(250,300);
		this.setLocation(300,300);
		
		this.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("ok")){
			String codepingying=this.jtfCodepingying.getText().trim();
			String codeCharacter=this.jtfCodeCharacter.getText().trim();
			String codewubi=this.jtfCodewubi.getText().trim();
			String codeDetach=this.jtfCodeDetach.getText().trim();
			
			/*再次获取系统时间*/
			Date d=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("ssmmkkddMMyyyy");
			String str=sdf.format(d);
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
			String str2=sdf2.format(d);
			
			String info=str+"@@"+codepingying+"@@"+codeCharacter+"@@"+codewubi+"@@"+codeDetach+"@@"+str2;
			
			if(cm.updateCode(this.cm,info, this.selectRowNum)){
				System.out.println("添加成功");
			}else {
				JOptionPane.showMessageDialog(this, "修改失败");
			}
			
			this.dispose();
		}else if(e.getActionCommand().equals("cancel")){
			this.dispose();
		}
	}

}
