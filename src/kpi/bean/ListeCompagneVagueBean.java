package kpi.bean;

/**
 * @author nbehlouli
 *
 */
public class ListeCompagneVagueBean {
	
	String nombase;
	String libelle_compagne;
	String compagne_id;
	String nominstance;
	
	
	
	public ListeCompagneVagueBean(String nombase, String libelle_compagne,
			String compagne_id, String nominstance) {
		super();
		this.nombase = nombase;
		this.libelle_compagne = libelle_compagne;
		this.compagne_id = compagne_id;
		this.nominstance = nominstance;
	}
	public ListeCompagneVagueBean() {
		super();
	}
	public ListeCompagneVagueBean(String nombase, String libelle_compagne,
			String compagne_id) {
		super();
		this.nombase = nombase;
		this.libelle_compagne = libelle_compagne;
		this.compagne_id = compagne_id;
	}
	public String getNombase() {
		return nombase;
	}
	public void setNombase(String nombase) {
		this.nombase = nombase;
	}
	public String getLibelle_compagne() {
		return libelle_compagne;
	}
	public void setLibelle_compagne(String libelle_compagne) {
		this.libelle_compagne = libelle_compagne;
	}
	public String getCompagne_id() {
		return compagne_id;
	}
	public void setCompagne_id(String compagne_id) {
		this.compagne_id = compagne_id;
	}
	
	
	
	public String getNominstance() {
		return nominstance;
	}
	public void setNominstance(String nominstance) {
		this.nominstance = nominstance;
	}
	

}
