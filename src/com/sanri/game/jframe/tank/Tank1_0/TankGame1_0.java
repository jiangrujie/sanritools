/**
 * 坦克大战游戏2.0
 * 作者：三日
 */

package com.sanri.game.jframe.tank.Tank1_0;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class TankGame1_0 extends JFrame{
	
	MyPanel mp = null;
	public static void main(String[] args) {
		TankGame1_0 tg = new TankGame1_0();
	}
	
	//构造函数
	public TankGame1_0(){
		mp = new MyPanel();
		
		//添加组件
		this.add(mp);
		
		//设置
		this.addKeyListener(mp);//注册监听,让mp来监听
		this.setSize(640,480);
		this.setLocation(100, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//显示
		this.setVisible(true);
	}

}

//我的画图面板
class MyPanel extends JPanel implements KeyListener{
	
	//定义一个我的坦克
	MyTank hero = null;
	
	//定义敌人的坦克组
	Vector<EnemyTank> ets = new Vector<EnemyTank>();	
	
	//定义敌人的坦克数量
	private int enemySize = 3;
	
	//构造函数
	public MyPanel(){
		
		hero = new MyTank(320,240);
		
		for(int i = 0;i<enemySize;i++){
			EnemyTank et = new EnemyTank((i+1)*50, 0);
			et.setDirect(1);
			ets.add(et);
		}
	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0,0, 640, 480);
		
		//画我的坦克
		this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
		
		//画敌人的坦克 
		for(int i=0;i<ets.size();i++){
			EnemyTank et = ets.get(i);
			this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 1);
		}
	}
	
	//画出坦克
	public void drawTank(int x,int y,Graphics g,int direct,int type){
		
		//判断坦克的类型
		switch(type){
		case 0:
			g.setColor(Color.cyan);
			break;
		case 1:
			g.setColor(Color.GREEN);
			break;
		}
		
		//判断方向
		switch(direct){
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
			//向上
			hero.setDirect(0);
			hero.moveUp();
			break;
		case KeyEvent.VK_DOWN:
			//向下
			hero.setDirect(1);
			hero.moveDown();
			break;
		case KeyEvent.VK_LEFT:
			//向左
			hero.setDirect(2);
			hero.moveLeft();
			break;
		case KeyEvent.VK_RIGHT:
			//向右
			hero.setDirect(3);
			hero.moveRight();
			break;
		}
		//重画
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
}

