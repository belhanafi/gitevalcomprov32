package administration.bean;

import java.io.Serializable;

public class ClientDBParams implements  Serializable {
	
	private  String login;
	private  String pwd ;
	private  String adresse_ip;
	private  String nom_instance;
	
	
	public ClientDBParams(String login,String pwd ,String adresse_ip,String nom_instance){
		this.login=login;
		this.pwd=pwd;
		this.adresse_ip=adresse_ip;
		this.nom_instance=nom_instance;
		
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getAdresse_ip() {
		return adresse_ip;
	}
	public void setAdresse_ip(String adresse_ip) {
		this.adresse_ip = adresse_ip;
	}
	public String getNom_instance() {
		return nom_instance;
	}
	public void setNom_instance(String nom_instance) {
		this.nom_instance = nom_instance;
	}
	
	
	

}
