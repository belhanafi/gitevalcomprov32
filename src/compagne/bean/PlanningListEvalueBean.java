package compagne.bean;

public class PlanningListEvalueBean {
	
	private int id_employe;
	private String nom_employe;
	
	
		
	public PlanningListEvalueBean(int id_employe, String nom_employe) {
		super();
		this.id_employe = id_employe;
		this.nom_employe = nom_employe;
	}
	
	
	
	
	public PlanningListEvalueBean() {
		super();
	}




	public int getId_employe() {
		return id_employe;
	}
	public void setId_employe(int id_employe) {
		this.id_employe = id_employe;
	}
	public String getNom_employe() {
		return nom_employe;
	}
	public void setNom_employe(String nom_employe) {
		this.nom_employe = nom_employe;
	}

}
