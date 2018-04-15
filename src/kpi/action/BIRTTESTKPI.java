package kpi.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.CategoryModel;
import org.zkoss.zul.Chart;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.SimpleCategoryModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.ChartEngine;

import Statistique.bean.StatTrancheAgePosteBean;
import Statistique.model.EmployeModel;
import common.ApplicationSession;
import compagne.bean.EmailEvaluateurBean;
import compagne.bean.SuiviCompStatutFicheBean;
import compagne.bean.SuiviCompagneBean;
import compagne.model.ResultatEvaluationModel;
import compagne.model.SuiviCompagneModel;

import org.zkoss.zk.ui.util.Clients;



public class BIRTTESTKPI extends GenericForwardComposer{


	
	Button btnExecPie;
	Button btnExecBar;

	AnnotateDataBinder binder;

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception 
	{
		super.doAfterCompose(comp);

	

	}


	

}


