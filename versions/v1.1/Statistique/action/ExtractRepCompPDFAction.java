package Statistique.action;
import java.io.*;
import java.sql.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.zkoss.util.media.AMedia;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;



import org.zkoss.zk.ui.Sessions;

import org.zkoss.zk.ui.util.GenericForwardComposer;

import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Listbox;

import Statistique.model.ExtractRepCompModel;

import java.sql.Connection;
import common.CreateDatabaseCon;


public class ExtractRepCompPDFAction extends  GenericForwardComposer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Iframe report;
	Listbox format;
/*public void onClick$pdfDownload() throws IOException, SQLException  {
	
	    InputStream is = null;
	    InputStream is1=null;
	    CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
        try {
            //generate report pdf stream
        	
        	// Get the real path for the report
    		String repSrc = Sessions.getCurrent().getWebApp().getRealPath("D:/cvsviews/zkevalcom/WebContent/WEB-INF/report/repcomp_pst_travail.jasper");
    		String subDir = Sessions.getCurrent().getWebApp().getRealPath("D:/cvsviews/zkevalcom/WebContent/WEB-INF/report/") + "/";

            is = this.getClass().getResourceAsStream(repSrc);
            is1 = this.getClass().getClassLoader().getResourceAsStream(repSrc);
                
            final Map params = new HashMap();
            params.put("ReportTitle", "The First Jasper Report Ever");
            params.put("MaxOrderID", new Integer(10500));
        	

            final byte[] buf = 
                JasperRunManager.runReportToPdf(is, params, conn);
                
            //prepare the AMedia for iframe
            final InputStream mediais = new ByteArrayInputStream(buf);
            final AMedia amedia = 
                new AMedia("FirstReport.pdf", "pdf", "application/pdf", mediais);
                
            //set iframe content
            report.setContent(amedia);
            
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
        	conn.close();
            if (is != null) {
                is.close();
            }
        }
    }*/

public void onClick$pdfDownload() throws IOException, SQLException  {
//Preparing parameters
//	
//	 // - Connexion à la base
//	CreateDatabaseCon dbcon=new CreateDatabaseCon();
//	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
//	try {
//    
//		 Map parametres = new HashMap();
//		 String jasperPath="./WEB-INF/report/";
//		 //parametres.put( "jasperPath", jasperPath );
//
//        // - Chargement et compilation du rapport
//		 String repSrc = Sessions.getCurrent().getWebApp().getRealPath("./WEB-INF/report/report1.jrxml");
// 		 JasperDesign jDesign = JRXmlLoader.load(repSrc);
//		 JasperReport jReport = JasperCompileManager.compileReport(jDesign);
//		 JasperPrint jasperPrint = JasperFillManager.fillReport( jReport,
//				 null, conn);
//		byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
//
//      
//		final InputStream mediais = new ByteArrayInputStream(bytes);
//        final AMedia amedia = 
//            new AMedia("FirstReport.pdf", "pdf", "application/pdf", mediais);
//            
//        //set iframe content
//        report.setContent(amedia);
//    } catch (JRException e) {
//
//        e.printStackTrace();
//    }  finally {
//       
//             conn.close();
//           
//    }
	if(format.getSelectedItem().getLabel().equals("PDF"))
	{
		downloadInPDFFormat();
	}
	else
		if(format.getSelectedItem().getLabel().equals("Excel"))
		{
			downloadInExcelFormat();
		}

}


/**
 * Cette méthode permet d'exporter les données competence poste de travail sous format Excel
 * @throws IOException
 * @throws SQLException
 */
