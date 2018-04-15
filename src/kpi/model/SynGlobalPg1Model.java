package kpi.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.zkoss.zk.ui.Sessions;

import kpi.bean.AnalSynthRadarBean;
import kpi.bean.DureeMoyenneBean;
import kpi.bean.IMIparVagueBean;
import kpi.bean.KPIAgeExpBean;
import kpi.bean.KpiIMIAgeBean;
import kpi.bean.KpiIMIExpBean;
import kpi.bean.SynGlobalPg1Ch1Bean;
import kpi.bean.TauxCouvertureCompagneBean;

import common.ApplicationSession;
import common.CreateDatabaseCon;

public class SynGlobalPg1Model {


	/**
	 * cette m�thode retrourne le nombre d'employes par BDD
	 * @return
	 */
	public List<SynGlobalPg1Ch1Bean> getNombreEmployesAllVague()	{


		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();

		//HashMap<String, HashMap<String, Integer>> listDb=ApplicationFacade.getInstance().getListDb();

		List <SynGlobalPg1Ch1Bean> listEmployeVague= new ArrayList<SynGlobalPg1Ch1Bean>();

		String query_finale="";
		int counter=1;

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			int map_size=listDb.entrySet().size();

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String vague = pair.getKey();
				Integer idcompagne = pair.getValue();
				String query ="select count(id_employe) nbemploye"+","+"'"+vague+ "' nomvague "+" from "+ entry.getKey()+"."+"planning_evaluation where id_compagne="+idcompagne;

				if (counter<map_size){
					query_finale=query_finale+query +" union ";
				}else
					query_finale=query_finale+query;

			}

