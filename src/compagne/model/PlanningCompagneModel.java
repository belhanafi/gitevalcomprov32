package compagne.model;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;

import administration.bean.AdministrationLoginBean;
import administration.bean.CompagneCreationBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.StructureEntrepriseBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import common.ApplicationFacade;
import common.ApplicationSession;
import common.CreateDatabaseCon;
import common.InitContext;
import common.PwdCrypt;
import compagne.bean.CompagneListBean;
import compagne.bean.EmailEvaluateurBean;
import compagne.bean.PlanningAgendaBean;
import compagne.bean.PlanningCompagneListBean;
import compagne.bean.PlanningListEvaluateurBean;
import compagne.bean.PlanningListEvalueBean;
import compagne.bean.SuiviCompagneBean;

public class PlanningCompagneModel {


	private ArrayList<PlanningCompagneListBean>  listcompagne =null; 
	private ListModel strset =null;
	private int database;//=ApplicationFacade.getInstance().getClient_database_id();

	/**
	 * cette méthode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	
	
	public List loadPlanningCompagnelist(int id_compagne) throws SQLException{


		listcompagne = new ArrayList<PlanningCompagneListBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sel_comp="";
		try {
			stmt = (Statement) conn.createStatement();
			
			if (id_compagne==-1){
				
				 sel_comp="select id_planning_evaluation,libelle_compagne ,e.id_employe as employe_id, (select concat(nom,' ', prenom) from employe where id_employe in(id_evaluateur)) as evaluateur,concat (nom,' ', prenom) as evalue,intitule_poste,t.structure_ent libelle_structure,"
						+  " date_evaluation,heure_debut_evaluation,date_fin_evaluation,heure_fin_evaluation,lieu,"
						+ " personne_ressources,p.id_evaluateur ,p.id_employe id_evalue,p.id_compagne"
						+ " from planning_evaluation p,employe e,compagne_evaluation c, poste_travail_description d ,"
						+ " (select code_structure, structure_ent from ("
						+ "  					 select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
						+ "						 and  libelle_section !='null' and  libelle_section !=''"
						+ "						 union"
						+ "						 select code_structure,libelle_service structure_ent from structure_entreprise"
						+ "						 where libelle_service is  not null"
						+ " 				     and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
						+ "    					 union"
						+ "						 select code_structure,libelle_departement structure_ent from structure_entreprise"
						+ "						 where libelle_departement is  not null"
						+ " 					  and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
						+ "						 union"
						+ "						 select code_structure,libelle_sous_direction structure_ent from structure_entreprise"
						+ "						 where libelle_sous_direction is  not null"
						+ " 					  and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
						+ "						 union"
						+ "						 select code_structure,libelle_unite structure_ent from structure_entreprise"
						+ "						 where libelle_unite is  not null"
						+ " 					  and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
						+ "						  and length(libelle_service)=0 and  length(libelle_section) =0"
						+ " 					 union"
						+ "						 select code_structure,libelle_direction structure_ent from structure_entreprise"
						+ "						 where libelle_direction is  not null"
						+ " 					  and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
						+ "						  and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise  ) t"
						+ "						 where   p.id_compagne=c.id_compagne and   p.id_employe=e.id_employe"
						+ "	                     and   p.code_poste=d.code_poste and e.code_structure=t.code_structure and e.code_structure=p.code_structure order by evaluateur";

			}else{
				
				 sel_comp="select id_planning_evaluation,libelle_compagne ,e.id_employe as employe_id, (select concat(nom,' ', prenom) from employe where id_employe in(id_evaluateur)) as evaluateur,concat (nom,' ', prenom) as evalue,intitule_poste,t.structure_ent libelle_structure,"
							+  " date_evaluation,heure_debut_evaluation,date_fin_evaluation,heure_fin_evaluation,lieu,"
							+ " personne_ressources,p.id_evaluateur ,p.id_employe id_evalue,p.id_compagne"
							+ " from planning_evaluation p,employe e,compagne_evaluation c, poste_travail_description d ,"
							+ " (select code_structure, structure_ent from ("
							+ "  					 select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
							+ "						 and  libelle_section !='null' and  libelle_section !=''"
							+ "						 union"
							+ "						 select code_structure,libelle_service structure_ent from structure_entreprise"
							+ "						 where libelle_service is  not null"
							+ " 				     and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
							+ "    					 union"
							+ "						 select code_structure,libelle_departement structure_ent from structure_entreprise"
							+ "						 where libelle_departement is  not null"
							+ " 					  and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
							+ "						 union"
							+ "						 select code_structure,libelle_sous_direction structure_ent from structure_entreprise"
							+ "						 where libelle_sous_direction is  not null"
							+ " 					  and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
							+ "						 union"
							+ "						 select code_structure,libelle_unite structure_ent from structure_entreprise"
							+ "						 where libelle_unite is  not null"
							+ " 					  and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
							+ "						  and length(libelle_service)=0 and  length(libelle_section) =0"
							+ " 					 union"
							+ "						 select code_structure,libelle_direction structure_ent from structure_entreprise"
							+ "						 where libelle_direction is  not null"
							+ " 					  and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
							+ "						  and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise  ) t"
							+ "						 where   p.id_compagne=c.id_compagne and   p.id_employe=e.id_employe and c.id_compagne=#id_compagne"
							+ "	                     and   p.code_poste=d.code_poste and e.code_structure=t.code_structure and e.code_structure=p.code_structure order by evaluateur";
				 
				 sel_comp = sel_comp.replaceAll("#id_compagne", "'"+ id_compagne+"'");
				
			}
			

			rs = (ResultSet) stmt.executeQuery(sel_comp);

			while(rs.next()){

				PlanningCompagneListBean compagne=new PlanningCompagneListBean();
				compagne.setId_planning_evaluation((rs.getInt("id_planning_evaluation")));	
				compagne.setLibelle_compagne((rs.getString("libelle_compagne")));
				compagne.setEvaluateur((rs.getString("evaluateur")));
				compagne.setEvalue(((rs.getString("evalue"))));
				compagne.setIntitule_poste((((rs.getString("intitule_poste")))));
				compagne.setLibelle_structure((rs.getString("libelle_structure")));
				compagne.setDate_evaluation(rs.getDate("date_evaluation"));
				compagne.setHeure_debut_evaluation(rs.getString("heure_debut_evaluation"));
				compagne.setHeure_fin_evaluation(rs.getString("heure_fin_evaluation"));
				compagne.setLieu(rs.getString("lieu"));
				compagne.setPersonne_ressources(rs.getString("personne_ressources"));
				compagne.setId_evalue(rs.getInt("employe_id"));
				compagne.setDate_fin_evaluation(rs.getDate("date_fin_evaluation"));
				compagne.setId_evaluateur(rs.getInt("id_evaluateur"));
				compagne.setId_evalue(rs.getInt("id_evalue"));
				compagne.setId_compagne(rs.getInt("id_compagne"));
				
				listcompagne.add(compagne);

			}
		

		} catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return listcompagne;



	}
	/*public List loadPlanningCompagnelist() throws SQLException{


		listcompagne = new ArrayList<PlanningCompagneListBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select id_planning_evaluation,libelle_compagne ,e.id_employe as employe_id, (select concat(nom,' ', prenom) from employe where id_employe in(id_evaluateur)) as evaluateur,concat (nom,' ', prenom) as evalue,intitule_poste,p.code_structure," +
					" date_evaluation,heure_debut_evaluation,date_fin_evaluation,heure_fin_evaluation,lieu,personne_ressources,p.id_evaluateur ,p.id_employe id_evalue,p.id_compagne " +
					" from planning_evaluation p,employe e,compagne_evaluation c, poste_travail_description d " +
					" where   p.id_compagne=c.id_compagne and   p.id_employe=e.id_employe and   p.code_poste=d.code_poste";

			rs = (ResultSet) stmt.executeQuery(sel_comp);

			while(rs.next()){

				PlanningCompagneListBean compagne=new PlanningCompagneListBean();
				compagne.setId_planning_evaluation((rs.getInt("id_planning_evaluation")));	
				compagne.setLibelle_compagne((rs.getString("libelle_compagne")));
				compagne.setEvaluateur((rs.getString("evaluateur")));
				compagne.setEvalue(((rs.getString("evalue"))));
				compagne.setIntitule_poste((((rs.getString("intitule_poste")))));
				compagne.setCode_structure((rs.getString("code_structure")));
				compagne.setDate_evaluation(rs.getDate("date_evaluation"));
				compagne.setHeure_debut_evaluation(rs.getString("heure_debut_evaluation"));
				compagne.setHeure_fin_evaluation(rs.getString("heure_fin_evaluation"));
				compagne.setLieu(rs.getString("lieu"));
				compagne.setPersonne_ressources(rs.getString("personne_ressources"));
				compagne.setId_evalue(rs.getInt("employe_id"));
				compagne.setDate_fin_evaluation(rs.getDate("date_fin_evaluation"));
				compagne.setId_evaluateur(rs.getInt("id_evaluateur"));
				compagne.setId_evalue(rs.getInt("id_evalue"));
				compagne.setId_compagne(rs.getInt("id_compagne"));

				listcompagne.add(compagne);

			}
			//			stmt.close();
			//			conn.close();

		} catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return listcompagne;



	}*/

