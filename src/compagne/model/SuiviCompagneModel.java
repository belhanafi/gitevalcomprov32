package compagne.model;

import java.sql.SQLException;
import java.text.ParseException;
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
import java.util.Properties;
import java.util.TreeMap;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;

import administration.bean.AdministrationLoginBean;
import administration.bean.Compagne;
import administration.bean.CompteBean;
import administration.bean.SelCliBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import common.CreateDatabaseCon;
import common.InitContext;
import common.PwdCrypt;
import compagne.bean.CompagneListBean;
import compagne.bean.EmailEvaluateurBean;
import compagne.bean.SuiviCompStatutFicheBean;
import compagne.bean.SuiviCompagneBean;

public class SuiviCompagneModel {

	private ArrayList<SuiviCompagneBean>  listevaluateur =null; 

	public List uploadListEvaluateur() throws SQLException{


		listevaluateur = new ArrayList<SuiviCompagneBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sqlquery=	"select id_employe,concat (nom ,' ',prenom) as nom_evaluateur ,email, round(sum(nbfichevalide)*100/ sum(totalemploye)) as progress " +
				"from (	select id_evaluateur as evaluateur ,count(r.id_employe) as nbfichevalide,0 as totalemploye from planning_evaluation t ,fiche_validation r" +
				" where t.id_planning_evaluation=r.id_planning_evaluation and t.id_employe=r.id_employe" +
				" and fiche_valide=1 group by id_evaluateur	union select id_evaluateur as evaluateur ,0 as nbfichevalide ,count(t.id_employe)as totalemploye from planning_evaluation t" +
				" group by id_evaluateur) as t2,employe  where id_employe=evaluateur group by 1,2,3";
		//System.out.println(sqlquery);

		try {
			stmt = (Statement) conn.createStatement();


			rs = (ResultSet) stmt.executeQuery(sqlquery);

			while(rs.next()){

				SuiviCompagneBean evalbean=new SuiviCompagneBean();
				evalbean.setEvaluateur(rs.getString("nom_evaluateur"));
				evalbean.setPourcentage(rs.getInt("progress"));
				evalbean.setId_employe(rs.getInt("id_employe"));
				evalbean.setEmail(rs.getString("email"));

				listevaluateur.add(evalbean);


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
		return listevaluateur;



	}
	public HashMap getCompagneList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{ 
			stmt = (Statement) conn.createStatement();
			//String db_list="select id_compagne,concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) as libelle_compagne from compagne_evaluation where now()<=date_fin";
			String db_list="select id_compagne,concat(libelle_compagne,'->', 'Du ',cast(date_debut as char)  ,' Au ',cast(date_fin as char) ) as libelle_compagne from compagne_evaluation order by id_compagne desc";


			rs = (ResultSet) stmt.executeQuery(db_list);


			while(rs.next()){
				map.put( rs.getString("libelle_compagne"),rs.getInt("id_compagne"));
			}
			//map.put("Selectionner Compagne",-1);
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


	public List getCompagneEmployeList(String id_employe ) throws SQLException
	{
		ArrayList<CompagneListBean> listcompagne = new ArrayList<CompagneListBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;

		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select  distinct e.id_compagne ,libelle_compagne,concat (d.nom, '',d.prenom) as evalue," +
					" date_format(r.date_evaluation,'%d-%m-%Y') as date_evaluation,concat (r.heure_debut_evaluation,'-' ,r.heure_fin_evaluation) as heure," +
					" r.lieu,date_format(date_fin,'%d-%m-%Y') as date_fin,compagne_type" +
					" from compagne_evaluation e , compagne_type t,planning_evaluation r ,employe d" +
					" where e.id_compagne_type=t.id_compagne_type and   r.id_compagne=e.id_compagne " +
					" and    r.id_evaluateur =#idemploye and    d.id_employe=r.id_employe";


			sql_query = sql_query.replaceAll("#idemploye", id_employe);
			//System.out.println(sql_query);
			rs = (ResultSet) stmt.executeQuery(sql_query);
			//System.out.println(sql_query);

			while(rs.next()){
				//map.put( rs.getString("libelle_compagne"),rs.getInt("id_compagne"));
				CompagneListBean cbn=new CompagneListBean();
				cbn.setId_compagne(rs.getInt("id_compagne"));
				cbn.setLibelle_compagne(rs.getString("libelle_compagne"));
				cbn.setDate_evaluation(rs.getString("date_evaluation"));
				cbn.setDate_fin(rs.getString("date_fin"));
				cbn.setCompagne_type(rs.getString("compagne_type"));
				cbn.setEvalue(rs.getString("evalue"));
				cbn.setHeure(rs.getString("heure"));
				cbn.setLieu(rs.getString("lieu"));

				listcompagne.add(cbn);
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

		return listcompagne;
	}

	public HashMap getStructEntrepriseList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String db_list=" select distinct v.code_structure,concat_ws (' > ',libelle_division,NULL,libelle_direction,libelle_unite,NULL,libelle_departement,libelle_service, null,libelle_section) as structure" +
					"  from planning_evaluation v, structure_entreprise t where v.code_structure=t.code_structure"; 
			rs = (ResultSet) stmt.executeQuery(db_list);


			while(rs.next()){
				map.put( rs.getString("v.code_structure"),rs.getString("structure"));
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

	public List filtrerListEvaluateur(int id_compagne,String code_structure ) throws SQLException{


		listevaluateur = new ArrayList<SuiviCompagneBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sqlquery="";
		if (code_structure.equalsIgnoreCase("-1")){

			sqlquery="select id_employe,concat (nom ,' ',prenom) as nom_evaluateur , intitule_poste,email,round(sum(nbfichevalide)*100/ sum(totalemploye)) as progress"+
					" from (	select id_evaluateur as evaluateur ,count(r.id_employe) as nbfichevalide,0 as totalemploye from planning_evaluation t ,fiche_validation r" +
					" where t.id_planning_evaluation=r.id_planning_evaluation" +
					" and t.id_employe=r.id_employe  and fiche_valide=1  and  t.id_compagne=#compgane "+
					" group by id_evaluateur   union select id_evaluateur as evaluateur ,0 as nbfichevalide ,count(t.id_employe)as totalemploye" +
					" from planning_evaluation t where   t.id_compagne=#compgane  group by id_evaluateur" +
					" ) as t2,employe e ,poste_travail_description p where e.id_employe=evaluateur and p.code_poste=e.code_poste  group by 1,2,3,4 order by 2";

		}
		else{
			sqlquery="select id_employe,concat (nom ,' ',prenom) as nom_evaluateur , intitule_poste,email,round(sum(nbfichevalide)*100/ sum(totalemploye)) as progress"+
					" from (	select id_evaluateur as evaluateur ,count(r.id_employe) as nbfichevalide,0 as totalemploye from planning_evaluation t ,fiche_validation r" +
					" where t.id_planning_evaluation=r.id_planning_evaluation" +
					" and t.id_employe=r.id_employe  and fiche_valide=1  and  t.id_compagne=#compgane "+
					" group by id_evaluateur   union select id_evaluateur as evaluateur ,0 as nbfichevalide ,count(t.id_employe)as totalemploye" +
					" from planning_evaluation t where   t.id_compagne=#compgane  group by id_evaluateur" +
					" ) as t2,employe e ,poste_travail_description p where e.id_employe=evaluateur and p.code_poste=e.code_poste and e.code_structure=#code_structure group by 1,2,3,4 order by 2";
			sqlquery = sqlquery.replaceAll("#code_structure", "'"+code_structure+"'");
		}


		sqlquery = sqlquery.replaceAll("#compgane", Integer.toString(id_compagne));

		//System.out.println(sqlquery);


		try {
			stmt = (Statement) conn.createStatement();


			rs = (ResultSet) stmt.executeQuery(sqlquery);

			while(rs.next()){

				SuiviCompagneBean evalbean=new SuiviCompagneBean();
				evalbean.setEvaluateur(rs.getString("nom_evaluateur"));
				evalbean.setPoste_travail(rs.getString("intitule_poste"));
				evalbean.setPourcentage(rs.getInt("progress"));
				evalbean.setId_employe(rs.getInt("id_employe"));
				evalbean.setEmail(rs.getString("email"));
				listevaluateur.add(evalbean);


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
		return listevaluateur;

	}	
	public List getEmailEvaluateur(List listemploye) throws SQLException{

		Iterator it = listemploye.iterator();
		List listemail = new ArrayList<EmailEvaluateurBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		String lec="";
		String lec_save="";
		EmailEvaluateurBean cpb;
		EmailEvaluateurBean evalbean=new EmailEvaluateurBean();
		ResultSet rs=null;	
		while (it.hasNext()){
			cpb  = (EmailEvaluateurBean) it.next();
			lec=String.valueOf(cpb.getId_employe());
			lec=','+lec;
			lec_save=lec_save+lec;

		}

		String sqlquery=	"select id_employe,email from employe where id_employe in (#listemploye)";
		sqlquery = sqlquery.replaceAll("#listemploye", lec_save);
		sqlquery = sqlquery.replace("(,","(");


		try {
			stmt = (Statement) conn.createStatement();


			rs = (ResultSet) stmt.executeQuery(sqlquery);

			while(rs.next()){


				evalbean.setEmail(rs.getString("email"));
				evalbean.setId_employe(rs.getInt("id_employe"));
				listemail.add(evalbean);


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
		return listemail;



	}
	public void sendAlertEvaluateur(List recipient,String idcompagne) throws SQLException, InterruptedException{
		final InitContext intctx = new InitContext();
		intctx.loadProperties();
		Properties props = new Properties();
		/*props.put("mail.smtp.host", intctx.getHost());
	props.put("mail.smtp.socketFactory.port", intctx.getPort());
	props.put("mail.smtp.socketFactory.class",
			"javax.net.ssl.SSLSocketFactory");
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.port", intctx.getPort());*/

		//props.setProperty("smtp.airaldgerie.dz", "localhost");
		props.setProperty(intctx.getServer(), "localhost");
		props.put("mail.smtp.host", intctx.getHost());
		props.put("mail.smtp.user", intctx.getUser());
		props.put("mail.smtp.password", intctx.getPassword());
		props.put("mail.smtp.port", intctx.getPort());

		Session session=Session.getDefaultInstance(props);
		List  listcompagne = new ArrayList<SuiviCompStatutFicheBean>();
		SuiviCompStatutFicheBean cmp=new SuiviCompStatutFicheBean();


		Iterator it = recipient.iterator();

		/*Session session = Session.getDefaultInstance(props,
		new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(intctx.getUser(),intctx.getPassword());
			}
		});*/

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(intctx.getFrom()));
			String lec;
			EmailEvaluateurBean cpb=null;
			String result="";





			while (it.hasNext()){

				try{
					cpb  = (EmailEvaluateurBean) it.next();
					message.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(cpb.getEmail()));
					message.setSubject(intctx.getSmtpsubject_alert());
					//listcompagne=getListFicheStatut(String.valueOf(cpb.getId_employe()));
					listcompagne=getListFicheStatut(cpb.getId_employe(),Integer.parseInt(idcompagne));
					//System.out.println("employe>>"+cpb.getId_employe()+"email>>"+cpb.getEmail());
					Iterator itcomp = listcompagne.iterator();


					String monmessage="<html> <body> 	<P>"+" Madame/Monsieur :  "+cpb.getNomevaluateur()+"</P>" +
							"<P>"+" Vous n'avez pas encore finalisé votre compagne d'évaluation. Vous êtes à "+ cpb.getPourcentage()+" % d'état d'avancement."+"</P>" +			             
							" <TABLE BORDER=10 >  <TR> <TH align='center'> Nom & Prénom évalue </TH>"+
							" <TH align='center'> Heure Clôture Evaluation</TH> "+
							" <TH align='center'> Date Clôture Evaluation</TH> "+
							"<TH align='center'> Staut Fiche</TH></TR>";


					while (itcomp.hasNext()){
						cmp=(SuiviCompStatutFicheBean) itcomp.next();
						String statut=cmp.getStatut();
						String coulour="";
						if (statut.equalsIgnoreCase("VALIDEE")){
							coulour="'#00FF00'";
						}else if (statut.equalsIgnoreCase("EN COURS")){
							coulour="'#F0700F'";

						}else{
							coulour="'#FF0000'";
						}
						result="<TR><TD ALIGN='center'>"+ cmp.getEvalue()+"</TD>"+
								"<TD  ALIGN='center'>"+ cmp.getHeure_fin_eval()+"</TD>"+
								"<TD  ALIGN='center'>"+ cmp.getDate_fin_eval()+"</TD>"+
								"<TD  ALIGN='center'  style='font-weight:bold' BGCOLOR="+coulour+">"+ statut+"</TD></TR>";

						//message.setText("Attention vous etes à :  "+cpb.getPourcentage()+" % de l'etat d'avancement sur la "+ cmp.getLibelle_compagne()+
						//	                        ". La date de cloture aura lieu le: "+cmp.getDate_fin()+"\n"+"\n"+intctx.getText());
						monmessage=monmessage+result;
					}

					monmessage=monmessage+" </TABLE> <P>"+"Cordialement"+	"</P>"+"<P>"+"Administrateur"+	"</P> </body></html>";
					//System.out.println(monmessage);
					StringBuilder sb = new StringBuilder();
					sb.append(monmessage);
					message.setContent(sb.toString(), "text/html");

					Transport.send(message);
					sb= new StringBuilder();
					monmessage="";

					insertAlertEmail(Integer.valueOf(idcompagne),cpb.getId_employe());

				}catch (MessagingException e) {
					Messagebox.show("Email non transmis à l'évaluateur : "+cpb.getNomevaluateur(), "Information",Messagebox.OK, Messagebox.INFORMATION);
					System.out.println("Email non transmis à l'évaluateur : "+cpb.getNomevaluateur());
					//insertAlertEmail(Integer.valueOf(idcompagne),cpb.getId_employe());
					
				}


			}



		} catch (MessagingException e) {
			Messagebox.show("Email non transmis. Problème de messagerie. Merci de contacter votre administrateur !", "Information",Messagebox.OK, Messagebox.INFORMATION); 
			throw new RuntimeException(e);
		}
		
	}



	public boolean validerCompagne(int idcompagne) throws ParseException
	{

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		boolean retour=false;

		try 
		{

			stmt = (Statement) conn.createStatement();
			String sql_query="INSERT INTO compagne_validation( id_compagne, compagne_valide)  VALUES(#id_compagne, 1)";
			sql_query = sql_query.replaceAll("#id_compagne", String.valueOf(idcompagne));


			stmt.execute(sql_query);
			retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La compagne  n'a pas été validée", "Erreur",Messagebox.OK, Messagebox.ERROR);
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

	public boolean isCompagneValidated(Integer id_compagne) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		boolean result=false;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select id_compagne_valide from compagne_validation where id_compagne=#id_compagne and compagne_valide=1";
			sql_query = sql_query.replaceAll("#id_compagne", String.valueOf(id_compagne));
			rs = (ResultSet) stmt.executeQuery(sql_query);


			while(rs.next()){
				if (rs.getRow()==1  ) {
					result=true;

				}
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

		return result;


	}

	public boolean calculerIMG(int id_compagne) throws ParseException
	{

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();//connectToEntrepriseDBMulti();
		Statement stmt=null;
		boolean retour=false;
		try 
		{

			stmt = (Statement) conn.createStatement();
			String sql_query="insert into img_stats (id_compagne,code_poste,img) select  id_compagne,code_poste,round(sum(moy_par_famille1)/count(code_famille),2) from(" +
					" select s.id_compagne,e.code_poste,code_famille,sum(moy_par_famille)/count(s.id_employe) as moy_par_famille1" +
					" from imi_stats s ,employe e, fiche_validation f where s.id_employe=e.id_employe and s.id_employe=f.id_employe" +
					" and fiche_valide=1  and s.id_compagne=#id_compagne group by e.code_poste,code_famille) as t2  group by code_poste ";

			String sql_query1="insert into moy_poste_famille_stats (id_compagne,code_poste,code_famille,moy_par_famille)" +
					" select s.id_compagne,e.code_poste,code_famille,round(sum(moy_par_famille)/count(s.id_employe),2) as moy_par_famille1" +
					" from imi_stats s ,employe e, fiche_validation f where s.id_employe=e.id_employe and s.id_employe=f.id_employe" +
					" and fiche_valide=1 and s.id_compagne=#id_compagne group by e.code_poste,code_famille ";

			//		String sql_query2="insert into  moy_poste_competence_stats (id_compagne,code_poste,code_competence,code_famille,moy_par_competence)" +
			//				          " select P.id_compagne,e.code_poste,r.code_competence,code_famille,round (avg( id_cotation),2) " +
			//				          " from fiche_evaluation f ,repertoire_competence r,employe e ,planning_evaluation p" +
			//				          " where f.id_repertoire_competence =r.id_repertoire_competence and    f.id_employe=e.id_employe" +
			//				          " and f.id_planning_evaluation=p.id_planning_evaluation and p.id_compagne=#id_compagne group by P.id_compagne,e.code_poste,code_famille,r.code_competence";
			//				          
			String sql_query2="insert into  moy_poste_competence_stats (id_compagne,code_poste,code_competence,code_famille,moy_par_competence) " +
					"select s.id_compagne,e.code_poste,s.code_competence,s.code_famille,round (avg( moy_competence),2) " +
					"from imi_competence_stat s , employe e " +
					"where s.id_employe=e.id_employe " +
					"and id_compagne=#id_compagne " +
					"group by  s.id_compagne,e.code_poste,s.code_famille,s.code_competence ";


			String sql_query_total = sql_query +" ; "+sql_query1+" ; "+sql_query2+" ;";
			sql_query_total = sql_query_total.replaceAll("#id_compagne", String.valueOf(id_compagne));


			stmt.execute(sql_query_total);
			retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La compagne  n'a pas été validée", "Erreur",Messagebox.OK, Messagebox.ERROR);
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





	/*
	 * remplissage de la list box structure entreprise
	 * @return
	 * @throws SQLException
	 */

	public Map getStructEntList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		ResultSet rs=null;
		try 
		{ 
			stmt = (Statement) conn.createStatement();
			String db_list=		
					"select code_structure, structure_ent from (" +
							" select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null "+
							" and  libelle_section !='null' and  libelle_section !='' " +
							" union " +
							" select code_structure,libelle_service structure_ent from structure_entreprise  " +
							" where libelle_service is  not null" +
							"  and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0   " +
							" union " +
							" select code_structure,libelle_departement structure_ent from structure_entreprise  " + 
							" where libelle_departement is  not null " +
							"  and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0  " +
							" union " +
							" select code_structure,libelle_sous_direction structure_ent from structure_entreprise   " +
							" where libelle_sous_direction is  not null " +
							"  and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0 " +   
							" union  " +
							" select code_structure,libelle_unite structure_ent from structure_entreprise   " +
							" where libelle_unite is  not null " +
							"  and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0  " +
							"  and length(libelle_service)=0 and  length(libelle_section) =0    " +
							" union  " +
							" select code_structure,libelle_direction structure_ent from structure_entreprise   " +
							" where libelle_direction is  not null " +
							"  and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0 " + 
							"  and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise  order by structure_ent ";
			//System.out.println(db_list);
			rs = (ResultSet) stmt.executeQuery(db_list);


			while(rs.next()){
				map.put( rs.getString("structure_ent"),rs.getString("code_structure"));
			}
			//map.put("Tous",-1);
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

		Map<String, String> treeMap = new TreeMap<String, String>(map);
		return  treeMap;
	}

	/***
	 * recupèrer la liste des évalués avec les différents statuts de leur fiche
	 * */
	public List getListFicheStatut(int id_evaluateur,int id_compagne ) throws SQLException
	{

		HashMap map = new HashMap();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		ResultSet rs=null;
		PreparedStatement psmt = null;
		List listefiche = new ArrayList<SuiviCompStatutFicheBean>();

		try 
		{
			String sql_query="select concat(e.nom,concat(' ',e.prenom)) as evalue,e.id_employe,date_fin_evaluation,heure_fin_evaluation,fiche_valide,CASE f.fiche_valide"
					+ " WHEN '1' THEN 'VALIDEE' WHEN '-1' THEN 'EN COURS'  END  as statut "
					+ " from fiche_validation f, planning_evaluation p, employe e"
					+ " where id_evaluateur=? and f.id_employe=p.id_employe and e.id_employe=p.id_employe and p.id_compagne=? and f.id_planning_evaluation=p.id_planning_evaluation"
					+ " union"
					+ " select concat(e.nom,concat(' ',e.prenom)) as evalue,e.id_employe,date_fin_evaluation,heure_fin_evaluation,'0','NON ENTAMEE'"
					+ " from planning_evaluation f,employe e"
					+ " where f.id_employe=e.id_employe and f.id_planning_evaluation  not in (select id_planning_evaluation from fiche_validation)"
					+ " and f.id_evaluateur=? and f.id_compagne=?";



			psmt =  conn.prepareStatement(sql_query);
			psmt.setInt(1, id_evaluateur);
			psmt.setInt(2, id_compagne);
			psmt.setInt(3, id_evaluateur);
			psmt.setInt(4, id_compagne);

			rs =  psmt.executeQuery();

			while(rs.next()){
				SuiviCompStatutFicheBean fichebean=new SuiviCompStatutFicheBean();
				fichebean.setEvalue(rs.getString("evalue"));
				fichebean.setId_employe(rs.getInt("id_employe"));
				fichebean.setStatut(rs.getString("statut"));
				fichebean.setDate_fin_eval(rs.getString("date_fin_evaluation"));
				fichebean.setHeure_fin_eval(rs.getString("heure_fin_evaluation"));
				fichebean.setFiche_valide(rs.getString("fiche_valide"));


				listefiche.add(fichebean);
			}

		} 
		catch ( SQLException e ) {

			e.printStackTrace();

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( psmt != null ) {
				try {
					psmt.close();
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
		return listefiche;


	}

	public ArrayList<SuiviCompagneBean> exportRapport(int id_compagne ) throws SQLException{


		listevaluateur = new ArrayList<SuiviCompagneBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		String sqlquery="";


		sqlquery="select e.id_employe,concat (nom ,' ',prenom) as nom_evaluateur , intitule_poste,email,round(sum(nbfichevalide)*100/ sum(totalemploye)) as progress ,"
				+ " sum(totalemploye-nbfichevalide)fichenoncote, sum(nbfichevalide)fichecote, h.date_alert dateExtract"+
				" from (	select id_evaluateur as evaluateur ,count(r.id_employe) as nbfichevalide,0 as totalemploye from planning_evaluation t ,fiche_validation r" +
				" where t.id_planning_evaluation=r.id_planning_evaluation" +
				" and t.id_employe=r.id_employe  and fiche_valide=1  and  t.id_compagne=#compgane "+
				" group by id_evaluateur   union select id_evaluateur as evaluateur ,0 as nbfichevalide ,count(t.id_employe)as totalemploye" +
				" from planning_evaluation t where   t.id_compagne=#compgane  group by id_evaluateur" +
				" ) as t2, poste_travail_description p,employe e LEFT OUTER JOIN histo_envoi_email h"
				+ "  ON  h.id_employe=e.id_employe and  h.id_compagne=#compgane where    e.id_employe=evaluateur and p.code_poste=e.code_poste"
				+ " group by 1,2,3,4 order by 2";




		sqlquery = sqlquery.replaceAll("#compgane", Integer.toString(id_compagne));

		//System.out.println(sqlquery);


		try {
			stmt = (Statement) conn.createStatement();


			rs = (ResultSet) stmt.executeQuery(sqlquery);

			while(rs.next()){

				SuiviCompagneBean evalbean=new SuiviCompagneBean();
				evalbean.setEvaluateur(rs.getString("nom_evaluateur"));
				evalbean.setPoste_travail(rs.getString("intitule_poste"));
				evalbean.setPourcentage(rs.getInt("progress"));
				evalbean.setId_employe(rs.getInt("id_employe"));
				evalbean.setEmail(rs.getString("email"));

				evalbean.setFichecote(rs.getInt("fichecote"));
				evalbean.setFichenoncote(rs.getInt("fichenoncote"));
				evalbean.setDateExtract(rs.getString("dateExtract"));
				listevaluateur.add(evalbean);


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
		return listevaluateur;

	}	


	public boolean insertAlertEmail(Integer id_compagne ,Integer id_evaluateur) throws SQLException 
	{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();

		PreparedStatement pstmt = null;
		boolean result=true;
		try {


			String requete_Sql="insert into histo_envoi_email (id_employe,id_compagne,date_alert ,code_ecran)"
					+ " values (?,?,now(),?)";
			pstmt = conn.prepareStatement(requete_Sql);


			pstmt.setInt(1, id_evaluateur);
			pstmt.setInt(2, id_compagne);
			pstmt.setString(3,"SUICOM" );


			//System.out.println(pstmt.toString());




			int row= pstmt.executeUpdate();
			if (row>0){
				//System.out.println(pstmt.toString());
				result=true;
			}else
				result=false;
			//pstmt.close();conn.close();

		} catch ( SQLException e ) {
			e.printStackTrace();
			System.out.println("Exception ors de l'insertion du poste de travail avecla requete  "+pstmt);
			result=false;

		} finally {

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

		return result;

	}



}