			counter++;


		}

		String query_total="select sum(nbemploye) total from( "+query_finale+" ) t";

		//System.out.println(query_finale);
		//System.out.println(query_total);


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		Statement stmt1=null;

		ResultSet rs=null;
		ResultSet rs1=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			rs = (ResultSet) stmt.executeQuery(query_total);

			double total=0.0;
			while(rs.next()){

				total= rs.getLong("total");

			}
			rs1 = (ResultSet) stmt.executeQuery(query_finale);
			while(rs1.next()){


				Double ratio= (rs1.getLong("nbemploye")/(double)total)*100;
				SynGlobalPg1Ch1Bean bean=new SynGlobalPg1Ch1Bean();
				bean.setNbEmploye(Math.round(ratio * 100.0) / 100.0);
				bean.setNomvague(rs1.getString("nomvague"));
				listEmployeVague.add(bean);



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

			if ( rs1 != null ) {
				try {
					rs1.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}
			if ( stmt1 != null ) {
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
		return listEmployeVague;


	}



	/**
	 * cette m�thode retrourne le taux de couverture par compagne
	 *  @return
	 */
	public ArrayList<TauxCouvertureCompagneBean> getTauxCouvertureAllVague()	{


		String query_finale="";
		int counter=1;
		//HashMap<String, HashMap<String, Integer>> listDb=ApplicationFacade.getInstance().getListDb();

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			int map_size=listDb.entrySet().size();

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String vague = pair.getKey();
				Integer idcompagne = pair.getValue();

				String query="select count(p.id_employe) nbemploye,( select count(id_employe) fiche_valide from "+entry.getKey()+"."+"fiche_validation where fiche_valide=1"
						+ " and id_planning_evaluation in (select id_planning_evaluation from "+entry.getKey()+"."+"planning_evaluation where id_compagne="+idcompagne+" )) nb_fiche_valide ,"+"'"+vague+ "' nomvague,f.date_fin"
						+ " from "+ entry.getKey()+"."+"planning_evaluation p,"+entry.getKey()+"."+"compagne_evaluation f where p.id_compagne="+idcompagne+" and p.id_compagne=f.id_compagne"; 

				if (counter<map_size){
					query_finale=query_finale+query +" union ";
				}else
					query_finale=query_finale+query +" order by date_fin,nomvague asc";

			}

			counter++;


		}

		//System.out.println(">>>"+query_finale);
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		ResultSet rs=null;
		SimpleDateFormat sdfDestination = new SimpleDateFormat("MMM yyyy");
		ArrayList<TauxCouvertureCompagneBean> listbean = new ArrayList<TauxCouvertureCompagneBean>();
		try 
		{
			stmt = (Statement) conn.createStatement();

			rs = (ResultSet) stmt.executeQuery(query_finale);
			while(rs.next()){
				Date date=rs.getDate("date_fin");
				String str_date = sdfDestination.format(date);
				TauxCouvertureCompagneBean bean =new TauxCouvertureCompagneBean();
				bean.setNbemploye(rs.getInt("nbemploye"));
				bean.setNbfichevalide(rs.getInt("nb_fiche_valide"));
				bean.setNomvague(rs.getString("nomvague")+" - "+str_date);
				Double pourcentage= (rs.getInt("nb_fiche_valide")/(double)rs.getInt("nbemploye"))*100;
				bean.setPourcentage(Math.round(pourcentage * 100.0) / 100.0);
				listbean.add(bean);
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
		return listbean;


	}


	/**
	 * cette m�thode retrourne  dur�e minamale d'�valuation
	 * @return
	 */
	public ArrayList<DureeMoyenneBean> getDur�eMinimEvol()	{


		//HashMap<String, HashMap<String, Integer>> listDb=ApplicationFacade.getInstance().getListDb();

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();

		String query="";
		int counter=1;
		String query_finale="";

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			int map_size=listDb.entrySet().size();

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String vague = pair.getKey();

				query="SELECT "+"'"+vague+"' vague,t.login, round(avg(round(TIMESTAMPDIFF(SECOND,t.date_histo,v.date_histo)/60)),2) AS duree"
						+ " FROM "+entry.getKey()+"."+"histo_fiche_evaluation t  JOIN "+entry.getKey()+"."+"histo_fiche_evaluation v ON t.login = (v.login)"
						+ " and t.date_histo<v.date_histo and (round(TIMESTAMPDIFF(SECOND,t.date_histo,v.date_histo)/60))<100"
						+ " group by 1,2";

				if (counter<map_size){
					query_finale=query_finale+query +" union ";
				}else{
					query_finale=query_finale+query +" order by vague";
				}

				counter++;
			}

		}

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		ResultSet rs=null;
		ArrayList<DureeMoyenneBean> listbean = new ArrayList<DureeMoyenneBean>();
		try 
		{
			stmt = (Statement) conn.createStatement();

			rs = (ResultSet) stmt.executeQuery(query_finale);
			//System.out.println(query_finale);
			while(rs.next()){

				DureeMoyenneBean bean =new DureeMoyenneBean();
				//bean.setAge(rs.getInt("age"));
				bean.setVague(rs.getString("vague"));
				bean.setDuree(rs.getFloat("duree"));
				listbean.add(bean);


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
		return listbean;


	}


	/**
	 * cette m�thode retrourne le resultat IMI par compagne
	 *  @return
	 */
	public ArrayList<IMIparVagueBean>getPerParIMI()	{

		HashMap<String, HashMap<String, Integer>> resultat= new HashMap<String, HashMap<String, Integer>>();
		HashMap<String, Integer> subresultat= new  HashMap<String, Integer>();
		String query="";
		//HashMap<String, HashMap<String, Integer>> listDb=ApplicationFacade.getInstance().getListDb();

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();


		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {



			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String vague = pair.getKey();
				Integer idcompagne = pair.getValue();

				query="select '"+vague+"' vague,"+"'Comp�tence � Acqu�rir' evaluation , round(count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" )) taux  from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne 
						+ "  union"
						+ " select '"+vague+"' vague,"+"'Comp�tence � D�velopper' evaluation , round(count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+")) taux  from  "+entry.getKey()+"."+"imi_stats where imi>2 and imi<=3 and id_compagne="+idcompagne
						+ "	union"
						+ " select '"+vague+"' vague,"+"'Comp�tence Aquise' evaluation , round(count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+")) taux   from  "+entry.getKey()+"."+"imi_stats where imi>3 and imi<4.5 and id_compagne="+idcompagne
						+ " union"
						+ " select '"+vague+"' vague,"+"'Expertise' evaluation , round(count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+")) taux from  "+entry.getKey()+"."+"imi_stats where imi>=4.5 and id_compagne="+idcompagne;
				//System.out.println(">>>"+query);

			}

		}

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		ResultSet rs=null;
		ArrayList<IMIparVagueBean> listbean = new ArrayList<IMIparVagueBean>();
		try 
		{
			stmt = (Statement) conn.createStatement();

			rs = (ResultSet) stmt.executeQuery(query);
			while(rs.next()){


				IMIparVagueBean bean =new IMIparVagueBean();

				bean.setEvaluation(rs.getString("evaluation"));
				bean.setTaux(rs.getInt("taux"));
				bean.setVague(rs.getString("vague"));
				listbean.add(bean);

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
		return listbean;


	}



	/**
	 *  cette m�thode retrourne  IMI par Age 
	 *  @return
	 */
	public ArrayList<KpiIMIAgeBean> getIMIAge()	{


		//HashMap<String, HashMap<String, Integer>> listDb=ApplicationFacade.getInstance().getListDb();

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();


		String query="";

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {

				Integer idcompagne = pair.getValue();



				query="select 'Comp�tence � acqu�rir' evaluation , ' Inf ou �gal � 30 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) <=30)"
						+ " union"
						+ " select 'Comp�tence � acqu�rir' evaluation , ' Entre 30 et 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >30 and round(DATEDIFF(now(),date_naissance)/365<50) )"
						+ " union"
						+ " select 'Comp�tence � acqu�rir' evaluation , ' Sup �gal � 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >=50)"
						+ " union"
						+ " select 'Comp�tence � d�velopper' evaluation , ' Inf ou �gal � 30 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) <=30)"
						+ " union"
						+ " select 'Comp�tence � d�velopper' evaluation , ' Entre 30 et 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >30 and round(DATEDIFF(now(),date_naissance)/365<50) )"
						+ " union"
						+ " select 'Comp�tence � d�velopper' evaluation , ' Sup �gal � 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >=50)"
						+ " union"
						+ " select 'Comp�tence  acquise' evaluation , ' Inf ou �gal � 30 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats  where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) <=30)"
						+ " union"
						+ " select 'Comp�tence  acquise' evaluation , ' Entre 30 et 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats   where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >30 and round(DATEDIFF(now(),date_naissance)/365<50) )"
						+ " union"
						+ " select 'Comp�tence  acquise' evaluation , ' Sup �gal � 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats  where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >=50)"
						+ " union"

			                  + " select 'Expertise' evaluation , ' Inf ou �gal � 30 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
			                  + " from "+entry.getKey()+"."+"imi_stats  where imi>=4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) <=30)"
			                  + " union"
			                  + " select 'Expertise' evaluation , ' Entre 30 et 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
			                  + " from "+entry.getKey()+"."+"imi_stats   where imi>=4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >30 and round(DATEDIFF(now(),date_naissance)/365<50) ) "
			                  + " union"
			                  + " select 'Expertise' evaluation , ' Sup �gal � 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
			                  + " from "+entry.getKey()+"."+"imi_stats  where imi>=4.5 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >=50) order by evaluation";



				//System.out.println(">>>"+query);

			}

		}

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		ResultSet rs=null;
		ArrayList<KpiIMIAgeBean> listbean = new ArrayList<KpiIMIAgeBean>();
		try 
		{
			stmt = (Statement) conn.createStatement();

			rs = (ResultSet) stmt.executeQuery(query);
			while(rs.next()){

				KpiIMIAgeBean bean =new KpiIMIAgeBean();
				bean.setPourcentage(rs.getInt("taux"));
				bean.setNiveauMaitrise(rs.getString("evaluation"));
				bean.setTrancheAge(rs.getString("trancheAge"));

				listbean.add(bean);


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
		return listbean;


	}


	/**
	 * cette m�thode retrourne  IMI par experience 
	 *  @return
	 */
	public ArrayList<KpiIMIExpBean> getIMIExperience()	{

		//HashMap<String, HashMap<String, Integer>> listDb=ApplicationFacade.getInstance().getListDb();

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();


		String query="";

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {



			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {

				Integer idcompagne = pair.getValue();



				query="select 'Comp�tence � acqu�rir' evaluation , ' Inf ou �gal � 10 ans' trancheExp,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) <=10)"
						+ " union"
						+ " select 'Comp�tence � acqu�rir' evaluation , ' Entre 10 et 20 ans'  trancheExp,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >10 and round(DATEDIFF(now(),date_recrutement)/365<20) )"
						+ " union"
						+ " select 'Comp�tence � acqu�rir' evaluation , ' Sup �gal � 20 ans'  trancheExp,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >=20)"
						+ " union"
						+ " select 'Comp�tence � d�velopper' evaluation , ' Inf ou �gal � 10 ans' trancheExp,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) <=10)"
						+ " union"
						+ " select 'Comp�tence � d�velopper' evaluation , ' Entre 10 et 20 ans'  trancheExp,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >10 and round(DATEDIFF(now(),date_recrutement)/365<20) )"
						+ " union"
						+ " select 'Comp�tence � d�velopper' evaluation , ' Sup �gal � 20 ans'  trancheExp,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >=20)"
						+ " union"
						+ " select 'Comp�tence  acquise' evaluation , ' Inf ou �gal � 10 ans' trancheExp,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats  where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) <=10)"
						+ " union"
						+ " select 'Comp�tence  acquise' evaluation , ' Entre 10 et 20 ans'  trancheExp,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats   where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >10 and round(DATEDIFF(now(),date_recrutement)/365<20) )"
						+ " union"
						+ " select 'Comp�tence  acquise' evaluation , ' Sup �gal � 20 ans'  trancheExp,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
						+ " from "+entry.getKey()+"."+"imi_stats  where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >=20)"
						+ " union"

                        + " select 'Expertise' evaluation , ' Inf ou �gal � 10 ans' trancheExp,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
                        + " from "+entry.getKey()+"."+"imi_stats  where imi>=4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) <=10)"
                        + " union"
                        + " select 'Expertise' evaluation , ' Entre 10 et 20 ans'  trancheExp,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
                        + " from "+entry.getKey()+"."+"imi_stats   where imi>=4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >30 and round(DATEDIFF(now(),date_recrutement)/365<50) ) "
                        + " union"
                        + " select 'Expertise' evaluation , ' Sup �gal � 20 ans'  trancheExp,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
                        + " from "+entry.getKey()+"."+"imi_stats  where imi>=4.5 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >=20) order by evaluation";


			}

		}

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		ResultSet rs=null;
		ArrayList<KpiIMIExpBean> listbean = new ArrayList<KpiIMIExpBean>();
		try 
		{
			stmt = (Statement) conn.createStatement();

			rs = (ResultSet) stmt.executeQuery(query);
			while(rs.next()){

				KpiIMIExpBean bean =new KpiIMIExpBean();
				bean.setPourcentage(rs.getInt("taux"));
				bean.setNiveauMaitrise(rs.getString("evaluation"));
				bean.setTrancheExp(rs.getString("trancheExp"));

				listbean.add(bean);


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
		return listbean;


	}


	/**
	 * cette m�thode retrourne  croissement Age, Experience et IMI par experience 
	 *  @return
	 */
	public ArrayList<KPIAgeExpBean> getExpIMICroissement()	{

		//HashMap<String, HashMap<String, Integer>> listDb=ApplicationFacade.getInstance().getListDb();


		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();

		String query="";

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				Integer idcompagne = pair.getValue();


				query="select distinct  round(DATEDIFF(now(),date_naissance)/365) age,round(DATEDIFF(now(),date_recrutement)/365) experience, avg(round(imi,2)) imi from "+entry.getKey()+"."+"imi_stats s ,"+entry.getKey()+"."+"employe e"
						+ " where s.id_employe=e.id_employe and id_compagne="+idcompagne +" group by 1,2";

			}

		}

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		ResultSet rs=null;
		ArrayList<KPIAgeExpBean> listbean = new ArrayList<KPIAgeExpBean>();
		try 
		{
			stmt = (Statement) conn.createStatement();

			rs = (ResultSet) stmt.executeQuery(query);
			while(rs.next()){

				KPIAgeExpBean bean =new KPIAgeExpBean();
				bean.setAge(rs.getInt("age"));
				bean.setExperience(rs.getInt("experience"));
				bean.setImi(rs.getFloat("imi"));
				listbean.add(bean);


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
		return listbean;


	}
	
	/**
	 * cette m�thode retrourne le taux de couverture par compagne RADAR
	 *  @return
	 */
	public ArrayList<AnalSynthRadarBean> getPerParIMIRadar()	{

		//HashMap<String, HashMap<String, Integer>> listDb=ApplicationFacade.getInstance().getListDb();

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();
		
		String query="";
		

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

		
			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
			
				Integer idcompagne = pair.getValue();

				query="select 'R�sultat Vague' typeres,'Comp�tence � Acqu�rir' evaluation , round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux,1 id_niveau_maitrise from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne
						+ "  union"
						+ " select 'R�sultat Vague' typeres,'Comp�tence � D�velopper' evaluation , round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux,2 id_niveau_maitrise from  "+entry.getKey()+"."+"imi_stats where imi>2 and imi<=3 and id_compagne="+idcompagne
						+ "	union"
						+ " select 'R�sultat Vague' typeres,'Comp�tence  Acquise' evaluation , round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux ,3 id_niveau_maitrise from  "+entry.getKey()+"."+"imi_stats where imi>3 and imi<4.5 and id_compagne="+idcompagne
						+ " union"
						+ " select 'R�sultat Vague' typeres,'Expertise' evaluation , round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux,4 id_niveau_maitrise from  "+entry.getKey()+"."+"imi_stats where imi>=4.5 and id_compagne="+idcompagne
						+" union"
						+ " select 'IMI Mix Id�al' typeres,libelle_niveau_maitrise evaluation ,objectif  taux,n.id_niveau_maitrise from "+entry.getKey()+"."+"imi_mix_ideal m ,  "+entry.getKey()+"."+"niveau_maitrise n"
						+ " where n.id_niveau_maitrise=m.id_niveau_maitrise and  m.id_compagne="+idcompagne+"  order by typeres,id_niveau_maitrise";
			                                                                                                                           

			}

		}

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		ResultSet rs=null;
		ArrayList<AnalSynthRadarBean> listbean = new ArrayList<AnalSynthRadarBean>();
		try 
		{
			stmt = (Statement) conn.createStatement();

			rs = (ResultSet) stmt.executeQuery(query);
			while(rs.next()){
				AnalSynthRadarBean bean=new AnalSynthRadarBean();

				bean.setTyperes(rs.getString("typeres"));
				bean.setEvaluation(rs.getString("evaluation"));
				bean.setTaux(rs.getInt("taux"));
				bean.setIdniveaumaitrise(rs.getInt("id_niveau_maitrise"));
				/*subresultat.put(rs.getString("evaluation"), rs.getInt("taux"));
				resultat.put(rs.getString("typeres"), new HashMap<String, Integer>());
				resultat.get(rs.getString("typeres")).put(rs.getString("evaluation"), rs.getInt("taux") );*/
				listbean.add(bean);

			}
			//resultat.put("IMI Compagne",subresultat);


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
		return listbean;


	}


}
