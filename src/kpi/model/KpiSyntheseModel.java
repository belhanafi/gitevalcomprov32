package kpi.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.text.html.ListView;

import org.zkoss.zk.ui.Sessions;

import kpi.bean.AnalSynthRadarBean;
import kpi.bean.DureeMoyenneBean;
import kpi.bean.KPIAgeExpBean;
import kpi.bean.KpiIMIAgeBean;
import kpi.bean.ListDureeCotatLowBean;
import kpi.bean.ListeCompagneVagueBean;
import kpi.bean.SynGlobalPg1Ch1Bean;
import kpi.bean.TauxCouvertureCompagneBean;
import Statistique.bean.StatTrancheAgePosteBean;
import common.ApplicationSession;
import common.CreateDatabaseCon;
import compagne.bean.CompagneListBean;
import compagne.bean.PlanningCompagneListBean;

public class KpiSyntheseModel {

	/**
	 * cette méthode retrourne le nombre d'employes par BDD
	 * @return
	 */
	public HashMap<String,Double> getNombreEmployesAllVague(HashMap<String, HashMap<String, Integer>> listDb)	{

		HashMap <String, Double> resultat= new HashMap<String,Double>();

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
				resultat.put(rs1.getString("nomvague"), Math.round(ratio * 100.0) / 100.0 );


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
		return resultat;


	}




