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
import java.util.Scanner;
import java.util.TreeMap;

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
import compagne.bean.GestionEmployesBean;
import compagne.bean.PlanningCompagneListBean;

public class GestionEmployesModel {


	private ArrayList<GestionEmployesBean>  listcompagne =null; 
	private ListModel strset =null;

	/**
	 * cette méthode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */

	public List loadListEmployes() throws SQLException{


		listcompagne = new ArrayList<GestionEmployesBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		final InitContext intctx = new InitContext();
		intctx.loadProperties();
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="";
			String nom_table="";
			String sql_query="select concat(c.nom,'-',c.prenom) as nom, id_employe,date_naissance,date_recrutement ,concat(d.niv_for_libelle,'-',libelle_diplome) as libelle_formation,p.intitule_poste, email,"
					+ "	CASE WHEN est_evaluateur='N' THEN 'NON' ELSE 'OUI' END as est_evaluateur,"
					+ " CASE WHEN est_responsable_rh='N' THEN 'NON' ELSE 'OUI' END as est_responsable_rh ,"
					+ " t.structure_ent libelle_structure,sexe_lbl,type_contrat_lbl"
					+ " from employe e  ,sexe s, type_contrat t,poste_travail_description p,formation f,#nom_table c  ,"
					+ " def_niv_formation d,(select code_structure, structure_ent from ("
					+ " select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
					+ " and  libelle_section !='null' and  libelle_section !=''"
					+ " union"
					+ " select code_structure,libelle_service structure_ent from structure_entreprise"
					+ " where libelle_service is  not null"
					+ " and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_departement structure_ent from structure_entreprise"
					+ " where libelle_departement is  not null"
					+ "	and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0 "
					+ " union"
					+ " select code_structure,libelle_sous_direction structure_ent from structure_entreprise "
					+ " where libelle_sous_direction is  not null"
					+ " and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_unite structure_ent from structure_entreprise"
					+ " where libelle_unite is  not null"
					+ " and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ " and length(libelle_service)=0 and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_direction structure_ent from structure_entreprise"
					+ " where libelle_direction is  not null and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ " and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise  )t"
					+ " where e.code_poste=p.code_poste and e.code_formation=f.code_formation"
					+ " and e.code_structure=t.code_structure and   e.id_compte=c.id_compte and   d.niv_for_id=f.niv_for_id and  e.code_contrat=t.code_contrat and e.code_sexe=s.code_sexe";
			if (intctx.getDbtype().equalsIgnoreCase("1")){
	
				sql_query = sql_query.replaceAll("#nom_table",  "common_evalcom.compte");

			}
			else{
				sql_query = sql_query.replaceAll("#nom_table", "'"+ "compte"+"'");

			}
			//System.out.println(sql_query);
			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				GestionEmployesBean bean=new GestionEmployesBean();

				//bean.setId_compte((rs.getInt("id_compte")));
				bean.setId_employe((rs.getInt("id_employe")));	
				bean.setNom_complet(rs.getString("nom"));
				//bean.setPrenom(rs.getString("prenom"));
				bean.setDate_naissance((rs.getDate("date_naissance")));
				bean.setDate_recrutement((rs.getDate("date_recrutement")));
				bean.setLibelle_formation(rs.getString("libelle_formation"));
				bean.setIntitule_poste(rs.getString("intitule_poste"));
				bean.setEmail(rs.getString("email"));
				bean.setEst_evaluateur(rs.getString("est_evaluateur"));
				bean.setEst_responsable_rh(rs.getString("est_responsable_rh"));
				bean.setLibelle_structure(rs.getString("libelle_structure"));
				bean.setSexe(rs.getString("sexe_lbl"));
				bean.setType_contrat(rs.getString("type_contrat_lbl"));
				
				

