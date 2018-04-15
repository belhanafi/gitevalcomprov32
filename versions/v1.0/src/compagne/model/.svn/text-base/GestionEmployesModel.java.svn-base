package compagne.model;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;

import administration.bean.AdministrationLoginBean;
import administration.bean.CompagneCreationBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.StructureEntrepriseBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import common.ApplicationFacade;
import common.CreateDatabaseCon;
import common.PwdCrypt;
import compagne.bean.GestionEmployesBean;
import compagne.bean.PlanningCompagneListBean;

public class GestionEmployesModel {
	

private ArrayList<GestionEmployesBean>  listcompagne =null; 
private ListModel strset =null;
	
	/**
	 * cette méthode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	public List loadListEmployes() throws SQLException{
		
		
		listcompagne = new ArrayList<GestionEmployesBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select concat(c.nom,'-',c.prenom) as nom, id_employe,date_naissance,date_recrutement ,concat(libelle_formation,'-',libelle_diplome) as libelle_formation,p.intitule_poste, email,  CASE WHEN est_evaluateur='N' THEN 'NON' ELSE 'OUI' END as est_evaluateur," +
					        " CASE WHEN est_responsable_rh='N' THEN 'NON' ELSE 'OUI' END as est_responsable_rh ,e.code_structure" +
					        " from employe e  ,poste_travail_description p,formation f,common_evalcom.compte c  where e.code_poste=p.code_poste and e.code_formation=f.code_formation  and   e.id_compte=c.id_compte order by id_employe" ;
					        
					        ResultSet rs = (ResultSet) stmt.executeQuery(sel_comp);
			
			while(rs.next()){
				
				GestionEmployesBean bean=new GestionEmployesBean();
				
				//bean.setId_compte((rs.getInt("id_compte")));
				bean.setId_employe((rs.getInt("id_employe")));	
				bean.setNom_complet(rs.getString("nom"));
				//bean.setPrenom(rs.getString("prenom"));
				bean.setDate_naissance((rs.getDate("date_naissance")));
				bean.setDate_recrutement((rs.getDate("date_recrutement")));
				bean.setLibelle_formation(rs.getString("libelle_formation"));
				bean.setIntitule_poste(rs.getString("intitule_poste"));
				bean.setEmail(rs.getString("email"));
				bean.setEst_evaluateur(rs.getString("est_evaluateur"));
				bean.setEst_responsable_rh(rs.getString("est_responsable_rh"));
				bean.setCode_structure(rs.getString("code_structure"));
				
				listcompagne.add(bean);
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		return listcompagne;
	
		
		
	}
	
	/**
	 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
	 * @param addedData
	 * @return
	 * @throws ParseException 
	 */
	public boolean addEmploye(GestionEmployesBean addedData) throws ParseException
	{
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		
		try 
		{
			                                                
			stmt = (Statement) conn.createStatement();
			String sql_query="INSERT INTO employe( nom, prenom, date_naissance, rattach_dg, date_recrutement, code_formation, code_poste, email, est_evaluateur, est_responsable_service, est_responsable_direction, est_responsable_division, est_responsable_departement, est_responsable_unite, est_responsable_section, est_responsable_rh, code_structure, id_compte)" +
					         " VALUES(#nom,#prenom,#date_naissance,'N',#date_recrutement,#code_formation,#code_poste,#email,#est_evaluateur,'N','N','N','N','N','N',#est_responsable_rh,#code_structure,#id_compte)";
			//sql_query = sql_query.replaceAll("#id_employe", "'"+ addedData.getId_employe()+"'");
			sql_query = sql_query.replaceAll("#nom", "'"+ addedData.getNom()+"'");
			sql_query = sql_query.replaceAll("#prenom", "'"+ addedData.getPrenom()+"'");
			sql_query = sql_query.replaceAll("#date_naissance", "'"+ formatter.format(addedData.getDate_naissance())+"'");
			sql_query = sql_query.replaceAll("#date_recrutement", "'"+ formatter.format(addedData.getDate_recrutement())+"'");
			sql_query = sql_query.replaceAll("#code_formation", "'"+ addedData.getCode_formation()+"'");
			sql_query = sql_query.replaceAll("#code_poste", "'"+ addedData.getCode_poste()+"'");
			sql_query = sql_query.replaceAll("#email", "'"+ addedData.getEmail()+"'");
			sql_query = sql_query.replaceAll("#est_evaluateur", "'"+ addedData.getEst_evaluateur()+"'");
			sql_query = sql_query.replaceAll("#est_responsable_rh", "'"+ addedData.getEst_responsable_rh()+"'");
			//sql_query = sql_query.replaceAll("#code_structure", "'"+ addedData.getCode_structure()+"'");
			sql_query = sql_query.replaceAll("#code_structure", "'"+ "S0000"+"'");
			sql_query = sql_query.replaceAll("#id_compte", "'"+ addedData.getId_compte()+"'");
		
				
		//System.out.println(sql_query);
			
			 stmt.execute(sql_query);
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("La donnée n'a pas été insérée dans la base données", "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				
				e1.printStackTrace();
				return false;
			}
			
			
			return false;
		}
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * cette classe permet de controler la validité des données insérées (par rapport à leurs taille)
	 * @param addedData
	 * @return
	 * @throws InterruptedException 
	 */
	
	public boolean controleIntegrite(GestionEmployesBean addedData) throws InterruptedException
	{
		try 
		{   	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                Integer chaine=addedData.getEmail().indexOf("@");
						
			
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			if(addedData.getDate_naissance().after(addedData.getDate_recrutement()))
			{
				Messagebox.show("La date de naissance doit est inferieure à la date de recrutement !", "Erreur",Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			else
				if(chaine==-1 )
				{
				Messagebox.show("L'adresse mail saisie n'est pas une adresse valide !", "Erreur",Messagebox.OK, Messagebox.ERROR);
				return false;
				}
				
								
									
		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (NumberFormatException nfe)
	    {
			Messagebox.show("Le mot de passe doit être un entier composé de 8 chiffres Exemple 21012001", "Erreur",Messagebox.OK, Messagebox.ERROR);
			return false;
	    }
		
			
		return true;
	}
	
	/**
	 * Cette classe permet de mettre à jour la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	public Boolean updateListeEmploye(GestionEmployesBean addedData)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
		    stmt = (Statement) conn.createStatement();
		    String sql_query="update employe  set nom=#nom, prenom=#prenom, date_naissance=#date_naissance, rattach_dg='N', date_recrutement=#date_recrutement, code_formation=#code_formation, code_poste=#code_poste, email=#email, est_evaluateur=#est_evaluateur, est_responsable_service='N', est_responsable_direction='N', est_responsable_division='N', est_responsable_departement='N'," +
		    		          " est_responsable_unite='N', est_responsable_section='N', est_responsable_rh=#est_responsable_rh, " +
		    		           " code_structure=#code_structure, id_compte=#id_compte where id_employe=#id_employe " ;
		
			
			sql_query = sql_query.replaceAll("#id_employe", "'"+ addedData.getId_employe()+"'");
			sql_query = sql_query.replaceAll("#nom", "'"+ addedData.getNom()+"'");
			sql_query = sql_query.replaceAll("#prenom", "'"+ addedData.getPrenom()+"'");
			sql_query = sql_query.replaceAll("#date_naissance", "'"+ formatter.format(addedData.getDate_naissance())+"'");
			sql_query = sql_query.replaceAll("#date_recrutement", "'"+ formatter.format(addedData.getDate_recrutement())+"'");
			sql_query = sql_query.replaceAll("#code_formation", "'"+ addedData.getCode_formation()+"'");
			sql_query = sql_query.replaceAll("#code_poste", "'"+ addedData.getCode_poste()+"'");
			sql_query = sql_query.replaceAll("#email", "'"+ addedData.getEmail()+"'");
			sql_query = sql_query.replaceAll("#est_evaluateur", "'"+ addedData.getEst_evaluateur()+"'");
			sql_query = sql_query.replaceAll("#est_responsable_rh", "'"+ addedData.getEst_responsable_rh()+"'");
			//sql_query = sql_query.replaceAll("#code_structure", "'"+ addedData.getCode_structure()+"'");
			sql_query = sql_query.replaceAll("#code_structure", "'"+ "S0000"+"'");
			sql_query = sql_query.replaceAll("#id_compte", "'"+ addedData.getId_compte()+"'");
		
			
		  // System.out.println(sql_query);
			
			 stmt.executeUpdate(sql_query);
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("La modification n'a pas été prise en compte car il existe une donnée ayant le même code établissement", "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				
				e1.printStackTrace();
				//return false;
			}
			
			
			return false;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * cette classe permet de supprimer une donnée de la table structure_entreprise
	 * @param codeStructure
	 */
	public Boolean deleteEmploye(GestionEmployesBean addedData)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="DELETE FROM  employe   where id_employe=#id_employe " ;
		
			
			sql_query = sql_query.replaceAll("#id_employe", "'"+ addedData.getId_employe()+"'");
			
		
			
			 stmt.executeUpdate(sql_query);
		} 
		catch (SQLException e) 
		{
			
				e.printStackTrace();
				return false;


		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/*
	*//**
	 * cette classe permet de supprimer une donnée de la table structure_entreprise
	 * @param codeStructure
	 * @throws SQLException 
	 *//*
	*/
	
	public HashMap getListFormation() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select   code_formation ,concat(libelle_formation,'-',libelle_diplome) as libelle_formation  from formation"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("libelle_formation"), rs.getString("code_formation"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	public HashMap getListPostes() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select code_poste,intitule_poste from poste_travail_description"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("intitule_poste"), rs.getString("code_poste"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	
	
	
	public HashMap getListStructure() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
			
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select code_structure  from structure_entreprise"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("code_structure"), rs.getString("code_structure"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	
	public HashMap selectedPoste(int employe) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
			
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select e.code_poste,intitule_poste from  employe e ,poste_travail_description t" +
					"  where id_employe=#employe and e.code_poste=t.code_poste "; 
			sql_query = sql_query.replaceAll("#employe", "'"+employe+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
			
			
			while(rs.next()){
				map.put(rs.getString("intitule_poste"), rs.getString("code_poste"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	
	public HashMap selectEmployes(String code_poste) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
			
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select id_employe,concat(nom,' ',prenom) as evalue from employe where est_evaluateur='N'" +
					         "  and code_poste=#code_poste  "; 
			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
			
			
			while(rs.next()){
				map.put(rs.getString("evalue"), rs.getInt("id_employe"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}
	


	public HashMap isEvaluateur() 
	{
		
		HashMap map = new HashMap();
		
		map.put("OUI", "Y");
		map.put("NON", "N");
		return (HashMap) sortByComparator(map);
	}
	
	public HashMap isResRH() 
	{
		
		HashMap map = new HashMap();
		
		map.put("OUI", "Y");
		map.put("NON", "N");
		return (HashMap) sortByComparator(map);
	}
	
	
	
	
	
	
	
	public HashMap getCompteList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		int database_id=ApplicationFacade.getInstance().getClient_database_id();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select id_compte,concat(nom,'-',prenom) as nom from common_evalcom.compte where database_id=#database_id";
			sql_query = sql_query.replaceAll("#database_id", "'"+database_id+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
			
			
			while(rs.next()){
				map.put( rs.getString("nom"),rs.getInt("id_compte"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
		
		
	}	
	
	public HashMap setlectedStructure(String code_structure) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();

		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select concat_ws('-->',libelle_division,libelle_direction,libelle_unite,libelle_departement,libelle_service,libelle_section) as structure from structure_entreprise where code_structure=#code_structure";
			sql_query = sql_query.replaceAll("#code_structure", "'"+code_structure+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
			
			
			while(rs.next()){
				map.put( rs.getString("structure"),rs.getString("structure"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
		
		
	}
	
	
	
	/*
	
	public String getCurrentDatetime(){
		Date today = Calendar.getInstance().getTime();
	    // (2) create our "formatter" (our custom format)
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	    // (3) create a new String in the format we want
	    String todaydate = formatter.format(today);
	    
	    return todaydate;
		
	}
	
	public  Integer getKeyMap(String key) throws SQLException{
		Integer idprofile=(Integer)gerProfileList() .get(key);
		return idprofile;
	}
	
	public static boolean isValidDateStr(String date) {
	    try {
	      String format="yyyy/MM/dd";
	    	SimpleDateFormat sdf = new SimpleDateFormat(format);
	      sdf.setLenient(false);
	      sdf.parse(date);
	    }
	    catch (ParseException e) {
	      return false;
	    }
	    catch (IllegalArgumentException e) {
	      return false;
	    }
	    return true;
	    }*/
	
	
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



}
