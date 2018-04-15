package compagne.action;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Timer;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;


import administration.bean.CompteBean;
import administration.bean.CotationBean;
import administration.model.CompetencePosteTravailModel;

import common.ApplicationFacade;
import compagne.bean.EmployesAEvaluerBean;
import compagne.bean.FicheEvaluationBean;
import compagne.bean.MapEmployesAEvaluerBean;
import compagne.model.FicheEvaluationModel;

public class FicheEvaluationAction extends GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	//variales pour la gestion du timer
	Timer timer;
	
	Label count;
	int secondes=0;
	int minutes=ApplicationFacade.getInstance().getTimerValue();
	boolean first=true;
	Button start;
	
	
	boolean validationtente=false;
	boolean autorise=false;
	Tab FValide ;
	Tabpanel fichevalide;
	Tab FicheEvaluation;
	Tab AEvaluer;
	Tabpanel maFiche;
	Tabpanel evaluations;
	Html html;
	Tabbox tb;
	Textbox posteTravail;
	Textbox nomEmploye;
	Textbox groupe;
	Combobox employe; 
	Combobox poste_travail;
	Combobox Famille;
	Button valider;
	Listbox employelb;
	Button help1;
	Button help2;
	Button help3;
	Popup help1Pop;
	Html htmlhelp1;
	Component comp1;
	
	//objets graphiques de l'onglet des omployes deja evalués
	Textbox posteTravailV;
	Textbox nomEmployeV;
	Button help1V;
	Button help2V;
	Combobox FamilleV;	
	Combobox employeV; 
	Combobox poste_travailV;
	Listbox employelbV;
	//objets graphique de l'onglet la fiche d'evaluation
	
	Textbox nomEmployeM;
	Textbox posteTravailM;
	Combobox FamilleM;
	Groupbox gb3;
	Listbox employelbM;
	Caption labelM;
	//objets a utiliser
	
	HashMap<String, ArrayList<FicheEvaluationBean>> mapPosteTravailFiche;
	HashMap<String, ArrayList<FicheEvaluationBean>> mapPosteTravailFicheV;
	HashMap <String, String > mapintitule_codeposte;
	HashMap <String, String > mapcode_intituleposte;
	HashMap <String, ArrayList<Listitem>> mapItemsFamille =new HashMap<String, ArrayList<Listitem>>();
	HashMap <String, ArrayList<Listitem>> mapItemsFamilleV =new HashMap<String, ArrayList<Listitem>>();
	HashMap <String, String > mapcode_description_poste;
	ArrayList <CotationBean> listCotation;
	HashMap<String , Combobox>listeCombo=new HashMap<String, Combobox>();
	
	HashMap<String , Combobox>listeComboV=new HashMap<String, Combobox>();
	
	HashMap<String,HashMap<String , Combobox>> mapFamilleCombo=new HashMap<String, HashMap<String , Combobox>> ();
	HashMap<String,HashMap<String , Combobox>> mapFamilleComboV=new HashMap<String, HashMap<String , Combobox>> ();
	ArrayList<Listitem> currentListItem=null;
	
	ArrayList<Listitem> currentListItemM=null;
	ArrayList<Listitem> currentListItemV=null;
	
	// objets de la selection en cours
	String selectedEmploye;
	MapEmployesAEvaluerBean mapEmployeAEvaluerBean;
	MapEmployesAEvaluerBean mapEmployeEvalueBean;
	String selectedFamille;
	String selectednomposteTravail;

	String selectedFamilleM;
	
	String selectedEmployeV;
	String selectedFamilleV;
	String selectednomposteTravailV;
	
	HashMap <String, ArrayList<FicheEvaluationBean>> mapfamilleFicheEvaluationM;
	HashMap <String, ArrayList<FicheEvaluationBean>> mapfamilleFicheEvaluationV;
	
	ArrayList <String> listFamillePoste;
	ArrayList <String> listFamillePosteV;
	
	int id_employe;
	
	//EmployesAEvaluerBean employerAEvaluerBean1;
	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		
		super.doAfterCompose(comp);
		comp1=comp;
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		
		valider.setDisabled(true);
		
		//recupération du profil de l'utilisateur
		
		CompteBean compteUtilisateur=ApplicationFacade.getInstance().getCompteUtilisateur();
		
		//récupération de l'id_employé associé à l'id_compte
		FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
		id_employe=ficheEvaluationModel.getIdEmploye(compteUtilisateur.getId_compte());
		compteUtilisateur.setId_employe(id_employe);
		
		
		//récuperation de la description des poste pour le bouton help
		
		mapcode_description_poste=ficheEvaluationModel.getPosteTravailDescription();
		
		//récupération de la cotation
		listCotation=ficheEvaluationModel.getCotations();
		
		
		//récupération de l'information sur la validité de la fiche de l'employé connecté
		boolean ficheValide=ficheEvaluationModel.getValiditeFiche(id_employe);
		
		//récupération des informations associées à une fiche d'evaluation à remplir;
		
		 mapPosteTravailFiche=ficheEvaluationModel.getInfosFicheEvaluationparPoste();
		 CompetencePosteTravailModel compt=new CompetencePosteTravailModel();
		 mapintitule_codeposte=compt.getlistepostesCode_postes();
		 mapcode_intituleposte=compt.getlisteCode_postes_intituleposte();
		//onglet Ma Fiche d'évaluation
		
		evaluations.setStyle("overflow:auto");
