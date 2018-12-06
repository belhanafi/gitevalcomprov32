package kpi.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kpi.bean.ActionDevelopmentBean;
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
import compagne.bean.EmployesAEvaluerBean;

public class PerFicheIndividuelleAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	AnnotateDataBinder binder;

	private Listbox poste_travail;
	private Listbox listcompagne;
	Listbox liste_echelle;
	Listbox liste_evalue;
	Listbox liste_action_development;
	Listbox list_propose;
	Listbox list_valide;
	Listbox list_programme;
	Listbox list_realise;
	Component comp1;
	private Include iframe;
	Button exporterWord;
	Button enregistrerAssociation;
	Window win;
 
	private HashMap <String, Radio> selectedRadio;
	private  Radio selectedRadioEchelle;
	private  Radio selectedRadioEvalue;
	private List<ListeCompagneVagueBean> model = new ArrayList<ListeCompagneVagueBean>();
	private List<EchelleMaitrise> modelEchelle = new ArrayList<EchelleMaitrise>();
	private List<ActionDevelopmentBean> modelEvalue = new ArrayList<ActionDevelopmentBean>();
	private List<ActionDevelopmentBean> modelActionDevelopment = new ArrayList<ActionDevelopmentBean>();
	private List<String> suiviSort=new ArrayList<String>();
	private HashMap<String, String> listSuiviSort;
	
	private ListeCompagneVagueBean selected;
	private EchelleMaitrise selectedEchelle;
	private EchelleMaitrise selectedEvalue;
	//private ActionDevelopmentBean selectedActionDevelopment;
	private HashMap<String, HashMap<String, Integer>> listDb = null;
	private Map map_poste=null;
	private CorrectionPosteModel correctionPosteMoel=new CorrectionPosteModel();
	private String selected_id_compagne="1";
	private HashMap<String, ArrayList<ActionDevelopmentBean>> mapEvalueAction=null;
	private Integer idcompagne;
	private String selectedPosteTravail=null;
	private Listbox direction1;
	Map map_direction=null;
	
	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		//System.out.println("doAfterCompose executed");


		super.doAfterCompose(comp);

		comp.setVariable(comp.getId() + "Ctrl", this, true);

		//récupération de la liste des compagnes


		
		// Affichage de la liste des compagnes
		KpiSyntheseModel init= new KpiSyntheseModel();
		model=init.getListeValidCampgneBase(); 
		

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

		if (Messagebox.show("Voulez vous exporter la fiche individuelle ?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			
			//int id_employe=(Integer) employeV.getSelectedItem().getValue();
			
			/*EmployesAEvaluerBean employerAEvaluerID=mapEmployeEvalueBean.getMapclesnomEmploye().get(selectedEmployeV);
			int id_employe=employerAEvaluerID.getId_employe();*/
			ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
			//int compagne_id= Integer.parseInt((String) map_compagne.get((String)compagneV.getSelectedItem().getLabel()));
			
	
			applicationSession.setId_employe(Integer.valueOf(selectedRadioEvalue.getContext()));
			applicationSession.setListDb(listDb);
			
			
			
			String url = "/run?__report=ficheIndividuelle1.rptdesign&formatRapport=doc";
			iframe.setSrc(url);
			iframe.invalidate();
			

			return;
		}

		else{
			return;
		}
	}

	public void onClick$enregistrerAssociation() throws InterruptedException, ParsePropertyException, InvalidFormatException, IOException
	{
		if(selectedRadioEvalue!=null){
			List <Listitem>listeSelection=liste_action_development.getItems();
	
			Iterator iterateur=listeSelection.iterator();
	//		String selectedIdEvalue=selectedRadioEvalue.getContext();
	//		String selectedEvalue=selectedRadioEvalue.getValue();
			//prcourir l'ensemble de la grid
			ArrayList<ActionDevelopmentBean> listActionUpdate=new ArrayList<ActionDevelopmentBean>();
			while (iterateur.hasNext()){
				Listitem item=(Listitem)iterateur.next();
				
				List <Listcell> listcell=item.getChildren();
				
				if(listcell.size()==10){
				
					Listcell cellAction=listcell.get(0);
					String selectedACtion=(String)cellAction.getLabel();
					
		
					 String propose=getIdSortFromSelection(listcell,5);
					 
					 String valide=getIdSortFromSelection(listcell,6);
					 
					 String programme=getIdSortFromSelection(listcell,7);
					 
					 String realise=getIdSortFromSelection(listcell,8);
					 cellAction=listcell.get(9);
					 String selectedACtionComp=(String)cellAction.getLabel();
		
					 ActionDevelopmentBean actionDev=new ActionDevelopmentBean(selectedACtionComp,selectedACtion,idcompagne.toString(),selectedPosteTravail,selectedRadioEvalue.getContext(),selectedRadioEvalue.getValue(),propose, valide, programme, realise);
					 listActionUpdate.add(actionDev);
					 //correctionPosteMoel.updateActionDevEmploye(listDb,actionDev);
				}
			}
			if (Messagebox.show("Voulez vous enregister les actions de développement pour cet employé ?", "Prompt", Messagebox.YES|Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES) {
				
				correctionPosteMoel.updateBatchActionDevEmploye(listDb,listActionUpdate);
				

				return;
			}

			else{
				return;
			}
			
		}
		else{
			Messagebox.show("Merci de selectionner un employé d'abord ", "Information",Messagebox.OK, Messagebox.INFORMATION);
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
	
	public List<ActionDevelopmentBean> getModelEvalue() {
		return modelEvalue;
	}
	
	public List<ActionDevelopmentBean> getModelActionDevelopment() {
		return modelActionDevelopment;
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
		
		
		ListModelList listModel2 = new ListModelList(new ArrayList<ActionDevelopmentBean>());
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
		map_direction=kpi.getListDirection(listDb);
		Set set = (map_direction).entrySet(); 
		Iterator i = set.iterator();
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			direction1.appendItem((String) me.getKey(),(String) me.getKey());
		}

		direction1.setSelectedIndex(0);
	

	}
	
	public void onModifyEchelle(ForwardEvent event) throws SQLException, InterruptedException{
		
	
		if(selectedPosteTravail!=null){
			Radio radio = (Radio) event.getOrigin().getTarget();
			if( selectedRadioEchelle!=null)
				selectedRadioEchelle.setChecked(false);
	
			if (radio.isChecked())
			{
				selectedRadioEchelle=radio;
				String idEchelle=radio.getContext();
				
				//chargement du contenu de la table action_development 
/*				mapEvalueAction=correctionPosteMoel.getDevelopmentEvalue(listDb,(new Integer(idcompagne)).toString(),selectedPosteTravail,idEchelle);
				
				
				
				if(mapEvalueAction==null || mapEvalueAction.size()==0){
					Messagebox.show("Aucun employé ne répond aux critères des filtres ou aucune action n'a été définie au niveau de l'ecran perspectives (Fiche Métier)", "Information",Messagebox.OK, Messagebox.INFORMATION);
					//si la grille est remplie ==> la vider
					modelActionDevelopment=new ArrayList<ActionDevelopmentBean>();
					ListModelList listModel = new ListModelList(modelActionDevelopment);
					liste_action_development.setModel(listModel);
					
					modelEvalue=new ArrayList<ActionDevelopmentBean>();
					
					ListModelList listModelEvalue = new ListModelList(modelEvalue);
					liste_evalue.setModel(listModelEvalue);
					exporterWord.setDisabled(true);
					enregistrerAssociation.setDisabled(true);
					
				}else{*/
					

					modelEvalue=new ArrayList<ActionDevelopmentBean>(getListAllEvalue().values());
					
					ListModelList listModelEvalue = new ListModelList(modelEvalue);
					listModelEvalue.sort(new Comparator<ActionDevelopmentBean>() {  
					   

						@Override
						public int compare(ActionDevelopmentBean arg0,
								ActionDevelopmentBean arg1) {
							return arg0.getEvalue().compareTo(arg1.getEvalue());
							
						}  
					}, true);
					liste_evalue.setModel(listModelEvalue);
					
					
//					Collection set =mapActionDevelopment.valuses();
//					
//					modelActionDevelopment=new ArrayList<ActionDevelopmentBean>(set);
//				
//					ListModelList listModel = new ListModelList(modelActionDevelopment);
//					liste_action_development.setModel(listModel);
					ListModelList listModel = new ListModelList();
					liste_action_development.setModel(listModel);
					
					//initListBoxSuivi( listSuiviSort);
					//exporterWord.setDisabled(false);
					enregistrerAssociation.setDisabled(false);
					//initListBoxSuiviSelection(mapEchelleDevAction);
				//}
					
				
				
			}
		}else{
			Messagebox.show("Merci de selectionner le poste de travail d'abord", "Information",Messagebox.OK, Messagebox.INFORMATION);
			initListBoxSuivi( listSuiviSort);
			Radio radio = (Radio) event.getOrigin().getTarget();
			radio.setChecked(false);
		}
		

	}
	
	public void onModifyEvalue(ForwardEvent event) throws SQLException, InterruptedException{
		
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

				mapEvalueAction=correctionPosteMoel.getDevelopmentEvalue(listDb,(new Integer(idcompagne)).toString(),selectedPosteTravail,selectedRadioEchelle.getContext(),selectedRadioEvalue.getContext());
				
				modelActionDevelopment=mapEvalueAction.get(selectedRadioEvalue.getContext());
			
				ListModelList listModel = new ListModelList();
				liste_action_development.setModel(listModel);
				
				listModel = new ListModelList(modelActionDevelopment);
				liste_action_development.setModel(listModel);

				
				initListBoxSuivi( listSuiviSort);
				
				initListBoxSuiviSelection(modelActionDevelopment, selectedRadioEvalue.getContext());
				
				
				
			}
		}else{
			Messagebox.show("Merci de selectionner le poste de travail d'abord", "Information",Messagebox.OK, Messagebox.INFORMATION);
			initListBoxSuivi( listSuiviSort);
			Radio radio = (Radio) event.getOrigin().getTarget();
			radio.setChecked(false);
		}
		

	}
	
	private void initListBoxSuiviSelection(HashMap<String, HashMap<String, ActionDevelopmentBean>>  mapEchelleDevAction){
		
		List <Listitem>listeSelection=liste_action_development.getItems();

		Iterator iterateur=listeSelection.iterator();
		//prcourir l'ensemble de la grid
		
		while (iterateur.hasNext()){
			Listitem item=(Listitem)iterateur.next();
			
			List <Listcell> listcell=item.getChildren();
			
			if(listcell.size()==10){
			
			Listcell cellAction=listcell.get(9);
			String selectedACtion=(String)cellAction.getLabel();
			HashMap<String, ActionDevelopmentBean> listeEchelleSort=mapEchelleDevAction.get(selectedRadioEchelle.getContext());
			if( selectedRadioEvalue!=null){
				ActionDevelopmentBean actionDeveloppement=listeEchelleSort.get(selectedACtion);
				
				//TODO trouver l'ordre de getPropose ...
				setIdSortFromSelection(listcell,5, actionDeveloppement.getPropose());
				
				setIdSortFromSelection(listcell,6, actionDeveloppement.getValidee());
				
				setIdSortFromSelection(listcell,7, actionDeveloppement.getProgrammee());
				
				setIdSortFromSelection(listcell,8, actionDeveloppement.getRealisee());
			}


			}
		}
	}
	
	private void initListBoxSuiviSelection(List< ActionDevelopmentBean>  listAction, String idEvaue){
		
		List <Listitem>listeSelection=liste_action_development.getItems();

		Iterator iterateur=listeSelection.iterator();
		//prcourir l'ensemble de la grid
		
		while (iterateur.hasNext()){
			
			Listitem item=(Listitem)iterateur.next();
			
			List <Listcell> listcell=item.getChildren();
			
			if(listcell.size()==10){
			
			Listcell cellAction=listcell.get(9);
			String selectedCompACtion=(String)cellAction.getLabel();
			
			

			ActionDevelopmentBean actionDeveloppement=getActionDev(selectedCompACtion,listAction);
			if(actionDeveloppement.getIdEvalue().equals(idEvaue)){
				//TODO trouver l'ordre de getPropose ...
				setIdSortFromSelection(listcell,5, actionDeveloppement.getPropose());
				
				setIdSortFromSelection(listcell,6, actionDeveloppement.getValidee());
				
				setIdSortFromSelection(listcell,7, actionDeveloppement.getProgrammee());
				
				setIdSortFromSelection(listcell,8, actionDeveloppement.getRealisee());
			 
			}

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
			if(selectedRadioEvalue!=null)
				initListBoxSuiviSelection(mapEvalueAction.get(selectedRadioEvalue.getContext()), selectedRadioEvalue.getContext());

		}


		public HashMap<String, ActionDevelopmentBean> getListAllEvalue(){
			HashMap<String, ActionDevelopmentBean> retour=correctionPosteMoel.getDevelopmentEvalue2(listDb,(new Integer(idcompagne)).toString(),selectedPosteTravail,selectedRadioEchelle.getContext());

//			Collection<ActionDevelopmentBean> set = listActionDevelopment.values( );
//			
//			for (Iterator iterator = set.iterator(); iterator.hasNext();) {
//				ActionDevelopmentBean actionDev=(ActionDevelopmentBean)iterator.next();
//				
//				retour.put(actionDev.getIdEvalue(), actionDev);
//				
//				
//			}
			
			return retour;
		}
		
		public ActionDevelopmentBean getActionDev(String selectedCompACtion,  List<ActionDevelopmentBean> listAction){
			
			for (Iterator <ActionDevelopmentBean> iterator = listAction.iterator(); iterator.hasNext();) {
				ActionDevelopmentBean actionDevelopmentBean = (ActionDevelopmentBean) iterator
						.next();
				if(actionDevelopmentBean.getIdActionCompPost().equals(selectedCompACtion))
					return actionDevelopmentBean;
			}
			return null;
		}
		
		
		public void onSelect$direction1() throws SQLException	{
			
			poste_travail.getItems().clear();
			
			poste_travail.appendItem("Tous Postes Travail","tous");
			
			String libelle_direction=(String)direction1.getSelectedItem().getValue();
			List <String> list_code_dir=(List)map_direction.get(libelle_direction);
			
			KpiSyntheseModel kpi=new KpiSyntheseModel();
			map_poste=kpi.getListPostTravailValid(listDb,list_code_dir);
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

			initListBoxSuivi( listSuiviSort);
			ListModelList listModel2 = new ListModelList(new ArrayList<ActionDevelopmentBean>());
			liste_action_development.setModel(listModel2);
			selectedPosteTravail=null;
			
		}
 

}

