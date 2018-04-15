package common;
import java.sql.*;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



import org.zkoss.zk.ui.Sessions;


//import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;




import administration.bean.CompteEntrepriseDatabaseBean;

public class CreateDatabaseCon
{

    Connection connection;

    public CreateDatabaseCon()
    {
        connection = null;
    }

    //utilisation de pool de connexion à commo_evalcom
    public Connection connectToPrincipalDB()
    {
        try
        {
            /*InitContext intctx = new InitContext();
            intctx.loadProperties();
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection(intctx.getJdbcurl(), intctx.getJdbcusername(), intctx.getJdbcpassword());
            connection.setAutoCommit(true);*/
        	 Context ctx = null;
             Connection con = null;
             Statement stmt = null;
             ResultSet rs = null;
             
        	ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/evalcomDB");
            connection =  ds.getConnection();
            //stmt = (Statement) con.createStatement();
        }
        catch(NamingException e){
        	System.out.println("NamingException  cannot create connection");
        	if(connection!=null)
        	{
        		try
                {
                    connection.close();
                    System.out.println("fermeture de la connexion à la BDD principal suite à une exception lors de son ouverture");
                }
                catch(Exception e1)
                {
                    throw new RuntimeException(e);
                }
        	}
        } catch (SQLException e) {
            e.printStackTrace();
        	System.out.println("SQLException  cannot create connection to common_evalcom");
        	if(connection!=null)
        	{
        		try
                {
                    connection.close();
                    System.out.println("fermeture de la connexion à la BDD principal suite à une exception lors de son ouverture");
                }
                catch(Exception e1)
                {
                    throw new RuntimeException(e);
                }
        	}
        }
        return connection;
    }

