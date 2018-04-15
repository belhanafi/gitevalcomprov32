package kpi.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kpi.bean.ActionDevelopmentMetierBean;
import kpi.bean.ListeCompagneVagueBean;
import kpi.bean.EchelleMaitrise;
import kpi.model.CorrectionPosteModel;
import kpi.model.KpiSyntheseModel;
import net.sf.jxls.exception.ParsePropertyException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Window;

import common.ApplicationSession;

public class CorrectionPosteAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	AnnotateDataBinder binder;

	private Listbox poste_travail;
	private Listbox listcompagne;
	Listbox liste_echelle;
	Listbox liste_action_development;
	Listbox list_propose;
	Listbox list_valide;
	Listbox list_programme;
	Listbox list_realise;
	Component comp1;
	private Include iframe3;
	
	
	

	Button exporterWord;
	Button EnregistrerAssociation;
	Window win;
 
	private HashMap <String, Radio> selectedRadio;
	private  Radio selectedRadioEchelle;




	private List<ListeCompagneVagueBean> model = new ArrayList<ListeCompagneVagueBean>();
	private List<EchelleMaitrise> modelEchelle = new ArrayList<EchelleMaitrise>();
	private List<ActionDevelopmentMetierBean> modelActionDevelopment = new ArrayList<ActionDevelopmentMetierBean>();
	private List<String> suiviSort=new ArrayList<String>();
	private HashMap<String, String> listSuiviSort;
	
	private ListeCompagneVagueBean selected;
	private EchelleMaitrise selectedEchelle;
	//private ActionDevelopmentMetierBean selectedActionDevelopment;
	private HashMap<String, HashMap<String, Integer>> listDb = null;
	private Map map_poste=null;
	private CorrectionPosteModel correctionPosteMoel=new CorrectionPosteModel();
	private String selected_id_compagne="1";
	private HashMap<String, HashMap<String, ActionDevelopmentMetierBean>> mapEchelleDevAction=null;
	private Integer idcompagne;
	private String selectedPosteTravail=null;
	
	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		//System.out.println("doAfterCompose executed");


		super.doAfterCompose(comp);

		comp.setVariable(comp.getId() + "Ctrl", this, true);

		//récupération de la liste des compagnes


		
		// Affichage de la liste des compagnes
		KpiSyntheseModel init= new KpiSyntheseModel();
		model=init.getListeCampgneVague(); 
		

		//chargement du contenu de la table action_development

		
		selectedRadio=new HashMap <String, Radio>();
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

