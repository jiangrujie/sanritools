package test.mini;

public class TestTime {
	static{
		System.out.println("父类静态块");
	}
	{
		System.out.println("父类代码块");
	}
	public TestTime(){
		System.out.println("父类构造函数 ");
	}
	
	public static void main(String[] args) {
		B b = new B();
		b = new B();
	}
}

class B extends TestTime{
	static{
		System.out.println("子类静态块");
	}
	{
		System.out.println("子类代码块");
	}
	public B(){
		System.out.println("子类构造函数 ");
	}
}
