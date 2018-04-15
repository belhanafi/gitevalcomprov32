package administration.model;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import administration.bean.CompteBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.zkoss.zk.ui.Sessions;

import common.ApplicationFacade;
import common.ApplicationSession;
import common.CreateDatabaseCon;
import common.PwdCrypt;
import common.bean.ArborescenceMenu;
import common.bean.EcranBean;

public class LoginModel {
	
	private List  user_compte;
	private List  list_secreens;
	private static int profile_id;
	private static int database_id;
	
	
	public void LoginModel(){
		
	}
	
	public int checkLoginPwd(String login,String password) throws SQLException, ParseException{
		PwdCrypt pwdcrypt=new PwdCrypt();
		user_compte = new ArrayList();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=  dbcon.connectToPrincipalDB();
		Statement stmt = null;
		int type_result=1;
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();
			String select_login="SELECT id_compte,id_profile,login,pwd,c.database_id,val_date_deb,val_date_fin,nom_client FROM compte c, cross_db  b where c.database_id=b.database_id"
					+ " and  upper(login)=#login ";
			select_login = select_login.replaceAll("#login", "'"+login.toUpperCase()+"'");
			//select_login = select_login.replaceAll("#password", "'"+pwdcrypt.crypter(password.toUpperCase())+"'");
			//System.out.println(select_login);
			rs = (ResultSet) stmt.executeQuery(select_login);
			
			
			while(rs.next()){
				if (rs.getRow()==1  ) {
					
					if(pwdcrypt.decrypter(rs.getString("pwd")).trim().equalsIgnoreCase(password.trim())){
					
						if (checkLoginValidity(rs.getDate("val_date_deb"),rs.getDate("val_date_fin"))==true){
						
							CompteBean db = new CompteBean(rs.getInt("id_compte"),rs.getInt("id_profile"),
			                         rs.getString("login"),pwdcrypt.decrypter(rs.getString("pwd")),rs.getInt("database_id"),
			                         rs.getDate("val_date_deb"),rs.getDate("val_date_fin"),rs.getString("nom_client"));
	 
									user_compte.add(db);
									type_result=0;
									database_id=rs.getInt("database_id");
						}
						
						else{
							type_result=2;
						}
					}
					else{
						type_result=1;
					}
				}
				
				else {
					type_result=1;
				}
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
//	        if(dbcon!=null){
//	        	try
//	        	{
//	        		((java.sql.Connection) dbcon).close();
//	        	}catch(SQLException igonre){}
//	        }
	        if ( conn != null ) {
	            try {
	            	conn.close();
	            } catch ( SQLException ignore ) {
	            }
	        }
	    }
			
		return type_result;	
	
		
		
	}
	
public static void checkProfile(List compte_user) throws SQLException  {
	
	Iterator it = compte_user.iterator();
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToPrincipalDB();
	Statement stmt=null;
	ResultSet rs=null;
 	CompteBean cpb=new CompteBean();
 	Integer profile=0;
 	while (it.hasNext()){
 		cpb  = (CompteBean) it.next();
		profile=cpb.getId_profile();
 	}
 	try
 	{
 	//enregistrement des informations relatifs � la personne connect� au niveau de la fa�ade
 	ApplicationSession applicationSession =(ApplicationSession) Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
 	if (applicationSession==null)
 		applicationSession=new ApplicationSession();
 	applicationSession.setCompteUtilisateur(cpb);
 	
 	//TODO supprimer cette lighne ApplicatinFACADE
 	//ApplicationFacade.getInstance().setCompteUtilisateur(cpb);
 	profile_id=profile;	
	stmt = (Statement) conn.createStatement();
	String select_profile=" select id_ecran,code_ecran,libelle_ecran,code_menu,libelle_menu"+ 
	                      " from liste_ecran where code_ecran in ("+
			              "select   code_ecran from droits where id_profile=#profile"+
	                      " and hide=0 ) ";
	

	select_profile = select_profile.replaceAll("#profile",profile.toString() );
	//System.out.println(select_profile);
	rs = (ResultSet) stmt.executeQuery(select_profile);

	List <String>listLibelleMenu=new ArrayList<String>();

	List <EcranBean>listeEcranBean=new ArrayList <EcranBean>();

	
	while(rs.next()){
		//System.out.println("dans la boucle");		                                                       
		//add result set to desired structure
        Integer val=new Integer(rs.getString ("id_ecran"));
		EcranBean ecranbean=new EcranBean(val, rs.getString("code_ecran"),rs.getString("libelle_ecran"), rs.getString("code_menu"), rs.getString("libelle_menu"));
		
		listeEcranBean.add(ecranbean);
		if(!listLibelleMenu.contains(rs.getString("libelle_menu")))
		{
			listLibelleMenu.add(rs.getString("libelle_menu"));
		}
		/*
				          
	   list_secreens.add(db)
				          
        */
		
				
	}
	ArborescenceMenu arborescenceMenu=new ArborescenceMenu(listeEcranBean,listLibelleMenu);
	applicationSession.setArborescenceMenubean(arborescenceMenu);
	 Sessions.getCurrent().setAttribute("APPLICATION_ATTRIBUTE",applicationSession);
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
	//TODO supprimer cette lighne ApplicatinFACADE
	//ApplicationFacade.getInstance().setArborescenceMenubean(arborescenceMenu);
	
}

public boolean checkLoginValidity(Date date_deb,Date date_fin) throws ParseException{
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
		
	//  begin validity date
	Date datedeb = new SimpleDateFormat("yyyy-MM-dd").parse(date_deb.toString());
     Calendar cal_deb=Calendar.getInstance();
     cal_deb.setTime(datedeb);
  
     
 //  end validity date
     Date datefin = new SimpleDateFormat("yyyy-MM-dd").parse(date_fin.toString());
     Calendar cal_fin=Calendar.getInstance();
     cal_fin.setTime(datefin);
     cal_fin.add(Calendar.DATE, 1);
            
     String datecuurent = sdf.format(new Date());
     Date date_current = sdf.parse(datecuurent);

        
     Calendar currentcal=Calendar.getInstance();
     currentcal.setTime(date_current);
     
    if(currentcal.before(cal_fin) && currentcal.after(cal_deb)||currentcal.equals(cal_deb) ){
    	
    	return true;
    }
      
    
    else  return false;
	
	
	
}
	
	   /* public static void main(String arg[]){
	    LoginModel init =new LoginModel();
	    List list=new ArrayList();
	    try {
			boolean result=init.checkLoginPwd("nbehlouli","123");
			checkProfile(init.getUser_compte());
			if (result==true) {System.out.println("GO") ;} else{ System.out.println("KO");}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
     } */
	    public List getUser_compte() {
			return user_compte;
		}

		public void setUser_compte(List user_compte) {
			this.user_compte = user_compte;
		}

		public int getProfile_id() {
			return profile_id;
		}

		public void setProfile_id(int profile_id) {
			this.profile_id = profile_id;
		}

		public static int getDatabase_id() {
			return database_id;
		}

		public static void setDatabase_id(int database_id) {
			LoginModel.database_id = database_id;
		}

		

}
