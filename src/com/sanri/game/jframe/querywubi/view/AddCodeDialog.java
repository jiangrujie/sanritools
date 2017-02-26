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
import com.sanri.game.jframe.querywubi.tools.AddDialogMonitor;

public class AddCodeDialog extends JDialog implements ActionListener{
	private JPanel jpLabel=null;
	private JPanel jpTextField=null;
	private JPanel totalPanel=null;
	
	private JLabel jlCodeNo=null,jlCodepingying=null,jlCodeCharacter=null,jlCodewubi=null,jlCodeDetach=null,jlCodeRemark=null;
	private JTextField jtfCodeNo=null,jtfCodepingying=null,jtfCodeCharacter=null,jtfCodewubi=null,jtfCodeDetach=null,jtfCodeRemark=null;
	
	private JButton jbOk=null;
	private JButton jbCancel=null;
	
	public AddCodeDialog(Frame owner,String title,boolean model){
		super(owner,title,model); 
		//标签
		this.jlCodeNo=new JLabel("序    号", JLabel.CENTER);
		this.jlCodepingying=new JLabel("拼音编码",JLabel.CENTER);
		this.jlCodeCharacter=new JLabel("汉    字",JLabel.CENTER);
		this.jlCodewubi=new JLabel("五笔编码",JLabel.CENTER);
		this.jlCodeDetach=new JLabel("拆字法",JLabel.CENTER);
		this.jlCodeRemark=new JLabel("时间",JLabel.CENTER);
		
		//文本域
		Date d=new Date();
		this.jtfCodeNo=new JTextField(10);
		SimpleDateFormat sdf2=new SimpleDateFormat("ssmmkkddMMyyyy");
		String str2=sdf2.format(d);
		this.jtfCodeNo.setText(str2);
		this.jtfCodeNo.setEditable(false);
		
		this.jtfCodepingying=new JTextField(10);
		this.jtfCodeCharacter=new JTextField(10);
		this.jtfCodewubi=new JTextField(10);
		this.jtfCodeDetach=new JTextField(10);
		this.jtfCodeRemark=new JTextField(10);
		
		this.jtfCodepingying.addKeyListener((new AddDialogMonitor(this)));
		this.jtfCodeCharacter.addKeyListener((new AddDialogMonitor(this)));
		this.jtfCodewubi.addKeyListener((new AddDialogMonitor(this)));
		this.jtfCodeDetach.addKeyListener((new AddDialogMonitor(this)));
		this.jtfCodeRemark.addKeyListener((new AddDialogMonitor(this)));
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String str=sdf.format(d);
		this.jtfCodeRemark.setText(str);
		
		//按扭区
		this.jbOk=new JButton("确认添加");
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
			String codeNo=this.jtfCodeNo.getText().trim();
			String codepingying=this.jtfCodepingying.getText().trim();
			String codeCharacter=this.jtfCodeCharacter.getText().trim();
			String codewubi=this.jtfCodewubi.getText().trim();
			String codeDetach=this.jtfCodeDetach.getText().trim();
			String codeRemark=this.jtfCodeRemark.getText().trim();
			
			String s=codeNo+"@@"+codepingying+"@@"+codeCharacter+"@@"+codewubi+"@@"+codeDetach+"@@"+codeRemark;
			
			CodeModel cm=new CodeModel();
			if(cm.addCode(s)){
				System.out.println("添加成功");
			}else {
				JOptionPane.showMessageDialog(this, "添加失败");
			}
			this.dispose();
		}else if(e.getActionCommand().equals("cancel")){
			this.dispose();
		}
	}

	public JPanel getJpLabel() {
		return jpLabel;
	}

	public void setJpLabel(JPanel jpLabel) {
		this.jpLabel = jpLabel;
	}

	public JPanel getJpTextField() {
		return jpTextField;
	}

	public void setJpTextField(JPanel jpTextField) {
		this.jpTextField = jpTextField;
	}

	public JPanel getTotalPanel() {
		return totalPanel;
	}

	public void setTotalPanel(JPanel totalPanel) {
		this.totalPanel = totalPanel;
	}

	public JLabel getJlCodeNo() {
		return jlCodeNo;
	}

	public void setJlCodeNo(JLabel jlCodeNo) {
		this.jlCodeNo = jlCodeNo;
	}

	public JLabel getJlCodepingying() {
		return jlCodepingying;
	}

	public void setJlCodepingying(JLabel jlCodepingying) {
		this.jlCodepingying = jlCodepingying;
	}

	public JLabel getJlCodeCharacter() {
		return jlCodeCharacter;
	}

	public void setJlCodeCharacter(JLabel jlCodeCharacter) {
		this.jlCodeCharacter = jlCodeCharacter;
	}

	public JLabel getJlCodewubi() {
		return jlCodewubi;
	}

	public void setJlCodewubi(JLabel jlCodewubi) {
		this.jlCodewubi = jlCodewubi;
	}

	public JLabel getJlCodeDetach() {
		return jlCodeDetach;
	}

	public void setJlCodeDetach(JLabel jlCodeDetach) {
		this.jlCodeDetach = jlCodeDetach;
	}

	public JLabel getJlCodeRemark() {
		return jlCodeRemark;
	}

	public void setJlCodeRemark(JLabel jlCodeRemark) {
		this.jlCodeRemark = jlCodeRemark;
	}

	public JTextField getJtfCodeNo() {
		return jtfCodeNo;
	}

	public void setJtfCodeNo(JTextField jtfCodeNo) {
		this.jtfCodeNo = jtfCodeNo;
	}

	public JTextField getJtfCodepingying() {
		return jtfCodepingying;
	}

	public void setJtfCodepingying(JTextField jtfCodepingying) {
		this.jtfCodepingying = jtfCodepingying;
	}

	public JTextField getJtfCodeCharacter() {
		return jtfCodeCharacter;
	}

	public void setJtfCodeCharacter(JTextField jtfCodeCharacter) {
		this.jtfCodeCharacter = jtfCodeCharacter;
	}

	public JTextField getJtfCodewubi() {
		return jtfCodewubi;
	}

	public void setJtfCodewubi(JTextField jtfCodewubi) {
		this.jtfCodewubi = jtfCodewubi;
	}

	public JTextField getJtfCodeDetach() {
		return jtfCodeDetach;
	}

	public void setJtfCodeDetach(JTextField jtfCodeDetach) {
		this.jtfCodeDetach = jtfCodeDetach;
	}

	public JTextField getJtfCodeRemark() {
		return jtfCodeRemark;
	}

	public void setJtfCodeRemark(JTextField jtfCodeRemark) {
		this.jtfCodeRemark = jtfCodeRemark;
	}

	public JButton getJbOk() {
		return jbOk;
	}

	public void setJbOk(JButton jbOk) {
		this.jbOk = jbOk;
	}

	public JButton getJbCancel() {
		return jbCancel;
	}

	public void setJbCancel(JButton jbCancel) {
		this.jbCancel = jbCancel;
	}
	
	
}
