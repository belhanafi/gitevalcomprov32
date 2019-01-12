package compagne.action;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.tools.shell.commands.SetCommand;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;

import compagne.bean.GestionEmployesBean;
import compagne.bean.PlanningCompagneListBean;
import compagne.model.CompagneCreationModel;
import compagne.model.GestionEmployesModel;
import compagne.model.PlanningCompagneModel;
import compagne.model.SuiviCompagneModel;
import Statistique.model.EmployeModel;
import administration.bean.AdministrationLoginBean;
import administration.bean.CompagneCreationBean;
import administration.bean.StructureEntrepriseBean;
import administration.model.AdministrationLoginModel;
import administration.model.StructureEntrepriseModel;

public class GestionEmployesAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Listbox admincomptelb;

	Listbox id_compte;
	Textbox id_employe;
	Textbox nom;
	Textbox prenom;
	Datebox date_naissance;
	Datebox date_recrutement;
	Listbox formation;
	Listbox poste;
	Listbox est_evaluateur;
	Listbox est_responsable_rh;
	Listbox  structure;
	Textbox  email;
	Label structure_lbl;

	Textbox libelleFormationFiltre;
	Textbox compteUtilisateurFiltre;
	Textbox matriculeFiltre;
	Textbox intituleFiltre;

	Textbox login;
	Textbox libelle_direction;
	
	Textbox evolution_carriere;
	Textbox antecedent_disciplinaire;


	Listbox sexe;
	Listbox type_contrat;


	AnnotateDataBinder binder;
	List<GestionEmployesBean> model = new ArrayList<GestionEmployesBean>();
	GestionEmployesBean selected;
	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button upload;
	Button download;
	Button effacer;

	Map map_formation=null;
	Map map_poste= new HashMap();
	Map map_evaluateur=null;
	Map map_resRH=null;
	Map map_structure=null;
	Map map_compte=null;
	private String nom_complet;
	private String is_evaluateur;
	private String is_res_rh;
	private String lbl_formation;
	private String lbl_poste;
	private String nom_employe;
	private String prenom_employe;
	private String lbl_structure;

	Map map_sexe=null;
	Map map_type_contrat=null;
	HashMap<String, String> map_listfilter=new HashMap <String, String>();

	Combobox filtre1;
	private  Textbox filtre1_txtbox; 

	Combobox filtre2;
	private  Textbox filtre2_txtbox; 

	private String sexe_str;
	public String getSexe_str() {
		return sexe_str;
	}


	public GestionEmployesAction() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		okAdd.setVisible(false);
		effacer.setVisible(false);
		login.setDisabled(true);
		libelle_direction.setDisabled(true);
		GestionEmployesModel init= new GestionEmployesModel();
		map_formation = new HashMap();
		map_formation=init.getListFormation();
		Set set = (map_formation).entrySet(); 
		Iterator i = set.iterator();


		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			formation.appendItem((String) me.getKey(),(String) me.getKey());
		}

		map_poste=init.getListPostes();
		set = (map_poste).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			poste.appendItem((String) me.getKey(),(String) me.getKey());
		}

		map_compte=init.getCompteList();

		//set = (map_compte).entrySet();
		set = (map_compte).keySet();

		ArrayList<String> liste=new ArrayList<String>(set);
		Collections.sort(liste);
		i = liste.iterator();
		// Display elements
		while(i.hasNext()) {
			//Map.Entry me = (Map.Entry)i.next();
			String me=(String)i.next();
			id_compte.appendItem((String) me/*.getKey()*/,(String) me/*.getKey()*/);
		}

		map_evaluateur=init.isEvaluateur();
		set = (map_evaluateur).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			est_evaluateur.appendItem((String) me.getKey(),(String) me.getKey());
		}

		map_resRH=init.isResRH();
		set = (map_resRH).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			est_responsable_rh.appendItem((String) me.getKey(),(String) me.getKey());
		}

		map_sexe=init.sexeListe();
		set = (map_sexe).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			sexe.appendItem((String) me.getKey(),(String) me.getKey());
		}


		map_type_contrat=init.typeContrat();
		set = (map_type_contrat).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			type_contrat.appendItem((String) me.getKey(),(String) me.getKey());
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

		//remplissage de la combobox filtre1
		map_listfilter.put( "Direction","libelle_direction");
		map_listfilter.put("Matricule","c.login");
		map_listfilter.put( "Structure","structure_ent");
		map_listfilter.put( "Poste de travail","intitule_poste");
		map_listfilter.put( "Direction","libelle_direction");
		map_listfilter.put( "Contrat","type_contrat_lbl");
		map_listfilter.put("Date de recrutement","date_recrutement");
		map_listfilter.put( "Nom","c.nom");


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
			filtre2.setSelectedIndex(1);




		model=init.loadListEmployes();
		binder = new AnnotateDataBinder(comp);
		if(model.size()!=0)
			selected=model.get(0);

		if(admincomptelb.getItemCount()!=0)
			admincomptelb.setSelectedIndex(0);
		binder.loadAll();

	}

	public List<GestionEmployesBean> getModel() {
		return model;
	}



	public GestionEmployesBean getSelected() {
		return selected;
	}

	public void setSelected(GestionEmployesBean selected) {
		this.selected = selected;
	}

	public void onClick$add() throws WrongValueException, ParseException {

		clearFields();


		id_compte.setSelectedIndex(0);
		formation.setSelectedIndex(0);
		poste.setSelectedIndex(0);
		est_evaluateur.setSelectedIndex(0);
		est_responsable_rh.setSelectedIndex(0);
		structure.setSelectedIndex(0);

		sexe.setSelectedIndex(0);
		type_contrat.setSelectedIndex(0);


		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);

	}

	public void onClick$okAdd()throws WrongValueException, ParseException, InterruptedException {
		//id_employe     nom         prenom       date_naissance     rattach_dg     date_recrutement     code_formation     code_poste     email                     est_evaluateur     est_responsable_service     est_responsable_direction     est_responsable_division     est_responsable_departement     est_responsable_unite     est_responsable_section     est_responsable_rh     code_structure     id_compte    
		GestionEmployesBean addedData = new GestionEmployesBean();

		//addedData.setId_employe(getSelectedIdEmploye());
		addedData.setDate_naissance(getSelectDateNaissance());
		addedData.setDate_recrutement(getSelectDateRecrutement());
		addedData.setCode_formation(getSelectedFormation());
		addedData.setCode_poste(getSelectPosteTravail());
		addedData.setEmail(getSelectedEmail());
		addedData.setCode_est_evaluateur(getSelectIsEvaluateur());
		addedData.setEst_evaluateur(getIs_evaluateur());
		addedData.setCode_est_responsable_rh(getSelectIsResRH());
		addedData.setEst_responsable_rh(getIs_res_rh());
		addedData.setId_compte(getIdcompte());
		addedData.setCode_structure(getSelectStructure());
		addedData.setNom_complet(getNom_complet());
		addedData.setIntitule_poste(getLbl_poste());
		addedData.setLibelle_formation(getLbl_formation());
		addedData.setNom(getNom_employe());
		addedData.setPrenom(getPrenom_employe());
		addedData.setLibelle_structure(getLbl_structure());

		addedData.setCode_sexe(getSelectSexe());
		addedData.setSexe(getSexe_str());

		addedData.setCode_type_contrat(getSelectTypeContrat());
		addedData.setType_contrat(getTypecontrat_str());
		addedData.setLogin(getSelectedLogin());
		addedData.setLibelle_direction(getSelectedLibelleDirection());
		
		addedData.setAntecedent_disciplinaire(getSelectedAntecedent_disciplinaire());
		addedData.setEvolution_carriere(getSelectedEvolution_carriere());




		//controle d'intégrité 
		GestionEmployesModel compagne_model =new GestionEmployesModel();
		//compagne_model.addPlanningCompagne(addedData);
		Boolean donneeValide=compagne_model.controleIntegrite(addedData);
		//Boolean donneeValide=true;

		if (donneeValide)
		{
			//insertion de la donnée ajoutée dans la base de donnée
			boolean donneeAjoute=compagne_model.addEmploye(addedData);
			// raffrechissemet de l'affichage
			if (donneeAjoute )
			{
				model.add(addedData);
				selected = addedData;
				binder.loadAll();

			}
		}

		//fillEvalueListBox();

		okAdd.setVisible(false);
		effacer.setVisible(false);
		add.setVisible(true);
		update.setVisible(true);
		delete.setVisible(true);


	}

	/**
	 * 
	 */

	/*
	public void fillEvalueListBox() {
		evalue.getItems().clear();
	    Set set = (map_evalue).entrySet(); 
	  	Iterator i = set.iterator();
		while(i.hasNext()) {
		  Map.Entry me = (Map.Entry)i.next();
		  evalue.appendItem((String) me.getKey(),(String) me.getKey());
		 }
	}*/



	public void onClick$update() throws WrongValueException, ParseException, InterruptedException {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}

		GestionEmployesBean addedData = new GestionEmployesBean();

		selected.setId_employe(getSelectedIdEmploye());
		selected.setDate_naissance(getSelectDateNaissance());
		selected.setDate_recrutement(getSelectDateRecrutement());
		selected.setCode_formation(getSelectedFormation());
		selected.setCode_poste(getSelectPosteTravail());
		selected.setEmail(getSelectedEmail());
		selected.setCode_est_evaluateur(getSelectIsEvaluateur());
		selected.setEst_evaluateur(getIs_evaluateur());

		selected.setCode_est_responsable_rh(getSelectIsResRH());
		selected.setEst_responsable_rh(getIs_res_rh());

		selected.setId_compte(getIdcompte());
		selected.setCode_structure(getSelectStructure());
		//selected.setCode_structure("S0000");
		selected.setNom_complet(getNom_complet());
		selected.setIntitule_poste(getLbl_poste());
		selected.setLibelle_formation(getLbl_formation());
		selected.setNom(getNom_employe());
		selected.setPrenom(getPrenom_employe());
		selected.setLibelle_structure(getLbl_structure());

		selected.setCode_sexe(getSelectSexe());
		selected.setSexe(getSexe_str());

		selected.setCode_type_contrat(getSelectTypeContrat());
		selected.setType_contrat(getTypecontrat_str());
		
		selected.setAntecedent_disciplinaire(getSelectedAntecedent_disciplinaire());
		selected.setEvolution_carriere(getSelectedEvolution_carriere());







		//controle d'intégrité 
		GestionEmployesModel compagne_model =new GestionEmployesModel();
		//controle d'intégrité 
		Boolean donneeValide=compagne_model.controleIntegrite(selected);
		if (donneeValide)
		{
			//insertion de la donnée ajoutée dans la base de donnée

			if (Messagebox.show("Voulez vous appliquer les modifications?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				//System.out.println("pressyes");
				compagne_model.updateListeEmploye(selected);
				binder.loadAll();
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
		GestionEmployesModel compagne_model =new GestionEmployesModel();
		//suppression de la donnée supprimée de la base de donnée
		selected.setId_employe(getSelectedIdEmploye()); 

		if (Messagebox.show("Voulez vous supprimer cet employé ?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			//System.out.println("pressyes");
			compagne_model.deleteEmploye(selected);
			model.remove(selected);
			binder.loadAll();
			//selected=model.get(model.size()-1);
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


	public void onSelect$admincomptelb() {
		closeErrorBox(new Component[] { id_compte,id_employe,sexe,type_contrat, date_naissance,date_recrutement,formation,
				poste,est_evaluateur,est_responsable_rh,structure,email});
	}


	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}

	private int getIdcompte() throws WrongValueException {

		GestionEmployesBean bean= new GestionEmployesBean();

		Integer name=(Integer) map_compte.get(id_compte.getSelectedItem().getLabel());
		setNom_complet(id_compte.getSelectedItem().getLabel());
		String []chaine=id_compte.getSelectedItem().getLabel().split("-");
		setNom_employe(chaine[0].trim());
		setPrenom_employe(chaine[1].trim());

		if (name==null) {
			throw new WrongValueException(id_compte, "Merci de saisir le compte de l'employe!");
		}
		return name;
	}

	private int getSelectedIdEmploye() throws WrongValueException {

		Integer name= Integer.parseInt(id_employe.getValue());

		if (name== null) {
			throw new WrongValueException(id_employe, "Merci de saisie id employe!");
		}
		return name;
	}

	private String getSelectedNom() throws WrongValueException {
		String name=nom.getValue();
		if (name==null) {
			throw new WrongValueException(nom, "Merci de saisir le nom de l'employe!");
		}
		return name;
	}

	private String getSelectedPrenom() throws WrongValueException {
		String name=prenom.getValue();
		if (name==null) {
			throw new WrongValueException(prenom, "Merci de saisir le prenom de l'employe!");
		}
		return name;
	}


	private Date getSelectDateNaissance() throws WrongValueException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String name = date_naissance.getText();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_naissance, "Merci de saisir la  date de naissance !");
		}
		Date datedeb = df.parse(name);
		return datedeb;
	}

	private Date getSelectDateRecrutement() throws WrongValueException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String name = date_recrutement.getText();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_recrutement, "Merci de saisir la  date de recrutement !");
		}
		Date datedeb = df.parse(name);
		return datedeb;
	}


	private String getSelectedFormation() throws WrongValueException {

		String name=(String) map_formation.get((String)formation.getSelectedItem().getLabel());
		setLbl_formation((String)formation.getSelectedItem().getLabel());
		if (name== null) {
			throw new WrongValueException(formation, "Merci de saisir une formation!");
		}
		return name;
	}

	private String getSelectPosteTravail() throws WrongValueException {
		String name = (String) map_poste.get((String)poste.getSelectedItem().getLabel());
		setLbl_poste((String) poste.getSelectedItem().getLabel());

		if (Strings.isBlank(name)) {
			throw new WrongValueException(poste, "Merci de saisir un poste de travail!");
		}
		return name;
	}


	private String getSelectIsEvaluateur() throws WrongValueException {
		String name = (String) map_evaluateur.get((String)est_evaluateur.getSelectedItem().getLabel());
		setIs_evaluateur((String)est_evaluateur.getSelectedItem().getLabel());

		if (Strings.isBlank(name)) {
			throw new WrongValueException(est_evaluateur, "Merci de preciser si l'employe est un evaluateur !");
		}
		return name;
	}


	private String getSelectIsResRH() throws WrongValueException {
		String name = (String) map_resRH.get((String)est_responsable_rh.getSelectedItem().getLabel());
		setIs_res_rh((String)est_responsable_rh.getSelectedItem().getLabel());

		if (Strings.isBlank(name)) {
			throw new WrongValueException(est_responsable_rh, "Merci de preciser si l'employe est un responsable des RH !");
		}
		return name;
	}


	private String getSelectStructure() throws WrongValueException {
		String name =  (String) map_structure.get((String)structure.getSelectedItem().getLabel());
		//String name =  (String)structure.getSelectedItem().getValue();
		setLbl_structure((String)structure.getSelectedItem().getLabel());

		if (Strings.isBlank(name)) {
			throw new WrongValueException(structure, "Merci de saisir une structure!");
		}
		return name;
	}

	private String getSelectSexe() throws WrongValueException {
		String name = (String) map_sexe.get((String)sexe.getSelectedItem().getLabel());
		setSexe_str((String)sexe.getSelectedItem().getLabel());

		if (Strings.isBlank(name)) {
			throw new WrongValueException(sexe, "Merci de preciser le sexe de l'employe  !");
		}
		return name;
	}

	private String getSelectTypeContrat() throws WrongValueException {
		String name = (String) map_type_contrat.get((String)type_contrat.getSelectedItem().getLabel());
		setTypecontrat_str((String)type_contrat.getSelectedItem().getLabel());

		if (Strings.isBlank(name)) {
			throw new WrongValueException(type_contrat, "Merci de preciser le type de contrat de l'employe  !");
		}
		return name;
	}

	private String getSelectedEmail() throws WrongValueException {
		String name=email.getValue();
		if (name==null) {
			throw new WrongValueException(email, "Merci de saisir une adresse mail valide!");
		}
		return name;
	}

	private String getSelectedLogin() throws WrongValueException {
		String name=login.getValue();
		if (name==null) {
			throw new WrongValueException(login, "Merci de saisir un  matricule valide!");
		}
		return name;
	}

	private String getSelectedEvolution_carriere() throws WrongValueException {
		String name=evolution_carriere.getValue();
		if (name==null) {
			throw new WrongValueException(libelle_direction, "Merci de saisir Libelle direction valide!");
		}
		return name;
	}
	
	private String getSelectedAntecedent_disciplinaire() throws WrongValueException {
		String name=antecedent_disciplinaire.getValue();
		if (name==null) {
			throw new WrongValueException(libelle_direction, "Merci de saisir Libelle direction valide!");
		}
		return name;
	}
	

	private String getSelectedLibelleDirection() throws WrongValueException {
		String name=libelle_direction.getValue();
		if (name==null) {
			throw new WrongValueException(libelle_direction, "Merci de saisir Libelle direction valide!");
		}
		return name;
	}



	public void clearFields(){

		email.setText("");
		login.setText("");
		libelle_direction.setText("");
	}

	public String getNom_complet() {
		return nom_complet;
	}

	public void setNom_complet(String nom_complet) {
		this.nom_complet = nom_complet;
	}

	public String getIs_evaluateur() {
		return is_evaluateur;
	}

	public void setIs_evaluateur(String is_evaluateur) {
		this.is_evaluateur = is_evaluateur;
	}

	public String getIs_res_rh() {
		return is_res_rh;
	}

	public void setIs_res_rh(String is_res_rh) {
		this.is_res_rh = is_res_rh;
	}

	public String getLbl_formation() {
		return lbl_formation;
	}

	public void setLbl_formation(String lbl_formation) {
		this.lbl_formation = lbl_formation;
	}

	public String getLbl_poste() {
		return lbl_poste;
	}

	public void setLbl_poste(String lbl_poste) {
		this.lbl_poste = lbl_poste;
	}

	public String getNom_employe() {
		return nom_employe;
	}

	public void setNom_employe(String nom_employe) {
		this.nom_employe = nom_employe;
	}

	public String getPrenom_employe() {
		return prenom_employe;
	}

	public void setPrenom_employe(String prenom_employe) {
		this.prenom_employe = prenom_employe;
	}



	/*public void onSelect$structure() throws SQLException, InterruptedException {
		GestionEmployesModel init =new GestionEmployesModel();
		Map map_sorted;
		//String code_structure =  (String) map_structure.get((String)structure.getSelectedItem().getValue());
		String code_structure=(String) structure.getSelectedItem().getValue();

		map_sorted= init.setlectedStructure(code_structure);
		Set set = (map_sorted).entrySet(); 
		Iterator i = set.iterator();

		// Display elements
		/*while(i.hasNext()) {
		  Map.Entry me = (Map.Entry)i.next();
		  structure_lbl.setValue((String) me.getKey());
		  structure.setTooltiptext((String) me.getKey());
		 }*/


	//}



	public void onClick$buttonReinitialiser() throws InterruptedException, SQLException {

		GestionEmployesModel gestionEmployeModel =new GestionEmployesModel();
		//recherche des elements selectionnées

		filtre1_txtbox.setValue("");  
		filtre2_txtbox.setValue("");

		//System.out.println("pressyes");
		model=gestionEmployeModel.loadListEmployes();
		//model.remove(selected);
		selected = null;
		binder.loadAll();



	}

	/*public void onClick$buttonRechercher() throws InterruptedException, SQLException {

		GestionEmployesModel gestionEmployeModel =new GestionEmployesModel();
		//recherche des elements selectionnées
		//String filtrelibelleFormation=libelleFormationFiltre.getValue();
		String filreCompteUtilisateur=compteUtilisateurFiltre.getValue();  
		//String filtreIntitule=intituleFiltre.getValue();  

		String filtrelogin=matriculeFiltre.getValue();


		//System.out.println("pressyes");
		model=gestionEmployeModel.filtreEmployes(filreCompteUtilisateur,filtrelogin);

		selected = null;
		binder.loadAll();



	}*/


	public String getLbl_structure() {
		return lbl_structure;
	}

	public void setLbl_structure(String lbl_structure) {
		this.lbl_structure = lbl_structure;
	}

	public void setSexe_str(String sexe_str) {
		this.sexe_str = sexe_str;
	}

	public String getTypecontrat_str() {
		return typecontrat_str;
	}

	public void setTypecontrat_str(String typecontrat_str) {
		this.typecontrat_str = typecontrat_str;
	}

	private String typecontrat_str;


	public void onSelect$id_compte() throws WrongValueException, SQLException {

		GestionEmployesModel gestionEmployeModel =new GestionEmployesModel();
		int id_compte_int=(Integer)map_compte.get(id_compte.getSelectedItem().getLabel());
		login.setText(gestionEmployeModel.getLogin(id_compte_int));
		//System.out.println("id_compte_int>>>"+id_compte_int);
	}

	public void onSelect$structure() throws WrongValueException, SQLException {

		GestionEmployesModel gestionEmployeModel =new GestionEmployesModel();
		String codestructure=(String)map_structure.get(structure.getSelectedItem().getLabel());
		libelle_direction.setText(gestionEmployeModel.getDirection(codestructure));
		//System.out.println("id_compte_int>>>"+id_compte_int);
	}
	 public void onClick$buttonRechercher() throws InterruptedException, SQLException, ParseException {

		 GestionEmployesModel gestionEmployeModel =new GestionEmployesModel();
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

			model=gestionEmployeModel.filtreEmployes(condition1, valeur_condition1, condition2,  valeur_condition2);

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