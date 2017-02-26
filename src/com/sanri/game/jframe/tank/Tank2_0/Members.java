package com.sanri.game.jframe.tank.Tank2_0;

class Tank{
	
	protected int x=0;			//坦克横坐标
	protected int y = 0;		//纵坐标
	
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
	
}

//玩家坦克
class MyTank extends Tank{
	
	private int speed = 3;	//我的坦克的速度
	private int direct = 0;	//方向
	private int color = 0;	//颜色
	
	//构造函数
	public MyTank(int x,int y){
		super(x,y);
	}
	
	//移动方法
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
	
	//set和get方法
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
}