package kpi.bean;

public class ListAdminRHBean {
	
	private String nomcomplet;
	private String email;
	
	
	
	public ListAdminRHBean() {
		super();
	}

	public ListAdminRHBean(String nomcomplet, String email) {
		super();
		this.nomcomplet = nomcomplet;
		this.email = email;
	}
	
	
	public String getNomcomplet() {
		return nomcomplet;
	}
	public void setNomcomplet(String nomcomplet) {
		this.nomcomplet = nomcomplet;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	

}
