package administration.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import common.Contantes;
import compagne.bean.GestionEmployesBean;
import compagne.model.GestionEmployesModel;
import administration.bean.Compagne;
import administration.bean.EmployeCompteBean;
import administration.bean.FichePlanningBean;
import administration.bean.FichePosteBean;
import administration.bean.PosteVsAptitudeBean;
import administration.bean.StructureEntrepriseBean;
import administration.model.CompagneModel;
import administration.model.CompetencePosteTravailModel;
import administration.model.EmployeCompteModel;
import administration.model.FichePlanningModel;
import administration.model.FichePosteModel;
import administration.model.StructureEntrepriseModel;



public class ChargementMasseAction extends GenericForwardComposer {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AnnotateDataBinder binder;
	Window win;
	List<StructureEntrepriseBean>savemodel = new ArrayList<StructureEntrepriseBean>();
	List<StructureEntrepriseBean> model = new ArrayList<StructureEntrepriseBean>();
	List<StructureEntrepriseBean> Addedmodel = new ArrayList<StructureEntrepriseBean>();
	List<FichePosteBean> savemodel2 = new ArrayList<FichePosteBean>();
	List<FichePosteBean> model2 = new ArrayList<FichePosteBean>();
	List<FichePosteBean> Addedmodel2 = new ArrayList<FichePosteBean>();
	List<EmployeCompteBean> savemodel3 = new ArrayList<EmployeCompteBean>();
	List<EmployeCompteBean> model3 = new ArrayList<EmployeCompteBean>();
	List<EmployeCompteBean> Addedmodel3 = new ArrayList<EmployeCompteBean>();
	List<FichePlanningBean> savemodel4 = new ArrayList<FichePlanningBean>();
	List<FichePlanningBean> Addedmodel4 = new ArrayList<FichePlanningBean>();
	List<FichePlanningBean> model4 = new ArrayList<FichePlanningBean>();
	List<Compagne> savemodel5 = new ArrayList<Compagne>();
	List<Compagne> Addedmodel5 = new ArrayList<Compagne>();
	List<Compagne> model5 = new ArrayList<Compagne>();
	
	List<PosteVsAptitudeBean> savemodel6 = new ArrayList<PosteVsAptitudeBean>();
	List<PosteVsAptitudeBean> Addedmodel6 = new ArrayList<PosteVsAptitudeBean>();
	List<PosteVsAptitudeBean> model6 = new ArrayList<PosteVsAptitudeBean>();


	Button okAdd;
	Button annuler;
	Div divupdown;

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		// création des données 
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();

		FichePosteModel init =new FichePosteModel();
		EmployeCompteModel init3= new EmployeCompteModel();
		FichePlanningModel init4= new FichePlanningModel();
		CompagneModel init5=new CompagneModel();
		CompetencePosteTravailModel init6=new CompetencePosteTravailModel();


		model=structureEntrepriseModel.checkStructureEntreprise();

		model2=init.loadFichesPostes();
		model3=init3.loadListEmployes();
		model4=init4.loadListPlanning();
		model5=init5.getAllCompagnes();
		model6=init6.loadPosteVsAptitude();

		HashMap<String,StructureEntrepriseBean> mapStructureEntreprise= init.getStructureEntreprise();


		Iterator<FichePosteBean> iterator=model2.iterator();
		while(iterator.hasNext())
		{
			FichePosteBean ficheBean=iterator.next();
			String codeStructure=ficheBean.getCode_structure();
			StructureEntrepriseBean structure=mapStructureEntreprise.get(codeStructure);
			if(structure!=null)
			{
				String libell_division=structure.getLibelleDivision();
				String libelleDirection=structure.getLibelleDirection();
				String libelleUnite=structure.getLibelleUnite();
				String libelleDepartement=structure.getLibelleDepartement();
				String libelleService=structure.getLibelleService();
				String libelleSection=structure.getLibelleSection();
				if(libell_division==null) libell_division="";
				if(libelleDirection==null)libelleDirection="";
				if(libelleUnite==null)libelleUnite="";
				if(libelleDepartement==null)libelleDepartement="";
				if(libelleService==null)libelleService="";
				if(libelleSection==null)libelleSection="";
				String valeurCode_Structure=codeStructure+","+libell_division+","+libelleDirection+","+libelleUnite+","+libelleDepartement+","+ libelleService+","+libelleSection;
				ficheBean.setCodeStructLibelle(valeurCode_Structure);
			}
		}

