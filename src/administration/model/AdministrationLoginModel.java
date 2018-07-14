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
import java.util.Scanner;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;

import administration.bean.AdministrationLoginBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.StructureEntrepriseBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import common.CreateDatabaseCon;
import common.PwdCrypt;

public class AdministrationLoginModel {
	

private ArrayList<AdministrationLoginBean>  listlogin =null; 
private ListModel strset =null;
	
	/**
	 * Cette méthode permet de dire si le login passé en paramètre existe dans les bases de données gérées par evalcom
	 * @param login
	 * @return
	 */
	public boolean isLoginExists(String login ,AdministrationLoginBean selected, String action)throws SQLException
	{
	
		boolean existe=true;
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		ResultSet rs=null;
		
		try {
				stmt = (Statement) conn.createStatement();
				String sel_compte="select id_compte "+ 
	                               "from compte c where login=#login ";
				sel_compte = sel_compte.replaceAll("#login", "'"+login+"'");                
				
				/*ResultSet*/ rs = (ResultSet) stmt.executeQuery(sel_compte);
				while (rs.next())
				{
				   //je peux faire l'upadte. il s'agit le login correspond au  id compte
					int id_compte=rs.getInt("id_compte");
					
				   if (action.equalsIgnoreCase("M")){
					   if (id_compte==Integer.valueOf(selected.getId_compte())){
						   existe=true;
					   }else{
						   
						   existe=false;
					   }
					   
				   }else{
					   
					 //je ne peux pas faire l'insert. il y a deja un login identique dans la base++
					   if (id_compte>0){
						   existe=false;
					   }else{
						   
						   existe=true;
					   }
					   
				   }
				  
				   
				}
				
//					stmt.close();
//					conn.close();
		
		} 
//		catch (SQLException e) {
//			stmt.close();
//			conn.close();
//			
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
		return existe;

	}
	/**
	 * cette méthode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<AdministrationLoginBean> checkLoginBean() throws SQLException{
		
		PwdCrypt pwdcrypt=new PwdCrypt();
		listlogin = new ArrayList<AdministrationLoginBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		ResultSet rs=null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_compte="select id_compte,nom,prenom,c.login,c.pwd,libelle_profile,l.nom_base,DATE_FORMAT(val_date_deb,'%Y/%m/%d') as val_date_deb,DATE_FORMAT(val_date_fin,'%Y/%m/%d') as val_date_fin ,modifiedpwd "+ 
                               "from compte c ,liste_db l ,profile p where c.database_id=l.database_id "+
                               "and c.id_profile=p.id_profile order by l.nom_base,nom";
			
			/*ResultSet*/ rs = (ResultSet) stmt.executeQuery(sel_compte);
			
			SimpleDateFormat formatDateJour = new SimpleDateFormat("yyyy/MM/dd");
			 
			
			while(rs.next()){
				
					AdministrationLoginBean admin_compte=new AdministrationLoginBean();
					
					admin_compte.setId_compte(Integer.toString(rs.getInt("id_compte")));
					admin_compte.setNom(rs.getString("nom"));
					admin_compte.setPrenom(rs.getString("prenom"));
					admin_compte.setLogin(rs.getString("login"));
					admin_compte.setMotdepasse(pwdcrypt.decrypter(rs.getString("pwd")));
					admin_compte.setProfile(rs.getString("libelle_profile"));
					admin_compte.setBasedonnee(rs.getString("l.nom_base"));
					admin_compte.setDate_deb_val(rs.getDate("val_date_deb"));
					admin_compte.setDate_fin_val(rs.getDate("val_date_fin"));
					admin_compte.setDatemodifpwd(rs.getString("modifiedpwd"));
					
					  
					listlogin.add(admin_compte);
				   
					
				}
//			stmt.close();
//			conn.close();
			
		}
