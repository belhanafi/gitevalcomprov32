package kpi.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kpi.bean.SuiviActionDevBean;
import kpi.model.FicheIndividuelleModel;
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

import compagne.bean.SuiviCompagneBean;
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
	Button executeexp;
	Map map = new HashMap();
	Listbox poste_travail;
	Map map_poste=null;




	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		selectedCheckBox=new HashMap <String, Checkbox>();
		unselectedCheckBox=new HashMap <String, Checkbox>();
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		SuiviCompagneModel init= new SuiviCompagneModel();

		Integer compagne_id=0;
		String code_structure_str="";

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

		Set set_ent = (init.getStructEntList()).entrySet(); 
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
				Messagebox.show("Aucune action de développement definie pour ce poste  pour cette compagne !", "Information", Messagebox.OK, Messagebox.INFORMATION);
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
	public void onClick$valider() throws SQLException, InterruptedException, ParseException {
		SuiviCompagneModel init= new SuiviCompagneModel();
		Map map = new HashMap();
		//Map map_struct = new HashMap();
		map=init.getCompagneList();
		//map_struct=init.getStructEntrepriseList();
		Integer compagne=(Integer) map.get(comp_list.getSelectedItem().getLabel());

		if (compagne!=-1){


			if (Messagebox.show("Voulez vous valider la compagne  "+comp_list.getSelectedItem().getLabel(), "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				//System.out.println("pressyes");
				boolean retour=init.validerCompagne(compagne);
				boolean retour2=init.calculerIMG(compagne);



				if (retour && retour2  ){

					Messagebox.show("Compagne validée avec succès", "Information",Messagebox.OK, Messagebox.INFORMATION); 
				}

				return;
			}

			else{
				return;
			}
		}

		else{
			Messagebox.show("Merci de valider une  vague  à la fois", "Information",Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
	}





	public void onSelect$comp_list() throws SQLException, InterruptedException {

		//int idcomp=Integer.valueOf((String)comp_list.getSelectedItem().getValue());
		Integer idcomp=(Integer) map.get(comp_list.getSelectedItem().getLabel());

	}	

	/*private void genExportExcel() throws SQLException, ParsePropertyException, InvalidFormatException, IOException {

		// System.out.println("\nApp Deployed Directory path: " + Sessions.getCurrent().getWebApp().getRealPath("WEB-INF"));

		FileOutputStream fOut= new FileOutputStream("RapportProgressCotation.xls");
		int idCompagne=Integer.valueOf((String)comp_list.getSelectedItem().getValue());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String dateExtract=dateFormat.format(date);

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String heureExtract=sdf.format(cal.getTime());

		List info =new ArrayList<String>();
		info.add(dateExtract);
		info.add(heureExtract);



		SuiviCompagneModel init= new SuiviCompagneModel();
		ArrayList<SuiviCompagneBean> list=init.exportRapport(idCompagne);
		Map beans = new HashMap();
		beans.put("employee", list);
		beans.put("infoDate", info);


		XLSTransformer transformer = new XLSTransformer(); 
		//transformer.groupCollection("department.staff"); 
		String reportLocation = Sessions.getCurrent().getWebApp().getRealPath("WEB-INF");
		String reportLocation1 = Sessions.getCurrent().getWebApp().getRealPath("WebContent");
		//System.out.println(reportLocation+ " "+reportLocation1);


		Workbook workbook = transformer.transformXLS(new FileInputStream(reportLocation+ "/template_rapportProgressCotation.xls"), beans);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();

		File file = new File("RapportProgressCotation.xls");
		Filedownload.save(file, "XLS");
		//transformer.transformXLS(templateFileName, beans, destFileName);
		//date = new Date();
		//System.out.println(" date fin" +date.toString());



	}
	public void onClick$executeexp() throws Exception {

		if (Messagebox.show("Voulez vous exporter le rapport de suivi compagne ?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			//exportMatriceCotationExlFileV2();
			genExportExcel();

			return;
		}

		else{
			return;
		}


	}*/



}
