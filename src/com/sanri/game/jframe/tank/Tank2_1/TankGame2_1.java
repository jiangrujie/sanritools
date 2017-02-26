package com.sanri.game.jframe.tank.Tank2_1;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class TankGame2_1 extends JFrame{
	
	//定义一个MyPanel组件
	MyPanel mp = null;
	
	public static void main(String[] args) {
		TankGame2_1 tg = new TankGame2_1();
	}
	
	//构造函数
	public TankGame2_1(){
		
		//创建一个组件
		mp = new MyPanel();
		
		Thread t = new Thread(mp);
		t.start();
		
		//添加组件
		this.add(mp);
		
		//注册监听
		this.addKeyListener(mp);
		
		//设置
		this.setTitle("三日作品:[坦克大战]");
		this.setIconImage((new ImageIcon("images\\my.jpg")).getImage());
		this.setSize(640,480);
		this.setLocation(200, 200);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		
		//显示
		this.setVisible(true);
	}
}

//定义我的画图面板
class MyPanel extends JPanel implements KeyListener,Runnable{
	
	private int enemySize=3;
	Vector<EnemyTank> ens = new Vector<EnemyTank>();
	MyTank hero = null;
	
	//构造函数
	public MyPanel(){
		
		//构建一个我的坦克
		hero = new MyTank(320, 240);
		
		//构建敌人坦克
		for(int i=0;i<enemySize;i++){
			EnemyTank et = new EnemyTank((i+1)*100, 0);
			ens.add(et);
		}
	}
	
	//重写paint方法
	public void paint(Graphics g){
		super.paint(g);
		
		//设置背景
		g.setColor(Color.gray);
		g.fillRect(0, 0, 640, 480);
		
		//画出子弹
		for(int i=0;i<this.hero.ss.size();i++){
			Shot myShot = this.hero.ss.get(i);
			if(myShot!=null && myShot.getIsLive()==1){
				g.setColor(Color.GREEN);
				g.draw3DRect(myShot.getX(), myShot.getY(), 1, 1, false);
			}
			if(myShot.getIsLive()==0){
				this.hero.ss.remove(myShot);
			}
		}
		
		//画出我的坦克
		this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), hero.getColor());
		
		//画敌人坦克
		for(int i=0;i<ens.size();i++){
			EnemyTank temp = ens.get(i);
			if(temp.getIsLive()==1){
				this.drawTank(temp.getX(), temp.getY(), g, 1, 1);
			}
		}
	}
	
	//坦克何时消失
	public void hitTank(Shot s,EnemyTank et){
		switch(et.getDirect()){
		case 0:
		case 1:
			if(s.getX()>et.getX() && s.getX()<et.getX()+20 && s.getY()>et.getY() && s.getY()<et.getY()+30){
				//子弹死亡
				s.setIsLive(0);
				//坦克死亡
				et.setIsLive(0);
			}
			break;
		case 2:
		case 3:
			if(s.getX()>et.getX() && s.getX()<et.getX()+30 && s.getY()>et.getY() && s.getY()<et.getY()+20){
				//子弹死亡
				s.setIsLive(0);
				//坦克死亡
				et.setIsLive(0);
			}
			break;
		}
	}
	
	//画一个坦克
	public void drawTank(int x,int y,Graphics g,int direct,int color){
		
		//判断坦克的颜色(颜色为0是自己的坦克,其它颜色为敌人的坦克)
		switch(color){
		case 0:
			g.setColor(Color.pink);
			break;
		case 1:
			g.setColor(Color.yellow);
			break;
		}
		
		switch(direct){
		/**
		 * 0:表示方向向上
		 * 1:表示方向向下
		 * 2:表示方向向左
		 * 3:表示方向向右
		 */
		//判断方向
		//默认炮火向上
		case 0:
			//画边上矩形
			g.fillRect(x,y, 5, 30);
			
			//画中间矩形
			g.fillRect(x+5,y+5, 10, 20);

			//画边上的矩形
			g.fillRect(x+15, y, 5, 30);
				
			//画直线
			g.drawLine(x+10, y+15, x+10, y-5);

			//画圆
			g.fillOval(x+5, y+10, 10, 10);
			break;
		//炮火向下
		case 1:
			//画边上矩形
			g.fillRect(x,y, 5, 30);
			
			//画中间矩形
			g.fillRect(x+5,y+5, 10, 20);

			//画边上的矩形
			g.fillRect(x+15, y, 5, 30);
				
			//画直线
			g.drawLine(x+10, y+15, x+10, y+35);

			//画圆
			g.fillOval(x+5, y+10, 10, 10);
			break;
		//炮火向左
		case 2:
			//画边上矩形
			g.fillRect(x-5,y+5, 30, 5);
			
			//画中间矩形
			g.fillRect(x,y+10, 20, 10);

			//画边上的矩形
			g.fillRect(x-5, y+20, 30, 5);
				
			//画直线
			g.drawLine(x+10, y+15, x-10, y+15);

			//画圆
			g.fillOval(x+5, y+10, 10, 10);
			break;
			//炮火向右
		case 3:
			//画边上矩形
			g.fillRect(x-5,y+5, 30, 5);
			
			//画中间矩形
			g.fillRect(x,y+10, 20, 10);

			//画边上的矩形
			g.fillRect(x-5, y+20, 30, 5);
				
			//画直线
			g.drawLine(x+10, y+15, x+30, y+15);

			//画圆
			g.fillOval(x+5, y+10, 10, 10);
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			hero.setDirect(0);
			hero.moveUp();
			break;
		case KeyEvent.VK_DOWN:
			hero.setDirect(1);
			hero.moveDown();
			break;
		case KeyEvent.VK_LEFT:
			hero.setDirect(2);
			hero.moveLeft();
			break;
		case KeyEvent.VK_RIGHT:
			hero.setDirect(3);
			hero.moveRight();
			break;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_SPACE){
			System.out.println("集合大小为:"+this.hero.ss.size());
			if(this.hero.ss.size()<5){
				hero.shotEnemy();
			}
		}
		
		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for(int i=0;i<hero.ss.size();i++){
				Shot myShot = hero.ss.get(i);
				if(myShot.getIsLive()==1){
					for(int j=0;j<ens.size();j++){
						EnemyTank et = ens.get(j);
						if(et.getIsLive()==1){
							this.hitTank(myShot, et);
						}
					}
				}
			}
			
			this.repaint();
		}
		
	}
	
}

