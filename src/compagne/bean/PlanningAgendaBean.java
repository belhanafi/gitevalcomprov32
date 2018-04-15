package compagne.bean;

import java.util.Date;

public class PlanningAgendaBean {

	private String  nomevaluateur;
	private String  prenomevaluateur;
	private String  email;
	private String  nomevalue;
	private String  prenomevalue;
	private Date    date_evaluation;
	private String  heure_debut_evaluation ;
	private String  heure_fin_evaluation;
	private String  lieu;
	private String personne_ressources;
	private int  id_evaluateur;
	private String login;
	private String pwd;
	private Date  date_fin_evaluation;


	public PlanningAgendaBean() {
		super();
	}



	public PlanningAgendaBean(String nomevaluateur, String prenomevaluateur,
			String email, String nomevalue, String prenomevalue,
			Date date_evaluation, String heure_debut_evaluation,
			String heure_fin_evaluation, String lieu,
			String personne_ressources, int id_evaluateur, String login,
			String pwd,Date  date_fin_evaluation) {
		super();
		this.nomevaluateur = nomevaluateur;
		this.prenomevaluateur = prenomevaluateur;
		this.email = email;
		this.nomevalue = nomevalue;
		this.prenomevalue = prenomevalue;
		this.date_evaluation = date_evaluation;
		this.heure_debut_evaluation = heure_debut_evaluation;
		this.heure_fin_evaluation = heure_fin_evaluation;
		this.lieu = lieu;
		this.personne_ressources = personne_ressources;
		this.id_evaluateur = id_evaluateur;
		this.login = login;
		this.pwd = pwd;
		this.date_fin_evaluation=date_fin_evaluation;
	}



	public int getId_evaluateur() {
		return id_evaluateur;
	}


	public void setId_evaluateur(int id_evaluateur) {
		this.id_evaluateur = id_evaluateur;
	}


	public String getNomevaluateur() {
		return nomevaluateur;
	}
	public void setNomevaluateur(String nomevaluateur) {
		this.nomevaluateur = nomevaluateur;
	}
	public String getPrenomevaluateur() {
		return prenomevaluateur;
	}
	public void setPrenomevaluateur(String prenomevaluateur) {
		this.prenomevaluateur = prenomevaluateur;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNomevalue() {
		return nomevalue;
	}
	public void setNomevalue(String nomevalue) {
		this.nomevalue = nomevalue;
	}
	public String getPrenomevalue() {
		return prenomevalue;
	}
	public void setPrenomevalue(String prenomevalue) {
		this.prenomevalue = prenomevalue;
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



	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}



	public Date getDate_fin_evaluation() {
		return date_fin_evaluation;
	}


	public void setDate_fin_evaluation(Date date_fin_evaluation) {
		this.date_fin_evaluation = date_fin_evaluation;
	}





}
