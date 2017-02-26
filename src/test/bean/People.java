package test.bean;

import java.util.List;

public class People {
	private String idCard;
	private String name;
	private int age;
	private List<String> favorite;
	
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public List<String> getFavorite() {
		return favorite;
	}
	public void setFavorite(List<String> favorite) {
		this.favorite = favorite;
	}
}
