package Statistique.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.CategoryModel;
import org.zkoss.zul.Chart;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.PieModel;
import org.zkoss.zul.SimpleCategoryModel;
import org.zkoss.zul.SimplePieModel;
import org.zkoss.zul.impl.ChartEngine;

import Statistique.bean.StatTrancheAgePosteBean;
import Statistique.model.EmployeModel;

public class StatPopAncien extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	
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
		
		Iterator it;
		List sect_items=init.getNombreEmployesEnciente();
        it = sect_items.iterator();
		while (it.hasNext()){
	 		cpb  = (StatTrancheAgePosteBean) it.next();
	 		if (cpb.getTranche().equalsIgnoreCase("1")){
	 			typetranche="entre 1 et 15 ans";
	 		}
	 		else if (cpb.getTranche().equalsIgnoreCase("2")){
	 			typetranche="entre 16 et 30 ans";
	 		}
	 		
	 		else{
	 			typetranche="Superieur à 31 ans";
	 		}
	 		 catmodel.setValue(cpb.getIntitule_poste(),typetranche,cpb.getPourcentage());
	 		mychart.setModel(catmodel);
	 		
			
		}
		
		
     /*   catmodel.setValue("Directeur de pôle", "Entre 1 et 15 ans", new Integer(60));
        catmodel.setValue("Directeur de pôle", "Entre 16 et 30 ans", new Integer(20));
        catmodel.setValue("Directeur de pôle", "Supérieur à 31", new Integer(10));
        
        
        catmodel.setValue("comptable", "Entre 1 et 15 ans", new Integer(55));
        catmodel.setValue("comptable", "Entre 16 et 30 ans", new Integer(30));
        catmodel.setValue("comptable", "Supérieur à 31", new Integer(5));
        
        mychart.setModel(catmodel);*/
        //ces instructions permettent de récuperer l'objet image pour l'export
        
        ChartEngine d=mychart.getEngine();
		image=d.drawChart(mychart);
		
	}
	
	 public void onClick$downloadimage() {


			//enregistrement du fichier
			Filedownload fichierdownload=new Filedownload();

			fichierdownload.save(image, "jpg", "Stat_Population_ancienneté.jpg");
		}
}
