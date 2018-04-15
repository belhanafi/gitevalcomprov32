package administration.model;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;

import administration.bean.AdministrationLoginBean;
import administration.bean.CompagneCreationBean;
import administration.bean.DataBaseClientLinkBean;
import administration.bean.DatabaseManagementBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.StructureEntrepriseBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import common.CreateDatabaseCon;
import common.PwdCrypt;

public class DatabaseManagementModel {
	

private ArrayList<DatabaseManagementBean>  listbean=null; 
private ListModel strset =null;
	
	/**
	 * cette méthode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	public List loadDatabaselist() throws SQLException{
		
		
		listbean = new ArrayList<DatabaseManagementBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select database_id,nom_base,login,pwd, adresse_ip,nom_instance from liste_db";
			
			rs = (ResultSet) stmt.executeQuery(sel_comp);
			
			while(rs.next()){
				
				DatabaseManagementBean bean=new DatabaseManagementBean();
				bean.setDatabase_id(rs.getInt("database_id"));
				bean.setNom_base(rs.getString("nom_base"));
				bean.setLogin(rs.getString("login"));
				bean.setPwd(rs.getString("pwd"));
				bean.setAdresse_ip(rs.getString("adresse_ip"));
				bean.setNom_instance(rs.getString("nom_instance"));
				
				
							
					  
				listbean.add(bean);
				   
					
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
		return listbean;
	
		
		
	}
	
	/**
	 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
	 * @param addedData
	 * @return
	 * @throws ParseException 
	 */
	public boolean addDatabase(DatabaseManagementBean addedData) throws ParseException
	{
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		Boolean retour=false;
		
		try 
		{
			                                                
			stmt = (Statement) conn.createStatement();
			String sql_query="INSERT INTO liste_db( nom_base, login, pwd, adresse_ip, nom_instance) values ( #nom_base, #login, #pwd, #adresse_ip, #nom_instance) ";
			
			sql_query = sql_query.replaceAll("#nom_base", "'"+ addedData.getNom_base().toUpperCase()+"'");
			sql_query = sql_query.replaceAll("#login", "'"+ addedData.getLogin()+"'");
			sql_query = sql_query.replaceAll("#pwd", "'"+ addedData.getPwd()+"'");
			sql_query = sql_query.replaceAll("#adresse_ip", "'"+ addedData.getAdresse_ip()+"'");
 		    sql_query = sql_query.replaceAll("#nom_instance", "'"+ addedData.getNom_instance()+"'");
		
			
			 stmt.execute(sql_query);
			 retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La donnée n'a pas été insérée dans la base ", "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				 //TODO Auto-generated catch block
				e1.printStackTrace();
			}

	    } finally {
	        
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
		return retour;
	}
	
	/**
	 * cette classe permet de controler la validité des données insérées (par rapport à leurs taille)
	 * @param addedData
	 * @return
	 * @throws InterruptedException 
	 */
	
	/*public boolean controleIntegrite(CompagneCreationBean addedData) throws InterruptedException
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
			 TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (NumberFormatException nfe)
	    {
			Messagebox.show("Le mot de passe doit être un entier composé de 8 chiffres Exemple 21012001", "Erreur",Messagebox.OK, Messagebox.ERROR);
			return false;
	    }
		
			
		return true;
	}
	
	*/
	
	/**
	 * Cette classe permet de mettre à jour la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	public Boolean updateDatabase(DatabaseManagementBean addedData)	{
		
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		Boolean retour=false;
		try 
		{
		    stmt = (Statement) conn.createStatement();
            String sql_query="Update liste_db set nom_base=#nom_base, login=#login, pwd=#pwd, adresse_ip=#adresse_ip, nom_instance=#nom_instance where database_id=#database_id";
            
            sql_query = sql_query.replaceAll("#database_id", "'"+ addedData.getDatabase_id()+"'");
        	sql_query = sql_query.replaceAll("#nom_base", "'"+ addedData.getNom_base().toUpperCase()+"'");
			sql_query = sql_query.replaceAll("#login", "'"+ addedData.getLogin()+"'");
			sql_query = sql_query.replaceAll("#pwd", "'"+ addedData.getPwd()+"'");
			sql_query = sql_query.replaceAll("#adresse_ip", "'"+ addedData.getAdresse_ip()+"'");
 		    sql_query = sql_query.replaceAll("#nom_instance", "'"+ addedData.getNom_instance()+"'");
			
			
				
			 stmt.executeUpdate(sql_query);
			 retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La modification n'a pas été prise en compte", "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				 //TODO Auto-generated catch block
				e1.printStackTrace();
			}

	    } finally {
	        
	       
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
		return retour;
	}
	
	/**
	 * cette classe permet de supprimer une donnée de la table structure_entreprise
	 * @param codeStructure
	 * @throws SQLException 
	 */
	public Boolean deleteDatabase(DatabaseManagementBean addedData) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		Boolean retour=false;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="DELETE FROM  liste_db   where database_id=#database_id"; 
			  sql_query = sql_query.replaceAll("#database_id", "'"+ addedData.getDatabase_id()+"'");
			
		
			
