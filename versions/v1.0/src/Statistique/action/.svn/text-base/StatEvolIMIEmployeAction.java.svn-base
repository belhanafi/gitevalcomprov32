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
import Statistique.model.LineChartEngine;
import Statistique.model.StatCotationEmployeModel;

public class StatEvolIMIEmployeAction extends  GenericForwardComposer{

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

	
	public StatEvolIMIEmployeAction()
	{
		
	}

	public void doAfterCompose(Component comp) throws Exception 
	{
        
		super.doAfterCompose(comp);
		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		//ListeCotationEmploye=cotationMoel.InitialiserStatCotationEmploye();
		
		map_compte=cotationMoel.getListEmployesFichValid();
		Set set = (map_compte).entrySet(); 
		Iterator i = set.iterator();
		
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			nom_employe.appendItem((String) me.getKey());
			}
		
		nom_employe.setVisible(true);
		nom_employe.setSelectedIndex(0);
		
		
	}
	
	 @SuppressWarnings("static-access")
	 public void onClick$downloadimage() 
	 {
			//enregistrement du fichier
			Filedownload fichierdownload=new Filedownload();

			fichierdownload.save(image, "jpg", "Stat_Population_Age.jpg");
	}
	 

	 
	public void onSelect$nom_employe() throws SQLException	 {
		
		     String employe_id= (String) map_compte.get((String)nom_employe.getSelectedItem().getLabel());
		     
		     CategoryModel catmodel = new SimpleCategoryModel();
	       
		     StatEvolIMIEmployeBean cpb;
			 Iterator it;
			 StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
			 List sect_items=cotationMoel.getEvolIMIEmploye(employe_id);
	         it = sect_items.iterator();
	         float imi=0;
			while (it.hasNext()){
		 		cpb  = (StatEvolIMIEmployeBean) it.next();
		 		catmodel.setValue("",cpb.getDate_evol(),cpb.getImi());
		 		
		 		//catmodel.setValue("IMI","indice de maitrise individuel",3);
		 		mychart.setModel(catmodel);
		 		
		 		
				
			}
			
			mychart.setModel(catmodel);
			mychart.setYAxis("IMI"); 
			
			ChartEngine d=mychart.getEngine();
			image=d.drawChart(mychart);
		
	 }
	 
	/*public void onSelect$compagne() throws SQLException
	 {
	
		 nom_employe.getItems().clear();
		 String compagne_id= (String) map_compagne.get((String)compagne.getSelectedItem().getLabel());
		 StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		    map_compte=cotationMoel.getListEmployesFichValid(compagne_id);
		    
		    if (map_compte.size()!=0){
		    	Set set = (map_compte).entrySet(); 
				Iterator i = set.iterator();
				
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
	 }*/
}