//		catch (SQLException e) {
//			stmt.close();
//			conn.close();
//			
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
		return listlogin;
	
		
		
	}
	
	public ArrayList<AdministrationLoginBean> filtreLoginBean(String condition1, String valeur_condition1, String condition2, String valeur_condition2 ) throws SQLException{
		
		PwdCrypt pwdcrypt=new PwdCrypt();
		listlogin = new ArrayList<AdministrationLoginBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		ResultSet rs=null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String whereClause1="";
			String whereClause2="";
			if(valeur_condition1!=null && !"".equals(valeur_condition1))
			{
				whereClause1= " and upper("+condition1+") like upper('"+valeur_condition1+"%')";
			}
			if(valeur_condition2!=null && !"".equals(valeur_condition2))
			{
				whereClause2= " and upper("+condition2+") like upper('"+valeur_condition2+"%')";
			}
			
			String sel_compte="select id_compte,nom,prenom,c.login,c.pwd,libelle_profile,l.nom_base,DATE_FORMAT(val_date_deb,'%Y/%m/%d') as val_date_deb,DATE_FORMAT(val_date_fin,'%Y/%m/%d') as val_date_fin ,modifiedpwd "+ 
                               "from compte c ,liste_db l ,profile p where c.database_id=l.database_id "+
                               "and c.id_profile=p.id_profile "+ whereClause1 + whereClause2 +" order by l.nom_base,nom";
			
			/*ResultSet*/ rs = (ResultSet) stmt.executeQuery(sel_compte);
			
			SimpleDateFormat formatDateJour = new SimpleDateFormat("yyyy/MM/dd");
			 
			
			while(rs.next()){
				
					AdministrationLoginBean admin_compte=new AdministrationLoginBean();
					
					admin_compte.setId_compte(Integer.toString(rs.getInt("id_compte")));
					admin_compte.setNom(rs.getString("nom"));
					admin_compte.setPrenom(rs.getString("prenom"));
					admin_compte.setLogin(rs.getString("login"));
					admin_compte.setMotdepasse(pwdcrypt.decrypter(rs.getString("pwd")));
					admin_compte.setProfile(rs.getString("libelle_profile"));
					admin_compte.setBasedonnee(rs.getString("l.nom_base"));
					admin_compte.setDate_deb_val(rs.getDate("val_date_deb"));
					admin_compte.setDate_fin_val(rs.getDate("val_date_fin"));
					admin_compte.setDatemodifpwd(rs.getString("modifiedpwd"));
					
					  
					listlogin.add(admin_compte);
				   
					
				}
//			stmt.close();
//			conn.close();
			
		}