    //recherche la BDD selectionnée dans la base pour s'y connecter
    //TODO utilisation de pool de connexion
    public Connection connectToSecondairesDB()
    {
        try
        {
            
        	/*InitContext intctx = new InitContext();
            CompteEntrepriseDatabaseBean compteEntrepriseDataBaseBean=ApplicationFacade.getInstance().getCompteEntrepriseDatabasebean();
            intctx.setJdbcpassword(compteEntrepriseDataBaseBean.getJdbcpassword());
            intctx.setJdbcusername(compteEntrepriseDataBaseBean.getJdbcusername());
            intctx.setJdbcurl(compteEntrepriseDataBaseBean.getJdbcurl());*/
            Class.forName("com.mysql.jdbc.Driver");
            ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
            CompteEntrepriseDatabaseBean compteEntrepriseDataBaseBean=applicationSession.getCompteEntrepriseDatabasebean();
            //TODO supprimer cette ligne ApplicationFacade
            //CompteEntrepriseDatabaseBean compteEntrepriseDataBaseBean=ApplicationFacade.getInstance().getCompteEntrepriseDatabasebean();
            compteEntrepriseDataBaseBean.setDBParams();
          Properties properties = new Properties(); // Create Properties object
         properties.put("user", compteEntrepriseDataBaseBean.getJdbcusername());         // Set user ID for connection
         properties.put("password", compteEntrepriseDataBaseBean.getJdbcpassword());     // Set password for connection
         properties.put("allowMultiQueries", "true");
         connection = (Connection) DriverManager.getConnection(compteEntrepriseDataBaseBean.getJdbcurl(), properties );
            //modifier la ligne ci dessous pour faire un pool de connexion
            //connection = (Connection) DriverManager.getConnection(compteEntrepriseDataBaseBean.getJdbcurl(), compteEntrepriseDataBaseBean.getJdbcusername(), compteEntrepriseDataBaseBean.getJdbcpassword());
            connection.setAutoCommit(true);
        }
        catch(ClassNotFoundException  e)
        {
        	e.printStackTrace();
        	//TODO ajouter logger d'erreur fatal
            if(connection != null)
            {
                try
                {
                    connection.close();
                }
                catch(Exception e1)
                {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
			// TODO ajouter logger d'erreur
        	//gestion des types de sql exception
			e.printStackTrace();
			
        	//TODO ajouter logger d'erreur fatal
            if(connection != null)
            {
                try
                {
                    connection.close();
                }
                catch(Exception e1)
                {
                    throw new RuntimeException(e);
                }
            }
		}
        catch (Exception r5)
        {
        	//gestion des types autres types d'exception
			r5.printStackTrace();
			
        	//TODO ajouter logger d'erreur fatal
            if(connection != null)
            {
                try
                {
                    connection.close();
                }
                catch(Exception e1)
                {
                    throw new RuntimeException(e1);
                }
            }
        }
        finally {
//		    if ( connection != null )
//		        try {
//		            /* Fermeture de la connexion */
//		        	connection.close();
//		        } catch ( SQLException ignore ) {
//		           //exception a igorer car elle surviens sila connection a déjà été fermée
//		        }
		}
        return connection;
    }

    public static void closeCon(Connection connection)
    {
        try
        {
            if(connection != null)
            {
                connection.close();
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e1)
        {
        	e1.printStackTrace();
        }
    }
    
    //TODO remplacer par connectToEntrepriseDB
//    public Connection connectToEntrepriseDBMulti()
//    {
//        try
//        {
//        	
//           	Class.forName("com.mysql.jdbc.Driver");
//            ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
//            CompteEntrepriseDatabaseBean compteEntrepriseDataBaseBean=applicationSession.getCompteEntrepriseDatabasebean();
//            //TODO cette ligne est a supprimer ApplicationFacade
//            //CompteEntrepriseDatabaseBean compteEntrepriseDataBaseBean=ApplicationFacade.getInstance().getCompteEntrepriseDatabasebean();
//            compteEntrepriseDataBaseBean.setDBParams();
//             Properties properties = new Properties(); // Create Properties object
//            properties.put("user", compteEntrepriseDataBaseBean.getJdbcusername());         // Set user ID for connection
//            properties.put("password", compteEntrepriseDataBaseBean.getJdbcpassword());     // Set password for connection
//            properties.put("allowMultiQueries", "true");
//            connection = (Connection) DriverManager.getConnection(compteEntrepriseDataBaseBean.getJdbcurl(), properties );
//            
//            connection.setAutoCommit(true);
//        }
//        catch(ClassNotFoundException e)
//        {
//        	e.printStackTrace();
//            if(connection != null)
//            {
//                try
//                {
//                    connection.close();
//                }
//                catch(Exception e1)
//                {
//                    throw new RuntimeException(e);
//                }
//            }
//        }catch (SQLException e) {
//			// TODO ajouter logger d'erreur
//        	//gestion des types de sql exception
//			e.printStackTrace();
//		}finally {
////		    if ( connection != null )
////		        try {
////		            /* Fermeture de la connexion */
////		        	connection.close();
////		        } catch ( SQLException ignore ) {
////		           //exception a igorer car elle surviens sila connection a déjà été fermée
////		        }
//		}
//        return connection;
//    }
    
    //TODO remplacer les appels de cette methode par connectToDB
//    public Connection connectToDBMulti()
//    {
//        try
//        {
//            InitContext intctx = new InitContext();
//            intctx.loadProperties();
//            Class.forName("com.mysql.jdbc.Driver");
//            
//            Properties properties = new Properties(); // Create Properties object
//            properties.put("user", intctx.getJdbcusername());         // Set user ID for connection
//            properties.put("password",intctx.getJdbcpassword());     // Set password for connection
//            properties.put("allowMultiQueries", "true");
//            connection = (Connection) DriverManager.getConnection(intctx.getJdbcurl(), properties );
//            
//            connection.setAutoCommit(true);
//            
//          
//        }
//        catch(ClassNotFoundException e)
//        {
//            if(connection != null)
//            {
//                try
//                {
//                    connection.close();
//                }
//                catch(Exception e1)
//                {
//                    throw new RuntimeException(e);
//                   
//                }
//            }
//        }catch (SQLException e) {
//			// TODO ajouter logger d'erreur
//        	//gestion des types de sql exception
//			e.printStackTrace();
//		}finally {
////		    if ( connection != null )
////		        try {
////		            /* Fermeture de la connexion */
////		        	connection.close();
////		        } catch ( SQLException ignore ) {
////		           //exception a igorer car elle surviens sila connection a déjà été fermée
////		        }
//		}
//        return connection;
//    }

  
    
  /* public static void main(String[] args){
    	
    	CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt=null;
		
		int type_result=0;
		try {
			stmt = (Statement) conn.createStatement();
			String sel_db="select code_formation,libelle_formation,libelle_diplome from formation";
			//System.out.println(select_login);
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_db);
			
			
			while(rs.next()){
			System.out.println(rs.getString("libelle_formation"));
			}
			
			stmt.close();
			conn.close();
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//stmt.close();
			//conn.close();
		}
		
			
	
    }*/
    
    
}
