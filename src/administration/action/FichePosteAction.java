package administration.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.zkoss.lang.Strings;
import org.zkoss.util.media.Media;
import org.zkoss.zk.au.out.AuClearWrongValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import common.Contantes;
import compagne.model.PlanningCompagneModel;
import administration.bean.AdministrationLoginBean;
import administration.bean.FichePosteBean;
import administration.bean.RepCompetenceBean;
import administration.bean.StructureEntrepriseBean;
import administration.model.AdministrationLoginModel;
import administration.model.FichePosteModel;
import administration.model.RepCompetenceModel;

public class FichePosteAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Listbox admincomptelb;
	Textbox code_poste;
	Textbox  intitule_poste;
	Listbox  formation_general;
	Textbox  formation_professionnelle;
	Textbox  experience;
	Textbox  profile_poste;
	Listbox code_poste_hierarchie;
	Listbox code_structure;
	Datebox date_maj_poste;
	Textbox sommaire_poste;
	Textbox  tache_responsabilite;
	Textbox  environement_perspectif;
	Listbox  is_cadre;
	Listbox  structure;


	Div divupdown;

	AnnotateDataBinder binder;
	List<FichePosteBean> model = new ArrayList<FichePosteBean>();
	FichePosteBean selected;
	List list_profile=null;
	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button upload;
	Button download;
	Button effacer;

	Map map_formation=null;
	Map map_poste= new HashMap();
	Map map_cadre=null;
	Map map_resRH=null;
	Map map_structure=null;
	Map map_compte=null;

	private String lbl_formation;
	private String lbl_poste;
	private String lbl_gsp;
	private String lbl_structure;


	public FichePosteAction() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);


		okAdd.setVisible(false);
		effacer.setVisible(false);
		code_poste.setDisabled(true);

		// création de la structure de l'entreprise bean
		FichePosteModel init =new FichePosteModel();

		map_formation=init.getListFormation();
		Set set = (map_formation).entrySet(); 
		Iterator i = set.iterator();

		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			formation_general.appendItem((String) me.getKey(),(String) me.getKey());
		}


		map_poste=init.getListPostes();
		set = (map_poste).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			code_poste_hierarchie.appendItem((String) me.getKey(),(String) me.getKey());
		}

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

		map_cadre=init.isCadre();
		set = (map_cadre).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			is_cadre.appendItem((String) me.getKey(),(String) me.getKey());
		}


		model=init.loadFichesPostes();
		binder = new AnnotateDataBinder(comp);


		//ces trois instructions permettent de selection le premier element de la listebox
		if(model.size()!=0)
			selected=model.get(0);

		if(admincomptelb.getItemCount()!=0)
			admincomptelb.setSelectedIndex(0);
		binder.loadAll();
	}

	public List<FichePosteBean> getModel() {
		return model;
	}



	public FichePosteBean getSelected() {
		return selected;
	}

	public void setSelected(FichePosteBean selected) {
		this.selected = selected;
	}

	public void onClick$add() throws WrongValueException, ParseException, SQLException {

		clearFields();
		FichePosteModel admini_model =new FichePosteModel();
		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);
		code_poste.setValue(admini_model.getNextCode("P",admini_model.getMaxKeyCode()));
		code_poste.setDisabled(true);

	}

	public void onClick$okAdd()throws WrongValueException, ParseException, InterruptedException {

		FichePosteBean addedData = new FichePosteBean();
		FichePosteModel admini_model =new FichePosteModel();
		String intit_poste=(getSelectedintitule_poste().replaceAll("/", "-")).replaceAll("\\\\", "-");


		addedData.setCode_poste(getSelectedcode_poste());
		addedData.setIntitule_poste(intit_poste);
		addedData.setFormation_general(getSelectedFormation());
		addedData.setFormation_professionnelle(getSelectedformation_professionnelle());
		addedData.setExperience(getSelectedexperience());
		addedData.setProfile_poste(getSelectedprofile_poste());
		addedData.setPoste_hierarchie(getSelectedcode_poste_hierarchie());
		addedData.setCode_structure(getSelectedcode_structure());
		addedData.setDate_maj_poste(getSelectdate_maj_poste());
		addedData.setSommaire_poste(getSelectedsommaire_poste());
		addedData.setTache_responsabilite(getSelectedtache_responsabilite());
		addedData.setEnvironement_perspectif(getSelectedenvironement_perspectif());
		addedData.setLibelle_formation(getSelectedFormation());
		addedData.setFormation_general(getLbl_formation());
		addedData.setLibelle_poste(getLbl_poste());
		addedData.setCode_gsp(getSelectIsCadre());
		addedData.setIs_cadre(getLbl_gsp());
		addedData.setLibelle_structure(getLbl_structure());


		//controle d'intégrité 

		//Boolean donneeValide=admini_model.controleIntegrite(addedData);
		Boolean donneeValide=true;

		if (donneeValide)
		{
			//insertion de la donnée ajoutée dans la base de donnée
			boolean donneeAjoute=admini_model.addPosteTravail(addedData);
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

	/**
	 * @throws WrongValueException
	 * @throws ParseException
	 * @throws InterruptedException
	 */
	public void onClick$update() throws WrongValueException, ParseException, InterruptedException {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		FichePosteBean addedData = new FichePosteBean();
		String intit_poste=(getSelectedintitule_poste().replaceAll("/", "-")).replaceAll("\\\\", "-");

		selected.setCode_poste(getSelectedcode_poste());
		selected.setIntitule_poste(intit_poste);
		selected.setFormation_general(getSelectedFormation());
		selected.setFormation_professionnelle(getSelectedformation_professionnelle());
		selected.setExperience(getSelectedexperience());
		selected.setProfile_poste(getSelectedprofile_poste());
		selected.setPoste_hierarchie(getSelectedcode_poste_hierarchie());
		selected.setCode_structure(getSelectedcode_structure());
		selected.setDate_maj_poste(getSelectdate_maj_poste());
		selected.setSommaire_poste(getSelectedsommaire_poste());
		selected.setTache_responsabilite(getSelectedtache_responsabilite());
		selected.setEnvironement_perspectif(getSelectedenvironement_perspectif());
		selected.setLibelle_formation(getLbl_formation());
		selected.setFormation_general(getLbl_formation());
		selected.setLibelle_poste(getLbl_poste());
				
		selected.setCode_gsp(getSelectIsCadre());
		selected.setIs_cadre(getLbl_gsp());
		selected.setLibelle_structure(getLbl_structure());


		//controle d'intégrité 
		FichePosteModel admin_model =new FichePosteModel();
		//Boolean donneeValide=admini_login_model.controleIntegrite(selected);
		Boolean donneeValide=true;
		if (donneeValide){

			if (Messagebox.show("Voulez vous appliquer les modifications?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				//System.out.println("pressyes");
				admin_model.majPosteTravail(selected);	
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
		FichePosteModel admin_model =new FichePosteModel();
		//suppression de la donnée supprimée de la base de donnée

		if (Messagebox.show("Voulez vous supprimer cette fiche de poste?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			//System.out.println("pressyes");
			selected.setCode_poste(getSelectedcode_poste());
			admin_model.supprimerFichePoste(selected);	model.remove(selected);
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
		admincomptelb.setSelectedIndex(0);
		binder.loadAll();


	}





	public void onSelect$admincomptelb() {
		closeErrorBox(new Component[] { code_poste, intitule_poste,formation_general,formation_professionnelle, 
				experience,profile_poste, code_poste_hierarchie, structure,date_maj_poste,sommaire_poste,
				tache_responsabilite,environement_perspectif,is_cadre});
	}


	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}


	private String getSelectedcode_poste() throws WrongValueException {
		String name =  code_poste.getValue();
		//affichable.getSelectedItem().getLabel();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(code_poste, "Le code poste ne doit pas être vide!");
		}
		return name;
	}

	private String getSelectedintitule_poste() throws WrongValueException {
		String name =  intitule_poste.getValue();
		//affichable.getSelectedItem().getLabel();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(intitule_poste, "Le intitulé du poste  ne doit pas être vide!");
		}
		return name;
	}

	private String getSelectedFormation() throws WrongValueException {

		String name=(String) map_formation.get((String)formation_general.getSelectedItem().getLabel());
		setLbl_formation((String)formation_general.getSelectedItem().getLabel());
		if (name== null) {
			throw new WrongValueException(formation_general, "Merci de saisir une formation!");
		}
		return name;
	}

	private String getSelectedformation_professionnelle() throws WrongValueException {
		String name =  formation_professionnelle.getValue();
		//affichable.getSelectedItem().getLabel();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(formation_professionnelle, "Le formation professionnelle ne doit pas être vide!");
		}
		return name;
	}

	private String getSelectedexperience() throws WrongValueException {
		String name =  experience.getValue();
		//affichable.getSelectedItem().getLabel();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(experience, "Le champ experience professionnelle ne doit pas être vide!");
		}
		return name;
	}

	private String getSelectedprofile_poste() throws WrongValueException {
		String name =  profile_poste.getValue();
		//affichable.getSelectedItem().getLabel();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(profile_poste, "Le champ profile poste ne doit pas être vide!");
		}
		return name;
	}

	private String getSelectedcode_poste_hierarchie() throws WrongValueException {

		String name;

		if (map_poste.size()==1){

			name=(String) map_poste.get((String)code_poste_hierarchie.getItemAtIndex(0).getLabel());
			setLbl_poste((String)code_poste_hierarchie.getItemAtIndex(0).getLabel());

		}else{
			 name=(String) map_poste.get((String)code_poste_hierarchie.getSelectedItem().getLabel());
			setLbl_poste((String)code_poste_hierarchie.getSelectedItem().getLabel());
			
		}
		
		if (name== null) {
			throw new WrongValueException(code_poste_hierarchie, "Merci de saisir un poste de travail hierarchique");
		}
		return name;
	}	

		
	private String getSelectedcode_structure() throws WrongValueException {
		String name =  (String) map_structure.get((String)structure.getSelectedItem().getLabel());
		//String name =  (String)structure.getSelectedItem().getValue();
		setLbl_structure((String)structure.getSelectedItem().getLabel());

		if (Strings.isBlank(name)) {
			throw new WrongValueException(structure, "Merci de saisir une structure!");
		}
		return name;
	}


	private Date getSelectdate_maj_poste() throws WrongValueException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String name = date_maj_poste.getText();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_maj_poste, "Merci de saisir la  date de mise à jour du poste!");
		}
		Date datedeb = df.parse(name);
		return datedeb;
	}

	private String getSelectedsommaire_poste() throws WrongValueException {
		String name = sommaire_poste.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(sommaire_poste, "Merci de saisir le sommaire du poste!");
		}
		return name;
	}

	private String getSelectedtache_responsabilite() throws WrongValueException {
		String name = tache_responsabilite.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(tache_responsabilite, "Merci de saisir les tâches et les résponsabilités liés au poste!");
		}
		return name;
	}


	private String getSelectedenvironement_perspectif() throws WrongValueException {
		String name = environement_perspectif.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(environement_perspectif, "Merci de saisir l'environement et les perspectifs d'évolution!");
		}
		return name;
	}

	private String getSelectIsCadre() throws WrongValueException {
		String name = (String) map_cadre.get((String)is_cadre.getSelectedItem().getLabel());
		setLbl_gsp((String)is_cadre.getSelectedItem().getLabel());

		if (Strings.isBlank(name)) {
			throw new WrongValueException(is_cadre, "Merci de preciser si l'employe est un evaluateur !");
		}
		return name;
	}





	public void onClick$upload() throws BiffException, InvalidFormatException, IOException {
		Executions.getCurrent().getDesktop().setAttribute("org.zkoss.zul.Fileupload.target", divupdown);

		try 
		{

			Fileupload fichierupload=new Fileupload();
			Media me=fichierupload.get("Merci de selectionner le fichier qui doit être chargé", "Chargement de fichier");

			//telechargerExcel(me);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	  public void onClick$download() {
			//chargement du contenu de la table Fiche_Poste et creation du fichier excel
			FichePosteModel fichePostemodel =new FichePosteModel();
			HSSFWorkbook workBook = new HSSFWorkbook();
			fichePostemodel.downloadFichePosteDataToXls(workBook);
			//enregistrement des données dans un fihcier xls
			FileOutputStream fOut;
			try 
			{
				fOut = new FileOutputStream(Contantes.nom_fichier_extraction_xls);
				workBook.write(fOut);
				fOut.flush();
				fOut.close();
				
				File file = new File(Contantes.nom_fichier_extraction_xls);
				Filedownload.save(file, "XLS");
			} 
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	public void telechargerExcel(Media med) throws BiffException, InvalidFormatException, IOException
	{

		//Media med=event.getMedia();

		if ((med != null)&&(med.getName()!=null)) 
		{
			String filename = med.getName();

			if ( filename.indexOf(".xls") == -1 ) 
			{
				alert(filename + " n'est pas un fichier excel");
			} 
			else 
			{

				// process the file...
				FichePosteModel ficheposteModel =new FichePosteModel();
				if ( filename.endsWith(".xls") ) 
				{
					//lecture et upload de fichiers OLE2 Office Documents 
					//InputStream ss=med.getStreamData();
					List<FichePosteBean> liste=ficheposteModel.uploadXLSFile(med.getStreamData());

					List<FichePosteBean> donneeRejetes;
					try 
					{
						HashMap <String,List<FichePosteBean>> listeDonnees=ficheposteModel.ChargementDonneedansBdd(liste);
						donneeRejetes =listeDonnees.get("supprimer");
						liste=null;
						liste=listeDonnees.get("inserer");;


						//raffrechissement de l'affichage
						Iterator<FichePosteBean> index=liste.iterator();
						while(index.hasNext())
						{
							FichePosteBean donnee=index.next();
							model.add(donnee);

						}

						binder.loadAll();
						if(donneeRejetes.size()!=0)
						{
							String listeRejet=new String("-->");
							//Afficharge de la liste des données rejetées
							Iterator<FichePosteBean> index1 =donneeRejetes.iterator();
							while(index1.hasNext())
							{


								FichePosteBean donnee=index1.next();
								String donneeString=donnee.getCode_poste()+";"+donnee.getIntitule_poste()
										+";"+donnee.getSommaire_poste()
										+";"+donnee.getTache_responsabilite()
										+";"+donnee.getEnvironement_perspectif()
										+";"+donnee.getFormation_general()
										+";"+donnee.getFormation_professionnelle()
										+";"+donnee.getExperience()
										+ ";"+donnee.getProfile_poste()
										+ ";"+donnee.getPoste_hierarchie()
										+ ";"+ donnee.getCode_structure()
										+ ";"+ donnee.getCode_poste()+","+ donnee.getIntitule_poste();

								//ajouter cadre ou non cadre a la fin 
								listeRejet=listeRejet+System.getProperty("line.separator")+donneeString;//saut de ligne

							}
							AfficherFenetreRejet(listeRejet);

						}
					} 
					catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
					if(filename.endsWith(".xlsx"))
					{

						// lecture de fichiers Office 2007+ XML
						InputStream ss=med.getStreamData();
						List<FichePosteBean> liste=ficheposteModel.uploadXLSXFile(ss);
						List<FichePosteBean> donneeRejetes;
						try 
						{
							HashMap <String,List<FichePosteBean>> listeDonnees=ficheposteModel.ChargementDonneedansBdd(liste);
							donneeRejetes =listeDonnees.get("supprimer");
							liste=null;
							liste=listeDonnees.get("inserer");;


							//raffrechissement de l'affichage
							Iterator<FichePosteBean> index=liste.iterator();
							while(index.hasNext())
							{
								FichePosteBean donnee=index.next();
								model.add(donnee);

							}

							binder.loadAll();
							if(donneeRejetes.size()!=0)
							{
								String listeRejet=new String("-->");
								//Afficharge de la liste des données rejetées
								Iterator<FichePosteBean> index1 =donneeRejetes.iterator();
								while(index1.hasNext())
								{
									FichePosteBean donnee=index1.next();
									String donneeString=donnee.getCode_poste()+";"+donnee.getIntitule_poste()
											+";"+donnee.getSommaire_poste()
											+";"+donnee.getTache_responsabilite()
											+";"+donnee.getEnvironement_perspectif()
											+";"+donnee.getFormation_general()
											+";"+donnee.getFormation_professionnelle()
											+";"+donnee.getExperience()
											+ ";"+donnee.getProfile_poste()
											+ ";"+donnee.getPoste_hierarchie()
											+ ";"+ donnee.getCode_structure()
											+ ";"+ donnee.getCode_poste()+","+ donnee.getIntitule_poste();

									//ajouter cadre ou non cadre a la fin 
									listeRejet=listeRejet+System.getProperty("line.separator")+donneeString;//saut de ligne

								}
								AfficherFenetreRejet(listeRejet);

							}
						} 
						catch (Exception e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

			} 				
		}
	}	


	public void AfficherFenetreRejet(String listeRejet)
	{
		Map<String, String> listDonne=new HashMap <String, String>();
		listDonne.put("rejet", listeRejet);



		final Window win = (Window) Executions.createComponents("../pages/REJDATA.zul", self, listDonne);

		win.setAttribute("popup", true);

		//decoratePopup(win);
		try 
		{
			win.doModal();

		} 
		catch (InterruptedException ex) 
		{
			ex.printStackTrace();
		} 
		catch (SuspendNotAllowedException ex) 
		{
			ex.printStackTrace();
		}
	}

	public void clearFields(){


		code_poste.setText("");
		intitule_poste.setText("");
		formation_professionnelle.setText("");
		experience.setText("");
		profile_poste.setText("");
		sommaire_poste.setText("");
		tache_responsabilite.setText("");
		environement_perspectif.setText("");
		formation_general.setSelectedIndex(0);
		//La premiere fois la liste box est vide
		//code_poste_hierarchie.setSelectedIndex(0);
		structure.setSelectedIndex(0);
		is_cadre.setSelectedIndex(0);


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




	public String getLbl_gsp() {
		return lbl_gsp;
	}

	public void setLbl_gsp(String lbl_gsp) {
		this.lbl_gsp = lbl_gsp;
	}

	/*public void onSelect$structure() throws SQLException, InterruptedException {
		FichePosteModel init =new FichePosteModel();
		Map map_sorted;
		String structure_st =  (String) map_structure.get((String)structure.getSelectedItem().getLabel());
		map_sorted= init.setlectedStructure(structure);
		Set set = (map_sorted).entrySet(); 
		Iterator i = set.iterator();

		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			structure.setTooltiptext((String) me.getKey());
		}


	} */

	public String getLbl_structure() {
		return lbl_structure;
	}

	public void setLbl_structure(String lbl_structure) {
		this.lbl_structure = lbl_structure;
	}

}