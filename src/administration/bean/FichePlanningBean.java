package administration.bean;

import java.util.Date;

public class FichePlanningBean {

	private String evaluateur;
	private String evalue;
	private String code_poste;
	private Date date_evaluation;
	private String heure_debut_eval;
	private String heure_fin_eval;
	private String lieu;
	private String personne_ressource;
	private String structure;
	private String causeRejet;
	private Date date_fin_evaluation;





	public FichePlanningBean() {
		super();
	}
	public FichePlanningBean(String evaluateur, String evalue,
			String code_poste, Date date_evaluation, String heure_debut_eval,
			String heure_fin_eval, String lieu, String personne_ressource,
			String structure,Date date_fin_evaluation) {
		super();
		this.evaluateur = evaluateur;
		this.evalue = evalue;
		this.code_poste = code_poste;
		this.date_evaluation = date_evaluation;
		this.heure_debut_eval = heure_debut_eval;
		this.heure_fin_eval = heure_fin_eval;
		this.lieu = lieu;
		this.personne_ressource = personne_ressource;
		this.structure = structure;
		this.date_fin_evaluation=date_fin_evaluation;

	}
	public String getStructure() {
		return structure;
	}
	public void setStructure(String structure) {
		this.structure = structure;
	}
	public String getEvaluateur() {
		return evaluateur;
	}
	public void setEvaluateur(String evaluateur) {
		this.evaluateur = evaluateur;
	}
	public String getEvalue() {
		return evalue;
	}
	public void setEvalue(String evalue) {
		this.evalue = evalue;
	}
	public String getCode_poste() {
		return code_poste;
	}
	public void setCode_poste(String code_poste) {
		this.code_poste = code_poste;
	}
	public Date getDate_evaluation() {
		return date_evaluation;
	}
	public void setDate_evaluation(Date date_evaluation) {
		this.date_evaluation = date_evaluation;
	}
	public String getHeure_debut_eval() {
		return heure_debut_eval;
	}
	public void setHeure_debut_eval(String heure_debut_eval) {
		this.heure_debut_eval = heure_debut_eval;
	}
	public String getHeure_fin_eval() {
		return heure_fin_eval;
	}
	public void setHeure_fin_eval(String heure_fin_eval) {
		this.heure_fin_eval = heure_fin_eval;
	}
	public String getLieu() {
		return lieu;
	}
	public void setLieu(String lieu) {
		this.lieu = lieu;
	}
	public String getPersonne_ressource() {
		return personne_ressource;
	}
	public void setPersonne_ressource(String personne_ressource) {
		this.personne_ressource = personne_ressource;
	}
	public String getCauseRejet() {
		return causeRejet;
	}
	public void setCauseRejet(String causeRejet) {
		this.causeRejet = causeRejet;
	}




	public Date getDate_fin_evaluation() {
		return date_fin_evaluation;
	}
	public void setDate_fin_evaluation(Date date_fin_evaluation) {
		this.date_fin_evaluation = date_fin_evaluation;
	}


}
