package kpi.bean;

public class KPIAgeExpBean {
	
	int age;
	int experience;
	float imi;
	
	public KPIAgeExpBean() {
		super();
	}
	public KPIAgeExpBean(int age, int experience, float imi) {
		super();
		this.age = age;
		this.experience = experience;
		this.imi = imi;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getExperience() {
		return experience;
	}
	public void setExperience(int experience) {
		this.experience = experience;
	}
	public float getImi() {
		return imi;
	}
	public void setImi(float imi) {
		this.imi = imi;
	}
	
	
	

}
