package kpi.bean;

public class PerspectiveDevBean {
	
	 String famille;
	 String libelle_competence;
	 String aptitude_observable;
	 float moy_competence;
	 
	 
	public PerspectiveDevBean(String famille, String libelle_competence,
			String aptitude_observable, float moy_competence) {
		super();
		this.famille = famille;
		this.libelle_competence = libelle_competence;
		this.aptitude_observable = aptitude_observable;
		this.moy_competence = moy_competence;
	}
	
	
	public PerspectiveDevBean() {
		// TODO Auto-generated constructor stub
	}


	public String getFamille() {
		return famille;
	}
	public void setFamille(String famille) {
		this.famille = famille;
	}
	public String getLibelle_competence() {
		return libelle_competence;
	}
	public void setLibelle_competence(String libelle_competence) {
		this.libelle_competence = libelle_competence;
	}
	public String getAptitude_observable() {
		return aptitude_observable;
	}
	public void setAptitude_observable(String aptitude_observable) {
		this.aptitude_observable = aptitude_observable;
	}
	public float getMoy_competence() {
		return moy_competence;
	}
	public void setMoy_competence(float moy_competence) {
		this.moy_competence = moy_competence;
	}
	 
	 
	 
	 
	 
	 
	 

}
