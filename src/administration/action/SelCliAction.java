package administration.action;



import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import administration.bean.CompteBean;
import administration.bean.SelCliBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.SelCliDbBean;
import administration.model.SelCliModel;
import common.ApplicationFacade;
import common.ApplicationSession;

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
		
		//forcer la selection du premier element de la liste
		sec_activite.setSelectedIndex(0);
		
		int index= 0;//sec_activite.getSelectedIndex();
		SelCliBean cpb1=new SelCliBean();
		SelCliDbBean cdb=new SelCliDbBean();
		cpb=(SelCliBean) sect_items.get(index);
		Iterator it1;

		client.getItems().clear();
		try {
			client_db_items=init.loadClientCombo(cpb1.getSecteur_id());
			it1 = client_db_items.iterator();
			while (it1.hasNext()){
		 		cdb  = (SelCliDbBean) it1.next();
		 		client.appendItem(cdb.getClient_name());	 		
			
			}
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// fin forcer la selection du premier element de la liste
		
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
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		CompteBean cptbean;

		try {
			db_items=init.loadDBLabel(cdb.getClient_id());
			it = db_items.iterator();
			while (it.hasNext()){
				cpb  = (SelCliDBNameBean) it.next();
		 		database_name.setValue(cpb.getNombase());	
		 		
		 		applicationSession.setClient_database_id(cpb.getDatbase_id());
		 		//recuperer le nom de la base pour l'afficher dans le Menu en haut avec le login
		 		cptbean=applicationSession.getCompteUtilisateur();
		 		cptbean.setNom_base(database_name.getValue());
		  		applicationSession.setCompteUtilisateur(cptbean);
		 		//TODO supprimer cette ligne DonneeFacade
		 		//ApplicationFacade.getInstance().setClient_database_id(cpb.getDatbase_id());
		 		//System.out.println("APRES"+ApplicationFacade.getInstance().getClient_database_id());			
			}
			Sessions.getCurrent().setAttribute("APPLICATION_ATTRIBUTE",applicationSession);
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
