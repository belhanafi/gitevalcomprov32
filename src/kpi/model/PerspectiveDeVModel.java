package kpi.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import kpi.bean.KpiIMIAgeBean;
import kpi.bean.PerspectiveDevBean;
import common.CreateDatabaseCon;

public class PerspectiveDeVModel {
	
	
	/**
	 * cette méthode retrourne  IMI par experience 
	 *  @return
	 */
	public HashMap<String,  ArrayList<PerspectiveDevBean>> getResultatIMIComp(HashMap<String, HashMap<String, Integer>> listDb)	{

		HashMap<String,  ArrayList<PerspectiveDevBean>> resultat= new HashMap<String,  ArrayList<PerspectiveDevBean>>();
		HashMap<String, Integer> subresultat= new  HashMap<String, Integer>();

		String query="";

		for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

			int map_size=listDb.entrySet().size();

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String vague = pair.getKey();
				Integer idcompagne = pair.getValue();
				
				query="select distinct r.famille,r.libelle_competence,r.aptitude_observable,moy_competence from "+entry.getKey()+"."+"imi_competence_stat i , "+entry.getKey()+"."+"repertoire_competence r"
						+ " where r.code_famille=i.code_famille and i.code_competence=r.code_competence"
						+ " and moy_competence <2 and id_compagne="+idcompagne;
				//System.out.println(">>>"+query);

			}

		}

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		ResultSet rs=null;
		ArrayList<PerspectiveDevBean> listbean = new ArrayList<PerspectiveDevBean>();
		try 
		{
			stmt = (Statement) conn.createStatement();

			rs = (ResultSet) stmt.executeQuery(query);
			while(rs.next()){

				PerspectiveDevBean bean =new PerspectiveDevBean();
				bean.setFamille(rs.getString("famille"));
				bean.setLibelle_competence(rs.getString("libelle_competence"));
				bean.setAptitude_observable(rs.getString("aptitude_observable"));
				bean.setMoy_competence(rs.getFloat("moy_competence"));
				listbean.add(bean);
				
				resultat.put(rs.getString("famille")+":"+rs.getString("libelle_competence"), listbean);

				


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
		return resultat;


	}
	
	
	

}
