package administration.action;



import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;

import common.ApplicationFacade;

import administration.bean.SelCliBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.SelCliDbBean;
import administration.model.SelCliModel;

public class SelCliAction extends GenericForwardComposer{
	Combobox sec_activite;
	Combobox client;
	Label database_name;
	List sect_items;
	SelCliModel init=new SelCliModel();
	List client_db_items;
	List db_items;
	Window main;
	Div div;
	Label msg;
	
	public SelCliAction(){
		
	}
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		//SelCliModel init=new SelCliModel();
		SelCliBean cpb;
		String secteur;
		Iterator it;
		sect_items=init.loadSectActivCombo();

		it = sect_items.iterator();
		while (it.hasNext()){
	 		cpb  = (SelCliBean) it.next();
	 		sec_activite.appendItem(cpb.getSecteur());
	 		
	 		
			
		}
		
		
  }
	
	public void onSelect$sec_activite() {
		int index= sec_activite.getSelectedIndex();
		SelCliBean cpb=new SelCliBean();
		SelCliDbBean cdb=new SelCliDbBean();
		cpb=(SelCliBean) sect_items.get(index);
		Iterator it;
		client.getItems().clear();
		try {
			client_db_items=init.loadClientCombo(cpb.getSecteur_id());
			it = client_db_items.iterator();
			while (it.hasNext()){
		 		cdb  = (SelCliDbBean) it.next();
		 		client.appendItem(cdb.getClient_name());	 		
			
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	public void onSelect$client() {
		
		int index= client.getSelectedIndex();
		
		SelCliDbBean cdb=new SelCliDbBean();
		cdb=(SelCliDbBean) client_db_items.get(index);
		SelCliDBNameBean cpb =new SelCliDBNameBean();
		Iterator it;
		
		try {
			db_items=init.loadDBLabel(cdb.getClient_id());
			it = db_items.iterator();
			while (it.hasNext()){
				cpb  = (SelCliDBNameBean) it.next();
		 		database_name.setValue(cpb.getNombase());	
		 		ApplicationFacade.getInstance().setClient_database_id(cpb.getDatbase_id());
		 		//System.out.println("APRES"+ApplicationFacade.getInstance().getClient_database_id());
			
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	public void onClick$valider(MouseEvent event) throws Exception	{
		
		if(Strings.isBlank(sec_activite.getValue()) || Strings.isEmpty(client.getValue())){
			msg.setValue("*Merci de choisir le secteur d'activité et le client");
			return;
		}
		Map data = new HashMap();
		data.put("secteur", sec_activite.getValue());
		data.put("client", client.getValue());
		Executions.createComponents("../pages/menu.zul", div, data);
		//permet de fermer la fenetre selcli
		main=(Window)this.self;
		main.detach();
		
	}

}
