package administration.action;


import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import administration.bean.DroitsAccessBean;
import administration.bean.ProfileDroitsAccessBean;
import administration.bean.ProfileSortedBean;
import administration.model.ProfileDroitsAccessModel;


public class ProfileDroitsAccesAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Listbox admincomptelb;
	Listbox admincomptelb1;

	Textbox id_profile;
	Textbox libelle_profile;
	Label lbl_id_profile;


	Combobox profile;


	Tab defBase;
	Tab baseClient;
	Component comp1;


	AnnotateDataBinder binder;
	AnnotateDataBinder binder1;

	List<ProfileDroitsAccessBean> model = new ArrayList<ProfileDroitsAccessBean>();
	List<DroitsAccessBean> model1 = new ArrayList<DroitsAccessBean>();
	ProfileDroitsAccessBean selected;
	DroitsAccessBean selected1;
	List list_profile=null;
	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button upload;
	Button download;
	Button valider;
	Button effacer;
	Map map_profile=null;
	Map map_listecran=null;

	HashMap <String, Checkbox> selectedCheckBox;
	HashMap <String, Checkbox> unselectedCheckBox;

	public ProfileDroitsAccesAction() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		okAdd.setVisible(false);
		effacer.setVisible(false);

		ProfileDroitsAccessModel init= new ProfileDroitsAccessModel();
		model=init.loadProfile();

		lbl_id_profile.setVisible(false);
		id_profile.setVisible(false);

		binder = new AnnotateDataBinder(comp);
		if(model.size()!=0)
			selected=model.get(0);

		if(admincomptelb.getItemCount()!=0)
			admincomptelb.setSelectedIndex(0);
		binder.loadAll();
		baseClient.addForward(Events.ON_CLICK, comp, "onSelectTab");

	}

	public List<ProfileDroitsAccessBean> getModel() {
		return model;
	}



	public ProfileDroitsAccessBean getSelected() {
		return selected;
	}

	public void setSelected(ProfileDroitsAccessBean selected) {
		this.selected = selected;
	}


	public List<DroitsAccessBean> getModel1() {
		return model1;
	}



	public DroitsAccessBean getSelected1() {
		return selected1;
	}

	public void setSelected1(DroitsAccessBean selected1) {
		this.selected1 = selected1;
	}

	public void onSelect$admincomptelb() {
		closeErrorBox(new Component[] { id_profile,libelle_profile});
	}

	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}


	private Integer getSelecteid_profile() throws WrongValueException {
		Integer name =  Integer.parseInt((String)id_profile.getValue());
		if (name==null) {
			throw new WrongValueException(id_profile, "Merci de saisie un id profil!");
		}
		return name;
	}

	private String getSelectedLibelle_profile() throws WrongValueException {
		String name = libelle_profile.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(libelle_profile, "Merci de saisir un label!");
		}
		return name;
	}

	public void onClick$add() throws WrongValueException, ParseException {

		clearFields();
		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);

	}
	public void onClick$okAdd()throws WrongValueException, ParseException, InterruptedException, SQLException {

		//Tab defBase;
		//Tab baseClient;

		if (defBase.isSelected()){

			ProfileDroitsAccessBean addedData = new ProfileDroitsAccessBean();
			addedData.setLibelle_profile(getSelectedLibelle_profile());


			//controle d'intégrité 
			ProfileDroitsAccessModel compagne_model =new ProfileDroitsAccessModel();
			//compagne_model.addCompagne(addedData);
			//Boolean donneeValide=compagne_model.controleIntegrite(addedData);
			Boolean donneeValide=true;

			if (donneeValide)
			{
				//insertion de la donnée ajoutée dans la base de donnée
				boolean donneeAjoute=compagne_model.addProfile(addedData);
				// raffrechissemet de l'affichage
				if (donneeAjoute )
				{
					model.add(addedData);
					selected = addedData;
					binder.loadAll();
				}
			}
		}

		okAdd.setVisible(false);
		effacer.setVisible(false);
		add.setVisible(true);
		update.setVisible(true);
		delete.setVisible(true);


	}


	public void clearFields(){
		id_profile.setText("");
		libelle_profile.setText("");

	}



	public void onClick$update() throws WrongValueException, ParseException, InterruptedException, SQLException {



		if (defBase.isSelected()){

			ProfileDroitsAccessBean addedData = new ProfileDroitsAccessBean();
			selected.setLibelle_profile(getSelectedLibelle_profile());
			selected.setId_profile(getSelecteid_profile());


			//controle d'intégrité 
			ProfileDroitsAccessModel compagne_model =new ProfileDroitsAccessModel();
			//Boolean donneeValide=compagne_model.controleIntegrite(addedData);
			Boolean donneeValide=true;
			if (donneeValide)
			{
				//insertion de la donnée ajoutée dans la base de donnée

				if (Messagebox.show("Voulez vous appliquer les modifications?", "Prompt", Messagebox.YES|Messagebox.NO,
						Messagebox.QUESTION) == Messagebox.YES) {
					//System.out.println("pressyes");
					compagne_model.updateProfile(selected);
					binder.loadAll();
					return;
				}

				else{
					return;
				}
			}	
		}

	}

	public void onClick$delete() throws InterruptedException, SQLException, ParseException {

		if (defBase.isSelected()){

			ProfileDroitsAccessBean addedData = new ProfileDroitsAccessBean();
			selected.setId_profile(getSelecteid_profile());
			//controle d'intégrité 
			ProfileDroitsAccessModel compagne_model =new ProfileDroitsAccessModel();

			if (Messagebox.show("Voulez vous supprimer cette base de données?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				//System.out.println("pressyes");
				compagne_model.deleteProfile(selected);
				model.remove(selected);
				selected = null;
				binder.loadAll();
				return;
			}

			else{
				return;
			}

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



	public void onSelectTab(ForwardEvent event) throws SQLException
	{

		selectedCheckBox=new HashMap <String, Checkbox>();
		unselectedCheckBox=new HashMap <String, Checkbox>();
		profile.getItems().clear();
		ProfileDroitsAccessModel init =new ProfileDroitsAccessModel();
		map_profile=init.selectProfiles();
		Set set = (map_profile).entrySet(); 
		Iterator i = set.iterator();

		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			profile.appendItem((String)me.getKey());
			//profilemodel.add((String) me.getKey());
		}
	}

	public void onSelect$profile() throws SQLException	 {

		Integer profile_id= (Integer) map_profile.get((String)profile.getSelectedItem().getLabel());

		ProfileDroitsAccessModel init =new ProfileDroitsAccessModel();

		model1=init.loadDroitsAccess(profile_id);
		map_listecran=init.listScreenAccessRight(model1);
		binder1 = new AnnotateDataBinder(self);
		if(model1.size()!=0)
			selected1=model1.get(0);

		if(admincomptelb1.getItemCount()!=0)
			admincomptelb1.setSelectedIndex(0);
		binder1.loadAll();


	}
	public void onCreation(ForwardEvent event){
		Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();
		Set set = (map_listecran).entrySet(); 
		Iterator i = set.iterator();

		String maChaine="";

		// Display elements
		while(i.hasNext()) {
			int tab[ ] = new int[3];
			int k=0;
			Map.Entry me = (Map.Entry)i.next();
			maChaine =(String)me.getValue();
			java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(maChaine, "|");

			while ( tokenizer.hasMoreTokens() ) {

				tab[k]=Integer.valueOf(tokenizer.nextToken());
				k++;
			}

			if ((checkbox.getName().equalsIgnoreCase("hide")) &&
					(checkbox.getValue().equalsIgnoreCase((String)me.getKey())) &&
					(tab[0]==1)){

				checkbox.setChecked(true);	 
			}

			else if ((checkbox.getName().equalsIgnoreCase("ecriture")) &&
					(checkbox.getValue().equalsIgnoreCase((String)me.getKey())) &&
					(tab[1]==1)){

				checkbox.setChecked(true);	 
			}

			else if ((checkbox.getName().equalsIgnoreCase("lecture")) &&
					(checkbox.getValue().equalsIgnoreCase((String)me.getKey())) &&
					(tab[2]==1)){

				checkbox.setChecked(true);	 
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
			selectedCheckBox.put(checkbox.getValue()+"|"+checkbox.getName(), checkbox);
		}
		else
		{
			//verifier si ça n'a pas encore été checked
			if(selectedCheckBox.containsValue(checkbox))
			{
				selectedCheckBox.remove(checkbox);

			}
			unselectedCheckBox.put(checkbox.getValue()+"|"+checkbox.getName(), checkbox);
		}
		//selectedCheckBox
	}

	public void onClick$valider() throws SQLException, InterruptedException{
		//binder1 = new AnnotateDataBinder(self);
		ProfileDroitsAccessModel init =new ProfileDroitsAccessModel();
		Integer profile_id= (Integer) map_profile.get((String)profile.getSelectedItem().getLabel());
		HashMap map_screen = setCheckedCkedBox();
		HashMap map_result=setUncheckedBox();


		if (Messagebox.show("Voulez vous appliquer les modifications?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			if (map_screen.size()!=0){
				init.execQueriesProfile(getScreensrights( map_screen,profile_id));
			}
			if (map_result.size()!=0){
				init.execQueriesProfile(getScreensrights( map_result,profile_id));
			}
			//binder1.loadAll();
			//binder1.loadAll();
			return;
		}

		else{
			binder1.loadAll();
			return;

		}


	}

	/**
	 * @return
	 */
	public HashMap setCheckedCkedBox() {
		HashMap map_screen = new HashMap();
		//Set<String> setselected = selectedCheckBox.keySet( );
		Set<String> setselected = selectedCheckBox.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		iterator=listselected.iterator();




		while (iterator.hasNext())
		{

			String tab[ ] = new String[2];
			String value="";
			int k=0;
			String cles=(String)iterator.next();
			Checkbox checkBox=selectedCheckBox.get(cles);
			java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(cles, "|");

			while ( tokenizer.hasMoreTokens() ) {

				tab[k]=tokenizer.nextToken();
				k++;

			}
			if (map_screen.containsKey(tab[0])){
				value=tab[1]+"|"+ map_screen.get(tab[0]);
			}

			else{
				value=tab[1];
			}
			// bean.setCodescreen( tab[0]);

			map_screen.put(tab[0], value);
			//list_droit.add(bean);	     			
		}
		return map_screen;
	}

	public HashMap setUncheckedBox() {

		HashMap map_screen = new HashMap();
		//Set<String> setselected = selectedCheckBox.keySet( );
		Set<String> setselected = unselectedCheckBox.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		iterator=listselected.iterator();




		while (iterator.hasNext())
		{

			String tab[ ] = new String[2];
			String value="";
			int k=0;
			String cles=(String)iterator.next();
			Checkbox checkBox=unselectedCheckBox.get(cles);
			java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(cles, "|");

			while ( tokenizer.hasMoreTokens() ) {

				tab[k]=tokenizer.nextToken();
				k++;

			}
			if (map_screen.containsKey(tab[0])){
				value=tab[1]+"0"+"|"+ map_screen.get(tab[0]);
			}

			else{
				value=tab[1]+"0";
			}
			// bean.setCodescreen( tab[0]);

			map_screen.put(tab[0], value);
			//list_droit.add(bean);	     			
		}
		return map_screen;
	}

	/**
	 * @param map_droit
	 * @param tab
	 */
	public HashMap  getScreensrights( HashMap map_screen, Integer profile_id) {


		List list_query= new ArrayList<String>();
		Set set1 = (map_screen).entrySet(); 
		Iterator i1 = set1.iterator();
		String codescreen="";
		//String delete_profile="delete from droits where id_profile="+profile_id;
		HashMap map_query= new HashMap();

		String hide="0";
		String ecriture="0";
		String lecture="0";

		while(i1.hasNext()) {
			String  insert_profile="update droits set   hide=#hide, ecriture=#ecriture, lecture=#lecture  where id_profile=#id_profile and code_ecran=#code_ecran ";
			Map.Entry me1 = (Map.Entry)i1.next();
			codescreen=(String)me1.getKey();


			if (((String)me1.getValue()).contentEquals("hide")){
				hide="1";
			}

			if (((String)me1.getValue()).contentEquals("ecriture")){
				ecriture="1";
			}

			if (((String)me1.getValue()).contentEquals("lecture")){
				lecture="1";
			}

			if (((String)me1.getValue()).contentEquals("hide0")){
				hide="0";
			}

			if (((String)me1.getValue()).contentEquals("ecriture0")){
				ecriture="0";
			}

			if (((String)me1.getValue()).contentEquals("lecture0")){
				lecture="0";
			}
			insert_profile = insert_profile.replaceAll("#id_profile", "'"+profile_id+"'");
			insert_profile = insert_profile.replaceAll("#code_ecran", "'"+codescreen+"'");

			insert_profile = insert_profile.replaceAll("#ecriture", "'"+ecriture+"'");
			insert_profile = insert_profile.replaceAll("#lecture", "'"+lecture+"'");
			insert_profile = insert_profile.replaceAll("#hide", "'"+hide+"'");

			map_query.put(codescreen, insert_profile);

		}

		return map_query;
	}

}