package com.sanri.game.jframe.querywubi.tools;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.sanri.game.jframe.querywubi.model.CodeModel;
import com.sanri.game.jframe.querywubi.view.AddCodeDialog;
import com.sanri.game.jframe.querywubi.view.MainFrame;

public class MainFrameMonitor implements KeyListener{
	CodeModel cm;
	MainFrame mf;

	public MainFrameMonitor(MainFrame mf){
		this.mf=mf;
		this.cm=mf.getCm();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
//			System.out.println("我按了回车键");
			String pingyingCode=mf.getInputCode().getText().trim();
			mf.getInputCode().setText("");
			cm=new CodeModel();
			cm.queryCode(pingyingCode);
			mf.getShowCode().setModel(cm);
		}else if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
//			System.out.println("我按了ESC键");
			cm=new CodeModel();
			cm.queryCode("");
			mf.getShowCode().setModel(cm);
		}else if(e.isControlDown() && (e.getKeyCode()==KeyEvent.VK_A)){
//			System.out.println("我按下了ctrl键和A键");
			new AddCodeDialog(mf,"添加编码",true);
			cm=new CodeModel();
			cm.queryCode("");
			mf.getShowCode().setModel(cm);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new MainFrame();
	}
}
