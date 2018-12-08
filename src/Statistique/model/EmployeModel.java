package Statistique.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;



import java.util.Map;
import java.util.TreeMap;

import Statistique.bean.EmployeCadreBean;
import Statistique.bean.EmployeFormationBean;
import Statistique.bean.StatTrancheAgePosteBean;
import administration.bean.AdministrationLoginBean;
import administration.bean.FichePosteBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import common.CreateDatabaseCon;

public class EmployeModel {

	/**
	 * cette méthode retrourne le nombre d'employes se trouvant dans la BDD
	 * @return
	 */
	public int getNombreEmployes()
	{
		int nbEmploye=0;

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT COUNT(*) AS rowcount FROM employe";

			rs = (ResultSet) stmt.executeQuery(select_structure);

			rs.next();
			nbEmploye = rs.getInt("rowcount") ;
			//conn.close();

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
		return nbEmploye;


	}
	/**
	 * retourne le nombre d'employes ayants entre 18 et 30 ans
	 * @return
	 * @throws SQLException 
	 */
	public int getNombreEmployes18_30ans() throws SQLException
	{
		int nbEmploye=0;
		int annee18=getAnneeAge(18);
		int annee30=getAnneeAge(30);


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT COUNT(*) AS rowcount FROM employe WHERE ";

			rs = (ResultSet) stmt.executeQuery(select_structure);

			rs.next();
			nbEmploye = rs.getInt("rowcount") ;

			stmt.close(); ((java.sql.Connection) dbcon).close();


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
		return nbEmploye;


	}
	/**
	 * retourne l'année de naissance correspondant à l'age Age
	 * @return
	 */
	public int getAnneeAge(int age )
	{
		int annee=0;
		Calendar date = Calendar.getInstance();
		int anneeActuel= date.get(Calendar.YEAR);
		annee=anneeActuel-age;
		return annee;
	}


