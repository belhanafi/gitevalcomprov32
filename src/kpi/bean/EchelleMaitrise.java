package kpi.bean;

public class EchelleMaitrise {
	
	private String  libelleEchelle;
	private String  idEchelle;
	
	
	public EchelleMaitrise(String libelleEchelle, String idEchelle) {
		super();
		this.libelleEchelle=libelleEchelle;
		this.idEchelle=idEchelle;
	}
	
	public String getLibelleEchelle() {
		return libelleEchelle;
	}
	public void setLibelleEchelle(String libelleEchelle) {
		this.libelleEchelle = libelleEchelle;
	}
	public String getIdEchelle() {
		return idEchelle;
	}
	public void setIdEchelle(String idEchelle) {
		this.idEchelle = idEchelle;
	}

	
}
