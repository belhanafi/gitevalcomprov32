package compagne.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import administration.bean.CotationBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import common.CreateDatabaseCon;
import compagne.bean.EmployesAEvaluerBean;
import compagne.bean.FicheEvaluationBean;
import compagne.bean.MapEmployesAEvaluerBean;

public class FicheEvaluationModel {
	
	/**
	 * cett eméthode permet de récuperer l'id_employ associé à l'id compte
	 * @return
	 */
	
	public int getIdEmploye(int id_compte)
	{
		int id_employe=-1;
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT id_employe  FROM employe where id_compte=#id_compte ";
			
			select_structure = select_structure.replaceAll("#id_compte", "'"+id_compte+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return id_employe;
		
	}
	
	public boolean getValiditeFiche(int id_employe)
	{
		boolean ficheValide=false;
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="SELECT fiche_valide  FROM fiche_validation where id_employe=#id_employe  ";
			
			String select_structure="SELECT fiche_valide, max(id_fiche_valide)  FROM fiche_validation where id_employe=#id_employe group by fiche_valide, id_fiche_valide ";
			
			select_structure = select_structure.replaceAll("#id_employe", "'"+id_employe+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
				else {
					return ficheValide;
				}
				
			}
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return ficheValide; 
	}
	/**
	 * cette méthode permet de récuperer les informations associes aux employés à évaluer
	 */

	public MapEmployesAEvaluerBean getListEmployesAEvaluer(int id_evaluateur)
	{
		MapEmployesAEvaluerBean listEmployesAEvaluerBean=new MapEmployesAEvaluerBean();
		HashMap<String, EmployesAEvaluerBean> MapclesnomEmploye=listEmployesAEvaluerBean.getMapclesnomEmploye();
		HashMap<String, HashMap<String,EmployesAEvaluerBean>> Mapclesposte=listEmployesAEvaluerBean.getMapclesposte();
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="select e.nom, e.prenom , p.intitule_poste from employe e , poste_travail_description p where id_employe in (select id_employe  from planning_evaluation where id_evaluateur='2' ) and e.code_poste=p.code_poste";
//			String select_structure="select distinct r.famille, r.code_famille, e.nom, e.prenom , p.intitule_poste from repertoire_competence r, employe e , poste_travail_description p where id_employe in (select id_employe  from planning_evaluation where id_evaluateur='2' ) and e.code_poste=p.code_poste and code_competence in (select code_competence from poste_travail_competence where code_poste='P002')";
//			select distinct r.famille, r.code_famille, e.nom, e.prenom , t.intitule_poste 
//			from repertoire_competence r, employe e , poste_travail_description t 
//			where id_employe in 
//			    (select distinct v.id_employe  
//			        from planning_evaluation v , compagne_evaluation n 
//			        where v.id_evaluateur='2'  and v.id_compagne in
//			                        ( select distinct id_compagne 
//			                            from compagne_evaluation  
//			                            where  date_fin>=now() and date_debut<=now()))
//			         and e.code_poste=t.code_poste and code_competence in 
//			                        (select distinct code_competence from poste_travail_competence where code_poste='P002')

			//String select_structure="select distinct r.famille, r.code_famille, e.nom, e.prenom , e.id_employe, t.intitule_poste , t.code_poste from repertoire_competence r, employe e , poste_travail_description t where id_employe in  (select distinct v.id_employe from planning_evaluation v , compagne_evaluation n where v.id_evaluateur=#id_evaluateur  and v.id_compagne in ( select distinct id_compagne  from compagne_evaluation  where  date_fin>=now() and date_debut<=now()))  and e.code_poste=t.code_poste ";
			
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
			"and e.id_employe not in (select i.id_employe from fiche_validation i where i.fiche_valide='1')  and k.id_planning_evaluation in (select distinct  w.id_planning_evaluation from planning_evaluation w , compagne_evaluation h where  h.date_fin>=now() and h.date_debut<=now() and w.id_compagne=h.id_compagne)";
			
			//la dernière ligne permet la selection des employé ayant une fiche d'evaluation non encore valide
			
			select_structure = select_structure.replaceAll("#id_evaluateur", "'"+id_evaluateur+"'");
			
			
			//System.out.println(select_structure);
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
				else {
					return listEmployesAEvaluerBean;
				}
				listEmployesAEvaluerBean.setMapclesnomEmploye(MapclesnomEmploye);
				listEmployesAEvaluerBean.setMapclesposte(Mapclesposte);
			}
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return listEmployesAEvaluerBean;
	}
	/**
	 * Cette méthode récupère les informations relatifs à une fiche d'evaluation qu'on doit afficher avant l'evaluation d'un employé
	 */
	
	public HashMap<String, ArrayList<FicheEvaluationBean>> getInfosFicheEvaluationparPoste()
	{
		HashMap<String, ArrayList<FicheEvaluationBean>> mapPosteTravailFiche=new HashMap<String, ArrayList<FicheEvaluationBean>>();
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select distinct r.famille,  p.code_poste, r.id_repertoire_competence, r.code_competence, r.libelle_competence , r.definition_competence, r.aptitude_observable from repertoire_competence r , poste_travail_competence p where r.code_competence=p.code_competence and p.code_poste in(select distinct code_poste from planning_evaluation) ";
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return mapPosteTravailFiche;
	}
	
