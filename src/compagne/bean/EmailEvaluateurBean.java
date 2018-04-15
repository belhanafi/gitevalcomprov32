package compagne.bean;

public class EmailEvaluateurBean {
	
	private int id_employe;
	private String email;
	private int pourcentage;
	private String nomevaluateur;

	
	
	
	public EmailEvaluateurBean(int id_employe, String email) {
		super();
		this.id_employe = id_employe;
		this.email = email;
	}
	
	
	public EmailEvaluateurBean() {
		super();
	}




	

	public String getNomevaluateur() {
		return nomevaluateur;
	}


	public void setNomevaluateur(String nomevaluateur) {
		this.nomevaluateur = nomevaluateur;
	}


	public EmailEvaluateurBean(int id_employe, String email, int pourcentage,
			String nomevaluateur) {
		super();
		this.id_employe = id_employe;
		this.email = email;
		this.pourcentage = pourcentage;
		this.nomevaluateur = nomevaluateur;
	}


	public int getPourcentage() {
		return pourcentage;
	}


	public void setPourcentage(int pourcentage) {
		this.pourcentage = pourcentage;
	}


	public int getId_employe() {
		return id_employe;
	}
	public void setId_employe(int id_employe) {
		this.id_employe = id_employe;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	

}
