package com.sanri.game.jframe.tank.Tank1_0;

//定义一个坦克类
class Tank{
	//横坐标
	protected int x=0;
	//纵坐标
	protected int y=0;
	//坦克的方向
	protected int direct = 0;
	//坦克的速度
	protected int speed = 3;
	
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
	//构造函数
	public Tank(int x,int y){
		this.x=x;
		this.y=y;
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

//我的坦克
class MyTank extends Tank{
	//通过父类方法构造 
	public MyTank(int x,int y){
		super(x,y);
	}
	
	//坦克向上移动
	public void moveUp(){
		this.setY(getY()-this.getSpeed());
	}
	//坦克向下移动
	public void moveDown(){
		this.setY(getY()+this.getSpeed());
	}
	//坦克向左移动
	public void moveLeft(){
		this.setX(getX()-this.getSpeed());
	}
	//坦克向右移动
	public void moveRight(){
		this.setX(getX()+this.getSpeed());
	}
}

//敌人的坦克
class EnemyTank extends Tank{
	public EnemyTank(int x,int y){
		super(x,y);
	}
}

