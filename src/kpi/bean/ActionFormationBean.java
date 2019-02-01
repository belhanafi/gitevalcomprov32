package kpi.bean;

public class ActionFormationBean {


	
	private String idEchelle;
	private String domaineFormation;
	private String themeFormation;
	private String libelleFormation; //le libelle saisi par le user

	
	
	//variabes utilis�es pour la sauvegarde des actions/dev dans la base de donn�es
	private String propose;
	private String validee;
	private String programmee;
	private String realisee;
	private String idCompagne;
	private String codePosteTravail;
	
	private String idTypeFormationExterne;
	
	// variable utilisee pour l'ecran perspective fiche individuelle
	private String evalue;
	private String idEvalue;
	//private String idActionCompPost;
	private String idActionFormEmploye;

	



	public ActionFormationBean( String idEchelle,
			String libelleFormation,  String propose,
			String validee, String programmee, String realisee,
			String idCompagne, String codePosteTravail, 
			String idEvalue,
			String idTypeFormationExterne) {
		super();
		
		this.idEchelle = idEchelle;
		this.libelleFormation = libelleFormation;
		this.propose = propose;
		this.validee = validee;
		this.programmee = programmee;
		this.realisee = realisee;
		this.idCompagne = idCompagne;
		this.codePosteTravail = codePosteTravail;
		this.idEvalue = idEvalue;
		this.idTypeFormationExterne=idTypeFormationExterne;
	}
	public ActionFormationBean(){
		super();
	}

	public String getIdEchelle() {
		return idEchelle;
	}
	public void setIdEchelle(String idEchelle) {
		this.idEchelle = idEchelle;
	}
	public String getLibelleFormation() {
		return libelleFormation;
	}
	public void setLibelleFormation(String libelleFormation) {
		this.libelleFormation = libelleFormation;
	}
	


	public String getDomaineFormation() {
		return domaineFormation;
	}
	public void setDomaineFormation(String domaineFormation) {
		this.domaineFormation = domaineFormation;
	}
	
	
	public String getThemeFormation() {
		return themeFormation;
	}
	public void setThemeFormation(String themeFormation) {
		this.themeFormation = themeFormation;
	}
	public String getPropose() {
		return propose;
	}


	public void setPropose(String propose) {
		this.propose = propose;
	}


	public String getValidee() {
		return validee;
	}


	public void setValidee(String validee) {
		this.validee = validee;
	}


	public String getProgrammee() {
		return programmee;
	}


	public void setProgrammee(String programmee) {
		this.programmee = programmee;
	}


	public String getRealisee() {
		return realisee;
	}


	public void setRealisee(String realisee) {
		this.realisee = realisee;
	}


	public String getIdCompagne() {
		return idCompagne;
	}


	public void setIdCompagne(String idCompagne) {
		this.idCompagne = idCompagne;
	}


	public String getCodePosteTravail() {
		return codePosteTravail;
	}


	public void setCodePosteTravail(String codePosteTravail) {
		this.codePosteTravail = codePosteTravail;
	}


	public String getEvalue() {
		return evalue;
	}


	public void setEvalue(String evalue) {
		this.evalue = evalue;
	}


	public String getIdEvalue() {
		return idEvalue;
	}


	public void setIdEvalue(String idEvalue) {
		this.idEvalue = idEvalue;
	}


//	public String getIdActionCompPost() {
//		return idActionCompPost;
//	}
//
//
//	public void setIdActionCompPost(String idActionCompPost) {
//		this.idActionCompPost = idActionCompPost;
//	}
	public String getIdActionFormEmploye() {
		return idActionFormEmploye;
	}
	public void setIdActionFormEmploye(String idActionFormEmploye) {
		this.idActionFormEmploye = idActionFormEmploye;
	}
	public String getIdTypeFormationExterne() {
		return idTypeFormationExterne;
	}
	public void setIdTypeFormationExterne(String typeFormationExterne) {
		this.idTypeFormationExterne = typeFormationExterne;
	}

	


}
