package kpi.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kpi.bean.ListeCompagneVagueBean;
import kpi.model.KpiSyntheseModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radio;
import common.ApplicationSession;

public class SynthGlobalPge1Action extends GenericForwardComposer implements EventListener {

	private static final long serialVersionUID = 1L;
	private Include iframe;
	private Include iframepg2;
	private Include iframeWord;
	private Include iframepg2Word;
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


	public void onClick$executerpg2() throws Exception {


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

		//ApplicationFacade.getInstance().setListDb(listDb);


		//System.out.println("AVANT"+ApplicationFacade.getInstance().getClient_database_id());

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");

		applicationSession.setListDb(listDb);

		Sessions.getCurrent().setAttribute("APPLICATION_ATTRIBUTE", applicationSession);

		String url = "/run?__report=synthGlobalPge2.rptdesign&formatRapport=html";
		iframepg2.setSrc(url);

		iframepg2.invalidate();


	}

	public void onClick$btReport() throws Exception
	{

		String url = "/run?__report=synthGlobalPge1.rptdesign&formatRapport=doc";
		iframeWord.setSrc(url);
		iframeWord.invalidate();
	}

	public void onClick$btReportpg2() throws Exception
	{

		String url = "/run?__report=synthGlobalPge2.rptdesign&formatRapport=doc";
		iframepg2Word.setSrc(url);
		iframepg2Word.invalidate();
	}


	/*public void onClick$btView() throws Exception
	{
		loadParams();
		String url = "/run?__report=synthGlobalPge1.rptdesign&formatRapport=html";
		iframe.setSrc(url);
	}*/


	public List<ListeCompagneVagueBean> getModel() {
		return model;
	}
	public ListeCompagneVagueBean getSelected() {
		return selected;
	}

	public void setSelected(ListeCompagneVagueBean selected) {
		this.selected = selected;
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


	public void onModifyRadio(ForwardEvent event){
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

	}
}
