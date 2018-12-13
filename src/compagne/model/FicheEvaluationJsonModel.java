package compagne.model;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.sql.rowset.serial.SerialBlob;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.zkoss.zk.ui.Sessions;

import common.ApplicationSession;
import common.CreateDatabaseCon;
import compagne.bean.CotationEvaluationBean;
import compagne.bean.FicheEvaluationBean;
import compagne.bean.FicheEvaluationJsonBean;

public class FicheEvaluationJsonModel {
	//utilisée dans le cadre du rattrapage des calculs imi /img


	public HashMap<String,String> getListSalariesPourRattrapage(String id_compagne)
	{
		HashMap<String,String> mapSalariesRattrapage= new HashMap<String,String>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select distinct f.id_employe, f.id_planning_evaluation from fiche_evaluation_json f, planning_evaluation p where f.id_planning_evaluation= p.id_planning_evaluation and id_compagne=#id_compagne";

			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);




			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String id_employe=rs.getString("id_employe");
					String id_planning_evaluation=rs.getString("id_planning_evaluation");
					mapSalariesRattrapage.put(id_employe, id_planning_evaluation);

				}
				//						else {
				//							return mapcode_descriptionPoste;
				//						}

			}
			//					stmt.close();
			//					conn.close();
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

		return mapSalariesRattrapage;
	}
	//utilisée dans le cadre du rattrapage des calculs imi /img
	public boolean viderTableValidationCompagne(String id_compagne)
	{
		//si on garde cette fonction il faudra utiliser le paramètre id_compagne pour ne vider que les compagnes qui concernent une compagne spécifiqud
		String requete="";
		boolean resultat=true;

		//création de la requete
		String  delet_structure="     ";		

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();//connectToEntrepriseDBMulti();
		PreparedStatement stmt=null; 
		PreparedStatement stmtDelete1=null; 
		PreparedStatement stmtDelete2=null; 
		PreparedStatement stmtDelete3=null; 


		try 
		{

			stmtDelete1=conn.prepareStatement("TRUNCATE Table img_stats");
			stmtDelete2=conn.prepareStatement("TRUNCATE Table moy_poste_famille_stats");
			stmtDelete3=conn.prepareStatement("TRUNCATE moy_poste_competence_stats");

			stmtDelete1.execute();
			stmtDelete2.execute();
			stmtDelete3.execute();


			conn.close();
			resultat=true;
		} 
		catch ( SQLException e ) {
			System.out.println(requete);
			e.printStackTrace();
			resultat=false;

		} finally {
			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}
			if ( stmtDelete1 != null ) {
				try {
					stmtDelete1.close();
				} catch ( SQLException ignore ) {
				}
			}
			if ( stmtDelete2 != null ) {
				try {
					stmtDelete2.close();
				} catch ( SQLException ignore ) {
				}
			}
			if ( stmtDelete3 != null ) {
				try {
					stmtDelete3.close();
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
		return resultat;

	}

	public boolean updateFicheEvalutionJson(String id_employe,String id_planning_evaluation,FicheEvaluationJsonBean ficheEvaluationJsonBean)
	{
		String requete="";
		boolean resultat=true;

		try {

			//conversion de l'objet FicheEvaluationJsonBean en flux json
			ObjectMapper mapper = new ObjectMapper();
			byte[] monObject= mapper.writeValueAsBytes(ficheEvaluationJsonBean);
			String sFicheEvalution = mapper.writeValueAsString(ficheEvaluationJsonBean);

			//création de la requete
			String  delet_structure="delete from fiche_evaluation_json where id_planning_evaluation=#id_planning_evaluation and id_employe=#id_employe";		
			String insert_structure="INSERT INTO fiche_evaluation (id_planning_evaluation,ficheEvaluation,id_employe) VALUES (#id_planning_evaluation,#id_repertoire_competence,#ficheEvaluation,#id_employe)";


			//				requete=requete+ delet_structure+" ; "+ insert_structure+ " ; ";
			//				
			//				requete = requete.replaceAll("#id_planning_evaluation", id_planning_evaluation);
			//
			//				
			//				requete = requete.replaceAll("#ficheEvaluation", monObject);
			//				requete = requete.replaceAll("#id_employe", id_employe);
			/******************************************/


			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToSecondairesDB();//connectToEntrepriseDBMulti();
			PreparedStatement stmt=null; 
			PreparedStatement stmtDelete=null; 


			try 
			{
				Blob b = new SerialBlob(monObject);
				//b.setBytes(1, monObject);
				stmtDelete=conn.prepareStatement("delete from fiche_evaluation_json where id_planning_evaluation=? and id_employe=?");
				stmtDelete.setInt(1,  new Integer(id_planning_evaluation));
				stmtDelete.setInt(2, new Integer(id_employe));
				stmt= conn.prepareStatement("INSERT INTO fiche_evaluation_json (id_planning_evaluation,ficheEvaluation,id_employe) VALUES (?,?,?)");

				stmt.setInt(1, new Integer(id_planning_evaluation));
				stmt.setBlob(2,b);
				stmt.setInt(3, new Integer(id_employe));

				stmtDelete.execute();
				stmt.executeUpdate();

				//stmt = (Statement) conn.createStatement();

				//stmt.execute(requete);

				conn.close();
				resultat=true;
			} 
			catch ( SQLException e ) {
				System.out.println(requete);
				e.printStackTrace();
				resultat=false;

			} finally {
				if ( stmt != null ) {
					try {
						stmt.close();
					} catch ( SQLException ignore ) {
					}
				}
				if ( stmtDelete != null ) {
					try {
						stmtDelete.close();
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
			return resultat;





		} catch (JsonGenerationException e) {
			e.printStackTrace();
			//TODO logger l'exception 
		} catch (JsonMappingException e) {
			e.printStackTrace();
			//TODO logger l'exception 
		} catch (IOException e) {
			e.printStackTrace();
			//TODO logger l'exception 
		}

		return resultat;
	}

	public HashMap <String, ArrayList<FicheEvaluationBean>> getMaFicheEvaluaton(int id_employe,	 HashMap <String, String> mapcodeCompetenceLibelleCompetence,
			HashMap <String, String> mapidRepCompetence_ApptitudeObservable,String idCompagne )
			{

		HashMap <String, ArrayList<FicheEvaluationBean>> mapFamilleFicheEvaluation=new HashMap <String, ArrayList<FicheEvaluationBean>>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="select DISTINCT r.famille , r.libelle_competence, r.aptitude_observable, f.id_cotation from repertoire_competence r, fiche_evaluation f where f.id_employe=#id_employe and r.id_repertoire_competence=f.id_repertoire_competence";
			//String select_structure="select DISTINCT r.famille , r.libelle_competence, r.aptitude_observable, f.id_cotation, max(p.date_evaluation) from repertoire_competence r, fiche_evaluation f , planning_evaluation p where f.id_employe=#id_employe and r.id_repertoire_competence=f.id_repertoire_competence group by  r.famille , r.libelle_competence, r.aptitude_observable, f.id_cotation ";

			//String select_structure="select DISTINCT c.compagne_type,   f.ficheEvaluation, max(p.date_evaluation) from compagne_type c ,  fiche_evaluation_json f , planning_evaluation p where f.id_employe=#id_employe and  f.id_planning_evaluation=p.id_planning_evaluation and p.id_compagne=c.id_compagne_type group by  c.compagne_type";
			//Modif Nabil du 16/12/2015

			String select_structure="select DISTINCT c.compagne_type,   f.ficheEvaluation, max(p.date_evaluation)"
					+ " from compagne_type c ,  fiche_evaluation_json f , planning_evaluation p,compagne_evaluation e"
					+ " where f.id_employe=#id_employe  and  f.id_planning_evaluation=p.id_planning_evaluation"
					+ " and e.id_compagne=p.id_compagne and e.id_compagne_type=c.id_compagne_type and e.id_compagne=#id_compagne  group by  c.compagne_type";

			select_structure = select_structure.replaceAll("#id_employe", "'"+id_employe+"'");
			select_structure = select_structure.replaceAll("#id_compagne", "'"+idCompagne+"'");

			rs = (ResultSet) stmt.executeQuery(select_structure);

			//pour une compagne donnée la requete ne va retourner qu'un seul résultat

			while(rs.next()) //laisser cette boucle et l'utiliser 
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));					

					String date_evaluation=rs.getString("max(p.date_evaluation)");
					String compagne_type=rs.getString("compagne_type");

					Blob sFicheEvaluation=rs.getBlob("ficheEvaluation");
					//transformation de l'objet stringJson en object java
					ObjectMapper mapper = new ObjectMapper();
					//mapper.getDeserializationConfig().without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
					mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					FicheEvaluationJsonBean ficheEvaluation = mapper.readValue(sFicheEvaluation.getBytes(1,(int)sFicheEvaluation.length()), FicheEvaluationJsonBean.class);
					//System.out.println("fiche evaluation "+ficheEvaluation);
					//recuperation des données à partir du flux json
					ArrayList <CotationEvaluationBean> listCotationEvaluationBean= ficheEvaluation.getListCotationEvaluationBean();
					Iterator <CotationEvaluationBean> iterateur=listCotationEvaluationBean.iterator();
					while(iterateur.hasNext())
					{

						CotationEvaluationBean cotationEvaluationBean=iterateur.next();
						int id_repertoire_competence=cotationEvaluationBean.getId_repertoire_competence();
						String codeCompetence=cotationEvaluationBean.getCode_competence();
						//recuperation du libelle de competence
						String libelle_competence=mapcodeCompetenceLibelleCompetence.get(codeCompetence);//cotationEvaluationBean.getLibelle_competence();
						//recuperation de l'apptitude observable
						String aptitude_observable=mapidRepCompetence_ApptitudeObservable.get(id_repertoire_competence+"");//cotationEvaluationBean.getAptitude_observable();
						String id_cotation=cotationEvaluationBean.getCotation();

						String famille=cotationEvaluationBean.getFamille();
						FicheEvaluationBean fiche=new FicheEvaluationBean();
						fiche.setAptitude_observable(aptitude_observable);
						fiche.setLibelle_competence(libelle_competence);
						fiche.setNiveau_maitrise(new Integer(id_cotation));
						fiche.setDate_evaluation(date_evaluation);
						fiche.setCompagne_type(compagne_type);
						fiche.setId_repertoire_competence(id_repertoire_competence);

						if(mapFamilleFicheEvaluation.containsKey(famille))
						{
							ArrayList<FicheEvaluationBean> listfiche=mapFamilleFicheEvaluation.get(famille);
							listfiche.add(fiche);
							mapFamilleFicheEvaluation.put(famille, listfiche);

						}
						else
						{
							ArrayList<FicheEvaluationBean> listfiche=new ArrayList<FicheEvaluationBean>();
							listfiche.add(fiche);
							mapFamilleFicheEvaluation.put(famille, listfiche);

						}

					}



				}
				//				else {
				//					return mapFamilleFicheEvaluation;
				//				}

			}
			//			stmt.close();
			//			conn.close();
		} 
		catch ( SQLException e ) {
			//TODO logguer l'exception

		} catch (JsonParseException e) {
			//TODO logguer l'exception
			e.printStackTrace();
		} catch (JsonMappingException e) {
			//TODO logguer l'exception
			e.printStackTrace();
		} catch (IOException e) {
			//TODO logguer l'exception
			e.printStackTrace();
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

		return mapFamilleFicheEvaluation;

			}

	// cette ethoderetourne la liste des compétences (clé == le code compétence)

	public HashMap<String, String > getListCompetence(int id_compagne) throws ParseException
	{
		HashMap<String, String> mapcode_libelleCompetence=new HashMap<String, String>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();
			
			
			
			if (verifDateDebCampVsDateJour(id_compagne)){
				
				select_structure="Select distinct libelle_competence,code_competence from repertoire_competence  ";
				
			}else{
				select_structure="Select distinct libelle_competence,code_competence from repertoire_competence  where affichable='O' ";
			}


			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String code_competence=rs.getString("code_competence");
					String libelle_competence=rs.getString("libelle_competence");
					mapcode_libelleCompetence.put(code_competence, libelle_competence);

				}
				//				else {
				//					return mapcode_descriptionPoste;
				//				}

			}
			//			stmt.close();
			//			conn.close();
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

		return mapcode_libelleCompetence;
	}
	public HashMap<String, String > getListapptitudeObservable(int id_compagne) throws ParseException
	{
		HashMap<String, String> mapidRepCompetence_ApptitudeObservable=new HashMap<String, String>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			if (verifDateDebCampVsDateJour(id_compagne)){
				select_structure="select distinct id_repertoire_competence,aptitude_observable from repertoire_competence";
			}else{
				select_structure="select distinct id_repertoire_competence,aptitude_observable from repertoire_competence where affichable='O' ";
			}



			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					int id_rep_competence=rs.getInt("id_repertoire_competence");
					String id_repertoire_competence=new Integer(id_rep_competence).toString();
					String aptitude_observable=rs.getString("aptitude_observable");
					mapidRepCompetence_ApptitudeObservable.put(id_repertoire_competence, aptitude_observable);

				}
				//				else {
				//					return mapcode_descriptionPoste;
				//				}

			}
			//			stmt.close();
			//			conn.close();
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

		return mapidRepCompetence_ApptitudeObservable;
	}

	//à partir de là c'est l'implémentation des methodes pour faire l'export des fiches d'evaluation vers le format json

	public ArrayList<FicheEvaluationBean> geListEmployeAExporter()
	{
		ArrayList<FicheEvaluationBean> listEmployes=new ArrayList<FicheEvaluationBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select distinct  id_employe, id_planning_evaluation from fiche_evaluation ";


			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					FicheEvaluationBean EMploye=new FicheEvaluationBean();
					int id_employe=rs.getInt("id_employe");
					int id_planning_evaluation=rs.getInt("id_planning_evaluation");
					EMploye.setId_employe(id_employe);
					EMploye.setId_planning_evaluation(id_planning_evaluation);
					listEmployes.add(EMploye);

				}
				//				else {
				//					return mapcode_descriptionPoste;
				//				}

			}
			//			stmt.close();
			//			conn.close();
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
		return listEmployes;
	}


	public HashMap<String, String> getlistefamillesCode_famille()
	{

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		HashMap<String, String > mapcode_famille=new HashMap<String, String >();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from planning_evaluation) ";
			String select_structure="SELECT famille,  code_famille FROM repertoire_competence ";


			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String famille=rs.getString("famille");
					String code_famille=rs.getString("code_famille");

					mapcode_famille.put(famille, code_famille);

				}
				else {
					return mapcode_famille;
				}

			}
			conn.close();
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
		return mapcode_famille;	

	}
	public boolean verifierNbAptitudecote(int nbAptitude, String code_poste)
	{



		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		HashMap<String, String > mapcode_famille=new HashMap<String, String >();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from planning_evaluation) ";
			String select_structure="select count(id_repertoire_competence) nombre_aptitude from poste_travail_comptence_aptitudeobservable where code_poste=#code_poste";

			select_structure = select_structure.replaceAll("#code_poste", "'"+code_poste+"'");
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					int nombre_aptitude=rs.getInt("nombre_aptitude");

					if(nombre_aptitude==nbAptitude)
						return true;
					else return false;
				}
				else {
					return false;
				}

			}
			conn.close();
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

		return false;
	}



	/**
	 * historisation des modifications sur la fiche évaluation 
	 * 
	 */

	public void histoFicheEvaluation(int nbrAptitude, int id_employe,String nomEmploye,String intitule_poste)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		String login=applicationSession.getCompteUtilisateur().getLogin();

		try
		{
			stmt = (Statement) conn.createStatement();
			String insert_structure="INSERT INTO  histo_fiche_evaluation  (login,date_histo,nbrAptitude,id_employe,nomEmploye,intitule_poste) "
					+ " VALUES (#login, timestamp(now()),#nbrAptitude,#id_employe,#nomEmploye,#intitule_poste)"; 
			insert_structure = insert_structure.replaceAll("#login", "'"+login+"'");
			insert_structure = insert_structure.replaceAll("#nbrAptitude", "'"+ nbrAptitude+ "'");
			insert_structure = insert_structure.replaceAll("#id_employe", "'"+ id_employe+ "'");
			insert_structure = insert_structure.replaceAll("#nomEmploye", "'"+ nomEmploye+ "'");
			insert_structure = insert_structure.replaceAll("#intitule_poste", "'"+ intitule_poste+ "'");




			//System.out.println(insert_structure);	
			stmt.execute(insert_structure);
		} 

		catch ( SQLException e ) {
			e.printStackTrace();

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

	}


	public HashMap getListCompagneValid() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select  id_compagne,concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) nomcompagne" +
					" from compagne_evaluation e 	where e.id_compagne in (select id_compagne from compagne_validation where compagne_valide=1)";
			rs = (ResultSet) stmt.executeQuery(sql_query);


			while(rs.next()){
				map.put(rs.getString("nomcompagne"), rs.getString("id_compagne"));
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


	public HashMap getListAllCompagne() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select  id_compagne,concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) nomcompagne" +
					" from compagne_evaluation e 	order by id_compagne Desc ";
			rs = (ResultSet) stmt.executeQuery(sql_query);


			while(rs.next()){
				map.put(rs.getString("nomcompagne"), rs.getString("id_compagne"));
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


	// recuperer la  date debut du compagne selectionnée
	public int getCompagneEnCours()
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		int max_compagne_id=0;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select max(id_compagne) id_compagne from compagne_evaluation where id_compagne not in (select id_compagne from  compagne_validation where compagne_valide=1)";

			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{

					max_compagne_id=rs.getInt("id_compagne");



				}
				//					else {
				//						return listCompagnes;
				//					}

			}
			//				stmt.close();
			//				conn.close();
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
		return max_compagne_id;
	}

	public HashMap<String, String> getlisteCode_postes_intituleposte(int id_compagne)
	{

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		HashMap<String, String > mapcode_poste=new HashMap<String, String >();
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from planning_evaluation) ";
			String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
					" where c.id_compagne=p.id_compagne and c.id_compagne=#id_compagne) ";


			select_structure = select_structure.replaceAll("#id_compagne", "'"+id_compagne+"'");




			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String intitule_poste=rs.getString("intitule_poste");
					String code_poste=rs.getString("code_poste");

					mapcode_poste.put(code_poste,intitule_poste );

				}
				else {
					return mapcode_poste;
				}

			}
			conn.close();
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


		return mapcode_poste;	

	}

	public HashMap<String, String> getlistepostesCode_postes(int id_compagne )
	{

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		HashMap<String, String > mapcode_poste=new HashMap<String, String >();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from planning_evaluation) ";
			String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from compagne_poste_travail p, compagne_evaluation c" +
					" where c.id_compagne=p.id_compagne and c.id_compagne=#id_compagne) ";

			select_structure = select_structure.replaceAll("#id_compagne", "'"+id_compagne+"'");


			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String intitule_poste=rs.getString("intitule_poste");
					String code_poste=rs.getString("code_poste");

					mapcode_poste.put(intitule_poste, code_poste);

				}
				else {
					return mapcode_poste;
				}

			}
			conn.close();
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
		return mapcode_poste;	

	}
	//

	/**
	 * Methode qui permet de recuperer uniquement la liste des compagnes validés
	 * Cette liste sera utilisée pour la combox compagne MaFiCheEvaluation
	 * L'évaluateur et l'évalué doivent avoir accès  à leur fiche uniquement une fois
	 * la compagne validée
	 * @return
	 * @throws SQLException
	 */

	public HashMap getListCompagneValidee() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		HashMap<String,Integer> aMap2 = new LinkedHashMap<String, Integer>();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select  c.id_compagne,concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) nomcompagne"
					+ "  from compagne_evaluation c ,compagne_validation e   where c.id_compagne=e.id_compagne"
					+ " and compagne_valide=1 order by id_compagne Desc ";
			rs = (ResultSet) stmt.executeQuery(sql_query);


			while(rs.next()){
				map.put(rs.getString("nomcompagne"), rs.getString("id_compagne"));
				//list_profile.add(rs.getString("libelle_profile"));
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

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return map;
	}

	//Methode qui permet d'afficher IMI et IMG dans le rapport PDF de la fiche evaluation; fiches déja validées
	public Map<String, Float> getIMIetIMG(int idEmploye, int idCompagne)
	{


		Map<String, Float> resultat=new HashMap<String, Float>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		PreparedStatement pstmt = null;
		ResultSet rs=null;

		try 
		{

			String sql=" select distinct i.id_employe,round(imi,2) imi,g.img from imi_stats i, img_stats g where id_employe=?"
					+ " and i.id_compagne=g.id_compagne and g.code_poste in (select code_poste from employe where id_employe=?)"
					+ " and g.id_compagne=?";
			pstmt = conn.prepareStatement(sql);


			pstmt.setInt(1, idEmploye);
			pstmt.setInt(2, idEmploye);
			pstmt.setInt(3, idCompagne);

			//System.out.println(pstmt.toString());

			rs =  pstmt.executeQuery();




			while(rs.next()){
				resultat.put("IMI", rs.getFloat("imi"));
				resultat.put("IMG", rs.getFloat("img"));
			}

		}catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( pstmt != null ) {
				try {
					pstmt.close();
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
		return resultat;
	}


	public boolean verifDateDebCampVsDateJour( int idCompagne) throws ParseException
	{


		Map<String, Float> resultat=new HashMap<String, Float>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		boolean retour=false;


		try 
		{

			String sql=" select date_debut, now() date_actuelle from compagne_evaluation where id_compagne=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, idCompagne);

			//System.out.println(pstmt.toString());

			rs =  pstmt.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = sdf.parse("2017-12-31");



			while(rs.next()){

				//cas  des campagnes cotées avec  le repertoire de compétences complet 272 records, sinon  nouvelles campagnes avec
				//repertoire de compétences réduit
				if (rs.getDate("date_debut").compareTo(date1)<0){
					retour=true;

				}

			}

		}catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( pstmt != null ) {
				try {
					pstmt.close();
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

	 public static <K, V extends Comparable<V>> Map<K, V> 
	    sortByValues(final Map<K, V> map) {
	    Comparator<K> valueComparator = 
	             new Comparator<K>() {
	      public int compare(K k1, K k2) {
	        int compare = 
	              map.get(k1).compareTo(map.get(k2));
	        if (compare == 0) 
	          return compare;
	        else 
	          return 1;
	      }
	    };
	 
	    Map<K, V> sortedByValues = 
	      new TreeMap<K, V>(valueComparator);
	    sortedByValues.putAll(map);
	    return sortedByValues;
	  }

}
