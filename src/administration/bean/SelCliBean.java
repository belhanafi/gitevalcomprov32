package administration.bean;



public class SelCliBean {
	
	private int  secteur_id;
	private String secteur;

	public  SelCliBean(){
		
	}
	
	public  SelCliBean (int  secteur_id,String secteur) {
		this.secteur_id=secteur_id;
		this.secteur=secteur;
		
	}
	public String getSecteur() {
		return secteur;
	}

	public void setSecteur(String secteur) {
		this.secteur = secteur;
	}
	public int getSecteur_id() {
		return secteur_id;
	}
	public void setSecteur_id(int secteur_id) {
		this.secteur_id = secteur_id;
	}

	
	

}
