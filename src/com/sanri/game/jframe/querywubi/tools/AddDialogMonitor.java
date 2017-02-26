package com.sanri.game.jframe.querywubi.tools;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.sanri.game.jframe.querywubi.model.CodeModel;
import com.sanri.game.jframe.querywubi.view.AddCodeDialog;

public class AddDialogMonitor implements KeyListener {

	AddCodeDialog acd;
	
	public AddDialogMonitor(AddCodeDialog acd){
		this.acd=acd;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			String codeNo=this.acd.getJtfCodeNo().getText().trim();
			String codepingying=this.acd.getJtfCodepingying().getText().trim();
			String codeCharacter=this.acd.getJtfCodeCharacter().getText().trim();
			String codewubi=this.acd.getJtfCodewubi().getText().trim();
			String codeDetach=this.acd.getJtfCodeDetach().getText().trim();
			String codeRemark=this.acd.getJtfCodeRemark().getText().trim();
			
			String s=codeNo+"@@"+codepingying+"@@"+codeCharacter+"@@"+codewubi+"@@"+codeDetach+"@@"+codeRemark;
			
			CodeModel cm=new CodeModel();
			if(cm.addCode(s)){
				System.out.println("添加成功");
			}else {
				
			}
			this.acd.dispose();
		}else if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
			this.acd.dispose();
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

}
