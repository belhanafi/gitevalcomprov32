package compagne.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import kpi.bean.ListDureeCotatLowBean;
import kpi.model.KpiSyntheseModel;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Textbox;

import common.ApplicationFacade;
import common.ApplicationSession;
import compagne.bean.EmailEvaluateurBean;
import compagne.bean.PlanningListEvaluateurBean;
import compagne.bean.SuiviCompStatutFicheBean;
import compagne.bean.SuiviCompagneBean;
import compagne.model.PlanningCompagneModel;
import compagne.model.SuiviCompagneModel;
import administration.bean.AdministrationLoginBean;
import administration.model.AdministrationLoginModel;

public class SuiviCompagneAction extends GenericForwardComposer{

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
	List<SuiviCompagneBean> model = new ArrayList<SuiviCompagneBean>();
	HashMap <String, Checkbox> selectedCheckBox;
	HashMap <String, Checkbox> unselectedCheckBox;
	boolean compage_termine=true;
	int profileid;
	Popup pp_list_evalue;
	List<SuiviCompStatutFicheBean> model1 = new ArrayList<SuiviCompStatutFicheBean>();
	AnnotateDataBinder binder1;
	SuiviCompStatutFicheBean fichebean=new SuiviCompStatutFicheBean();
	Component comp1;
	Button executeexp;
	Map map = new HashMap();

	
	
	
	public List<SuiviCompStatutFicheBean> getModel1() {
		return model1;
	}


	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		selectedCheckBox=new HashMap <String, Checkbox>();
		unselectedCheckBox=new HashMap <String, Checkbox>();
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		SuiviCompagneModel init= new SuiviCompagneModel();
		msg.setVisible(false);
		msg.setValue("");
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
		// création de la structure de l'entreprise bean
		//AdministrationLoginModel admin_compte =new AdministrationLoginModel();
		//model=init.uploadListEvaluateur();

		model=init.filtrerListEvaluateur(compagne_id,code_structure_str);

		binder = new AnnotateDataBinder(comp);
		binder.loadAll();
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		//TODO supprimer cette ligne Applicationfacade
		//profileid=ApplicationFacade.getInstance().getCompteUtilisateur().getId_profile();
		profileid=applicationSession.getCompteUtilisateur().getId_profile();
		checkPercentageProfile(profileid);

		/*if(comp_list.getItemCount()==1)
			valider.setDisabled(true);*/


