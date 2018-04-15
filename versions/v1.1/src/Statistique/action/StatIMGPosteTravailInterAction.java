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
import org.zkoss.zul.impl.ChartEngine;

import Statistique.bean.EmployeMoyFamBean;
import Statistique.bean.StatCotationEmployeBean;
import Statistique.bean.StatMoyFamillePosteBean;
import Statistique.bean.StatTrancheAgePosteBean;
import Statistique.model.StatCotationEmployeModel;

public class StatIMGPosteTravailInterAction extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	Combobox poste_travail;
	Combobox compagne;
	ArrayList<StatCotationEmployeBean> ListeCotationEmploye;
	String selectedEmploye;
	String selectedCompagne;
	StatCotationEmployeBean selectedBean;
	
	Map map_poste=null;
	Map map_compagne=null;

	
	public StatIMGPosteTravailInterAction()
	{
		
	}

	public void doAfterCompose(Component comp) throws Exception 
	{
        
		super.doAfterCompose(comp);
		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		//ListeCotationEmploye=cotationMoel.InitialiserStatCotationEmploye();
		
		
		
		
		map_compagne=cotationMoel.getListCompagneInter();
		
	  	Set set = (map_compagne).entrySet(); 
	  	Iterator i = set.iterator();
		// Display elements
		while(i.hasNext()) {
		  Map.Entry me = (Map.Entry)i.next();
		  compagne.appendItem((String) me.getKey());
	   }
		// forcer la selection de la permiere ligne
		poste_travail.setVisible(false);
		if(compagne.getItemCount()>0)
			compagne.setSelectedIndex(0);
		
	
	}
	
	 @SuppressWarnings("static-access")
	 public void onClick$downloadimage() 
	 {
			//enregistrement du fichier
			Filedownload fichierdownload=new Filedownload();

			fichierdownload.save(image, "jpg", "Stat_Population_Age.jpg");
	}
	 

	 
	public void onSelect$poste_travail() throws SQLException	 {
		
		     String poste= (String) map_poste.get((String)poste_travail.getSelectedItem().getLabel());
		     String compagne_id= (String) map_compagne.get((String)compagne.getSelectedItem().getLabel());
		     
		     CategoryModel catmodel = new SimpleCategoryModel();
		     List charts=new ArrayList<CategoryModel>();
		     StatMoyFamillePosteBean cpb;
			 Iterator it;
			 StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
			 List sect_items=cotationMoel.getListPosteMoyFamIntr(poste,compagne_id);
	         it = sect_items.iterator();
	         while (it.hasNext()){
		 		cpb  = (StatMoyFamillePosteBean) it.next();
		 		
		 		 catmodel.setValue(cpb.getFamille(),"Familles de compétence",cpb.getMoy_famille());
		 		//catmodel.setValue("IMI","indice de maitrise individuel",3);
		 		mychart.setModel(catmodel);
		 		
		 		
				
			}
			
			catmodel.setValue("IMG","Indice de maitrise global",cotationMoel.getIMGParPosteInter(poste,compagne_id));
	 		mychart.setModel(catmodel);
			
			ChartEngine d=mychart.getEngine();
			image=d.drawChart(mychart);
		
	 }
	 
	 public void onSelect$compagne() throws SQLException
	 {
		 poste_travail.getItems().clear();
		 poste_travail.setVisible(true);
		 String compagne_id= (String) map_compagne.get((String)compagne.getSelectedItem().getLabel());
		 StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		 map_poste=cotationMoel.getListPostTravailValidInt(compagne_id);
		 Set set = (map_poste).entrySet(); 
		 Iterator i = set.iterator();
			
			while(i.hasNext()) {
				Map.Entry me = (Map.Entry)i.next();
				poste_travail.appendItem((String) me.getKey());
				}
			
			poste_travail.setSelectedIndex(0);
			
		 
		 
	 }
}
