package kpi.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import kpi.bean.AnalSynthRadarBean;
import kpi.bean.DureeMoyenneBean;
import kpi.bean.KPIAgeExpBean;
import kpi.bean.KpiIMIAgeBean;
import kpi.bean.ListDureeCotatLowBean;
import kpi.bean.ListeCompagneVagueBean;
import kpi.bean.TauxCouvertureCompagneBean;
import kpi.model.KpiSyntheseModel;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.ngi.zhighcharts.SimpleExtXYModel;
import org.ngi.zhighcharts.ZHighCharts;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.SimplePieModel;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import compagne.bean.EmailEvaluateurBean;
import compagne.bean.MatriceCotationBean;
import compagne.model.ResultatEvaluationModel;
import Statistique.model.StatCotationEmployeModel;
import administration.bean.FichePosteBean;




public class AnalyseSyntheseAction extends GenericForwardComposer implements EventListener {
	@SuppressWarnings("deprecation")
	Listbox comp_list;
	Integer compagne_id=0;
	AnnotateDataBinder binder;
	private SimplePieModel pieModel = new SimplePieModel();
	private SimplePieModel pieModel1 = new SimplePieModel();
	

	List<ListeCompagneVagueBean> model = new ArrayList<ListeCompagneVagueBean>();
	List<ListeCompagneVagueBean> model_filtre = new ArrayList<ListeCompagneVagueBean>();
	HashMap <String, Checkbox> selectedCheckBox;
	HashMap <String, Checkbox> unselectedCheckBox;
	
	HashMap <String, Checkbox> selectedCheckBoxCot;
	HashMap <String, Checkbox> unselectedCheckBoxCot;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");


	HashMap <String, Radio> selectedRadio;
	HashMap <String, Radio> unselectedRadio;

	Listbox  listVaguelb;
	Listbox listVaguelb1;
	//Pie chart
	private ZHighCharts chartComp2;
	private ZHighCharts chartComp3;
	private ZHighCharts chartComp4;
	private ZHighCharts chartComp5 ;
	private ZHighCharts chartComp6 ;
	private ZHighCharts chartComp7 ;
	//private ZHighCharts chartComp8 ;
	private ZHighCharts chartAgeExpIMIScattered ;
	private ZHighCharts chartdureeMoyenne;


	private ListeCompagneVagueBean selected;
	Component comp1;
	private SimpleExtXYModel dataChartMode3; 
	private SimpleExtXYModel dataChartMode4 ; 
	private SimpleExtXYModel dataChartMode6 ;
	private SimpleExtXYModel dataChartMode7;
	private SimpleExtXYModel dataChartAgeExpIMIScattered;
	private SimpleExtXYModel datadureeMoyenne;

	private Listbox poste_travail;
	private Listbox poste_travail1;
	Map map_poste=null;
	HashMap<String, HashMap<String, Integer>> listDb = null;
	Window win;
	Tabbox tb;
	Button executeexp;







	@SuppressWarnings("deprecation")
	public void  doAfterCompose(Component comp) throws Exception {

		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		selectedCheckBox=new HashMap <String, Checkbox>();
		unselectedCheckBox=new HashMap <String, Checkbox>();
		
		selectedCheckBoxCot=new HashMap <String, Checkbox>();
		unselectedCheckBoxCot=new HashMap <String, Checkbox>();
		
		selectedRadio=new HashMap <String, Radio>();
		unselectedRadio=new HashMap <String, Radio>();
		KpiSyntheseModel init= new KpiSyntheseModel();
		model=init.getListeCampgneVague(); 

		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

		if(listVaguelb.getItemCount()!=0)
			listVaguelb.setSelectedIndex(0);


		comp1=comp;

		


	}

	public void onCreatePie(Component comp ,HashMap<String, HashMap<String, Integer>> listdb, ZHighCharts chartComp) throws Exception {

		pieModel=null;
		pieModel = new SimplePieModel();

		//================================================================================
		// Pie chart
		//================================================================================
		//String  chartflow='"'+chartComp.getContext()+'"';
		chartComp = (ZHighCharts) comp.getFellow(chartComp.getContext());
		chartComp.setTitle("Répartition Employés par Métier");
		//chartComp2.setSubTitle("2010");
		chartComp.setType("pie");
		chartComp.setTooltipFormatter("function formatTooltip(obj){" +
				"return obj.key + '<br />: <b>'+obj.y+'%</b>'" +
				"}");
		chartComp.setPlotOptions("{" +
				"pie:{" +
				"allowPointSelect: true," +
				"cursor: 'pointer'," +
				"dataLabels: {" +
				"enabled: true, " +
				"color: '#000000'," +
				"connectorColor: '#000000'," +
				"formatter: function() {"+
				"return '<b>'+ this.point.name +'</b>: '+ this.percentage.toFixed(2) +' %';"+
				"}"+
				"}" +
				"}" +
				"}");							
		chartComp.setModel(pieModel);
		KpiSyntheseModel kpi=new KpiSyntheseModel();
		HashMap <String,Double> pie_map=new HashMap <String,Double>();

		pie_map=kpi.getNombreEmployesAllVague(listdb);

		for(Entry<String, Double> pair : pie_map.entrySet()){

			//Adding some data to the model
			pieModel.setValue(pair.getKey(), pair.getValue());

		}

	}

