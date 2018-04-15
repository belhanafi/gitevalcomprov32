package common.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Window;

import common.ApplicationFacade;
import common.InitContext;
import common.bean.ArborescenceMenu;
import common.bean.EcranBean;


public class TabComposer extends GenericForwardComposer{
	

	Div contentDiv;
	Tabbox tb;
	Tabs tbtabs;
	Tabpanels tbpanels;
	Div div;
	Window main;
	
	public TabComposer(){


		
	}
	
	
	public void doAfterCompose(Component comp) throws Exception 
	{
		super.doAfterCompose(comp);
		
		try
		{
			
			tb.setTabscroll(true);
			//creation de tab0 et sa fermeture pour qu'i n'y ait pas de décalage lors de l'ajout des listboxs
			Tab newTab0 = new Tab(); 
			newTab0.isClosable();	
			tbtabs.appendChild(newTab0);			
			newTab0.close();
			
			//enregistrement des informations associée à la durée d'une evaluation
			
			InitContext contexte=new InitContext();
			contexte.loadProperties();
			Integer valeurTimer=new Integer(contexte.getTimerValue());
			ApplicationFacade.getInstance().setTimerValue(valeurTimer);
			//recuperation de l astructure du menu et sa construction
			ArborescenceMenu arborescenceMenuBean=ApplicationFacade.getInstance().getArborescenceMenubean();
			
			HashMap<String, List<EcranBean>> listeMenu=arborescenceMenuBean.getArborescenceMenu();
			
			Set<String> set = listeMenu.keySet( );
			
			
			List<String> list = new ArrayList<String>(set);
			Collections.sort(list);
			@SuppressWarnings("rawtypes")
			Iterator it=list.iterator();
			
			while(it.hasNext())
			{
				String cles=(String) it.next();
				List<EcranBean> liste=listeMenu.get(cles);
				Iterator<EcranBean> iterator=liste.iterator();
				
				//creation du tab
				Tab newTab = new Tab();

				newTab.setLabel(cles);
				
				Tabpanel newPanel = new Tabpanel();
				newPanel.setStyle("overflow:auto");
				
				Label newLabel = new Label();
				newLabel.setValue("");
				
				newPanel.appendChild(newLabel);
				
				//creation du menu
				MenuComposer borderLayout=new MenuComposer(liste);
				borderLayout.setContentDiv(contentDiv);
				
				newPanel.appendChild(borderLayout.getMenuListbox());
				
				tbpanels.appendChild(newPanel);
				
				tbtabs.appendChild(newTab);		
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void onClick$exit(MouseEvent event) throws Exception{
		Executions.createComponents("../login/login.zul", div, null);
		main=(Window)this.self;
		main.detach();
		ApplicationFacade.getInstance().resetArguments();
		
	}
	 	
}
