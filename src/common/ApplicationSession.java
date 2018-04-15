package common;

import java.io.Serializable;
import java.util.HashMap;

import administration.bean.CompteBean;
import administration.bean.CompteEntrepriseDatabaseBean;
import common.bean.ArborescenceMenu;

public class ApplicationSession implements Serializable {


	//variable contenant les informations pour se connecter à la base evalcom associée à une entreprise spécifique


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  CompteEntrepriseDatabaseBean compteEntrepriseDatabasebean =new CompteEntrepriseDatabaseBean("","","");
	// variable contenant la structure du menu principale
	private ArborescenceMenu arborescenceMenubean;
	//variable contenant la database id rattachée à l'utilisateur
	private int client_database_id;

	//variable contenant des informations relatifs à l'utilisateur connecté
	private CompteBean compteUtilisateur=new CompteBean();
	private int TimerValue;
	private HashMap<String, HashMap<String, Integer>> listDb;
    private String code_poste;	
    private int id_employe;
    private int id_compagne;
    
    



	

	public int getId_compagne() {
		return id_compagne;
	}

	public void setId_compagne(int id_compagne) {
		this.id_compagne = id_compagne;
	}

	public int getId_employe() {
		return id_employe;
	}

	public void setId_employe(int id_employe) {
		this.id_employe = id_employe;
	}

	public String getCode_poste() {
		return code_poste;
	}

	public void setCode_poste(String code_poste) {
		this.code_poste = code_poste;
	}

	public int getTimerValue() {
		return TimerValue;
	}

	public void setTimerValue(int timerValue) {
		TimerValue = timerValue;
	}

	public CompteBean getCompteUtilisateur() {
		return compteUtilisateur;
	}

	public void setCompteUtilisateur(CompteBean compteUtilisateur) {
		this.compteUtilisateur = compteUtilisateur;
	}



	public int getClient_database_id() {
		return client_database_id;
	}

	public void setClient_database_id(int client_database_id) {
		this.client_database_id = client_database_id;
	}



	public CompteEntrepriseDatabaseBean getCompteEntrepriseDatabasebean() {
		return compteEntrepriseDatabasebean;
	}

	public void setCompteEntrepriseDatabasebean(
			CompteEntrepriseDatabaseBean compteEntrepriseDatabasebean) {
		this.compteEntrepriseDatabasebean = compteEntrepriseDatabasebean;
	}



	public ArborescenceMenu getArborescenceMenubean() {
		return arborescenceMenubean;
	}

	public void setArborescenceMenubean(ArborescenceMenu arborescenceMenubean) {
		this.arborescenceMenubean = arborescenceMenubean;
	}

	public void resetArguments(){

		//this.compteEntrepriseDatabasebean=null;
		this.arborescenceMenubean.getArborescenceMenu().clear();
		this.arborescenceMenubean=null;
		this.client_database_id=0;
		this.compteUtilisateur=null;
	}

	public HashMap<String, HashMap<String, Integer>> getListDb() {
		return listDb;
	}

	public void setListDb(HashMap<String, HashMap<String, Integer>> listDb) {
		this.listDb = listDb;
	}


}
