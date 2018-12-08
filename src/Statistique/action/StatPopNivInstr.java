package Statistique.action;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kpi.model.SuviActionDEVModel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.CategoryModel;
import org.zkoss.zul.Chart;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.PieModel;
import org.zkoss.zul.SimpleCategoryModel;
import org.zkoss.zul.SimplePieModel;
import org.zkoss.zul.impl.ChartEngine;

import Statistique.bean.EmployeCadreBean;
import Statistique.bean.EmployeFormationBean;
import Statistique.model.EmployeModel;

public class StatPopNivInstr extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	Listbox  comp_struct_ent_list;
	Label message;
	Listbox comp_poste_list;
	private Listbox direction;
	Map map_direction=null;
	Map map = new HashMap();
	Listbox comp_list;
	Label  label_direction;
	Label label_structure ;
	Label label_poste;


	public StatPopNivInstr()
	{

	}

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		EmployeModel init=new EmployeModel();
		EmployeFormationBean cpb;
		String typetranche="";
		String code_structure_str="";
		String code_poste="";
		Integer compagne_id=0;
		String direction_str="";
		
		// Affichage de la liste des direction
				direction.appendItem("Toutes Directions","-1");

				map_direction=init.getListDirection();
				Set set1 = (map_direction).entrySet(); 
				Iterator i1 = set1.iterator();
				while(i1.hasNext()) {
					Map.Entry me = (Map.Entry)i1.next();
					direction.appendItem((String) me.getKey(),(String) me.getKey());
				}



				if(direction.getItemCount()>0){
					direction.setSelectedIndex(0);
					direction_str=(String) direction.getSelectedItem().getValue();
				}

				if(direction_str.equalsIgnoreCase("-1")){


					comp_struct_ent_list.setVisible(false);
					comp_poste_list.setVisible(false);

					label_structure.setVisible(false);
					label_poste.setVisible(false);

				}


		// Affichage de la liste postes de travail
		comp_poste_list.appendItem("Tous Postes Travail","-1");

		Set set_ent = (init.getStructEntList()).entrySet(); 
		Iterator itr_ = set_ent.iterator();
		// Affichage de la liste des strcutures entreprise
		comp_struct_ent_list.appendItem("Toutes Structures","-1");
		while(itr_.hasNext()) {
			Map.Entry me = (Map.Entry)itr_.next();

			comp_struct_ent_list.appendItem((String) me.getKey(),(String) me.getValue());

			//profilemodel.add((String) me.getKey());
		}
		if(comp_struct_ent_list.getItemCount()>0){
			comp_struct_ent_list.setSelectedIndex(0);
			code_structure_str=(String) comp_struct_ent_list.getSelectedItem().getValue();
		}

		if(comp_poste_list.getItemCount()>0){
			comp_poste_list.setSelectedIndex(0);
			code_poste=(String) comp_poste_list.getSelectedItem().getValue();
		}

		Iterator it;
		List sect_items=init.getNombreEmployesNivForm("0","0","-1");
		it = sect_items.iterator();
		PieModel piemodel = new SimplePieModel();

		while (it.hasNext()){
			cpb  = (EmployeFormationBean) it.next();

			piemodel.setValue(cpb.getNiveau(),cpb.getPourcentage());
			mychart.setModel(piemodel);

		}
		//ces instructions permettent de récuperer l'objet image pour l'export
		try
		{
			ChartEngine d=mychart.getEngine();
			mychart.setTitle("Structure: "+(String) comp_struct_ent_list.getSelectedItem().getLabel());
			image=d.drawChart(mychart);
		}
		catch(Exception e)
		{
			//exception lors de l'affichage du jfreechart car base vide
		}

	}

	public void onClick$downloadimage() {


		//enregistrement du fichier
		Filedownload fichierdownload=new Filedownload();

		fichierdownload.save(image, "jpg", "Stat_Population_Niveau_Instruction.jpg");
	}


	


	public void onSelect$comp_poste_list() throws SQLException {

		message.setValue("");

		EmployeModel init=new EmployeModel();
		String code_structure=(String) comp_struct_ent_list.getSelectedItem().getValue();
		String code_poste=(String) comp_poste_list.getSelectedItem().getValue();
		String lbl_direction=(String) direction.getSelectedItem().getValue();
		String code_direction="";
		String structure_filter="";

		if (lbl_direction.equalsIgnoreCase("Toutes Campagnes")){

			structure_filter="-1";
		}else{
			List <String> list_code_dir=(List)map_direction.get(lbl_direction);


			if (list_code_dir.size()>0){
				String structure = "(";
				for ( int i=0;i<list_code_dir.size();i++){
					if (i <list_code_dir.size()-1)
						structure+="'"+list_code_dir.get(i)+"',";
					else
						structure+="'"+list_code_dir.get(i)+"'";
				}
				structure+=")";
				structure_filter=" e.code_structure in "+structure;
			}else{

				structure_filter="";

			}
		}

		generateChart( code_structure, code_poste,structure_filter);

	}


	private void generateChart( String code_structure,	String code_poste,String code_direction) throws SQLException {
		EmployeModel init=new EmployeModel();
		Map map = new HashMap();
		Map map_structure = new HashMap();
		map_structure=init.getStructEntList();
		EmployeFormationBean cpb;
		Iterator it;
		List sect_items=init.getNombreEmployesNivForm(code_structure,code_poste,code_direction);
		it = sect_items.iterator();
		PieModel piemodel = new SimplePieModel();

		if (sect_items.size()>0){
			mychart.setVisible(true);

			while (it.hasNext()){
				cpb  = (EmployeFormationBean) it.next();

				piemodel.setValue(cpb.getNiveau(),cpb.getPourcentage());
				mychart.setModel(piemodel);

			}

			//ces instructions permettent de récuperer l'objet image pour l'export
			try
			{
				ChartEngine d=mychart.getEngine();
				mychart.setTitle("Structure: "+(String) comp_struct_ent_list.getSelectedItem().getLabel());

				image=d.drawChart(mychart);
			}
			catch(Exception e)
			{
				//exception lors de l'affichage du jfreechart car base vide
			}
		}else{
			message.setVisible(true);
			message.setValue("Pas de résultat à afficher");
			mychart.setVisible(false);
		}
	}


	public void onSelect$direction() throws SQLException	{

		message.setValue("");
		mychart.setVisible(false);
		comp_poste_list.getItems().clear();
		comp_struct_ent_list.getItems().clear();
		String code_structure_str="";

		String libelle_direction=(String)direction.getSelectedItem().getValue();

		if (libelle_direction.equalsIgnoreCase("-1")){
			comp_struct_ent_list.setVisible(false);
			comp_poste_list.setVisible(false);

			label_structure.setVisible(false);
			label_poste.setVisible(false);

			//generer le chart pour toutes les direction indépendement des structures et postes
			generateChart( "0","0", "-1");


		}else{

			comp_struct_ent_list.setVisible(true);
			comp_poste_list.setVisible(true);

			label_structure.setVisible(true);
			label_poste.setVisible(true);

			List <String> list_code_dir=(List)map_direction.get(libelle_direction);

			SuviActionDEVModel init= new SuviActionDEVModel();
			//recuperer la liste des structures
			Set set_ent = (init.getStructEntList(list_code_dir)).entrySet(); 
			Iterator itr = set_ent.iterator();

			// Affichage de la liste des strcutures entreprise
			comp_struct_ent_list.appendItem("Toutes Structures","-1");
			while(itr.hasNext()) {

				Map.Entry me = (Map.Entry)itr.next();
				comp_struct_ent_list.appendItem((String) me.getKey(),(String) me.getValue());

				//profilemodel.add((String) me.getKey());
			}
			if(comp_struct_ent_list.getItemCount()>0){
				comp_struct_ent_list.setSelectedIndex(0);
				code_structure_str=(String) comp_struct_ent_list.getSelectedItem().getValue();
			}

			comp_poste_list.appendItem("Tous Postes Travail","-1");
			String code_direction="";
			String structure_filter="";

			if (list_code_dir.size()>0){
				String structure = "(";
				for ( int i=0;i<list_code_dir.size();i++){
					if (i <list_code_dir.size()-1)
						structure+="'"+list_code_dir.get(i)+"',";
					else
						structure+="'"+list_code_dir.get(i)+"'";
				}
				structure+=")";
				structure_filter=" e.code_structure in "+structure;
			}else{

				structure_filter="";

			}
			//generer le chart pour une direction donnée  indépendement des structures et postes
			generateChart("-1","-1", structure_filter);

		}


	}
	
	
	public void onSelect$comp_struct_ent_list() throws SQLException {

		message.setValue("");
		mychart.setVisible(false);
		comp_poste_list.getItems().clear();
		comp_poste_list.appendItem("Tous Postes Travail","-1");

		EmployeModel init=new EmployeModel();
		Map map = new HashMap();
		Map map_structure = new HashMap();
		String code_structure=(String) comp_struct_ent_list.getSelectedItem().getValue();

		String code_direction="";
		String structure_filter="";
		String lbl_direction=(String) direction.getSelectedItem().getValue();
		List <String> list_code_dir=(List)map_direction.get(lbl_direction);

		if (list_code_dir.size()>0){
			String structure = "(";
			for ( int i=0;i<list_code_dir.size();i++){
				if (i <list_code_dir.size()-1)
					structure+="'"+list_code_dir.get(i)+"',";
				else
					structure+="'"+list_code_dir.get(i)+"'";
			}
			structure+=")";
			structure_filter=" e.code_structure in "+structure;
		}else{

			structure_filter="";

		}


		if ((code_structure.equalsIgnoreCase("-1"))){
			comp_poste_list.getItems().clear();
			comp_poste_list.appendItem("Tous Postes Travail","-1");
			//generer le chart pour une direction donnée  indépendement des structures et postes
			generateChart( "-1","-1", structure_filter);



		}
		else {
			Set set_ent = (init.getListPosteStructure(code_structure)).entrySet(); 
			Iterator itr_ = set_ent.iterator();
			//si la structure ne cotient aucun poste de travail. on cache la liste box poste 
			if (set_ent.size()==0){
				comp_poste_list.setVisible(false);
				label_poste.setVisible(false);
			}else{
				comp_poste_list.setVisible(true);
				label_poste.setVisible(true);
			}

			while(itr_.hasNext()) {
				Map.Entry me = (Map.Entry)itr_.next();
				comp_poste_list.appendItem((String) me.getKey(),(String) me.getValue());

				//profilemodel.add((String) me.getKey());
			}
			
			generateChart( code_structure,"-1", "0");
		}

		

	}

	public void onSelect$comp_list() throws SQLException	{

		int compagne_id=Integer.parseInt((String) comp_list.getSelectedItem().getValue());

		if(compagne_id==-1){

			
			comp_struct_ent_list.setVisible(false);
			comp_poste_list.setVisible(false);
		}else{
			
			comp_struct_ent_list.setVisible(true);
			comp_poste_list.setVisible(true);
		}

	}
	
	


}
