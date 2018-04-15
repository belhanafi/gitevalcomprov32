package compagne.model;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import common.CreateDatabaseCon;
import compagne.bean.EvaluationParCompetenceBean;

public class ResultatEvaluationModel {
	
	
	// le type retour est a  modifier plus tartd
	public ArrayList<String> getListeCompagnes()
	{
		ArrayList <String > listCompagnes=new ArrayList<String>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT id_compagne, date_debut, libelle_compagne  FROM compagne_evaluation ";
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String id_compagne=rs.getString("id_compagne");
					String date_debut=rs.getString("date_debut");
					String libelle_compagne=rs.getString("libelle_compagne");
					listCompagnes.add(id_compagne+"#"+date_debut+"#"+libelle_compagne);
					
	
				}
				else {
					return listCompagnes;
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
		return listCompagnes;
	}
	
	public HashMap <String, HashMap<String, ArrayList<String>>> getInfosFamillesCompetence(String id_compagne)
	{
		
		HashMap <String, HashMap<String, ArrayList<String>>>	 mapPosteFamilleCompetence	=new HashMap <String, HashMap<String, ArrayList<String>>>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select distinct p.intitule_poste,  r.famille, r.libelle_competence from repertoire_competence r, poste_travail_description p, poste_travail_competence l where p.code_poste=l.code_poste and r.code_competence=l.code_competence and p.code_poste in(select distinct v.code_poste from img_stats v where id_compagne=#id_compagne)";
			
			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
				else {
					return mapPosteFamilleCompetence;
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
		return mapPosteFamilleCompetence;
	}
	
	public HashMap<String, HashMap<String, HashMap< String, HashMap<String, Double>> >> getAllIMICompetence(String id_compagne)
	{
		HashMap<String, HashMap<String, HashMap< String, HashMap<String, Double>> >> mapposteEmployeFamilleCompIMI=new HashMap<String, HashMap<String, HashMap< String, HashMap<String, Double>> >>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
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
			" from poste_travail_description f, imi_competence_stat c, employe e, repertoire_competence r "+
			" where c.id_employe=e.id_employe "+
			" and c.id_compagne=#id_compagne "+
			" and c.code_famille=r.code_famille "+
			" and c.code_competence=r.code_competence "+
			" and e.code_poste=f.code_poste"			;
			
			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
				else {
					return mapposteEmployeFamilleCompIMI;
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
		return mapposteEmployeFamilleCompIMI;
	}
	
	public HashMap<String, HashMap<String, String>> getInfosIMIStat(String id_compagne)
	{
		HashMap<String, HashMap<String, String>> mapEmployeFamilleIMI=new HashMap<String, HashMap<String, String>>();
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
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
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
				else {
					return mapEmployeFamilleIMI;
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
		
		return mapEmployeFamilleIMI;
	}
	
	public HashMap <String, Double> getIMGparPoste(String id_compagne)
	{
		HashMap <String, Double> mapPosteIMG=new HashMap<String, Double>();
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="select distinct p.intitule_poste, round(i.img, 2) as img from poste_travail_description p, img_stats i where p.code_poste=i.code_poste and i.id_compagne=#id_compagne";
			String select_structure="select distinct p.intitule_poste, i.img from poste_travail_description p, img_stats i where p.code_poste=i.code_poste and i.id_compagne=#id_compagne";
			
			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
				else {
					return mapPosteIMG;
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
		return mapPosteIMG;
	}
	public HashMap <String, HashMap<String, Double>> getIMGFamille(String id_compagne)
	{
		HashMap <String, HashMap<String, Double>> mapIMGPosteFamille=new HashMap <String, HashMap<String, Double>>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
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
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
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
				else {
					return mapIMGPosteFamille;
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
		return mapIMGPosteFamille;
		
	}
	
	public HashMap<String, HashMap<String, HashMap< String, Double>>> getmoyPosteCompetenceStats(String id_compagne)
	{
		HashMap<String, HashMap<String, HashMap< String, Double>>> mapPostFamilleCompetenceStats=new HashMap<String, HashMap<String, HashMap< String, Double>>>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
//			String select_structure="select distinct f.intitule_poste, c.code_famille,r.famille,   r.libelle_competence, round(c.moy_par_competence,2) as moy_par_competence   " 
//				+" from poste_travail_description f, moy_poste_competence_stats c, employe e, repertoire_competence r "  
//				+" where c.id_compagne=#id_compagne " 
//				+" and c.code_famille=r.code_famille  " 
//				+"  and c.code_competence=r.code_competence " 
//				+" and e.code_poste=f.code_poste";

			String select_structure="select distinct f.intitule_poste, c.code_famille,r.famille,   r.libelle_competence, c.moy_par_competence  " 
				+" from poste_travail_description f, moy_poste_competence_stats c, employe e, repertoire_competence r "  
				+" where c.id_compagne=#id_compagne " 
				+" and c.code_famille=r.code_famille  " 
				+"  and c.code_competence=r.code_competence " 
				+" and e.code_poste=f.code_poste";
			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String intitule_poste=rs.getString("intitule_poste");
					String code_famille=rs.getString("code_famille");
					String famille=rs.getString("famille");
					
					String libelle_competence=rs.getString("libelle_competence");
					Double moy_par_competence=rs.getDouble("moy_par_competence");
					
					
					/**************************/
					DecimalFormat df = new DecimalFormat("########.00"); 
					df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
					df.setMinimumFractionDigits ( 2 ) ;
					df.setDecimalSeparatorAlwaysShown ( true ) ;
					String f=df.format(moy_par_competence);
					moy_par_competence= Double.parseDouble(f.replace(',', '.'));
					
					/*****************************/
					if(mapPostFamilleCompetenceStats.containsKey(intitule_poste))
					{
						HashMap<String, HashMap< String, Double>>mapFamilleCompetence=mapPostFamilleCompetenceStats.get(intitule_poste);
						if(mapFamilleCompetence.containsKey(famille))
						{
							HashMap<String, Double> mapCompetence=mapFamilleCompetence.get(famille);
							if(mapCompetence.containsKey(libelle_competence))
							{
								mapCompetence.put(libelle_competence, moy_par_competence);
								mapFamilleCompetence.put(famille, mapCompetence);
								mapPostFamilleCompetenceStats.put(intitule_poste, mapFamilleCompetence);
							}
							else
							{
								mapCompetence.put(libelle_competence, moy_par_competence);
								mapFamilleCompetence.put(famille, mapCompetence);
								mapPostFamilleCompetenceStats.put(intitule_poste, mapFamilleCompetence);
							}

						}
						else
						{
							HashMap< String, Double>mapCompetence=new  HashMap< String, Double>();
							mapCompetence.put(libelle_competence,  moy_par_competence);
							mapFamilleCompetence.put(famille, mapCompetence);
							mapPostFamilleCompetenceStats.put(intitule_poste, mapFamilleCompetence);
						}
					}
					else
					{
						HashMap<String, Double> mapCompetence=new HashMap<String, Double>();
						mapCompetence.put(libelle_competence, moy_par_competence);
						HashMap<String,HashMap<String, Double> > mapFamilleCompetence=new HashMap<String,HashMap<String, Double> >();
						mapFamilleCompetence.put(famille, mapCompetence);
						
						mapPostFamilleCompetenceStats.put(intitule_poste, mapFamilleCompetence);
					}
	
				}
				else {
					return mapPostFamilleCompetenceStats;
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
		return mapPostFamilleCompetenceStats;
	}
	
	public HashMap<String, ArrayList<String>> getEmployeTriIMI(String id_compagne)
	{
		HashMap<String, ArrayList<String>> mapPostEmployeTriIMI=new HashMap<String, ArrayList<String>>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
//			String select_structure="select distinct e.nom, e.prenom,  round (i.imi, 2) as imi , p.intitule_poste "
//			 +" from employe e,  imi_stats i , poste_travail_description p "
//			 +" where i.id_employe=e.id_employe "  
//			 +" and p.code_poste=e.code_poste "
//			 +" and id_compagne=#id_compagne  ORDER BY imi DESC";
			
			String select_structure="select distinct e.nom, e.prenom,  i.imi,p.intitule_poste "
				 +" from employe e,  imi_stats i , poste_travail_description p "
				 +" where i.id_employe=e.id_employe "  
				 +" and p.code_poste=e.code_poste "
				 +" and id_compagne=#id_compagne  ORDER BY imi DESC";			
			
			select_structure = select_structure.replaceAll("#id_compagne", id_compagne);
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String nom=rs.getString("nom");
					String prenom=rs.getString("prenom");
					String employe=nom+" " +prenom;
					//Double imi=rs.getDouble("imi"); 
					String intitule_poste=rs.getString("intitule_poste");
					
					if(mapPostEmployeTriIMI.containsKey(intitule_poste))
					{
						mapPostEmployeTriIMI.get(intitule_poste).add(employe);
					}
					else
					{
						ArrayList<String> listEmploye=new ArrayList<String>();
						listEmploye.add(employe);
						mapPostEmployeTriIMI.put(intitule_poste, listEmploye);
					}
					
	
				}
				else {
					return mapPostEmployeTriIMI;
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
		return mapPostEmployeTriIMI;
	}

}
