package kpi.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kpi.bean.ProgresActionDevBean;
import kpi.bean.SuiviActionDevBean;
import kpi.model.FicheIndividuelleModel;

import kpi.model.SuviActionDEVModel;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Progressmeter;
import compagne.model.SuiviCompagneModel;
import Statistique.model.EmployeModel;


public class SuiviActionDEVAction extends GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Listbox comp_list;
	Listbox  comp_struct_ent_list;
	Listbox  evaluelb;
	Listcell listcheckbox;
	AnnotateDataBinder binder;
	Progressmeter progressbar;
	//List<AdministrationLoginBean> model = new ArrayList<AdministrationLoginBean>();
	//SuiviCompagneBean selected;
	Button search;
	Button valider;
	Button sendmail;
	Label msg;
	Label employee_name;
	List<String> profilemodel=new ArrayList<String>();
	List<String> basedonneemodel=new ArrayList<String>();
	List<SuiviActionDevBean> model = new ArrayList<SuiviActionDevBean>();
	HashMap <String, Checkbox> selectedCheckBox;
	HashMap <String, Checkbox> unselectedCheckBox;
	boolean compage_termine=true;
	int profileid;
	Popup pp_list_evalue;

	AnnotateDataBinder binder1;
	Component comp1;
	Button execut_prog;
	Button execute_suivi;

	Map map = new HashMap();
	Listbox poste_travail;
	Map map_poste=null;
	private Listbox direction;
	Map map_direction=null;





	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		selectedCheckBox=new HashMap <String, Checkbox>();
		unselectedCheckBox=new HashMap <String, Checkbox>();
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		SuiviCompagneModel init= new SuiviCompagneModel();

		Integer compagne_id=0;


		map=init.getCompagneList();

		Set set = (init.getCompagneList()).entrySet(); 
		Iterator i = set.iterator();

		// Affichage de la liste des compagnes
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();

			comp_list.appendItem((String) me.getKey(),String.valueOf(me.getValue()));

			//profilemodel.add((String) me.getKey());
		}
		if(comp_list.getItemCount()>0){
			comp_list.setSelectedIndex(0);
			compagne_id=Integer.parseInt((String) comp_list.getSelectedItem().getValue());
		}


		if (direction.getSelectedItems().size()>0) 
			direction.getItems().clear();

		SuviActionDEVModel kpi=new SuviActionDEVModel();
		map_direction=kpi.getListDirection();
		Set set1 = (map_direction).entrySet(); 
		Iterator i1 = set1.iterator();
		while(i1.hasNext()) {
			Map.Entry me = (Map.Entry)i1.next();
			direction.appendItem((String) me.getKey(),(String) me.getKey());
		}

		direction.setSelectedIndex(0);






		progressbar.setStyle("background:#FF0000;");
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

		comp1=comp;

	}



	/**
	 * @param profileid
	 */
	public void checkPercentageProfile(int profileid) {
		for (int pos=0;pos< this.getModel().size();pos++){

			if (this.getModel().get(pos).getPourcentage()<100 && profileid !=1){
				valider.setDisabled(true);
				compage_termine=false;
				sendmail.setDisabled(false);
				return;
			}

		}
	}
	public List<SuiviActionDevBean> getModel() {
		return model;
	}

	public void onSelect$poste_travail() throws SQLException {

		Integer compagne=(Integer) map.get(comp_list.getSelectedItem().getLabel());
		String code_poste=(String) poste_travail.getSelectedItem().getValue();

		FicheIndividuelleModel init_m=new FicheIndividuelleModel();
		model=init_m.getActionDevProgress(code_poste, compagne);

		if (model.size()==0){

			try {
				Messagebox.show("Aucune action de développement definie pour ce poste  pour cette campagne !", "Information", Messagebox.OK, Messagebox.INFORMATION);
				return;


			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		binder = new AnnotateDataBinder(comp1);
		binder.loadAll();

	}

	public void onSelect$comp_struct_ent_list() throws SQLException {
		//vider la structure la listbox
		poste_travail.getItems().clear();
		SuiviCompagneModel init= new SuiviCompagneModel(); 

		//map_struct=init.getStructEntrepriseList();
		Integer compagne=(Integer) map.get(comp_list.getSelectedItem().getLabel());
		String code_structure=(String) comp_struct_ent_list.getSelectedItem().getValue();
		//FicheIndividuelleModel init_m=new FicheIndividuelleModel();
		//model=init_m.getActionDevProgress(code_structure, compagne);
		poste_travail.appendItem("Tous Postes Travail","-1");
		EmployeModel init_p=new EmployeModel();
		Set set_ent = (init_p.getListPosteStructure(code_structure)).entrySet(); 
		Iterator itr_ = set_ent.iterator();
		//si la structure ne cotient aucun poste de travail. on cache la liste box poste 
		if (set_ent.size()==0){
			//comp_poste_list.setVisible(false);
			poste_travail.setVisible(false);
		}else{

			poste_travail.setVisible(true);
		}

		while(itr_.hasNext()) {
			Map.Entry me = (Map.Entry)itr_.next();
			poste_travail.appendItem((String) me.getKey(),(String) me.getValue());


			//profilemodel.add((String) me.getKey());
		}
		
	

		if (poste_travail.getItemCount()>0)
			poste_travail.setSelectedIndex(0);



	}
	public void onCreation(ForwardEvent event){
		if(event.getOrigin()!=null)
		{
			Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();

			if(checkbox!=null)
				if(checkbox.getValue()!=null)
					if (Integer.parseInt(checkbox.getValue())==100){
						checkbox.setVisible(false);
					}


		}

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

	public void onClick$sendmail() throws SQLException, InterruptedException {

		FicheIndividuelleModel init= new FicheIndividuelleModel();
		List lisAdmin=init.getListAdmin();

		if (lisAdmin.size()!=0){

			if (Messagebox.show("Voulez vous envoyer l'alerte email aux adminitrateurs RH?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				init.sendAlertEvaluateur(model,lisAdmin);

				return;
			}

			else{
				return;
			}

		}else{
			Messagebox.show("La liste des employés ne comporte aucun administrateur RH !", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);

			return;
		}






	}



	public void onSelect$comp_list() throws SQLException, InterruptedException {

		//int idcomp=Integer.valueOf((String)comp_list.getSelectedItem().getValue());
		Integer idcomp=(Integer) map.get(comp_list.getSelectedItem().getLabel());

	}	

	private void genExportExcel() throws SQLException, ParsePropertyException, InvalidFormatException, IOException {

		// System.out.println("\nApp Deployed Directory path: " + Sessions.getCurrent().getWebApp().getRealPath("WEB-INF"));

		FileOutputStream fOut= new FileOutputStream("SuiviActionDeveloppement.xls");
		int idCompagne=Integer.valueOf((String)comp_list.getSelectedItem().getValue());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();


		FicheIndividuelleModel init_m=new FicheIndividuelleModel();
		ArrayList<SuiviActionDevBean> list=init_m.exportRapport( idCompagne);
		List maps = new ArrayList();
		List sheetNames = new ArrayList();
		 
		Map beans = new HashMap();
		beans.put("employee", list);
		
		



		XLSTransformer transformer = new XLSTransformer(); 
		String reportLocation = Sessions.getCurrent().getWebApp().getRealPath("WEB-INF");
		String reportLocation1 = Sessions.getCurrent().getWebApp().getRealPath("WebContent");

		Workbook workbook = transformer.transformXLS(new FileInputStream(reportLocation+ "/template_suivi_action_dev.xls"), beans);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();

		File file = new File("SuiviActionDeveloppement.xls");
		Filedownload.save(file, "XLS");




	}
	
	
	private void genExportProgressActionExcel() throws SQLException, ParsePropertyException, InvalidFormatException, IOException {

		// System.out.println("\nApp Deployed Directory path: " + Sessions.getCurrent().getWebApp().getRealPath("WEB-INF"));

		FileOutputStream fOut= new FileOutputStream("SuiviProgresActionDeveloppement.xls");
		int idCompagne=Integer.valueOf((String)comp_list.getSelectedItem().getValue());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();


		SuviActionDEVModel init_m=new SuviActionDEVModel();
		ArrayList<ProgresActionDevBean> list=init_m.exportRapport(idCompagne);

		Map beans = new HashMap();
		beans.put("action_dev", list);



		XLSTransformer transformer = new XLSTransformer(); 
		String reportLocation = Sessions.getCurrent().getWebApp().getRealPath("WEB-INF");
		String reportLocation1 = Sessions.getCurrent().getWebApp().getRealPath("WebContent");

		Workbook workbook = transformer.transformXLS(new FileInputStream(reportLocation+ "/template_suivi_action_dev1.xls"), beans);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();

		File file = new File("SuiviProgresActionDeveloppement.xls");
		Filedownload.save(file, "XLS");




	}

	
	
	public void onClick$execut_prog() throws Exception {

		if (Messagebox.show("Voulez vous exporter le rapport de progression des actions de développement ?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			//exportMatriceCotationExlFileV2();
			genExportProgressActionExcel();
			

			return;
		}

		else{
			return;
		}


	}
	
	public void onClick$execute_suivi() throws Exception {

		if (Messagebox.show("Voulez vous exporter le rapport de suivi des actions de développement ?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			
			
			genExportExcel();

			return;
		}

		else{
			return;
		}


	}

	public void onSelect$direction() throws SQLException	{

		comp_struct_ent_list.getItems().clear();
		String code_structure_str="";
		
		String libelle_direction=(String)direction.getSelectedItem().getValue();
		List <String> list_code_dir=(List)map_direction.get(libelle_direction);
		
		SuviActionDEVModel init= new SuviActionDEVModel();
		//recuperer la liste des structures
		Set set_ent = (init.getStructEntList(list_code_dir)).entrySet(); 
		Iterator itr = set_ent.iterator();

		// Affichage de la liste des strcutures entreprise
		comp_struct_ent_list.appendItem("Toutes Structures","-1");
		while(itr.hasNext()) {
			
			Map.Entry me = (Map.Entry)itr.next();
			comp_struct_ent_list.appendItem((String) me.getKey(),(String) me.getValue());

			//profilemodel.add((String) me.getKey());
		}
		if(comp_struct_ent_list.getItemCount()>0){
			comp_struct_ent_list.setSelectedIndex(0);
			code_structure_str=(String) comp_struct_ent_list.getSelectedItem().getValue();
		}

	}


}
