package compagne.bean;

import java.util.Date;

public class SuiviCompagneBean   {
	
	private int  id_employe; 
	private int pourcentage;
	private String evaluateur ;
	private String email;
	private String poste_travail;
	private int fichenoncote;
	private int fichecote;
	private String dateExtract;
	private String login;
	
	
	
	
	

	public SuiviCompagneBean(int id_employe, int pourcentage,
			String evaluateur, String email, String poste_travail,
			int fichenoncote, int fichecote, String dateExtract, String login) {
		super();
		this.id_employe = id_employe;
		this.pourcentage = pourcentage;
		this.evaluateur = evaluateur;
		this.email = email;
		this.poste_travail = poste_travail;
		this.fichenoncote = fichenoncote;
		this.fichecote = fichecote;
		this.dateExtract = dateExtract;
		this.login = login;
	}




	public SuiviCompagneBean(  String evaluateur,String poste_travail,int pourcentage,
			int id_employe, String email,String dateExtract) {
		super();
		
		this.evaluateur = evaluateur;
		this.poste_travail = poste_travail;
		this.pourcentage = pourcentage;
		this.id_employe = id_employe;
		this.email = email;
		this.dateExtract=dateExtract;
		
	}

	
	
	
	public SuiviCompagneBean(int pourcentage, String evaluateur, String email,
			String poste_travail) {
		super();
		this.pourcentage = pourcentage;
		this.evaluateur = evaluateur;
		this.email = email;
		this.poste_travail = poste_travail;
	}

	public SuiviCompagneBean() {
		
	}
	
	public SuiviCompagneBean(int id_employe, int pourcentage, String evaluateur) {
		super();
		this.id_employe = id_employe;
		this.pourcentage = pourcentage;
		this.evaluateur = evaluateur;
	}
	
	
	public SuiviCompagneBean(int pourcentage, String evaluateur) {
		super();
		this.pourcentage = pourcentage;
		this.evaluateur = evaluateur;
	}
	
	
	public SuiviCompagneBean(int id_employe, int pourcentage,
			String evaluateur, String email) {
		super();
		this.id_employe = id_employe;
		this.pourcentage = pourcentage;
		this.evaluateur = evaluateur;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEvaluateur() {
		return evaluateur;
	}
	public void setEvaluateur(String evaluateur) {
		this.evaluateur = evaluateur;
	}
	public int getPourcentage() {
		return pourcentage;
	}
	public void setPourcentage(int pourcentage) {
		this.pourcentage = pourcentage;
	}

	public int getId_employe() {
		return id_employe;
	}

	public void setId_employe(int id_employe) {
		this.id_employe = id_employe;
	}


	
	public String getPoste_travail() {
		return poste_travail;
	}

	public void setPoste_travail(String poste_travail) {
		this.poste_travail = poste_travail;
	}
	
	public int getFichenoncote() {
		return fichenoncote;
	}




	public void setFichenoncote(int fichenoncote) {
		this.fichenoncote = fichenoncote;
	}




	public int getFichecote() {
		return fichecote;
	}




	public void setFichecote(int fichencote) {
		this.fichecote = fichencote;
	}
	
	public String getDateExtract() {
		return dateExtract;
	}




	public void setDateExtract(String dateExtract) {
		this.dateExtract = dateExtract;
	}
	
	
	public String getLogin() {
		return login;
	}




	public void setLogin(String login) {
		this.login = login;
	}
}
