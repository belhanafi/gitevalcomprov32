package compagne.bean;

public class EmployeFamilleBean {
	
	String operationelles_moy;
	String personnelles_moy;
	String affaires_moy;
	String  matricule;
	String relationelles_moy;
	
	
	
	public EmployeFamilleBean(String matricule,String oPERATIONNELLES_moy,
			String pERSONNELLES_moy, String aFFAIRES_moy, 
			String rELATIONNELLES_moy) {
		super();
		operationelles_moy = oPERATIONNELLES_moy;
		personnelles_moy = pERSONNELLES_moy;
		affaires_moy = aFFAIRES_moy;
		this.matricule = matricule;
		relationelles_moy = rELATIONNELLES_moy;
	}



	public String getOperationelles_moy() {
		return operationelles_moy;
	}


	public void setOperationelles_moy(String operationelles_moy) {
		this.operationelles_moy = operationelles_moy;
	}


	public String getPersonnelles_moy() {
		return personnelles_moy;
	}



	public void setPersonnelles_moy(String personnelles_moy) {
		this.personnelles_moy = personnelles_moy;
	}



	public String getAffaires_moy() {
		return affaires_moy;
	}



	public void setAffaires_moy(String affaires_moy) {
		this.affaires_moy = affaires_moy;
	}



	public String getMatricule() {
		return matricule;
	}



	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}



	public String getRelationelles_moy() {
		return relationelles_moy;
	}



	public void setRelationelles_moy(String relationelles_moy) {
		this.relationelles_moy = relationelles_moy;
	}
	
	
	
	
	
	
	
	
	
	
	

}
