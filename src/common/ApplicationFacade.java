package common;

import java.util.ArrayList;
import java.util.HashMap;

import administration.bean.CompteBean;
import administration.bean.CompteEntrepriseDatabaseBean;
import administration.bean.StructureEntrepriseBean;
import common.bean.ArborescenceMenu;

public class ApplicationFacade {

	
	/**
     * Instance static sur la classe --> singleton
     */
    private static ApplicationFacade instance = null;
    
    /**
     * Constructeur prive --> singleton
     */
    private ApplicationFacade() {

    }
    
    

    //variable contenant les informations pour se connecter à la base evalcom associée à une entreprise spécifique
    
    
   private  CompteEntrepriseDatabaseBean compteEntrepriseDatabasebean =new CompteEntrepriseDatabaseBean("","","");
    // variable contenant la structure du menu principale
    private ArborescenceMenu arborescenceMenubean;
    //variable contenant la database id rattachée à l'utilisateur
    private int client_database_id;
    
    //variable contenant des informations relatifs à l'utilisateur connecté
    private CompteBean compteUtilisateur=new CompteBean();
    
    
    private int TimerValue;
    
	private HashMap<String, HashMap<String, Integer>> listDb;
    
    
    
	public HashMap<String, HashMap<String, Integer>> getListDb() {
		return listDb;
	}

	public void setListDb(HashMap<String, HashMap<String, Integer>> listDb) {
		this.listDb = listDb;
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

	public static void setInstance(ApplicationFacade instance) {
		ApplicationFacade.instance = instance;
	}

	public int getClient_database_id() {
		return client_database_id;
	}

	public void setClient_database_id(int client_database_id) {
		this.client_database_id = client_database_id;
	}

	public static ApplicationFacade getInstance() {
        if (instance == null) {
        	ApplicationFacadeSynchornizee();
        }
        return instance;
    }

    public CompteEntrepriseDatabaseBean getCompteEntrepriseDatabasebean() {
		return compteEntrepriseDatabasebean;
	}

	public void setCompteEntrepriseDatabasebean(
			CompteEntrepriseDatabaseBean compteEntrepriseDatabasebean) {
		this.compteEntrepriseDatabasebean = compteEntrepriseDatabasebean;
	}

	public synchronized static void ApplicationFacadeSynchornizee() {
        if (instance == null) {
            instance = new ApplicationFacade();
        }
    }
    
    public ArborescenceMenu getArborescenceMenubean() {
		return arborescenceMenubean;
	}

	public void setArborescenceMenubean(ArborescenceMenu arborescenceMenubean) {
		this.arborescenceMenubean = arborescenceMenubean;
	}
	
	public void resetArguments(){
		
		//this.compteEntrepriseDatabasebean=null;
		this.arborescenceMenubean=null;
		this.client_database_id=0;
		this.compteUtilisateur=null;
	}

	
    
}
