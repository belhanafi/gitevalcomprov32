package administration.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import administration.bean.AdministrationLoginBean;
import administration.bean.FichePosteBean;
import administration.bean.RepCompetenceBean;
import administration.bean.StructureEntrepriseBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import common.CreateDatabaseCon;
import common.PwdCrypt;

public class FichePosteModel {
	
private ArrayList<FichePosteBean>  listcomp =null; 
	
	
	
public List loadFichesPostes() throws SQLException{
		
		listcomp = new ArrayList<FichePosteBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sql_query="select p.code_poste ,p.intitule_poste,trim(p.sommaire_poste) as sommaire_poste ,trim(p.tache_responsabilite) as tache_responsabilite ,trim(p.environement_perspectif) as environement_perspectif ,concat(libelle_formation,'-',libelle_diplome) as formation_general," +
					         " p.formation_professionnelle ,p.experience,trim(p.profile_poste) as profile_poste,t.intitule_poste as libelle_poste,p.code_structure,p.date_maj_poste,CASE WHEN p.is_cadre='N' THEN 'NON' ELSE 'OUI' END as is_cadre " +
					         " from poste_travail_description p,structure_entreprise s ,poste_travail_description t, formation f   where p.code_structure=s.code_structure  and t.code_poste=p.code_poste_hierarchie" +
					         " and p.code_formation=f.code_formation";
			//System.out.println(sql_query);
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
						
			while(rs.next()){
				
				FichePosteBean bean =new FichePosteBean();
				
				bean.setCode_poste(rs.getString("code_poste"));
				bean.setIntitule_poste(rs.getString("intitule_poste"));
				bean.setSommaire_poste(rs.getString("sommaire_poste"));
				bean.setTache_responsabilite(rs.getString("tache_responsabilite"));
				bean.setEnvironement_perspectif(rs.getString("environement_perspectif"));
				bean.setFormation_general(rs.getString("formation_general"));
				bean.setFormation_professionnelle(rs.getString("formation_professionnelle"));
				bean.setProfile_poste(rs.getString("profile_poste"));
				bean.setLibelle_poste(rs.getString("libelle_poste"));
				bean.setCode_structure(rs.getString("code_structure"));
				bean.setDate_maj_poste(rs.getDate("date_maj_poste"));
				bean.setIs_cadre(rs.getString("is_cadre"));
				
				    listcomp.add(bean);
				   
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		return listcomp;
	
		
		
	}


/**
 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
 * @param addedData
 * @return
 * @throws ParseException 
 */
public boolean addPosteTravail(FichePosteBean addedData) throws ParseException
{
	

	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt = null;
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	try 
	{
		                                                
		stmt = (Statement) conn.createStatement();
		String select_structure="INSERT INTO poste_travail_description(code_poste, intitule_poste, sommaire_poste, tache_responsabilite, environement_perspectif, code_formation, formation_professionnelle, experience, profile_poste, code_poste_hierarchie, code_structure, date_maj_poste,is_cadre)" +
	 		          " VALUES(#code_poste,#intitule_poste,#sommaire_poste,#tache_responsabilite,#environement_perspectif,#code_formation,#formation_professionnelle,#experience,#profile_poste,#hierarchie,#code_structure,#date_maj_poste,#is_cadre)";
	 

		
		select_structure = select_structure.replaceAll("#code_poste", "'"+addedData.getCode_poste()+"'");
		select_structure = select_structure.replaceAll("#intitule_poste", "'"+addedData.getIntitule_poste()+"'");
		select_structure = select_structure.replaceAll("#sommaire_poste", "'"+removeString(addedData.getSommaire_poste())+"'");
		select_structure = select_structure.replaceAll("#tache_responsabilite", "'"+removeString(addedData.getTache_responsabilite())+"'");
	    select_structure = select_structure.replaceAll("#environement_perspectif", "'"+removeString(addedData.getEnvironement_perspectif())+"'");
	    select_structure = select_structure.replaceAll("#code_formation", "'"+removeString(addedData.getFormation_general())+"'");
        select_structure = select_structure.replaceAll("#formation_professionnelle", "'"+removeString(addedData.getFormation_professionnelle())+"'");
		select_structure = select_structure.replaceAll("#experience", "'"+removeString(addedData.getExperience())+"'");
		select_structure = select_structure.replaceAll("#profile_poste", "'"+addedData.getProfile_poste()+"'");
		select_structure = select_structure.replaceAll("#hierarchie", "'"+addedData.getPoste_hierarchie()+"'");
		select_structure = select_structure.replaceAll("#code_structure", "'"+addedData.getCode_structure()+"'");
		select_structure = select_structure.replaceAll("#date_maj_poste", "'"+formatter.format(addedData.getDate_maj_poste())+"'");
		select_structure = select_structure.replaceAll("#is_cadre","'"+addedData.getIs_cadre()+"'");
		
		
						
	//System.out.println(select_structure);
		
		 stmt.execute(select_structure);
	} 
	catch (SQLException e) 
	{
		try 
		{
			
			Messagebox.show("La donnée n'a pas été insérée dans la base de données" +e, "Erreur",Messagebox.OK, Messagebox.ERROR);
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

public Boolean majPosteTravail(FichePosteBean addedData)
{
	
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt;
	
	try 
	{
		
		
		stmt = (Statement) conn.createStatement();
		String select_structure="UPDATE poste_travail_description set intitule_poste=#intitule_poste, sommaire_poste=#sommaire_poste, tache_responsabilite=#tache_responsabilite, environement_perspectif=#environement_perspectif, code_formation=#code_formation, formation_professionnelle=#formation_professionnelle, experience=#experience, profile_poste=#profile_poste" +
        "  , code_poste_hierarchie=#hierarchie, code_structure=#code_structure, date_maj_poste=#date_maj_poste,is_cadre=#is_cadre where code_poste=#code_poste";

		select_structure = select_structure.replaceAll("#code_poste", "'"+addedData.getCode_poste()+"'");
		select_structure = select_structure.replaceAll("#intitule_poste", "'"+addedData.getIntitule_poste()+"'");
		select_structure = select_structure.replaceAll("#sommaire_poste", "'"+removeString(addedData.getSommaire_poste())+"'");
		select_structure = select_structure.replaceAll("#tache_responsabilite", "'"+removeString(addedData.getTache_responsabilite())+"'");
	    select_structure = select_structure.replaceAll("#environement_perspectif", "'"+removeString(addedData.getEnvironement_perspectif())+"'");
	    select_structure = select_structure.replaceAll("#code_formation", "'"+removeString(addedData.getFormation_general())+"'");
        select_structure = select_structure.replaceAll("#formation_professionnelle", "'"+removeString(addedData.getFormation_professionnelle())+"'");
		select_structure = select_structure.replaceAll("#experience", "'"+removeString(addedData.getExperience())+"'");
		select_structure = select_structure.replaceAll("#profile_poste", "'"+addedData.getProfile_poste()+"'");
		select_structure = select_structure.replaceAll("#hierarchie", "'"+addedData.getPoste_hierarchie()+"'");
		select_structure = select_structure.replaceAll("#code_structure", "'"+addedData.getCode_structure()+"'");
		select_structure = select_structure.replaceAll("#date_maj_poste", "'"+formatter.format(addedData.getDate_maj_poste())+"'");
		select_structure = select_structure.replaceAll("#is_cadre","'"+addedData.getIs_cadre()+"'");

	   //System.out.println(update_structure);
		
		 stmt.executeUpdate(select_structure);
	} 
	catch (SQLException e) 
	{
		try 
		{
			Messagebox.show("La modification n'a pas été prise en compte car il existe une donnée ayant le même code établissement", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
 * cette classe permet de supprimer une donnée de la table structure_entreprise
 * @param codeStructure
 */
public void supprimerFichePoste(FichePosteBean addedData)
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt;
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String sql_query="DELETE FROM  poste_travail_description  where code_poste=#code_poste";

		sql_query = sql_query.replaceAll("#code_poste", "'"+addedData.getCode_poste()+"'");
	
		
		 stmt.executeUpdate(sql_query);
	} 
	catch (SQLException e) 
	{
		
			e.printStackTrace();
			//return false;


	}
	try {
		conn.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
/*//**
 * Cette méthode permet de faire un chargement d'un fichier xlsx 
 * @return
 *//*
*//**
 * Cette méthode permet de faire un upload d'un fichier xlsx (fichier vers BDD)
 * @return
 *//*
public List <RepCompetenceBean> uploadXLSXFile(InputStream inputStream)
{
	listcomp=new ArrayList <RepCompetenceBean>();
	
	 // Creer l'objet representant le fichier Excel
    try 
    {
		XSSFWorkbook fichierExcel = new XSSFWorkbook(inputStream);
		// creer l'objet representant les lignes Excel
        XSSFRow ligne;

        // creer l'objet representant les cellules Excel
        XSSFCell cellule;

        //lecture de la première feuille excel
        XSSFSheet feuilleExcel=fichierExcel.getSheetAt(0);
        
        // lecture du contenu de la feuille excel
        int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();
        
        for(int numLigne =1;numLigne<=nombreLigne; numLigne++)
        {
        	
        	ligne = feuilleExcel.getRow(numLigne);
            int nombreColonne = ligne.getLastCellNum()
                    - ligne.getFirstCellNum();
            RepCompetenceBean structurentreprise=new RepCompetenceBean();
            // parcours des colonnes de la ligne en cours
            for (short numColonne = 0; numColonne < nombreColonne; numColonne++) 
            {
            	try
            	{
            	
            	cellule = ligne.getCell(numColonne);
            	
            	String valeur= cellule.getStringCellValue();
            	
            	
            	if(numColonne==0)
            	{
            		structurentreprise.setCode_famille(valeur);
            	}
            	else
            		if(numColonne==1)
	            	{
            			structurentreprise.setFamille(valeur);
	            	}
	            	else
	            		if(numColonne==2)
		            	{
	            			structurentreprise.setCode_groupe(valeur);
		            	}
		            	else
		            		if(numColonne==3)
			            	{
		            			structurentreprise.setGroupe(valeur);
			            	}
			            	else
			            		if(numColonne==4)
				            	{
			            			structurentreprise.setCode_competence(valeur);
				            	}
				            	else
				            		if(numColonne==5)
					            	{
				            			structurentreprise.setLibelle_competence(valeur);
					            	}
					            	else
					            		if(numColonne==6)
						            	{
					            			structurentreprise.setDefinition_competence(valeur);
						            	}
						            	else
						            		if(numColonne==7)
							            	{
						            			structurentreprise.setAptitude_observable(valeur);
							            	}
							            	else
							            		if(numColonne==8)
								            	{
							            			structurentreprise.setAffichable(valeur);
								            	}
								            	
            	}catch(Exception e)
            	{
            		
            	}

            }//fin for colonne
            listcomp.add(structurentreprise);
        }//fin for ligne

	} 
    catch (IOException e) 
    {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
    
	return listcomp;
}
*//**
 * @throws IOException 
 * @throws InvalidFormatException 
 * @throws FileNotFoundException 
 * @throws BiffException 
 * Cette méthode permet de faire un upload d'un fichier xls (fichier vers BDD)
 * @return
 * @throws  
 *//*


public List <RepCompetenceBean> uploadXLSFile(InputStream inputStream)
{
	listcomp=new ArrayList <RepCompetenceBean>();
	
	 // Creer l'objet representant le fichier Excel
    try 
    {
    	HSSFWorkbook fichierExcel = new HSSFWorkbook(inputStream);
		// creer l'objet representant les lignes Excel
        HSSFRow ligne;

        // creer l'objet representant les cellules Excel
        HSSFCell cellule;

        //lecture de la première feuille excel
        HSSFSheet feuilleExcel=fichierExcel.getSheetAt(0);
        
        // lecture du contenu de la feuille excel
        int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();
        
        for(int numLigne =1;numLigne<=nombreLigne; numLigne++)
        {
        	//System.out.println("de nouveau dans la boucle");
        	ligne = feuilleExcel.getRow(numLigne);
            int nombreColonne = ligne.getLastCellNum()
                    - ligne.getFirstCellNum();
            RepCompetenceBean structurentreprise=new RepCompetenceBean();
            // parcours des colonnes de la ligne en cours
            for (short numColonne = 0; numColonne < nombreColonne; numColonne++) 
            {
            	try
            	{
            	//System.out.println("de nouveau dan scolonne");
            	cellule = ligne.getCell(numColonne);
            	
            	String valeur= cellule.getStringCellValue();
            	//System.out.println(" numligne = "+numLigne+" numColonne= "+numColonne +" valeur ="+valeur);
            	
            	if(numColonne==0)
            	{
            		structurentreprise.setCode_famille(valeur);
            	}
            	else
            		if(numColonne==1)
	            	{
            			structurentreprise.setFamille(valeur);
	            	}
	            	else
	            		if(numColonne==2)
		            	{
	            			structurentreprise.setCode_groupe(valeur);
		            	}
		            	else
		            		if(numColonne==3)
			            	{
		            			structurentreprise.setGroupe(valeur);
			            	}
			            	else
			            		if(numColonne==4)
				            	{
			            			structurentreprise.setCode_competence(valeur);
				            	}
				            	else
				            		if(numColonne==5)
					            	{
				            			structurentreprise.setLibelle_competence(valeur);
					            	}
					            	else
					            		if(numColonne==6)
						            	{
					            			structurentreprise.setDefinition_competence(valeur);
						            	}
						            	else
						            		if(numColonne==7)
							            	{
						            			structurentreprise.setAptitude_observable(valeur);
							            	}
							            	else
							            		if(numColonne==8)
								            	{
							            			structurentreprise.setAffichable(valeur);
								            	}
								            	
            	}catch(Exception e)
            	{
            		
            	}

            }//fin for colonne
            listcomp.add(structurentreprise);
        }//fin for ligne

	} 
    catch (IOException e) 
    {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
    
	return listcomp;
}

public List <RepCompetenceBean> uploadXLSFileS(String inputFile) throws BiffException, InvalidFormatException, IOException  
{
	listcomp=new ArrayList <RepCompetenceBean>();
	Workbook w;
	
	InputStream inp = new FileInputStream(inputFile);
     w = (Workbook) WorkbookFactory.create(inp);
   
	
	//File inputWorkbook = new File(inputFile);
	//RepCompetenceBean structurentreprise=new RepCompetenceBean();
	
	//w = Workbook.getWorkbook(inputWorkbook);
	// Get the first sheet
	Sheet sheet = w.getSheet(0);
	// Loop over first 10 column and lines
	for (int i = 1; i < sheet.getRows(); i++) {
		RepCompetenceBean structurentreprise=new RepCompetenceBean();
	     for (int j = 0; j < sheet.getColumns(); j++) {
	    	 Cell cell = sheet.getCell(j, i);
	    	 
	    	 if(j==0)
	        	{
	        		structurentreprise.setCode_famille(cell.getContents());
	        	}
	        	else
	        		if(j==1)
	            	{
	        			structurentreprise.setFamille(cell.getContents());
	            	}
	            	else
	            		if(j==2)
		            	{
	            			structurentreprise.setCode_groupe(cell.getContents());
		            	}
		            	else
		            		if(j==3)
			            	{
		            			structurentreprise.setGroupe(cell.getContents());
			            	}
			            	else
			            		if(j==4)
				            	{
			            			structurentreprise.setCode_competence(cell.getContents());
				            	}
				            	else
				            		if(j==5)
					            	{
				            			structurentreprise.setLibelle_competence(cell.getContents());
					            	}
					            	else
					            		if(j==6)
						            	{
					            			structurentreprise.setDefinition_competence(cell.getContents());
						            	}
						            	else
						            		if(j==7)
							            	{
						            			structurentreprise.setAptitude_observable(cell.getContents());
							            	}
							            	else
							            		if(j==8)
								            	{
							            			structurentreprise.setAffichable(cell.getContents());
								            	}
	    	
	     }
	     listcomp.add(structurentreprise);     
	}
	        		
         return listcomp;
}


*//**
 * Cette méthode permet de charger le contenu de la table Structure_entreprise et de créer un fichier excel avec ces données
 *//*
public void downloadStructureEntrepriseDataToXls()
{
	
	
	//recupération du contenu de la table Structure_entreprise
	try 
	{
		ArrayList<RepCompetenceBean> listerepBean=(ArrayList<RepCompetenceBean>) loadRepCompetence();
		
		//creation du fichier xls
		
		Iterator <RepCompetenceBean>index=listerepBean.iterator();
		//creation d'un document excel 
		HSSFWorkbook workBook = new HSSFWorkbook();
		
		//creation d'une feuille excel
		 HSSFSheet sheet = workBook.createSheet("Répertoire compétences");
		 
		 //creation de l'entête du document excel
		 HSSFRow row = sheet.createRow(0);
		 HSSFCell cell = row.createCell((short)0);
		 

		 cell.setCellValue("code famille");
		 //			 cell.setCellStyle(cellStyle);
		 HSSFCell cell1 = row.createCell((short)1);
		 cell1.setCellValue("Famille");
		 //			 cell1.setCellStyle(cellStyle);
		 
		 HSSFCell cell2 = row.createCell((short)2);
		 cell2.setCellValue("code groupe");
		 //			 cell2.setCellStyle(cellStyle);
		 
		 HSSFCell cell3 = row.createCell((short)3);
		 cell3.setCellValue("Groupe");
		 //			 cell3.setCellStyle(cellStyle);
		 
		 HSSFCell cell4 = row.createCell((short)4);
		 cell4.setCellValue("Code compétence");
		 //			 cell4.setCellStyle(cellStyle);
		 
		 HSSFCell cell5 = row.createCell((short)5);
		 cell5.setCellValue("Libellé compétence");
		 //			 cell5.setCellStyle(cellStyle);
		 
		 HSSFCell cell6 = row.createCell((short)6);
		 cell6.setCellValue("Définition compétence");
		 //			 cell6.setCellStyle(cellStyle);
		 
		 HSSFCell cell7 = row.createCell((short)7);
		 cell7.setCellValue("Aptitude observable");
		 //			 cell7.setCellStyle(cellStyle);
		 
		 HSSFCell cell8 = row.createCell((short)8);
		 cell8.setCellValue("Affichable");
		 //			 cell8.setCellStyle(cellStyle);
		 
		 
		 
		 int i=1;
		while (index.hasNext())
		{
			
		RepCompetenceBean donnee=(RepCompetenceBean)index.next();
			
			 HSSFRow row1 = sheet.createRow(i);
			 HSSFCell cel = row1.createCell((short)0);
			  
			 cel = row1.createCell((short)0);
			 cel.setCellValue(donnee.getCode_famille());
			 cel = row1.createCell((short)1);
			 cel.setCellValue(donnee.getFamille());
			 cel = row1.createCell((short)2);
			 cel.setCellValue(donnee.getCode_groupe());
			 cel = row1.createCell((short)3);
			 cel.setCellValue(donnee.getGroupe());
			 cel = row1.createCell((short)4);
			 cel.setCellValue(donnee.getCode_competence());
			 cel = row1.createCell((short)5);
			 cel.setCellValue(donnee.getLibelle_competence());
			 cel = row1.createCell((short)6);
			 cel.setCellValue(donnee.getDefinition_competence());
			 cel = row1.createCell((short)7);
			 cel.setCellValue(donnee.getAptitude_observable());
			 cel = row1.createCell((short)8);
			 cel.setCellValue(donnee.getAffichable());
			 
			 i++;
		}
			

		FileOutputStream fOut;
		try 
		{
			fOut = new FileOutputStream("repertoire_competence.xls");
			workBook.write(fOut);
			fOut.flush();
			fOut.close();
			
			File file = new File("repertoire_competence.xls");
			Filedownload.save(file, "XLS");
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
	}
	catch (SQLException e) 
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

*//**
 * cette méthode permet de vérifier l'integrite des données et retourne les données rejetés
 * @return
 *//*
public HashMap <String,List<RepCompetenceBean>> ChargementDonneedansBdd(List <RepCompetenceBean> liste)throws Exception
{
	//Verification de l'integrité des données à inserer doublon dans le fichier
	List <RepCompetenceBean> listeAInserer=new ArrayList <RepCompetenceBean>();
	List <RepCompetenceBean> listeDonneesRejetes=new ArrayList <RepCompetenceBean>();

	for(int i=0; i<liste.size();i++)
	{
		RepCompetenceBean donnee=liste.get(i);
		boolean donneerejete=false;
		for(int j=i+1;j<liste.size();j++)
		{
			RepCompetenceBean donnee2=liste.get(j);
			if(donnee.getCode_famille().equals(donnee2.getCode_famille())&& donnee.getCode_groupe().equals(donnee2.getCode_groupe())&& donnee.getCode_competence().equals(donnee2.getCode_competence())
					&& donnee.getAptitude_observable().equalsIgnoreCase(donnee2.getAptitude_observable()))
			{
				listeDonneesRejetes.add(donnee);
				donneerejete=true;
				//System.out.println("donnee rejete "+donnee.getCodestructure());
			
			}
		}
		if((i==liste.size()-1)||(i==0)||(donneerejete==false))
			listeAInserer.add(donnee);
		//System.out.println("donne a inserer "+donnee.getCodestructure());
		
		
		
	}
	//List <RepCompetenceBean> listeAInsererFinal=new ArrayList <RepCompetenceBean>();
	ArrayList<RepCompetenceBean>strctureEntreprisebdd =loadRepCompetence();
	Iterator <RepCompetenceBean>iterator=listeAInserer.iterator();
	
	//Verification de l'integrité des données à inserer doublon avec les données de la base
	
	List <RepCompetenceBean> listeAInsererFinal=new ArrayList <RepCompetenceBean>();
	ArrayList<RepCompetenceBean>strctureEntreprisebdd =loadRepCompetence();
	Iterator <RepCompetenceBean>iterator=listeAInserer.iterator();
	
	while(iterator.hasNext())
	{
		//System.out.println("1");
		
		RepCompetenceBean bean=(RepCompetenceBean)iterator.next();
		
		Iterator<RepCompetenceBean> index=strctureEntreprisebdd.iterator();
		boolean donneerejete=false;
		while(index.hasNext())
		{
			//System.out.println("2");
			
			RepCompetenceBean bean2=(RepCompetenceBean)index.next();
			if(bean.getFamille().equals(bean2.getFamille()))
			{
				//System.out.println("3");
				
				listeDonneesRejetes.add(bean);
				donneerejete=true;
				continue;
			}
		}
		if(!donneerejete)
		{
			//System.out.println("ajout donnee"+bean.getCodestructure() );
			
			listeAInsererFinal.add(bean);
		}
		//System.out.println("4");
		
	}
	
	//Insertion des données dans la table Structure_entreprise
	Iterator<RepCompetenceBean> index =listeAInserer.iterator();
	while(index.hasNext())
	{
		RepCompetenceBean donneeBean=(RepCompetenceBean)index.next();
		
		addRepCompBean(donneeBean);			
	}
	
		
		HashMap <String,List<RepCompetenceBean>> donneeMap=new HashMap<String,List<RepCompetenceBean>>();
		donneeMap.put("inserer", listeAInserer);
		donneeMap.put("supprimer", listeDonneesRejetes);
	//System.out.println("taille liste= "+ liste.size());
	
	return donneeMap;
}
*/


public HashMap getListFormation() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt = null;
	HashMap map = new HashMap();
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String profile_list="select   code_formation ,concat(libelle_formation,'-',libelle_diplome) as libelle_formation  from formation"; 
		ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
		
		
		while(rs.next()){
			map.put(rs.getString("libelle_formation"), rs.getString("code_formation"));
			//list_profile.add(rs.getString("libelle_profile"));
        }
		stmt.close();conn.close();
	} 
	catch (SQLException e){
			e.printStackTrace();
			stmt.close();conn.close();
	}
	
	return map;
}	

public HashMap getListPostes() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt = null;
	HashMap map = new HashMap();
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String profile_list="select code_poste,intitule_poste from poste_travail_description"; 
		ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
		
		
		while(rs.next()){
			map.put(rs.getString("intitule_poste"), rs.getString("code_poste"));
			//list_profile.add(rs.getString("libelle_profile"));
        }
		stmt.close();conn.close();
	} 
	catch (SQLException e){
			e.printStackTrace();
			stmt.close();conn.close();
	}
	
	return map;
}	

public String getMaxKeyCode() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt = null;
	String result ="";
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String profile_list="select max(code_poste) as max_code from poste_travail_description"; 
		ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
		
		
		while(rs.next()){
			result=rs.getString("max_code");
			//list_profile.add(rs.getString("libelle_profile"));
        }
		stmt.close();conn.close();
	} 
	catch (SQLException e){
			e.printStackTrace();
			stmt.close();conn.close();
	}
	
	return result;
}	




public HashMap getListStructure() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt = null;
	HashMap map = new HashMap();
		
	try 
	{
		stmt = (Statement) conn.createStatement();
		String profile_list="select code_structure  from structure_entreprise"; 
		ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
		
		
		while(rs.next()){
			map.put(rs.getString("code_structure"), rs.getString("code_structure"));
			//list_profile.add(rs.getString("libelle_profile"));
        }
		stmt.close();conn.close();
	} 
	catch (SQLException e){
			e.printStackTrace();
			stmt.close();conn.close();
	}
	
	return map;
}	

public String removeString(String chaine){
	
	String chaine_trt=chaine;
           chaine_trt=chaine_trt.replaceAll("’"," ");
	return chaine_trt;
}


public HashMap setlectedStructure(String code_structure) throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt = null;
	HashMap map = new HashMap();

	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String sql_query="select concat_ws('-->',libelle_division,libelle_direction,libelle_unite,libelle_departement,libelle_service,libelle_section) as structure from structure_entreprise where code_structure=#code_structure";
		sql_query = sql_query.replaceAll("#code_structure", "'"+code_structure+"'");
		ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
		
		
		while(rs.next()){
			map.put( rs.getString("structure"),rs.getString("structure"));
        }
		stmt.close();conn.close();
	} 
	catch (SQLException e){
			e.printStackTrace();
			stmt.close();conn.close();
	}
	
	return map;
	
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
public HashMap isCadre() 
{
	
	HashMap map = new HashMap();
	
	map.put("OUI", "Y");
	map.put("NON", "N");
	return map;
}


}
