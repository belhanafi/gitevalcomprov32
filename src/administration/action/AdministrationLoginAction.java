package administration.action;

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
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import administration.bean.AdministrationLoginBean;
import administration.bean.StructureEntrepriseBean;
import administration.model.AdministrationLoginModel;
import administration.model.StructureEntrepriseModel;

public class AdministrationLoginAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Listbox admincomptelb;
	Textbox id_compte;
	Textbox  nom;
	Textbox  prenom;
	Textbox  login;
	Textbox  motdepasse;
	Listbox  profile;
	Listbox  basedonnee;
	Datebox date_deb_val;
	Datebox date_fin_val;
	Textbox datemodifpwd;
	private  Textbox nomFiltre; 
	private  Textbox prenomFiltre;  
	private  Textbox profilFiltre;  
	private  Textbox bddFiltre;  
	AnnotateDataBinder binder;
	List<AdministrationLoginBean> model = new ArrayList<AdministrationLoginBean>();
	AdministrationLoginBean selected;
	AdministrationLoginBean selected_bckp;

	List list_profile=null;
	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button upload;
	Button download;
	Button effacer;
	List<String> profilemodel=new ArrayList<String>();
	List<String> basedonneemodel=new ArrayList<String>();
	HashMap<String, String> map_listfilter=new HashMap <String, String>();
	Component comp1;
	Combobox filtre1;
	private  Textbox filtre1_txtbox; 

	Combobox filtre2;
	private  Textbox filtre2_txtbox; 




	public AdministrationLoginAction() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		okAdd.setVisible(false);
		effacer.setVisible(false);
		AdministrationLoginModel init= new AdministrationLoginModel();


		Set set = (init.gerProfileList()).entrySet(); 
		Set sec= (init.getDatabaseList()).entrySet();

		Iterator i = set.iterator();
		Iterator i1 = sec.iterator();

		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			profile.appendItem((String) me.getKey(),(String) me.getKey());
			//profilemodel.add((String) me.getKey());
		}
		// Display elements
		while(i1.hasNext()) {
			Map.Entry me = (Map.Entry)i1.next();
			basedonnee.appendItem((String) me.getKey(),(String) me.getKey());
			//basedonneemodel.add((String) me.getKey());
		}

		//remplissage de la combobox filtre1
		map_listfilter.put( "Matricule","c.login");
		map_listfilter.put( "Nom","nom");
		map_listfilter.put( "Division","nom_base");
		map_listfilter.put( "Date fin validité","c.val_date_fin");
		map_listfilter.put( "Profil","libelle_profile");


		Set set_filter = map_listfilter.entrySet(); 
		Iterator itr = set_filter.iterator();

		// Affichage de la liste des compagnes
		while(itr.hasNext()) {
			Map.Entry me = (Map.Entry)itr.next();

			filtre1.appendItem(String.valueOf(me.getKey()));
			filtre2.appendItem(String.valueOf(me.getKey()));


		}

		// forcer la selection de la permiere ligne
		if(filtre1.getItemCount()>0)
			filtre1.setSelectedIndex(0);

		if(filtre2.getItemCount()>0)
			filtre2.setSelectedIndex(0);

		/*map_listfilter.put("libelle_direction", "Direction");
		map_listfilter.put("login", "Matricule");
		map_listfilter.put("structure_ent", "Structure");
		map_listfilter.put("intitule_poste", "Poste de travail");
		map_listfilter.put("libelle_direction", "Direction");
		map_listfilter.put("code_contrat", "Contrat");
		map_listfilter.put("date_recrutement", "Date de recrutement");*/





		// création de la structure de l'entreprise bean
		//AdministrationLoginModel admin_compte =new AdministrationLoginModel();
		model=init.checkLoginBean();
		//login.setDisabled(true);


		binder = new AnnotateDataBinder(comp);
		if(model.size()!=0)
			selected=model.get(0);

		if(admincomptelb.getItemCount()!=0)
			admincomptelb.setSelectedIndex(0);
		binder.loadAll();
		comp1=comp;



	}

	public List<AdministrationLoginBean> getModel() {
		return model;
	}



	public AdministrationLoginBean getSelected() {
		return selected;
	}

	public void setSelected(AdministrationLoginBean selected) {
		this.selected = selected;
	}

	public void onClick$add() throws WrongValueException, ParseException {

		clearFields();
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String datecuurent = sdf.format(new Date());*/
		profile.setSelectedIndex(0);
		basedonnee.setSelectedIndex(0);

		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);
		login.setDisabled(false);

	}

	public void onClick$okAdd()throws WrongValueException, ParseException, InterruptedException {

		AdministrationLoginBean addedData = new AdministrationLoginBean();

		addedData.setNom(getSelectedNom());
		addedData.setPrenom(getSelectedPrenom());
		addedData.setLogin(getSelectedLogin());
		addedData.setMotdepasse(getSelectedcodemotdepasse());
		addedData.setProfile(getSelectedprofile());
		addedData.setBasedonnee(getSelectedbasedonnee());
		addedData.setDate_deb_val( getSelecteddate_deb_val());
		addedData.setDate_fin_val( getSelecteddate_fin_val());

		//controle d'intégrité 
		AdministrationLoginModel admini_login_model =new AdministrationLoginModel();
		Boolean donneeValide=admini_login_model.controleIntegrite(addedData);
		//Boolean donneeValide=true;

		//verifier si le login existe déja
		Boolean loginExists=false;
		try {

			loginExists=admini_login_model.isLoginExists(addedData.getLogin(),addedData, "A");



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!loginExists)
		{
			//afficher un message
			Messagebox.show("Ce login existe deja, merci de choisir un autre login/matricule", "Erreur", Messagebox.OK, Messagebox.ERROR);
			//System.out.println("pressyes");
			try {

				okAdd.setVisible(false);
				effacer.setVisible(false);
				add.setVisible(true);
				update.setVisible(true);
				delete.setVisible(true);
				model=admini_login_model.checkLoginBean();
				selected = null;
				binder.loadAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//model.remove(selected);


			return;

		}
		else
		{	
			if (donneeValide)
			{
				//insertion de la donnée ajoutée dans la base de donnée
				boolean donneeAjoute=admini_login_model.addAdministrationLoginBean(addedData);
				// raffrechissemet de l'affichage
				if (donneeAjoute )
				{
					model.add(addedData);

					selected = addedData;

					binder.loadAll();
				}
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
		AdministrationLoginModel admini_login_model =new AdministrationLoginModel();


		//String codeStructureselectione=selected.getCodestructure();
		//System.out.println(getSelectedcodeStructure());
		selected.setId_compte(getSelectedIdCompte());
		selected.setNom(getSelectedNom());
		selected.setPrenom(getSelectedPrenom());
		selected.setLogin(getSelectedLogin());
		selected.setMotdepasse(getSelectedcodemotdepasse());
		selected.setProfile(getSelectedprofile());
		selected.setBasedonnee(getSelectedbasedonnee());
		selected.setDate_deb_val( getSelecteddate_deb_val());
		selected.setDate_fin_val( getSelecteddate_fin_val());

		//controle d'intégrité 
		//AdministrationLoginModel admini_login_model =new AdministrationLoginModel();

		//verifier si le login existe déja
		Boolean loginExists=false;
		try {

			loginExists=admini_login_model.isLoginExists(selected.getLogin(),selected,"M");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(!loginExists)
		{
			//afficher un message
			Messagebox.show("Ce login existe deja, merci de choisir un autre login/matricule", "Erreur", Messagebox.OK, Messagebox.ERROR);
			try {
				model=admini_login_model.checkLoginBean();
				selected = null;
				binder.loadAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return;



		}
		else
		{
			Boolean donneeValide=admini_login_model.controleIntegrite(selected);
			if (donneeValide)
			{
				//insertion de la donnée ajoutée dans la base de donnée

				if (Messagebox.show("Voulez vous appliquer les modifications?", "Prompt", Messagebox.YES|Messagebox.NO,
						Messagebox.QUESTION) == Messagebox.YES) {
					//System.out.println("pressyes");
					admini_login_model.majAdminLoginBean(selected);	binder.loadAll();
					return;
				}

				else{
					return;
				}
			}	
		}


	}

	public void onClick$delete() throws InterruptedException {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		AdministrationLoginModel admini_login_model =new AdministrationLoginModel();
		//suppression de la donnée supprimée de la base de donnée

		if (Messagebox.show("Voulez vous supprimer cet utilisateur?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			//System.out.println("pressyes");
			admini_login_model.supprimerLogin(selected);
			model.remove(selected);
			selected = null;
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


	}

	public void onClick$upload() {
		//excel vers BDD
		//chargement du fichier excel contenant les données
		//partie affichage

		//partie base de données
	}

	public void onClick$download() {
		//BDD vers excel

		// partie affichage

		//partie base de données
	}

	public void onSelect$admincomptelb() {

		closeErrorBox(new Component[] { nom, prenom,login,motdepasse,  profile, 
				basedonnee,date_deb_val, date_fin_val, datemodifpwd });
	}


	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}


	private String getSelectedIdCompte() throws WrongValueException {
		String name = id_compte.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nom, "id compte ne doit pas être vide!");
		}
		return name;
	}



	private String getSelectedNom() throws WrongValueException {
		String name = nom.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nom, "le nom de l'utilisateur ne doit pas être vide!");
		}
		return name;
	}

	private String getSelectedPrenom() throws WrongValueException {
		String name = prenom.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(prenom, "le prénom de l'utilisateur ne doit pas être vide!");
		}
		return name;
	}

	private String getSelectedLogin() throws WrongValueException {
		String name = login.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(login, "le login ne doit pas être vide!");
		}
		return name;
	}

	private String getSelectedcodemotdepasse() throws WrongValueException {
		String name = motdepasse.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(motdepasse, "le mot de passe ne doit pas être vide!");
		}
		return name;
	}

	private String getSelectedprofile() throws WrongValueException {
		String name = profile.getSelectedItem().getLabel();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(profile, "le profile  ne doit pas être vide!");
		}
		return name;
	}

	private String getSelectedbasedonnee() throws WrongValueException {
		String name = basedonnee.getSelectedItem().getLabel();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(basedonnee, "la basedonnee ne doit pas être vide!");
		}
		return name;
	}

	private Date getSelecteddate_deb_val() throws WrongValueException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String name = date_deb_val.getText();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_deb_val, "la date debut validité ne doit pas être vide!");
		}
		Date datedeb = df.parse(name); 
		return datedeb;
	}

	private Date getSelecteddate_fin_val() throws WrongValueException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String name = date_fin_val.getText();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_fin_val, "la date fin validité ne doit pas être vide!");
		}
		Date datefin = df.parse(name); 
		return datefin;
	}


	public void clearFields(){
		nom.setText("");
		prenom.setText("");
		login.setText("");
		motdepasse.setText("");
		//profile.setItemRenderer("");
		//basedonnee.setText("");
		datemodifpwd.setText("");

	}

	public void onClick$buttonReinitialiser() throws InterruptedException, SQLException {

		AdministrationLoginModel admini_login_model =new AdministrationLoginModel();
		//recherche des elements selectionnées

		filtre1_txtbox.setValue("");  
		filtre2_txtbox.setValue("");

		//System.out.println("pressyes");
		model=admini_login_model.checkLoginBean();
		//model.remove(selected);
		selected = null;
		binder.loadAll();



	}

	public void onClick$buttonRechercher() throws InterruptedException, SQLException {

		AdministrationLoginModel admini_login_model =new AdministrationLoginModel();
		//recherche des elements selectionnées

		String valeur_condition1=filtre1_txtbox.getText();
		String valeur_condition2=filtre2_txtbox.getText();

		if (valeur_condition1.length()==0 && valeur_condition2.length()==0){
			Messagebox.show("Merci de saisir au moins un critère de recherche !", "Erreur", Messagebox.OK, Messagebox.ERROR);
			return;

		}


		String condition1=map_listfilter.get(filtre1.getValue());
		String condition2=map_listfilter.get(filtre2.getValue());

		if (condition1.equalsIgnoreCase(condition2)){

			Messagebox.show("Merci de saisir  des critères de recherche différents!", "Erreur", Messagebox.OK, Messagebox.ERROR);
			return;

		}


		if (condition1.equalsIgnoreCase("c.val_date_fin") && valeur_condition1.length()>0){

			if (!isValidDate(valeur_condition1)){
				Messagebox.show("Merci de saisir  une data de validité au format JJ/MM/AAAA !", "Erreur", Messagebox.OK, Messagebox.ERROR);
				return;
			}

		}

		if (condition2.equalsIgnoreCase("c.val_date_fin")&& valeur_condition2.length()>0 ){

			if (!isValidDate(valeur_condition2)){
				Messagebox.show("Merci de saisir  une data de validité au format JJ/MM/AAAA !", "Erreur", Messagebox.OK, Messagebox.ERROR);
				return;
			}

		}



		//System.out.println("pressyes");
		model=admini_login_model.filtreLoginBean(condition1, valeur_condition1, condition2,  valeur_condition2);

		selected = null;
		binder.loadAll();
	}

	public static boolean isValidDate(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public void onSelect$filtre1() throws SQLException {
		
		filtre1_txtbox.setText("");
		
	}
    public void onSelect$filtre2() throws SQLException {
    	filtre2_txtbox.setText("");
		
	}


}