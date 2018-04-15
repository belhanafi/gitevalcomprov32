package kpi.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import kpi.bean.ListeCompagneVagueBean;
import kpi.bean.PerspectiveDevBean;
import kpi.model.KpiSyntheseModel;
import kpi.model.PerspectiveDeVModel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;

public class PerspectiveDevAction extends GenericForwardComposer{

	private static final long serialVersionUID = 1L;
	AnnotateDataBinder binder;
	Grid gridperspective;
	Columns compcolumn;
	Rows lignes;
	Group groups;
	HashMap <String, Radio> selectedRadio;
	HashMap <String, Radio> unselectedRadio;

	List<ListeCompagneVagueBean> model = new ArrayList<ListeCompagneVagueBean>();
	private ListeCompagneVagueBean selected;




	Listbox  listVaguelb;
	Window win;
	HashMap<String, HashMap<String, Integer>> listDb = null;


	public PerspectiveDevAction() {
	}


	@SuppressWarnings("deprecation")
	public void  doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);


		comp.setVariable(comp.getId() + "Ctrl", this, true);
		
		
		selectedRadio=new HashMap <String, Radio>();
		unselectedRadio=new HashMap <String, Radio>();
		
		KpiSyntheseModel init= new KpiSyntheseModel();
		model=init.getListeCampgneVague(); 

		binder = new AnnotateDataBinder(comp);


	

		binder.loadAll();



	}


	private void afficherGrid(HashMap<String,  ArrayList<PerspectiveDevBean>> list) {
		
		

		
		
		for (Entry<String,  ArrayList<PerspectiveDevBean>> entry : list.entrySet()) {

			int map_size=list.entrySet().size();
			String nom_groupe=entry.getKey();
			Group group = new Group(nom_groupe);
			group.setOpen(false);
			group.setTooltip("tooltipBuzones");
			Iterator itr=entry.getValue().iterator();
			while (itr.hasNext()){
				
				PerspectiveDevBean bean =(PerspectiveDevBean)itr.next();
						
				if (nom_groupe.trim().equalsIgnoreCase((bean.getFamille()+":"+bean.getLibelle_competence()).trim())){
					
					
					
					Row row1=new Row();
					row1.appendChild(new Label(bean.getLibelle_competence()));
					row1.appendChild(new Label(bean.getAptitude_observable()));
					row1.appendChild(new Label(String.valueOf(bean.getMoy_competence())));
					lignes.appendChild(group);
					lignes.appendChild(row1);
					
				}
				
				
				
			}


		}
		
	
		lignes.setParent(gridperspective);
		
		
		gridperspective.renderAll();

		binder.loadAll();

		gridperspective.renderAll();
		
		
		

		/*lignes.setParent(gridperspective);
			
		Group group = new Group("Mon groupe");
		group.setOpen(false);
		group.setTooltip("tooltipBuzones");
		
		Row row1=new Row();
		

		row1.appendChild(new Label("aaaa"));
		row1.appendChild(new Label("aaaa"));
		row1.appendChild(new Label("aaaa"));

		group.setId("2222");
		
		lignes.appendChild(group);
		lignes.appendChild(row1);
		
		Row row2=new Row();
		row2.appendChild(new Label("bbbb"));
		row2.appendChild(new Label("bbbb"));
		row2.appendChild(new Label("bbbb"));
		
		
		Group group2 = new Group("Mon groupe2");
		group2.setOpen(true);
		group2.setTooltip("tooltipBuzones");
		
		
		lignes.appendChild(group2);
		lignes.appendChild(row2);
		
	
		
		gridperspective.renderAll();
		
		binder.loadAll();*/

	}


	public void onClick$executer2() throws Exception {
		
		if (selectedRadio.size()==0){
			
			Messagebox.show(" Merci de selectionner une compgane !", "Information",Messagebox.OK, Messagebox.INFORMATION); 
			return;
			
		}
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
		PerspectiveDeVModel init=new PerspectiveDeVModel();
		HashMap<String,  ArrayList<PerspectiveDevBean>> list=init.getResultatIMIComp(listDb);
		
		afficherGrid(list);



	}

	public void onModifyRadio1(ForwardEvent event){
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

}
