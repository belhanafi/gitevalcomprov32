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
import compagne.model.CompagneCreationModel;

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
	Textbox idcompagne;
	Textbox id_compagne_type;
	Textbox  nom;
	Listbox type_compagne;
	Datebox date_deb_comp;
	Datebox date_fin_comp;
	AnnotateDataBinder binder;
	List<CompagneCreationBean> model = new ArrayList<CompagneCreationBean>();
	List<CompagnePosteMapBean> model1 = new ArrayList<CompagnePosteMapBean>();
	
	CompagneCreationBean selected;
	CompagnePosteMapBean selected1;
	
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

    HashMap <String, Checkbox> selectedCheckBox;
	HashMap <String, Checkbox> unselectedCheckBox;
	HashMap <String, Checkbox> listCheckBox;
	AnnotateDataBinder binder1;
	
	public CompagneCreationAction() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		
		super.doAfterCompose(comp);
		
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
		okAdd.setVisible(false);
		effacer.setVisible(false);
		add.setVisible(true);
		update.setVisible(true);
		delete.setVisible(true);
		
				
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

	public void onClick$delete() throws InterruptedException {
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
			compagne_model.deleteCompagne(selected);
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
		admincomptelb.setSelectedIndex(0);
		binder.loadAll();
		
		
	}

	
	
	public void onSelect$admincomptelb() {
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
	
	private int getSelectedtype_compagne() throws WrongValueException {
		Integer name=(Integer) map.get((String)type_compagne.getSelectedItem().getLabel());
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
}