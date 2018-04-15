package Statistique.bean;

public class StatMoyFamillePosteBean {
	
	private String famille;
	private float  moy_famille;
	private float  img;
	private String intitule_poste;
	
	
	
	
	
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



	public float getImg() {
		return img;
	}



	public void setImg(float img) {
		this.img = img;
	}



	public String getIntitule_poste() {
		return intitule_poste;
	}



	public void setIntitule_poste(String intitule_poste) {
		this.intitule_poste = intitule_poste;
	}
	
	
	
	

}
