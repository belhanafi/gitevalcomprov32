package administration.bean;

public class ProfileDroitsAccessBean {
	
	private int id_profile;
	private String libelle_profile;
	
	
	
	public ProfileDroitsAccessBean() {
		super();
	}
	public ProfileDroitsAccessBean(int id_profile, String libelle_profile) {
		super();
		this.id_profile = id_profile;
		this.libelle_profile = libelle_profile;
	}
	public int getId_profile() {
		return id_profile;
	}
	public void setId_profile(int id_profile) {
		this.id_profile = id_profile;
	}
	public String getLibelle_profile() {
		return libelle_profile;
	}
	public void setLibelle_profile(String libelle_profile) {
		this.libelle_profile = libelle_profile;
	}
	
	
	
	
	

}