	public List getNombreEmployesParPoste(String code_structure, String code_poste) throws SQLException
	{

		ArrayList<StatTrancheAgePosteBean>   liststatbean = new ArrayList<StatTrancheAgePosteBean>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			if (code_structure.equalsIgnoreCase("-1")&& code_poste.equalsIgnoreCase("-1")){
				select_structure="select p.intitule_poste, if ((round( DATEDIFF(curdate(),e.date_naissance)/365))>18" +
						" and (round( DATEDIFF(curdate(),e.date_naissance)/365))<30 ,'1', if ((round( DATEDIFF(curdate(),e.date_naissance)/365))>30" +
						" and (round( DATEDIFF(curdate(),e.date_naissance)/365))<46 ,'2','3')) as tranche," +
						" round(count(e.code_poste)*100/(select count(*) from employe)) as pourcentage from employe e ,poste_travail_description p" +
						" where p.code_poste=e.code_poste group by intitule_poste,tranche order by tranche ";

			}else if (code_poste.equalsIgnoreCase("-1")){
				select_structure="select p.intitule_poste, if ((round( DATEDIFF(curdate(),e.date_naissance)/365))>18" +
						" and (round( DATEDIFF(curdate(),e.date_naissance)/365))<30 ,'1', if ((round( DATEDIFF(curdate(),e.date_naissance)/365))>30" +
						" and (round( DATEDIFF(curdate(),e.date_naissance)/365))<46 ,'2','3')) as tranche," +
						" round(count(e.code_poste)*100/(select count(*) from employe where code_structure =#code_structure)) as pourcentage from employe e ,poste_travail_description p" +
						" where p.code_poste=e.code_poste and  e.code_structure =#code_structure group by intitule_poste,tranche order by tranche ";

				select_structure = select_structure.replaceAll("#code_structure", "'"+ code_structure+"'");

			}
			else{
				select_structure="select p.intitule_poste, if ((round( DATEDIFF(curdate(),e.date_naissance)/365))>18" +
						" and (round( DATEDIFF(curdate(),e.date_naissance)/365))<30 ,'1', if ((round( DATEDIFF(curdate(),e.date_naissance)/365))>30" +
						" and (round( DATEDIFF(curdate(),e.date_naissance)/365))<46 ,'2','3')) as tranche," +
						" round(count(e.code_poste)*100/(select count(*) from employe where code_structure =#code_structure and code_poste=#code_poste)) as pourcentage from employe e ,poste_travail_description p" +
						" where p.code_poste=e.code_poste and  e.code_structure =#code_structure and e.code_poste=#code_poste group by intitule_poste,tranche order by tranche ";

				select_structure = select_structure.replaceAll("#code_structure", "'"+ code_structure+"'");
				select_structure = select_structure.replaceAll("#code_poste", "'"+ code_poste+"'");


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

	public List getNombreEmployesCadre(String code_structure, String code_poste,String filterdirection) throws SQLException
	{

		ArrayList<EmployeCadreBean>   liststatbean = new ArrayList<EmployeCadreBean>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();
			
			//generer le chart pour toutes les direction indépendement des structures et postes

			if (filterdirection.equalsIgnoreCase("-1")){

				sql_query="select d.gsp_libelle as is_cadre,round(count(e.code_poste)*100/(select count(*) from employe)) as pourcentage" +
						"  from employe e,poste_travail_description p, def_gsp d where p.code_poste=e.code_poste and p.gsp_id=d.gsp_id"
						+ " group by gsp_libelle";


			}
			

			//generer le chart pour une direction donnée  indépendement des structures et postes

			if (code_structure.equalsIgnoreCase("-1")&& code_poste.equalsIgnoreCase("-1")){

				sql_query="select d.gsp_libelle as is_cadre,round(count(e.code_poste)*100/(select count(*) from employe)) as pourcentage" +
						"  from employe e,poste_travail_description p, def_gsp d where p.code_poste=e.code_poste and p.gsp_id=d.gsp_id"
						+ " and "+filterdirection+ " group by gsp_libelle";


			}
			//generer le chart pour une structure  donnée  indépendement des postes

			 if (!code_structure.equalsIgnoreCase("-1")&& code_poste.equalsIgnoreCase("-1")){
				sql_query="select d.gsp_libelle as is_cadre,round(count(e.code_poste)*100/(select count(*) from employe where code_structure =#code_structure )) as pourcentage" +
						"  from employe e,poste_travail_description p, def_gsp d where p.code_poste=e.code_poste and p.gsp_id=d.gsp_id"
						+ " and e.code_structure =#code_structure "
						+ " group by gsp_libelle";
				//System.out.println(select_structure);
				sql_query = sql_query.replaceAll("#code_structure", "'"+ code_structure+"'");
				
			}
			
			//generer le chart pour une structure  donnée  et un poste donné

			if((!filterdirection.equalsIgnoreCase("-1"))&& (!code_structure.equalsIgnoreCase("-1"))&& (!code_poste.equalsIgnoreCase("-1"))) {
				sql_query="select d.gsp_libelle as is_cadre,round(count(e.code_poste)*100/(select count(*) from employe where code_structure =#code_structure and code_poste=#code_poste)) as pourcentage" +
						"  from employe e,poste_travail_description p, def_gsp d where p.code_poste=e.code_poste and p.gsp_id=d.gsp_id"
						+ " and e.code_structure =#code_structure and e.code_poste=#code_poste"
						+ " group by gsp_libelle";
				//System.out.println(select_structure);
				sql_query = sql_query.replaceAll("#code_structure", "'"+ code_structure+"'");
				sql_query = sql_query.replaceAll("#code_poste", "'"+ code_poste+"'");


			}


			//System.out.println(sql_query);
			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				EmployeCadreBean stat_bean=new EmployeCadreBean();
				stat_bean.setIs_cadre(rs.getString("is_cadre"));
				stat_bean.setPourcentage(rs.getInt("pourcentage"));

				liststatbean.add(stat_bean);


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
		return liststatbean;


	}

	public List getNombreEmployesEnciente(String code_structure,String code_poste) throws SQLException
	{

		ArrayList<StatTrancheAgePosteBean>   liststatbean = new ArrayList<StatTrancheAgePosteBean>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			if (code_structure.equalsIgnoreCase("-1")&& code_poste.equalsIgnoreCase("-1")){

				sql_query="select p.intitule_poste, if ((round( DATEDIFF(curdate(),e.date_recrutement)/365))>1  and (round( DATEDIFF(curdate(),e.date_recrutement)/365))<11 ,'1', if ((round( DATEDIFF(curdate(),e.date_recrutement)/365))>11 and (round( DATEDIFF(curdate(),e.date_recrutement)/365))<21 ,'2','3')) as tranche," +
						" round(count(e.code_poste)*100/(select count(*) from employe)) as pourcentage from employe e ,poste_travail_description p" +
						" where p.code_poste=e.code_poste group by intitule_poste,tranche order by tranche" ;

			}else if (code_poste.equalsIgnoreCase("-1")){
				sql_query="select p.intitule_poste, if ((round( DATEDIFF(curdate(),e.date_recrutement)/365))>1  and (round( DATEDIFF(curdate(),e.date_recrutement)/365))<11 ,'1', if ((round( DATEDIFF(curdate(),e.date_recrutement)/365))>11 and (round( DATEDIFF(curdate(),e.date_recrutement)/365))<21 ,'2','3')) as tranche," +
						" round(count(e.code_poste)*100/(select count(*) from employe where code_structure =#code_structure )) as pourcentage from employe e ,poste_travail_description p" +
						" where p.code_poste=e.code_poste and e.code_structure =#code_structure group by intitule_poste,tranche order by tranche" ;
				sql_query = sql_query.replaceAll("#code_structure", "'"+ code_structure+"'");

			}
			
			else{
				sql_query="select p.intitule_poste, if ((round( DATEDIFF(curdate(),e.date_recrutement)/365))>1  and (round( DATEDIFF(curdate(),e.date_recrutement)/365))<11 ,'1', if ((round( DATEDIFF(curdate(),e.date_recrutement)/365))>11 and (round( DATEDIFF(curdate(),e.date_recrutement)/365))<21 ,'2','3')) as tranche," +
						" round(count(e.code_poste)*100/(select count(*) from employe where code_structure =#code_structure and code_poste=#code_poste )) as pourcentage from employe e ,poste_travail_description p" +
						" where p.code_poste=e.code_poste and e.code_structure =#code_structure and e.code_poste=#code_poste group by intitule_poste,tranche order by tranche" ;
				sql_query = sql_query.replaceAll("#code_structure", "'"+ code_structure+"'");
				sql_query = sql_query.replaceAll("#code_poste", "'"+ code_poste+"'");


			}



			//System.out.println(sql_query);

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				StatTrancheAgePosteBean stat_bean=new StatTrancheAgePosteBean();
				stat_bean.setIntitule_poste(rs.getString("intitule_poste"));
				stat_bean.setTranche(rs.getString("tranche"));
				stat_bean.setPourcentage(rs.getInt("pourcentage"));

				liststatbean.add(stat_bean);


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
		return liststatbean;


	}

	public List getNombreEmployesNivForm(String code_structure, String code_poste) throws SQLException
	{

		ArrayList<EmployeFormationBean>   liststatbean = new ArrayList<EmployeFormationBean>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			if (code_structure.equalsIgnoreCase("-1")&& code_poste.equalsIgnoreCase("-1")){

				sql_query="select d.niv_for_libelle as libelle_formation,round(count(e.code_poste)*100/(select count(*) from employe)) as pourcentage" +
						" from employe e,formation f, def_niv_formation d where f.code_formation=e.code_formation" +
						" and   d.niv_for_id=f.niv_for_id group by niv_for_libelle" ;
			}else if (code_poste.equalsIgnoreCase("-1")){
				sql_query="select d.niv_for_libelle as libelle_formation,round(count(e.code_poste)*100/(select count(*) from employe where code_structure =#code_structure)) as pourcentage" +
						" from employe e,formation f, def_niv_formation d where f.code_formation=e.code_formation" +
						" and   d.niv_for_id=f.niv_for_id  and e.code_structure =#code_structure group by niv_for_libelle" ;

				sql_query = sql_query.replaceAll("#code_structure", "'"+ code_structure+"'");

			}
			
			else{
				sql_query="select d.niv_for_libelle as libelle_formation,round(count(e.code_poste)*100/(select count(*) from employe where code_structure =#code_structure and code_poste=#code_poste)) as pourcentage" +
						" from employe e,formation f, def_niv_formation d where f.code_formation=e.code_formation" +
						" and   d.niv_for_id=f.niv_for_id  and e.code_structure =#code_structure and e.code_poste=#code_poste group by niv_for_libelle" ;

				sql_query = sql_query.replaceAll("#code_structure", "'"+ code_structure+"'");
				sql_query = sql_query.replaceAll("#code_poste", "'"+ code_poste+"'");


			}



			//System.out.println(sql_query);

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				EmployeFormationBean stat_bean=new EmployeFormationBean();
				stat_bean.setNiveau(rs.getString("libelle_formation"));
				stat_bean.setPourcentage(rs.getInt("pourcentage"));

				liststatbean.add(stat_bean);


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
		return liststatbean;


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
	
	
	
	/***
	 * recupèrer la liste des poste d'une structure donnée
	 */
		public Map getListPosteStructure(String code_structure ) throws SQLException
		{
			
			HashMap map = new HashMap();
			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToSecondairesDB();
			ResultSet rs=null;
			PreparedStatement psmt = null;
				
			try 
			{
				String select_structure="select p.code_poste,p.intitule_poste from employe e, poste_travail_description p where e.code_poste=p.code_poste and e.code_structure=? order by p.intitule_poste";
				psmt =  conn.prepareStatement(select_structure);
				psmt.setString(1, code_structure);
				rs =  psmt.executeQuery();

				while(rs.next()){
					map.put( rs.getString("intitule_poste"),rs.getString("code_poste"));
				}

			} 
			catch ( SQLException e ) {
				
				e.printStackTrace();

			} finally {
				
				if ( rs != null ) {
					try {
						rs.close();
					} catch ( SQLException ignore ) {
					}
				}

				if ( psmt != null ) {
					try {
						psmt.close();
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
			return treeMap;


		}
		
		
		
		/**
		 * cette méthode retrourne  La liste des direction si direction est null dans la table structure_entreprise,
		 * on recupère la division
		 *  @return
		 */
		public HashMap<String,List<String>> getListDirection()	{

		
			String query="";

		
								
					query="select code_structure, case   when length(trim(direction))=0 then 'DIR NON RENSEIGNEE' ELSE direction END direction "
							+ " from ("
							+ "	select code_structure,libelle_direction as direction from structure_entreprise where length(trim(libelle_direction))!=0 "
							+ " union distinct"
							+ " select code_structure,libelle_division as direction from structure_entreprise where length(trim(libelle_direction))=0 "
							+ " )  tb order by direction";


			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToSecondairesDB();
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
		
		
		public Map getStructEntList(List <String> list_direction) throws SQLException
		{
			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToSecondairesDB();
			Statement stmt = null;
			HashMap map = new HashMap();
			ResultSet rs=null;
			
			String structure = "(";
			for ( int i=0;i<list_direction.size();i++){
				if (i <list_direction.size()-1)
			        structure+="'"+list_direction.get(i)+"',";
				else
					 structure+="'"+list_direction.get(i)+"'";
			}
			structure+=")";
			
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
								"  and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise where code_structure in "+structure+"  order by structure_ent ";
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
				//String db_list="select id_compagne,concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) as libelle_compagne from compagne_evaluation where now()<=date_fin";
				String db_list="select id_compagne,concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) as libelle_compagne from compagne_evaluation order by id_compagne desc";


				rs = (ResultSet) stmt.executeQuery(db_list);


				while(rs.next()){
					map.put( rs.getString("libelle_compagne"),rs.getInt("id_compagne"));
				}
				//map.put("Selectionner Compagne",-1);
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
