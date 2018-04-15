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
import Statistique.bean.StatEvolIMGBean;
import Statistique.bean.StatEvolIMIEmployeBean;
import Statistique.bean.StatTrancheAgePosteBean;
import Statistique.model.LineChartEngine;
import Statistique.model.StatCotationEmployeModel;

public class StatEvolIMGPosteTravailAction extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Chart mychart;
	Flashchart mychart;
	byte[] image;
	Combobox poste_travail;
	ArrayList<StatCotationEmployeBean> ListeCotationEmploye;
	String selectedEmploye;
	String selectedCompagne;
	StatCotationEmployeBean selectedBean;
	LineChartEngine lce =new LineChartEngine();

	Map map_poste=null;
	Label message;



	public StatEvolIMGPosteTravailAction()
	{

	}

	public void doAfterCompose(Component comp) throws Exception 
	{

		super.doAfterCompose(comp);
		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		//ListeCotationEmploye=cotationMoel.InitialiserStatCotationEmploye();

		map_poste=cotationMoel.getListPostTravailValid();
		Set set = (map_poste).entrySet(); 
		Iterator i = set.iterator();

		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			poste_travail.appendItem((String) me.getKey());
		}

		if(poste_travail.getItemCount()>0)
			poste_travail.setSelectedIndex(0);



	}




	@SuppressWarnings("static-access")
	public void onClick$downloadimage() 
	{
		//enregistrement du fichier
		Filedownload fichierdownload=new Filedownload();

		fichierdownload.save(image, "jpg", "Stat_Population_Age.jpg");
	}



	public void onSelect$poste_travail() throws SQLException	 {
		
		message.setValue("");


		String code_poste= (String) map_poste.get((String)poste_travail.getSelectedItem().getLabel());

		SimpleCategoryModel catmodel = new SimpleCategoryModel();

		StatEvolIMGBean cpb;
		Iterator it;
		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		List sect_items=cotationMoel.getEvolIMGPoste(code_poste);
		it = sect_items.iterator();
		float imi=0;
		if (sect_items.size()>0){
			mychart.setVisible(true);
			while (it.hasNext()){
				cpb  = (StatEvolIMGBean)it.next();
				catmodel.setValue("",cpb.getDate_evol(),cpb.getImg());

				//catmodel.setValue("IMI","indice de maitrise individuel",3);
				//mychart.setModel(catmodel);



			}

			mychart.setModel(catmodel);
			mychart.setType("line");

		}
		else{
			message.setVisible(true);
			message.setValue("Pas de résultat à afficher");
			mychart.setVisible(false);
		}


	}

}
