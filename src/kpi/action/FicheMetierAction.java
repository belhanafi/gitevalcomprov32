package kpi.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kpi.bean.ListeCompagneVagueBean;
import kpi.model.KpiSyntheseModel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Window;

import common.ApplicationFacade;
import common.ApplicationSession;

public class FicheMetierAction extends GenericForwardComposer implements EventListener {

	private static final long serialVersionUID = 1L;
	
	private Include iframe;
	private Include iframe2;
	private Include iframeWord;
	private Include iframe2Word;
	
	
	List<ListeCompagneVagueBean> model = new ArrayList<ListeCompagneVagueBean>();
	List<ListeCompagneVagueBean> model_filtre = new ArrayList<ListeCompagneVagueBean>();
	HashMap <String, Checkbox> selectedCheckBox;
	HashMap <String, Checkbox> unselectedCheckBox;
	HashMap <String, Radio> selectedRadio;
	HashMap <String, Radio> unselectedRadio;
	AnnotateDataBinder binder;
	Listbox  listVaguelb;
	private ListeCompagneVagueBean selected;
	HashMap<String, HashMap<String, Integer>> listDb = null;
	private Listbox poste_travail1;
	private Listbox poste_travail2;
	Map map_poste=null;
    private Listbox direction1;
	Map map_direction=null;


	public void  doAfterCompose(Component comp) throws Exception {

		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		selectedCheckBox=new HashMap <String, Checkbox>();
		unselectedCheckBox=new HashMap <String, Checkbox>();
		selectedRadio=new HashMap <String, Radio>();
		unselectedRadio=new HashMap <String, Radio>();
		KpiSyntheseModel init= new KpiSyntheseModel();
		model=init.getListeCampgneVague(); 

		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

		if(listVaguelb.getItemCount()!=0)
			listVaguelb.setSelectedIndex(0);


		//Components.wireVariables(this, this);
		//Components.addForwards(this, this);

	}


	public void onClick$executer() throws Exception {
		
		
		Set<String> setselected = selectedCheckBox.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		iterator=listselected.iterator();
		
		listDb = new HashMap<String,HashMap<String,Integer>>();

		while(iterator.hasNext()) {

			String cles=(String)iterator.next();
			Checkbox checkBox=selectedCheckBox.get(cles);
			if (selectedCheckBox.get(cles).isChecked()){

				Integer idcompagne=Integer.parseInt(selectedCheckBox.get(cles).getContext());
				String nominstance=selectedCheckBox.get(cles).getName();
				String vague=selectedCheckBox.get(cles).getValue();


				listDb.put(nominstance, new HashMap<String, Integer>());
				listDb.get(nominstance).put(vague, idcompagne);

			}

		}
		
		//ApplicationFacade.getInstance().setListDb(listDb);


		//System.out.println("AVANT"+ApplicationFacade.getInstance().getClient_database_id());
		
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		
		applicationSession.setListDb(listDb);
		
		Sessions.getCurrent().setAttribute("APPLICATION_ATTRIBUTE", applicationSession);

		String url = "/run?__report=synthGlobalPge1.rptdesign&formatRapport=html";
		iframe.setSrc(url);
		iframe.invalidate();
		 

	}
	
	


