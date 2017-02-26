
package com.sanri.game.jframe.taiji;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;


public class TaiJiProgram extends JFrame{
	
	//定义组件
	MyPanel mp = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TaiJiProgram tjp = new TaiJiProgram();
	}	

	//构造函数
	public TaiJiProgram(){

		mp = new MyPanel();
		Thread t = new Thread(mp);
		t.start();
		
		//添加组件
		this.add(mp);
		
		//设置
		this.addKeyListener(mp);
		this.setIconImage((new ImageIcon("src\\0003.jpg")).getImage());
		this.setSize(640,480);
		this.setLocation(200, 200);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		
		//显示
		this.setVisible(true);
	}

}

class MyPanel extends JPanel implements Runnable,KeyListener{
	
	TaiJi tj = null;
	
	public void paint(Graphics g){
		super.paint(g);

		g.translate(320, 240);
		//System.out.println(tj.getAngle());
		this.drawTaiJi(tj.getX(), tj.getY(), tj.getD(), tj.getAngle(), tj.getMd(), g);
	}
	
	public MyPanel(){
		tj = new TaiJi(-50, -50, 100, 0);
		tj.setMoveSpeed(2);
		tj.setCircleSpeed(2);
		Thread t = new Thread(tj);
		t.start();
	}
	
	public void drawTaiJi(int x,int y,int d,int angle,int md,Graphics g){//md为小圆直径
		
		//算出圆心坐标
		int a=x+d/2;
		int b=y+d/2;
		
		g.setColor(Color.black);
		g.fillArc(x, y, d, d, 90+angle, 180);
		
		g.setColor(Color.white);
		g.fillArc(x, y, d, d, -90+angle, 180);
		
		g.setColor(Color.white);
		int ix,iy;
		ix=(int)(a-(d/4)*Math.sin(angle*(Math.PI/180))-d/4);
		iy=(int)(b-(d/4)*Math.cos(angle*(Math.PI/180))-d/4);
		g.fillArc(ix, iy, d/2, d/2, 90+angle, 180);
		
		g.setColor(Color.black);
		int jx,jy;
		jx=(int)(a+(d/4)*Math.sin(angle*(Math.PI/180))-d/4);
		jy=(int)(b+(d/4)*Math.cos(angle*(Math.PI/180))-d/4);
		g.fillArc(jx, jy, d/2, d/2, -90+angle, 180);
		
		g.setColor(Color.black);
		int mx,my;
		mx=(int)(a-(d/4)*Math.sin(angle*(Math.PI/180))-md/2);
		my=(int)(b-(d/4)*Math.cos(angle*(Math.PI/180))-md/2);
		g.fillOval(mx, my, md, md);
		
		g.setColor(Color.white);
		int nx,ny;
		nx=(int)(a+(d/4)*Math.sin(angle*(Math.PI/180))-md/2);
		ny=(int)(b+(d/4)*Math.cos(angle*(Math.PI/180))-md/2);
		g.fillOval(nx, ny, md, md);
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
			this.repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()){
		case KeyEvent.VK_W:
			tj.moveUp();
			break;
		case KeyEvent.VK_S:
			tj.moveDown();
			break;
		case KeyEvent.VK_A:
			tj.moveLeft();
			break;
		case KeyEvent.VK_D:
			tj.moveRight();
			break;
		case KeyEvent.VK_K:
			tj.changeBig();
			break;
		case KeyEvent.VK_I:
			tj.changeSmall();
			break;
		case KeyEvent.VK_J:
			tj.upCircleSpeed();
			break;
		case KeyEvent.VK_U:
			tj.downCircleSpeed();
			break;
		case KeyEvent.VK_SPACE:
			tj.setCircleSpeed(0);
			break;
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


