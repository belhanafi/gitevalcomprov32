package Statistique.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import common.CreateDatabaseCon;

public class ExtractRepCompModel {
	
	public HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> getCompagneCompetence()
	{
		HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> mapFamilleGroupeCompetencePoste =new HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>();
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select distinct r.famille, r.groupe, r.libelle_competence, d.intitule_poste"
							+" from repertoire_competence r, poste_travail_description d , poste_travail_competence c"
							+" where d.code_poste=c.code_poste and c.code_competence=r.code_competence";
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String famille=rs.getString("famille");
					String groupe=rs.getString("groupe");
					String libelle_competence=rs.getString("libelle_competence");
					String intitule_poste=rs.getString("d.intitule_poste");
					
					
					if(mapFamilleGroupeCompetencePoste.containsKey(famille))
					{
						HashMap<String, HashMap<String, ArrayList<String>>> mapGroupeCompetencePoste=mapFamilleGroupeCompetencePoste.get(famille);
						if(mapGroupeCompetencePoste.containsKey(groupe))
						{
							HashMap<String, ArrayList<String>> mapCompetencePoste=mapGroupeCompetencePoste.get(groupe);
							if(mapCompetencePoste.containsKey(libelle_competence))
							{
								
								ArrayList<String> listPosteTravail=mapCompetencePoste.get(libelle_competence);
								listPosteTravail.add(intitule_poste);
								mapCompetencePoste.put(libelle_competence, listPosteTravail);
								mapGroupeCompetencePoste.put(groupe, mapCompetencePoste);
								mapFamilleGroupeCompetencePoste.put(famille, mapGroupeCompetencePoste);
								
							}
							else
							{
								ArrayList<String> listPosteTravail=new ArrayList<String>();
								listPosteTravail.add(intitule_poste);
								mapCompetencePoste.put(libelle_competence, listPosteTravail);
								mapGroupeCompetencePoste.put(groupe, mapCompetencePoste);
								mapFamilleGroupeCompetencePoste.put(famille, mapGroupeCompetencePoste);
							}
							
						}
						else
						{
							ArrayList<String> listPosteTravail=new ArrayList<String>();
							listPosteTravail.add(intitule_poste);
							HashMap<String, ArrayList<String>> mapCompetencePoste=new HashMap <String, ArrayList<String>>();
							mapCompetencePoste.put(libelle_competence, listPosteTravail);
							mapGroupeCompetencePoste.put(groupe, mapCompetencePoste);
							mapFamilleGroupeCompetencePoste.put(famille, mapGroupeCompetencePoste);
						}
					}
					else
					{
						ArrayList<String> listPosteTravail=new ArrayList<String>();
						listPosteTravail.add(intitule_poste);
						HashMap<String, ArrayList<String>> mapCompetencePoste=new HashMap <String, ArrayList<String>>();
						mapCompetencePoste.put(libelle_competence, listPosteTravail);
						HashMap<String, HashMap<String, ArrayList<String>>> mapGroupeCompetencePoste=new HashMap <String, HashMap<String, ArrayList<String>>>();
						mapGroupeCompetencePoste.put(groupe, mapCompetencePoste);
						mapFamilleGroupeCompetencePoste.put(famille, mapGroupeCompetencePoste);
					}
	
				}
				else 
				{
					return mapFamilleGroupeCompetencePoste;
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
		
		
		return mapFamilleGroupeCompetencePoste;
	}
	
	public HashMap<String, Integer> getNBCompetenceParFamille()
	{
		HashMap<String, Integer> mapFamilleNbCompetence=new HashMap<String, Integer>();
		
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select  famille, count(code_competence) as code_comp from repertoire_competence group by famille";
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String famille=rs.getString("famille");
					String nb_competence=rs.getString("code_comp");
					mapFamilleNbCompetence.put(famille, new Integer(nb_competence));
					
	
				}
				else {
					return mapFamilleNbCompetence;
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
		
		
		return mapFamilleNbCompetence;
	}

	
	
	public HashMap<String, Integer> getNBCompetenceParGroupe()
	{
		HashMap<String, Integer> mapFamilleNbGroupe=new HashMap<String, Integer>();
		
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select  groupe, count(code_competence) as code_comp from repertoire_competence group by groupe";
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String groupe=rs.getString("groupe");
					String nb_competence=rs.getString("code_comp");
					mapFamilleNbGroupe.put(groupe, new Integer(nb_competence));
					
	
				}
				else {
					return mapFamilleNbGroupe;
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
		
		
		return mapFamilleNbGroupe;
	}
	
	
	public ArrayList<String> getPoste_Travail()
	{
		ArrayList<String> listePosteTravail=new ArrayList<String>();
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select intitule_poste from poste_travail_description where code_poste in(select distinct code_poste from planning_evaluation)";
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String intitule_poste=rs.getString("intitule_poste");
					
					listePosteTravail.add(intitule_poste);
					
	
				}
				else 
				{
					return listePosteTravail;
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
		
		return listePosteTravail;
	}
}
