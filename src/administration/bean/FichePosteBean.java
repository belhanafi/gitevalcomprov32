package administration.bean;

import java.io.Serializable;
import java.util.Date;

public class FichePosteBean implements  Serializable {


	private String code_poste;
	private String intitule_poste;
	private String sommaire_poste;
	private String tache_responsabilite;
	private String environement_perspectif;
	private String formation_general;
	private String formation_professionnelle ;
	private String experience;
	private String profile_poste;
	private String code_poste_hierarchie;
	private String code_structure;
	private Date  date_maj_poste;
	private String libelle_formation;
	private String libelle_poste;
	private String is_cadre;
	private String code_gsp;
	private	String codeFormationNiv;
	private String hierarchie;
	private String codeStructLibelle;
	private String causeRejet;
	private String libelle_structure;


	
	
	
	public FichePosteBean(String code_poste, String intitule_poste,
			String sommaire_poste, String tache_responsabilite,
			String environement_perspectif, String formation_general,
			String formation_professionnelle, String experience,
			String profile_poste, String code_poste_hierarchie,
			String code_structure, Date date_maj_poste,
			String libelle_formation, String libelle_poste, String is_cadre,
			String code_gsp, String codeFormationNiv, String hierarchie,
			String codeStructLibelle, String causeRejet,
			String libelle_structure, String code_formation,
			String niv_formation) {
		super();
		this.code_poste = code_poste;
		this.intitule_poste = intitule_poste;
		this.sommaire_poste = sommaire_poste;
		this.tache_responsabilite = tache_responsabilite;
		this.environement_perspectif = environement_perspectif;
		this.formation_general = formation_general;
		this.formation_professionnelle = formation_professionnelle;
		this.experience = experience;
		this.profile_poste = profile_poste;
		this.code_poste_hierarchie = code_poste_hierarchie;
		this.code_structure = code_structure;
		this.date_maj_poste = date_maj_poste;
		this.libelle_formation = libelle_formation;
		this.libelle_poste = libelle_poste;
		this.is_cadre = is_cadre;
		this.code_gsp = code_gsp;
		this.codeFormationNiv = codeFormationNiv;
		this.hierarchie = hierarchie;
		this.codeStructLibelle = codeStructLibelle;
		this.causeRejet = causeRejet;
		this.libelle_structure = libelle_structure;
		this.code_formation = code_formation;
		this.niv_formation = niv_formation;
	}



	public String getCode_poste_hierarchie() {
		return code_poste_hierarchie;
	}



	public void setCode_poste_hierarchie(String code_poste_hierarchie) {
		this.code_poste_hierarchie = code_poste_hierarchie;
	}



	public String getLibelle_structure() {
		return libelle_structure;
	}



	public void setLibelle_structure(String libelle_structure) {
		this.libelle_structure = libelle_structure;
	}



	public String getCauseRejet() {
		return causeRejet;
	}



	public void setCauseRejet(String causeRejet) {
		this.causeRejet = causeRejet;
	}



	public String getCodeStructLibelle() {
		return codeStructLibelle;
	}



	public void setCodeStructLibelle(String codeStructLibelle) {
		this.codeStructLibelle = codeStructLibelle;
	}



	public String getHierarchie() {
		return hierarchie;
	}



	public void setHierarchie(String hierarchie) {
		this.hierarchie = hierarchie;
	}



	public String getCodeFormationNiv() {
		return codeFormationNiv;
	}



	public void setCodeFormationNiv(String codeFormationNiv) {
		this.codeFormationNiv = codeFormationNiv;
	}









	private String code_formation;
	private String niv_formation;

	public String getCode_formation() {
		return code_formation;
	}



	public void setCode_formation(String code_formation) {
		this.code_formation = code_formation;
	}



	public String getNiv_formation() {
		return niv_formation;
	}



	public void setNiv_formation(String niv_formation) {
		this.niv_formation = niv_formation;
	}



	public FichePosteBean(){

	}



	public FichePosteBean(String code_poste, String intitule_poste,
			String sommaire_poste, String tache_responsabilite,
			String environement_perspectif, String formation_general,
			String formation_professionnelle, String experience,
			String profile_poste, String code_poste_hierarchie,
			String code_structure, Date date_maj_poste,
			String libelle_formation, String libelle_poste, String is_cadre,
			String code_gsp) {
		super();
		this.code_poste = code_poste;
		this.intitule_poste = intitule_poste;
		this.sommaire_poste = sommaire_poste;
		this.tache_responsabilite = tache_responsabilite;
		this.environement_perspectif = environement_perspectif;
		this.formation_general = formation_general;
		this.formation_professionnelle = formation_professionnelle;
		this.experience = experience;
		this.profile_poste = profile_poste;
		this.code_poste_hierarchie = code_poste_hierarchie;
		this.code_structure = code_structure;
		this.date_maj_poste = date_maj_poste;
		this.libelle_formation = libelle_formation;
		this.libelle_poste = libelle_poste;
		this.is_cadre = is_cadre;
		this.code_gsp = code_gsp;
	}









	public String getIs_cadre() {
		return is_cadre;
	}




	public void setIs_cadre(String is_cadre) {
		this.is_cadre = is_cadre;
	}




	public String getLibelle_poste() {
		return libelle_poste;
	}



	public void setLibelle_poste(String libelle_poste) {
		this.libelle_poste = libelle_poste;
	}



	public String getCode_poste() {
		return code_poste;
	}

	public void setCode_poste(String code_poste) {
		this.code_poste = code_poste;
	}

	public String getIntitule_poste() {
		return intitule_poste;
	}

	public void setIntitule_poste(String intitule_poste) {
		this.intitule_poste = intitule_poste;
	}

	public String getSommaire_poste() {
		return sommaire_poste;
	}

	public void setSommaire_poste(String sommaire_poste) {
		this.sommaire_poste = sommaire_poste;
	}

	public String getTache_responsabilite() {
		return tache_responsabilite;
	}

	public void setTache_responsabilite(String tache_responsabilite) {
		this.tache_responsabilite = tache_responsabilite;
	}

	public String getEnvironement_perspectif() {
		return environement_perspectif;
	}

	public void setEnvironement_perspectif(String environement_perspectif) {
		this.environement_perspectif = environement_perspectif;
	}

	public String getFormation_general() {
		return formation_general;
	}

	public void setFormation_general(String formation_general) {
		this.formation_general = formation_general;
	}

	public String getFormation_professionnelle() {
		return formation_professionnelle;
	}

	public void setFormation_professionnelle(String formation_professionnelle) {
		this.formation_professionnelle = formation_professionnelle;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getProfile_poste() {
		return profile_poste;
	}

	public void setProfile_poste(String profile_poste) {
		this.profile_poste = profile_poste;
	}

	public String getPoste_hierarchie() {
		return code_poste_hierarchie;
	}

	public void setPoste_hierarchie(String code_poste_hierarchie) {
		this.code_poste_hierarchie = code_poste_hierarchie;
	}

	public String getCode_structure() {
		return code_structure;
	}

	public void setCode_structure(String code_structure) {
		this.code_structure = code_structure;
	}

	public Date getDate_maj_poste() {
		return date_maj_poste;
	}

	public void setDate_maj_poste(Date date_maj_poste) {
		this.date_maj_poste = date_maj_poste;
	}

	public String getLibelle_formation() {
		return libelle_formation;
	}

	public void setLibelle_formation(String libelle_formation) {
		this.libelle_formation = libelle_formation;
	}









	public String getCode_gsp() {
		return code_gsp;
	}









	public void setCode_gsp(String code_gsp) {
		this.code_gsp = code_gsp;
	}






}