	/**
	 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
	 * @param addedData
	 * @return
	 * @throws ParseException 
	 */
	public boolean addPlanningCompagne(PlanningCompagneListBean addedData) throws ParseException
	{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		boolean retour=false;

		try 
		{

			stmt = (Statement) conn.createStatement();
			String sql_query="INSERT INTO planning_evaluation(id_compagne, id_employe, date_evaluation, id_evaluateur, heure_debut_evaluation, heure_fin_evaluation, lieu, code_poste, personne_ressources, code_structure,date_fin_evaluation)" +
					" VALUES(#id_compagne,#id_employe, #date_evaluation, #id_evaluateur, #heure_debut_evaluation, #heure_fin_evaluation, #lieu, #code_poste, #personne_ressources, #code_structure,#date_fin_evaluation);";

			sql_query = sql_query.replaceAll("#id_compagne", "'"+ addedData.getId_compagne()+"'");
			sql_query = sql_query.replaceAll("#id_employe", "'"+ addedData.getId_evalue()+"'");
			sql_query = sql_query.replaceAll("#date_evaluation", "'"+ formatter.format(addedData.getDate_evaluation())+"'");
			sql_query = sql_query.replaceAll("#id_evaluateur", "'"+ addedData.getId_evaluateur()+"'");
			sql_query = sql_query.replaceAll("#heure_debut_evaluation", "'"+ addedData.getHeure_debut_evaluation()+"'");
			sql_query = sql_query.replaceAll("#heure_fin_evaluation", "'"+ addedData.getHeure_fin_evaluation()+"'");
			sql_query = sql_query.replaceAll("#lieu", "'"+ addedData.getLieu()+"'");
			sql_query = sql_query.replaceAll("#code_poste", "'"+ addedData.getCode_poste()+"'");
			sql_query = sql_query.replaceAll("#personne_ressources", "'"+ addedData.getPersonne_ressources()+"'");
			sql_query = sql_query.replaceAll("#code_structure", "'"+ addedData.getCode_structure()+"'");
			sql_query = sql_query.replaceAll("#date_fin_evaluation", "'"+ formatter.format(addedData.getDate_fin_evaluation())+"'");


			//System.out.println(select_structure);

			stmt.execute(sql_query);
			retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La donnée n'a pas été insérée dans la base données :"+e.toString(), "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} finally {
			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return retour;
	}

	/**
	 * cette classe permet de controler la validité des données insérées (par rapport à leurs taille)
	 * @param addedData
	 * @return
	 * @throws InterruptedException 
	 */

	public boolean controleIntegrite(PlanningCompagneListBean addedData ,Map heuredebut,Map heurefin) throws InterruptedException
	{
		try 
		{   	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

		Integer int_heuredebut=(Integer) heuredebut.get(addedData.getHeure_debut_evaluation());
		Integer int_heurefin=(Integer) heurefin.get(addedData.getHeure_fin_evaluation());



		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		if(addedData.getLieu().length()>30)
		{
			Messagebox.show("Le lieu ne doit pas dépasser 30 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);

			return false;
		}
		else
			if(addedData.getPersonne_ressources().length()>80)
			{
				Messagebox.show("La personne ressources ne doit pas dépasser 30 caractères !", "Erreur",Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			else
				if(int_heuredebut >= int_heurefin)
				{
					Messagebox.show("L'heure de fin de l'évaluation doit  être superieure à l'heure de debut !", "Erreur",Messagebox.OK, Messagebox.ERROR);
					return false;
				}


		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (NumberFormatException nfe)
		{
			Messagebox.show("Le mot de passe doit être un entier composé de 8 chiffres Exemple 21012001", "Erreur",Messagebox.OK, Messagebox.ERROR);
			return false;
		}


		return true;
	}

	/**
	 * Cette classe permet de mettre à jour la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	public Boolean updateCompagne(PlanningCompagneListBean addedData)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Boolean retour=false;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="update planning_evaluation set id_compagne=#id_compagne, id_employe=#id_employe, date_evaluation=#date_evaluation, id_evaluateur=#id_evaluateur, " +
					" heure_debut_evaluation=#heure_debut_evaluation, heure_fin_evaluation=#heure_fin_evaluation, lieu=#lieu," +
					" code_poste=#code_poste, personne_ressources=#personne_ressources, code_structure=#code_structure ,date_fin_evaluation=#date_fin_evaluation where id_planning_evaluation=#id_planning_evaluation" ;


			sql_query = sql_query.replaceAll("#id_compagne", "'"+ addedData.getId_compagne()+"'");
			sql_query = sql_query.replaceAll("#id_employe", "'"+ addedData.getId_evalue()+"'");
			sql_query = sql_query.replaceAll("#date_evaluation", "'"+ formatter.format(addedData.getDate_evaluation())+"'");
			sql_query = sql_query.replaceAll("#id_evaluateur", "'"+ addedData.getId_evaluateur()+"'");
			sql_query = sql_query.replaceAll("#heure_debut_evaluation", "'"+ addedData.getHeure_debut_evaluation()+"'");
			sql_query = sql_query.replaceAll("#heure_fin_evaluation", "'"+ addedData.getHeure_fin_evaluation()+"'");
			sql_query = sql_query.replaceAll("#lieu", "'"+ addedData.getLieu()+"'");
			sql_query = sql_query.replaceAll("#code_poste", "'"+ addedData.getCode_poste()+"'");
			sql_query = sql_query.replaceAll("#personne_ressources", "'"+ addedData.getPersonne_ressources()+"'");
			sql_query = sql_query.replaceAll("#code_structure", "'"+ addedData.getCode_structure()+"'");
			sql_query = sql_query.replaceAll("#id_planning_evaluation", "'"+ addedData.getId_planning_evaluation()+"'");
			sql_query = sql_query.replaceAll("#date_fin_evaluation", "'"+ formatter.format(addedData.getDate_fin_evaluation())+"'");



			//System.out.println(update_structure);

			stmt.executeUpdate(sql_query);
			retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La modification n'a pas été prise en compte"+e.toString(), "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return retour;
	}

	/**
	 * cette classe permet de supprimer une donnée de la table structure_entreprise
	 * @param codeStructure
	 */
	public Boolean deleteCompagne(PlanningCompagneListBean addedData)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Boolean retour=false;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="DELETE FROM  planning_evaluation   where id_planning_evaluation=#id_planning_evaluation" ; 
			sql_query = sql_query.replaceAll("#id_planning_evaluation", "'"+ addedData.getId_planning_evaluation()+"'");



			stmt.executeUpdate(sql_query);
			retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;

		} finally {
			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return retour;
	}

	/*
	 *//**
	 * cette classe permet de supprimer une donnée de la table structure_entreprise
	 * @param codeStructure
	 * @throws SQLException 
	 *//*
	 */

	public HashMap getCompagneValid() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			//	String profile_list="select id_compagne,libelle_compagne from compagne_evaluation" +
			//		" where date_debut>=now() order by date_debut"; 

			String profile_list="select id_compagne,libelle_compagne from compagne_evaluation order by id_compagne DESC"; 
			rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				map.put(rs.getString("libelle_compagne"), rs.getInt("id_compagne"));
				//list_profile.add(rs.getString("libelle_profile"));
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return map;
	}	

	public HashMap getCompagneFiltreValid() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		map.put("Toutes campagnes ", -1);
		try 
		{
			stmt = (Statement) conn.createStatement();
			//	String profile_list="select id_compagne,libelle_compagne from compagne_evaluation" +
			//		" where date_debut>=now() order by date_debut"; 

			String profile_list="select id_compagne,libelle_compagne from compagne_evaluation order by id_compagne DESC"; 
			rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				map.put(rs.getString("libelle_compagne"), rs.getInt("id_compagne"));
				//list_profile.add(rs.getString("libelle_profile"));
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return map;
	}	
	
	public HashMap getListEvaluteur() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		final InitContext intctx = new InitContext();
		intctx.loadProperties();
		ResultSet rs=null;

		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="";
			if (intctx.getDbtype().equalsIgnoreCase("1")){
				profile_list="select id_employe id_evaluateur,concat(e.nom,' ',e.prenom) as evaluateur " +
						" from employe e,common_evalcom.compte c where est_evaluateur='Y'" +
						" and e.id_compte=c.id_compte and c.database_id=#databaseid"; 
			}else{
				profile_list="select id_employe id_evaluateur,concat(e.nom,' ',e.prenom) as evaluateur " +
						" from employe e,compte c where est_evaluateur='Y'" +
						" and e.id_compte=c.id_compte and c.database_id=#databaseid"; 
			}

			ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
			database=applicationSession.getClient_database_id();
			profile_list = profile_list.replaceAll("#databaseid", "'"+database+"'");
			rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				map.put(rs.getString("evaluateur"), rs.getInt("id_evaluateur"));
				//list_profile.add(rs.getString("libelle_profile"));
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return map;
	}	

	public List  getListEvalue() throws SQLException
	{
		List listevalue = new ArrayList<PlanningListEvalueBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		final InitContext intctx = new InitContext();
		intctx.loadProperties();
		ResultSet rs=null;	
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="";
			if (intctx.getDbtype().equalsIgnoreCase("1")){
				profile_list="select id_employe,concat(e.nom,' ',e.prenom) as evalue " +
						" from employe e,common_evalcom.compte c where e.id_compte=c.id_compte and c.database_id=#databaseid"; 
			}else{
				profile_list="select id_employe,concat(e.nom,' ',e.prenom) as evalue " +
						" from employe e,compte c where e.id_compte=c.id_compte and c.database_id=#databaseid"; 
			}

			ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
			database=applicationSession.getClient_database_id();
			profile_list = profile_list.replaceAll("#databaseid", "'"+database+"'");
			rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				PlanningListEvalueBean evalbean=new PlanningListEvalueBean();
				evalbean.setId_employe(rs.getInt("id_employe"));
				evalbean.setNom_employe(rs.getString("evalue"));

				//map.put(rs.getString("evalue"), rs.getInt("id_employe"));
				//list_profile.add(rs.getString("libelle_profile"));
				listevalue.add(evalbean);
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return listevalue;
	}


	public HashMap getListPosteCompagne(Integer id_compagne) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;	
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select p.code_poste,p.intitule_poste from poste_travail_description p, compagne_poste_travail c where p.code_poste!='P000'"
					+ " and p.code_poste=c.code_poste and c.id_compagne=#id_compagne"; 

			profile_list = profile_list.replaceAll("#id_compagne", "'"+id_compagne+"'");
			rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				map.put(rs.getString("intitule_poste"), rs.getString("code_poste"));
				//list_profile.add(rs.getString("libelle_profile"));
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return (HashMap) sortByComparator(map);
	}	
	public HashMap getListPoste() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;	
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select code_poste,intitule_poste from poste_travail_description where code_poste!='P000'"; 
			rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				map.put(rs.getString("intitule_poste"), rs.getString("code_poste"));
				//list_profile.add(rs.getString("libelle_profile"));
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return map;
	}	
	public HashMap getListStructure() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;	
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select code_structure  from structure_entreprise"; 
			rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				map.put(rs.getString("code_structure"), rs.getString("code_structure"));
				//list_profile.add(rs.getString("libelle_profile"));
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return map;
	}	


	public HashMap selectedPoste(int employe) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;	
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select e.code_poste,intitule_poste from  employe e ,poste_travail_description t" +
					"  where id_employe=#employe and e.code_poste=t.code_poste "; 
			sql_query = sql_query.replaceAll("#employe", "'"+employe+"'");
			rs = (ResultSet) stmt.executeQuery(sql_query);


			while(rs.next()){
				map.put(rs.getString("intitule_poste"), rs.getString("code_poste"));
				//list_profile.add(rs.getString("libelle_profile"));
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return map;
	}	


	public List selectEmployes(String code_poste) throws SQLException{
		List listevalue = new ArrayList<PlanningListEvalueBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;	
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select id_employe,concat(nom,' ',prenom) as evalue from employe where  code_poste=#code_poste  "; 
			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
			rs = (ResultSet) stmt.executeQuery(sql_query);


			while(rs.next()){

				PlanningListEvalueBean evaluebean=new PlanningListEvalueBean();
				evaluebean.setId_employe(rs.getInt("id_employe"));
				evaluebean.setNom_employe(rs.getString("evalue"));
				listevalue.add(evaluebean);

				//list_profile.add(rs.getString("libelle_profile"));
			}

			//ajouter une ligne par defaut. Pour gere le cas ou la liste contient un seul element
			listevalue.add(new PlanningListEvalueBean(-1,"Selectionner un evalué"));
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return listevalue;
	}


	
	
	
	 public HashMap selectStructure(Integer id_employe) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;	
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select code_structure, structure_ent from ( "
					+ " select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
					+ " and  libelle_section !='null' and  libelle_section !=''"
					+ " union"
					+ "	 select code_structure,libelle_service structure_ent from structure_entreprise"
					+ "  where libelle_service is  not null and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
					+ " union"
					+ "  select code_structure,libelle_departement structure_ent from structure_entreprise"
					+ "  where libelle_departement is  not null and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
					+ " union"
					+ "  select code_structure,libelle_sous_direction structure_ent from structure_entreprise"
					+ "  where libelle_sous_direction is  not null and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
					+ " union"
					+ "  select code_structure,libelle_unite structure_ent from structure_entreprise"
					+ "  where libelle_unite is  not null and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "  and length(libelle_service)=0 and  length(libelle_section) =0"
					+ " union"
					+ "  select code_structure,libelle_direction structure_ent from structure_entreprise where libelle_direction is  not null"
					+ "  and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "  and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise"
					+ "  where   code_structure in (select code_structure from employe where id_employe=#id_employe)";
			sql_query = sql_query.replaceAll("#id_employe", "'"+id_employe+"'");
			rs = (ResultSet) stmt.executeQuery(sql_query);
			//System.out.println(sql_query);


			while(rs.next()){
				map.put(rs.getString("structure_ent"), rs.getString("code_structure"));
				//list_profile.add(rs.getString("libelle_profile"));
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return map;
	}

	/*public HashMap selectStructure(Integer id_employe) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;	
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select code_structure from employe  where id_employe=#id_employe"; 
			sql_query = sql_query.replaceAll("#id_employe", "'"+id_employe+"'");
			rs = (ResultSet) stmt.executeQuery(sql_query);
			//System.out.println(sql_query);


			while(rs.next()){
				map.put(rs.getString("code_structure"), rs.getString("code_structure"));
				//list_profile.add(rs.getString("libelle_profile"));
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return map;
	}*/



	public HashMap getHeureDebut() 
	{

		
		HashMap map = new HashMap();

		for (int i=8;i<=20;i++){
			map.put(String.format("%02d", i)+":00", i);
		}
		return (HashMap) sortByComparator(map);
	}

	public HashMap getHeureFin()
	{

		HashMap map = new HashMap();

		for (int i=8;i<=20;i++){
			map.put(String.format("%02d", i)+":00", i);
		}
		return (HashMap) sortByComparator(map);
	}	





	private static Map sortByComparator(Map unsortMap) {

		List list = new LinkedList(unsortMap.entrySet());

		//sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		//put sorted list into map again
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}	

	public HashMap setlectedStructure(String code_structure) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;

		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select concat_ws('-->',libelle_division,libelle_direction,libelle_unite,libelle_departement,libelle_service,libelle_section) as structure from structure_entreprise where code_structure=#code_structure";
			sql_query = sql_query.replaceAll("#code_structure", "'"+code_structure+"'");
			rs = (ResultSet) stmt.executeQuery(sql_query);


			while(rs.next()){
				map.put( rs.getString("structure"),rs.getString("structure"));
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return map;


	}


	public void sendPlanningToEvaluateur(List recipient,ArrayList<String> list_refevaluateur) throws SQLException{
		final InitContext intctx = new InitContext();
		intctx.loadProperties();
		Properties props = new Properties();
		/*props.put("mail.smtp.host", intctx.getHost());
		props.put("mail.smtp.socketFactory.port", intctx.getPort());
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", intctx.getPort());*/

		//props.setProperty("smtp.airaldgerie.dz", "localhost");
		props.setProperty(intctx.getServer(), "localhost");
		props.put("mail.smtp.host", intctx.getHost());
		props.put("mail.smtp.user", intctx.getUser());
		props.put("mail.smtp.password", intctx.getPassword());
		props.put("mail.smtp.port", intctx.getPort());

		Session session=Session.getDefaultInstance(props);

		List  listcompagne = new ArrayList<CompagneListBean>();
		//CompagneListBean cmp=new CompagneListBean();


		//ple  =  (PlanningAgendaBean) recipient.iterator();
		Iterator itr=list_refevaluateur.iterator();

		/*Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(intctx.getUser(),intctx.getPassword());
			}
		});*/

		try {

			MimeMessage message = new MimeMessage(session);
			String reslt="";
			String monmessage="";
			String nomevaluateur="";
			String login="";
			String pwd="";
			PlanningAgendaBean cpb;
			int ple;
			String mail="";
			PwdCrypt crypt=new PwdCrypt();
			message.setFrom(new InternetAddress(intctx.getFrom()));
			while(itr.hasNext()){

				ple  = Integer.parseInt((String) itr.next());
				Iterator it = recipient.iterator();	
				monmessage="<html> <body> 	<P>"+" Madame/Monsieur : "+"#nomevaluateur"+"</P>" +
						"<P>"+" Merci de trouver ci-dessous le planning de l'évaluation des compétences  de votre équipe"+"</P>" +			             
						" <TABLE BORDER=10>  <TR>  <TH align='center'> Evalué</TH>" +
						" <TH align='center'> Date début </TH> <TH align='center'> Heure début</TH>" +
						"<TH align='center'> Date fin</TH> <TH align='center'> Heure fin</TH> <TH align='center'> Lieu </TH>  </TR>";


				while (it.hasNext()){
					cpb  = (PlanningAgendaBean) it.next();

					if (cpb.getId_evaluateur()==ple){

						mail=cpb.getEmail();
						nomevaluateur=cpb.getNomevaluateur()+" "+cpb.getPrenomevaluateur();
						login=cpb.getLogin();
						pwd=crypt.decrypter(cpb.getPwd());
						message.setSubject("Planning évaluation des compétences");
						reslt="<TR>"+"<TD>"+ cpb.getNomevalue() +" "+ cpb.getPrenomevalue()+"</TD>"+
								"<TD>"+cpb.getDate_evaluation()+"</TD>"+
								"<TD>"+cpb.getHeure_debut_evaluation()+"</TD>"+
								"<TD>"+cpb.getDate_fin_evaluation()+"</TD>"+
								"<TD>"+cpb.getHeure_fin_evaluation()+"</TD>"+
								"<TD>"+ cpb.getLieu()+"</TD>"+
								"</TR>";  

						monmessage=monmessage+reslt;
					}
				}
				monmessage=monmessage.replaceAll("#nomevaluateur", nomevaluateur);
				monmessage=monmessage+	" </TABLE>"+"<P>"+"Vos codes d'accès à l'application Evalcom:\n ";
				monmessage=monmessage+"Utilisateur="+login+" et "+" Mot de passe="+pwd+"</P>";
				monmessage=monmessage+"	<P>"+"Cordialement"+	"</P>"+"<P>"+"Administrateur"+	"</P> </body></html>";
				StringBuilder sb = new StringBuilder();
				sb.append(monmessage);



				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(mail));

				message.setRecipients(Message.RecipientType.CC,
						InternetAddress.parse(intctx.getCc()));

				message.setContent(sb.toString(), "text/html");

				//System.out.println("to Evaluateur>>:"+monmessage);


				Transport.send(message);
				sb= new StringBuilder();
				nomevaluateur="";
				monmessage="";


			}
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}


	public List getListEvaluateur(int id_compagne) throws SQLException{


		List listevaluateur = new ArrayList<SuiviCompagneBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sqlquery="";
		if  (id_compagne==-1){
			sqlquery=	"select distinct id_evaluateur, concat(e.nom,' ',e.prenom) as evaluateur  from planning_evaluation p, employe e" +
					" where p.id_evaluateur=e.id_employe order by nom ";

		}
		else {
			sqlquery=	"select distinct id_evaluateur, concat(e.nom,' ',e.prenom) as evaluateur from planning_evaluation p, employe e" +
					" where p.id_evaluateur=e.id_employe and id_compagne=#id_compagne order by nom ";
			sqlquery = sqlquery.replaceAll("#id_compagne", "'"+id_compagne+"'");


		}


		try {
			stmt = (Statement) conn.createStatement();


			rs = (ResultSet) stmt.executeQuery(sqlquery);

			while(rs.next()){

				PlanningListEvaluateurBean evalbean=new PlanningListEvaluateurBean();
				evalbean.setId_evaluateur(rs.getInt("id_evaluateur"));
				evalbean.setEvaluateur(rs.getString("evaluateur"));



				listevaluateur.add(evalbean);


			}
			//				stmt.close();
			//				conn.close();

		} catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return listevaluateur;



	}


	public HashMap getCompagneList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;


		try 
		{
			stmt = (Statement) conn.createStatement();
			String db_list="select id_compagne,concat(libelle_compagne,'->', 'Du ',date_debut,' Au ',date_fin) as libelle_compagne from compagne_evaluation order by date_debut"; 
			rs = (ResultSet) stmt.executeQuery(db_list);


			while(rs.next()){
				map.put( rs.getString("libelle_compagne"),rs.getInt("id_compagne"));
			}
			map.put("Toutes les vagues",-1);
			
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		/*HashMap map = new HashMap();
		map.put("Toutes les vagues",-1);*/

		return map;
	}


	public List getPlanningEvaluateur(String evaluateur,Integer id_compagne) throws SQLException{


		List listevaluateur = new ArrayList<PlanningAgendaBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sqlquery="";

		sqlquery=	"select  p.id_evaluateur,e.nom as nomevaluateur,e.prenom as prenomevaluateur,e.email,t.nom as nomevalue,t.prenom as prenomevalue," +
				" p.date_evaluation,p.heure_debut_evaluation ,p.heure_fin_evaluation,p.lieu,p.personne_ressources,c.login,c.pwd,p.date_fin_evaluation" +
				" from employe e,planning_evaluation p,employe t, common_evalcom.compte c where e.id_employe in #evaluateur  and p.id_evaluateur=e.id_employe" +
				" and t.id_employe=p.id_employe   and c.id_compte=e.id_compte and p.id_compagne=#id_compagne";
		sqlquery = sqlquery.replaceAll("#evaluateur", evaluateur);
		sqlquery = sqlquery.replaceAll("#id_compagne",id_compagne.toString());





		try {
			stmt = (Statement) conn.createStatement();


			rs = (ResultSet) stmt.executeQuery(sqlquery);

			while(rs.next()){

				PlanningAgendaBean evalbean=new PlanningAgendaBean();
				evalbean.setNomevaluateur(rs.getString("nomevaluateur"));
				evalbean.setPrenomevaluateur(rs.getString("prenomevaluateur"));
				evalbean.setEmail(rs.getString("email"));
				evalbean.setNomevalue(rs.getString("nomevalue"));
				evalbean.setPrenomevalue(rs.getString("prenomevalue"));
				evalbean.setPrenomevalue(rs.getString("prenomevalue"));
				evalbean.setDate_evaluation(rs.getDate("date_evaluation"));
				evalbean.setHeure_debut_evaluation(rs.getString("heure_debut_evaluation"));
				evalbean.setHeure_fin_evaluation(rs.getString("heure_fin_evaluation"));
				evalbean.setLieu(rs.getString("lieu"));
				evalbean.setPersonne_ressources(rs.getString("personne_ressources"));
				evalbean.setId_evaluateur(rs.getInt("id_evaluateur"));
				evalbean.setLogin(rs.getString("login"));
				evalbean.setPwd(rs.getString("pwd"));
				evalbean.setDate_fin_evaluation(rs.getDate("date_fin_evaluation"));

				listevaluateur.add(evalbean);


			}
			//				stmt.close();
			//				conn.close();

		} catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return listevaluateur;



	}

	public List getPlanningAllEvaluateur() throws SQLException{


		List listevaluateur = new ArrayList<PlanningAgendaBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sqlquery="";

		sqlquery="select  p.id_evaluateur,e.nom as nomevaluateur,e.prenom as prenomevaluateur," +
				" e.email,t.nom as nomevalue,t.prenom as prenomevalue," +
				" p.date_evaluation,p.heure_debut_evaluation ,p.heure_fin_evaluation,p.lieu,p.personne_ressources" +
				" from employe e,planning_evaluation p,employe t" +
				" where p.id_evaluateur=e.id_employe 	and t.id_employe=p.id_employe" +
				" and date_evaluation > now() order by p.id_evaluateur";





		try {
			stmt = (Statement) conn.createStatement();


			rs = (ResultSet) stmt.executeQuery(sqlquery);

			while(rs.next()){

				PlanningAgendaBean evalbean=new PlanningAgendaBean();
				evalbean.setNomevaluateur(rs.getString("nomevaluateur"));
				evalbean.setPrenomevaluateur(rs.getString("prenomevaluateur"));
				evalbean.setEmail(rs.getString("email"));
				evalbean.setNomevalue(rs.getString("nomevalue"));
				evalbean.setPrenomevalue(rs.getString("prenomevalue"));
				evalbean.setPrenomevalue(rs.getString("prenomevalue"));
				evalbean.setDate_evaluation(rs.getDate("date_evaluation"));
				evalbean.setHeure_debut_evaluation(rs.getString("heure_debut_evaluation"));
				evalbean.setHeure_fin_evaluation(rs.getString("heure_fin_evaluation"));
				evalbean.setLieu(rs.getString("lieu"));
				evalbean.setPersonne_ressources(rs.getString("personne_ressources"));
				evalbean.setId_evaluateur(rs.getInt("id_evaluateur"));


				listevaluateur.add(evalbean);


			}
			//				stmt.close();
			//				conn.close();

		}catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return listevaluateur;



	}
	public void sendPlanningToDRH(List recipient) throws SQLException{
		final InitContext intctx = new InitContext();
		intctx.loadProperties();
		Properties props = new Properties();
		props.setProperty(intctx.getServer(), "localhost");

		props.put("mail.smtp.host", intctx.getHost());
		props.put("mail.smtp.user", intctx.getUser());
		props.put("mail.smtp.password", intctx.getPassword());
		props.put("mail.smtp.port", intctx.getPort());

		Session session=Session.getDefaultInstance(props);

		List  listcompagne = new ArrayList<CompagneListBean>();


		Map map_evaluateurs = new HashMap();
		Map map_drh=new HashMap();

		map_evaluateurs=getListAllEvaluateurs();
		Set set = (map_evaluateurs).entrySet(); 
		Iterator itr = set.iterator();
		map_drh=getListDRHs();

		Set set1 = (map_drh).entrySet(); 
		Iterator itr_drh = set1.iterator();



		try {

			MimeMessage message = new MimeMessage(session);
			String reslt="";
			String monmessage="";
			String nomevaluateur="";
			message.setSubject("Planning évaluation des compétences");
			message.setFrom(new InternetAddress(intctx.getFrom()));
			String nomdrh="";
			String emaildrh="";

			while(itr_drh.hasNext()){

				Map.Entry me = (Map.Entry)itr_drh.next();
				nomdrh=nomdrh+(String)me.getKey()+",";
				emaildrh=emaildrh+(String)me.getValue()+",";
				PlanningAgendaBean cpb;
				PlanningAgendaBean ple;
				String mail="";
				monmessage="<html> <body> 	<P>"+" Madames/Monsieurs : "+nomdrh+"</P>" +
						"<P>"+" Merci de trouver ci-dessous le planning complet des évaluations des compétences "+"</P>" ;
				while(itr.hasNext()){
					Map.Entry me1 = (Map.Entry)itr.next();
					monmessage=monmessage+"<P>"+" Evaluateur M,Mme : "+(String)me1.getKey() +"</P>";
					monmessage=monmessage+" <TABLE BORDER=10>"+" <TR>  <TH align='center'> Evalué</TH>" +
							" <TH align='center'> Date</TH> <TH align='center'> Heure début</TH>" +
							"<TH align='center'> Heure fin</TH> <TH align='center'> Lieu </TH>  </TR>";
					

							Iterator it = recipient.iterator();	


							while (it.hasNext()){
								cpb  = (PlanningAgendaBean) it.next();

								if (cpb.getId_evaluateur()==(Integer) me1.getValue()){					

									reslt="<TR>"+"<TD>"+ cpb.getNomevalue() +" "+ cpb.getPrenomevalue()+"</TD>"+
											"<TD>"+cpb.getDate_evaluation()+"</TD>"+
											"<TD>"+cpb.getHeure_debut_evaluation()+"</TD>"+
											"<TD>"+cpb.getHeure_fin_evaluation()+"</TD>"+
											"<TD>"+ cpb.getLieu()+"</TD>"+
											"</TR>";  								      
									monmessage=monmessage+reslt;
								}
							}

							monmessage=monmessage+" </TABLE>";
							System.out.println("to DRH>>:"+monmessage.toString());

				}



			}
			//monmessage=monmessage.replaceAll("#nomevaluateur", nomevaluateur);
			monmessage=monmessage+"<P>"+"Cordialement"+	"</P>"+"<P>"+"Administrateur"+	"</P> </body></html>";
			//System.out.println(monmessage);
			StringBuilder sb = new StringBuilder();
			sb.append(monmessage);



			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(emaildrh));

			message.setRecipients(Message.RecipientType.CC,
					InternetAddress.parse(intctx.getCc()));

			message.setContent(sb.toString(), "text/html");



			Transport.send(message);
			sb= new StringBuilder();
			nomevaluateur="";
			monmessage="";
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

	public HashMap getListAllEvaluateurs() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select  distinct p.id_evaluateur ,concat(e.nom,' ',e.prenom) as nom  from planning_evaluation p,employe e" +
					" where e.id_employe =p.id_evaluateur  and date_evaluation > now() order by p.id_evaluateur "; 
			rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				map.put(rs.getString("nom"), rs.getInt("p.id_evaluateur"));
				//list_profile.add(rs.getString("libelle_profile"));
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return map;
	}


	public HashMap getListDRHs() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select  distinct concat(nom,' ',prenom) as nom,email  from employe" +
					" where  est_responsable_rh='Y'"; 
			rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				map.put(rs.getString("nom"), rs.getString("email"));
				
			}
			
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return map;
	}	
	
	public List filtrePlanningCompagne(String filtreNomEvaluateur,String filreNomEvalue) throws SQLException{


		listcompagne = new ArrayList<PlanningCompagneListBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();

			String whereClause="";
			if(filtreNomEvaluateur!=null && !"".equals(filtreNomEvaluateur))
			{
				whereClause= "HAVING upper (evaluateur)like upper('"+filtreNomEvaluateur+"%')";
			}
			if(filreNomEvalue!=null && !"".equals(filreNomEvalue))
			{
				whereClause= " and upper(e.nom) like upper('"+filreNomEvalue+"%')";
			}

			String sel_comp=" select id_planning_evaluation,libelle_compagne , (select concat(nom,' ', prenom) from employe where id_employe in(id_evaluateur)) as evaluateur,concat (nom,' ', prenom) as evalue,intitule_poste,t.structure_ent libelle_structure,"
					+ "  date_evaluation,heure_debut_evaluation,date_fin_evaluation,heure_fin_evaluation,lieu,personne_ressources,p.id_evaluateur ,p.id_employe id_evalue,p.id_compagne"
					+ "  from planning_evaluation p,employe e,compagne_evaluation c, poste_travail_description d,"
					+ "  (select code_structure, structure_ent from ( "
					+ "  select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null "
					+ "  and  libelle_section !='null' and  libelle_section !=''  "
					+ " union"
					+ " select code_structure,libelle_service structure_ent from structure_entreprise"
					+ " where libelle_service is  not null and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_departement structure_ent from structure_entreprise"
					+ " where libelle_departement is  not null"
					+ " and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_sous_direction structure_ent from structure_entreprise"
					+ " where libelle_sous_direction is  not null"
					+ " and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
					+ " union  "
					+ "	  select code_structure,libelle_unite structure_ent from structure_entreprise"
					+ "   where libelle_unite is  not null and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "	  and length(libelle_service)=0 and  length(libelle_section) =0"
					+ " union"
					+ "	 select code_structure,libelle_direction structure_ent from structure_entreprise"
					+ "	 where libelle_direction is  not null and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "  and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise )t where   p.id_compagne=c.id_compagne and   p.id_employe=e.id_employe and t.code_structure=e.code_structure and e.code_structure=p.code_structure"
					+ "  and   p.code_poste=d.code_poste " +whereClause;

			rs = (ResultSet) stmt.executeQuery(sel_comp);

			while(rs.next()){

				PlanningCompagneListBean compagne=new PlanningCompagneListBean();
				compagne.setId_planning_evaluation((rs.getInt("id_planning_evaluation")));	
				compagne.setLibelle_compagne((rs.getString("libelle_compagne")));
				compagne.setEvaluateur((rs.getString("evaluateur")));
				compagne.setEvalue(((rs.getString("evalue"))));
				compagne.setIntitule_poste((((rs.getString("intitule_poste")))));
				//compagne.setCode_structure((rs.getString("code_structure")));
				compagne.setLibelle_structure((rs.getString("libelle_structure")));
				compagne.setDate_evaluation(rs.getDate("date_evaluation"));
				compagne.setHeure_debut_evaluation(rs.getString("heure_debut_evaluation"));
				compagne.setHeure_fin_evaluation(rs.getString("heure_fin_evaluation"));
				compagne.setLieu(rs.getString("lieu"));
				compagne.setPersonne_ressources(rs.getString("personne_ressources"));
				compagne.setId_evaluateur(rs.getInt("id_evaluateur"));
				compagne.setId_evalue(rs.getInt("id_evalue"));
				compagne.setId_compagne(rs.getInt("id_compagne"));
				compagne.setDate_fin_evaluation(rs.getDate("date_fin_evaluation"));




				listcompagne.add(compagne);

			}
			

		} catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return listcompagne;



	}
	


	/*public List filtrePlanningCompagne(String filtreNomEvaluateur,String filreNomEvalue) throws SQLException{


		listcompagne = new ArrayList<PlanningCompagneListBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();

			String whereClause="";
			if(filtreNomEvaluateur!=null && !"".equals(filtreNomEvaluateur))
			{
				whereClause= "HAVING upper (evaluateur)like upper('"+filtreNomEvaluateur+"%')";
			}
			if(filreNomEvalue!=null && !"".equals(filreNomEvalue))
			{
				whereClause= " and upper(e.nom) like upper('"+filreNomEvalue+"%')";
			}

			String sel_comp="select id_planning_evaluation,libelle_compagne , (select concat(nom,' ', prenom) from employe where id_employe in(id_evaluateur)) as evaluateur,concat (nom,' ', prenom) as evalue,intitule_poste,p.code_structure," +
					" date_evaluation,heure_debut_evaluation,heure_fin_evaluation,lieu,personne_ressources,p.id_evaluateur ,p.id_employe id_evalue,p.id_compagne " +
					" from planning_evaluation p,employe e,compagne_evaluation c, poste_travail_description d " +
					" where   p.id_compagne=c.id_compagne and   p.id_employe=e.id_employe and   p.code_poste=d.code_poste "+whereClause;

			rs = (ResultSet) stmt.executeQuery(sel_comp);

			while(rs.next()){

				PlanningCompagneListBean compagne=new PlanningCompagneListBean();
				compagne.setId_planning_evaluation((rs.getInt("id_planning_evaluation")));	
				compagne.setLibelle_compagne((rs.getString("libelle_compagne")));
				compagne.setEvaluateur((rs.getString("evaluateur")));
				compagne.setEvalue(((rs.getString("evalue"))));
				compagne.setIntitule_poste((((rs.getString("intitule_poste")))));
				compagne.setCode_structure((rs.getString("code_structure")));
				compagne.setDate_evaluation(rs.getDate("date_evaluation"));
				compagne.setHeure_debut_evaluation(rs.getString("heure_debut_evaluation"));
				compagne.setHeure_fin_evaluation(rs.getString("heure_fin_evaluation"));
				compagne.setLieu(rs.getString("lieu"));
				compagne.setPersonne_ressources(rs.getString("personne_ressources"));
				compagne.setId_evaluateur(rs.getInt("id_evaluateur"));
				compagne.setId_evalue(rs.getInt("id_evalue"));
				compagne.setId_compagne(rs.getInt("id_compagne"));



				listcompagne.add(compagne);

			}
			//			stmt.close();
			//			conn.close();

		} catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return listcompagne;



	}
	*/
	
	/***
	 * Methode qui permet de verifier s 'il y  une   fiche est validée ou en cours de validation
	 * @param employe
	 * @return
	 * @throws SQLException
	 */
	
	public boolean verifEmployeFicheValid(PlanningCompagneListBean addedData) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;	
		boolean result=false;
		try 
		{
			stmt = (Statement) conn.createStatement();
			/*String sql_query="select * from fiche_validation where id_employe=#id_employe and fiche_valide  in (-1 ,1) "
					+ " and id_planning_evaluation in (select id_planning_evaluation from planning_evaluation where id_compagne=#id_compagne)"; */
			
			String sql_query="select * from fiche_validation where id_employe=#id_employe and fiche_valide  in (-1 ,1) "
			+ " and id_planning_evaluation =#id_planning_evaluation";
			//sql_query = sql_query.replaceAll("#id_compagne", "'"+ addedData.getId_compagne()+"'");
			sql_query = sql_query.replaceAll("#id_employe", "'"+ addedData.getId_evalue()+"'");	
			sql_query = sql_query.replaceAll("#id_planning_evaluation", "'"+ addedData.getId_planning_evaluation()+"'");	
			rs = (ResultSet) stmt.executeQuery(sql_query);

		
			
			while(rs.next()){
				result=true;
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return result;
	}	

	/**
	 * cette methode permet d'ouvrir une fiche deja validée dans le cas des recours
	 * Evolution demandée dans la v2.1 point numero 2
	 * @param codeStructure
	 * @throws InterruptedException 
	 */
	public Boolean reinitFicheEvaluation(PlanningCompagneListBean addedData) throws InterruptedException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Boolean retour=false;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="update fiche_validation set fiche_valide=-1 where id_planning_evaluation=#id_planning_evaluation and fiche_valide=1" ; 
			sql_query = sql_query.replaceAll("#id_planning_evaluation", "'"+ addedData.getId_planning_evaluation()+"'");



			stmt.executeUpdate(sql_query);
			int count=stmt.getUpdateCount();
			if (count>0){
				retour=true;
			}else{
				Messagebox.show("La fiche d'évaluation de M/Mme " +addedData.getEvalue()+" n'a pas été validée encore !", "Information",Messagebox.OK, Messagebox.INFORMATION); 
				return false;
			}
			
		} 

		catch ( SQLException e ) {
			Messagebox.show("Problème technique, la fiche de  M/Mme " +addedData.getEvalue()+" n'a pas été ouverte", "Information",Messagebox.OK, Messagebox.INFORMATION); 

			retour=false;

		} finally {
			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return retour;
	}
	
	
	/**
	 * cette methode permet d'ouvrir une compagne  deja validée  apres l'ouverture du fiche d'évaluation
	 * Dans le cas des recours,Evolution demandée dans la v2.1 point numero 2
	 * @param codeStructure
	 */
	public Boolean reinitCompagneEvaluation(PlanningCompagneListBean addedData)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Boolean retour=false;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query1="delete from compagne_validation where id_compagne=#id_compagne" ; 
			String sql_query2="delete from img_stats where id_compagne=#id_compagne" ; 
			String sql_query3="delete from moy_poste_famille_stats where id_compagne=#id_compagne" ; 
			String sql_query4="delete from moy_poste_competence_stats where id_compagne=#id_compagne" ; 
			
			String sql_query_total = sql_query1 +" ; "+sql_query2+" ; "+sql_query3+" ; "+sql_query4+";";
			sql_query_total = sql_query_total.replaceAll("#id_compagne", String.valueOf(addedData.getId_compagne()));

			stmt.executeUpdate(sql_query_total);
			retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;

		} finally {
			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return retour;
	}
	
	
	/*
	 * remplissage de la list box structure entreprise
	 * @return
	 * @throws SQLException
	 */

	public Map getStructEntList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{ 
			stmt = (Statement) conn.createStatement();
			String db_list=		
					"select code_structure, structure_ent from (" +
							" select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null "+
							" and  libelle_section !='null' and  libelle_section !='' " +
							" union " +
							" select code_structure,libelle_service structure_ent from structure_entreprise  " +
							" where libelle_service is  not null" +
							"  and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0   " +
							" union " +
							" select code_structure,libelle_departement structure_ent from structure_entreprise  " + 
							" where libelle_departement is  not null " +
							"  and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0  " +
							" union " +
							" select code_structure,libelle_sous_direction structure_ent from structure_entreprise   " +
							" where libelle_sous_direction is  not null " +
							"  and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0 " +   
							" union  " +
							" select code_structure,libelle_unite structure_ent from structure_entreprise   " +
							" where libelle_unite is  not null " +
							"  and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0  " +
							"  and length(libelle_service)=0 and  length(libelle_section) =0    " +
							" union  " +
							" select code_structure,libelle_direction structure_ent from structure_entreprise   " +
							" where libelle_direction is  not null " +
							"  and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0 " + 
							"  and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise  order by structure_ent ";
			//System.out.println(db_list);
			rs = (ResultSet) stmt.executeQuery(db_list);


			while(rs.next()){
				map.put( rs.getString("structure_ent"),rs.getString("code_structure"));
			}
			//map.put("Tous",-1);
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		Map<String, String> treeMap = new TreeMap<String, String>(map);
		return  treeMap;
	}
	
	public HashMap getListAllCompagne() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select  id_compagne,concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) nomcompagne" +
					" from compagne_evaluation e 	order by id_compagne ";
			rs = (ResultSet) stmt.executeQuery(sql_query);


			while(rs.next()){
				map.put(rs.getString("nomcompagne"), rs.getString("id_compagne"));
				//list_profile.add(rs.getString("libelle_profile"));
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return map;
	}

}
