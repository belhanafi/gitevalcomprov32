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

import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;

import administration.bean.AdministrationLoginBean;
import administration.bean.CompagneCreationBean;
import administration.bean.CotationIMIvsStrategieBean;
import administration.bean.DataBaseClientLinkBean;
import administration.bean.DatabaseManagementBean;
import administration.bean.IMIvsStrategieBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.StructureEntrepriseBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import common.CreateDatabaseCon;
import common.PwdCrypt;
import common.StringUtils;

public class CotationIMIvsStrategieModel {


	private ArrayList<CotationIMIvsStrategieBean>  listbean=null; 


	private ListModel strset =null;
	

	/**
	 * cette méthode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	public List loadCotationCompetence() throws SQLException{


		listbean = new ArrayList<CotationIMIvsStrategieBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select id_cotation,label_cotation,definition_cotation,valeur_cotation from cotation_competence";

			 rs = (ResultSet) stmt.executeQuery(sel_comp);

			while(rs.next()){

				CotationIMIvsStrategieBean bean=new CotationIMIvsStrategieBean();
				bean.setId_cotation(rs.getInt("id_cotation"));
				bean.setLabel_cotation(rs.getString("label_cotation"));
				bean.setDefinition_cotation(rs.getString("definition_cotation"));
				bean.setValeur_cotation(String.valueOf(rs.getInt("valeur_cotation")));

				listbean.add(bean);


			}
			stmt.close();
			conn.close();

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
	public boolean addCotation(CotationIMIvsStrategieBean addedData) throws ParseException
	{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;

		Boolean retour=false;
		try 
		{

			stmt = (Statement) conn.createStatement();
			String sql_query=" INSERT INTO cotation_competence(label_cotation, definition_cotation, valeur_cotation)  VALUES(#label_cotation,#definition_cotation, #valeur_cotation) ";

			sql_query = sql_query.replaceAll("#label_cotation", "'"+ addedData.getLabel_cotation().toUpperCase()+"'");
			sql_query = sql_query.replaceAll("#definition_cotation", "'"+ addedData.getDefinition_cotation()+"'");
			sql_query = sql_query.replaceAll("#valeur_cotation", "'"+ addedData.getValeur_cotation()+"'");

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
	 * @throws SQLException 
	 */

