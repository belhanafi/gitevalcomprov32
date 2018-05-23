package administration.model;


import java.io.IOException;
import java.io.InputStream;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import administration.bean.AptitudePosteTravailBean;
import administration.bean.Compagne;
import administration.bean.CompetencePosteTravailBean;
import administration.bean.FichePosteBean;
import administration.bean.PosteVsAptitudeBean;
import administration.bean.ProfileDroitsAccessBean;
import common.ApplicationSession;
import common.Contantes;
import common.CreateDatabaseCon;


public class CompetencePosteTravailModel {


	/**
	 * mise à jour de la table postedetravail_competence en supprimant des associations existantes
	 * 
	 */

	public void updateUnCheckedPoteTravailCompetence(ArrayList <String>listunselected, HashMap <String, String>mapCodeCompetence, HashMap <String, String>mapCodePoste)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Iterator<String> iterator =listunselected.iterator();
		try
		{
			while(iterator.hasNext())
			{
				String cles=iterator.next();
				String[] liste=cles.split("#");
				//			String famille=liste[0];
				//			String groupe=liste[1];
				String competence=liste[2];
				String posteTravail=liste[3];

				//récuperation du code_competence et du code_posteTravail

				String code_competence=mapCodeCompetence.get(competence);
				String code_poste=mapCodePoste.get(posteTravail);




				//mise à jour de la table poste_travail_competence
				/*****************************************/
				try 
				{
					stmt = (Statement) conn.createStatement();
					String update_structure="DELETE FROM  poste_travail_competence  WHERE code_poste=#code_poste and code_competence=#code_competence"; 
					update_structure = update_structure.replaceAll("#code_poste", "'"+code_poste+"'");
					update_structure = update_structure.replaceAll("#code_competence", "'"+code_competence+"'");

					//System.out.println(update_structure);
					stmt.executeUpdate(update_structure);
				} 
				catch (SQLException e) 
				{

					e.printStackTrace();
					//return false;


				}

				/**********************************************/




			}
		}
		catch ( Exception e ) {

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

	}

	/**
	 * mise à jour de la table postedetravail_competence_aptitudeobservable en supprimant des associations existantes
	 * 
	 */

	public void updateUnCheckedPoteTravailCompetenceAptitudeObs(ArrayList <String>listunselected, HashMap <String, String>mapCodeCompetence, HashMap <String, String>mapCodePoste)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Iterator<String> iterator =listunselected.iterator();
		try
		{
			while(iterator.hasNext())
			{
				String cles=iterator.next();
				String[] liste=cles.split("#");
				//			String famille=liste[0];
				//			String groupe=liste[1];
				String competence=liste[2];
				String posteTravail=liste[3];
				String codeAptitudeObservable=liste[4];

				//récuperation du code_competence et du code_posteTravail

				String code_competence=mapCodeCompetence.get(competence);
				String code_poste=mapCodePoste.get(posteTravail);




				//mise à jour de la table poste_travail_competence
				/*****************************************/
				try 
				{
					stmt = (Statement) conn.createStatement();
					String update_structure="DELETE FROM  poste_travail_comptence_aptitudeobservable  WHERE code_poste=#code_poste and code_competence=#code_competence and id_repertoire_competence=#code_aptitude"; 
					update_structure = update_structure.replaceAll("#code_poste", "'"+code_poste+"'");
					update_structure = update_structure.replaceAll("#code_competence", "'"+code_competence+"'");
					update_structure = update_structure.replaceAll("#code_aptitude", "'"+codeAptitudeObservable+"'");

					System.out.println(update_structure);
					stmt.executeUpdate(update_structure);
					
					String requete="DELETE-->"+code_poste+"/"+code_competence+"/"+codeAptitudeObservable;
					histoPostAptitude(requete);
					
				} 
				catch (SQLException e) 
				{

					e.printStackTrace();
					//return false;


				}

				/**********************************************/




			}
		}
		catch ( Exception e ) {

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

	}

	/**
	 * mise à jour de la table postedetravail_competence en ajoutant des associations existantes
	 * 
	 */

	public void updateCheckedPoteTravailCompetence(ArrayList <String>listselected, HashMap <String, String>mapCodeCompetence, HashMap <String, String>mapCodePoste)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Iterator<String> iterator =listselected.iterator();
		try
		{
			while(iterator.hasNext())
			{
				String cles=iterator.next();
				String[] liste=cles.split("#");
				//String famille=liste[0];
				//String groupe=liste[1];
				String competence=liste[2];
				String posteTravail=liste[3];

				//récuperation du code_competence et du code_posteTravail


				String code_competence=mapCodeCompetence.get(competence);
				String code_poste=mapCodePoste.get(posteTravail);

				//mise à jour de la table poste_travail_competence
				/*****************************************/
				try 
				{
					stmt = (Statement) conn.createStatement();
					String insert_structure="INSERT INTO  poste_travail_competence  (code_poste,code_competence) VALUES (#code_poste, #code_competence)"; 
					insert_structure = insert_structure.replaceAll("#code_poste", "'"+code_poste+"'");
					insert_structure = insert_structure.replaceAll("#code_competence", "'"+code_competence+"'");

					//System.out.println(insert_structure);	
					stmt.execute(insert_structure);
				} 
				catch (SQLException e) 
				{

					e.printStackTrace();
					//return false;


				}

				/**********************************************/


			} 
		}
		catch ( Exception e ) {

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

	}

	/**
	 * mise à jour de la table postedetravail_competence en ajoutant des associations existantes
	 * 
	 */

	public void updateCheckedPoteTravailCompetenceAptitudeObs(ArrayList <String>listselected, HashMap <String, String>mapCodeCompetence, HashMap <String, String>mapCodePoste)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Iterator<String> iterator =listselected.iterator();
		try
		{
			while(iterator.hasNext())
			{
				String cles=iterator.next();
				String[] liste=cles.split("#");
				//String famille=liste[0];
				//String groupe=liste[1];
				String competence=liste[2];
				String posteTravail=liste[3];
				String codeAptitudeObservable=liste[4];
				//récuperation du code_competence et du code_posteTravail


				String code_competence=mapCodeCompetence.get(competence);
				String code_poste=mapCodePoste.get(posteTravail);

				//mise à jour de la table poste_travail_competence
				/*****************************************/
				try 
				{
					stmt = (Statement) conn.createStatement();
					String insert_structure="INSERT INTO  poste_travail_comptence_aptitudeobservable  (code_poste,code_competence, id_repertoire_competence) VALUES (#code_poste, #code_competence,#code_aptitude)"; 
					insert_structure = insert_structure.replaceAll("#code_poste", "'"+code_poste+"'");
					insert_structure = insert_structure.replaceAll("#code_competence", "'"+code_competence+"'");
					insert_structure = insert_structure.replaceAll("#code_aptitude", "'"+codeAptitudeObservable+"'");
					System.out.println(insert_structure);	
					stmt.execute(insert_structure);
					String requete="INSERT-->"+code_poste+"/"+code_competence+"/"+codeAptitudeObservable;
					histoPostAptitude(requete);
				} 
				catch (SQLException e) 
				{

					e.printStackTrace();
					//return false;


				}

				/**********************************************/


			} 
		}
		catch ( Exception e ) {

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

	}

