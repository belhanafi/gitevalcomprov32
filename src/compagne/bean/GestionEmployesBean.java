package compagne.bean;

import java.sql.Time;
import java.util.Date;

public class GestionEmployesBean implements Comparable {

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
	private String code_est_evaluateur;
	private String code_est_responsable_rh;


	private String sexe;
	private String type_contrat;

	private String code_sexe;
	private String code_type_contrat;



	private String libelle_structure;




	public GestionEmployesBean(int id_compte, int id_employe,
			String nom_complet, String nom, String prenom, Date date_naissance,
			Date date_recrutement, String libelle_formation,
			String intitule_poste, String email, String est_evaluateur,
			String est_responsable_rh, String code_structure,
			String code_formation, String code_poste,
			String code_est_evaluateur, String code_est_responsable_rh,String libelle_structure,String sexe,String type_contrat) {
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
		this.code_est_evaluateur = code_est_evaluateur;
		this.code_est_responsable_rh = code_est_responsable_rh;
		this.libelle_structure=libelle_structure;
		this.sexe=sexe;
		this.type_contrat=type_contrat;
	}

	public String getCode_est_responsable_rh() {
		return code_est_responsable_rh;
	}

	public void setCode_est_responsable_rh(String code_est_responsable_rh) {
		this.code_est_responsable_rh = code_est_responsable_rh;
	}

	public String getCode_est_evaluateur() {
		return code_est_evaluateur;
	}

	public void setCode_est_evaluateur(String code_est_evaluateur) {
		this.code_est_evaluateur = code_est_evaluateur;
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

	@Override
	public int compareTo(Object o) {
		GestionEmployesBean p=(GestionEmployesBean)o;
		int result = this.nom_complet.compareToIgnoreCase(p.nom_complet);

		return result;
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


}
