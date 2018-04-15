package compagne.bean;

public class PlanningListEvaluateurBean {

	private int id_evaluateur;
    private String evaluateur;
    
    
    
    
	public PlanningListEvaluateurBean() {
		super();
	}


	public PlanningListEvaluateurBean(int id_evaluateur, String evaluateur) {
		super();
		this.id_evaluateur = id_evaluateur;
		this.evaluateur = evaluateur;
	}
	
	
	public int getId_evaluateur() {
		return id_evaluateur;
	}
	public void setId_evaluateur(int id_evaluateur) {
		this.id_evaluateur = id_evaluateur;
	}
	public String getEvaluateur() {
		return evaluateur;
	}
	public void setEvaluateur(String evaluateur) {
		this.evaluateur = evaluateur;
	}
    
    
	
	
	
	

}
