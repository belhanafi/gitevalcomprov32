package Statistique.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.zkoss.zk.ui.Sessions;

import common.ApplicationFacade;
import common.ApplicationSession;
import common.CreateDatabaseCon;
import common.InitContext;
import Statistique.bean.EmployeCadreBean;
import Statistique.bean.EmployeMoyFamBean;
import Statistique.bean.StatEvolIMGBean;
import Statistique.bean.StatEvolIMIEmployeBean;
import Statistique.bean.StatIndiceMoyPosteBean;
import Statistique.bean.StatMoyFamillePosteBean;
import Statistique.bean.StatCotationEmployeBean;

public class StatCotationEmployeModel {

	private int database=ApplicationFacade.getInstance().getClient_database_id();


	public ArrayList<StatCotationEmployeBean> InitialiserStatCotationEmploye()
	{

		ArrayList<StatCotationEmployeBean> liste=new ArrayList<StatCotationEmployeBean>();
		StatCotationEmployeBean statBean1=new StatCotationEmployeBean();
		statBean1.setNomEmploye("Helene BAUM");
		HashMap<String, Double> listeCotations3=new HashMap<String, Double>();
		listeCotations3.put("Affaire", new Double(3.00));
		listeCotations3.put("Relationnelle", new Double(2.5));
		listeCotations3.put("Personnelle", new Double(1.7));
		listeCotations3.put("Opérationnelle", new Double(1.00));

		HashMap<String, Double> listeCotations11=new HashMap<String, Double>();
		listeCotations11.put("Affaire", new Double(1.00));
		listeCotations11.put("Relationnelle", new Double(2.0));
		listeCotations11.put("Personnelle", new Double(2));
		listeCotations11.put("Opérationnelle", new Double(0.5));

		HashMap<String, Double> listeCotations21=new HashMap<String, Double>();
		listeCotations21.put("Affaire", new Double(3.00));
		listeCotations21.put("Relationnelle", new Double(2.5));
		listeCotations21.put("Personnelle", new Double(2));
		listeCotations21.put("Opérationnelle", new Double(1.00));

		HashMap<String, HashMap<String, Double>> listeCompagnes1=new HashMap<String, HashMap<String, Double>>();
		listeCompagnes1.put("Compagne1", listeCotations3);
		listeCompagnes1.put("Compagne2", listeCotations11);
		listeCompagnes1.put("Compagne3", listeCotations21);

		statBean1.setCompagne_listCotation(listeCompagnes1);


		liste.add(statBean1);
		StatCotationEmployeBean statBean=new StatCotationEmployeBean();
		statBean.setNomEmploye("Lilian DAUTAIS");
		HashMap<String, Double> listeCotations=new HashMap<String, Double>();
		listeCotations.put("Affaire", new Double(2.00));
		listeCotations.put("Relationnelle", new Double(3.5));
		listeCotations.put("Personnelle", new Double(2));
		listeCotations.put("Opérationnelle", new Double(1.00));

		HashMap<String, Double> listeCotations1=new HashMap<String, Double>();
		listeCotations1.put("Affaire", new Double(1.00));
		listeCotations1.put("Relationnelle", new Double(2.5));
		listeCotations1.put("Personnelle", new Double(2));
		listeCotations1.put("Opérationnelle", new Double(0.5));

		HashMap<String, Double> listeCotations2=new HashMap<String, Double>();
		listeCotations2.put("Affaire", new Double(1.00));
		listeCotations2.put("Relationnelle", new Double(1.5));
		listeCotations2.put("Personnelle", new Double(2));
		listeCotations2.put("Opérationnelle", new Double(1.00));

		HashMap<String, HashMap<String, Double>> listeCompagnes=new HashMap<String, HashMap<String, Double>>();
		listeCompagnes.put("Compagne1", listeCotations);
		listeCompagnes.put("Compagne2", listeCotations1);
		listeCompagnes.put("Compagne3", listeCotations2);

		statBean.setCompagne_listCotation(listeCompagnes);
		liste.add(statBean);
		return liste;
	}


	public Map getListEmployesFichValid(String code_structure) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		String sql_query="";
		ResultSet rs=null;
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		//TODo supprimer cette ligne ApplicatinFacade
		//		int id_profile=ApplicationFacade.getInstance().getCompteUtilisateur().getId_profile();
		//		int id_evaluateur=ApplicationFacade.getInstance().getCompteUtilisateur().getId_compte();

		int id_profile=applicationSession.getCompteUtilisateur().getId_profile();
		int id_evaluateur=applicationSession.getCompteUtilisateur().getId_compte();
		final InitContext intctx = new InitContext();
		intctx.loadProperties();

