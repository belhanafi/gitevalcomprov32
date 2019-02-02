package kpi.model;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.rowset.serial.SerialBlob;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.zkoss.zul.Messagebox;

import kpi.bean.ActionDevelopmentBean;
import kpi.bean.ActionDevelopmentMetierBean;
import kpi.bean.ActionFormationBean;
import kpi.bean.EchelleMaitrise;
import common.ApplicationFacade;
import common.CreateDatabaseCon;
import compagne.bean.MixIdealBean;

public class CorrectionPosteModel {
	/**
	 * cette m�thode retrourne le contenu de la table echelle_maitrise_img
	 * @return
	 */
	public ArrayList<EchelleMaitrise> getListeEchelleMaitrise(HashMap<String, HashMap<String, Integer>> listDb)	{

		ArrayList<EchelleMaitrise> resultat= new ArrayList<EchelleMaitrise>();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;

		ResultSet rs=null;
		String query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();

					query="select  distinct e.id_echelle,e.libelleechelle  from "+entry.getKey()+"."+"echelle_maitrise_img e";

					break;

				}
				break;

			}




			rs = (ResultSet) stmt.executeQuery(query);


			while(rs.next()){
				EchelleMaitrise echelleMaitrise=new EchelleMaitrise(rs.getString("libelleechelle"),rs.getString("id_echelle"));
				resultat.add(echelleMaitrise);
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

		return resultat;
	}


	//	public HashMap<String, HashMap<String, ActionDevelopmentBean>> getDevelopmentAction(HashMap<String, HashMap<String, Integer>> listDb){
	//		
	//		HashMap<String, HashMap<String, ActionDevelopmentBean>> resultat= new HashMap<String, HashMap<String, ActionDevelopmentBean>>();
	//
	//		
	//		CreateDatabaseCon dbcon=new CreateDatabaseCon();
	//		Connection conn=(Connection) dbcon.connectToPrincipalDB();
	//		Statement stmt = null;
	//
	//		
	//		
	//
	//		ResultSet rs=null;
	//		String query="";
	//		try 
	//		{
	//			stmt = (Statement) conn.createStatement();
	//
	//			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {
	//
	//				int map_size=listDb.entrySet().size();
	//
	//				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
	//					String vague = pair.getKey();
	//					Integer idcompagne = pair.getValue();
	//
	//					query="select  distinct e.id_action,e.id_echelle, e.libelle_action_formation, e.libelle_action_ori_prof, e.libelle_action_disipline, e.libelle_action_mobilite  from "
	//					+entry.getKey()+"."+"actions_developpement e ";
	//							
	//					break;
	//
	//				}
	//				break;
	//
	//			}
	//
	//
	//
	//
	//			rs = (ResultSet) stmt.executeQuery(query);
	//
	//
	//			while(rs.next()){
	//				ActionDevelopmentBean actionDevelopment =new ActionDevelopmentBean();
	//				actionDevelopment.setIdAction(Integer.toString(rs.getInt("id_action")));
	//				actionDevelopment.setIdAction(Integer.toString(rs.getInt("id_echelle")));
	//				actionDevelopment.setLibelleFormation(rs.getString("libelle_action_formation"));
	//				actionDevelopment.setLibelleOriProf(rs.getString("libelle_action_ori_prof"));
	//				actionDevelopment.setLibelleDiscipline(rs.getString("libelle_action_disipline"));
	//				actionDevelopment.setLibelleMobilite(rs.getString("libelle_action_mobilite"));
	//
	//
	//				
	//				if(resultat.containsKey(actionDevelopment.getIdEchelle())){
	//					
	//					resultat.get(actionDevelopment.getIdEchelle()).put(actionDevelopment.getIdAction(), actionDevelopment);
	//				}else{
	//					//on ne peut pas avoir deux actionDevelopemt ayant lem�me idAction
	//					HashMap<String, ActionDevelopmentBean> mapAction=new HashMap<String, ActionDevelopmentBean>();
	//					mapAction.put(actionDevelopment.getIdAction(), actionDevelopment);
	//					resultat.put(actionDevelopment.getIdEchelle(),mapAction );					
	//					
	//				}
	//			}
	//
	//		}
	//		catch ( SQLException e ) {
	//
	//		} finally {
	//
	//			if ( rs != null ) {
	//				try {
	//					rs.close();
	//				} catch ( SQLException ignore ) {
	//				}
	//			}
	//
	//			if ( stmt != null ) {
	//				try {
	//					stmt.close();
	//				} catch ( SQLException ignore ) {
	//				}
	//			}
	//
	//			if ( conn != null ) {
	//				try {
	//					conn.close();
	//				} catch ( SQLException ignore ) {
	//				}
	//			}
	//		}
	//
	//		return resultat;
	//	}

	public HashMap<String, HashMap<String, ActionDevelopmentMetierBean>> getDevelopmentAction(HashMap<String, HashMap<String, Integer>> listDb, String idCompagne, String codePosteTravail,String idEchelle ){

		HashMap<String, HashMap<String, ActionDevelopmentMetierBean>> resultat= new HashMap<String, HashMap<String, ActionDevelopmentMetierBean>>();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;

		String comparaison="";

		if("1".equals(idEchelle)){
			comparaison=" imi >1 and imi <=3  ";
		}else if(("2".equals(idEchelle))){
			comparaison=" imi >3 and imi <=4   ";
		}else if("3".equals(idEchelle)){
			comparaison=" imi>4 ";
		}

		ResultSet rs=null;
		String query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();

					//1er select pour remonter les employ�s ayant des actions dans la table actionsdev_employe
					//2eme select pour remonter les employ�s ayant des actions dans actionsdev_employe ou  au minimum
					//une action dans la tables actionsdev_poste
					//3eme select s'est pour une fiche metier vierge ou le poste n'existe pa dans la table actionsdev_poste et aucun employ� 
					//ayant le poste en question n'a d'action definie dans la table actionsdev_employe


					query="select count(distinct id_employe) nbeffectif,libelle_action_formation,libelle_action_ori_prof,  libelle_action_disipline,libelle_action_mobilite,"
							+ " e.id_action, p.proposee,p.validee,p.programmee,p.realisee from " +entry.getKey()+".actionsdev_employe  e ," +entry.getKey()+".actions_developpement a left outer join " +entry.getKey()+".actionsdev_poste p on"
							+ " a.id_action=p.id_action  and p.code_poste='"+codePosteTravail+"'" +" where   a.id_echelle="+idEchelle+"  and e.id_compagne="+idCompagne+"    and e.validee='O'"
							+ " and a.id_action=e.id_action and e.id_employe in (select id_employe from " +entry.getKey()+".employe where code_poste='"+codePosteTravail+"') group by e.id_action"
							+"	union"
							+ " select 0 nbeffectif,libelle_action_formation,libelle_action_ori_prof, "
							+ " libelle_action_disipline,libelle_action_mobilite,   a.id_action, p.proposee,p.validee,p.programmee,p.realisee"
							+ "  from " +entry.getKey()+".actions_developpement a , " +entry.getKey()+".actionsdev_poste p where  a.id_action=p.id_action"
							+ " and   a.id_echelle="+idEchelle+"  and p.id_compagne="+idCompagne+"  and p.code_poste='"+codePosteTravail+"' and a.id_action  not in"
							+ " (select distinct id_action from " +entry.getKey()+".actionsdev_employe a, " +entry.getKey()+".employe e where a.id_compagne="+idCompagne
							+ " and validee='O' and a.id_employe=e.id_employe and e.code_poste='"+codePosteTravail+"')"
							+ "	union"
							+ " select 0 nbeffectif,libelle_action_formation,libelle_action_ori_prof,"
							+ " libelle_action_disipline,libelle_action_mobilite,   a.id_action, NULL proposee, NULL validee, NULL programmee, NULL realisee"
							+ " from " +entry.getKey()+".actions_developpement a  where  a.id_action not in (select distinct id_action from " +entry.getKey()+".actionsdev_employe m , " +entry.getKey()+".employe e where m.id_employe=e.id_employe  and  validee='O'  and code_poste='"+codePosteTravail+"')"
							+ " and id_echelle="+idEchelle;




					break;

				}
				break;

			}




			rs = (ResultSet) stmt.executeQuery(query);


			while(rs.next()){
				ActionDevelopmentMetierBean actionDevelopment =new ActionDevelopmentMetierBean();
				actionDevelopment.setIdAction(Integer.toString(rs.getInt("id_action")));
				actionDevelopment.setIdEchelle(idEchelle);
				actionDevelopment.setLibelleFormation(rs.getString("libelle_action_formation"));
				actionDevelopment.setLibelleOriProf(rs.getString("libelle_action_ori_prof"));
				actionDevelopment.setLibelleDiscipline(rs.getString("libelle_action_disipline"));
				actionDevelopment.setLibelleMobilite(rs.getString("libelle_action_mobilite"));
				actionDevelopment.setEffectifs(rs.getString("nbeffectif"));
				actionDevelopment.setPropose(rs.getString("proposee"));
				actionDevelopment.setValidee(rs.getString("validee"));
				actionDevelopment.setProgrammee(rs.getString("programmee"));
				actionDevelopment.setRealisee(rs.getString("realisee"));


				if(resultat.containsKey(actionDevelopment.getIdEchelle())){

					resultat.get(actionDevelopment.getIdEchelle()).put(actionDevelopment.getIdAction(), actionDevelopment);
				}else{
					//on ne peut pas avoir deux actionDevelopemt ayant lem�me idAction
					HashMap<String, ActionDevelopmentMetierBean> mapAction=new HashMap<String, ActionDevelopmentMetierBean>();
					mapAction.put(actionDevelopment.getIdAction(), actionDevelopment);
					resultat.put(actionDevelopment.getIdEchelle(),mapAction );					

				}
			}

		}
		catch ( SQLException e ) {
			System.out.println(e.toString());

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

	public HashMap<String, ActionDevelopmentBean> getDevelopmentEvalue2(HashMap<String, HashMap<String, Integer>> listDb, String idCompagne, String codePosteTravail,String idEchelle, int id_profile, String  login_evaluateur ){

		HashMap<String,  ActionDevelopmentBean> resultat= new HashMap<String,  ActionDevelopmentBean>();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;

		String comparaison="";

		if("1".equals(idEchelle)){
			comparaison=" imi >1 and imi <=3  ";
		}else if(("2".equals(idEchelle))){
			comparaison=" imi >3 and imi <=4   ";
		}else if("3".equals(idEchelle)){
			comparaison=" imi>4 ";
		}

		ResultSet rs=null;
		String query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();

					if (id_profile==3){

						query="select distinct p.id_action, i.id_employe, concat(nom,concat( ' ',prenom))nomcomplet,"
								+ " p.libelle_action_formation,p.libelle_action_ori_prof,  p.libelle_action_disipline,p.libelle_action_mobilite,"
								+ " p.id_action, d.proposee,d.validee,d.programmee,d.realisee"
								+ " from   "+entry.getKey()+".planning_evaluation n, "+entry.getKey()+".imi_stats i,    "+entry.getKey()+".employe e,"+entry.getKey()+".actions_developpement p LEFT OUTER JOIN "+entry.getKey()+".actionsdev_employe d on d.id_action=p.id_action" 
								+ " where id_echelle="+idEchelle+"   and n.id_compagne=i.id_compagne  "
								+ " and  n.id_evaluateur in (select id_employe  from "+entry.getKey()+".employe where id_compte in (select id_compte from common_evalcom.compte where login="+"'"+login_evaluateur+"')) " 
								+ " and e.code_poste=n.code_poste  and   n.id_employe=i.id_employe and i.id_compagne="+idCompagne+" and e.code_poste='"+codePosteTravail+"'  and i.id_employe=e.id_employe and        "+comparaison +" order by 3";   


					}else{
						query="select distinct p.id_action, i.id_employe, concat(nom,concat( ' ',prenom))nomcomplet,"
								+ " p.libelle_action_formation,p.libelle_action_ori_prof,  p.libelle_action_disipline,p.libelle_action_mobilite,"
								+ " p.id_action, d.proposee,d.validee,d.programmee,d.realisee"
								+ " from      "+entry.getKey()+".imi_stats i,    "+entry.getKey()+".employe e,"+entry.getKey()+".actions_developpement p LEFT OUTER JOIN "+entry.getKey()+".actionsdev_employe d on d.id_action=p.id_action"
								+ " where id_echelle="+idEchelle+"   and i.id_compagne="+idCompagne+" and e.code_poste='"+codePosteTravail+"'  and i.id_employe=e.id_employe and        "+comparaison +" order by 3";   


					}




					break;

				}
				break;

			}




			rs = (ResultSet) stmt.executeQuery(query);


			while(rs.next()){
				ActionDevelopmentBean actionDevelopment =new ActionDevelopmentBean();
				actionDevelopment.setIdActionCompPost(Integer.toString(rs.getInt("id_action")));
				actionDevelopment.setIdEchelle(idEchelle);
				actionDevelopment.setLibelleFormation(rs.getString("libelle_action_formation"));
				actionDevelopment.setLibelleOriProf(rs.getString("libelle_action_ori_prof"));
				actionDevelopment.setLibelleDiscipline(rs.getString("libelle_action_disipline"));
				actionDevelopment.setLibelleMobilite(rs.getString("libelle_action_mobilite"));
				actionDevelopment.setPropose(rs.getString("proposee"));
				actionDevelopment.setValidee(rs.getString("validee"));
				actionDevelopment.setProgrammee(rs.getString("programmee"));
				actionDevelopment.setRealisee(rs.getString("realisee"));
				actionDevelopment.setEvalue(rs.getString("nomcomplet"));
				actionDevelopment.setIdEvalue(Integer.toString(rs.getInt("id_employe")));


				resultat.put(actionDevelopment.getIdEvalue(),actionDevelopment );					


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

		return resultat;
	}

	public HashMap<String, ArrayList<ActionDevelopmentBean>> getDevelopmentEvalue(HashMap<String, HashMap<String, Integer>> listDb, String idCompagne, String codePosteTravail,String idEchelle,String idEvalue ){

		HashMap<String, ArrayList<ActionDevelopmentBean>> resultat= new HashMap<String, ArrayList<ActionDevelopmentBean>>();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;

		String comparaison="";

		if("1".equals(idEchelle)){
			comparaison=" imi >1 and imi <=3  ";
		}else if(("2".equals(idEchelle))){
			comparaison=" imi >3 and imi <=4   ";
		}else if("3".equals(idEchelle)){
			comparaison=" imi>4 ";
		}

		ResultSet rs=null;
		String query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {



				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {


					query="select distinct a.id_action, e.id_employe, concat(nom,concat( ' ',prenom))nomcomplet, "
							+ " libelle_action_formation,libelle_action_ori_prof,  libelle_action_disipline,libelle_action_mobilite,"
							+ " a.id_action, d.proposee,d.validee,d.programmee,d.realisee from " +entry.getKey()+".actionsdev_employe d," +entry.getKey()+".actions_developpement a ,"
							+ " " +entry.getKey()+".employe e where d.id_employe='"+idEvalue+"'"+"	and a.id_action=d.id_action  and e.id_employe=d.id_employe "
							+ " and  e.code_poste='"+codePosteTravail+"'  and id_echelle="+idEchelle+" and d.id_compagne= "+idCompagne
							+ " union"
							+ " select distinct a.id_action, e.id_employe, concat(nom,concat( ' ',prenom))nomcomplet,"
							+ " libelle_action_formation,libelle_action_ori_prof,  libelle_action_disipline,libelle_action_mobilite,"
							+ " a.id_action,NULL proposee, NULL validee,NULL programmee, NULL realisee"
							+ " from " +entry.getKey()+".employe e," +entry.getKey()+".actions_developpement a where a.id_action  not in (select id_action from " +entry.getKey()+".actionsdev_employe"
							+ " where id_employe='"+idEvalue+"'" +" and id_compagne="+idCompagne+" )  and id_echelle="+idEchelle+" and"
							+ " e.code_poste='"+codePosteTravail+"'  and  e.id_employe='"+idEvalue+"'"; 



					break;

				}
				break;

			}




			rs = (ResultSet) stmt.executeQuery(query);


			while(rs.next()){
				ActionDevelopmentBean actionDevelopment =new ActionDevelopmentBean();
				actionDevelopment.setIdActionCompPost(Integer.toString(rs.getInt("id_action")));
				actionDevelopment.setIdAction(Integer.toString(rs.getInt("id_action")));
				actionDevelopment.setIdEchelle(idEchelle);
				actionDevelopment.setLibelleFormation(rs.getString("libelle_action_formation"));
				actionDevelopment.setLibelleOriProf(rs.getString("libelle_action_ori_prof"));
				actionDevelopment.setLibelleDiscipline(rs.getString("libelle_action_disipline"));
				actionDevelopment.setLibelleMobilite(rs.getString("libelle_action_mobilite"));
				actionDevelopment.setPropose(rs.getString("proposee"));
				actionDevelopment.setValidee(rs.getString("validee"));
				actionDevelopment.setProgrammee(rs.getString("programmee"));
				actionDevelopment.setRealisee(rs.getString("realisee"));
				actionDevelopment.setEvalue(rs.getString("nomcomplet"));
				actionDevelopment.setIdEvalue(Integer.toString(rs.getInt("id_employe")));

				if(resultat.containsKey(actionDevelopment.getIdEvalue())){

					resultat.get(actionDevelopment.getIdEvalue()).add(actionDevelopment);
				}else{

					ArrayList<ActionDevelopmentBean> listeAction=new ArrayList <ActionDevelopmentBean>();
					listeAction.add(actionDevelopment);
					resultat.put(actionDevelopment.getIdEvalue(),listeAction );					

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

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return resultat;
	}


	public HashMap<String, ArrayList<ActionFormationBean>> getFormationEvalue(HashMap<String, HashMap<String, Integer>> listDb, String idCompagne, String codePosteTravail,String idEchelle,String idEvalue ){

		HashMap<String, ArrayList<ActionFormationBean>> resultat= new HashMap<String, ArrayList<ActionFormationBean>>();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;

		String comparaison="";

		if("1".equals(idEchelle)){
			comparaison=" imi >1 and imi <=3  ";
		}else if(("2".equals(idEchelle))){
			comparaison=" imi >3 and imi <=4   ";
		}else if("3".equals(idEchelle)){
			comparaison=" imi>4 ";
		}

		ResultSet rs=null;
		String query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {



				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {

					query="select distinct  i.id_employe, concat(nom,concat( ' ',prenom))nomcomplet,"
							+ "	d.action_formation, d.proposee,d.validee,d.programmee,d.realisee , d.id_echelle , d.id_type_formation_ext, d.id_compagne, d.id_action_form_employe,  f.domaine_formation, f.thematique_domaine_formation"
							+ " from  "+entry.getKey()+".imi_stats i,   "+entry.getKey()+".employe e,  "+entry.getKey()+".actionsforma_employe d,   "+entry.getKey()+".type_formation_externe f"
							+ " where d.id_echelle="+idEchelle+"   and i.id_compagne="+idCompagne+"  and e.code_poste='"+codePosteTravail+"' "
							+ " and i.id_employe=e.id_employe and d.id_employe=e.id_employe and  e.id_employe='"+idEvalue+"'" +" and     "+comparaison 
							+"and d.id_type_formation_ext=f.id_type_formation_ext";
					/*+ " union"
							+ " select distinct  i.id_employe, concat(nom,concat( ' ',prenom))nomcomplet,'', '','','',''"
							+ " from  "+entry.getKey()+".imi_stats i,    "+entry.getKey()+".employe e"
							+ " where i.id_compagne="+idCompagne+"  and e.code_poste='"+codePosteTravail+"'  and i.id_employe=e.id_employe  and e.id_employe='"+idEvalue+"'" +" and     "+comparaison +" order by 3"; 
					 */

					/*query="select distinct  e.id_employe, concat(nom,concat( ' ',prenom))nomcomplet,"
						+ " action_formation, d.proposee,d.validee,d.programmee,d.realisee from " +entry.getKey()+".actionsforma_employe d,"
						+  entry.getKey()+".employe e where d.id_employe='"+idEvalue+"'" +" and e.id_employe=d.id_employe and  e.code_poste='"+codePosteTravail+"'  and d.id_compagne="+idCompagne;
					 */


					break;

				}
				break;

			}




			rs = (ResultSet) stmt.executeQuery(query);


			while(rs.next()){
				ActionFormationBean actionDevelopment =new ActionFormationBean();

				actionDevelopment.setLibelleFormation(rs.getString("action_formation"));
				String propose=rs.getString("proposee");
				String validee=rs.getString("validee");
				String programmee=rs.getString("programmee");
				String realisee=rs.getString("realisee");
				if("o".equalsIgnoreCase(propose))
					actionDevelopment.setPropose("Oui");
				else
					if("N".equalsIgnoreCase(propose))
						actionDevelopment.setPropose("Non");
					else 
						actionDevelopment.setPropose("");
				//actionDevelopment.setPropose(rs.getString("proposee"));
				if("o".equalsIgnoreCase(validee))
					actionDevelopment.setValidee("Oui");
				else
					if("N".equalsIgnoreCase(validee))
						actionDevelopment.setValidee("Non");
					else 
						actionDevelopment.setValidee("");
				//actionDevelopment.setValidee(rs.getString("validee"));
				if("o".equalsIgnoreCase(programmee))
					actionDevelopment.setProgrammee("Oui");
				else
					if("N".equalsIgnoreCase(programmee))
						actionDevelopment.setProgrammee("Non");
					else 
						actionDevelopment.setProgrammee("");
				//actionDevelopment.setProgrammee(rs.getString("programmee"));
				if("o".equalsIgnoreCase(realisee))
					actionDevelopment.setRealisee("Oui");
				else
					if("N".equalsIgnoreCase(realisee))
						actionDevelopment.setRealisee("Non");
					else 
						actionDevelopment.setRealisee("");
				//actionDevelopment.setRealisee(rs.getString("realisee"));
				actionDevelopment.setEvalue(rs.getString("nomcomplet"));
				actionDevelopment.setIdEvalue(Integer.toString(rs.getInt("id_employe")));
				actionDevelopment.setIdEchelle(Integer.toString(rs.getInt("id_echelle")));
				actionDevelopment.setIdTypeFormationExterne(rs.getString("id_type_formation_ext"));
				actionDevelopment.setIdActionFormEmploye(Integer.toString(rs.getInt("id_action_form_employe")));
				actionDevelopment.setDomaineFormation(rs.getString("domaine_formation").toUpperCase());
				actionDevelopment.setThemeFormation(rs.getString("thematique_domaine_formation").toUpperCase());

				if(resultat.containsKey(actionDevelopment.getIdEvalue())){

					resultat.get(actionDevelopment.getIdEvalue()).add(actionDevelopment);
				}else{

					ArrayList<ActionFormationBean> listeAction=new ArrayList <ActionFormationBean>();
					listeAction.add(actionDevelopment);
					resultat.put(actionDevelopment.getIdEvalue(),listeAction );					

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

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}

		return resultat;
	}


	public HashMap<String, String> getListeSuiviSort(HashMap<String, HashMap<String, Integer>> listDb)	{

		HashMap <String, String> retour=new HashMap<String, String>();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;

		ResultSet rs=null;
		String query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();

					//query="select  distinct e.id_suivi_sort,e.libelle_suivi_sort  from "+entry.getKey()+"."+"suivi_sort e";
					query="select  distinct e.id_suivi_sort,e.libelle_suivi_sort  from "+entry.getKey()+"."+"suivi_sort e where id_suivi_sort not in ('O','X')";		
					break;

				}
				break;

			}




			rs = (ResultSet) stmt.executeQuery(query);


			while(rs.next()){

				retour.put(rs.getString("id_suivi_sort"),rs.getString("libelle_suivi_sort"));
				//
			}
			retour.put(" ", "");
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

		return retour;
	}


	public HashMap<String, String> getListeSuiviSortEvalue(HashMap<String, HashMap<String, Integer>> listDb)	{

		HashMap <String, String> retour=new HashMap<String, String>();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;

		ResultSet rs=null;
		String query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();

					//query="select  distinct e.id_suivi_sort,e.libelle_suivi_sort  from "+entry.getKey()+"."+"suivi_sort e";
					query="select  distinct e.id_suivi_sort,e.libelle_suivi_sort  from "+entry.getKey()+"."+"suivi_sort e where id_suivi_sort in ('O','X')";		
					break;

				}
				break;

			}




			rs = (ResultSet) stmt.executeQuery(query);


			while(rs.next()){

				retour.put(rs.getString("id_suivi_sort"),rs.getString("libelle_suivi_sort"));
				//
			}
			retour.put(" ", "");
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

		return retour;
	}
	public boolean updateActionDevPoste(HashMap<String, HashMap<String, Integer>> listDb,ArrayList<ActionDevelopmentMetierBean> listActionUpdate){
		String requete="";
		boolean resultat=true;

		try {

			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToPrincipalDB();
			PreparedStatement stmtInsert = null;
			PreparedStatement stmtUpdate=null; 

			ResultSet rs=null;
			String query="";
			conn.setAutoCommit(false);

			try 
			{	
				Iterator<ActionDevelopmentMetierBean> iterateur =listActionUpdate.iterator();

				for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

					for (Entry<String, Integer> pair : entry.getValue().entrySet()) {

						stmtUpdate= conn.prepareStatement("UPDATE  "+entry.getKey()+".actionsdev_poste SET proposee=?,validee=?, programmee=?,realisee=?, nbeffectif=? WHERE id_action=?  and id_compagne=? and code_poste=?");
						//stmtUpdate=conn.prepareStatement("delete from "+entry.getKey()+".actionsdev_poste where id_action =? and id_compagne=? and code_poste=?");
						stmtInsert= conn.prepareStatement("INSERT INTO "+entry.getKey()+".actionsdev_poste (id_action,id_compagne,code_poste, proposee, validee, programmee, realisee,nbeffectif) VALUES (?,?,?, ?, ?, ?, ?,?)");

						while(iterateur.hasNext()){
							ActionDevelopmentMetierBean actionDev=iterateur.next();

							stmtUpdate.setString(1, actionDev.getPropose());
							stmtUpdate.setString(2, actionDev.getValidee());
							stmtUpdate.setString(3, actionDev.getProgrammee());
							stmtUpdate.setString(4, actionDev.getRealisee());
							stmtUpdate.setInt(5, new Integer(actionDev.getEffectifs()));
							stmtUpdate.setInt(6,  new Integer(actionDev.getIdAction()));
							stmtUpdate.setInt(7, new Integer(actionDev.getIdCompagne()));
							stmtUpdate.setString(8, actionDev.getCodePosteTravail());



							stmtUpdate.addBatch();

							stmtInsert.setInt(1,  new Integer(actionDev.getIdAction()));
							stmtInsert.setInt(2, new Integer(actionDev.getIdCompagne()));
							stmtInsert.setString(3, actionDev.getCodePosteTravail());
							stmtInsert.setString(4, actionDev.getPropose());
							stmtInsert.setString(5, actionDev.getValidee());
							stmtInsert.setString(6, actionDev.getProgrammee());
							stmtInsert.setString(7, actionDev.getRealisee());
							stmtInsert.setInt(8, new Integer(actionDev.getEffectifs()));

							stmtInsert.addBatch();
						}
						break;

					}
					break;

				}


				int [] updateCounts = stmtUpdate.executeBatch();


				int total_update=processUpdateCounts(updateCounts);


				if(total_update==0){
					stmtInsert.executeBatch();

				}
				conn.commit();

				conn.close();
			} 
			catch ( SQLException e ) {
				System.out.println(requete);
				e.printStackTrace();
				resultat=false;

			} finally {
				if ( stmtInsert != null ) {
					try {
						stmtInsert.close();
					} catch ( SQLException ignore ) {
					}
				}
				if ( stmtUpdate != null ) {
					try {
						stmtUpdate.close();
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





		} catch (Exception e) {
			e.printStackTrace();
			//TODO logger l'exception 
		}

		return resultat;
	}



	//	public boolean updateActionDevEmploye(HashMap<String, HashMap<String, Integer>> listDb, ActionDevelopmentBean actionDev){
	//		String requete="";
	//		boolean resultat=true;
	//
	//		try {
	//
	//			CreateDatabaseCon dbcon=new CreateDatabaseCon();
	//			Connection conn=(Connection) dbcon.connectToPrincipalDB();
	//			PreparedStatement stmtInsert = null;
	//			PreparedStatement stmtUpadte=null; 
	//			
	//			ResultSet rs=null;
	//			String query="";
	//
	//
	//			try 
	//			{	
	//
	//				for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {
	//	
	//					for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
	//
	//						
	//
	//						stmtUpadte= conn.prepareStatement("UPDATE  "+entry.getKey()+".actionsdev_employe SET proposee=?,validee=?, programmee=?,realisee=? WHERE id_action_comp_post=?  and id_employe=?");
	//						stmtUpadte.setString(1, actionDev.getPropose());
	//						stmtUpadte.setString(2, actionDev.getValidee());
	//						stmtUpadte.setString(3, actionDev.getProgrammee());
	//						stmtUpadte.setString(4, actionDev.getRealisee());
	//						
	//						stmtUpadte.setInt(5,  new Integer(actionDev.getIdActionCompPost()));
	//						stmtUpadte.setInt(6, new Integer(actionDev.getIdEvalue()));
	//
	//						
	//
	//						stmtUpadte.execute();
	//						
	//						if(stmtUpadte.getUpdateCount()==0){
	//							stmtInsert= conn.prepareStatement("INSERT INTO "+entry.getKey()+".actionsdev_employe (id_action_comp_post,id_employe, proposee, validee, programmee, realisee) VALUES (?,?, ?, ?, ?, ?)");
	//							
	//							stmtInsert.setInt(1,  new Integer(actionDev.getIdActionCompPost()));
	//							stmtInsert.setInt(2, new Integer(actionDev.getIdEvalue()));
	//							stmtInsert.setString(3, actionDev.getPropose());
	//							stmtInsert.setString(4, actionDev.getValidee());
	//							stmtInsert.setString(5, actionDev.getProgrammee());
	//							stmtInsert.setString(6, actionDev.getRealisee());
	//							
	//							stmtInsert.executeUpdate();
	//						}
	//						
	//						
	//		
	//						break;
	//		
	//						}
	//					break;
	//		
	//				}
	//
	//
	//				
	//
	//
	//				conn.close();
	//				
	//				resultat=true;
	//			} 
	//			catch ( SQLException e ) {
	//				System.out.println(requete);
	//				e.printStackTrace();
	//				resultat=false;
	//
	//			} finally {
	//				if ( stmtUpadte != null ) {
	//					try {
	//						stmtUpadte.close();
	//					} catch ( SQLException ignore ) {
	//					}
	//				}
	//				if ( stmtInsert != null ) {
	//					try {
	//						stmtInsert.close();
	//					} catch ( SQLException ignore ) {
	//					}
	//				}
	//
	//				if ( conn != null ) {
	//					try {
	//						conn.close();
	//					} catch ( SQLException ignore ) {
	//					}
	//				}
	//			}
	//			return resultat;
	//
	//
	//
	//
	//
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			//TODO logger l'exception 
	//		}
	//
	//		return resultat;
	//	}
	//	
	public Boolean updateBatchActionDevEmploye(HashMap<String, HashMap<String, Integer>> listDb,ArrayList<ActionDevelopmentBean> listActionUpdate)
	{
		String requete="";
		boolean resultat=true;

		try {

			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToPrincipalDB();
			PreparedStatement stmtInsert = null;
			PreparedStatement stmtUpadte=null; 

			ResultSet rs=null;
			String query="";

			conn.setAutoCommit(false);
			try 
			{	

				Iterator<ActionDevelopmentBean> iterateur =listActionUpdate.iterator();



				for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

					for (Entry<String, Integer> pair : entry.getValue().entrySet()) {

						stmtUpadte= conn.prepareStatement("UPDATE  "+entry.getKey()+".actionsdev_employe SET proposee=?,validee=?, programmee=?,realisee=? WHERE id_action=?  and id_employe=? and id_compagne=?");
						stmtInsert= conn.prepareStatement("INSERT INTO "+entry.getKey()+".actionsdev_employe (id_action,id_employe, proposee, validee, programmee, realisee,id_compagne) VALUES (?,?, ?, ?, ?, ?,?)");

						while(iterateur.hasNext()){
							ActionDevelopmentBean actionDev=iterateur.next();


							stmtUpadte.setString(1, actionDev.getPropose());
							stmtUpadte.setString(2, actionDev.getValidee());
							stmtUpadte.setString(3, actionDev.getProgrammee());
							stmtUpadte.setString(4, actionDev.getRealisee());




							stmtUpadte.setInt(5,  new Integer(actionDev.getIdActionCompPost()));
							stmtUpadte.setInt(6, new Integer(actionDev.getIdEvalue()));
							stmtUpadte.setString(7, actionDev.getIdCompagne());



							stmtUpadte.addBatch();




							stmtInsert.setInt(1,  new Integer(actionDev.getIdActionCompPost()));
							stmtInsert.setInt(2, new Integer(actionDev.getIdEvalue()));
							stmtInsert.setString(3, actionDev.getPropose());
							stmtInsert.setString(4, actionDev.getValidee());
							stmtInsert.setString(5, actionDev.getProgrammee());
							stmtInsert.setString(6, actionDev.getRealisee());
							stmtInsert.setString(7, actionDev.getIdCompagne());

							stmtInsert.addBatch();

						}
						break;
					}
					break;
				}

				int [] updateCounts = stmtUpadte.executeBatch();


				int total_update=processUpdateCounts(updateCounts);


				if(total_update==0){
					stmtInsert.executeBatch();

				}
				conn.commit();

				conn.close();

				resultat=true;
			} 
			catch ( SQLException e ) {
				try 
				{
					Messagebox.show("La modification n'a pas �t� prise en compte", "Erreur",Messagebox.OK, Messagebox.ERROR);
				} 
				catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
				resultat=false;

			} finally {
				if ( stmtUpadte != null ) {
					try {
						stmtUpadte.close();
					} catch ( SQLException ignore ) {
					}
				}
				if ( stmtInsert != null ) {
					try {
						stmtInsert.close();
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

		} catch (Exception e) {
			e.printStackTrace();
			//TODO logger l'exception 
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


	public HashMap<String, ActionFormationBean> getDevelopmentEvalueForma(HashMap<String, HashMap<String, Integer>> listDb, String idCompagne, String codePosteTravail,String idEchelle, int id_profile, String  login_evaluateur ){

		HashMap<String,  ActionFormationBean> resultat= new HashMap<String,  ActionFormationBean>();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;

		String comparaison="";

		if("1".equals(idEchelle)){
			comparaison=" imi >1 and imi <=3  ";
		}else if(("2".equals(idEchelle))){
			comparaison=" imi >3 and imi <=4   ";
		}else if("3".equals(idEchelle)){
			comparaison=" imi>4 ";
		}

		ResultSet rs=null;
		String query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();

					if (id_profile==3){


						query="select distinct d.id_action, i.id_employe, concat(nom,concat( ' ',prenom))nomcomplet,null action_formation ,"
								+ " d.proposee,d.validee,d.programmee,d.realisee"
								+ "	from "+entry.getKey()+".planning_evaluation n, "+entry.getKey()+".imi_stats i,"+entry.getKey()+".employe e LEFT OUTER JOIN  "+entry.getKey()+".actionsdev_employe d on  d.id_employe=e.id_employe"
								+ " where   n.id_compagne=i.id_compagne	and e.code_poste=n.code_poste  and   n.id_employe=i.id_employe and i.id_compagne="+idCompagne
								+ "	and e.code_poste='"+codePosteTravail+"' "+"  and i.id_employe=e.id_employe and     "+comparaison
								+ "	and  n.id_evaluateur in (select id_employe  from "+entry.getKey()+".employe where id_compte in (select id_compte from common_evalcom.compte where login="+"'"+login_evaluateur+"'))"
								+ " order by 3";
					}else{

						query="select distinct d.id_action, i.id_employe, concat(nom,concat( ' ',prenom))nomcomplet,null action_formation,"
								+ " d.proposee,d.validee,d.programmee,d.realisee"
								+ "	from "+entry.getKey()+".planning_evaluation n, "+entry.getKey()+".imi_stats i,"+entry.getKey()+".employe e LEFT OUTER JOIN  "+entry.getKey()+".actionsdev_employe d on  d.id_employe=e.id_employe"
								+ " where   n.id_compagne=i.id_compagne	and e.code_poste=n.code_poste  and   n.id_employe=i.id_employe and i.id_compagne="+idCompagne
								+ "	and e.code_poste='"+codePosteTravail+"' "+"  and i.id_employe=e.id_employe and     "+comparaison
								+ " order by 3";

					}



					break;

				}
				break;

			}
			rs = (ResultSet) stmt.executeQuery(query);


			while(rs.next()){

				ActionFormationBean actionDevelopment =new ActionFormationBean();
				actionDevelopment.setIdEchelle(idEchelle);
				actionDevelopment.setLibelleFormation(rs.getString("action_formation"));
				actionDevelopment.setPropose(rs.getString("proposee"));
				actionDevelopment.setValidee(rs.getString("validee"));
				actionDevelopment.setProgrammee(rs.getString("programmee"));
				actionDevelopment.setRealisee(rs.getString("realisee"));
				actionDevelopment.setEvalue(rs.getString("nomcomplet"));
				actionDevelopment.setIdEvalue(Integer.toString(rs.getInt("id_employe")));
				resultat.put(actionDevelopment.getIdEvalue(),actionDevelopment );					


			}

		}
		catch ( SQLException e ) {
			
			System.out.println(e.toString());

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


	public HashMap<String, Integer> getComptageActionDevMetier(HashMap<String, HashMap<String, Integer>> listDb, String idCompagne, String codePosteTravail){

		HashMap<String,  Integer> resultat= new HashMap<String,  Integer>();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;



		ResultSet rs=null;
		String query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();


					query="select sum(proposee) proposee,sum(validee) validee,sum(programmee) programmee,sum(realisee) realisee from ("
							+ "  select id_action,count(proposee)proposee,0 validee,0 programmee,0  realisee from "+entry.getKey()+".actionsdev_employe"
							+ "  where proposee='O' and  id_compagne="+idCompagne+" and id_employe in (select id_employe from "+entry.getKey()+".employe where code_poste='"+codePosteTravail+"') group by 1"
							+ "	union"
							+ "	select id_action,0 proposee,count(validee) validee,0 programmee,0 realisee from "+entry.getKey()+".actionsdev_employe"
							+ " where validee='O' and  id_compagne="+idCompagne+" and id_employe in (select id_employe from "+entry.getKey()+".employe where code_poste='"+codePosteTravail+"') group by 1"
							+ " union"
							+ "	select id_action,0 proposee,0 validee,count( programmee) programmee,0 realisee from "+entry.getKey()+".actionsdev_employe"
							+ " where programmee='O' and  id_compagne="+idCompagne+" and id_employe in (select id_employe from "+entry.getKey()+".employe where code_poste='"+codePosteTravail+"') group by 1"
							+ "	union"
							+ "	select id_action,0 proposee,0 validee,0 programmee,count( realisee) realisee from "+entry.getKey()+".actionsdev_employe"
							+ " where realisee='O' and  id_compagne="+idCompagne+" and id_employe in (select id_employe from "+entry.getKey()+".employe where code_poste='"+codePosteTravail+"') group by 1"
							+ "	) tab ";


					break;

				}
				break;

			}
			rs = (ResultSet) stmt.executeQuery(query);


			while(rs.next()){

				resultat.put("proposee",rs.getInt("proposee"));
				resultat.put("validee",rs.getInt("validee"));
				resultat.put("programmee",rs.getInt("programmee"));
				resultat.put("realisee",rs.getInt("realisee"));



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

		return resultat;
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


	public Boolean updateBatchActionDevForma(HashMap<String, HashMap<String, Integer>> listDb,ArrayList<ActionFormationBean> listActionUpdate)
	{
		String requete="";
		boolean resultat=true;

		try {

			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToPrincipalDB();
			PreparedStatement stmtInsert = null;
			PreparedStatement stmtUpadte=null; 

			ResultSet rs=null;
			String query="";

			conn.setAutoCommit(false);
			try 
			{	

				Iterator<ActionFormationBean> iterateur =listActionUpdate.iterator();



				for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

					for (Entry<String, Integer> pair : entry.getValue().entrySet()) {

						stmtUpadte= conn.prepareStatement("UPDATE  "+entry.getKey()+".actionsforma_employe SET proposee=?,validee=?, programmee=?,realisee=? WHERE id_action_form_employe=?  and id_employe=? and id_compagne=?");
						stmtInsert= conn.prepareStatement("INSERT INTO "+entry.getKey()+".actionsforma_employe (id_action_form_employe,id_employe, proposee, validee, programmee, realisee,id_compagne,id_echelle,action_formation, id_type_formation_ext) VALUES (?,?, ?, ?, ?, ?,?,?, ?,?)");

						while(iterateur.hasNext()){
							ActionFormationBean actionDev=iterateur.next();


							stmtUpadte.setString(1, actionDev.getPropose());
							stmtUpadte.setString(2, actionDev.getValidee());
							stmtUpadte.setString(3, actionDev.getProgrammee());
							stmtUpadte.setString(4, actionDev.getRealisee());




							stmtUpadte.setInt(5,  new Integer(actionDev.getIdActionFormEmploye()));
							stmtUpadte.setInt(6, new Integer(actionDev.getIdEvalue()));
							stmtUpadte.setString(7, actionDev.getIdCompagne());



							stmtUpadte.addBatch();




							stmtInsert.setInt(1,  new Integer(actionDev.getIdActionFormEmploye()));
							stmtInsert.setInt(2, new Integer(actionDev.getIdEvalue()));
							stmtInsert.setString(3, actionDev.getPropose());
							stmtInsert.setString(4, actionDev.getValidee());
							stmtInsert.setString(5, actionDev.getProgrammee());
							stmtInsert.setString(6, actionDev.getRealisee());
							stmtInsert.setString(7, actionDev.getIdCompagne());
							stmtInsert.setString(8, actionDev.getIdEchelle());
							stmtInsert.setString(9, actionDev.getLibelleFormation());
							stmtInsert.setString(10, actionDev.getIdTypeFormationExterne());

							stmtInsert.addBatch();

						}
						break;
					}
					break;
				}

				int [] updateCounts = stmtUpadte.executeBatch();


				int total_update=processUpdateCounts(updateCounts);


				if(total_update==0){
					stmtInsert.executeBatch();

				}
				conn.commit();

				conn.close();

				resultat=true;
			} 
			catch ( SQLException e ) {
				try 
				{
					Messagebox.show("La modification n'a pas �t� prise en compte", "Erreur",Messagebox.OK, Messagebox.ERROR);
				} 
				catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
				resultat=false;

			} finally {
				if ( stmtUpadte != null ) {
					try {
						stmtUpadte.close();
					} catch ( SQLException ignore ) {
					}
				}
				if ( stmtInsert != null ) {
					try {
						stmtInsert.close();
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

		} catch (Exception e) {
			e.printStackTrace();
			//TODO logger l'exception 
		}
		return resultat;


	}

	public Boolean addBatchActionForma(HashMap<String, HashMap<String, Integer>> listDb,ActionFormationBean actionFormationBean)
	{

		boolean resultat=true;

		try {

			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToPrincipalDB();
			PreparedStatement stmtInsert = null;

			ResultSet rs=null;
			String query="";

			conn.setAutoCommit(false);
			try 
			{	





				for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

					for (Entry<String, Integer> pair : entry.getValue().entrySet()) {


						stmtInsert= conn.prepareStatement("INSERT INTO "+entry.getKey()+".actionsforma_employe (id_employe, proposee, validee, programmee, realisee,id_compagne,id_echelle,action_formation, id_type_formation_ext) VALUES (?,?, ?, ?, ?, ?,?,?, ?)");

						stmtInsert.setInt(1, new Integer(actionFormationBean.getIdEvalue()));
						stmtInsert.setString(2, getEquivalanceBdd(actionFormationBean.getPropose()));
						stmtInsert.setString(3, getEquivalanceBdd(actionFormationBean.getValidee()));
						stmtInsert.setString(4, getEquivalanceBdd(actionFormationBean.getProgrammee()));
						stmtInsert.setString(5, getEquivalanceBdd(actionFormationBean.getRealisee()));
						stmtInsert.setString(6, actionFormationBean.getIdCompagne());
						stmtInsert.setInt(7, new Integer(actionFormationBean.getIdEchelle()));
						stmtInsert.setString(8, actionFormationBean.getLibelleFormation());
						stmtInsert.setInt(9, new Integer(actionFormationBean.getIdTypeFormationExterne()));



						stmtInsert.addBatch();

						break;
					}
					break;
				}

				int [] updateCounts = stmtInsert.executeBatch();



				conn.commit();

				conn.close();


			} 
			catch ( SQLException e ) {
				try 
				{
					Messagebox.show("La modification n'a pas �t� prise en compte", "Erreur",Messagebox.OK, Messagebox.ERROR);
				} 
				catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
				resultat=false;

			} finally {
				if ( stmtInsert != null ) {
					try {
						stmtInsert.close();
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

		} catch (Exception e) {
			e.printStackTrace();
			//TODO logger l'exception 
		}
		return resultat;


	}

	public Boolean updateBatchActionForma(HashMap<String, HashMap<String, Integer>> listDb,ActionFormationBean actionFormationBean)
	{
		String requete="";
		boolean resultat=true;

		try {

			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToPrincipalDB();
			PreparedStatement stmtInsert = null;
			PreparedStatement stmtUpadte=null; 

			ResultSet rs=null;
			String query="";

			conn.setAutoCommit(false);
			try 
			{	





				for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

					for (Entry<String, Integer> pair : entry.getValue().entrySet()) {

						stmtUpadte= conn.prepareStatement("UPDATE  "+entry.getKey()+".actionsforma_employe SET proposee=?,validee=?, programmee=?,realisee=? , action_formation=? , id_type_formation_ext= ? WHERE id_action_form_employe=?  and id_employe=? and id_compagne=? and id_echelle=?");
						stmtInsert= conn.prepareStatement("INSERT INTO "+entry.getKey()+".actionsforma_employe (id_action_form_employe,id_employe, proposee, validee, programmee, realisee,id_compagne,id_echelle,action_formation, id_type_formation_ext) VALUES (?,?, ?, ?, ?, ?,?,?, ?,?)");

						stmtUpadte.setString(1, getEquivalanceBdd(actionFormationBean.getPropose()));
						stmtUpadte.setString(2, getEquivalanceBdd(actionFormationBean.getValidee()));
						stmtUpadte.setString(3, getEquivalanceBdd(actionFormationBean.getProgrammee()));
						stmtUpadte.setString(4, getEquivalanceBdd(actionFormationBean.getRealisee()));
						stmtUpadte.setString(5, actionFormationBean.getLibelleFormation());
						stmtUpadte.setInt(6, new Integer(actionFormationBean.getIdTypeFormationExterne()));



						stmtUpadte.setInt(7,  new Integer(actionFormationBean.getIdActionFormEmploye()));
						stmtUpadte.setInt(8, new Integer(actionFormationBean.getIdEvalue()));
						stmtUpadte.setString(9, actionFormationBean.getIdCompagne());
						stmtUpadte.setInt(10, new Integer(actionFormationBean.getIdEchelle()));

						stmtUpadte.addBatch();

						break;
					}
					break;
				}

				int [] updateCounts = stmtUpadte.executeBatch();


				int total_update=processUpdateCounts(updateCounts);


				if(total_update==0){
					resultat=false;

				}
				conn.commit();

				conn.close();


			} 
			catch ( SQLException e ) {
				try 
				{
					Messagebox.show("La modification n'a pas �t� prise en compte", "Erreur",Messagebox.OK, Messagebox.ERROR);
				} 
				catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
				resultat=false;

			} finally {
				if ( stmtUpadte != null ) {
					try {
						stmtUpadte.close();
					} catch ( SQLException ignore ) {
					}
				}
				if ( stmtInsert != null ) {
					try {
						stmtInsert.close();
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

		} catch (Exception e) {
			e.printStackTrace();
			//TODO logger l'exception 
		}
		return resultat;


	}


	public Boolean deleteBatchActionForma(HashMap<String, HashMap<String, Integer>> listDb,ActionFormationBean actionFormationBean)
	{
		boolean resultat=true;

		try {

			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToPrincipalDB();
			PreparedStatement stmtDelete = null;


			ResultSet rs=null;
			String query="";

			conn.setAutoCommit(false);
			try 
			{	

				for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

					for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
						stmtDelete= conn.prepareStatement("DELETE FROM  "+entry.getKey()+".actionsforma_employe WHERE id_action_form_employe="+actionFormationBean.getIdActionFormEmploye());

						stmtDelete.addBatch();

						break;
					}
					break;
				}

				int [] updateCounts = stmtDelete.executeBatch();

				conn.commit();

				conn.close();


			} 
			catch ( SQLException e ) {
				try 
				{
					Messagebox.show("La suppression n'a pas �t� prise en compte", "Erreur",Messagebox.OK, Messagebox.ERROR);
				} 
				catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
				resultat=false;

			} finally {
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

		} catch (Exception e) {
			e.printStackTrace();
			//TODO logger l'exception 
		}
		return resultat;


	}

	private String getEquivalanceBdd (String valeur){
		String retour ="N";
		if("Oui".equalsIgnoreCase(valeur)) 
			return "O";
		else
			if("Non".equalsIgnoreCase(valeur))return "N";
			else return "X";

	}



	public HashMap<String, HashMap<String, String>> getListeTypeFotmation(HashMap<String, HashMap<String, Integer>> listDb) throws InterruptedException	{

		HashMap<String, HashMap<String, String>> retour=new HashMap<String, HashMap<String, String>>();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;

		ResultSet rs=null;
		String query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();

					//query="select  distinct e.id_suivi_sort,e.libelle_suivi_sort  from "+entry.getKey()+"."+"suivi_sort e";
					//uery="select  distinct e.id_type_formation_ext,e.libelle_formation_externe  from "+entry.getKey()+"."+"type_formation_externe e";	
					query="select  distinct e.id_type_formation_ext,upper(e.domaine_formation) domaine_formation , upper(e.thematique_domaine_formation)  thematique_domaine_formation  from "+entry.getKey()+"."+"type_formation_externe e order by 2";
					break;

				}
				break;

			}




			rs = (ResultSet) stmt.executeQuery(query);

			//HashMap<String, HashMap<String,String>> mapDomaineThematique =new HashMap<String, HashMap<String,String>>();


			while(rs.next()){

				String domaine_formation=rs.getString("domaine_formation");
				String id_type_formation_ext=rs.getString("id_type_formation_ext");
				String thematique_domaine_formation=rs.getString("thematique_domaine_formation");

				if(retour.containsKey(domaine_formation)){
					HashMap<String,String> mapThematique=retour.get(domaine_formation);
					if(mapThematique.containsKey(id_type_formation_ext)){
						//normalement il est impossible d'avir ce car car id_type_formation_ext est une cl�
						retour.get(domaine_formation).put(id_type_formation_ext, thematique_domaine_formation);
					}else{
						retour.get(domaine_formation).put(id_type_formation_ext, thematique_domaine_formation);
					}

				}else{
					HashMap<String,String> mapThematique=new HashMap<String,String>();

					mapThematique.put(id_type_formation_ext, thematique_domaine_formation);

					retour.put(domaine_formation, mapThematique);
				}
				//retour.put(rs.getString("id_type_formation_ext"),rs.getString("thematique_domaine_formation"));

				//
			}
			//retour.put(" ", "");
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

		return (HashMap)sortByComparatorkey(retour);
	}


	private static Map sortByComparatorkey(Map unsortMap) throws InterruptedException {

		Map sortedMap = new LinkedHashMap();
		try{
			List list = new LinkedList(unsortMap.entrySet());
	
			//sort list based on comparator
			Collections.sort(list, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((Comparable) ((Map.Entry) (o1)).getKey())
							.compareTo(((Map.Entry) (o2)).getKey());
				}
			});
	
			//put sorted list into map again
			
			for (Iterator it = list.iterator(); it.hasNext();) {
				Map.Entry entry = (Map.Entry)it.next();
				sortedMap.put(entry.getKey(), entry.getValue());
			}
			
		}catch (Exception e){
		
				Messagebox.show("Merci de selectionner une compagne! ", "Information",Messagebox.OK, Messagebox.ERROR);
		
		}
		return sortedMap;
		
	}	

	public String getIdThemeFormation(HashMap<String, HashMap<String, Integer>> listDb, String themeFormation, String domaineFormation)	{

		String retour="";


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;

		ResultSet rs=null;
		String query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();					
					query="select  distinct e.id_type_formation_ext from "+entry.getKey()+"."+"type_formation_externe e where upper(thematique_domaine_formation)=upper('"+themeFormation+"') and upper(domaine_formation)=upper('"+domaineFormation+"')";
					break;

				}
				break;

			}




			rs = (ResultSet) stmt.executeQuery(query);

			//HashMap<String, HashMap<String,String>> mapDomaineThematique =new HashMap<String, HashMap<String,String>>();
			
			
			while(rs.next()){
				

				retour=rs.getString("id_type_formation_ext");


				//retour.put(rs.getString("id_type_formation_ext"),rs.getString("thematique_domaine_formation"));

				//
			}
			//retour.put(" ", "");
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

		return retour;
	}
	
}