		Iterator<EmployeCompteBean> iterator2=model3.iterator();
		while(iterator2.hasNext())
		{
			EmployeCompteBean employeCompteBean=iterator2.next();
			String codeStructure=employeCompteBean.getCode_structure();

			StructureEntrepriseBean structure=mapStructureEntreprise.get(codeStructure);
			if(structure!=null)
			{
				String libell_division=structure.getLibelleDivision();
				String libelleDirection=structure.getLibelleDirection();
				String libelleUnite=structure.getLibelleUnite();
				String libelleDepartement=structure.getLibelleDepartement();
				String libelleService=structure.getLibelleService();
				String libelleSection=structure.getLibelleSection();
				if(libell_division==null) libell_division="";
				if(libelleDirection==null)libelleDirection="";
				if(libelleUnite==null)libelleUnite="";
				if(libelleDepartement==null)libelleDepartement="";
				if(libelleService==null)libelleService="";
				if(libelleSection==null)libelleSection="";
				String valeurCode_Structure=codeStructure+","+libell_division+","+libelleDirection+","+libelleUnite+","+libelleDepartement+","+ libelleService+","+libelleSection;
				employeCompteBean.setCode_structure(valeurCode_Structure);
			}
		}
		
		Iterator<FichePlanningBean> iterator3=model4.iterator();
		while(iterator2.hasNext())
		{
			FichePlanningBean planBean=iterator3.next();
			String codeStructure=planBean.getStructure();

			StructureEntrepriseBean structure=mapStructureEntreprise.get(codeStructure);
			if(structure!=null)
			{
				String libell_division=structure.getLibelleDivision();
				String libelleDirection=structure.getLibelleDirection();
				String libelleUnite=structure.getLibelleUnite();
				String libelleDepartement=structure.getLibelleDepartement();
				String libelleService=structure.getLibelleService();
				String libelleSection=structure.getLibelleSection();
				if(libell_division==null) libell_division="";
				if(libelleDirection==null)libelleDirection="";
				if(libelleUnite==null)libelleUnite="";
				if(libelleDepartement==null)libelleDepartement="";
				if(libelleService==null)libelleService="";
				if(libelleSection==null)libelleSection="";
				String valeurCode_Structure=codeStructure+","+libell_division+","+libelleDirection+","+libelleUnite+","+libelleDepartement+","+ libelleService+","+libelleSection;
				planBean.setStructure(valeurCode_Structure);
			}
		}
		comp.setVariable(comp.getId() + "Ctrl", this, true);

		binder = new AnnotateDataBinder(comp);

