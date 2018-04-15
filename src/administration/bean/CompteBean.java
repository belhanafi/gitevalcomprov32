package administration.bean;
import java.io.Serializable;
import java.util.Date;
public class CompteBean implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id_compte; 
	private int id_profile;
	private String login;
	private String pwd;
	private int database_id;
	private Date val_date_deb;
	private Date val_date_fin; 
	private String nom_base;

	
	//cette variable est utilisé dans la fiche d'evaluation
	private int id_employe;


	public CompteBean()
	{

	}
	public CompteBean ( int id_compte,int id_profile,
			String login,String pwd,int database_id, Date val_date_deb,
			Date val_date_fin,String nom_base) {	


		this.id_compte=id_compte;
		this.id_profile=id_profile;
		this.login=login;
		this.pwd=pwd;
		this.database_id=database_id;
		this.val_date_deb=val_date_deb;
		this.val_date_fin=val_date_fin;
		this.nom_base=nom_base;

	}

	public int getId_compte() {
		return id_compte;
	}
	public void setId_compte(int id_compte) {
		this.id_compte = id_compte;
	}
	public int getId_profile() {
		return id_profile;
	}
	public void setId_profile(int id_profile) {
		this.id_profile = id_profile;
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
	public int getDatabase_id() {
		return database_id;
	}
	public void setDatabase_id(int database_id) {
		this.database_id = database_id;
	}
	public Date getVal_date_deb() {
		return val_date_deb;
	}
	public void setVal_date_deb(Date val_date_deb) {
		this.val_date_deb = val_date_deb;
	}
	public Date getVal_date_fin() {
		return val_date_fin;
	}
	public void setVal_date_fin(Date val_date_fin) {
		this.val_date_fin = val_date_fin;
	}
	public int getId_employe() {
		return id_employe;
	}
	public void setId_employe(int id_employe) {
		this.id_employe = id_employe;
	}
	
	public String getNom_base() {
		return nom_base;
	}
	public void setNom_base(String nom_base) {
		this.nom_base = nom_base;
	}

}
