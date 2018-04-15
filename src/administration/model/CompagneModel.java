package administration.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import administration.bean.Compagne;
import administration.bean.CompagneCreationBean;
import administration.bean.CompagneType;
import administration.bean.Employe;
import administration.bean.FichePlanningBean;
import administration.bean.StructureEntrepriseBean;

import java.sql.Connection;
import java.sql.Statement;

import common.Contantes;
import common.CreateDatabaseCon;

/**
 * @author nbehlouli
 * 
 *
 */

public class CompagneModel
{

	private List<Compagne> compagnes = null;

	public List<Compagne> getAllCompagnes()
			throws SQLException
			{

		compagnes = new ArrayList<Compagne>();

		CreateDatabaseCon dbcon = new CreateDatabaseCon();
		Connection conn = (Connection) dbcon.connectToSecondairesDB();
		Statement stmt=null;
		ResultSet rs=null;
		try
		{
			stmt = (Statement) conn.createStatement();
			String select_all_compagne =  "select max(id_compagne),libelle_compagne,compagne_type,DATE_FORMAT(date_debut, '%Y-%m-%d')as date_debut,DATE_FORMAT(date_fin, '%Y-%m-%d') as date_fin,c.id_compagne_type,t.compagne_type  from compagne_evaluation c,compagne_type t" +
					" where c.id_compagne_type=t.id_compagne_type";

			rs = (ResultSet) stmt.executeQuery( select_all_compagne );

			while ( rs.next() )
			{
				if ( rs.getRow() >= 1 )
				{
					// id_compagne, id_employe, date_debut, date_fin, libelle_compagne, code_structure, id_compagne_type
					Compagne compagne = new Compagne();

					compagne.setIdCompagne( rs.getInt( "id_compagne" ) );
					compagne.setDateDebut( rs.getString( "date_debut" ) );
					compagne.setDateFin( rs.getString( "date_fin" ) );
					compagne.setLibelleCompagne( rs.getString( "libelle_compagne" ) );
					compagne.setCompagneType(  rs.getString( "compagne_type" ) );
					compagne.setId_compagneType(rs.getInt("id_compagne_type"));

					compagnes.add( compagne );

				}
				else
				{
					return compagnes;
				}

			}
			// stmt.close(); conn.close();
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
		

		return compagnes;

			}

	public void setCompagnes( List<Compagne> compagnes )
	{
		this.compagnes = compagnes;
	}

	public List<Compagne> getCompagnes()
	{
		return compagnes;
	}

	public boolean controleIntegrite( Compagne addedData )
	{
		try
		{
			if ( addedData.getIdCompagne() > 16777215 )
			{
				Messagebox.show( "L'id comagne est trop grand", "Erreur", Messagebox.OK, Messagebox.ERROR );
				return false;
			}
		}
		catch ( InterruptedException e1 )
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return true;
	}

	
	/**
	 * cette méthode permet de vérifier l'integrite des données et retourne les données rejetés
	 * @return
	 */


	public HashMap <String,List<Compagne>> ChargementDonneedansBdd(List <Compagne> liste)throws Exception
	{
		//Verification de l'integrité des données à inserer doublon dans le fichier
		List <Compagne> listeAInserer=new ArrayList <Compagne>();
		List <Compagne> listeDonneesRejetes=new ArrayList <Compagne>();




		for(int i=0; i<liste.size();i++)
		{
			Compagne donnee=liste.get(i);
			boolean donneerejete=false;
			for(int j=i+1;j<liste.size();j++)
			{
				Compagne donnee2=liste.get(j);
				if(donnee.getDateFin().compareTo(donnee.getDateDebut())==-1)
				{
					donnee.setCauseRejet("La date de fin doite etre superieure à la date de debut");
					listeDonneesRejetes.add(donnee);
					donneerejete=true;



				}
			}
			if((i==liste.size()-1)||(i==0)||(donneerejete==false))
				listeAInserer.add(donnee);

		}


		//Verification de l'integrité des données à inserer doublon avec les données de la base

		List <Compagne> listeAInsererFinal=new ArrayList <Compagne>();
		@SuppressWarnings("unchecked")
		List<Compagne>compagnegBdd =getAllCompagnes();
		Iterator <Compagne>iterator=listeAInserer.iterator();

		while(iterator.hasNext())
		{

			Compagne bean=(Compagne)iterator.next();

			Iterator<Compagne> index=compagnegBdd.iterator();
			boolean donneerejete=false;
			while(index.hasNext())
			{

				Compagne bean2=(Compagne)index.next();
				if(bean.getDateDebut().compareTo(bean2.getDateDebut())==0 && bean.getDateDebut().compareTo(bean2.getDateDebut())==0)
				{
					bean.setCauseRejet("Une compagne d'évaluation portant sur la même periode existe déja");
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
		Iterator<Compagne> index =listeAInsererFinal.iterator();
		String requete="";
		while(index.hasNext())
		{
			Compagne donneeBean=(Compagne)index.next();
			boolean retour=ConstructionRequeteAddCompagne(donneeBean);
			if (retour){
				dupliquerCompagnePosteTravailChrg();
			}else{
				Messagebox.show("Compagne d'évaluation n'a pas été créée", "Erreur", Messagebox.OK, Messagebox.ERROR);
			}
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
		HashMap <String,List<Compagne>> donneeMap=new HashMap<String,List<Compagne>>();
		donneeMap.put("inserer", listeAInsererFinal);
		donneeMap.put("supprimer", listeDonneesRejetes);
		return donneeMap;
	}




	public boolean ConstructionRequeteAddCompagne(Compagne addedData) throws SQLException 
	{


		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();

		PreparedStatement pstmt = null;
		boolean result=true;
		try {


			String requete_Sql="insert into compagne_evaluation (date_debut,date_fin,libelle_compagne ,id_compagne_type)"
					+ " values (?,?,?,?)";
			pstmt = conn.prepareStatement(requete_Sql);

			
			pstmt.setString(1, addedData.getDateDebut());
			pstmt.setString(2, addedData.getDateFin());
			pstmt.setString(3, addedData.getLibelleCompagne().replaceAll("'"," "));
			pstmt.setInt(4, addedData.getId_compagneType());


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
	

	/**
	 * Cette méthode permet de faire un upload d'un fichier xlsx (fichier vers BDD)
	 * @return
	 */
	public List <Compagne> uploadXLSXFile(InputStream inputStream)
	{

		ArrayList <Compagne> listPlanningBean=new ArrayList<Compagne>();

		// Creer l'objet representant le fichier Excel
		try 
		{
			XSSFWorkbook fichierExcel = new XSSFWorkbook(inputStream);
			// creer l'objet representant les lignes Excel
			XSSFRow ligne;

			// creer l'objet representant les cellules Excel
			XSSFCell cellule;

			//lecture de la première feuille excel
			XSSFSheet feuilleExcel=fichierExcel.getSheet(Contantes.onglet_compagne_evaluation);

			// lecture du contenu de la feuille excel
			int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();

			for(int numLigne =1;numLigne<=nombreLigne; numLigne++)
			{

				ligne = feuilleExcel.getRow(numLigne);
				if(ligne!=null)
				{
					int nombreColonne = ligne.getLastCellNum()
							- ligne.getFirstCellNum();
					Compagne compagneBean=new Compagne();
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
							String date1=null;
							String date2=null;

							if((numColonne==0))
							{
								try
								{
									date1= convertDateToString(cellule.getDateCellValue());
									//date1=cellule.getStringCellValue();
								}
								catch(Exception e)
								{
									System.out.println("erreur lors de la lecture d'une colonne date numColonne="+numColonne +" valeur saiaie="+cellule.toString()+ " numero de ligne= "+numLigne);
									try {
										throw new Exception (e);
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
							if((numColonne==1))
							{
								try
								{
									date2= convertDateToString(cellule.getDateCellValue());
									//date2=cellule.getStringCellValue();
								}
								catch(Exception e)
								{
									System.out.println("erreur lors de la lecture d'une colonne date numColonne="+numColonne +" valeur saiaie="+cellule.toString()+ " numero de ligne= "+numLigne);
									try {
										throw new Exception (e);
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
								compagneBean.setDateDebut(date1);
							}
							else
								if(numColonne==1)
								{
									compagneBean.setDateFin(date2);
								}
								else
									if(numColonne==2)
									{
										compagneBean.setLibelleCompagne(valeur);

									}
									else
										if(numColonne==3)
										{
											String[] val=valeur.split(",");
											int Id_compagneType=Integer.valueOf(val[0].replaceAll("\\s+",""));
											compagneBean.setId_compagneType(Id_compagneType);
											compagneBean.setCompagneType(val[1]);

										}
										


						}
						else
							if(numColonne==0)
								inserer=false;
					}//fin for colonne
					if(inserer)
						listPlanningBean.add(compagneBean);
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
	
	public String convertDateToString(java.util.Date dateinput){
		
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		String reportDate = df.format(dateinput);
		
		return reportDate;

	}
	
	public Boolean dupliquerCompagnePosteTravailChrg() throws SQLException{
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();//connectToEntrepriseDBMulti();
		Statement stmt = null;
		boolean retour=false;

		
		
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="insert into compagne_poste_travail(id_compagne,code_poste)"
					       + " select ( select max(id_compagne) from compagne_validation) ,code_poste"
					       + " from poste_travail_description"; 
			
			//sql_query = sql_query.replaceAll("#id_compagne", "'"+String.valueOf(addedData.getId_compagne())+"'");
			
		
			
			 stmt.executeUpdate(sql_query);
			 retour=true;
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
		return retour;

		
		
	}
	
}