				listcompagne.add(bean);

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
		Collections.sort(listcompagne);
		return listcompagne;



	}
	/*public List loadListEmployes() throws SQLException{


		listcompagne = new ArrayList<GestionEmployesBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		final InitContext intctx = new InitContext();
		intctx.loadProperties();
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="";
			if (intctx.getDbtype().equalsIgnoreCase("1")){

				sel_comp="select concat(c.nom,'-',c.prenom) as nom, id_employe,date_naissance,date_recrutement ,concat(d.niv_for_libelle,'-',libelle_diplome) as libelle_formation,p.intitule_poste, email,  CASE WHEN est_evaluateur='N' THEN 'NON' ELSE 'OUI' END as est_evaluateur," +
						" CASE WHEN est_responsable_rh='N' THEN 'NON' ELSE 'OUI' END as est_responsable_rh ,e.code_structure" +
						" from employe e  ,poste_travail_description p,formation f,common_evalcom.compte c  , def_niv_formation d" +
						"  where e.code_poste=p.code_poste and e.code_formation=f.code_formation" +
						"  and   e.id_compte=c.id_compte and   d.niv_for_id=f.niv_for_id order by 1";
			}
			else{
				sel_comp="select concat(c.nom,'-',c.prenom) as nom, id_employe,date_naissance,date_recrutement ,concat(d.niv_for_libelle,'-',libelle_diplome) as libelle_formation,p.intitule_poste, email,  CASE WHEN est_evaluateur='N' THEN 'NON' ELSE 'OUI' END as est_evaluateur," +
						" CASE WHEN est_responsable_rh='N' THEN 'NON' ELSE 'OUI' END as est_responsable_rh ,e.code_structure" +
						" from employe e  ,poste_travail_description p,formation f,compte c  , def_niv_formation d" +
						"  where e.code_poste=p.code_poste and e.code_formation=f.code_formation" +
						"  and   e.id_compte=c.id_compte and   d.niv_for_id=f.niv_for_id order by 1";
			}
			rs = (ResultSet) stmt.executeQuery(sel_comp);

			while(rs.next()){

				GestionEmployesBean bean=new GestionEmployesBean();

				//bean.setId_compte((rs.getInt("id_compte")));
				bean.setId_employe((rs.getInt("id_employe")));	
				bean.setNom_complet(rs.getString("nom"));
				//bean.setPrenom(rs.getString("prenom"));
				bean.setDate_naissance((rs.getDate("date_naissance")));
				bean.setDate_recrutement((rs.getDate("date_recrutement")));
				bean.setLibelle_formation(rs.getString("libelle_formation"));
				bean.setIntitule_poste(rs.getString("intitule_poste"));
				bean.setEmail(rs.getString("email"));
				bean.setEst_evaluateur(rs.getString("est_evaluateur"));
				bean.setEst_responsable_rh(rs.getString("est_responsable_rh"));
				bean.setCode_structure(rs.getString("code_structure"));

				listcompagne.add(bean);

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
		Collections.sort(listcompagne);
		return listcompagne;



	}*/

	/**
	 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
	 * @param addedData
	 * @return
	 * @throws ParseException 
	 */
	public boolean addEmploye(GestionEmployesBean addedData) throws ParseException
	{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Boolean resultat =false;

		try 
		{

			stmt = (Statement) conn.createStatement();
			String sql_query="INSERT INTO employe( nom, prenom, date_naissance, rattach_dg, date_recrutement, code_formation, code_poste, email, est_evaluateur, est_responsable_service, est_responsable_direction, est_responsable_division, est_responsable_departement, est_responsable_unite, est_responsable_section, est_responsable_rh, code_structure, id_compte,code_sexe,code_contrat)" +
					" VALUES(#nom,#prenom,#date_naissance,'N',#date_recrutement,#code_formation,#code_poste,#email,#est_evaluateur,'N','N','N','N','N','N',#est_responsable_rh,#code_structure,#id_compte,#sexe,#type_contrat)";
			//sql_query = sql_query.replaceAll("#id_employe", "'"+ addedData.getId_employe()+"'");
			sql_query = sql_query.replaceAll("#nom", "'"+ addedData.getNom()+"'");
			sql_query = sql_query.replaceAll("#prenom", "'"+ addedData.getPrenom()+"'");
			sql_query = sql_query.replaceAll("#date_naissance", "'"+ formatter.format(addedData.getDate_naissance())+"'");
			sql_query = sql_query.replaceAll("#date_recrutement", "'"+ formatter.format(addedData.getDate_recrutement())+"'");
			sql_query = sql_query.replaceAll("#code_formation", "'"+ addedData.getCode_formation()+"'");
			sql_query = sql_query.replaceAll("#code_poste", "'"+ addedData.getCode_poste()+"'");
			sql_query = sql_query.replaceAll("#email", "'"+ addedData.getEmail()+"'");
			sql_query = sql_query.replaceAll("#est_evaluateur", "'"+ addedData.getCode_est_evaluateur()+"'");
			sql_query = sql_query.replaceAll("#est_responsable_rh", "'"+ addedData.getCode_est_responsable_rh()+"'");
			sql_query = sql_query.replaceAll("#code_structure", "'"+ addedData.getCode_structure()+"'");
			//sql_query = sql_query.replaceAll("#code_structure", "'"+ "S0000"+"'");
			sql_query = sql_query.replaceAll("#id_compte", "'"+ addedData.getId_compte()+"'");
			
			sql_query = sql_query.replaceAll("#sexe", "'"+ addedData.getCode_sexe()+"'");
			sql_query = sql_query.replaceAll("#type_contrat", "'"+ addedData.getCode_type_contrat()+"'");


			//System.out.println(sql_query);

			stmt.execute(sql_query);
			resultat=true;
		} 

		catch ( SQLException e ) {
			resultat=false;
			try 
			{
				//Messagebox.show("La donnée n'a pas été insérée dans la base données", "Erreur",Messagebox.OK, Messagebox.ERROR);
				Messagebox.show("La donnée n'a pas été insérée dans la base de données" +e, "Erreur",Messagebox.OK, Messagebox.ERROR);
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
		return resultat;
	}

	/**
	 * cette classe permet de controler la validité des données insérées (par rapport à leurs taille)
	 * @param addedData
	 * @return
	 * @throws InterruptedException 
	 */

	public boolean controleIntegrite(GestionEmployesBean addedData) throws InterruptedException
	{
		try 
		{   	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		Integer chaine=addedData.getEmail().indexOf("@");


		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		if(addedData.getDate_naissance().after(addedData.getDate_recrutement()))
		{
			Messagebox.show("La date de naissance doit est inferieure à la date de recrutement !", "Erreur",Messagebox.OK, Messagebox.ERROR);
			return false;
		}
		else
			if(chaine==-1 )
			{
				Messagebox.show("L'adresse mail saisie n'est pas une adresse valide !", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	public Boolean updateListeEmploye(GestionEmployesBean addedData)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		Boolean retour=false;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="update employe  set nom=#nom, prenom=#prenom, date_naissance=#date_naissance, rattach_dg='N', date_recrutement=#date_recrutement, code_formation=#code_formation, code_poste=#code_poste, email=#email, est_evaluateur=#est_evaluateur, est_responsable_service='N', est_responsable_direction='N', est_responsable_division='N', est_responsable_departement='N'," +
					" est_responsable_unite='N', est_responsable_section='N', est_responsable_rh=#est_responsable_rh, " +
					" code_structure=#code_structure, id_compte=#id_compte, code_sexe=#code_sexe,code_contrat=#code_contrat where id_employe=#id_employe " ;


			sql_query = sql_query.replaceAll("#id_employe", "'"+ addedData.getId_employe()+"'");
			sql_query = sql_query.replaceAll("#nom", "'"+ addedData.getNom()+"'");
			sql_query = sql_query.replaceAll("#prenom", "'"+ addedData.getPrenom()+"'");
			sql_query = sql_query.replaceAll("#date_naissance", "'"+ formatter.format(addedData.getDate_naissance())+"'");
			sql_query = sql_query.replaceAll("#date_recrutement", "'"+ formatter.format(addedData.getDate_recrutement())+"'");
			sql_query = sql_query.replaceAll("#code_formation", "'"+ addedData.getCode_formation()+"'");
			sql_query = sql_query.replaceAll("#code_poste", "'"+ addedData.getCode_poste()+"'");
			sql_query = sql_query.replaceAll("#email", "'"+ addedData.getEmail()+"'");
			sql_query = sql_query.replaceAll("#est_evaluateur", "'"+ addedData.getCode_est_evaluateur()+"'");
			sql_query = sql_query.replaceAll("#est_responsable_rh", "'"+ addedData.getCode_est_responsable_rh()+"'");
			sql_query = sql_query.replaceAll("#code_structure", "'"+ addedData.getCode_structure()+"'");
			//sql_query = sql_query.replaceAll("#code_structure", "'"+ "S0000"+"'");
			sql_query = sql_query.replaceAll("#id_compte", "'"+ addedData.getId_compte()+"'");
			sql_query = sql_query.replaceAll("#code_sexe", "'"+ addedData.getCode_sexe()+"'");
			sql_query = sql_query.replaceAll("#code_contrat", "'"+ addedData.getCode_type_contrat()+"'");



			//System.out.println(sql_query);

			stmt.executeUpdate(sql_query);
			retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La modification n'a pas été prise en compte "+e, "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	 * @throws InterruptedException 
	 */
	public Boolean deleteEmploye(GestionEmployesBean addedData) throws InterruptedException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Boolean retour=false;

		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="DELETE FROM  employe   where id_employe=#id_employe " ;


			sql_query = sql_query.replaceAll("#id_employe", "'"+ addedData.getId_employe()+"'");



			stmt.executeUpdate(sql_query);
			retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;
			Messagebox.show("La suppression de l'enregistrement a échoué "+e, "Erreur",Messagebox.OK, Messagebox.ERROR);
			return false;

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

	public HashMap getListFormation() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select   f.code_formation ,concat(d.niv_for_libelle,'-',f.libelle_diplome)" +
					" as libelle_formation  from formation f, def_niv_formation d" +
					" where f.niv_for_id=d.niv_for_id"; 
			rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				map.put(rs.getString("libelle_formation"), rs.getString("code_formation"));
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

	public HashMap getListPostes() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select code_poste,intitule_poste from poste_travail_description"; 
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


	public HashMap selectEmployes(String code_poste) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;	
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select id_employe,concat(nom,' ',prenom) as evalue from employe where est_evaluateur='N'" +
					"  and code_poste=#code_poste  "; 
			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
			rs = (ResultSet) stmt.executeQuery(sql_query);


			while(rs.next()){
				map.put(rs.getString("evalue"), rs.getInt("id_employe"));
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



	public HashMap isEvaluateur() 
	{

		HashMap map = new HashMap();

		map.put("OUI", "Y");
		map.put("NON", "N");
		return (HashMap) sortByComparator(map);
	}

	public HashMap isResRH() 
	{

		HashMap map = new HashMap();

		map.put("OUI", "Y");
		map.put("NON", "N");
		return (HashMap) sortByComparator(map);
	}
	
	
	/*
	 * remplissage de la list box structure entreprise
	 * @return
	 * @throws SQLException
	 */

	public Map sexeListe() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{ 
			stmt = (Statement) conn.createStatement();
			String db_list=	"select code_sexe,sexe_lbl from sexe";
			//System.out.println(db_list);
			rs = (ResultSet) stmt.executeQuery(db_list);


			while(rs.next()){
				map.put( rs.getString("sexe_lbl"),rs.getString("code_sexe"));
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
	
	public Map typeContrat() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{ 
			stmt = (Statement) conn.createStatement();
			String db_list=	"select code_contrat,type_contrat_lbl from type_contrat";
			//System.out.println(db_list);
			rs = (ResultSet) stmt.executeQuery(db_list);


			while(rs.next()){
				map.put( rs.getString("type_contrat_lbl"),rs.getString("code_contrat"));
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
	
	
	public HashMap getCompteList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		final InitContext intctx = new InitContext();
		intctx.loadProperties();
		HashMap map = new HashMap();
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		//TODO supprimer cette ligne ApplicationFacade
		//int database_id=ApplicationFacade.getInstance().getClient_database_id();
		int database_id=applicationSession.getClient_database_id();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="";
			if (intctx.getDbtype().equalsIgnoreCase("1")){
				sql_query="select id_compte,concat(nom,'-',prenom) as nom from common_evalcom.compte where database_id=#database_id";
				sql_query = sql_query.replaceAll("#database_id", "'"+database_id+"'");
			}
			else{
				sql_query="select id_compte,concat(nom,'-',prenom) as nom from compte where database_id=#database_id";
				sql_query = sql_query.replaceAll("#database_id", "'"+database_id+"'");
			}

			rs = (ResultSet) stmt.executeQuery(sql_query);


			while(rs.next()){
				map.put( rs.getString("nom"),rs.getInt("id_compte"));
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



	/*

	public String getCurrentDatetime(){
		Date today = Calendar.getInstance().getTime();
	    // (2) create our "formatter" (our custom format)
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	    // (3) create a new String in the format we want
	    String todaydate = formatter.format(today);

	    return todaydate;

	}

	public  Integer getKeyMap(String key) throws SQLException{
		Integer idprofile=(Integer)gerProfileList() .get(key);
		return idprofile;
	}

	public static boolean isValidDateStr(String date) {
	    try {
	      String format="yyyy/MM/dd";
	    	SimpleDateFormat sdf = new SimpleDateFormat(format);
	      sdf.setLenient(false);
	      sdf.parse(date);
	    }
	    catch (ParseException e) {
	      return false;
	    }
	    catch (IllegalArgumentException e) {
	      return false;
	    }
	    return true;
	    }*/


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


	public List  filtreEmployes(String filreCompteUtilisateur/*,String filtrelibelleFormation,String filtreIntitule*/) throws SQLException{


		listcompagne = new ArrayList<GestionEmployesBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		final InitContext intctx = new InitContext();
		intctx.loadProperties();
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();
			String whereClause="";
			if(filreCompteUtilisateur!=null && !"".equals(filreCompteUtilisateur))
			{
				whereClause= " and upper(c.nom) like upper('"+filreCompteUtilisateur+"%')";
			}
			//				if(filtrelibelleFormation!=null && !"".equals(filtrelibelleFormation))
			//				{
			//					whereClause= " and ( upper(d.niv_for_libelle) like upper('"+filtrelibelleFormation+"%')"+ " or upper(libelle_diplome) like upper('"+filtrelibelleFormation+"%')) ";
			//				}
			//				if(filtreIntitule!=null && !"".equals(filtreIntitule))
			//				{
			//					whereClause= " and upper(p.intitule_poste) like upper('"+filtreIntitule+"%') ";
			//				}


			String sel_comp="";
			
			/*sel_comp="select concat(c.nom,'-',c.prenom) as nom, id_employe,date_naissance,date_recrutement ,concat(d.niv_for_libelle,'-',libelle_diplome) as libelle_formation,p.intitule_poste, email,  CASE WHEN est_evaluateur='N' THEN 'NON' ELSE 'OUI' END as est_evaluateur," +
					" CASE WHEN est_responsable_rh='N' THEN 'NON' ELSE 'OUI' END as est_responsable_rh ,e.code_structure" +
					" from employe e  ,poste_travail_description p,formation f,#nom_table c  , def_niv_formation d" +
					"  where e.code_poste=p.code_poste and e.code_formation=f.code_formation" +
					"  and   e.id_compte=c.id_compte and   d.niv_for_id=f.niv_for_id "+whereClause+" order by 1";*/
			
			sel_comp="select concat(c.nom,'-',c.prenom) as nom, id_employe,date_naissance,date_recrutement ,concat(d.niv_for_libelle,'-',libelle_diplome) as libelle_formation,p.intitule_poste, email,  CASE WHEN est_evaluateur='N' THEN 'NON' ELSE 'OUI' END as est_evaluateur,"
					+ "	 CASE WHEN est_responsable_rh='N' THEN 'NON' ELSE 'OUI' END as est_responsable_rh ,t.structure_ent libelle_structure,sexe_lbl,type_contrat_lbl"
					+ "  from employe e ,sexe s, type_contrat t,poste_travail_description p,formation f,common_evalcom.compte c  , def_niv_formation d,"
					+ "  (select code_structure, structure_ent from ("
					+ "  select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
					+ "	 and  libelle_section !='null' and  libelle_section !=''"
					+ "	 union"
					+ "	 select code_structure,libelle_service structure_ent from structure_entreprise"
					+ "  where libelle_service is  not null and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
					+ "  union"
					+ "  select code_structure,libelle_departement structure_ent from structure_entreprise "
					+ "  where libelle_departement is  not null and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
					+ "  union"
					+ "  select code_structure,libelle_sous_direction structure_ent from structure_entreprise"
					+ "  where libelle_sous_direction is  not null and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
					+ "  union"
					+ "	 select code_structure,libelle_unite structure_ent from structure_entreprise"
					+ "  where libelle_unite is  not null and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "  and length(libelle_service)=0 and  length(libelle_section) =0"
					+ "  union"
					+ "  select code_structure,libelle_direction structure_ent from structure_entreprise"
					+ "  where libelle_direction is  not null and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "  and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise )t"
					+ "  where e.code_poste=p.code_poste and e.code_formation=f.code_formation  and t.code_structure=e.code_structure"
					+ "  and   e.id_compte=c.id_compte and  d.niv_for_id=f.niv_for_id and  e.code_contrat=t.code_contrat and e.code_sexe=s.code_sexe"+whereClause+" order by 1";
					  
			
			if (intctx.getDbtype().equalsIgnoreCase("1")){
				
				sel_comp = sel_comp.replaceAll("#nom_table",  "common_evalcom.compte");

			}
			else{
				sel_comp = sel_comp.replaceAll("#nom_table", "'"+ "compte"+"'");

			}
			
			rs = (ResultSet) stmt.executeQuery(sel_comp);

			while(rs.next()){

				GestionEmployesBean bean=new GestionEmployesBean();

				//bean.setId_compte((rs.getInt("id_compte")));
				bean.setId_employe((rs.getInt("id_employe")));	
				bean.setNom_complet(rs.getString("nom"));
				//bean.setPrenom(rs.getString("prenom"));
				bean.setDate_naissance((rs.getDate("date_naissance")));
				bean.setDate_recrutement((rs.getDate("date_recrutement")));
				bean.setLibelle_formation(rs.getString("libelle_formation"));
				bean.setIntitule_poste(rs.getString("intitule_poste"));
				bean.setEmail(rs.getString("email"));
				bean.setEst_evaluateur(rs.getString("est_evaluateur"));
				bean.setEst_responsable_rh(rs.getString("est_responsable_rh"));
				//bean.setCode_structure(rs.getString("code_structure"));
				bean.setLibelle_structure(rs.getString("libelle_structure"));
				bean.setSexe(rs.getString("sexe_lbl"));
				bean.setType_contrat(rs.getString("type_contrat_lbl"));


				listcompagne.add(bean);

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
		return listcompagne;



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
	
	
	
	
	

}
