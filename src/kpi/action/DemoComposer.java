package kpi.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import kpi.model.KpiSyntheseModel;

import org.ngi.zhighcharts.SimpleExtXYModel;
import org.ngi.zhighcharts.ZHighCharts;
import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimplePieModel;
import org.zkoss.zul.Window;

import compagne.model.SuiviCompagneModel;

public class DemoComposer extends Window implements EventListener {
	protected Log logger = Log.lookup(this.getClass());

	//Pie chart
	private ZHighCharts chartComp2;
	private ZHighCharts chartComp3;
	private SimplePieModel pieModel = new SimplePieModel();
	Listbox comp_list=new Listbox();
	Integer compagne_id=0;
	Component comp;

	// date format used to capture date time
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
	
	public DemoComposer() throws SQLException{
		AnalyseSyntheseAction mod= new AnalyseSyntheseAction();
		try {
			mod.doAfterCompose(comp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


	/*public void onCreate( ) throws Exception {


		//================================================================================
		// Pie chart
		//================================================================================

		chartComp2 = (ZHighCharts) getFellow("chartComp2");
		chartComp2.setTitle("Répartition Employés par Métier");
		//chartComp2.setSubTitle("2010");
		chartComp2.setType("pie");
		chartComp2.setTooltipFormatter("function formatTooltip(obj){" +
				"return obj.key + '<br />Browser Share: <b>'+obj.y+'%</b>'" +
				"}");
		chartComp2.setPlotOptions("{" +
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
		chartComp2.setModel(pieModel);
		KpiSyntheseModel kpi=new KpiSyntheseModel();
		HashMap <String,Double> pie_map=new HashMap <String,Double>();

		pie_map=kpi.getNombreEmployesAllVague();

		for(Entry<String, Double> pair : pie_map.entrySet()){

			//Adding some data to the model
			pieModel.setValue(pair.getKey(), pair.getValue());

		}





	}*/

	@Override
	public void onEvent(Event arg0) throws Exception {
		

	}

	/**
	 * internal method to convert date&amp;time from string to epoch milliseconds
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	private long getDateTime(String date) throws Exception {
		return sdf.parse(date).getTime();
	}

}

