package compagne.action;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.codehaus.groovy.tools.shell.commands.SetCommand;
import org.zkoss.lang.Strings;
import org.zkoss.zk.au.out.AuClearWrongValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;

import compagne.bean.EmailEvaluateurBean;
import compagne.bean.PlanningCompagneListBean;
import compagne.bean.PlanningListEvaluateurBean;
import compagne.bean.PlanningListEvalueBean;
import compagne.bean.SuiviCompagneBean;
import compagne.model.CompagneCreationModel;
import compagne.model.GestionEmployesModel;
import compagne.model.PlanningCompagneModel;
import compagne.model.SuiviCompagneModel;
import administration.bean.AdministrationLoginBean;
import administration.bean.CompagneCreationBean;
import administration.bean.StructureEntrepriseBean;
import administration.model.AdministrationLoginModel;
import administration.model.StructureEntrepriseModel;

public class PlanningCompagneAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Listbox admincomptelb;
	Listbox evaluateurlb;
	Combobox comp_list;
	Combobox chbox_drh;
	Textbox id_planning;
	Listbox nom_compagne;
	Listbox Evaluateur;
	Listbox Evalue;
	Listbox code_poste;
	Listbox  structure;
	Listbox filtre_compagne;
	Datebox date_evaluation;
	Listbox heure_debut_evaluation;
	Listbox heure_fin_evaluation;
	Textbox lieu;
	Textbox  personne_ressources;
	Textbox nomEvaluateurFiltre;
	Textbox nomEvaleFiltre;
	Label comp_list_label;
	Datebox date_fin_evaluation;


	private String lbl_compagne;
	private String lbl_evaluateur;
	private String lbl_evalue;
	private String lbl_poste;
	private Integer id_compagne=0;
	private String lbl_structure;



	AnnotateDataBinder binder;
	AnnotateDataBinder binder1;
	AnnotateDataBinder binder2;
	List<PlanningCompagneListBean> model = new ArrayList<PlanningCompagneListBean>();
	List<PlanningListEvaluateurBean> model1 = new ArrayList<PlanningListEvaluateurBean>();
	List<PlanningListEvalueBean> model2 = new ArrayList<PlanningListEvalueBean>();



	PlanningCompagneListBean selected;
	SuiviCompagneBean selected1;
	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button upload;
	Button download;
	Button effacer;
	Map map_nomcomp=null;
	Map map_evaluateur=null;
	Map map_evalue=null;
	Map map_evalue_sorted=null;
	
	Map map_filtrecompagne=null;


	Map map_structure_sorted=null;
	Map map_heuredebut=null;
	Map map_heurefin=null;
	Map map_poste= new HashMap();
	Map map_structure=null;
	Button sendmail;
	Popup pp_sel_evaluateur;
	Button fermer;

	HashMap <String, Checkbox> selectedCheckBox;
	HashMap <String, Checkbox> unselectedCheckBox;
	ListModelList listModel;
	ListModelList listModel1;
	Component comp1;






	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		okAdd.setVisible(false);
		effacer.setVisible(false);
		PlanningCompagneModel init= new PlanningCompagneModel();
		map_nomcomp = new HashMap();
		map_nomcomp=init.getCompagneValid();
		Set set = (map_nomcomp).entrySet(); 
		Iterator i = set.iterator();
		int id_compagne_filtre=0;

		nom_compagne.appendItem("Toutes campagnes ","Toutes campagnes ");
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			nom_compagne.appendItem((String) me.getKey(),(String) me.getKey());
		}

        //remplissage du filtre liste compagne
		map_filtrecompagne= new HashMap();
		map_filtrecompagne=init.getCompagneFiltreValid();
		Set set1 = (map_filtrecompagne).entrySet(); 
		Iterator i1 = set1.iterator();

		//filtre_compagne.appendItem("Toutes compagnes ","-1");
		
		// Display elements
		while(i1.hasNext()) {
			Map.Entry me = (Map.Entry)i1.next();
			filtre_compagne.appendItem((String) me.getKey(),(String) me.getKey());
		}
		
		if(filtre_compagne.getItemCount()>0){
			filtre_compagne.setSelectedIndex(0);
			id_compagne_filtre=(Integer) map_filtrecompagne.get((String)filtre_compagne.getSelectedItem().getLabel());
			//id_compagne_filtre=Integer.valueOf((String)filtre_compagne.getSelectedItem().getValue());
		}

		//remplissage de la liste box evaluateur
		
		map_evaluateur=init.getListEvaluteur();
		Map<String, String> treeMap = new TreeMap<String, String>(map_evaluateur);
		Set set_itr = (treeMap).entrySet(); 
		Iterator itr = set_itr.iterator();
		// Display elements
		Evaluateur.appendItem("Selectionner evaluateur ","-1");
		while(itr.hasNext()) {
			Map.Entry me = (Map.Entry)itr.next();
			Evaluateur.appendItem((String) me.getKey(),(String) me.getKey());
		}
		
		 
			


		//refresh liste evalués
		//model2=init.getListEvalue();
		//listModel = new ListModelList(model2);
		//evalue.setModel(listModel);
		//evalue.appendItem("Selectionné evalué", "Selectionné evalué");
		Evalue.setDisabled(true);



		map_heuredebut=init.getHeureDebut();  
		set = (map_heuredebut).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			heure_debut_evaluation.appendItem((String) me.getKey(),(String) me.getKey());
		}

		map_heurefin=init.getHeureFin();
		set = (map_heurefin).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			heure_fin_evaluation.appendItem((String) me.getKey(),(String) me.getKey());
		}


		map_poste=init.getListPoste();
		set = (map_poste).entrySet();
		code_poste.appendItem("Selectionner le poste de travail ","Selectionner le poste de travail");
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			code_poste.appendItem((String) me.getKey(),(String) me.getKey());
		}

		/*map_structure=init.getListStructure();
		set = (map_structure).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			structure.appendItem((String) me.getKey(),(String) me.getKey());
		}*/
		
		
       String code_structure_str="";
		
		map_structure=init.getStructEntList();
		Set set_ent = (map_structure).entrySet(); 
		Iterator itr_ = set_ent.iterator();
		// Affichage de la liste des strcutures entreprise
		structure.appendItem("Selectionner Structure","-1");
		while(itr_.hasNext()) {
			Map.Entry me = (Map.Entry)itr_.next();

			structure.appendItem((String) me.getKey(),(String) me.getKey());

			//profilemodel.add((String) me.getKey());
		}
		if(structure.getItemCount()>0){
			structure.setSelectedIndex(0);
			code_structure_str=(String) structure.getSelectedItem().getValue();
		}


		model=init.loadPlanningCompagnelist(id_compagne_filtre);
		binder = new AnnotateDataBinder(comp);
		if(model.size()!=0)
			selected=model.get(0);

		if(admincomptelb.getItemCount()!=0)
			admincomptelb.setSelectedIndex(0);
		binder.loadAll();
		nom_compagne.setSelectedIndex(0);
		//Evaluateur.setSelectedIndex(0);
		code_poste.setSelectedIndex(0);
		
		comp1=comp;

	}

	public List<PlanningCompagneListBean> getModel() {
		return model;
	}

	public List<PlanningListEvaluateurBean> getModel1() {
		return model1;
	}


	public PlanningCompagneListBean getSelected() {
		return selected;
	}

	public void setSelected(PlanningCompagneListBean selected) {
		this.selected = selected;
	}

	public void onClick$add() throws WrongValueException, ParseException {

		clearFields();

		nom_compagne.setSelectedIndex(0);
		Evaluateur.setSelectedIndex(0);
		//evalue.setSelectedIndex(0);
		Evalue.setDisabled(false);

		heure_debut_evaluation.setSelectedIndex(0);
		heure_fin_evaluation.setSelectedIndex(1);
		code_poste.setSelectedIndex(0);
		//code_structure.setSelectedIndex(0);

		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		//update.setVisible(false);
		delete.setVisible(false);

	}

	public void onClick$okAdd()throws WrongValueException, ParseException, InterruptedException {

		PlanningCompagneListBean addedData = new PlanningCompagneListBean();
		addedData.setId_compagne(getSelectedIdCompagne());
		addedData.setId_evalue(getSelectedEvalue());
		addedData.setDate_evaluation(getSelectDateEvaluation());
		addedData.setId_evaluateur(getSelectedEvaluateur());
		addedData.setHeure_debut_evaluation(getSelectedHeureDeb());
		addedData.setDate_fin_evaluation(getSelectDateFinEvaluation());
		addedData.setHeure_fin_evaluation(getSelectedHeureFin());
		addedData.setLieu(getSelectedLieu());
		addedData.setCode_poste(getSelectPosteTravail());
		addedData.setPersonne_ressources(getSelectedPersonneRessources());
		addedData.setCode_structure(getSelectStructure());
		addedData.setLibelle_compagne(getLbl_compagne());
		addedData.setEvaluateur(getLbl_evaluateur());
		addedData.setEvalue(getLbl_evalue());
		addedData.setIntitule_poste(getLbl_poste());
		addedData.setLibelle_structure(getLbl_structure());



		//controle d'intégrité 
		PlanningCompagneModel compagne_model =new PlanningCompagneModel();
		//compagne_model.addPlanningCompagne(addedData);
		Boolean donneeValide=compagne_model.controleIntegrite(addedData,map_heuredebut,map_heurefin);
		//Boolean donneeValide=true;

		if (donneeValide)
		{
			//insertion de la donnée ajoutée dans la base de donnée
			boolean donneeAjoute=compagne_model.addPlanningCompagne(addedData);
			// raffrechissemet de l'affichage
			if (donneeAjoute )
			{
				model.add(addedData);
				selected = addedData;
				binder.loadAll();

			}
		}

		/*try {
			fillEvalueListBox();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		/*Evalue.getItems().clear();
		Evalue.setDisabled(true);

		code_structure.getItems().clear();
		code_structure.setDisabled(true);*/

		okAdd.setVisible(false);
		effacer.setVisible(false);
		add.setVisible(true);
		//update.setVisible(true);
		delete.setVisible(true);


	}

	/**
	 * @throws SQLException 
	 * 
	 */
	public void fillEvalueListBox() throws SQLException {
		Evalue.getItems().clear();
		PlanningCompagneModel init= new PlanningCompagneModel();
		model2=init.getListEvalue();
		listModel = new ListModelList(model2);
		Evalue.setModel(listModel);
	}

	public void onClick$update() throws WrongValueException, ParseException, InterruptedException, SQLException {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		PlanningCompagneListBean addedData = new PlanningCompagneListBean();
		selected.setId_compagne(getSelectedIdCompagne());
		selected.setId_evalue(getSelectedEvalue());
		selected.setDate_evaluation(getSelectDateEvaluation());
		selected.setId_evaluateur(getSelectedEvaluateur());
		selected.setHeure_debut_evaluation(getSelectedHeureDeb());
		selected.setDate_fin_evaluation(getSelectDateFinEvaluation());
		selected.setHeure_fin_evaluation(getSelectedHeureFin());
		selected.setLieu(getSelectedLieu());
		selected.setCode_poste(getSelectPosteTravail());
		selected.setPersonne_ressources(getSelectedPersonneRessources());
		selected.setCode_structure(getSelectStructure());
		selected.setLibelle_compagne(getLbl_compagne());
		selected.setEvaluateur(getLbl_evaluateur());
		selected.setEvalue(getLbl_evalue());
		selected.setIntitule_poste(getLbl_poste());
		selected.setId_planning_evaluation(getSelectedIdplanning());
		selected.setLibelle_structure(getLbl_structure());


		//controle d'intégrité 
		PlanningCompagneModel compagne_model =new PlanningCompagneModel();
		//controle d'intégrité 
		Boolean donneeValide=compagne_model.controleIntegrite(selected,map_heuredebut,map_heurefin);
		if (donneeValide)
		{
			//insertion de la donnée ajoutée dans la base de donnée

			if (Messagebox.show("Voulez vous appliquer les modifications?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				//System.out.println("pressyes");

				if (compagne_model.verifEmployeFicheValid(selected)){

					Messagebox.show("Modification non permise.La fiche de l'évalué selectionné a été validée ou en cours de validation !", "Information",Messagebox.OK, Messagebox.INFORMATION); 

					return;
				}
				compagne_model.updateCompagne(selected);
				binder.loadAll();
				return;
			}

			else{
				return;
			}
		}	



	}

	public void onClick$delete() throws InterruptedException, SQLException {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		PlanningCompagneModel compagne_model =new PlanningCompagneModel();
		//suppression de la donnée supprimée de la base de donnée
		selected.setId_planning_evaluation(getSelectedIdplanning()); 

		if (Messagebox.show("Voulez vous supprimer cet enregistrement?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			//System.out.println("pressyes");
			
			if (compagne_model.verifEmployeFicheValid(selected)){

				Messagebox.show("Suppression non permise.La fiche de l'évalué selectionné a été validée ou en cours de validation !", "Information",Messagebox.OK, Messagebox.INFORMATION); 

				return;
			}
			compagne_model.deleteCompagne(selected);
			model.remove(selected);
			binder.loadAll();
			//selected=model.get(model.size());
			return;
		}

		else{
			return;
		}





	}

	public void onClick$effacer()  {


		clearFields();
		okAdd.setVisible(false);
		add.setVisible(true);
		//update.setVisible(true);
		delete.setVisible(true);
		admincomptelb.setSelectedIndex(0);
		binder.loadAll();


	}


	public void onSelect$admincomptelb() {

		//mise à jour de la liste d'evaluateur
		int index=admincomptelb.getSelectedIndex();


		PlanningCompagneListBean planing=model.get(index); 

		PlanningListEvalueBean planningEvalue=new PlanningListEvalueBean();
		planningEvalue.setNom_employe(planing.getEvalue());
		planningEvalue.setId_employe(planing.getId_evalue());

	


		model2=new ArrayList<PlanningListEvalueBean>();
		model2.add(planningEvalue);
		Evalue.getItems().clear();
/*
		model1=new ArrayList<PlanningListEvaluateurBean>();
		model1.add(planningEvaluateur);
		//Evaluateur.getItems().clear();
*/
		listModel = new ListModelList(model2);
		Evalue.setModel(listModel);
		Evalue.disableClientUpdate(false);


	/*	listModel1 = new ListModelList(model1);
		Evaluateur.setModel(listModel1);
		Evaluateur.disableClientUpdate(false);*/

		closeErrorBox(new Component[] { nom_compagne, Evaluateur, code_poste,Evalue,structure,date_evaluation,
				heure_debut_evaluation,date_fin_evaluation,heure_fin_evaluation,lieu,personne_ressources});
	}


	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}


	private int getSelectedIdCompagne() throws WrongValueException {

		Integer name=(Integer) map_nomcomp.get((String)nom_compagne.getSelectedItem().getLabel());
		setLbl_compagne((String)nom_compagne.getSelectedItem().getLabel());

		if (name== null) {
			throw new WrongValueException(nom_compagne, "Merci de saisir un nom de compagne!");
		}
		return name;
	}
	
	




	private int getSelectedEvaluateur() throws WrongValueException {

		Integer name=0;
		try{
			//name=(Integer) Evaluateur.getSelectedItem().getValue();
			
			 name=(Integer) map_evaluateur.get(Evaluateur.getSelectedItem().getLabel());

			setLbl_evaluateur((String)Evaluateur.getSelectedItem().getLabel());
			//System.out.println("id employe>>"+name);

			if (name== null) {
				throw new WrongValueException(Evaluateur, "Merci de saisir un Evaluateur!");
			}
		}catch (Exception e){
			try
			{
				PlanningListEvaluateurBean p=model1.get(0);
				name=p.getId_evaluateur();
				setLbl_evaluateur(p.getEvaluateur());


			}
			catch(Exception e2)
			{
				throw new WrongValueException(Evaluateur, "Merci de saisir un Evaluateur!");
			}
		}
		return name;
	}


	private Integer getSelectedIdplanning() throws WrongValueException {

		Integer name= Integer.parseInt(id_planning.getValue());

		if (name== null) {
			throw new WrongValueException(Evaluateur, "Merci de saisir un évaluateur!");
		}
		return name;
	}

	private int getSelectedEvalue() throws WrongValueException {

		Integer name=0;
		try{
			name=(Integer) Evalue.getSelectedItem().getValue();
			setLbl_evalue((String)Evalue.getSelectedItem().getLabel());
			//System.out.println("id employe>>"+name);

			if (name== null) {
				throw new WrongValueException(Evalue, "Merci de saisir un évalue!");
			}
		}catch (Exception e){
			try
			{

				PlanningListEvalueBean p=model2.get(0);
				name=p.getId_employe();
				setLbl_evalue(p.getNom_employe());

			}
			catch(Exception e2)
			{
				throw new WrongValueException(Evalue, "Merci de saisir un évalue!");
			}
		}
		return name;
	}

	private String getSelectPosteTravail() throws WrongValueException {
		String name = (String) map_poste.get((String)code_poste.getSelectedItem().getLabel());
		setLbl_poste((String)code_poste.getSelectedItem().getLabel());
		if (Strings.isBlank(name)) {
			throw new WrongValueException(code_poste, "Merci de saisir un poste de travail!");
		}
		return name;
	}

	/*private String getSelectStructure() throws WrongValueException {
		String name = structure.getSelectedItem().getLabel();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(structure, "Merci de saisir une structure!");
		}
		return name;
	}*/
	
	private String getSelectStructure() throws WrongValueException {
		String name =  (String) map_structure.get((String)structure.getSelectedItem().getLabel());
		//String name =  (String)structure.getSelectedItem().getValue();
		setLbl_structure((String)structure.getSelectedItem().getLabel());

		if (Strings.isBlank(name)) {
			throw new WrongValueException(structure, "Merci de saisir une structure!");
		}
		return name;
	}

	private Date getSelectDateEvaluation() throws WrongValueException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String name = date_evaluation.getText();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_evaluation, "Merci de saisir une date d'évaluation!");
		}
		Date datedeb = df.parse(name);
		return datedeb;
	}

	private Date getSelectDateFinEvaluation() throws WrongValueException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String name = date_fin_evaluation.getText();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_fin_evaluation, "Merci de saisir une date  de fin d'évaluation!");
		}
		Date datefin = df.parse(name);
		return datefin;
	}

	private String getSelectedHeureDeb() throws WrongValueException {
		String name=heure_debut_evaluation.getSelectedItem().getLabel();
		if (name==null) {
			throw new WrongValueException(heure_debut_evaluation, "Merci de choisir  une heure de début d'évaluation!");
		}
		return name;
	}

	private String getSelectedHeureFin() throws WrongValueException {
		String name=heure_fin_evaluation.getSelectedItem().getLabel();
		if (name==null) {
			throw new WrongValueException(heure_fin_evaluation, "Merci de choisir  une heure de fin d'évaluation!");
		}
		return name;
	}

	private String getSelectedLieu() throws WrongValueException {
		String name=lieu.getValue();
		if (name==null) {
			throw new WrongValueException(lieu, "Merci de saisir le lieu de l'évaluation!");
		}
		return name;
	}

	private String getSelectedPersonneRessources() throws WrongValueException {
		String name=personne_ressources.getValue();
		if (name==null) {
			throw new WrongValueException(personne_ressources, "Merci de saisir la personne ressources!");
		}
		return name;
	}

	public void clearFields(){

		lieu.setText("");
		personne_ressources.setText("");

	}

	public String getLbl_compagne() {
		return lbl_compagne;
	}

	public void setLbl_compagne(String lbl_compagne) {
		this.lbl_compagne = lbl_compagne;
	}

	public String getLbl_evaluateur() {
		return lbl_evaluateur;
	}

	public void setLbl_evaluateur(String lbl_evaluateur) {
		this.lbl_evaluateur = lbl_evaluateur;
	}

	public String getLbl_evalue() {
		return lbl_evalue;
	}

	public void setLbl_evalue(String lbl_evalue) {
		this.lbl_evalue = lbl_evalue;
	}

	public String getLbl_poste() {
		return lbl_poste;
	}

	public void setLbl_poste(String lbl_poste) {
		this.lbl_poste = lbl_poste;
	}


	public void onSelect$Evalue() throws SQLException, InterruptedException {
		PlanningCompagneModel init =new PlanningCompagneModel();
		structure.getItems().clear();

		Integer  id_employe = getSelectedEvalue();
		map_structure_sorted= init.selectStructure(id_employe);


		Set set = (map_structure_sorted).entrySet(); 
		Iterator i = set.iterator();

		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			structure.appendItem((String) me.getKey(),(String) me.getKey());
		}

		structure.setSelectedIndex(0);
		structure.setDisabled(true);
	}


	public void onSelect$code_poste() throws SQLException, InterruptedException {
		PlanningCompagneModel init =new PlanningCompagneModel();
		ListModelList listModel;
		Evalue.getItems().clear();
		//binder2 = new AnnotateDataBinder(self);
		String poste= (String) map_poste.get(code_poste.getSelectedItem().getLabel());
		//map_evalue_sorted=init.selectEmployes(poste);
		//model2.set(0, new PlanningListEvalueBean(0,"Selectionner evalué") );
		model2=(List<PlanningListEvalueBean>) init.selectEmployes(poste);

		listModel = new ListModelList(model2);

		//		PlanningListEvalueBean selectDefaut=new PlanningListEvalueBean();
		//		selectDefaut.setNom_employe("Selectionner un evalué");
		//		selectDefaut.setId_employe(-1);
		//		listModel.add(selectDefaut);

		Evalue.setModel(listModel);


		Evalue.setDisabled(false);
		Evalue.setSelectedIndex(listModel.size()-1);

	}

	public void onSelect$filtre_compagne() throws SQLException, InterruptedException {
		PlanningCompagneModel init= new PlanningCompagneModel();
		int idcomp=(Integer) map_filtrecompagne.get((String)filtre_compagne.getSelectedItem().getLabel());
		//int idcomp=Integer.valueOf((String)filtre_compagne.getSelectedItem().getLabel());
		model=init.loadPlanningCompagnelist(idcomp);
		binder = new AnnotateDataBinder(comp1);
		if(model.size()!=0)
			selected=model.get(0);

		if(admincomptelb.getItemCount()!=0)
			admincomptelb.setSelectedIndex(0);
		binder.loadAll();
	}	
	
	
	public void onSelect$nom_compagne() throws SQLException, InterruptedException {
		
		
		
		
		code_poste.getItems().clear();
		PlanningCompagneModel init =new PlanningCompagneModel();
		Map map_sorted;
		id_compagne = getSelectedIdCompagne();    
		map_sorted= init.getListPosteCompagne(id_compagne);
		Set set = (map_sorted).entrySet(); 
		Iterator i = set.iterator();
		code_poste.appendItem("Selectionner le poste de travail ","Selectionner le poste de travail");
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();

			code_poste.appendItem((String) me.getKey(),(String) me.getKey());
		}
		code_poste.getItemAtIndex(0);
	}	
	
	

	
	
	public void onSelect$code_structure() throws SQLException, InterruptedException {
		PlanningCompagneModel init =new PlanningCompagneModel();
		Map map_sorted;
		String structure_en =  (String) map_structure.get((String)structure.getSelectedItem().getLabel());
		map_sorted= init.setlectedStructure(structure_en);
		Set set = (map_sorted).entrySet(); 
		Iterator i = set.iterator();

		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			structure.setTooltiptext((String) me.getKey());
		}


	}

	public void onClick$sendmail() throws SQLException{
		comp_list.getItems().clear();
		PlanningCompagneModel init= new PlanningCompagneModel();
		Set set = (init.getCompagneList()).entrySet(); 

		Iterator i = set.iterator();

		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();

			comp_list.appendItem((String) me.getKey());

			//profilemodel.add((String) me.getKey());
		}
		//comp_list.setSelectedIndex (1);

		//model1=init.uploadListEvaluateur();
		pp_sel_evaluateur.open(10, 20);
	}

	public void onSelect$comp_list() throws SQLException {


		PlanningCompagneModel init= new PlanningCompagneModel(); 
		binder1 = new AnnotateDataBinder(self);
		selectedCheckBox=new HashMap <String, Checkbox>();
		unselectedCheckBox=new HashMap <String, Checkbox>();	
		Map map = new HashMap();
		//Map map_struct = new HashMap();
		map=init.getCompagneList();
		//map_struct=init.getStructEntrepriseList();
		id_compagne=(Integer) map.get(comp_list.getSelectedItem().getLabel());

		//String structure=struct_list.getSelectedItem().getLabel();
		model1=init.getListEvaluateur(id_compagne);
		binder1.loadAll();

	}


	public void onClick$fermer(){
		pp_sel_evaluateur.close();
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
	public void onClick$sendemail1() throws SQLException, InterruptedException{


		PlanningCompagneModel init= new PlanningCompagneModel();
		Set<String> setselected = selectedCheckBox.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		iterator=listselected.iterator();
		String list_evaluateur="(";
		while (iterator.hasNext())
		{
			String cles=(String)iterator.next();
			Checkbox checkBox=selectedCheckBox.get(cles);
			if (selectedCheckBox.get(cles).isChecked()){

				list_evaluateur=list_evaluateur+selectedCheckBox.get(cles).getValue()+",";

			}
		}
		list_evaluateur=list_evaluateur+")";
		list_evaluateur = list_evaluateur.replace(",)",")");

		if (!list_evaluateur.equalsIgnoreCase("()")){

			if (Messagebox.show("Voulez vous envoyer le planning par email aux évaluateurs sélectionnés ?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				init.sendPlanningToEvaluateur(init.getPlanningEvaluateur(list_evaluateur, id_compagne),listselected);
				pp_sel_evaluateur.close();
				return;
			}

			else{
				return;
			}




		}

		//System.out.println(list_evaluateur);
	}

	public void onModifyChbox_drh(ForwardEvent event) throws InterruptedException, SQLException{
		Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();		

		if (checkbox.isChecked())
		{
			PlanningCompagneModel init= new PlanningCompagneModel();

			if (Messagebox.show("Voulez vous envoyer le planning complet validé au DRH ?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				init.sendPlanningToDRH(init.getPlanningAllEvaluateur());
				//pp_sel_evaluateur.close();
				return;
			}

			else{
				return;
			}
		}

		//selectedCheckBox
	}

	public void onModifyChbox_val_drh(ForwardEvent event){
		Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();		

		if (checkbox.isChecked())
		{
			evaluateurlb.setVisible(true);
			comp_list.setVisible(true);
			comp_list_label.setVisible(true);
		}
		else
		{
			evaluateurlb.setVisible(false);
			comp_list.setVisible(false);
			comp_list_label.setVisible(false);

		}
		//selectedCheckBox
	}

	public Map getMap_evalue() {
		return map_evalue;
	}

	public void setMap_evalue(Map map_evalue) {
		this.map_evalue = map_evalue;
	}




	public ListModelList getListModel() {
		return listModel;
	}

	public void setListModel(ListModelList listModel) {
		this.listModel = listModel;
	}

	public PlanningCompagneAction() {
	}

	public Map getMap_evalue_sorted() {
		return map_evalue_sorted;
	}

	public void setMap_evalue_sorted(Map map_evalue_sorted) {
		this.map_evalue_sorted = map_evalue_sorted;
	}

	public List<PlanningListEvalueBean> getModel2() {
		return model2;
	}

	public void setModel2(List<PlanningListEvalueBean> model2) {
		this.model2 = model2;
	}

	public void onClick$buttonReinitialiser() throws InterruptedException, SQLException {

		PlanningCompagneModel compagne_model =new PlanningCompagneModel();
		//recherche des elements selectionnées


		nomEvaluateurFiltre.setValue("");
		nomEvaleFiltre.setValue("");

		//System.out.println("pressyes");
		int init_val=-1;
		model=compagne_model.loadPlanningCompagnelist(init_val);
		//model.remove(selected);
		selected = null;
		binder.loadAll();
		Evalue.getItems().clear();


	}

	public void onClick$buttonRechercher() throws InterruptedException, SQLException {

		PlanningCompagneModel compagne_model =new PlanningCompagneModel();
		//recherche des elements selectionnées
		String filtreNomEvaluateur=nomEvaluateurFiltre.getValue();
		String filreNomEvalue=nomEvaleFiltre.getValue();  


		//System.out.println("pressyes");
		model=compagne_model.filtrePlanningCompagne(filtreNomEvaluateur,filreNomEvalue);

		selected = null;

		binder.loadAll();

		Evalue.getItems().clear();

	}

	/*
	 * methode  qui pemet la reouverture d'une fiche d'évaluation en  cas de retour
	 * Evolution introduite dans le V2.1.Point2
	 */
	public void onClick$reinit() throws InterruptedException {
		if (selected == null) {
			alert("Aucune ligne n'a été selectionnée");
			return;
		}
		PlanningCompagneModel compagne_model =new PlanningCompagneModel();
		selected.setId_planning_evaluation(getSelectedIdplanning()); 

		if (Messagebox.show("Voulez vous ouvrir  la fiche d'évaluation de Mme/M " +selected.getEvalue()+"?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			if (compagne_model.reinitFicheEvaluation(selected)){
				compagne_model.reinitCompagneEvaluation(selected);
				Messagebox.show("La fiche et la compagne  d'évaluation ont été ouvertes avec succès!", "Information",Messagebox.OK, Messagebox.INFORMATION); 

			}		

			binder.loadAll();
			//selected=model.get(model.size());
			return;
		}

		else{
			return;
		}





	}
	
	

	public String getLbl_structure() {
		return lbl_structure;
	}

	public void setLbl_structure(String lbl_structure) {
		this.lbl_structure = lbl_structure;
	}



}