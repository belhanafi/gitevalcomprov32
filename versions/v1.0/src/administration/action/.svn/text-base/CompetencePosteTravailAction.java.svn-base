package administration.action;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;

import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;

import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;


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
	Combobox groupe;
	
	String selectedFamille;

	Button valider;
	Button annuler;
	
	Checkbox cc;
	HashMap<String,HashMap<String, HashMap<String, ArrayList<String>> >> familleGroupe;
	HashMap<String, String> mapCodeCompetence=new HashMap <String, String>();
	HashMap<String, String> mapCodePoste=new HashMap <String, String>();
	ArrayList<String> listeposteTravail;
	
	HashMap <String, Checkbox> selectedCheckBox;
	HashMap <String, Checkbox> unselectedCheckBox;
	Component comp1;
	
	public CompetencePosteTravailAction() {
	}

	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		//récupération des données à partir de la base
		
		selectedCheckBox=new HashMap <String, Checkbox>();
		unselectedCheckBox=new HashMap <String, Checkbox>();
		CompetencePosteTravailBean competencePosteTravailBean=new CompetencePosteTravailBean();
		CompetencePosteTravailModel model=new CompetencePosteTravailModel();
		competencePosteTravailBean=model.getCompetencePosteTravailBean();
		//assocation
		HashMap<String , ArrayList<String>> mapCompetencePoste=model.getAssociationPosteTravailCompetence();
		competencePosteTravailBean=model.fusion(competencePosteTravailBean,mapCompetencePoste );
		
		
		competencePosteTravailBean.setPosteTravail(model.getlistepostes());
		mapCodePoste=model.getlistepostesCode_postes();
		competencePosteTravailBean.setMapCodePoste(mapCodePoste);
		//MapFamille mapFamille=competencePosteTravailBean.getMapFamille();
		/*ArrayList<String>*/ listeposteTravail=competencePosteTravailBean.getPosteTravail();
		
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
		
		familleGroupe=competencePosteTravailBean.getListefamilles();
		mapCodeCompetence=competencePosteTravailBean.getMapCodeCompetence();
		
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
		nom_famille.setSelectedIndex(0);
		


		

		HashMap<String, HashMap<String, ArrayList<String>>> mapGroupeCompetence=familleGroupe.get(selectedFamille);
		Set<String> setGroupe =mapGroupeCompetence.keySet(); 
		
		List<String> listGroupe = new ArrayList<String>(setGroupe);
		
		for (int niv1=0; niv1<listGroupe.size();niv1++)
		{
			Row row=new Row();
		
			row.setParent(lignes);

			Cell celluleNiv1=new Cell();
			celluleNiv1.setSclass("Groupe");
			String sgroupe=listGroupe.get(niv1);
			
			HashMap<String, ArrayList<String>> competencePosteTravail= mapGroupeCompetence.get(sgroupe);
			Set<String> setcompetence =competencePosteTravail.keySet(); 
			
			List<String> listCompetence = new ArrayList<String>(setcompetence);

			//recuperation du nombre de lignes que doit contenir le niveau 1 et le nombre de feuilles

			
			int nbniv1=listCompetence.size();
			celluleNiv1.setRowspan(nbniv1);
				
		
			Label labelNiv1=new Label();

			
			//attribution du style
			labelNiv1.setStyle("font-family: Verdana, Verdana, Arial,Helvetica,sans-serif; font-size: 10px;font-weight: ;");
			labelNiv1.setValue(sgroupe); //affichage libelle niveau 1 ; niveau famille
			labelNiv1.setParent(celluleNiv1);
			celluleNiv1.setParent(row);

			
			
			//construction du niveau2
			

			for(int z=0;z<listCompetence.size();z++)
			{
				
				
					String scompetence=listCompetence.get(z);
					
					if((z!=0))
						row=new Row();
					row.setParent(lignes);
					Cell celluleNiv2= new Cell();
					celluleNiv2.setSclass("Competence");


			

					Label labelNiv2=new Label();
					
					//attribution du style
					labelNiv2.setStyle("font-family: Verdana, Verdana, Arial,Helvetica,sans-serif; font-size: 10px;font-weight: ;");
					labelNiv2.setValue(scompetence); //affichage du label groupe
					labelNiv2.setParent(celluleNiv2);
					celluleNiv2.setParent(row);

					
					ArrayList <String> listPosteTravailmap=competencePosteTravail.get(scompetence);
					
					

					for(int i=0;i<listeposteTravail.size();i++)
					{
						Checkbox checkbox=new Checkbox();
						String poste=listeposteTravail.get(i);
						Iterator<String> iterator=listPosteTravailmap.iterator();
						boolean selectionner=false;
						
						while(iterator.hasNext())
						{
							String posteTravail=(String)iterator.next();
							
							
							if(posteTravail.equals(poste))
							{
								selectionner=true;


							}

						}
						if(selectionner)
							checkbox.setChecked(true);
						else
							checkbox.setChecked(false);
						checkbox.setParent(row);
						
						checkbox.addForward("onCheck", comp, "onModifyCheckedBox");
						String valeur =selectedFamille+"#"+sgroupe+"#"+scompetence+"#"+poste;
						checkbox.setValue(valeur);
					}

				//}
			}
		}
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
				 if (Messagebox.show("Voulez vous appliquer les modifications apportés à   cette famille ?", "Prompt", Messagebox.YES|Messagebox.NO,
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
		 
		 //selection des groupes associés à cette famille
		 //Map attribut=competencePostetravailgd.re;
		lignes.detach();
		lignes=new Rows();
		lignes.setParent(competencePostetravailgd);
		afficherGrille(selectedFamille);
		competencePostetravailgd.renderAll();
	 }
	 

	 
	 public void afficherGrille(String selectedFamille)
	 {

			
		 	selectedCheckBox=new HashMap <String, Checkbox>();
			unselectedCheckBox=new HashMap <String, Checkbox>();
			HashMap<String, HashMap<String, ArrayList<String>>> mapGroupeCompetence=familleGroupe.get(selectedFamille);
			Set<String> setGroupe =mapGroupeCompetence.keySet(); 
			
			List<String> listGroupe = new ArrayList<String>(setGroupe);
			
			for (int niv1=0; niv1<listGroupe.size();niv1++)
			{
				Row row=new Row();
			
				row.setParent(lignes);

				Cell celluleNiv1=new Cell();
				celluleNiv1.setSclass("Groupe");
				String sgroupe=listGroupe.get(niv1);

				HashMap<String, ArrayList<String>> competencePosteTravail= mapGroupeCompetence.get(sgroupe);
				Set<String> setcompetence =competencePosteTravail.keySet(); 
				
				List<String> listCompetence = new ArrayList<String>(setcompetence);

				//recuperation du nombre de lignes que doit contenir le niveau 1 et le nombre de feuilles

				
				int nbniv1=listCompetence.size();
				celluleNiv1.setRowspan(nbniv1);
					
			
				Label labelNiv1=new Label();
				
				//attribution du style
				labelNiv1.setStyle("font-family: Verdana, Verdana, Arial,Helvetica,sans-serif; font-size: 10px;font-weight: ;");
				labelNiv1.setValue(listGroupe.get(niv1)); //affichage libelle niveau 1 ; niveau famille
				labelNiv1.setParent(celluleNiv1);
				celluleNiv1.setParent(row);

				
				
				//construction du niveau2
				

				for(int z=0;z<listCompetence.size();z++)
				{
				
					
						String scompetence=listCompetence.get(z);
						
						if((z!=0))
							row=new Row();
						row.setParent(lignes);
						Cell celluleNiv2= new Cell();
						celluleNiv2.setSclass("Competence");


				

						Label labelNiv2=new Label();
						//attribution du style
						labelNiv2.setStyle("font-family: Verdana, Verdana, Arial,Helvetica,sans-serif; font-size: 10px;font-weight: ;");
						labelNiv2.setValue(scompetence); //affichage du label groupe
						labelNiv2.setParent(celluleNiv2);
						celluleNiv2.setParent(row);

						
						ArrayList <String> listPosteTravailmap=competencePosteTravail.get(scompetence);
					
						
						for(int i=0;i<listeposteTravail.size();i++)
						{
							Checkbox checkbox=new Checkbox();
							String poste=listeposteTravail.get(i);
							Iterator<String> iterator=listPosteTravailmap.iterator();
							boolean selectionner=false;
							
							while(iterator.hasNext())
							{
								String posteTravail=(String)iterator.next();
								
								
								if(posteTravail.equals(poste))
								{
									selectionner=true;
									
									//checkbox.setChecked(true);
								}

							}
							if(selectionner)
								checkbox.setChecked(true);
							else
								checkbox.setChecked(false);
							checkbox.setParent(row);
							checkbox.addForward("onCheck", comp1, "onModifyCheckedBox");
							String valeur =selectedFamille+"#"+sgroupe+"#"+scompetence+"#"+poste;
							checkbox.setValue(valeur);
						}

					//}
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
		competencePosteTravailModel.updateUnCheckedPoteTravailCompetence(listunselected,mapCodeCompetence, mapCodePoste);
		updateAffichageuncheked(listunselected);
		
		//mise a jour de la liste des checked
		 Set<String> setselected = selectedCheckBox.keySet( );
		 ArrayList<String> listselected = new ArrayList<String>(setselected);
		competencePosteTravailModel.updateCheckedPoteTravailCompetence(listselected,mapCodeCompetence, mapCodePoste);
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
				HashMap<String, HashMap<String, ArrayList<String>>> mapgroupe=familleGroupe.get(famille);
				HashMap<String, ArrayList<String>> mapcompetence=mapgroupe.get(groupe);
				ArrayList<String> listPosteTravail=mapcompetence.get(competence);
				
				listPosteTravail.remove(posteTravail);
				
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
				HashMap<String, HashMap<String, ArrayList<String>>> mapgroupe=familleGroupe.get(famille);
				HashMap<String, ArrayList<String>> mapcompetence=mapgroupe.get(groupe);
				ArrayList<String> listPosteTravail=mapcompetence.get(competence);
				
				listPosteTravail.add(posteTravail);
				
			}
	 }
}


