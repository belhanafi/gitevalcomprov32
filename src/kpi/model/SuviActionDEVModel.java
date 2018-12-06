package kpi.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import kpi.bean.ProgresActionDevBean;
import kpi.bean.SuiviActionDevBean;
import common.CreateDatabaseCon;

public class SuviActionDEVModel {
	
	
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
	
	
	public ArrayList<ProgresActionDevBean> exportRapport(int id_compagne) throws SQLException
	{
        
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ArrayList<ProgresActionDevBean> listmoyfam = new ArrayList<ProgresActionDevBean>();
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();


			 sql_query="select login,concat(m.nom,concat(' ',m.prenom)) nom,intitule_poste,action,libelle_direction,structure_ent,"
					+ " round(sum(progres) *100/(select count(id_employe) from planning_evaluation where id_compagne=#id_compagne and id_evaluateur=p.id_evaluateur),2) taux from("
					+ " select e.id_employe,d.intitule_poste,concat(t.libelle_action_formation,concat(libelle_action_ori_prof,concat("
					+ " libelle_action_disipline,concat(libelle_action_mobilite,'')))) as action,"
					+ " count(realisee) progres from actionsdev_employe e, employe y,"
					+ " poste_travail_description d, actions_developpement t"
					+ " where e.id_employe=y.id_employe and y.code_poste=d.code_poste and t.id_action=e.id_action and realisee='O'"
					+ " group by  1,2,3) tab  , planning_evaluation p , structure_entreprise s,employe m,"
					+ " (select code_structure, structure_ent from ("
					+ "				 select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
					+ "				 and  libelle_section !='null' and  libelle_section !=''"
					+ "   			 union"
					+ "             select code_structure,libelle_service structure_ent from structure_entreprise"
					+ "          	where libelle_service is  not null"
					+ "             and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
					+ "             union"
					+ "             select code_structure,libelle_departement structure_ent from structure_entreprise"
					+ "             where libelle_departement is  not null"
					+ " 			and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
					+ "             union"
					+ " 			select code_structure,libelle_sous_direction structure_ent from structure_entreprise"
					+ "             where libelle_sous_direction is  not null"
					+ " 			and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
					+ "             union"
					+ "             select code_structure,libelle_unite structure_ent from structure_entreprise"
					+ "             where libelle_unite is  not null"
					+ "            and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "            and length(libelle_service)=0 and  length(libelle_section) =0"
					+ "            union"
					+ "            select code_structure,libelle_direction structure_ent from structure_entreprise"
					+ "            where libelle_direction is  not null"
					+ " 		   and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "            and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise) tt, common_evalcom.compte c "
					+ "            where tab.id_employe=p.id_employe and p.id_compagne=#id_compagne and p.id_evaluateur=m.id_employe and tt.code_structure=p.code_structure and m.id_compte=c.id_compte"
					+ "            and s.code_structure=p.code_structure group by 1,4"; 


					
			sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");



			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				ProgresActionDevBean bean=new ProgresActionDevBean();
				bean.setLogin(rs.getString("login"));
				bean.setNom(rs.getString("nom"));
				bean.setIntitule_poste(rs.getString("intitule_poste"));
				bean.setAction(rs.getString("action"));
				bean.setLibelle_direction(rs.getString("libelle_direction"));
				bean.setStructure_ent(rs.getString("structure_ent"));
				bean.setTaux(rs.getFloat("taux"));

				
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
