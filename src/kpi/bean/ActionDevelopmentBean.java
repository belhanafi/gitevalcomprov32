package kpi.bean;

public class ActionDevelopmentBean {


	private String idAction ;
	private String idEchelle;
	private String libelleFormation;
	private String libelleOriProf;
	private String libelleDiscipline;
	private String libelleMobilite;
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
	

	public ActionDevelopmentBean(String idAction, String idEchelle, String libelleFormation, String libelleOriProf, String libelleDiscipline, String libelleMobilite) {
		super();
		this.idAction=idAction;
		this.idEchelle=idEchelle;
		this.libelleFormation=libelleFormation;
		this.libelleOriProf=libelleOriProf;
		this.libelleDiscipline=libelleDiscipline;
		this.libelleMobilite=libelleMobilite;
	}


	public ActionDevelopmentBean(String idAction,String idCompagne,String codePosteTravail,String propose, String validee, String programmee, String realisee){
		super();
		this.idAction=idAction;
		this.idCompagne=idCompagne;
		this.codePosteTravail=codePosteTravail;
		this.propose=propose;
		this.validee=validee;
		this.programmee=programmee;
		this.realisee=realisee;
	
	}
	
	public ActionDevelopmentBean(String idActionCompPost,String idAction,String idCompagne,String codePosteTravail,String idEvalue,String evalue, String propose, String validee, String programmee, String realisee){
		super();
		this.idAction=idAction;
		this.idCompagne=idCompagne;
		this.codePosteTravail=codePosteTravail;
		this.idEvalue=idEvalue;
		this.evalue=evalue;
		this.propose=propose;
		this.validee=validee;
		this.programmee=programmee;
		this.realisee=realisee;
		this.idActionCompPost=idActionCompPost;
	}
	
	public ActionDevelopmentBean( String libelleFormation, String libelleOriProf, String libelleDiscipline, String libelleMobilite) {
		super();

		this.libelleFormation=libelleFormation;
		this.libelleOriProf=libelleOriProf;
		this.libelleDiscipline=libelleDiscipline;
		this.libelleMobilite=libelleMobilite;
	}

	



	public ActionDevelopmentBean(){
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
	public String getLibelleOriProf() {
		return libelleOriProf;
	}
	public void setLibelleOriProf(String libelleOriProf) {
		this.libelleOriProf = libelleOriProf;
	}
	public String getLibelleDiscipline() {
		return libelleDiscipline;
	}
	public void setLibelleDiscipline(String libelleDiscipline) {
		this.libelleDiscipline = libelleDiscipline;
	}
	public String getLibelleMobilite() {
		return libelleMobilite;
	}
	public void setLibelleMobilite(String libelleMobilite) {
		this.libelleMobilite = libelleMobilite;
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
