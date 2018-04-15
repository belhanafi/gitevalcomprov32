package kpi.bean;

public class StatExperienceBean {
	
	private String intitule_poste;
	private String tranche;
	private int pourcentage;
	
	public StatExperienceBean(String intitule_poste, String tranche,
			int pourcentage) {
		super();
		this.intitule_poste = intitule_poste;
		this.tranche = tranche;
		this.pourcentage = pourcentage;
	}

	public StatExperienceBean() {
		super();
	}

	public String getIntitule_poste() {
		return intitule_poste;
	}

	public void setIntitule_poste(String intitule_poste) {
		this.intitule_poste = intitule_poste;
	}

	public String getTranche() {
		return tranche;
	}

	public void setTranche(String tranche) {
		this.tranche = tranche;
	}

	public int getPourcentage() {
		return pourcentage;
	}

	public void setPourcentage(int pourcentage) {
		this.pourcentage = pourcentage;
	}
	
	
	
	

}