//		catch (SQLException e) {
//			stmt.close();
//			conn.close();
//			
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
		return listlogin;
	
		
		
	}
	
	/**
	 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
	 * @param addedData
	 * @return
	 * @throws ParseException 
	 */

	@SuppressWarnings({ "finally", "finally" })
	public boolean addAdministrationLoginBean(AdministrationLoginBean addedData) throws ParseException
	{
		
		PwdCrypt pwdcrypt=new PwdCrypt();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		boolean retour =false;
		
		try 
		{
			                                                
			stmt = (Statement) conn.createStatement();
			String select_structure="INSERT INTO compte ( id_profile, login, pwd, database_id, val_date_deb, val_date_fin, modifiedpwd, nom, prenom) VALUES (#id_profile,#login,#pwd,#database_id,#val_date_deb,#val_date_fin,#modifiedpwd,#nom,#prenom)";
			select_structure = select_structure.replaceAll("#id_profile", Integer.toString(getKeyMap(addedData.getProfile())));
			select_structure = select_structure.replaceAll("#login", "'"+addedData.getLogin()+"'");
			select_structure = select_structure.replaceAll("#pwd", "'"+pwdcrypt.crypter(addedData.getMotdepasse())+"'");
			select_structure = select_structure.replaceAll("#database_id", Integer.toString((Integer)getDatabaseList().get((addedData.getBasedonnee()))));
			select_structure = select_structure.replaceAll("#val_date_deb", "'"+ formatter.format(addedData.getDate_deb_val())+"'");
			select_structure = select_structure.replaceAll("#val_date_fin", "'"+formatter.format(addedData.getDate_fin_val())+"'");
			select_structure = select_structure.replaceAll("#modifiedpwd", "'"+getCurrentDatetime()+"'");
			select_structure = select_structure.replaceAll("#nom", "'"+addedData.getNom()+"'");
			select_structure = select_structure.replaceAll("#prenom", "'"+addedData.getPrenom()+"'");
			
		//System.out.println(select_structure);
			
			 stmt.execute(select_structure);
			 retour=true;
		}
		catch ( SQLException e ) {
			try 
			{
				Messagebox.show("La donnée n'a pas été insérée dans la base données", "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
	        return retour;
	    }
		

//		catch (SQLException e) 
//		{
//			try 
//			{
//				Messagebox.show("La donnée n'a pas été insérée dans la base données", "Erreur",Messagebox.OK, Messagebox.ERROR);
//			} 
//			catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			// TODO Auto-generated catch block
//			try {
//				conn.close();
//			} catch (SQLException e1) {
//				// TODO Auto-generated catch block
//				
//				e1.printStackTrace();
//				//return false;
//			}
//			
//			
//			return false;
//		}
//		try {
//			conn.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return true;
	}
	/**
	 * cette classe permet de controler la validité des données insérées (par rapport à leurs taille)
	 * @param addedData
	 * @return
	 * @throws InterruptedException 
	 */
	public boolean controleIntegrite(AdministrationLoginBean addedData) throws InterruptedException
	{
		try 
		{   
			
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			//int pwd = Integer.parseInt(addedData.getMotdepasse());
			
			
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			if(addedData.getNom().length()>50)
			{
				Messagebox.show("La taille du champ nom ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
				
				return false;
			}
			else
				if(addedData.getPrenom().length()>50)
				{
					Messagebox.show("La taille du champ prénom ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
					return false;
				}
				else
					if(addedData.getLogin().length()>11)
					{
						Messagebox.show("La taille du champ login ne doit pas dépasser 10 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
						return false;
					}
					/*else
						 
						if(addedData.getMotdepasse().length()<=10)
						{
							Messagebox.show("La taille du mot de passe doit contenir au maximum 10 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
							return false;
						}*/
						else
							if(addedData.getBasedonnee().length()>50)
							{
								Messagebox.show("La taille du champ base de données ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
								return false;
							}
							else
								if(!isValidDateStr(formatter.format(addedData.getDate_deb_val())))
								{
									Messagebox.show("La date debut validité doit être au format AAAA/MM/DD", "Erreur",Messagebox.OK, Messagebox.ERROR);
									return false;
								}
								else
									if(!isValidDateStr(formatter.format(addedData.getDate_fin_val())))
									{
										Messagebox.show("La date fin validité doit être au format AAAA/MM/DD", "Erreur",Messagebox.OK, Messagebox.ERROR);
										return false;
									}
									
		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (NumberFormatException nfe)
	    {
			Messagebox.show("La taille du mot de passe doit contenir maximum 10 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
			return false;
	    }
		
			
		return true;
	}
	
	/**
	 * Cette classe permet de mettre à jour la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	public Boolean majAdminLoginBean(AdministrationLoginBean addedData)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		PwdCrypt pwdcrypt=new PwdCrypt();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		Boolean retour=false;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="UPDATE  compte SET id_profile=#id_profile ,login=#login,pwd=#pwd,database_id=#database_id, val_date_deb=#val_date_deb,val_date_fin=#val_date_fin,modifiedpwd=#modifiedpwd,nom=#nom,prenom=#prenom WHERE id_compte=#valeur_id_compte"; 
			select_structure = select_structure.replaceAll("#id_profile", Integer.toString(getKeyMap(addedData.getProfile())));
			select_structure = select_structure.replaceAll("#login", "'"+addedData.getLogin()+"'");
			select_structure = select_structure.replaceAll("#pwd", "'"+pwdcrypt.crypter(addedData.getMotdepasse())+"'");
			select_structure = select_structure.replaceAll("#database_id", Integer.toString((Integer)getDatabaseList().get((addedData.getBasedonnee()))));
			select_structure = select_structure.replaceAll("#val_date_deb", "'"+formatter.format(addedData.getDate_deb_val())+"'");
			select_structure = select_structure.replaceAll("#val_date_fin", "'"+formatter.format(addedData.getDate_fin_val())+"'");
			select_structure = select_structure.replaceAll("#modifiedpwd", "'"+getCurrentDatetime()+"'");
			select_structure = select_structure.replaceAll("#nom", "'"+addedData.getNom()+"'");
			select_structure = select_structure.replaceAll("#prenom", "'"+addedData.getPrenom()+"'");
			select_structure = select_structure.replaceAll("#valeur_id_compte", addedData.getId_compte());
			
		   //System.out.println(update_structure);
			
			 stmt.executeUpdate(select_structure);
			 retour=true;
		} 
		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La modification n'a pas été prise en compte. Merci de contacter votre administrateur ", "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				// TODO Auto-generated catch block
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
	 * @throws InterruptedException 
	 */
	public void supprimerLogin(AdministrationLoginBean addedData) throws InterruptedException
	{
		
		if(addedData.getId_compte()==null){
			Messagebox.show("La liste des employés doit être rafraichie avant de supprimer la ligne! ", "Erreur", Messagebox.OK, Messagebox.ERROR);
			return;
			
		}
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt=null;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sup_login="DELETE FROM  compte  WHERE id_compte=#valeur_id_compte"; 
			sup_login = sup_login.replaceAll("#valeur_id_compte", addedData.getId_compte());
			
		
			
			 stmt.executeUpdate(sup_login);
		} 
		catch ( SQLException e ) {

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
//		catch (SQLException e) 
//		{
//			
//				e.printStackTrace();
//				//return false;
//
//
//		}
//		try {
//			conn.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	/**
	 * cette classe permet de supprimer une donnée de la table structure_entreprise
	 * @param codeStructure
	 * @throws SQLException 
	 */
	public HashMap gerProfileList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		List list_profile=new ArrayList();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select  id_profile, libelle_profile from profile "; 
			 rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("libelle_profile"), rs.getInt("id_profile"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
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
	
	
	public HashMap getDatabaseList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String db_list="select  database_id, nom_base from liste_db"; 
			rs = (ResultSet) stmt.executeQuery(db_list);
			
			
			while(rs.next()){
				map.put( rs.getString("nom_base"),rs.getInt("database_id"));
	        }
			stmt.close();conn.close();
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
	    }


}