@SuppressWarnings("deprecation")
public void downloadInExcelFormat() throws IOException, SQLException
{
	//récupération des données 
	
	ExtractRepCompModel extractRepCompModel=new ExtractRepCompModel();
	ArrayList<String> listePosteTravail=extractRepCompModel.getPoste_Travail();
	HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> mapFamilleGroupeCompetencePoste =extractRepCompModel.getCompagneCompetence();
	HashMap<String, Integer> mapFamilleNbCompetence=/*extractRepCompModel.*/getNBCompetenceParFamille(mapFamilleGroupeCompetencePoste);
	HashMap<String, Integer> mapGroupeNbCompetence=/*extractRepCompModel.*/getNBCompetenceParGroupe(mapFamilleGroupeCompetencePoste);
	HashMap <String, Short> mapFamilleColor=new HashMap<String, Short>();
	
	int nbCompAll=getNBCompetence(mapFamilleGroupeCompetencePoste);
	//creation du fichier xls

	//creation d'un document excel 
	HSSFWorkbook workBook = new HSSFWorkbook();
	
	//creation du style de texte
	
	HSSFFont font1 = workBook.createFont();
    font1.setFontHeightInPoints((short)8);
    font1.setFontName("Arial");


    /************************************************/
    
	
	//creation de l'onglet
	HSSFSheet sheet = workBook.createSheet("Répartition des compétences par poste de travail");
	
ArrayList<HSSFCell> liste=new ArrayList <HSSFCell>();	
for(int h=1;h<nbCompAll+1;h++)
{
	HSSFRow row3 = sheet.createRow(h);
	HSSFCell c=row3.createCell(2);
	liste.add(c);
}
	
	//creation de l'entête du document excel
	
	//cellule Famille
	HSSFRow row = sheet.createRow(0);
	
	HSSFCell cell = row.createCell((short)0);
	
	
	HSSFCellStyle cellStyle = null;
	cellStyle = workBook.createCellStyle();
	cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
	cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	
	cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
    cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
    cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
    cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
    
    cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
    cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    
    //cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    //cellStyle.setWrapText(true);
    cellStyle.setFont(font1);
    cell.setCellValue("Familles");
 	cell.setCellStyle(cellStyle);
 	
 	//cellule Groupe
 	
 	HSSFCell cell1 = row.createCell((short)1);
	
	

    //cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    //cellStyle.setWrapText(true);
    //cellStyle.setFont(font1);
    cell1.setCellValue("Groupes");
 	cell1.setCellStyle(cellStyle);
 	
 	
 	//cellule Compétence
 	
 	HSSFCell cell2 = row.createCell((short)2);
	
	

    //cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    //cellStyle.setWrapText(true);
    //cellStyle.setFont(font1);
    cell2.setCellValue("Compétences");
 	cell2.setCellStyle(cellStyle);
 	
 	
 	//création des cellules poste de travail
 	Iterator <String> iteratorposteTravail=listePosteTravail.iterator();
 	
 	int indexColonne=3;
 	while(iteratorposteTravail.hasNext())
 	{
 		String intitule_poste_travail=iteratorposteTravail.next();
 		
 		
		HSSFCellStyle cellStyle1 = null;
		cellStyle1 = workBook.createCellStyle();
		cellStyle1.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//        cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        
        //specification des bordures des cellules
        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBottomBorderColor(HSSFColor.BLACK.index);
        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setLeftBorderColor(HSSFColor.BLACK.index);
        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setRightBorderColor(HSSFColor.BLACK.index);
        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setTopBorderColor(HSSFColor.BLACK.index);
        cellStyle1.setFont(font1);
 	   short io=90;
       cellStyle1.setRotation(io);
       
       HSSFCell cellCompetence = row.createCell((short)indexColonne);
	 	
	 	cellCompetence.setCellStyle(cellStyle1);
	 	cellCompetence.setCellValue(intitule_poste_travail);
	 	
	 	
	 	indexColonne++;
 	}
 	
 	//creation des cellules familles au autres
 	
 	int numeroLigne=1;
 	Set <String> setFamilleGroupeCompetencePoste=mapFamilleGroupeCompetencePoste.keySet();
 	Iterator<String> iteratorFamille=setFamilleGroupeCompetencePoste.iterator();
 	while(iteratorFamille.hasNext())
 	{
 		String famille=iteratorFamille.next();
 		
 		int nbCompetence=mapFamilleNbCompetence.get(famille);
 		
 		//creation de la ligne famille
 		HSSFRow row1 = sheet.createRow(numeroLigne);
 		
 		
 		
 		short couleur=HSSFColor.RED.index;
 		if(famille.equalsIgnoreCase("Affaires"))
 			 couleur=HSSFColor.RED.index;
 		else
 			if(famille.equalsIgnoreCase("RELATIONNELLES"))
 				couleur=HSSFColor.GREEN.index;
 			else
 				if(famille.equalsIgnoreCase("PERSONNELLES"))
 					couleur=HSSFColor.LIGHT_BLUE.index;
 				else
 					if(famille.equalsIgnoreCase("Operationnelles"))
 						couleur=HSSFColor.ORCHID.index;
 					else//couleur orange par défaut
 						couleur=HSSFColor.ORANGE.index;
 		mapFamilleColor.put(famille, new Short(couleur));
		
 		HSSFCellStyle cellStyle2 = null;
 		cellStyle2 = workBook.createCellStyle();
 		cellStyle2.setFillForegroundColor(couleur);
 		
 		cellStyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
 		
 		cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	    cellStyle2.setBottomBorderColor(HSSFColor.BLACK.index);
 	    cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
 	    cellStyle2.setLeftBorderColor(HSSFColor.BLACK.index);
 	    cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
 	    cellStyle2.setRightBorderColor(HSSFColor.BLACK.index);
 	    cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
 	    cellStyle2.setTopBorderColor(HSSFColor.BLACK.index);
 	    
 	    cellStyle2.setAlignment(CellStyle.ALIGN_CENTER);
 	    cellStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

 	    
 	    cellStyle2.setFont(font1);
 	    
 	    int finRow=numeroLigne+nbCompetence-1;
 	    
 
 	 	
 
 	    //creation de la première colonne (familles)
 		HSSFCell cell3 = row1.createCell((short)0);
 	    cell3.setCellValue(famille);
 	 	cell3.setCellStyle(cellStyle2);
 	 	
 	   Region region = new Region(numeroLigne,(short)0,finRow,(short)0);
	    //sheet.addMergedRegion(new CellRangeAddress(numeroLigne,(short)finRow,0,(short)0));
	    
	  sheet.addMergedRegion(region);
	  //specification du style de la region
	  
	 HSSFRegionUtil.setBorderBottom( HSSFCellStyle.BORDER_THIN,
	        region, sheet, workBook );
	    HSSFRegionUtil.setBorderTop( HSSFCellStyle.BORDER_THIN,
	        region, sheet, workBook );
	    HSSFRegionUtil.setBorderLeft( HSSFCellStyle.BORDER_THIN,
	        region, sheet, workBook );
	    HSSFRegionUtil.setBorderRight( HSSFCellStyle.BORDER_THIN,
	        region, sheet, workBook );
	   HSSFRegionUtil.setBottomBorderColor(HSSFColor.BLACK.index, region, sheet, workBook);
	    HSSFRegionUtil.setTopBorderColor(HSSFColor.BLACK.index, region, sheet, workBook);
	    HSSFRegionUtil.setLeftBorderColor(HSSFColor.BLACK.index, region, sheet, workBook);
	    HSSFRegionUtil.setRightBorderColor(HSSFColor.BLACK.index, region, sheet, workBook);
	   
 	 	//creation lignes groupe
 	 	
 	 	int debutGroupeLigne=numeroLigne;
 	 	HashMap<String, HashMap<String, ArrayList<String>>>mapGroupeCompetencePoste= mapFamilleGroupeCompetencePoste.get(famille);
 	 	Set<String> setGroupeCompetencePoste=mapGroupeCompetencePoste.keySet();
 	 	Iterator<String> iteratorGroupe=setGroupeCompetencePoste.iterator();
 	 	int i=0;
 	 	while(iteratorGroupe.hasNext())
 	 	{
 	 		
 	 		String groupe=iteratorGroupe.next();
 	 		int nbCompetenceG=mapGroupeNbCompetence.get(groupe);
 	 		int finGRow=debutGroupeLigne+nbCompetenceG-1;
 	 		//sheet.addMergedRegion(new CellRangeAddress(debutGroupeLigne,(short)finGRow,1,(short)1));
	 		
 	 	    
 	 	    //creation de la première colonne (familles)

 	 	  HSSFRow row2=null;
 	 	 	
 	 	 	if(i==0)
 	 	 	{
 	 	 		HSSFCell cell4 = row1.createCell((short)1);
 	 	 	    cell4.setCellValue(groupe);
 	 	 	 	cell4.setCellStyle(cellStyle2);
 	 	 	}
 	 	 	else
 	 	 	{
 	 	 		row2 = sheet.createRow(debutGroupeLigne);
 	 	 		HSSFCell cell4 = row2.createCell((short)1);
 	 	 	    cell4.setCellValue(groupe);
 	 	 	 	cell4.setCellStyle(cellStyle2);
 	 	 	}
 	 	 	
 	 	  /*******************************/
  	 	   Region region1 = new Region(debutGroupeLigne,(short)1,finGRow,(short)1);
  	 	    //sheet.addMergedRegion(new CellRangeAddress(numeroLigne,(short)finRow,0,(short)0));
  	 	    
  	 	  sheet.addMergedRegion(region1);
  	 	  //specification du style de la region
  	 	  
  	 	 HSSFRegionUtil.setBorderBottom( HSSFCellStyle.BORDER_THIN,
  	 	        region1, sheet, workBook );
  	 	    HSSFRegionUtil.setBorderTop( HSSFCellStyle.BORDER_THIN,
  	 	        region1, sheet, workBook );
  	 	    HSSFRegionUtil.setBorderLeft( HSSFCellStyle.BORDER_THIN,
  	 	        region1, sheet, workBook );
  	 	    HSSFRegionUtil.setBorderRight( HSSFCellStyle.BORDER_THIN,
  	 	        region1, sheet, workBook );
  	 	   HSSFRegionUtil.setBottomBorderColor(HSSFColor.BLACK.index, region1, sheet, workBook);
  	 	    HSSFRegionUtil.setTopBorderColor(HSSFColor.BLACK.index, region1, sheet, workBook);
  	 	    HSSFRegionUtil.setLeftBorderColor(HSSFColor.BLACK.index, region1, sheet, workBook);
  	 	    HSSFRegionUtil.setRightBorderColor(HSSFColor.BLACK.index, region1, sheet, workBook);
  	 	   	 		
  	 		
 /***********************************/ 	 	 	
 	 	 	//creation lignes competence
 	 	 	
 	 	 	//////////////////////////////////////////////
 	 	 	int debutCompetenceLigne=debutGroupeLigne;
 	 	 	HashMap<String, ArrayList<String>>mapCompetencePoste= mapGroupeCompetencePoste.get(groupe);
 	 	 	Set<String> setCompetencePoste=mapCompetencePoste.keySet();
 	 	 	Iterator<String> iteratorCompetence=setCompetencePoste.iterator();
 	 	 	int j=debutGroupeLigne;
 	 	 	while(iteratorCompetence.hasNext())
 	 	 	{
 	 	 		
 	 	 		String competence=iteratorCompetence.next();
 	 	 		
 	 	 		HSSFCellStyle cellStyle3 = null;
 	 	 		cellStyle3 = workBook.createCellStyle();
 	 	 		cellStyle3.setFillForegroundColor(HSSFColor.WHITE.index);
 	 		
 	 	 		cellStyle3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
 	 		
 	 	 		cellStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	 	 		cellStyle3.setBottomBorderColor(HSSFColor.BLACK.index);
 	 	 		cellStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
 	 	 		cellStyle3.setLeftBorderColor(HSSFColor.BLACK.index);
 	 	 		cellStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);
 	 	 		cellStyle3.setRightBorderColor(HSSFColor.BLACK.index);
 	 	 		cellStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);
 	 	 		cellStyle3.setTopBorderColor(HSSFColor.BLACK.index);
 	 	    
 	 	 		cellStyle3.setAlignment(CellStyle.ALIGN_LEFT);
 	 	 		cellStyle3.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
 	 	    
 	 	 		cellStyle3.setFont(font1);
 	 	 		HSSFRow rowCree=null;
 	 	 	 	
// 	 	 	 	if((i==0) && (j==debutGroupeLigne))
// 	 	 	 	{
// 	 	 	 		
// 	 	 	 		HSSFCell cell4 = row1.createCell((short)2);
//	 	 	 	    cell4.setCellValue(competence);
//	 	 	 	 	cell4.setCellStyle(cellStyle3);
//	 	 	 	 	rowCree=row1;
// 	 	 	 	}
// 	 	 	 	else
// 	 	 	 	{
// 	 	 	 		if(j==debutGroupeLigne)
// 	 	 	 		{
// 	 	 	 			
// 	 	 	 			HSSFCell cell4 = row2.createCell((short)2);
// 	 	 	 			cell4.setCellValue(competence);
// 	 	 	 			cell4.setCellStyle(cellStyle3);
// 	 	 	 			rowCree=row2;
// 	 	 	 		}
// 	 	 	 		else
// 	 	 	 		{

 	 	 	 			
 	 	 	 			///HSSFRow row3 = sheet.createRow(j);
 	 	 	 			HSSFCell cell4 = liste.get(j-1);//row3.createCell((short)2);
 	 	 	 			cell4.setCellValue(competence);
 	 	 	 			cell4.setCellStyle(cellStyle3);
 	 	 	 			rowCree=cell4.getRow();
 	 	 	 		
//
// 	 	 	 		}
// 	 	 	 	}

 	 	 	 	
 	 	 	 	//cellule poste de travail 
 	 	 	 	
 	 	 	 	ArrayList<String> mapPosteTravail=mapCompetencePoste.get(competence);
 	 	 	 	Iterator<String> iteratorPoste=listePosteTravail.iterator();
 	 	 	 	int numColonnePoste=3;
 	 	 	 	while(iteratorPoste.hasNext())
 	 	 	 	{
 	 	 	 		HSSFCell cell5=rowCree.createCell(numColonnePoste);
 	 	 	 		String poste=iteratorPoste.next();
 	 	 	 		if(mapPosteTravail.contains(poste))
 	 	 	 		{
 	 	 	 			cell5.setCellStyle(cellStyle2);
 	 	 	 			
 	 	 	 		}
 	 	 	 		else
 	 	 	 		{
 	 	 	 			cell5.setCellStyle(cellStyle3);
 	 	 	 		}
 	 	 	 		numColonnePoste++;
 	 	 	 	}
 	 	 	 	debutCompetenceLigne ++;
 	 	 	 	j++;
 	 	 	}
  	 	
 	 	 	
 	 	 	///////////////////////////////////////////////
 	 	 	
 	 	 	debutGroupeLigne=debutGroupeLigne+nbCompetenceG;
 	 	 	i++;
 	 	}
 	 	
 	 	//incrementation des lignes
 	 	numeroLigne=numeroLigne+nbCompetence;
 	 	
 	 	 
 	}
 	
 	int nbColonne=listePosteTravail.size()+3;
 	for (int i=0;i<nbColonne+1;i++)
 	{
 		sheet.autoSizeColumn(i);
 		
 	}	
    /***********************************************************/
    
    
	
 	FileOutputStream fOut;
 	try 
 	{
 		fOut = new FileOutputStream("Repartition_Competence_PosteTravail.xls");
 		workBook.write(fOut);
		fOut.flush();
		fOut.close();
	
		File file = new File("Repartition_Competence_PosteTravail.xls");
		Filedownload.save(file, "XLS");
 	} 
 	catch (FileNotFoundException e) 
 	{
 		// TODO Auto-generated catch block
 		e.printStackTrace();
 	}
 	catch (IOException e) 
 	{
	// TODO Auto-generated catch block
 		e.printStackTrace();
 	}
					

}


