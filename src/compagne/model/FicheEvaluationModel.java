package compagne.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import administration.bean.CotationBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import common.CreateDatabaseCon;
import compagne.bean.EmployesAEvaluerBean;
import compagne.bean.FicheEvaluationBean;
import compagne.bean.MapEmployesAEvaluerBean;

public class FicheEvaluationModel {

	/**
	 * cette méthode permet de savoir si l'employé est un evaluateur
	 * 
	 */
	public boolean getEstEvauateur(int id_employe)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		Boolean retour=false;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT est_evaluateur  FROM employe where id_employe=#id_employe ";

			select_structure = select_structure.replaceAll("#id_employe", "'"+id_employe+"'");
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					String est_evaluateur=rs.getString("est_evaluateur");
					if(est_evaluateur.equals("N"))
					{
						//return false;
						retour=false;
					}
					else
					{
						//return true;
						retour=true;
					}

				}
				else {
					{	//return false;
						retour=false;
					}
				}

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

		return retour;
	}
	/**
	 * cett eméthode permet de récuperer l'id_employ associé à l'id compte
	 * @return
	 */

	public int getIdEmploye(int id_compte)
	{
		int id_employe=-1;

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT id_employe  FROM employe where id_compte=#id_compte ";

			select_structure = select_structure.replaceAll("#id_compte", "'"+id_compte+"'");
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					id_employe=rs.getInt("id_employe");

				}
				else {
					return id_employe;
				}

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

		return id_employe;

	}

	public boolean getValiditeFiche(int id_employe)
	{
		boolean ficheValide=false;
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="SELECT fiche_valide  FROM fiche_validation where id_employe=#id_employe  ";

			String select_structure="SELECT fiche_valide, max(id_fiche_valide)  FROM fiche_validation where id_employe=#id_employe group by fiche_valide, id_fiche_valide ";

			select_structure = select_structure.replaceAll("#id_employe", "'"+id_employe+"'");
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					int validite=rs.getInt("fiche_valide");
					if(validite==1)
						ficheValide=true;
					else
						ficheValide=false;
				}
				//				else {
				//					return ficheValide;
				//				}

			}
			//conn.close();
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

		return ficheValide; 
	}
	/**
	 * cette méthode permet de récuperer les informations associes aux employés à évaluer
	 */

	public MapEmployesAEvaluerBean getListEmployesAEvaluer(int id_evaluateur, int id_compagne)
	{
		MapEmployesAEvaluerBean listEmployesAEvaluerBean=new MapEmployesAEvaluerBean();
		HashMap<String, EmployesAEvaluerBean> MapclesnomEmploye=listEmployesAEvaluerBean.getMapclesnomEmploye();
		HashMap<String, HashMap<String,EmployesAEvaluerBean>> Mapclesposte=listEmployesAEvaluerBean.getMapclesposte();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();

			/*String select_structure="select distinct  k.id_planning_evaluation,r.famille, r.code_famille, e.nom, e.prenom , k.id_employe, t.intitule_poste , t.code_poste "+ 
					"from repertoire_competence r, employe e , poste_travail_description t, planning_evaluation k  "+ 
					"where k.id_evaluateur=#id_evaluateur and   k.id_compagne  in (select distinct  max(id_compagne) from compagne_evaluation  "+    
					" where  date_fin>=now() and date_debut<=now()) "+ 
					"and k.id_employe=e.id_employe "+ 
					"and e.code_poste=t.code_poste   "+ 
					//"and ( k.id_planning_evaluation,k.id_employe) not in (select w.id_planning_evaluation,w.id_employe from fiche_validation w  where fiche_valide!='1' )";
					"and ( k.id_planning_evaluation,k.id_employe) not in (select w.id_planning_evaluation,w.id_employe from fiche_validation w  where fiche_valide='1' )";*/
			//la dernière ligne permet la selection des employé ayant une fiche d'evaluation non encore valide
			
			//Modif Nabil du 08/10/2017 limiter l'affichage  dans la combox poste travail à uniquement aux postes de travail associé à la campgane en cours
			// dans la table compagne_poste_travail
			String select_structure="select distinct  k.id_planning_evaluation,r.famille, r.code_famille, e.nom, e.prenom , k.id_employe, t.intitule_poste , t.code_poste "+ 
					"from repertoire_competence r, employe e , poste_travail_description t, planning_evaluation k  "+ 
					"where k.id_evaluateur=#id_evaluateur and   k.id_compagne  in (select distinct  max(c.id_compagne) from compagne_evaluation c  where id_compagne not in (select id_compagne from compagne_validation )) "+ 
					"and k.id_employe=e.id_employe "+ 
					"and e.code_poste=t.code_poste   "+ 
					//"and ( k.id_planning_evaluation,k.id_employe) not in (select w.id_planning_evaluation,w.id_employe from fiche_validation w  where fiche_valide!='1' )";
					"and ( k.id_planning_evaluation,k.id_employe) not in (select w.id_planning_evaluation,w.id_employe from fiche_validation w  where fiche_valide='1' ) and k.id_compagne=#id_compagne and t.code_poste in (select code_poste from compagne_poste_travail"
					+" where id_compagne=#id_compagne )";
			select_structure = select_structure.replaceAll("#id_evaluateur", "'"+id_evaluateur+"'");
			select_structure = select_structure.replaceAll("#id_compagne", "'"+id_compagne+"'");


			//System.out.println(select_structure);
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));

					int id_employe =rs.getInt("id_employe") ;
					String poste_travail=rs.getString("intitule_poste");
					String code_poste=rs.getString("code_poste") ; ;
					String nom_employe=rs.getString("nom")+" "+ rs.getString("prenom");
					String famille=rs.getString("famille");
					String code_famille=rs.getString("code_famille");
					int id_planning_evaluation=rs.getInt("id_planning_evaluation");

					if(MapclesnomEmploye.containsKey(nom_employe))
					{
						ArrayList<String> listFamille=MapclesnomEmploye.get(nom_employe).getCode_famille();
						listFamille.add(code_famille);
						MapclesnomEmploye.get(nom_employe).setCode_famille(listFamille);

						ArrayList<String> listLibelleFamille=MapclesnomEmploye.get(nom_employe).getFamille();
						listLibelleFamille.add(famille);
						MapclesnomEmploye.get(nom_employe).setFamille(listLibelleFamille);
					}
					else
					{

						EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
						employesAEvaluerBean.setCode_poste(code_poste);
						employesAEvaluerBean.setId_employe(id_employe);
						employesAEvaluerBean.setNom_employe(nom_employe);
						employesAEvaluerBean.setPoste_travail(poste_travail);
						employesAEvaluerBean.getCode_famille().add(code_famille);
						employesAEvaluerBean.getFamille().add(famille);
						employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
						MapclesnomEmploye.put(nom_employe,employesAEvaluerBean);
					}
					if(Mapclesposte.containsKey(poste_travail))
					{
						HashMap<String, EmployesAEvaluerBean> mapEmploye=Mapclesposte.get(poste_travail);
						if(mapEmploye.containsKey(nom_employe))
						{
							ArrayList<String> listFamille=mapEmploye.get(nom_employe).getCode_famille();
							listFamille.add(code_famille);
							mapEmploye.get(nom_employe).setCode_famille(listFamille);

							ArrayList<String> listLibelleFamille=mapEmploye.get(nom_employe).getFamille();
							listLibelleFamille.add(famille);
							mapEmploye.get(nom_employe).setFamille(listLibelleFamille);
							Mapclesposte.put(poste_travail, mapEmploye);

						}
						else
						{
							EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
							employesAEvaluerBean.setCode_poste(code_poste);
							employesAEvaluerBean.setId_employe(id_employe);
							employesAEvaluerBean.setNom_employe(nom_employe);
							employesAEvaluerBean.setPoste_travail(poste_travail);
							employesAEvaluerBean.getCode_famille().add(code_famille);
							employesAEvaluerBean.getFamille().add(famille);
							employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
							mapEmploye.put(nom_employe,employesAEvaluerBean);
							Mapclesposte.put(poste_travail, mapEmploye);
						}

					}
					else
					{
						HashMap<String, EmployesAEvaluerBean> mapEmploye=new HashMap<String, EmployesAEvaluerBean>();
						EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
						employesAEvaluerBean.setCode_poste(code_poste);
						employesAEvaluerBean.setId_employe(id_employe);
						employesAEvaluerBean.setNom_employe(nom_employe);
						employesAEvaluerBean.setPoste_travail(poste_travail);
						employesAEvaluerBean.getCode_famille().add(code_famille);
						employesAEvaluerBean.getFamille().add(famille);
						employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
						mapEmploye.put(nom_employe,employesAEvaluerBean);
						Mapclesposte.put(poste_travail, mapEmploye);
					}			


				}
				//				else {
				//					return listEmployesAEvaluerBean;
				//				}
				listEmployesAEvaluerBean.setMapclesnomEmploye(MapclesnomEmploye);
				listEmployesAEvaluerBean.setMapclesposte(Mapclesposte);
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

		return listEmployesAEvaluerBean;
	}
	/**
	 * Cette méthode récupère les informations relatifs à une fiche d'evaluation qu'on doit afficher avant l'evaluation d'un employé
	 * @throws ParseException 
	 */

	public HashMap<String, ArrayList<FicheEvaluationBean>> getInfosFicheEvaluationparPoste(int  id_compagne) throws ParseException
	{
		HashMap<String, ArrayList<FicheEvaluationBean>> mapPosteTravailFiche=new HashMap<String, ArrayList<FicheEvaluationBean>>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;

		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			int retour=compareDate();
			if (retour==0){
				select_structure="select distinct r.famille,  p.code_poste, r.id_repertoire_competence, r.code_competence, r.libelle_competence , r.definition_competence, r.aptitude_observable from repertoire_competence r , poste_travail_competence p where r.code_competence=p.code_competence and p.code_poste in(select distinct code_poste from planning_evaluation where id_compagne=#id_compagne) ";

			}else{

				select_structure="select distinct r.famille,  p.code_poste, r.id_repertoire_competence, r.code_competence, r.libelle_competence , r.definition_competence,"
						+ "	r.aptitude_observable 	from repertoire_competence r , poste_travail_comptence_aptitudeobservable p"
						+ " where r.code_competence=p.code_competence and p.code_poste in(select distinct code_poste from planning_evaluation where id_compagne=#id_compagne)"
						+ " and r.id_repertoire_competence=p.id_repertoire_competence";
			}

			select_structure = select_structure.replaceAll("#id_compagne", "'"+id_compagne+"'");


			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));

					int id_repertoire_competence=rs.getInt("id_repertoire_competence") ;
					String code_competence=rs.getString("code_competence");
					String libelle_competence=rs.getString("libelle_competence");
					String definition_competence=rs.getString("definition_competence");
					String aptitude_observable=rs.getString("aptitude_observable");
					String code_poste=rs.getString("code_poste");
					String famille=rs.getString("famille");

					FicheEvaluationBean ficheEvaluation=new FicheEvaluationBean();
					ficheEvaluation.setCode_competence(code_competence);
					ficheEvaluation.setId_repertoire_competence(id_repertoire_competence);
					ficheEvaluation.setLibelle_competence(libelle_competence);
					ficheEvaluation.setDefinition_competence(definition_competence);
					ficheEvaluation.setAptitude_observable(aptitude_observable);


					if(mapPosteTravailFiche.containsKey(code_poste+"#"+famille))
					{
						ArrayList<FicheEvaluationBean> listFiches=mapPosteTravailFiche.get(code_poste+"#"+famille);
						listFiches.add(ficheEvaluation);
						mapPosteTravailFiche.put(code_poste+"#"+famille, listFiches);
					}
					else
					{
						ArrayList<FicheEvaluationBean> listFiches=new ArrayList<FicheEvaluationBean>();
						listFiches.add(ficheEvaluation);
						mapPosteTravailFiche.put(code_poste+"#"+famille, listFiches);
					}
				}
				else {
					return mapPosteTravailFiche;
				}

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

		return mapPosteTravailFiche;
	}




	public HashMap<String, ArrayList<FicheEvaluationBean>> getInfosFicheEvaluationparPosteComp(int id_compagne) throws ParseException
	{
		HashMap<String, ArrayList<FicheEvaluationBean>> mapPosteTravailFiche=new HashMap<String, ArrayList<FicheEvaluationBean>>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;

		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			int retour=compareDate();
			if (retour==0){
				select_structure="select distinct r.famille,  p.code_poste, r.id_repertoire_competence, r.code_competence, r.libelle_competence , r.definition_competence, r.aptitude_observable from repertoire_competence r , poste_travail_competence p where r.code_competence=p.code_competence and p.code_poste in(select distinct code_poste from planning_evaluation where id_compagne= #id_compagne) ";

			}else{

				select_structure="select distinct r.famille,  p.code_poste, r.id_repertoire_competence, r.code_competence, r.libelle_competence , r.definition_competence,"
						+ "	r.aptitude_observable 	from repertoire_competence r , poste_travail_comptence_aptitudeobservable p"
						+ " where r.code_competence=p.code_competence and p.code_poste in(select distinct code_poste from planning_evaluation where id_compagne= #id_compagne)"
						+ " and r.id_repertoire_competence=p.id_repertoire_competence";
			}

			select_structure = select_structure.replaceAll("#id_compagne", "'"+id_compagne+"'");


			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));

					int id_repertoire_competence=rs.getInt("id_repertoire_competence") ;
					String code_competence=rs.getString("code_competence");
					String libelle_competence=rs.getString("libelle_competence");
					String definition_competence=rs.getString("definition_competence");
					String aptitude_observable=rs.getString("aptitude_observable");
					String code_poste=rs.getString("code_poste");
					String famille=rs.getString("famille");

					FicheEvaluationBean ficheEvaluation=new FicheEvaluationBean();
					ficheEvaluation.setCode_competence(code_competence);
					ficheEvaluation.setId_repertoire_competence(id_repertoire_competence);
					ficheEvaluation.setLibelle_competence(libelle_competence);
					ficheEvaluation.setDefinition_competence(definition_competence);
					ficheEvaluation.setAptitude_observable(aptitude_observable);


					if(mapPosteTravailFiche.containsKey(code_poste+"#"+famille))
					{
						ArrayList<FicheEvaluationBean> listFiches=mapPosteTravailFiche.get(code_poste+"#"+famille);
						listFiches.add(ficheEvaluation);
						mapPosteTravailFiche.put(code_poste+"#"+famille, listFiches);
					}
					else
					{
						ArrayList<FicheEvaluationBean> listFiches=new ArrayList<FicheEvaluationBean>();
						listFiches.add(ficheEvaluation);
						mapPosteTravailFiche.put(code_poste+"#"+famille, listFiches);
					}
				}
				else {
					return mapPosteTravailFiche;
				}

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

		return mapPosteTravailFiche;
	}

	public HashMap<String, String > getPosteTravailDescription()
	{
		HashMap<String, String> mapcode_descriptionPoste=new HashMap<String, String>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT code_poste, sommaire_poste  FROM poste_travail_description  ";


			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String code_poste=rs.getString("code_poste");
					String description_poste=rs.getString("sommaire_poste");
					mapcode_descriptionPoste.put(code_poste, description_poste);

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

		return mapcode_descriptionPoste;
	}
	public ArrayList <CotationBean> getCotations()
	{
		ArrayList <CotationBean>listCotation=new ArrayList<CotationBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT id_cotation, label_cotation, definition_cotation, valeur_cotation  FROM cotation_competence ";


			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					int id_cotation=rs.getInt("id_cotation");
					String label_cotation=rs.getString("label_cotation");
					String definition_cotation=rs.getString("definition_cotation");
					int valeur_cotation=rs.getInt("valeur_cotation");
					CotationBean cotationBean=new CotationBean();
					cotationBean.setId_cotation(id_cotation);
					cotationBean.setLabel_cotation(label_cotation);
					cotationBean.setDefinition_cotation(definition_cotation);
					cotationBean.setValeur_cotation(valeur_cotation);
					listCotation.add(cotationBean);

				}
				//				else {
				//					return listCotation;
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


		return listCotation;
	}
	public boolean  updateMultiQuery(String requete)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();//connectToEntrepriseDBMulti();
		Statement stmt=null;
		boolean resultat=true;

		try 
		{
			stmt = (Statement) conn.createStatement();

			stmt.execute(requete);

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

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return resultat;


	}

	public String updateFicheEvalutionConstructionRequete(String id_repertoire_competence,String id_employe,String id_planning_evaluation,String id_cotation, String requete)
	{

		String  delet_structure="delete from fiche_evaluation where id_planning_evaluation=#id_planning_evaluation and id_repertoire_competence=#id_repertoire_competence and id_employe=#id_employe";		
		String insert_structure="INSERT INTO fiche_evaluation (id_planning_evaluation,id_repertoire_competence,id_cotation,id_employe) VALUES (#id_planning_evaluation,#id_repertoire_competence,#id_cotation,#id_employe)";


		requete=requete+ delet_structure+" ; "+ insert_structure+ " ; ";

		requete = requete.replaceAll("#id_planning_evaluation", id_planning_evaluation);
		requete = requete.replaceAll("#id_repertoire_competence", id_repertoire_competence);
		if(id_cotation.equalsIgnoreCase("N/A")) id_cotation="0";
		requete = requete.replaceAll("#id_cotation", id_cotation);
		requete = requete.replaceAll("#id_employe", id_employe);
		//System.out.println(requete);
		return requete;

	}

	public void validerFicheEvaluation(String id_planning_evaluation, String id_employe, String statut_fiche)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();//connectToEntrepriseDBMulti();
		Statement stmt=null;
		String insert_structure="";
		String delete_structure="";

		try 
		{
			stmt = (Statement) conn.createStatement();
			if (statut_fiche.equalsIgnoreCase("0")){

				delete_structure=" delete from fiche_validation  where id_planning_evaluation=#id_planning_evaluation and id_employe=#id_employe";
				insert_structure="INSERT INTO fiche_validation (id_planning_evaluation,id_employe, fiche_valide) VALUES (#id_planning_evaluation,#id_employe,0)";

			}
			else {
				if (statut_fiche.equalsIgnoreCase("-1"))
				{
					delete_structure=" delete from fiche_validation  where id_planning_evaluation=#id_planning_evaluation and id_employe=#id_employe";
					insert_structure="INSERT INTO fiche_validation (id_planning_evaluation,id_employe, fiche_valide) VALUES (#id_planning_evaluation,#id_employe,-1)";

				}
				else
				{
					delete_structure=" delete from fiche_validation  where id_planning_evaluation=#id_planning_evaluation and id_employe=#id_employe";
					insert_structure="INSERT INTO fiche_validation (id_planning_evaluation,id_employe, fiche_valide) VALUES (#id_planning_evaluation,#id_employe,1)";

				}
			}
			String query=delete_structure+"; " + insert_structure+" ;";
			query = query.replaceAll("#id_planning_evaluation", id_planning_evaluation);
			query = query.replaceAll("#id_employe", id_employe);
			query = query.replaceAll("#statut_fiche", statut_fiche);


			stmt.execute(query);

			//conn.close();
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

	}
	/**
	 * retourne le nom et prenom d'un employé à partir de don identifiant
	 * @param id_employe
	 * @return
	 */
	public String getNomUtilisateur(int id_employe)
	{
		String nom_utilisateur="";
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="SELECT e.nom, e.prenom , p.intitule_poste FROM employe e, poste_travail_description p where e.id_employe=#id_employe and e.code_poste=p.code_poste";

			String select_structure="SELECT e.nom, e.prenom , p.intitule_poste,t.structure_ent structure_lbl FROM employe e, "
					+ " poste_travail_description p,"
					+ " (select code_structure, structure_ent from  ("
					+ "	select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
					+ " and  libelle_section !='null' and  libelle_section !=''"
					+ " union"
					+ "	select code_structure,libelle_service structure_ent from structure_entreprise"
					+ " where libelle_service is  not null and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_departement structure_ent from structure_entreprise"
					+ " where libelle_departement is  not null and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_sous_direction structure_ent from structure_entreprise"
					+ " where libelle_sous_direction is  not null and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_unite structure_ent from structure_entreprise where libelle_unite is  not null"
					+ " and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ " and length(libelle_service)=0 and  length(libelle_section) =0"
					+ "	union"
					+ " select code_structure,libelle_direction structure_ent from structure_entreprise"
					+ " where libelle_direction is  not null and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ " and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise)t"
					+ "	 where e.id_employe=#id_employe and e.code_poste=p.code_poste and e.code_structure=t.code_structure";

			select_structure = select_structure.replaceAll("#id_employe", ""+id_employe);

			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					nom_utilisateur=rs.getString("nom")+" "+ rs.getString("prenom") + "#"+rs.getString("intitule_poste")+"#"+rs.getString("structure_lbl");

				}
				//				else {
				//					return nom_utilisateur;
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

		return nom_utilisateur;
	}

	public String getStatutFicheEval(String id_planning_evaluation, String id_employe)
	{
		String statut_fiche="";
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select fiche_valide from fiche_validation where id_planning_evaluation=#id_planning_evaluation and id_employe=#id_employe";

			select_structure = select_structure.replaceAll("#id_employe", ""+id_employe);
			select_structure = select_structure.replaceAll("#id_planning_evaluation", ""+id_planning_evaluation);

			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					statut_fiche=String.valueOf(rs.getInt("fiche_valide"));

				}
				//				else {
				//					return statut_fiche;
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

		return statut_fiche;
	}


	public HashMap <String, ArrayList<FicheEvaluationBean>> getMaFicheEvaluaton(int id_employe)
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

			String select_structure="select DISTINCT c.compagne_type, r.famille , r.libelle_competence,r.code_competence, r.aptitude_observable, f.id_cotation, r.id_repertoire_competence,max(p.date_evaluation) from compagne_type c , repertoire_competence r, fiche_evaluation f , planning_evaluation p where f.id_employe=#id_employe and r.id_repertoire_competence=f.id_repertoire_competence and f.id_planning_evaluation=p.id_planning_evaluation and p.id_compagne=c.id_compagne_type group by  c.compagne_type,r.famille , r.libelle_competence, r.aptitude_observable, f.id_cotation,r.id_repertoire_competence ";
			select_structure = select_structure.replaceAll("#id_employe", "'"+id_employe+"'");
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					String famille=rs.getString("famille");
					String libelle_competence=rs.getString("libelle_competence");
					String aptitude_observable=rs.getString("aptitude_observable");
					String id_cotation=rs.getString("id_cotation");
					String date_evaluation=rs.getString("max(p.date_evaluation)");
					String compagne_type=rs.getString("compagne_type");
					String id_repertoire_competence=rs.getString("id_repertoire_competence");
					String codeCompetence=rs.getString("code_competence");
					FicheEvaluationBean fiche=new FicheEvaluationBean();
					fiche.setAptitude_observable(aptitude_observable);
					fiche.setLibelle_competence(libelle_competence);
					fiche.setNiveau_maitrise(new Integer(id_cotation));
					fiche.setDate_evaluation(date_evaluation);
					fiche.setCompagne_type(compagne_type);
					fiche.setId_repertoire_competence(new Integer(id_repertoire_competence));
					fiche.setCode_competence(codeCompetence);
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
				//				else {
				//					return mapFamilleFicheEvaluation;
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

		return mapFamilleFicheEvaluation;


	}

	public ArrayList <String> getFamilleAssociePoste(String code_poste) throws ParseException
	{
		ArrayList <String> listeFamille= new ArrayList<String>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			int retour=compareDate();
			if (retour==0){

				select_structure="select distinct  g.famille from repertoire_competence g , poste_travail_competence w , poste_travail_description a where g.code_competence=w.code_competence and w.code_poste=#code_poste";

			}else{
				select_structure="select distinct  g.famille from repertoire_competence g , poste_travail_comptence_aptitudeobservable w , poste_travail_description a where g.code_competence=w.code_competence and w.code_poste=#code_poste";

			}


			select_structure = select_structure.replaceAll("#code_poste", "'"+code_poste+"'");
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					listeFamille.add(rs.getString("famille"));
					//System.out.println(rs.getString("famille"));
				}
				//				else {
				//					return listeFamille;
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

		return listeFamille;


	}

	/**
	 * cette méthode permet de récuperer les informations associes aux employés à évaluer
	 */

	public MapEmployesAEvaluerBean getListEmployesvalue(int id_evaluateur)
	{
		MapEmployesAEvaluerBean listEmployesAEvaluerBean=new MapEmployesAEvaluerBean();
		HashMap<String, EmployesAEvaluerBean> MapclesnomEmploye=listEmployesAEvaluerBean.getMapclesnomEmploye();
		HashMap<String, HashMap<String,EmployesAEvaluerBean>> Mapclesposte=listEmployesAEvaluerBean.getMapclesposte();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();

			String select_structure="select distinct  k.id_planning_evaluation,r.famille, r.code_famille, e.nom, e.prenom , e.id_employe, t.intitule_poste , t.code_poste "+ 
					"from repertoire_competence r, employe e , poste_travail_description t, planning_evaluation k "+ 
					"where e.id_employe in "+   
					" (select distinct v.id_employe "+  
					"from planning_evaluation v , compagne_evaluation n "+  
					" where v.id_evaluateur=#id_evaluateur  and v.id_compagne "+ 
					"in ( select distinct id_compagne "+   
					"from compagne_evaluation "+   
					"where  date_fin>=now() and date_debut<=now()))  "+  
					"and e.code_poste=t.code_poste  and e.code_poste =k.code_poste and e.id_employe=k.id_employe "+
					"and e.id_employe  in (select i.id_employe from fiche_validation i where i.fiche_valide='1')";

			//la dernière ligne permet la selection des employé ayant une fiche d'evaluation  valide

			select_structure = select_structure.replaceAll("#id_evaluateur", "'"+id_evaluateur+"'");


			//System.out.println(select_structure);
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));

					int id_employe =rs.getInt("id_employe") ;
					String poste_travail=rs.getString("intitule_poste");
					String code_poste=rs.getString("code_poste") ; ;
					String nom_employe=rs.getString("nom")+" "+ rs.getString("prenom");
					String famille=rs.getString("famille");
					String code_famille=rs.getString("code_famille");
					int id_planning_evaluation=rs.getInt("id_planning_evaluation");

					if(MapclesnomEmploye.containsKey(nom_employe))
					{
						ArrayList<String> listFamille=MapclesnomEmploye.get(nom_employe).getCode_famille();
						listFamille.add(code_famille);
						MapclesnomEmploye.get(nom_employe).setCode_famille(listFamille);

						ArrayList<String> listLibelleFamille=MapclesnomEmploye.get(nom_employe).getFamille();
						listLibelleFamille.add(famille);
						MapclesnomEmploye.get(nom_employe).setFamille(listLibelleFamille);
					}
					else
					{

						EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
						employesAEvaluerBean.setCode_poste(code_poste);
						employesAEvaluerBean.setId_employe(id_employe);
						employesAEvaluerBean.setNom_employe(nom_employe);
						employesAEvaluerBean.setPoste_travail(poste_travail);
						employesAEvaluerBean.getCode_famille().add(code_famille);
						employesAEvaluerBean.getFamille().add(famille);
						employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
						MapclesnomEmploye.put(nom_employe,employesAEvaluerBean);
					}
					if(Mapclesposte.containsKey(poste_travail))
					{
						HashMap<String, EmployesAEvaluerBean> mapEmploye=Mapclesposte.get(poste_travail);
						if(mapEmploye.containsKey(nom_employe))
						{
							ArrayList<String> listFamille=mapEmploye.get(nom_employe).getCode_famille();
							listFamille.add(code_famille);
							mapEmploye.get(nom_employe).setCode_famille(listFamille);

							ArrayList<String> listLibelleFamille=mapEmploye.get(nom_employe).getFamille();
							listLibelleFamille.add(famille);
							mapEmploye.get(nom_employe).setFamille(listLibelleFamille);
							Mapclesposte.put(poste_travail, mapEmploye);

						}
						else
						{
							EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
							employesAEvaluerBean.setCode_poste(code_poste);
							employesAEvaluerBean.setId_employe(id_employe);
							employesAEvaluerBean.setNom_employe(nom_employe);
							employesAEvaluerBean.setPoste_travail(poste_travail);
							employesAEvaluerBean.getCode_famille().add(code_famille);
							employesAEvaluerBean.getFamille().add(famille);
							employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
							mapEmploye.put(nom_employe,employesAEvaluerBean);
							Mapclesposte.put(poste_travail, mapEmploye);
						}

					}
					else
					{
						HashMap<String, EmployesAEvaluerBean> mapEmploye=new HashMap<String, EmployesAEvaluerBean>();
						EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
						employesAEvaluerBean.setCode_poste(code_poste);
						employesAEvaluerBean.setId_employe(id_employe);
						employesAEvaluerBean.setNom_employe(nom_employe);
						employesAEvaluerBean.setPoste_travail(poste_travail);
						employesAEvaluerBean.getCode_famille().add(code_famille);
						employesAEvaluerBean.getFamille().add(famille);
						employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
						mapEmploye.put(nom_employe,employesAEvaluerBean);
						Mapclesposte.put(poste_travail, mapEmploye);
					}			


				}
				//				else {
				//					return listEmployesAEvaluerBean;
				//				}
				listEmployesAEvaluerBean.setMapclesnomEmploye(MapclesnomEmploye);
				listEmployesAEvaluerBean.setMapclesposte(Mapclesposte);
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

		return listEmployesAEvaluerBean;
	}


	/**
	 * cette méthode permet de récuperer les informations associes aux employés à évaluer
	 */

	public MapEmployesAEvaluerBean getListEmployesvalueComp(int id_evaluateur,int id_compagne)
	{
		MapEmployesAEvaluerBean listEmployesAEvaluerBean=new MapEmployesAEvaluerBean();
		HashMap<String, EmployesAEvaluerBean> MapclesnomEmploye=listEmployesAEvaluerBean.getMapclesnomEmploye();
		HashMap<String, HashMap<String,EmployesAEvaluerBean>> Mapclesposte=listEmployesAEvaluerBean.getMapclesposte();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();

			String select_structure="select distinct  k.id_planning_evaluation,r.famille, r.code_famille, e.nom, e.prenom , "
					+ "  e.id_employe, t.intitule_poste , t.code_poste"
					+ "  	from repertoire_competence r, employe e , poste_travail_description t, planning_evaluation k,fiche_validation i "
					+ "     where k.id_evaluateur=#id_evaluateur  and k.id_compagne =#id_compagne and e.code_poste=t.code_poste "
					+ "     and e.code_poste =k.code_poste and e.id_employe=k.id_employe"
					+ "     and i.fiche_valide ='1' and i.id_planning_evaluation=k.id_planning_evaluation";

			//la dernière ligne permet la selection des employé ayant une fiche d'evaluation  valide

			select_structure = select_structure.replaceAll("#id_evaluateur", "'"+id_evaluateur+"'");
			select_structure = select_structure.replaceAll("#id_compagne", "'"+id_compagne+"'");



			//System.out.println(select_structure);
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));

					int id_employe =rs.getInt("id_employe") ;
					String poste_travail=rs.getString("intitule_poste");
					String code_poste=rs.getString("code_poste") ; ;
					String nom_employe=rs.getString("nom")+" "+ rs.getString("prenom");
					String famille=rs.getString("famille");
					String code_famille=rs.getString("code_famille");
					int id_planning_evaluation=rs.getInt("id_planning_evaluation");

					if(MapclesnomEmploye.containsKey(nom_employe))
					{
						ArrayList<String> listFamille=MapclesnomEmploye.get(nom_employe).getCode_famille();
						listFamille.add(code_famille);
						MapclesnomEmploye.get(nom_employe).setCode_famille(listFamille);

						ArrayList<String> listLibelleFamille=MapclesnomEmploye.get(nom_employe).getFamille();
						listLibelleFamille.add(famille);
						MapclesnomEmploye.get(nom_employe).setFamille(listLibelleFamille);
					}
					else
					{

						EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
						employesAEvaluerBean.setCode_poste(code_poste);
						employesAEvaluerBean.setId_employe(id_employe);
						employesAEvaluerBean.setNom_employe(nom_employe);
						employesAEvaluerBean.setPoste_travail(poste_travail);
						employesAEvaluerBean.getCode_famille().add(code_famille);
						employesAEvaluerBean.getFamille().add(famille);
						employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
						MapclesnomEmploye.put(nom_employe,employesAEvaluerBean);
					}
					if(Mapclesposte.containsKey(poste_travail))
					{
						HashMap<String, EmployesAEvaluerBean> mapEmploye=Mapclesposte.get(poste_travail);
						if(mapEmploye.containsKey(nom_employe))
						{
							ArrayList<String> listFamille=mapEmploye.get(nom_employe).getCode_famille();
							listFamille.add(code_famille);
							mapEmploye.get(nom_employe).setCode_famille(listFamille);

							ArrayList<String> listLibelleFamille=mapEmploye.get(nom_employe).getFamille();
							listLibelleFamille.add(famille);
							mapEmploye.get(nom_employe).setFamille(listLibelleFamille);
							Mapclesposte.put(poste_travail, mapEmploye);

						}
						else
						{
							EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
							employesAEvaluerBean.setCode_poste(code_poste);
							employesAEvaluerBean.setId_employe(id_employe);
							employesAEvaluerBean.setNom_employe(nom_employe);
							employesAEvaluerBean.setPoste_travail(poste_travail);
							employesAEvaluerBean.getCode_famille().add(code_famille);
							employesAEvaluerBean.getFamille().add(famille);
							employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
							mapEmploye.put(nom_employe,employesAEvaluerBean);
							Mapclesposte.put(poste_travail, mapEmploye);
						}

					}
					else
					{
						HashMap<String, EmployesAEvaluerBean> mapEmploye=new HashMap<String, EmployesAEvaluerBean>();
						EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
						employesAEvaluerBean.setCode_poste(code_poste);
						employesAEvaluerBean.setId_employe(id_employe);
						employesAEvaluerBean.setNom_employe(nom_employe);
						employesAEvaluerBean.setPoste_travail(poste_travail);
						employesAEvaluerBean.getCode_famille().add(code_famille);
						employesAEvaluerBean.getFamille().add(famille);
						employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
						mapEmploye.put(nom_employe,employesAEvaluerBean);
						Mapclesposte.put(poste_travail, mapEmploye);
					}			


				}
				//				else {
				//					return listEmployesAEvaluerBean;
				//				}
				listEmployesAEvaluerBean.setMapclesnomEmploye(MapclesnomEmploye);
				listEmployesAEvaluerBean.setMapclesposte(Mapclesposte);
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

		return listEmployesAEvaluerBean;
	}

	public MapEmployesAEvaluerBean getListTousEmployesvalueComp(int id_compagne)
	{
		MapEmployesAEvaluerBean listEmployesAEvaluerBean=new MapEmployesAEvaluerBean();
		HashMap<String, EmployesAEvaluerBean> MapclesnomEmploye=listEmployesAEvaluerBean.getMapclesnomEmploye();
		HashMap<String, HashMap<String,EmployesAEvaluerBean>> Mapclesposte=listEmployesAEvaluerBean.getMapclesposte();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();


			String select_structure="select distinct  k.id_planning_evaluation,r.famille, r.code_famille, e.nom, e.prenom , e.id_employe, t.intitule_poste , t.code_poste"
					+ " from repertoire_competence r, employe e , poste_travail_description t, planning_evaluation k"
					+ " where e.id_employe in  (select distinct v.id_employe from planning_evaluation v , compagne_evaluation n ,fiche_validation  i"
					+ "                 		 where    v.id_compagne in ( select distinct id_compagne from compagne_evaluation"
					+ "                          where  id_compagne=#id_compagne) and i.fiche_valide='1' and i.id_planning_evaluation=v.id_planning_evaluation)"
					+ " and e.code_poste=t.code_poste  and e.code_poste =k.code_poste and e.id_employe=k.id_employe and k.id_planning_evaluation "
					+ " in (select distinct  w.id_planning_evaluation from planning_evaluation w , compagne_evaluation h where  h.id_compagne=#id_compagne and w.id_compagne=h.id_compagne)";

			//la dernière ligne permet la selection des employé ayant une fiche d'evaluation valide
			select_structure = select_structure.replaceAll("#id_compagne", "'"+id_compagne+"'");




			//System.out.println(select_structure);
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));

					int id_employe =rs.getInt("id_employe") ;
					String poste_travail=rs.getString("intitule_poste");
					String code_poste=rs.getString("code_poste") ; ;
					String nom_employe=rs.getString("nom")+" "+ rs.getString("prenom");
					String famille=rs.getString("famille");
					String code_famille=rs.getString("code_famille");
					int id_planning_evaluation=rs.getInt("id_planning_evaluation");

					if(MapclesnomEmploye.containsKey(nom_employe))
					{
						ArrayList<String> listFamille=MapclesnomEmploye.get(nom_employe).getCode_famille();
						listFamille.add(code_famille);
						MapclesnomEmploye.get(nom_employe).setCode_famille(listFamille);

						ArrayList<String> listLibelleFamille=MapclesnomEmploye.get(nom_employe).getFamille();
						listLibelleFamille.add(famille);
						MapclesnomEmploye.get(nom_employe).setFamille(listLibelleFamille);
					}
					else
					{

						EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
						employesAEvaluerBean.setCode_poste(code_poste);
						employesAEvaluerBean.setId_employe(id_employe);
						employesAEvaluerBean.setNom_employe(nom_employe);
						employesAEvaluerBean.setPoste_travail(poste_travail);
						employesAEvaluerBean.getCode_famille().add(code_famille);
						employesAEvaluerBean.getFamille().add(famille);
						employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
						MapclesnomEmploye.put(nom_employe,employesAEvaluerBean);
					}
					if(Mapclesposte.containsKey(poste_travail))
					{
						HashMap<String, EmployesAEvaluerBean> mapEmploye=Mapclesposte.get(poste_travail);
						if(mapEmploye.containsKey(nom_employe))
						{
							ArrayList<String> listFamille=mapEmploye.get(nom_employe).getCode_famille();
							listFamille.add(code_famille);
							mapEmploye.get(nom_employe).setCode_famille(listFamille);

							ArrayList<String> listLibelleFamille=mapEmploye.get(nom_employe).getFamille();
							listLibelleFamille.add(famille);
							mapEmploye.get(nom_employe).setFamille(listLibelleFamille);
							Mapclesposte.put(poste_travail, mapEmploye);

						}
						else
						{
							EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
							employesAEvaluerBean.setCode_poste(code_poste);
							employesAEvaluerBean.setId_employe(id_employe);
							employesAEvaluerBean.setNom_employe(nom_employe);
							employesAEvaluerBean.setPoste_travail(poste_travail);
							employesAEvaluerBean.getCode_famille().add(code_famille);
							employesAEvaluerBean.getFamille().add(famille);
							employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
							mapEmploye.put(nom_employe,employesAEvaluerBean);
							Mapclesposte.put(poste_travail, mapEmploye);
						}

					}
					else
					{
						HashMap<String, EmployesAEvaluerBean> mapEmploye=new HashMap<String, EmployesAEvaluerBean>();
						EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
						employesAEvaluerBean.setCode_poste(code_poste);
						employesAEvaluerBean.setId_employe(id_employe);
						employesAEvaluerBean.setNom_employe(nom_employe);
						employesAEvaluerBean.setPoste_travail(poste_travail);
						employesAEvaluerBean.getCode_famille().add(code_famille);
						employesAEvaluerBean.getFamille().add(famille);
						employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
						mapEmploye.put(nom_employe,employesAEvaluerBean);
						Mapclesposte.put(poste_travail, mapEmploye);
					}			


				}
				//				else {
				//					return listEmployesAEvaluerBean;
				//				}
				listEmployesAEvaluerBean.setMapclesnomEmploye(MapclesnomEmploye);
				listEmployesAEvaluerBean.setMapclesposte(Mapclesposte);
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

		return listEmployesAEvaluerBean;
	}

	public MapEmployesAEvaluerBean getListTousEmployesvalue(int id_compagne)
	{
		MapEmployesAEvaluerBean listEmployesAEvaluerBean=new MapEmployesAEvaluerBean();
		HashMap<String, EmployesAEvaluerBean> MapclesnomEmploye=listEmployesAEvaluerBean.getMapclesnomEmploye();
		HashMap<String, HashMap<String,EmployesAEvaluerBean>> Mapclesposte=listEmployesAEvaluerBean.getMapclesposte();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();

			/*String select_structure="select distinct  k.id_planning_evaluation,r.famille, r.code_famille, e.nom, e.prenom , e.id_employe, t.intitule_poste , t.code_poste "+ 
					"from repertoire_competence r, employe e , poste_travail_description t, planning_evaluation k "+ 
					"where e.id_employe in "+   
					" (select distinct v.id_employe "+  
					"from planning_evaluation v , compagne_evaluation n "+  
					" where    v.id_compagne "+ 
					"in ( select distinct id_compagne "+   
					"from compagne_evaluation "+   
					"where  date_fin>=now() and date_debut<=now()))  "+  
					"and e.code_poste=t.code_poste  and e.code_poste =k.code_poste and e.id_employe=k.id_employe "+
					"and e.id_employe  in (select i.id_employe from fiche_validation i where i.fiche_valide='1')  and k.id_planning_evaluation in (select distinct  w.id_planning_evaluation from planning_evaluation w , compagne_evaluation h where  h.date_fin>=now() and h.date_debut<=now() and w.id_compagne=h.id_compagne)";*/



			String select_structure="select distinct  k.id_planning_evaluation,r.famille, r.code_famille, e.nom, e.prenom , e.id_employe, t.intitule_poste , t.code_poste"
					+ " from repertoire_competence r, employe e , poste_travail_description t, planning_evaluation k"
					+ " where e.id_employe in  (select distinct v.id_employe from planning_evaluation v , compagne_evaluation n ,fiche_validation  i"
					+ "                 		 where    v.id_compagne in ( select distinct id_compagne from compagne_evaluation"
					+ "                          where  id_compagne=#id_compagne) and i.fiche_valide='1' and i.id_planning_evaluation=v.id_planning_evaluation)"
					+ " and e.code_poste=t.code_poste  and e.code_poste =k.code_poste and e.id_employe=k.id_employe and k.id_planning_evaluation "
					+ " in (select distinct  w.id_planning_evaluation from planning_evaluation w , compagne_evaluation h where  h.id_compagne=#id_compagne and w.id_compagne=h.id_compagne)";

			select_structure = select_structure.replaceAll("#id_compagne", "'"+id_compagne+"'");


			//la dernière ligne permet la selection des employé ayant une fiche d'evaluation valide




			//System.out.println(select_structure);
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));

					int id_employe =rs.getInt("id_employe") ;
					String poste_travail=rs.getString("intitule_poste");
					String code_poste=rs.getString("code_poste") ; ;
					String nom_employe=rs.getString("nom")+" "+ rs.getString("prenom");
					String famille=rs.getString("famille");
					String code_famille=rs.getString("code_famille");
					int id_planning_evaluation=rs.getInt("id_planning_evaluation");

					if(MapclesnomEmploye.containsKey(nom_employe))
					{
						ArrayList<String> listFamille=MapclesnomEmploye.get(nom_employe).getCode_famille();
						listFamille.add(code_famille);
						MapclesnomEmploye.get(nom_employe).setCode_famille(listFamille);

						ArrayList<String> listLibelleFamille=MapclesnomEmploye.get(nom_employe).getFamille();
						listLibelleFamille.add(famille);
						MapclesnomEmploye.get(nom_employe).setFamille(listLibelleFamille);
					}
					else
					{

						EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
						employesAEvaluerBean.setCode_poste(code_poste);
						employesAEvaluerBean.setId_employe(id_employe);
						employesAEvaluerBean.setNom_employe(nom_employe);
						employesAEvaluerBean.setPoste_travail(poste_travail);
						employesAEvaluerBean.getCode_famille().add(code_famille);
						employesAEvaluerBean.getFamille().add(famille);
						employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
						MapclesnomEmploye.put(nom_employe,employesAEvaluerBean);
					}
					if(Mapclesposte.containsKey(poste_travail))
					{
						HashMap<String, EmployesAEvaluerBean> mapEmploye=Mapclesposte.get(poste_travail);
						if(mapEmploye.containsKey(nom_employe))
						{
							ArrayList<String> listFamille=mapEmploye.get(nom_employe).getCode_famille();
							listFamille.add(code_famille);
							mapEmploye.get(nom_employe).setCode_famille(listFamille);

							ArrayList<String> listLibelleFamille=mapEmploye.get(nom_employe).getFamille();
							listLibelleFamille.add(famille);
							mapEmploye.get(nom_employe).setFamille(listLibelleFamille);
							Mapclesposte.put(poste_travail, mapEmploye);

						}
						else
						{
							EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
							employesAEvaluerBean.setCode_poste(code_poste);
							employesAEvaluerBean.setId_employe(id_employe);
							employesAEvaluerBean.setNom_employe(nom_employe);
							employesAEvaluerBean.setPoste_travail(poste_travail);
							employesAEvaluerBean.getCode_famille().add(code_famille);
							employesAEvaluerBean.getFamille().add(famille);
							employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
							mapEmploye.put(nom_employe,employesAEvaluerBean);
							Mapclesposte.put(poste_travail, mapEmploye);
						}

					}
					else
					{
						HashMap<String, EmployesAEvaluerBean> mapEmploye=new HashMap<String, EmployesAEvaluerBean>();
						EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
						employesAEvaluerBean.setCode_poste(code_poste);
						employesAEvaluerBean.setId_employe(id_employe);
						employesAEvaluerBean.setNom_employe(nom_employe);
						employesAEvaluerBean.setPoste_travail(poste_travail);
						employesAEvaluerBean.getCode_famille().add(code_famille);
						employesAEvaluerBean.getFamille().add(famille);
						employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
						mapEmploye.put(nom_employe,employesAEvaluerBean);
						Mapclesposte.put(poste_travail, mapEmploye);
					}			


				}
				//				else {
				//					return listEmployesAEvaluerBean;
				//				}
				listEmployesAEvaluerBean.setMapclesnomEmploye(MapclesnomEmploye);
				listEmployesAEvaluerBean.setMapclesposte(Mapclesposte);
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

		return listEmployesAEvaluerBean;
	}

	public String getIdCompagne_Codefamille(String id_planning_evaluation,String nomFamille)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		String id_compagne_famille="";
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select distinct p.id_compagne, r.code_famille from repertoire_competence r, planning_evaluation p where p.id_planning_evaluation=#id_planning_evaluation and r.famille=#famille";

			select_structure = select_structure.replaceAll("#id_planning_evaluation", "'"+id_planning_evaluation+"'");
			select_structure = select_structure.replaceAll("#famille", "'"+nomFamille+"'");
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					String id_compagne=rs.getString("id_compagne");
					String code_famille=rs.getString("code_famille");
					id_compagne_famille=id_compagne+"#"	+code_famille;
				}
				//				else {
				//					return id_compagne_famille;
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

		return id_compagne_famille;
	}



	public String enregistrerIMiStatConstructionRequete(String id_compagne,String id_employ,double INiFamille,String code_famille,double statIMI, String requete)
	{

		String insert_structure="INSERT INTO imi_stats (id_compagne,id_employe,moy_par_famille,code_famille, imi) VALUES (#id_compagne,#id_employe,#moy_par_famille,#code_famille, #imi)";
		insert_structure = insert_structure.replaceAll("#id_compagne", id_compagne);
		insert_structure = insert_structure.replaceAll("#id_employe", id_employ);
		insert_structure = insert_structure.replaceAll("#moy_par_famille", INiFamille+"");
		insert_structure = insert_structure.replaceAll("#code_famille", "'"+code_famille+"'");
		insert_structure = insert_structure.replaceAll("#imi", statIMI+"");



		requete=requete+insert_structure+ " ; ";

		//System.out.println(requete);
		return requete;
	}
	public HashMap<String, String> getmaprepCompetenceCodeCompetence() throws ParseException
	{
		HashMap<String, String> mapRepCompetenceCompetence=new HashMap<String, String>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			int retour=compareDate();

			if (retour==0){
				select_structure="select distinct r.id_repertoire_competence, r.code_competence from repertoire_competence r , poste_travail_competence p where r.code_competence=p.code_competence and p.code_poste in(select distinct code_poste from planning_evaluation) ";

			}else{
				select_structure="select distinct r.id_repertoire_competence, r.code_competence from repertoire_competence r , poste_travail_comptence_aptitudeobservable p"
						+ "	where r.code_competence=p.code_competence and p.code_poste in(select distinct code_poste from planning_evaluation)"
						+ " and r.id_repertoire_competence=p.id_repertoire_competence";

			}



			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					String id_repertoire_competence=rs.getString("id_repertoire_competence");
					String code_competence=rs.getString("code_competence");
					mapRepCompetenceCompetence.put(id_repertoire_competence, code_competence);

				}
				//				else {
				//					return mapRepCompetenceCompetence;
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

		return mapRepCompetenceCompetence;
	}

	public HashMap<String, String> getmaprepCompetenceCodeCompetenceRattrapage() throws ParseException
	{
		HashMap<String, String> mapRepCompetenceCompetence=new HashMap<String, String>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			int retour=compareDate();

			if (retour==0){
				select_structure="select distinct r.id_repertoire_competence, r.code_competence from repertoire_competence r , poste_travail_competence p where r.code_competence=p.code_competence and p.code_poste in(select distinct code_poste from planning_evaluation) ";

			}else{
				select_structure="select distinct r.id_repertoire_competence, r.code_competence from repertoire_competence r , poste_travail_comptence_aptitudeobservable p"
						+ "	where r.code_competence=p.code_competence and p.code_poste in(select distinct code_poste from planning_evaluation)"
						+ " and r.id_repertoire_competence=p.id_repertoire_competence";

			}



			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					String id_repertoire_competence=rs.getString("id_repertoire_competence");
					String code_competence=rs.getString("code_competence");
					mapRepCompetenceCompetence.put(id_repertoire_competence, code_competence);

				}
				//				else {
				//					return mapRepCompetenceCompetence;
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

		return mapRepCompetenceCompetence;
	}

	public boolean insertImiCompetenceStat(String requete)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();//connectToEntrepriseDBMulti();
		Statement stmt=null;
		boolean resultat;

		try 
		{
			stmt = (Statement) conn.createStatement();						
			stmt.execute(requete);

			//stmt.close();conn.close();
			resultat=true;
		} 
		catch ( SQLException e ) {
			resultat=false;

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
		return resultat;
	}
	//TODO prendre en compte le paramettre idCompagne;
	public String getMonEvaluateur(int idEmploye, String idCompagne)
	{
		String resultat="";

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;

		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select * from employe where id_employe in (select id_evaluateur from planning_evaluation where id_employe=#id_employe and id_compagne=#id_compagne)";

			select_structure = select_structure.replaceAll("#id_employe", "'"+idEmploye+"'");
			select_structure = select_structure.replaceAll("#id_compagne", "'"+idCompagne+"'");

			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					String nomEvaluateur=rs.getString("nom");
					String prenomEvaluateur=rs.getString("prenom");
					resultat= nomEvaluateur+ " "+prenomEvaluateur;

				}
				else {
					{	//return false;
						resultat="";
					}
				}

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
		return resultat;
	}

	// recuperer la  date debut du compagne selectionnée
	public String getDateFinLastCompagne()
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		String date_fin="";
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select date_debut from compagne_evaluation where id_compagne=(select max(id_compagne) from compagne_evaluation)";

			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{

					date_fin=rs.getString("date_debut");



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
		return date_fin;
	}


	public int compareDate() throws ParseException  {
		String dateString1 = "2015-12-01"; //1er octobre 2015
		//String dateString2 = "05-13-2012";
		int retour=0;

		String date_fin=getDateFinLastCompagne();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Date date1 = format.parse(dateString1);
		Date date2 = format.parse(date_fin);

		if (date2.compareTo(date1) <= 0) {
			//System.out.println("dateString1 is an earlier date than dateString2");
			//utiliser l'ancienne table competence_poste_travail
			retour=0;
		}else
			//utiliser l'ancienne table competence_poste_travail
			retour=1;
		return retour;
	}

	//


	

}
