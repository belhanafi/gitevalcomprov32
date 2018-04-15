package Statistique.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.CategoryModel;
import org.zkoss.zul.Chart;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Flashchart;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleCategoryModel;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.SimpleXYModel;
import org.zkoss.zul.XYModel;
import org.zkoss.zul.impl.ChartEngine;

import Statistique.bean.EmployeMoyFamBean;
import Statistique.bean.StatCotationEmployeBean;
import Statistique.bean.StatEvolIMIEmployeBean;
import Statistique.bean.StatTrancheAgePosteBean;
import Statistique.model.EmployeModel;
import Statistique.model.LineChartEngine;
import Statistique.model.StatCotationEmployeModel;

public class StatEvolIMIEmployeAction extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Chart mychart;
	Flashchart mychart;
	byte[] image;
	Combobox nom_employe;
	Combobox compagne;
	ArrayList<StatCotationEmployeBean> ListeCotationEmploye;
	String selectedEmploye;
	String selectedCompagne;
	StatCotationEmployeBean selectedBean;
	Listbox  comp_struct_ent_list;


	Map map_compte=null;
	Map map_compagne=null;
	Label message;



	public StatEvolIMIEmployeAction()
	{

	}

	public void doAfterCompose(Component comp) throws Exception 
	{

		super.doAfterCompose(comp);
		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		//ListeCotationEmploye=cotationMoel.InitialiserStatCotationEmploye();

		String code_structure_str="";
		EmployeModel init=new EmployeModel();
		Set set_ent = (init.getStructEntList()).entrySet(); 
		Iterator itr_ = set_ent.iterator();
		// Affichage de la liste des strcutures entreprise
		comp_struct_ent_list.appendItem("Selectionner Structure","-1");
		while(itr_.hasNext()) {
			Map.Entry me = (Map.Entry)itr_.next();

			comp_struct_ent_list.appendItem((String) me.getKey(),(String) me.getValue());

			//profilemodel.add((String) me.getKey());
		}
		if(comp_struct_ent_list.getItemCount()>0){
			comp_struct_ent_list.setSelectedIndex(0);
			code_structure_str=(String) comp_struct_ent_list.getSelectedItem().getValue();
		}



	}

	@SuppressWarnings("static-access")
	public void onClick$downloadimage() 
	{
		//enregistrement du fichier
		Filedownload fichierdownload=new Filedownload();

		fichierdownload.save(image, "jpg", "Stat_Population_Age.jpg");
	}



	public void onSelect$nom_employe() throws SQLException	 {
		message.setValue("");


		String employe_id= (String) map_compte.get((String)nom_employe.getSelectedItem().getLabel());

		SimpleCategoryModel  catmodel = new SimpleCategoryModel();

		StatEvolIMIEmployeBean cpb;
		Iterator it;
		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		List sect_items=cotationMoel.getEvolIMIEmploye(employe_id);
		it = sect_items.iterator();
		float imi=0;
		if (sect_items.size()>0){
			mychart.setVisible(true);
			while (it.hasNext()){
				cpb  = (StatEvolIMIEmployeBean) it.next();
				catmodel.setValue("",cpb.getDate_evol(),cpb.getImi());
			}

			mychart.setModel(catmodel);
			//mychart.setYAxis("IMI"); 
			//ChartEngine d=mychart.getEngine();
			//image=d.drawChart(mychart);
			mychart.setType("line");
		}else{
			message.setVisible(true);
			message.setValue("Pas de résultat à afficher");
			mychart.setVisible(false);

		}

	}


	public void onSelect$comp_struct_ent_list() throws SQLException {

		message.setValue("");

		CategoryModel catmodel = new SimpleCategoryModel();

		EmployeModel init=new EmployeModel();
		StatTrancheAgePosteBean cpb;
		List charts=new ArrayList<CategoryModel>();
		String code_structure=(String) comp_struct_ent_list.getSelectedItem().getValue();

		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();

		if (map_compte!=null){
			map_compte.clear();
			nom_employe.getItems().clear();
		}


		map_compte=cotationMoel.getListEmployesFichValid(code_structure);

		if (map_compte.size()!=0){
			Set set = (map_compte).entrySet(); 
			Iterator i = set.iterator();

			nom_employe.appendItem("Selectionner Employé");

			while(i.hasNext()) {
				Map.Entry me = (Map.Entry)i.next();
				nom_employe.appendItem((String) me.getKey());
			}

			nom_employe.setVisible(true);
			nom_employe.setSelectedIndex(0);

		}

		else {
			nom_employe.setVisible(false);
		}

	}
}
