package common;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import administration.bean.ClientDBParams;
import administration.bean.CompteEntrepriseDatabaseBean;

public class CreateDatabaseCon
{

    Connection connection;

    public CreateDatabaseCon()
    {
        connection = null;
    }

    public Connection connectToDB()
    {
        try
        {
            InitContext intctx = new InitContext();
            intctx.loadProperties();
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection(intctx.getJdbcurl(), intctx.getJdbcusername(), intctx.getJdbcpassword());
            connection.setAutoCommit(true);
        }
        catch(Exception e)
        {
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
        return connection;
    }

    public Connection connectToEntrepriseDB()
    {
        try
        {
            
        	/*InitContext intctx = new InitContext();
            CompteEntrepriseDatabaseBean compteEntrepriseDataBaseBean=ApplicationFacade.getInstance().getCompteEntrepriseDatabasebean();
            intctx.setJdbcpassword(compteEntrepriseDataBaseBean.getJdbcpassword());
            intctx.setJdbcusername(compteEntrepriseDataBaseBean.getJdbcusername());
            intctx.setJdbcurl(compteEntrepriseDataBaseBean.getJdbcurl());*/
            Class.forName("com.mysql.jdbc.Driver");
            CompteEntrepriseDatabaseBean compteEntrepriseDataBaseBean=ApplicationFacade.getInstance().getCompteEntrepriseDatabasebean();
            compteEntrepriseDataBaseBean.setDBParams();
            connection = (Connection) DriverManager.getConnection(compteEntrepriseDataBaseBean.getJdbcurl(), compteEntrepriseDataBaseBean.getJdbcusername(), compteEntrepriseDataBaseBean.getJdbcpassword());
            connection.setAutoCommit(true);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
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
    }
    
    
    public Connection connectToEntrepriseDBMulti()
    {
        try
        {
        	
           	Class.forName("com.mysql.jdbc.Driver");
            CompteEntrepriseDatabaseBean compteEntrepriseDataBaseBean=ApplicationFacade.getInstance().getCompteEntrepriseDatabasebean();
            compteEntrepriseDataBaseBean.setDBParams();
             Properties properties = new Properties(); // Create Properties object
            properties.put("user", compteEntrepriseDataBaseBean.getJdbcusername());         // Set user ID for connection
            properties.put("password", compteEntrepriseDataBaseBean.getJdbcpassword());     // Set password for connection
            properties.put("allowMultiQueries", "true");
            connection = (Connection) DriverManager.getConnection(compteEntrepriseDataBaseBean.getJdbcurl(), properties );
            
            connection.setAutoCommit(true);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
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
        return connection;
    }
    
    public Connection connectToDBMulti()
    {
        try
        {
            InitContext intctx = new InitContext();
            intctx.loadProperties();
            Class.forName("com.mysql.jdbc.Driver");
            
            Properties properties = new Properties(); // Create Properties object
            properties.put("user", intctx.getJdbcusername());         // Set user ID for connection
            properties.put("password",intctx.getJdbcpassword());     // Set password for connection
            properties.put("allowMultiQueries", "true");
            connection = (Connection) DriverManager.getConnection(intctx.getJdbcurl(), properties );
            
            connection.setAutoCommit(true);
            
          
        }
        catch(Exception e)
        {
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
        return connection;
    }

  
    
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
