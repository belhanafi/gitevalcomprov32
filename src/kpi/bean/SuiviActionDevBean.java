package kpi.bean;

public class SuiviActionDevBean {
	
	
	
	private int pourcentage;
	private String progres;

	private String evalue;
	private String login;
	private String libelle_direction;
	private String structure_ent;
	private String poste_travail;
	private String actionDev;
	
	private String libelle_action_formation;
	
	private String libelle_action_ori_prof ;
	private String libelle_action_disipline ;
	private String libelle_action_mobilite;
	private String evaluateur;

	


	public SuiviActionDevBean( int pourcentage, String evalue) {
		super();
		
		this.pourcentage = pourcentage;
		this.evalue = evalue;
	}

	public SuiviActionDevBean() {
		
	}

	public String getEvalue() {
		return evalue;
	}
	public void setEvalue(String evalue) {
		this.evalue = evalue;
	}
	
	public int getPourcentage() {
		return pourcentage;
	}
	public void setPourcentage(int pourcentage) {
		this.pourcentage = pourcentage;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLibelle_direction() {
		return libelle_direction;
	}

	public void setLibelle_direction(String libelle_direction) {
		this.libelle_direction = libelle_direction;
	}

	public String getStructure_ent() {
		return structure_ent;
	}

	public void setStructure_ent(String structure_ent) {
		this.structure_ent = structure_ent;
	}

	public String getPoste_travail() {
		return poste_travail;
	}

	public void setPoste_travail(String poste_travail) {
		this.poste_travail = poste_travail;
	}

	public String getActionDev() {
		return actionDev;
	}

	public void setActionDev(String actionDev) {
		this.actionDev = actionDev;
	}

	public String getLibelle_action_formation() {
		return libelle_action_formation;
	}

	public void setLibelle_action_formation(String libelle_action_formation) {
		this.libelle_action_formation = libelle_action_formation;
	}

	public String getLibelle_action_ori_prof() {
		return libelle_action_ori_prof;
	}

	public void setLibelle_action_ori_prof(String libelle_action_ori_prof) {
		this.libelle_action_ori_prof = libelle_action_ori_prof;
	}

	public String getLibelle_action_disipline() {
		return libelle_action_disipline;
	}

	public void setLibelle_action_disipline(String libelle_action_disipline) {
		this.libelle_action_disipline = libelle_action_disipline;
	}

	public String getLibelle_action_mobilite() {
		return libelle_action_mobilite;
	}

	public void setLibelle_action_mobilite(String libelle_action_mobilite) {
		this.libelle_action_mobilite = libelle_action_mobilite;
	}

	
	public SuiviActionDevBean(int pourcentage, String progres, String evalue,
			String login, String libelle_direction, String structure_ent,
			String poste_travail, String actionDev,
			String libelle_action_formation, String libelle_action_ori_prof,
			String libelle_action_disipline, String libelle_action_mobilite,
			String evaluateur) {
		super();
		this.pourcentage = pourcentage;
		this.progres = progres;
		this.evalue = evalue;
		this.login = login;
		this.libelle_direction = libelle_direction;
		this.structure_ent = structure_ent;
		this.poste_travail = poste_travail;
		this.actionDev = actionDev;
		this.libelle_action_formation = libelle_action_formation;
		this.libelle_action_ori_prof = libelle_action_ori_prof;
		this.libelle_action_disipline = libelle_action_disipline;
		this.libelle_action_mobilite = libelle_action_mobilite;
		this.evaluateur = evaluateur;
	}

	public String getProgres() {
		return progres;
	}

	public void setProgres(String progres) {
		this.progres = progres;
	}

	public String getEvaluateur() {
		return evaluateur;
	}

	public void setEvaluateur(String evaluateur) {
		this.evaluateur = evaluateur;
	}


	
}