		binder.loadAll();



	}

	public List<StructureEntrepriseBean> getModel() {
		return model;
	}
	public List<FichePosteBean> getModel2() {
		return model2;
	}

	public List<EmployeCompteBean> getModel3() {
		return model3;
	}

	public List<FichePlanningBean> getModel4() {
		return model4;
	}
	
	public List<Compagne> getModel5() {
		return model5;
	}
	public List<PosteVsAptitudeBean> getModel6() {
		return model6;
	}

	public void onClick$download() {
		//chargement du contenu de la table Fiche_Poste et creation du fichier excel
		FichePosteModel fichePostemodel =new FichePosteModel();

		EmployeCompteModel init3= new EmployeCompteModel();

		List<EmployeCompteBean> listeemployes=new ArrayList<EmployeCompteBean>();

		try {
			listeemployes=init3.loadListEmployes();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//creation du document xls
		HSSFWorkbook workBook = new HSSFWorkbook();


		//création de l'onglet structure entreprise et remplissage des données

		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();

		structureEntrepriseModel.downloadStructureEntrepriseDataToXls(workBook);

		//creation de l'onglet listePostetravail et remplissage des données
		fichePostemodel.downloadFichePosteDataToXls(workBook);

		//creation de l'onglet liste employés et remplissage des données
		EmployeCompteModel employeCompte=new EmployeCompteModel();
		employeCompte.downloadEmployeCompteDataToXls(workBook,listeemployes);

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
	public void onClick$upload() 
	{

		okAdd.setVisible(true);
		annuler.setVisible(true);
		Executions.getCurrent().getDesktop().setAttribute("org.zkoss.zul.Fileupload.target", divupdown);

		try 
		{

			Fileupload fichierupload=new Fileupload();


			Media me=fichierupload.get("Merci de selectionner le fichier qui doit être chargé", "Chargement de fichier", true);

			processMedia(me);


		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		 

	}

	public void onClick$annuler() throws InterruptedException
	{
		//raffrechissement de l'affichage
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		FichePosteModel init =new FichePosteModel();
		Addedmodel=null;
		Addedmodel2=null;
		Addedmodel3=null;
		
		Addedmodel4=null;
		Addedmodel5=null;
		Addedmodel6=null;
		
		savemodel=null;savemodel2=null;savemodel3=null;
		savemodel4=null;savemodel5=null;savemodel6=null;


		EmployeCompteModel init3= new EmployeCompteModel();
		try {

			
			if (Messagebox.show("Voulez vous effacer  les données chargées dans la base ?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) 
			{
				model=structureEntrepriseModel.checkStructureEntreprise();
				model2=init.loadFichesPostes();
				model3=init3.loadListEmployes();
				FichePlanningModel init4= new FichePlanningModel();
				model4=init4.loadListPlanning();
				CompagneModel init5= new CompagneModel();
				model5=init5.getAllCompagnes();
			    CompetencePosteTravailModel init6= new CompetencePosteTravailModel();
				model6=init6.loadPosteVsAptitude();
				
			}else {
				
				return;
			}
				
			
			


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//rendre les boutons non visibles
		annuler.setVisible(false);
		okAdd.setVisible(false);
		//raffrechissement de l'affichage
		binder.loadAll();

	}
	public void onClick$okAdd()
	{
		//		 String rejetStructure="--> Rejets Structure Entreprise" +System.getProperty("line.separator");
		//		 String rejetPoste=System.getProperty("line.separator")+"--> Rejets Liste fiches postes"+System.getProperty("line.separator");
		//		 String rejetEMploye=System.getProperty("line.separator")+"--> Rejets Liste Employes"+System.getProperty("line.separator");
		//		 String rejet="";

		FichePosteModel init =new FichePosteModel();
		EmployeCompteModel init3= new EmployeCompteModel();
		FichePlanningModel init4= new FichePlanningModel();
		CompagneModel init5=new CompagneModel();
		CompetencePosteTravailModel init6= new CompetencePosteTravailModel();

		StructureEntrepriseModel structureEntrepriseModel=new StructureEntrepriseModel();
		List<FichePosteBean> donneeRejetesPoste;
		List<EmployeCompteBean> donneeRejetesEmplyeCompte;
		List<FichePlanningBean> donneeRejetesPlanning;
		List<StructureEntrepriseBean> donneeRejetes;
		List<Compagne> donneeRejetesCompagne;
		List<PosteVsAptitudeBean> donneeRejetespostVsApt;



		try {
			if (Messagebox.show("Voulez vous charger les données dans la base ?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) 
			{

				
				//mise à jour  de la table structure_entreprise
				
				if (Messagebox.show("Voulez vous charger les données Structure entreprise ?", "Prompt", Messagebox.YES|Messagebox.NO,
						Messagebox.QUESTION) == Messagebox.YES) 
				{

				

				HashMap <String,List<StructureEntrepriseBean>> listeDonnees=structureEntrepriseModel.ChargementDonneedansBdd(Addedmodel);
				 donneeRejetes =listeDonnees.get("supprimer");
				Addedmodel=null;
				Addedmodel=listeDonnees.get("inserer");;
				}else{
					return;
				}

				//mise à jour de la table poste_Travail_description
				if (Messagebox.show("Voulez vous charger les données Poste de Travail ?", "Prompt", Messagebox.YES|Messagebox.NO,
						Messagebox.QUESTION) == Messagebox.YES) 
				{
				FichePosteModel fichePosteModel=new FichePosteModel();

				HashMap <String,List<FichePosteBean>> listeDonneesPoste=fichePosteModel.ChargementDonneedansBdd(Addedmodel2);
				donneeRejetesPoste =listeDonneesPoste.get("supprimer");
				}else{
					return;
				}
				

				//mise à jour de des tables Compte et Employe
				if (Messagebox.show("Voulez vous charger les données Employé/Compte ?", "Prompt", Messagebox.YES|Messagebox.NO,
						Messagebox.QUESTION) == Messagebox.YES) 
				{
				EmployeCompteModel employeCompteModel=new EmployeCompteModel();

				HashMap <String,List<EmployeCompteBean>> listeDonneesEmployeCompte=employeCompteModel.ChargementDonneedansBdd(Addedmodel3);
				 donneeRejetesEmplyeCompte =listeDonneesEmployeCompte.get("supprimer");
				
				}else{
					return;
				}
				
				//mise à jour de la table compagne evaluation
				if (Messagebox.show("Voulez vous charger les données de la compagne d' évaluation ?", "Prompt", Messagebox.YES|Messagebox.NO,
						Messagebox.QUESTION) == Messagebox.YES) 
				{
				CompagneModel compagnModel=new CompagneModel();

				HashMap <String,List<Compagne>> listeDonneescomp=compagnModel.ChargementDonneedansBdd(Addedmodel5);
				donneeRejetesCompagne =listeDonneescomp.get("supprimer");
				}else{
					return;
				}
				
				//mise à jour de la table planning_evaluation
				if (Messagebox.show("Voulez vous charger les données Planning évaluation ?", "Prompt", Messagebox.YES|Messagebox.NO,
						Messagebox.QUESTION) == Messagebox.YES) 
				{
				FichePlanningModel planningModel=new FichePlanningModel();

				HashMap <String,List<FichePlanningBean>> listeDonneesplanning=planningModel.ChargementDonneedansBdd(Addedmodel4);
				 donneeRejetesPlanning =listeDonneesplanning.get("supprimer");
				}else{
					return;
				}
				
				//mise à jour de la table poste_travail_comptence_aptitudeobservable
				if (Messagebox.show("Voulez vous charger les données rattachement  postes travail vs comptence_aptitudeobservable ?", "Prompt", Messagebox.YES|Messagebox.NO,
						Messagebox.QUESTION) == Messagebox.YES) 
				{
				CompetencePosteTravailModel posteVscomp= new CompetencePosteTravailModel();

				HashMap <String,List<PosteVsAptitudeBean>> listeposteApt=posteVscomp.ChargementDonneedansBdd(Addedmodel6);
				donneeRejetespostVsApt =listeposteApt.get("supprimer");
				}else{
					return;
				}

				//mise à jour des tables compte de la base common et employe
				model=structureEntrepriseModel.checkStructureEntreprise();
				model2=init.loadFichesPostes();
				model3=init3.loadListEmployes();
				model4=init4.loadListPlanning();
				model5=init5.getAllCompagnes();
				model6=init6.loadPosteVsAptitude();


				binder.loadAll();
				//System.out.println("Tout "+rejet);
				//AfficherFenetreRejet1(rejet);
				if((donneeRejetes.size()!=0)||(donneeRejetesPoste.size()!=0)||(donneeRejetesEmplyeCompte.size()!=0) ||(donneeRejetesPlanning.size()!=0)||(donneeRejetesCompagne.size()!=0)){
					CreationfichierRejet(donneeRejetes,donneeRejetesPoste,donneeRejetesEmplyeCompte,donneeRejetesPlanning,donneeRejetespostVsApt);
				}
				

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		okAdd.setVisible(false);
		annuler.setVisible(false);
	}
	/**
	 * cette méthode permet de créer et d'afficher un fichier excel contenant  les données rejetées
	 * @param donneeRejetes
	 * @param donneeRejetesPoste
	 * @param donneeRejetesEmplyeCompte
	 */
	public void CreationfichierRejet(List<StructureEntrepriseBean> donneeRejetes,List<FichePosteBean> donneeRejetesPoste,List<EmployeCompteBean>donneeRejetesEmplyeCompte,List<FichePlanningBean> donneeRejetesPlanning,List<PosteVsAptitudeBean> donneeRejetespostVsApt)
	{
		//chargement du contenu de la table Fiche_Poste et creation du fichier excel
		FichePosteModel fichePostemodel =new FichePosteModel();

		//creation du document xls
		HSSFWorkbook workBook = new HSSFWorkbook();


		//création de l'onglet structure entreprise et remplissage des données

		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();

		structureEntrepriseModel.downloadStructureEntrepriseRejectedDataToXls(workBook,donneeRejetes);

		//creation de l'onglet listePostetravail et remplissage des données
		fichePostemodel.downloadFichePosteRejectedDataToXls(workBook,donneeRejetesPoste);

		//creation de l'onglet liste employés et remplissage des données
		EmployeCompteModel employeCompte=new EmployeCompteModel();
		employeCompte.downloadEmployeCompteRejectedDataToXls(workBook,donneeRejetesEmplyeCompte);

		//enregistrement des données dans un fihcier xls
		FileOutputStream fOut;
		try 
		{
			fOut = new FileOutputStream(Contantes.nom_fichier_rejected_xls);
			workBook.write(fOut);
			fOut.flush();
			fOut.close();

			File file = new File(Contantes.nom_fichier_rejected_xls);
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
	/**
	 * cette méthode permet d'afficher les données rejetées et qui n'ont pas été intégres dans la table
	 */
	public void AfficherFenetreRejet1(String listeRejet)
	{

		Map<String, String> listDonne=new HashMap <String, String>();
		listDonne.put("rejet", listeRejet);


		//			Map<String, String > ll=new HashMap<String, String>();
		//			String ss="rrrrrrr"+System.getProperty("line.separator")+"gggggggg"; 
		//			ll.put("rejet", ss);
		final Window win2 = (Window) Executions.createComponents("../pages/REJDATA.zul", self, listDonne);
		// We send a message to the Controller of the popup that it works in popup-mode.
		win2.setAttribute("popup", true);

		//decoratePopup(win);
		try 
		{
			win2.doModal();

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
	/**
	 * cette méthode permet de charger les données qui se trouvent dans le fichier xls
	 * @param med
	 * @throws InterruptedException 
	 */
	public String processMedia(Media med) throws InterruptedException
	{
		List <FichePosteBean> listFichePoste=null;
		List <FichePlanningBean> listPlanning=null;
		List <Compagne>listcompagne=null;
		List <PosteVsAptitudeBean>listPostVsCompe=null;


		//Media med=event.getMedia();
		String rejet="";

		if ((med != null)&&(med.getName()!=null)) 
		{
			String filename = med.getName();

			if ( !filename.endsWith(".xlsx")) 
			{
				Messagebox.show("Le fichier selectionné n'est pas un fichier Excel ou Excel 2007 et superieur !", "Information",Messagebox.OK, Messagebox.INFORMATION); 

			} 
			else 
			{

				// process the file...
				StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
				if ( filename.endsWith(".xls") ) 
				{
					//lecture et upload de fichiers OLE2 Office Documents 
					List<StructureEntrepriseBean> liste=structureEntrepriseModel.uploadStructureEntrepriseXLSFile(med.getStreamData());
					List<StructureEntrepriseBean> donneeRejetes;
					try 
					{						 

						//sauvegarde du contenu du model
						savemodel=model;
						//raffrechissement de l'affichage
						Iterator<StructureEntrepriseBean> index=liste.iterator();
						while(index.hasNext())
						{
							StructureEntrepriseBean donnee=index.next();
							model.add(donnee);
							Addedmodel.add(donnee);
						}

						binder.loadAll();

					} 
					catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//lecture des données se trouvant dans l'onglet liste poste
					FichePosteModel fichePosteModel=new FichePosteModel();
					listFichePoste=fichePosteModel.uploadXLSFile(med.getStreamData());
					//sauvegarde du contenu du model
					savemodel2=model2;
					//raffrechissement de l'affichage
					Iterator<FichePosteBean> index=listFichePoste.iterator();
					while(index.hasNext())
					{
						FichePosteBean donnee=index.next();
						model2.add(donnee);
						Addedmodel2.add(donnee);

					}
					//lecture des données se trouvant dans l'onglet liste_employés
					EmployeCompteModel employeCompteModel=new EmployeCompteModel();
					List<EmployeCompteBean> listEmployeCompteBean= employeCompteModel.uploadXLSXFile(med.getStreamData());
					//sauvegarde du contenu du model
					savemodel3=model3;


					//raffrechissement de l'affichage
					Iterator<EmployeCompteBean> index1=listEmployeCompteBean.iterator();
					while(index1.hasNext())
					{

						EmployeCompteBean donnee=index1.next();
						model3.add(donnee);
						Addedmodel3.add(donnee);

					}

					binder.loadAll();
				}
				else
					if(filename.endsWith(".xlsx"))
					{

						// lecture de fichiers Office 2007+ XML
						List<StructureEntrepriseBean> liste=structureEntrepriseModel.uploadStructureEntrepriseXLSXFile(med.getStreamData());
						List<StructureEntrepriseBean> donneeRejetes;
						try 
						{


							//sauvegarde du contenu du model
							savemodel=model;
							//raffrechissement de l'affichage
							Iterator<StructureEntrepriseBean> index=liste.iterator();
							while(index.hasNext())
							{
								StructureEntrepriseBean donnee=index.next();
								model.add(donnee);
								Addedmodel.add(donnee);
							}

							binder.loadAll();

						} 
						catch (Exception e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//lecture des données se trouvant dans l'onglet liste poste
						FichePosteModel fichePosteModel=new FichePosteModel();
						listFichePoste=fichePosteModel.uploadXLSXFile(med.getStreamData());

						//sauvegarde du contenu du model
						savemodel2=model2;
						//raffrechissement de l'affichage
						Iterator<FichePosteBean> index=listFichePoste.iterator();
						while(index.hasNext())
						{
							FichePosteBean donnee=index.next();
							model2.add(donnee);
							Addedmodel2.add(donnee);

						}
						//lecture des données se trouvant dans l'onglet liste_employés
						EmployeCompteModel employeCompteModel=new EmployeCompteModel();
						List<EmployeCompteBean> listEmployeCompteBean= employeCompteModel.uploadXLSXFile(med.getStreamData());

						//sauvegarde du contenu du model
						savemodel3=model3;

						//raffrechissement de l'affichage
						Iterator<EmployeCompteBean> index1=listEmployeCompteBean.iterator();
						while(index1.hasNext())
						{

							EmployeCompteBean donnee=index1.next();
							model3.add(donnee);
							Addedmodel3.add(donnee);

						}


						//lecture des données se trouvant dans l'onglet planning compagne
						FichePlanningModel ficheplanningModel=new FichePlanningModel();
						listPlanning=ficheplanningModel.uploadXLSXFile(med.getStreamData());

						//sauvegarde du contenu du model
						savemodel4=model4;
						//raffrechissement de l'affichage
						Iterator<FichePlanningBean> index4=listPlanning.iterator();
						while(index4.hasNext())
						{
							FichePlanningBean donnee=index4.next();
							model4.add(donnee);
							Addedmodel4.add(donnee);

						}
						
						//lecture des données se trouvant dans l'onglet  compagne
						CompagneModel compagneModel=new CompagneModel();
						listcompagne=compagneModel.uploadXLSXFile(med.getStreamData());

						//sauvegarde du contenu du model
						savemodel5=model5;
						//raffrechissement de l'affichage
						Iterator<Compagne> index5=listcompagne.iterator();
						while(index5.hasNext())
						{
							Compagne donnee=index5.next();
							model5.add(donnee);
							Addedmodel5.add(donnee);

						}
						
						//lecture des données se trouvant dans l'onglet  poste trvail competence
						CompetencePosteTravailModel postvscomp=new CompetencePosteTravailModel();
						listPostVsCompe=postvscomp.uploadXLSXPosteTravailAptitudeMass(med.getStreamData());

						//sauvegarde du contenu du model
						savemodel6=model6;
						//raffrechissement de l'affichage
						Iterator<PosteVsAptitudeBean> index6=listPostVsCompe.iterator();
						while(index6.hasNext())
						{
							PosteVsAptitudeBean donnee=index6.next();
							model6.add(donnee);
							Addedmodel6.add(donnee);

						}

						binder.loadAll();
					}

			} 				
		}
		return rejet;
	}	


}
