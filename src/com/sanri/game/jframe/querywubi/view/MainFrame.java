package com.sanri.game.jframe.querywubi.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.sanri.game.jframe.querywubi.model.CodeModel;
import com.sanri.game.jframe.querywubi.tools.MainFrameMonitor;

public class MainFrame extends JFrame implements ActionListener {
	
	private JPanel mainPanel=null;
	private JPanel topPanel=null;
	private JPanel centerPanel=null;
	private JPanel downPanel=null;
	private JTable showCode=null;
	private JScrollPane jsp=null;
	private JLabel clew=null;
	private JTextField inputCode=null;
	
	private JButton queryCode=null;
	private JButton home=null;
	private JButton addCode=null;
	private JButton delCode=null;
	private JButton updateCode=null;
	
	private CodeModel cm=null;
	
	public MainFrame(){

		this.mainPanel=new JPanel();
		this.topPanel=new JPanel();
		this.downPanel=new JPanel();
		this.centerPanel=new JPanel();
		
		cm=new CodeModel();
		cm.queryCode("");
		this.showCode=new JTable(cm);

		this.jsp=new JScrollPane(this.showCode);
		this.clew=new JLabel("输入你要查询的拼音");
		this.inputCode=new JTextField(10);
		this.inputCode.addKeyListener((new MainFrameMonitor(this)));
		
		/*按扭区*/
		this.home=new JButton("返回");
		this.queryCode=new JButton("查询");
		this.addCode=new JButton("添加编码");
		this.delCode=new JButton("删除编码");
		this.updateCode=new JButton("修改编码");
		
		this.home.addActionListener(this);
		this.queryCode.addActionListener(this);
		this.addCode.addActionListener(this);
		this.delCode.addActionListener(this);
		this.updateCode.addActionListener(this);
		
		this.home.setActionCommand("home");
		this.queryCode.setActionCommand("query");
		this.addCode.setActionCommand("add");
		this.delCode.setActionCommand("del");
		this.updateCode.setActionCommand("update");
		
		/*添加*/
		this.topPanel.add(this.clew);
		this.topPanel.add(this.inputCode);
		this.topPanel.add(this.home);
		this.topPanel.add(this.queryCode);
		
		this.centerPanel.add(this.jsp);
		
		this.downPanel.add(this.addCode);
		this.downPanel.add(this.delCode);
		this.downPanel.add(this.updateCode);
		
		this.mainPanel.setLayout(new BorderLayout());
		this.mainPanel.add(this.topPanel,BorderLayout.NORTH);
		this.mainPanel.add(this.centerPanel,BorderLayout.CENTER);
		this.mainPanel.add(this.downPanel,BorderLayout.SOUTH);
		
		this.add(this.mainPanel);
		
		/*设置*/		
		this.setTitle("三日五笔编码查询");
		this.setSize(500,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("query")){
			String pingyingCode=this.inputCode.getText().trim();
			this.inputCode.setText("");
			cm=new CodeModel();
			cm.queryCode(pingyingCode);
			this.showCode.setModel(cm);
		}else if(e.getActionCommand().equals("home")){
			cm=new CodeModel();
			cm.queryCode("");
			this.showCode.setModel(cm);
		}else if(e.getActionCommand().equals("add")){
			new AddCodeDialog(this,"添加编码",true);
			cm=new CodeModel();
			cm.queryCode("");
			this.showCode.setModel(cm);
		}else if(e.getActionCommand().equals("del")){
			int rowNum=this.showCode.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "请选中一行再点这");
				return ;
			}else{
				cm.delCode(cm,rowNum);
				cm=new CodeModel();
				cm.queryCode("");
				this.showCode.setModel(cm);
			}
		}else if(e.getActionCommand().equals("update")){
			int rowNum=this.showCode.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "请选中一行再点这");
				return ;
			}else{
				new UpdateCodeDialog(this, "更改编码", true, cm, rowNum);
				cm=new CodeModel();
				cm.queryCode("");
				this.showCode.setModel(cm);
			}
		}
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public JPanel getTopPanel() {
		return topPanel;
	}

	public void setTopPanel(JPanel topPanel) {
		this.topPanel = topPanel;
	}

	public JPanel getCenterPanel() {
		return centerPanel;
	}

	public void setCenterPanel(JPanel centerPanel) {
		this.centerPanel = centerPanel;
	}

	public JPanel getDownPanel() {
		return downPanel;
	}

	public void setDownPanel(JPanel downPanel) {
		this.downPanel = downPanel;
	}

	public JTable getShowCode() {
		return showCode;
	}

	public void setShowCode(JTable showCode) {
		this.showCode = showCode;
	}

	public JScrollPane getJsp() {
		return jsp;
	}

	public void setJsp(JScrollPane jsp) {
		this.jsp = jsp;
	}

	public JLabel getClew() {
		return clew;
	}

	public void setClew(JLabel clew) {
		this.clew = clew;
	}

	public JTextField getInputCode() {
		return inputCode;
	}

	public void setInputCode(JTextField inputCode) {
		this.inputCode = inputCode;
	}

	public JButton getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(JButton queryCode) {
		this.queryCode = queryCode;
	}

	public JButton getHome() {
		return home;
	}

	public void setHome(JButton home) {
		this.home = home;
	}

	public JButton getAddCode() {
		return addCode;
	}

	public void setAddCode(JButton addCode) {
		this.addCode = addCode;
	}

	public JButton getDelCode() {
		return delCode;
	}

	public void setDelCode(JButton delCode) {
		this.delCode = delCode;
	}

	public JButton getUpdateCode() {
		return updateCode;
	}

	public void setUpdateCode(JButton updateCode) {
		this.updateCode = updateCode;
	}

	public CodeModel getCm() {
		return cm;
	}

	public void setCm(CodeModel cm) {
		this.cm = cm;
	}
}