		if (init.isCompagneValidated(compagne_id)){
			msg.setValue(comp_list.getSelectedItem().getLabel()+ "  a été validée" ); 
			msg.setVisible(true);
			valider.setDisabled(true);
			//sendmail.visible(true);
		}else{
			valider.setDisabled(false);
		}
		
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
	public List<SuiviCompagneBean> getModel() {
		return model;
	}
	public void onSelect$comp_struct_ent_list() throws SQLException {
		SuiviCompagneModel init= new SuiviCompagneModel(); 
		msg.setValue("");

		
		Map map_structure = new HashMap();
		
		map_structure=init.getStructEntList();
		//map_struct=init.getStructEntrepriseList();
		Integer compagne=(Integer) map.get(comp_list.getSelectedItem().getLabel());
		String code_structure=(String) comp_struct_ent_list.getSelectedItem().getValue();
		//String structure=struct_list.getSelectedItem().getLabel();
		model=init.filtrerListEvaluateur(compagne,code_structure);
		
		
		if (compagne>1) {
			model=init.filtrerListEvaluateur(compagne,code_structure);
			valider.setDisabled(false);
			checkPercentageProfile(profileid);

			if (init.isCompagneValidated(compagne)){
				msg.setValue(comp_list.getSelectedItem().getLabel()+ "  a été validée" ); 
				msg.setVisible(true);
				valider.setDisabled(true);
				//sendmail.setDisabled(true);
			}else{
				valider.setDisabled(false);
			}
		}

		//binder = new AnnotateDataBinder(comp);
		binder.loadAll();
	


	}
	public void onCreation(ForwardEvent event){
		if(event.getOrigin()!=null)
		{
			Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();

			if(checkbox!=null)
				if(checkbox.getName()!=null)
					if (Integer.parseInt(checkbox.getName())==100){
						checkbox.setVisible(false);
					}
			
			
		}

	}
	public void onCreationButton(ForwardEvent event){
		if(event.getOrigin()!=null)
		{
			Button button = (Button) event.getOrigin().getTarget();
			
			if(button!=null)
				if(button.getLabel()!=null)
					if (Integer.parseInt(button.getLabel())==100){
						button.setVisible(false);
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
		List employe = new ArrayList<Integer>();
		List listevalbean=new ArrayList<EmailEvaluateurBean>();
		//EmailEvaluateurBean evalbean=new EmailEvaluateurBean();
		SuiviCompagneModel init= new SuiviCompagneModel();
		Set<String> setselected = selectedCheckBox.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		iterator=listselected.iterator();
		String idCompagne=(String)comp_list.getSelectedItem().getValue();
		while (iterator.hasNext())
		{
			String cles=(String)iterator.next();
			Checkbox checkBox=selectedCheckBox.get(cles);
			if (selectedCheckBox.get(cles).isChecked()){
				//System.out.println("value>>"+ selectedCheckBox.get(cles).getValue()+"contexted>>"+ selectedCheckBox.get(cles).getContext()+"label>>"+selectedCheckBox.get(cles).getLabel());
				EmailEvaluateurBean evalbean=new EmailEvaluateurBean();
				evalbean.setId_employe(Integer.parseInt(selectedCheckBox.get(cles).getValue()));
				evalbean.setPourcentage(Integer.parseInt(selectedCheckBox.get(cles).getName()));
				evalbean.setEmail(selectedCheckBox.get(cles).getContext());
				evalbean.setNomevaluateur(selectedCheckBox.get(cles).getId());

				listevalbean.add(evalbean);
			}
		}

		if (Messagebox.show("Voulez vous envoyer l'alerte email aux évaluateurs sélectionnés?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			init.sendAlertEvaluateur(listevalbean,idCompagne);
			
			return;
		}

		else{
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

	
	
	public void onClickButton(Event event){
		//this listens to onClick events in the listbox
		//but prompts a message only if it gets a forward
		if (event instanceof org.zkoss.zk.ui.event.ForwardEvent) {
			try {
				ForwardEvent ev = (ForwardEvent) event;	
				Button button = (Button) ev.getOrigin().getTarget();
				
				afficherListEvalue(button.getId());
				//Messagebox.show(ev.getOrigin().getTarget().getId());			
			} catch(Exception e) {

			}
		} else
			return;
	}
	
	public void afficherListEvalue(String id_evaluateur) throws SQLException{
		
		SuiviCompagneModel init= new SuiviCompagneModel();
		String idCompagne=(String)comp_list.getSelectedItem().getValue();
		model1=init.getListFicheStatut(Integer.parseInt(id_evaluateur), Integer.parseInt(idCompagne));
		
		binder1 = new AnnotateDataBinder(self);
		evaluelb.setItemRenderer(new ListitemRenderer() {

			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				SuiviCompStatutFicheBean bo = (SuiviCompStatutFicheBean) arg1;
				Listcell evaluecell=new Listcell();
				arg0.appendChild(evaluecell);
				evaluecell.setLabel(bo.getEvalue());
				
				Listcell statutcell=new Listcell();
				arg0.appendChild(statutcell);
				String statut=bo.getFiche_valide();
				statutcell.setLabel(bo.getStatut());
				if (statut.equalsIgnoreCase("1")){
					statutcell.setStyle("font-family: Verdana, Verdana, Arial,Helvetica,sans-serif; font-size: 10px;font-weight: ;background-color: green;font-weight:bold;color:black;");

				}else if (statut.equalsIgnoreCase("-1")){
					statutcell.setStyle("font-family: Verdana, Verdana, Arial,Helvetica,sans-serif; font-size: 10px;font-weight: ;background-color: orange;font-weight:bold;color:black;");

				}else{
					statutcell.setStyle("font-family: Verdana, Verdana, Arial,Helvetica,sans-serif; font-size: 10px;font-weight: ;background-color: red;font-weight:bold;color:black;");

				}
				
				
				
			}
			
		});
		
		pp_list_evalue.open(200, 200);
		
		binder1.loadAll();

	}
	
	public void onSelect$comp_list() throws SQLException, InterruptedException {
		
		//int idcomp=Integer.valueOf((String)comp_list.getSelectedItem().getValue());
		Integer idcomp=(Integer) map.get(comp_list.getSelectedItem().getLabel());
		//int idcomp=Integer.valueOf((String)filtre_compagne.getSelectedItem().getLabel());
		SuiviCompagneModel init= new SuiviCompagneModel();
		model=init.filtrerListEvaluateur(idcomp,"-1");
		binder = new AnnotateDataBinder(comp1);
		
		if (init.isCompagneValidated(idcomp)){
			msg.setValue(comp_list.getSelectedItem().getLabel()+ "  a été validée" ); 
			msg.setVisible(true);
			valider.setDisabled(true);
			//sendmail.setDisabled(true);
		}else{
			valider.setDisabled(false);
			
		}
		
		binder.loadAll();
	}	
	
	private void genExportExcel() throws SQLException, ParsePropertyException, InvalidFormatException, IOException {

		// System.out.println("\nApp Deployed Directory path: " + Sessions.getCurrent().getWebApp().getRealPath("WEB-INF"));

		FileOutputStream fOut= new FileOutputStream("RapportProgressCotation.xls");
		int idCompagne=Integer.valueOf((String)comp_list.getSelectedItem().getValue());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		/*String dateExtract=dateFormat.format(date);
			
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String heureExtract=sdf.format(cal.getTime());
        
        List info =new ArrayList<String>();
        info.add(dateExtract);
        info.add(heureExtract);*/

       
		
		SuiviCompagneModel init= new SuiviCompagneModel();
		ArrayList<SuiviCompagneBean> list=init.exportRapport(idCompagne);
		Map beans = new HashMap();
		beans.put("employee", list);
		//beans.put("infoDate", info);
		
		
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
		
		
	}
	
	

}
