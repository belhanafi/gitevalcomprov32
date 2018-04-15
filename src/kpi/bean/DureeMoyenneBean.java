package kpi.bean;

/**
 * @author nbehlouli
 *
 */
public class DureeMoyenneBean {
	
	private String vague;
	private float duree;
	public String getVague() {
		return vague;
	}
	public void setVague(String vague) {
		this.vague = vague;
	}
	public float getDuree() {
		return duree;
	}
	public void setDuree(float duree) {
		this.duree = duree;
	}
	public DureeMoyenneBean(String vague, float duree) {
		super();
		this.vague = vague;
		this.duree = duree;
	}
	public DureeMoyenneBean() {
		super();
	}
	
	
	
	

}