	public void onClick$btReportpg2() throws Exception
	{

		String url = "/run?__report=FicheMetierPge2.rptdesign&formatRapport=doc";
		iframe2Word.setSrc(url);
		iframe2Word.invalidate();
		
	}
	
	

	
	
	
	
	
	public void onClick$btReportpg() throws Exception
	{


		String url = "/run?__report=FicheMetierPge1.rptdesign&formatRapport=doc";
		iframeWord.setSrc(url);
		iframeWord.invalidate();
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
			selectedCheckBox.put(checkbox.getName(), checkbox);
		}
		else
		{
			//verifier si ça n'a pas encore été checked
			if(selectedCheckBox.containsValue(checkbox))
			{
				selectedCheckBox.remove(checkbox);

			}
			unselectedCheckBox.put(checkbox.getName(), checkbox);
		}
		//selectedCheckBox
	}
	
	
	public void onModifyRadio(ForwardEvent event) throws SQLException{
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
			selectedRadio.put(radio.getSclass(), radio);
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

				Integer idcompagne=Integer.parseInt(selectedRadio.get(cles).getContext());
				String nominstance=selectedRadio.get(cles).getSclass();
				String vague=selectedRadio.get(cles).getValue();


				listDb.put(nominstance, new HashMap<String, Integer>());
				listDb.get(nominstance).put(vague, idcompagne);

			}

		}

		if (poste_travail1.getSelectedItems().size()>0){


			poste_travail1.getItems().clear();


		}

		/*KpiSyntheseModel kpi=new KpiSyntheseModel();
		map_poste=kpi.getListPostTravailValid(listDb);
		Set set = (map_poste).entrySet(); 
		Iterator i = set.iterator();
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			poste_travail1.appendItem((String) me.getKey(),(String) me.getValue());
		}

		poste_travail1.setSelectedIndex(0);*/
		
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
	
	public void onSelect$direction1() throws SQLException	{
		
		String libelle_direction=(String)direction1.getSelectedItem().getValue();
		List <String> list_code_dir=(List)map_direction.get(libelle_direction);
		
		KpiSyntheseModel kpi=new KpiSyntheseModel();
		map_poste=kpi.getListPostTravailValid(listDb,list_code_dir);
		Set set = (map_poste).entrySet(); 
		
		Iterator i = set.iterator();
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			poste_travail1.appendItem((String) me.getKey(),(String) me.getValue());
		}
		
	}
	
	
	
	public void onSelect$poste_travail1() throws SQLException
	{
		Set<String> setselected = selectedRadio.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		iterator=listselected.iterator();

		listDb = new HashMap<String,HashMap<String,Integer>>();

		while(iterator.hasNext()) {

			String cles=(String)iterator.next();
			Radio radio=selectedRadio.get(cles);
			if (selectedRadio.get(cles).isChecked()){

							
				Integer idcompagne=Integer.parseInt(selectedRadio.get(cles).getContext());
				String nominstance=selectedRadio.get(cles).getSclass();
				String vague=selectedRadio.get(cles).getValue();


				listDb.put(nominstance, new HashMap<String, Integer>());
				listDb.get(nominstance).put(vague, idcompagne);


			}

		}
			
		String code_poste= (String)poste_travail1.getSelectedItem().getValue();
        ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		applicationSession.setListDb(listDb);
		applicationSession.setCode_poste(code_poste);
		
		Sessions.getCurrent().setAttribute("APPLICATION_ATTRIBUTE", applicationSession);
		

		String url = "/run?__report=FicheMetierPge1.rptdesign&formatRapport=html";
		iframe.setSrc(url);
		iframe.invalidate();

		


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
	
	
	
	public void onSelect$poste_travail2() throws SQLException
	{
		Set<String> setselected = selectedRadio.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		iterator=listselected.iterator();

		listDb = new HashMap<String,HashMap<String,Integer>>();

		while(iterator.hasNext()) {

			String cles=(String)iterator.next();
			Radio radio=selectedRadio.get(cles);
			if (selectedRadio.get(cles).isChecked()){

							
				Integer idcompagne=Integer.parseInt(selectedRadio.get(cles).getContext());
				String nominstance=selectedRadio.get(cles).getSclass();
				String vague=selectedRadio.get(cles).getValue();


				listDb.put(nominstance, new HashMap<String, Integer>());
				listDb.get(nominstance).put(vague, idcompagne);


			}

		}
			
		String code_poste= (String)poste_travail2.getSelectedItem().getValue();
        ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		applicationSession.setListDb(listDb);
		applicationSession.setCode_poste(code_poste);
		
		Sessions.getCurrent().setAttribute("APPLICATION_ATTRIBUTE", applicationSession);
		

		String url = "/run?__report=FicheMetierPge2.rptdesign&formatRapport=html";
		iframe2.setSrc(url);
		iframe2.invalidate();

		


	}
	
	
	public void onModifyRadio2(ForwardEvent event) throws SQLException{
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
			selectedRadio.put(radio.getSclass(), radio);
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

				Integer idcompagne=Integer.parseInt(selectedRadio.get(cles).getContext());
				String nominstance=selectedRadio.get(cles).getSclass();
				String vague=selectedRadio.get(cles).getValue();


				listDb.put(nominstance, new HashMap<String, Integer>());
				listDb.get(nominstance).put(vague, idcompagne);

			}

		}

		if (poste_travail2.getSelectedItems().size()>0){


			poste_travail2.getItems().clear();


		}

		/*
		 * A MODIFIER MODIFIER NE PAS OUBLIER
		 * KpiSyntheseModel kpi=new KpiSyntheseModel();
		map_poste=kpi.getListPostTravailValid(listDb);
		Set set = (map_poste).entrySet(); 
		Iterator i = set.iterator();
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			poste_travail2.appendItem((String) me.getKey(),(String) me.getValue());
		}*/

		poste_travail2.setSelectedIndex(0);

	}
	
}
