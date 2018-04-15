package Statistique.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kpi.bean.TauxCouvertureCompagneBean;
import kpi.model.KpiSyntheseModel;

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
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.ChartEngine;

import compagne.model.SuiviCompagneModel;
import Statistique.bean.EmployeCadreBean;
import Statistique.bean.StatTrancheAgePosteBean;
import Statistique.model.EmployeModel;

import org.jfree.chart.JFreeChart;
import org.ngi.zhighcharts.SimpleExtXYModel;
import org.ngi.zhighcharts.ZHighCharts;

public class StatPopPosteTravail extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	Listbox  comp_struct_ent_list;
	Window winGraph2;
	Label message;
	Listbox comp_poste_list;
	Label label_poste;
	private SimpleExtXYModel dataChartMode3; 



	public StatPopPosteTravail()
	{

	}

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		EmployeModel init=new EmployeModel();
		String typetranche="";
		String code_structure_str="";
		String code_poste="";
		EmployeCadreBean cpb;
		
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
		List sect_items=init.getNombreEmployesCadre(code_structure_str,code_poste);
		it = sect_items.iterator();
		PieModel piemodel = new SimplePieModel();
		
		while (it.hasNext()){
			cpb  = (EmployeCadreBean) it.next();

			piemodel.setValue(cpb.getIs_cadre(),cpb.getPourcentage());
			mychart.setModel(piemodel);

		}

		//ces instructions permettent de r�cuperer l'objet image pour l'export
		try
		{
			ChartEngine d=mychart.getEngine();
			
			mychart.setTitle("Structure: "+(String) comp_struct_ent_list.getSelectedItem().getLabel());

			image=d.drawChart(mychart);
			
			mychart.setBorder("#060000");
		}
		catch(Exception e)
		{
			//exception lors de l'affichage du jfreechart car base vide
		}



	}

	public void onClick$downloadimage() {


		//enregistrement du fichier
		Filedownload fichierdownload=new Filedownload();

		fichierdownload.save(image, "jpg", "Stat_Population_Poste_Travail.jpg");
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
			generateChart(init, "-1", "-1");

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
		generateChart(init, code_structure, code_poste);

	}

	private void generateChart(EmployeModel init, String code_structure,
			String code_poste) throws SQLException {
		EmployeCadreBean cpb;
		Iterator it;
		List sect_items=init.getNombreEmployesCadre(code_structure,code_poste);
		it = sect_items.iterator();
		PieModel piemodel = new SimplePieModel();

		if (sect_items.size()>0){
			
			mychart.setVisible(true);

			while (it.hasNext()){
				cpb  = (EmployeCadreBean) it.next();

				piemodel.setValue(cpb.getIs_cadre(),cpb.getPourcentage());
				mychart.setModel(piemodel);

			}

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
			message.setValue("Pas de r�sultat � afficher");
			mychart.setVisible(false);
		}
	}
	
	
	
}
