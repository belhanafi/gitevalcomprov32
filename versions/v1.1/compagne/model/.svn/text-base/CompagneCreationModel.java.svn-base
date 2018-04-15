package compagne.model;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

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
import compagne.bean.CompagnePosteMapBean;

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
	
	
	public HashMap getListCompagne() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		List list_profile=new ArrayList();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select id_compagne,concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) as libelle_compagne from compagne_evaluation where now()<=date_fin order by date_fin"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("libelle_compagne"), rs.getInt("id_compagne"));
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
	

public List loadPosteMapToComapgne(int compagne_id) throws SQLException{
		
		
		ArrayList<CompagnePosteMapBean>   listposte = new ArrayList<CompagnePosteMapBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select distinct code_poste,intitule_poste,sum(st) as map_stat " +
							" from ( select p.code_poste,intitule_poste,1 as st from compagne_poste_travail c ,poste_travail_description p" +
							" where c.code_poste=p.code_poste and c. id_compagne=#id_compagne union  select code_poste,intitule_poste,0 as st from poste_travail_description ) as t1" +
							" group by code_poste,intitule_poste";
			
			sel_comp = sel_comp.replaceAll("#id_compagne", "'"+ compagne_id+"'");

			ResultSet rs = (ResultSet) stmt.executeQuery(sel_comp);
			
			while(rs.next()){
				
				CompagnePosteMapBean compagne=new CompagnePosteMapBean();
				compagne.setMap_stat(rs.getInt("map_stat"));	
				compagne.setCode_poste(rs.getString("code_poste"));
				compagne.setLibelle_poste(rs.getString("intitule_poste"));
						  
				listposte.add(compagne);
				   
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		return listposte;
	
		
		
	}
	

public List appliquerMapPosteCompagne(HashMap checked_poste, int compagne_id ) throws SQLException{
	
	
	ArrayList<CompagnePosteMapBean>   listposte = new ArrayList<CompagnePosteMapBean>();
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDBMulti();
	Statement stmt = null;
	
	Set set = (checked_poste).entrySet(); 
	Iterator i = set.iterator();
	String delete_before_insert="delete from compagne_poste_travail where id_compagne="+"'"+compagne_id+"'";
	String insert="";
	// Display elements
	while(i.hasNext()) {
	Map.Entry me = (Map.Entry)i.next();
	insert=insert+"insert into compagne_poste_travail values ("+"'"+me.getValue()+"',"+ "'"+(String) me.getKey()+"' )"+
	        ";";
	}
	String final_query=delete_before_insert+";"+insert;
	
	try {
		stmt = (Statement) conn.createStatement();
			stmt.execute(final_query);
		
		
		stmt.close();
		conn.close();
		
	} catch (SQLException e) {
		stmt.close();
		conn.close();
		
	}
	return listposte;

	
	
}


}
