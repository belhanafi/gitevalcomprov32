package compagne.bean;

public class MixIdealBean {
	
	private String libelle_compagne;
	private String libelle_niveau_maitrise;
	private float objectif;
	private int id_compagne;
	private String date_debut;
	private String date_fin ;
	private int id_niveau_maitrise;
	
	
	



	public MixIdealBean() {
		// TODO Auto-generated constructor stub
	}

	public int getId_compagne() {
		return id_compagne;
	}
	public void setId_compagne(int id_compagne) {
		this.id_compagne = id_compagne;
	}
	public String getLibelle_compagne() {
		return libelle_compagne;
	}
	public void setLibelle_compagne(String libelle_compagne) {
		this.libelle_compagne = libelle_compagne;
	}
	public String getLibelle_niveau_maitrise() {
		return libelle_niveau_maitrise;
	}
	public void setLibelle_niveau_maitrise(String libelle_niveau_maitrise) {
		this.libelle_niveau_maitrise = libelle_niveau_maitrise;
	}
	public float getObjectif() {
		return objectif;
	}
	public void setObjectif(float objectif) {
		this.objectif = objectif;
	}
	
	
	
	
	public String getDate_debut() {
		return date_debut;
	}

	public void setDate_debut(String date_debut) {
		this.date_debut = date_debut;
	}

	public String getDate_fin() {
		return date_fin;
	}

	public void setDate_fin(String date_fin) {
		this.date_fin = date_fin;
	}
	
	
	public int getId_niveau_maitrise() {
		return id_niveau_maitrise;
	}

	public void setId_niveau_maitrise(int id_niveau_maitrise) {
		this.id_niveau_maitrise = id_niveau_maitrise;
	}
	

}