/**
 * Cette méthode permet d'exporter les données competence poste de travail sous format PDF
 * @throws IOException
 * @throws SQLException
 */

public void downloadInPDFFormat() throws IOException, SQLException
{
	 // - Connexion à la base
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	try {
    
		 Map parametres = new HashMap();
		 String jasperPath="./WEB-INF/report/";
		 //parametres.put( "jasperPath", jasperPath );

        // - Chargement et compilation du rapport
		 String repSrc = Sessions.getCurrent().getWebApp().getRealPath("./WEB-INF/report/report1.jrxml");
 		 JasperDesign jDesign = JRXmlLoader.load(repSrc);
		 JasperReport jReport = JasperCompileManager.compileReport(jDesign);
		 JasperPrint jasperPrint = JasperFillManager.fillReport( jReport,
				 null, conn);
		byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

      
		final InputStream mediais = new ByteArrayInputStream(bytes);
        final AMedia amedia = 
            new AMedia("FirstReport.pdf", "pdf", "application/pdf", mediais);
            
        //set iframe content
        report.setContent(amedia);
    } catch (JRException e) {

        e.printStackTrace();
    }  finally {
       
             conn.close();
           
    }
}
/**
 * retourne le nombre de competence par famille
 * @param mapFamilleGroupeCompetencePoste
 * @return
 */
