package administration.model;

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
import java.util.Set;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;

import administration.bean.AdministrationLoginBean;
import administration.bean.CompagneCreationBean;
import administration.bean.CotationIMIvsStrategieBean;
import administration.bean.DataBaseClientLinkBean;
import administration.bean.DatabaseManagementBean;
import administration.bean.DroitsAccessBean;
import administration.bean.IMIvsStrategieBean;
import administration.bean.ProfileDroitsAccessBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.StructureEntrepriseBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import common.CreateDatabaseCon;
import common.PwdCrypt;

public class ProfileDroitsAccessModel {
	

private ArrayList<ProfileDroitsAccessBean>  listbean=null; 


private ListModel strset =null;
	
	/**
	 * cette méthode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	public List loadProfile() throws SQLException{
		
		
		listbean = new ArrayList<ProfileDroitsAccessBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select  id_profile ,libelle_profile  from profile";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_comp);
			
			while(rs.next()){
				
				ProfileDroitsAccessBean bean=new ProfileDroitsAccessBean();
				bean.setId_profile(rs.getInt("id_profile"));
				bean.setLibelle_profile(rs.getString("libelle_profile"));
		 
				listbean.add(bean);
				   
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		
	
		return listbean;
	
		
		
	}
	
	/**
	 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
	 * @param addedData
	 * @return
	 * @throws ParseException 
	 */
	public boolean addProfile(ProfileDroitsAccessBean addedData) throws ParseException
	{
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDBMulti();
		Statement stmt = null;
	
		
		try 
		{
			                                                
			stmt = (Statement) conn.createStatement();
			String sql_query=" INSERT INTO profile( libelle_profile)  VALUES(#libelle_profile) ";
			
			sql_query = sql_query.replaceAll("#libelle_profile", "'"+ addedData.getLibelle_profile().toUpperCase()+"'");
		
			String sql_query_droit="insert into droits  (id_profile, code_ecran, hide, ecriture, lecture)" +
					               " select (select max(id_profile) from profile),code_ecran,0,0,0 from liste_ecran";

			
			
				 stmt.execute(sql_query+";"+sql_query_droit);
			
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("La donnée n'a pas été insérée dans la base ", "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				 //TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 //TODO Auto-generated catch block
			try {
				stmt.close();conn.close();
			} catch (SQLException e1) {
				 //TODO Auto-generated catch block
				
				e1.printStackTrace();
				return false;
			}
			
			
			return false;
		}
		try {
			stmt.close();conn.close();
		} catch (SQLException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
		
	/**
	 * Cette classe permet de mettre à jour la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	public Boolean updateProfile(ProfileDroitsAccessBean addedData)	{
		
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		
		try 
		{
		    stmt = (Statement) conn.createStatement();
			String sql_query=" update profile set  libelle_profile=#libelle_profile where id_profile=#id_profile  ";
			
			sql_query = sql_query.replaceAll("#libelle_profile", "'"+ addedData.getLibelle_profile().toUpperCase()+"'");
			sql_query = sql_query.replaceAll("#id_profile", "'"+ addedData.getId_profile()+"'");
			
				
			 stmt.executeUpdate(sql_query);
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("La modification n'a pas été prise en compte", "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				 //TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 //TODO Auto-generated catch block
			try {
				stmt.close();conn.close();
			} catch (SQLException e1) {
				 //TODO Auto-generated catch block
				
				e1.printStackTrace();
				return false;
			}
			
			
			return false;
		}
		try {
			stmt.close();conn.close();
		} catch (SQLException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * cette classe permet de supprimer une donnée de la table structure_entreprise
	 * @param codeStructure
	 * @throws SQLException 
	 */
	public Boolean deleteProfile(ProfileDroitsAccessBean addedData) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDBMulti();
		Statement stmt = null;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
           String sql_query=" delete from  droits  where id_profile=#id_profile ; "+" delete from  profile  where id_profile=#id_profile  ";
	    	sql_query = sql_query.replaceAll("#id_profile", "'"+ addedData.getId_profile()+"'");
			
			
		
			
			 stmt.executeUpdate(sql_query);
		} 
		catch (SQLException e) 
		{
			
				e.printStackTrace();
				stmt.close();conn.close();
				return false;


		}
		try {
			stmt.close();conn.close();
		} catch (SQLException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	



public HashMap list_valeursCotation() {
	
	HashMap map = new HashMap();
	for (int i=1 ;i<6;i++){
		map.put(i, i);
	}
	
	return (HashMap) sortByComparator(map);
}

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


public List loadDroitsAccess(Integer id_profile) throws SQLException{
	
	
	List listbean1 = new ArrayList<DroitsAccessBean>();
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToDB();
	Statement stmt = null;
	
	try {
		stmt = (Statement) conn.createStatement();
		String sql_query=" select code_ecran,libelle_menu,libelle_ecran,sum(hide) as hide , sum(ecriture) as ecriture, sum(lecture) as lecture" +
				         "  from ( select  d.code_ecran,l.libelle_menu,l.libelle_ecran,d.hide,d.ecriture,d.lecture from droits d  , liste_ecran  l  where  d.code_ecran=l.code_ecran and  id_profile=#id_profile" +
				         " union select  l.code_ecran,l.libelle_menu,l.libelle_ecran ,0 as hide ,0 as ecriture,0 as lecture from liste_ecran  l  )  t2 group by libelle_menu,libelle_ecran";
		
		
		sql_query = sql_query.replaceAll("#id_profile", "'"+ id_profile+"'");
		
		ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
		
		while(rs.next()){
			
			DroitsAccessBean bean=new DroitsAccessBean();
			 bean.setCode_ecran(rs.getString("code_ecran"));
			 bean.setLibelle_menu(rs.getString("libelle_menu"));
			 bean.setLibelle_ecran(rs.getString("libelle_ecran"));
			 bean.setHide(rs.getString("hide"));
			 bean.setEcriture(rs.getString("ecriture"));
			 bean.setLecture(rs.getString("lecture"));
			 
			 
			
	 
			listbean1.add(bean);
			   
				
			}
		stmt.close();
		conn.close();
		
	} catch (SQLException e) {
		stmt.close();
		conn.close();
		
	}

	return listbean1;

	
	
}



public HashMap selectProfiles() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToDB();
	Statement stmt = null;
	HashMap map = new HashMap();
		
	try 
	{
		stmt = (Statement) conn.createStatement();
		String sql_query="select id_profile,libelle_profile from profile "; 
		
		ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
		
		
		while(rs.next()){
			map.put(rs.getString("libelle_profile"), rs.getInt("id_profile"));
			//list_profile.add(rs.getString("libelle_profile"));
        }
		stmt.close();conn.close();
	} 
	catch (SQLException e){
			e.printStackTrace();
			stmt.close();conn.close();
	}
	
	return (HashMap) sortByComparator(map);
}


public HashMap  listScreenAccessRight(List <DroitsAccessBean> listbean) throws SQLException
{
	Iterator<DroitsAccessBean> index=listbean.iterator();
	HashMap map = new HashMap();
	while(index.hasNext())
	{
		DroitsAccessBean donnee=index.next();
		map.put(donnee.getCode_ecran(), donnee.getHide()+"|"+donnee.getEcriture()+"|"+
				donnee.getLecture());
	}	
	
	return (HashMap) sortByComparator(map);
}

public void execQueriesProfile(HashMap map) throws SQLException{
	
	
	
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToDBMulti();
	Statement stmt = null;
	
	String sql_query_delete="";
	String sql_query_insert="";
	String sql_query_all="";
	
	
	
	Set set = (map).entrySet(); 
	Iterator i = set.iterator();
	
	// Display elements
	while(i.hasNext()) {
	Map.Entry me = (Map.Entry)i.next();
	
		 sql_query_insert=sql_query_insert+(String)me.getValue()+";";
	}
	
	try {
		stmt = (Statement) conn.createStatement();
   	   stmt.execute(sql_query_insert);	
	
		stmt.close();
		conn.close();
		
	} catch (SQLException e) {
		stmt.close();
		conn.close();
		
	}
	


	
	
}


}
