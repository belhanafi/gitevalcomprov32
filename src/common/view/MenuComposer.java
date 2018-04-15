package common.view;




import java.util.Iterator;
import java.util.List;



import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import org.zkoss.zul.Tree;


import common.bean.EcranBean;


public class MenuComposer extends GenericForwardComposer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ListModelList menuModel = new ListModelList();
	Listbox menuListbox;
	
	Tree explorerTree;
	MenuNodeSelectListener listener = new MenuNodeSelectListener();
	MenuNodeItemRenderer renderer = new MenuNodeItemRenderer();
	Div contentDiv;
	List <EcranBean> listeEcranBean;
	



	public MenuComposer(List <EcranBean> listeEcranBean)
	{
		this.listeEcranBean=listeEcranBean;
		menuListbox=new Listbox();
		Iterator<EcranBean> iterator=listeEcranBean.iterator();
		while(iterator.hasNext())
		{
			EcranBean ecranBean=(EcranBean)iterator.next();
			
			//affecter ces informations au menuModel appropriés
			menuModel.add(new MenuNode(ecranBean.getLibelle_ecran(),"../pages/"+ecranBean.getCode_ecran()+".zul"));
			//System.out.println(" libelle --> "+ecranBean.getLibelle_ecran()+  "  code ecran --> "+ecranBean.getCode_ecran()+".zul");
		}
		
		menuListbox.setModel(menuModel);
		menuListbox.setItemRenderer(renderer);
		menuListbox.addEventListener(Events.ON_SELECT,listener);
		menuListbox.addEventListener(Events.ON_CLICK,listener);
	}
	
	
	public void doAfterCompose(Component comp) throws Exception 
	{
		super.doAfterCompose(comp);
		
		
		menuListbox.setModel(menuModel);
		menuListbox.setItemRenderer(renderer);
		menuListbox.addEventListener(Events.ON_SELECT,listener);
		menuListbox.addEventListener(Events.ON_CLICK,listener);
		
	}

	class MenuNode 
	{
		String label;
		String link;
		public MenuNode(String label,String link)
		{
			this.label = label;
			this.link = link;
		}
		public String getLabel() 
		{
			return label;
		}
		public void setLabel(String label) 
		{
			this.label = label;
		}
		public String getLink() 
		{
			return link;
		}
		public void setLink(String link) 
		{
			this.link = link;
		}
	}
	
	class MenuNodeItemRenderer implements ListitemRenderer
	{

		public void render(Listitem item, Object data) throws Exception 
		{
			
			MenuNode node = (MenuNode)data;
			Image image=new Image("/WebContent/image/fleche_droite_2.jpg");
			image.getSrc();

			//System.out.println("current directory-->"+image.getSrc());
			item.setImage("/image/fleche_droite_2.gif");
			item.setLabel(node.getLabel());
			item.setValue(node);
			

		}
	}
	
	class MenuNodeSelectListener implements EventListener{
		public void onEvent(Event event) throws Exception {
			Listitem item = menuListbox.getSelectedItem();
			contentDiv.getChildren().clear();		
			if(item!=null)
			{			
				MenuNode node = (MenuNode)item.getValue();			
				Executions.createComponents(node.getLink(),contentDiv,null);
			}
		}		
	}
	
	public void setContentDiv(Div contentDiv) {
		this.contentDiv = contentDiv;
	}
	
	public Listbox getMenuListbox() {
		return menuListbox;
	}

}
