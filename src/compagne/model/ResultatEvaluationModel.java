package compagne.model;



import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.zkoss.zul.Messagebox;

import common.CreateDatabaseCon;
import compagne.bean.EmployeFamilleBean;
import compagne.bean.EvaluationParCompetenceBean;
import compagne.bean.MatriceCotationBean;

public class ResultatEvaluationModel {


	// le type retour est a  modifier plus tartd
	public ArrayList<String> getListeCompagnes()
	{
		ArrayList <String > listCompagnes=new ArrayList<String>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT id_compagne, date_debut, libelle_compagne  FROM compagne_evaluation ";


			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String id_compagne=rs.getString("id_compagne");
					String date_debut=rs.getString("date_debut");
					String libelle_compagne=rs.getString("libelle_compagne");
					listCompagnes.add(id_compagne+"#"+date_debut+"#"+libelle_compagne);


				}
				//				else {
				//					return listCompagnes;
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
		return listCompagnes;
	}

	public HashMap <String, HashMap<String, ArrayList<String>>> getInfosFamillesCompetence(String id_compagne) throws ParseException
	{

		HashMap <String, HashMap<String, ArrayList<String>>>	 mapPosteFamilleCompetence	=new HashMap <String, HashMap<String, ArrayList<String>>>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		String select_structure="";

		try 
		{
			stmt = (Statement) conn.createStatement();

			int retour =compareDate(id_compagne);

			if( retour==0){
				select_structure="select distinct p.intitule_poste,  r.famille, r.libelle_competence from repertoire_competence r, poste_travail_description p, poste_travail_competence l where p.code_poste=l.code_poste and r.code_competence=l.code_competence and p.code_poste in(select distinct v.code_poste from img_stats v where id_compagne=#id_compagne)";

			}else{
				select_structure="select distinct p.intitule_poste,  r.famille, r.libelle_competence"
						+ "	from repertoire_competence r, poste_travail_description p, poste_travail_comptence_aptitudeobservable l"
						+ " where p.code_poste=l.code_poste and r.code_competence=l.code_competence and p.code_poste in(select distinct v.code_poste from img_stats v where id_compagne=#id_compagne)"
						+ " and r.id_repertoire_competence=l.id_repertoire_competence";
			}



			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{

					String intitule_poste=rs.getString("intitule_poste");
					String famille=rs.getString("famille");
					String libelle_competence=rs.getString("libelle_competence");

					//remplissage de la structure
					if(mapPosteFamilleCompetence.containsKey(intitule_poste))
					{

						if(mapPosteFamilleCompetence.get(intitule_poste).containsKey(famille))
						{
							mapPosteFamilleCompetence.get(intitule_poste).get(famille).add(libelle_competence);
						}
						else //nouvelle famille
						{
							ArrayList<String> d=new ArrayList<String>();
							d.add(libelle_competence);
							mapPosteFamilleCompetence.get(intitule_poste).put(famille, d);

						}
					}
					else //nouveau poste
					{
						ArrayList<String> d=new ArrayList<String>();
						d.add(libelle_competence);
						HashMap<String, ArrayList<String>> liste=new HashMap<String, ArrayList<String> >();
						liste.put(famille, d);
						mapPosteFamilleCompetence.put(intitule_poste, liste);

					}

				}
				//				else {
				//					return mapPosteFamilleCompetence;
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
		return mapPosteFamilleCompetence;
	}
	public HashMap <String, HashMap<String, ArrayList<String>>> getInfosFamillesCompetenceRattrapage(String id_compagne) throws ParseException
	{

		HashMap <String, HashMap<String, ArrayList<String>>>	 mapPosteFamilleCompetence	=new HashMap <String, HashMap<String, ArrayList<String>>>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			int retour=compareDate(id_compagne);

			if (retour==0){
				select_structure="select distinct p.intitule_poste,  r.famille, r.libelle_competence from repertoire_competence r, poste_travail_description p, poste_travail_competence l where p.code_poste=l.code_poste and r.code_competence=l.code_competence and p.code_poste in(select distinct v.code_poste from img_stats v where id_compagne=#id_compagne)";

			}else{
				select_structure="select distinct p.intitule_poste,  r.famille, r.libelle_competence"
						+ "	from repertoire_competence r, poste_travail_description p, poste_travail_comptence_aptitudeobservable l"
						+ " where p.code_poste=l.code_poste and r.code_competence=l.code_competence and p.code_poste in(select distinct v.code_poste from img_stats v where id_compagne=#id_compagne)"
						+ " and r.id_repertoire_competence=l.id_repertoire_competence";
			}




			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{

					String intitule_poste=rs.getString("intitule_poste");
					String famille=rs.getString("famille");
					String libelle_competence=rs.getString("libelle_competence");

					//remplissage de la structure
					if(mapPosteFamilleCompetence.containsKey(intitule_poste))
					{

						if(mapPosteFamilleCompetence.get(intitule_poste).containsKey(famille))
						{
							mapPosteFamilleCompetence.get(intitule_poste).get(famille).add(libelle_competence);
						}
						else //nouvelle famille
						{
							ArrayList<String> d=new ArrayList<String>();
							d.add(libelle_competence);
							mapPosteFamilleCompetence.get(intitule_poste).put(famille, d);

						}
					}
					else //nouveau poste
					{
						ArrayList<String> d=new ArrayList<String>();
						d.add(libelle_competence);
						HashMap<String, ArrayList<String>> liste=new HashMap<String, ArrayList<String> >();
						liste.put(famille, d);
						mapPosteFamilleCompetence.put(intitule_poste, liste);

					}

				}
				//				else {
				//					return mapPosteFamilleCompetence;
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
		return mapPosteFamilleCompetence;
	}

	/*
	 * duplication  methode pour la matrice de cotation
	 * 
	 */

	public HashMap <String, String>  getInfosFamillesCompetenceRattrapageMat(String id_compagne) throws ParseException
	{

		HashMap <String,String> 	 mapPosteFamilleCompetence	=new HashMap <String,String> ();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();

			int retour=compareDate(id_compagne);

			if (retour==0){
				select_structure="select distinct  r.famille, r.libelle_competence 	from repertoire_competence r order by 1";

			}else{
				select_structure="select distinct  r.famille, r.libelle_competence from repertoire_competence r,"
						+ " poste_travail_comptence_aptitudeobservable l "
						+ " where   r.id_repertoire_competence=l.id_repertoire_competence order by 1";
			}


			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{

					//String intitule_poste=rs.getString("intitule_poste");
					String famille=rs.getString("famille");
					String libelle_competence=rs.getString("libelle_competence");

					mapPosteFamilleCompetence.put(famille, libelle_competence);

				}

			}
			//				else {
			//					return mapPosteFamilleCompetence;
			//				}


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
		return mapPosteFamilleCompetence;
	}


	public HashMap<String, HashMap<String, HashMap< String, HashMap<String, Double>> >> getAllIMICompetence(String id_compagne)
	{
		HashMap<String, HashMap<String, HashMap< String, HashMap<String, Double>> >> mapposteEmployeFamilleCompIMI=new HashMap<String, HashMap<String, HashMap< String, HashMap<String, Double>> >>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			//			String select_structure="select distinct f.intitule_poste, c.code_famille,r.famille,  e.nom, e.prenom, r.libelle_competence, round (c.moy_competence,2) as moy_competence  "+
			//			" from poste_travail_description f, imi_competence_stat c, employe e, repertoire_competence r "+
			//			" where c.id_employe=e.id_employe "+
			//			" and c.id_compagne=#id_compagne "+
			//			" and c.code_famille=r.code_famille "+
			//			" and c.code_competence=r.code_competence "+
			//			" and e.code_poste=f.code_poste";

			String select_structure="select distinct f.intitule_poste, c.code_famille,r.famille,  e.nom, e.prenom, r.libelle_competence, c.moy_competence,2  "+
					" from poste_travail_description f, imi_competence_stat c, employe e, repertoire_competence r , img_stats g "+
					" where c.id_employe=e.id_employe "+
					" and c.id_compagne=#id_compagne "+
					" and c.code_famille=r.code_famille "+
					" and c.code_competence=r.code_competence "+
					" and e.code_poste=f.code_poste  and e.code_poste=g.code_poste and  g.id_compagne=c.id_compagne"; 
			//" and  e.id_employe   in (select id_employe from fiche_validation where fiche_valide=1)"	;

			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);

			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String intitule_poste=rs.getString("intitule_poste");
					String code_famille=rs.getString("code_famille");
					String famille=rs.getString("famille");
					String nom=rs.getString("nom");
					String prenom=rs.getString("prenom");
					String libelle_competence=rs.getString("libelle_competence");

					Double moy_competence=rs.getDouble("moy_competence");
					DecimalFormat df = new DecimalFormat("########.00"); 
					df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
					df.setMinimumFractionDigits ( 2 ) ;
					df.setDecimalSeparatorAlwaysShown ( true ) ;
					String f=df.format(moy_competence);
					moy_competence= Double.parseDouble(f.replace(',', '.'));



					String employe=nom+" " +prenom;
					if(mapposteEmployeFamilleCompIMI.containsKey(intitule_poste))
					{
						HashMap<String , HashMap<String,HashMap<String, Double> > >mapEmployeFamilleCompetence=mapposteEmployeFamilleCompIMI.get(intitule_poste);
						if(mapEmployeFamilleCompetence.containsKey(employe))
						{
							HashMap<String,HashMap<String, Double> > mapFamilleCompetence=mapEmployeFamilleCompetence.get(employe);
							if(mapFamilleCompetence.containsKey(famille))
							{
								HashMap<String, Double> mapCompetence=mapFamilleCompetence.get(famille);
								if(mapCompetence.containsKey(libelle_competence))
								{
									mapCompetence.put(libelle_competence, moy_competence);					
									mapFamilleCompetence.put(famille, mapCompetence);
									mapEmployeFamilleCompetence.put(employe,  mapFamilleCompetence);
									mapposteEmployeFamilleCompIMI.put(intitule_poste, mapEmployeFamilleCompetence);
								}
								else
								{
									mapCompetence.put(libelle_competence, moy_competence);					
									mapFamilleCompetence.put(famille, mapCompetence);
									mapEmployeFamilleCompetence.put(employe,  mapFamilleCompetence);
									mapposteEmployeFamilleCompIMI.put(intitule_poste, mapEmployeFamilleCompetence);
								}
							}
							else
							{
								HashMap<String, Double> mapCompetence=new HashMap<String, Double>();
								mapCompetence.put(libelle_competence, moy_competence);					
								mapFamilleCompetence.put(famille, mapCompetence);
								mapEmployeFamilleCompetence.put(employe,  mapFamilleCompetence);
								mapposteEmployeFamilleCompIMI.put(intitule_poste, mapEmployeFamilleCompetence);
							}
						}
						else
						{
							HashMap<String, Double> mapCompetence=new HashMap<String, Double>();
							mapCompetence.put(libelle_competence, moy_competence);
							HashMap<String,HashMap<String, Double> > mapFamilleCompetence=new HashMap<String,HashMap<String, Double> >();
							mapFamilleCompetence.put(famille, mapCompetence);
							mapEmployeFamilleCompetence.put(employe,  mapFamilleCompetence);
							mapposteEmployeFamilleCompIMI.put(intitule_poste, mapEmployeFamilleCompetence);
						}

					}
					else
					{
						HashMap<String, Double> mapCompetence=new HashMap<String, Double>();
						mapCompetence.put(libelle_competence, moy_competence);
						HashMap<String,HashMap<String, Double> > mapFamilleCompetence=new HashMap<String,HashMap<String, Double> >();
						mapFamilleCompetence.put(famille, mapCompetence);
						HashMap<String , HashMap<String,HashMap<String, Double> > >mapEmployeFamilleCompetence=new HashMap<String , HashMap<String,HashMap<String, Double> > >();

						mapEmployeFamilleCompetence.put(employe,  mapFamilleCompetence);
						mapposteEmployeFamilleCompIMI.put(intitule_poste, mapEmployeFamilleCompetence);
					}

				}
				//				else {
				//					return mapposteEmployeFamilleCompIMI;
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
		return mapposteEmployeFamilleCompIMI;
	}

	public HashMap<String, HashMap<String, String>> getInfosIMIStat(String id_compagne)
	{
		HashMap<String, HashMap<String, String>> mapEmployeFamilleIMI=new HashMap<String, HashMap<String, String>>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			//			String select_structure="select distinct e.nom, e.prenom, r.famille, round(i.moy_par_famille, 2) as moy_par_famille , round (i.imi, 2) as imi "
			//				+" from employe e, repertoire_competence r, imi_stats i "
			//				+" where i.id_employe=e.id_employe  "
			//				+" and r.code_famille=i.code_famille "
			//				+" and id_compagne=#id_compagne ORDER BY imi DESC";

			String select_structure="select distinct e.nom, e.prenom, r.famille, i.moy_par_famille , i.imi "
					+" from employe e, repertoire_competence r, imi_stats i "
					+" where i.id_employe=e.id_employe  "
					+" and r.code_famille=i.code_famille "
					+" and id_compagne=#id_compagne ORDER BY imi DESC";
			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String nom=rs.getString("nom");
					String prenom=rs.getString("prenom");
					String employe=nom+" " +prenom;
					String famille=rs.getString("famille");



					Double moy_famille=rs.getDouble("moy_par_famille");
					Double imiD=rs.getDouble("imi");


					/**************************/
					DecimalFormat df = new DecimalFormat("########.00"); 
					df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
					df.setMinimumFractionDigits ( 2 ) ;
					df.setDecimalSeparatorAlwaysShown ( true ) ;
					String moy_par_famille=df.format(moy_famille).replace(',', '.');
					//					Double a=Double.parseDouble(f.replace(',', '.'));
					//					String moy_par_famille= a.toString().replace(',', '.');
					String f1=df.format(imiD);
					Double b=Double.parseDouble(f1.replace(',', '.'));
					String imi=b.toString();
					/*****************************/

					String stats=moy_par_famille+"#"+imi;

					if (mapEmployeFamilleIMI.containsKey(employe))
					{
						HashMap<String, String> mapFamille=mapEmployeFamilleIMI.get(employe);
						if(mapFamille.containsKey(famille))
						{
							mapFamille.put(famille, stats);
							mapEmployeFamilleIMI.put(employe, mapFamille);
						}
						else
						{

							mapFamille.put(famille, stats);
							mapEmployeFamilleIMI.put(employe, mapFamille);
						}

					}
					else
					{
						HashMap<String, String> mapFamille=new HashMap<String, String>();
						mapFamille.put(famille, stats);
						mapEmployeFamilleIMI.put(employe, mapFamille);

					}



				}
				//				else {
				//					return mapEmployeFamilleIMI;
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

		return mapEmployeFamilleIMI;
	}

	public HashMap <String, Double> getIMGparPoste(String id_compagne)
	{
		HashMap <String, Double> mapPosteIMG=new HashMap<String, Double>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="select distinct p.intitule_poste, round(i.img, 2) as img from poste_travail_description p, img_stats i where p.code_poste=i.code_poste and i.id_compagne=#id_compagne";
			String select_structure="select distinct p.intitule_poste, i.img from poste_travail_description p, img_stats i where p.code_poste=i.code_poste and i.id_compagne=#id_compagne";

			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String intitule_poste=rs.getString("intitule_poste");
					Double img=rs.getDouble("img");

					/**************************/
					DecimalFormat df = new DecimalFormat("########.00"); 
					df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
					df.setMinimumFractionDigits ( 2 ) ;
					df.setDecimalSeparatorAlwaysShown ( true ) ;
					String f=df.format(img);
					img= Double.parseDouble(f.replace(',', '.'));

					/*****************************/

					mapPosteIMG.put(intitule_poste, img);

				}
				//				else {
				//					return mapPosteIMG;
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
		return mapPosteIMG;
	}
	public HashMap <String, HashMap<String, Double>> getIMGFamille(String id_compagne)
	{
		HashMap <String, HashMap<String, Double>> mapIMGPosteFamille=new HashMap <String, HashMap<String, Double>>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			//			String select_structure="select distinct p.intitule_poste, r.famille, round(i.moy_par_famille,2) as moy_par_famille"
			//				+" from poste_travail_description p, repertoire_competence r, moy_poste_famille_stats i"
			//				+" where p.code_poste=i.code_poste "
			//				+" and r.code_famille=i.code_famille"
			//				+" and id_compagne=#id_compagne ";

			String select_structure="select distinct p.intitule_poste, r.famille, i.moy_par_famille"
					+" from poste_travail_description p, repertoire_competence r, moy_poste_famille_stats i"
					+" where p.code_poste=i.code_poste "
					+" and r.code_famille=i.code_famille"
					+" and id_compagne=#id_compagne ";
			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String intitule_poste=rs.getString("intitule_poste");
					String famille=rs.getString("famille");
					Double moy_par_famille=rs.getDouble("moy_par_famille");

					/**************************/
					DecimalFormat df = new DecimalFormat("########.00"); 
					df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
					df.setMinimumFractionDigits ( 2 ) ;
					df.setDecimalSeparatorAlwaysShown ( true ) ;
					String f=df.format(moy_par_famille);
					moy_par_famille= Double.parseDouble(f.replace(',', '.'));

					/*****************************/


					if(mapIMGPosteFamille.containsKey(intitule_poste))
					{
						HashMap<String, Double> mapFamille=mapIMGPosteFamille.get(intitule_poste);
						if(mapFamille.containsKey(famille))
						{
							mapFamille.put(famille, moy_par_famille);
							mapIMGPosteFamille.put(intitule_poste,mapFamille);
						}
						else
						{
							mapFamille.put(famille, moy_par_famille);
							mapIMGPosteFamille.put(intitule_poste,mapFamille);
						}
					}
					else
					{
						HashMap<String, Double> mapFamille=new HashMap<String, Double>();
						mapFamille.put(famille, moy_par_famille);
						mapIMGPosteFamille.put(intitule_poste,mapFamille);
					}

				}
				//				else {
				//					return mapIMGPosteFamille;
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
		return mapIMGPosteFamille;

	}

	public HashMap<String, HashMap<String, HashMap< String, Double>>> getmoyPosteCompetenceStats(String id_compagne) throws ParseException
	{
		HashMap<String, HashMap<String, HashMap< String, Double>>> mapPostFamilleCompetenceStats=new HashMap<String, HashMap<String, HashMap< String, Double>>>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();
			//			String select_structure="select distinct f.intitule_poste, c.code_famille,r.famille,   r.libelle_competence, round(c.moy_par_competence,2) as moy_par_competence   " 
			//				+" from poste_travail_description f, moy_poste_competence_stats c, employe e, repertoire_competence r "  
			//				+" where c.id_compagne=#id_compagne " 
			//				+" and c.code_famille=r.code_famille  " 
			//				+"  and c.code_competence=r.code_competence " 
			//				+" and e.code_poste=f.code_poste";

			//			String select_structure="select distinct f.intitule_poste, c.code_famille,r.famille,   r.libelle_competence, c.moy_par_competence  " 
			//				+" from poste_travail_description f, moy_poste_competence_stats c, employe e, repertoire_competence r "  
			//				+" where c.id_compagne=#id_compagne " 
			//				+" and c.code_famille=r.code_famille  " 
			//				+"  and c.code_competence=r.code_competence " 
			//				+" and e.code_poste=f.code_poste";

			//			la requete ci dessus à été remplacé par celle qui suit (nabil) pouroptimisaton

			int retour=compareDate(id_compagne);

			if (retour==0){

				select_structure="select distinct f.intitule_poste, r.code_famille,r.famille,   r.libelle_competence  " 
						+" from poste_travail_description f, compagne_poste_travail c ,poste_travail_competence p, repertoire_competence r "  
						+" where c.id_compagne=#id_compagne  "
						+" and c.code_poste=p.code_poste "
						+" and c.code_poste=f.code_poste "
						+" and p.code_competence=r.code_competence "
						+" order by f.intitule_poste, r.code_famille,r.famille,   r.libelle_competence " ;
			}else{
				select_structure="select distinct f.intitule_poste, r.code_famille,r.famille,   r.libelle_competence"
						+ " from poste_travail_description f, compagne_poste_travail c ,poste_travail_comptence_aptitudeobservable p, repertoire_competence r"
						+ " where c.id_compagne=#id_compagne  and c.code_poste=p.code_poste and c.code_poste=f.code_poste"
						+ " and p.code_competence=r.code_competence and p.id_repertoire_competence=r.id_repertoire_competence"
						+ " order by f.intitule_poste, r.code_famille,r.famille,   r.libelle_competence"  ;
			}

			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);

			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String intitule_poste=rs.getString("intitule_poste");
					String code_famille=rs.getString("code_famille");
					String famille=rs.getString("famille");

					String libelle_competence=rs.getString("libelle_competence");
					//Double moy_par_competence=rs.getDouble("moy_par_competence");


					/**************************/
					DecimalFormat df = new DecimalFormat("########.00"); 
					df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
					df.setMinimumFractionDigits ( 2 ) ;
					df.setDecimalSeparatorAlwaysShown ( true ) ;
					//					String f=df.format(moy_par_competence);
					//					moy_par_competence= Double.parseDouble(f.replace(',', '.'));

					/*****************************/
					if(mapPostFamilleCompetenceStats.containsKey(intitule_poste))
					{
						HashMap<String, HashMap< String, Double>>mapFamilleCompetence=mapPostFamilleCompetenceStats.get(intitule_poste);
						if(mapFamilleCompetence.containsKey(famille))
						{
							HashMap<String, Double> mapCompetence=mapFamilleCompetence.get(famille);
							if(mapCompetence.containsKey(libelle_competence))
							{
								//mapCompetence.put(libelle_competence, moy_par_competence);
								mapFamilleCompetence.put(famille, mapCompetence);
								mapPostFamilleCompetenceStats.put(intitule_poste, mapFamilleCompetence);
							}
							else
							{
								//mapCompetence.put(libelle_competence, moy_par_competence);
								mapFamilleCompetence.put(famille, mapCompetence);
								mapPostFamilleCompetenceStats.put(intitule_poste, mapFamilleCompetence);
							}

						}
						else
						{
							HashMap< String, Double>mapCompetence=new  HashMap< String, Double>();
							//mapCompetence.put(libelle_competence,  moy_par_competence);
							mapFamilleCompetence.put(famille, mapCompetence);
							mapPostFamilleCompetenceStats.put(intitule_poste, mapFamilleCompetence);
						}
					}
					else
					{
						HashMap<String, Double> mapCompetence=new HashMap<String, Double>();
						//mapCompetence.put(libelle_competence, moy_par_competence);
						HashMap<String,HashMap<String, Double> > mapFamilleCompetence=new HashMap<String,HashMap<String, Double> >();
						mapFamilleCompetence.put(famille, mapCompetence);

						mapPostFamilleCompetenceStats.put(intitule_poste, mapFamilleCompetence);
					}

				}
				//				else {
				//					return mapPostFamilleCompetenceStats;
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
		return mapPostFamilleCompetenceStats;
	}


	public HashMap<String, HashMap<String, HashMap< String, Double>>> getmoyPosteCompetenceStatsRattrapage(String id_compagne) throws ParseException
	{
		HashMap<String, HashMap<String, HashMap< String, Double>>> mapPostFamilleCompetenceStats=new HashMap<String, HashMap<String, HashMap< String, Double>>>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		String select_structure="";
		try 
		{
			stmt = (Statement) conn.createStatement();
			//			String select_structure="select distinct f.intitule_poste, c.code_famille,r.famille,   r.libelle_competence, round(c.moy_par_competence,2) as moy_par_competence   " 
			//				+" from poste_travail_description f, moy_poste_competence_stats c, employe e, repertoire_competence r "  
			//				+" where c.id_compagne=#id_compagne " 
			//				+" and c.code_famille=r.code_famille  " 
			//				+"  and c.code_competence=r.code_competence " 
			//				+" and e.code_poste=f.code_poste";

			//			String select_structure="select distinct f.intitule_poste, c.code_famille,r.famille,   r.libelle_competence, c.moy_par_competence  " 
			//				+" from poste_travail_description f, moy_poste_competence_stats c, employe e, repertoire_competence r "  
			//				+" where c.id_compagne=#id_compagne " 
			//				+" and c.code_famille=r.code_famille  " 
			//				+"  and c.code_competence=r.code_competence " 
			//				+" and e.code_poste=f.code_poste";

			//			la requete ci dessus à été remplacé par celle qui suit (nabil) pouroptimisaton


			int retour=compareDate(id_compagne);

			if (retour==0){

				select_structure="select distinct f.intitule_poste, r.code_famille,r.famille,   r.libelle_competence  " 
						+" from poste_travail_description f, compagne_poste_travail c ,poste_travail_competence p, repertoire_competence r "  
						+" where c.id_compagne=#id_compagne  "
						+" and c.code_poste=p.code_poste "
						+" and c.code_poste=f.code_poste "
						+" and p.code_competence=r.code_competence "
						+" order by f.intitule_poste, r.code_famille,r.famille,   r.libelle_competence " ;

			}else{

				select_structure="select distinct f.intitule_poste, r.code_famille,r.famille,   r.libelle_competence"
						+ " from poste_travail_description f, compagne_poste_travail c ,poste_travail_comptence_aptitudeobservable p, repertoire_competence r"
						+ " where c.id_compagne=#id_compagne  and c.code_poste=p.code_poste and c.code_poste=f.code_poste"
						+ " and p.code_competence=r.code_competence and p.id_repertoire_competence=r.id_repertoire_competence"
						+ " order by f.intitule_poste, r.code_famille,r.famille,   r.libelle_competence"  ;


			}





			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);

			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String intitule_poste=rs.getString("intitule_poste");
					String code_famille=rs.getString("code_famille");
					String famille=rs.getString("famille");

					String libelle_competence=rs.getString("libelle_competence");
					//Double moy_par_competence=rs.getDouble("moy_par_competence");


					/**************************/
					DecimalFormat df = new DecimalFormat("########.00"); 
					df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
					df.setMinimumFractionDigits ( 2 ) ;
					df.setDecimalSeparatorAlwaysShown ( true ) ;
					//					String f=df.format(moy_par_competence);
					//					moy_par_competence= Double.parseDouble(f.replace(',', '.'));

					/*****************************/
					if(mapPostFamilleCompetenceStats.containsKey(intitule_poste))
					{
						HashMap<String, HashMap< String, Double>>mapFamilleCompetence=mapPostFamilleCompetenceStats.get(intitule_poste);
						if(mapFamilleCompetence.containsKey(famille))
						{
							HashMap<String, Double> mapCompetence=mapFamilleCompetence.get(famille);
							if(mapCompetence.containsKey(libelle_competence))
							{
								//mapCompetence.put(libelle_competence, moy_par_competence);
								mapFamilleCompetence.put(famille, mapCompetence);
								mapPostFamilleCompetenceStats.put(intitule_poste, mapFamilleCompetence);
							}
							else
							{
								//mapCompetence.put(libelle_competence, moy_par_competence);
								mapFamilleCompetence.put(famille, mapCompetence);
								mapPostFamilleCompetenceStats.put(intitule_poste, mapFamilleCompetence);
							}

						}
						else
						{
							HashMap< String, Double>mapCompetence=new  HashMap< String, Double>();
							//mapCompetence.put(libelle_competence,  moy_par_competence);
							mapFamilleCompetence.put(famille, mapCompetence);
							mapPostFamilleCompetenceStats.put(intitule_poste, mapFamilleCompetence);
						}
					}
					else
					{
						HashMap<String, Double> mapCompetence=new HashMap<String, Double>();
						//mapCompetence.put(libelle_competence, moy_par_competence);
						HashMap<String,HashMap<String, Double> > mapFamilleCompetence=new HashMap<String,HashMap<String, Double> >();
						mapFamilleCompetence.put(famille, mapCompetence);

						mapPostFamilleCompetenceStats.put(intitule_poste, mapFamilleCompetence);
					}

				}
				//				else {
				//					return mapPostFamilleCompetenceStats;
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
		return mapPostFamilleCompetenceStats;
	}


	public HashMap<String, ArrayList<String>> getEmployeTriIMI(String id_compagne)
	{
		HashMap<String, ArrayList<String>> mapPostEmployeTriIMI=new HashMap<String, ArrayList<String>>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();


			/*String select_structure="select distinct e.nom, e.prenom,  i.imi,p.intitule_poste "
					+" from employe e,  imi_stats i , poste_travail_description p "
					+" where i.id_employe=e.id_employe "  
					+" and p.code_poste=e.code_poste "
					+" and id_compagne=#id_compagne"
					+" and  e.id_employe   in (select id_employe from fiche_validation where fiche_valide=1)"
					+" ORDER BY imi DESC";	*/	

			String select_structure="select login matricule,concat(e.nom, concat( \" \" ,e.prenom)) as nom,DATE_FORMAT(e.date_naissance, '%d/%c/%Y') date_naissance,DATE_FORMAT(e.date_recrutement, '%d/%c/%Y') date_recrutement,d.niv_for_libelle formation,libelle_direction,structure_ent,p.intitule_poste ,round(imi,2) imi"
					+ " from poste_travail_description p , employe e, common_evalcom.compte c, formation f , def_niv_formation d, imi_stats s  ,"
					+ " ("
					+ "    select libelle_direction,code_structure, structure_ent from ("
					+ "           select libelle_direction,code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
					+ "           and  libelle_section !='null' and  libelle_section !=''"
					+ "           union"
					+ "           select libelle_direction,code_structure,libelle_service structure_ent from structure_entreprise"
					+ "           where libelle_service is  not null and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
					+ "           union"
					+ "           select libelle_direction,code_structure,libelle_departement structure_ent from structure_entreprise"
					+ "           where libelle_departement is  not null and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
					+ "           union"
					+ "           select libelle_direction,code_structure,libelle_sous_direction structure_ent from structure_entreprise"
					+ "           where libelle_sous_direction is  not null and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
					+ "           union"
					+ "           select libelle_direction,code_structure,libelle_unite structure_ent from structure_entreprise"
					+ "           where libelle_unite is  not null and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "           and length(libelle_service)=0 and  length(libelle_section) =0"
					+ "           union"
					+ "           select libelle_direction,code_structure,libelle_direction structure_ent from structure_entreprise"
					+ "           where libelle_direction is  not null and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "           and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise ) t"

					+ " where e.code_structure=t.code_structure and p.code_poste=e.code_poste  and s.id_employe=e.id_employe"
					+ " and s.id_compagne=6   and c.id_compte=e.id_compte and d.niv_for_id=f.niv_for_id and e.code_formation=f.code_formation"
					+ " union"
					+ " select '99999999999' matricule,'DUMMY' as nom,'01/01/3999' date_naissance,'01/01/3999' date_recrutement,'DUMMY' formation,'vvvvvvvvvvvvvv' direction,'ZZZZZZZZZZZZZZ' structure_ent,'DUMMY' intitule_poste , 99 imi"
					+ " order by   imi DESC,nom";

			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);
			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String matricule=rs.getString("matricule");
					String nom=rs.getString("nom");
					String date_naissance=rs.getString("date_naissance");
					String date_recrutement=rs.getString("date_recrutement");
					String formation=rs.getString("formation");
					String structure_ent=rs.getString("structure_ent");
					String intitule_poste=rs.getString("intitule_poste");
					String imi=rs.getString("imi");
					String libelle_direction=rs.getString("libelle_direction");

					String cles1=matricule+"#"+nom+"#"+libelle_direction+"#"+structure_ent+"#"+intitule_poste+"#"+date_naissance+"#"+date_recrutement+"#"+formation;

					if (!matricule.equalsIgnoreCase("99999999999") && !nom.equalsIgnoreCase("DUMMY")){
						if(mapPostEmployeTriIMI.containsKey(intitule_poste))
						{
							mapPostEmployeTriIMI.get(intitule_poste).add(cles1);
						}
						else
						{
							ArrayList<String> listEmploye=new ArrayList<String>();
							listEmploye.add(cles1);
							mapPostEmployeTriIMI.put(intitule_poste, listEmploye);
						}
					}



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
		return mapPostEmployeTriIMI;
	}


	// recuperer la  date debut du compagne selectionnée
	public String getDateFinSelectedCompagne(String id_compagne)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		String date_fin="";
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT  date_debut  FROM compagne_evaluation where id_compagne=#id_compagne";

			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);



			rs = (ResultSet) stmt.executeQuery(select_structure);


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{

					date_fin=rs.getString("date_debut");



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
		return date_fin;
	}


	public int compareDate(String id_compagne) throws ParseException  {
		String dateString1 = "2015-12-01"; //1er octobre 2015
		//String dateString2 = "05-13-2012";
		int retour=0;

		String date_fin=getDateFinSelectedCompagne(id_compagne);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Date date1 = format.parse(dateString1);
		Date date2 = format.parse(date_fin);

		if (date2.compareTo(date1) <= 0) {
			//System.out.println("dateString1 is an earlier date than dateString2");
			//utiliser l'ancienne table competence_poste_travail
			retour=0;
		}else
			//utiliser nouvelle  table poste_travail_comptence_aptitudeobservable
			retour=1;
		return retour;
	}


	public HashMap getListLastCompagneValid() throws SQLException
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

	public HashMap<String, HashMap<String,String>> getMatriceCotationExlFile(String id_compagne, ArrayList<String> listCles){
		HashMap<String, HashMap<String,String>> mapEmployeFamille=new HashMap<String, HashMap<String,String>>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select login matricule,concat(e.nom, concat(\" \",e.prenom)) as nom,e.date_naissance,e.date_recrutement,d.niv_for_libelle formation,structure_ent,p.intitule_poste ,famille,moy_par_famille ,imi"
					+ " from poste_travail_description p , employe e, common_evalcom.compte c, formation f , def_niv_formation d, imi_stats s  ,"
					+ "	 ("
					+ "   select code_structure, structure_ent from ("
					+ "   select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
					+ "   and  libelle_section !='null' and  libelle_section !=''"
					+ "   union"
					+ "	 select code_structure,libelle_service structure_ent from structure_entreprise"
					+ "  where libelle_service is  not null and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
					+ "  union"
					+ " select code_structure,libelle_departement structure_ent from structure_entreprise"
					+ " where libelle_departement is  not null and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_sous_direction structure_ent from structure_entreprise"
					+ " where libelle_sous_direction is  not null and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_unite structure_ent from structure_entreprise"
					+ " where libelle_unite is  not null and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ " and length(libelle_service)=0 and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_direction structure_ent from structure_entreprise"
					+ " where libelle_direction is  not null and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ " and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise ) t, (select distinct code_famille,famille from repertoire_competence) r"
					+ " where e.code_structure=t.code_structure and p.code_poste=e.code_poste  and s.id_employe=e.id_employe"
					+ " and s.id_compagne=#id_compagne  and s.code_famille=r.code_famille  and c.id_compte=e.id_compte"
					+ " and f.code_formation=e.code_formation and d.niv_for_id=f.niv_for_id"
					+ " order by t.code_structure,imi desc" ;


			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);
			//System.out.println("#id_compagne>>"+ id_compagne+"#sql>>"+select_structure);
			rs = (ResultSet) stmt.executeQuery(select_structure);
			String matricule_bckp="";


			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{

					String matricule=rs.getString("matricule");
					String nom=rs.getString("nom");
					String date_naissance=rs.getString("date_naissance");
					String date_recrutement=rs.getString("date_recrutement");
					String formation=rs.getString("formation");
					String structure_ent=rs.getString("structure_ent");
					String intitule_poste=rs.getString("intitule_poste");

					String famille=rs.getString("famille");
					String moy_par_famille=rs.getString("moy_par_famille");
					String imi=rs.getString("imi");


					String cles1=matricule+"#"+nom+"#"+date_naissance+"#"+date_recrutement+"#"+formation+"#"+structure_ent+"#"+intitule_poste+"#"+imi;

					if(mapEmployeFamille.containsKey(cles1))
					{
						HashMap<String, String> mapFamille=mapEmployeFamille.get(cles1);
						if(mapFamille.containsKey(famille))
						{

							mapFamille.put(famille, moy_par_famille);
							mapEmployeFamille.put(cles1, mapFamille);
						}
						else
						{

							mapFamille.put(famille, moy_par_famille);
							mapEmployeFamille.put(cles1, mapFamille);
						}

					}
					else
					{
						HashMap< String, String>mapFamille=new  HashMap< String, String>();
						listCles.add(cles1);
						mapFamille.put(famille, moy_par_famille);
						mapEmployeFamille.put(cles1, mapFamille);
					}


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
		return mapEmployeFamille;
	}


	public List<MatriceCotationBean> getMatriceCotationExlFileNew(String id_compagne){
		HashMap<String, HashMap<String,String>> mapEmployeFamille=new HashMap<String, HashMap<String,String>>();
		List<MatriceCotationBean> employees = new ArrayList<MatriceCotationBean>();
		List<EmployeFamilleBean> famillebean = new ArrayList<EmployeFamilleBean>();

		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select login matricule,concat(e.nom, concat(\" \",e.prenom)) as nom,e.date_naissance,e.date_recrutement,d.niv_for_libelle formation,structure_ent,p.intitule_poste ,famille,round(moy_par_famille,2) moy_par_famille ,round(imi,2) imi "
					+ " from poste_travail_description p , employe e, common_evalcom.compte c, formation f , def_niv_formation d, imi_stats s  ,"
					+ "	 ("
					+ "   select code_structure, structure_ent from ("
					+ "   select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
					+ "   and  libelle_section !='null' and  libelle_section !=''"
					+ "   union"
					+ "	 select code_structure,libelle_service structure_ent from structure_entreprise"
					+ "  where libelle_service is  not null and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
					+ "  union"
					+ " select code_structure,libelle_departement structure_ent from structure_entreprise"
					+ " where libelle_departement is  not null and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_sous_direction structure_ent from structure_entreprise"
					+ " where libelle_sous_direction is  not null and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_unite structure_ent from structure_entreprise"
					+ " where libelle_unite is  not null and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ " and length(libelle_service)=0 and  length(libelle_section) =0"
					+ " union"
					+ " select code_structure,libelle_direction structure_ent from structure_entreprise"
					+ " where libelle_direction is  not null and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ " and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise ) t, (select distinct code_famille,famille from repertoire_competence) r"
					+ " where e.code_structure=t.code_structure and p.code_poste=e.code_poste  and s.id_employe=e.id_employe"
					+ " and s.id_compagne=#id_compagne  and s.code_famille=r.code_famille  and c.id_compte=e.id_compte"
					+ " and f.code_formation=e.code_formation and d.niv_for_id=f.niv_for_id"
					+ " union"
					+" select '99999999999' matricule,'DUMMY' as nom,'01/01/3999' date_naissance,'01/01/3999' date_recrutement,'DUMMY' formation,'ZZZZZZZZZZZZZZ' structure_ent,'DUMMY' intitule_poste ,'DUMMY' famille, 99 moy_par_famille , 99 imi"
					+ " order by  structure_ent,imi desc ,nom ";



			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);
			//System.out.println("#id_compagne>>"+ id_compagne+"#sql>>"+select_structure);
			rs = (ResultSet) stmt.executeQuery(select_structure);
			String cles="";
			String cles_bckp="";
			String RELATIONNELLES_moy="";
			String PERSONNELLES_moy="";
			String OPERATIONNELLES_moy="";
			String AFFAIRES_moy="";
			String[] listDonnee=null;
			String structure_ent="";
			String structure_ent_bckp="";
			int order=1;


			while(rs.next())
			{

				structure_ent=rs.getString("structure_ent");

				String matricule=rs.getString("matricule");
				String nom=rs.getString("nom");
				String date_naissance=rs.getString("date_naissance");
				String date_recrutement=rs.getString("date_recrutement");
				String formation=rs.getString("formation");

				String intitule_poste=rs.getString("intitule_poste");
				//String moy_par_famille=rs.getString("moy_par_famille");
				String imi=rs.getString("imi");
				cles=matricule+"#"+nom+"#"+date_naissance+"#"+date_recrutement+"#"+formation+"#"+structure_ent+"#"+intitule_poste+"#"+imi;

				if (!cles_bckp.equalsIgnoreCase(cles)){

					listDonnee=cles_bckp.split("#");
					//employees.add( new MatriceCotationBean(matricule, nom, date_naissance, date_recrutement,formation,structure_ent,intitule_poste,imi) );
					if (!listDonnee[0].equalsIgnoreCase("")){
						EmployeFamilleBean beans=new EmployeFamilleBean(listDonnee[0],OPERATIONNELLES_moy,PERSONNELLES_moy,AFFAIRES_moy,RELATIONNELLES_moy);
						employees.add( new MatriceCotationBean(order,listDonnee[0], listDonnee[1], listDonnee[2], listDonnee[3],listDonnee[4],listDonnee[5],listDonnee[6],Double.parseDouble(listDonnee[7]),beans) );
						//vider la chaine pour le prochain employé 
						cles_bckp="";
						if (structure_ent.equalsIgnoreCase(structure_ent_bckp) || structure_ent_bckp.equalsIgnoreCase("") ){
							order =order+1;
						}else{
							order=1;
						}
					}

				}
				//vérifier si on est dans le cas d'une nouvelle ligne pour un employé cles_bckp="" ou on est en train de 
				//parcourir les compétence du meme employé 
				if (cles_bckp.equalsIgnoreCase(cles)|| cles_bckp.equalsIgnoreCase("")){

					if (rs.getString("famille").equalsIgnoreCase("RELATIONNELLES")){

						RELATIONNELLES_moy=rs.getString("moy_par_famille");

					}else if (rs.getString("famille").equalsIgnoreCase("PERSONNELLES")){
						PERSONNELLES_moy= rs.getString("moy_par_famille");;

					}else if (rs.getString("famille").equalsIgnoreCase("OPERATIONNELLES")){
						OPERATIONNELLES_moy= rs.getString("moy_par_famille");;

					}else if (rs.getString("famille").equalsIgnoreCase("AFFAIRES")){
						AFFAIRES_moy= rs.getString("moy_par_famille");;

					}else{
						double pas_valeur=0;
					}


				}


				cles_bckp=cles;
				structure_ent_bckp=structure_ent;


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
		return employees;
	}

}