	public HashMap<String, String > getPosteTravailDescription()
	{
		HashMap<String, String> mapcode_descriptionPoste=new HashMap<String, String>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT code_poste, sommaire_poste  FROM poste_travail_description  ";
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String code_poste=rs.getString("code_poste");
					String description_poste=rs.getString("sommaire_poste");
					mapcode_descriptionPoste.put(code_poste, description_poste);
	
				}
				else {
					return mapcode_descriptionPoste;
				}
				
			}
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return mapcode_descriptionPoste;
	}
	public ArrayList <CotationBean> getCotations()
	{
		ArrayList <CotationBean>listCotation=new ArrayList<CotationBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT id_cotation, label_cotation, definition_cotation, valeur_cotation  FROM cotation_competence ";
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
				else {
					return listCotation;
				}
				
			}
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
		return listCotation;
	}
	public void updateMultiQuery(String requete)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDBMulti();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			
			 stmt.execute(requete);
			 
			 conn.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();

			// TODO Auto-generated catch block
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				
				e1.printStackTrace();
				//return false;
			}
			
			
			
		}

	
	}
	
	public String updateFicheEvalutionConstructionRequete(String id_repertoire_competence,String id_employe,String id_planning_evaluation,String id_cotation, String requete)
	{

			
			String insert_structure="INSERT INTO fiche_evaluation (id_planning_evaluation,id_repertoire_competence,id_cotation,id_employe) VALUES (#id_planning_evaluation,#id_repertoire_competence,#id_cotation,#id_employe)";
			insert_structure = insert_structure.replaceAll("#id_planning_evaluation", id_planning_evaluation);
			insert_structure = insert_structure.replaceAll("#id_repertoire_competence", id_repertoire_competence);
			insert_structure = insert_structure.replaceAll("#id_cotation", id_cotation);
			insert_structure = insert_structure.replaceAll("#id_employe", id_employe);
			
			requete=requete+ insert_structure+ " ; ";
			System.out.println(requete);
			return requete;
	
	}
	public void validerFicheEvaluation(String id_planning_evaluation, String id_employe)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String insert_structure="INSERT INTO fiche_validation (id_planning_evaluation,id_employe, fiche_valide) VALUES (#id_planning_evaluation,#id_employe,1)";
			insert_structure = insert_structure.replaceAll("#id_planning_evaluation", id_planning_evaluation);

			insert_structure = insert_structure.replaceAll("#id_employe", id_employe);
			
			
			 stmt.execute(insert_structure);
			 
			 conn.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();

			// TODO Auto-generated catch block
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				
				e1.printStackTrace();
				//return false;
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
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT e.nom, e.prenom , p.intitule_poste FROM employe e, poste_travail_description p where e.id_employe=#id_employe and e.code_poste=p.code_poste";
			
			select_structure = select_structure.replaceAll("#id_employe", ""+id_employe);
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					nom_utilisateur=rs.getString("nom")+" "+ rs.getString("prenom") + "#"+rs.getString("intitule_poste");
					
				}
				else {
					return nom_utilisateur;
				}
				
			}
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return nom_utilisateur;
	}
	
	public HashMap <String, ArrayList<FicheEvaluationBean>> getMaFicheEvaluaton(int id_employe)
	{
		
		HashMap <String, ArrayList<FicheEvaluationBean>> mapFamilleFicheEvaluation=new HashMap <String, ArrayList<FicheEvaluationBean>>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="select DISTINCT r.famille , r.libelle_competence, r.aptitude_observable, f.id_cotation from repertoire_competence r, fiche_evaluation f where f.id_employe=#id_employe and r.id_repertoire_competence=f.id_repertoire_competence";
			//String select_structure="select DISTINCT r.famille , r.libelle_competence, r.aptitude_observable, f.id_cotation, max(p.date_evaluation) from repertoire_competence r, fiche_evaluation f , planning_evaluation p where f.id_employe=#id_employe and r.id_repertoire_competence=f.id_repertoire_competence group by  r.famille , r.libelle_competence, r.aptitude_observable, f.id_cotation ";
			
			String select_structure="select DISTINCT c.compagne_type, r.famille , r.libelle_competence, r.aptitude_observable, f.id_cotation, max(p.date_evaluation) from compagne_type c , repertoire_competence r, fiche_evaluation f , planning_evaluation p where f.id_employe=#id_employe and r.id_repertoire_competence=f.id_repertoire_competence and f.id_planning_evaluation=p.id_planning_evaluation and p.id_compagne=c.id_compagne_type group by  c.compagne_type,r.famille , r.libelle_competence, r.aptitude_observable, f.id_cotation ";
			select_structure = select_structure.replaceAll("#id_employe", "'"+id_employe+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
					
					FicheEvaluationBean fiche=new FicheEvaluationBean();
					fiche.setAptitude_observable(aptitude_observable);
					fiche.setLibelle_competence(libelle_competence);
					fiche.setNiveau_maitrise(new Integer(id_cotation));
					fiche.setDate_evaluation(date_evaluation);
					fiche.setCompagne_type(compagne_type);
					
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
				else {
					return mapFamilleFicheEvaluation;
				}
				
			}
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return mapFamilleFicheEvaluation;
		  
		
	}
	
	public ArrayList <String> getFamilleAssociePoste(String code_poste)
	{
		ArrayList <String> listeFamille= new ArrayList<String>();
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select distinct  g.famille from repertoire_competence g , poste_travail_competence w , poste_travail_description a where g.code_competence=w.code_competence and w.code_poste=#code_poste";
			
			select_structure = select_structure.replaceAll("#code_poste", "'"+code_poste+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					listeFamille.add(rs.getString("famille"));
	
				}
				else {
					return listeFamille;
				}
				
			}
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
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
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
				else {
					return listEmployesAEvaluerBean;
				}
				listEmployesAEvaluerBean.setMapclesnomEmploye(MapclesnomEmploye);
				listEmployesAEvaluerBean.setMapclesposte(Mapclesposte);
			}
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return listEmployesAEvaluerBean;
	}
	
	
	public MapEmployesAEvaluerBean getListTousEmployesvalue()
	{
		MapEmployesAEvaluerBean listEmployesAEvaluerBean=new MapEmployesAEvaluerBean();
		HashMap<String, EmployesAEvaluerBean> MapclesnomEmploye=listEmployesAEvaluerBean.getMapclesnomEmploye();
		HashMap<String, HashMap<String,EmployesAEvaluerBean>> Mapclesposte=listEmployesAEvaluerBean.getMapclesposte();
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			
			String select_structure="select distinct  k.id_planning_evaluation,r.famille, r.code_famille, e.nom, e.prenom , e.id_employe, t.intitule_poste , t.code_poste "+ 
			"from repertoire_competence r, employe e , poste_travail_description t, planning_evaluation k "+ 
			"where e.id_employe in "+   
			   " (select distinct v.id_employe "+  
			        "from planning_evaluation v , compagne_evaluation n "+  
			           " where    v.id_compagne "+ 
			            "in ( select distinct id_compagne "+   
			                    "from compagne_evaluation "+   
			                    "where  date_fin>=now() and date_debut<=now()))  "+  
			"and e.code_poste=t.code_poste  and e.code_poste =k.code_poste and e.id_employe=k.id_employe "+
			"and e.id_employe  in (select i.id_employe from fiche_validation i where i.fiche_valide='1')  and k.id_planning_evaluation in (select distinct  w.id_planning_evaluation from planning_evaluation w , compagne_evaluation h where  h.date_fin>=now() and h.date_debut<=now() and w.id_compagne=h.id_compagne)";
			
			//la dernière ligne permet la selection des employé ayant une fiche d'evaluation valide
			
			
			
			
			//System.out.println(select_structure);
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
				else {
					return listEmployesAEvaluerBean;
				}
				listEmployesAEvaluerBean.setMapclesnomEmploye(MapclesnomEmploye);
				listEmployesAEvaluerBean.setMapclesposte(Mapclesposte);
			}
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return listEmployesAEvaluerBean;
	}
	
	public String getIdCompagne_Codefamille(String id_planning_evaluation,String nomFamille)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		String id_compagne_famille="";
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select distinct p.id_compagne, r.code_famille from repertoire_competence r, planning_evaluation p where p.id_planning_evaluation=#id_planning_evaluation and r.famille=#famille";
			
			select_structure = select_structure.replaceAll("#id_planning_evaluation", "'"+id_planning_evaluation+"'");
			select_structure = select_structure.replaceAll("#famille", "'"+nomFamille+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					String id_compagne=rs.getString("id_compagne");
					String code_famille=rs.getString("code_famille");
					id_compagne_famille=id_compagne+"#"	+code_famille;
				}
				else {
					return id_compagne_famille;
				}
				
			}
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
		
		System.out.println(requete);
		return requete;
	}
	public HashMap<String, String> getmaprepCompetenceCodeCompetence()
	{
		HashMap<String, String> mapRepCompetenceCompetence=new HashMap<String, String>();
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select distinct r.id_repertoire_competence, r.code_competence from repertoire_competence r , poste_travail_competence p where r.code_competence=p.code_competence and p.code_poste in(select distinct code_poste from planning_evaluation) ";
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					String id_repertoire_competence=rs.getString("id_repertoire_competence");
					String code_competence=rs.getString("code_competence");
					mapRepCompetenceCompetence.put(id_repertoire_competence, code_competence);
	
				}
				else {
					return mapRepCompetenceCompetence;
				}
				
			}
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return mapRepCompetenceCompetence;
	}
	
	public void insertImiCompetenceStat(String requete)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDBMulti();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();						
			 stmt.execute(requete);
			 
			 conn.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();

			// TODO Auto-generated catch block
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				
				e1.printStackTrace();
				//return false;
			}
			
			
			
		}
	}
}
