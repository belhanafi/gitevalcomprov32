package administration.action;


import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import compagne.model.SuiviCompagneModel;
import administration.bean.AptitudePosteTravailBean;
import administration.bean.CompetencePosteTravailBean;
//import administration.bean.CompetencePosteTravailBean.MapFamille;
//import administration.bean.CompetencePosteTravailBean.mapCompetence;
//import administration.bean.CompetencePosteTravailBean.mapGroupe;
import administration.model.CompetencePosteTravailModel;



public class CompetencePosteTravailAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	AnnotateDataBinder binder;
	Grid competencePostetravailgd;

	Columns compcolumn;
	Rows lignes;
	Combobox nom_famille;
	Combobox nom_competence;
	Combobox nom_groupe;
	Combobox groupe;

	String selectedFamille;
	String selectedGroupe;
	String selectedCompetence;

	Button valider;
	Button annuler;

	Checkbox cc;
	HashMap<String,HashMap <String,HashMap<String, HashMap<String, ArrayList<String>>  >>> familleGroupe;
	HashMap<String, String> mapCodeCompetence=new HashMap <String, String>();
	HashMap<String, String> mapCodePoste=new HashMap <String, String>();
	HashMap<String, ArrayList<String>> mapComptetenceAptitudeObservable;
	ArrayList<String> listeposteTravail;

	HashMap <String, Checkbox> selectedCheckBox;
	HashMap <String, Checkbox> unselectedCheckBox;
	Component comp1;
	Div divupdown;
	Map map = new HashMap();
	Combobox comp_list;




	public CompetencePosteTravailAction() {
	}


	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Integer compagne_id=0;
		CompetencePosteTravailModel model=new CompetencePosteTravailModel();
		
		/*map=model.getCompagneList();

		Set set = (model.getCompagneList()).entrySet(); 
		Iterator itr = set.iterator();

		// Affichage de la liste des compagnes
		while(itr.hasNext()) {
			Map.Entry me = (Map.Entry)itr.next();

			comp_list.appendItem((String) me.getKey());
			
			//profilemodel.add((String) me.getKey());
		}
		if(comp_list.getItemCount()>0){
			comp_list.setSelectedIndex(0);
			compagne_id= (Integer) map.get((String)comp_list.getSelectedItem().getLabel());
		}*/

		//récupération des données à partir de la base

		selectedCheckBox=new HashMap <String, Checkbox>();
		unselectedCheckBox=new HashMap <String, Checkbox>();

		AptitudePosteTravailBean aptitudePosteTravailBean=new AptitudePosteTravailBean();
		
		aptitudePosteTravailBean=model.getAptitudePosteTravailBean();
		/*récupérer les aptitudes observables associés à chaque compétence*/
		mapComptetenceAptitudeObservable=model.getListAptitudeObservable();
		//assocation
		HashMap<String , ArrayList<String>> mapCompetencePoste=model.getAssociationPosteTravailCompetenceAptitudeObservable(aptitudePosteTravailBean);
		aptitudePosteTravailBean=model.fusionAptitude(aptitudePosteTravailBean,mapCompetencePoste );

		aptitudePosteTravailBean.setMapCompetenceAptitudeObservable(mapComptetenceAptitudeObservable);
		aptitudePosteTravailBean.setPosteTravail(model.getlistepostes());
		mapCodePoste=model.getlistepostesCode_postes();
		aptitudePosteTravailBean.setMapCodePoste(mapCodePoste);
		//MapFamille mapFamille=competencePosteTravailBean.getMapFamille();
		/*ArrayList<String>*/ listeposteTravail=aptitudePosteTravailBean.getPosteTravail();

		comp.setVariable(comp.getId() + "Ctrl", this, true);
		binder = new AnnotateDataBinder(comp);



		//construction des colonnes poste de travail
		for(int i=0;i<listeposteTravail.size();i++)
		{
			Column compcolumns=new Column();

			compcolumns.setStyle("text-align:vertical;");
			compcolumns.setWidth("100px" );
			compcolumns.setLabel(listeposteTravail.get(i));
			compcolumns.setParent(compcolumn);

		}		
		//construction des rows spreadsheet

		familleGroupe=aptitudePosteTravailBean.getListefamilles();
		mapCodeCompetence=aptitudePosteTravailBean.getMapCodeCompetence();

		Set<String> setFamille = familleGroupe.keySet( );


		List<String> listFamille = new ArrayList<String>(setFamille);

		//remplissage de la combobox listefamille
		for (int niv1=0; niv1<listFamille.size();niv1++)
		{
			String nom=listFamille.get(niv1);
			if(niv1==0)
				selectedFamille=nom;
			nom_famille.appendItem(nom);
		}

		// forcer la selection de la permiere ligne
		if(nom_famille.getItemCount()>0)
			nom_famille.setSelectedIndex(0);



		//remplissage de lalistbox groupe
		if(familleGroupe!=null)
		{

			HashMap <String,HashMap<String, HashMap<String, ArrayList<String>>  >> mapGroupeCompetence=familleGroupe.get(selectedFamille);
			if(mapGroupeCompetence!=null)
			{
				Set<String> setGroupe =mapGroupeCompetence.keySet(); 

				List<String> listGroupe = new ArrayList<String>(setGroupe);
				for (int niv1=0; niv1<listGroupe.size();niv1++)
				{
					String nom=listGroupe.get(niv1);
					if(niv1==0)
						selectedGroupe=nom;
					nom_groupe.appendItem(nom);
				}

				//forcer la selection du premier groupe
				if(nom_groupe.getItemCount()>0)
					nom_groupe.setSelectedIndex(0);

				//remplissage de la listBox compétence
				HashMap<String, HashMap<String, ArrayList<String>>  > competencePosteTravail= mapGroupeCompetence.get(selectedGroupe);
				if(competencePosteTravail!=null)
				{
					Set<String> setcompetence =competencePosteTravail.keySet(); 

					List<String> listCompetence = new ArrayList<String>(setcompetence);

					for (int niv1=0; niv1<listCompetence.size();niv1++)
					{
						String nom=listCompetence.get(niv1);
						if(niv1==0)
							selectedCompetence=nom;
						nom_competence.appendItem(nom);
					}

					//forcer la selection de la première compétence
					if(nom_competence.getItemCount()>0)
						nom_competence.setSelectedIndex(0);
					//Affichage du contenu du tableau
					//1- Affichage de la colonne apptitude observale.

					/**********************************************************************************************************/

					/*for(int z=0;z<listCompetence.size();z++)
					{*/


					//String scompetence=listCompetence.get(z);
					String codeCompetence=mapCodeCompetence.get(selectedCompetence);
					ArrayList<String> listAptirudeObservable=mapComptetenceAptitudeObservable.get(codeCompetence);
					for(int zz=0;zz<listAptirudeObservable.size();zz++)
					{
						Row row=new Row();

						String[] a=listAptirudeObservable.get(zz).split("#");
						String saptitudeObservable=a[1];
						String codeAptitude=a[0];
						if((zz!=0))
							row=new Row();
						row.setParent(lignes);
						Cell celluleNiv2= new Cell();
						celluleNiv2.setSclass("AptitudeObservable");




						Label labelNiv2=new Label();

						//attribution du style
						labelNiv2.setStyle("font-family: Verdana, Verdana, Arial,Helvetica,sans-serif; font-size: 10px;font-weight: ;");

						labelNiv2.setValue(saptitudeObservable); //affichage du label groupe
						labelNiv2.setParent(celluleNiv2);
						celluleNiv2.setParent(row);


						HashMap<String, ArrayList<String>>   mapCompotence_AptitudePost=competencePosteTravail.get(selectedCompetence);
						//ArrayList <String> listPosteTravailmap=competencePosteTravail.get(/*selectedCompetence*/cles);

						ArrayList <String> listPosteTravailmap=mapCompotence_AptitudePost.get(codeAptitude);

						for(int i=0;i<listeposteTravail.size();i++)
						{
							Checkbox checkbox=new Checkbox();
							String poste=listeposteTravail.get(i);


							boolean selectionner=false;
							if(listPosteTravailmap!=null)
							{
								Iterator<String> iterator=listPosteTravailmap.iterator();
								while(iterator.hasNext())
								{
									String posteTravail=(String)iterator.next();


									if(posteTravail.equals(poste))
									{
										selectionner=true;


									}

								}
							}
							if(selectionner)
								checkbox.setChecked(true);
							else
								checkbox.setChecked(false);
							checkbox.setParent(row);

							checkbox.addForward("onCheck", comp, "onModifyCheckedBox");
							String valeur =selectedFamille+"#"+selectedGroupe+"#"+selectedCompetence+"#"+poste+"#"+codeAptitude;
							checkbox.setValue(valeur);
						}

					}
					//}



					/*******************************************************************************************************/
					//2- Affichage du poste de travail

					//3- cocher les cases en fonction du contenu de la table
				}
			}
		}


		//		if(familleGroupe!=null)
		//		{
		//
		//		HashMap<String, HashMap<String, ArrayList<String>>> mapGroupeCompetence=familleGroupe.get(selectedFamille);
		//		if(mapGroupeCompetence!=null)
		//		{
		//			Set<String> setGroupe =mapGroupeCompetence.keySet(); 
		//			
		//			List<String> listGroupe = new ArrayList<String>(setGroupe);
		//			
		//			for (int niv1=0; niv1<listGroupe.size();niv1++)
		//			{
		//				Row row=new Row();
		//			s
		//				row.setParent(lignes);
		//	
		//				Cell celluleNiv1=new Cell();
		//				celluleNiv1.setSclass("Groupe");
		//				String sgroupe=listGroupe.get(niv1);
		//				
		//				HashMap<String, ArrayList<String>> competencePosteTravail= mapGroupeCompetence.get(sgroupe);
		//				Set<String> setcompetence =competencePosteTravail.keySet(); 
		//				
		//				List<String> listCompetence = new ArrayList<String>(setcompetence);
		//	
		//				//recuperation du nombre de lignes que doit contenir le niveau 1 et le nombre de feuilles
		//	
		//				
		//				int nbniv1=listCompetence.size();
		//				celluleNiv1.setRowspan(nbniv1);
		//					
		//			
		//				Label labelNiv1=new Label();
		//	
		//				
		//				//attribution du style
		//				labelNiv1.setStyle("font-family: Verdana, Verdana, Arial,Helvetica,sans-serif; font-size: 10px;font-weight: ;");
		//				labelNiv1.setValue(sgroupe); //affichage libelle niveau 1 ; niveau famille
		//				labelNiv1.setParent(celluleNiv1);
		//				celluleNiv1.setParent(row);
		//	
		//				
		//				
		//				//construction du niveau2
		//				
		//	
		//				for(int z=0;z<listCompetence.size();z++)
		//				{
		//					
		//					
		//						String scompetence=listCompetence.get(z);
		//						
		//						if((z!=0))
		//							row=new Row();
		//						row.setParent(lignes);
		//						Cell celluleNiv2= new Cell();
		//						celluleNiv2.setSclass("Competence");
		//	
		//	
		//				
		//	
		//						Label labelNiv2=new Label();
		//						
		//						//attribution du style
		//						labelNiv2.setStyle("font-family: Verdana, Verdana, Arial,Helvetica,sans-serif; font-size: 10px;font-weight: ;");
		//						labelNiv2.setValue(scompetence); //affichage du label groupe
		//						labelNiv2.setParent(celluleNiv2);
		//						celluleNiv2.setParent(row);
		//	
		//						
		//						ArrayList <String> listPosteTravailmap=competencePosteTravail.get(scompetence);
		//						
		//						
		//	
		//						for(int i=0;i<listeposteTravail.size();i++)
		//						{
		//							Checkbox checkbox=new Checkbox();
		//							String poste=listeposteTravail.get(i);
		//							Iterator<String> iterator=listPosteTravailmap.iterator();
		//							boolean selectionner=false;
		//							
		//							while(iterator.hasNext())
		//							{
		//								String posteTravail=(String)iterator.next();
		//								
		//								
		//								if(posteTravail.equals(poste))
		//								{
		//									selectionner=true;
		//	
		//	
		//								}
		//	
		//							}
		//							if(selectionner)
		//								checkbox.setChecked(true);
		//							else
		//								checkbox.setChecked(false);
		//							checkbox.setParent(row);
		//							
		//							checkbox.addForward("onCheck", comp, "onModifyCheckedBox");
		//							String valeur =selectedFamille+"#"+sgroupe+"#"+scompetence+"#"+poste;
		//							checkbox.setValue(valeur);
		//						}
		//	
		//					//}
		//					}
		//				}
		//			}
		//		}
		comp1=comp;
		binder.loadAll();

	}

	public void onModifyCheckedBox(ForwardEvent event){
		Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();		

		if (checkbox.isChecked())
		{
			//verifier si ça n'a pas encore été unchecked
			if(unselectedCheckBox.containsValue(checkbox))
			{
				unselectedCheckBox.remove(checkbox);

			}
			selectedCheckBox.put(checkbox.getValue(), checkbox);
		}
		else
		{
			//verifier si ça n'a pas encore été checked
			if(selectedCheckBox.containsValue(checkbox))
			{
				selectedCheckBox.remove(checkbox);

			}
			unselectedCheckBox.put(checkbox.getValue(), checkbox);
		}
		//selectedCheckBox
	}

	public void onSelect$nom_famille()
	{

		//s'il y a eu des modifications demander à l'utilisateur de valider ces modifications
		if (!selectedCheckBox.isEmpty() || !unselectedCheckBox.isEmpty())
		{
			try 
			{
				if (Messagebox.show("Voulez vous appliquer les modifications apportés à  cette famille ?", "Prompt", Messagebox.YES|Messagebox.NO,
						Messagebox.QUESTION) == Messagebox.YES)
				{
					validerModifications();
				}
				else
				{
					selectedCheckBox=new HashMap <String, Checkbox>();
					unselectedCheckBox=new HashMap <String, Checkbox>();
				}
			} 
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		selectedFamille=nom_famille.getSelectedItem().getLabel();
		/*********************************************************************************************/
		//selection des groupes associés à cette famille
		HashMap <String,HashMap<String, HashMap<String, ArrayList<String>>  >> mapGroupeCompetence=familleGroupe.get(selectedFamille);

		if(mapGroupeCompetence!=null)
		{
			Set<String> setGroupe =mapGroupeCompetence.keySet(); 

			List<String> listGroupe = new ArrayList<String>(setGroupe);
			nom_groupe.getChildren().clear();
			for (int niv1=0; niv1<listGroupe.size();niv1++)
			{
				String nom=listGroupe.get(niv1);
				if(niv1==0)
					selectedGroupe=nom;
				nom_groupe.appendItem(nom);
			}

			//forcer la selection du premier groupe
			if(nom_groupe.getItemCount()>0)
				nom_groupe.setSelectedIndex(0);

			//remplissage de la listBox compétence
			HashMap<String, HashMap<String, ArrayList<String>>  > competencePosteTravail= mapGroupeCompetence.get(selectedGroupe);
			if(competencePosteTravail!=null)
			{
				Set<String> setcompetence =competencePosteTravail.keySet(); 

				List<String> listCompetence = new ArrayList<String>(setcompetence);
				nom_competence.getChildren().clear();
				for (int niv1=0; niv1<listCompetence.size();niv1++)
				{
					String nom=listCompetence.get(niv1);
					if(niv1==0)
						selectedCompetence=nom;
					nom_competence.appendItem(nom);
				}

				//forcer la selection de la première compétence
				if(nom_competence.getItemCount()>0)
					nom_competence.setSelectedIndex(0);
				//Affichage du contenu du tableau
				//1- Affichage de la colonne apptitude observale.
			}
		}


		/**************************************************************************************/
		//Map attribut=competencePostetravailgd.re;
		lignes.detach();
		lignes=new Rows();
		lignes.setParent(competencePostetravailgd);
		afficherGrille(selectedFamille,selectedGroupe,selectedCompetence);
		competencePostetravailgd.renderAll();
	}

	public void onSelect$nom_groupe()
	{

		//s'il y a eu des modifications demander à l'utilisateur de valider ces modifications
		if (!selectedCheckBox.isEmpty() || !unselectedCheckBox.isEmpty())
		{
			try 
			{
				if (Messagebox.show("Voulez vous appliquer les modifications apportés à  ce groupe ?", "Prompt", Messagebox.YES|Messagebox.NO,
						Messagebox.QUESTION) == Messagebox.YES)
				{
					validerModifications();
				}
				else
				{
					selectedCheckBox=new HashMap <String, Checkbox>();
					unselectedCheckBox=new HashMap <String, Checkbox>();
				}
			} 
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		selectedGroupe=nom_groupe.getSelectedItem().getLabel();
		/*********************************************************************************************/
		//selection des groupes associés à cette famille
		HashMap <String,HashMap<String, HashMap<String, ArrayList<String>>  >> mapGroupeCompetence=familleGroupe.get(selectedFamille);
		// HashMap<String, HashMap<String, ArrayList<String>>> mapGroupeCompetence=familleGroupe.get(selectedFamille);
		if(mapGroupeCompetence!=null)
		{

			//remplissage de la listBox compétence
			HashMap<String, HashMap<String, ArrayList<String>>  > competencePosteTravail= mapGroupeCompetence.get(selectedGroupe);
			if(competencePosteTravail!=null)
			{
				Set<String> setcompetence =competencePosteTravail.keySet(); 

				List<String> listCompetence = new ArrayList<String>(setcompetence);
				nom_competence.getChildren().clear();
				for (int niv1=0; niv1<listCompetence.size();niv1++)
				{
					String nom=listCompetence.get(niv1);
					if(niv1==0)
						selectedCompetence=nom;
					nom_competence.appendItem(nom);
				}

				//forcer la selection de la première compétence
				if(nom_competence.getItemCount()>0)
					nom_competence.setSelectedIndex(0);
				//Affichage du contenu du tableau
				//1- Affichage de la colonne apptitude observale.
			}
		}


		/**************************************************************************************/
		//Map attribut=competencePostetravailgd.re;
		lignes.detach();
		lignes=new Rows();
		lignes.setParent(competencePostetravailgd);
		afficherGrille(selectedFamille,selectedGroupe,selectedCompetence);
		competencePostetravailgd.renderAll();
	}

	public void onSelect$nom_competence()
	{

		//s'il y a eu des modifications demander à l'utilisateur de valider ces modifications
		if (!selectedCheckBox.isEmpty() || !unselectedCheckBox.isEmpty())
		{
			try 
			{
				if (Messagebox.show("Voulez vous appliquer les modifications apportés à   cette compétence ?", "Prompt", Messagebox.YES|Messagebox.NO,
						Messagebox.QUESTION) == Messagebox.YES)
				{
					validerModifications();
				}
				else
				{
					selectedCheckBox=new HashMap <String, Checkbox>();
					unselectedCheckBox=new HashMap <String, Checkbox>();
				}
			} 
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		selectedCompetence=nom_competence.getSelectedItem().getLabel();

		//selection des groupes associés à cette famille
		//Map attribut=competencePostetravailgd.re;
		lignes.detach();
		lignes=new Rows();
		lignes.setParent(competencePostetravailgd);
		afficherGrille(selectedFamille, selectedGroupe, selectedCompetence);
		competencePostetravailgd.renderAll();
	}

	public void afficherGrille(String selectedFamille,String selectedGroup,String selectedCompetence)
	{


		selectedCheckBox=new HashMap <String, Checkbox>();
		unselectedCheckBox=new HashMap <String, Checkbox>();
		/**********************************************************************************/

		HashMap<String,HashMap<String, HashMap<String, ArrayList<String>>>> mapGroupeCompetence=familleGroupe.get(selectedFamille);
		//Set<String> setGroupe =mapGroupeCompetence.keySet(); 
		HashMap<String, HashMap<String, ArrayList<String>>> competencePosteTravail= mapGroupeCompetence.get(selectedGroupe);
		String codeCompetence=mapCodeCompetence.get(selectedCompetence);
		ArrayList<String> listAptirudeObservable=mapComptetenceAptitudeObservable.get(codeCompetence);
		for(int zz=0;zz<listAptirudeObservable.size();zz++)
		{
			Row row=new Row();

			String[] a=listAptirudeObservable.get(zz).split("#");
			String saptitudeObservable=a[1];
			String codeAptitude=a[0];
			if((zz!=0))
				row=new Row();
			row.setParent(lignes);
			Cell celluleNiv2= new Cell();
			celluleNiv2.setSclass("AptitudeObservable");




			Label labelNiv2=new Label();

			//attribution du style
			labelNiv2.setStyle("font-family: Verdana, Verdana, Arial,Helvetica,sans-serif; font-size: 10px;font-weight: ;");

			labelNiv2.setValue(saptitudeObservable); //affichage du label groupe
			labelNiv2.setParent(celluleNiv2);
			celluleNiv2.setParent(row);


			//ArrayList <String> listPosteTravailmap=competencePosteTravail.get(selectedCompetence);


			HashMap<String, ArrayList<String>>   mapCompotence_AptitudePost=competencePosteTravail.get(selectedCompetence);
			ArrayList <String> listPosteTravailmap=mapCompotence_AptitudePost.get(codeAptitude);



			for(int i=0;i<listeposteTravail.size();i++)
			{
				Checkbox checkbox=new Checkbox();
				String poste=listeposteTravail.get(i);
				boolean selectionner=false;
				if(listPosteTravailmap!=null)
				{
					Iterator<String> iterator=listPosteTravailmap.iterator();


					while(iterator.hasNext())
					{
						String posteTravail=(String)iterator.next();


						if(posteTravail.equals(poste))
						{
							selectionner=true;

							continue;
						}

					}
				}
				if(selectionner)
					checkbox.setChecked(true);
				else
					checkbox.setChecked(false);
				checkbox.setParent(row);

				checkbox.addForward("onCheck", comp1, "onModifyCheckedBox");
				String valeur =selectedFamille+"#"+selectedGroupe+"#"+selectedCompetence+"#"+poste+"#"+codeAptitude;
				checkbox.setValue(valeur);
			}

		}

		binder.loadAll();
	}

	public void onClick$valider() throws InterruptedException
	{
		//valider les modifications
		if (Messagebox.show("Voulez vous valider ces modifications ?  ", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			validerModifications();
			return;
		}
		else{
			return;
		}

	}
	/**
	 * cet evennement permet d'annuler les modifications apports sur cet écran
	 * @throws InterruptedException 
	 */
	public void onClick$annuler() throws InterruptedException
	{
		//mise à jour de l'affichage 
		if (Messagebox.show("Voulez vous annuler les sélections ?  ", "Prompt", Messagebox.YES|Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES) {
			annulerModifications();
			return;
		}

		else{
			return;
		}
	}


	/**
	 * 
	 */
	public void annulerModifications() {
		Set<String> setunselected = unselectedCheckBox.keySet( );
		ArrayList<String> listunselected = new ArrayList<String>(setunselected);
		Iterator<String>iterator=listunselected.iterator();
		while (iterator.hasNext())
		{
			String cles=(String)iterator.next();
			Checkbox checkBox=unselectedCheckBox.get(cles);
			checkBox.setChecked(true);
		}

		Set<String> setselected = selectedCheckBox.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		iterator=listselected.iterator();
		while (iterator.hasNext())
		{
			String cles=(String)iterator.next();
			Checkbox checkBox=selectedCheckBox.get(cles);
			checkBox.setChecked(false);
		}

		selectedCheckBox=new HashMap <String, Checkbox>();
		unselectedCheckBox=new HashMap <String, Checkbox>();
		binder.loadAll();
	}

	/**
	 * cette méthode permet d'enregistrer les modifications apportés dans la base de données
	 */
	public void validerModifications()
	{
		//mise a jour de la liste des unchecked
		Set<String> setunselected = unselectedCheckBox.keySet( );


		ArrayList<String> listunselected = new ArrayList<String>(setunselected);
		CompetencePosteTravailModel competencePosteTravailModel=new CompetencePosteTravailModel();
		competencePosteTravailModel.updateUnCheckedPoteTravailCompetenceAptitudeObs(listunselected,mapCodeCompetence, mapCodePoste);
		updateAffichageuncheked(listunselected);

		//mise a jour de la liste des checked
		Set<String> setselected = selectedCheckBox.keySet( );
		ArrayList<String> listselected = new ArrayList<String>(setselected);
		competencePosteTravailModel.updateCheckedPoteTravailCompetenceAptitudeObs(listselected,mapCodeCompetence, mapCodePoste);
		updateAffichagecheked(listselected);

		selectedCheckBox=new HashMap <String, Checkbox>();
		unselectedCheckBox=new HashMap <String, Checkbox>();

		binder.loadAll();

	}
	/**
	 * 
	 * @param listunselected
	 * @param selectedFamille
	 */
	public void updateAffichageuncheked(ArrayList <String>listunselected )
	{



		Iterator<String> iterator=listunselected.iterator();
		while (iterator.hasNext())
		{
			String cles=iterator.next();
			String[] liste=cles.split("#");
			String famille=liste[0];
			String groupe=liste[1];
			String competence=liste[2];
			String posteTravail=liste[3];
			String codeAptitude=liste[4];
			HashMap<String,HashMap<String, HashMap<String, ArrayList<String>>>> mapgroupe=familleGroupe.get(famille);
			HashMap<String,HashMap<String, ArrayList<String>>> mapcompetence=mapgroupe.get(groupe);
			HashMap<String, ArrayList<String>> mapAptitueObservable=mapcompetence.get(competence);

			/*Set<String> setAptitude =mapAptitueObservable.keySet(); 

				List<String> listAptitude = new ArrayList<String>(setAptitude);
				for (Iterator iterator2 = listAptitude.iterator(); iterator2
						.hasNext();) {
					String aptitude = (String) iterator2.next();*/

			ArrayList<String> listPosteTravail=mapAptitueObservable.get(codeAptitude);
			if(listPosteTravail.contains(posteTravail))		
				listPosteTravail.remove(posteTravail);
			mapAptitueObservable.put(codeAptitude,listPosteTravail);
			//}


		}
	}
	public void updateAffichagecheked(ArrayList <String>listselected )
	{



		Iterator<String> iterator=listselected.iterator();
		while (iterator.hasNext())
		{
			String cles=iterator.next();
			String[] liste=cles.split("#");
			String famille=liste[0];
			String groupe=liste[1];
			String competence=liste[2];
			String posteTravail=liste[3];
			String codeAptitude=liste[4];
			HashMap<String,HashMap<String, HashMap<String, ArrayList<String>>>> mapgroupe=familleGroupe.get(famille);
			HashMap<String,HashMap<String, ArrayList<String>>> mapcompetence=mapgroupe.get(groupe);
			HashMap<String,ArrayList<String>> mapAptitueObservable=mapcompetence.get(competence);

			/*Set<String> setAptitude =mapAptitueObservable.keySet(); 

				List<String> listAptitude = new ArrayList<String>(setAptitude);
				for (Iterator iterator2 = listAptitude.iterator(); iterator2
						.hasNext();) {
					String aptitude = (String) iterator2.next();*/

			ArrayList<String> listPosteTravail=mapAptitueObservable.get(codeAptitude);
			if(!listPosteTravail.contains(posteTravail))
				listPosteTravail.add(posteTravail);
			mapAptitueObservable.put(codeAptitude,listPosteTravail);
			//			}
			//				

		}
	}



	public void onClick$exportExcel() throws WrongValueException, ParseException {
		Executions.getCurrent().getDesktop().setAttribute("org.zkoss.zul.Fileupload.target", divupdown);

		try 
		{

			Fileupload fichierupload=new Fileupload();


			Media me=fichierupload.get("Merci de selectionner le fichier qui doit être chargé", "Chargement de fichier", true);

			chargerDonnees(me);


		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * cette méthode permet de charger les données qui se trouvent dans le fichier xls
	 * @param med
	 * @throws InterruptedException 
	 */
	public String chargerDonnees(Media med) throws InterruptedException
	{

		String ligneRejet="";

		if ((med != null)&&(med.getName()!=null)) 
		{
			String filename = med.getName();

			if(filename.endsWith(".xlsx")){

				CompetencePosteTravailModel competencePoste=new CompetencePosteTravailModel();
				try {
					ligneRejet=competencePoste.uploadXLSXPosteTravailAptitude(med.getStreamData());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} 				

		return ligneRejet;
	}
}


