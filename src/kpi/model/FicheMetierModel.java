package kpi.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.zkoss.zk.ui.Sessions;

import Statistique.bean.EmployeFormationBean;
import Statistique.bean.StatMoyFamillePosteBean;
import Statistique.bean.StatTrancheAgePosteBean;
import kpi.bean.AnalSynthRadarBean;
import kpi.bean.DureeMoyenneBean;
import kpi.bean.IMIMedianeBean;
import kpi.bean.IMIparVagueBean;
import kpi.bean.KPIAgeExpBean;
import kpi.bean.KpiIMIAgeBean;
import kpi.bean.KpiIMIExpBean;
import kpi.bean.MoyCompetenceBean;
import kpi.bean.StatExperienceBean;
import kpi.bean.StatPariteParPosteBean;
import kpi.bean.StatTypeContratBean;
import kpi.bean.SynGlobalPg1Ch1Bean;
import kpi.bean.TauxCouvertureCompagneBean;
import common.ApplicationFacade;
import common.ApplicationSession;
import common.CreateDatabaseCon;

public class FicheMetierModel {


	/**
	 * retourne  stats par tanches ages rapport BIRT
	 * @return
	 */


	public List getNombreEmployesParPoste() throws SQLException
	{

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();
		String code_poste=applicationSession.getCode_poste();

		ArrayList<StatTrancheAgePosteBean>   liststatbean = new ArrayList<StatTrancheAgePosteBean>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();


			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {



				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();


					select_structure="select p.intitule_poste, case  when (round( DATEDIFF(curdate(),e.date_naissance)/365))>18  and (round( DATEDIFF(curdate(),e.date_naissance)/365))<=25 THEN '<=25'"
							+ " when (round( DATEDIFF(curdate(),e.date_naissance)/365))>=26 and (round( DATEDIFF(curdate(),e.date_naissance)/365))<36 THEN '26 à 35 ans'"
							+ " when (round( DATEDIFF(curdate(),e.date_naissance)/365))>=36 and (round( DATEDIFF(curdate(),e.date_naissance)/365))<=44 THEN '36 à 44 ans'"
							+ " when (round( DATEDIFF(curdate(),e.date_naissance)/365))>=45 and (round( DATEDIFF(curdate(),e.date_naissance)/365))<=50 THEN '45 à 50 ans'"
							+ " when (round( DATEDIFF(curdate(),e.date_naissance)/365))>51   and (round( DATEDIFF(curdate(),e.date_naissance)/365))<=55 THEN '51 à 55 ans'"
							+ " when (round( DATEDIFF(curdate(),e.date_naissance)/365))>=56  and (round( DATEDIFF(curdate(),e.date_naissance)/365))<=60 THEN '56 à 60 ans' ELSE '>60 ans' END as tranche,"
							+ " round(count(e.code_poste)) as pourcentage from "+entry.getKey()+"."+"employe e ,"+entry.getKey()+"."+"poste_travail_description p"
							+ "  where p.code_poste=e.code_poste  and e.code_poste=#code_poste group by 1,2 order by 2";


					select_structure = select_structure.replaceAll("#code_poste", "'"+ code_poste+"'");
					//System.out.println(">>>"+query);

				}

			}







			//System.out.println(select_structure);

			rs = (ResultSet) stmt.executeQuery(select_structure);

