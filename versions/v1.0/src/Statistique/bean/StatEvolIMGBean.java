package Statistique.bean;

public class StatEvolIMGBean {
	
	private String date_evol;
	private float img;
	
	public StatEvolIMGBean(String date_evol, float img) {
		super();
		this.date_evol = date_evol;
		this.img = img;
	}
	
	
	
	
	public StatEvolIMGBean() {
		super();
	}




	public String getDate_evol() {
		return date_evol;
	}

	public void setDate_evol(String date_evol) {
		this.date_evol = date_evol;
	}

	public float getImg() {
		return img;
	}

	public void setImg(float img) {
		this.img = img;
	}

	
	
	
	
	
	

}
