package test.bean;

import java.util.Map;

public class School {
	private String name;
	
	private Map<String,String> onlineYear;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getOnlineYear() {
		return onlineYear;
	}

	public void setOnlineYear(Map<String, String> onlineYear) {
		this.onlineYear = onlineYear;
	}
}
