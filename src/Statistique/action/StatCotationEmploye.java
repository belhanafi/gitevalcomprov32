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
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleCategoryModel;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.impl.ChartEngine;

import Statistique.bean.EmployeMoyFamBean;
import Statistique.bean.StatCotationEmployeBean;
import Statistique.bean.StatTrancheAgePosteBean;
import Statistique.model.EmployeModel;
import Statistique.model.StatCotationEmployeModel;

public class StatCotationEmploye extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	Combobox nom_employe;
	Combobox compagne;
	ArrayList<StatCotationEmployeBean> ListeCotationEmploye;
	String selectedEmploye;
	String selectedCompagne;
	StatCotationEmployeBean selectedBean;

	Map map_compte=null;
	Map map_compagne=null;
	Listbox  comp_struct_ent_list;
	String compagne_id="";
	Label message;






	public StatCotationEmploye()
	{

	}

	public void doAfterCompose(Component comp) throws Exception 
	{

		super.doAfterCompose(comp);
		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		//ListeCotationEmploye=cotationMoel.InitialiserStatCotationEmploye();

		map_compagne=cotationMoel.getListCompagneValid();

		Set set = (map_compagne).entrySet(); 
		Iterator i = set.iterator();
		// Display elements
		compagne.appendItem("Liste Compagnes");

		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			compagne.appendItem((String) me.getKey());
		}
		// forcer la selection de la permiere ligne
		nom_employe.setVisible(false);
		if(compagne.getItemCount()>0)
			compagne.setSelectedIndex(0);

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

		CategoryModel catmodel = new SimpleCategoryModel();
		List charts=new ArrayList<CategoryModel>();
		EmployeMoyFamBean cpb;
		Iterator it;
		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		List sect_items=cotationMoel.getListEmployesMoyFam(employe_id);
		it = sect_items.iterator();
		float imi=0;
		if (sect_items.size()>0){
			mychart.setVisible(true);
			while (it.hasNext()){
				cpb  = (EmployeMoyFamBean) it.next();
				imi=(float) cpb.getImi();
				catmodel.setValue(cpb.getCode_famille(),"Familles de compétence",cpb.getMoy_famille());
				//catmodel.setValue("IMI","indice de maitrise individuel",3);
				mychart.setModel(catmodel);



			}

			catmodel.setValue("IMI","Indice de maitrise individuel",imi);
			mychart.setModel(catmodel);

			ChartEngine d=mychart.getEngine();
			mychart.setTitle("  Structure: "+(String) comp_struct_ent_list.getSelectedItem().getLabel() +"  Employé:  "+ (String)nom_employe.getSelectedItem().getLabel());
			image=d.drawChart(mychart);
		}else{
			message.setVisible(true);
			message.setValue("Pas de résultat à afficher");
			mychart.setVisible(false);
		}

	}

	public void onSelect$compagne() throws SQLException
	{

		nom_employe.getItems().clear();
		compagne_id= (String) map_compagne.get((String)compagne.getSelectedItem().getLabel());
	}


	public void onSelect$comp_struct_ent_list() throws SQLException {
		
		message.setValue("");


		CategoryModel catmodel = new SimpleCategoryModel();

		EmployeModel init=new EmployeModel();
		StatTrancheAgePosteBean cpb;
		List charts=new ArrayList<CategoryModel>();

		if (map_compte!=null){
			map_compte.clear();
			nom_employe.getItems().clear();
		}

		String code_structure=(String) comp_struct_ent_list.getSelectedItem().getValue();

		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		map_compte=cotationMoel.getListEmployesFichValid(compagne_id,code_structure);

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

	public String getCompagne_id() {
		return compagne_id;
	}

	public void setCompagne_id(String compagne_id) {
		this.compagne_id = compagne_id;
	}
}
