package administration.model;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;




import administration.bean.CompetencePosteTravailBean;

import administration.model.CompetencePosteTravailModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import common.CreateDatabaseCon;


public class CompetencePosteTravailModel {

	
	/**
	 * mise à jour de la table postedetravail_competence en supprimant des associations existantes
	 * 
	 */
	
	public void updateUnCheckedPoteTravailCompetence(ArrayList <String>listunselected, HashMap <String, String>mapCodeCompetence, HashMap <String, String>mapCodePoste)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		Iterator<String> iterator =listunselected.iterator();
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
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * mise à jour de la table postedetravail_competence en ajoutant des associations existantes
	 * 
	 */
	
	public void updateCheckedPoteTravailCompetence(ArrayList <String>listselected, HashMap <String, String>mapCodeCompetence, HashMap <String, String>mapCodePoste)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		Iterator<String> iterator =listselected.iterator();
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
			
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT intitule_poste  FROM poste_travail_description where code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
            " where c.id_compagne=p.id_compagne and now()<=date_fin) ";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
			
		return listposteTravail;	
	
	}
	
	public HashMap<String, String> getlistepostesCode_postes()
	{
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		HashMap<String, String > mapcode_poste=new HashMap<String, String >();
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from planning_evaluation) ";
			String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
            " where c.id_compagne=p.id_compagne and now()<=date_fin) ";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
			
		return mapcode_poste;	
	
	}
	
	public HashMap<String, String> getlisteCode_postes_intituleposte()
	{
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		HashMap<String, String > mapcode_poste=new HashMap<String, String >();
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from planning_evaluation) ";
			String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
            " where c.id_compagne=p.id_compagne and now()<=date_fin) ";
			
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
			
		return mapcode_poste;	
	
	}
	
	public HashMap<String, String> getlisteLibelle_code_competence()
	{
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		HashMap<String, String > mapcode_competence=new HashMap<String, String >();
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT libelle_competence,  code_competence FROM repertoire_competence ";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
			
		return mapcode_competence;	
	
	}
	/**
	 * récupération des données qui associe le repertoire de competence au poste de travail
	 * @throws SQLException 
	 */
	public CompetencePosteTravailBean getCompetencePosteTravailBean() throws SQLException
	{
		//ArrayList<String> listposteTravail = new ArrayList<String>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		
		Statement stmt;
		
		

			stmt = (Statement) conn.createStatement();
			//String select_structure="select distinct a.id_repertoire_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste from repertoire_competence a, poste_travail_competence b, poste_travail_description e where a.code_competence=b.code_competence and e.code_poste=b.code_poste and e.code_poste in(select distinct code_poste from planning_evaluation)"; 
			//String select_structure="select distinct  a.code_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste from repertoire_competence a, poste_travail_competence b, poste_travail_description e where a.code_competence=b.code_competence and e.code_poste=b.code_poste and e.code_poste in(select distinct code_poste from planning_evaluation)";
			/*String select_structure="select distinct  a.code_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste" +
			" from  poste_travail_description e ,repertoire_competence a LEFT OUTER  JOIN poste_travail_competence b" +
			" on ( a.code_competence=a.code_competence) where e.code_poste in(select distinct code_poste from planning_evaluation)";*/
			
			String select_structure="select distinct  a.code_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste" +
			" from  poste_travail_description e ,repertoire_competence a LEFT OUTER  JOIN poste_travail_competence b" +
			" on ( a.code_competence=a.code_competence) where e.code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
					                " where c.id_compagne=p.id_compagne and now()<=date_fin)";
            
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			CompetencePosteTravailBean competencePosteTravailBean=new CompetencePosteTravailBean();
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
			return competencePosteTravailBean;

		
	}
	
	public HashMap<String , ArrayList<String>> getAssociationPosteTravailCompetence()
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		HashMap<String, ArrayList<String> > mapcompetence_poste=new HashMap<String, ArrayList<String>>();
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="select distinct  p.intitule_poste ,  c.libelle_competence from  repertoire_competence c, poste_travail_description p, poste_travail_competence k where k.code_competence=c.code_competence and k.code_poste=p.code_poste  and p.code_poste in(select distinct code_poste from planning_evaluation) ";
			//String select_structure="select  code_poste, code_competence from poste_travail_competence where code_poste in(select distinct code_poste from planning_evaluation)";
			String select_structure="select  code_poste, code_competence from poste_travail_competence where code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
					                " where c.id_compagne=p.id_compagne and now()<=date_fin )";
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
			
		return mapcompetence_poste;
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
}
