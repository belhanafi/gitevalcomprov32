package kpi.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import kpi.bean.FicheIndivInfoGenBean;
import kpi.bean.FicheIndivPersDevBean;
import kpi.bean.ListAdminRHBean;
import kpi.bean.MoyCompetenceBean;
import kpi.bean.SuiviActionDevBean;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import Statistique.bean.StatMoyFamillePosteBean;
import common.ApplicationSession;
import common.CreateDatabaseCon;
import common.InitContext;


public class FicheIndividuelleModel {


	/**
	 * retourne  stats par tanches ages rapport BIRT
	 * @return
	 */


	public List getInfoGeneralEmploye() throws SQLException
	{

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		int id_employe=applicationSession.getId_employe();
		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();

		ArrayList<FicheIndivInfoGenBean>   liststatbean = new ArrayList<FicheIndivInfoGenBean>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {



				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();



					sql_query="select c.login matricule,e.nom,e.prenom, id_employe,date_naissance,date_recrutement ,concat(d.niv_for_libelle,'-',libelle_diplome) as libelle_formation,p.intitule_poste, email,"

					+ " t.structure_ent libelle_structure,sexe_lbl,type_contrat_lbl,case    when length(trim(libelle_direction))=0 then 'DIR NON RENSEIGNEE'"
					+ " else libelle_direction end as libelle_direction,e.evolution_carriere,e.antecedent_disciplinaire,csp"
					+ " from "+entry.getKey()+"."+"employe e  ,"+entry.getKey()+"."+"sexe s, "+entry.getKey()+"."+"type_contrat t,"+entry.getKey()+"."+"poste_travail_description p,"+entry.getKey()+"."+"formation f,common_evalcom.compte c  ,"
					+ " "+entry.getKey()+"."+"def_niv_formation d,(select code_structure, structure_ent,libelle_direction from ("
					+ " select code_structure,libelle_section structure_ent,libelle_direction  from "+entry.getKey()+"."+"structure_entreprise  where libelle_section is  not null"
					+ " and  libelle_section !='null' and  libelle_section !=''"
					+ " union"
					+ " select code_structure,libelle_service structure_ent,libelle_direction  from "+entry.getKey()+"."+"structure_entreprise"
					+ " where libelle_service is  not null"
					+ " and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_departement structure_ent,libelle_direction  from "+entry.getKey()+"."+"structure_entreprise"
					+ " where libelle_departement is  not null"
					+ "	and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0 "
					+ " union"
					+ " select code_structure,libelle_sous_direction structure_ent,libelle_direction  from "+entry.getKey()+"."+"structure_entreprise "
					+ " where libelle_sous_direction is  not null"
					+ " and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_unite structure_ent,libelle_direction  from "+entry.getKey()+"."+"structure_entreprise"
					+ " where libelle_unite is  not null"
					+ " and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ " and length(libelle_service)=0 and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_direction structure_ent,libelle_direction  from "+entry.getKey()+"."+"structure_entreprise"
					+ " where libelle_direction is  not null and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ " and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise  )t"
					+ " where e.code_poste=p.code_poste and e.code_formation=f.code_formation"
					+ " and e.code_structure=t.code_structure and   e.id_compte=c.id_compte and   d.niv_for_id=f.niv_for_id and  e.code_contrat=t.code_contrat and e.code_sexe=s.code_sexe and e.id_employe=#id_employe and c.id_compte=e.id_compte";

					sql_query = sql_query.replaceAll("#id_employe",  "'"+id_employe+"'");

				}
			}


			//System.out.println(select_structure);

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				FicheIndivInfoGenBean bean=new FicheIndivInfoGenBean();

				//bean.setId_compte((rs.getInt("id_compte")));
				bean.setId_employe((rs.getInt("id_employe")));	
				bean.setNom(rs.getString("nom"));
				bean.setPrenom(rs.getString("prenom"));
				bean.setDate_naissance((rs.getDate("date_naissance")));
				bean.setDate_recrutement((rs.getDate("date_recrutement")));
				bean.setLibelle_formation(rs.getString("libelle_formation"));
				bean.setIntitule_poste(rs.getString("intitule_poste"));
				//bean.setEmail(rs.getString("email"));
				//bean.setEst_evaluateur(rs.getString("est_evaluateur"));
				//bean.setEst_responsable_rh(rs.getString("est_responsable_rh"));
				bean.setLibelle_structure(rs.getString("libelle_structure"));
				bean.setSexe(rs.getString("sexe_lbl"));
				bean.setType_contrat(rs.getString("type_contrat_lbl"));
				bean.setMatricule(rs.getString("matricule"));
				bean.setLibelle_direction(rs.getString("libelle_direction"));

				bean.setAntecedent_disciplinaire(rs.getString("antecedent_disciplinaire"));
				bean.setEvolution_carriere(rs.getString("evolution_carriere"));
				bean.setCsp(rs.getString("csp"));

				liststatbean.add(bean);
			}



		} catch ( SQLException e ) {
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
		return liststatbean;


	}

	public List getListPosteMoyFam() throws SQLException
	{

		//ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		//int id_employe=applicationSession.getId_employe();
		//int idcompagne=applicationSession.getId_compagne();
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		int id_employe=applicationSession.getId_employe();
		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		ArrayList<StatMoyFamillePosteBean> listmoyfam = new ArrayList<StatMoyFamillePosteBean>();
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {



				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();

					sql_query="select r.famille,round(moy_par_famille,2) moy_par_famille,round(imi,2) imi from "+entry.getKey()+"."+"imi_stats i, "+entry.getKey()+"."+"repertoire_competence r"
							+ " where r.code_famille=i.code_famille and id_employe=#id_employe and id_compagne=#id_compagne group by r.famille";

					sql_query = sql_query.replaceAll("#id_employe", "'"+id_employe+"'");
					sql_query = sql_query.replaceAll("#id_compagne", "'"+idcompagne+"'");

				}
			}
			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				StatMoyFamillePosteBean bean=new StatMoyFamillePosteBean();
				bean.setFamille((rs.getString("famille")));	
				bean.setMoy_famille((rs.getFloat("moy_par_famille")));
				bean.setImg((rs.getFloat("imi")));
				//bean.setIntitule_poste((rs.getString("intitule_poste")));
				listmoyfam.add(bean);

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

		return listmoyfam;
	}




	public List getMoyCompetencePoste() throws SQLException
	{

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		int id_employe=applicationSession.getId_employe();
		//int idcompagne=applicationSession.getId_compagne();
		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		ArrayList<MoyCompetenceBean> listmoyfam = new ArrayList<MoyCompetenceBean>();
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();


			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {



				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();



					sql_query="select  r.famille,r.libelle_competence,round(moy_competence,2) moy_competence from "+entry.getKey()+"."+"imi_competence_stat i, "+entry.getKey()+"."+"repertoire_competence r"
							+ " where r.code_famille=i.code_famille and r.code_competence=i.code_competence and id_employe=#id_employe and id_compagne=#id_compagne"
							+ " group by r.famille,r.libelle_competence";


					sql_query = sql_query.replaceAll("#id_employe", "'"+id_employe+"'");
					sql_query = sql_query.replaceAll("#id_compagne", "'"+idcompagne+"'");
				}
			}



			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				MoyCompetenceBean bean=new MoyCompetenceBean();
				bean.setFamille((rs.getString("famille")));	
				bean.setCompetence((rs.getString("libelle_competence")));	
				bean.setMoy_competence((rs.getFloat("moy_competence")));

				listmoyfam.add(bean);

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

		return listmoyfam;
	}


	public List getActionDevEmploye() throws SQLException
	{

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		int id_employe=applicationSession.getId_employe();

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ArrayList<FicheIndivPersDevBean> listmoyfam = new ArrayList<FicheIndivPersDevBean>();
		ResultSet rs=null;
		String sql_query="";

		try 
		{
			stmt = (Statement) conn.createStatement();




			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {

				int map_size=listDb.entrySet().size();

				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();

					sql_query="select g.libelleechelle,a.libelle_action_formation,libelle_action_ori_prof,a.libelle_action_disipline,"
							+ " a.libelle_action_mobilite,CASE WHEN e.proposee='O' THEN 'OUI' ELSE 'NON' END as proposee,CASE WHEN  e.validee='O' THEN 'OUI' ELSE 'NON' END as validee,CASE WHEN   e.programmee='O' THEN 'OUI' ELSE 'NON' END as programmee,CASE WHEN  e.realisee='O' THEN 'OUI' ELSE 'NON' END as realisee"
							+ " from "+entry.getKey()+"."+"actionsdev_employe  e, "+entry.getKey()+"."+"actions_developpement a , "
							+  entry.getKey()+"."+"echelle_maitrise_img g where a.id_action=e.id_action"
							+ "  and a.id_echelle=g.id_echelle and e.id_employe=#id_employe and e.id_compagne=#id_compagne"
							+ " and validee='O' order by 1";


					sql_query = sql_query.replaceAll("#id_employe", "'"+id_employe+"'");
					sql_query = sql_query.replaceAll("#id_compagne", "'"+idcompagne+"'");

				}
			}


			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				FicheIndivPersDevBean bean=new FicheIndivPersDevBean();
				bean.setLibelleechelle((rs.getString("libelleechelle")));	
				bean.setLibelle_action_formation(rs.getString("libelle_action_formation"));	

				bean.setLibelle_action_ori_prof(rs.getString("libelle_action_ori_prof"));	
				bean.setLibelle_action_disipline(rs.getString("libelle_action_disipline"));	
				bean.setLibelle_action_mobilite(rs.getString("libelle_action_mobilite"));	

				bean.setProposee(rs.getString("proposee"));	
				bean.setValidee(rs.getString("validee"));	
				bean.setProgrammee(rs.getString("programmee"));	
				bean.setRealisee(rs.getString("realisee"));	

				listmoyfam.add(bean);

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

		return listmoyfam;
	}



	public List getActionDevPoste() throws SQLException
	{


		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		HashMap<String, HashMap<String, Integer>> listDb=applicationSession.getListDb();
		String code_poste=applicationSession.getCode_poste();

		//code_poste="P172";
		//idcompagne=7;

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ArrayList<FicheIndivPersDevBean> listmoyfam = new ArrayList<FicheIndivPersDevBean>();
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			for (Entry<String, HashMap<String, Integer>> entry : listDb.entrySet()) {



				for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
					String vague = pair.getKey();
					Integer idcompagne = pair.getValue();

					sql_query="select   nbeffectif, g.libelleechelle,"
							+ " a.libelle_action_formation,libelle_action_ori_prof,a.libelle_action_disipline,"
							+ " a.libelle_action_mobilite,p.proposee,p.validee,p.programmee,p.realisee,intitule_poste"
							+ " from "+entry.getKey()+"."+"actionsdev_poste  p,"+entry.getKey()+"."+"actions_developpement a , "+entry.getKey()+"."+"echelle_maitrise_img g, "+entry.getKey()+"."+"poste_travail_description n"
							+ " where a.id_action=p.id_action and a.id_echelle=g.id_echelle"
							+ " and g.id_echelle=1 and p.code_poste=#code_poste and p.id_compagne=#id_compagne and n.code_poste=p.code_poste"
							+ " union"
							+ " select   nbeffectif, g.libelleechelle,"
							+ " a.libelle_action_formation,libelle_action_ori_prof,a.libelle_action_disipline,"
							+ " a.libelle_action_mobilite,p.proposee,p.validee,p.programmee,p.realisee,intitule_poste"
							+ " from "+entry.getKey()+"."+"actionsdev_poste  p,"+entry.getKey()+"."+"actions_developpement a , "+entry.getKey()+"."+"echelle_maitrise_img g,"+entry.getKey()+"."+"poste_travail_description n"
							+ " where a.id_action=p.id_action and a.id_echelle=g.id_echelle and g.id_echelle=2 and p.code_poste=#code_poste and p.id_compagne=#id_compagne and n.code_poste=p.code_poste"
							+ " union"
							+ " select   nbeffectif, g.libelleechelle,"
							+ " a.libelle_action_formation,libelle_action_ori_prof,a.libelle_action_disipline,"
							+ " a.libelle_action_mobilite,p.proposee,p.validee,p.programmee,p.realisee,intitule_poste"
							+ " from "+entry.getKey()+"."+"actionsdev_poste  p,"+entry.getKey()+"."+"actions_developpement a , "+entry.getKey()+"."+"echelle_maitrise_img g, "+entry.getKey()+"."+"poste_travail_description n"
							+ " where a.id_action=p.id_action and a.id_echelle=g.id_echelle"
							+ "  and g.id_echelle=3 and p.code_poste=#code_poste and p.id_compagne=#id_compagne and n.code_poste=p.code_poste";	




					sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
					sql_query = sql_query.replaceAll("#id_compagne", "'"+idcompagne+"'");

				}
			}


			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				FicheIndivPersDevBean bean=new FicheIndivPersDevBean();
				bean.setLibelleechelle((rs.getString("libelleechelle")));	
				bean.setLibelle_action_formation(rs.getString("libelle_action_formation"));	

				bean.setLibelle_action_ori_prof(rs.getString("libelle_action_ori_prof"));	
				bean.setLibelle_action_disipline(rs.getString("libelle_action_disipline"));	
				bean.setLibelle_action_mobilite(rs.getString("libelle_action_mobilite"));	

				bean.setProposee(rs.getString("proposee"));	
				bean.setValidee(rs.getString("validee"));	
				bean.setProgrammee(rs.getString("programmee"));	
				bean.setRealisee(rs.getString("realisee"));
				bean.setNbeffectif((rs.getInt("nbeffectif")));
				bean.setLibelle_poste(rs.getString("intitule_poste"));



				listmoyfam.add(bean);

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

		return listmoyfam;
	}

	public List getActionDevProgress(String code_poste,int id_compagne) throws SQLException
	{

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ArrayList<SuiviActionDevBean> listmoyfam = new ArrayList<SuiviActionDevBean>();
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();


			sql_query="select c.login,m.id_employe,concat(m.nom,concat(' ',m.prenom)) evalue,p.intitule_poste,case    when length(trim(libelle_direction))=0 then 'DIR NON RENSEIGNEE'"
					+ " else libelle_direction end as libelle_direction,round((sum(taux)*100)/ sum(total),0) pourcentage"
					+ " from ( select e.id_employe,count(e.realisee) taux, 0 total from actionsdev_employe e,employe p where e.realisee='O'"
					+ "  and p.code_poste=#code_poste and e.id_compagne=#id_compagne and p.id_employe=e.id_employe"
					+ "  group by e.id_employe"
					+ " union"
					+ " select e.id_employe, 0 taux ,count(e.id_employe) total  from actionsdev_employe e,employe p where"
					+ " p.code_poste=#code_poste and e.id_compagne=#id_compagne  and p.id_employe=e.id_employe group by e.id_employe) t, employe m, poste_travail_description p ,common_evalcom.compte c,structure_entreprise s"
					+ " where t.id_employe=m.id_employe and p.code_poste=m.code_poste and m.id_compte=c.id_compte and s.code_structure=m.code_structure group by 1,2";



			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
			sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");



			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				SuiviActionDevBean bean=new SuiviActionDevBean();
				bean.setLogin(rs.getString("login"));
				bean.setEvalue(rs.getString("evalue"));
				//bean.setLibelle_poste(rs.getString("intitule_poste"));
				bean.setPourcentage(rs.getInt("pourcentage"));
				bean.setLibelle_direction(rs.getString("libelle_direction"));

				listmoyfam.add(bean);

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

		return listmoyfam;
	}

	public void sendAlertEvaluateur(List<SuiviActionDevBean>  listEvalue ,List<ListAdminRHBean> listAdmin) throws SQLException, InterruptedException{
		final InitContext intctx = new InitContext();
		intctx.loadProperties();
		Properties props = new Properties();
		props.setProperty(intctx.getServer(), "localhost");
		props.put("mail.smtp.host", intctx.getHost());
		props.put("mail.smtp.user", intctx.getUser());
		props.put("mail.smtp.password", intctx.getPassword());
		props.put("mail.smtp.port", intctx.getPort());

		Session session=Session.getDefaultInstance(props);
		//Iterator itr=getListAdmin().iterator();
		Iterator itr=listAdmin.iterator();
		ListAdminRHBean cpb=null;

		try {


			while(itr.hasNext()){
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(intctx.getFrom()));

				String result="";
				cpb=(ListAdminRHBean) itr.next();


				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(cpb.getEmail()));
				message.setSubject(intctx.getActionDev_alert());

				String monmessage="<html> <body> 	<P>"+" Madame/Monsieur :  "+cpb.getNomcomplet()+"</P>" +
						"<P>"+" Ci-dessous l'�tat d'avancement des actions de d�veloppement r�alis�es pour les �valu�s suivants: </P>" +			             
						" <TABLE BORDER=10 >  <TR> <TH align='center'> Nom & Pr�nom Evalue </TH>"+
						"<TH align='center'> Taux de r�alisation des actions</TH></TR>";

				for (SuiviActionDevBean elt : listEvalue) {
					int  statut=Integer.valueOf(elt.getPourcentage());
					String coulour="";
					if (statut == 100){

						coulour="'#00FF00'";
					}else if (statut> 50 && statut<100){
						coulour="'#F0700F'";

					}else{
						coulour="'#FF0000'";
					}
					result="<TR><TD ALIGN='center'>"+ elt.getEvalue()+"</TD>"+
							"<TD  ALIGN='center'  style='font-weight:bold' BGCOLOR="+coulour+">"+ statut+"</TD></TR>";


					monmessage=monmessage+result;
				}



				monmessage=monmessage+" </TABLE> <P>"+"Cordialement"+	"</P>"+"<P>"+"Administrateur"+	"</P> </body></html>";
				//System.out.println(monmessage);
				StringBuilder sb = new StringBuilder();
				sb.append(monmessage);
				message.setContent(sb.toString(), "text/html");
				//System.out.println(message.toString());

				//Transport.send(message);
				sb= new StringBuilder();
				monmessage="";

			}





		}catch (MessagingException e) {
			Messagebox.show("Email non transmis � l'�valuateur : "+cpb.getNomcomplet(), "Information",Messagebox.OK, Messagebox.INFORMATION);
			System.out.println("Email non transmis � l'�valuateur : "+cpb.getNomcomplet());
			//insertAlertEmail(Integer.valueOf(idcompagne),cpb.getId_employe());

		}




	}


	/**
	 * retourne  stats par tanches ages rapport BIRT
	 * @return
	 */


	public List getListAdmin() throws SQLException
	{

		ArrayList<ListAdminRHBean>   liststatbean = new ArrayList<ListAdminRHBean>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();
			sql_query="select concat(nom,concat(' ',prenom)) nomcomplet, email from employe where est_responsable_rh='Y'";


			//System.out.println(select_structure);

			rs = (ResultSet) stmt.executeQuery(sql_query);

			while(rs.next()){

				ListAdminRHBean bean=new ListAdminRHBean();

				bean.setEmail(rs.getString("email"));
				bean.setNomcomplet(rs.getString("nomcomplet"));

				liststatbean.add(bean);
			}



		} catch ( SQLException e ) {
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
		return liststatbean;


	}

	public ArrayList<SuiviActionDevBean> exportRapport(int id_compagne) throws SQLException
	{

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ArrayList<SuiviActionDevBean> listmoyfam = new ArrayList<SuiviActionDevBean>();
		ResultSet rs=null;
		String sql_query="";
		try 
		{
			stmt = (Statement) conn.createStatement();


			//			sql_query="select c.login,m.id_employe,concat(m.nom,concat(' ',m.prenom)) evalue,p.intitule_poste,case    when length(trim(libelle_direction))=0 then 'DIR NON RENSEIGNEE'"
			//					+ " else libelle_direction end as libelle_direction,round((sum(taux)*100)/ sum(total),0) pourcentage"
			//					+ " from ( select e.id_employe,count(e.realisee) taux, 0 total from actionsdev_employe e,employe p where e.realisee='O'"
			//					+ "  and p.code_poste=#code_poste and e.id_compagne=#id_compagne and p.id_employe=e.id_employe"
			//					+ "  group by e.id_employe"
			//					+ " union"
			//					+ " select e.id_employe, 0 taux ,count(e.id_employe) total  from actionsdev_employe e,employe p where"
			//					+ " p.code_poste=#code_poste and e.id_compagne=#id_compagne  and p.id_employe=e.id_employe group by e.id_employe) t, employe m, poste_travail_description p ,common_evalcom.compte c,structure_entreprise s"
			//					+ " where t.id_employe=m.id_employe and p.code_poste=m.code_poste and m.id_compte=c.id_compte and s.code_structure=m.code_structure group by 1,2";


			/*sql_query="select  (select  concat(nom,concat(' ',prenom)) from employe e where id_employe in (select distinct id_evaluateur from planning_evaluation p where p.id_employe=m.id_employe)) evaluateur,id_action,c.login,m.id_employe,concat(m.nom,concat(' ',m.prenom)) evalue,libelle_action_formation,"
					+ " libelle_action_ori_prof , libelle_action_disipline , libelle_action_mobilite,p.intitule_poste,structure_ent,case    when length(trim(libelle_direction))=0 then 'DIR NON RENSEIGNEE'"
					+ " else libelle_direction end as libelle_direction,case    when sum(taux) >0 then 'OUI' ELSE 'NON' end  progres"
					+ " from ( select e.id_action,e.id_employe,count(e.realisee) taux, 0 total,libelle_action_formation, libelle_action_ori_prof , libelle_action_disipline , libelle_action_mobilite"
					+ " from actionsdev_employe e,employe p ,actions_developpement v  where e.realisee='O'   and e.id_compagne=#id_compagne and p.id_employe=e.id_employe and e.id_action=v.id_action  group by e.id_employe,e.id_action"
					+ " union"
					+ " select e.id_action,e.id_employe, 0 taux ,count(e.id_employe) total,libelle_action_formation, libelle_action_ori_prof , libelle_action_disipline , libelle_action_mobilite"
					+ " from actionsdev_employe e,employe p,actions_developpement v  where   e.id_compagne=#id_compagne  and p.id_employe=e.id_employe  and e.id_action=v.id_action group by e.id_employe,e.id_action) t,"
					+ " employe m, poste_travail_description p ,common_evalcom.compte c,"
					+ "  ("
					+ "    select libelle_direction,code_structure, structure_ent from ("
					+ "           select libelle_direction,code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
					+ "           and  libelle_section !='null' and  libelle_section !=''"
					+ "           union"
					+ "          select libelle_direction,code_structure,libelle_service structure_ent from structure_entreprise"
					+ "          where libelle_service is  not null and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
					+ "          union"
					+ "          select libelle_direction,code_structure,libelle_departement structure_ent from structure_entreprise"
					+ "          where libelle_departement is  not null and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
					+ "          union"
					+ "          select libelle_direction,code_structure,libelle_sous_direction structure_ent from structure_entreprise"
					+ "          where libelle_sous_direction is  not null and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
					+ " 		 union"
					+ " 		 select libelle_direction,code_structure,libelle_unite structure_ent from structure_entreprise where libelle_unite is  not null and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "          and length(libelle_service)=0 and  length(libelle_section) =0"
					+ " 		 union"
					+ " 		 select libelle_direction,code_structure,libelle_direction structure_ent from structure_entreprise"
					+ "			 where libelle_direction is  not null and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "			 and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise )aggreg"
					+ "			 where t.id_employe=m.id_employe and p.code_poste=m.code_poste and m.id_compte=c.id_compte and aggreg.code_structure=m.code_structure   group by 2,3  order by 5";*/



			sql_query="select  id_action,c.login,m.id_employe,concat(m.nom,concat(' ',m.prenom)) evalue,date_recrutement ddr, round(imi,2) imi,libelle_action_formation,"
					+ "  libelle_action_ori_prof , libelle_action_disipline , libelle_action_mobilite,p.intitule_poste,structure_ent,"
					+ "  case    when length(trim(libelle_direction))=0 then 'DIR NON RENSEIGNEE'"
					+ "  else libelle_direction end as libelle_direction,case    when (sum(realisee)) >0 then 'OUI' ELSE 'NON' end  realisee,"
					+ "                                              case    when (sum(proposee)) >0 then 'OUI' ELSE 'NON' end  proposee,"
					+ "                                              case    when (sum(programmee)) >0 then 'OUI' ELSE 'NON' end  programmee,"
					+ "                                              case    when (sum(validee)) >0 then 'OUI' ELSE 'NON' end  validee"
					+ " from ("
					+ "  select e.id_action,e.id_employe,case when e.proposee='O' then 1 else 0 end proposee ,case when e.validee='O' then 1 else 0 end validee, case when e.programmee='O' then 1 else 0 end programmee, case when e.realisee='O' then 1 else 0 end realisee, libelle_action_formation, libelle_action_ori_prof , libelle_action_disipline , libelle_action_mobilite"
					+ "        from actionsdev_employe e,employe p ,actions_developpement v  where  e.id_compagne=#id_compagne and p.id_employe=e.id_employe and e.id_action=v.id_action"
					+ "	union"
					+ " select e.id_action,e.id_employe,case when e.proposee='O' then 1 else 0 end proposee ,case when e.validee='O' then 1 else 0 end validee, case when e.programmee='O' then 1 else 0 end programmee, case when e.realisee='O' then 1 else 0 end realisee, libelle_action_formation, libelle_action_ori_prof , libelle_action_disipline , libelle_action_mobilite"
					+ "       from actionsdev_employe e,employe p ,actions_developpement v  where  e.id_compagne=#id_compagne and p.id_employe=e.id_employe and e.id_action=v.id_action"
					+ " union"
					+ " select e.id_action,e.id_employe,case when e.proposee='O' then 1 else 0 end proposee ,case when e.validee='O' then 1 else 0 end validee, case when e.programmee='O' then 1 else 0 end programmee, case when e.realisee='O' then 1 else 0 end realisee,libelle_action_formation, libelle_action_ori_prof , libelle_action_disipline , libelle_action_mobilite"
					+ "      from actionsdev_employe e,employe p,actions_developpement v  where   e.id_compagne=#id_compagne  and p.id_employe=e.id_employe  and e.id_action=v.id_action ) t,"
					+ "	 employe m, poste_travail_description p ,common_evalcom.compte c,"
					+ " ("
					+ "	    select libelle_direction,code_structure, structure_ent from ("
					+ "			          select libelle_direction,code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
					+ "   			           and  libelle_section !='null' and  libelle_section !=''"
					+ " 		          union"
					+ "			          select libelle_direction,code_structure,libelle_service structure_ent from structure_entreprise"
					+ "				           where libelle_service is  not null and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
					+ " 		          union"
					+ "			          select libelle_direction,code_structure,libelle_departement structure_ent from structure_entreprise"
					+ "   		              where libelle_departement is  not null and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
					+ "			          union"
					+ " 		          select libelle_direction,code_structure,libelle_sous_direction structure_ent from structure_entreprise"
					+ "			             where libelle_sous_direction is  not null and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
					+ "  		 		 union"
					+ "			 		 select libelle_direction,code_structure,libelle_unite structure_ent from structure_entreprise where libelle_unite is  not null and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "				          and length(libelle_service)=0 and  length(libelle_section) =0"
					+ "			 		 union"
					+ "			 		 select libelle_direction,code_structure,libelle_direction structure_ent from structure_entreprise"
					+ " 					 where libelle_direction is  not null and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "							 and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise )aggreg, imi_stats x"
					+ "							 where t.id_employe=m.id_employe and p.code_poste=m.code_poste"
					+ "                          and m.id_compte=c.id_compte and aggreg.code_structure=m.code_structure and x.id_employe=t.id_employe"
					+ "                          group by 1,2  order by 4";



			sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");



			rs = (ResultSet) stmt.executeQuery(sql_query);
			String login_save="";
			String login_new="";
			double nb_realsee=0;
			double nb_proposee=0;
			double d = 2.34568;
		     DecimalFormat f = new DecimalFormat("##.00");


			while(rs.next()){


				SuiviActionDevBean bean=new SuiviActionDevBean();
				login_new=rs.getString("login");
				bean.setLogin(login_new);

				if (!login_save.equalsIgnoreCase(login_new)){

					if (nb_proposee >0 ){

						double taux= 100 * nb_realsee / nb_proposee;
						int taille_liste=listmoyfam.size();					
						for  (int i=taille_liste-11;i <=taille_liste-1;i++){
							listmoyfam.get(i).setProgres(f.format(taux));
						}
						nb_realsee=0;
						nb_proposee=0;
						
						

					}

					login_save=login_new;
				}

				if (rs.getString("realisee").equalsIgnoreCase("OUI") )nb_realsee=nb_realsee+1;
				if (rs.getString("proposee").equalsIgnoreCase("OUI") ) nb_proposee=nb_proposee+1;


				bean.setEvalue(rs.getString("evalue"));
				bean.setDdr(rs.getString("ddr"));

				bean.setLibelle_direction(rs.getString("libelle_direction"));
				bean.setLibelle_action_disipline(rs.getString("libelle_action_disipline"));
				bean.setLibelle_action_formation(rs.getString("libelle_action_formation"));
				bean.setLibelle_action_mobilite(rs.getString("libelle_action_mobilite"));
				bean.setLibelle_action_ori_prof(rs.getString("libelle_action_ori_prof"));
				bean.setPoste_travail(rs.getString("intitule_poste"));
				bean.setStructure_ent(rs.getString("structure_ent"));
				bean.setImi(f.format(rs.getDouble("imi")));
				bean.setProposee(rs.getString("proposee"));
				bean.setValidee(rs.getString("validee"));
				bean.setProgrammee(rs.getString("programmee"));
				bean.setRealisee(rs.getString("realisee"));
				bean.setProgres("0");
				listmoyfam.add(bean);

			}
			//stmt.close();conn.close();
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

		return listmoyfam;
	}

}
