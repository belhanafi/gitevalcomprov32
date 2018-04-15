package administration.bean;

import java.util.Date;

public class EmployeCompteBean {



	private String nom;
	private String prenom;
	private int id_profile;
	private Date val_date_deb;
	private Date val_date_fin; 
	private Date date_naissance;
	private Date date_recrutement;
	private String code_formation;
	private String libelle_formation;
	private String code_poste;
	private String intitule_poste;
	private String email;
	private String est_evaluateur;
	private String est_responsable_rh;
	private String code_structure;
	private String profile;
	private String nom_complet;
	private String code_est_evaluateur;
	private String code_est_responsable_rh;
	private String login;

	
	private String  causeRejet;


	public String getCauseRejet() {
		return causeRejet;
	}
	public void setCauseRejet(String causeRejet) {
		this.causeRejet = causeRejet;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}


	public int getId_compte() {
		return id_compte;
	}
	public void setId_compte(int id_compte) {
		this.id_compte = id_compte;
	}
	private int id_compte; 
	//	
	//	 private String login;
	//	 private String pwd;
	//	 private int database_id;


	private int id_employe;

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
	public int getId_profile() {
		return id_profile;
	}
	public void setId_profile(int id_profile) {
		this.id_profile = id_profile;
	}
	public Date getVal_date_deb() {
		return val_date_deb;
	}
	public void setVal_date_deb(Date val_date_deb) {
		this.val_date_deb = val_date_deb;
	}
	public Date getVal_date_fin() {
		return val_date_fin;
	}
	public void setVal_date_fin(Date val_date_fin) {
		this.val_date_fin = val_date_fin;
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
	public String getCode_formation() {
		return code_formation;
	}
	public void setCode_formation(String code_formation) {
		this.code_formation = code_formation;
	}
	public String getLibelle_formation() {
		return libelle_formation;
	}
	public void setLibelle_formation(String libelle_formation) {
		this.libelle_formation = libelle_formation;
	}
	public String getCode_poste() {
		return code_poste;
	}
	public void setCode_poste(String code_poste) {
		this.code_poste = code_poste;
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
	public int getId_employe() {
		return id_employe;
	}
	public void setId_employe(int id_employe) {
		this.id_employe = id_employe;
	}
	public String getNom_complet() {
		return nom_complet;
	}
	public void setNom_complet(String nom_complet) {
		this.nom_complet = nom_complet;
	}
	public String getCode_est_evaluateur() {
		return code_est_evaluateur;
	}
	public void setCode_est_evaluateur(String code_est_evaluateur) {
		this.code_est_evaluateur = code_est_evaluateur;
	}
	public String getCode_est_responsable_rh() {
		return code_est_responsable_rh;
	}
	public void setCode_est_responsable_rh(String code_est_responsable_rh) {
		this.code_est_responsable_rh = code_est_responsable_rh;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}

}
