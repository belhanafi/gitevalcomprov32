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
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleCategoryModel;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.impl.ChartEngine;

import Statistique.bean.EmployeMoyFamBean;
import Statistique.bean.StatCotationEmployeBean;
import Statistique.bean.StatMoyFamillePosteBean;
import Statistique.bean.StatTrancheAgePosteBean;
import Statistique.model.StatCotationEmployeModel;

public class StatIMGPosteTravailAction extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	Combobox poste_travail;
	Combobox compagne;
	ArrayList<StatCotationEmployeBean> ListeCotationEmploye;
	String selectedEmploye;
	String selectedCompagne;
	StatCotationEmployeBean selectedBean;
	
	Map map_poste=null;
	Map map_compagne=null;

	
	public StatIMGPosteTravailAction()
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
		// forcer la selection de la permiere ligne
		poste_travail.setVisible(false);
		compagne.setSelectedIndex(0);
		
		
		//selection des données associées à la dernière compagne
		// et affichage du graphe
		/*CategoryModel catmodel = new SimpleCategoryModel();
		HashMap<String , Double> listeStat=compagnes.get(selectedCompagne);
		Set<String> listekey=listeStat.keySet();
		Iterator indexkey=listekey.iterator();
		while(indexkey.hasNext())
		{
			String valeurcles=(String)indexkey.next();
			Double valeurStat=listeStat.get(valeurcles);
			catmodel.setValue("IMI", valeurcles, valeurStat);
		}
		
		
//		//catmodel.setValue("poste", "entre 18 et 30 ans", new Integer(pourcentage));
//        catmodel.setValue("IMI", "Affaire", new Double(2));
//        catmodel.setValue("IMI", "Relationnelle", new Double(3.5));
//        catmodel.setValue("IMI", "Personnelle", new Double(1.3));
//        catmodel.setValue("IMI", "Opérationnelle", new Double(1));
		mychart.setTitle("Cotation moyenne de l'employé " +selectedEmploye+" pour la compagne "+ selectedCompagne);
        mychart.setModel(catmodel);
        
        //ces instructions permettent de récuperer l'objet image pour l'export
        
        ChartEngine d=mychart.getEngine();
		image=d.drawChart(mychart);		
*/
	}
	
	 @SuppressWarnings("static-access")
	 public void onClick$downloadimage() 
	 {
			//enregistrement du fichier
			Filedownload fichierdownload=new Filedownload();

			fichierdownload.save(image, "jpg", "Stat_Population_Age.jpg");
	}
	 

	 
	public void onSelect$poste_travail() throws SQLException	 {
		
		     String poste= (String) map_poste.get((String)poste_travail.getSelectedItem().getLabel());
		     
		     CategoryModel catmodel = new SimpleCategoryModel();
		     List charts=new ArrayList<CategoryModel>();
		     StatMoyFamillePosteBean cpb;
			 Iterator it;
			 StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
			 List sect_items=cotationMoel.getListPosteMoyFam(poste);
	         it = sect_items.iterator();
	         while (it.hasNext()){
		 		cpb  = (StatMoyFamillePosteBean) it.next();
		 		
		 		 catmodel.setValue(cpb.getFamille(),"Familles de compétence",cpb.getMoy_famille());
		 		//catmodel.setValue("IMI","indice de maitrise individuel",3);
		 		mychart.setModel(catmodel);
		 		
		 		
				
			}
			
			catmodel.setValue("IMG","Indice de maitrise global",cotationMoel.getIMGParPoste(poste));
	 		mychart.setModel(catmodel);
			
			ChartEngine d=mychart.getEngine();
			image=d.drawChart(mychart);
		
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
}