	public boolean controleIntegrite(CotationIMIvsStrategieBean addedData) throws InterruptedException, SQLException
	{
		try 
		{   

			Iterator<CotationIMIvsStrategieBean> index=loadCotationCompetence().iterator();
			while(index.hasNext())
			{
				CotationIMIvsStrategieBean donnee=index.next();
				if (Integer.valueOf(addedData.getValeur_cotation())==Integer.valueOf(donnee.getValeur_cotation())){

					Messagebox.show("La valeur de la cotation: "+Integer.valueOf(addedData.getValeur_cotation())+" exist deja!", "Erreur",Messagebox.OK, Messagebox.ERROR);

					return false;

				}

			}

		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		return true;
	}


	public boolean controleIntegriteImi(IMIvsStrategieBean addedData) throws InterruptedException, SQLException
	{
		try 
		{   
			if (Integer.valueOf(addedData.getImi_borne_inf())>= Integer.valueOf(addedData.getImi_borne_sup())){
				Messagebox.show("La borne inferieure doit être inferieure strictement a la borne superieure!", "Erreur",Messagebox.OK, Messagebox.ERROR);
				return false;		 
			}
			Iterator<IMIvsStrategieBean> index=loadIMIvsStrategie().iterator();
			while(index.hasNext())
			{
				IMIvsStrategieBean donnee=index.next();
				if ((Integer.valueOf(addedData.getImi_borne_inf())>=Integer.valueOf(donnee.getImi_borne_inf()) && 
						Integer.valueOf(addedData.getImi_borne_inf())<Integer.valueOf(donnee.getImi_borne_sup()))) {
					Messagebox.show("Chevauchement  de la borne inferieure avec des bornes existantes ", "Erreur",Messagebox.OK, Messagebox.ERROR);
					return false;	 
				}
				else if ((Integer.valueOf(addedData.getImi_borne_sup())>=Integer.valueOf(donnee.getImi_borne_inf()) && 
						Integer.valueOf(addedData.getImi_borne_sup())<Integer.valueOf(donnee.getImi_borne_sup()))) {
					Messagebox.show("Chevauchement  de la borne superieure avec des bornes existantes ", "Erreur",Messagebox.OK, Messagebox.ERROR);
					return false;	 
				}

			}

		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		return true;
	}




	/**
	 * Cette classe permet de mettre à jour la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	public Boolean updateCotation(CotationIMIvsStrategieBean addedData)	{



		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		Boolean retour=false; 
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query=" update cotation_competence set label_cotation=#label_cotation, definition_cotation=#definition_cotation, valeur_cotation=#valeur_cotation where id_cotation=#id_cotation";         
			sql_query = sql_query.replaceAll("#label_cotation", "'"+ addedData.getLabel_cotation().toUpperCase()+"'");
			sql_query = sql_query.replaceAll("#definition_cotation", "'"+ addedData.getDefinition_cotation()+"'");
			sql_query = sql_query.replaceAll("#valeur_cotation", "'"+ addedData.getValeur_cotation()+"'");
			sql_query = sql_query.replaceAll("#id_cotation", "'"+ addedData.getId_cotation()+"'");



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
	public Boolean deleteCotation(CotationIMIvsStrategieBean addedData) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		Boolean retour=false;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="DELETE FROM  cotation_competence   where id_cotation=#id_cotation"; 
			sql_query = sql_query.replaceAll("#id_cotation", "'"+ addedData.getId_cotation()+"'");



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

	public List loadIMIvsStrategie() throws SQLException{


		List listbean1 = new ArrayList<IMIvsStrategieBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select id_imi_startegie,imi_borne_inf,imi_borne_sup,besoin_developpement,startegie from imi_strategie";

			 rs = (ResultSet) stmt.executeQuery(sel_comp);

			while(rs.next()){

				IMIvsStrategieBean bean=new IMIvsStrategieBean();
				bean.setId_imi_startegie(rs.getInt("id_imi_startegie"));
				bean.setImi_borne_inf(String.valueOf(rs.getInt("imi_borne_inf")));
				bean.setImi_borne_sup(String.valueOf(rs.getInt("imi_borne_sup")));
				bean.setBesoin_developpement(rs.getString("besoin_developpement"));
				bean.setStartegie(rs.getString("startegie"));
				listbean1.add(bean);


			}
			stmt.close();
			conn.close();

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


		return listbean1;

	}


	public boolean addImiVsStrat(IMIvsStrategieBean addedData) throws ParseException
	{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;

		Boolean retour=false;
		try 
		{

			stmt = (Statement) conn.createStatement();
			String sql_query="INSERT INTO imi_strategie( imi_borne_inf, imi_borne_sup, besoin_developpement, startegie) " +
					" values (#imi_borne_inf,#imi_borne_sup,#besoin_developpement,#startegie)";		
			sql_query = sql_query.replaceAll("#imi_borne_inf", "'"+ addedData.getImi_borne_inf()+"'");
			sql_query = sql_query.replaceAll("#imi_borne_sup", "'"+ addedData.getImi_borne_sup()+"'");
			sql_query = sql_query.replaceAll("#besoin_developpement", "'"+ addedData.getBesoin_developpement()+"'");
			sql_query = sql_query.replaceAll("#startegie", "'"+ addedData.getStartegie()+"'");

			stmt.execute(sql_query);conn.close();
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


	public boolean UpdateImiVsStrat(IMIvsStrategieBean addedData) throws ParseException
	{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		Boolean retour=false;

		try 
		{
			StringUtils stringUtils=new StringUtils();

			stmt = (Statement) conn.createStatement();
			String sql_query="Update imi_strategie set imi_borne_inf=#imi_borne_inf, imi_borne_sup=#imi_borne_sup, besoin_developpement=#besoin_developpement, startegie=#startegie where id_imi_startegie=#id_imi_startegie ";
			sql_query = sql_query.replaceAll("#imi_borne_inf", "'"+ addedData.getImi_borne_inf()+"'");
			sql_query = sql_query.replaceAll("#imi_borne_sup", "'"+ addedData.getImi_borne_sup()+"'");
			sql_query = sql_query.replaceAll("#besoin_developpement", "'"+ removeString(addedData.getBesoin_developpement())+"'");
			sql_query = sql_query.replaceAll("#startegie", "'"+ removeString(addedData.getStartegie())+"'");
			sql_query = sql_query.replaceAll("#id_imi_startegie", "'"+ addedData.getId_imi_startegie()+"'");


			stmt.execute(sql_query);
			retour=true;
			//stmt.close();conn.close();
		} 
		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La donnée n'a pas été modifiée dans la base ", "Erreur",Messagebox.OK, Messagebox.ERROR);
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


	public boolean deleteImiVsStrat(IMIvsStrategieBean addedData) throws ParseException
	{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		Boolean retour=false;

		try 
		{

			stmt = (Statement) conn.createStatement();
			String sql_query="delete from imi_strategie where id_imi_startegie=#id_imi_startegie ";
			sql_query = sql_query.replaceAll("#id_imi_startegie", "'"+ addedData.getId_imi_startegie()+"'");


			stmt.execute(sql_query);
			retour=true;
			//conn.close();
		} 
		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La donnée n'a pas été suprimmée dans la base ", "Erreur",Messagebox.OK, Messagebox.ERROR);
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


	public String removeString(String chaine){
		String chaine_trt="";
		if(chaine==null)
			return "";
		else		
			if(!chaine.equals(""))
			{
				chaine_trt=chaine;
	           chaine_trt=chaine_trt.replaceAll("’"," ");
	           chaine_trt=chaine_trt.replaceAll("'"," ");
			}
		return chaine_trt;
	}


}
