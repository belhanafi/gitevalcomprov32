package Statistique.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ngi.zhighcharts.SimpleExtXYModel;
import org.ngi.zhighcharts.ZHighCharts;
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
import Statistique.bean.StatIndiceMoyPosteBean;
import Statistique.bean.StatMoyFamillePosteBean;
import Statistique.bean.StatTrancheAgePosteBean;
import Statistique.model.StatCotationEmployeModel;

public class StatIndiceMoyPosteActionHighchart extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	Combobox poste_travail;
	Combobox compagne;
	Combobox code_famille;
	ArrayList<StatCotationEmployeBean> ListeCotationEmploye;
	String selectedEmploye;
	String selectedCompagne;
	StatCotationEmployeBean selectedBean;

	Map map_poste=null;
	Map map_compagne=null;
	Map map_famille=null;
	Label message;
	private SimpleExtXYModel dataChart10; 
	private ZHighCharts chart10;
	Component comp1;


	public StatIndiceMoyPosteActionHighchart()
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
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			compagne.appendItem((String) me.getKey());
		}


		map_famille=cotationMoel.getListFamille();
		Set set1 = (map_famille).entrySet(); 
		Iterator i1 = set1.iterator();
		// Display elements
		while(i1.hasNext()) {
			Map.Entry me1 = (Map.Entry)i1.next();
			code_famille.appendItem((String) me1.getKey());
			//code_famile.appendItem("tqtqt");
		}



		// forcer la selection de la permiere ligne
		poste_travail.setVisible(false);
		if(compagne.getItemCount()>0)
			compagne.setSelectedIndex(0);
		code_famille.setVisible(false);

		comp1=comp;

	}

	@SuppressWarnings("static-access")
	public void onClick$downloadimage() 
	{
		//enregistrement du fichier
		Filedownload fichierdownload=new Filedownload();

		fichierdownload.save(image, "jpg", "Stat_IndiceMoyPoste.jpg");
	}



	public void onSelect$code_famille() throws SQLException	 {
		
		message.setValue("");


		String poste= (String) map_poste.get((String)poste_travail.getSelectedItem().getLabel());
		String famille= (String) map_famille.get((String)code_famille.getSelectedItem().getLabel());
		String compagne_id= (String) map_compagne.get((String)compagne.getSelectedItem().getLabel());
		try {
			onCreateBar(comp1 ,poste,famille,compagne_id,chart10) ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*CategoryModel catmodel = new SimpleCategoryModel();
		List charts=new ArrayList<CategoryModel>();
		StatIndiceMoyPosteBean cpb;
		Iterator it;
		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		List sect_items=cotationMoel.getIndiceMoyPoste(poste,famille);
		it = sect_items.iterator();
		if (sect_items.size()>0){
			mychart.setVisible(true);
			while (it.hasNext()){
				cpb  = (StatIndiceMoyPosteBean) it.next();

				catmodel.setValue(cpb.getCompetence(),(String)code_famille.getSelectedItem().getLabel(),cpb.getIndice_moy());
				//catmodel.setValue("IMI","indice de maitrise individuel",3);
				mychart.setModel(catmodel);



			}

			catmodel.setValue("IMMF","Indice de maitrise moyen par famille",cotationMoel.getIndiceMoyPerPoste(poste,famille));
			catmodel.setValue("IMG","Indice de maitrise global - IMG - ",cotationMoel.getIMGParPoste(poste));
			mychart.setModel(catmodel);

			ChartEngine d=mychart.getEngine();
			image=d.drawChart(mychart);
		}else{
			message.setVisible(true);
			message.setValue("Pas de résultat à afficher");
			mychart.setVisible(false);
		}*/

	}

	public void onSelect$compagne() throws SQLException
	{

		poste_travail.getItems().clear();
		poste_travail.setVisible(true);
		String compagne_id= (String) map_compagne.get((String)compagne.getSelectedItem().getLabel());
		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		map_poste=cotationMoel.getListPostTravailValid(compagne_id);
		Set set = (map_poste).entrySet(); 
		Iterator i = set.iterator();

		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			poste_travail.appendItem((String) me.getKey());
		}

		poste_travail.setSelectedIndex(0);



	}

	public void onSelect$poste_travail()  {

		code_famille.setVisible(true);
	}
	
	public void onCreateBar(Component comp ,String poste,String famille,String compagne_id,ZHighCharts chartComp ) throws Exception {

		dataChart10=null;
		dataChart10= new SimpleExtXYModel();


		chartComp = (ZHighCharts) comp.getFellow(chartComp.getContext());
		chartComp.setType("bar"); //Mandatory!
		chartComp.setTitle("Répartition des IMG par type de competences");
		chartComp.setSubTitle("");


		chartComp.setTooltipFormatter("function formatTooltip(obj){" +
				"return ''+obj.series.name +': '+ obj.y +' %';" +
				"}");
		chartComp.setYAxisTitle("Niveau de maitrise %");
		chartComp.setLegend("{" +
				"layout: 'vertical'," +
				"align: 'right'," +
				"verticalAlign: 'top'," +
				"x: -100," +
				"y: 100," +
				"floating: true," +
				"borderWidth: 1," +
				"backgroundColor: '#FFFFFF'," +
				"shadow: true" +
				"}");

		chartComp.setPlotOptions("{" +
				"bar: {" +
				"dataLabels: {" +
				"enabled: true" +
				"}" +
				"}" +
				"}");

		chartComp.setyAxisOptions("{" +
				"min: 0," +
				"labels: {" +
				"overflow: 'justify'" +
				"}" +
				"}");
		
		chartComp.setLegend("{" +
				"layout: 'horizontal'," +
				"align: 'center'," +
				"verticalAlign: 'bottom'," +
				"x: 200, " +
				"y: 10, " +
				"floating: true," +
				"backgroundColor: '#FFFFFF'," +
				"borderWidth: 1" +
				"}");
	
		
		chartComp.setModel(dataChart10);

		StatIndiceMoyPosteBean cpb;
		int counter=0;
		Iterator it;
		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		List sect_items=cotationMoel.getIndiceMoyPoste(poste,famille,compagne_id);
		it = sect_items.iterator();
		if (sect_items.size()>0){
		
			while (it.hasNext()){
				cpb  = (StatIndiceMoyPosteBean) it.next();

				/*catmodel.setValue(cpb.getFamille(),"Familles de compétence",cpb.getMoy_famille());
				//catmodel.setValue("IMI","indice de maitrise individuel",3);
				mychart.setModel(catmodel);*/
				//(cpb.getCompetence(),(String)code_famille.getSelectedItem().getLabel(),cpb.getIndice_moy())
				dataChart10.addValue(cpb.getCompetence(), counter, cpb.getIndice_moy());
				counter++;



			}
			
			dataChart10.addValue("Indice de maitrise moyen par famille",counter+1,cotationMoel.getIndiceMoyPerPoste(poste,famille));
			dataChart10.addValue("Indice de maitrise global - IMG - ",counter+2,cotationMoel.getIMGParPoste(poste));
			
			//String cat_final="'"+(String)code_famille.getSelectedItem().getLabel()+"',"+"'"+"Indice de maitrise moyen par famille"+"',"+"'"+"Indice de maitrise global - IMG - "+"'";
			String cat_final="'"+(String)code_famille.getSelectedItem().getLabel()+"'";
			
			chartComp.setxAxisOptions("{" +	"categories: [" +cat_final+	"]" + "}");

			/*dataChart10.addValue(cpb.getFamille(), counter, cpb.getMoy_famille());
			catmodel.setValue("IMG","Indice de maitrise global",cotationMoel.getIMGParPosteInter(poste,compagne_id));
			mychart.setModel(catmodel);

			ChartEngine d=mychart.getEngine();
			image=d.drawChart(mychart);*/
		}else{
			message.setVisible(true);
			message.setValue("Pas de résultat à afficher");
			mychart.setVisible(false);
		}
		


	}


}
