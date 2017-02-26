/**
 * 钟表程序
 */
package com.sanri.game.jframe;

import java.awt.*;
import java.util.Calendar;

import javax.swing.*;

public class ColockPro extends JFrame {
	
	MyPanel mp = null;
	
	public static void main(String[] args) {
		ColockPro cp = new ColockPro();

	}
	//构造函数
	public ColockPro(){
		
		mp = new MyPanel();
		Thread t = new Thread(mp);
		t.start();
		
		//添加组件
		this.add(mp);
		
		//设置
		this.setSize(640,480);
		this.setLocation(200, 200);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		
		//显示
		this.setVisible(true);
	}

}

class MyPanel extends JPanel implements Runnable{
	public void paint(Graphics g){
		super.paint(g);
		g.translate(320, 240);	
		
//		g.drawRect(-100, -100, 200, 200);
		g.drawOval(-120, -120, 240, 240);
		this.drawColockFace(-100, -100, 200, g);
		this.drawPoint(-100,-100,200,g);
	}
	
	//画表盘
	public void drawColockFace(int x,int y,int d,Graphics g){
		
		//求圆心位置
		int a = x+d/2;
		int b = y+d/2;

		int x1,y1;
		
		g.drawOval(x, y, d, d);			//画圆

		g.setColor(Color.blue);
		for(int i=0;i<=360;i+=30){
			x1=(int)(d/2*(Math.sin(i*(Math.PI/180))));
			y1=(int)(d/2*(Math.cos(i*(Math.PI/180))));
						
			g.drawLine(x1, y1, a,b);
		}
		
		g.setColor(this.getBackground());
		g.fillOval(x+10, y+10, d-20, d-20);		//把中间的线盖住
		
		
		//中心画一个点
		g.setColor(Color.red);
		g.draw3DRect(a, b, 2, 2, true);
		
		//打上小点
		for(int i=6;i<=360;i+=6){
			x1=(int)((d/2)*(Math.sin(i*(Math.PI/180))));
			y1=(int)(-(d/2)*(Math.cos(i*(Math.PI/180))));
			
			if(i%30!=0){
				g.draw3DRect(x1, y1, 1, 1, true);
			}
		}
		
		//标数字
		g.setColor(Color.red);
		for(int i=30;i<=90;i+=30){
			x1=(int)((d/2+10)*(Math.sin(i*(Math.PI/180))));
			y1=(int)(-(d/2+10)*(Math.cos(i*(Math.PI/180))));
					
			g.drawString(i/30+"", x1, y1);
		}
		for(int i=120;i<=180;i+=30){
			x1=(int)((d/2+15)*(Math.sin(i*(Math.PI/180))));
			y1=(int)(-(d/2+15)*(Math.cos(i*(Math.PI/180))));
					
			g.drawString(i/30+"", x1, y1);
		}
		for(int i=210;i<=270;i+=30){
			x1=(int)((d/2+15)*(Math.sin(i*(Math.PI/180))));
			y1=(int)(-(d/2+15)*(Math.cos(i*(Math.PI/180))));
			
			g.drawString(i/30+"", x1, y1);
		}
		for(int i=300;i<=360;i+=30){
			x1=(int)((d/2+10)*(Math.sin(i*(Math.PI/180))));
			y1=(int)(-(d/2+10)*(Math.cos(i*(Math.PI/180))));
					
			g.drawString(i/30+"", x1, y1);
		}
		

	}

	//画指针
	public void drawPoint(int x,int y,int d,Graphics g){
		int hour,min,sec;
		int hourx,houry,minx,miny,secx,secy;
		int a=x+d/2;
		int b=y+d/2;
		
		Calendar cal = Calendar.getInstance();
		hour=cal.getTime().getHours();
		min=cal.getTime().getMinutes();
		sec=cal.getTime().getSeconds();
		System.out.println(hour+"小时 "+min+"分钟 "+sec+"秒钟 ");
	
		//画小时
		hourx=(int)((d/6)*(Math.sin((Math.PI/180)*hour*30)));
		houry=(int)(-(d/6)*(Math.cos((Math.PI/180)*hour*30)));
		g.drawLine(hourx, houry, a, b);
		
		//画分钟
		minx=(int)((d/4)*(Math.sin((Math.PI/180)*min*6)));
		miny=(int)(-(d/4)*(Math.cos((Math.PI/180)*min*6)));
		g.drawLine(minx, miny, a, b);
		
		//画秒钟
		secx=(int)((d/3)*(Math.sin((Math.PI/180)*sec*6)));
		secy=(int)(-(d/3)*(Math.cos((Math.PI/180)*sec*6)));
		g.drawLine(secx, secy, a, b);
		
		//在面板上写出时间
		g.drawString(hour+"小时"+min+"分钟"+sec+"秒钟", -200, -100);
	}

	@Override
	public void run() {
		while(true){
			try{
				Thread.sleep(1000);
			}catch(Exception e){
				e.printStackTrace();
			}
			this.repaint();
		}
	}
	
}