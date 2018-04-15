package administration.bean;

public class DroitsAccessBean {
	
	 private String code_ecran;
	 private String libelle_menu;
	 private String libelle_ecran;
	 private String hide;
	 private String ecriture;
	 private String lecture;
	 
	 
	 
	public DroitsAccessBean() {
		super();
	}


	public DroitsAccessBean(String code_ecran, String libelle_menu,
			String libelle_ecran, String hide, String ecriture, String lecture) {
		super();
		this.code_ecran = code_ecran;
		this.libelle_menu = libelle_menu;
		this.libelle_ecran = libelle_ecran;
		this.hide = hide;
		this.ecriture = ecriture;
		this.lecture = lecture;
	}
	
	
	public String getCode_ecran() {
		return code_ecran;
	}
	public void setCode_ecran(String code_ecran) {
		this.code_ecran = code_ecran;
	}
	public String getLibelle_menu() {
		return libelle_menu;
	}
	public void setLibelle_menu(String libelle_menu) {
		this.libelle_menu = libelle_menu;
	}
	public String getLibelle_ecran() {
		return libelle_ecran;
	}
	public void setLibelle_ecran(String libelle_ecran) {
		this.libelle_ecran = libelle_ecran;
	}
	public String getHide() {
		return hide;
	}
	public void setHide(String hide) {
		this.hide = hide;
	}
	public String getEcriture() {
		return ecriture;
	}
	public void setEcriture(String ecriture) {
		this.ecriture = ecriture;
	}
	public String getLecture() {
		return lecture;
	}
	public void setLecture(String lecture) {
		this.lecture = lecture;
	}
	 
	 
 

}