//		if(listcompagne.getItemCount()!=0)
//			listcompagne.setSelectedIndex(0);


		comp1=comp;

	}





	@SuppressWarnings("deprecation")
	public void onClick$exporterWord() throws InterruptedException
	{

		if (Messagebox.show("Voulez vous exporter la liste des employés ?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			

			return;
		}

		else{
			return;
		}
	}

	public void onClick$enregistrerAssociation() throws InterruptedException, ParsePropertyException, InvalidFormatException, IOException
	{
		List <Listitem>listeSelection=liste_action_development.getItems();
		
		
		

		Iterator iterateur=listeSelection.iterator();
		//prcourir l'ensemble de la grid
		ArrayList<ActionDevelopmentMetierBean> listActionUpdate=new ArrayList<ActionDevelopmentMetierBean>();
		while (iterateur.hasNext()){
			Listitem item=(Listitem)iterateur.next();
			
			List <Listcell> listcell=item.getChildren();
			
			if(listcell.size()==10){
			
			Listcell cellAction=listcell.get(0);
			String selectedACtion=(String)cellAction.getLabel();
			
			Listcell cellAction1=listcell.get(1);
			String effectifs=(String)cellAction1.getLabel();
			
			 // String effectifs=getIdSortFromSelection(listcell,1);
			 String propose=getIdSortFromSelection(listcell,6);
			 
			 String valide=getIdSortFromSelection(listcell,7);
			 
			 String programme=getIdSortFromSelection(listcell,8);
			 
			 String realise=getIdSortFromSelection(listcell,9);

			 ActionDevelopmentMetierBean actionDev=new ActionDevelopmentMetierBean(selectedACtion,idcompagne.toString(),selectedPosteTravail,propose, valide, programme, realise,effectifs);
			 listActionUpdate.add(actionDev);
			 
			}
		}
		
		if (Messagebox.show("Voulez vous créer des actions pour le poste sélectionné ?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			correctionPosteMoel.updateActionDevPoste(listDb,listActionUpdate);

			return;
		}

		else{
			return;
		}
		

	}
	
	private String getIdSortFromSelection(List<Listcell> llistcell, int index)
	{

		ArrayList<String> arraySortKey=new ArrayList<>(listSuiviSort.keySet());
		Listcell cellSelected=llistcell.get(index);
		List <Listbox> listeSortSelected= cellSelected.getChildren();
		Listbox  listeSelected=listeSortSelected.get(0);
		int indexSelected=listeSelected.getSelectedIndex();
		 if(indexSelected==-1)
			 return "";
		String valeur=(String)arraySortKey.get(indexSelected);

		
			
		return valeur;
	}
	
	private void setIdSortFromSelection(List<Listcell> llistcell,int index, String toSelect)
	{

		ArrayList<String> arraySortKey=new ArrayList<>(listSuiviSort.keySet());
		Listcell cellSelected=llistcell.get(index);
		List <Listbox> listeSortSelected= cellSelected.getChildren();
		Listbox  listeSelected=listeSortSelected.get(0);
		
		List listitem=listeSelected.getItems();
		
		int indexToSelect=(int)arraySortKey.indexOf(toSelect);
		if(indexToSelect!=-1) 
			listeSelected.setSelectedIndex(indexToSelect);

	}
	
	
	public List<EchelleMaitrise> getModelEchelle() {
		return modelEchelle;
	}
	
	public List<ActionDevelopmentMetierBean> getModelActionDevelopment() {
		return modelActionDevelopment;
	}
	
	
	public EchelleMaitrise getSelectedEchelle() {
		return selectedEchelle;
	}

	public void setSelectedEchelle(EchelleMaitrise selectedEchelle) {
		this.selectedEchelle = selectedEchelle;
	}
	
	
	
	public List<ListeCompagneVagueBean> getModel() {
		return model;
	}
	public ListeCompagneVagueBean getSelected() {
		return selected;
	}

	public void setSelected(ListeCompagneVagueBean selected) {
		this.selected = selected;
	}
	
	

	public void onSelect$poste_travail() throws SQLException
	{
		selectedPosteTravail= (String)poste_travail.getSelectedItem().getValue();
		
		ListModelList listModel = new ListModelList(modelEchelle);
		liste_echelle.setModel(listModel);
		
		
		ListModelList listModel2 = new ListModelList(new ArrayList<ActionDevelopmentMetierBean>());
		liste_action_development.setModel(listModel2);
		initListBoxSuivi( listSuiviSort);

	}
	
	public void onModifyCompagne(ForwardEvent event) throws SQLException{
		Radio radio = (Radio) event.getOrigin().getTarget();

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
			//Radio radio=selectedRadio.get(cles);

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

		KpiSyntheseModel kpi=new KpiSyntheseModel();
		map_poste=kpi.getListPostTravailValid(listDb);
		Set set = (map_poste).entrySet(); 
		Iterator i = set.iterator();

		

		poste_travail.appendItem("Tous Postes Travail","tous");

		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			poste_travail.appendItem((String) me.getKey(),(String) me.getValue());
		}

		poste_travail.setSelectedIndex(0);
		
		
		//chargement des d'echelles
		modelEchelle=correctionPosteMoel.getListeEchelleMaitrise(listDb);
		
		

		//binder.loadComponent(poste_travail);
		//binder.loadAll();
		//chargement de la table suivi_sort
		listSuiviSort= correctionPosteMoel.getListeSuiviSort( listDb);
		ListModelList listModel = new ListModelList(modelEchelle);
		liste_echelle.setModel(listModel);

		initListBoxSuivi( listSuiviSort);
		ListModelList listModel2 = new ListModelList(new ArrayList<ActionDevelopmentMetierBean>());
		liste_action_development.setModel(listModel2);
		selectedPosteTravail=null;

	}
	
	public void onModifyEchelle(ForwardEvent event) throws SQLException, InterruptedException{
		if(selectedPosteTravail!=null){
			Radio radio = (Radio) event.getOrigin().getTarget();
			if( selectedRadioEchelle!=null)
				selectedRadioEchelle.setChecked(false);
	
			if (radio.isChecked())
			{
				String idEchelle=radio.getContext();
				
				//chargement du contenu de la table action_development 
				mapEchelleDevAction=correctionPosteMoel.getDevelopmentAction(listDb,(new Integer(idcompagne)).toString(),selectedPosteTravail,idEchelle);
				
				mapEchelleDevAction.get(selectedEchelle);
				HashMap <String, ActionDevelopmentMetierBean> mapActionDevelopment =mapEchelleDevAction.get(idEchelle);
				Collection set =mapActionDevelopment.values();
					
				
				modelActionDevelopment=new ArrayList<ActionDevelopmentMetierBean>(set);
	
				ListModelList listModel = new ListModelList(modelActionDevelopment);
				liste_action_development.setModel(listModel);
				
				initListBoxSuivi( listSuiviSort);
				//initListBoxSuiviSelection(mapEchelleDevAction);
				selectedRadioEchelle=radio;
				
			}
		}else{
			Messagebox.show("Merci de selectionner le poste de travail d'abord", "Information",Messagebox.OK, Messagebox.INFORMATION);
			initListBoxSuivi( listSuiviSort);
			Radio radio = (Radio) event.getOrigin().getTarget();
			radio.setChecked(false);
		}
		

	}
	
	private void initListBoxSuiviSelection(HashMap<String, HashMap<String, ActionDevelopmentMetierBean>>  mapEchelleDevAction){
		
		List <Listitem>listeSelection=liste_action_development.getItems();

		Iterator iterateur=listeSelection.iterator();
		//prcourir l'ensemble de la grid
		
		while (iterateur.hasNext()){
			Listitem item=(Listitem)iterateur.next();
			
			List <Listcell> listcell=item.getChildren();
			
			if(listcell.size()==10){
			
			Listcell cellAction=listcell.get(0);
			String selectedACtion=(String)cellAction.getLabel();
			HashMap<String, ActionDevelopmentMetierBean> listeEchelleSort=mapEchelleDevAction.get(selectedRadioEchelle.getContext());
			
			ActionDevelopmentMetierBean actionDeveloppement=listeEchelleSort.get(selectedACtion);
			
			//TODO trouver l'ordre de getPropose ...
			setIdSortFromSelection(listcell,6, actionDeveloppement.getPropose());
			
			setIdSortFromSelection(listcell,7, actionDeveloppement.getValidee());
			
			setIdSortFromSelection(listcell,8, actionDeveloppement.getProgrammee());
			
			setIdSortFromSelection(listcell,9, actionDeveloppement.getRealisee());
			 


			}
		}
	}

		private void initListBoxSuivi( HashMap<String,String> listSuiviSort)
		{

			//vider le contenu des listes pour les initialier
			Collection set =listSuiviSort.values();
			suiviSort=new ArrayList<String>(set);
			
			
			ListModelList listModel = new ListModelList(suiviSort);
			list_propose.setModel(listModel);
			
			list_programme.setModel(listModel);
			list_realise.setModel(listModel);
			list_valide.setModel(listModel);
			

		}
		public void onAfterRender$liste_action_development(ForwardEvent event) {
			initListBoxSuiviSelection(mapEchelleDevAction);
			//System.out.println("ouverture de la listebox");
			//System.out.println("################onAfterRender$lst_test::Active Page: " + ((Listbox)event.getOrigin().getTarget()).getActivePage());
		}


		public void onClick$btReportpg3() throws Exception
		{

			
			if (Messagebox.show("Voulez vous exporter les persectives développement du poste ?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				
				ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
				applicationSession.setListDb(listDb);
				String code_poste=selectedPosteTravail= (String)poste_travail.getSelectedItem().getValue();
				applicationSession.setCode_poste(code_poste);
				String url = "/run?__report=perspectiveDevPoste.rptdesign&formatRapport=doc";
				iframe3.setSrc(url);
				iframe3.invalidate();
				

				return;
			}

			else{
				return;
			}
			
		
		}
 
 

}