	/**
	 * cette méthode retrourne la liste des vagues(basede données) et les compagnes evaluation validées  dans chaque BD
	 * 	 * @return
	 */
	public List getListeCampgneVague()	{

		HashMap <String, String> listDb= new HashMap<String,String>();
		ArrayList<ListeCompagneVagueBean> listcompagne = new ArrayList<ListeCompagneVagueBean>();

		String query_finale="";
		int counter=1;

		String listDBQuery="select nom_base,nom_instance from liste_db l,cross_db c where l.database_id=c.database_id and secteur_id!=0";



		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		Statement stmt1=null;

		ResultSet rs=null;
		ResultSet rs1=null;

		try 
		{
			stmt = (Statement) conn.createStatement();
			rs = (ResultSet) stmt.executeQuery(listDBQuery);


			while(rs.next()){

				listDb.put(rs.getString("nom_base"), rs.getString("nom_instance"));


			}


			int map_size=listDb.entrySet().size();
			for (Entry<String, String> pair : listDb.entrySet()) {
				String nom_base = pair.getKey();
				String nom_instance = pair.getValue();

				String query="select"+" '"+ nom_base+"' nom_base , "+" '"+ nom_instance+"' nom_instance , "+"concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) as libelle_compagne"
						+ ", id_compagne from "+nom_instance+"."+"compagne_evaluation where id_compagne in (select id_compagne from "+ nom_instance+"."+"compagne_validation where compagne_valide=1)  " ;


				if (counter<map_size){
					query_finale=query_finale+query +" union ";
				}else{
					query_finale=query_finale+query +" order by nom_base,id_compagne desc";
				}

				counter++;

			}



			rs1 = (ResultSet) stmt.executeQuery(query_finale);
			while(rs1.next()){

				ListeCompagneVagueBean bean =new ListeCompagneVagueBean();
				bean.setCompagne_id(String.valueOf(rs1.getInt("id_compagne")));
				bean.setNombase(rs1.getString("nom_base"));
				bean.setLibelle_compagne(rs1.getString("libelle_compagne"));
				bean.setNominstance(rs1.getString("nom_instance"));
				listcompagne.add(bean);



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
		return listcompagne;


	}



	
	/**
	 * cette méthode retrourne la liste des campagnes d'évaluation d'une base de  données selectionnée à la connexion
	 * 	 * 	 * @return
	 */
	public List getListeValidCampgneBase()	{

		HashMap <String, String> listDb= new HashMap<String,String>();
		ArrayList<ListeCompagneVagueBean> listcompagne = new ArrayList<ListeCompagneVagueBean>();
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		int database_id=applicationSession.getClient_database_id();

	
		String query_finale="";
		int counter=1;

		String listDBQuery="select nom_base,nom_instance from liste_db l,cross_db c where l.database_id=c.database_id and secteur_id!=0 and l.database_id=#database_id";
		listDBQuery = listDBQuery.replaceAll("#database_id", "'"+database_id+"'");



		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		Statement stmt1=null;

		ResultSet rs=null;
		ResultSet rs1=null;

		try 
		{
			stmt = (Statement) conn.createStatement();
			rs = (ResultSet) stmt.executeQuery(listDBQuery);


			while(rs.next()){

				listDb.put(rs.getString("nom_base"), rs.getString("nom_instance"));


			}


			int map_size=listDb.entrySet().size();
			for (Entry<String, String> pair : listDb.entrySet()) {
				String nom_base = pair.getKey();
				String nom_instance = pair.getValue();

				String query="select"+" '"+ nom_base+"' nom_base , "+" '"+ nom_instance+"' nom_instance , "+"concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) as libelle_compagne"
						+ ", id_compagne from "+nom_instance+"."+"compagne_evaluation where id_compagne in (select id_compagne from "+ nom_instance+"."+"compagne_validation where compagne_valide=1)  " ;


				if (counter<map_size){
					query_finale=query_finale+query +" union ";
				}else{
					query_finale=query_finale+query +" order by nom_base,id_compagne desc";
				}

				counter++;

			}



			rs1 = (ResultSet) stmt.executeQuery(query_finale);
			while(rs1.next()){

				ListeCompagneVagueBean bean =new ListeCompagneVagueBean();
				bean.setCompagne_id(String.valueOf(rs1.getInt("id_compagne")));
				bean.setNombase(rs1.getString("nom_base"));
				bean.setLibelle_compagne(rs1.getString("libelle_compagne"));
				bean.setNominstance(rs1.getString("nom_instance"));
				listcompagne.add(bean);



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
		return listcompagne;


	}



	/**
	 * cette méthode retrourne le taux de couverture par compagne
	 *  @return
	 */
	public ArrayList<TauxCouvertureCompagneBean> getTauxCouvertureAllVague(HashMap<String, HashMap<String, Integer>> listDb)	{


		String query_finale="";
		int counter=1;

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
	 * cette méthode retrourne le taux de couverture par compagne
	 *  @return
	 */
	public ArrayList<AnalSynthRadarBean> getPerParIMI(HashMap<String, HashMap<String, Integer>> listDb)	{

		HashMap<String, HashMap<String, Integer>> resultat= new HashMap<String, HashMap<String, Integer>>();
		HashMap<String, Integer> subresultat= new  HashMap<String, Integer>();


		String query="";
		int counter=1;

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			int map_size=listDb.entrySet().size();

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String vague = pair.getKey();
				Integer idcompagne = pair.getValue();

				query="select 'Résultat Vague' typeres,'Compétence à Acquérir' evaluation , round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux,1 id_niveau_maitrise from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne
						+ "  union"
						+ " select 'Résultat Vague' typeres,'Compétence à Développer' evaluation , round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux,2 id_niveau_maitrise from  "+entry.getKey()+"."+"imi_stats where imi>2 and imi<=3 and id_compagne="+idcompagne
						+ "	union"
						+ " select 'Résultat Vague' typeres,'Compétence  Acquise' evaluation , round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux ,3 id_niveau_maitrise from  "+entry.getKey()+"."+"imi_stats where imi>3 and imi<4.5 and id_compagne="+idcompagne
						+ " union"
						+ " select 'Résultat Vague' typeres,'Expertise' evaluation , round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux,4 id_niveau_maitrise from  "+entry.getKey()+"."+"imi_stats where imi>=4.5 and id_compagne="+idcompagne
						+" union"
						+ " select 'IMI Mix Idéal' typeres,libelle_niveau_maitrise evaluation ,objectif  taux,n.id_niveau_maitrise from "+entry.getKey()+"."+"imi_mix_ideal m ,  "+entry.getKey()+"."+"niveau_maitrise n"
						+ " where n.id_niveau_maitrise=m.id_niveau_maitrise and  m.id_compagne="+idcompagne+"  order by typeres,id_niveau_maitrise";
				//System.out.println(">>>"+query);yh                                                                                                                            

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


	public HashMap getListPostTravailValid(HashMap<String, HashMap<String, Integer>> listDb,List <String> list_code_dir) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		String query="";
		String structure = "(";
		for ( int i=0;i<list_code_dir.size();i++){
			if (i <list_code_dir.size()-1)
		        structure+="'"+list_code_dir.get(i)+"',";
			else
				 structure+="'"+list_code_dir.get(i)+"'";
		}
		structure+=")";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();

					query="select  distinct t.code_poste,t.intitule_poste  from "+entry.getKey()+"."+"compagne_evaluation e, "+entry.getKey()+"."+"planning_evaluation p, "+entry.getKey()+"."+"poste_travail_description t" +
							" where e.id_compagne in (select id_compagne from "+entry.getKey()+"."+"compagne_validation where compagne_valide=1) " +
							" and p.id_compagne=e.id_compagne  and t.code_poste=p.code_poste and e.id_compagne="+idcompagne+
							" and t.code_structure in "+structure;
					//System.out.println(">>>"+query);

				}

			}




			rs = (ResultSet) stmt.executeQuery(query);


			while(rs.next()){
				map.put(rs.getString("intitule_poste"), rs.getString("code_poste"));
				//list_profile.add(rs.getString("libelle_profile"));
			}
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {
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

		return map;
	}


	/**
	 * cette méthode retrourne le taux de couverture par compagne
	 *  @return
	 */
	public HashMap<String, HashMap<String, Integer>> getPerParIMIPoste(HashMap<String, HashMap<String, Integer>> listDb,String code_poste)	{

		HashMap<String, HashMap<String, Integer>> resultat= new HashMap<String, HashMap<String, Integer>>();
		HashMap<String, Integer> subresultat= new  HashMap<String, Integer>();


		String query="";
		int counter=1;

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			int map_size=listDb.entrySet().size();

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String vague = pair.getKey();
				Integer idcompagne = pair.getValue();

				if (code_poste.equalsIgnoreCase("tous")){

					query="select 'Compétence à acquérir' evaluation , round(count(id_employe)*100/(select count(id_employe) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" )) taux  from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne 
							+ "  union"
							+ " select 'Compétence à développer' evaluation , round(count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" )) taux  from  "+entry.getKey()+"."+"imi_stats where imi>2 and imi<=3 and id_compagne="+idcompagne 
							+ "	union"
							+ " select 'Compétence  acquise' evaluation , round(count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" )) taux   from  "+entry.getKey()+"."+"imi_stats where imi>3 and imi<4.5 and id_compagne="+idcompagne 
							+ " union"
							+ " select 'Expertise' evaluation , round(count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" )) taux from  "+entry.getKey()+"."+"imi_stats where imi>=4.5 and id_compagne="+idcompagne  ;
				}

				else{

					query="select 'Compétence à acquérir' evaluation , round(count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"'))) taux  from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne +" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"' )"
							+ "  union"
							+ " select 'Compétence à développer' evaluation , round(count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"'))) taux  from  "+entry.getKey()+"."+"imi_stats where imi>2 and imi<=3 and id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"' )"
							+ "	union"
							+ " select 'Compétence  acquise' evaluation , round(count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"'))) taux   from  "+entry.getKey()+"."+"imi_stats where imi>3 and imi<4.5 and id_compagne="+idcompagne+" and id_employe in (select id_employe from  "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"' )"
							+ " union"
							+ " select 'Expertise' evaluation , round(count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"'))) taux from  "+entry.getKey()+"."+"imi_stats where imi>=4.5 and id_compagne="+idcompagne +" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"' )";
					//System.out.println(">>>"+query);
				}
			}

		}

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		ResultSet rs=null;
		ArrayList<TauxCouvertureCompagneBean> listbean = new ArrayList<TauxCouvertureCompagneBean>();
		try 
		{
			stmt = (Statement) conn.createStatement();

			rs = (ResultSet) stmt.executeQuery(query);
			while(rs.next()){

				subresultat.put(rs.getString("evaluation"), rs.getInt("taux"));

				//resultat.put("IMI Compagne", new HashMap<String, Integer>());
				//resultat.get("IMI Compagne").put(rs.getString("evaluation"), rs.getInt("taux") );

			}
			resultat.put("IMI Compagne",subresultat);


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
		return resultat;


	}




	/**
	 *  cette méthode retrourne  IMI par Age 
	 *  @return
	 */
	public ArrayList<KpiIMIAgeBean> getIMIAge(HashMap<String, HashMap<String, Integer>> listDb, String code_poste)	{

		HashMap<String,  List> resultat= new HashMap<String,  List>();
		HashMap<String, Integer> subresultat= new  HashMap<String, Integer>();

		String query="";

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			int map_size=listDb.entrySet().size();

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String vague = pair.getKey();
				Integer idcompagne = pair.getValue();

				if (code_poste.equalsIgnoreCase("tous")){

					query="select 'Compétence à acquérir' evaluation , ' Inf ou égal à 30 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) <=30)"
							+ " union"
							+ " select 'Compétence à acquérir' evaluation , ' Entre 30 et 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >30 and round(DATEDIFF(now(),date_naissance)/365<50) )"
							+ " union"
							+ " select 'Compétence à acquérir' evaluation , ' Sup égal à 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >=50)"
							+ " union"
							+ " select 'Compétence à développer' evaluation , ' Inf ou égal à 30 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) <=30)"
							+ " union"
							+ " select 'Compétence à développer' evaluation , ' Entre 30 et 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >30 and round(DATEDIFF(now(),date_naissance)/365<50) )"
							+ " union"
							+ " select 'Compétence à développer' evaluation , ' Sup égal à 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >=50)"
							+ " union"
							+ " select 'Compétence  acquise' evaluation , ' Inf ou égal à 30 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) <=30)"
							+ " union"
							+ " select 'Compétence  acquise' evaluation , ' Entre 30 et 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats   where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >30 and round(DATEDIFF(now(),date_naissance)/365<50) )"
							+ " union"
							+ " select 'Compétence  acquise' evaluation , ' Sup égal à 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >=50)"
							+ " union"

			                  + " select 'Expertise' evaluation , ' Inf ou égal à 30 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
			                  + " from "+entry.getKey()+"."+"imi_stats  where imi>=4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) <=30)"
			                  + " union"
			                  + " select 'Expertise' evaluation , ' Entre 30 et 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
			                  + " from "+entry.getKey()+"."+"imi_stats   where imi>=4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >30 and round(DATEDIFF(now(),date_naissance)/365<50) ) "
			                  + " union"
			                  + " select 'Expertise' evaluation , ' Sup égal à 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
			                  + " from "+entry.getKey()+"."+"imi_stats  where imi>=4.5 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >=50) order by evaluation";

				}else{

					query="select 'Compétence à acquérir' evaluation , ' Inf ou égal à 30 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) <=30 and code_poste="+"'"+code_poste+"')"
							+ " union"
							+ " select 'Compétence à acquérir' evaluation , ' Entre 30 et 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >30 and round(DATEDIFF(now(),date_naissance)/365<50) and code_poste="+"'"+code_poste+"' )"
							+ " union"
							+ " select 'Compétence à acquérir' evaluation , ' Sup égal à 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >=50 and code_poste="+"'"+code_poste+"')"
							+ " union"
							+ " select 'Compétence à développer' evaluation , ' Inf ou égal à 30 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) <=30 and code_poste="+"'"+code_poste+"')"
							+ " union"
							+ " select 'Compétence à développer' evaluation , ' Entre 30 et 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >30 and round(DATEDIFF(now(),date_naissance)/365<50) and code_poste="+"'"+code_poste+"' )"
							+ " union"
							+ " select 'Compétence à développer' evaluation , ' Sup égal à 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >=50 and code_poste="+"'"+code_poste+"')"
							+ " union"
							+ " select 'Compétence  acquise' evaluation , ' Inf ou égal à 30 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) <=30 and code_poste="+"'"+code_poste+"')"
							+ " union"
							+ " select 'Compétence  acquise' evaluation , ' Entre 30 et 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats   where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >30 and round(DATEDIFF(now(),date_naissance)/365<50) and code_poste="+"'"+code_poste+"' )"
							+ " union"
							+ " select 'Compétence  acquise' evaluation , ' Sup égal à 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >=50 and code_poste="+"'"+code_poste+"')"
							+ " union"

			                  + " select 'Expertise' evaluation , ' Inf ou égal à 30 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
			                  + " from "+entry.getKey()+"."+"imi_stats  where imi>=4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) <=30 and code_poste="+"'"+code_poste+"')"
			                  + " union"
			                  + " select 'Expertise' evaluation , ' Entre 30 et 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
			                  + " from "+entry.getKey()+"."+"imi_stats   where imi>=4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >30 and round(DATEDIFF(now(),date_naissance)/365<50) and code_poste="+"'"+code_poste+"' ) "
			                  + " union"
			                  + " select 'Expertise' evaluation , ' Sup égal à 50 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
			                  + " from "+entry.getKey()+"."+"imi_stats  where imi>=4.5 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_naissance)/365) >=50 and code_poste="+"'"+code_poste+"') order by evaluation";

				}

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
	 * cette méthode retrourne  IMI par experience 
	 *  @return
	 */
	public ArrayList<KpiIMIAgeBean> getIMIExperience(HashMap<String, HashMap<String, Integer>> listDb, String code_poste)	{

		HashMap<String,  List> resultat= new HashMap<String,  List>();
		HashMap<String, Integer> subresultat= new  HashMap<String, Integer>();

		String query="";

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			int map_size=listDb.entrySet().size();

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String vague = pair.getKey();
				Integer idcompagne = pair.getValue();

				if (code_poste.equalsIgnoreCase("tous")){

					query="select 'Compétence à acquérir' evaluation , ' Inf ou égal à 10 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) <=10)"
							+ " union"
							+ " select 'Compétence à acquérir' evaluation , ' Entre 10 et 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >10 and round(DATEDIFF(now(),date_recrutement)/365<20) )"
							+ " union"
							+ " select 'Compétence à acquérir' evaluation , ' Sup égal à 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >=20)"
							+ " union"
							+ " select 'Compétence à développer' evaluation , ' Inf ou égal à 10 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) <=10)"
							+ " union"
							+ " select 'Compétence à développer' evaluation , ' Entre 10 et 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >10 and round(DATEDIFF(now(),date_recrutement)/365<20) )"
							+ " union"
							+ " select 'Compétence à développer' evaluation , ' Sup égal à 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >=20)"
							+ " union"
							+ " select 'Compétence  acquise' evaluation , ' Inf ou égal à 10 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) <=10)"
							+ " union"
							+ " select 'Compétence  acquise' evaluation , ' Entre 10 et 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats   where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >10 and round(DATEDIFF(now(),date_recrutement)/365<20) )"
							+ " union"
							+ " select 'Compétence  acquise' evaluation , ' Sup égal à 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >=20)"
							+ " union"

                        + " select 'Expertise' evaluation , ' Inf ou égal à 10 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
                        + " from "+entry.getKey()+"."+"imi_stats  where imi>=4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) <=10)"
                        + " union"
                        + " select 'Expertise' evaluation , ' Entre 10 et 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
                        + " from "+entry.getKey()+"."+"imi_stats   where imi>=4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >30 and round(DATEDIFF(now(),date_recrutement)/365<50) ) "
                        + " union"
                        + " select 'Expertise' evaluation , ' Sup égal à 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+"))) taux"
                        + " from "+entry.getKey()+"."+"imi_stats  where imi>=4.5 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >=20) order by evaluation";
				}else{


					query="select 'Compétence à acquérir' evaluation , ' Inf ou égal à 10 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) <=10 and code_poste="+"'"+code_poste+"')"
							+ " union"
							+ " select 'Compétence à acquérir' evaluation , ' Entre 10 et 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >10 and round(DATEDIFF(now(),date_recrutement)/365<20) and code_poste="+"'"+code_poste+"' )"
							+ " union"
							+ " select 'Compétence à acquérir' evaluation , ' Sup égal à 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats where imi>=0 and imi<=2 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >=20 and code_poste="+"'"+code_poste+"')"
							+ " union"
							+ " select 'Compétence à développer' evaluation , ' Inf ou égal à 10 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) <=10 and code_poste="+"'"+code_poste+"')"
							+ " union"
							+ " select 'Compétence à développer' evaluation , ' Entre 10 et 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >10 and round(DATEDIFF(now(),date_recrutement)/365<20) and code_poste="+"'"+code_poste+"' )"
							+ " union"
							+ " select 'Compétence à développer' evaluation , ' Sup égal à 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>2 and imi<=3  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >=20 and code_poste="+"'"+code_poste+"')"
							+ " union"
							+ " select 'Compétence  acquise' evaluation , ' Inf ou égal à 10 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) <=10 and code_poste="+"'"+code_poste+"')"
							+ " union"
							+ " select 'Compétence  acquise' evaluation , ' Entre 10 et 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats   where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >10 and round(DATEDIFF(now(),date_recrutement)/365<20) and code_poste="+"'"+code_poste+"' )"
							+ " union"
							+ " select 'Compétence  acquise' evaluation , ' Sup égal à 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
							+ " from "+entry.getKey()+"."+"imi_stats  where imi>3 and imi<4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >=20 and code_poste="+"'"+code_poste+"')"
							+ " union"

	                        + " select 'Expertise' evaluation , ' Inf ou égal à 10 ans' trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
	                        + " from "+entry.getKey()+"."+"imi_stats  where imi>=4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) <=10 and code_poste="+"'"+code_poste+"')"
	                        + " union"
	                        + " select 'Expertise' evaluation , ' Entre 10 et 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
	                        + " from "+entry.getKey()+"."+"imi_stats   where imi>=4.5  and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >10 and round(DATEDIFF(now(),date_recrutement)/365<20 and code_poste="+"'"+code_poste+"' ) ) "
	                        + " union"
	                        + " select 'Expertise' evaluation , ' Sup égal à 20 ans'  trancheAge,round((count(id_employe)*100/(select count(*) from "+entry.getKey()+"."+"imi_stats where id_compagne="+idcompagne+" and id_employe in (select id_employe from "+entry.getKey()+"."+"employe where code_poste="+"'"+code_poste+"')))) taux"
	                        + " from "+entry.getKey()+"."+"imi_stats  where imi>=4.5 and id_compagne="+idcompagne+" and id_employe in (select id_employe  from "+entry.getKey()+"."+"employe where round(DATEDIFF(now(),date_recrutement)/365) >=20 and code_poste="+"'"+code_poste+"') order by evaluation";	



				}

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
	 * cette méthode retrourne  croissement Age, Experience et IMI par experience 
	 *  @return
	 */
	public ArrayList<KPIAgeExpBean> getAgeIMICroissement(HashMap<String, HashMap<String, Integer>> listDb, String code_poste)	{

		HashMap<String,  List> resultat= new HashMap<String,  List>();
		HashMap<String, Integer> subresultat= new  HashMap<String, Integer>();

		String query="";

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			int map_size=listDb.entrySet().size();

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String vague = pair.getKey();
				Integer idcompagne = pair.getValue();

				if (code_poste.equalsIgnoreCase("tous")){

					query="select distinct round(DATEDIFF(now(),date_naissance)/365) age, avg(round(imi,2)) imi from "+entry.getKey()+"."+"imi_stats s ,"+entry.getKey()+"."+"employe e"
							+ " where s.id_employe=e.id_employe and id_compagne="+idcompagne +" group by 1";
				}else{

					query="select distinct round(DATEDIFF(now(),date_naissance)/365) age, avg(round(imi,2)) imi from "+entry.getKey()+"."+"imi_stats s ,"+entry.getKey()+"."+"employe e"
							+ " where s.id_employe=e.id_employe and id_compagne="+idcompagne +" and code_poste="+"'"+code_poste+"' group by 1";


				}




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
				//bean.setExperience(rs.getInt("experience"));
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
	 * cette méthode retrourne  croissement Age, Experience et IMI par experience 
	 *  @return
	 */
	public ArrayList<KPIAgeExpBean> getExpIMICroissement(HashMap<String, HashMap<String, Integer>> listDb,String code_poste)	{

		HashMap<String,  List> resultat= new HashMap<String,  List>();
		HashMap<String, Integer> subresultat= new  HashMap<String, Integer>();

		String query="";

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			int map_size=listDb.entrySet().size();

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String vague = pair.getKey();
				Integer idcompagne = pair.getValue();

				if (code_poste.equalsIgnoreCase("tous")){
					query="select distinct  round(DATEDIFF(now(),date_recrutement)/365) experience, avg(round(imi,2)) imi from "+entry.getKey()+"."+"imi_stats s ,"+entry.getKey()+"."+"employe e"
							+ " where s.id_employe=e.id_employe and id_compagne="+idcompagne +" group by 1";
				}else{
					query="select distinct  round(DATEDIFF(now(),date_recrutement)/365) experience, avg(round(imi,2)) imi from "+entry.getKey()+"."+"imi_stats s ,"+entry.getKey()+"."+"employe e"
							+ " where s.id_employe=e.id_employe and id_compagne="+idcompagne +" and code_poste="+"'"+code_poste+"' group by 1";
				}

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
				//bean.setAge(rs.getInt("age"));
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
	 * cette méthode retrourne  durée minamale d'évaluation
	 * @return
	 */
	public ArrayList<DureeMoyenneBean> getDuréeMinimEvol(HashMap<String, HashMap<String, Integer>> listDb)	{


		HashMap<String, Integer> subresultat= new  HashMap<String, Integer>();

		String query="";
		int counter=1;
		String query_finale="";

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			int map_size=listDb.entrySet().size();

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String vague = pair.getKey();
				Integer idcompagne = pair.getValue();

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
	 * cette méthode retrourne  durée minamale d'évaluation
	 * @return
	 */
	public ArrayList<ListDureeCotatLowBean> getListCotationBaclee(HashMap<String, HashMap<String, Integer>> listDb)	{



		String query="";
		int counter=1;
		String query_finale="";

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			int map_size=listDb.entrySet().size();

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String vague = pair.getKey();
//
//			query="SELECT t.login,'"+vague+"' vague, concat (nom, concat(' ',prenom)) evaluateur, t.nomEmploye evalue, round(avg(round(TIMESTAMPDIFF(SECOND,t.date_histo,v.date_histo)/60)),2) AS duree"
//						+ " FROM common_evalcom.compte h, "+entry.getKey()+"."+"histo_fiche_evaluation t  JOIN "+entry.getKey()+"."+"histo_fiche_evaluation v ON t.login = (v.login)"
//						+ " and t.date_histo<v.date_histo and (round(TIMESTAMPDIFF(SECOND,t.date_histo,v.date_histo)/60))<10 and (round(TIMESTAMPDIFF(SECOND,t.date_histo,v.date_histo)/60))>3"
//					+ " where h.login=t.login "
//					+ " group by 1,2";
//				
				
				
				
				query=" select h.login,'"+vague+"' vague, concat (h.nom, concat(' ',h.prenom)) evaluateur,evalue, intitule_poste,libelle_direction,structure_ent, sum(duree) duree"
						+ " from("
						+ "      select t.login,t.nomEmploye evalue,round(avg(round(TIMESTAMPDIFF(SECOND,t.date_histo,v.date_histo)/60)),2) AS duree"
						+ "      from "+entry.getKey()+"."+"histo_fiche_evaluation t,  "+entry.getKey()+"."+"histo_fiche_evaluation v"
						+ "      where t.login = v.login  and t.date_histo<v.date_histo and (round(TIMESTAMPDIFF(SECOND,t.date_histo,v.date_histo)/60))<10"
						+ "      and (round(TIMESTAMPDIFF(SECOND,t.date_histo,v.date_histo)/60))>3 group by 1"
						+ "      union"
						+ "      select  h.login, ' ', 0 as duree from  "+entry.getKey()+"."+"employe e,common_evalcom.compte h"
						+ "      where  e.id_compte=h.id_compte )  aggreg_table  , "+entry.getKey()+"."+"employe e,common_evalcom.compte h ,"+entry.getKey()+"."+"poste_travail_description p,"
						+ "      ( select libelle_direction,code_structure, structure_ent from ("
						+ "                  select libelle_direction,code_structure,libelle_section structure_ent  from "+entry.getKey()+"."+"structure_entreprise  where libelle_section is  not null"
						+ "                  and  libelle_section !='null' and  libelle_section !=''"
						+ "  	             union"
						+ " 	           select libelle_direction,code_structure,libelle_service structure_ent from "+entry.getKey()+"."+"structure_entreprise"
						+ "                where libelle_service is  not null and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
						+ "                union"
						+ "                select libelle_direction,code_structure,libelle_departement structure_ent from "+entry.getKey()+"."+"structure_entreprise"
						+ "                where libelle_departement is  not null and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
						+ "                union"
						+ "                select libelle_direction,code_structure,libelle_sous_direction structure_ent from "+entry.getKey()+"."+"structure_entreprise"
						+ "                where libelle_sous_direction is  not null and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
						+ "		           union"
						+ "		           select libelle_direction,code_structure,libelle_unite structure_ent from "+entry.getKey()+"."+"structure_entreprise"
						+ "		           where libelle_unite is  not null and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
						+ "		           and length(libelle_service)=0 and  length(libelle_section) =0"
						+ "    	           union"
						+ "		           select libelle_direction,code_structure,libelle_direction structure_ent from "+entry.getKey()+"."+"structure_entreprise"
						+ "		           where libelle_direction is  not null and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
						+ "		           and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise ) t"
						+ "  			where  duree >3 and duree<10 and h.login=aggreg_table.login and e.id_compte=h.id_compte and e.code_poste=p.code_poste and t.code_structure=e.code_structure"
						+ "			group by 1";

				

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
		ArrayList<ListDureeCotatLowBean> listbean = new ArrayList<ListDureeCotatLowBean>();
		try 
		{
			stmt = (Statement) conn.createStatement();

			rs = (ResultSet) stmt.executeQuery(query_finale);
			while(rs.next()){

				ListDureeCotatLowBean bean =new ListDureeCotatLowBean();
				//bean.setAge(rs.getInt("age"));
				bean.setVague(rs.getString("vague"));
				bean.setDuree(rs.getFloat("duree"));
				bean.setEvaluateur(rs.getString("evaluateur"));
				bean.setEvalue(rs.getString("evalue"));
				bean.setLogin(rs.getString("login"));
				bean.setStructure_entreprise(rs.getString("structure_ent"));
				bean.setPoste_travail(rs.getString("intitule_poste"));
				bean.setLibelle_direction(rs.getString("libelle_direction"));
				
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
	 * cette méthode retrourne  La liste des direction si direction est null dans la table structure_entreprise,
	 * on recupère la division
	 *  @return
	 */
	public HashMap<String,List<String>> getListDirection(HashMap<String, HashMap<String, Integer>> listDb)	{

	
		String query="";

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

	

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
							
				query="select code_structure, case   when length(trim(direction))=0 then 'DIR NON RENSEIGNEE' ELSE direction END direction "
						+ " from ("
						+ "	select code_structure,libelle_direction as direction from "+entry.getKey()+"."+"structure_entreprise where length(trim(libelle_direction))!=0 "
						+ " union distinct"
						+ " select code_structure,libelle_division as direction from "+entry.getKey()+"."+"structure_entreprise where length(trim(libelle_direction))=0 "
						+ " )  tb order by direction";

			}

		}

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		ResultSet rs=null;
		HashMap<String,List<String>> mapListDir = new HashMap<String,List<String>> ();
		
		try 
		{
			stmt = (Statement) conn.createStatement();

			rs = (ResultSet) stmt.executeQuery(query);
			String libelle_direction="";
		
			while(rs.next()){
				
				libelle_direction=rs.getString("direction");
				
				if(mapListDir.get(libelle_direction)== null){
					List <String> list_code_dir=new ArrayList<String>();
					list_code_dir.add(rs.getString("code_structure"));
					mapListDir.put(libelle_direction, list_code_dir);
								
				}else{
					List <String> list_code_dir=mapListDir.get(libelle_direction);
					list_code_dir.add(rs.getString("code_structure"));
					mapListDir.put(libelle_direction,list_code_dir );
					
					
				}
				
				//listDir.put(libelle_direction_save, list_code_dir);
				
				
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
		return mapListDir;


	}
	public HashMap getListPostTravailValid1(HashMap<String, HashMap<String, Integer>> listDb) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		String query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();

					query="select  distinct t.code_poste,t.intitule_poste  from "+entry.getKey()+"."+"compagne_evaluation e, "+entry.getKey()+"."+"planning_evaluation p, "+entry.getKey()+"."+"poste_travail_description t" +
							" where e.id_compagne in (select id_compagne from "+entry.getKey()+"."+"compagne_validation where compagne_valide=1) " +
							" and p.id_compagne=e.id_compagne  and t.code_poste=p.code_poste and e.id_compagne="+idcompagne;
					//System.out.println(">>>"+query);

				}

			}




			rs = (ResultSet) stmt.executeQuery(query);


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

	


}
