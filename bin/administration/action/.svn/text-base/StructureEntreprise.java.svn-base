package administration.action;


import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.zkoss.lang.Strings;
import org.zkoss.util.media.Media;
import org.zkoss.zk.au.out.AuClearWrongValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;


import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


import administration.bean.StructureEntrepriseBean;
import administration.model.FichePosteModel;
import administration.model.StructureEntrepriseModel;


public class StructureEntreprise extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Listbox structureEntrepriselb;
	
	Textbox codeStructure;	
	Textbox codeDivision;
	Textbox nomDivision;
	Textbox codeDirection;
	Textbox nomDirection;
	Textbox codeUnite;
	Textbox nomUnite;
	Textbox codeDepartement;
	Textbox nomdepatrement;
	Textbox codeService;
	Textbox NomService;
	Textbox codeSection;
	Textbox nomSection;
	Button upload;
	Fileupload fichierupload;
	AnnotateDataBinder binder;
	Window win;
	Textbox intro;

	Div divupdown;
	
	List<StructureEntrepriseBean> model = new ArrayList<StructureEntrepriseBean>();
	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button download;
	Button effacer;
	
	
	
	StructureEntrepriseBean selected;
	
	


	public StructureEntreprise() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// création de la structure de l'entreprise bean
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		model=structureEntrepriseModel.checkStructureEntreprise();
		effacer.setVisible(false);
		
		codeStructure.setDisabled(true);
		codeDivision.setDisabled(true);
		codeDirection.setDisabled(true);
		codeUnite.setDisabled(true);
		codeDepartement.setDisabled(true);
		codeService.setDisabled(true);
		codeSection.setDisabled(true);
	
		comp.setVariable(comp.getId() + "Ctrl", this, true);

		binder = new AnnotateDataBinder(comp);
		if(model.size()!=0)
			selected=model.get(0);
		
		if(structureEntrepriselb.getItemCount()!=0)
			structureEntrepriselb.setSelectedIndex(0);
		binder.loadAll();
		
		
		
	}

	public List<StructureEntrepriseBean> getModel() {
		return model;
	}



	public StructureEntrepriseBean getSelected() {
		return selected;
	}

	public void setSelected(StructureEntrepriseBean selected) {
		this.selected = selected;
	}
	
	public void onClick$add() throws WrongValueException, ParseException, SQLException {
		
		clearFields();
		StructureEntrepriseModel admini_model =new StructureEntrepriseModel();
		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);
		String [] chaine = new String [8];
		int i=1;
		java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(admini_model.getMaxKeyCode(), "|");
		while ( tokenizer.hasMoreTokens() ) {
		    //System.out.println(tokenizer.nextToken());
			chaine[i]=tokenizer.nextToken();
		    i++;
		}
				
			codeStructure.setValue(admini_model.getNextCode("S",chaine[1],5));
			codeDivision.setValue(admini_model.getNextCode("V",chaine[2],4));
			codeDirection.setValue(admini_model.getNextCode("D",chaine[3],4));
			codeUnite.setValue(admini_model.getNextCode("U",chaine[4],4));
			codeDepartement.setValue(admini_model.getNextCode("T",chaine[5],4));
			codeService.setValue(admini_model.getNextCode("R",chaine[6],4));;
			codeSection.setValue(admini_model.getNextCode("C",chaine[7],4));;
		
		//code_poste.setValue(admini_model.getNextCode("P",admini_model.getMaxKeyCode()));
		//code_poste.setDisabled(true);
		
	}

	public void onClick$okAdd() {
		
		StructureEntrepriseBean addedData = new StructureEntrepriseBean();
		addedData.setCodestructure(getSelectedcodeStructure());
		addedData.setCodeDivision(getSelectedcodeDivision());
		addedData.setLibelleDivision(getSelectednomDivision());
		addedData.setCodeDirection(getSelectedcodeDirection());
		addedData.setLibelleDirection(getSelectednomDirection());
		addedData.setCodeUnite(getSelectedcodeUnite());
		addedData.setLibelleUnite(getSelectednomUnite());
		addedData.setCodeDepartement(getSelectedcodeDepartement());
		addedData.setLibelleDepartement(getSelectednomDepartement());
		addedData.setCodeService(getSelectedcodeService());
		addedData.setLibelleService(getSelectednomService());
		addedData.setCodesection(getSelectedcodeSection());
		addedData.setLibelleSection(getSelectednomSection());
		
		//controle d'intégrité 
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		Boolean donneeValide=structureEntrepriseModel.controleIntegrite(addedData);
		if (donneeValide)
		{
			//insertion de la donnée ajoutée dans la base de donnée
			boolean donneeAjoute=structureEntrepriseModel.addStructureEntrepriseBean(addedData);
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

	public void onClick$update() throws InterruptedException {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		String codeStructureselectione=selected.getCodestructure();
		
		selected.setCodestructure(getSelectedcodeStructure());
		selected.setCodeDivision(getSelectedcodeDivision());
		selected.setLibelleDivision(getSelectednomDivision());
		selected.setCodeDirection(getSelectedcodeDirection());
		selected.setLibelleDirection(getSelectednomDirection());
		selected.setCodeUnite(getSelectedcodeUnite());
		selected.setLibelleUnite(getSelectednomUnite());
		selected.setCodeDepartement(getSelectedcodeDepartement());
		selected.setLibelleDepartement(getSelectednomDepartement());
		selected.setCodeService(getSelectedcodeService());
		selected.setLibelleService(getSelectednomService());
		selected.setCodesection(getSelectedcodeSection());
		selected.setLibelleSection(getSelectednomSection());
		
		//controle d'intégrité 
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		Boolean donneeValide=structureEntrepriseModel.controleIntegrite(selected);
		if (donneeValide)
		{
			if (Messagebox.show("Voulez vous appliquer les modifications?", "Prompt", Messagebox.YES|Messagebox.NO,
				    Messagebox.QUESTION) == Messagebox.YES) { //insertion de la donnée ajoutée dans la base de donnée
			boolean donneeAjoute=structureEntrepriseModel.majStructureEntrepriseBean(selected,codeStructureselectione);
			// raffrechissemet de l'affichage
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
		
		if (Messagebox.show("Voulez vous supprimer cette structure?", "Prompt", Messagebox.YES|Messagebox.NO,
			    Messagebox.QUESTION) == Messagebox.YES) {
			
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		//suppression de la donnée supprimée de la base de donnée
		structureEntrepriseModel.supprimerStructureEntrepriseBean(selected.getCodestructure());
		model.remove(selected);
		selected = null;
		binder.loadAll();
		return;
		}
		else{
			return;
		}
	}



	public void affichermessage()
	{
		try {
			Messagebox.show("La taille du champ Code division ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//public void onUpload$divupdown(UploadEvent event)throws InterruptedException
	public void processMedia(Media med)
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
				StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
				if ( filename.endsWith(".xls") ) 
				{
					//lecture et upload de fichiers OLE2 Office Documents 
					List<StructureEntrepriseBean> liste=structureEntrepriseModel.uploadStructureEntrepriseXLSFile(med.getStreamData());
					List<StructureEntrepriseBean> donneeRejetes;
					try 
					{
						 HashMap <String,List<StructureEntrepriseBean>> listeDonnees=structureEntrepriseModel.ChargementDonneedansBdd(liste);
						 donneeRejetes =listeDonnees.get("supprimer");
						 liste=null;
						 liste=listeDonnees.get("inserer");;
						
					
						//raffrechissement de l'affichage
						Iterator<StructureEntrepriseBean> index=liste.iterator();
						while(index.hasNext())
						{
							StructureEntrepriseBean donnee=index.next();
							model.add(donnee);
							
						}
				
						binder.loadAll();
						if(donneeRejetes.size()!=0)
						{
							String listeRejet=new String("-->");
							//Afficharge de la liste des données rejetées
							Iterator<StructureEntrepriseBean> index1 =donneeRejetes.iterator();
							while(index1.hasNext())
							{
								StructureEntrepriseBean donnee=index1.next();
								String donneeString=donnee.getCodestructure()+";"+donnee.getCodeDivision()
								+";"+donnee.getLibelleDivision()
								 +";"+donnee.getCodeDirection()
								+";"+donnee.getLibelleDirection()
								+";"+donnee.getCodeUnite()
								+";"+donnee.getLibelleUnite()
								+";"+donnee.getCodeDepartement()
								+ ";"+donnee.getLibelleDepartement()
								+";"+donnee.getCodeService()
								+";"+donnee.getLibelleService()
								+ ";"+donnee.getCodesection()
								+ ";"+donnee.getLibelleSection();
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
						List<StructureEntrepriseBean> liste=structureEntrepriseModel.uploadStructureEntrepriseXLSXFile(med.getStreamData());
						List<StructureEntrepriseBean> donneeRejetes;
						try 
						{
							 HashMap <String,List<StructureEntrepriseBean>> listeDonnees=structureEntrepriseModel.ChargementDonneedansBdd(liste);
							 donneeRejetes =listeDonnees.get("supprimer");
							 liste=null;
							 liste=listeDonnees.get("inserer");;
							
						
							//raffrechissement de l'affichage
							Iterator<StructureEntrepriseBean> index=liste.iterator();
							while(index.hasNext())
							{
								StructureEntrepriseBean donnee=index.next();
								model.add(donnee);
								
							}
					
							binder.loadAll();
							if(donneeRejetes.size()!=0)
							{
								String listeRejet=new String("-->");
								//Afficharge de la liste des données rejetées
								Iterator<StructureEntrepriseBean> index1 =donneeRejetes.iterator();
								while(index1.hasNext())
								{
									StructureEntrepriseBean donnee=index1.next();
									String donneeString=donnee.getCodestructure()+";"+donnee.getCodeDivision()
									+";"+donnee.getLibelleDivision()
									 +";"+donnee.getCodeDirection()
									+";"+donnee.getLibelleDirection()
									+";"+donnee.getCodeUnite()
									+";"+donnee.getLibelleUnite()
									+";"+donnee.getCodeDepartement()
									+ ";"+donnee.getLibelleDepartement()
									+";"+donnee.getCodeService()
									+";"+donnee.getLibelleService()
									+ ";"+donnee.getCodesection()
									+ ";"+donnee.getLibelleSection();
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


	
	public void onClick$upload() {
		Executions.getCurrent().getDesktop().setAttribute("org.zkoss.zul.Fileupload.target", divupdown);
		
		try 
		{
			
			Fileupload fichierupload=new Fileupload();
			
			//Media me=fichierupload.get("Merci de selectionner le fichier qui doit être chargé", "Chargement de fichier", true);
			Media me=fichierupload.get("Merci de selectionner le fichier qui doit être chargé", "Chargement de fichier", true);
			
			processMedia(me);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
//	public void onClick$download() {
//		//chargement du contenu de la table structure_entreprise et creation du fichier excel
//		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
//		
//		structureEntrepriseModel.downloadStructureEntrepriseDataToXls();
//
//	}
	
	public void onSelect$structureEntrepriselb() {
		closeErrorBox(new Component[] { codeStructure, codeDivision,nomDivision,codeDirection,  nomDirection, 
				codeUnite,nomUnite, codeDepartement, nomdepatrement, codeService,NomService, codeSection, nomSection });
	}
	
	
	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}



	private String getSelectedcodeStructure() throws WrongValueException {
		String name = codeStructure.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeStructure, "le Code Structure ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedcodeDivision() throws WrongValueException {
		String name = codeDivision.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeDivision, "le Code Division ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectednomDivision() throws WrongValueException {
		String name = nomDivision.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomDivision, "le nom Division ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedcodeDirection() throws WrongValueException {
		String name = codeDirection.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeDirection, "le codeDirection ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectednomDirection() throws WrongValueException {
		String name = nomDirection.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomDirection, "le nom Direction ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedcodeUnite() throws WrongValueException {
		String name = codeUnite.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeUnite, "le codeUnite ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectednomUnite() throws WrongValueException {
		String name = nomUnite.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomUnite, "le nom Unite ne doit pas être vide!");
		}
		return name;
	}

	private String getSelectedcodeDepartement() throws WrongValueException {
		String name = codeDepartement.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeDepartement, "le codeDepartement ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectednomDepartement() throws WrongValueException {
		String name = nomdepatrement.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomdepatrement, "le nom Departement ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedcodeService() throws WrongValueException {
		String name = codeService.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeService, "le codeService ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectednomService() throws WrongValueException {
		String name = NomService.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(NomService, "le nom Service ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedcodeSection() throws WrongValueException {
		String name = codeSection.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeSection, "le codeSection ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectednomSection() throws WrongValueException {
		String name = nomSection.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomSection, "le nom Section ne doit pas être vide!");
		}
		return name;
	}
	
	public void EnregistrerDonneesRejetes(ArrayList <String> listeRejet)
	{
		java.io.InputStream is = desktop.getWebApp().getResourceAsStream("/test/download.html");
        if (is != null)
            Filedownload.save(is, "text/html", "download.html");
        else
            alert("/test/download.html not found");
	}

    /**
     * cette méthode permet d'afficher les données rejetées et qui n'ont pas été intégres dans la table
     */
    public void AfficherFenetreRejet(String listeRejet)
    {
    	Map<String, String> listDonne=new HashMap <String, String>();
		listDonne.put("rejet", listeRejet);
		
		
//		Map<String, String > ll=new HashMap<String, String>();
//		String ss="rrrrrrr"+System.getProperty("line.separator")+"gggggggg"; 
//		ll.put("rejet", ss);
    	final Window win = (Window) Executions.createComponents("../pages/REJDATA.zul", self, listDonne);
        // We send a message to the Controller of the popup that it works in popup-mode.
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
		
 
		
    	codeStructure.setText("");
    	codeDivision.setText("");
    	nomDivision.setText("");
    	codeDirection.setText("");
    	nomDirection.setText("");
    	codeUnite.setText("");
    	nomUnite.setText("");
    	codeDepartement.setText("");
    	nomdepatrement.setText("");
    	codeService.setText("");
    	NomService.setText("");
    	codeSection.setText("");
    	nomSection.setText("");
	   
		
  }
    public void onClick$effacer()  {
		
    	
		clearFields();
		okAdd.setVisible(false);
		add.setVisible(true);
		update.setVisible(true);
		delete.setVisible(true);
		structureEntrepriselb.setSelectedIndex(0);
		binder.loadAll();
		
		
	}


}
