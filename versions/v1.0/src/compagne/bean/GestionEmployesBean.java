package compagne.bean;

import java.sql.Time;
import java.util.Date;

public class GestionEmployesBean {

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
	 

	

	

	public GestionEmployesBean(int id_compte, int id_employe,
			String nom_complet, String nom, String prenom, Date date_naissance,
			Date date_recrutement, String libelle_formation,
			String intitule_poste, String email, String est_evaluateur,
			String est_responsable_rh, String code_structure,
			String code_formation, String code_poste) {
		super();
		this.id_compte = id_compte;
		this.id_employe = id_employe;
		this.nom_complet = nom_complet;
		this.nom = nom;
		this.prenom = prenom;
		this.date_naissance = date_naissance;
		this.date_recrutement = date_recrutement;
		this.libelle_formation = libelle_formation;
		this.intitule_poste = intitule_poste;
		this.email = email;
		this.est_evaluateur = est_evaluateur;
		this.est_responsable_rh = est_responsable_rh;
		this.code_structure = code_structure;
		this.code_formation = code_formation;
		this.code_poste = code_poste;
	}

	public int getId_compte() {
		return id_compte;
	}

	public void setId_compte(int id_compte) {
		this.id_compte = id_compte;
	}

	public GestionEmployesBean() {
		super();
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
	

	 
	 

		
	

}