			 stmt.executeUpdate(sql_query);
			 retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;

	    } finally {
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
		return retour;
	}
	
	public HashMap getListDB() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select  distinct database_id , upper(nom_base) as nom_base from liste_db"; 
			rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("nom_base"), rs.getInt("database_id"));
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
	
public List loadDatabaseClientlist() throws SQLException{
		
		
		List listbean = new ArrayList<DataBaseClientLinkBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select  client_id,nom_client,secteur_id,nom_secteur,upper(l.nom_base) as nom_base" +
					        " from cross_db c, liste_db l where l.database_id=c.database_id";
			
			rs = (ResultSet) stmt.executeQuery(sel_comp);
			
			while(rs.next()){
				
				DataBaseClientLinkBean bean=new DataBaseClientLinkBean();
				bean.setClient_id(rs.getInt("client_id"));
				bean.setNom_client(rs.getString("nom_client"));
				bean.setSecteur_id(rs.getInt("secteur_id"));
				bean.setNom_secteur(rs.getString("nom_secteur"));
				//bean.setDatabase_id(rs.getInt("database_id"));
				bean.setNom_base(rs.getString("nom_base"));
				
				  
				listbean.add(bean);
				   
					
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
		return listbean;

 }

public boolean addLinkDatabaseClient(DataBaseClientLinkBean addedData) throws ParseException
{
	
	
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToPrincipalDB();
	Statement stmt = null;
	Boolean retour=false;
	
	try 
	{
		                                                
		stmt = (Statement) conn.createStatement();
		String sql_query=" INSERT INTO cross_db(nom_client, secteur_id, nom_secteur, database_id)   VALUES(#nom_client,#secteur_id,#nom_secteur,#database_id);";
		
		sql_query = sql_query.replaceAll("#nom_client", "'"+ addedData.getNom_client().toUpperCase()+"'");
		sql_query = sql_query.replaceAll("#secteur_id", "'"+ 0+"'");
		sql_query = sql_query.replaceAll("#nom_secteur", "'"+ addedData.getNom_secteur().toUpperCase()+"'");
		sql_query = sql_query.replaceAll("#database_id", "'"+ addedData.getDatabase_id()+"'");
		
		 stmt.execute(sql_query);
		 retour=true;
	} 
	catch ( SQLException e ) {
		retour=false;
		try 
		{
			Messagebox.show("La donnée n'a pas été insérée dans la base ", "Erreur",Messagebox.OK, Messagebox.ERROR);
		} 
		catch (InterruptedException e1) {
			 //TODO Auto-generated catch block
			e1.printStackTrace();
		}

    } finally {
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
	return retour;
}

public boolean updateLinkDatabaseClient(DataBaseClientLinkBean addedData) throws ParseException
{
	
	
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToPrincipalDB();
	Statement stmt = null;
	Boolean retour=false;
	
	try 
	{
		                                                
		stmt = (Statement) conn.createStatement();
		String sql_query=" Update cross_db set nom_client=#nom_client, secteur_id=#secteur_id,nom_secteur=#nom_secteur,database_id=#database_id  where client_id=#client_id";
		
		sql_query = sql_query.replaceAll("#nom_client", "'"+ addedData.getNom_client().toUpperCase()+"'");
		sql_query = sql_query.replaceAll("#secteur_id", "'"+ 0+"'");
		sql_query = sql_query.replaceAll("#nom_secteur", "'"+ addedData.getNom_secteur().toUpperCase()+"'");
		sql_query = sql_query.replaceAll("#database_id", "'"+ addedData.getDatabase_id()+"'");
		sql_query = sql_query.replaceAll("#client_id", "'"+ addedData.getClient_id()+"'");
		
		 stmt.execute(sql_query);
		 retour=true;
	} 
	catch ( SQLException e ) {
		retour=false;
		try 
		{
			Messagebox.show("La donnée n'a pas été modifiée", "Erreur",Messagebox.OK, Messagebox.ERROR);
		} 
		catch (InterruptedException e1) {
			 //TODO Auto-generated catch block
			e1.printStackTrace();
		}
    } finally {
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
	return retour;
}

public boolean deleteLinkDatabaseClient(DataBaseClientLinkBean addedData) throws ParseException
{
	
	
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToPrincipalDB();
	Statement stmt = null;
	Boolean retour=false;
	
	try 
	{
		                                                
		stmt = (Statement) conn.createStatement();
		String sql_query=" delete from cross_db where client_id=#client_id";
		
		sql_query = sql_query.replaceAll("#client_id", "'"+ addedData.getClient_id()+"'");
		
		 stmt.execute(sql_query);
		 retour=true;
	} 

	catch ( SQLException e ) {
		retour=false;
		try 
		{
			Messagebox.show("La donnée n'a pas été supprimée ", "Erreur",Messagebox.OK, Messagebox.ERROR);
		} 
		catch (InterruptedException e1) {
			 //TODO Auto-generated catch block
			e1.printStackTrace();
		}

    } finally {
        
       
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
	return retour;
}
	


}