		try 
		{
			stmt = (Statement) conn.createStatement();
			if (id_profile==1 || id_profile==2 ){

				if (code_structure.equalsIgnoreCase("-1")){

					sql_query="select e.id_employe,concat(e.nom ,' ' ,e.prenom) as nom from fiche_validation f ,employe e " +
							" where f.id_employe=e.id_employe and f.fiche_valide=1 and f.id_planning_evaluation" +
							" in (select distinct id_planning_evaluation  from planning_evaluation )" ;
				}else{

					sql_query="select e.id_employe,concat(e.nom ,' ' ,e.prenom) as nom from fiche_validation f ,employe e " +
							" where f.id_employe=e.id_employe and f.fiche_valide=1 and f.id_planning_evaluation" +
							" in (select distinct id_planning_evaluation  from planning_evaluation )" +
							" and e.code_structure =#code_structure order by concat(e.nom ,' ' ,e.prenom) ";

					sql_query = sql_query.replaceAll("#code_structure", "'"+ code_structure+"'");

				}

			}

			else{

				if (code_structure.equalsIgnoreCase("-1")){
					sql_query="select e.id_employe,concat(e.nom ,' ' ,e.prenom) as nom from fiche_validation f ,employe e " +
							" where f.id_employe=e.id_employe and f.fiche_valide=1 and f.id_planning_evaluation" +
							" in (select id_planning_evaluation  from planning_evaluation compagne_evaluation" +
							"  where  id_evaluateur in (select id_employe from employe where id_compte=#id_evaluateur))" ;

					sql_query = sql_query.replaceAll("#id_evaluateur", "'"+id_evaluateur+"'");
				}else{

					sql_query="select e.id_employe,concat(e.nom ,' ' ,e.prenom) as nom from fiche_validation f ,employe e " +
							" where f.id_employe=e.id_employe and f.fiche_valide=1 and f.id_planning_evaluation" +
							" in (select id_planning_evaluation  from planning_evaluation compagne_evaluation" +
							"  where  id_evaluateur in (select id_employe from employe where id_compte=#id_evaluateur))" +
							" and e.code_structure =#code_structure order by concat(e.nom ,' ' ,e.prenom) ";

					sql_query = sql_query.replaceAll("#code_structure", "'"+ code_structure+"'");
					sql_query = sql_query.replaceAll("#id_evaluateur", "'"+id_evaluateur+"'");
				}


				//sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");
			}


			//System.out.println(sql_query);

			rs = (ResultSet) stmt.executeQuery(sql_query);


			while(rs.next()){
				map.put(rs.getString("nom"), rs.getString("id_employe"));
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

		Map<String, String> treeMap = new TreeMap<String, String>(map);
		return  treeMap;
	}


	public Map getListEmployesFichValid(String id_compagne, String code_structure) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		String sql_query="";
		ResultSet rs=null;
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		//TODO supprimer cette ligne ApplicationFacade
		//		int id_profile=ApplicationFacade.getInstance().getCompteUtilisateur().getId_profile();
		//		int id_evaluateur=ApplicationFacade.getInstance().getCompteUtilisateur().getId_compte();

		int id_profile=applicationSession.getCompteUtilisateur().getId_profile();
		int id_evaluateur=applicationSession.getCompteUtilisateur().getId_compte();
		final InitContext intctx = new InitContext();
		intctx.loadProperties();

		try 
		{
			stmt = (Statement) conn.createStatement();
			if (id_profile==1 || id_profile==2 ){

				if (code_structure.equalsIgnoreCase("-1")){
					sql_query="select e.id_employe,concat(e.nom ,' ' ,e.prenom) as nom from fiche_validation f ,employe e " +
							"  where f.id_employe=e.id_employe and f.fiche_valide=1 and f.id_planning_evaluation" +
							"  in (select id_planning_evaluation  from planning_evaluation where id_compagne=#id_compagne) order by concat(e.nom ,' ' ,e.prenom) " ;

					sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");
				}else{
					sql_query="select e.id_employe,concat(e.nom ,' ' ,e.prenom) as nom from fiche_validation f ,employe e " +
							"  where f.id_employe=e.id_employe and f.fiche_valide=1 and f.id_planning_evaluation" +
							"  in (select id_planning_evaluation  from planning_evaluation where id_compagne=#id_compagne)" +
							" and e.code_structure =#code_structure order by concat(e.nom ,' ' ,e.prenom) ";
					sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");
					sql_query = sql_query.replaceAll("#code_structure", "'"+ code_structure+"'");

				}

			}

			else{
				if (code_structure.equalsIgnoreCase("-1")){
					sql_query="select e.id_employe,concat(e.nom ,' ' ,e.prenom) as nom from fiche_validation f ,employe e " +
							"  where f.id_employe=e.id_employe and f.fiche_valide=1 and f.id_planning_evaluation in (select id_planning_evaluation  from planning_evaluation" +
							"  where id_compagne=#id_compagne  and id_evaluateur in (select id_employe from employe where id_compte=#id_evaluateur)) order by concat(e.nom ,' ' ,e.prenom)" ;

					sql_query = sql_query.replaceAll("#id_evaluateur", "'"+id_evaluateur+"'");
					sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");

				}else{
					sql_query="select e.id_employe,concat(e.nom ,' ' ,e.prenom) as nom from fiche_validation f ,employe e " +
							" where f.id_employe=e.id_employe and f.fiche_valide=1 and f.id_planning_evaluation in (select id_planning_evaluation  from planning_evaluation" +
							" where id_compagne=#id_compagne  and id_evaluateur in (select id_employe from employe where id_compte=#id_evaluateur))" +
							" and e.code_structure =#code_structure order by concat(e.nom ,' ' ,e.prenom)";

					sql_query = sql_query.replaceAll("#id_evaluateur", "'"+id_evaluateur+"'");
					sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");
					sql_query = sql_query.replaceAll("#code_structure", "'"+ code_structure+"'");


				}

			}

			rs = (ResultSet) stmt.executeQuery(sql_query);
			//System.out.println(sql_query);

			while(rs.next()){
				map.put(rs.getString("nom"), rs.getString("id_employe"));
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

		Map<String, String> treeMap = new TreeMap<String, String>(map);
		return  treeMap;
	}

	public HashMap getListCompagneValid() throws SQLException
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
					" from compagne_evaluation e 	where e.id_compagne in (select id_compagne from compagne_validation where compagne_valide=1)";
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

	public List getListEmployesMoyFam(String id_employe) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		List listmoyfam = new ArrayList<EmployeMoyFamBean>();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select  r.famille,round(moy_par_famille,2) as moy_par_famille,round(imi,2) as imi from imi_stats s ,repertoire_competence r where id_employe=#id_employe" +
					" and s.code_famille=r.code_famille group by  r.famille";

			sql_query = sql_query.replaceAll("#id_employe", "'"+id_employe+"'");

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				EmployeMoyFamBean bean=new EmployeMoyFamBean();
				bean.setCode_famille((rs.getString("famille")));	
				bean.setMoy_famille((rs.getFloat("moy_par_famille")));
				bean.setImi((rs.getFloat("imi")));
				listmoyfam.add(bean);

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

		return listmoyfam;
	}


	public List getEvolIMIEmploye(String id_employe) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		List listbean = new ArrayList<StatEvolIMIEmployeBean>();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select DATE_FORMAT(c.date_debut ,' %b %Y ')as date_eval, round(imi,2) imi from imi_stats s  ,compagne_evaluation c" +
					" where s.id_compagne=c.id_compagne  and id_employe=#id_employe group by c.date_debut";


			sql_query = sql_query.replaceAll("#id_employe", "'"+id_employe+"'");

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				StatEvolIMIEmployeBean bean=new StatEvolIMIEmployeBean();
				bean.setDate_evol((rs.getString("date_eval")));	
				bean.setImi((rs.getFloat("imi")));
				listbean.add(bean);

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

		return listbean;
	}


