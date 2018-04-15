package compagne.bean;

public class SuiviCompStatutFicheBean {
	
	private String evalue;
	private int id_employe;
	private String statut;
	private String date_fin_eval;
	private String heure_fin_eval;
	private String fiche_valide;


	public SuiviCompStatutFicheBean(String evalue, int id_employe,
			String statut, String date_fin_eval, String heure_fin_eval,
			String fiche_valide) {
		super();
		this.evalue = evalue;
		this.id_employe = id_employe;
		this.statut = statut;
		this.date_fin_eval = date_fin_eval;
		this.heure_fin_eval = heure_fin_eval;
		this.fiche_valide = fiche_valide;
		
	}
	public SuiviCompStatutFicheBean() {
		// TODO Auto-generated constructor stub
	}
	public String getEvalue() {
		return evalue;
	}
	public void setEvalue(String evalue) {
		this.evalue = evalue;
	}
	public int getId_employe() {
		return id_employe;
	}
	public void setId_employe(int id_employe) {
		this.id_employe = id_employe;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getDate_fin_eval() {
		return date_fin_eval;
	}

	public void setDate_fin_eval(String date_fin_eval) {
		this.date_fin_eval = date_fin_eval;
	}


	public String getHeure_fin_eval() {
		return heure_fin_eval;
	}


	public void setHeure_fin_eval(String heure_fin_eval) {
		this.heure_fin_eval = heure_fin_eval;
	}
	
	
	public String getFiche_valide() {
		return fiche_valide;
	}
	public void setFiche_valide(String fiche_valide) {
		this.fiche_valide = fiche_valide;
	}
	
	
	
	
	

}
