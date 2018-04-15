package compagne.bean;

import java.sql.Time;
import java.util.Date;

public class PlanningCompagneListBean {

	private int id_planning_evaluation;
	private String libelle_compagne;
	private String evaluateur;
	private String evalue;
	private String intitule_poste;
	private String code_structure;
	private Date date_evaluation;
	private String heure_debut_evaluation;
	private String heure_fin_evaluation;
	private String lieu;
	private String personne_ressources;
	private int id_compagne;
	private int id_evaluateur;
	private int id_evalue;
	private String code_poste;
	


	public PlanningCompagneListBean(int id_planning_evaluation,
			String libelle_compagne, String evaluateur, String evalue,
			String intitule_poste, String code_structure, Date date_evaluation,
			String heure_debut_evaluation, String heure_fin_evaluation,
			String lieu, String personne_ressources, int id_compagne,
			int id_evaluateur, int id_evalue, String code_poste) {
		super();
		this.id_planning_evaluation = id_planning_evaluation;
		this.libelle_compagne = libelle_compagne;
		this.evaluateur = evaluateur;
		this.evalue = evalue;
		this.intitule_poste = intitule_poste;
		this.code_structure = code_structure;
		this.date_evaluation = date_evaluation;
		this.heure_debut_evaluation = heure_debut_evaluation;
		this.heure_fin_evaluation = heure_fin_evaluation;
		this.lieu = lieu;
		this.personne_ressources = personne_ressources;
		this.id_compagne = id_compagne;
		this.id_evaluateur = id_evaluateur;
		this.id_evalue = id_evalue;
		this.code_poste = code_poste;
	}


	public PlanningCompagneListBean() {
		super();
	}


	public int getId_planning_evaluation() {
		return id_planning_evaluation;
	}

	public void setId_planning_evaluation(int id_planning_evaluation) {
		this.id_planning_evaluation = id_planning_evaluation;
	}


	public String getLibelle_compagne() {
		return libelle_compagne;
	}

	public void setLibelle_compagne(String libelle_compagne) {
		this.libelle_compagne = libelle_compagne;
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

	public String getIntitule_poste() {
		return intitule_poste;
	}

	public void setIntitule_poste(String intitule_poste) {
		this.intitule_poste = intitule_poste;
	}

	public String getCode_structure() {
		return code_structure;
	}

	public void setCode_structure(String code_structure) {
		this.code_structure = code_structure;
	}

	public Date getDate_evaluation() {
		return date_evaluation;
	}

	public void setDate_evaluation(Date date_evaluation) {
		this.date_evaluation = date_evaluation;
	}




	public String getHeure_debut_evaluation() {
		return heure_debut_evaluation;
	}

	public void setHeure_debut_evaluation(String heure_debut_evaluation) {
		this.heure_debut_evaluation = heure_debut_evaluation;
	}

	public String getHeure_fin_evaluation() {
		return heure_fin_evaluation;
	}

	public void setHeure_fin_evaluation(String heure_fin_evaluation) {
		this.heure_fin_evaluation = heure_fin_evaluation;
	}

	public String getLieu() {
		return lieu;
	}

	public void setLieu(String lieu) {
		this.lieu = lieu;
	}

	public String getPersonne_ressources() {
		return personne_ressources;
	}

	public void setPersonne_ressources(String personne_ressources) {
		this.personne_ressources = personne_ressources;
	}

	public int getId_compagne() {
		return id_compagne;
	}

	public void setId_compagne(int id_compagne) {
		this.id_compagne = id_compagne;
	}

	public int getId_evaluateur() {
		return id_evaluateur;
	}

	public void setId_evaluateur(int id_evaluateur) {
		this.id_evaluateur = id_evaluateur;
	}

	public int getId_evalue() {
		return id_evalue;
	}

	public void setId_evalue(int id_evalue) {
		this.id_evalue = id_evalue;
	}


	public String getCode_poste() {
		return code_poste;
	}


	public void setCode_poste(String code_poste) {
		this.code_poste = code_poste;
	}
	
	
		
	

}
