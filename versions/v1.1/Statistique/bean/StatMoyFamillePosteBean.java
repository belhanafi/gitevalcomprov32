package Statistique.bean;

public class StatMoyFamillePosteBean {
	
	private String famille;
	private float  moy_famille;
	
	
	
	
	
	public StatMoyFamillePosteBean() {
		super();
	}



	public StatMoyFamillePosteBean(String famille, float moy_famille) {
		super();
		this.famille = famille;
		this.moy_famille = moy_famille;
	}
	
	
	
	public String getFamille() {
		return famille;
	}
	public void setFamille(String famille) {
		this.famille = famille;
	}
	public float getMoy_famille() {
		return moy_famille;
	}
	public void setMoy_famille(float moy_famille) {
		this.moy_famille = moy_famille;
	}
	
	
	

}
