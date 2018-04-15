package administration.bean;

import java.io.Serializable;
import java.util.Date;

public class CompagneCreationBean implements Serializable {
	
	private String nom_compagne;
	private String type_compagne;
	private Date date_deb_comp;
	private Date date_fin_comp;
	private int id_compagne_type;
	private int id_compagne;
	



	public CompagneCreationBean(String nom_compagne, String type_compagne,
			Date date_deb_comp, Date date_fin_comp, int id_compagne_type,
			int id_compagne) {
		super();
		this.nom_compagne = nom_compagne;
		this.type_compagne = type_compagne;
		this.date_deb_comp = date_deb_comp;
		this.date_fin_comp = date_fin_comp;
		this.id_compagne_type = id_compagne_type;
		this.id_compagne = id_compagne;
	}



	public CompagneCreationBean() {
		super();
	}


	
	public int getId_compagne() {
		return id_compagne;
	}

	public void setId_compagne(int id_compagne) {
		this.id_compagne = id_compagne;
	}

	public int getId_compagne_type() {
		return id_compagne_type;
	}

	public void setId_compagne_type(int id_compagne_type) {
		this.id_compagne_type = id_compagne_type;
	}
	
	public String getNom_compagne() {
		return nom_compagne;
	}

	public void setNom_compagne(String nom_compagne) {
		this.nom_compagne = nom_compagne;
	}

	public Date getDate_deb_comp() {
		return date_deb_comp;
	}

	public void setDate_deb_comp(Date date_deb_comp) {
		this.date_deb_comp = date_deb_comp;
	}

	public Date getDate_fin_comp() {
		return date_fin_comp;
	}

	public void setDate_fin_comp(Date date_fin_comp) {
		this.date_fin_comp = date_fin_comp;
	}

	public String getType_compagne() {
		return type_compagne;
	}

	public void setType_compagne(String type_compagne) {
		this.type_compagne = type_compagne;
	}
	
	
	
	
	 
}