	public List getEvolIMGPoste(String code_poste) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		List listbean = new ArrayList<StatEvolIMGBean>();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query=" select DATE_FORMAT(c.date_fin ,' %b %Y ')as date_eval, round(img,2) img from img_stats s  ,compagne_evaluation c" +
					"  where s.id_compagne=c.id_compagne  and code_poste=#code_poste group by c.date_fin ";

			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				StatEvolIMGBean bean=new StatEvolIMGBean();
				bean.setDate_evol(rs.getString("date_eval"));	
				bean.setImg(rs.getFloat("img"));
				listbean.add(bean);

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

		return listbean;
	}






	public HashMap getListPostTravailValid(String id_compagne) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select  distinct t.code_poste,t.intitule_poste from compagne_evaluation e, planning_evaluation p, poste_travail_description t" +
					" where e.id_compagne in (select id_compagne from compagne_validation where compagne_valide=1) " +
					" and p.id_compagne=e.id_compagne  and t.code_poste=p.code_poste and e.id_compagne=#id_compagne";

			sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");

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


	public HashMap getListPostTravailValidInt(String id_compagne) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select  distinct t.code_poste,t.intitule_poste from compagne_evaluation e, planning_evaluation p, poste_travail_description t" +
					" where e.id_compagne in (#id_compagne) " +
					" and p.id_compagne=e.id_compagne  and t.code_poste=p.code_poste and e.id_compagne=#id_compagne";

			sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");

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


	public HashMap getListPostTravailValid() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select  distinct t.code_poste,t.intitule_poste from compagne_evaluation e, planning_evaluation p, poste_travail_description t" +
					" where e.id_compagne in (select id_compagne from compagne_validation where compagne_valide=1) " +
					" and p.id_compagne=e.id_compagne  and t.code_poste=p.code_poste ";

			//sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");

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

	public List getListPosteMoyFam(String code_poste, String id_compagne) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		List listmoyfam = new ArrayList<StatMoyFamillePosteBean>();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select r.famille,round(moy_par_famille,2) as moy_par_famille from moy_poste_famille_stats m, repertoire_competence r" +
					" where r.code_famille=m.code_famille and  code_poste=#code_poste and id_compagne=#id_compagne group by  r.famille";

			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
			sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				StatMoyFamillePosteBean bean=new StatMoyFamillePosteBean();
				bean.setFamille((rs.getString("famille")));	
				bean.setMoy_famille((rs.getFloat("moy_par_famille")));
				listmoyfam.add(bean);

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

		return listmoyfam;
	}


	public List getListPosteMoyFamIntr(String code_poste,String id_compagne) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		List listmoyfam = new ArrayList<StatMoyFamillePosteBean>();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select r.famille,round(sum(moy_par_famille)/count(s.id_employe),2) as moy_par_famille" +
					" from imi_stats s ,employe e, fiche_validation f  ,repertoire_competence r where s.id_employe=e.id_employe and s.id_employe=f.id_employe" +
					" and fiche_valide=1 and s.id_compagne=#id_compagne and e.code_poste=#code_poste and r.code_famille=s.code_famille group by  r.famille";

			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
			sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				StatMoyFamillePosteBean bean=new StatMoyFamillePosteBean();
				bean.setFamille((rs.getString("famille")));	
				bean.setMoy_famille((rs.getFloat("moy_par_famille")));
				listmoyfam.add(bean);

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
		return listmoyfam;
	}

	public float getIMGParPoste(String code_poste) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		float result=0;
		ResultSet rs=null;

		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select round(img,2)as img from img_stats where code_poste=#code_poste" ;

			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){


				result=rs.getFloat("img");

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

	public float getIMGParPosteInter(String code_poste,String compagne_id) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		float result=0;
		ResultSet rs=null;

		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select  avg(moy_par_famille1)as img from (select code_famille,round(sum(moy_par_famille)/count(s.id_employe),2) as moy_par_famille1" +
					" from imi_stats s ,employe e, fiche_validation f where s.id_employe=e.id_employe and s.id_employe=f.id_employe" +
					"  and fiche_valide=1 and s.id_compagne=#compagne_id and e.code_poste=#code_poste group by code_famille) as t1" ;

			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
			sql_query = sql_query.replaceAll("#compagne_id", "'"+compagne_id+"'");

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){


				result=rs.getFloat("img");

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



	public HashMap getListFamille() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select distinct code_famille,famille from repertoire_competence";
			rs = (ResultSet) stmt.executeQuery(sql_query);


			while(rs.next()){
				map.put(rs.getString("famille"), rs.getString("code_famille"));
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

	public List getIndiceMoyPoste(String code_poste,String code_famille,String compagne_id) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		List listmoyfam = new ArrayList<StatIndiceMoyPosteBean>();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select distinct r.libelle_competence,moy_par_competence from moy_poste_competence_stats m , repertoire_competence r" +
					" where code_poste=#code_poste and m.code_famille=#code_famille and r.code_competence=m.code_competence and r.code_famille=m.code_famille"
					+ " and m.id_compagne=#compagne_id";

			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
			sql_query = sql_query.replaceAll("#code_famille", "'"+code_famille+"'");
			sql_query = sql_query.replaceAll("#compagne_id", "'"+compagne_id+"'");

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				StatIndiceMoyPosteBean bean=new StatIndiceMoyPosteBean();
				bean.setCompetence((rs.getString("libelle_competence")));	
				bean.setIndice_moy((rs.getFloat("moy_par_competence")));
				listmoyfam.add(bean);

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

		return listmoyfam;
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


	public float getIndiceMoyPerPoste(String code_poste,String code_famille) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		float result=0;
		ResultSet rs=null;

		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query=" select round(avg(moy_par_competence),2) as indice_moy from moy_poste_competence_stats where code_poste=#code_poste  and code_famille=#code_famille" ;

			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
			sql_query = sql_query.replaceAll("#code_famille", "'"+code_famille+"'");

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){


				result=rs.getFloat("indice_moy");

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

	public HashMap getListCompagneInter() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query=" select  id_compagne,concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) nomcompagne" +
					" from compagne_evaluation e where now() between e.date_debut and e.date_fin";

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
