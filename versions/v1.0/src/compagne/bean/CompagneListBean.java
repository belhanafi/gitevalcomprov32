package compagne.bean;

public class CompagneListBean {

	
	private int id_compagne;
	private String libelle_compagne;
	private String date_debut;
	private String date_fin;
	private String compagne_type;
	
	

	public CompagneListBean(int id_compagne, String libelle_compagne,
			String date_debut, String date_fin, String compagne_type) {
		super();
		this.id_compagne = id_compagne;
		this.libelle_compagne = libelle_compagne;
		this.date_debut = date_debut;
		this.date_fin = date_fin;
		this.compagne_type = compagne_type;
	}
	
	
	
	
	public CompagneListBean() {
		super();
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
	public String getCompagne_type() {
		return compagne_type;
	}
	public void setCompagne_type(String compagne_type) {
		this.compagne_type = compagne_type;
	}
	
	
	
	

}