//		posteTravail.setDisabled(true);
//		nomEmploye.setDisabled(true);
		
		//remplissage de la combobox famille
		

		if(ficheValide)
		{
			
			//construction et affichage du contenu de la page d'evaluation de la personne connecté
			
			
			//récupération du nom de la personne connecté
			String retour=ficheEvaluationModel.getNomUtilisateur(id_employe);
			String[] val=retour.split("#");
			nomEmployeM.setText(val[0]);
			String intitule_posteM=val[1];
			posteTravailM.setText(intitule_posteM);
			
			String code_posteM=mapintitule_codeposte.get(intitule_posteM);
			Set <String> listeFamille=mapPosteTravailFiche.keySet();
			Iterator<String> itarator =listeFamille.iterator();
			ArrayList <String > listeFamilleM=new ArrayList<String>();
			while(itarator.hasNext())
			{
				String cles=itarator.next();
				if(cles.startsWith(code_posteM+"#"))
				{
					//inserer famille dans la comboBox
					String[] val1=cles.split("#");
					FamilleM.appendItem(val1[1]);
					listeFamilleM.add(val1[1]);
					
				}
			}
			if(listeFamilleM.size()==0)
			{
				//aucune compagne n'est prévu pour ce poste de travail
				
				//enlever ce qui existe et afficher la page HTML 
				//affichage d'un message disant que la fiche ne peut être visible
				gb3.detach();
				html=new Html();
				String content="Vous n'avez été convoqué à aucune compagne d'évaluation ";
				html.setStyle("color:red;margin-left:15px");
				html.setContent(content);
				html.setParent(maFiche);
			}
			else
			{
				
				//récupération de la fiche d'avaluation de l'id_employe
				
				mapfamilleFicheEvaluationM=ficheEvaluationModel.getMaFicheEvaluaton(id_employe);
				//affichage de la cotation
				if(FamilleM.getItemCount()>0)
					FamilleM.setSelectedIndex(0);
		 
				selectedFamilleM=listeFamilleM.get(0);
				ArrayList<FicheEvaluationBean> listficheEvaluationBean=mapfamilleFicheEvaluationM.get(selectedFamilleM);
				ArrayList<Listitem> liste=new ArrayList<Listitem>();
				
				/******************************/
				Iterator <FicheEvaluationBean> iteratorFiche=listficheEvaluationBean.iterator();
				while (iteratorFiche.hasNext())
				{
					FicheEvaluationBean ficheEvaluationBean=iteratorFiche.next();
				
					//mise à jour du titre
					
					
					//labelM.setLabel("Fiche Employé : évaluation du "+  ficheEvaluationBean.getDate_evaluation() +" de la compagne "+ ficheEvaluationBean.getCompagne_type());
					labelM.setLabel("Fiche Employé : Compagne  "+ ficheEvaluationBean.getCompagne_type() +" du "+ ficheEvaluationBean.getDate_evaluation() );
					
					//affichage des donnnées dans le tableau
					Listitem listItem=new Listitem();
					listItem.setParent(employelbM);
				 	 
					 
					//cellule competence
					Listcell cellulecompetence=new Listcell();
					cellulecompetence.setLabel(ficheEvaluationBean.getLibelle_competence());
					cellulecompetence.setTooltiptext(ficheEvaluationBean.getDefinition_competence());
					cellulecompetence.setParent(listItem);
					 
					//cellule aptitude observable
					Listcell celluleaptitude=new Listcell();
					celluleaptitude.setLabel(ficheEvaluationBean.getAptitude_observable());
					celluleaptitude.setParent(listItem);
					 
					 
					//cellule niveau de maitrise
					Listcell cellulecotation=new Listcell();
					Iterator<CotationBean> itcotationBean =listCotation.iterator();
					boolean conti=true;
					int valCotation=0;
					while((itcotationBean.hasNext() && conti))
					{
						CotationBean cotationbean=itcotationBean.next();
						int valeur=cotationbean.getId_cotation();
						
						valCotation=cotationbean.getValeur_cotation();
						if(valeur==ficheEvaluationBean.getNiveau_maitrise())

						{
							conti=false;
							valCotation=ficheEvaluationBean.getNiveau_maitrise();
						}
					}
							
					
					cellulecotation.setLabel(valCotation+"");
				 
					cellulecotation.setParent(listItem);
					
					liste.add(listItem);
				}
				/*****************/
				currentListItemM=liste;
			}
		}
		else
		{
			//enlever ce qui existe et afficher la page HTML 
			
			gb3.detach();
			
			//affichage d'un message disant que la fiche ne peut être visible
			html=new Html();
			String content="Vous n'avez pas accès à votre fiche d'évaluation car elle n'a pas encore été complété par l'évaluateur";
			html.setStyle("color:red;margin-left:15px");
			html.setContent(content);
			html.setParent(maFiche);
		}
		if (compteUtilisateur.getId_profile()==1)
		{
			FicheEvaluation.detach();
			maFiche.detach();
		}
		//si c'est un évaluateur alors on affiche la liste des fiches associés aux employés à évaluer
		if(compteUtilisateur.getId_profile()==3)
		{
			
			//remplissage du contenu de la combo associée aux postes de travail
			 mapEmployeAEvaluerBean=ficheEvaluationModel.getListEmployesAEvaluer(id_employe);
			 HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeAEvaluerBean.getMapclesposte();
			Set <String>listePoste= Mapclesposte.keySet();
			Iterator <String > iterator=listePoste.iterator();
			poste_travail.appendItem("Tous poste de travail");
			while(iterator.hasNext())
			{
				String nomPoste=iterator.next();
				poste_travail.appendItem(nomPoste);
			}
			//selection du premier item (tous poste de travail)
			poste_travail.setSelectedIndex(0);
			
			//remplissage de la comboBox avec tous les nom des employes quelque soit leur type de poste
			HashMap<String, EmployesAEvaluerBean> mapclesEmploye=mapEmployeAEvaluerBean.getMapclesnomEmploye();
			Set <String>listeEmploye=mapclesEmploye.keySet();
			iterator=listeEmploye.iterator();
			employe.appendItem("sélectionner un employé");
			while(iterator.hasNext())
			{
				String nomEmploye=iterator.next();
				employe.appendItem(nomEmploye);
				
			}
			//selection du premier item de la combobox employe
			if(employe.getItemCount()>0)
				employe.setSelectedIndex(0);
			
		}
		else //ne pas afficher cet onglet
		{
			
			/**
			 * TODO ne rendre cette page invisible que sur certaines conditions
			 */
			//AEvaluer.setVisible(false);
			AEvaluer.detach();
			evaluations.detach();
			
			

		}
		
		//si c'est un administrateur, il peut voir toutes les fiches d'evaluations ou evaluateur
		if((compteUtilisateur.getId_profile()==3)||(compteUtilisateur.getId_profile()==2) ||(compteUtilisateur.getId_profile()==1))
		{
			
			
			mapPosteTravailFicheV=ficheEvaluationModel.getInfosFicheEvaluationparPoste();
			 
			 
			 
			//remplissage du contenu de la combo associée aux postes de travail
			//si c'est un evaluateur lancer cette methode
			if (compteUtilisateur.getId_profile()==3)
				mapEmployeEvalueBean=ficheEvaluationModel.getListEmployesvalue(id_employe);
			 
			//sinon (administrateur ) lancer un eautre méthode qui récupère tous ceux qui ont été évaluées
			if ((compteUtilisateur.getId_profile()==2)||(compteUtilisateur.getId_profile()==1))
				mapEmployeEvalueBean=ficheEvaluationModel.getListTousEmployesvalue();
			 HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeEvalueBean.getMapclesposte();
			Set <String>listePoste= Mapclesposte.keySet();
			Iterator <String > iterator=listePoste.iterator();
			poste_travailV.appendItem("Tous poste de travail");
			while(iterator.hasNext())
			{
				String nomPoste=iterator.next();
				poste_travailV.appendItem(nomPoste);
			}
			//selection du premier item (tous poste de travail)
			poste_travailV.setSelectedIndex(0);
			
			//remplissage de la comboBox avec tous les nom des employes quelque soit leur type de poste
			HashMap<String, EmployesAEvaluerBean> mapclesEmploye=mapEmployeEvalueBean.getMapclesnomEmploye();
			Set <String>listeEmploye=mapclesEmploye.keySet();
			iterator=listeEmploye.iterator();
			employeV.appendItem("sélectionner un employé");
			while(iterator.hasNext())
			{
				String nomEmploye=iterator.next();
				employeV.appendItem(nomEmploye);

			}
			//selection du premier item de la combobox employe
			if(employeV.getItemCount()>0)
				employeV.setSelectedIndex(0);
		}
		else
		{
			//rendre l'onglet invisible 
			FValide.detach();
			fichevalide.detach();
		}
		tb.setSelectedIndex(0);
		 timer.addEventListener(Events.ON_TIMER, new EventListener() {
				public void onEvent(Event evt) {
					
					timer.setRepeats(true);
					
					//afficher le timer chaque minute
					if(first)
					{
						count.setValue(  minutes  + ":00" );
						first=false;
					}
					else
					{
						if(secondes<=10)
						{	
							if(secondes==0)
							{
								secondes=60;
								if(minutes<=10)
									count.setValue( "0" + --minutes  + ":" + --secondes);
								else
									count.setValue( --minutes  + ":" + --secondes);
							}
							else
							{
								if(minutes<=9)
									count.setValue( "0" + minutes  + ":0" + --secondes);
								else
									count.setValue( minutes  + ":0" + --secondes);
							}

							
						}
						else
						{
							if(minutes<=9)
								count.setValue( "0" + minutes  + ":" + --secondes);
							else
								count.setValue( minutes  + ":" + --secondes);
						}
					}
				 	if (minutes <= 0) {
					timer.stop();
					return;
				 	}
				}
				});
		 count.setVisible(false);
		 start.setDisabled(true);
	}
	
	/**
	 * evenement a gerer lors de la selection d'un employé
	 */
	 public void onSelect$employe()
	 {
		 //rinitialisation du timer
		 
		 	count.setVisible(true);
		 	start.setDisabled(false);
		 	timer.stop();
			
			
			secondes=0;
			minutes=60;
			first=true;
			count.setValue("60:00");
		//fin reinitialisation du timer	
			
		 validationtente=false;
		 //vider le contenu de la grille associée à l'ancien employé selectionné
		 autorise=true;
		 
		 mapFamilleCombo=new HashMap<String, HashMap<String , Combobox>> (); 
		 valider.setDisabled(false);
		 if (currentListItem!=null)
		 {
			 Iterator<Listitem> iterator=currentListItem.iterator();
			 while(iterator.hasNext())
			 {
				 Listitem item=iterator.next();
				 item.detach();
			 }
			 selectedEmploye="";
			 nomEmploye.setText(selectedEmploye);
			 selectednomposteTravail="";
			 posteTravail.setText(selectednomposteTravail);
		 }
		 //si un employé a déja été selectionner alors vider le contnu de la combobox
		 if(Famille.getItemCount()>0)
		 {
			int nb= Famille.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{
				
				Famille.removeItemAt(i);
			}
		 }
		
		 mapItemsFamille=new 	HashMap<String, ArrayList<Listitem>>();	 
		 //employelb.renderAll();
		 //lors de la selection d'un employé, affichage d ela fiche associé à cet employé si elle existe 
		 //si la fiche n'existe pas , il faut la creer
		 
		 //1. affichage des informations relatifs à l'employé

		 selectedEmploye=employe.getSelectedItem().getLabel();
		 
		 if(!selectedEmploye.equals("sélectionner un employé"))
		 {
			 
			 nomEmploye.setText(selectedEmploye);
			 EmployesAEvaluerBean employerAEvaluerBean=mapEmployeAEvaluerBean.getMapclesnomEmploye().get(selectedEmploye);
			 //employerAEvaluerBean1=employerAEvaluerBean;
			 selectednomposteTravail=employerAEvaluerBean.getPoste_travail();
			 posteTravail.setText(selectednomposteTravail);
			 
			 String code_poste=mapintitule_codeposte.get(selectednomposteTravail);
			 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
			 listFamillePoste=ficheEvaluationModel.getFamilleAssociePoste(code_poste);
			 //ArrayList <String> listFamille=employerAEvaluerBean.getFamille();
			 Iterator<String> iterator=listFamillePoste.iterator();
		 	while(iterator.hasNext())
		 	{
		 		String famille=iterator.next();
		 		Famille.appendItem(famille);
		 	}
		 	if(Famille.getItemCount()>0)
		 		Famille.setSelectedIndex(0);
		 
		 	selectedFamille=listFamillePoste.get(0);
		 
		 	//afficher toutes les données associées à ce poste de travail
		 
		 	//recuperation du code_poste associé à l'intitule
		 	
		 
		 	String cles=code_poste+"#"+selectedFamille;
		 
		 



			 //afficher le contenu de mapPosteTravailFiche
			 ArrayList<FicheEvaluationBean> listFiche=mapPosteTravailFiche.get(cles);
			 
			 
			 Iterator<FicheEvaluationBean> iterator2=listFiche.iterator();
			 
			 ArrayList<Listitem> liste=new ArrayList<Listitem>();
			 listeCombo=new HashMap<String, Combobox>();
			 while (iterator2.hasNext())
			 {
				 FicheEvaluationBean ficheEvaluation=(FicheEvaluationBean)iterator2.next();
				 String libelleCompetence=ficheEvaluation.getLibelle_competence();
				 String aptitudeObservable=ficheEvaluation.getAptitude_observable();
				 String descriptionCompetence=ficheEvaluation.getDefinition_competence();
				 
				 //affichage des donnnées dans le tableau
				 Listitem listItem=new Listitem();
				 listItem.setParent(employelb);
				 	 
					 
				 //cellule competence
				 Listcell cellulecompetence=new Listcell();
				 cellulecompetence.setLabel(libelleCompetence);
				 cellulecompetence.setTooltiptext(descriptionCompetence);
				 cellulecompetence.setParent(listItem);
					 
				 //cellule aptitude observable
				 Listcell celluleaptitude=new Listcell();
				 celluleaptitude.setLabel(aptitudeObservable);
				 celluleaptitude.setParent(listItem);
					 
					 
				 //cellule niveau de maitrise
				 Listcell cellulecotation=new Listcell();
				 Combobox cotation=new Combobox();
				 //cotation.setWidth("50%");
				 Iterator <CotationBean> iterator4=listCotation.iterator();
				 String valeur="";
				 while(iterator4.hasNext())
				 {
					 CotationBean cotationBean=(CotationBean)iterator4.next();
					 cotation.appendItem(cotationBean.getValeur_cotation()+"");
					 
					 valeur= ficheEvaluation.getId_repertoire_competence()+"#"+employerAEvaluerBean.getId_employe()+"#"+employerAEvaluerBean.getId_planning_evaluation();
					 cotation.setContext(valeur);
					 
					 cotation.setName("-1");
					 cotation.setReadonly(true);
					 cotation.setParent(cellulecotation);
					 cellulecotation.setParent(listItem);
					 
				 }
				 cotation.addForward("onSelect", comp1, "onSelectEvaluation");
				 listeCombo.put(valeur,cotation );
				 liste.add(listItem);
					 
			 }
			 mapItemsFamille.put(cles, liste);
			 currentListItem=liste;
			 mapFamilleCombo.put(selectedFamille, listeCombo);
			 
			 // affichage du timer

		 }		 
	 }
	 
	 /**
		 * evenement a gerer lors de la selection d'un poste de travail
		 */
	 public void onSelect$poste_travail()
	 {
		 

		 
		 //vider le contenu de la grille associée à l'ancien employé selectionné
		 if (currentListItem!=null)
		 {
			 Iterator<Listitem> iterator=currentListItem.iterator();
			 while(iterator.hasNext())
			 {
				 Listitem item=iterator.next();
				 item.detach();
			 }
			 selectedEmploye="";
			 nomEmploye.setText(selectedEmploye);
			 selectednomposteTravail="";
			 posteTravail.setText(selectednomposteTravail);
		 }
		 //si un employé a déja été selectionné alors vider le contnu de la combobox
		 if(Famille.getItemCount()>0)
		 {
			int nb= Famille.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{
				
				Famille.removeItemAt(i);
			}
		 }
		 //vider le contenu de la combo employe 
		 if(employe.getItemCount()>0)
		 {
			int nb= employe.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{
				
				employe.removeItemAt(i);
			}
		 }
		 employe.appendItem("sélectionner un employé");
		 
		 //1. mise à jour de la liste des employes avec la selection de l'attribut selectionner un employe
		 //avec toutes les conséquences qui doivent se découler de cette de cette selection
		 
		//remplissage du contenu de la combo associée aux postes de travail
		 selectednomposteTravail=poste_travail.getSelectedItem().getLabel();
		 if(!selectednomposteTravail.equals("Tous poste de travail"))
		 {
			 HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeAEvaluerBean.getMapclesposte();
			 HashMap<String, EmployesAEvaluerBean> mapEmploye=Mapclesposte.get(selectednomposteTravail);
			Set <String> listEmploye=mapEmploye.keySet();
			Iterator<String> iterator =listEmploye.iterator();
			while(iterator.hasNext())
			{
				String nomEmploye=iterator.next();
				employe.appendItem(nomEmploye);
			}
			employe.setSelectedIndex(0);
			 
			 
		 }
		 else
		 {
			 //afficher tous les employe (tous poste de travail confondu)
			 
			//remplissage de la comboBox avec tous les nom des employes quelque soit leur type de poste
				HashMap<String, EmployesAEvaluerBean> mapclesEmploye=mapEmployeAEvaluerBean.getMapclesnomEmploye();
				Set <String>listeEmploye=mapclesEmploye.keySet();
				Iterator<String> iterator=listeEmploye.iterator();
				//employe.appendItem("sélectionner un employé");
				while(iterator.hasNext())
				{
					String nomEmploye=iterator.next();
					employe.appendItem(nomEmploye);
					
				}
				//selection du premier item de la combobox employe
				if(employe.getItemCount()>0)
					employe.setSelectedIndex(0);
			 
		 }


	 }
	 
	 /**
		 * evenement a gerer lors de la selection d'une famille
		 */
	 public void onSelect$Famille()
	 {
		 //récupération de la famille selectionnée
		 selectedFamille=(String)Famille.getSelectedItem().getLabel();
		 
		 //lors de la selection d'une famille, il faut :
		 //1. vider le contenu de la table
		 //2. remplir la table avec le cntenu de la nouvelle cles code_poste, famille
		 
		 
		//1. vider le contenu de la table
		 if (currentListItem!=null)
		 {
			 Iterator<Listitem> iterator=currentListItem.iterator();
			 while(iterator.hasNext())
			 {
				 Listitem item=iterator.next();
				 item.detach();
			 }
		 }
		 
		 //recuperer les infos associé à l'employé selectionné
		 EmployesAEvaluerBean employerAEvaluerBean=mapEmployeAEvaluerBean.getMapclesnomEmploye().get(selectedEmploye);
		//recuperation du code_poste associé à l'intitule
		 String code_poste=mapintitule_codeposte.get(selectednomposteTravail);
		 
		 String cles=code_poste+"#"+selectedFamille;
		 
		 //afficher le contenu de mapPosteTravailFiche
		 ArrayList<FicheEvaluationBean> listFiche=mapPosteTravailFiche.get(cles);
		 
		 
		 Iterator<FicheEvaluationBean> iterator2=listFiche.iterator();
		 
		 ArrayList<Listitem> liste=new ArrayList<Listitem>();
		 
		 HashMap<String, Combobox> listeComboMAJ=mapFamilleCombo.get(selectedFamille);
		 listeCombo=new HashMap<String, Combobox>();
		 while (iterator2.hasNext())
		 {
			 FicheEvaluationBean ficheEvaluation=(FicheEvaluationBean)iterator2.next();
			 String libelleCompetence=ficheEvaluation.getLibelle_competence();
			 String aptitudeObservable=ficheEvaluation.getAptitude_observable();
			 String descriptionCompetence=ficheEvaluation.getDefinition_competence();
			 
			 //affichage des donnnées dans le tableau
			 Listitem listItem=new Listitem();
			 listItem.setParent(employelb);
			 	 
				 
			 //cellule competence
			 Listcell cellulecompetence=new Listcell();
			 cellulecompetence.setLabel(libelleCompetence);
			 cellulecompetence.setTooltiptext(descriptionCompetence);
			 cellulecompetence.setParent(listItem);
				 
			 //cellule aptitude observable
			 Listcell celluleaptitude=new Listcell();
			 celluleaptitude.setLabel(aptitudeObservable);
			 celluleaptitude.setParent(listItem);
				 
				 
			 //cellule niveau de maitrise
			 Listcell cellulecotation=new Listcell();
			 Combobox cotation=new Combobox();
			 //cotation.setWidth("50%");
			 Iterator <CotationBean> iterator4=listCotation.iterator();
			 String valeur="";
			 boolean dejaVisite=false;
			 
			 if (validationtente)
			 {
				 cotation.setStyle("background:red;");
				 //cotation.setWidth("50%");
			 }
			 
			 if(listeComboMAJ!=null)
				 dejaVisite=true;
				 
			 while(iterator4.hasNext())
			 {
				 CotationBean cotationBean=(CotationBean)iterator4.next();
				 
				 
				 valeur= ficheEvaluation.getId_repertoire_competence()+"#"+employerAEvaluerBean.getId_employe()+"#"+employerAEvaluerBean.getId_planning_evaluation();
				 
				 if (dejaVisite==true)
				 {
					 
					 cotation=listeComboMAJ.get(valeur);
					 //System.out.println("dans deja visite");
				 }
				 else
				 {
					 //System.out.println("pas encore visite");
					 cotation.appendItem(cotationBean.getValeur_cotation()+"");
					 cotation.setContext(valeur);
					
					 cotation.setName("-1");
					 
				 }
				 if(cotation!=null)
				 {
					 cotation.setReadonly(true);
					 cotation.setParent(cellulecotation);
				 }
				 
				 cellulecotation.setParent(listItem);
				 
				 
			 }

			 if(cotation!=null)
			 {
				
				
				 cotation.addForward("onSelect", comp1, "onSelectEvaluation");
				 listeCombo.put(valeur,cotation );
			 }

			 liste.add(listItem);
				 
		 }
		 mapFamilleCombo.put(selectedFamille, listeCombo);
		 mapItemsFamille.put(cles, liste);
		 currentListItem=liste;
	 		 
	 }
	 public void onClick$help1()
	 {
		 //Affichage de la description du poste de travail selectionné
		 
		 String code=mapintitule_codeposte.get(selectednomposteTravail);
		 
		 String descriptionPoste=mapcode_description_poste.get(code);
		 if(descriptionPoste!=null)
		 {
			 //Html html=new Html();
			 //htmlhelp1.setContent(content)
			 String content=descriptionPoste;
			 //htmlhelp1.setStyle("background-color: #1eadff");
			 htmlhelp1.setContent(content);
			 htmlhelp1.setParent(help1Pop);
			 help1Pop.open(help1);
		 }
			

	 }
	 
	 public void onClick$help2()
	 {
		 String message=CreationMessageHelp2(listCotation);
		 
		 //htmlhelp1.setStyle("background-color: #1eadff");
		 htmlhelp1.setContent(message);
		 htmlhelp1.setParent(help1Pop);
		 help1Pop.open(help2);
	 }
	 
	 
	 public void onClick$help1V()
	 {
		 //Affichage de la description du poste de travail selectionné
		 
		 String code=mapintitule_codeposte.get(selectednomposteTravailV);
		 
		 String descriptionPoste=mapcode_description_poste.get(code);
		 if(descriptionPoste!=null)
		 {
			 //Html html=new Html();
			 //htmlhelp1.setContent(content)
			 String content=descriptionPoste;
			 //htmlhelp1.setStyle("background-color: #1eadff");
			 htmlhelp1.setContent(content);
			 htmlhelp1.setParent(help1Pop);
			 help1Pop.open(help1);
		 }
			

	 }
	 
	 public void onClick$help2V()
	 {
		 String message=CreationMessageHelp2(listCotation);
		 
		 //htmlhelp1.setStyle("background-color: #1eadff");
		 htmlhelp1.setContent(message);
		 htmlhelp1.setParent(help1Pop);
		 help1Pop.open(help2);
	 }
	 
	 public void onClick$help3()
	 {
		 String message=CreationMessageHelp2(listCotation);
		 
		 //htmlhelp1.setStyle("background-color: #1eadff");
		 htmlhelp1.setContent(message);
		 htmlhelp1.setParent(help1Pop);
		 help1Pop.open(help2);
	 }
	 public String CreationMessageHelp2(ArrayList <CotationBean> listeCotation)
	 {
		 String message="";
		 Iterator <CotationBean> iterator=listeCotation.iterator();
		 while(iterator.hasNext())
		 {
			 CotationBean cotation=(CotationBean)iterator.next();
			 message=message+ "--> "+cotation.getValeur_cotation()+" : "+ cotation.getLabel_cotation() +", "+ cotation.getDefinition_cotation() +"<br>";
		 }
		 return message;
	 }
	 
	 public void onClick$valider()
	 {
		 
		
		 	
		 //verifier que toutes les cotations ont été remplies 
		 //sinon afficher un message comme quoi la validation n'a pas été prise en compte car
		 // toutes les comptétences n'ont pas été évaluées
		 //verifier que toutes les familles ont été selectionnées
		// ArrayList <String> listFamille=employerAEvaluerBean1.getFamille();
		 validationtente=true;
		 if(autorise)
		 {
			 autorise=false;
			 valider.setVisible(false);
			 valider.setDisabled(true);
		 
			 Set <String> famillesRemplies=mapFamilleCombo.keySet();
			 if(listFamillePoste.size()==famillesRemplies.size())
			 {
				 // famille par famille que tous les combos sont remplies
				 Iterator <String>itfamille=famillesRemplies.iterator();
				 boolean continuer=true;
				 while(itfamille.hasNext())
				 {
					 String clles=itfamille.next();
					 HashMap<String , Combobox>listeComb=mapFamilleCombo.get(clles);
					 Set <String >listclesCombo=listeComb.keySet();
					 Iterator<String> iterator =listclesCombo.iterator();
				 
					 while (iterator.hasNext()/*&& continuer*/)
					 {
						 String cles=iterator.next();
						 Combobox combo=listeComb.get(cles);
						 //combo.setWidth("50%");
						 if(combo.getName().equals("-1"))
						 {

							 continuer=false;							 
							 combo.setStyle("background:red;");
							 //combo.setWidth("50%");

						 }
						 //combo.setWidth("50%");
					 }
				 }
			 
			 if(continuer==false)
			 {
				 valider.setVisible(true);
				 valider.setDisabled(false);
				 autorise=true;
				 try 
				 {
					 
					Messagebox.show("Vos modifications ne peuvent être validées tant que vous n'avez pas évalué toutes les compétences de l'employé", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
				 } 
				 catch (InterruptedException e) 
				 {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 else
			 {
				 
				//rinitialisation du timer
				 
				 	count.setVisible(false);
				 	start.setDisabled(true);
				 	timer.stop();
				 	
				 //désactiver le bouton valider
				 valider.setDisabled(true);
				 //récupérer les modifications et enregistrer dans la base de donnée
				 //valider la fiche dans la table appropriée
				 
				 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
				 
				 HashMap<String, String> maprepCompComp= ficheEvaluationModel.getmaprepCompetenceCodeCompetence();
				/**********************************/
				 // famille par famille que tous les combos sont remplies
				 famillesRemplies=mapFamilleCombo.keySet();
				 itfamille=famillesRemplies.iterator();
				 String id_planning_evaluation="";
				 String id_employe="";	
				 
				 
				 HashMap<String, HashMap<String, ArrayList<Double>>> mapFamilleCompetence=new HashMap<String, HashMap<String, ArrayList<Double>>>();
				 HashMap <String, Double> FamilleIMI=new HashMap<String, Double>();
				 double statIMI=0;
				 int nbimi=0; 
				 String requeteUpdateFicheEvalution="";
				 while(itfamille.hasNext())
				 {
					 String clles=itfamille.next();
					 HashMap<String , Combobox>listeComb=mapFamilleCombo.get(clles);
					 Set <String >listclesCombo=listeComb.keySet();
 
					 listclesCombo=listeComb.keySet();
					 Iterator<String> iterator =listclesCombo.iterator();
					 //continuer=true;

					 
					 double statINIFamille=0;
					 int nbvaleur=0;
					 
					 while (iterator.hasNext())
					 {
						 String cles=(String)iterator.next();
						 Combobox combo=listeComb.get(cles);
						 String valeurs=combo.getContext();
						 String[] val=valeurs.split("#");
						 String id_repertoire_competence=val[0];
						 id_employe=val[1];
						 id_planning_evaluation=val[2];
						 String id_cotation=combo.getName();						 
						 
						 int valeurCotation=getValeurCotation(id_cotation);
						 statINIFamille=statINIFamille+valeurCotation;
						 
						 nbvaleur++;
						 nbimi++;
						 statIMI=statIMI+valeurCotation;
						 
						 //construction de la requete de la mise à jour de l afiche d'evaluation
						 requeteUpdateFicheEvalution=ficheEvaluationModel.updateFicheEvalutionConstructionRequete(id_repertoire_competence,id_employe,id_planning_evaluation,id_cotation,requeteUpdateFicheEvalution);
						 
						 
						 //remplissage des informations moyenne competence
						 //System.out.println("famille clles ..............." +clles);
						 String code_competence=maprepCompComp.get(id_repertoire_competence);
						 if(mapFamilleCompetence.containsKey(clles))
						 {
							 
							 
							 if(mapFamilleCompetence.get(clles).containsKey(code_competence))
							 {
								 ArrayList<Double> liste=mapFamilleCompetence.get(clles).get(code_competence);
								 liste.add(new Double(valeurCotation));
								 mapFamilleCompetence.get(clles).put(code_competence, liste);
							 }
							 else
							 {
								 ArrayList<Double> liste=new ArrayList<Double>(); 
								 liste.add(new Double(valeurCotation));
								 mapFamilleCompetence.get(clles).put(code_competence, liste);
							 }
						 }
						 else
						 {
							 
							 ArrayList<Double> liste=new ArrayList<Double>(); 
							 liste.add(new Double(valeurCotation));
							 HashMap<String, ArrayList<Double>> map=new HashMap<String, ArrayList<Double>>();
							 map.put(code_competence, liste);
							 mapFamilleCompetence.put(clles, map);
						 }
					 }
					 
					 String clesIMI =id_planning_evaluation+"#"+id_employe+"#"+clles;
					 statINIFamille=statINIFamille/nbvaleur;
					 FamilleIMI.put(clesIMI, statINIFamille);
				 
					 //validation de la fiche
					 
				 }
				 
				 //mise à jour de la fiche d'evaluation (exécution de la requete)
				 ficheEvaluationModel.updateMultiQuery(requeteUpdateFicheEvalution);
				 
				 
				 statIMI=statIMI/nbimi;
				 
				 //validation de la fiche d'evaluation
				 ficheEvaluationModel.validerFicheEvaluation(id_planning_evaluation, id_employe);
				
				 
				//a la fin de l'evaluation de toute la famille enregistrement de l'IMI par famille et de l'IMI total dans la table
				 //cette fonction calcul l'IMi en utilisant les notations des apptitudes observales
				// enregistrerIMIStat(FamilleIMI,statIMI);
				 
				 ////cette fonction calcul l'IMi en utilisant les calculs de smoyennes pâr competence
				 enregistrerIMIMoyenneFamilleutilisantCompetence(mapFamilleCompetence,id_planning_evaluation, id_employe);
				 
				 //enregistrement dans la base les stats IMI par competence
				 
				 enregistrerIMIStatParCompetence(mapFamilleCompetence,id_planning_evaluation, id_employe);
				 
				 //mis eà jour calcul imi en prenant en compte les calculs des moyennes par competence
				 
				 //vider le contenu du tableau 
				 //effacer le nom de l'evalué de la combo de cet onglet et le mettre dans l'onglet des personnes déja évaluées
				 
				 rafraichirAffichage();
			 }
			 
		 }
		 else
		 {
			 valider.setDisabled(false);
			 autorise=true;
			 
			 Iterator <String>itfamille=famillesRemplies.iterator();
			 
			 while(itfamille.hasNext())
			 {
				 String clles=itfamille.next();
				 HashMap<String , Combobox>listeComb=mapFamilleCombo.get(clles);
				 Set <String >listclesCombo=listeComb.keySet();
				 Iterator<String> iterator =listclesCombo.iterator();
				 
				 while (iterator.hasNext()/*&& continuer*/)
				 {
					 String cles=iterator.next();
					 Combobox combo=listeComb.get(cles);
					 //combo.setWidth("50%");
					 if(combo.getName().equals("-1"))
					 {

						 
						 combo.setStyle("background:red;");
						 //combo.setWidth("50%");
						// employelb.renderAll();
						 //System.out.println("couleur");
					 }
					 //combo.setWidth("50px");
				 }
			 }
			 //afficher messagebox
			 try 
			 {

				Messagebox.show("Vos modifications ne peuvent être validées tant que vous n'avez pas évalué toutes les compétences de l'employé", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			 } 
			 catch (InterruptedException e) 
			 {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 
		 valider.setVisible(true);
		 }
		 //employelb.renderAll()
	 }
	 
	 public void onSelectEvaluation(ForwardEvent event)
	 {
		 Combobox combo = (Combobox) event.getOrigin().getTarget();	

		 //System.out.println("taille "+combo.getWidth());
		 //mettre id evaluations
		 String valeurCotation=(String)combo.getSelectedItem().getLabel();
		 //recherche de l'id_cotation
		 Iterator <CotationBean> iterator4=listCotation.iterator();
		 String valeur="";
		 int id=0;
		 boolean trouver=false;
		 while((iterator4.hasNext()&& !trouver))
		 {
			 CotationBean cotationBean=iterator4.next();
			 valeur=cotationBean.getValeur_cotation()+"";
			 if(valeur.equals(valeurCotation))
			 {
				 trouver=true;
				 id=cotationBean.getId_cotation();
			 }
		 }
		
		 combo.setName(id+"");
		 if( validationtente)
		 {
			 combo.setStyle("background:##ECEAE4;");
			 //combo.setWidth("50%");
		 }
		// employelb.renderAll();
	 }
	 
	 public void onSelect$FamilleM()
	 {
		//récupération de la famille selectionnée
		 selectedFamilleM=(String)FamilleM.getSelectedItem().getLabel();
		 
		 //lors de la selection d'une famille, il faut :
		 //1. vider le contenu de la table
		 //2. remplir la table avec le cntenu de la nouvelle cles code_poste, famille
		 
		 
		//1. vider le contenu de la table
		 if (currentListItemM!=null)
		 {
			 Iterator<Listitem> iterator=currentListItemM.iterator();
			 while(iterator.hasNext())
			 {
				 Listitem item=iterator.next();
				 item.detach();
			 }
		 }
		 
		 
		 /******************************************************************/


			ArrayList<FicheEvaluationBean> listficheEvaluationBean=mapfamilleFicheEvaluationM.get(selectedFamilleM);
			ArrayList<Listitem> liste=new ArrayList<Listitem>();
			
			/******************************/
			Iterator <FicheEvaluationBean> iteratorFiche=listficheEvaluationBean.iterator();
			while (iteratorFiche.hasNext())
			{
				FicheEvaluationBean ficheEvaluationBean=iteratorFiche.next();
			
				//affichage des donnnées dans le tableau
				Listitem listItem=new Listitem();
				listItem.setParent(employelbM);
			 	 
				 
				//cellule competence
				Listcell cellulecompetence=new Listcell();
				cellulecompetence.setLabel(ficheEvaluationBean.getLibelle_competence());
				cellulecompetence.setTooltiptext(ficheEvaluationBean.getDefinition_competence());
				cellulecompetence.setParent(listItem);
				 
				//cellule aptitude observable
				Listcell celluleaptitude=new Listcell();
				celluleaptitude.setLabel(ficheEvaluationBean.getAptitude_observable());
				celluleaptitude.setParent(listItem);
				 
				 
				//cellule niveau de maitrise
				Listcell cellulecotation=new Listcell();
				Iterator<CotationBean> itcotationBean =listCotation.iterator();
				boolean conti=true;
				int valCotation=0;
				while((itcotationBean.hasNext() && conti))
				{
					CotationBean cotationbean=itcotationBean.next();
					int valeur=cotationbean.getId_cotation();
					
					valCotation=cotationbean.getValeur_cotation();
					if(valeur==ficheEvaluationBean.getNiveau_maitrise())

					{
						conti=false;
						valCotation=ficheEvaluationBean.getNiveau_maitrise();
					}
				}
						
				
				cellulecotation.setLabel(valCotation+"");
			 
				cellulecotation.setParent(listItem);
				
				liste.add(listItem);
			}
			
			currentListItemM=liste;
		 
		 /********************************************************************/
	 }
	 
	 
	 /**
		 * evenement a gerer lors de la selection d'un poste de travail dans l'onglet des fiche valide
		 */
	 public void onSelect$poste_travailV()
	 {
		 

		 
		 //vider le contenu de la grille associée à l'ancien employé selectionné
		 if (currentListItemV!=null)
		 {
			 Iterator<Listitem> iterator=currentListItemV.iterator();
			 while(iterator.hasNext())
			 {
				 Listitem item=iterator.next();
				 item.detach();
			 }
			 selectedEmployeV="";
			 nomEmployeV.setText(selectedEmployeV);
			 selectednomposteTravailV="";
			 posteTravailV.setText(selectednomposteTravailV);
		 }
		 //si un employé a déja été selectionner alors vider le contnu de la combobox
		 if(FamilleV.getItemCount()>0)
		 {
			int nb= FamilleV.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{
				
				FamilleV.removeItemAt(i);
			}
		 }
		 //vider le contenu de la combo employe 
		 if(employeV.getItemCount()>0)
		 {
			int nb= employeV.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{
				
				employeV.removeItemAt(i);
			}
		 }
		 employeV.appendItem("sélectionner un employé");
		 
		 //1. mise à jour de la liste des employes avec la selection de l'attribut selectionner un employe
		 //avec toutes les conséquences qui doivent se découler de cette de cette selection
		 
		//remplissage du contenu de la combo associée aux postes de travail
		 selectednomposteTravailV=poste_travailV.getSelectedItem().getLabel();
		 if(!selectednomposteTravailV.equals("Tous poste de travail"))
		 {
			 HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeEvalueBean.getMapclesposte();
			 HashMap<String, EmployesAEvaluerBean> mapEmploye=Mapclesposte.get(selectednomposteTravailV);
			Set <String> listEmploye=mapEmploye.keySet();
			Iterator<String> iterator =listEmploye.iterator();
			while(iterator.hasNext())
			{
				String nomEmploye=iterator.next();
				employeV.appendItem(nomEmploye);
			}
			employeV.setSelectedIndex(0);
			 
			 
		 }
		 else
		 {
			//remplissage de la comboBox avec tous les nom des employes quelque soit leur type de poste
				HashMap<String, EmployesAEvaluerBean> mapclesEmploye=mapEmployeEvalueBean.getMapclesnomEmploye();
				Set <String>listeEmploye=mapclesEmploye.keySet();
				Iterator <String >iterator=listeEmploye.iterator();
				//employeV.appendItem("sélectionner un employé");
				while(iterator.hasNext())
				{
					String nomEmploye=iterator.next();
					employeV.appendItem(nomEmploye);

				}
				//selection du premier item de la combobox employe
				if(employeV.getItemCount()>0)
					employeV.setSelectedIndex(0);
		 }
	 }
	 
	 /**
		 * evenement a gerer lors de la selection d'une famille dans l'onglet employe evalué
		 */
	 public void onSelect$FamilleV()
	 {
		 //récupération de la famille selectionnée
		 selectedFamilleV=(String)FamilleV.getSelectedItem().getLabel();
		 
		 //lors de la selection d'une famille, il faut :
		 //1. vider le contenu de la table
		 //2. remplir la table avec le cntenu de la nouvelle cles code_poste, famille
		 
		 
		//1. vider le contenu de la table
		 if (currentListItemV!=null)
		 {
			 Iterator<Listitem> iterator=currentListItemV.iterator();
			 while(iterator.hasNext())
			 {
				 Listitem item=iterator.next();
				 item.detach();
			 }
		 }
		 
		 //recuperer les infos associé à l'employé selectionné
		 EmployesAEvaluerBean employerAEvaluerBean=mapEmployeEvalueBean.getMapclesnomEmploye().get(selectedEmployeV);
		 
		 
		//recuperation du code_poste associé à l'intitule
		 //String code_poste=mapintitule_codeposte.get(selectednomposteTravailV);
		 
		 //String cles=code_poste+"#"+selectedFamilleV;
		 
		 //afficher le contenu de mapfamilleFicheEvaluationV
		 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
		 
		 mapfamilleFicheEvaluationV=ficheEvaluationModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe());
		 
		 ArrayList<FicheEvaluationBean> listFiche=mapfamilleFicheEvaluationV.get(selectedFamilleV);
		 
		 
		 Iterator<FicheEvaluationBean> iterator2=listFiche.iterator();
		 
		 ArrayList<Listitem> liste=new ArrayList<Listitem>();
		 
		 //HashMap<String, Combobox> listeComboMAJ=mapFamilleCombo.get(selectedFamilleV);
		 //listeComboV=new HashMap<String, Combobox>();
		 while (iterator2.hasNext())
		 {
			 FicheEvaluationBean ficheEvaluation=(FicheEvaluationBean)iterator2.next();
			 String libelleCompetence=ficheEvaluation.getLibelle_competence();
			 String aptitudeObservable=ficheEvaluation.getAptitude_observable();
			 String descriptionCompetence=ficheEvaluation.getDefinition_competence();
			 
			 //affichage des donnnées dans le tableau
			 Listitem listItem=new Listitem();
			 listItem.setParent(employelbV);
			 	 
				 
			 //cellule competence
			 Listcell cellulecompetence=new Listcell();
			 cellulecompetence.setLabel(libelleCompetence);
			 cellulecompetence.setTooltiptext(descriptionCompetence);
			 cellulecompetence.setParent(listItem);
				 
			 //cellule aptitude observable
			 Listcell celluleaptitude=new Listcell();
			 celluleaptitude.setLabel(aptitudeObservable);
			 celluleaptitude.setParent(listItem);
				 
				 
			//cellule niveau de maitrise
				Listcell cellulecotation=new Listcell();
				Iterator<CotationBean> itcotationBean =listCotation.iterator();
				boolean conti=true;
				int valCotation=0;
				while((itcotationBean.hasNext() && conti))
				{
					CotationBean cotationbean=itcotationBean.next();
					int valeur=cotationbean.getId_cotation();
					
					valCotation=cotationbean.getValeur_cotation();
					if(valeur==ficheEvaluation.getNiveau_maitrise())

					{
						conti=false;
						valCotation=ficheEvaluation.getNiveau_maitrise();
					}
				}
						
				
				cellulecotation.setLabel(valCotation+"");
			 
				cellulecotation.setParent(listItem);
				
				liste.add(listItem);
			}
			
			currentListItemV=liste;
	 		 
	 }
	 
		/**
		 * evenement a gerer lors de la selection d'un employé de l'onglet employe evalué
		 */
		 public void onSelect$employeV()
		 {
			 
			
			 //vider le contenu de la grille associée à l'ancien employé selectionné
			 if (currentListItemV!=null)
			 {
				 Iterator<Listitem> iterator=currentListItemV.iterator();
				 while(iterator.hasNext())
				 {
					 Listitem item=iterator.next();
					 item.detach();
				 }
				 selectedEmployeV="";
				 nomEmployeV.setText(selectedEmployeV);
				 selectednomposteTravailV="";
				 posteTravailV.setText(selectednomposteTravailV);
			 }
			 //si un employé a déja été selectionner alors vider le contnu de la combobox
			 if(FamilleV.getItemCount()>0)
			 {
				int nb= FamilleV.getItemCount();
				for (int i=nb-1;i>=0;i--)
				{
					
					FamilleV.removeItemAt(i);
				}
			 }
			
			 mapItemsFamilleV=new 	HashMap<String, ArrayList<Listitem>>();	 
			 employelbV.renderAll();
			 //lors de la selection d'un employé, affichage de la fiche associé à cet employé si elle existe 
			 //si la fiche n'existe pas , il faut la creer
			 
			 //1. affichage des informations relatifs à l'employé

			 selectedEmployeV=employeV.getSelectedItem().getLabel();
			 
			 if(!selectedEmployeV.equals("sélectionner un employé"))
			 {
				 
				 nomEmployeV.setText(selectedEmployeV);
				 EmployesAEvaluerBean employerAEvaluerBean=mapEmployeEvalueBean.getMapclesnomEmploye().get(selectedEmployeV);
				 //employerAEvaluerBean1=employerAEvaluerBean;
				 selectednomposteTravailV=employerAEvaluerBean.getPoste_travail();
				 posteTravailV.setText(selectednomposteTravailV);
				 
				 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
				 mapfamilleFicheEvaluationV=ficheEvaluationModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe());
				 
				 String code_poste=mapintitule_codeposte.get(selectednomposteTravailV);
				 
				 listFamillePosteV=ficheEvaluationModel.getFamilleAssociePoste(code_poste);
				 //ArrayList <String> listFamille=employerAEvaluerBean.getFamille();
				 Iterator<String> iterator=listFamillePosteV.iterator();
			 	while(iterator.hasNext())
			 	{
			 		String famille=iterator.next();
			 		FamilleV.appendItem(famille);
			 	}
			 	if(FamilleV.getItemCount()>0)
			 		FamilleV.setSelectedIndex(0);
			 
			 	selectedFamilleV=listFamillePosteV.get(0);
			 
			 	//afficher toutes les données associées à ce poste de travail
			 
			 	//recuperation du code_poste associé à l'intitule
			 	
			 
			 	//String cles=code_poste+"#"+selectedFamilleV;
			 
			 //System.out.println(cles);



			 mapfamilleFicheEvaluationV=ficheEvaluationModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe());
			 
			 ArrayList<FicheEvaluationBean> listFiche=mapfamilleFicheEvaluationV.get(selectedFamilleV);
			 
				 //System.out.println("cles="+cles);
				 Iterator<FicheEvaluationBean> iterator2=listFiche.iterator();
				 
				 ArrayList<Listitem> liste=new ArrayList<Listitem>();
				 listeComboV=new HashMap<String, Combobox>();
				 while (iterator2.hasNext())
				 {
					 FicheEvaluationBean ficheEvaluation=(FicheEvaluationBean)iterator2.next();
					 String libelleCompetence=ficheEvaluation.getLibelle_competence();
					 String aptitudeObservable=ficheEvaluation.getAptitude_observable();
					 String descriptionCompetence=ficheEvaluation.getDefinition_competence();
					 
					 //affichage des donnnées dans le tableau
					 Listitem listItem=new Listitem();
					 listItem.setParent(employelbV);
					 	 
						 
					 //cellule competence
					 Listcell cellulecompetence=new Listcell();
					 cellulecompetence.setLabel(libelleCompetence);
					 cellulecompetence.setTooltiptext(descriptionCompetence);
					 cellulecompetence.setParent(listItem);
						 
					 //cellule aptitude observable
					 Listcell celluleaptitude=new Listcell();
					 celluleaptitude.setLabel(aptitudeObservable);
					 celluleaptitude.setParent(listItem);
						 
						 
					//cellule niveau de maitrise
						Listcell cellulecotation=new Listcell();
						Iterator<CotationBean> itcotationBean =listCotation.iterator();
						boolean conti=true;
						int valCotation=0;
						while((itcotationBean.hasNext() && conti))
						{
							CotationBean cotationbean=itcotationBean.next();
							int valeur=cotationbean.getId_cotation();
							
							valCotation=cotationbean.getValeur_cotation();
							if(valeur==ficheEvaluation.getNiveau_maitrise())

							{
								conti=false;
								valCotation=ficheEvaluation.getNiveau_maitrise();
							}
						}
								
						
						cellulecotation.setLabel(valCotation+"");
					 
						cellulecotation.setParent(listItem);
						
						liste.add(listItem);
					}
					
					currentListItemV=liste;
			 }		 
		 }
		 
		 public void rafraichirAffichage()
		 {
//				super.doAfterCompose(comp);
//				comp1=comp;
//				comp.setVariable(comp.getId() + "Ctrl", this, true);
//				
			 
			 	valider.setDisabled(true);
				//recupération du profil de l'utilisateur
				CompteBean compteUtilisateur=ApplicationFacade.getInstance().getCompteUtilisateur();
				
				//récupération de l'id_employé associé à l'id_compte
				FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
//				int id_employe=ficheEvaluationModel.getIdEmploye(compteUtilisateur.getId_compte());
				compteUtilisateur.setId_employe(id_employe);
//				
//				
//				//récuperation de la description des poste pour le bouton help
//				
//				mapcode_description_poste=ficheEvaluationModel.getPosteTravailDescription();
//				
//				//récupération de la cotation
//				listCotation=ficheEvaluationModel.getCotations();
				
				
				//récupération de l'information sur la validité de la fiche de l'employé connecté
//				boolean ficheValide=ficheEvaluationModel.getValiditeFiche(id_employe);
				
				//récupération des informations associées à une fiche d'evaluation à remplir;
				
				 mapPosteTravailFiche=ficheEvaluationModel.getInfosFicheEvaluationparPoste();
				 CompetencePosteTravailModel compt=new CompetencePosteTravailModel();
				 mapintitule_codeposte=compt.getlistepostesCode_postes();
				 mapcode_intituleposte=compt.getlisteCode_postes_intituleposte();
				//onglet Ma Fiche d'évaluation
				
				//evaluations.setStyle("overflow:auto");
//				posteTravail.setDisabled(true);
//				nomEmploye.setDisabled(true);
				
				//remplissage de la combobox famille
				

				//si c'est un évaluateur alors on affiche la liste des fiches associés aux employés à évaluer
				if(compteUtilisateur.getId_profile()==3)
				{
					
					//remplissage du contenu de la combo associée aux postes de travail
					 mapEmployeAEvaluerBean=ficheEvaluationModel.getListEmployesAEvaluer(id_employe);
					 HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeAEvaluerBean.getMapclesposte();
					Set <String>listePoste= Mapclesposte.keySet();
					Iterator <String > iterator=listePoste.iterator();
					//vider l'encien contenu de poste de travail 
					
					
					 if(poste_travail.getItemCount()>0)
					 {
						int nb= poste_travail.getItemCount();
						for (int i=nb-1;i>=0;i--)
						{
							
							poste_travail.removeItemAt(i);
						}
					 }
					
					poste_travail.appendItem("Tous poste de travail");
					while(iterator.hasNext())
					{
						String nomPoste=iterator.next();
						poste_travail.appendItem(nomPoste);
					}
					//selection du premier item (tous poste de travail)
					poste_travail.setSelectedIndex(0);
					
					//remplissage de la comboBox avec tous les nom des employes quelque soit leur type de poste
					HashMap<String, EmployesAEvaluerBean> mapclesEmploye=mapEmployeAEvaluerBean.getMapclesnomEmploye();
					Set <String>listeEmploye=mapclesEmploye.keySet();
					iterator=listeEmploye.iterator();
					
					//vider l'encien contenu de  la combo employe
					
					 if(employe.getItemCount()>0)
					 {
						int nb= employe.getItemCount();
						for (int i=nb-1;i>=0;i--)
						{
							
							employe.removeItemAt(i);
						}
					 }
					 
					employe.appendItem("sélectionner un employé");
					while(iterator.hasNext())
					{
						String nomEmploye=iterator.next();
						employe.appendItem(nomEmploye);
						
					}
					//selection du premier item de la combobox employe
					if(employe.getItemCount()>0)
						employe.setSelectedIndex(0);
					
					
					 if (currentListItem!=null)
					 {
						 Iterator<Listitem> iterator1=currentListItem.iterator();
						 while(iterator1.hasNext())
						 {
							 Listitem item=iterator1.next();
							 item.detach();
						 }
						 selectedEmploye="";
						 nomEmploye.setText(selectedEmploye);
						 selectednomposteTravail="";
						 posteTravail.setText(selectednomposteTravail);
					 }
					
				}
				else //ne pas afficher cet onglet
				{
					
					/**
					 * TODO ne rendre cette page invisible que sur certaines conditions
					 */
					//AEvaluer.setVisible(false);
					AEvaluer.detach();
					evaluations.detach();
					
					

				}
				
				//si c'est un administrateur, il peut voir toutes les fiches d'evaluations ou evaluateur
				if((compteUtilisateur.getId_profile()==3)||(compteUtilisateur.getId_profile()==2))
				{
					
					
					mapPosteTravailFicheV=ficheEvaluationModel.getInfosFicheEvaluationparPoste();
					 
					 
					 
					//remplissage du contenu de la combo associée aux postes de travail
					//si c'est un evaluateur lancer cette methode
					if (compteUtilisateur.getId_profile()==3)
						mapEmployeEvalueBean=ficheEvaluationModel.getListEmployesvalue(id_employe);
					 
					//sinon (administrateur ) lancer une autre méthode qui récupère tous ceux qui ont été évaluées
					if (compteUtilisateur.getId_profile()==2)
						mapEmployeEvalueBean=ficheEvaluationModel.getListTousEmployesvalue();
					 HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeEvalueBean.getMapclesposte();
					Set <String>listePoste= Mapclesposte.keySet();
					Iterator <String > iterator=listePoste.iterator();
					
					
					//vider l'encien contenu de  la combo poste_travail
					
					 if(poste_travailV.getItemCount()>0)
					 {
						int nb= poste_travailV.getItemCount();
						for (int i=nb-1;i>=0;i--)
						{
							
							poste_travailV.removeItemAt(i);
						}
					 }
					poste_travailV.appendItem("Tous poste de travail");
					while(iterator.hasNext())
					{
						String nomPoste=iterator.next();
						poste_travailV.appendItem(nomPoste);
					}
					//selection du premier item (tous poste de travail)
					poste_travailV.setSelectedIndex(0);
					
					//remplissage de la comboBox avec tous les nom des employes quelque soit leur type de poste
					HashMap<String, EmployesAEvaluerBean> mapclesEmploye=mapEmployeEvalueBean.getMapclesnomEmploye();
					Set <String>listeEmploye=mapclesEmploye.keySet();
					iterator=listeEmploye.iterator();
					
					
					//vider l'encien contenu de  la combo poste_travail
					
					 if(employeV.getItemCount()>0)
					 {
						int nb= employeV.getItemCount();
						for (int i=nb-1;i>=0;i--)
						{
							
							employeV.removeItemAt(i);
						}
					 }
					 
					employeV.appendItem("sélectionner un employé");
					while(iterator.hasNext())
					{
						String nomEmploye=iterator.next();
						employeV.appendItem(nomEmploye);

					}
					//selection du premier item de la combobox employe
					if(employeV.getItemCount()>0)
						employeV.setSelectedIndex(0);
					
					
					
				}
				else
				{
					//rendre l'onglet invisible 
					FValide.detach();
					fichevalide.detach();
				}
				tb.setSelectedIndex(0);
				 valider.setDisabled(false);
		 }
		 public int getValeurCotation(String idCotation)
		 {
			 
			 //recherche de l'id_cotation
			 Iterator <CotationBean> iterator4=listCotation.iterator();
			 String valeur="";
			 int id=0;
			 boolean trouver=false;
			 while((iterator4.hasNext()&& !trouver))
			 {
				 CotationBean cotationBean=iterator4.next();
				 valeur=cotationBean.getId_cotation()+"";
				 if(valeur.equals(idCotation))
				 {
					 trouver=true;
					 id=cotationBean.getValeur_cotation();
				 }
			 }
			 
			 return id;
		 }
		 
		 public void enregistrerIMIStat(HashMap <String, Double> FamilleIMI,double statIMI)
		 {
			
			 Set <String >listclesIMIStat=FamilleIMI.keySet();
			 Iterator<String> iterator =listclesIMIStat.iterator();
			 String requete="";
			 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
			 while(iterator.hasNext())
			 {
				
				 String cles=iterator.next();
				 
				 String val[]=cles.split("#");
				 String id_planning_evaluation=val[0];
				 String id_employ=val[1];
				 String nomFamille=val[2];
				 
				 double INiFamille=FamilleIMI.get(cles);
				 
				 
				 String valeur=ficheEvaluationModel.getIdCompagne_Codefamille(id_planning_evaluation,nomFamille);
				 
				 String v[]=valeur.split("#");
				 String id_compagne=v[0];
				 String code_famille=v[1];
				 //contruction de la requete
				 requete=ficheEvaluationModel.enregistrerIMiStatConstructionRequete(id_compagne,id_employ,INiFamille,code_famille,statIMI, requete);
				 
			 }
			 
			 ficheEvaluationModel.updateMultiQuery(requete);
		 }
		 public void enregistrerIMIStatParCompetence(HashMap<String, HashMap<String , ArrayList<Double>>> mapFamilleCompetence,String id_planning_evaluation, String id_employe)
		 {
			 
			 Set <String >listFamille= mapFamilleCompetence.keySet();
			 
			 
			 Iterator<String> iteratorFamille=listFamille.iterator();
			 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
			 String requete="";
			 while (iteratorFamille.hasNext())
			 {
				 String famille=iteratorFamille.next();
				 
				 String valeur=ficheEvaluationModel.getIdCompagne_Codefamille(id_planning_evaluation,famille);
				 
				 String v[]=valeur.split("#");
				 String id_compagne=v[0];
				 String code_famille=v[1];
				 
				 HashMap<String , ArrayList<Double>> mapcodeCompetence=mapFamilleCompetence.get(famille);
				 Set <String> listCompetence=mapcodeCompetence.keySet();
				 Iterator<String> iteratorCodeCompetence=listCompetence.iterator();
				 while(iteratorCodeCompetence.hasNext())
				 {
					 String code_competence=iteratorCodeCompetence.next();
					 
					 //calcul d ela moyenne par competence
					 ArrayList<Double> listeCompetence=mapcodeCompetence.get(code_competence);
					 
					 int nbCompetence=listeCompetence.size();
					 Double moyenne=new Double(0);
					 for(int i=0;i<nbCompetence;i++)
					 {
						 moyenne=moyenne+listeCompetence.get(i);
					 }
					 moyenne=moyenne/nbCompetence;
					 requete=requete+"  INSERT INTO IMI_COMPETENCE_STAT  (id_compagne,id_employe,code_famille,code_competence,moy_competence) VALUES (#id_compagne,#id_employe,#code_famille,#code_competence,#moy_competence) ; ";
					 
					 requete = requete.replaceAll("#id_compagne", id_compagne);
					 requete = requete.replaceAll("#id_employe", id_employe);
					 requete = requete.replaceAll("#code_famille", "'"+code_famille+ "'");
					 requete = requete.replaceAll("#code_competence", "'"+code_competence+ "'");
					 requete = requete.replaceAll("#moy_competence", moyenne+"");
				 }
				 
				 
			 }
			 
			 
			 ficheEvaluationModel.insertImiCompetenceStat(requete);
		 }
		
		 
		 
		 public void enregistrerIMIMoyenneFamilleutilisantCompetence(HashMap<String, HashMap<String , ArrayList<Double>>> mapFamilleCompetence,String id_planning_evaluation, String id_employe)
		 {
			 
			 Set <String >listFamille= mapFamilleCompetence.keySet();
			 
			 
			 Iterator<String> iteratorFamille=listFamille.iterator();
			 int nbFamille=listFamille.size();
			 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
			 String requete="";
			 Double moyIMI=new Double(0);
			 while (iteratorFamille.hasNext())
			 {
				 String famille=iteratorFamille.next();
				 
				 String valeur=ficheEvaluationModel.getIdCompagne_Codefamille(id_planning_evaluation,famille);
				 
				 String v[]=valeur.split("#");
				 String id_compagne=v[0];
				 String code_famille=v[1];
				 
				 HashMap<String , ArrayList<Double>> mapcodeCompetence=mapFamilleCompetence.get(famille);
				 Set <String> listCompetence=mapcodeCompetence.keySet();
				 Iterator<String> iteratorCodeCompetence=listCompetence.iterator();
				 ArrayList<Double > valeurFamilleMoyenne=new ArrayList<Double>();
				 while(iteratorCodeCompetence.hasNext())
				 {
					 String code_competence=iteratorCodeCompetence.next();
					 
					 //calcul d ela moyenne par competence
					 ArrayList<Double> listeCompetence=mapcodeCompetence.get(code_competence);
					 
					 int nbCompetence=listeCompetence.size();
					 Double moyenne=new Double(0);
					 for(int i=0;i<nbCompetence;i++)
					 {
						 moyenne=moyenne+listeCompetence.get(i);
					 }
					 moyenne=moyenne/nbCompetence;
					 valeurFamilleMoyenne.add(moyenne);

				 }
				 //calcul de la moyenne par famille
				 Iterator<Double> iterator=valeurFamilleMoyenne.iterator();
				 Double moyenne=new Double(0);
				 int nbVal=valeurFamilleMoyenne.size();
				 while(iterator.hasNext())
				 {
					 Double var=iterator.next();
					 moyenne=moyenne+var;
				 }
				 moyenne=moyenne/nbVal;
				 
				 moyIMI=moyIMI+moyenne;
				 requete=requete+ " INSERT INTO imi_stats (id_compagne,id_employe,moy_par_famille,code_famille, imi) VALUES (#id_compagne,#id_employe,#moy_par_famille,#code_famille, #imi) ; ";
				 requete = requete.replaceAll("#id_compagne", id_compagne);
				 requete = requete.replaceAll("#id_employe", id_employe);
				 requete = requete.replaceAll("#moy_par_famille", moyenne+"");
				 requete = requete.replaceAll("#code_famille", "'"+code_famille+"'");
				 
				 
			 }
			 
			 //mise a jour de la requete avec l'IMI;
			 Double calculIMI=moyIMI/nbFamille;
			 
			 requete = requete.replaceAll("#imi", calculIMI+"");
			 
			 ficheEvaluationModel.insertImiCompetenceStat(requete);
			 
		 }
}
