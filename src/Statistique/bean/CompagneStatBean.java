package Statistique.bean;

public class CompagneStatBean {
	
	private int id_compagne;
	private String lbl_compagne;
	
	
	
	
	public CompagneStatBean() {
		super();
	}


	public CompagneStatBean(int id_compagne, String lbl_compagne) {
		super();
		this.id_compagne = id_compagne;
		this.lbl_compagne = lbl_compagne;
	}
	
	
	public int getId_compagne() {
		return id_compagne;
	}
	public void setId_compagne(int id_compagne) {
		this.id_compagne = id_compagne;
	}
	public String getLbl_compagne() {
		return lbl_compagne;
	}
	public void setLbl_compagne(String lbl_compagne) {
		this.lbl_compagne = lbl_compagne;
	}
	
	

}
