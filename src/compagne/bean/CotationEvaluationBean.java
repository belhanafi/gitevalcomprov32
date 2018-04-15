package compagne.bean;

public class CotationEvaluationBean {
	private int id_repertoire_competence; //laligne associée à cette compétence dans le repertoire de compétence
	//private String libelle_competence; 
	private String code_competence;
	//private String aptitude_observable;
	private String cotation; //la cotation saisie par l'évaluateur pour cette apptitude observable
	private String famille;
	
	public String getFamille() {
		return famille;
	}
	public void setFamille(String famille) {
		this.famille = famille;
	}
	public int getId_repertoire_competence() {
		return id_repertoire_competence;
	}
	public void setId_repertoire_competence(int id_repertoire_competence) {
		this.id_repertoire_competence = id_repertoire_competence;
	}
//	public String getLibelle_competence() {
//		return libelle_competence;
//	}
//	public void setLibelle_competence(String libelle_competence) {
//		this.libelle_competence = libelle_competence;
//	}
	public String getCode_competence() {
		return code_competence;
	}
	public void setCode_competence(String code_competence) {
		this.code_competence = code_competence;
	}
//	public String getAptitude_observable() {
//		return aptitude_observable;
//	}
//	public void setAptitude_observable(String aptitude_observable) {
//		this.aptitude_observable = aptitude_observable;
//	}
	public String getCotation() {
		return cotation;
	}
	public void setCotation(String cotation) {
		this.cotation = cotation;
	}

}
