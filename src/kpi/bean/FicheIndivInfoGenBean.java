package kpi.bean;

import java.sql.Time;
import java.util.Date;

public class FicheIndivInfoGenBean {



	private int id_compte; 
	private int id_employe;
	private String nom_complet;
	private String nom;
	private String prenom;
	private Date date_naissance;
	private Date date_recrutement;
	private String libelle_formation;
	private String intitule_poste;
	private String email;
	private String est_evaluateur;
	private String est_responsable_rh;
	private String code_structure;
	private String code_formation;
	private String code_poste;
	private String sexe;
	private String type_contrat;
	private String code_sexe;
	private String code_type_contrat;
	private String libelle_structure;
	private String matricule;
	private String libelle_direction;

	private String evolution_carriere;
	private String antecedent_disciplinaire;



	public FicheIndivInfoGenBean() {
		super();
	}

	


	

	public String getMatricule() {
		return matricule;
	}


	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}


	public int getId_compte() {
		return id_compte;
	}

	public void setId_compte(int id_compte) {
		this.id_compte = id_compte;
	}

	
	public int getId_employe() {
		return id_employe;
	}

	public void setId_employe(int id_employe) {
		this.id_employe = id_employe;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Date getDate_naissance() {
		return date_naissance;
	}

	public void setDate_naissance(Date date_naissance) {
		this.date_naissance = date_naissance;
	}

	public Date getDate_recrutement() {
		return date_recrutement;
	}

	public void setDate_recrutement(Date date_recrutement) {
		this.date_recrutement = date_recrutement;
	}

	public String getLibelle_formation() {
		return libelle_formation;
	}

	public void setLibelle_formation(String libelle_formation) {
		this.libelle_formation = libelle_formation;
	}

	public String getIntitule_poste() {
		return intitule_poste;
	}

	public void setIntitule_poste(String intitule_poste) {
		this.intitule_poste = intitule_poste;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEst_evaluateur() {
		return est_evaluateur;
	}

	public void setEst_evaluateur(String est_evaluateur) {
		this.est_evaluateur = est_evaluateur;
	}

	public String getEst_responsable_rh() {
		return est_responsable_rh;
	}

	public void setEst_responsable_rh(String est_responsable_rh) {
		this.est_responsable_rh = est_responsable_rh;
	}

	public String getCode_structure() {
		return code_structure;
	}

	public void setCode_structure(String code_structure) {
		this.code_structure = code_structure;
	}

	public String getCode_formation() {
		return code_formation;
	}

	public void setCode_formation(String code_formation) {
		this.code_formation = code_formation;
	}

	public String getCode_poste() {
		return code_poste;
	}

	public void setCode_poste(String code_poste) {
		this.code_poste = code_poste;
	}

	public String getNom_complet() {
		return nom_complet;
	}

	public void setNom_complet(String nom_complet) {
		this.nom_complet = nom_complet;
	}

	
	public String getLibelle_structure() {
		return libelle_structure;
	}

	public void setLibelle_structure(String libelle_structure) {
		this.libelle_structure = libelle_structure;
	}


	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public String getType_contrat() {
		return type_contrat;
	}

	public void setType_contrat(String type_contrat) {
		this.type_contrat = type_contrat;
	}
	public String getCode_sexe() {
		return code_sexe;
	}

	public void setCode_sexe(String code_sexe) {
		this.code_sexe = code_sexe;
	}

	public String getCode_type_contrat() {
		return code_type_contrat;
	}

	public void setCode_type_contrat(String code_type_contrat) {
		this.code_type_contrat = code_type_contrat;
	}


	public String getLibelle_direction() {
		return libelle_direction;
	}

	public void setLibelle_direction(String libelle_direction) {
		this.libelle_direction = libelle_direction;
	}






	public String getEvolution_carriere() {
		return evolution_carriere;
	}






	public void setEvolution_carriere(String evolution_carriere) {
		this.evolution_carriere = evolution_carriere;
	}






	public String getAntecedent_disciplinaire() {
		return antecedent_disciplinaire;
	}


	public void setAntecedent_disciplinaire(String antecedent_disciplinaire) {
		this.antecedent_disciplinaire = antecedent_disciplinaire;
	}


}