	public List<ListeCompagneVagueBean> getModel() {
		return model;
	}
	public ListeCompagneVagueBean getSelected() {
		return selected;
	}

	public void setSelected(ListeCompagneVagueBean selected) {
		this.selected = selected;
	}



	public void onClick$executer() throws Exception {
		Set<String> setselected = selectedCheckBox.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		iterator=listselected.iterator();
		HashMap<String, HashMap<String, Integer>> listDb = null;
		listDb = new HashMap<String,HashMap<String,Integer>>();

		while(iterator.hasNext()) {



			String cles=(String)iterator.next();
			Checkbox checkBox=selectedCheckBox.get(cles);
			if (selectedCheckBox.get(cles).isChecked()){

				Integer idcompagne=Integer.parseInt(selectedCheckBox.get(cles).getContext());
				String nominstance=selectedCheckBox.get(cles).getName();
				String vague=selectedCheckBox.get(cles).getValue();


				listDb.put(nominstance, new HashMap<String, Integer>());
				listDb.get(nominstance).put(vague, idcompagne);


			}

		}

		onCreatePie(comp1,listDb,chartComp2);
		onCreateMixBar(comp1,listDb,chartComp3);
	}

	
	
	
	public void onModifyCheckedBoxCotat(ForwardEvent event){
		Checkbox  checkbox = (Checkbox) event.getOrigin().getTarget();		

		if (checkbox.isChecked())
		{
			//verifier si ça n'a pas encore été unchecked
			if(unselectedCheckBoxCot.containsValue(checkbox))
			{
				unselectedCheckBoxCot.remove(checkbox.getName());

			}
			selectedCheckBoxCot.put(checkbox.getName(), checkbox);
		}
		else
		{
			//verifier si ça n'a pas encore été checked
			if(selectedCheckBoxCot.containsValue(checkbox))
			{
				selectedCheckBoxCot.remove(checkbox.getName());

			}
			unselectedCheckBoxCot.put(checkbox.getName(), checkbox);
		}
	
	}


	public void onModifyCheckedBox(ForwardEvent event){
		
		Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();		

		if (checkbox.isChecked())
		{
			//verifier si ça n'a pas encore été unchecked
			if(unselectedCheckBox.containsValue(checkbox))
			{
				unselectedCheckBox.remove(checkbox.getName());

			}
			selectedCheckBox.put(checkbox.getName(), checkbox);
		}
		else
		{
			//verifier si ça n'a pas encore été checked
			if(selectedCheckBox.containsValue(checkbox))
			{
				selectedCheckBox.remove(checkbox.getName());
				
				

			}
			unselectedCheckBox.put(checkbox.getName(), checkbox);
		}
		
	}




	public void onCreateMixBar(Component comp ,HashMap<String, HashMap<String, Integer>> listdb, ZHighCharts chartComp) throws Exception {

		dataChartMode3=null;
		dataChartMode3= new SimpleExtXYModel();


		chartComp = (ZHighCharts) comp.getFellow(chartComp.getContext());
		chartComp.setType("line"); //Mandatory!
		chartComp.setTitle("Taux de  Réalisation");
		chartComp.setTooltipFormatter("function formatTooltip(obj){ " +
				"return ''+obj.x  +': '+ obj.y;" +
				"}");

		KpiSyntheseModel kpi=new KpiSyntheseModel();
		ArrayList<TauxCouvertureCompagneBean> listbean = new ArrayList<TauxCouvertureCompagneBean>();

		listbean=kpi.getTauxCouvertureAllVague(listdb);
		String cat_final="";
		int counter=0;
		String cat="";



		Iterator<TauxCouvertureCompagneBean> itr = listbean.iterator();
		while (itr.hasNext()) {
			TauxCouvertureCompagneBean bean=itr.next();

			if (counter<listbean.size()-1){
				cat="'"+bean.getNomvague()+"'"+",";
			}else
				cat= "'"+bean.getNomvague()+"'";

			// la BAR Fiche Validé
			dataChartMode3.addValue("Fiche Validée", counter, bean.getNbfichevalide());


			// la BAR Total Employé
			dataChartMode3.addValue("Total Employé", counter, bean.getNbemploye());


			// courbe lineaire  Taux couverture 
			dataChartMode3.addValue("Taux Couverture", counter, bean.getPourcentage());

			cat_final=cat_final+cat;
			counter++;


		}


		chartComp.setxAxisOptions("{" +	"categories: [" +cat_final+	"]" + "}");
		chartComp.setTooltipFormatter("function formatTooltip(obj){ " +
				"return ''+obj.x  +': '+ obj.y;" +
				"}");

		chartComp.setModel(dataChartMode3);

		Map seriesType = new HashMap();
		seriesType.put("type", "column");

		chartComp.setSeriesOptions("Fiche Validée", seriesType);
		chartComp.setSeriesOptions("Total Employé", seriesType);

		seriesType = new HashMap();
		seriesType.put("type", "line");
		chartComp.setSeriesOptions("Taux Couverture", seriesType);
		chartComp.setPlotOptions("{line:{dataLabels: {  enabled: true ,formatter: function ()  { return this.y +"+"' %'"+"},color: '#0B0A0A',backgroundColor:'#FFFFFF',borderColor:'#0B0A0A',fontWeight: 'bold',fontSize: '12px'}, enableMouseTracking: false}}");


	}

