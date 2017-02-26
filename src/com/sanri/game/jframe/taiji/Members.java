package com.sanri.game.jframe.taiji;

class TaiJi implements Runnable {
	private int x;
	private int y;
	private int d;
	private int angle;
	private int md;
	private int circleSpeed=10;
	private int moveSpeed=2;
	private int changeSpeed=3;			//变化大小的速度
	private int changCircleSpeed=1;		//变化旋转速度的速度
	
	public TaiJi(int x,int y,int d,int angle){
		this.x=x;
		this.y=y;
		this.d=d;
		this.angle=angle;
		
		this.md=d/10;
	}
	
	public void moveUp(){
		if(this.y>-240){
			this.y-=this.moveSpeed;
		}
	}
	public void moveDown(){
		if(this.y<200-this.d){
//			System.out.println("y= "+y);
			this.y+=this.moveSpeed;
		}
	}
	public void moveLeft(){
		if(this.x>-320){
			this.x-=this.moveSpeed;
		}
	}
	public void moveRight(){
		if(this.x<310-this.d){
//			System.out.println("x= "+x);
			this.x+=this.moveSpeed;
		}
	}
	
	public void changeBig(){
		if(this.d<400){
			this.d+=this.changeSpeed;
			this.md=this.d/10;
		}
	}
	public void changeSmall(){
		if(this.d>0){
			this.d-=this.changeSpeed;
			this.md=this.d/10;
		}
	}
	public void upCircleSpeed(){
		this.circleSpeed+=this.changCircleSpeed;
	}
	public void downCircleSpeed(){
		this.circleSpeed-=this.changCircleSpeed;
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
	public int getD() {
		return d;
	}
	public void setD(int d) {
		this.d = d;
	}
	public int getAngle() {
		return angle;
	}
	public void setAngle(int angle) {
		this.angle = angle;
	}
	public int getMd() {
		return md;
	}
	public void setMd(int md) {
		this.md = md;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
			this.angle+=this.circleSpeed;
//			System.out.println("angle"+angle);
			if(angle==360){
//				try {
//					Thread.sleep(1000);
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
				this.angle=0;
			}
		}
	}

	public int getCircleSpeed() {
		return circleSpeed;
	}

	public void setCircleSpeed(int circleSpeed) {
		this.circleSpeed = circleSpeed;
	}

	public int getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
}