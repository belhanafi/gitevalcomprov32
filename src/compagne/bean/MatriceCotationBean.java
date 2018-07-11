package compagne.bean;

import java.util.ArrayList;
import java.util.List;

public class MatriceCotationBean {
	
	
	private String matricule ;
	private String nom ;
	private String date_naissance;
	private String date_recrutement;
	private String formation;
	private String structure_ent;
	private String intitule_poste;
	private double imi;
	private EmployeFamilleBean famille_cotation ;
	private int ordre;
	private String libelle_direction;
	
	
	


	public MatriceCotationBean(int ordre,String matricule, String nom,
			String date_naissance, String date_recrutement, String formation,
			String structure_ent, String intitule_poste,double imi,EmployeFamilleBean famille_cotation,String libelle_direction) {
		super();
		this.ordre=ordre;
		this.matricule = matricule;
		this.nom = nom;
		this.date_naissance = date_naissance;
		this.date_recrutement = date_recrutement;
		this.formation = formation;
		this.structure_ent = structure_ent;
		this.intitule_poste = intitule_poste;
		this.imi=imi;
		this.famille_cotation=famille_cotation;
		this.libelle_direction=libelle_direction;
	}
	
	

	public int getOrdre() {
		return ordre;
	}



	public void setOrdre(int ordre) {
		this.ordre = ordre;
	}	



	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getDate_naissance() {
		return date_naissance;
	}
	public void setDate_naissance(String date_naissance) {
		this.date_naissance = date_naissance;
	}
	public String getDate_recrutement() {
		return date_recrutement;
	}
	public void setDate_recrutement(String date_recrutement) {
		this.date_recrutement = date_recrutement;
	}
	public String getFormation() {
		return formation;
	}
	public void setFormation(String formation) {
		this.formation = formation;
	}
	public String getStructure_ent() {
		return structure_ent;
	}
	public void setStructure_ent(String structure_ent) {
		this.structure_ent = structure_ent;
	}
	public String getIntitule_poste() {
		return intitule_poste;
	}
	public void setIntitule_poste(String intitule_poste) {
		this.intitule_poste = intitule_poste;
	}

	/*famille=rs.getString("famille");
	String moy_par_famille=rs.getString("moy_par_famille");
	String imi=rs.getString("imi");*/
	
	
	public double getImi() {
		return imi;
	}


	public void setImi(double imi) {
		this.imi = imi;
	}


	public void setFamille_cotation(EmployeFamilleBean famille_cotation) {
		this.famille_cotation = famille_cotation;
	}

	public EmployeFamilleBean getFamille_cotation() {
		return famille_cotation;
	}



	public String getLibelle_direction() {
		return libelle_direction;
	}



	public void setLibelle_direction(String libelle_direction) {
		this.libelle_direction = libelle_direction;
	}

	


}
