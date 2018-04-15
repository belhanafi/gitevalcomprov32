package administration.bean;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.zkoss.zk.ui.Sessions;

import common.ApplicationFacade;
import common.ApplicationSession;
import common.CreateDatabaseCon;



public class CompteEntrepriseDatabaseBean implements Serializable {



	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jdbcurl;
	   private String jdbcusername;
	   private String jdbcpassword;
/**
 * TODO enlever ce constructeur
 * @param jdbcurl
 * @param jdbcusername
 * @param jdbcpassword
 */
	   /**
	 * @param jdbcurl
	 * @param jdbcusername
	 * @param jdbcpassword
	 */
	public  CompteEntrepriseDatabaseBean(String jdbcurl, String jdbcusername, String jdbcpassword)
	   {
		   /*this.jdbcurl="jdbc:mysql://localhost:3306/evalcom";
		   this.jdbcusername="root";
		   this.jdbcpassword="admin";*/
		
		   this.jdbcurl=jdbcurl;
		   this.jdbcusername=jdbcusername;
		   this.jdbcpassword=jdbcpassword;
		
	   }
	
	public  CompteEntrepriseDatabaseBean(){
		
	}
	   
	public String getJdbcurl() {
		return jdbcurl;
	}
	public void setJdbcurl(String jdbcurl) {
		this.jdbcurl = jdbcurl;
	}
	public String getJdbcusername() {
		return jdbcusername;
	}
	public void setJdbcusername(String jdbcusername) {
		this.jdbcusername = jdbcusername;
	}
	public String getJdbcpassword() {
		return jdbcpassword;
	}
	public void setJdbcpassword(String jdbcpassword) {
		this.jdbcpassword = jdbcpassword;
	}
	   
	public List getClientDatabaseConnection() throws SQLException{
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		ResultSet rs = null;
		List list_db_params= new ArrayList();
			
		try {
				stmt = (Statement) conn.createStatement();
				String sel_db="select login,pwd,adresse_ip,nom_instance from liste_db where database_id=#databaseid";
				//TODO cette ligne est a supprimer 
				//sel_db = sel_db.replaceAll("#databaseid", Integer.toString(ApplicationFacade.getInstance().getClient_database_id()));
				ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
				int idBase=applicationSession.getClient_database_id();
				sel_db = sel_db.replaceAll("#databaseid", Integer.toString(idBase));
				//sel_db = sel_db.replaceAll("#databaseid", Integer.toString(1));
				//System.out.println(select_login);
				/*ResultSet*/ rs = (ResultSet) stmt.executeQuery(sel_db);
							
				
				while(rs.next()){
					ClientDBParams db = new ClientDBParams(rs.getString("login"),rs.getString("pwd"),rs.getString("adresse_ip"),rs.getString("nom_instance"));
					list_db_params.add(db);
				}
			
//			stmt.close();
//			conn.close();
		}		
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			stmt.close();
//			conn.close();
//		}
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
		
			
		return list_db_params;	
	
		
		
	}
	
	public void setDBParams() throws SQLException{
		Iterator it;
		ClientDBParams cdb;
		it = getClientDatabaseConnection().iterator();
			while (it.hasNext()){
		 		cdb  = (ClientDBParams) it.next();
		 		setJdbcpassword(cdb.getPwd());
		 		setJdbcurl("jdbc:mysql://"+cdb.getAdresse_ip()+"/"+cdb.getNom_instance());
		 		setJdbcusername(cdb.getLogin());
		 		
			
			}
		
		
	}
}


