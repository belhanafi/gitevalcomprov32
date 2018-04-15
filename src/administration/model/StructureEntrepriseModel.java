package administration.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;



import administration.bean.StructureEntrepriseBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import common.Contantes;
import common.CreateDatabaseCon;
import common.StringUtils;

public class StructureEntrepriseModel {
	
	private ArrayList<StructureEntrepriseBean>  listStructureEntreprise =null; 
	
	/**
	 * cette méthode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<StructureEntrepriseBean> checkStructureEntreprise() throws SQLException{
		
		listStructureEntreprise = new ArrayList<StructureEntrepriseBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT * FROM structure_entreprise";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next()){
				if (rs.getRow()>=1) {
					StructureEntrepriseBean structureEntreprise=new StructureEntrepriseBean();
					structureEntreprise.setCodestructure(rs.getString("code_structure"));
					structureEntreprise.setCodeDivision(rs.getString("code_division"));
					structureEntreprise.setLibelleDivision(rs.getString("libelle_division"));
					structureEntreprise.setCodeDirection(rs.getString("code_direction"));
					structureEntreprise.setLibelleDirection(rs.getString("libelle_direction"));
					structureEntreprise.setCodeUnite(rs.getString("code_unite"));
					structureEntreprise.setLibelleUnite(rs.getString("libelle_unite"));
					structureEntreprise.setCodeSousDirection(rs.getString("code_sous_direction"));
					structureEntreprise.setLibelleSousDirection(rs.getString("libelle_sous_direction"));
					structureEntreprise.setCodeDepartement(rs.getString("code_departement"));
					structureEntreprise.setLibelleDepartement(rs.getString("libelle_departement"));
					structureEntreprise.setCodeService(rs.getString("code_service"));
					structureEntreprise.setLibelleService(rs.getString("libelle_service"));
					structureEntreprise.setCodesection(rs.getString("code_section"));
					structureEntreprise.setLibelleSection(rs.getString("libelle_section"));
					listStructureEntreprise.add(structureEntreprise);
				   
					
				}else {
					rs.close();
					stmt.close();
					conn.close();
					return listStructureEntreprise;
				}
				
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			conn.close();
		}
		 finally {
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
		
			
		return listStructureEntreprise;	
	
		
		
	}
	/**
	 * cette méthode permet de supprimer le contenu de la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	public boolean viderTableStructureEntreprise()  throws Exception
	{
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="DELETE * FROM structure_entreprise)";

			
			 stmt.execute(select_structure);
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("Erreur lors de la suppression du contenu de la table structure_entreprise", "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				
				e1.printStackTrace();
				//return false;
			}
			
			
			return false;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
	 * @param addedData
	 * @return
	 */
	public boolean addStructureEntrepriseBean(StructureEntrepriseBean addedData)
	{
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Boolean retour=false;
		StringUtils stringUtils=new StringUtils();
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="INSERT INTO structure_entreprise (code_structure,code_division,libelle_division,code_direction, libelle_direction,code_unite,libelle_unite,code_sous_direction,libelle_sous_direction,code_departement,libelle_departement,code_service,libelle_service,code_section,libelle_section) VALUES (#code_structure,#code_division,#libelle_division,#code_direction,#libelle_direction,#code_unite,#libelle_unite,#code_sous_direction,#libelle_sous_direction,#code_departement,#libelle_departement,#code_service,#libelle_service,#code_section,#libelle_section)";
			select_structure = select_structure.replaceAll("#code_structure", "'"+addedData.getCodestructure()+"'");
			select_structure = select_structure.replaceAll("#code_division", "'"+addedData.getCodeDivision()+"'");
			select_structure = select_structure.replaceAll("#libelle_division", "'"+stringUtils.removeString(addedData.getLibelleDivision())+"'");
			select_structure = select_structure.replaceAll("#code_direction", "'"+addedData.getCodeDirection()+"'");
			select_structure = select_structure.replaceAll("#libelle_direction", "'"+stringUtils.removeString(addedData.getLibelleDirection())+"'");
			select_structure = select_structure.replaceAll("#code_unite", "'"+addedData.getCodeUnite()+"'");
			select_structure = select_structure.replaceAll("#libelle_unite", "'"+stringUtils.removeString(addedData.getLibelleUnite())+"'");
			select_structure = select_structure.replaceAll("#code_sous_direction", "'"+addedData.getCodeSousDirection()+"'");
			select_structure = select_structure.replaceAll("#libelle_sous_direction", "'"+stringUtils.removeString(addedData.getLibelleSousDirection())+"'");
			select_structure = select_structure.replaceAll("#code_departement", "'"+addedData.getCodeDepartement()+"'");
			select_structure = select_structure.replaceAll("#libelle_departement", "'"+stringUtils.removeString(addedData.getLibelleDepartement())+"'");
			select_structure = select_structure.replaceAll("#code_service", "'"+addedData.getCodeService()+"'");
			select_structure = select_structure.replaceAll("#libelle_service", "'"+stringUtils.removeString(addedData.getLibelleService())+"'");
			select_structure = select_structure.replaceAll("#code_section", "'"+addedData.getCodesection()+"'");
			select_structure = select_structure.replaceAll("#libelle_section", "'"+stringUtils.removeString(addedData.getLibelleSection())+"'");
			
			
			
			 stmt.execute(select_structure);
			 retour=true;
			// System.out.println(select_structure);
//			 stmt.close();
//			 conn.close();
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
	 * cette classe permet de controler la validité des données insérées (par rapport à leurs taille)
	 * @param addedData
	 * @return
	 */
	public boolean controleIntegrite(StructureEntrepriseBean addedData)
	{
		try 
		{
			if(addedData.getCodestructure().length()>5)
			{
				Messagebox.show("La taille du champ Code structure ne doit pas dépasser 5 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			else
				if(addedData.getCodeDivision().length()>4)
				{
					Messagebox.show("La taille du champ Code division ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
					return false;
				}
				else
					if(addedData.getLibelleDivision().length()>50)
					{
						Messagebox.show("La taille du champ Libelle division ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
						return false;
					}
					else
						if(addedData.getCodeDirection().length()>4)
						{
							Messagebox.show("La taille du champ Code direction ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
							return false;
						}
						else
							if(addedData.getLibelleDirection().length()>50)
							{
								Messagebox.show("La taille du champ Libelle direction ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
								return false;
							}
							else
								if(addedData.getCodeUnite().length()>4)
								{
									Messagebox.show("LaL taille du champ Code unite ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
									return false;
								}
								else
									if(addedData.getLibelleUnite().length()>50)
									{
										Messagebox.show("La taille du champ Libelle unite ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
										return false;
									}
									else
										if(addedData.getCodeSousDirection().length()>4)
										{
											Messagebox.show("La taille du champ Code sousdirection ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
											return false;
										}
										else
											if(addedData.getLibelleSousDirection().length()>50)
											{
												Messagebox.show("La taille du champ Libelle sous direction ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
												return false;
											}
											else
												if(addedData.getCodeDepartement().length()>4)
												{
													Messagebox.show("La taille du champ Code département ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
													return false;
												}
												else
													if(addedData.getLibelleDepartement().length()>50)
													{
														Messagebox.show("La taille du champ Libelle département ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
														return false;
													}
													else
														if(addedData.getCodeService().length()>4)
														{
															Messagebox.show("La taille du champ Code service ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
															return false;
														}
														else
															if(addedData.getLibelleService().length()>50)
															{
																Messagebox.show("La taille du champ Libelle service ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
																return false;
															}
															else
																if(addedData.getCodesection().length()>4)
																{
																	Messagebox.show("La taille du champ Code section ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
																	return false;
																}
																else
																	if(addedData.getLibelleSection().length()>5)
																	{
																		Messagebox.show("La taille du champ Libelle section ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
																		return false;
																	}
		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
			
		return true;
	}
	
	/**
	 * Cette classe permet de mettre à jour la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	public Boolean majStructureEntrepriseBean(StructureEntrepriseBean addedData, String selectedCodeStructure)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		Boolean retour=false;
		StringUtils stringUtils=new StringUtils();

		try 
		{
			stmt = (Statement) conn.createStatement();
			String update_structure="UPDATE  structure_entreprise SET code_structure=#code_structure ,code_division=#code_division,libelle_division=#libelle_division,code_direction=#code_direction, libelle_direction=#libelle_direction,code_unite=#code_unite,libelle_unite=#libelle_unite,  code_sous_direction=#code_sous_direction, libelle_sous_direction=#libelle_sous_direction,code_departement=#code_departement,libelle_departement=#libelle_departement,code_service=#code_service,libelle_service=#libelle_service,code_section=#code_section,libelle_section=#libelle_section WHERE code_structure=#valeur_code_structure"; 
			update_structure = update_structure.replaceAll("#code_structure", "'"+addedData.getCodestructure()+"'");
			update_structure = update_structure.replaceAll("#code_division", "'"+addedData.getCodeDivision()+"'");
			update_structure = update_structure.replaceAll("#libelle_division", "'"+stringUtils.removeString(addedData.getLibelleDivision())+"'");
			update_structure = update_structure.replaceAll("#code_direction", "'"+addedData.getCodeDirection()+"'");
			update_structure = update_structure.replaceAll("#libelle_direction", "'"+stringUtils.removeString(addedData.getLibelleDirection())+"'");
			update_structure = update_structure.replaceAll("#code_unite", "'"+addedData.getCodeUnite()+"'");
			update_structure = update_structure.replaceAll("#libelle_unite", "'"+stringUtils.removeString(addedData.getLibelleUnite())+"'");
			update_structure = update_structure.replaceAll("#code_sous_direction", "'"+addedData.getCodeSousDirection()+"'");
			update_structure = update_structure.replaceAll("#libelle_sous_direction", "'"+stringUtils.removeString(addedData.getLibelleSousDirection())+"'");
			update_structure = update_structure.replaceAll("#code_departement", "'"+addedData.getCodeDepartement()+"'");
			update_structure = update_structure.replaceAll("#libelle_departement", "'"+stringUtils.removeString(addedData.getLibelleDepartement())+"'");
			update_structure = update_structure.replaceAll("#code_service", "'"+addedData.getCodeService()+"'");
			update_structure = update_structure.replaceAll("#libelle_service", "'"+stringUtils.removeString(addedData.getLibelleService())+"'");
			update_structure = update_structure.replaceAll("#code_section", "'"+addedData.getCodesection()+"'");
			update_structure = update_structure.replaceAll("#libelle_section", "'"+stringUtils.removeString(addedData.getLibelleSection())+"'");
			update_structure = update_structure.replaceAll("#valeur_code_structure", "'"+selectedCodeStructure+"'");
		
			
			 stmt.executeUpdate(update_structure);
			 retour=true;
		} 

		catch ( SQLException e ) {
			retour=false;
			try 
			{
				Messagebox.show("La modification n'a pas été prise en compte car il existe une donnée ayant le même code établissement", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	public void supprimerStructureEntrepriseBean(String codeStructure)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String update_structure="DELETE FROM  structure_entreprise  WHERE code_structure=#code_structure"; 
			update_structure = update_structure.replaceAll("#code_structure", "'"+codeStructure+"'");
			
		
			
			 stmt.executeUpdate(update_structure);
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
	 * cette méthode permet de vérifier l'integrite des données et retourne les données rejetés
	 * @return
	 */
	public HashMap <String,List<StructureEntrepriseBean>> ChargementDonneedansBdd(List <StructureEntrepriseBean> liste)throws Exception
	{
		//Verification de l'integrité des données à inserer doublon dans le fichier
		List <StructureEntrepriseBean> listeAInserer=new ArrayList <StructureEntrepriseBean>();
		List <StructureEntrepriseBean> listeDonneesRejetes=new ArrayList <StructureEntrepriseBean>();
		
		for(int i=0; i<liste.size();i++)
		{
			StructureEntrepriseBean donnee=liste.get(i);
			boolean donneerejete=false;
			for(int j=i+1;j<liste.size();j++)
			{
				StructureEntrepriseBean donnee2=liste.get(j);
				if(donnee.getCodestructure().equals(donnee2.getCodestructure()))
				{
					donnee.setCauseRejet("le code "+ donnee.getCodestructure()+" existe déja dans le fichier à inserer");
					listeDonneesRejetes.add(donnee);
					
					donneerejete=true;
					
				}
			}
			if((i==liste.size()-1)||(i==0)||(donneerejete==false))
				listeAInserer.add(donnee);
			
		}
		
		//Verification de l'integrité des données à inserer doublon avec les données de la base
		
		List <StructureEntrepriseBean> listeAInsererFinal=new ArrayList <StructureEntrepriseBean>();
		ArrayList<StructureEntrepriseBean>strctureEntreprisebdd =checkStructureEntreprise();
		Iterator <StructureEntrepriseBean>iterator=listeAInserer.iterator();
		
		while(iterator.hasNext())
		{
			
			StructureEntrepriseBean bean=(StructureEntrepriseBean)iterator.next();
			
			Iterator<StructureEntrepriseBean> index=strctureEntreprisebdd.iterator();
			boolean donneerejete=false;
			while(index.hasNext())
			{
				
				
				StructureEntrepriseBean bean2=(StructureEntrepriseBean)index.next();
				
				if(bean.getCodestructure().equals(bean2.getCodestructure()))
				{
					bean.setCauseRejet("le code "+ bean.getCodestructure()+" existe déja dans la base de donnée");
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
		Iterator<StructureEntrepriseBean> index =listeAInsererFinal.iterator();
		while(index.hasNext())
		{
			StructureEntrepriseBean donneeBean=(StructureEntrepriseBean)index.next();
			
			addStructureEntrepriseBean(donneeBean);	
			
		}
		
			
			HashMap <String,List<StructureEntrepriseBean>> donneeMap=new HashMap<String,List<StructureEntrepriseBean>>();
			donneeMap.put("inserer", listeAInsererFinal);
			donneeMap.put("supprimer", listeDonneesRejetes);
		
		return donneeMap;
	}
	
	/**
	 * Cette méthode permet de faire un chargement d'un fichier xls 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public List <StructureEntrepriseBean> uploadStructureEntrepriseXLSFile(InputStream inputStream)
	{
		listStructureEntreprise=new ArrayList <StructureEntrepriseBean>();
		
		 // Creer l'objet representant le fichier Excel
        try 
        {
			HSSFWorkbook fichierExcel = new HSSFWorkbook(inputStream);
			// creer l'objet representant les lignes Excel
	        HSSFRow ligne;

	        // creer l'objet representant les cellules Excel
	        HSSFCell cellule;

	        //lecture de la première feuille excel
	        HSSFSheet feuilleExcel=fichierExcel.getSheet(Contantes.onglet_structure_entreprise);
	        
	        // lecture du contenu de la feuille excel
	        int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();
	        
	        for(int numLigne =1;numLigne<=nombreLigne; numLigne++)
	        {
	        	
	        	ligne = feuilleExcel.getRow(numLigne);
	        	if(ligne!=null)
	        	{
	            int nombreColonne = ligne.getLastCellNum()
	                    - ligne.getFirstCellNum();
	            StructureEntrepriseBean structurentreprise=new StructureEntrepriseBean();
	            // parcours des colonnes de la ligne en cours
	            boolean inserer=true;
	            short numColonne=-1;
	            while( (numColonne < nombreColonne)&&(inserer) )
	            {
	            	numColonne++;
	            	cellule = ligne.getCell(numColonne);
	            	if(cellule!=null)
	            	{
	            	inserer=true;
	            	String valeur= cellule.getStringCellValue();
	            	
	            	
	            	if(numColonne==0)
	            	{
	            		if(valeur==null)
	            			{
	            			inserer=false;
	            			
	            			}
	            			else
	            				if(valeur.equals("")||(valeur.equals(" ")))
	            					{
	            					inserer=false;;
	            					
	            					}
	            		structurentreprise.setCodestructure(valeur);
	            		
	            	}
	            	else
	            		if(numColonne==1)
		            	{
	            			structurentreprise.setCodeDivision(valeur);
		            	}
		            	else
		            		if(numColonne==2)
			            	{
		            			structurentreprise.setLibelleDivision(valeur);
			            	}
			            	else
			            		if(numColonne==3)
				            	{
			            			structurentreprise.setCodeDirection(valeur);
				            	}
				            	else
				            		if(numColonne==4)
					            	{
				            			structurentreprise.setLibelleDirection(valeur);
					            	}
					            	else
					            		if(numColonne==5)
						            	{
					            			structurentreprise.setCodeUnite(valeur);
						            	}
						            	else
						            		if(numColonne==6)
							            	{
						            			structurentreprise.setLibelleUnite(valeur);
							            	}
						            		else
							            		if(numColonne==7)
								            	{
							            			structurentreprise.setCodeSousDirection(valeur);
								            	}
							            		else
								            		if(numColonne==8)
									            	{
								            			structurentreprise.setLibelleSousDirection(valeur);
									            	}
									            	else
									            		if(numColonne==9)
										            	{
									            			structurentreprise.setCodeDepartement(valeur);
										            	}
										            	else
										            		if(numColonne==10)
											            	{
										            			structurentreprise.setLibelleDepartement(valeur);
											            	}
											            	else
											            		if(numColonne==11)
												            	{
											            			structurentreprise.setCodeService(valeur);
												            	}
												            	else
												            		if(numColonne==12)
													            	{
												            			structurentreprise.setLibelleService(valeur);
													            	}
												            		else
												            			if(numColonne==13)
												            			{
												            				structurentreprise.setCodesection(valeur);
												            			}
												            			else
												            				if(numColonne==14)
												            				{
												            					structurentreprise.setLibelleSection(valeur);
												            				}
	            	}
	            	else
	            		if(numColonne==0)
	            			inserer=false;
	            	
	            }//fin for colonne
	            if(inserer)listStructureEntreprise.add(structurentreprise);
	        	}
	        }//fin for ligne

		} 
        catch (IOException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        
		return listStructureEntreprise;
	}
	
	/**
	 * Cette méthode permet de faire un upload d'un fichier xlsx (fichier vers BDD)
	 * @return
	 */
	public List <StructureEntrepriseBean> uploadStructureEntrepriseXLSXFile(InputStream inputStream)
	{
		listStructureEntreprise=new ArrayList <StructureEntrepriseBean>();
		
		 // Creer l'objet representant le fichier Excel
        try 
        {
			XSSFWorkbook fichierExcel = new XSSFWorkbook(inputStream);
			// creer l'objet representant les lignes Excel
	        XSSFRow ligne;

	        // creer l'objet representant les cellules Excel
	        XSSFCell cellule;

	        //lecture de la première feuille excel
	        XSSFSheet feuilleExcel=fichierExcel.getSheet(Contantes.onglet_structure_entreprise);
	        
	        // lecture du contenu de la feuille excel
	        int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();
	        
	        for(int numLigne =1;numLigne<=nombreLigne; numLigne++)
	        {
	        	
	        	ligne = feuilleExcel.getRow(numLigne);
	        	if(ligne!=null)
	        	{
	            int nombreColonne = ligne.getLastCellNum()
	                    - ligne.getFirstCellNum();
	            StructureEntrepriseBean structurentreprise=new StructureEntrepriseBean();
	            // parcours des colonnes de la ligne en cours
	            boolean inserer=true;
	            short numColonne=-1;
	            while( (numColonne < nombreColonne)&&(inserer) )
	            {
	            	numColonne++;
	            	cellule = ligne.getCell(numColonne);
	            	if(cellule!=null)
	            	{
	            	inserer=true;
	            	//System.out.println("numColonne"+numColonne+" Ligne="+numLigne+" nombreLignes="+nombreLigne);
	            	String valeur= cellule.getStringCellValue();
	            	
	            	
	            	if(numColonne==0)
	            	{
	            		if(valeur==null)
	            			{
	            			inserer=false;
	            			
	            			}
	            			else
	            				if(valeur.equals("")||(valeur.equals(" ")))
	            					{
	            					inserer=false;;
	            					
	            					}
	            		structurentreprise.setCodestructure(valeur);
	            		
	            	}
	            	else
	            		if(numColonne==1)
		            	{
	            			structurentreprise.setCodeDivision(valeur);
		            	}
		            	else
		            		if(numColonne==2)
			            	{
		            			structurentreprise.setLibelleDivision(valeur);
			            	}
			            	else
			            		if(numColonne==3)
				            	{
			            			structurentreprise.setCodeDirection(valeur);
				            	}
				            	else
				            		if(numColonne==4)
					            	{
				            			structurentreprise.setLibelleDirection(valeur);
					            	}
					            	else
					            		if(numColonne==5)
						            	{
					            			structurentreprise.setCodeUnite(valeur);
						            	}
						            	else
						            		if(numColonne==6)
							            	{
						            			structurentreprise.setLibelleUnite(valeur);
							            	}
						            		else
							            		if(numColonne==7)
								            	{
							            			structurentreprise.setCodeSousDirection(valeur);
								            	}
							            		else
								            		if(numColonne==8)
									            	{
								            			structurentreprise.setLibelleSousDirection(valeur);
									            	}
									            	else
									            		if(numColonne==9)
										            	{
									            			structurentreprise.setCodeDepartement(valeur);
										            	}
										            	else
										            		if(numColonne==10)
											            	{
										            			structurentreprise.setLibelleDepartement(valeur);
											            	}
											            	else
											            		if(numColonne==11)
												            	{
											            			structurentreprise.setCodeService(valeur);
												            	}
												            	else
												            		if(numColonne==12)
													            	{
												            			structurentreprise.setLibelleService(valeur);
													            	}
												            		else
												            			if(numColonne==13)
												            			{
												            				structurentreprise.setCodesection(valeur);
												            			}
												            			else
												            				if(numColonne==14)
												            				{
												            					structurentreprise.setLibelleSection(valeur);
												            				}
	            	}
	            	else
	            		if(numColonne==0)
	            			inserer=false;
	            	
	            }//fin for colonne
	            if(inserer)listStructureEntreprise.add(structurentreprise);
	        	}
	        }//fin for ligne

		} 
        catch (IOException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        
		return listStructureEntreprise;
	}
	
	
	
	/**
	 * Cette méthode permet de charger le contenu de la table Structure_entreprise et de créer un fichier excel avec ces données
	 */
	public void downloadStructureEntrepriseDataToXls(HSSFWorkbook workBook)
	{
		
		//recupération du contenu de la table Structure_entreprise
		try 
		{
			ArrayList<StructureEntrepriseBean> listeStructureEntrepriseBean=checkStructureEntreprise();
			
			//creation du fichier xls
			
			Iterator <StructureEntrepriseBean>index=listeStructureEntrepriseBean.iterator();
			//creation d'un document excel 
			//HSSFWorkbook workBook = new HSSFWorkbook();
			
			//creation d'une feuille excel
			 HSSFSheet sheet = workBook.createSheet(Contantes.onglet_structure_entreprise);
			 
			 //creation de l'entête du document excel
			 HSSFRow row = sheet.createRow(0);
			 HSSFCell cell = row.createCell((short)0);
			 
			 HSSFCellStyle cellStyle = null;
			 HSSFFont font1 = workBook.createFont();
			 font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
			 font1.setFontHeightInPoints((short)8);
			 font1.setFontName("Arial");
			 
			 cellStyle = workBook.createCellStyle();
		     cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		     cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		     cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		     cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		     cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		     cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
		     cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		     cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
		     cellStyle.setFont(font1);
		        
			 cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
			 cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			 cell.setCellValue("Code structure");
			 			 cell.setCellStyle(cellStyle);
			 HSSFCell cell1 = row.createCell((short)1);
			 cell1.setCellValue("Code division");
			 			 cell1.setCellStyle(cellStyle);
			 
			 HSSFCell cell2 = row.createCell((short)2);
			 cell2.setCellValue("Nom division");
			 			 cell2.setCellStyle(cellStyle);
			 
			 HSSFCell cell3 = row.createCell((short)3);
			 cell3.setCellValue("Code direction");
			 			 cell3.setCellStyle(cellStyle);
			 
			 HSSFCell cell4 = row.createCell((short)4);
			 cell4.setCellValue("Nom direction");
			 			 cell4.setCellStyle(cellStyle);
			 
			 HSSFCell cell5 = row.createCell((short)5);
			 cell5.setCellValue("Code unité");
			 			 cell5.setCellStyle(cellStyle);
			 
			 HSSFCell cell6 = row.createCell((short)6);
			 cell6.setCellValue("Nom unité");
			 			 cell6.setCellStyle(cellStyle);
			 HSSFCell cell7 = row.createCell((short)7);
			 cell7.setCellValue("Code sous direction");
			 cell7.setCellStyle(cellStyle);
						 
			 HSSFCell cell8 = row.createCell((short)8);
			cell8.setCellValue("Nom sous direction");
			cell8.setCellStyle(cellStyle);
			
			 HSSFCell cell9 = row.createCell((short)9);
			 cell9.setCellValue("Code département");
			 			 cell9.setCellStyle(cellStyle);
			 
			 HSSFCell cell10 = row.createCell((short)10);
			 cell10.setCellValue("Nom département");
			 			 cell10.setCellStyle(cellStyle);
			 
			 HSSFCell cell11 = row.createCell((short)11);
			 cell11.setCellValue("Code service");
			 			 cell11.setCellStyle(cellStyle);
			 
			 HSSFCell cell12 = row.createCell((short)12);
			 cell12.setCellValue("Nom service");
			 			 cell12.setCellStyle(cellStyle);
			 
			 HSSFCell cell13 = row.createCell((short)13);
			 cell13.setCellValue("Code section");
			 			 cell13.setCellStyle(cellStyle);
			 
			 HSSFCell cell14 = row.createCell((short)14);
			 cell14.setCellValue("Nom section");
			 			 cell14.setCellStyle(cellStyle);
			 			 
			 HSSFCell cell15 = row.createCell((short)15);
			cell15.setCellValue("code+libellé");
			cell15.setCellStyle(cellStyle);
			 
			 int i=1;
			while (index.hasNext())
			{
				
				StructureEntrepriseBean donnee=(StructureEntrepriseBean)index.next();
				
				 HSSFRow row1 = sheet.createRow(i);
				 HSSFCell cel = row1.createCell((short)0);
				 cel.setCellValue(donnee.getCodestructure());
				 
				 cel = row1.createCell((short)1);
				 cel.setCellValue(donnee.getCodeDivision());
				 cel = row1.createCell((short)2);
				 cel.setCellValue(donnee.getLibelleDivision());
				 cel = row1.createCell((short)3);
				 cel.setCellValue(donnee.getCodeDirection());
				 cel = row1.createCell((short)4);
				 cel.setCellValue(donnee.getLibelleDirection());
				 cel = row1.createCell((short)5);
				 cel.setCellValue(donnee.getCodeUnite());
				 cel = row1.createCell((short)6);
				 cel.setCellValue(donnee.getLibelleUnite());
				 cel = row1.createCell((short)7);
				 cel.setCellValue(donnee.getCodeSousDirection());
				 cel = row1.createCell((short)8);
				 cel.setCellValue(donnee.getLibelleSousDirection());
				 cel = row1.createCell((short)9);
				 cel.setCellValue(donnee.getCodeDepartement());
				 cel = row1.createCell((short)10);
				 cel.setCellValue(donnee.getLibelleDepartement());
				 cel = row1.createCell((short)11);
				 cel.setCellValue(donnee.getCodeService());
				 cel = row1.createCell((short)12);
				 cel.setCellValue(donnee.getLibelleService());
				 cel = row1.createCell((short)13);
				 cel.setCellValue(donnee.getCodesection());
				 cel = row1.createCell((short)14);
				 cel.setCellValue(donnee.getLibelleSection());
				 cel = row1.createCell((short)15);
				 //cel.setCellValue(donnee.getLibelleSection());
				 i++;
				 //cel.setCellFormula("A"+i+"& \",\"&C"+i+"& \",\"&E"+"& \",\"&G"+"& \",\"&I"+"& \",\"&K"+"& \",\"&M");
				 
				 cel.setCellFormula("A"+i+"& \",\"&C"+i+"& \",\"&E"+i+"& \",\"&G"+i+"& \",\"&I"+i+"& \",\"&K"+i+"& \",\"&M"+i);
			}

			//autosize des colonnes
			for (int j=0;j<=14;j++)
		 	{
		 		sheet.autoSizeColumn(j);
		 		
		 	}
			
//			FileOutputStream fOut;
//			try 
//			{
//				fOut = new FileOutputStream("Structure_entreprise.xls");
//				workBook.write(fOut);
//				fOut.flush();
//				fOut.close();
//				
//				File file = new File("Structure_entreprise.xls");
//				Filedownload.save(file, "XLS");
//			} 
//			catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
					
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void downloadStructureEntrepriseRejectedDataToXls(HSSFWorkbook workBook,List<StructureEntrepriseBean> listeStructureEntrepriseBean)
	{
		
		
		try 
		{//
			
			
			//creation du fichier xls
			
			Iterator <StructureEntrepriseBean>index=listeStructureEntrepriseBean.iterator();
			//creation d'un document excel 
			//HSSFWorkbook workBook = new HSSFWorkbook();
			
			//creation d'une feuille excel
			 HSSFSheet sheet = workBook.createSheet(Contantes.onglet_structure_entreprise);
			 
			 //creation de l'entête du document excel
			 HSSFRow row = sheet.createRow(0);
			 HSSFCell cell = row.createCell((short)0);
			 
			 HSSFCellStyle cellStyle = null;
			 HSSFFont font1 = workBook.createFont();
			 font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
			 font1.setFontHeightInPoints((short)8);
			 font1.setFontName("Arial");
			 
			 cellStyle = workBook.createCellStyle();
		     cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		     cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		     cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		     cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		     cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		     cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
		     cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		     cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
		     cellStyle.setFont(font1);
		        
			 cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
			 cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			 cell.setCellValue("Code structure");
			 			 cell.setCellStyle(cellStyle);
			 HSSFCell cell1 = row.createCell((short)1);
			 cell1.setCellValue("Code division");
			 			 cell1.setCellStyle(cellStyle);
			 
			 HSSFCell cell2 = row.createCell((short)2);
			 cell2.setCellValue("Nom division");
			 			 cell2.setCellStyle(cellStyle);
			 
			 HSSFCell cell3 = row.createCell((short)3);
			 cell3.setCellValue("Code direction");
			 			 cell3.setCellStyle(cellStyle);
			 
			 HSSFCell cell4 = row.createCell((short)4);
			 cell4.setCellValue("Nom direction");
			 			 cell4.setCellStyle(cellStyle);
			 
			 HSSFCell cell5 = row.createCell((short)5);
			 cell5.setCellValue("Code unité");
			 			 cell5.setCellStyle(cellStyle);
			 
			 HSSFCell cell6 = row.createCell((short)6);
			 cell6.setCellValue("Nom unité");
			 			 cell6.setCellStyle(cellStyle);
			 
			 HSSFCell cell7 = row.createCell((short)7);
			 cell7.setCellValue("Code département");
			 			 cell7.setCellStyle(cellStyle);
			 
			 HSSFCell cell8 = row.createCell((short)8);
			 cell8.setCellValue("Nom département");
			 			 cell8.setCellStyle(cellStyle);
			 
			 HSSFCell cell9 = row.createCell((short)9);
			 cell9.setCellValue("Code service");
			 			 cell9.setCellStyle(cellStyle);
			 
			 HSSFCell cell10 = row.createCell((short)10);
			 cell10.setCellValue("Nom service");
			 			 cell10.setCellStyle(cellStyle);
			 
			 HSSFCell cell11 = row.createCell((short)11);
			 cell11.setCellValue("Code section");
			 			 cell11.setCellStyle(cellStyle);
			 
			 HSSFCell cell12 = row.createCell((short)12);
			 cell12.setCellValue("Nom section");
			 			 cell12.setCellStyle(cellStyle);
			 			 
			 HSSFCell cell13 = row.createCell((short)13);
			cell13.setCellValue("code+libellé");
			cell13.setCellStyle(cellStyle);
			
			HSSFCell cell14 = row.createCell((short)14);
			cell14.setCellValue("Cause du rejet");
			cell14.setCellStyle(cellStyle);
			 
			 int i=1;
			while (index.hasNext())
			{
				
				StructureEntrepriseBean donnee=(StructureEntrepriseBean)index.next();
				
				 HSSFRow row1 = sheet.createRow(i);
				 HSSFCell cel = row1.createCell((short)0);
				 cel.setCellValue(donnee.getCodestructure());
				 
				 cel = row1.createCell((short)1);
				 cel.setCellValue(donnee.getCodeDivision());
				 cel = row1.createCell((short)2);
				 cel.setCellValue(donnee.getLibelleDivision());
				 cel = row1.createCell((short)3);
				 cel.setCellValue(donnee.getCodeDirection());
				 cel = row1.createCell((short)4);
				 cel.setCellValue(donnee.getLibelleDirection());
				 cel = row1.createCell((short)5);
				 cel.setCellValue(donnee.getCodeUnite());
				 cel = row1.createCell((short)6);
				 cel.setCellValue(donnee.getLibelleUnite());
				 cel = row1.createCell((short)7);
				 cel.setCellValue(donnee.getCodeDepartement());
				 cel = row1.createCell((short)8);
				 cel.setCellValue(donnee.getLibelleDepartement());
				 cel = row1.createCell((short)9);
				 cel.setCellValue(donnee.getCodeService());
				 cel = row1.createCell((short)10);
				 cel.setCellValue(donnee.getLibelleService());
				 cel = row1.createCell((short)11);
				 cel.setCellValue(donnee.getCodesection());
				 cel = row1.createCell((short)12);
				 cel.setCellValue(donnee.getLibelleSection());
				 cel = row1.createCell((short)13);
				 //cel.setCellValue(donnee.getLibelleSection());
				 i++;
				 //cel.setCellFormula("A"+i+"& \",\"&C"+i+"& \",\"&E"+"& \",\"&G"+"& \",\"&I"+"& \",\"&K"+"& \",\"&M");
				 
				 cel.setCellFormula("A"+i+"& \",\"&C"+i+"& \",\"&E"+i+"& \",\"&G"+i+"& \",\"&I"+i+"& \",\"&K"+i+"& \",\"&M"+i);
				 
				 cel = row1.createCell((short)14);
				 cel.setCellValue(donnee.getCauseRejet());
			}

			//autosize des colonnes
			for (int j=0;j<=15;j++)
		 	{
		 		sheet.autoSizeColumn(j);
		 		
		 	}
			

			
					
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	  public String getMaxKeyCode() throws SQLException
	    {
	    	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	    	Connection conn=(Connection) dbcon.connectToSecondairesDB();
	    	Statement stmt = null;
	    	String result = null ;
	    	int i=1;
	    	
	    	try 
	    	{
	    		stmt = (Statement) conn.createStatement();
	    		String profile_list="select concat_ws('|', max(code_structure), max(code_division) ,max(code_direction),max(code_unite)," +
	    				            " max(code_sous_direction), max(code_departement),max(code_service),max(code_section)) as max_code from structure_entreprise"; 
	    		ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
	    		
	    		
	    		while(rs.next()){
	    			result=rs.getString("max_code");
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
	    	
	    	return result;
	    }
	    
	    public static String getNextCode(String charto,String code,int size) {

	    	String nextvalue="XXXX";

	    	if (code==null || code.length()==0){

		    	nextvalue=charto+"001";
		    	return nextvalue;
        	}

	    	String[]list=code.split(charto);
	    	String chaine=String.valueOf(Integer.parseInt(list[1])+1);

		    if (size==4)
		    {
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
	
		    }
		    
		    
		    if (size==5){
		    	if (chaine.length() <5){
	
			    	if (chaine.length()==1){
		
			    	chaine=charto+"000"+chaine;
		
			    	}
		
			    	if (chaine.length()==2){
		
			    	chaine=charto+"00"+chaine;
		
			    	}
		
			    	if (chaine.length()==3){
		
			    		chaine=charto+"0"+chaine;
		
			    	}
			    	
			    	if (chaine.length()==4){
			    		
			    		chaine=charto+chaine;
		
			    	}
		
			    	nextvalue=chaine;
			    	 
		
			   }
		 
		    }
		    
		   	return nextvalue;
}

}
	   
