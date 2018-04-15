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
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.PieModel;
import org.zkoss.zul.SimpleCategoryModel;
import org.zkoss.zul.SimplePieModel;
import org.zkoss.zul.impl.ChartEngine;

import Statistique.bean.EmployeCadreBean;
import Statistique.bean.EmployeFormationBean;
import Statistique.bean.StatTrancheAgePosteBean;
import Statistique.model.EmployeModel;

public class StatPopAncien extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	Listbox  comp_struct_ent_list;
	Label message;
	Listbox comp_poste_list;
	Label label_poste;


	public StatPopAncien()
	{

	}

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);



		CategoryModel catmodel = new SimpleCategoryModel();

		EmployeModel init=new EmployeModel();
		StatTrancheAgePosteBean cpb;
		String typetranche="";
		List charts=new ArrayList<CategoryModel>();
		String code_structure_str="";
		String code_poste="";

		// Affichage de la liste postes de travail
		comp_poste_list.appendItem("Tous Postes Travail","-1");

		Set set_ent = (init.getStructEntList()).entrySet(); 
		Iterator itr_ = set_ent.iterator();
		// Affichage de la liste des strcutures entreprise
		comp_struct_ent_list.appendItem("Toutes Structures","-1");
		while(itr_.hasNext()) {
			Map.Entry me = (Map.Entry)itr_.next();

			comp_struct_ent_list.appendItem((String) me.getKey(),(String) me.getValue());

			//profilemodel.add((String) me.getKey());
		}
		if(comp_struct_ent_list.getItemCount()>0){
			comp_struct_ent_list.setSelectedIndex(0);
			code_structure_str=(String) comp_struct_ent_list.getSelectedItem().getValue();
		}

		if(comp_poste_list.getItemCount()>0){
			comp_poste_list.setSelectedIndex(0);
			code_poste=(String) comp_poste_list.getSelectedItem().getValue();
		}

		Iterator it;
		List sect_items=init.getNombreEmployesEnciente(code_structure_str,code_poste);
		it = sect_items.iterator();
		while (it.hasNext()){
			cpb  = (StatTrancheAgePosteBean) it.next();
			if (cpb.getTranche().equalsIgnoreCase("1")){
				typetranche="entre 1 et 10 ans";
			}
			else if (cpb.getTranche().equalsIgnoreCase("2")){
				typetranche="entre 10 et 20 ans";
			}

			else{
				typetranche="Superieur à 20 ans";
			}
			catmodel.setValue(cpb.getIntitule_poste(),typetranche,cpb.getPourcentage());
			mychart.setModel(catmodel);


		}


		//ces instructions permettent de récuperer l'objet image pour l'export
		try
		{
			ChartEngine d=mychart.getEngine();
			mychart.setTitle("Structure: "+(String) comp_struct_ent_list.getSelectedItem().getLabel());

			image=d.drawChart(mychart);
		}
		catch(Exception e)
		{
			//exception lors de l'affichage du jfreechart car base vide
		}

	}

	public void onClick$downloadimage() {


		//enregistrement du fichier
		Filedownload fichierdownload=new Filedownload();

		fichierdownload.save(image, "jpg", "Stat_Population_ancienneté.jpg");
	}


	public void onSelect$comp_struct_ent_list() throws SQLException {

		message.setValue("");
		mychart.setVisible(false);

		comp_poste_list.getItems().clear();
		comp_poste_list.appendItem("Tous Postes Travail","-1");

		EmployeModel init=new EmployeModel();
		Map map = new HashMap();
		Map map_structure = new HashMap();
		//map_structure=init.getStructEntList();
		//EmployeCadreBean cpb;
		String code_structure=(String) comp_struct_ent_list.getSelectedItem().getValue();



		if ((code_structure.equalsIgnoreCase("-1"))){
			comp_poste_list.getItems().clear();
			comp_poste_list.appendItem("Tous Postes Travail","-1");
			generateChart( "-1", "-1");

		}
		else {
			Set set_ent = (init.getListPosteStructure(code_structure)).entrySet(); 
			Iterator itr_ = set_ent.iterator();
			//si la structure ne cotient aucun poste de travail. on cache la liste box poste 
			if (set_ent.size()==0){
				comp_poste_list.setVisible(false);
				label_poste.setVisible(false);
			}else{
				comp_poste_list.setVisible(true);
				label_poste.setVisible(true);
			}

			while(itr_.hasNext()) {
				Map.Entry me = (Map.Entry)itr_.next();
				comp_poste_list.appendItem((String) me.getKey(),(String) me.getValue());

				//profilemodel.add((String) me.getKey());
			}
		}


	}

	public void onSelect$comp_poste_list() throws SQLException {

		message.setValue("");
		

		EmployeModel init=new EmployeModel();
		String code_structure=(String) comp_struct_ent_list.getSelectedItem().getValue();
		String code_poste=(String) comp_poste_list.getSelectedItem().getValue();
		generateChart( code_structure, code_poste);

	}


	private void generateChart(String code_structure,String code_poste) throws SQLException {

		CategoryModel catmodel = new SimpleCategoryModel();
		Iterator it;
		EmployeModel init=new EmployeModel();
		StatTrancheAgePosteBean cpb;
		String typetranche="";
		List charts=new ArrayList<CategoryModel>();
		String libelle_structure=comp_struct_ent_list.getItemAtIndex(comp_struct_ent_list.getSelectedItem().getIndex()).getLabel();
		catmodel=new SimpleCategoryModel();
		List sect_items=init.getNombreEmployesEnciente(code_structure,code_poste);
		it = sect_items.iterator();
		if (sect_items.size()>0){
			mychart.setVisible(true);

			while (it.hasNext()){
				cpb  = (StatTrancheAgePosteBean) it.next();
				if (cpb.getTranche().equalsIgnoreCase("1")){
					typetranche="entre 1 et 10 ans";
				}
				else if (cpb.getTranche().equalsIgnoreCase("2")){
					typetranche="entre 10 et 20 ans";
				}

				else{
					typetranche="Superieur à 20 ans";
				}
				catmodel.setValue(cpb.getIntitule_poste(),typetranche,cpb.getPourcentage());
				mychart.setModel(catmodel);


			}
			//ces instructions permettent de récuperer l'objet image pour l'export
			try
			{
				ChartEngine d=mychart.getEngine();
				mychart.setTitle("Structure: "+(String) comp_struct_ent_list.getSelectedItem().getLabel());

				image=d.drawChart(mychart);
			}
			catch(Exception e)
			{
				//exception lors de l'affichage du jfreechart car base vide
			}


		}else{
			message.setVisible(true);
			message.setValue("Pas de résultat à afficher");
			mychart.setVisible(false);
		}
	}



}