	/**
	 * récupération de la liste des poste de travail
	 * @return
	 */
	public ArrayList<String> getlistepostes()
	{
		ArrayList<String> listposteTravail = new ArrayList<String>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT intitule_poste  FROM poste_travail_description where code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
					" where c.id_compagne=p.id_compagne and now()<=date_fin) ";

			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					listposteTravail.add(rs.getString("intitule_poste"));

				}
				else {
					return listposteTravail;
				}

			}
			conn.close();
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
		return listposteTravail;	

	}

	public HashMap<String, String> getlistepostesCode_postes()
	{

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		HashMap<String, String > mapcode_poste=new HashMap<String, String >();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from planning_evaluation) ";
			String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
					" where c.id_compagne=p.id_compagne and now()<=date_fin) ";

			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String intitule_poste=rs.getString("intitule_poste");
					String code_poste=rs.getString("code_poste");

					mapcode_poste.put(intitule_poste, code_poste);

				}
				else {
					return mapcode_poste;
				}

			}
			conn.close();
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
		return mapcode_poste;	

	}

	public HashMap<String, String> getlisteCode_postes_intituleposte()
	{

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		HashMap<String, String > mapcode_poste=new HashMap<String, String >();
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from planning_evaluation) ";
			String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
					" where c.id_compagne=p.id_compagne and now()<=date_fin) ";



			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String intitule_poste=rs.getString("intitule_poste");
					String code_poste=rs.getString("code_poste");

					mapcode_poste.put(code_poste,intitule_poste );

				}
				else {
					return mapcode_poste;
				}

			}
			conn.close();
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


		return mapcode_poste;	

	}

	public HashMap<String, String> getlisteLibelle_code_competence()
	{

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		HashMap<String, String > mapcode_competence=new HashMap<String, String >();
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT libelle_competence,  code_competence FROM repertoire_competence ";

			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String libelle_competence=rs.getString("libelle_competence");
					String code_competence=rs.getString("code_competence");

					mapcode_competence.put(libelle_competence,code_competence );

				}
				else {
					return mapcode_competence;
				}

			}
			conn.close();
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


		return mapcode_competence;	

	}
	/**
	 * récupération des données qui associe le repertoire de competence au poste de travail
	 * @throws SQLException 
	 */
	public AptitudePosteTravailBean getAptitudePosteTravailBean() throws SQLException
	{
		//ArrayList<String> listposteTravail = new ArrayList<String>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		AptitudePosteTravailBean aptitudePosteTravailBean=new AptitudePosteTravailBean();
		Statement stmt=null;
		ResultSet rs=null;
		try
		{

			stmt = (Statement) conn.createStatement();
			//String select_structure="select distinct a.id_repertoire_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste from repertoire_competence a, poste_travail_competence b, poste_travail_description e where a.code_competence=b.code_competence and e.code_poste=b.code_poste and e.code_poste in(select distinct code_poste from planning_evaluation)"; 
			//String select_structure="select distinct  a.code_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste from repertoire_competence a, poste_travail_competence b, poste_travail_description e where a.code_competence=b.code_competence and e.code_poste=b.code_poste and e.code_poste in(select distinct code_poste from planning_evaluation)";
			/*String select_structure="select distinct  a.code_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste" +
			" from  poste_travail_description e ,repertoire_competence a LEFT OUTER  JOIN poste_travail_competence b" +
			" on ( a.code_competence=a.code_competence) where e.code_poste in(select distinct code_poste from planning_evaluation)";*/

			/*String select_structure="select distinct a.id_repertoire_competence, a.code_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste" +
					" from  poste_travail_description e ,repertoire_competence a LEFT OUTER  JOIN poste_travail_competence b" +
					" on ( a.code_competence=a.code_competence) where e.code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
					" where c.id_compagne=p.id_compagne and now()<=date_fin) and a.affichable='O'";*/
			String select_structure="select distinct a.id_repertoire_competence, a.code_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste" +
					" from  poste_travail_description e ,repertoire_competence a LEFT OUTER  JOIN poste_travail_competence b" +
					" on ( a.code_competence=a.code_competence) where e.code_poste in(select distinct code_poste from compagne_poste_travail p) and a.affichable='O'";


			rs = (ResultSet) stmt.executeQuery(select_structure);


			HashMap <String,HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>> >> mapFamille=new HashMap <String,HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>  >>();
			aptitudePosteTravailBean.setListefamilles(mapFamille);
			HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>> > mapgroupe=new HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>> >();
			HashMap<String, HashMap<String, ArrayList<String>>>  mapcompetence=new  HashMap<String, HashMap<String, ArrayList<String>>>() ;

			HashMap <String, String > mapCodeCompetence=new HashMap <String, String >();
			//			
			HashMap<String, ArrayList<String>> mapAptitudeObservable=new HashMap<String, ArrayList<String>>();
			//HashMap <String, String > mapIdRepertoireCompetence=new HashMap <String, String >();
			//int i=0;
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{


					String famille=rs.getString("famille");
					String groupe=rs.getString("groupe");
					String libelle_competence=rs.getString("libelle_competence");
					String intitule_poste=rs.getString("intitule_poste");
					String code_competence=rs.getString("code_competence");
					String aptitude_observable=rs.getString("id_repertoire_competence");

					mapCodeCompetence.put(libelle_competence, code_competence);

					mapFamille=aptitudePosteTravailBean.getListefamilles();
					if(mapFamille.containsKey(famille))
					{
						mapgroupe=mapFamille.get(famille);
						if(mapgroupe.containsKey(groupe))
						{
							mapcompetence=mapgroupe.get(groupe);
							if(mapcompetence.containsKey(libelle_competence))
							{
								mapAptitudeObservable=mapcompetence.get(libelle_competence);
								if(mapAptitudeObservable.containsKey(aptitude_observable))
								{
									mapAptitudeObservable.get(aptitude_observable).add(intitule_poste);
								}
								else
								{
									ArrayList<String> listeposte=new ArrayList<String>();
									listeposte.add(intitule_poste);

									mapAptitudeObservable.put(aptitude_observable,listeposte );
								}
								//mapcompetence.get(libelle_competence).add(mapAptitudeObservable);
							}
							else
							{
								ArrayList<String> listeposte=new ArrayList<String>();
								listeposte.add(intitule_poste);
								HashMap<String, ArrayList<String>> mapAptitudeObservable1=new HashMap<String, ArrayList<String>>();
								mapAptitudeObservable1.put(aptitude_observable,listeposte );
								mapcompetence.put(libelle_competence,mapAptitudeObservable1 );
							}
						}
						else
						{
							ArrayList<String> listeposte=new ArrayList<String>();
							listeposte.add(intitule_poste);
							HashMap<String, ArrayList<String>> mapAptitudeObservable1=new HashMap<String, ArrayList<String>>();
							mapAptitudeObservable1.put(aptitude_observable,listeposte );

							HashMap<String, HashMap<String, ArrayList<String>>> mapcompetence1=new HashMap<String, HashMap<String, ArrayList<String>>>();
							mapcompetence1.put(libelle_competence,mapAptitudeObservable1 );

							mapgroupe.put(groupe, mapcompetence1);
						}

					}
					else
					{
						HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>> > mapgroupe1=new HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>> >();
						HashMap<String, HashMap<String, ArrayList<String>>> mapcompetence1=new HashMap<String, HashMap<String, ArrayList<String>>>();
						HashMap<String, ArrayList<String>> mapAptitudeObservable1=new HashMap<String, ArrayList<String>>();

						ArrayList<String> listeposte=new ArrayList<String>();
						listeposte.add(intitule_poste);
						mapAptitudeObservable1.put(aptitude_observable,listeposte );
						mapcompetence1.put(libelle_competence,mapAptitudeObservable1 );
						mapgroupe1.put(groupe, mapcompetence1);
						mapFamille.put(famille, mapgroupe1);
					}

					aptitudePosteTravailBean.setListefamilles(mapFamille);
					aptitudePosteTravailBean.setMapCodeCompetence(mapCodeCompetence);
				}
				else 
				{
					return aptitudePosteTravailBean;
				}
				//competencePosteTravailBean.setListefamilles(mapFamille);

			}
			conn.close();
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
		return aptitudePosteTravailBean;


	}

	public CompetencePosteTravailBean getCompetencePosteTravailBean() throws SQLException
	{
		//ArrayList<String> listposteTravail = new ArrayList<String>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		CompetencePosteTravailBean competencePosteTravailBean=new CompetencePosteTravailBean();
		Statement stmt=null;
		ResultSet rs=null;
		try
		{

			stmt = (Statement) conn.createStatement();
			//String select_structure="select distinct a.id_repertoire_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste from repertoire_competence a, poste_travail_competence b, poste_travail_description e where a.code_competence=b.code_competence and e.code_poste=b.code_poste and e.code_poste in(select distinct code_poste from planning_evaluation)"; 
			//String select_structure="select distinct  a.code_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste from repertoire_competence a, poste_travail_competence b, poste_travail_description e where a.code_competence=b.code_competence and e.code_poste=b.code_poste and e.code_poste in(select distinct code_poste from planning_evaluation)";
			/*String select_structure="select distinct  a.code_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste" +
			" from  poste_travail_description e ,repertoire_competence a LEFT OUTER  JOIN poste_travail_competence b" +
			" on ( a.code_competence=a.code_competence) where e.code_poste in(select distinct code_poste from planning_evaluation)";*/

			String select_structure="select distinct  a.code_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste" +
					" from  poste_travail_description e ,repertoire_competence a LEFT OUTER  JOIN poste_travail_competence b" +
					" on ( a.code_competence=a.code_competence) where e.code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
					" where c.id_compagne=p.id_compagne and now()<=date_fin) and a.affichable='O'";


			rs = (ResultSet) stmt.executeQuery(select_structure);


			HashMap <String,HashMap<String, HashMap<String, ArrayList<String>> >> mapFamille=new HashMap <String,HashMap<String, HashMap<String, ArrayList<String>> >>();
			competencePosteTravailBean.setListefamilles(mapFamille);
			HashMap<String, HashMap<String, ArrayList<String>> > mapgroupe=new HashMap<String, HashMap<String, ArrayList<String>> >();
			HashMap<String, ArrayList<String>> mapcompetence=new HashMap<String, ArrayList<String>>();
			HashMap <String, String > mapCodeCompetence=new HashMap <String, String >();
			//int i=0;
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{

					//String id_repertoire_competence=rs.getString("id_repertoire_competence");
					String famille=rs.getString("famille");
					String groupe=rs.getString("groupe");
					String libelle_competence=rs.getString("libelle_competence");
					String intitule_poste=rs.getString("intitule_poste");
					String code_competence=rs.getString("code_competence");


					mapCodeCompetence.put(libelle_competence, code_competence);

					mapFamille=competencePosteTravailBean.getListefamilles();
					if(mapFamille.containsKey(famille))
					{
						mapgroupe=mapFamille.get(famille);
						if(mapgroupe.containsKey(groupe))
						{
							mapcompetence=mapgroupe.get(groupe);
							if(mapcompetence.containsKey(libelle_competence))
							{
								mapcompetence.get(libelle_competence).add(intitule_poste);
							}
							else
							{
								ArrayList<String> listeposte=new ArrayList<String>();
								listeposte.add(intitule_poste);

								mapcompetence.put(libelle_competence,listeposte );
							}
						}
						else
						{
							ArrayList<String> listeposte=new ArrayList<String>();
							listeposte.add(intitule_poste);
							HashMap<String, ArrayList<String>> mapcompetence1=new HashMap<String, ArrayList<String>>();

							mapcompetence1.put(libelle_competence,listeposte );
							mapgroupe.put(groupe, mapcompetence1);
						}

					}
					else
					{
						HashMap<String, HashMap<String, ArrayList<String>> > mapgroupe1=new HashMap<String, HashMap<String, ArrayList<String>> >();
						HashMap<String, ArrayList<String>> mapcompetence1=new HashMap<String, ArrayList<String>>();

						ArrayList<String> listeposte=new ArrayList<String>();
						listeposte.add(intitule_poste);
						mapcompetence1.put(libelle_competence,listeposte );
						mapgroupe1.put(groupe, mapcompetence1);
						mapFamille.put(famille, mapgroupe1);
					}

					competencePosteTravailBean.setListefamilles(mapFamille);
					competencePosteTravailBean.setMapCodeCompetence(mapCodeCompetence);
				}
				else 
				{
					return competencePosteTravailBean;
				}
				//competencePosteTravailBean.setListefamilles(mapFamille);

			}
			conn.close();
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
		return competencePosteTravailBean;


	}
	public HashMap<String, ArrayList<String> > getListAptitudeObservable()
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;

		HashMap<String, ArrayList<String> > mapcompetence_aptitudeObservable=new HashMap<String, ArrayList<String>>();
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="select distinct  p.intitule_poste ,  c.libelle_competence from  repertoire_competence c, poste_travail_description p, poste_travail_competence k where k.code_competence=c.code_competence and k.code_poste=p.code_poste  and p.code_poste in(select distinct code_poste from planning_evaluation) ";
			//String select_structure="select  code_poste, code_competence from poste_travail_competence where code_poste in(select distinct code_poste from planning_evaluation)";
			String select_structure="select distinct code_competence,aptitude_observable, id_repertoire_competence from repertoire_competence  where affichable='O' ";
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{

					String aptitude_observable=rs.getString("aptitude_observable");
					String codecompetence=rs.getString("code_competence");
					String repertoireCompetence=rs.getInt("id_repertoire_competence")+"";
					if(mapcompetence_aptitudeObservable.containsKey(codecompetence))
					{
						mapcompetence_aptitudeObservable.get(codecompetence).add(repertoireCompetence+"#"+aptitude_observable);
					}
					else
					{
						ArrayList<String> listAptitudeObservable=new ArrayList<String>();
						listAptitudeObservable.add(repertoireCompetence+"#"+aptitude_observable);
						mapcompetence_aptitudeObservable.put(codecompetence, listAptitudeObservable);
					}

				}
				else {
					return mapcompetence_aptitudeObservable;
				}

			}
			//			stmt.close();
			//			conn.close();
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


		return mapcompetence_aptitudeObservable;
	}
	public HashMap<String , ArrayList<String>> getAssociationPosteTravailCompetence()
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		HashMap<String, ArrayList<String> > mapcompetence_poste=new HashMap<String, ArrayList<String>>();
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="select distinct  p.intitule_poste ,  c.libelle_competence from  repertoire_competence c, poste_travail_description p, poste_travail_competence k where k.code_competence=c.code_competence and k.code_poste=p.code_poste  and p.code_poste in(select distinct code_poste from planning_evaluation) ";
			//String select_structure="select  code_poste, code_competence from poste_travail_competence where code_poste in(select distinct code_poste from planning_evaluation)";
			String select_structure="select  code_poste, code_competence from poste_travail_competence where code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
					" where c.id_compagne=p.id_compagne and now()<=date_fin )";
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//					String intitule_poste=rs.getString("intitule_poste");
					//					String libelle_competence=rs.getString("c.libelle_competence");
					//					
					//					if(mapcompetence_poste.containsKey(libelle_competence))
					//					{
					//						mapcompetence_poste.get(libelle_competence).add(intitule_poste);
					//					}
					//					else
					//					{
					//						ArrayList<String> listePoste=new ArrayList<String>();
					//						listePoste.add(intitule_poste);
					//						mapcompetence_poste.put(libelle_competence, listePoste);
					//					}

					String code_poste=rs.getString("code_poste");
					String codecompetence=rs.getString("code_competence");

					if(mapcompetence_poste.containsKey(codecompetence))
					{
						mapcompetence_poste.get(codecompetence).add(code_poste);
					}
					else
					{
						ArrayList<String> listePoste=new ArrayList<String>();
						listePoste.add(code_poste);
						mapcompetence_poste.put(codecompetence, listePoste);
					}

				}
				else {
					return mapcompetence_poste;
				}

			}
			//			stmt.close();
			//			conn.close();
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


		return mapcompetence_poste;
	}
	public HashMap<String , ArrayList<String>> getAssociationPosteTravailCompetenceAptitudeObservable(AptitudePosteTravailBean aptitudePosteTravailBean)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		HashMap<String, ArrayList<String> > mapaptitudeObservable_poste=new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String> > mapcompetence_poste=new HashMap<String, ArrayList<String>>();
		try 
		{
			stmt = (Statement) conn.createStatement();

			/*String select_structure=" select code_poste, code_competence, id_repertoire_competence from poste_travail_comptence_aptitudeobservable where code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
					" where c.id_compagne=p.id_compagne and now()<=date_fin )";*/
			
			//afficher uniquement  les postes de travail sélectionnés pour la derniere campagne (ecran ratachhement poste travail campagne)
			String select_structure=" select code_poste, code_competence, id_repertoire_competence from poste_travail_comptence_aptitudeobservable"
					+ " where code_poste in(select distinct code_poste from compagne_poste_travail where id_compagne in ( select max(id_compagne)"
					+ " from compagne_evaluation  ))";
			
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//					

					String code_poste=rs.getString("code_poste");
					String codeaptitude=rs.getInt("id_repertoire_competence")+"";
					String codecompetence=rs.getString("code_competence");
					String cles=codecompetence+"#"+codeaptitude;
					if(mapcompetence_poste.containsKey(codecompetence))
					{
						mapcompetence_poste.get(codecompetence).add(code_poste);
					}
					else
					{
						ArrayList<String> listePoste=new ArrayList<String>();
						listePoste.add(code_poste);
						mapcompetence_poste.put(codecompetence, listePoste);
					}


					if(mapaptitudeObservable_poste.containsKey(cles))
					{
						mapaptitudeObservable_poste.get(cles).add(code_poste);
					}
					else
					{
						ArrayList<String> listePoste=new ArrayList<String>();
						listePoste.add(code_poste);
						mapaptitudeObservable_poste.put(cles, listePoste);
					}

				}
				else {
					return mapaptitudeObservable_poste;
				}

			}
			//			stmt.close();
			//			conn.close();
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

		aptitudePosteTravailBean.setMapCompetencePosteTravail(mapcompetence_poste);
		return mapaptitudeObservable_poste;
	}
	public AptitudePosteTravailBean fusionAptitude(AptitudePosteTravailBean aptitudePosteTravailBean,HashMap<String , ArrayList<String>> mapCompetencePoste )
	{

		HashMap <String, String> maplibelle_code_competence =getlisteLibelle_code_competence();
		HashMap <String,HashMap <String,HashMap<String, HashMap<String, ArrayList<String>> >>> mapFamille=aptitudePosteTravailBean.getListefamilles();
		HashMap<String, String> mapcode_libelle_poste=getlisteCode_postes_intituleposte();
		Set<String> clesMapFamille=mapFamille.keySet();
		HashMap<String , ArrayList<String>> mapCompetencePost=aptitudePosteTravailBean.getMapCompetencePosteTravail();

		//HashMap<String , ArrayList<String>> mapAptitudeObservablePost=aptitudePosteTravailBean.getMapCompetencePosteTravail();
		Iterator<String> iterator=clesMapFamille.iterator();
		while(iterator.hasNext())
		{
			String famille= iterator.next();
			HashMap <String,HashMap<String, HashMap<String, ArrayList<String>> >> mapGroupe=mapFamille.get(famille);

			Set<String> clesMapGroupe=mapGroupe.keySet();

			Iterator <String> iterator2=clesMapGroupe.iterator();
			while(iterator2.hasNext())
			{
				String groupe=iterator2.next();
				HashMap<String, HashMap<String, ArrayList<String>> > mapCompetence=mapGroupe.get(groupe);

				Set<String> clesCompetence=mapCompetence.keySet();

				Iterator <String>iterator3=clesCompetence.iterator();
				while(iterator3.hasNext())
				{
					String competence=iterator3.next();

					String scode_competence=maplibelle_code_competence.get(competence);

					/**************************************/
					HashMap<String, ArrayList<String>>  mapAptitude=mapCompetence.get(competence);
					Set<String> clesAptitude=mapAptitude.keySet();
					Iterator <String>iterator4=clesAptitude.iterator();
					while(iterator4.hasNext())
					{
						String aptitude=iterator4.next();

						String cles=scode_competence+"#"+aptitude;
						ArrayList<String> slistCode_poste=/*mapCompetencePost.get(scode_competence);*/mapCompetencePoste.get(cles);
						ArrayList <String > libelle_poste=new ArrayList <String>();
						if(slistCode_poste!=null)
						{
							Iterator <String>iterator5=slistCode_poste.iterator();

							while(iterator5.hasNext())
							{
								String code=(String)iterator5.next();
								String libelle=mapcode_libelle_poste.get(code);

								libelle_poste.add(libelle);

							}
						}
						//mapCompetence.remove(competence);
						mapAptitude.put(aptitude, libelle_poste);
						//mapCompetence.put(competence,libelle_poste);
					}
					mapCompetence.put(competence,mapAptitude);
				}
				mapGroupe.put(groupe, mapCompetence);
			}
			mapFamille.put(famille, mapGroupe);

		}

		return aptitudePosteTravailBean;
	}


	public CompetencePosteTravailBean fusion(CompetencePosteTravailBean competencePosteTravailBean,HashMap<String , ArrayList<String>> mapCompetencePoste )
	{

		HashMap <String, String> maplibelle_code_competence =getlisteLibelle_code_competence();
		HashMap <String,HashMap<String, HashMap<String, ArrayList<String>> >> mapFamille=competencePosteTravailBean.getListefamilles();
		HashMap<String, String> mapcode_libelle_poste=getlisteCode_postes_intituleposte();
		Set<String> clesMapFamille=mapFamille.keySet();

		Iterator<String> iterator=clesMapFamille.iterator();
		while(iterator.hasNext())
		{
			String famille= iterator.next();
			HashMap<String, HashMap<String, ArrayList<String>> > mapGroupe=mapFamille.get(famille);

			Set<String> clesMapGroupe=mapGroupe.keySet();

			Iterator <String> iterator2=clesMapGroupe.iterator();
			while(iterator2.hasNext())
			{
				String groupe=iterator2.next();
				HashMap<String, ArrayList<String>> mapCompetence=mapGroupe.get(groupe);

				Set<String> clesCompetence=mapCompetence.keySet();

				Iterator <String>iterator3=clesCompetence.iterator();
				while(iterator3.hasNext())
				{
					String competence=iterator3.next();

					String scode_competence=maplibelle_code_competence.get(competence);



					ArrayList<String> slistCode_poste=mapCompetencePoste.get(scode_competence);
					ArrayList <String > libelle_poste=new ArrayList <String>();
					if(slistCode_poste!=null)
					{
						Iterator <String>iterator4=slistCode_poste.iterator();

						while(iterator4.hasNext())
						{
							String code=(String)iterator4.next();
							String libelle=mapcode_libelle_poste.get(code);

							libelle_poste.add(libelle);

						}
					}
					//mapCompetence.remove(competence);
					mapCompetence.put(competence,libelle_poste);

				}
				mapGroupe.put(groupe, mapCompetence);
			}
			mapFamille.put(famille, mapGroupe);

		}

		return competencePosteTravailBean;
	}


	/**
	 * historisation des modifications sur l'ecran rattachement poste travail aptitudes observables
	 * 
	 */

	public void histoPostAptitude(String requete)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		String login=applicationSession.getCompteUtilisateur().getLogin();

		try
		{
			stmt = (Statement) conn.createStatement();
			String insert_structure="INSERT INTO  histo_poste_trav_aptitudeobservable  (login,date_histo,requete) VALUES (#login, timestamp(now()),#requete)"; 
			insert_structure = insert_structure.replaceAll("#login", "'"+login+"'");
			insert_structure = insert_structure.replaceAll("#requete", '"'+requete+'"');

			//System.out.println(insert_structure);	
			stmt.execute(insert_structure);
		} 
		
        catch ( SQLException e ) {

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

	}
	/**
	 * Methode utilise dans l'écran rattachement competence vs aptitudes observables
	 * @param inputStream
	 * @return
	 * @throws SQLException 
	 * @throws InterruptedException 
	 */
	
	@SuppressWarnings("resource")
	public String uploadXLSXPosteTravailAptitude(InputStream inputStream) throws SQLException, InterruptedException{
		String retour=new String();
		List <PosteVsAptitudeBean> listPostVsAptBean=new ArrayList<PosteVsAptitudeBean>();


		// Creer l'objet representant le fichier Excel
		try 
		{
			XSSFWorkbook fichierExcel = new XSSFWorkbook(inputStream);
			//lecture de la première feuille excel
			XSSFSheet feuilleExcel=fichierExcel.getSheet(Contantes.onglet_posteTravailAptitude);

			//parcourirFeuilleXls(feuilleExcel);
			listPostVsAptBean=parcourirFeuilleXlsMass(feuilleExcel);
			if (deletePostApt()){
				addPosteTravailAptitude(listPostVsAptBean);
				bulkHistoPostAptitude(listPostVsAptBean);
			}
			

		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return retour;
	}
	/**
	 * Methode utilise dans l'écran chargement de masse
	 * @param inputStream
	 * @return
	 */
	@SuppressWarnings("resource")
	public List <PosteVsAptitudeBean> uploadXLSXPosteTravailAptitudeMass(InputStream inputStream){
		String retour=new String();
		List <PosteVsAptitudeBean> listPostVsAptBean=new ArrayList<PosteVsAptitudeBean>();


		// Creer l'objet representant le fichier Excel
		try 
		{
			XSSFWorkbook fichierExcel = new XSSFWorkbook(inputStream);
			//lecture de la première feuille excel
			XSSFSheet feuilleExcel=fichierExcel.getSheet(Contantes.onglet_posteTravailAptitude);

			listPostVsAptBean=parcourirFeuilleXlsMass(feuilleExcel);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return listPostVsAptBean;
	}

	/**
	 * 
	 * @param feuilleExcel
	 */
	public void parcourirFeuilleXls(XSSFSheet feuilleExcel){
		// creer l'objet representant les lignes Excel
		XSSFRow ligne;

		
		int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();

		HashMap<Integer, String> mapPosteTravail=new HashMap<Integer, String>();
		for(int numLigne =0;numLigne<=nombreLigne; numLigne++){
			
			ligne = feuilleExcel.getRow(numLigne);
			if(ligne!=null){
			
				int nombreColonne = ligne.getLastCellNum()- ligne.getFirstCellNum();
				// parcours des colonnes de la ligne en cours
				
				
				if(numLigne==0){
					mapPosteTravail=getcodePosteFromXLS(ligne, nombreColonne);
				}
				else{
					chargerBaseFromXls(ligne,  numLigne,nombreColonne, mapPosteTravail);
				}
			}//fin if ligne!=null
		}//fin for ligne
	}
	
	
	/**
	 *  Methode utilise dans l'écran chargement de masse
	 * @param feuilleExcel
	 */
	public List <PosteVsAptitudeBean> parcourirFeuilleXlsMass(XSSFSheet feuilleExcel){
		// creer l'objet representant les lignes Excel
		XSSFRow ligne;
		List <PosteVsAptitudeBean> listPostVsAptBean2=new ArrayList<PosteVsAptitudeBean>();
		List <PosteVsAptitudeBean> listPostVsAptBean=new ArrayList<PosteVsAptitudeBean>();


		
		int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();

		HashMap<Integer, String> mapPosteTravail=new HashMap<Integer, String>();
		for(int numLigne =0;numLigne<=nombreLigne; numLigne++){
			
			ligne = feuilleExcel.getRow(numLigne);
			if(ligne!=null){
			
				int nombreColonne = ligne.getLastCellNum()- ligne.getFirstCellNum();
				// parcours des colonnes de la ligne en cours
				
				
				if(numLigne==0){
					mapPosteTravail=getcodePosteFromXLS(ligne, nombreColonne);
				}
				else{
					listPostVsAptBean=chargerBaseFromXlsMass(ligne,  numLigne,nombreColonne, mapPosteTravail);
					listPostVsAptBean2.addAll(listPostVsAptBean);
				}
			}//fin if ligne!=null
		}//fin for ligne
		
		return listPostVsAptBean2;
	}
	
	/**
	 * 
	 * @param ligne
	 * @param nombreColonne
	 * @return
	 */
	private HashMap<Integer, String> getcodePosteFromXLS(XSSFRow ligne, int nombreColonne){
		HashMap<Integer, String>  retour=new HashMap<Integer, String>();
		XSSFCell cellule;
		for (int numColonne = 7; numColonne < nombreColonne; numColonne++) {
			cellule = ligne.getCell(numColonne);
			try
			{
				String valeur=cellule.getStringCellValue();
				if(valeur!=null){
					String val[]=valeur.split(",");
					retour.put(numColonne, val[0]);
				}
				//date2=cellule.getStringCellValue();
			}
			catch(Exception e)
			{
				System.out.println("erreur lors de la lecture de l'entete   numColonne="+numColonne +" valeur saiaie="+cellule.toString()+ " numero de ligne= 0");
				try {
					throw new Exception (e);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return retour;
	}
	
	/**
	 * 
	 * @param ligne
	 * @param numLigne
	 * @param nombreColonne
	 * @param mapPosteTravail
	 */
	private void chargerBaseFromXls(XSSFRow ligne, int numLigne,int nombreColonne, HashMap<Integer, String> mapPosteTravail){
		
		// creer l'objet representant les cellules Excel
				XSSFCell cellule;
		short numColonne=-1;
		
		String idRepertoireCompetence="";
		String codeCompetence="";
		
		while( (numColonne < nombreColonne) ){
			numColonne++;
			cellule = ligne.getCell(numColonne);

				String codePoste="";
				
				if(cellule!=null)
				{							
					if((numColonne==0)) //id_ repertoire competence
					{
						try
						{
							//idRepertoireCompetence=cellule.getStringCellValue();
							idRepertoireCompetence=Integer.toString((int)cellule.getNumericCellValue());

						}
						catch(Exception e)
						{
							System.out.println("erreur lors de la lecture d'une colonne date numColonne="+numColonne +" valeur saiaie="+cellule.toString()+ " numero de ligne= "+numLigne);
							try {
								throw new Exception (e);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					else
					{
						if((numColonne==1)) //code competence
						{
							try
							{
								codeCompetence=cellule.getStringCellValue();
								//date2=cellule.getStringCellValue();
							}
							catch(Exception e)
							{
								System.out.println("erreur lors de la lecture d'une colonne date numColonne="+numColonne +" valeur saiaie="+cellule.toString()+ " numero de ligne= "+numLigne);
								try {
									throw new Exception (e);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
						else{
							if((numColonne>=7)) //poste de travail
							{
								try
								{
									String selection=cellule.getStringCellValue();
									if("1".equalsIgnoreCase(selection)|| "x".equalsIgnoreCase(selection)){
										//codePoste=mapPosteTravail.get(numColonne);
										codePoste=mapPosteTravail.get(new Integer(numColonne).intValue());
										//insertion ligne dans la base de données
										insererCheckedPoteTravailCompetenceAptitudeObs(Integer.parseInt(idRepertoireCompetence),codeCompetence,codePoste);
										
										
										
										
										
									}
								}
								catch(Exception e)
								{
									System.out.println("erreur lors de la lecture d'une colonne date numColonne="+numColonne +" valeur saiaie="+cellule.toString()+ " numero de ligne= "+numLigne);
									try {
										throw new Exception (e);
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						}
					}
				}//if cellule!=null
			
				
		}//fin for colonne
	}
	
	
	/**
	 * 	 *  Methode utilise dans l'écran chargement de masse

	 * @param ligne
	 * @param numLigne
	 * @param nombreColonne
	 * @param mapPosteTravail
	 */
	private  List <PosteVsAptitudeBean> chargerBaseFromXlsMass(XSSFRow ligne, int numLigne,int nombreColonne, HashMap<Integer, String> mapPosteTravail){
		
		// creer l'objet representant les cellules Excel
		ArrayList <PosteVsAptitudeBean> listPostVsAptBean=new ArrayList<PosteVsAptitudeBean>();
		XSSFCell cellule;
		short numColonne=-1;
		
		String idRepertoireCompetence="";
		String codeCompetence="";
		
		while( (numColonne < nombreColonne) ){
			numColonne++;
			cellule = ligne.getCell(numColonne);
			PosteVsAptitudeBean postVsAptbean=new PosteVsAptitudeBean();


				String codePoste="";
				
				if(cellule!=null)
				{							
					if((numColonne==0)) //id_ repertoire competence
					{
						try
						{
							//idRepertoireCompetence=String.valueOf((int)cellule.getNumericCellValue());
							idRepertoireCompetence=Integer.toString((int)cellule.getNumericCellValue());
						}
						catch(Exception e)
						{
							System.out.println("erreur lors de la lecture d'une colonne date numColonne="+numColonne +" valeur saiaie="+cellule.toString()+ " numero de ligne= "+numLigne);
							try {
								throw new Exception (e);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					else
					{
						if((numColonne==1)) //code competence
						{
							try
							{
								codeCompetence=cellule.getStringCellValue();
								//date2=cellule.getStringCellValue();
							}
							catch(Exception e)
							{
								System.out.println("erreur lors de la lecture d'une colonne date numColonne="+numColonne +" valeur saiaie="+cellule.toString()+ " numero de ligne= "+numLigne);
								try {
									throw new Exception (e);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
						else{
							if((numColonne>=7)) //poste de travail
							{
								try
								{
									String selection=cellule.getStringCellValue();
									if("1".equalsIgnoreCase(selection)|| "x".equalsIgnoreCase(selection)){
										codePoste=mapPosteTravail.get(new Integer(numColonne).intValue());
										//insertion ligne dans la base de données
										postVsAptbean.setCode_competence(codeCompetence);
										postVsAptbean.setIdRepCompetence(Integer.parseInt(idRepertoireCompetence));
										postVsAptbean.setCode_poste(codePoste);
										//insererCheckedPoteTravailCompetenceAptitudeObs(idRepertoireCompetence,codeCompetence,codePoste);
										//System.out.println(idRepertoireCompetence+";"+codeCompetence+";"+codePoste);
										listPostVsAptBean.add(postVsAptbean);	

									}
								}
								catch(Exception e)
								{
									System.out.println("erreur lors de la lecture d'une colonne date numColonne="+numColonne +" valeur saiaie="+cellule.toString()+ " numero de ligne= "+numLigne);
									try {
										throw new Exception (e);
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						}
					}
				}//if cellule!=null
			
		}//fin for colonne
		
		return listPostVsAptBean;
	}
	public void insererCheckedPoteTravailCompetenceAptitudeObs(int idRepertoireCompetence, String codeCompetence, String codePoste)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;

		try
		{


				//mise à jour de la table poste_travail_competence
				/*****************************************/
				try 
				{
					stmt = (Statement) conn.createStatement();
					String insert_structure="INSERT INTO  poste_travail_comptence_aptitudeobservable  (code_poste,code_competence, id_repertoire_competence) VALUES (#code_poste, #code_competence,#code_aptitude)"; 
					insert_structure = insert_structure.replaceAll("#code_poste", "'"+codePoste+"'");
					insert_structure = insert_structure.replaceAll("#code_competence", "'"+codeCompetence+"'");
					insert_structure = insert_structure.replaceAll("#code_aptitude", "'"+idRepertoireCompetence+"'");
					System.out.println(insert_structure);	
					stmt.execute(insert_structure);
					String requete="INSERT-->"+codePoste+"/"+codeCompetence+"/"+idRepertoireCompetence;
					histoPostAptitude(requete);
				} 
				catch (SQLException e) 
				{

					e.printStackTrace();
					//return false;


				}

		}
		catch ( Exception e ) {

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

	}
	
	
	public void insererCheckedPoteTravailCompetenceAptitudeObsMass(String idRepertoireCompetence, String codeCompetence, String codePoste)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;

		try
		{


				//mise à jour de la table poste_travail_competence
				/*****************************************/
				try 
				{
					stmt = (Statement) conn.createStatement();
					String insert_structure="INSERT INTO  poste_travail_comptence_aptitudeobservable  (code_poste,code_competence, id_repertoire_competence) VALUES (#code_poste, #code_competence,#code_aptitude)"; 
					insert_structure = insert_structure.replaceAll("#code_poste", "'"+codePoste+"'");
					insert_structure = insert_structure.replaceAll("#code_competence", "'"+codeCompetence+"'");
					insert_structure = insert_structure.replaceAll("#code_aptitude", "'"+idRepertoireCompetence+"'");
					System.out.println(insert_structure);	
					stmt.execute(insert_structure);
					String requete="INSERT-->"+codePoste+"/"+codeCompetence+"/"+idRepertoireCompetence;
					histoPostAptitude(requete);
				} 
				catch (SQLException e) 
				{

					e.printStackTrace();
					//return false;


				}

		}
		catch ( Exception e ) {

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

	}
	
	
	/**
	 * récupération de la liste des poste de travail
	 * @return
	 */
	public List<PosteVsAptitudeBean> loadPosteVsAptitude()
	{
		List<PosteVsAptitudeBean> listposteTravail = new ArrayList<PosteVsAptitudeBean>();
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select code_poste, code_competence, id_repertoire_competence from poste_travail_comptence_aptitudeobservable";

			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				
					PosteVsAptitudeBean bean = new PosteVsAptitudeBean();

					bean.setIdRepCompetence( rs.getInt( "id_repertoire_competence" ) );
					bean.setCode_poste( rs.getString( "code_poste" ) );
					bean.setCode_competence( rs.getString( "code_competence" ) );
					listposteTravail.add( bean );

				

			}
			conn.close();
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
		return listposteTravail;	

	}
	
	
	/**
	 * cette méthode permet de vérifier l'integrite des données et retourne les données rejetés
	 * @return
	 */


	public HashMap <String,List<PosteVsAptitudeBean>> ChargementDonneedansBdd(List <PosteVsAptitudeBean> liste)throws Exception
	{
		//Verification de l'integrité des données à inserer doublon dans le fichier
		List <PosteVsAptitudeBean> listeAInserer=new ArrayList <PosteVsAptitudeBean>();
		List <PosteVsAptitudeBean> listeDonneesRejetes=new ArrayList <PosteVsAptitudeBean>();


		for(int i=0; i<liste.size();i++)
		{
			PosteVsAptitudeBean donnee=liste.get(i);
			boolean donneerejete=false;
			for(int j=i+1;j<liste.size();j++)
			{
				PosteVsAptitudeBean donnee2=liste.get(j);
				if(donnee.getIdRepCompetence()==(donnee2.getIdRepCompetence())&& donnee.getCode_poste().equalsIgnoreCase(donnee2.getCode_poste()))
				{
					donnee.setCauseRejet("le code "+ donnee.getIdRepCompetence()+" ou "+donnee.getCode_poste()+"existe déja ou en doublon dans le fichier à inserer");
					listeDonneesRejetes.add(donnee);
					donneerejete=true;
					
				}
			}
			if((i==liste.size()-1)||(i==0)||(donneerejete==false))
				listeAInserer.add(donnee);
			
		}
		
		
		//Verification de l'integrité des données à inserer doublon avec les données de la base
		
		List <PosteVsAptitudeBean> listeAInsererFinal=new ArrayList <PosteVsAptitudeBean>();
		@SuppressWarnings("unchecked")
		List<PosteVsAptitudeBean>fichePostebdd =loadPosteVsAptitude();
		Iterator <PosteVsAptitudeBean>iterator=listeAInserer.iterator();
		
		while(iterator.hasNext())
		{
			
			PosteVsAptitudeBean bean=(PosteVsAptitudeBean)iterator.next();
			
			Iterator<PosteVsAptitudeBean> index=fichePostebdd.iterator();
			boolean donneerejete=false;
			while(index.hasNext())
			{
				
				PosteVsAptitudeBean bean2=(PosteVsAptitudeBean)index.next();
				if(bean.getCode_poste().equalsIgnoreCase(bean2.getCode_poste())&& bean.getIdRepCompetence()==(bean2.getIdRepCompetence()) )
				{
					bean.setCauseRejet("le code "+ bean.getIdRepCompetence()+" ou "+bean.getCode_poste()+"existe déja ou en doublon dans la base de données");;
					listeDonneesRejetes.add(bean);
					donneerejete=true;
					continue;
				}
			}
			if(!donneerejete)
			{
				
				listeAInsererFinal.add(bean);
			}
			
		}
		//Insertion des données dans la table Structure_entreprise
		Iterator<PosteVsAptitudeBean> index =listeAInsererFinal.iterator();
		String requete="";
		while(index.hasNext())
		{
			PosteVsAptitudeBean donneeBean=(PosteVsAptitudeBean)index.next();
			boolean retour=ConstructionRequeteAddCompagne(donneeBean);
			
			//addPosteTravail(donneeBean);			
		}
		//execution de la requete
		/*try
		{
			//System.out.println(requete);
			if(requete!="")
				updateMultiQuery(requete);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}*/
		HashMap <String,List<PosteVsAptitudeBean>> donneeMap=new HashMap<String,List<PosteVsAptitudeBean>>();
		donneeMap.put("inserer", listeAInsererFinal);
		donneeMap.put("supprimer", listeDonneesRejetes);
		return donneeMap;
	}




	public boolean ConstructionRequeteAddCompagne(PosteVsAptitudeBean addedData) throws SQLException 
	{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();

		PreparedStatement pstmt = null;
		boolean result=true;
		try {

			String requete_Sql="INSERT INTO  poste_travail_comptence_aptitudeobservable  (code_poste,code_competence, id_repertoire_competence)"
					+ " values (?,?,?)";
			pstmt = conn.prepareStatement(requete_Sql);

			
			pstmt.setString(1, addedData.getCode_poste());
			pstmt.setString(2, addedData.getCode_competence());
			pstmt.setInt(3, addedData.getIdRepCompetence());


			//System.out.println(pstmt.toString());




			int row= pstmt.executeUpdate();
			if (row>0){
				//System.out.println(pstmt.toString());
				result=true;
			}else
				result=false;
			//pstmt.close();conn.close();

		} catch ( SQLException e ) {
			e.printStackTrace();
			System.out.println("Exception ors de l'insertion du poste de travail avecla requete  "+pstmt);
			result=false;

		} finally {

			if ( pstmt != null ) {
				try {
					pstmt.close();
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
	
	
	
	
	public boolean addPosteTravailAptitude(List<PosteVsAptitudeBean> addedData) throws SQLException, InterruptedException 
	{

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		PreparedStatement pstmt=null;
		boolean result=false;
		
		
		try {
		    // Disable auto-commit
			conn.setAutoCommit(false);

		    // Create a prepared statement
		    
		    
		    String requete_Sql="INSERT INTO  poste_travail_comptence_aptitudeobservable  (code_poste,code_competence, id_repertoire_competence)"
					+ " values (?,?,?)";
		    pstmt = conn.prepareStatement(requete_Sql);
		    
		   
		    
		  //Insertion des données dans la table Structure_entreprise
			Iterator<PosteVsAptitudeBean> index =addedData.iterator();
			String requete="";
			while(index.hasNext()){
				PosteVsAptitudeBean donneeBean=(PosteVsAptitudeBean)index.next();
				
				pstmt.setString(1, donneeBean.getCode_poste());
				pstmt.setString(2, donneeBean.getCode_competence());
				pstmt.setInt(3, donneeBean.getIdRepCompetence());
				pstmt.addBatch();
			}

		    // Execute the batch
		    int [] updateCounts = pstmt.executeBatch();

		    // All statements were successfully executed.
		    // updateCounts contains one element for each batched statement.
		    // updateCounts[i] contains the number of rows affected by that statement.
		    int total_update=processUpdateCounts(updateCounts);

		    // Since there were no errors, commit
		    conn.commit();
		    System.out.println("Nombre de ligne insérées dans poste_travail_comptence_aptitudeobservable : "+total_update);
			Messagebox.show("Nombre de lignes insérées : "+total_update, "Information",Messagebox.OK, Messagebox.INFORMATION); 
		    result=true;
		} catch (BatchUpdateException e) {
		    // Not all of the statements were successfully executed
		    int[] updateCounts = e.getUpdateCounts();

		    // Some databases will continue to execute after one fails.
		    // If so, updateCounts.length will equal the number of batched statements.
		    // If not, updateCounts.length will equal the number of successfully executed statements
		    processUpdateCounts(updateCounts);

		    // Either commit the successfully executed statements or rollback the entire batch
		    conn.rollback();
		} catch (SQLException e) {
		}finally {

			if ( pstmt != null ) {
				try {
					pstmt.close();
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
	
	
	
	public static int processUpdateCounts(int[] updateCounts) {
		
		int resultat_update=0;
		
	    for (int i=0; i<updateCounts.length; i++) {
	    	if (updateCounts[i] >= 0) {
	    		// Successfully executed; the number represents number of affected rows
	    		resultat_update=resultat_update+updateCounts[i];
	    	} else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
	    		// Successfully executed; number of affected rows not available
	    	} else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
	    		// Failed to execute
	    	}
	    }
	    
	    return resultat_update;
	}

	/**
	 * cette classe permet de supprimer une donnée de la table structure_entreprise
	 * @param codeStructure
	 * @throws SQLException 
	 */
	public Boolean deletePostApt() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		Boolean retour=false;
		try 
		{
			stmt = (Statement) conn.createStatement();
           String sql_query=" delete from  poste_travail_comptence_aptitudeobservable ";
			
			
		
			
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
	
	

	/**
	 * historisation des modifications sur l'ecran rattachement poste travail aptitudes observables
	 * @throws SQLException 
	 * 
	 */

	public boolean bulkHistoPostAptitude(List<PosteVsAptitudeBean> addedData) throws SQLException	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		PreparedStatement pstmt=null;
		boolean result=false;
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		String login=applicationSession.getCompteUtilisateur().getLogin();
		
		try{
			conn.setAutoCommit(false);
			 // Create a prepared statement
		    
			String requete_Sql="INSERT INTO  histo_poste_trav_aptitudeobservable  (login,date_histo,requete) VALUES (?, timestamp(now()),?)"; 

		 
		    pstmt = conn.prepareStatement(requete_Sql);
		    
		   
		    
		  //Insertion des données dans la table Structure_entreprise
			Iterator<PosteVsAptitudeBean> index =addedData.iterator();
			String requete="";
			while(index.hasNext()){
				PosteVsAptitudeBean donneeBean=(PosteVsAptitudeBean)index.next();
				pstmt.setString(1,login);
				pstmt.setString(2, "BULK INSERT -->"+donneeBean.getCode_poste()+"/"+donneeBean.getCode_competence()+"/"+donneeBean.getIdRepCompetence() );
				
				pstmt.addBatch();
			}

		    // Execute the batch
		    int [] updateCounts = pstmt.executeBatch();

		    // All statements were successfully executed.
		    // updateCounts contains one element for each batched statement.
		    // updateCounts[i] contains the number of rows affected by that statement.
		    int total_update=processUpdateCounts(updateCounts);

		    // Since there were no errors, commit
		    conn.commit();
		    System.out.println("Nombre de ligne insérées dans histo_poste_trav_aptitudeobservable : "+total_update);
		    result=true;
		}catch (BatchUpdateException e) {
		    // Not all of the statements were successfully executed
		    int[] updateCounts = e.getUpdateCounts();

		    // Some databases will continue to execute after one fails.
		    // If so, updateCounts.length will equal the number of batched statements.
		    // If not, updateCounts.length will equal the number of successfully executed statements
		    processUpdateCounts(updateCounts);

		    // Either commit the successfully executed statements or rollback the entire batch
		    conn.rollback();
		} catch (SQLException e) {
		}finally {

			if ( pstmt != null ) {
				try {
					pstmt.close();
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
}