			while(rs.next()){

				StatTrancheAgePosteBean stat_bean=new StatTrancheAgePosteBean();
				stat_bean.setIntitule_poste(rs.getString("intitule_poste"));
				stat_bean.setTranche(rs.getString("tranche"));
				stat_bean.setPourcentage(rs.getInt("pourcentage"));

				liststatbean.add(stat_bean);


			}



		} catch ( SQLException e ) {
			System.out.println(e.toString());

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
		return liststatbean;


	}

	/**
	 * retourne  stats par anncienté ages rapport BIRT
	 * @return
	 */


	public List getNombreEmployesParExperience() throws SQLException
	{

		String code_structure="";

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();
		String code_poste=applicationSession.getCode_poste();

		ArrayList<StatExperienceBean>   liststatbean = new ArrayList<StatExperienceBean>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();


			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();


					select_structure="select p.intitule_poste, case  when (round( DATEDIFF(curdate(),e.date_recrutement)/365))>1  and (round( DATEDIFF(curdate(),e.date_recrutement)/365))<=3 THEN '1 à 3 ans'"
							+ " when (round( DATEDIFF(curdate(),e.date_recrutement)/365))>=4 and (round( DATEDIFF(curdate(),e.date_recrutement)/365))<=6 THEN '4 à 6 ans'"
							+ " when (round( DATEDIFF(curdate(),e.date_recrutement)/365))>=7 and (round( DATEDIFF(curdate(),e.date_recrutement)/365))<=10 THEN '7 à 10 ans'"
							+ " when (round( DATEDIFF(curdate(),e.date_recrutement)/365))>=11 and (round( DATEDIFF(curdate(),e.date_recrutement)/365))<=15 THEN '11 à 15 ans'"
							+ " when (round( DATEDIFF(curdate(),e.date_recrutement)/365))>=16   and (round( DATEDIFF(curdate(),e.date_recrutement)/365))<=20 THEN '16 à 20 ans'"
							+ " when (round( DATEDIFF(curdate(),e.date_recrutement)/365))>=21   and (round( DATEDIFF(curdate(),e.date_recrutement)/365))<=25 THEN '21 à 25 ans'"
							+ " when (round( DATEDIFF(curdate(),e.date_recrutement)/365))>=26  and (round( DATEDIFF(curdate(),e.date_recrutement)/365))<=30 THEN '26 à 30 ans' ELSE '>30 ans' END as tranche,"
							+ " round(count(e.code_poste)) as pourcentage from "+entry.getKey()+"."+"employe e ,"+entry.getKey()+"."+"poste_travail_description p"
							+ "  where p.code_poste=e.code_poste  and e.code_poste=#code_poste group by 1,2 order by 2";


					select_structure = select_structure.replaceAll("#code_poste", "'"+ code_poste+"'");
					//System.out.println(">>>"+query);

				}

			}







			//System.out.println(select_structure);

			rs = (ResultSet) stmt.executeQuery(select_structure);

			while(rs.next()){

				StatExperienceBean stat_bean=new StatExperienceBean();
				stat_bean.setIntitule_poste(rs.getString("intitule_poste"));
				stat_bean.setTranche(rs.getString("tranche"));
				stat_bean.setPourcentage(rs.getInt("pourcentage"));

				liststatbean.add(stat_bean);


			}



		} catch ( SQLException e ) {
			System.out.println(e.toString());

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
		return liststatbean;


	}


	public List getStatPariteParPoste() throws SQLException
	{

		String code_structure="";

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();
		String code_poste=applicationSession.getCode_poste();
		ArrayList<StatPariteParPosteBean>   liststatbean = new ArrayList<StatPariteParPosteBean>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();

					sql_query="select n.sexe_lbl as sexe_lbl,count(e.id_employe) as nbemploye "
							+ " from "+entry.getKey()+"."+"employe e, "+entry.getKey()+"."+"sexe n  where e.code_sexe=n.code_sexe and e.code_poste=#code_poste group by sexe_lbl";

					sql_query = sql_query.replaceAll("#code_poste", "'"+ code_poste+"'");
					//System.out.println(">>>"+query);

				}

			}





			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				StatPariteParPosteBean stat_bean=new StatPariteParPosteBean();
				stat_bean.setSexe_lbl(rs.getString("sexe_lbl"));
				stat_bean.setNbemploye(rs.getInt("nbemploye"));

				liststatbean.add(stat_bean);


			}


		} catch ( SQLException e ) {
			System.out.println(e.toString());
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
		return liststatbean;


	}


	public List getNombreEmployesNivForm() throws SQLException
	{

		String code_structure="";

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();
		String code_poste=applicationSession.getCode_poste();
		ArrayList<EmployeFormationBean>   liststatbean = new ArrayList<EmployeFormationBean>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();


					sql_query="select d.niv_for_libelle as libelle_formation,count(e.id_employe) as pourcentage" +
							" from "+entry.getKey()+"."+"employe e,"+entry.getKey()+"."+"formation f, "+entry.getKey()+"."+"def_niv_formation d where f.code_formation=e.code_formation" +
							" and   d.niv_for_id=f.niv_for_id   and e.code_poste=#code_poste group by niv_for_libelle" ;


					sql_query = sql_query.replaceAll("#code_poste", "'"+ code_poste+"'");
					//System.out.println(">>>"+query);

				}

			}





			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				EmployeFormationBean stat_bean=new EmployeFormationBean();
				stat_bean.setNiveau(rs.getString("libelle_formation"));
				stat_bean.setPourcentage(rs.getInt("pourcentage"));

				liststatbean.add(stat_bean);


			}


		} catch ( SQLException e ) {
			System.out.println(e.toString());
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
		return liststatbean;


	}

	public List getStatTypeContrat() throws SQLException
	{

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();
		String code_poste=applicationSession.getCode_poste();
		ArrayList<StatTypeContratBean>   liststatbean = new ArrayList<StatTypeContratBean>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					
					sql_query="select n.type_contrat_lbl as type_contrat_lbl,count(e.id_employe) as nbemploye"
							+ " from "+entry.getKey()+"."+"employe e, "+entry.getKey()+"."+"type_contrat n  where e.code_contrat=n.code_contrat and e.code_poste=#code_poste group by type_contrat_lbl";


					sql_query = sql_query.replaceAll("#code_poste", "'"+ code_poste+"'");

				}

			}

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				StatTypeContratBean stat_bean=new StatTypeContratBean();
				stat_bean.setType_contrat_lbl(rs.getString("type_contrat_lbl"));
				stat_bean.setNbemploye(rs.getInt("nbemploye"));

				liststatbean.add(stat_bean);


			}


		} catch ( SQLException e ) {
			System.out.println(e.toString());
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
		return liststatbean;


	}


	public List getListPosteMoyFam() throws SQLException
	{

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();
		String code_poste=applicationSession.getCode_poste();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ArrayList<StatMoyFamillePosteBean> listmoyfam = new ArrayList<StatMoyFamillePosteBean>();
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					
					Integer idcompagne = pair.getValue();

					sql_query="select r.famille,round(moy_par_famille,2) as moy_par_famille,i.img,p.intitule_poste from "+entry.getKey()+"."+"moy_poste_famille_stats m, "+entry.getKey()+"."+"repertoire_competence r , "+entry.getKey()+"."+"img_stats i" +
					
							", "+entry.getKey()+"."+"poste_travail_description p where r.code_famille=m.code_famille and  m.code_poste=#code_poste  "
							+ "and m.id_compagne=#id_compagne and i.code_poste =m.code_poste and m.id_compagne=i.id_compagne and p.code_poste=m.code_poste group by  r.famille";

					sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
					sql_query = sql_query.replaceAll("#id_compagne", "'"+idcompagne+"'");


				}

			}


			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				StatMoyFamillePosteBean bean=new StatMoyFamillePosteBean();
				bean.setFamille((rs.getString("famille")));	
				bean.setMoy_famille((rs.getFloat("moy_par_famille")));
				bean.setImg((rs.getFloat("img")));
				bean.setIntitule_poste((rs.getString("intitule_poste")));
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

	
	public List getMidianParPoste() throws SQLException
	{

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();
		String code_poste=applicationSession.getCode_poste();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ArrayList<IMIMedianeBean> listmoyfam = new ArrayList<IMIMedianeBean>();
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					
					Integer idcompagne = pair.getValue();
					
				

					sql_query="select count(distinct e.id_employe) nombremploye, '1 à <= 3'  tranche from "+entry.getKey()+"."+"imi_stats i , "+entry.getKey()+"."+"employe  e "
							+ " where i.id_employe=e.id_employe and e.code_poste=#code_poste  and i.id_compagne=#id_compagne and  imi>1  and imi<=3"
							+ " union"
							+ " select count(distinct e.id_employe) nombremploye, '3 à <= 4'  tranche from "+entry.getKey()+"."+"imi_stats i , "+entry.getKey()+"."+"employe  e"
							+ " where i.id_employe=e.id_employe and e.code_poste=#code_poste  and i.id_compagne=#id_compagne and  imi>3  and imi<=4"
							+ " union"
							+ " select count(distinct e.id_employe) nombremploye, '> 4'  tranche from "+entry.getKey()+"."+"imi_stats i , "+entry.getKey()+"."+"employe  e"
							+ "  where i.id_employe=e.id_employe and e.code_poste=#code_poste  and i.id_compagne=#id_compagne and  imi>4 order by tranche";


			
					sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
					sql_query = sql_query.replaceAll("#id_compagne", "'"+idcompagne+"'");


				}

			}


			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				IMIMedianeBean bean=new IMIMedianeBean();
				bean.setTranche((rs.getString("tranche")));	
				bean.setNombremploye((rs.getInt("nombremploye")));
				
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
	
	public List getMoyCompetencePoste() throws SQLException
	{

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();
		String code_poste=applicationSession.getCode_poste();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ArrayList<MoyCompetenceBean> listmoyfam = new ArrayList<MoyCompetenceBean>();
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					
					Integer idcompagne = pair.getValue();
					
				
					sql_query="select distinct r.famille,r.libelle_competence,round(moy_par_competence,2) as moy_par_competence "
							+ "   from "+entry.getKey()+"."+"moy_poste_competence_stats m, "+entry.getKey()+"."+"repertoire_competence r"
							+ "   where r.code_famille=m.code_famille and  m.code_poste=#code_poste"
							+ "   and m.id_compagne=#id_compagne and  r.code_famille=m.code_famille and r.code_competence=m.code_competence";
					 
							
					sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
					sql_query = sql_query.replaceAll("#id_compagne", "'"+idcompagne+"'");


				}

			}


			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				MoyCompetenceBean bean=new MoyCompetenceBean();
				bean.setFamille((rs.getString("famille")));	
				bean.setCompetence((rs.getString("libelle_competence")));	
				bean.setMoy_competence((rs.getFloat("moy_par_competence")));
				
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
	
	
	
	

	
	
	
	
}
