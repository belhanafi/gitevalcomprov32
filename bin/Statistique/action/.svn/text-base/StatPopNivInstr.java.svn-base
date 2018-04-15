package Statistique.action;

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
import Statistique.bean.EmployeFormationBean;
import Statistique.model.EmployeModel;

public class StatPopNivInstr extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	
	public StatPopNivInstr()
	{
		
	}

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		EmployeModel init=new EmployeModel();
		EmployeFormationBean cpb;
		String typetranche="";
		
		Iterator it;
		List sect_items=init.getNombreEmployesNivForm();
        it = sect_items.iterator();
        PieModel piemodel = new SimplePieModel();
        
		while (it.hasNext()){
	 		cpb  = (EmployeFormationBean) it.next();
	 		
	 		   piemodel.setValue(cpb.getNiveau(),cpb.getPourcentage());
	 		  mychart.setModel(piemodel);
			
		}
	    //ces instructions permettent de récuperer l'objet image pour l'export
        
        ChartEngine d=mychart.getEngine();
		image=d.drawChart(mychart);
		
	}
	
	 public void onClick$downloadimage() {


			//enregistrement du fichier
			Filedownload fichierdownload=new Filedownload();

			fichierdownload.save(image, "jpg", "Stat_Population_Niveau_Instruction.jpg");
		}
}
