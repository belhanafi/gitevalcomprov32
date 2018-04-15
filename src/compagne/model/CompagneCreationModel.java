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
import administration.bean.PosteVsAptitudeBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.StructureEntrepriseBean;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import common.CreateDatabaseCon;
import common.PwdCrypt;
import compagne.bean.CompagnePosteMapBean;
import compagne.bean.MixIdealBean;

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
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select id_compagne,libelle_compagne,compagne_type,DATE_FORMAT(date_debut, '%Y-%m-%d')as date_debut,DATE_FORMAT(date_fin, '%Y-%m-%d') as date_fin,c.id_compagne_type from compagne_evaluation c,compagne_type t" +
					" where c.id_compagne_type=t.id_compagne_type";

			rs = (ResultSet) stmt.executeQuery(sel_comp);

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
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Boolean retour=false;
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
			retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La donnée n'a pas été insérée dans la base données", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Boolean retour=false;
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
			retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La modification n'a pas été prise en compte", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	 */
	public Boolean deleteCompagne(CompagneCreationBean addedData)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Boolean retour=false;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="DELETE FROM  compagne_evaluation   where id_compagne=#id_compagne"; 
			sql_query = sql_query.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");



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
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		List list_profile=new ArrayList();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select id_compagne_type,compagne_type from compagne_type"; 
			rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				map.put(rs.getString("compagne_type"), rs.getInt("id_compagne_type"));
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


	public HashMap getListCompagne() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		List list_profile=new ArrayList();

		try 
		{
			stmt = (Statement) conn.createStatement();
			//String profile_list="select id_compagne,concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) as libelle_compagne from compagne_evaluation where now()<=date_fin order by date_fin";
			String profile_list="select id_compagne,concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) as libelle_compagne from compagne_evaluation  order by id_compagne desc";
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				map.put(rs.getString("libelle_compagne"), rs.getInt("id_compagne"));
				//list_profile.add(rs.getString("libelle_profile"));
			}
			//stmt.close();conn.close();
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

		return map;
	}	


	public List loadPosteMapToComapgne(int compagne_id) throws SQLException{


		ArrayList<CompagnePosteMapBean>   listposte = new ArrayList<CompagnePosteMapBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select distinct code_poste,intitule_poste,sum(st) as map_stat " +
					" from ( select p.code_poste,intitule_poste,1 as st from compagne_poste_travail c ,poste_travail_description p" +
					" where c.code_poste=p.code_poste and c. id_compagne=#id_compagne union  select code_poste,intitule_poste,0 as st from poste_travail_description   where code_poste !='P000' ) as t1" +
					" group by code_poste,intitule_poste";

			sel_comp = sel_comp.replaceAll("#id_compagne", "'"+ compagne_id+"'");

			rs = (ResultSet) stmt.executeQuery(sel_comp);

			while(rs.next()){

				CompagnePosteMapBean compagne=new CompagnePosteMapBean();
				compagne.setMap_stat(rs.getInt("map_stat"));	
				compagne.setCode_poste(rs.getString("code_poste"));
				compagne.setLibelle_poste(rs.getString("intitule_poste"));

				listposte.add(compagne);


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
		return listposte;



	}


	public List appliquerMapPosteCompagne(HashMap checked_poste, int compagne_id ) throws SQLException{


		ArrayList<CompagnePosteMapBean>   listposte = new ArrayList<CompagnePosteMapBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();//connectToEntrepriseDBMulti();
		Statement stmt = null;

		stmt = (Statement) conn.createStatement();
		Set set = (checked_poste).entrySet(); 
		Iterator i = set.iterator();
		String delete_before_insert="delete from compagne_poste_travail where id_compagne="+"'"+compagne_id+"'";
		stmt.addBatch(delete_before_insert);
		String insert="";
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			insert="insert into compagne_poste_travail values ("+"'"+me.getValue()+"',"+ "'"+(String) me.getKey()+"' )";

			stmt.addBatch(insert);
		}
		//String final_query=delete_before_insert+";"+insert;

		try {


			stmt.executeBatch();


			//		stmt.close();
			//		conn.close();

		} catch ( SQLException e ) {

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
		return listposte;



	}


	public Boolean dupliquerPlanning(CompagneCreationBean addedData ) throws SQLException{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();//connectToEntrepriseDBMulti();
		Statement stmt = null;
		boolean retour=false;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");




		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="insert into planning_evaluation ( id_compagne,id_employe,date_evaluation,id_evaluateur,heure_debut_evaluation,"
					+ " heure_fin_evaluation, lieu ,    code_poste,     personne_ressources  , code_structure,date_fin_evaluation)"
					+ " select  #id_compagne,id_employe,#date_evaluation,id_evaluateur,heure_debut_evaluation,"
					+ " heure_fin_evaluation,lieu ,code_poste,personne_ressources,code_structure,#date_fin_evaluation  from planning_evaluation"
					+ " where id_compagne in (select max(id_compagne) from compagne_validation  where compagne_valide=1)";

			sql_query = sql_query.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");
			sql_query = sql_query.replaceAll("#date_evaluation", "'"+ formatter.format(addedData.getDate_deb_comp())+"'");
			sql_query = sql_query.replaceAll("#date_fin_evaluation", "'"+formatter.format(addedData.getDate_fin_comp())+"'");





			stmt.executeUpdate(sql_query);
			retour=true;
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
		return retour;



	}

	public Boolean VerifPlanningCreation(CompagneCreationBean addedData ) throws SQLException{


		ArrayList<CompagnePosteMapBean>   listposte = new ArrayList<CompagnePosteMapBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		boolean retour=false;
		try {
			stmt = (Statement) conn.createStatement();
			String sql_query="select  id_planning_evaluation from planning_evaluation where id_compagne=#id_compagne";

			sql_query = sql_query.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				retour=true;


			}
			//		stmt.close();
			//		conn.close();

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
		return retour;



	}


	public Boolean dupliquerCompagnePosteTravail(CompagneCreationBean addedData ) throws SQLException{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();//connectToEntrepriseDBMulti();
		Statement stmt = null;
		boolean retour=false;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");




		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="insert into compagne_poste_travail(id_compagne,code_poste)"
					+ "  select  #id_compagne  ,   code_poste from compagne_poste_travail  "
					+ "  where id_compagne in (select max(id_compagne) from compagne_validation  where compagne_valide=1)";

			sql_query = sql_query.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");



			stmt.executeUpdate(sql_query);
			retour=true;
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
		return retour;



	}


	/**
	 * verifier si un planing a été déja crée pour cette compagne
	 * @return
	 * @throws SQLException
	 */
	public boolean checkIfPlanningExist(int compagne_id) throws SQLException{


		listcompagne = new ArrayList<CompagneCreationBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		boolean retour=false;
		try {
			stmt = (Statement) conn.createStatement();
			String sql_query="select id_compagne from planning_evaluation where id_compagne=#id_compagne";
			sql_query = sql_query.replaceAll("#id_compagne", "'"+String.valueOf(compagne_id)+"'");


			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				retour=true;


			}
			//		stmt.close();
			//		conn.close();

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
		return retour;



	}

	/*
	 *//**
	 * cette methode permet de recuperer le mix ideal defini pour chaque compagne
	 * @param codeStructure
	 * @throws SQLException 
	 *//*
	 */

	public ArrayList<MixIdealBean> getMixNiveauMaitrise() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		List list_profile=new ArrayList();
		ResultSet rs=null;
		ArrayList<MixIdealBean> list=new ArrayList<MixIdealBean>();
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select m.id_compagne,libelle_compagne,libelle_niveau_maitrise,objectif,date_debut,date_fin,m.id_niveau_maitrise from imi_mix_ideal m ,compagne_evaluation c, niveau_maitrise n"
					+ " where c.id_compagne=m.id_compagne and n.id_niveau_maitrise=m.id_niveau_maitrise order by c.id_compagne desc"; 
			rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				MixIdealBean bean= new MixIdealBean();

				bean.setId_compagne(rs.getInt("id_compagne"));
				bean.setLibelle_compagne(rs.getString("libelle_compagne"));
				bean.setLibelle_niveau_maitrise(rs.getString("libelle_niveau_maitrise"));
				bean.setObjectif(rs.getFloat("objectif"));
				bean.setDate_debut(rs.getString("date_debut"));
				bean.setDate_fin(rs.getString("date_fin"));
				bean.setId_niveau_maitrise(rs.getInt("id_niveau_maitrise"));
				list.add(bean);


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

		return list;
	}	


	public Boolean VerifCompagneCreation(MixIdealBean addedData ) throws SQLException{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		boolean retour=false;
		try {
			stmt = (Statement) conn.createStatement();
			String sql_query="select  id_compagne from imi_mix_ideal where id_compagne=#id_compagne";

			sql_query = sql_query.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				retour=true;
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

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return retour;



	}

	public Boolean dupliquerImiMixIdealSelected(MixIdealBean addedData ) throws SQLException{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();//connectToEntrepriseDBMulti();
		Statement stmt = null;
		boolean retour=false;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");




		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="insert into imi_mix_ideal(id_compagne,id_niveau_maitrise,objectif)"
					+ " select  #id_compagne  ,   id_niveau_maitrise,objectif from imi_mix_ideal"
					+ " where id_compagne in (select max(id_compagne) from compagne_validation  where compagne_valide=1)";

			sql_query = sql_query.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");



			stmt.executeUpdate(sql_query);
			retour=true;
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
		return retour;



	}

	public Boolean dupliquerImiMixIdeal(CompagneCreationBean addedData ) throws SQLException{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();//connectToEntrepriseDBMulti();
		Statement stmt = null;
		boolean retour=false;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");




		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="insert into imi_mix_ideal(id_compagne,id_niveau_maitrise,objectif)"
					+ " select  #id_compagne  ,   id_niveau_maitrise,objectif from imi_mix_ideal"
					+ " where id_compagne in (select max(id_compagne) from compagne_validation  where compagne_valide=1)";

			sql_query = sql_query.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");



			stmt.executeUpdate(sql_query);
			retour=true;
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
		return retour;



	}


	/**
	 * Cette classe permet de mettre à jour la table imi_mix_ideal
	 * @param addedData
	 * @return
	 */
	public Boolean updateImiMixIdeal(List<MixIdealBean> addedData)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Boolean retour=false;
		PreparedStatement pstmt=null;
		try 
		{

			// Disable auto-commit
			conn.setAutoCommit(false);

			String requete_Sql="Update imi_mix_ideal set objectif=?  where id_compagne=? and id_niveau_maitrise=?" ;
			pstmt = conn.prepareStatement(requete_Sql);



			//Insertion des données dans la table Structure_entreprise
			Iterator<MixIdealBean> itr=addedData.iterator();;

			while(itr.hasNext()){
				MixIdealBean bean= (MixIdealBean)itr.next();

				pstmt.setInt(2, bean.getId_compagne());
				pstmt.setInt(3,  bean.getId_niveau_maitrise());
				pstmt.setFloat(1, bean.getObjectif());
				pstmt.addBatch();
			}

			// Execute the batch
			int [] updateCounts = pstmt.executeBatch();

			// All statements were successfully executed.
			// updateCounts contains one element for each batched statement.
			// updateCounts[i] contains the number of rows affected by that statement.
			int total_update=processUpdateCounts(updateCounts);

			// Since there were no errors, commit
			conn.commit();
			//System.out.println("Nombre de ligne insérées dans imi_mix_ideal : "+total_update);	

		} 

		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La modification n'a pas été prise en compte", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	 * cette classe permet de supprimer une donnée de la table imi_mix_ideal
	 * @param codeStructure
	 */
	public Boolean deleteImiMixIdeal(MixIdealBean addedData)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Boolean retour=false;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="DELETE FROM  imi_mix_ideal   where id_compagne=#id_compagne "; 
			sql_query = sql_query.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");
			//sql_query = sql_query.replaceAll("#id_niveau_maitrise", "'"+String.valueOf(addedData.getId_niveau_maitrise())+"'");

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


	/**
	 * cette classe permet de verifier que la somme des objectifs du mix est égale à 100%
	 * @param codeStructure
	 */
	public float checkIMIMix(List<MixIdealBean> addedData)
	{
		float resultat = 0;

		Iterator itr=addedData.iterator();
		while (itr.hasNext()){
			MixIdealBean bean =(MixIdealBean)itr.next();
			resultat=resultat+bean.getObjectif();
		}


		return resultat;
	}

	public static int processUpdateCounts(int[] updateCounts) {

		int resultat_update=0;

		for (int i=0; i<updateCounts.length; i++) {
			if (updateCounts[i] >= 0) {
				// Successfully executed; the number represents number of affected rows
				resultat_update=resultat_update+updateCounts[i];
			} else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
				// Successfully executed; number of affected rows not available
			} else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
				// Failed to execute
			}
		}

		return resultat_update;
	}



	public boolean reinitCompagne(CompagneCreationBean addedData) throws SQLException, InterruptedException 
	{

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		//PreparedStatement pstmt=null;
		boolean result=false;

		Statement stmt = conn.createStatement();
		try {
			// Disable auto-commit
			conn.setAutoCommit(false);
		

			// Create a prepared statement
			String del_planning="DELETE FROM  planning_evaluation  where id_compagne=#id_compagne";  
			del_planning = del_planning.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");
			stmt.addBatch(del_planning);
			
			
			String del_comp_poste="DELETE FROM  compagne_poste_travail   where id_compagne=#id_compagne"; 
			
			del_comp_poste = del_comp_poste.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");
			stmt.addBatch(del_comp_poste);
						
			String del_mix="DELETE FROM  imi_mix_ideal   where id_compagne=#id_compagne"; 
			del_mix = del_mix.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");
			stmt.addBatch(del_mix);
			
			
			String del_comp="DELETE FROM  compagne_evaluation   where id_compagne=#id_compagne"; 
			del_comp = del_comp.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");
			stmt.addBatch(del_comp);

			//pstmt = conn.prepareStatement(requete_Sql);
			


			// Execute the batch
			int [] updateCounts = stmt.executeBatch();
		

			// All statements were successfully executed.
			// updateCounts contains one element for each batched statement.
			// updateCounts[i] contains the number of rows affected by that statement.
			int total_update=processUpdateCounts(updateCounts);

			// Since there were no errors, commit
			conn.commit();
			//System.out.println("Nombre de ligne mis a jour dans poste_travail_comptence_aptitudeobservable : "+total_update);
			//Messagebox.show("Nombre de lignes insérées : "+total_update, "Information",Messagebox.OK, Messagebox.INFORMATION); 
			result=true;
		} catch (BatchUpdateException e) {
			// Not all of the statements were successfully executed
			int[] updateCounts = e.getUpdateCounts();

			// Some databases will continue to execute after one fails.
			// If so, updateCounts.length will equal the number of batched statements.
			// If not, updateCounts.length will equal the number of successfully executed statements
			processUpdateCounts(updateCounts);

			// Either commit the successfully executed statements or rollback the entire batch
			conn.rollback();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}finally {

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

		return result;
	}
}