public HashMap<String, Integer> getNBCompetenceParFamille(HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> mapFamilleGroupeCompetencePoste)
{
	HashMap<String, Integer> mapFamilleNbCompetence=new HashMap<String, Integer>();
	
	Set<String> setFamille=mapFamilleGroupeCompetencePoste.keySet();
	Iterator<String> iteratorFamille=setFamille.iterator();
	while(iteratorFamille.hasNext())
	{
		int nbCompetence=0;
		String famille=iteratorFamille.next();
		HashMap<String, HashMap<String, ArrayList<String>>> mapGroupeCompetencePoste=mapFamilleGroupeCompetencePoste.get(famille);
		Set<String> setGroupe=mapGroupeCompetencePoste.keySet();
		Iterator<String > iteratorGroupe=setGroupe.iterator();
		while(iteratorGroupe.hasNext())
		{
			String groupe=iteratorGroupe.next();
			HashMap<String, ArrayList<String>> mapCompetencePoste=mapGroupeCompetencePoste.get(groupe);
			int nbComp=mapCompetencePoste.size();
			nbCompetence=nbCompetence+nbComp;
		}
		mapFamilleNbCompetence.put(famille, nbCompetence);
		
	}
	return mapFamilleNbCompetence;
	
}

public HashMap<String, Integer> getNBCompetenceParGroupe(HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> mapFamilleGroupeCompetencePoste)
{
	HashMap<String, Integer> mapGroupeNbCompetence=new HashMap<String, Integer>();
	
	Set<String> setFamille=mapFamilleGroupeCompetencePoste.keySet();
	Iterator<String> iteratorFamille=setFamille.iterator();
	while(iteratorFamille.hasNext())
	{
		
		String famille=iteratorFamille.next();
		HashMap<String, HashMap<String, ArrayList<String>>> mapGroupeCompetencePoste=mapFamilleGroupeCompetencePoste.get(famille);
		Set<String> setGroupe=mapGroupeCompetencePoste.keySet();
		Iterator<String > iteratorGroupe=setGroupe.iterator();
		while(iteratorGroupe.hasNext())
		{
			
			String groupe=iteratorGroupe.next();
			HashMap<String, ArrayList<String>> mapCompetencePoste=mapGroupeCompetencePoste.get(groupe);
			int nbComp=mapCompetencePoste.size();
			mapGroupeNbCompetence.put(groupe, nbComp);
			
		}
		
		
	}
	return mapGroupeNbCompetence;
	
}

public int getNBCompetence(HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> mapFamilleGroupeCompetencePoste)
{
	HashMap<String, Integer> mapGroupeNbCompetence=new HashMap<String, Integer>();
	
	Set<String> setFamille=mapFamilleGroupeCompetencePoste.keySet();
	Iterator<String> iteratorFamille=setFamille.iterator();
	int nbCompetence=0;
	while(iteratorFamille.hasNext())
	{
		
		String famille=iteratorFamille.next();
		HashMap<String, HashMap<String, ArrayList<String>>> mapGroupeCompetencePoste=mapFamilleGroupeCompetencePoste.get(famille);
		Set<String> setGroupe=mapGroupeCompetencePoste.keySet();
		Iterator<String > iteratorGroupe=setGroupe.iterator();
		while(iteratorGroupe.hasNext())
		{
			
			String groupe=iteratorGroupe.next();
			HashMap<String, ArrayList<String>> mapCompetencePoste=mapGroupeCompetencePoste.get(groupe);
			int nbComp=mapCompetencePoste.size();
			//mapGroupeNbCompetence.put(groupe, nbComp);
			nbCompetence=nbCompetence+nbComp;
			
		}
		
		
	}
	return nbCompetence;
	
}
}