	public void onCreateIMISpiederChart(Component comp ,HashMap<String, HashMap<String, Integer>> listdb, ZHighCharts chartComp) throws Exception {

		//================================================================================
		// Spiderweb
		//===============================================================================
		//dataChartMode4= new SimpleExtXYModel();
		dataChartMode4=null;
		dataChartMode4= new SimpleExtXYModel();

		chartComp = (ZHighCharts) comp.getFellow(chartComp.getContext());
		chartComp.setType("line");
		chartComp.setOptions("{" +
				"polar: true," +
				"plotBackgroundColor: null," +
				"plotBackgroundImage: null," +
				"plotBorderWidth: 0," +
				"plotShadow: false" +
				"}");
		chartComp.setTitle("Analyse des Résultats IMI");
		chartComp.setSubTitle("Résultats IMI obtenus vs Mix Idéal");
		chartComp.setPane("{" +
				"size: '80%'" +
				"}");

		chartComp.setyAxisOptions("{ " +
				"gridLineInterpolation: 'polygon'," +
				"lineWidth: 0," +
				"min: 0" +
				"}");
		chartComp.setTooltipOptions("{" +
				"shared: true" +
				"}");
		chartComp.setTooltipFormatter("function formatTooltip(obj){ " +
				"return obj.x + '<br/>" +
				"<span style=\"color:'+ obj.points[0].series.color +';\">'+ obj.points[0].series.name +'</span> : <b> %'+ obj.points[0].y +'</b><br/>" + //données 1ére série
				"<span style=\"color:'+ obj.points[1].series.color +';\">'+ obj.points[1].series.name +'</span> : <b> %'+ obj.points[1].y +'</b>';" + //données 2ème série
				"}");

		chartComp.setModel(dataChartMode4);
		KpiSyntheseModel kpi=new KpiSyntheseModel();
		ArrayList<AnalSynthRadarBean>  resultat=kpi.getPerParIMI(listdb);
		Iterator itr = resultat.iterator();

		//Map series = new HashMap();
		//series.put("pointPlacement", "on");
		//chartComp.setSeriesOptions("Résultat IMI", series); 
		int counter=0;
		String typeres_bckp="";

		List categorie = new ArrayList();

		while (itr.hasNext()) {
			AnalSynthRadarBean element = (AnalSynthRadarBean) itr.next();
			String typeres=element.getTyperes();
			String evaluation = element.getEvaluation();
			Integer imi = element.getTaux();


			if (!typeres.equalsIgnoreCase(typeres_bckp)){
				Map series = new HashMap();
				series.put("pointPlacement", "on");
				chartComp.setSeriesOptions(typeres_bckp, series); 

				counter=0;
				dataChartMode4.addValue(typeres, counter , imi);

			}else{

				dataChartMode4.addValue(typeres, counter , imi);

			}

			counter++;
			
			if(!categorie.contains(evaluation))
				categorie.add(evaluation);
			
			typeres_bckp=typeres;
		}
		String cat="";
		String cat_final="";
		int ctr=0;
		for (int i=0;i < categorie.size();i++)
		{
			if (ctr<categorie.size()-1){
				cat="'"+categorie.get(i)+"'"+",";
			}else
				cat= "'"+categorie.get(i)+"'";

			cat_final=cat_final+cat;
			ctr++;


		}


		/*	Map series = new HashMap();
		series.put("pointPlacement", "on");
		chartComp.setSeriesOptions("Résultat IMI", series); 
		series = new HashMap();
		series.put("pointPlacement", "on");
		chartComp.setSeriesOptions("IMI Mix Idéal", series);
		int counter=0;
		String cat="";
		String cat_final="";
		for (Entry<String, HashMap<String, Integer>> entry : resultat.entrySet()) {
			int map_size=resultat.entrySet().size();
			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String evaluation = pair.getKey();
				Integer imi = pair.getValue();
				dataChartMode4.addValue("Résultat IMI", counter , imi);
				if (counter<entry.getValue().entrySet().size()-1){
					cat="'"+evaluation+"'"+",";
				}else
					cat= "'"+evaluation+"'";
				cat_final=cat_final+cat;
				counter++;
			}
		}*/

		chartComp.setxAxisOptions("{" +	"categories: [" +cat_final+	"]" + "}");
		/*Number actuData[] = {1, 5, 84, 10};
		for (int i = 0;i<actuData.length;i++){
			dataChartMode4.addValue("IMI Mix Idéal", i , actuData[i]);
		}	*/	
	}

