package administration.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import administration.bean.CompteBean;
import administration.bean.SelCliBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.SelCliDbBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import common.CreateDatabaseCon;

public class SelCliModel {
	private List  sect_activite;
	private List  list_db_client;
	private List  list_db;
	
	public void SelCliModel(){
		
	}
	
	public List loadSectActivCombo() throws SQLException{
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		sect_activite= new ArrayList();
		ResultSet rs=null;
		int type_result=0;
		try {
			stmt = (Statement) conn.createStatement();
			String list_sect_activ="select distinct secteur_id,nom_secteur from cross_db";
			
			//System.out.println(select_login);
			rs = (ResultSet) stmt.executeQuery(list_sect_activ);
			
			
			while(rs.next()){
		
 				 SelCliBean db = new SelCliBean(rs.getInt("secteur_id"),rs.getString("nom_secteur"));
 				 sect_activite.add(db);
			}
				
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
	        
//	        if ( dbcon != null ) {
//	            try {
//	            	((java.sql.Connection)dbcon).close();
//	            } catch ( SQLException ignore ) {
//	            }
//	        }
	        if ( conn != null ) {
	            try {
	            	conn.close();
	            } catch ( SQLException ignore ) {
	            }
	        }
	    }	
		return sect_activite;	
	
		
		
	}
	
public List loadClientCombo(int sect_activite_id) throws SQLException{
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		list_db_client= new ArrayList();
		ResultSet rs=null;
		int type_result=0;
		try {
			stmt = (Statement) conn.createStatement();
			String list_client_db="select client_id, nom_client from cross_db where secteur_id=#sect_id";
			//String list_client_db="select client_id, nom_client from cross_db where secteur_id=#sect_id and database_id in (7,8)";

			
			list_client_db = list_client_db.replaceAll("#sect_id", Integer.toString(sect_activite_id));
			//System.out.println(select_login);
			rs = (ResultSet) stmt.executeQuery(list_client_db);
			
			
			while(rs.next()){
		
				SelCliDbBean db = new SelCliDbBean(rs.getInt("client_id"),rs.getString("nom_client"));
				list_db_client.add(db);
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
	        
//	        if ( dbcon != null ) {
//	            try {
//	            	((java.sql.Connection)dbcon).close();
//	            } catch ( SQLException ignore ) {
//	            }
//	        }
	        if ( conn != null ) {
	            try {
	            	conn.close();
	            } catch ( SQLException ignore ) {
	            }
	        }
	    }
		
			
		return list_db_client;	
	
		
		
	}
	
public List loadDBLabel(int client_id) throws SQLException{
	
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToPrincipalDB();
	Statement stmt=null;
	list_db= new ArrayList();
	ResultSet rs=null;
	int type_result=0;
	try {
		stmt = (Statement) conn.createStatement();
		String list_db_str="select t.database_id,t.nom_base from cross_db e,liste_db t " +
				" where client_id=#client_id and e.database_id=t.database_id";

		list_db_str = list_db_str.replaceAll("#client_id", Integer.toString(client_id));
		//System.out.println(select_login);
		rs = (ResultSet) stmt.executeQuery(list_db_str);
		
		
		while(rs.next()){
	
			SelCliDBNameBean db = new SelCliDBNameBean(rs.getInt("database_id"),rs.getString("nom_base"));
			list_db.add(db);
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
        
//        if ( dbcon != null ) {
//            try {
//            	((java.sql.Connection)dbcon).close();
//            } catch ( SQLException ignore ) {
//            }
//        }
        if ( conn != null ) {
            try {
            	conn.close();
            } catch ( SQLException ignore ) {
            }
        }
    }
	
		
	return list_db;	

	
	
}
	/*public static void main(String args[]){
		SelCliModel init=new SelCliModel();
		SelCliDBNameBean cpb;
		String secteur;
		Iterator it;
		try {
			it = init.loadDBLabel(1).iterator();
			while (it.hasNext()){
		 		cpb  = (SelCliDBNameBean) it.next();
				secteur =cpb.getNombase();
				System.out.println(secteur);
				
				
		 	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
	}*/

}
