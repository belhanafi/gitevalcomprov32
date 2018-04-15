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

import Statistique.bean.EmployeCadreBean;
import Statistique.bean.StatTrancheAgePosteBean;
import Statistique.model.EmployeModel;

public class StatPopPosteTravail extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	
	public StatPopPosteTravail()
	{
		
	}

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		EmployeModel init=new EmployeModel();
		EmployeCadreBean cpb;
		String typetranche="";
		
		Iterator it;
		List sect_items=init.getNombreEmployesCadre();
        it = sect_items.iterator();
        PieModel piemodel = new SimplePieModel();
        
		while (it.hasNext()){
	 		cpb  = (EmployeCadreBean) it.next();
	 		/*if (cpb.getIs_cadre().equalsIgnoreCase("2")){
	 			typetranche="Non cadre";
	 		}
	 		
	 		else{
	 			typetranche="Cadre";
	 		}
	 	*/
	 		 
	 		piemodel.setValue(cpb.getIs_cadre(),cpb.getPourcentage());
	 		  mychart.setModel(piemodel);
			
		}
		        
        //ces instructions permettent de récuperer l'objet image pour l'export
        
        ChartEngine d=mychart.getEngine();
		image=d.drawChart(mychart);
		
	}
	
	 public void onClick$downloadimage() {


			//enregistrement du fichier
			Filedownload fichierdownload=new Filedownload();

			fichierdownload.save(image, "jpg", "Stat_Population_Poste_Travail.jpg");
		}
}