	public void onClick$executer1() throws Exception {

		poste_travail.getItems().clear();
		Set<String> setselected = selectedRadio.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		iterator=listselected.iterator();

		listDb = new HashMap<String,HashMap<String,Integer>>();

		while(iterator.hasNext()) {

			String cles=(String)iterator.next();
			Radio radio=selectedRadio.get(cles);
			if (selectedRadio.get(cles).isChecked()){

			
				
				Integer idcompagne=Integer.parseInt(selectedRadio.get(cles).getContext());
				String nominstance=selectedRadio.get(cles).getSclass();
				String vague=selectedRadio.get(cles).getValue();
				listDb.put(nominstance, new HashMap<String, Integer>());
				listDb.get(nominstance).put(vague, idcompagne);

			}

		}

		onCreateIMISpiederChart(comp1, listDb, chartComp4);
	


		KpiSyntheseModel kpi=new KpiSyntheseModel();
		map_poste=kpi.getListPostTravailValid1(listDb);
		Set set = (map_poste).entrySet(); 
		Iterator i = set.iterator();
		poste_travail.appendItem("Tous Postes Travail","tous");
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			poste_travail.appendItem((String) me.getKey(),(String) me.getValue());
		}

		poste_travail.setSelectedIndex(0);
		
		


	}


	public void onModifyRadio(ForwardEvent event){
		Radio radio = (Radio) event.getOrigin().getTarget();

		if(selectedRadio.size()>0){
			Set set =selectedRadio.entrySet();
			Iterator i = set.iterator();
			while(i.hasNext()){

				Map.Entry me = (Map.Entry)i.next();

				Radio radio_save= (Radio) me.getValue();
				radio_save.setChecked(false);
				selectedRadio.remove(me.getKey());


			}

		}

		if (radio.isChecked())
		{
			//verifier si ça n'a pas encore été checked
			selectedRadio.put(radio.getId(), radio);
		}

	}

	public void onSelect$poste_travail() throws SQLException
	{
		String code_poste= (String)poste_travail.getSelectedItem().getValue();

		try {

			onCreatePieIMI(comp1,listDb,chartComp5,code_poste);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void onCreatePieIMI(Component comp ,HashMap<String, HashMap<String, Integer>> listdb, ZHighCharts chartComp, String code_poste) throws Exception {
	
		//================================================================================
		// Pie chart
		//================================================================================
		pieModel1=null;
		pieModel1 = new SimplePieModel();

		chartComp = (ZHighCharts) comp.getFellow(chartComp.getContext());
		chartComp.setTitle("Répartition Employés par Métier");
		chartComp.setSubTitle("");
		chartComp.setType("pie");
		chartComp.setTooltipFormatter("function formatTooltip(obj){" +
				"return obj.key + '<br />: <b>'+obj.y+'%</b>'" +
				"}");
		chartComp.setPlotOptions("{" +
				"pie:{" +
				"allowPointSelect: true," +
				"cursor: 'pointer'," +
				"dataLabels: {" +
				"enabled: true, " +
				"color: '#000000'," +
				"connectorColor: '#000000'," +
				"formatter: function() {"+
				"return '<b>'+ this.point.name +'</b>: '+ this.percentage.toFixed(2) +' %';"+
				"}"+
				"}" +
				"}" +
				"}");	
		
		chartComp.setModel(pieModel1);
		KpiSyntheseModel kpi=new KpiSyntheseModel();
		HashMap<String, HashMap<String, Integer>> pie_map=new HashMap<String, HashMap<String, Integer>>();

		pie_map=kpi.getPerParIMIPoste(listdb,code_poste);

		for (Entry<String, HashMap<String, Integer>> entry : pie_map.entrySet()) {

			int map_size=pie_map.entrySet().size();

			for (Entry<String, Integer> pair : entry.getValue().entrySet()) {
				String evaluation = pair.getKey();
				Integer imi = pair.getValue();
				pieModel1.setValue(evaluation, imi);

			}


		}


	}

	/*public void onClick$executer2() throws Exception {
		Set<String> setselected = selectedRadio.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		iterator=listselected.iterator();

		listDb = new HashMap<String,HashMap<String,Integer>>();

		while(iterator.hasNext()) {

			String cles=(String)iterator.next();
			Radio radio=selectedRadio.get(cles);

			if (selectedRadio.get(cles).isChecked()){

				Integer idcompagne=Integer.parseInt(selectedRadio.get(cles).getContext());
				String nominstance=selectedRadio.get(cles).getSclass();
				String vague=selectedRadio.get(cles).getValue();


				listDb.put(nominstance, new HashMap<String, Integer>());
				listDb.get(nominstance).put(vague, idcompagne);

			}

		}

		onCreateBarIMIAge(comp1, listDb, chartComp6);
		onCreateBarIMIExp(comp1, listDb, chartComp7);
		onCreateAgeExpIMIScattredChart(comp1, listDb, chartAgeExpIMIScattered);

	}*/

	public void onModifyRadio1(ForwardEvent event){
		Radio radio = (Radio) event.getOrigin().getTarget();

		if(selectedRadio.size()>0){
			Set set =selectedRadio.entrySet();
			Iterator i = set.iterator();
			while(i.hasNext()){

				Map.Entry me = (Map.Entry)i.next();

				Radio radio_save= (Radio) me.getValue();
				radio_save.setChecked(false);
				selectedRadio.remove(me.getKey());


			}

		}

		if (radio.isChecked())
		{
			//verifier si ça n'a pas encore été checked
			selectedRadio.put(radio.getId(), radio);
		}

	}

	public void onCreateBarIMIAge(Component comp ,HashMap<String, HashMap<String, Integer>> listdb, ZHighCharts chartComp,String code_poste ) throws Exception {

		//================================================================================
		// Basic bar
		//================================================================================
		dataChartMode6=null;
		dataChartMode6 = new SimpleExtXYModel();

		chartComp = (ZHighCharts) comp.getFellow(chartComp.getContext());
		chartComp.setType("bar");
		chartComp.setTitle("Analyse IMI par Age");
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

		chartComp.setxAxisOptions("{" +
				"categories: [" +
				"'Compétence  acquise', " +
				"'Compétence à acquérir', " +
				"'Compétence à développer', " +
				"'Expertise'" +
				"]" +
				"}");


		KpiSyntheseModel kpi=new KpiSyntheseModel();
		ArrayList<KpiIMIAgeBean> listbean = new ArrayList<KpiIMIAgeBean>();

		listbean=kpi.getIMIAge(listdb,code_poste);
		String cat_final="";
		int counter=0;
		String cat="";
		int ctr=0;
		int compteur=0;
		String niveauMaitraise_Bkp="";

		List<String> list_trancheAge = Arrays.asList("Inf ou égal à 30 ans","Entre 30 et 50 ans","Sup égal à 50 ans");
		chartComp.setModel(dataChartMode6);



		for (String pair : list_trancheAge) {


			for (KpiIMIAgeBean list : listbean) {
				if (list.getTrancheAge().trim().equalsIgnoreCase(pair.trim())  ){

					dataChartMode6.addValue(pair, ctr, list.getPourcentage());
					//System.out.println(list.getNiveauMaitrise()+"--"+pair+"--"+ctr+"--"+list.getPourcentage());


					ctr++;

				}

				if (ctr==4)
					ctr=0;


			}



		}


		//chartComp.setxAxisOptions("{" +	"categories: [" +cat_final+	"]" + "}");
		chartComp.setTooltipFormatter("function formatTooltip(obj){ " +
				"return ''+obj.x  +': '+ obj.y;" +
				"}");

	}

	public void onCreateBarIMIExp(Component comp ,HashMap<String, HashMap<String, Integer>> listdb, ZHighCharts chartComp,String code_poste ) throws Exception {

		//================================================================================
		// Basic bar
		//================================================================================
		dataChartMode7=null;
		dataChartMode7 = new SimpleExtXYModel();

		chartComp = (ZHighCharts) comp.getFellow(chartComp.getContext());
		chartComp.setType("bar");
		chartComp.setTitle("Analyse IMI par Année d'Expérience");
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

		chartComp.setxAxisOptions("{" +
				"categories: [" +
				"'Compétence  acquise', " +
				"'Compétence à acquérir', " +
				"'Compétence à développer', " +
				"'Expertise'" +
				"]" +
				"}");


		KpiSyntheseModel kpi=new KpiSyntheseModel();
		ArrayList<KpiIMIAgeBean> listbean = new ArrayList<KpiIMIAgeBean>();

		listbean=kpi.getIMIExperience(listdb,code_poste);
		String cat_final="";
		int counter=0;
		String cat="";
		int ctr=0;
		int compteur=0;
		String niveauMaitraise_Bkp="";

		List<String> list_trancheExp = Arrays.asList("Inf ou égal à 10 ans","Entre 10 et 20 ans","Sup égal à 20 ans");
		chartComp.setModel(dataChartMode7);



		for (String pair : list_trancheExp) {


			for (KpiIMIAgeBean list : listbean) {
				if (list.getTrancheAge().trim().equalsIgnoreCase(pair.trim())  ){

					dataChartMode7.addValue(pair, ctr, list.getPourcentage());
					//System.out.println(list.getNiveauMaitrise()+"--"+pair+"--"+ctr+"--"+list.getPourcentage());


					ctr++;

				}

				if (ctr==4)
					ctr=0;


			}



		}


		//chartComp.setxAxisOptions("{" +	"categories: [" +cat_final+	"]" + "}");
		chartComp.setTooltipFormatter("function formatTooltip(obj){ " +
				"return ''+obj.x  +': '+ obj.y;" +
				"}");

	}



	public void onCreateAgeExpIMIScattredChart(Component comp ,HashMap<String, HashMap<String, Integer>> listdb, ZHighCharts chartComp,String code_poste) throws Exception {

		//================================================================================
		// Scatter plot
		//================================================================================
		dataChartAgeExpIMIScattered=null;
		dataChartAgeExpIMIScattered =new SimpleExtXYModel();

		chartComp = (ZHighCharts) comp.getFellow(chartComp.getContext());
		chartComp.setType("scatter");
		chartComp.setOptions("{" +
				"zoomType: 'xy'" +
				"}");
		chartComp.setTitle("Corrélation Age et Expérience Vs IMI");
		//chartComp.setSubTitle("Source: Heinz  2003");
		chartComp.setxAxisOptions("{" +
				"startOnTick: true," +
				"endOnTick: true," +
				"showLastLabel: true" +
				"}");
		chartComp.setTooltipFormatter("function formatTooltip(obj){" +
				"return ''+obj.x +' ans, '+ obj.y +' IMI ';" +
				"}");
		chartComp.setYAxisTitle("IMI");
		chartComp.setXAxisTitle("Année");
		chartComp.setLegend("{" +
				"layout: 'horizontal'," +
				"align: 'center'," +
				"verticalAlign: 'bottom'," +
				"x: 30, " +
				"y: 10, " +
				"floating: true," +
				"backgroundColor: '#FFFFFF'," +
				"borderWidth: 1" +
				"}");
		chartComp.setPlotOptions("{" +
				"scatter: {" +
				"marker: { " +
				"radius: 5," +
				"states: {" +
				"hover: {" +
				"enabled: true," +
				"ineColor: 'rgb(100,100,100)'" +
				"}" +
				"}" +
				"}," +
				"states: {" +
				"hover: {" +
				"marker: {" +
				"enabled: false" +
				"}" +
				"}" +
				"}" +
				"}" +
				"}");

		chartComp.setyAxisOptions("{" +
				"min:0, " +
				"minorGridLineWidth:0, " +
				"gridLineWidth:0, " +
				"alternateGridColor:null, " +
				"plotBands:[" +
				"{ " +
				"from:0.0, " +
				"to:2.0, " +
				"color:'rgba(68,170,213,0.1)', " +
				"label:{ " +
				"text:'A acquérir', " +
				"style:{ " +
				"color:'#606060' " +
				"} " +
				"} " +
				"}," +
				"{ " +
				"from:2.0, " +
				"to:3.0, " +
				"color:'rgba(0,0,0,0)', " +
				"label:{ " +
				"text:'A développer ', " +
				"style:{ " +
				"color:'#606060' " +
				"} " +
				"} " +
				"}," +
				"{ " +
				"from:3.0, " +
				"to:4.5, " +
				"color:'rgba(68,170,213,0.1)', " +
				"label:{ " +
				"text:'Acquise', " +
				"style:{ " +
				"color:'#606060' " +
				"} " +
				"} " +
				"}," +
				"{ " +
				"from:4.5, " +
				"to:5, " +
				"color:'rgba(0,0,0,0)', " +
				"label:{ " +
				"text:'Expertise', " +
				"style:{ " +
				"color:'#606060' " +
				"} " +
				"} " +
				"}," +
				"{ " +
				"from:8, " +
				"to:11, " +
				"color:'rgba(68,170,213,0.1)'," +
				"label:{ " +
				"text:'Freshbreeze', " +
				"style:{ " +
				"color:'#606060' " +
				"} " +
				"} " +
				"}," +
				"{ " +
				"from:11, " +
				"to:14, " +
				"color: 'rgba(0, 0, 0, 0)'," +
				"label: {" +
				"text: 'Strong breeze'," +
				"style: {" +
				"color: '#606060'" +
				"}" +
				"} " +
				"}, " +
				"{" +
				"from: 14," +
				"to: 15," +
				"color: 'rgba(68, 170, 213, 0.1)'," +
				"label: {" +
				"text: 'High wind'," +
				"style: {" +
				"color: '#606060'" +
				"}" +
				"}" +
				"}" +
				"]" +
				"}");

		chartComp.setModel(dataChartAgeExpIMIScattered);



		Map style = new HashMap();
		style.put("color", "rgba(223, 83, 83, .5)");
		chartComp.setSeriesOptions("Age", style);

		//Number femaledata [][] = {{161.2, 51.6}, {167.5, 59.0}, {159.5, 49.2}, {157.0, 63.0}, {155.8, 53.6}};

		KpiSyntheseModel kpi=new KpiSyntheseModel();
		ArrayList<KPIAgeExpBean> listbean = new ArrayList<KPIAgeExpBean>();
		listbean=kpi.getAgeIMICroissement(listdb,code_poste);
		for (KPIAgeExpBean list : listbean) {
			dataChartAgeExpIMIScattered.addValue("Age",list.getAge(), list.getImi() );

		}



		style = new HashMap();
		style.put("color", "rgba(119, 152, 191, .5)");
		chartComp.setSeriesOptions("Experience", style);


		ArrayList<KPIAgeExpBean> listbean1 = new ArrayList<KPIAgeExpBean>();
		listbean1=kpi.getExpIMICroissement(listdb,code_poste);
		for (KPIAgeExpBean list : listbean1) {
			dataChartAgeExpIMIScattered.addValue("Experience",list.getExperience(), list.getImi() );

		}





	}


	public void onClick$executer3() throws Exception {
		
		//executeexp.setVisible(false);
		Set<String> setselected = selectedCheckBoxCot.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		iterator=listselected.iterator();
		HashMap<String, HashMap<String, Integer>> listDb = null;
		listDb = new HashMap<String,HashMap<String,Integer>>();

		while(iterator.hasNext()) {



			String cles=(String)iterator.next();
			Checkbox checkBox=selectedCheckBoxCot.get(cles);
			if (selectedCheckBoxCot.get(cles).isChecked()){

				Integer idcompagne=Integer.parseInt(selectedCheckBoxCot.get(cles).getContext());
				String nominstance=selectedCheckBoxCot.get(cles).getName();
				String vague=selectedCheckBoxCot.get(cles).getValue();


				listDb.put(nominstance, new HashMap<String, Integer>());
				listDb.get(nominstance).put(vague, idcompagne);


			}

		}
		this.listDb=listDb;
		onCreateDureeMoyenneScattredChart(comp1, listDb, chartdureeMoyenne);
		//selectedCheckBoxCot.clear();
	
	

	}

	public void onCreateDureeMoyenneScattredChart(Component comp ,HashMap<String, HashMap<String, Integer>> listdb, ZHighCharts chartComp) throws Exception {

		//================================================================================
		// Scatter plot
		//================================================================================
		datadureeMoyenne=null;
		datadureeMoyenne =new SimpleExtXYModel();

		chartComp = (ZHighCharts) comp.getFellow(chartComp.getContext());
		chartComp.setType("scatter");
		chartComp.setOptions("{" +
				"zoomType: 'xy'" +
				"}");
		chartComp.setTitle("Durée Moyenne Evaluation");
		//chartComp.setSubTitle("Source: Heinz  2003");
		chartComp.setxAxisOptions("{" +
				"startOnTick: true," +
				"endOnTick: true," +
				"showLastLabel: true" +
				"}");
		chartComp.setTooltipFormatter("function formatTooltip(obj){" +
				"return ''+obj.x +' Temps (mn), '+ obj.y +' ';" +
				"}");
		chartComp.setYAxisTitle("");
		chartComp.setXAxisTitle("Temps (mn)");
		chartComp.setLegend("{" +
				"layout: 'horizontal'," +
				"align: 'center'," +
				"verticalAlign: 'bottom'," +
				"x: 30, " +
				"y: 10, " +
				"floating: true," +
				"backgroundColor: '#FFFFFF'," +
				"borderWidth: 1" +
				"}");
		chartComp.setPlotOptions("{" +
				"scatter: {" +
				"marker: { " +
				"radius: 5," +
				"states: {" +
				"hover: {" +
				"enabled: true," +
				"ineColor: 'rgb(100,100,100)'" +
				"}" +
				"}" +
				"}," +
				"states: {" +
				"hover: {" +
				"marker: {" +
				"enabled: false" +
				"}" +
				"}" +
				"}" +
				"}" +
				"}");

		chartComp.setxAxisOptions("{" +
				"min:0, " +
				"minorGridLineWidth:0, " +
				"gridLineWidth:0, " +
				"alternateGridColor:null, " +
				"plotBands:[" +
				"{ " +
				"from:0.0, " +
				"to:7.0, " +
				"color:'rgba(68,170,213,0.1)', " +
				"label:{ " +
				"text:'Durée courte', " +
				"style:{ " +
				"color:'#F78181' " +
				"} " +
				"} " +
				"}," +
				"{ " +
				"from:7.0, " +
				"to:15.0, " +
				"color:'rgba(0,0,0,0)', " +
				"label:{ " +
				"text:'Durée Acceptable ', " +
				"style:{ " +
				"color:'#BCF5A9' " +
				"} " +
				"} " +
				"}," +
				"{ " +
				"from:15.0, " +
				"to:400.0, " +
				"color:'rgba(68,170,213,0.1)', " +
				"label:{ " +
				"text:'Durée longue', " +
				"style:{ " +
				"color:'#606060' " +
				"} " +
				"} " +
				"}," +
				"{ " +
				"from:400.0, " +
				"to:500.0, " +
				"color:'rgba(0,0,0,0)', " +
				"label:{ " +
				"text:'', " +
				"style:{ " +
				"color:'#606060' " +
				"} " +
				"} " +
				"}," +
				"{ " +
				"from:500.0, " +
				"to:600., " +
				"color:'rgba(68,170,213,0.1)'," +
				"label:{ " +
				"text:'', " +
				"style:{ " +
				"color:'#606060' " +
				"} " +
				"} " +
				"}," +
				"{ " +
				"from:600, " +
				"to:700, " +
				"color: 'rgba(0, 0, 0, 0)'," +
				"label: {" +
				"text: 'Strong breeze'," +
				"style: {" +
				"color: '#606060'" +
				"}" +
				"} " +
				"}, " +
				"{" +
				"from: 700," +
				"to: 800," +
				"color: 'rgba(68, 170, 213, 0.1)'," +
				"label: {" +
				"text: 'High wind'," +
				"style: {" +
				"color: '#606060'" +
				"}" +
				"}" +
				"}" +
				"]" +
				"}");

		chartComp.setModel(datadureeMoyenne);



		Map style = new HashMap();
		style.put("color", "rgba(223, 83, 83, .5)");
		chartComp.setSeriesOptions("Vague", style);

		KpiSyntheseModel kpi=new KpiSyntheseModel();
		ArrayList<DureeMoyenneBean> listbean = new ArrayList<DureeMoyenneBean>();
		listbean=kpi.getDuréeMinimEvol(listdb);
		String vague_bckp="";
		int serie=0;
		for (DureeMoyenneBean list : listbean) {

			if (!vague_bckp.equalsIgnoreCase(list.getVague())){
				serie++;
			}
			datadureeMoyenne.addValue(list.getVague(), list.getDuree(),serie);
			vague_bckp=list.getVague();
		}
		
		executeexp.setVisible(true);

		
	}


	public void onModifyRadioIMIAge(ForwardEvent event) throws SQLException{
		Radio radio = (Radio) event.getOrigin().getTarget();

		if(selectedRadio.size()>0){
			Set set =selectedRadio.entrySet();
			Iterator i = set.iterator();
			while(i.hasNext()){

				Map.Entry me = (Map.Entry)i.next();

				Radio radio_save= (Radio) me.getValue();
				radio_save.setChecked(false);
				selectedRadio.remove(me.getKey());


			}

		}

		if (radio.isChecked())
		{
			//verifier si ça n'a pas encore été checked
			selectedRadio.put(radio.getId(), radio);
		}

		Set<String> setselected = selectedRadio.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		Iterator<String>iterator=listselected.iterator();
		iterator=listselected.iterator();

		listDb = new HashMap<String,HashMap<String,Integer>>();

		while(iterator.hasNext()) {

			String cles=(String)iterator.next();
			//Radio radio=selectedRadio.get(cles);

			if (selectedRadio.get(cles).isChecked()){

				Integer idcompagne=Integer.parseInt(selectedRadio.get(cles).getContext());
				String nominstance=selectedRadio.get(cles).getSclass();
				String vague=selectedRadio.get(cles).getValue();


				listDb.put(nominstance, new HashMap<String, Integer>());
				listDb.get(nominstance).put(vague, idcompagne);

			}

		}

		if (poste_travail1.getSelectedItems().size()>0){


			poste_travail1.getItems().clear();


		}

		KpiSyntheseModel kpi=new KpiSyntheseModel();
		map_poste=kpi.getListPostTravailValid1(listDb);
		Set set = (map_poste).entrySet(); 
		Iterator i = set.iterator();

		poste_travail1.appendItem("Tous Postes Travail","tous");

		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			poste_travail1.appendItem((String) me.getKey(),(String) me.getValue());
		}

		poste_travail1.setSelectedIndex(0);

		//poste_travail_libelle=(String) map_poste.get((String)poste_travail.getSelectedItem().getLabel());

		//poste_travail_libelle=(String)poste_travail.getSelectedItem().getLabel();


	}

	public void onSelect$poste_travail1() throws SQLException
	{
		String code_poste= (String)poste_travail1.getSelectedItem().getValue();

		try {

			onCreateBarIMIAge(comp1, listDb, chartComp6,code_poste);
			onCreateBarIMIExp(comp1, listDb, chartComp7,code_poste);
			onCreateAgeExpIMIScattredChart(comp1, listDb, chartAgeExpIMIScattered,code_poste);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	
	private void genExportExcel(HashMap<String, HashMap<String, Integer>> listDb) throws ParsePropertyException, InvalidFormatException, IOException{

		// System.out.println("\nApp Deployed Directory path: " + Sessions.getCurrent().getWebApp().getRealPath("WEB-INF"));

		FileOutputStream fOut= new FileOutputStream("DureeCotationAno.xls");

		KpiSyntheseModel cotation=new KpiSyntheseModel();
		List<ListDureeCotatLowBean>employees=cotation.getListCotationBaclee(listDb);
		Map beans = new HashMap();
		beans.put("employee", employees);
		XLSTransformer transformer = new XLSTransformer(); 
		//transformer.groupCollection("department.staff"); 
		String reportLocation = Sessions.getCurrent().getWebApp().getRealPath("WEB-INF");
		String reportLocation1 = Sessions.getCurrent().getWebApp().getRealPath("WebContent");
		//System.out.println(reportLocation+ " "+reportLocation1);

	
		Workbook workbook = transformer.transformXLS(new FileInputStream(reportLocation+ "/list_cotation_anomalie.xls"), beans);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();

		File file = new File("DureeCotationAno.xls");
		Filedownload.save(file, "XLS");
		//transformer.transformXLS(templateFileName, beans, destFileName);
		//date = new Date();
		//System.out.println(" date fin" +date.toString());



	}
	public void onClick$executeexp() throws Exception {
		
		if (Messagebox.show("Voulez vous exporter la liste des cotations de durée courte ?", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			//exportMatriceCotationExlFileV2();
			genExportExcel(listDb);

			return;
		}

		else{
			return;
		}
		
		
	}
	
	

}
