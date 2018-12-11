package kpi.bean;

public class ActionFormationBean {


	private String idAction ;
	private String idEchelle;
	private String libelleFormation;
	private String nbeffectif;
	
	
	//variabes utilisées pour la sauvegarde des actions/dev dans la base de données
	private String propose;
	private String validee;
	private String programmee;
	private String realisee;
	private String idCompagne;
	private String codePosteTravail;
	
	// variable utilisee pour l'ecran perspective fiche individuelle
	private String evalue;
	private String idEvalue;
	private String idActionCompPost;
	

	



	public ActionFormationBean(){
		super();
	}
	public String getIdAction() {
		return idAction;
	}
	public void setIdAction(String idAction) {
		this.idAction = idAction;
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


	public String getIdActionCompPost() {
		return idActionCompPost;
	}


	public void setIdActionCompPost(String idActionCompPost) {
		this.idActionCompPost = idActionCompPost;
	}

	


}
