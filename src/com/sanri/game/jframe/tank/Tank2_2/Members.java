package com.sanri.game.jframe.tank.Tank2_2;
import java.util.*;

//炸弹类
class Bomb{
	private int x=0;
	private int y=0;
	private int life=9;
	boolean isLive = true;
	
	public Bomb(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public void lifeDown(){
		if(this.life>0){
			life--;
		}else{
			this.isLive=false;
		}
	}
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}


}

//子弹类
class Shot implements Runnable{
	private int isLive=1;
	private int x=0;
	private int y=0;
	private int speed=5;
	private int direct;
	
	//构造函数
	public Shot(int x,int y,int direct){
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	

	//重写run函数
	public void run(){
		while(true){
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			switch(direct){
			case 0:
				this.y-=this.speed;
				break;
			case 1:
				this.y+=this.speed;
				break;
			case 2:
				this.x-=this.speed;
				break;
			case 3:
				this.x+=this.speed;
				break;
				
			}
			
			//结束子弹线程
			if(x<0 || x>640 ||y<0 ||y>480){
				this.isLive=0;
				break;
			}
			
		}
	}
	
	//set和get方法
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}


	public int getIsLive() {
		return isLive;
	}


	public void setIsLive(int isLive) {
		this.isLive = isLive;
	}


	public int getDirect() {
		return direct;
	}


	public void setDirect(int direct) {
		this.direct = direct;
	}
	
}

//坦克类
class Tank{
	
	protected int x=0;			//坦克横坐标
	protected int y = 0;		//纵坐标
	protected int color;		//坦克颜色
	protected int direct=0;		//坦克方向
	protected int speed = 3;	//坦克速度
	
	//构造函数
	public Tank(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	//set和get方法
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
}

//玩家坦克
class MyTank extends Tank{
	
	Vector<Shot> ss = new Vector<Shot>();		//子弹
	
	//构造函数
	public MyTank(int x,int y){
		super(x,y);
	}
	
	//坦克开火方法
	public void shotEnemy(){
		Shot s;
		switch(direct){
		case 0:
			s = new Shot(this.x+10,this.y-5 , direct);
			ss.add(s);
			break;
		case 1:
			s = new Shot(this.x+10, this.y+35, direct);
			ss.add(s);
			break;
		case 2:
			s = new Shot(this.x-10, this.y+15, direct);
			ss.add(s);
			break;
		case 3:
			s = new Shot(this.x+30, this.y+15, direct);
			ss.add(s);
			break;
		}
		
		//子弹进程启动
		for(int i=0;i<ss.size();i++){
			Thread temp = new Thread(ss.get(i));
			temp.start();
		}
		
	}
	
	//坦克移动方法
	public void moveUp(){
			this.y-=this.speed;
	}
	
	public void moveDown(){
		this.y+=this.speed;
	}
	
	public void moveLeft(){
		this.x-=this.speed;
	}
	
	public void moveRight(){
		this.x+=this.speed;
	}

}

//敌人的坦克 
class EnemyTank extends Tank implements Runnable{
	
	private int isLive = 1;
	Vector<Shot> enemyShots = new Vector<Shot>();
	
	//构造函数
	public EnemyTank(int x,int y){
		super(x, y);
	}

	public int getIsLive() {
		return isLive;
	}

	public void setIsLive(int isLive) {
		this.isLive = isLive;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(this.isLive==1){
				switch(this.direct){
				case 0:
					for(int i=0;i<30;i++){
						if(y>0){
							y-=this.speed;
						}
						try {
							Thread.sleep(70);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					break;
				case 1:
					for(int i=0;i<30;i++){
						if(y<480){
							y+=this.speed;
						}
						try {
							Thread.sleep(70);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					break;
				case 2:
					for(int i=0;i<30;i++){
						if(x>0){
							x-=this.speed;
						}
						try {
							Thread.sleep(70);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					break;
				case 3:
					for(int i=0;i<30;i++){
						if(x<640){
							x+=this.speed;
						}
						try {
							Thread.sleep(70);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					break;
				}
			}else{
				break;
			}
			
			//改变方向
			this.direct = (int)(Math.random()*4);
		}
	}
}