package test.bean;

import java.util.List;

public class MiddleStudent extends Student {
	private List<School> schools;
	private List<Float> maxFen;

	public List<School> getSchools() {
		return schools;
	}

	public void setSchools(List<School> schools) {
		this.schools = schools;
	}

	public List<Float> getMaxFen() {
		return maxFen;
	}

	public void setMaxFen(List<Float> maxFen) {
		this.maxFen = maxFen;
	}
}
