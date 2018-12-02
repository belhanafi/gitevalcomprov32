package administration.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import administration.action.StructureEntreprise;
import administration.bean.AdministrationLoginBean;
import administration.bean.EmployeCompteBean;
import administration.bean.FichePlanningBean;
import administration.bean.FichePosteBean;
import administration.bean.RepCompetenceBean;
import administration.bean.StructureEntrepriseBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import common.Contantes;
import common.CreateDatabaseCon;
import common.InitContext;
import common.PwdCrypt;
import common.StringUtils;

public class  FichePlanningModel {

	private ArrayList<FichePlanningBean>  listcomp =null; 




	public List loadListPlanning() throws SQLException{

		ArrayList<FichePlanningBean> listplanning = new ArrayList<FichePlanningBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		final InitContext intctx = new InitContext();
		intctx.loadProperties();
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp;

			sel_comp="select e.code_structure,concat (e.nom,e.prenom) evaluateur ,concat (s.nom,s.prenom) evalue,p.code_poste,p.date_evaluation,p.heure_debut_evaluation,"
					+ " p.heure_fin_evaluation,p.lieu,p.personne_ressources,date_fin_evaluation   from planning_evaluation p ,employe e, employe s"
					+ " where e.id_employe=p.id_evaluateur  and s.id_employe=p.id_employe";


			rs = (ResultSet) stmt.executeQuery(sel_comp);

			while(rs.next()){

				FichePlanningBean bean=new FichePlanningBean();

				bean.setStructure((rs.getString("code_structure")));
				bean.setEvaluateur((rs.getString("evaluateur")));	
				bean.setEvalue((rs.getString("evalue")));	
				bean.setCode_poste((rs.getString("code_poste")));	
				bean.setDate_evaluation((rs.getDate("date_evaluation")));	
				bean.setHeure_debut_eval((rs.getString("heure_debut_evaluation")));	
				bean.setHeure_fin_eval((rs.getString("heure_fin_evaluation")));
				bean.setLieu((rs.getString("lieu")));
				bean.setPersonne_ressource((rs.getString("personne_ressources")));	
				bean.setDate_fin_evaluation((rs.getDate("date_fin_evaluation")));	




				listplanning.add(bean);

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
		return listplanning;



	}



	/**
	 * cette méthode permet de vérifier l'integrite des données et retourne les données rejetés
	 * @return
	 */


	public HashMap <String,List<FichePlanningBean>> ChargementDonneedansBdd(List <FichePlanningBean> liste)throws Exception
	{
		//Verification de l'integrité des données à inserer doublon dans le fichier
		List <FichePlanningBean> listeAInserer=new ArrayList <FichePlanningBean>();
		List <FichePlanningBean> listeDonneesRejetes=new ArrayList <FichePlanningBean>();




		for(int i=0; i<liste.size();i++)
		{
			FichePlanningBean donnee=liste.get(i);
			boolean donneerejete=false;
			for(int j=i+1;j<liste.size();j++)
			{
				FichePlanningBean donnee2=liste.get(j);
				if(donnee.getEvalue().equals(donnee2.getEvalue()))
				{
					donnee.setCauseRejet("le code "+ donnee.getCode_poste()+" existe déja dans le fichier à inserer");
					listeDonneesRejetes.add(donnee);
					donneerejete=true;

				}
			}
			if((i==liste.size()-1)||(i==0)||(donneerejete==false))
				listeAInserer.add(donnee);

		}


		//Verification de l'integrité des données à inserer doublon avec les données de la base

		List <FichePlanningBean> listeAInsererFinal=new ArrayList <FichePlanningBean>();
		@SuppressWarnings("unchecked")
		List<FichePlanningBean>ficheplanningBdd =loadListPlanning();
		Iterator <FichePlanningBean>iterator=listeAInserer.iterator();

		while(iterator.hasNext())
		{

			FichePlanningBean bean=(FichePlanningBean)iterator.next();

			Iterator<FichePlanningBean> index=ficheplanningBdd.iterator();
			boolean donneerejete=false;
			while(index.hasNext())
			{

				FichePlanningBean bean2=(FichePlanningBean)index.next();
				if(bean.getCode_poste().equals(bean2.getCode_poste()))
				{
					bean.setCauseRejet("le code "+ bean.getCode_poste()+" existe déja dans la base de données");
					listeDonneesRejetes.add(bean);
					donneerejete=true;
					continue;
				}
			}
			if(!donneerejete)
			{

				listeAInsererFinal.add(bean);
			}

		}

		//Insertion des données dans la table Structure_entreprise
		Iterator<FichePlanningBean> index =listeAInsererFinal.iterator();
		String requete="";
		while(index.hasNext())
		{
			FichePlanningBean donneeBean=(FichePlanningBean)index.next();
			ConstructionRequeteAddPlannning(donneeBean);
			//addPosteTravail(donneeBean);			
		}
		//execution de la requete
		/*try
		{
			//System.out.println(requete);
			if(requete!="")
				updateMultiQuery(requete);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}*/
		HashMap <String,List<FichePlanningBean>> donneeMap=new HashMap<String,List<FichePlanningBean>>();
		donneeMap.put("inserer", listeAInsererFinal);
		donneeMap.put("supprimer", listeDonneesRejetes);
		return donneeMap;
	}


	public void ConstructionRequeteAddPlannning(FichePlanningBean addedData) throws SQLException 
	{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


		PreparedStatement pstmt = null;
		try {

			String requete_Sql="insert into planning_evaluation (id_compagne, id_employe, date_evaluation, id_evaluateur, heure_debut_evaluation, heure_fin_evaluation,"
					+ " lieu,  personne_ressources, code_poste,code_structure,date_fin_evaluation)"
					+ "	select (select max(id_compagne) from compagne_evaluation ),(select (id_employe)"
					+ " from employe where  trim(REPLACE(concat (nom,concat('  ',prenom)),' ',''))=trim(REPLACE((?),' ',''))),?,"
					+ " (select (id_employe) from employe where  trim(REPLACE(concat (nom,concat('  ',prenom)),' ',''))=trim(REPLACE((?),' ',''))),"
					+ " ?, ?,?,?,?,?,?  from employe  where  trim(REPLACE(concat (nom,concat('  ',prenom)),' ',''))=trim(REPLACE((?),' ','')) ";

			pstmt = conn.prepareStatement(requete_Sql);
			
		

			pstmt.setString(1, addedData.getEvalue().replaceAll("'"," "));
			pstmt.setString(2, formatter.format(addedData.getDate_evaluation()));
			pstmt.setString(3, addedData.getEvaluateur().replaceAll("'"," "));
			pstmt.setString(4, addedData.getHeure_debut_eval());
			pstmt.setString(5, addedData.getHeure_fin_eval());
			pstmt.setString(6, addedData.getLieu().replaceAll("'"," "));
			pstmt.setString(7, addedData.getPersonne_ressource().replaceAll("'"," "));
			pstmt.setString(8, addedData.getCode_poste());
			pstmt.setString(9, addedData.getStructure());
			pstmt.setString(10, formatter.format(addedData.getDate_fin_evaluation()));
			pstmt.setString(11, addedData.getEvalue().replaceAll("'"," "));
			

			///System.out.println(pstmt.toString());




			int row= pstmt.executeUpdate();
			if (row!=1){
				System.out.println(pstmt.toString());
			}
			//pstmt.close();conn.close();

		} catch ( SQLException e ) {
			e.printStackTrace();
			System.out.println("Exception ors de l'insertion du poste de travail avecla requete  "+pstmt);

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






	}


	public String getMaxKeyCode() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		String result ="";
		ResultSet rs=null;
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select max(code_poste) as max_code from poste_travail_description"; 
			rs = (ResultSet) stmt.executeQuery(profile_list);


			while(rs.next()){
				result=rs.getString("max_code");
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

		return result;
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




	public static String getNextCode(String charto,String code) {

		String nextvalue=
				"XXXX";

		if (code==null || code.length()==0){

			nextvalue=charto+
					"001";

			return nextvalue;

		}

		String[]list=code.split(charto);

		String chaine=String.valueOf(Integer.parseInt(list[1])+1);

		if (chaine.length() <4){

			if (chaine.length()==1){

				chaine=charto+"00"+chaine;

			}

			if (chaine.length()==2){

				chaine=charto+"0"+chaine;

			}

			if (chaine.length()==3){

				chaine=charto+chaine;

			}

			nextvalue=chaine;

		}

		return nextvalue;

	}


	public List <FichePosteBean> uploadXLSFile(InputStream inputStream)
	{

		ArrayList <FichePosteBean> listFichePostebean=new ArrayList<FichePosteBean>();

		// Creer l'objet representant le fichier Excel
		try 
		{
			HSSFWorkbook fichierExcel = new HSSFWorkbook(inputStream);
			// creer l'objet representant les lignes Excel
			HSSFRow ligne;

			// creer l'objet representant les cellules Excel
			HSSFCell cellule;

			//lecture de la première feuille excel
			HSSFSheet feuilleExcel=fichierExcel.getSheet(Contantes.onglet_fiche_postes);

			// lecture du contenu de la feuille excel
			int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();



			for(int numLigne =1;numLigne<=nombreLigne; numLigne++)
			{


				ligne = feuilleExcel.getRow(numLigne);
				if(ligne!=null)
				{

					int nombreColonne = ligne.getLastCellNum()
							- ligne.getFirstCellNum();
					FichePosteBean fichePoste=new FichePosteBean();
					// parcours des colonnes de la ligne en cours
					short numColonne=-1;
					boolean inserer=true;
					while( (numColonne < nombreColonne)&&(inserer) )
					{
						numColonne++;


						cellule = ligne.getCell(numColonne);
						Double v;
						String valeur="";
						if(cellule!=null)
						{
							inserer=true;

							if(numColonne==7)
							{
								try
								{
									v=cellule.getNumericCellValue();
									valeur=v.toString();
								}
								catch(Exception e)
								{
									valeur=cellule.getStringCellValue();
								}
							}
							else
								valeur= cellule.getStringCellValue();


							if(numColonne==0)
							{
								if(valeur==null)
								{
									inserer=false;
								}
								else
									if(valeur.equals("")||valeur.equals(" "))
										inserer=false;
								fichePoste.setCode_poste(valeur);
							}
							else
								if(numColonne==1)
								{
									fichePoste.setIntitule_poste(valeur);
								}
								else
									if(numColonne==2)
									{
										fichePoste.setSommaire_poste(valeur);
									}
									else
										if(numColonne==3)
										{
											fichePoste.setTache_responsabilite(valeur);
										}
										else
											if(numColonne==4)
											{
												fichePoste.setEnvironement_perspectif(valeur);
											}
											else
												if(numColonne==5)
												{
													String[] val=valeur.split(",");
													fichePoste.setFormation_general(val[0]);
													if(val.length>1)
														fichePoste.setLibelle_formation(val[1]);
												}
												else
													if(numColonne==6)
													{
														fichePoste.setFormation_professionnelle(valeur);
													}
													else
														if(numColonne==7)
														{
															fichePoste.setExperience(valeur);
														}
														else
															if(numColonne==8)
															{
																fichePoste.setProfile_poste(valeur);
															}
															else
																if(numColonne==9)
																{
																	fichePoste.setPoste_hierarchie(valeur);
																}
																else
																	if(numColonne==10)
																	{
																		String[] val=valeur.split(",");
																		fichePoste.setCode_structure(val[0]);
																	}
																	else
																		if(numColonne==11)
																		{
																			fichePoste.setCode_gsp(valeur);
																		}


						}
						else
							if(numColonne==0)
								inserer=false;
					}//fin for colonne
					if(inserer)
						listFichePostebean.add(fichePoste);
				}
			}//fin for ligne

		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}



		return listFichePostebean;
	}

	/**
	 * Cette méthode permet de faire un upload d'un fichier xlsx (fichier vers BDD)
	 * @return
	 */
	public List <FichePlanningBean> uploadXLSXFile(InputStream inputStream)
	{

		ArrayList <FichePlanningBean> listPlanningBean=new ArrayList<FichePlanningBean>();

		// Creer l'objet representant le fichier Excel
		try 
		{
			XSSFWorkbook fichierExcel = new XSSFWorkbook(inputStream);
			// creer l'objet representant les lignes Excel
			XSSFRow ligne;

			// creer l'objet representant les cellules Excel
			XSSFCell cellule;

			//lecture de la première feuille excel
			XSSFSheet feuilleExcel=fichierExcel.getSheet(Contantes.onglet_planning_evaluation);

			// lecture du contenu de la feuille excel
			int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();

			for(int numLigne =1;numLigne<=nombreLigne; numLigne++)
			{

				ligne = feuilleExcel.getRow(numLigne);
				if(ligne!=null)
				{
					int nombreColonne = ligne.getLastCellNum()
							- ligne.getFirstCellNum();
					FichePlanningBean planningBean=new FichePlanningBean();
					// parcours des colonnes de la ligne en cours
					short numColonne=-1;
					boolean inserer=true;
					while( (numColonne < nombreColonne)&&(inserer) )
					{
						numColonne++;
						inserer=true;


						cellule = ligne.getCell(numColonne);
						if(cellule!=null)
						{

							String valeur="";
							Date date=null;
							if((numColonne==4)|| (numColonne==6))
							{
								try
								{
									//date= cellule.getDateCellValue().toString();
									date=cellule.getDateCellValue();
								}
								catch(Exception e)
								{
									System.out.println("erreur lors de la lecture d'une colonne date numColonne="+numColonne +" valeur saiaie="+cellule.toString()+ " numero de ligne= "+numLigne);
									try {
										//throw new Exception (e);
										date=StringToDate(cellule.getStringCellValue());

									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
							else
							{
								try
								{
									valeur=cellule.getStringCellValue();
								}
								catch(Exception e)
								{
									double val=cellule.getNumericCellValue();
								}
							}

							if(numColonne==0)
							{
								if(valeur==null)
									inserer=false;
								else
									if(valeur.equals("")||valeur.equals(" "))
										inserer=false;
								String val[]=valeur.split(",");
								planningBean.setStructure(val[0]);
							}
							else
								if(numColonne==1)
								{
									String[] val=valeur.split(",");
									planningBean.setEvaluateur(val[0]+" "+val[1]);
								}
								else
									if(numColonne==2)
									{
										String[] val=valeur.split(",");
										planningBean.setEvalue(val[0]+" "+val[1]);
									}
									else
										if(numColonne==3)
										{
											String[] val=valeur.split(",");
											planningBean.setCode_poste(val[0]);
										}
										else
											if(numColonne==4)
											{
												planningBean.setDate_evaluation(date);
											}
											else
												if(numColonne==5)
												{

													planningBean.setHeure_debut_eval(valeur);
												}
												else
													if(numColonne==6)
													{
														planningBean.setDate_fin_evaluation(date);
													}
							
													else
														if(numColonne==7)
														{
															planningBean.setHeure_fin_eval(valeur);
														}
													else
														if(numColonne==8)
														{
															planningBean.setLieu(valeur);
														}
														else
															if(numColonne==9)
															{
																planningBean.setPersonne_ressource(valeur);
															}



						}
						else
							if(numColonne==0)
								inserer=false;
					}//fin for colonne
					if(inserer)
						listPlanningBean.add(planningBean);
				}
			}//fin for ligne

		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return listPlanningBean;
	}
	
	/****
	 * suppression  du planning de la derniere compagne
	 */
	public void supprimerPlanning() throws SQLException{

		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		final InitContext intctx = new InitContext();
		intctx.loadProperties();
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp;

			sel_comp="delete from planning_evaluation where id_compagne in (select max(id_compagne) from compagne_evaluation) ";


			rs = (ResultSet) stmt.executeQuery(sel_comp);

			

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
	



	}
	private   Date StringToDate(String strDate)   {
		Date dtReturn = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
		try {
			dtReturn = simpleDateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dtReturn;
	}

	private   String  DateToString(Date strDate)   {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
		String datestring = simpleDateFormat.format(strDate); 
		return datestring;
	}
	

}
