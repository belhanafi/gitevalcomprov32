package administration.action;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
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

import administration.bean.AdministrationLoginBean;
import administration.bean.RepCompetenceBean;
import administration.bean.StructureEntrepriseBean;
import administration.model.AdministrationLoginModel;
import administration.model.RepCompetenceModel;
import administration.model.StructureEntrepriseModel;

public class RepCompetenceAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Listbox admincomptelb;
	Textbox id_repertoire_competence;
	Textbox  code_famille;
	Textbox  famille;
	Textbox  code_groupe;
	Textbox  groupe;
	Textbox  code_competence;
	Textbox libelle_competence;
	Textbox definition_competence;
	Textbox aptitude_observable;
	Listbox affichable;
	Div divupdown;
		
	AnnotateDataBinder binder;
	List<RepCompetenceBean> model = new ArrayList<RepCompetenceBean>();
	RepCompetenceBean selected;
	List list_profile=null;
	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button upload;
	Button download;
	Button effacer;
	
	public RepCompetenceAction() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		
		affichable.appendItem("O", "O");
		affichable.appendItem("N", "N");
		
		okAdd.setVisible(false);
		effacer.setVisible(false);
			
		// création de la structure de l'entreprise bean
		RepCompetenceModel rep_comp =new RepCompetenceModel();
		model=rep_comp.loadRepCompetence();
		binder = new AnnotateDataBinder(comp);
		
		
		//ces trois instructions permettent de selection le premier element de la listebox
		if(model.size()!=0)
			selected=model.get(0);
		
		if(admincomptelb.getItemCount()!=0)
			admincomptelb.setSelectedIndex(0);
		binder.loadAll();
	}

	public List<RepCompetenceBean> getModel() {
		return model;
	}



	public RepCompetenceBean getSelected() {
		return selected;
	}

	public void setSelected(RepCompetenceBean selected) {
		this.selected = selected;
	}

	public void onClick$add() throws WrongValueException, ParseException {
		
		clearFields();
		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);
		
	}
	
	public void onClick$okAdd()throws WrongValueException, ParseException, InterruptedException {
	 	
		RepCompetenceBean addedData = new RepCompetenceBean();
		addedData.setAffichable(getSelectedAffichable());
		//addedData.setId_repertoire_competence(Integer.parseInt(getSelectedIdRepCompetence()));
		addedData.setCode_famille((getSelectedCode_famille()));
		addedData.setFamille(getSelectedfamille());
		addedData.setCode_groupe(getSelectedCode_groupe());
		addedData.setGroupe(getSelectedGroupe());
		addedData.setCode_competence( getSelectedCode_competence());
		addedData.setLibelle_competence( getSelectedlibelle_competence());
		addedData.setDefinition_competence( getSelectedDefinition_competence());
		addedData.setAptitude_observable( getSelectedaptitude_observable());
	
		//controle d'intégrité 
		RepCompetenceModel admini_model =new RepCompetenceModel();
		//Boolean donneeValide=admini_model.controleIntegrite(addedData);
		Boolean donneeValide=true;
		
		if (donneeValide)
		{
			//insertion de la donnée ajoutée dans la base de donnée
			boolean donneeAjoute=admini_model.addRepCompBean(addedData);
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
		RepCompetenceBean addedData = new RepCompetenceBean();
		selected.setAffichable(getSelectedAffichable());
		selected.setId_repertoire_competence(Integer.parseInt(getSelectedIdRepCompetence()));
		selected.setCode_famille((getSelectedCode_famille()));
		selected.setFamille(getSelectedfamille());
		selected.setCode_groupe(getSelectedCode_groupe());
		selected.setGroupe(getSelectedGroupe());
		selected.setCode_competence( getSelectedCode_competence());
		selected.setLibelle_competence( getSelectedlibelle_competence());
		selected.setDefinition_competence( getSelectedDefinition_competence());
		selected.setAptitude_observable( getSelectedaptitude_observable());
		
		//controle d'intégrité 
		RepCompetenceModel admin_model =new RepCompetenceModel();
		//Boolean donneeValide=admini_login_model.controleIntegrite(selected);
		Boolean donneeValide=true;
		if (donneeValide){
			
			if (Messagebox.show("Voulez vous appliquer les modifications?", "Prompt", Messagebox.YES|Messagebox.NO,
				    Messagebox.QUESTION) == Messagebox.YES) {
				    //System.out.println("pressyes");
				admin_model.majRepCompBean(selected);	binder.loadAll();
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
		RepCompetenceModel admini_login_model =new RepCompetenceModel();
		//suppression de la donnée supprimée de la base de donnée
		
		if (Messagebox.show("Voulez vous supprimer cette compétence?", "Prompt", Messagebox.YES|Messagebox.NO,
			    Messagebox.QUESTION) == Messagebox.YES) {
			    //System.out.println("pressyes");
			selected.setId_repertoire_competence(Integer.parseInt(getSelectedIdRepCompetence()));
			admini_login_model.supprimerComp(selected);	model.remove(selected);
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


	
	

	public void onSelect$admincomptelb() {
		closeErrorBox(new Component[] { id_repertoire_competence, code_famille,famille,code_groupe,  groupe, 
				code_competence,libelle_competence, definition_competence, aptitude_observable,affichable });
	}
	
	
	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}


	private String getSelectedAffichable() throws WrongValueException {
		String name = affichable.getSelectedItem().getLabel();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(affichable, "champ competence affichable ne doit pas être vide!");
		}
		return name;
	}

	

	private String getSelectedIdRepCompetence() throws WrongValueException {
		String name = id_repertoire_competence.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(id_repertoire_competence, "id_repertoire_competence ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedCode_famille() throws WrongValueException {
		String name = code_famille.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(code_famille, "code famille ne doit pas être vide!");
		}
		return name;
	}
	
	
	private String getSelectedfamille() throws WrongValueException {
		String name = famille.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(famille, "famille ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedCode_groupe() throws WrongValueException {
		String name = code_groupe.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(code_groupe, "code groupe ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedGroupe() throws WrongValueException {
		String name = groupe.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(groupe, "groupe ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedCode_competence() throws WrongValueException {
		String name = code_competence.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(code_competence, "code competencene doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedlibelle_competence() throws WrongValueException{
		
		String name = libelle_competence.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(libelle_competence, "libelle competence ne doit pas être vide!");
		}
	 
		return name;
	}

   private String getSelectedDefinition_competence() throws WrongValueException{
		
		String name = definition_competence.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(definition_competence, "libelle competence ne doit pas être vide!");
		}
	 
		return name;
	}

   private String getSelectedaptitude_observable() throws WrongValueException{
		
		String name = aptitude_observable.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(aptitude_observable, "aptitude_observable ne doit pas être vide!");
		}
	 
		return name;
	}
   
 

  public void clearFields(){
	    id_repertoire_competence.setText("");
	    code_famille.setText("");
	    famille.setText("");
	    code_groupe.setText("");
		groupe.setText("");
		code_competence.setText("");
		libelle_competence.setText("");
		definition_competence.setText("");
		aptitude_observable.setText("");
		affichable.setSelectedIndex(0);
		
		
  }
  
  public void onClick$upload() throws BiffException, InvalidFormatException, IOException {
		Executions.getCurrent().getDesktop().setAttribute("org.zkoss.zul.Fileupload.target", divupdown);
		
		try 
		{
			
			Fileupload fichierupload=new Fileupload();
			
			//Media me=fichierupload.get("Merci de selectionner le fichier qui doit être chargé", "Chargement de fichier", true);
			Media me=fichierupload.get("Merci de selectionner le fichier qui doit être chargé", "Chargement de fichier");
			
			processMedia(me);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

  
  public void onClick$download() {
		//chargement du contenu de la table structure_entreprise et creation du fichier excel
		RepCompetenceModel repcommodel =new RepCompetenceModel();
		repcommodel.downloadStructureEntrepriseDataToXls();
		
		
	}
  
public void processMedia(Media med) throws BiffException, InvalidFormatException, IOException
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
				RepCompetenceModel repcomModel =new RepCompetenceModel();
				if ( filename.endsWith(".xls") ) 
				{
					//lecture et upload de fichiers OLE2 Office Documents 
					//InputStream ss=med.getStreamData();
					List<RepCompetenceBean> liste=repcomModel.uploadXLSFile(med.getStreamData());
					//List<RepCompetenceBean> liste=repcomModel.uploadXLSFileS(filename);
					List<RepCompetenceBean> donneeRejetes;
					try 
					{
						 HashMap <String,List<RepCompetenceBean>> listeDonnees=repcomModel.ChargementDonneedansBdd(liste);
						 donneeRejetes =listeDonnees.get("supprimer");
						 liste=null;
						 liste=listeDonnees.get("inserer");;
						
					
						//raffrechissement de l'affichage
						Iterator<RepCompetenceBean> index=liste.iterator();
						while(index.hasNext())
						{
							RepCompetenceBean donnee=index.next();
							model.add(donnee);
							
						}
				
						binder.loadAll();
						if(donneeRejetes.size()!=0)
						{
							String listeRejet=new String("-->");
							//Afficharge de la liste des données rejetées
							Iterator<RepCompetenceBean> index1 =donneeRejetes.iterator();
							while(index1.hasNext())
							{
							

								RepCompetenceBean donnee=index1.next();
								String donneeString=donnee.getCode_famille()+";"+donnee.getFamille()
								+";"+donnee.getCode_groupe()
								 +";"+donnee.getGroupe()
								+";"+donnee.getCode_competence()
								+";"+donnee.getLibelle_competence()
								+";"+donnee.getDefinition_competence()
								+";"+donnee.getAptitude_observable()
								+ ";"+donnee.getAffichable();
								
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
						List<RepCompetenceBean> liste=repcomModel.uploadXLSXFile(ss);
						List<RepCompetenceBean> donneeRejetes;
						try 
						{
							 HashMap <String,List<RepCompetenceBean>> listeDonnees=repcomModel.ChargementDonneedansBdd(liste);
							 donneeRejetes =listeDonnees.get("supprimer");
							 liste=null;
							 liste=listeDonnees.get("inserer");;
							
						
							//raffrechissement de l'affichage
							Iterator<RepCompetenceBean> index=liste.iterator();
							while(index.hasNext())
							{
								RepCompetenceBean donnee=index.next();
								model.add(donnee);
								
							}
					
							binder.loadAll();
							if(donneeRejetes.size()!=0)
							{
								String listeRejet=new String("-->");
								//Afficharge de la liste des données rejetées
								Iterator<RepCompetenceBean> index1 =donneeRejetes.iterator();
								while(index1.hasNext())
								{
									RepCompetenceBean donnee=index1.next();
									String donneeString=donnee.getCode_famille()+";"+donnee.getFamille()
									+";"+donnee.getCode_groupe()
									 +";"+donnee.getGroupe()
									+";"+donnee.getCode_competence()
									+";"+donnee.getLibelle_competence()
									+";"+donnee.getDefinition_competence()
									+";"+donnee.getAptitude_observable()
									+ ";"+donnee.getAffichable();
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

}