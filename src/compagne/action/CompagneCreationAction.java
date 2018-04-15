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

import org.zkoss.lang.Strings;
import org.zkoss.zk.au.out.AuClearWrongValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;

import compagne.bean.CompagnePosteMapBean;
import compagne.bean.MixIdealBean;
import compagne.bean.PlanningCompagneListBean;
import compagne.model.CompagneCreationModel;
import compagne.model.PlanningCompagneModel;
import administration.bean.AdministrationLoginBean;
import administration.bean.CompagneCreationBean;
import administration.bean.FichePosteBean;
import administration.bean.StructureEntrepriseBean;
import administration.model.AdministrationLoginModel;
import administration.model.ProfileDroitsAccessModel;
import administration.model.StructureEntrepriseModel;

public class CompagneCreationAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Listbox admincomptelb;
	Listbox admincomptelb1;
	Listbox mixlb;


	Textbox idcompagne;
	Textbox id_compagne_type;
	Textbox  nom;
	Listbox type_compagne;
	Datebox date_deb_comp;
	Datebox date_fin_comp;
	AnnotateDataBinder binder;
	List<CompagneCreationBean> model = new ArrayList<CompagneCreationBean>();
	List<CompagnePosteMapBean> model1 = new ArrayList<CompagnePosteMapBean>();
	List<MixIdealBean> model_mix = new ArrayList<MixIdealBean>();

	Textbox idcompagne_mix;
	Textbox datedebut;
	Textbox datefin;
	Textbox niveaumaitise;
	Textbox objectif;

	CompagneCreationBean selected;
	CompagnePosteMapBean selected1;
	MixIdealBean selectedmix;
	Map map_nomcomp=null;



	List list_profile=null;
	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button upload;
	Button download;
	Button effacer;
	Button valider;
	Map map=null;
	Map ma_compagne=null;
	Tab compagne_tb;
	Tab compvsPoste_tb;
	Combobox compagne_cb;
	private String lbl_compagne_type;
	Tab mix_tb;
	HashMap <String, Checkbox> selectedCheckBox;
	HashMap <String, Checkbox> unselectedCheckBox;
	HashMap <String, Checkbox> listCheckBox;
	AnnotateDataBinder binder1;
	Button dupliquer;

	Listbox nomcompagne;
	public CompagneCreationAction() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {

		super.doAfterCompose(comp);

		dupliquer.setVisible(false);
		selectedCheckBox=new  HashMap <String, Checkbox>();
		unselectedCheckBox=new  HashMap <String, Checkbox>();
		listCheckBox=new  HashMap <String, Checkbox>();
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		okAdd.setVisible(false);
		effacer.setVisible(false);
		CompagneCreationModel init= new CompagneCreationModel();
		map = new HashMap();
		map=init.getCompagneType();
		Set set = (map).entrySet(); 
		Iterator i = set.iterator();

		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			type_compagne.appendItem((String) me.getKey(),(String) me.getKey());
			//profilemodel.add((String) me.getKey());
		}
		model=init.loadCompagnelist();
		binder = new AnnotateDataBinder(comp);
		if(model.size()!=0)
			selected=model.get(0);

		if(admincomptelb.getItemCount()!=0)
			admincomptelb.setSelectedIndex(0);
		binder.loadAll();
		compvsPoste_tb.addForward(Events.ON_CLICK, comp, "onSelectTab");
		mix_tb.addForward(Events.ON_CLICK, comp, "onSelectTab");


	}

	public List<CompagneCreationBean> getModel() {
		return model;
	}

	public CompagneCreationBean getSelected() {
		return selected;
	}

	public void setSelected(CompagneCreationBean selected) {
		this.selected = selected;
	}



	public List<CompagnePosteMapBean> getModel1() {
		return model1;
	}

	public void setModel1(List<CompagnePosteMapBean> model1) {
		this.model1 = model1;
	}

	public CompagnePosteMapBean getSelected1() {
		return selected1;
	}

	public void setSelected1(CompagnePosteMapBean selected1) {
		this.selected1 = selected1;
	}

	public void onClick$add() throws WrongValueException, ParseException {

		clearFields();
		type_compagne.setSelectedIndex(0);
		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);

	}

	public void onClick$okAdd()throws WrongValueException, ParseException, InterruptedException {

		CompagneCreationBean addedData = new CompagneCreationBean();

		addedData.setNom_compagne(getSelectedNom());
		addedData.setId_compagne_type(getSelectedtype_compagne());
		addedData.setDate_deb_comp(getSelecteddate_deb_comp());
		addedData.setDate_fin_comp(getSelecteddate_fin_comp());
		addedData.setType_compagne(getLbl_compagne_type());


		//controle d'intégrité 
		CompagneCreationModel compagne_model =new CompagneCreationModel();
		//compagne_model.addCompagne(addedData);
		Boolean donneeValide=compagne_model.controleIntegrite(addedData);
		//Boolean donneeValide=true;

		if (donneeValide)
		{
			//insertion de la donnée ajoutée dans la base de donnée
			boolean donneeAjoute=compagne_model.addCompagne(addedData);
			// raffrechissemet de l'affichage
			if (donneeAjoute )
			{
				model.add(addedData);

				selected = addedData;

				binder.loadAll();
			}
		}

		//admincomptelb.invalidate();
		okAdd.setVisible(false);
		effacer.setVisible(false);
		add.setVisible(true);
		update.setVisible(true);
		delete.setVisible(true);
		try {
			refreshPage();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onClick$update() throws WrongValueException, ParseException, InterruptedException {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		CompagneCreationBean addedData = new CompagneCreationBean();
		selected.setId_compagne(getSelectedIdCompagne()); 
		selected.setNom_compagne(getSelectedNom());
		selected.setId_compagne_type(getSelectedtype_compagne());
		selected.setDate_deb_comp(getSelecteddate_deb_comp());
		selected.setDate_fin_comp(getSelecteddate_fin_comp());
		selected.setType_compagne(getLbl_compagne_type());

		//controle d'intégrité 
		CompagneCreationModel compagne_model =new CompagneCreationModel();
		//controle d'intégrité 
		Boolean donneeValide=compagne_model.controleIntegrite(selected);
		if (donneeValide)
		{
			//insertion de la donnée ajoutée dans la base de donnée

			if (Messagebox.show("Voulez vous appliquer les modifications?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				//System.out.println("pressyes");
				compagne_model.updateCompagne(selected);
				binder.loadAll();
				return;
			}

			else{
				return;
			}
		}	



	}


	public void onClick$update_mix() throws WrongValueException, ParseException, InterruptedException {
		if (model_mix == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		CompagneCreationBean addedData = new CompagneCreationBean();
		//selectedmix.setObjectif(getSelectedObjectif()); 

		CompagneCreationModel compagne_model =new CompagneCreationModel();
		//controle d'intégrité 

		//insertion de la donnée ajoutée dans la base de donnée

		if (Messagebox.show("Voulez vous appliquer les modifications?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			//System.out.println("pressyes");
			
			if (compagne_model.checkIMIMix(model_mix)==100){
				compagne_model.updateImiMixIdeal(model_mix);
				binder.loadAll();
				return;
			}else{
				Messagebox.show("La somme des objectifs doit être égale à 100%", "Erreur",Messagebox.OK, Messagebox.ERROR);
				return;
			}
			
		}

		else{
			return;
		}
	}	




	public void onClick$delete() throws InterruptedException, SQLException {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		CompagneCreationModel compagne_model =new CompagneCreationModel();
		//suppression de la donnée supprimée de la base de donnée
		selected.setId_compagne(getSelectedIdCompagne()); 

		if (Messagebox.show("Voulez vous supprimer cet enregistrement?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			//System.out.println("pressyes");
			//compagne_model.deleteCompagne(selected);
			compagne_model.reinitCompagne(selected);
			model.remove(selected);
			selected = null;
			binder.loadAll();
			return;
		}

		else{
			return;
		}





	}

	public void onClick$delete_mix() throws InterruptedException, SQLException {
		if (selectedmix == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		CompagneCreationModel compagne_model =new CompagneCreationModel();
		//suppression de la donnée supprimée de la base de donnée
		selectedmix.setId_compagne(getSelectedNomcompagneMix()); 

		if (Messagebox.show("Voulez vous supprimer Le mix ideal pour cette compagne?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			//System.out.println("pressyes");
			compagne_model.deleteImiMixIdeal(selectedmix);
			model_mix.remove(selectedmix);
			selectedmix = null;
			refreshPageMix();
			binder.loadAll();
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
		update.setVisible(true);
		delete.setVisible(true);
		admincomptelb.setSelectedIndex(0);
		binder.loadAll();


	}



	public void onSelect$admincomptelb() throws SQLException {

		dupliquer.setVisible(false);

		//mise à jour de la liste d'evaluateur
		int index=admincomptelb.getSelectedIndex();


		CompagneCreationBean compagne=model.get(index);
		CompagneCreationModel compagneModel= new CompagneCreationModel();
		if (!compagneModel.checkIfPlanningExist(compagne.getId_compagne())){

			/*
			 * enlever le commentaire pour activer la fonction
			 */
			dupliquer.setVisible(true);

		}

		closeErrorBox(new Component[] { idcompagne,nom,type_compagne, date_deb_comp, date_fin_comp,id_compagne_type});
	}


	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}


	private int getSelectedIdCompagne() throws WrongValueException {

		Integer  name = Integer.parseInt(idcompagne.getValue());
		if (name== null) {
			throw new WrongValueException(nom, "Merci de saisie un nom de compagne!");
		}
		return name;
	}

	private String getSelectedNom() throws WrongValueException {
		String name = nom.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nom, "Merci de saisie un nom de compagne!");
		}
		return name;
	}


	private int getSelectedNomcompagneMix() throws WrongValueException {
		Integer name=(Integer) map_nomcomp.get((String)nomcompagne.getSelectedItem().getLabel());
		//setLbl_compagne_type((String)type_compagne.getSelectedItem().getLabel());

		if (name==null) {
			throw new WrongValueException(type_compagne, "Merci de choisir un type compagne!");
		}
		return name;
	}



	private Float getSelectedObjectif() throws WrongValueException {
		Float name= Float.parseFloat(objectif.getValue());
		//setLbl_compagne_type((String)type_compagne.getSelectedItem().getLabel());

		if (name==null) {
			throw new WrongValueException(objectif, "Merci de choisir un objectif!");
		}
		return name;
	}
	

	private int getSelectedtype_compagne() throws WrongValueException {
		Integer name=(Integer) map.get((String)type_compagne.getSelectedItem().getLabel());
		setLbl_compagne_type((String)type_compagne.getSelectedItem().getLabel());

		if (name==null) {
			throw new WrongValueException(type_compagne, "Merci de choisir un type compagne!");
		}
		return name;
	}


	private Date getSelecteddate_deb_comp() throws WrongValueException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String name = date_deb_comp.getText();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_deb_comp, "la date debut compagne ne doit pas être vide!");
		}
		Date datedeb = df.parse(name); 
		return datedeb;
	}

	private Date getSelecteddate_fin_comp() throws WrongValueException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String name = date_fin_comp.getText();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_fin_comp, "la date fin compagne ne doit pas être vide!");
		}
		Date datefin = df.parse(name); 
		return datefin;
	}


	public void clearFields(){
		nom.setText("");

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


	public void onSelectTab(ForwardEvent event) throws SQLException
	{

		Tab tb = (Tab) event.getOrigin().getTarget();	

		String idtab=tb.getId();

		if (idtab.equalsIgnoreCase("compvsPoste_tb")){

			compagne_cb.getItems().clear();
			CompagneCreationModel init =new CompagneCreationModel();
			ma_compagne=init.getListCompagne();
			Set set = (ma_compagne).entrySet(); 
			Iterator i = set.iterator();

			// Display elements
			while(i.hasNext()) {
				Map.Entry me = (Map.Entry)i.next();
				compagne_cb.appendItem((String)me.getKey());

			}

		}else{

			idcompagne_mix.setDisabled(true);
			nomcompagne.setDisabled(true);
			datedebut.setDisabled(true);
			datefin.setDisabled(true);
			niveaumaitise.setDisabled(true);
			
			PlanningCompagneModel init= new PlanningCompagneModel();
			map_nomcomp = new HashMap();
			map_nomcomp=init.getCompagneValid();
			Set set = (map_nomcomp).entrySet(); 
			Iterator i = set.iterator();

			nomcompagne.appendItem("Toutes campagnes ","Toutes campagnes ");
			// Display elements
			while(i.hasNext()) {
				Map.Entry me = (Map.Entry)i.next();
				nomcompagne.appendItem((String) me.getKey(),(String) me.getKey());
			}

			CompagneCreationModel init1= new CompagneCreationModel();

			model_mix=init1.getMixNiveauMaitrise();

			//Component comp1 = null;
			//binder = new AnnotateDataBinder(comp1);
			if(model_mix.size()!=0)
				selectedmix=model_mix.get(0);

			if(mixlb.getItemCount()!=0)
				mixlb.setSelectedIndex(0);
			binder.loadAll();

		}


	}

	public void onSelect$compagne_cb() throws SQLException	 {


		Integer compagne_id= (Integer) ma_compagne.get((String)compagne_cb.getSelectedItem().getLabel());

		CompagneCreationModel init =new CompagneCreationModel();

		model1=init.loadPosteMapToComapgne(compagne_id);
		binder1 = new AnnotateDataBinder(self);
		if(model1.size()!=0)
			selected1=model1.get(0);

		if(admincomptelb1.getItemCount()!=0)
			admincomptelb1.setSelectedIndex(0);
		binder1.loadAll();



	}

	public void onCreation(ForwardEvent event){


		Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();
		listCheckBox.put(checkbox.getValue(),(Checkbox) checkbox);
		if (checkbox.getName().equalsIgnoreCase("1")){
			checkbox.setChecked(true);
			selectedCheckBox.put(checkbox.getValue(),(Checkbox) checkbox);

		}

		//System.out.println(selectedCheckBox.size());
	}

	public void onClick$valider() throws InterruptedException  {

		HashMap map_checked_cb = new HashMap();	
		Set<String> setselected = listCheckBox.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		CompagneCreationModel init =new CompagneCreationModel();

		while (iterator.hasNext()){
			String cles=(String)iterator.next();
			Checkbox checkBox=listCheckBox.get(cles);
			if (checkBox.isChecked()){
				map_checked_cb.put(checkBox.getValue(), (Integer) ma_compagne.get((String)compagne_cb.getSelectedItem().getLabel()));
			}
		}
		try {
			if (Messagebox.show("Voulez vous ajouter ces postes de travail à la compagne: "+(String)compagne_cb.getSelectedItem().getLabel()+"?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				init.appliquerMapPosteCompagne(map_checked_cb, (Integer) ma_compagne.get((String)compagne_cb.getSelectedItem().getLabel()));
				selectedCheckBox.clear();
				unselectedCheckBox.clear();

				//binder1.loadAll();
				return;
			}
			else{
				return;
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				Messagebox.show("La modification n'a pas été prise en compte"+e, "Erreur",Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}


	public void onClick$dupliquer() throws InterruptedException, WrongValueException, ParseException  {

		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		CompagneCreationBean addedData = new CompagneCreationBean();
		selected.setId_compagne(getSelectedIdCompagne()); 
		selected.setDate_deb_comp(getSelecteddate_deb_comp());
		selected.setDate_fin_comp(getSelecteddate_fin_comp());



		CompagneCreationModel init =new CompagneCreationModel();


		try {
			if (Messagebox.show("Voulez vous dupliquer le planning de la derniere compagne validée ? ", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {

				if (!init.VerifPlanningCreation(selected)){
					init.dupliquerPlanning(selected);
					init.dupliquerCompagnePosteTravail(selected);
					init.dupliquerImiMixIdeal(selected);
					return;
				}else{
					Messagebox.show( "Un planning avec la reference de la compagne selectionnée existe déja !", "Erreur", Messagebox.OK, Messagebox.ERROR );

				}



			}
			else{
				return;
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				Messagebox.show("La modification n'a pas été prise en compte"+e, "Erreur",Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}


	public String getLbl_compagne_type() {
		return lbl_compagne_type;
	}

	public void setLbl_compagne_type(String lbl_compagne_type) {
		this.lbl_compagne_type = lbl_compagne_type;
	}

	public List<MixIdealBean> getModel_mix() {
		return model_mix;
	}

	public void setModel_mix(List<MixIdealBean> model_mix) {
		this.model_mix = model_mix;
	}

	public MixIdealBean getSelectedmix() {
		return selectedmix;
	}

	public void setSelectedmix(MixIdealBean selectedmix) {
		this.selectedmix = selectedmix;
	}

	public void onClick$dupliquer_mix() throws InterruptedException, WrongValueException, ParseException  {

		if (selectedmix == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		//CompagneCreationBean addedData = new CompagneCreationBean();
		selectedmix.setId_compagne(getSelectedNomcompagneMix()); 
		//selected.setDate_deb_comp(getSelecteddate_deb_comp());
		//selected.setDate_fin_comp(getSelecteddate_fin_comp());


		CompagneCreationModel init =new CompagneCreationModel();


		try {
			if (Messagebox.show("Voulez vous dupliquer les objectifs de la compagne précédente ? ", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {

				if (!init.VerifCompagneCreation(selectedmix)){
					init.dupliquerImiMixIdealSelected(selectedmix);

					return;
				}else{
					Messagebox.show( "La Mix Ideal pour la compagne selectionée existe déja!", "Erreur", Messagebox.OK, Messagebox.ERROR );

				}



			}
			else{
				return;
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				Messagebox.show("La modification n'a pas été prise en compte"+e, "Erreur",Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void refreshPage() throws SQLException{
		CompagneCreationModel init= new CompagneCreationModel();
		model=init.loadCompagnelist();
		binder.loadAll();
	}

	public void refreshPageMix() throws SQLException{
		CompagneCreationModel init= new CompagneCreationModel();
		model_mix=init.getMixNiveauMaitrise();
		binder.loadAll();
	}
	
	
	
	public void onSelect$mixlb() throws SQLException {

		idcompagne_mix.setDisabled(true);
		nomcompagne.setDisabled(true);
		datedebut.setDisabled(true);
		datefin.setDisabled(true);
		niveaumaitise.setDisabled(true);

		//closeErrorBoxMix(new Component[] { idcompagne_mix,nomcompagne,datedebut, datefin, niveaumaitise,objectif});
	}


	private void closeErrorBoxMix(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}
	
	
	


}