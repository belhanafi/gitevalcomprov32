package Statistique.bean;

public class EmployeFormationBean {
	
	private String  niveau;
	private int pourcentage;
	public EmployeFormationBean(String niveau, int pourcentage) {
		super();
		this.niveau = niveau;
		this.pourcentage = pourcentage;
	}
	public EmployeFormationBean() {
		super();
	}
	public String getNiveau() {
		return niveau;
	}
	public void setNiveau(String niveau) {
		this.niveau = niveau;
	}
	public int getPourcentage() {
		return pourcentage;
	}
	public void setPourcentage(int pourcentage) {
		this.pourcentage = pourcentage;
	}
	
	
	
	
	

}
