package kpi.bean;

public class SuiviActionDevBean {
	
	
	
	private int pourcentage;
	private String evalue;

	
	public SuiviActionDevBean( int pourcentage, String evalue) {
		super();
		
		this.pourcentage = pourcentage;
		this.evalue = evalue;
	}

	public SuiviActionDevBean() {
		
	}

	public String getEvalue() {
		return evalue;
	}
	public void setEvalue(String evalue) {
		this.evalue = evalue;
	}
	
	public int getPourcentage() {
		return pourcentage;
	}
	public void setPourcentage(int pourcentage) {
		this.pourcentage = pourcentage;
	}
	
	
	
	
}
