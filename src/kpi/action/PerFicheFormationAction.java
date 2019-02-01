package kpi.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kpi.bean.ActionDevelopmentBean;
import kpi.bean.ActionFormationBean;
import kpi.bean.EchelleMaitrise;
import kpi.bean.ListeCompagneVagueBean;
import kpi.model.CorrectionPosteModel;
import kpi.model.KpiSyntheseModel;
import net.sf.jxls.exception.ParsePropertyException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.zkoss.lang.Strings;
import org.zkoss.zk.au.out.AuClearWrongValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import administration.bean.AdministrationLoginBean;
import administration.bean.CompteBean;
import administration.model.AdministrationLoginModel;
import common.ApplicationSession;

public class PerFicheFormationAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	AnnotateDataBinder binder;

	private Listbox poste_travail;
	private Listbox listcompagne;
	Listbox liste_echelle;
	Listbox liste_evalue;
	Listbox liste_action_formation;

	Listbox liste_domaine_formation;
	Listbox theme_domaine_formation;



	Listbox propose;
	Listbox validee;
	Listbox programmee;
	Listbox realisee;
	Component comp1;
	private Include iframe;
	Button exporterWord;
	Button enregistrerAssociation;
	Button new_record;
	Window win;
	Textbox formation_;

	private HashMap <String, Radio> selectedRadio;
	private  Radio selectedRadioEchelle;
	private  Radio selectedRadioEvalue;
	private List<ListeCompagneVagueBean> model = new ArrayList<ListeCompagneVagueBean>();
	private List<EchelleMaitrise> modelEchelle = new ArrayList<EchelleMaitrise>();
	private List<ActionFormationBean> modelEvalue = new ArrayList<ActionFormationBean>();
	private List<ActionFormationBean> modelActionFormation = new ArrayList<ActionFormationBean>();
	




	private List<String> suiviSort=new ArrayList<String>();

	private HashMap<String, HashMap<String, String>> mapFormationExterne;
	private HashMap<String, String> listSuiviSort;



	private EchelleMaitrise selectedEchelle;
	private EchelleMaitrise selectedEvalue;

	private HashMap<String, HashMap<String, Integer>> listDb = null;
	private Map map_poste=null;
	private CorrectionPosteModel correctionPosteMoel=new CorrectionPosteModel();
	private String selected_id_compagne="1";
	private HashMap<String, ArrayList<ActionFormationBean>> mapEvalueAction=null;
	private Integer idcompagne;
	private String selectedPosteTravail=null;
	private Listbox direction1;
	Map map_direction=null;
	private String propose_str;
	ActionFormationBean selected;

	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button effacer;
	Grid gridDetails;
	ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
	CompteBean compteUtilisateur=applicationSession.getCompteUtilisateur();
	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		//System.out.println("doAfterCompose executed");


		super.doAfterCompose(comp);

		comp.setVariable(comp.getId() + "Ctrl", this, true);

		//récupération de la liste des compagnes

		okAdd.setVisible(false);
		
		// Affichage de la liste des compagnes
		KpiSyntheseModel init= new KpiSyntheseModel();
		model=init.getListeValidCampgneBase(); 


		//chargement du contenu de la table action_development

		gridDetails.setVisible(false);

		selectedRadio=new HashMap <String, Radio>();
		

		//cas evaluateur colonnes  grisées (validée, programmée et réalisée)
				if (compteUtilisateur.getId_profile()==3){
					validee.setDisabled(true);
					programmee.setDisabled(true);
					realisee.setDisabled(true);
					

				}

				// cas Service RH de la direction de rattachement,colonnes  grisées (proposée et réalisée)
				if(compteUtilisateur.getId_profile()==5){
					realisee.setDisabled(true);
					propose.setDisabled(true);
					programmee.setDisabled(true);
				}

				// cas sous direction de formation ,colonnes  grisées (proposée, validée) et les colonnes libelleOriProf,libelleDiscipline et libelleMobilite
				if(compteUtilisateur.getId_profile()==6){

					propose.setDisabled(true);
					validee.setDisabled(true);
			
				}

		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

		//		if(listcompagne.getItemCount()!=0)
		//			listcompagne.setSelectedIndex(0);


		comp1=comp;

	}



	@SuppressWarnings("deprecation")
	public void onClick$new_record() throws InterruptedException
	{

		//initialisation du model associé a liste_action_development
		ListModelList listModel = new ListModelList();
		liste_action_formation.setModel(listModel);
		
		ActionFormationBean actionFormationBean=new ActionFormationBean();

		modelActionFormation.add(actionFormationBean);	

		
		listModel = new ListModelList(modelActionFormation);
		liste_action_formation.setModel(listModel);
		

		
		/***************************/

	}

	@SuppressWarnings("deprecation")
	public void onClick$delete_record() throws InterruptedException
	{
		if(modelActionFormation!=null && modelActionFormation.size()>0){

			modelActionFormation.remove(modelActionFormation.size()-1);	

			ListModelList listModel2 = new ListModelList(modelActionFormation);
			liste_action_formation.setModel(listModel2);
		}


	}




	



	public List<EchelleMaitrise> getModelEchelle() {
		return modelEchelle;
	}

	public List<ActionFormationBean> getModelEvalue() {
		return modelEvalue;
	}

	public List<ActionFormationBean> getModelActionFormation() {
		return modelActionFormation;
	}

	public void setModelActionFormation(
			List<ActionFormationBean> modelActionFormation) {
		this.modelActionFormation = modelActionFormation;
	}
	

	public EchelleMaitrise getSelectedEchelle() {
		return selectedEchelle;
	}

	public void setSelectedEchelle(EchelleMaitrise selectedEchelle) {
		this.selectedEchelle = selectedEchelle;
	}

	public EchelleMaitrise getSelectedEvalue() {
		return selectedEvalue;
	}

	public void setSelectedEvalue(EchelleMaitrise selectedEvalue) {
		this.selectedEvalue = selectedEvalue;
	}



	public List<ListeCompagneVagueBean> getModel() {
		return model;
	}
	public ActionFormationBean getSelected() {
		return selected;
	}

	public void setSelected(ActionFormationBean selected) {
		this.selected = selected;
	}



	public void onSelect$poste_travail() throws SQLException
	{

		desactiverBoutons();
		gridDetails.setVisible(false);
		selectedPosteTravail= (String)poste_travail.getSelectedItem().getValue();

		ListModelList listModel = new ListModelList(modelEchelle);
		liste_echelle.setModel(listModel);


//		
		modelEvalue=new ArrayList<ActionFormationBean>();
//
		ListModelList listModelEvalue = new ListModelList(modelEvalue);
//		
		liste_evalue.setModel(listModelEvalue);
		
		modelActionFormation=new ArrayList<ActionFormationBean>();

		ListModelList listModelActionormation = new ListModelList(modelActionFormation);
		
		liste_action_formation.setModel(listModelActionormation);
		
		 viderListCrud();


	}

	public void onModifyCompagne(ForwardEvent event) throws SQLException{
		Radio radio = (Radio) event.getOrigin().getTarget();

		desactiverBoutons();
		gridDetails.setVisible(false);
		if(selectedRadio.size()>0){
			Set set =selectedRadio.entrySet();
			Iterator i = set.iterator();
			while(i.hasNext()){

				Map.Entry me = (Map.Entry)i.next();

				Radio radio_save= (Radio) me.getValue();
				radio_save.setChecked(false);
				selectedRadio.remove(me.getKey());


			}

		}

		if (radio.isChecked())
		{
			//verifier si ça n'a pas encore été checked
			selectedRadio.put(radio.getId(), radio);
		}

		Set<String> setselected = selectedRadio.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		iterator=listselected.iterator();

		listDb = new HashMap<String,HashMap<String,Integer>>();

		while(iterator.hasNext()) {

			String cles=(String)iterator.next();

			if (selectedRadio.get(cles).isChecked()){

				idcompagne=Integer.parseInt(selectedRadio.get(cles).getContext());
				String nominstance=selectedRadio.get(cles).getSclass();
				String vague=selectedRadio.get(cles).getValue();


				listDb.put(nominstance, new HashMap<String, Integer>());
				listDb.get(nominstance).put(vague, idcompagne);

			}

		}

		if (poste_travail.getSelectedItems().size()>0){
			poste_travail.getItems().clear();
		}

		if (direction1.getSelectedItems().size()>0) 
			direction1.getItems().clear();

		KpiSyntheseModel kpi=new KpiSyntheseModel();
		map_direction=kpi.getListDirectionFicheIndiv(listDb,compteUtilisateur.getId_profile(),compteUtilisateur.getLogin());
		direction1.appendItem("Sélectionner direction", "Sélectionner direction");

		Set set = (map_direction).entrySet(); 
		Iterator i = set.iterator();
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			direction1.appendItem((String) me.getKey(),(String) me.getKey());
		}

		direction1.setSelectedIndex(0);

		
		modelActionFormation=new ArrayList<ActionFormationBean>();

		ListModelList listModelActionormation = new ListModelList(modelActionFormation);
		
		liste_action_formation.setModel(listModelActionormation);


	}

	public void onModifyEchelle(ForwardEvent event) throws SQLException, InterruptedException{

		desactiverBoutons();
		gridDetails.setVisible(false);
		 viderListCrud();

		if(selectedPosteTravail!=null){
			Radio radio = (Radio) event.getOrigin().getTarget();
			if( selectedRadioEchelle!=null)
				selectedRadioEchelle.setChecked(false);

			if (radio.isChecked())
			{
				selectedRadioEchelle=radio;
				String idEchelle=radio.getContext();

				try{
					modelEvalue=new ArrayList<ActionFormationBean>(getListAllEvalue().values());
					
	
					ListModelList listModelEvalue = new ListModelList(modelEvalue);
					listModelEvalue.sort(new Comparator<ActionFormationBean>() {  
	
	
						@Override
						public int compare(ActionFormationBean arg0,
								ActionFormationBean arg1) {
							return arg0.getEvalue().compareTo(arg1.getEvalue());
	
						}  
					}, true);
					liste_evalue.setModel(listModelEvalue);


				}
				catch(Exception e){
					e.printStackTrace();
				}

				
				ListModel listModel2 = new ListModelList(new ArrayList<ActionDevelopmentBean>());
				liste_action_formation.setModel(listModel2);

				viderListCrud();
				

			}
		}else{
			Messagebox.show("Merci de selectionner le poste de travail d'abord", "Information",Messagebox.OK, Messagebox.INFORMATION);
			ArrayList<String> listDomaineFormation=new ArrayList<>(mapFormationExterne.keySet());
			Radio radio = (Radio) event.getOrigin().getTarget();
			radio.setChecked(false);
		}


	}

	public void onModifyEvalue(ForwardEvent event) throws SQLException, InterruptedException{

		gridDetails.setVisible(false);
		//vider map a chaque selection d'employé
		if (mapEvalueAction!=null){
			mapEvalueAction.clear();
		}

		if(selectedPosteTravail!=null){
			Radio radio = (Radio) event.getOrigin().getTarget();
			if( selectedRadioEvalue!=null)
				selectedRadioEvalue.setChecked(false);

			if (radio.isChecked())
			{
				selectedRadioEvalue=radio;

				mapEvalueAction=correctionPosteMoel.getFormationEvalue(listDb,(new Integer(idcompagne)).toString(),selectedPosteTravail,selectedRadioEchelle.getContext(),selectedRadioEvalue.getContext());

				modelActionFormation=mapEvalueAction.get(selectedRadioEvalue.getContext());

				if(modelActionFormation==null)
					modelActionFormation=new ArrayList<ActionFormationBean>();

					ListModelList listModel = new ListModelList();

	
					listModel = new ListModelList(modelActionFormation);
					liste_action_formation.setModel(listModel);
					liste_action_formation.renderAll();
					

					viderListCrud();
					
					remplirListCrud();
					
					okAdd.setVisible(false);
					add.setVisible(true);
					update.setVisible(true);
					delete.setVisible(true);
					
			}
		}else{
			Messagebox.show("Merci de selectionner le poste de travail d'abord", "Information",Messagebox.OK, Messagebox.INFORMATION);
			//initListBoxSuivi( listSuiviSort);
			Radio radio = (Radio) event.getOrigin().getTarget();
			radio.setChecked(false);
		}


	}



	public HashMap<String, ActionFormationBean> getListAllEvalue(){
		HashMap<String, ActionFormationBean> retour=correctionPosteMoel.getDevelopmentEvalueForma(listDb,(new Integer(idcompagne)).toString(),selectedPosteTravail,selectedRadioEchelle.getContext(),compteUtilisateur.getId_profile(),compteUtilisateur.getLogin());


		return retour;
	}

	public ActionFormationBean getActionDev(String selectedActionFormEmploye,  List<ActionFormationBean> listAction){

		for (Iterator <ActionFormationBean> iterator = listAction.iterator(); iterator.hasNext();) {
			ActionFormationBean actionDevelopmentBean = (ActionFormationBean) iterator
					.next();
			if(actionDevelopmentBean.getIdActionFormEmploye()!=null && actionDevelopmentBean.getIdActionFormEmploye().equals(selectedActionFormEmploye))
				return actionDevelopmentBean;
		}
		return null;
	}


	public void onSelect$direction1() throws SQLException	{

		desactiverBoutons();
		gridDetails.setVisible(false);
		poste_travail.getItems().clear();

		poste_travail.appendItem("Tous Postes Travail","tous");

		String libelle_direction=(String)direction1.getSelectedItem().getValue();
		List <String> list_code_dir=(List)map_direction.get(libelle_direction);

		KpiSyntheseModel kpi=new KpiSyntheseModel();
		map_poste=kpi.getListPostTravailValidFicheIndiv(listDb,list_code_dir,compteUtilisateur.getId_profile(),compteUtilisateur.getLogin());
		Set set = (map_poste).entrySet(); 

		Iterator i = set.iterator();
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			poste_travail.appendItem((String) me.getKey(),(String) me.getValue());
		}

		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			poste_travail.appendItem((String) me.getKey(),(String) me.getValue());
		}

		poste_travail.setSelectedIndex(0);


		//chargement des d'echelles
		modelEchelle=correctionPosteMoel.getListeEchelleMaitrise(listDb);

		//chargement de la table suivi_sort
		listSuiviSort= correctionPosteMoel.getListeSuiviSortEvalue( listDb);
		ListModelList listModel = new ListModelList(modelEchelle);
		liste_echelle.setModel(listModel);

		ListModelList listModel2 = new ListModelList(new ArrayList<ActionDevelopmentBean>());
		liste_action_formation.setModel(listModel2);
		selectedPosteTravail=null;
		

		modelEvalue=new ArrayList<ActionFormationBean>();

		ListModelList listModelEvalue = new ListModelList(modelEvalue);
		
		liste_evalue.setModel(listModelEvalue);
		
		modelActionFormation=new ArrayList<ActionFormationBean>();

		ListModelList listModelActionormation = new ListModelList(modelActionFormation);
		
		liste_action_formation.setModel(listModelActionormation);

		//chargement des tpes de formation
		mapFormationExterne= correctionPosteMoel.getListeTypeFotmation( listDb);
		
		viderListCrud();
	}
	

	public void onModifyPropose(ForwardEvent event) throws SQLException, InterruptedException{

         propose.getSelectedItem().getLabel();
		
		}
	  
	public void setSuiviSort(List<String> data) {
	    this.suiviSort = data;
	    //render template
	}

	public void onSelect$liste_domaine_formation()
	{
		String selectedDomaine=liste_domaine_formation.getSelectedItem().getLabel();
		

		//vider le contenu de domaine formation
		if(theme_domaine_formation.getItemCount()>0)
		{
			int nb= theme_domaine_formation.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{

				theme_domaine_formation.removeItemAt(i);
			}
		}
		HashMap<String, String> mapThematique=mapFormationExterne.get(selectedDomaine);
		Set set = mapThematique.entrySet();
		Iterator i = set.iterator();
		
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			theme_domaine_formation.appendItem((String) me.getValue(),(String)me.getValue());
		}
		theme_domaine_formation.renderAll();
		
	}
	
	private void viderListCrud(){
		
		formation_.setText("");
		//vider le contenu de liste domaine formation
		if(liste_domaine_formation.getItemCount()>0)
		{
			int nb= liste_domaine_formation.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{

				liste_domaine_formation.removeItemAt(i);
			}
		}
		
		//vider le contenu de domaine formation
		if(theme_domaine_formation.getItemCount()>0)
		{
			int nb= theme_domaine_formation.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{

				theme_domaine_formation.removeItemAt(i);
			}
		}
		//vider le contenu de domaine formation
		
		if(propose!=null &&propose.getItemCount()>0)
		{
			int nb= propose.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{

				propose.removeItemAt(i);
			}
		}
		
		if(validee!=null && validee.getItemCount()>0)
		{
			int nb= validee.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{

				validee.removeItemAt(i);
			}
		}
		
		if(realisee!=null && realisee.getItemCount()>0)
		{
			int nb= realisee.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{

				realisee.removeItemAt(i);
			}
		}
		
		if(programmee!=null && programmee.getItemCount()>0)
		{
			int nb= programmee.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{

				programmee.removeItemAt(i);
			}
		}
		
	}
	
	public Listbox getTheme_domaine_formation() {
		return theme_domaine_formation;
	}



	public void setTheme_domaine_formation(Listbox theme_domaine_formation) {
		this.theme_domaine_formation = theme_domaine_formation;
	}
	
	public void remplirListCrud(){
		
		ArrayList<String> listDomaineFormation=new ArrayList<>(mapFormationExterne.keySet());
		ListModelList listModelTypeFormation = new ListModelList(listDomaineFormation);
		Iterator iterateur=listDomaineFormation.iterator();
		while(iterateur.hasNext()){
			String valeur=(String)iterateur.next();
			liste_domaine_formation.appendItem(valeur,valeur);
		}
		if(liste_domaine_formation.getItemCount()>0)
			liste_domaine_formation.setSelectedIndex(0);
		String selectedDomaine=liste_domaine_formation.getItemAtIndex(0).getLabel();
		

		HashMap<String, String> mapThematique=mapFormationExterne.get(selectedDomaine);
		Set set = mapThematique.entrySet();
		Iterator i = set.iterator();
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			theme_domaine_formation.appendItem((String) me.getValue(),(String)me.getValue());
		}
		
		if(theme_domaine_formation.getItemCount()>0)
			theme_domaine_formation.setSelectedIndex(0);
		
		List listOuiNon=Arrays.asList("Oui","Non");
		Iterator i2 = listOuiNon.iterator();
		while(i2.hasNext()) {
			String valeur=(String)i2.next();
			propose.appendItem(valeur,valeur);
			validee.appendItem(valeur,valeur);
			realisee.appendItem(valeur,valeur);
			programmee.appendItem(valeur,valeur);
		}
		propose.renderAll();
		validee.renderAll();
		realisee.renderAll();
		programmee.renderAll();
		
	}
	public void onClick$add() throws WrongValueException, ParseException {

		formation_.setText("");

		int nbListeDomaine=liste_domaine_formation.getItemCount();
		if(!liste_domaine_formation.getItemAtIndex(nbListeDomaine-1).getValue().equals("")){
			liste_domaine_formation.appendItem("", "");
			theme_domaine_formation.appendItem("", "");
			propose.appendItem("", "");
			validee.appendItem("", "");
			programmee.appendItem("", "");
			realisee.appendItem("", "");
		}
		liste_domaine_formation.setSelectedIndex(liste_domaine_formation.getItemCount()-1);
		
		theme_domaine_formation.setSelectedIndex(theme_domaine_formation.getItemCount()-1);
		
		propose.setSelectedIndex(2);
		
		validee.setSelectedIndex(2);
		
		programmee.setSelectedIndex(2);
		
		realisee.setSelectedIndex(2);

		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);

		gridDetails.setVisible(true);

	}
	
	/******************/
	
	public void onClick$okAdd()throws WrongValueException, ParseException, InterruptedException {

		ActionFormationBean actionFormation = new ActionFormationBean();
		
		actionFormation.setIdEvalue(selectedRadioEvalue.getContext());
		actionFormation.setLibelleFormation(formation_.getText());
		actionFormation.setIdEchelle(selectedRadioEchelle.getContext());
		actionFormation.setCodePosteTravail(selectedPosteTravail);
		actionFormation.setIdCompagne(idcompagne.toString());

		if(checkSelection()){
			
			String idThemeFormation=correctionPosteMoel.getIdThemeFormation(listDb,theme_domaine_formation.getSelectedItem().getLabel(),liste_domaine_formation.getSelectedItem().getLabel());
			actionFormation.setIdTypeFormationExterne(idThemeFormation);
			actionFormation.setThemeFormation(theme_domaine_formation.getSelectedItem().getLabel());
			
			actionFormation.setDomaineFormation(liste_domaine_formation.getSelectedItem().getLabel());
			
			//TODO prendre en compte la recuperation 
			actionFormation.setPropose((String) propose.getSelectedItem().getLabel());
			actionFormation.setValidee((String) validee.getSelectedItem().getLabel());
			actionFormation.setProgrammee((String) programmee.getSelectedItem().getLabel());
			actionFormation.setRealisee((String) realisee.getSelectedItem().getLabel());
	
	
			//insertion de la donnée ajoutée dans la base de donnée
			boolean donneeAjoute=true;
			correctionPosteMoel.addBatchActionForma(listDb, actionFormation);
			// raffrechissemet de l'affichage
			if (donneeAjoute )
			{
//				ListModelList listModel = new ListModelList();
//				liste_action_formation.setModel(listModel);
//				modelActionFormation.add(actionFormation);	
//	
//				
//				listModel = new ListModelList(modelActionFormation);
//				liste_action_formation.setModel(listModel);
//				
//				
//	
//				selected = actionFormation;
				
				majAffichageListeFormation();
	
				//binder.loadAll();
			}
	
			okAdd.setVisible(false);
			effacer.setVisible(false);
			add.setVisible(true);
			update.setVisible(true);
			delete.setVisible(true);
		}

	}

	public void onClick$update() throws WrongValueException, ParseException, InterruptedException {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}


		//verifier si le login existe déja
		ActionFormationBean actionFormation = new ActionFormationBean();


		
		actionFormation.setIdEvalue(selectedRadioEvalue.getContext());
		actionFormation.setLibelleFormation(formation_.getText());
		actionFormation.setIdEchelle(selectedRadioEchelle.getContext());
		actionFormation.setCodePosteTravail(selectedPosteTravail);
		actionFormation.setIdCompagne(idcompagne.toString());
		
		if(checkSelection()){

			String idThemeFormation=correctionPosteMoel.getIdThemeFormation(listDb,theme_domaine_formation.getSelectedItem().getLabel(),liste_domaine_formation.getSelectedItem().getLabel());
			actionFormation.setIdTypeFormationExterne(idThemeFormation);

			actionFormation.setThemeFormation(theme_domaine_formation.getSelectedItem().getLabel());
			actionFormation.setDomaineFormation(liste_domaine_formation.getSelectedItem().getLabel());
			actionFormation.setIdActionFormEmploye(selected.getIdActionFormEmploye());

			actionFormation.setPropose((String) propose.getSelectedItem().getLabel());
			actionFormation.setValidee((String) validee.getSelectedItem().getLabel());
			actionFormation.setProgrammee((String) programmee.getSelectedItem().getLabel());
			actionFormation.setRealisee((String) realisee.getSelectedItem().getLabel());
	
					//insertion de la donnée ajoutée dans la base de donnée
	
			if (Messagebox.show("Voulez vous appliquer les modifications?", "Prompt", Messagebox.YES|Messagebox.NO,
						Messagebox.QUESTION) == Messagebox.YES) {
	
				correctionPosteMoel.updateBatchActionForma(listDb, actionFormation);
				
				majAffichageListeFormation();
				//binder.loadAll();
				return;
			}
	
			else{
				return;
			}
	

		}

	}

	public void onClick$delete() throws InterruptedException {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		ActionFormationBean actionFormation = new ActionFormationBean();
		//suppression de la donnée supprimée de la base de donnée

		if (Messagebox.show("Voulez vous supprimer cete formation?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {

			correctionPosteMoel.deleteBatchActionForma(listDb, selected);
			modelActionFormation.remove(selected);
			
			ListModelList listModel = new ListModelList();
			
			listModel = new ListModelList(modelActionFormation);
			liste_action_formation.setModel(listModel);
			
			//majAffichageListeFormation();
			selected = null;
			//binder.loadAll();
			return;
		}

		else{
			return;
		}

	}

	public void onClick$effacer()  {


		formation_.setText("");
		okAdd.setVisible(false);
		add.setVisible(true);
		update.setVisible(true);
		delete.setVisible(true);


	}
	
	public void majAffichageListeFormation(){
		
		mapEvalueAction=correctionPosteMoel.getFormationEvalue(listDb,(new Integer(idcompagne)).toString(),selectedPosteTravail,selectedRadioEchelle.getContext(),selectedRadioEvalue.getContext());

		modelActionFormation=mapEvalueAction.get(selectedRadioEvalue.getContext());

		if(modelActionFormation==null)
			modelActionFormation=new ArrayList<ActionFormationBean>();

			ListModelList listModel = new ListModelList();


			listModel = new ListModelList(modelActionFormation);
			liste_action_formation.setModel(listModel);
			liste_action_formation.renderAll();

		
	}

	public void onSelect$liste_action_formation() {

		String selectedDomaine=selected.getDomaineFormation();
		

		//vider le contenu de domaine formation
		if(theme_domaine_formation.getItemCount()>0)
		{
			int nb= theme_domaine_formation.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{

				theme_domaine_formation.removeItemAt(i);
			}
		}
		
		HashMap<String, String> mapThematique=mapFormationExterne.get(selectedDomaine);
		Set set = mapThematique.entrySet();
		Iterator i = set.iterator();
		int index=0;
		int idexInit=index;
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			if(selected.getThemeFormation().equalsIgnoreCase((String) me.getValue())) idexInit=index;
			theme_domaine_formation.appendItem((String) me.getValue(),(String) me.getValue());
			index++;
		}
		if(theme_domaine_formation.getItemCount()>=idexInit)
			theme_domaine_formation.setSelectedIndex(idexInit);

		
		gridDetails.setVisible(true);
		theme_domaine_formation.renderAll();
		closeErrorBox(new Component[] { liste_domaine_formation, theme_domaine_formation,formation_,propose,  validee, 
				programmee,realisee });

	}


	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}
	
	public void onSelect$theme_domaine_formation() {
		if("Autre".equalsIgnoreCase(((String)theme_domaine_formation.getSelectedItem().getValue()).trim())||
				"Autres".equalsIgnoreCase(((String)theme_domaine_formation.getSelectedItem().getValue()).trim())){
			formation_.setDisabled(false);
		}else{
			formation_.setDisabled(true);
		}
	}
	
	/******************/
	public Listbox getListe_action_formation() {
		return liste_action_formation;
	}



	public void setListe_action_formation(Listbox liste_action_formation) {
		this.liste_action_formation = liste_action_formation;
	}



	public Listbox getListe_domaine_formation() {
		return liste_domaine_formation;
	}



	public void setListe_domaine_formation(Listbox liste_domaine_formation) {
		this.liste_domaine_formation = liste_domaine_formation;
	}
	public boolean checkSelection() throws InterruptedException{
		
		if(theme_domaine_formation.getSelectedItem() ==null || StringUtils.isEmpty((String)theme_domaine_formation.getSelectedItem().getLabel())){
			Messagebox.show("Merci de selectionner un thème de formation ", "Erreur", Messagebox.OK, Messagebox.ERROR);
						return false;
		}
		if(liste_domaine_formation.getSelectedItem()==null || StringUtils.isEmpty((String)liste_domaine_formation.getSelectedItem().getLabel())){
			Messagebox.show("Merci de selectionner un domaine de formation ", "Erreur", Messagebox.OK, Messagebox.ERROR);
						return false;
		}
		/*if(propose.getSelectedItem() == null || StringUtils.isEmpty((String)propose.getSelectedItem().getLabel())){
			Messagebox.show("Merci de spécifier si le formation a été proposée ", "Erreur", Messagebox.OK, Messagebox.ERROR);
						return false;
		}
		if(validee.getSelectedItem()  == null || StringUtils.isEmpty((String)validee.getSelectedItem().getLabel())){
			Messagebox.show("Merci de spécifier si le formation a été validée", "Erreur", Messagebox.OK, Messagebox.ERROR);
						return false;
		}
		if(programmee.getSelectedItem() == null || StringUtils.isEmpty((String)programmee.getSelectedItem().getLabel())){
			Messagebox.show("Merci de spécifier si le formation a été programée", "Erreur", Messagebox.OK, Messagebox.ERROR);
						return false;
		}
		if(realisee.getSelectedItem() == null || StringUtils.isEmpty((String)realisee.getSelectedItem().getLabel())){
			Messagebox.show("Merci de spécifier si le formation a été réalisée", "Erreur", Messagebox.OK, Messagebox.ERROR);
						return false;
		}*/
		return true;

	
		
		
	}
	
	public void desactiverBoutons(){
		okAdd.setVisible(false);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);
	}
}

