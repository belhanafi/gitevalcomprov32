package compagne.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.CategoryModel;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Foot;
import org.zkoss.zul.Footer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;

import Statistique.bean.StatTrancheAgePosteBean;
import Statistique.model.EmployeModel;
import Statistique.model.StatCotationEmployeModel;



import common.view.MenuComposer;
import compagne.bean.MatriceCotationBean;
import compagne.model.PlanningCompagneModel;
import compagne.model.ResultatEvaluationModel;

import org.zkoss.zk.ui.Sessions;



public class ResultatEvaluationAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Tabs tbtabs;
	Tabpanels tbpanels;
	Listbox nomCompagne;
	String selected_id_compagne="1";
	Popup pp_export_xls;
	HashMap <String, Checkbox> selectedCheckBox;
	HashMap <String, Checkbox> unselectedCheckBox;
	Checkbox chbox_all;
	Checkbox chbox_mat;
	Listbox  comp_struct_ent_list;
	Listbox comp_poste_list;
	Label label_poste;
	int compteur=0;
	Button exporter;
	Button exporterMat;
	static  long chrono = 0 ; 



	HashMap<String, String> map_compagne_idCompagne;




	HashMap <String, HashMap<String, ArrayList<String>>> mapPosteFamilleCompetence;

	HashMap<String, HashMap<String, HashMap< String, HashMap<String, Double>> >> mapPosteEmployeFamilleCompetence;

	HashMap<String, HashMap<String, String>> mapEmployeFamilleIMI;
	HashMap<String, HashMap<String, Double>> mapFamilleIMG;
	HashMap<String, Double> mapPosteIMG;

	HashMap<String, HashMap<String, HashMap< String, Double>>> mapPostFamilleCompetenceStats;

	HashMap<String, ArrayList<String>> mapPostEmployeTriIMI;
	int call_compteur=0;
	private static String templateFileName = "/object_collection_template.xls"; 
	private static String destFileName = "MatriceCotation.xls";
	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		//System.out.println("doAfterCompose executed");


		super.doAfterCompose(comp);

		comp.setVariable(comp.getId() + "Ctrl", this, true);

		//récupération de la liste des compagnes


		ResultatEvaluationModel cotationMoel=new ResultatEvaluationModel();
		Set set = (cotationMoel.getListLastCompagneValid()).entrySet(); 
		Iterator i = set.iterator();

		// Affichage de la liste des compagnes
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();

			nomCompagne.appendItem((String) me.getKey(),String.valueOf(me.getValue()));

			//profilemodel.add((String) me.getKey());
		}


		if(nomCompagne.getItemCount()>0 )
		{
			nomCompagne.setSelectedIndex(0);

			//String selectedNomCompagne=nomCompagne.getItemAtIndex(0).getLabel();
			//selected_id_compagne=map_compagne_idCompagne.get(selectedNomCompagne);
			selected_id_compagne=((String) nomCompagne.getSelectedItem().getValue());
						
		}

		EmployeModel init=new EmployeModel();
		String code_structure_str="";
		String code_poste="";

		// Affichage de la liste postes de travail
		comp_poste_list.appendItem("Tous Postes Travail","-1");

		Set set_ent = (init.getStructEntList()).entrySet(); 
		Iterator itr_ = set_ent.iterator();
		// Affichage de la liste des strcutures entreprise
		comp_struct_ent_list.appendItem("Toutes Structures","-1");
		while(itr_.hasNext()) {
			Map.Entry me = (Map.Entry)itr_.next();

			comp_struct_ent_list.appendItem((String) me.getKey(),(String) me.getValue());

			//profilemodel.add((String) me.getKey());
		}
		if(comp_struct_ent_list.getItemCount()>0){
			comp_struct_ent_list.setSelectedIndex(0);
			code_structure_str=(String) comp_struct_ent_list.getSelectedItem().getValue();
		}

		if(comp_poste_list.getItemCount()>0){
			comp_poste_list.setSelectedIndex(0);
			code_poste=(String) comp_poste_list.getSelectedItem().getValue();
		}

		exporter.setVisible(false);
		exporterMat.setVisible(false);
		//creation de tab0 et sa fermeture pour qu'i n'y ait pas de décalage lors de l'ajout des listboxs
		Tab newTab0 = new Tab(); 
		newTab0.isClosable();	
		tbtabs.appendChild(newTab0);			
		newTab0.close();



	}




	private void init() throws ParseException {
		ResultatEvaluationModel resultatEvaluationModel=new ResultatEvaluationModel();

		chrono = java.lang.System.currentTimeMillis() ; 
		mapPosteFamilleCompetence=resultatEvaluationModel.getInfosFamillesCompetenceRattrapage(selected_id_compagne);
		long chrono2 = java.lang.System.currentTimeMillis() ; 
		long temps = chrono2 - chrono ; 
		//System.out.println("1 Temps ecoule = " + temps + " ms") ; 
		chrono = java.lang.System.currentTimeMillis() ; 
		mapPosteEmployeFamilleCompetence=resultatEvaluationModel.getAllIMICompetence(selected_id_compagne);

		chrono2 = java.lang.System.currentTimeMillis() ; 
		temps = chrono2 - chrono ; 
		//System.out.println("2 Temps ecoule = " + temps + " ms") ; 

		chrono = java.lang.System.currentTimeMillis() ; 
		mapEmployeFamilleIMI=resultatEvaluationModel.getInfosIMIStat(selected_id_compagne);

		chrono2 = java.lang.System.currentTimeMillis() ; 
		temps = chrono2 - chrono ; 
		//System.out.println("3 Temps ecoule = " + temps + " ms") ; 

		chrono = java.lang.System.currentTimeMillis() ; 
		mapFamilleIMG=resultatEvaluationModel.getIMGFamille(selected_id_compagne);

		chrono2 = java.lang.System.currentTimeMillis() ; 
		temps = chrono2 - chrono ; 
		//System.out.println("4 Temps ecoule = " + temps + " ms") ; 

		chrono = java.lang.System.currentTimeMillis() ; 
		mapPosteIMG=resultatEvaluationModel.getIMGparPoste(selected_id_compagne);

		chrono2 = java.lang.System.currentTimeMillis() ; 
		temps = chrono2 - chrono ; 
		//System.out.println("5 Temps ecoule = " + temps + " ms") ; 

		chrono = java.lang.System.currentTimeMillis() ; 
		mapPostFamilleCompetenceStats=resultatEvaluationModel.getmoyPosteCompetenceStatsRattrapage(selected_id_compagne);

		chrono2 = java.lang.System.currentTimeMillis() ; 
		temps = chrono2 - chrono ; 
		//System.out.println("6 Temps ecoule = " + temps + " ms") ; 

		chrono = java.lang.System.currentTimeMillis() ; 
		mapPostEmployeTriIMI =resultatEvaluationModel.getEmployeTriIMI(selected_id_compagne);
		chrono2 = java.lang.System.currentTimeMillis() ; 
		temps = chrono2 - chrono ; 
		//System.out.println("7 Temps ecoule = " + temps + " ms") ; 
		//AfficherInfosCompagne();
	}




	@SuppressWarnings("deprecation")
	public void onClick$exporter() throws InterruptedException
	{

		if (Messagebox.show("Voulez vous exporter les résultats détaillés ?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			exportResultatGlobalExlFile();

			return;
		}

		else{
			return;
		}
	}

	public void onClick$exporterMat() throws InterruptedException, ParsePropertyException, InvalidFormatException, IOException
	{
		if (Messagebox.show("Voulez vous exporter la matrice de cotation?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			//exportMatriceCotationExlFileV2();
			exportMatriceCotationExlFileV3();

			return;
		}

		else{
			return;
		}

	}


	/*
	 * Methode d'export les resultats globaux de la compagne sous format Excel
	 * 
	 */

	private void exportResultatGlobalExlFile() {
		if(nomCompagne.getItemCount()!=0)
		{
			//récupération des informations d'entête du fichier excel


			//récupération du nombre de compétence toute famille confondu


			//creation du fichier xls

			//creation d'un document excel 
			HSSFWorkbook workBook = new HSSFWorkbook();

			//creation du style de texte

			HSSFFont font1 = workBook.createFont();
			font1.setFontHeightInPoints((short)8);
			font1.setFontName("Arial");

			//Style
			HSSFCellStyle cellStylea = null;
			cellStylea = workBook.createCellStyle();

			cellStylea.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStylea.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStylea.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylea.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylea.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylea.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylea.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylea.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylea.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylea.setFont(font1);
			cellStylea.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylea.setFillForegroundColor(HSSFColor.RED.index);

			HSSFCellStyle cellStyleaa = null;
			cellStyleaa = workBook.createCellStyle();

			cellStyleaa.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStyleaa.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyleaa.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleaa.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleaa.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleaa.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleaa.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleaa.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleaa.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleaa.setFont(font1);
			cellStyleaa.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleaa.setFillForegroundColor(HSSFColor.RED.index);

			//Style
			HSSFCellStyle cellStyleb = null;
			cellStyleb = workBook.createCellStyle();

			cellStyleb.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStyleb.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyleb.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleb.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleb.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleb.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleb.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleb.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleb.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleb.setFont(font1);
			cellStyleb.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleb.setFillForegroundColor(HSSFColor.GREEN.index);

			HSSFCellStyle cellStylebb = null;
			cellStylebb = workBook.createCellStyle();

			cellStylebb.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStylebb.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStylebb.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylebb.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylebb.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylebb.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylebb.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylebb.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylebb.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylebb.setFont(font1);
			cellStylebb.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylebb.setFillForegroundColor(HSSFColor.GREEN.index);
			//Style
			HSSFCellStyle cellStylec = null;
			cellStylec = workBook.createCellStyle();

			cellStylec.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStylec.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStylec.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylec.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylec.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylec.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylec.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylec.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylec.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylec.setFont(font1);
			cellStylec.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylec.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);

			HSSFCellStyle cellStylecc = null;
			cellStylecc = workBook.createCellStyle();

			cellStylecc.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStylecc.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStylecc.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylecc.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylecc.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylecc.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylecc.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylecc.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylecc.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylecc.setFont(font1);
			cellStylecc.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylecc.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
			//Style
			HSSFCellStyle cellStyled = null;
			cellStyled = workBook.createCellStyle();

			cellStyled.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStyled.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyled.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyled.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyled.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyled.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyled.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyled.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyled.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyled.setFont(font1);
			cellStyled.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyled.setFillForegroundColor(HSSFColor.ORCHID.index);

			HSSFCellStyle cellStyledd = null;
			cellStyledd = workBook.createCellStyle();

			cellStyledd.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStyledd.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyledd.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyledd.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyledd.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyledd.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyledd.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyledd.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyledd.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyledd.setFont(font1);
			cellStyledd.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyledd.setFillForegroundColor(HSSFColor.ORCHID.index);
			//style
			HSSFCellStyle cellStyle0 = null;
			cellStyle0 = workBook.createCellStyle();
			cellStyle0.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyle0.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyle0.setFont(font1);


			cellStyle0.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle0.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle0.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle0.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle0.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle0.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle0.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle0.setTopBorderColor(HSSFColor.BLACK.index);

			cellStyle0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//style
			HSSFCellStyle cellStyle10 = null;
			cellStyle10 = workBook.createCellStyle();
			cellStyle10.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyle10.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyle10.setAlignment(CellStyle.ALIGN_CENTER);

			//specification des bordures des cellules
			cellStyle10.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle10.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle10.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle10.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle10.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle10.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle10.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle10.setTopBorderColor(HSSFColor.BLACK.index);
			//style
			HSSFCellStyle cellStyle11 = null;
			cellStyle11 = workBook.createCellStyle();
			cellStyle11.setFillForegroundColor(HSSFColor.WHITE.index);

			cellStyle11.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyle11.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle11.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle11.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle11.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle11.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle11.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle11.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle11.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyle11.setAlignment(CellStyle.ALIGN_CENTER);
			//style

			//style
			HSSFCellStyle cellStyle6 = null;
			cellStyle6 = workBook.createCellStyle();
			cellStyle6.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			cellStyle6.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStyle6.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle6.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle6.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle6.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle6.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle6.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle6.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle6.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyle6.setFont(font1);
			cellStyle6.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//style
			//	    HSSFCellStyle cellStyle7 = null;
			// 		cellStyle7 = workBook.createCellStyle();
			// 		
			// 		cellStyle7.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			//	
			// 		cellStyle7.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			// 		cellStyle7.setBottomBorderColor(HSSFColor.BLACK.index);
			// 		cellStyle7.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			// 		cellStyle7.setLeftBorderColor(HSSFColor.BLACK.index);
			// 		cellStyle7.setBorderRight(HSSFCellStyle.BORDER_THIN);
			// 		cellStyle7.setRightBorderColor(HSSFColor.BLACK.index);
			// 		cellStyle7.setBorderTop(HSSFCellStyle.BORDER_THIN);
			// 		cellStyle7.setTopBorderColor(HSSFColor.BLACK.index);
			// 		cellStyle7.setFont(font1);
			// 		cellStyle7.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//style
			HSSFCellStyle cellStyle8 = null;
			cellStyle8 = workBook.createCellStyle();
			cellStyle8.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			cellStyle8.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStyle8.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle8.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle8.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle8.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle8.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle8.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle8.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle8.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyle8.setFont(font1);
			cellStyle8.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//style dernière ligne 
			HSSFCellStyle cellStyle9 = null;
			cellStyle9 = workBook.createCellStyle();
			cellStyle9.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			cellStyle9.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStyle9.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle9.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle9.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle9.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle9.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle9.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle9.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle9.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyle9.setFont(font1);
			cellStyle9.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//creation d'une feuille excel associé à un poste de travail

			//créatoin du squelette du fichier 
			Set <String>setPosteTravail=mapPosteFamilleCompetence.keySet();
			Iterator <String > iteratorPosteTravail=setPosteTravail.iterator();

			while (iteratorPosteTravail.hasNext())
			{	
				// construction de la feuille ligne par ligne



				String nomOnglet=iteratorPosteTravail.next();

				//creation de l'onglet
				//System.out.println("nomOnglet="+nomOnglet);
				HSSFSheet sheet = workBook.createSheet(nomOnglet);

				HashMap<String, ArrayList<String>> mapFamilleCompetence=mapPosteFamilleCompetence.get(nomOnglet);

				int nbToutesComp=getNbCompetenceAllFamille(mapFamilleCompetence);
				//creation de l'entête du document excel

				//cellule nom et prenom de l'evalué
				HSSFRow row = sheet.createRow(0);
				HSSFCell cell = row.createCell((short)0);



				sheet.addMergedRegion(new CellRangeAddress(0,(short)2,0,0));





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

				cell.setCellValue("Nom et Prénom de l'évalué");
				cell.setCellStyle(cellStyle);


				//cellule critère d'evaluation qui doit regrouper toutes les familles

				sheet.addMergedRegion(new CellRangeAddress(0,(short)0,1,(short)nbToutesComp));
				HSSFCell cell1 = row.createCell((short)1);
				cell1.setCellValue("Critères d'évaluation");
				cell1.setCellStyle(cellStyle);


				//cellule IMI 
				sheet.addMergedRegion(new CellRangeAddress(0,(short)2,nbToutesComp+1,(short)nbToutesComp+1));
				HSSFCell cell2 = row.createCell((short)nbToutesComp+1);
				cell2.setCellValue("IMI");
				cell2.setCellStyle(cellStyle);

				Set<String> setFamilleCompetence=mapFamilleCompetence.keySet();
				Iterator <String> iteratorFamilleCompetence=setFamilleCompetence.iterator();

				int indexColonne=1;
				HashMap<String, HSSFCellStyle> familleColor=new HashMap<String,HSSFCellStyle>();
				HashMap<String, Short> familleColor2=new HashMap<String,Short>();
				HSSFRow row1 = sheet.createRow(1);
				while(iteratorFamilleCompetence.hasNext())
				{
					String clesFamille=iteratorFamilleCompetence.next();
					int nbcompetence=mapFamilleCompetence.get(clesFamille).size();
					//System.out.println(clesFamille);
					//creation des secondes cellules associées aux familles
					HSSFCellStyle cellStyle5 = null;
					cellStyle5 = workBook.createCellStyle();
					short couleur=HSSFColor.RED.index;
					if(clesFamille.equalsIgnoreCase("Affaires"))
					{	 couleur=HSSFColor.RED.index;
					familleColor.put(clesFamille, cellStylea);
					familleColor.put(clesFamille+"1", cellStyleaa);
					}
					else
						if(clesFamille.equalsIgnoreCase("RELATIONNELLES"))
						{
							couleur=HSSFColor.GREEN.index;
							familleColor.put(clesFamille, cellStyleb);
							familleColor.put(clesFamille+"1", cellStylebb);
						}
						else
							if(clesFamille.equalsIgnoreCase("PERSONNELLES"))
							{
								couleur=HSSFColor.LIGHT_BLUE.index;
								familleColor.put(clesFamille, cellStylec);
								familleColor.put(clesFamille+"1", cellStylecc);
							}
							else
								if(clesFamille.equalsIgnoreCase("OPERATIONNELLES"))
								{
									couleur=HSSFColor.ORCHID.index;
									familleColor.put(clesFamille, cellStyled);
									familleColor.put(clesFamille+"1", cellStyledd);
								}
								else//couleur orange par défaut
								{
									couleur=HSSFColor.ORANGE.index;
									familleColor.put(clesFamille, cellStyled);
									familleColor.put(clesFamille+"1", cellStyledd);
								}
					familleColor2.put(clesFamille, couleur);
					cellStyle5.setFillForegroundColor(couleur);

					cellStyle5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cellStyle5.setAlignment(HSSFCellStyle.ALIGN_CENTER);

					sheet.addMergedRegion(new CellRangeAddress(1,(short)1,indexColonne,(short)indexColonne+nbcompetence-1));
					HSSFCell cellFamillle = row1.createCell((short)indexColonne);

					cellFamillle.setCellValue(clesFamille);
					cellFamillle.setCellStyle(cellStyle5);				 	
					indexColonne=indexColonne+nbcompetence;

				}

				//creation de la troisième ligne competences
				iteratorFamilleCompetence=setFamilleCompetence.iterator();

				HSSFRow row2 = sheet.createRow(2);
				short longueur=18*256;
				row2.setHeight(longueur);
				indexColonne=1;
				while(iteratorFamilleCompetence.hasNext())
				{
					String clesfamille=iteratorFamilleCompetence.next();
					ArrayList<String> listeCompetence=mapFamilleCompetence.get(clesfamille);

					HSSFCellStyle cellStyle1 =familleColor.get(clesfamille);

					Iterator <String> iteratorCompetence=listeCompetence.iterator();
					while(iteratorCompetence.hasNext())
					{
						String libelle_competence=iteratorCompetence.next();
						//		 			HSSFCellStyle cellStyle1 = null;
						//					cellStyle1 = workBook.createCellStyle();
						//		 			cellStyle1.setFillForegroundColor(couleur);
						//		 			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
						//					cellStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						//
						//			        
						//			        //specification des bordures des cellules
						//			        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
						//			        cellStyle1.setBottomBorderColor(HSSFColor.BLACK.index);
						//			        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
						//			        cellStyle1.setLeftBorderColor(HSSFColor.BLACK.index);
						//			        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
						//			        cellStyle1.setRightBorderColor(HSSFColor.BLACK.index);
						//			        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
						//			        cellStyle1.setTopBorderColor(HSSFColor.BLACK.index);
						//			        cellStyle1.setFont(font1);
						short io=90;
						cellStyle1.setRotation(io);
						sheet.addMergedRegion(new CellRangeAddress(2,(short)2,indexColonne,(short)indexColonne));
						HSSFCell cellCompetence = row2.createCell((short)indexColonne);

						cellCompetence.setCellStyle(cellStyle1);
						cellCompetence.setCellValue(libelle_competence);


						indexColonne++;
					}

				}

				//creation des données employe

				//HashMap<String, HashMap<String, HashMap< String, HashMap<String, Double>> >> mapPosteEmployeFamilleCompetence
				HashMap<String, HashMap< String, HashMap<String, Double>> > EmployeFamilleCompetence=mapPosteEmployeFamilleCompetence.get(nomOnglet);

				Set <String >listEmploye =EmployeFamilleCompetence.keySet();
				Iterator<String> iteratorEmploye=listEmploye.iterator();

				int numLigne=3;


				///////prise en cmpte du tri/////////////
				ArrayList<String> listemployeTrie=mapPostEmployeTriIMI.get(nomOnglet);
				Iterator<String> iteratoremployeTrie=listemployeTrie.iterator();
				////////////////////////////////////////////////
				while(/*iteratorEmploye.hasNext()*/iteratoremployeTrie.hasNext())
				{
					String nomEmploye=iteratoremployeTrie.next();

					//String nomEmploye=iteratorEmploye.next();

					//creation d'une ligne employe


					HSSFRow row3 = sheet.createRow(numLigne);


					//cellStyle0.setWrapText(true);
					HSSFCell cell0 = row3.createCell((short)0);
					cell0.setCellValue(nomEmploye);
					cell0.setCellStyle(cellStyle0);


					//remplissage des moyennes par competence
					HashMap< String, HashMap<String, Double>>  mapFamilleCompetence1=EmployeFamilleCompetence.get(nomEmploye);
					setFamilleCompetence=mapFamilleCompetence.keySet();
					iteratorFamilleCompetence=setFamilleCompetence.iterator();

					Double IMI=new Double(0);
					indexColonne=1;
					while(iteratorFamilleCompetence.hasNext())
					{
						String nomFamille=iteratorFamilleCompetence.next();
						//recuperation de la valeur de l'IMI
						String val=mapEmployeFamilleIMI.get(nomEmploye).get(nomFamille);
						String[]v=val.split("#");
						IMI=new Double(v[1]);
						//System.out.println(nomEmploye);
						//System.out.println(nomFamille);

						HashMap<String, Double> mapCompetence=mapFamilleCompetence1.get(nomFamille);
						ArrayList<String> listeCompetence=mapFamilleCompetence.get(nomFamille);
						int nbComp=listeCompetence.size();
						HSSFFont font = workBook.createFont();
						for(int i=0;i<nbComp;i++)
						{
							String competence=listeCompetence.get(i);
							//System.out.println(competence);

							Double valeurStat=mapCompetence.get(competence);

							short couleurCellule=familleColor2.get(nomFamille);


							font.setColor(couleurCellule);
							font.setFontHeightInPoints((short)8);
							font.setFontName("Arial");
							cellStyle10.setFont(font);


							HSSFCell cellCompetence = row3.createCell((short)indexColonne);

							cellCompetence.setCellStyle(cellStyle10);
							//cellCompetence.setCellValue(valeurStat);

							if(valeurStat==null )
								cellCompetence.setCellValue("null");
							else
								if( valeurStat==0 )
									cellCompetence.setCellValue("");
								else
									cellCompetence.setCellValue(valeurStat+"");
							//System.out.println( "valeur stat "+ valeurStat +" indexColonne ="+indexColonne +" competence = "+competence);
							indexColonne++;
						}
					}

					//ajouter la cellule IMI


					HSSFFont font = workBook.createFont();
					font.setColor(HSSFColor.BLACK.index);
					font.setFontHeightInPoints((short)8);
					font.setFontName("Arial");
					cellStyle11.setFont(font);
					//specification des bordures des cellules


					HSSFCell cellCompetence = row3.createCell((short)indexColonne);

					cellCompetence.setCellStyle(cellStyle11);
					if(IMI==0 || IMI==null)
						cellCompetence.setCellValue("");
					else
						cellCompetence.setCellValue(IMI+"");


					numLigne++;




					//////////////////////////////////:::


					HSSFRow row4 = sheet.createRow(numLigne);
					indexColonne=1;
					iteratorFamilleCompetence=setFamilleCompetence.iterator();
					while(iteratorFamilleCompetence.hasNext())
					{
						String nomFamille=iteratorFamilleCompetence.next();


						HSSFCellStyle cellStyle3  =familleColor.get(nomFamille+"1");

						//				    HSSFCellStyle cellStyle3 = null;
						//			 		cellStyle3 = workBook.createCellStyle();
						//
						//			 		cellStyle3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						//				
						//			 		cellStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
						//			 		cellStyle3.setBottomBorderColor(HSSFColor.BLACK.index);
						//			 		cellStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
						//			 		cellStyle3.setLeftBorderColor(HSSFColor.BLACK.index);
						//			 		cellStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);
						//			 		cellStyle3.setRightBorderColor(HSSFColor.BLACK.index);
						//			 		cellStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);
						//			 		cellStyle3.setTopBorderColor(HSSFColor.BLACK.index);
						//			 		cellStyle3.setFont(font1);
						//			 		cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						//			 		cellStyle3.setFillForegroundColor(couleur);


						ArrayList<String> listeCompetence=mapFamilleCompetence.get(nomFamille);

						int nbcompetence=listeCompetence.size();

						sheet.addMergedRegion(new CellRangeAddress(numLigne,(short)numLigne,indexColonne,(short)indexColonne+nbcompetence-1));
						int ff=indexColonne+nbcompetence-1;
						//System.out.println("CellRangeAddress("+numLigne+",(short)"+numLigne+","+indexColonne+",(short)"+ff+")");
						HSSFCell cell3 = row4.createCell((short)indexColonne);


						String val=mapEmployeFamilleIMI.get(nomEmploye).get(nomFamille);
						String[]v=val.split("#");
						Double moy_Famille=new Double(v[0]);

						if(moy_Famille==0 || moy_Famille==null)
							cell3.setCellValue("");
						else
							cell3.setCellValue(moy_Famille+"");
						cell3.setCellStyle(cellStyle3);	

						indexColonne=indexColonne+nbcompetence;


					}
					///////////////////////////////
					numLigne++;

				}			 	
				//numLigne++;
				//construction des lignes Maitrise moyenne par domaine de competence
				HSSFRow row6 = sheet.createRow(numLigne);

				HSSFCell cell6 = row6.createCell((short)0);


				cell6.setCellValue("Maitrise moyenne par domaine de competence");
				cell6.setCellStyle(cellStyle6);	



				////////////////////////////////////////////////
				indexColonne=1;
				iteratorFamilleCompetence=setFamilleCompetence.iterator();
				HSSFFont font = workBook.createFont();
				while(iteratorFamilleCompetence.hasNext())
				{
					String nomFamille=iteratorFamilleCompetence.next();


					HSSFCellStyle cellStyle7=familleColor.get(nomFamille+"1");
					//HSSFFont font = workBook.createFont();
					font.setColor(HSSFColor.BLACK.index);
					font.setFontHeightInPoints((short)8);
					font.setFontName("Arial");
					cellStyle7.setFont(font);
					//cellStyle7.setFillForegroundColor(couleur);


					ArrayList<String> listeCompetence=mapFamilleCompetence.get(nomFamille);

					int nbcompetence=listeCompetence.size();

					sheet.addMergedRegion(new CellRangeAddress(numLigne,(short)numLigne,indexColonne,(short)indexColonne+nbcompetence-1));
					int ff=indexColonne+nbcompetence-1;
					//System.out.println("CellRangeAddress("+numLigne+",(short)"+numLigne+","+indexColonne+",(short)"+ff+")");
					HSSFCell cell7 = row6.createCell((short)indexColonne);


					Double valIMG=mapFamilleIMG.get(nomOnglet).get(nomFamille);
					if(valIMG==0 || valIMG==null)
						cell7.setCellValue("");
					else
						cell7.setCellValue(valIMG+"");

					cell7.setCellStyle(cellStyle7);	

					indexColonne=indexColonne+nbcompetence;


				}
				///////////////////////////////

				numLigne++;
				//construction des lignes IMG
				HSSFRow row8 = sheet.createRow(numLigne);


				HSSFCell cell8 = row8.createCell((short)0);


				cell8.setCellValue("IMG");
				cell8.setCellStyle(cellStyle8);	



				////////////////////////////////////////////////


				//construction des lignes IMG
				//HSSFRow row9 = sheet.createRow(numLigne);


				//calculer le nombre de cellules qui doivent être mergés
				iteratorFamilleCompetence=setFamilleCompetence.iterator();
				int nbcomp=0;
				while(iteratorFamilleCompetence.hasNext())
				{
					String cles=iteratorFamilleCompetence.next();
					ArrayList <String > liste=mapFamilleCompetence.get(cles);
					nbcomp=nbcomp+liste.size();
				}


				sheet.addMergedRegion(new CellRangeAddress(numLigne,(short)numLigne,1,(short)nbcomp));
				HSSFCell cell9 = row8.createCell((short)1);

				Double IMG=mapPosteIMG.get(nomOnglet);
				if(IMG==0 || IMG== null)
					cell9.setCellValue(""); 
				else
					cell9.setCellValue(IMG+"");
				cell9.setCellStyle(cellStyle9);	

				//System.out.println(nbToutesComp);

				for (int i=0;i<nbToutesComp;i++)
				{
					//System.out.println(i);
					sheet.autoSizeColumn(i);

				}	
			}


			FileOutputStream fOut;
			try 
			{
				fOut = new FileOutputStream("ResultatEvaluation.xls");
				workBook.write(fOut);
				fOut.flush();
				fOut.close();

				File file = new File("ResultatEvaluation.xls");
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
	}

	public int getNbCompetenceAllFamille(HashMap<String, ArrayList<String>>  mapFamilleCompetence)
	{

		int nbCompetence=0;

		Set <String>setFamille=mapFamilleCompetence.keySet();
		Iterator <String > iteratorFamille=setFamille.iterator();

		while (iteratorFamille.hasNext())
		{	
			String famille=iteratorFamille.next();
			ArrayList<String> listCompetence=mapFamilleCompetence.get(famille);
			nbCompetence=nbCompetence+listCompetence.size();
		}
		return nbCompetence;

	}

	public void onSelect$nomCompagne() throws SQLException, ParseException
	{




		if(nomCompagne.getItemCount()!=0)
		{
			String selectedNomCompagne=nomCompagne.getSelectedItem().getLabel();

			//System.out.println("compagne selectionné " +selectedNomCompagne);
			selected_id_compagne=map_compagne_idCompagne.get(selectedNomCompagne);

			//System.out.println("selected_id_compagne selectionné " +selected_id_compagne);

			ResultatEvaluationModel resultatEvaluationModel=new ResultatEvaluationModel();

			mapPosteFamilleCompetence=resultatEvaluationModel.getInfosFamillesCompetenceRattrapage(selected_id_compagne);

			mapPosteEmployeFamilleCompetence=resultatEvaluationModel.getAllIMICompetence(selected_id_compagne);

			mapEmployeFamilleIMI=resultatEvaluationModel.getInfosIMIStat(selected_id_compagne);
			mapFamilleIMG=resultatEvaluationModel.getIMGFamille(selected_id_compagne);
			mapPosteIMG=resultatEvaluationModel.getIMGparPoste(selected_id_compagne);

			mapPostFamilleCompetenceStats=resultatEvaluationModel.getmoyPosteCompetenceStatsRattrapage(selected_id_compagne);

			mapPostEmployeTriIMI =resultatEvaluationModel.getEmployeTriIMI(selected_id_compagne);
			//supprimer l'affichage precedant s'il existe
			List<Tab> listOnglet=tbtabs.getChildren();
			List<Tabpanel> listPanel=tbpanels.getChildren();
			if (listOnglet.size()!=0)
			{
				Iterator<Tab> listeIterator=listOnglet.iterator();
				// int i=0;
				int nbOnglets=listOnglet.size();
				nbOnglets=nbOnglets-1;
				for(int i=nbOnglets;i>=0;i--)
				{
					Tab tab=listOnglet.get(i);
					Tabpanel panel=listPanel.get(i);
					//System.out.println("detachement tab  " + tab.getLabel());
					panel.detach();
					tab.detach();;
				}
			}

			AfficherInfosCompagne();

		}
	}



	public void onSelectTab(ForwardEvent event) throws SQLException
	{
		if (event.getTarget() instanceof Tab) {
			Tab t = (Tab) event.getTarget();
			Tabbox tb = (Tabbox) t.getParent().getParent();
			System.out.println(tb.getSelectedPanel());
			System.out.println(t.getLabel());

			Tabpanel newPanel = new Tabpanel();
			newPanel.setStyle("overflow:auto");

			Label newLabel = new Label();
			newLabel.setValue("");

			newPanel.appendChild(newLabel);

			Grid grid;
			try {
				grid = creationtTableau(tb.getSelectedPanel(),t.getLabel());
				//creation du contenu de l'onglet

				newPanel.appendChild(grid);

				tbpanels.appendChild(newPanel);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		



		}




	}

	public void AfficherInfosCompagne()
	{
		Set <String> SetPoste=mapPosteEmployeFamilleCompetence.keySet();
		Iterator<String> iteratorPoste=SetPoste.iterator();

		//creation de tab0 et sa fermeture pour qu'i n'y ait pas de décalage lors de l'ajout des listboxs
		Tab newTab0 = new Tab(); 
		newTab0.isClosable();	
		tbtabs.appendChild(newTab0);			
		newTab0.close();

		String nomOnglet=iteratorPoste.next();
		//creation du tab
		Tab newTab = new Tab();

		newTab.setLabel(nomOnglet);


		Tabpanel newPanel = new Tabpanel();
		newPanel.setStyle("overflow:auto");

		Label newLabel = new Label();
		newLabel.setValue("");

		newPanel.appendChild(newLabel);

		Grid grid;
		try {
			grid = creationtTableau(newPanel,nomOnglet);
			//creation du contenu de l'onglet

			newPanel.appendChild(grid);

			tbpanels.appendChild(newPanel);

			tbtabs.appendChild(newTab);	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		



	}

	public Grid creationtTableau(Tabpanel newPanel,String nomOnglet) throws ParseException
	{
		Grid grid=new Grid();


		//specification des caracteristiques du tableau
		grid.setSpan(true);
		grid.setSizedByContent(true);
		grid.setWidth("100%");
		grid.setHeight("500px");


		Columns colonneTitre=new Columns();

		grid.appendChild(colonneTitre);
		colonneTitre.setSizable(true);

		//creation de la colonne nomEmploye
		Column colonneNomEmploye=new Column();
		colonneTitre.appendChild(colonneNomEmploye);
		//<column label="Code structure" align="center" width="100px" />
		colonneNomEmploye.setLabel("Nom et Prénom de l'évalué");
		colonneNomEmploye.setAlign("center");
		colonneNomEmploye.setWidth("100px");
		
		if (mapPosteFamilleCompetence==null){
			init();
		}

		HashMap<String, ArrayList<String>> mapFamilleCompetence=mapPosteFamilleCompetence.get(nomOnglet);
		HashMap<String, HashMap< String, HashMap<String, Double>> > mapEmployeFamilleCompetence= mapPosteEmployeFamilleCompetence.get(nomOnglet);
		HashMap<String,Double> mapFIMG=mapFamilleIMG.get(nomOnglet);

		if(mapFIMG==null){
			try {
				Messagebox.show("Aucune fiche n'a été validée pour le poste de travail: "+nomOnglet, "Information", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//throw new RuntimeException(e);
				System.out.println(" Aucune fiche n'a été validé pour ce code poste: "+nomOnglet );
			}


		}
		Set <String> setFamilleCompetence=mapFamilleCompetence.keySet();
		Iterator <String>iteratorFamille=setFamilleCompetence.iterator();
		while(iteratorFamille.hasNext())
		{
			//creation des colonnes famille
			String famille=iteratorFamille.next();
			Column colonneFamille=new Column();
			colonneTitre.appendChild(colonneFamille);
			//<column label="Code structure" align="center" width="100px" />
			colonneFamille.setLabel(famille);
			colonneFamille.setAlign("center");
			colonneFamille.setWidth("100px");

		}

		//creation de la colonne IMI
		Column colonneIMI=new Column();	
		colonneTitre.appendChild(colonneIMI);
		colonneIMI.setLabel("IMI");
		colonneIMI.setAlign("center");
		colonneIMI.setWidth("100px");


		//creation des lignes pour chaque employe
		Rows rows=new Rows();
		grid.appendChild(rows);
		Set <String>setEmploye =mapEmployeFamilleCompetence.keySet();
		Iterator <String> iteratorEmploye=setEmploye.iterator();

		///////prise en cmpte du tri/////////////
		ArrayList<String> listemployeTrie=mapPostEmployeTriIMI.get(nomOnglet);
		Iterator<String> iteratoremployeTrie=listemployeTrie.iterator();
		///////////////////
		while(/*iteratorEmploye.hasNext()*/iteratoremployeTrie.hasNext())
		{
			//String nomEmploye=iteratorEmploye.next();
			String nomEmploye=iteratoremployeTrie.next();
			HashMap<String, String> mapFamilleIMI=mapEmployeFamilleIMI.get(nomEmploye);
			//creation de la ligne employe
			Row ligne=new Row();
			rows.appendChild(ligne);
			//remplissage des colonnes familles et IMI de la ligne employe
			Label nom=new Label();
			nom.setValue(nomEmploye);
			//System.out.println("employe= "+ nomEmploye);

			ligne.appendChild(nom);
			iteratorFamille=setFamilleCompetence.iterator();
			String IMI="";
			while(iteratorFamille.hasNext())
			{

				String nomFamille=iteratorFamille.next();
				//System.out.println("nomFamille=" +nomFamille);
				String valeurIMI=mapFamilleIMI.get(nomFamille);

				String[] valeur=valeurIMI.split("#");
				String val=valeur[0];
				IMI=valeur[1];
				Label infosFamille=new Label();
				infosFamille.setValue(val);

				ligne.appendChild(infosFamille);
				//System.out.println("valeur="+ val);
			}
			//affichage de la valeur IMI dans le tableau
			Label infosIMI=new Label();
			infosIMI.setValue(IMI);

			ligne.appendChild(infosIMI);
		}

		//creation de la ligne IMG par famille


		Row ligne=new Row();
		rows.appendChild(ligne);
		//remplissage des colonnes familles et IMI de la ligne employe
		Label nom=new Label();
		nom.setValue("Maitrise moyenne par domaine de competence");


		ligne.appendChild(nom);
		iteratorFamille=setFamilleCompetence.iterator();
		int nbfamille=0;
		while(iteratorFamille.hasNext())
		{

			String nomFamille=iteratorFamille.next();

			Double valeur=mapFIMG.get(nomFamille);


			String val=valeur.toString();

			Label infosFamilleIMG=new Label();
			infosFamilleIMG.setValue(val);

			ligne.appendChild(infosFamilleIMG);
			nbfamille++;
		}
		//creation de la dernière ligne IMG (footer)


		//creation de la ligne IMG par famille


		//		Row ligneIMG=new Row();
		//		rows.appendChild(ligneIMG);
		//		
		//		Label nomIMG=new Label();
		//		nomIMG.setValue("IMG");		
		//		ligneIMG.appendChild(nomIMG);
		//		
		Double img=mapPosteIMG.get(nomOnglet);

		nbfamille++;
		Row ligneIMGVal=new Row();
		ligneIMGVal.setSpans(nbfamille+"");
		rows.appendChild(ligneIMGVal);
		//remplissage des colonnes familles et IMI de la ligne employe
		Label valIMG=new Label();

		valIMG.setValue("IMG = "+img.toString());		
		ligneIMGVal.appendChild(valIMG);


		return grid;
	}




	public void onClick$fermer(){
		pp_export_xls.close();
	}


	public void onCreation(ForwardEvent event){
		Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();


	}

	public void onModifyCheckedBox(ForwardEvent event){
		Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();		

		if (checkbox.isChecked())
		{
			//verifier si ça n'a pas encore été unchecked
			if(unselectedCheckBox.containsValue(checkbox))
			{
				unselectedCheckBox.remove(checkbox);

			}
			selectedCheckBox.put(checkbox.getValue(), checkbox);
		}
		else
		{
			//verifier si ça n'a pas encore été checked
			if(selectedCheckBox.containsValue(checkbox))
			{
				selectedCheckBox.remove(checkbox);

			}
			unselectedCheckBox.put(checkbox.getValue(), checkbox);
		}
		//selectedCheckBox
	}
	public void onClick$telecharger() throws SQLException, InterruptedException{




		if (Messagebox.show("Voulez vous Télécharger le fichier Excel ?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			//init.sendPlanningToEvaluateur(init.getPlanningEvaluateur(list_evaluateur, id_compagne),listselected);
			pp_export_xls.close();
			return;
		}

		else{
			return;
		}




		//System.out.println(list_evaluateur);
	}

	public void onModifyChbox_all(ForwardEvent event) throws InterruptedException, SQLException{
		Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();
		chbox_mat.setChecked(false);
		chbox_all.setChecked(true);


		if (checkbox.isChecked())
		{
			//PlanningCompagneModel init= new PlanningCompagneModel();


			if (Messagebox.show("Voulez vous exporter les résultats détaillés ?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				exportResultatGlobalExlFile();
				pp_export_xls.open(20,30);

				return;
			}

			else{
				return;
			}
		}

		//selectedCheckBox
	}

	public void onModifyChbox_mat(ForwardEvent event) throws InterruptedException, SQLException{
		Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();
		chbox_all.setChecked(false);
		chbox_mat.setChecked(true);



		if (checkbox.isChecked())
		{
			PlanningCompagneModel init= new PlanningCompagneModel();

			if (Messagebox.show("Voulez vous exporter la matrice de cotation?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				exportMatriceCotationExlFileV2();
				pp_export_xls.open(20,30);
				return;
			}

			else{
				return;
			}
		}

		//selectedCheckBox
	}


	/*
	 * Methode d'export de la matrice de cotation sous format Excel
	 * 
	 */


	@SuppressWarnings("serial")
	private void exportMatriceCotationExlFileV2() {

		ResultatEvaluationModel resultatEvaluationModel=new ResultatEvaluationModel();
		ArrayList <String > listCles=new ArrayList<String>();
		HashMap<String, HashMap<String,String>> mapEmployeFamille =resultatEvaluationModel.getMatriceCotationExlFile(selected_id_compagne,listCles);
		ArrayList<String> listFamille=new ArrayList<String>(){{ add("PERSONNELLES"); add("OPERATIONNELLES");add("AFFAIRES") ;add("RELATIONNELLES");}};
		if(nomCompagne.getItemCount()!=0)
		{

			//creation du fichier xls

			//creation d'un document excel 
			HSSFWorkbook workBook = new HSSFWorkbook();

			//creation du style de texte

			HSSFFont font1 = workBook.createFont();
			font1.setFontHeightInPoints((short)8);
			font1.setFontName("Arial");


			//creation d'une feuille excel associé à un poste de travail

			//créatoin du squelette du fichier 
			Set <String>setPosteTravail=mapPosteFamilleCompetence.keySet();
			Iterator <String > iteratorPosteTravail=setPosteTravail.iterator();


			// construction de la feuille ligne par ligne





			//creation de l'onglet
			//System.out.println("nomOnglet="+nomOnglet);
			HSSFSheet sheet = workBook.createSheet("Mat Cotation par structure-IMI");



			int nbToutesComp=4;//getNbCompetenceAllFamille(mapFamilleCompetence);
			//creation de l'entête du document excel

			//cellule ordre
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell = row.createCell((short)0);



			sheet.addMergedRegion(new CellRangeAddress(0,(short)1,0,0));





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


			cell.setCellValue("Ordre");
			cell.setCellStyle(cellStyle);

			HSSFCell cellmatricule = row.createCell((short)1);
			cellmatricule.setCellValue("Matricule");
			cellmatricule.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(0,(short)1,1,1));

			HSSFCell cellPrenomEvalue = row.createCell((short)2);
			cellPrenomEvalue.setCellValue("Nom et Prénom de l'évalué");
			cellPrenomEvalue.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(0,(short)1,2,2));

			HSSFCell celldatenaissance = row.createCell((short)3);
			celldatenaissance.setCellValue("Date de naissance");
			celldatenaissance.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(0,(short)1,3,3));

			HSSFCell celldaterecrutement = row.createCell((short)4);
			celldaterecrutement.setCellValue("Date de recrutement");
			celldaterecrutement.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(0,(short)1,4,4));

			HSSFCell cellformation = row.createCell((short)5);
			cellformation.setCellValue("Formation");
			cellformation.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(0,(short)1,5,5));


			HSSFCell cell_str = row.createCell((short)6);
			cell_str.setCellValue("Structure");
			cell_str.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(0,(short)1,6,6));

			HSSFCell cell_PosteTravail = row.createCell((short)7);
			cell_PosteTravail.setCellValue("Poste de travail");
			cell_PosteTravail.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(0,(short)1,7,7));

			//cellule critère d'evaluation qui doit regrouper toutes les familles

			sheet.addMergedRegion(new CellRangeAddress(0,(short)0,8,(short)nbToutesComp+7));
			HSSFCell cell1 = row.createCell((short)8);
			cell1.setCellValue("Cotation des compétences");
			cell1.setCellStyle(cellStyle);


			//cellule IMI 
			sheet.addMergedRegion(new CellRangeAddress(0,(short)1,nbToutesComp+8,(short)nbToutesComp+8));
			HSSFCell cell2 = row.createCell((short)nbToutesComp+8);
			cell2.setCellValue("IMI");
			cell2.setCellStyle(cellStyle);



			int indexColonne=8;
			HashMap<String, Short> familleColor=new HashMap<String,Short>();
			HashMap<String, HSSFCellStyle> familleColor2=new HashMap<String,HSSFCellStyle>();
			HSSFRow row1 = sheet.createRow(1);

			Object list[]=setPosteTravail.toArray();

			String poste=(String)list[0];

			HashMap<String, ArrayList<String>> mapFamilleCompetence2=mapPosteFamilleCompetence.get(poste);
			Set<String> setFamilleCompetence1=mapFamilleCompetence2.keySet();
			Iterator <String> iteratorFamilleCompetence=listFamille.iterator();//setFamilleCompetence1.iterator();

			/**************************/
			HSSFCellStyle cellStyleaa = null;
			cellStyleaa = workBook.createCellStyle();

			cellStyleaa.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStyleaa.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyleaa.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleaa.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleaa.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleaa.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleaa.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleaa.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleaa.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleaa.setFont(font1);
			cellStyleaa.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleaa.setFillForegroundColor(HSSFColor.RED.index);

			HSSFCellStyle cellStylebb = null;
			cellStylebb = workBook.createCellStyle();

			cellStylebb.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStylebb.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStylebb.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylebb.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylebb.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylebb.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylebb.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylebb.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylebb.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylebb.setFont(font1);
			cellStylebb.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylebb.setFillForegroundColor(HSSFColor.GREEN.index);


			HSSFCellStyle cellStylecc = null;
			cellStylecc = workBook.createCellStyle();

			cellStylecc.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStylecc.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStylecc.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylecc.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylecc.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylecc.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylecc.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylecc.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylecc.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylecc.setFont(font1);
			cellStylecc.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylecc.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);

			HSSFCellStyle cellStyledd = null;
			cellStyledd = workBook.createCellStyle();

			cellStyledd.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStyledd.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyledd.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyledd.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyledd.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyledd.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyledd.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyledd.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyledd.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyledd.setFont(font1);
			cellStyledd.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyledd.setFillForegroundColor(HSSFColor.ORANGE.index);
			/******************************/
			while(iteratorFamilleCompetence.hasNext())
			{
				String clesFamille=iteratorFamilleCompetence.next();


				int nbcompetence=1;//mapFamilleCompetence.get(clesFamille).size();

				//creation des secondes cellules associées aux familles

				cellStyle = workBook.createCellStyle();
				short couleur=HSSFColor.RED.index;
				if(clesFamille.equalsIgnoreCase("Affaires")){
					couleur=HSSFColor.RED.index;
					familleColor2.put(clesFamille, cellStyleaa);
				}
				else{
					if(clesFamille.equalsIgnoreCase("RELATIONNELLES")){
						couleur=HSSFColor.GREEN.index;
						familleColor2.put(clesFamille, cellStylebb);
					}
					else{
						if(clesFamille.equalsIgnoreCase("PERSONNELLES")){
							couleur=HSSFColor.LIGHT_BLUE.index;
							familleColor2.put(clesFamille, cellStylecc);
						}
						else{
							if(clesFamille.equalsIgnoreCase("OPERATIONNELLES")){
								couleur=HSSFColor.ORANGE.index;
								familleColor2.put(clesFamille, cellStyledd);
							}
							else{//couleur orange par défaut
								couleur=HSSFColor.ORANGE.index;
								familleColor2.put(clesFamille, cellStyledd);
							}
						}
					}
				}
				familleColor.put(clesFamille, new Short(couleur));
				cellStyle.setFillForegroundColor(couleur);

				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

				cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);

				HSSFCell cellFamillle = row1.createCell((short)indexColonne);

				cellFamillle.setCellValue(clesFamille);
				cellFamillle.setCellStyle(cellStyle);				 	
				indexColonne=indexColonne+nbcompetence;

			}

			Iterator <String> clesIterator=listCles.iterator();
			int numLigne=2;
			HSSFCellStyle cellStyle0 = null;
			cellStyle0 = workBook.createCellStyle();
			cellStyle0.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyle0.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyle0.setFont(font1);


			cellStyle0.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle0.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle0.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle0.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle0.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle0.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle0.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle0.setTopBorderColor(HSSFColor.BLACK.index);

			cellStyle0.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			HSSFCellStyle cellStyle1 = null;
			cellStyle1 = workBook.createCellStyle();
			cellStyle1.setFillForegroundColor(HSSFColor.WHITE.index);

			cellStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			HSSFFont font = workBook.createFont();
			font.setColor(HSSFColor.BLACK.index);
			font.setFontHeightInPoints((short)8);
			font.setFontName("Arial");
			cellStyle1.setFont(font);
			//specification des bordures des cellules
			cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle1.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle1.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle1.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle1.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);

			HSSFCellStyle cellStyle3 = null;
			cellStyle3 = workBook.createCellStyle();
			cellStyle3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			cellStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle3.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle3.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle3.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle3.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyle3.setFont(font1);
			cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			String structure_bckp="";
			int ordre_bkp=1;
			int ordre=1;
			while(clesIterator.hasNext())
			{

				String cles=clesIterator.next();
				HashMap <String,String> mapFamille=mapEmployeFamille.get(cles);
				String[] listDonnee=cles.split("#");





				//creation d'une ligne employe


				HSSFRow row3 = sheet.createRow(numLigne);


				//cellStyle0.setWrapText(true);
				//cellule ordre


				HSSFCell cellmat = row3.createCell((short)1);
				cellmat.setCellValue(listDonnee[0]);
				cellmat.setCellStyle(cellStyle0);

				HSSFCell cellnom = row3.createCell((short)2);
				cellnom.setCellValue(listDonnee[1]);
				cellnom.setCellStyle(cellStyle0);

				HSSFCell celldatN = row3.createCell((short)3);
				celldatN.setCellValue(listDonnee[2]);
				celldatN.setCellStyle(cellStyle0);

				HSSFCell celldatR = row3.createCell((short)4);
				celldatR.setCellValue(listDonnee[3]);
				celldatR.setCellStyle(cellStyle0);

				HSSFCell cellformation1 = row3.createCell((short)5);
				cellformation1.setCellValue(listDonnee[4]);
				cellformation1.setCellStyle(cellStyle0);



				HSSFCell cellStructure = row3.createCell((short)6);
				cellStructure.setCellValue(listDonnee[5]);
				cellStructure.setCellStyle(cellStyle0);

				if (!listDonnee[5].equalsIgnoreCase(structure_bckp)){
					ordre=ordre_bkp;
					structure_bckp=listDonnee[5];
				}else{
					ordre++;
				}

				HSSFCell cellOrdre = row3.createCell((short)0);
				cellOrdre.setCellValue(new Integer(ordre).toString());
				cellOrdre.setCellStyle(cellStyle0);

				HSSFCell cellPosteTravail = row3.createCell((short)7);
				cellPosteTravail.setCellValue(listDonnee[6]);
				cellPosteTravail.setCellStyle(cellStyle0);


				//remplissage des moyennes par competence
				//				 		HashMap< String, HashMap<String, Double>>  mapFamilleCompetence1=EmployeFamilleCompetence.get(nomEmploye);
				//				 		setFamilleCompetence=mapFamilleCompetence.keySet();
				//					 	iteratorFamilleCompetence=setFamilleCompetence.iterator();
				String val=getDeuxChiffres(listDonnee[7]);
				Double IMI=new Double(val);
				indexColonne=8;
				iteratorFamilleCompetence=listFamille.iterator(); 



				while(iteratorFamilleCompetence.hasNext())
				{
					String nomFamille=iteratorFamilleCompetence.next();

					int nbComp=1;//listeCompetence.size();




					HSSFCellStyle cellStyleFamille=familleColor2.get(nomFamille);

					//la famille en question  ne fait pas partie des familles  coté pour l'évalué
					//cas d'un évalué qui a une famille coté sur 4 par exemple
					if (familleColor.get(nomFamille)==null){

					}else{
						short couleur=familleColor.get(nomFamille);



						//cellStyle3.setFillForegroundColor(couleur);


						int nbcompetence=1;//listeCompetence.size();

						sheet.addMergedRegion(new CellRangeAddress(numLigne,(short)numLigne,indexColonne,(short)indexColonne+nbcompetence-1));
						int ff=indexColonne+nbcompetence-1;
						//System.out.println("CellRangeAddress("+numLigne+",(short)"+numLigne+","+indexColonne+",(short)"+ff+")");
						HSSFCell cell3 = row3.createCell((short)indexColonne);

						String moy_Famille=mapFamille.get(nomFamille);

						if("".equals(moy_Famille) || "0".equals(moy_Famille))
							cell3.setCellValue("");
						else
						{
							val=getDeuxChiffres(moy_Famille);
							cell3.setCellValue(val);
						}
						cell3.setCellStyle(cellStyleFamille);	

						indexColonne=indexColonne+nbcompetence;

					}



				}

				//ajouter la cellule IMI

				int indexColonne_imi=12;


				HSSFCell cellCompetence = row3.createCell((short)indexColonne_imi);

				cellCompetence.setCellStyle(cellStyle1);
				if(IMI==0 || IMI==null)
					cellCompetence.setCellValue("");
				else
					cellCompetence.setCellValue(IMI+"");


				numLigne++;


				//}


				for (int i=0;i<nbToutesComp+4;i++)
				{
					sheet.autoSizeColumn(i);

				}	
			}


			FileOutputStream fOut;
			try 
			{
				fOut = new FileOutputStream("MatriceCotation.xls");
				workBook.write(fOut);
				fOut.flush();
				fOut.close();

				File file = new File("MatriceCotation.xls");
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
	}

	private String getDeuxChiffres(String valeur){
		if(valeur!=null){
			Double moy_competence=new Double(valeur);
			DecimalFormat df = new DecimalFormat("########.00"); 
			df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
			df.setMinimumFractionDigits ( 2 ) ;
			df.setDecimalSeparatorAlwaysShown ( true ) ;
			String f=df.format(moy_competence);
			moy_competence= Double.parseDouble(f.replace(',', '.'));
			return moy_competence.toString();
		}
		else return "";
	}



	public void onSelect$comp_struct_ent_list() throws SQLException {

		//message.setValue("");
		//mychart.setVisible(false);

		comp_poste_list.getItems().clear();
		comp_poste_list.appendItem("Tous Postes Travail","-1");

		EmployeModel init=new EmployeModel();
		Map map = new HashMap();
		Map map_structure = new HashMap();
		//map_structure=init.getStructEntList();
		//EmployeCadreBean cpb;
		String code_structure=(String) comp_struct_ent_list.getSelectedItem().getValue();



		if ((code_structure.equalsIgnoreCase("-1"))){
			comp_poste_list.getItems().clear();
			comp_poste_list.appendItem("Tous Postes Travail","-1");
			//generateChart( "-1", "-1");

		}
		else {
			Set set_ent = (init.getListPosteStructure(code_structure)).entrySet(); 
			Iterator itr_ = set_ent.iterator();
			//si la structure ne cotient aucun poste de travail. on cache la liste box poste 
			if (set_ent.size()==0){
				comp_poste_list.setVisible(false);
				label_poste.setVisible(false);
			}else{
				comp_poste_list.setVisible(true);
				label_poste.setVisible(true);
			}

			while(itr_.hasNext()) {
				Map.Entry me = (Map.Entry)itr_.next();
				comp_poste_list.appendItem((String) me.getKey(),(String) me.getValue());

				//profilemodel.add((String) me.getKey());
			}
		}


	}

	public void onSelect$comp_poste_list() throws SQLException {

		EmployeModel init=new EmployeModel();
		String code_structure=(String) comp_struct_ent_list.getSelectedItem().getValue();
		String code_poste=(String) comp_poste_list.getSelectedItem().getValue();
		try {
			afficherResultatParPoste(code_poste);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void afficherResultatParPoste(String code_poste) throws ParseException{

		exporter.setVisible(true);
		exporterMat.setVisible(true);
		String label_poste=comp_poste_list.getSelectedItem().getLabel();

		Tab newTab = new Tab();
		newTab.setClosable(true);

		newTab.setLabel(label_poste);


		Tabpanel newPanel = new Tabpanel();
		newPanel.setStyle("overflow:auto");

		Label newLabel = new Label();
		newLabel.setValue("");

		newPanel.appendChild(newLabel);

		//Grid grid=creationtTableau(newPanel,nomOnglet);

		//creation du contenu de l'onglet
		//Grid grid=new Grid();

		Grid grid=creationtTableau(newPanel,label_poste);

		newPanel.appendChild(grid);

		tbpanels.appendChild(newPanel);

		tbtabs.appendChild(newTab);	

		/*selected_id_compagne=((String) nomCompagne.getSelectedItem().getValue());

		ResultatEvaluationModel resultatEvaluationModel=new ResultatEvaluationModel();

		mapPosteFamilleCompetence=resultatEvaluationModel.getInfosFamillesCompetenceRattrapage(selected_id_compagne);

		mapPosteEmployeFamilleCompetence=resultatEvaluationModel.getAllIMICompetence(selected_id_compagne);

		mapEmployeFamilleIMI=resultatEvaluationModel.getInfosIMIStat(selected_id_compagne);
		mapFamilleIMG=resultatEvaluationModel.getIMGFamille(selected_id_compagne);
		mapPosteIMG=resultatEvaluationModel.getIMGparPoste(selected_id_compagne);

		mapPostFamilleCompetenceStats=resultatEvaluationModel.getmoyPosteCompetenceStatsRattrapage(selected_id_compagne);

		mapPostEmployeTriIMI =resultatEvaluationModel.getEmployeTriIMI(selected_id_compagne);

		AfficherInfosCompagne();*/


	}

	private void exportMatriceCotationExlFileV3() throws ParsePropertyException, InvalidFormatException, IOException{

		// System.out.println("\nApp Deployed Directory path: " + Sessions.getCurrent().getWebApp().getRealPath("WEB-INF"));

		FileOutputStream fOut= new FileOutputStream("MatriceCotation.xls");

		ResultatEvaluationModel resultatEvaluationModel=new ResultatEvaluationModel();
		List<MatriceCotationBean>employees=resultatEvaluationModel.getMatriceCotationExlFileNew(selected_id_compagne);
		Map beans = new HashMap();
		beans.put("employee", employees);
		XLSTransformer transformer = new XLSTransformer(); 
		//transformer.groupCollection("department.staff"); 
		String reportLocation = Sessions.getCurrent().getWebApp().getRealPath("WEB-INF");
		String reportLocation1 = Sessions.getCurrent().getWebApp().getRealPath("WebContent");
		//System.out.println(reportLocation+ " "+reportLocation1);


		Workbook workbook = transformer.transformXLS(new FileInputStream(reportLocation+ "/MatriceCotation_templatev22.xls"), beans);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();

		File file = new File("MatriceCotation.xls");
		Filedownload.save(file, "XLS");
		//transformer.transformXLS(templateFileName, beans, destFileName);
		//date = new Date();
		//System.out.println(" date fin" +date.toString());



	}

}
