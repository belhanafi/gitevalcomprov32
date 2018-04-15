package compagne.model;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import common.CreateDatabaseCon;
import common.PwdCrypt;

public class CompagneCreationModel {
	

private ArrayList<CompagneCreationBean>  listcompagne =null; 
private ListModel strset =null;
	
	/**
	 * cette méthode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	public List loadCompagnelist() throws SQLException{
		
		
		listcompagne = new ArrayList<CompagneCreationBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select id_compagne,libelle_compagne,compagne_type,DATE_FORMAT(date_debut, '%Y-%m-%d')as date_debut,DATE_FORMAT(date_fin, '%Y-%m-%d') as date_fin,c.id_compagne_type from compagne_evaluation c,compagne_type t" +
					        " where c.id_compagne_type=t.id_compagne_type";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_comp);
			
			while(rs.next()){
				
				CompagneCreationBean compagne=new CompagneCreationBean();
				compagne.setId_compagne(rs.getInt("id_compagne"));	
				compagne.setNom_compagne(rs.getString("libelle_compagne"));
				compagne.setType_compagne(rs.getString("compagne_type"));
				compagne.setDate_deb_comp(rs.getDate("date_debut"));
				compagne.setDate_fin_comp(rs.getDate("date_fin"));
				compagne.setId_compagne_type(rs.getInt("id_compagne_type"));


			
					  
				listcompagne.add(compagne);
				   
					
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
	public boolean addCompagne(CompagneCreationBean addedData) throws ParseException
	{
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		
		try 
		{
			                                                
			stmt = (Statement) conn.createStatement();
			String select_structure="INSERT INTO compagne_evaluation ( date_debut, date_fin,libelle_compagne ,id_compagne_type) VALUES (#date_debut,#date_fin,#libelle_compagne ,#id_compagne_type)";
			
			select_structure = select_structure.replaceAll("#date_debut", "'"+ formatter.format(addedData.getDate_deb_comp())+"'");
			select_structure = select_structure.replaceAll("#date_fin", "'"+formatter.format(addedData.getDate_fin_comp())+"'");
			select_structure = select_structure.replaceAll("#libelle_compagne", "'"+addedData.getNom_compagne()+"'");
			select_structure = select_structure.replaceAll("#id_compagne_type", "'"+String.valueOf(addedData.getId_compagne_type())+"'");
			
		//System.out.println(select_structure);
			
			 stmt.execute(select_structure);
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
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				
				e1.printStackTrace();
				return false;
			}
			
			
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
	
	/**
	 * cette classe permet de controler la validité des données insérées (par rapport à leurs taille)
	 * @param addedData
	 * @return
	 * @throws InterruptedException 
	 */
	
	public boolean controleIntegrite(CompagneCreationBean addedData) throws InterruptedException
	{
		try 
		{   
			
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
					
			
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			if(addedData.getNom_compagne().length()>50)
			{
				Messagebox.show("La taille du champ nom ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
				
				return false;
			}
			else
								if(addedData.getDate_deb_comp().after(addedData.getDate_fin_comp()))
								{
									Messagebox.show("La date fin de  compagne doit être superieure à la date debut compagne !", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	public Boolean updateCompagne(CompagneCreationBean addedData)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
		    stmt = (Statement) conn.createStatement();
            String select_structure="Update compagne_evaluation set date_debut=#date_debut, date_fin=#date_fin,libelle_compagne=#libelle_compagne ,id_compagne_type=#id_compagne_type where id_compagne=#id_compagne";
			select_structure = select_structure.replaceAll("#date_debut", "'"+ formatter.format(addedData.getDate_deb_comp())+"'");
			select_structure = select_structure.replaceAll("#date_fin", "'"+formatter.format(addedData.getDate_fin_comp())+"'");
			select_structure = select_structure.replaceAll("#libelle_compagne", "'"+addedData.getNom_compagne()+"'");
			select_structure = select_structure.replaceAll("#id_compagne_type", "'"+String.valueOf(addedData.getId_compagne_type())+"'");
			select_structure = select_structure.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");
					
			
		   //System.out.println(update_structure);
			
			 stmt.executeUpdate(select_structure);
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("La modification n'a pas été prise en compte", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	public Boolean deleteCompagne(CompagneCreationBean addedData)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="DELETE FROM  compagne_evaluation   where id_compagne=#id_compagne"; 
			sql_query = sql_query.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");
			
		
			
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
	
	public HashMap getCompagneType() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		List list_profile=new ArrayList();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select id_compagne_type,compagne_type from compagne_type"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("compagne_type"), rs.getInt("id_compagne_type"));
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
	/*
	
	public HashMap getDatabaseList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String db_list="select  database_id, nom_base from liste_db"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(db_list);
			
			
			while(rs.next()){
				map.put( rs.getString("nom_base"),rs.getInt("database_id"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	
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


}
