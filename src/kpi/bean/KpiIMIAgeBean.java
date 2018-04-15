package kpi.bean;

public class KpiIMIAgeBean {
	
	private String trancheAge;
	private String niveauMaitrise;
	private int pourcentage;
	
	
	public KpiIMIAgeBean(String trancheAge, String niveauMaitrise,
			int pourcentage) {
		super();
		this.trancheAge = trancheAge;
		this.niveauMaitrise = niveauMaitrise;
		this.pourcentage = pourcentage;
	}
	public KpiIMIAgeBean() {
		// TODO Auto-generated constructor stub
	}
	public String getTrancheAge() {
		return trancheAge;
	}
	public void setTrancheAge(String trancheAge) {
		this.trancheAge = trancheAge;
	}
	public String getNiveauMaitrise() {
		return niveauMaitrise;
	}
	public void setNiveauMaitrise(String niveauMaitrise) {
		this.niveauMaitrise = niveauMaitrise;
	}
	public int getPourcentage() {
		return pourcentage;
	}
	public void setPourcentage(int pourcentage) {
		this.pourcentage = pourcentage;
	}
	
	
	
	
	

}
