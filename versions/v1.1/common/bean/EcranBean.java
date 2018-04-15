package common.bean;

import java.io.Serializable;

public class EcranBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id_ecran;
	private String code_ecran;
	private String libelle_ecran;
	private String code_menu;
	private String libelle_menu;
	
	
	public EcranBean(int id_ecran, String code_ecran,String libelle_ecran, String code_menu, String libelle_menu)
	{
		this.id_ecran=id_ecran;
		this.code_ecran=code_ecran;
		this.libelle_menu=libelle_menu;
		this.code_menu=code_menu;
		this.libelle_ecran=libelle_ecran;
	}
	public int getId_ecran() {
		return id_ecran;
	}
	public void setId_ecran(int id_ecran) {
		this.id_ecran = id_ecran;
	}
	public String getCode_ecran() {
		return code_ecran;
	}
	public void setCode_ecran(String code_ecran) {
		this.code_ecran = code_ecran;
	}
	public String getLibelle_ecran() {
		return libelle_ecran;
	}
	public void setLibelle_ecran(String libelle_ecran) {
		this.libelle_ecran = libelle_ecran;
	}
	public String getCode_menu() {
		return code_menu;
	}
	public void setCode_menu(String code_menu) {
		this.code_menu = code_menu;
	}
	public String getLibele_menu() {
		return libelle_menu;
	}
	public void setLibele_menu(String libele_menu) {
		this.libelle_menu = libele_menu;
	}
	
	

}
