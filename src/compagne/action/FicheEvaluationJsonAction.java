package compagne.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;

import Statistique.model.StatCotationEmployeModel;
import administration.bean.CompteBean;
import administration.bean.CotationBean;
import administration.model.CompetencePosteTravailModel;
import common.ApplicationSession;
import compagne.bean.CotationEvaluationBean;
import compagne.bean.EmployesAEvaluerBean;
import compagne.bean.FicheEvaluationBean;
import compagne.bean.FicheEvaluationJsonBean;
import compagne.bean.MapEmployesAEvaluerBean;
import compagne.bean.PlanningListEvaluateurBean;
import compagne.model.FicheEvaluationJsonModel;
import compagne.model.FicheEvaluationModel;

public class FicheEvaluationJsonAction extends GenericForwardComposer{

	//logger 
	private static final Logger logger = Logger.getLogger(FicheEvaluationJsonAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	//variales pour la gestion du timer
	//	Timer timer;
	//	
	//	Label count;
	//	
	//	int minutes=60;//ApplicationFacade.getInstance().getTimerValue();
	boolean first=true;



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
	Button confirmer;
	Listbox employelb;
	Button help1;
	Button help2;
	Button help3;
	Popup help1Pop;
	Html htmlhelp1;
	Component comp1;
	private Include iframe;

	//objets graphiques de l'onglet des omployes deja evalués
	Textbox posteTravailV;
	Textbox nomEmployeV;
	Button help1V;
	Button help2V;
	Combobox FamilleV;	
	Combobox employeV; 
	Combobox poste_travailV;
	Combobox compagneV;
	Listbox employelbV;
	Combobox directionV;
	//objets graphique de l'onglet la fiche d'evaluation


	Textbox structureV;
	Textbox evaluateurV;

	Label evaluateur_lblV;
	Label structure_lblV;

	Textbox nomEmployeM;
	Textbox nomEvaluateurM;
	Textbox posteTravailM;
	Combobox FamilleM;
	Groupbox gb3;
	Listbox employelbM;
	Caption labelM;
	Combobox compagneM;


	//TODO ces deux boutons sont é supprimer
	Button exporterJson;
	Button rattrapageCalculIMI;
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
	HashMap<String, ArrayList<FicheEvaluationBean>> mapFicheEvaluee;

	ArrayList <String> listFamillePoste;
	ArrayList <String> listFamillePosteV;

	int id_employe;

	String id_planning_eval;
	String id_evalue;	

	HashMap <String, String> mapcodeCompetenceLibelleCompetence;
	HashMap <String, String> mapidRepCompetence_ApptitudeObservable;

	Map map_compagne=null;
	Map map_compagneValidee=null;

	Integer compagne_id=-2;
	int type_appel=-1; //0  si l'appel de la methode se fait appartr de  doAfterCompose 1 si l'appel se fait appartir de la methode onSelect$compagneV





	//EmployesAEvaluerBean employerAEvaluerBean1;


	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.GenericForwardComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp1=comp;
		comp.setVariable(comp.getId() + "Ctrl", this, true);

		valider.setDisabled(true);
		confirmer.setDisabled(true);


		FicheEvaluationJsonModel ficheJson=new FicheEvaluationJsonModel();
		map_compagne=ficheJson.getListAllCompagne();
		Set set = (map_compagne).entrySet(); 
		Iterator i = set.iterator();
		// Display elements
		compagneV.appendItem("Toutes campagnes");
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			compagneV.appendItem((String) me.getKey());

		}

		if(compagneV.getItemCount()>0){
			compagneV.setSelectedIndex(0);

		}

		/**
		 *  recuperer uniquement la liste des compagnes validés
		 * Cette liste sera utilisée pour la combox compagne MaFiCheEvaluation
		 * L'évaluateur et l'évalué doivent avoir accés  é leur fiche uniquement une fois
		 * la compagne validée
		 *
		 */

		map_compagneValidee=ficheJson.getListCompagneValidee();
		Set set1 = (map_compagneValidee).entrySet(); 
		Iterator i1 = set1.iterator();
		// Display elements
		while(i1.hasNext()) {
			Map.Entry me = (Map.Entry)i1.next();
			compagneM.appendItem((String) me.getKey());
		}

		if(compagneM.getItemCount()>0){

			compagneM.setSelectedIndex(0);

		}



		compagne_id=ficheJson.getCompagneEnCours();
		//compagne_id= Integer.parseInt((String) map_compagne.get((String)compagneV.getSelectedItem().getLabel()));
		loadFicheEval(0,compagne_id);




	}

	private void loadFicheEval(int type_appel,int compagne_id ) throws Exception,
	ParseException, SQLException {





		//recupération du profil de l'utilisateur
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		CompteBean compteUtilisateur=applicationSession.getCompteUtilisateur();
		//TODO ligne a supprimer ApplicationFacade
		//CompteBean compteUtilisateur=ApplicationFacade.getInstance().getCompteUtilisateur();

		//récupération de l'id_employé associé é l'id_compte
		FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
		FicheEvaluationJsonModel ficheEvaluationJsonModel=new FicheEvaluationJsonModel();
		id_employe=ficheEvaluationModel.getIdEmploye(compteUtilisateur.getId_compte());
		compteUtilisateur.setId_employe(id_employe);

		mapcodeCompetenceLibelleCompetence=ficheEvaluationJsonModel.getListCompetence(compagne_id);
		mapidRepCompetence_ApptitudeObservable=ficheEvaluationJsonModel.getListapptitudeObservable(compagne_id);
		//récuperation de la description des poste pour le bouton help

		mapcode_description_poste=ficheEvaluationModel.getPosteTravailDescription();

		//récupération de la cotation
		listCotation=ficheEvaluationModel.getCotations();


		//récupération de l'information sur la validité de la fiche de l'employé connecté
		boolean ficheValide=ficheEvaluationModel.getValiditeFiche(id_employe);

		//récupération des informations associées é une fiche d'evaluation é remplir;

		mapPosteTravailFiche=ficheEvaluationModel.getInfosFicheEvaluationparPoste(compagne_id);
		//CompetencePosteTravailModel compt=new CompetencePosteTravailModel();
		mapintitule_codeposte=ficheEvaluationJsonModel.getlistepostesCode_postes(compagne_id);
		mapcode_intituleposte=ficheEvaluationJsonModel.getlisteCode_postes_intituleposte(compagne_id);
		//onglet Ma Fiche d'évaluation

		evaluations.setStyle("overflow:auto");
		//		posteTravail.setDisabled(true);
		//		nomEmploye.setDisabled(true);

		//remplissage de la combobox famille


		if((ficheValide) &&  compagneM.getItemCount()>0)
		{

			//construction et affichage du contenu de la page d'evaluation de la personne connecté

			//initialisation du nom de l'evaluateur é blanc
			String nomEvaluateur= ficheEvaluationModel.getMonEvaluateur(id_employe, String.valueOf(compagne_id));
			nomEvaluateurM.setText(nomEvaluateur);
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
				//affichage d'un message disant que la fiche ne peut étre visible
				//gb3.detach();
				html=new Html();
				//String content="Vous n'avez pas  été convoqué é aucune campagne d'évaluation ";
				String content=" ";
				html.setStyle("color:red;margin-left:15px");
				html.setContent(content);
				html.setParent(maFiche);
			}
			else
			{

				//récupération de la fiche d'avaluation de l'id_employe

				//content="";

				mapfamilleFicheEvaluationM=ficheEvaluationJsonModel.getMaFicheEvaluaton(id_employe, mapcodeCompetenceLibelleCompetence, mapidRepCompetence_ApptitudeObservable,String.valueOf(compagne_id));


				//affichage de la cotation
				if(FamilleM.getItemCount()>0)
					FamilleM.setSelectedIndex(0);

				selectedFamilleM=listeFamilleM.get(0);
				ArrayList<FicheEvaluationBean> listficheEvaluationBean=mapfamilleFicheEvaluationM.get(selectedFamilleM);
				ArrayList<Listitem> liste=new ArrayList<Listitem>();
				if(listficheEvaluationBean!=null)
				{
					/******************************/
					Iterator <FicheEvaluationBean> iteratorFiche=listficheEvaluationBean.iterator();
					while (iteratorFiche.hasNext())
					{
						FicheEvaluationBean ficheEvaluationBean=iteratorFiche.next();

						//mise é jour du titre


						//labelM.setLabel("Fiche Employé : évaluation du "+  ficheEvaluationBean.getDate_evaluation() +" de la compagne "+ ficheEvaluationBean.getCompagne_type());
						//labelM.setLabel("Fiche Employé : Compagne  "+ ficheEvaluationBean.getCompagne_type() +" du "+ ficheEvaluationBean.getDate_evaluation() );
						labelM.setLabel("Fiche Employé ");

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

						String squotation=valCotation+"";
						if("0".equals(squotation)|| "6".equals(squotation))
							squotation="N/A";
						cellulecotation.setLabel(squotation);

						cellulecotation.setParent(listItem);

						liste.add(listItem);
					}
					/*****************/
					currentListItemM=liste;

				}
			}//TODO faire la recuperation des fiches d'evaluations dans json ici??
		}
		else
		{
			//enlever ce qui existe et afficher la page HTML 
			String content="";
			gb3.detach();

			//affichage d'un message disant que la fiche ne peut étre visible
			html=new Html();
			content="Vous n'avez pas accés é votre fiche d'évaluation car elle n'a pas encore été complétée par l'évaluateur";
			html.setStyle("color:red;margin-left:15px");
			html.setContent(content);
			html.setParent(maFiche);
		}
		if (compteUtilisateur.getId_profile()==1)
		{
			FicheEvaluation.detach();
			maFiche.detach();
		}
		//si c'est un évaluateur alors on affiche la liste des fiches associés aux employés é évaluer
		boolean est_evaluateur=false;
		//if(compteUtilisateur.getId_profile()==3)
		//un administrateur peut étre un évaluateur aors on test l'attribut est_evauateur de la table employe
		est_evaluateur=ficheEvaluationModel.getEstEvauateur(id_employe);
		if(est_evaluateur)
		{

			//remplissage du contenu de la combo associée aux postes de travail
			mapEmployeAEvaluerBean=ficheEvaluationModel.getListEmployesAEvaluer(id_employe,compagne_id);
			HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeAEvaluerBean.getMapclesposte();
			Set <String>listePoste= Mapclesposte.keySet();
			Iterator <String > iterator=listePoste.iterator();
			poste_travail.appendItem("Tous postes de travail");
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
		//
		if((compteUtilisateur.getId_profile()==3)||(compteUtilisateur.getId_profile()==2) ||(compteUtilisateur.getId_profile()==1))
		{

			//remplissage et récupération des compagnes
			//compagneV.appendItem("Toute compagne");
			//créer une mapposte de travailpar compagne
			//TODO implémenter la méthode qui retourne la map compagne PoteTravail
			//mapCompagnePosteTravailV
			//créer une map fiche valide par compagne
			//TODO implémenter la méthode qui retourne la map compagne/fiche
			//mapCompagneFicheV

			//debut Modif NB du 19/02

			/*if (type_appel==0){
				compagne_id= Integer.parseInt((String) map_compagne.get((String)compagneV.getItemAtIndex(0).getLabel()));
			}*/


			mapPosteTravailFicheV=ficheEvaluationModel.getInfosFicheEvaluationparPosteComp(compagne_id);



			//remplissage du contenu de la combo associée aux postes de travail
			//si c'est un evaluateur lancer cette methode
			if (compteUtilisateur.getId_profile()==3)
				mapEmployeEvalueBean=ficheEvaluationModel.getListEmployesvalueComp(id_employe,compagne_id);

			//sinon (administrateur ) lancer un eautre méthode qui récupére tous ceux qui ont été évaluées
			if ((compteUtilisateur.getId_profile()==2)||(compteUtilisateur.getId_profile()==1)){
				//modif point 2 v3.2 05/07/2018
				directionV.setVisible(true);

				mapEmployeEvalueBean=ficheEvaluationModel.getListTousEmployesvalueComp(compagne_id);
			}

			// debut modif point 2 v3.2 05/07/2018
			HashMap<String, HashMap<String,ArrayList<String>>> Mapdirection=mapEmployeEvalueBean.getMapclesdirection();
			Set <String>listedirection= Mapdirection.keySet();
			Iterator <String > iterator_direction=listedirection.iterator();
			directionV.appendItem("Selectionner Direction");
			while(iterator_direction.hasNext())
			{
				String nomdirection=iterator_direction.next();
				directionV.appendItem(nomdirection);
			}
			//selection du premier item (tous direction)
			directionV.setSelectedIndex(0);
			// fin modif point 2 v3.2 05/07/2018


			HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeEvalueBean.getMapclesposte();
			Set <String>listePoste= Mapclesposte.keySet();
			Iterator <String > iterator=listePoste.iterator();

			

		
				poste_travailV.appendItem("Tous postes de travail");
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
		if (type_appel==0){
			tb.setSelectedIndex(0);
			//vider Ma fiche d'évaluation la selection de la fiche se fait uniquement apres selection de la listbox compagneM
			employelbM.getItems().clear();

		}

		//		minutes=applicationSession.getTimerValue();
		//		 timer.addEventListener(Events.ON_TIMER, new EventListener() {
		//				public void onEvent(Event evt) {
		//					
		//					timer.setRepeats(true);
		//					
		//					//afficher le timer chaque minute
		//					if(first)
		//					{
		//						count.setValue(  minutes  + ":00" );
		//						first=false;
		//					}
		//					else
		//					{
		//						if(secondes<=10)
		//						{	
		//							if(secondes==0)
		//							{
		//								secondes=60;
		//								if(minutes<=10)
		//									count.setValue( "0" + --minutes  + ":" + --secondes);
		//								else
		//									count.setValue( --minutes  + ":" + --secondes);
		//							}
		//							else
		//							{
		//								if(minutes<=9)
		//									count.setValue( "0" + minutes  + ":0" + --secondes);
		//								else
		//									count.setValue( minutes  + ":0" + --secondes);
		//							}
		//
		//							
		//						}
		//						else
		//						{
		//							if(minutes<=9)
		//								count.setValue( "0" + minutes  + ":" + --secondes);
		//							else
		//								count.setValue( minutes  + ":" + --secondes);
		//						}
		//					}
		//				 	if (minutes <= 0) {
		//					timer.stop();
		//					return;
		//				 	}
		//				}
		//				});
		//		 count.setVisible(false);
		//		 start.setDisabled(true);
	}

	/**
	 * evenement a gerer lors de la selection d'un employé
	 * @throws ParseException 
	 */
	public void onSelect$employe() throws ParseException
	{
		boolean familleVisite=true;
		//rinitialisation du timer

		//count.setVisible(true);


		//			secondes=0;
		//			minutes=60;
		first=true;
		//count.setValue("60:00");
		//fin reinitialisation du timer	

		validationtente=false;
		//vider le contenu de la grille associée é l'ancien employé selectionné
		autorise=true;

		mapFamilleCombo=new HashMap<String, HashMap<String , Combobox>> (); 
		valider.setDisabled(false);
		confirmer.setDisabled(false);
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
		//lors de la selection d'un employé, affichage d ela fiche associé é cet employé si elle existe 
		//si la fiche n'existe pas , il faut la creer

		//1. affichage des informations relatifs é l'employé

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
			FicheEvaluationJsonModel ficheEvaluationJsonModel=new FicheEvaluationJsonModel();
			//System.out.println(code_poste);
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

			try {
				selectedFamille=listFamillePoste.get(0);
			}catch (IndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace()
				try {
					Messagebox.show("Probléme de configuration: Merci de vérifier association du poste travail vs campagne OU poste travail vs aptitudes Obs !", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


			}

			//afficher toutes les données associées é ce poste de travail

			//recuperation du code_poste associé é l'intitule


			String cles=code_poste+"#"+selectedFamille;

			FicheEvaluationModel fiche_model=new FicheEvaluationModel();
			String statut_fiche=fiche_model.getStatutFicheEval(String.valueOf(employerAEvaluerBean.getId_planning_evaluation()),
					String.valueOf(employerAEvaluerBean.getId_employe()));
			ArrayList<FicheEvaluationBean> listFiche;



			mapFicheEvaluee=ficheEvaluationJsonModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe(), mapcodeCompetenceLibelleCompetence, mapidRepCompetence_ApptitudeObservable,String.valueOf(compagne_id));
			// statut fiche=0 ==>  toutes les données ont ete enregitrées 
			// statut fiche=-1 ==>  au moins une quotation a été enregistrée 
			// statut fiche=  ==>  aucune quotation n'a été enregistré
			if (statut_fiche.equalsIgnoreCase("0")){
				listFiche=mapFicheEvaluee.get(selectedFamille);
				familleVisite=false;
			}else{
				if(statut_fiche.equalsIgnoreCase("-1")){
					//liste fiche va contenir la fusion entre mapPosteTravailFiche.get(cles);et listFiche=mapFicheEvaluee.get(selectedFamille);
					ArrayList<FicheEvaluationBean> listFiche1=mapFicheEvaluee.get(selectedFamille);
					ArrayList<FicheEvaluationBean> listFiche2=mapPosteTravailFiche.get(cles);
					if(listFiche1==null)
					{
						listFiche=listFiche2;
						familleVisite=false;
					}
					else
					{
						if(listFiche1!=null && listFiche1.size()==listFiche2.size())
							listFiche=listFiche1;
						else
						{

							listFiche=new ArrayList<FicheEvaluationBean>();
							Iterator<FicheEvaluationBean> iterator2=listFiche2.iterator();
							while(iterator2.hasNext())
							{
								Iterator<FicheEvaluationBean> iterator1=listFiche1.iterator();
								FicheEvaluationBean fiche2=iterator2.next();
								boolean dejaAffecte=false;
								while(iterator1.hasNext())
								{
									FicheEvaluationBean fiche1=iterator1.next();
									if(fiche1!=null && fiche2!=null)
									{
										if(fiche1.getAptitude_observable().equals(fiche2.getAptitude_observable())&&
												fiche1.getId_employe()==fiche2.getId_employe()&&
												fiche1.getId_planning_evaluation()== fiche2.getId_planning_evaluation())
										{
											listFiche.add(fiche1);
											dejaAffecte=true;
											break;
										}
									} 
								}
								if(!dejaAffecte)
								{
									fiche2.setNiveau_maitrise(-1);
									listFiche.add(fiche2);
								}
							}
						}
					}

				}
				else
				{
					listFiche=mapPosteTravailFiche.get(cles);
					familleVisite=false;
				}
			}



			//afficher le contenu de mapPosteTravailFiche



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
					String vleurCotation=cotationBean.getValeur_cotation()+"";
					if("0".equals(vleurCotation))
						vleurCotation="N/A";
					if(familleVisite)
					{
						cotation.appendItem(vleurCotation);
						//cotation.appendItem(cotationBean.getValeur_cotation()+"");

						valeur= ficheEvaluation.getId_repertoire_competence()+"#"+employerAEvaluerBean.getId_employe()+"#"+employerAEvaluerBean.getId_planning_evaluation();
						cotation.setContext(valeur);

						cotation.setName("-1");
						cotation.setReadonly(true);
						cotation.setParent(cellulecotation);
						cellulecotation.setParent(listItem);

						if (statut_fiche.equalsIgnoreCase("0") || statut_fiche.equalsIgnoreCase("-1")){
							String niveauMaitrise=ficheEvaluation.getNiveau_maitrise()+"";
							if("0".equals(niveauMaitrise))
								niveauMaitrise="N/A";
							//						 cotation.setValue(ficheEvaluation.getNiveau_maitrise()+"");
							//						 cotation.setName(ficheEvaluation.getNiveau_maitrise()+"");
							if(!"-1".equals(niveauMaitrise))
							{
								cotation.setValue(niveauMaitrise);
								cotation.setName(niveauMaitrise);
							}
							else
								cotation.setStyle("background:red;");
						}

					}
					else //si la famille n'a pas été enregistrée entant que brouillon
					{
						valeur= ficheEvaluation.getId_repertoire_competence()+"#"+employerAEvaluerBean.getId_employe()+"#"+employerAEvaluerBean.getId_planning_evaluation();
						//cotation.setStyle("background:red;");
						//cotation.setWidth("50%");

						//System.out.println("pas encore visite");
						String itemCotation=cotationBean.getValeur_cotation()+"";
						if("0".equals(itemCotation))
							itemCotation="N/A";
						cotation.appendItem(itemCotation);
						//cotation.appendItem(cotationBean.getValeur_cotation()+"");
						cotation.setContext(valeur);

						cotation.setName("-1");
						cotation.setParent(cellulecotation);
						cellulecotation.setParent(listItem);
					}
				}
				cotation.addForward("onSelect", comp1, "onSelectEvaluation");
				listeCombo.put(valeur,cotation );
				liste.add(listItem);

			}
			mapItemsFamille.put(cles, liste);
			currentListItem=liste;
			mapFamilleCombo.put(selectedFamille, listeCombo);

			//initialisation du reste des combos au cas oé il y deja eu une cotatio
			iterator=listFamillePoste.iterator();
			while (iterator.hasNext())
			{
				String famille=iterator.next();
				if(!selectedFamille.equalsIgnoreCase(famille))
				{
					listFiche=mapFicheEvaluee.get(famille);
					if(listFiche!=null)
					{
						iterator2=listFiche.iterator();
						listeCombo=new HashMap<String, Combobox>();
						while (iterator2.hasNext())
						{
							FicheEvaluationBean ficheEvaluation=(FicheEvaluationBean)iterator2.next();
							String libelleCompetence=ficheEvaluation.getLibelle_competence();
							String aptitudeObservable=ficheEvaluation.getAptitude_observable();
							String descriptionCompetence=ficheEvaluation.getDefinition_competence();

							//affichage des donnnées dans le tableau
							// Listitem listItem=new Listitem();
							//listItem.setParent(employelb);


							//cellule competence
							//						 Listcell cellulecompetence=new Listcell();
							//						 cellulecompetence.setLabel(libelleCompetence);
							//						 cellulecompetence.setTooltiptext(descriptionCompetence);
							//cellulecompetence.setParent(listItem);

							//cellule aptitude observable
							//						 Listcell celluleaptitude=new Listcell();
							//						 celluleaptitude.setLabel(aptitudeObservable);
							//						 celluleaptitude.setParent(listItem);


							//cellule niveau de maitrise
							// Listcell cellulecotation=new Listcell();
							Combobox cotation=new Combobox();
							//cotation.setWidth("50%");
							Iterator <CotationBean> iterator4=listCotation.iterator();
							String valeur="";
							while(iterator4.hasNext())
							{
								CotationBean cotationBean=(CotationBean)iterator4.next();
								String vleurCotation=cotationBean.getValeur_cotation()+"";
								if("0".equals(vleurCotation))
									vleurCotation="N/A";

								cotation.appendItem(vleurCotation);
								//cotation.appendItem(cotationBean.getValeur_cotation()+"");

								valeur= ficheEvaluation.getId_repertoire_competence()+"#"+employerAEvaluerBean.getId_employe()+"#"+employerAEvaluerBean.getId_planning_evaluation();
								cotation.setContext(valeur);

								cotation.setName("-1");
								cotation.setReadonly(true);
								//							 cotation.setParent(cellulecotation);
								//							 cellulecotation.setParent(listItem);

								if (statut_fiche.equalsIgnoreCase("0")|| statut_fiche.equalsIgnoreCase("-1")){
									String niveauMaitrise=ficheEvaluation.getNiveau_maitrise()+"";
									if("0".equals(niveauMaitrise))
										niveauMaitrise="N/A";
									//									 cotation.setValue(ficheEvaluation.getNiveau_maitrise()+"");
									//									 cotation.setName(ficheEvaluation.getNiveau_maitrise()+"");
									cotation.setValue(niveauMaitrise);
									cotation.setName(niveauMaitrise);
								}


							}
							cotation.addForward("onSelect", comp1, "onSelectEvaluation");
							listeCombo.put(valeur,cotation );
						}
					}
					if(listFiche!=null) //si une quotation a été déjé effectué;enregistrer sinon ne rien faire
						mapFamilleCombo.put(famille, listeCombo);
				}

			}
			// affichage du timer

		}		 
	}

	/**
	 * evenement a gerer lors de la selection d'un poste de travail
	 */
	public void onSelect$poste_travail()
	{



		//vider le contenu de la grille associée é l'ancien employé selectionné
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

		//1. mise é jour de la liste des employes avec la selection de l'attribut selectionner un employe
		//avec toutes les conséquences qui doivent se découler de cette de cette selection

		//remplissage du contenu de la combo associée aux postes de travail
		selectednomposteTravail=poste_travail.getSelectedItem().getLabel();
		if(!selectednomposteTravail.equals("Tous postes de travail"))
		{
			HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeAEvaluerBean.getMapclesposte();
			HashMap<String, EmployesAEvaluerBean> mapEmploye=Mapclesposte.get(selectednomposteTravail);
		    

			Set <String> listEmploye=mapEmploye.keySet();
			
			//Modif NB du 15/07/2018 point 4 v3.2 tri par ordre alphabetéque des evalués pour les fiche deja validées
			 //Set<String> listEmploye = new TreeSet<String>();
			//listEmploye=mapEmploye.keySet();

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
	 * evenement a gerer lors de la selection d'une famille pour la ficher d'evaluation d'un evalué
	 */
	public void onSelect$Famille()
	{

		boolean familleVisite=true;	
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

		//recuperer les infos associé é l'employé selectionné
		EmployesAEvaluerBean employerAEvaluerBean=mapEmployeAEvaluerBean.getMapclesnomEmploye().get(selectedEmploye);
		//recuperation du code_poste associé é l'intitule
		String code_poste=mapintitule_codeposte.get(selectednomposteTravail);

		String cles=code_poste+"#"+selectedFamille;

		//afficher le contenu de mapPosteTravailFiche
		// ArrayList<FicheEvaluationBean> listFiche=mapPosteTravailFiche.get(cles);

		/*******************************/
		FicheEvaluationModel fiche_model=new FicheEvaluationModel();
		String statut_fiche=fiche_model.getStatutFicheEval(String.valueOf(employerAEvaluerBean.getId_planning_evaluation()),
				String.valueOf(employerAEvaluerBean.getId_employe()));
		ArrayList<FicheEvaluationBean> listFiche;
		FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
		FicheEvaluationJsonModel ficheEvaluationJsonModel=new FicheEvaluationJsonModel();
		mapFicheEvaluee=ficheEvaluationJsonModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe(), mapcodeCompetenceLibelleCompetence, mapidRepCompetence_ApptitudeObservable,String.valueOf(compagne_id));
		//		 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
		//		 mapFicheEvaluee=ficheEvaluationModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe());
		if (statut_fiche.equalsIgnoreCase("0")){
			listFiche=mapFicheEvaluee.get(selectedFamille);
			familleVisite=false;
		}/*else{
			  listFiche=mapPosteTravailFiche.get(cles);
		  }*/
		/***********************/
		else{
			if(statut_fiche.equalsIgnoreCase("-1")){
				validationtente=true;
				//liste fiche va contenir la fusion entre mapPosteTravailFiche.get(cles);et listFiche=mapFicheEvaluee.get(selectedFamille);
				ArrayList<FicheEvaluationBean> listFiche1=mapFicheEvaluee.get(selectedFamille);
				ArrayList<FicheEvaluationBean> listFiche2=mapPosteTravailFiche.get(cles);

				if(listFiche1==null)
				{
					listFiche=listFiche2;
					familleVisite=false;	
				}
				else
				{
					if(listFiche1!=null && listFiche1.size()==listFiche2.size())
						listFiche=listFiche1;
					else
					{

						listFiche=new ArrayList<FicheEvaluationBean>();
						Iterator<FicheEvaluationBean> iterator2=listFiche2.iterator();
						while(iterator2.hasNext())
						{
							Iterator<FicheEvaluationBean> iterator1=listFiche1.iterator();
							FicheEvaluationBean fiche2=iterator2.next();
							boolean dejaAffecte=false;
							while(iterator1.hasNext())
							{
								FicheEvaluationBean fiche1=iterator1.next();
								if(fiche1!=null && fiche2!=null)
								{
									if(fiche1.getAptitude_observable().equals(fiche2.getAptitude_observable())&&
											fiche1.getId_employe()==fiche2.getId_employe()&&
											fiche1.getId_planning_evaluation()== fiche2.getId_planning_evaluation())
									{
										listFiche.add(fiche1);
										dejaAffecte=true;
										break;
									}
								} 
							}
							if(!dejaAffecte)
							{
								fiche2.setNiveau_maitrise(-1);
								listFiche.add(fiche2);
							}
						}
					}

				}  
			}
			else
			{
				listFiche=mapPosteTravailFiche.get(cles);
				familleVisite=false;
			}
		}

		/***********************/


		Iterator<FicheEvaluationBean> iterator2=listFiche.iterator();

		ArrayList<Listitem> liste=new ArrayList<Listitem>();

		HashMap<String, Combobox> listeComboMAJ=mapFamilleCombo.get(selectedFamille);
		listeCombo=new HashMap<String, Combobox>();
		/**********************/
		//		 Iterator<FicheEvaluationBean> iterator2=listFiche.iterator();
		//		 
		//		 ArrayList<Listitem> liste=new ArrayList<Listitem>();
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
				String vleurCotation=cotationBean.getValeur_cotation()+"";
				if("0".equals(vleurCotation))
					vleurCotation="N/A";
				//si la famille a déjé été visitée et enregistrée entant que brouillon
				if(familleVisite)
				{
					cotation.appendItem(vleurCotation);
					//cotation.appendItem(cotationBean.getValeur_cotation()+"");

					valeur= ficheEvaluation.getId_repertoire_competence()+"#"+employerAEvaluerBean.getId_employe()+"#"+employerAEvaluerBean.getId_planning_evaluation();
					cotation.setContext(valeur);

					cotation.setName("-1");
					cotation.setReadonly(true);


					if (statut_fiche.equalsIgnoreCase("0") || statut_fiche.equalsIgnoreCase("-1")){
						String niveauMaitrise=ficheEvaluation.getNiveau_maitrise()+"";
						if("0".equals(niveauMaitrise) )
							niveauMaitrise="N/A";
						//					 cotation.setValue(ficheEvaluation.getNiveau_maitrise()+"");
						//					 cotation.setName(ficheEvaluation.getNiveau_maitrise()+"");
						if(!"-1".equals(niveauMaitrise))
						{
							cotation.setValue(niveauMaitrise);
							cotation.setName(niveauMaitrise);
						}
						cotation.setParent(cellulecotation);
						cellulecotation.setParent(listItem);
					}
				}
				else //si la famille n'a pas été enregistrée entant que brouillon
				{
					valeur= ficheEvaluation.getId_repertoire_competence()+"#"+employerAEvaluerBean.getId_employe()+"#"+employerAEvaluerBean.getId_planning_evaluation();
					//cotation.setStyle("background:red;");
					//cotation.setWidth("50%");

					//System.out.println("pas encore visite");
					String itemCotation=cotationBean.getValeur_cotation()+"";
					if("0".equals(itemCotation))
						itemCotation="N/A";
					cotation.appendItem(itemCotation);
					//cotation.appendItem(cotationBean.getValeur_cotation()+"");
					cotation.setContext(valeur);

					cotation.setName("-1");
					cotation.setParent(cellulecotation);
					cellulecotation.setParent(listItem);
				}


			}
			cotation.addForward("onSelect", comp1, "onSelectEvaluation");
			listeCombo.put(valeur,cotation );
			liste.add(listItem);

		}

		/***************/
		//		 while (iterator2.hasNext())
		//		 {
		//			 FicheEvaluationBean ficheEvaluation=(FicheEvaluationBean)iterator2.next();
		//			 String libelleCompetence=ficheEvaluation.getLibelle_competence();
		//			 String aptitudeObservable=ficheEvaluation.getAptitude_observable();
		//			 String descriptionCompetence=ficheEvaluation.getDefinition_competence();
		//			 
		//			 //affichage des donnnées dans le tableau
		//			 Listitem listItem=new Listitem();
		//			 listItem.setParent(employelb);
		//			 	 
		//				 
		//			 //cellule competence
		//			 Listcell cellulecompetence=new Listcell();
		//			 cellulecompetence.setLabel(libelleCompetence);
		//			 cellulecompetence.setTooltiptext(descriptionCompetence);
		//			 cellulecompetence.setParent(listItem);
		//				 
		//			 //cellule aptitude observable
		//			 Listcell celluleaptitude=new Listcell();
		//			 celluleaptitude.setLabel(aptitudeObservable);
		//			 celluleaptitude.setParent(listItem);
		//				 
		//				 
		//			 //cellule niveau de maitrise
		//			 Listcell cellulecotation=new Listcell();
		//			 Combobox cotation=new Combobox();
		//			 //cotation.setWidth("50%");
		//			 Iterator <CotationBean> iterator4=listCotation.iterator();
		//			 String valeur="";
		//			 boolean dejaVisite=false;
		//			 
		//			 if (validationtente)
		//			 {
		//				 cotation.setStyle("background:red;");
		//				 //cotation.setWidth("50%");
		//			 }
		//			 
		//			 if(listeComboMAJ!=null)
		//				 dejaVisite=true;
		//				 
		//			 while(iterator4.hasNext())
		//			 {
		//				 CotationBean cotationBean=(CotationBean)iterator4.next();
		//				 
		//				 
		//				 valeur= ficheEvaluation.getId_repertoire_competence()+"#"+employerAEvaluerBean.getId_employe()+"#"+employerAEvaluerBean.getId_planning_evaluation();
		//				 
		//				 if (dejaVisite==true)
		//				 {
		//					 
		//					 cotation=listeComboMAJ.get(valeur);
		//					 //System.out.println("dans deja visite");
		//				 }
		//				 if(!dejaVisite || cotation==null)
		//				 {
		//					 if(cotation==null)
		//					 {
		//						 cotation=new Combobox();
		//						 if (validationtente)
		//						 {
		//							 cotation.setStyle("background:red;");
		//							 //cotation.setWidth("50%");
		//						 }
		//					 }
		//					 //System.out.println("pas encore visite");
		//					 String itemCotation=cotationBean.getValeur_cotation()+"";
		//					 if("0".equals(itemCotation))
		//						 itemCotation="N/A";
		//					 cotation.appendItem(itemCotation);
		//					 //cotation.appendItem(cotationBean.getValeur_cotation()+"");
		//					 cotation.setContext(valeur);
		//					
		//					 cotation.setName("-1");
		//					 
		//				 }
		//				 if(cotation!=null)
		//				 {
		//					 cotation.setReadonly(true);
		//					 cotation.setParent(cellulecotation);
		//				 }
		//				 
		//				 cellulecotation.setParent(listItem);
		//				 
		//				 
		//			 }
		//
		//			 if(cotation!=null)
		//			 {
		//				
		//				
		//				 cotation.addForward("onSelect", comp1, "onSelectEvaluation");
		//				 listeCombo.put(valeur,cotation );
		//			 }
		//
		//			 liste.add(listItem);
		//				 
		//		 }
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

	public void onClick$valider() throws InterruptedException, ParseException
	{

		if ( Messagebox.show( "Voulez-vous enregister la  fiche d'évaluation ?", "Prompt", Messagebox.YES | Messagebox.NO,
				Messagebox.QUESTION ) == Messagebox.YES )	 {

			validationtente=true;
			if(autorise)
			{
				//instanciation del'objet ficheEvaluationJsonBean
				FicheEvaluationJsonBean ficheEvaluationJsonBean=new FicheEvaluationJsonBean();

				autorise=false;
				//				 valider.setVisible(false); grisé
				confirmer.setVisible(true);

				//				 valider.setDisabled(true); grisé
				//confirmer.setDisabled(true);

				Set <String> famillesRemplies=mapFamilleCombo.keySet();
				//nouveau comportement 
				// enregistrement des données saisies et mettre en rouge ce qui n'a pas encore été saisi
				//si toutes les fiches ont eté évaluées 

				valider.setDisabled(false);
				confirmer.setDisabled(false);
				autorise=true;
				FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
				String requeteUpdateFicheEvalution="";
				Iterator <String>itfamille=famillesRemplies.iterator();
				String id_planning_evaluation="";
				String id_employe="";

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
						//si donnée non saisie ==> mettre la combo en rouge
						if(combo.getName().equals("-1"))
						{


							combo.setStyle("background:red;");

						}
						else //si la donnée a été saisie==> l'enregistrer
						{
							/*********************************/
							//rinitialisation du timer


							//récupérer les modifications et enregistrer dans la base de donnée



							HashMap<String, String> maprepCompComp= ficheEvaluationModel.getmaprepCompetenceCodeCompetence();
							/**********************************/
							// famille par famille que tous les combos sont remplies






							HashMap<String, HashMap<String, ArrayList<Double>>> mapFamilleCompetence=new HashMap<String, HashMap<String, ArrayList<Double>>>();
							HashMap <String, Double> FamilleIMI=new HashMap<String, Double>();
							double statIMI=0;
							int nbimi=0; 


							String valeurs=combo.getContext();
							String[] val=valeurs.split("#");
							String id_repertoire_competence=val[0];
							id_employe=val[1];
							id_planning_evaluation=val[2];
							String id_cotation=combo.getName();	

							setId_evalue(id_employe);
							setId_planning_eval(id_planning_evaluation);

							int valeurCotation=getValeurCotation(id_cotation);

							//construction de la requete de la mise é jour de l afiche d'evaluation
							//requeteUpdateFicheEvalution=ficheEvaluationModel.updateFicheEvalutionConstructionRequete(id_repertoire_competence,id_employe,id_planning_evaluation,id_cotation,requeteUpdateFicheEvalution);
							//System.out.println(requeteUpdateFicheEvalution);

							//							 ficheEvaluationModel.updateMultiQuery(requeteUpdateFicheEvalution);
							//							 requeteUpdateFicheEvalution="";
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

							/***************************************/
							CotationEvaluationBean cotationEvaluationBean=new CotationEvaluationBean();
							cotationEvaluationBean.setCotation(valeurCotation+"");
							String libelleCompetence=mapcodeCompetenceLibelleCompetence.get(code_competence);

							//cotationEvaluationBean.setLibelle_competence(libelleCompetence);
							cotationEvaluationBean.setId_repertoire_competence(new Integer(id_repertoire_competence));
							cotationEvaluationBean.setFamille(clles);
							cotationEvaluationBean.setCode_competence(code_competence);
							String apptitudeObservable=mapidRepCompetence_ApptitudeObservable.get(id_repertoire_competence);
							// cotationEvaluationBean.setAptitude_observable(apptitudeObservable);
							ficheEvaluationJsonBean.AjouterCotation(cotationEvaluationBean);


						}
					}

				}//fin de la boucle qui permet de récupérer les données de l'ecran
				ficheEvaluationJsonBean.setFicheValide(false);
				//ces deux variables doivent étre positionnées quand on gérera l'affichage de plusieurs compagnes pour un employe
				//				 ficheEvaluationJsonBean.setCompagne_type(compagne_type);
				//				 ficheEvaluationJsonBean.setDate_evaluation(date_evaluation);
				ficheEvaluationJsonBean.setId_employe(new Integer(id_employe));
				ficheEvaluationJsonBean.setId_planning_evaluation(new Integer(id_planning_eval));
				FicheEvaluationJsonModel ficheJsonModel=new  FicheEvaluationJsonModel();
				boolean result_fich_eval=ficheJsonModel.updateFicheEvalutionJson(id_employe, id_planning_evaluation, ficheEvaluationJsonBean);
				//exécution de la requéte de mise a jour ou d'enregistrement dans la base
				// boolean result_fich_eval=ficheEvaluationModel.updateMultiQuery(requeteJson);



				autorise=true;

				if (result_fich_eval /*&& result_imi_stat && result_imi_compstat*/ ){

					//validation de la fiche d'evaluation
					ficheEvaluationModel.validerFicheEvaluation(id_planning_evaluation, id_employe,"-1");

				}


				valider.setVisible(true);
				confirmer.setVisible(true);
			}
			//employelb.renderAll()

			return;
		}

		else
		{
			return;
		}

		//verifier que toutes les cotations ont été remplies 
		//sinon afficher un message comme quoi la validation n'a pas été prise en compte car
		// toutes les comptétences n'ont pas été évaluées
		//verifier que toutes les familles ont été selectionnées
		// ArrayList <String> listFamille=employerAEvaluerBean1.getFamille();

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

		employelbM.getItems().clear();
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
		if (listficheEvaluationBean==null){

			try {
				Messagebox.show("L'employé  "+nomEmployeM.getValue()+" ne dispose pas de fiche pour la compagne sélectionné", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;


		}
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

			String sValCotation=valCotation+"";
			if(0==valCotation || valCotation==6)
				sValCotation="N/A";
			cellulecotation.setLabel(sValCotation);

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
		

		//modif point 2 v3.2 05/07/2018 ajout combo direction uniquement pour le profil super admin and admin
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		CompteBean compteUtilisateur=applicationSession.getCompteUtilisateur();
		String poste =(String)poste_travailV.getSelectedItem().getLabel();
		String direction =(String)directionV.getSelectedItem().getLabel();
		if ((compteUtilisateur.getId_profile()==2)||(compteUtilisateur.getId_profile()==1)){

			//directionV.setVisible(true);
			employeV.getItems().clear();
			employelbV.getItems().clear();
		
			// debut modif point 2 v3.2 05/07/2018
			HashMap<String,ArrayList<String>> Mapdirection=mapEmployeEvalueBean.getMapclesdirection().get(direction);
			ArrayList<String> employelist= Mapdirection.get(poste);
			if(employelist.size()>0)
			Collections.sort(employelist);


			if (employelist==null || employelist.size()==0   ){
				try {
					Messagebox.show("Merci de selectionner un poste de travail ", "Erreur", Messagebox.OK, Messagebox.ERROR);
					return;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			employeV.appendItem("sélectionner un employé");
			
				for (String employe : employelist) {
					employeV.appendItem(employe);
				}
				
				
				/* for (Map.Entry<String, EmployesAEvaluerBean> entryEmp : mapEmp.entrySet()) {
				    String nom_employe = entry.getKey();
				    EmployesAEvaluerBean employesAEvaluerBean=entryEmp.getValue();
				    //mapEmploye.put(nom_employe,employesAEvaluerBean);
			    }*/

			



			//selection du premier item (tous direction)
			employeV.setSelectedIndex(0);
			// fin modif point 2 v3.2 05/07/2018

		}

		//fin modif point 2 v3.2
		else{



			//debut Modif NB du 18/02/16 Point 9
			structureV.setVisible(false);
			structureV.setText("");
			evaluateurV.setText("");

			evaluateurV.setVisible(false);
			structure_lblV.setVisible(false);
			evaluateur_lblV.setVisible(false);
			//Fin Modif NB du 18/02/16 Point 9

			//vider le contenu de la grille associée é l'ancien employé selectionné
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

			//1. mise é jour de la liste des employes avec la selection de l'attribut selectionner un employe
			//avec toutes les conséquences qui doivent se découler de cette de cette selection

			//remplissage du contenu de la combo associée aux postes de travail
			selectednomposteTravailV=poste_travailV.getSelectedItem().getLabel();
			if(!selectednomposteTravailV.equals("Tous postes de travail"))
			{
				HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeEvalueBean.getMapclesposte();
				HashMap<String, EmployesAEvaluerBean> mapEmploye=Mapclesposte.get(selectednomposteTravailV);
				
				//Modif NB du 15/07/2018 point 4 v3.2 tri par ordre alphabetéque des evalués pour les fiche deja validées
				//transformer set en List et apres filtre la list 
				//Set <String> listEmploye=mapEmploye.keySet();
				 Set<String> set=mapEmploye.keySet();
				 List<String> listEmploye = new ArrayList<>(set);
				 Collections.sort(listEmploye);
				
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
	}

	//	 public void onSelectFamilleV()
	//	 {
	//		 
	//			boolean familleVisite=true;	
	//		 //récupération de la famille selectionnée
	//		 selectedFamilleV=(String)FamilleV.getSelectedItem().getLabel();
	//		 
	//		 //lors de la selection d'une famille, il faut :
	//		 //1. vider le contenu de la table
	//		 //2. remplir la table avec le cntenu de la nouvelle cles code_poste, famille
	//		 
	//		 
	//		//1. vider le contenu de la table
	//		 if (currentListItemV!=null)
	//		 {
	//			 Iterator<Listitem> iterator=currentListItemV.iterator();
	//			 while(iterator.hasNext())
	//			 {
	//				 Listitem item=iterator.next();
	//				 item.detach();
	//			 }
	//		 }
	//		 
	//		 //recuperer les infos associé é l'employé selectionné
	//		 EmployesAEvaluerBean employerAEvaluerBean=mapEmployeEvalueBean.getMapclesnomEmploye().get(selectedEmployeV);
	//		//recuperation du code_poste associé é l'intitule
	//		 String code_poste=mapintitule_codeposte.get(selectednomposteTravailV);
	//		 
	//		 String cles=code_poste+"#"+selectedFamilleV;
	//		 
	//		 //afficher le contenu de mapPosteTravailFiche
	//		// ArrayList<FicheEvaluationBean> listFiche=mapPosteTravailFiche.get(cles);
	//		 
	//		 /*******************************/
	//		 FicheEvaluationModel fiche_model=new FicheEvaluationModel();
	//		 String statut_fiche=fiche_model.getStatutFicheEval(String.valueOf(employerAEvaluerBean.getId_planning_evaluation()),
	//				                                           String.valueOf(employerAEvaluerBean.getId_employe()));
	//		 ArrayList<FicheEvaluationBean> listFiche;
	//		 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
	//		 FicheEvaluationJsonModel ficheEvaluationJsonModel=new FicheEvaluationJsonModel();
	//		 mapfamilleFicheEvaluationV=ficheEvaluationJsonModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe(), mapcodeCompetenceLibelleCompetence, mapidRepCompetence_ApptitudeObservable);
	////		 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
	////		 mapFicheEvaluee=ficheEvaluationModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe());
	//		  if (statut_fiche.equalsIgnoreCase("0")){
	//			  listFiche=mapfamilleFicheEvaluationV.get(selectedFamilleV);
	//			  familleVisite=false;
	//		  }/*else{
	//			  listFiche=mapPosteTravailFiche.get(cles);
	//		  }*/
	//		  /***********************/
	//		  else{
	//			  if(statut_fiche.equalsIgnoreCase("-1")){
	//				  validationtente=true;
	//				  //liste fiche va contenir la fusion entre mapPosteTravailFiche.get(cles);et listFiche=mapFicheEvaluee.get(selectedFamille);
	//				  ArrayList<FicheEvaluationBean> listFiche1=mapFicheEvaluee.get(selectedFamilleV);
	//				  ArrayList<FicheEvaluationBean> listFiche2=mapPosteTravailFicheV.get(cles);
	//				  
	//				  if(listFiche1==null)
	//				  {
	//					  listFiche=listFiche2;
	//					  familleVisite=false;	
	//				  }
	//				  else
	//				  {
	//					  if(listFiche1!=null && listFiche1.size()==listFiche2.size())
	//						  listFiche=listFiche1;
	//					  else
	//					  {
	//						 
	//						  listFiche=new ArrayList<FicheEvaluationBean>();
	//						  Iterator<FicheEvaluationBean> iterator2=listFiche2.iterator();
	//						  while(iterator2.hasNext())
	//						  {
	//							  Iterator<FicheEvaluationBean> iterator1=listFiche1.iterator();
	//							  FicheEvaluationBean fiche2=iterator2.next();
	//							  boolean dejaAffecte=false;
	//							  while(iterator1.hasNext())
	//							  {
	//								  FicheEvaluationBean fiche1=iterator1.next();
	//								  if(fiche1!=null && fiche2!=null)
	//								  {
	//									  if(fiche1.getAptitude_observable().equals(fiche2.getAptitude_observable())&&
	//											  fiche1.getId_employe()==fiche2.getId_employe()&&
	//											  fiche1.getId_planning_evaluation()== fiche2.getId_planning_evaluation())
	//									  {
	//										  listFiche.add(fiche1);
	//										  dejaAffecte=true;
	//										  break;
	//									  }
	//								  } 
	//							  }
	//							  if(!dejaAffecte)
	//							  {
	//								  fiche2.setNiveau_maitrise(-1);
	//								  listFiche.add(fiche2);
	//							  }
	//						  }
	//					  }
	//				  
	//				  }  
	//			  }
	//			  else
	//			  {
	//				  listFiche=mapPosteTravailFicheV.get(cles);
	//				  familleVisite=false;
	//			  }
	//		  }
	//		
	//		 /***********************/
	//		 
	//		 
	//		 Iterator<FicheEvaluationBean> iterator2=listFiche.iterator();
	//		 
	//		 ArrayList<Listitem> liste=new ArrayList<Listitem>();
	//		 
	//		 HashMap<String, Combobox> listeComboMAJ=mapFamilleComboV.get(selectedFamilleV);
	//		 listeComboV=new HashMap<String, Combobox>();
	//		 /**********************/
	////		 Iterator<FicheEvaluationBean> iterator2=listFiche.iterator();
	////		 
	////		 ArrayList<Listitem> liste=new ArrayList<Listitem>();
	//		 listeComboV=new HashMap<String, Combobox>();
	//		 while (iterator2.hasNext())
	//		 {
	//			 FicheEvaluationBean ficheEvaluation=(FicheEvaluationBean)iterator2.next();
	//			 String libelleCompetence=ficheEvaluation.getLibelle_competence();
	//			 String aptitudeObservable=ficheEvaluation.getAptitude_observable();
	//			 String descriptionCompetence=ficheEvaluation.getDefinition_competence();
	//			 
	//			 //affichage des donnnées dans le tableau
	//			 Listitem listItem=new Listitem();
	//			 listItem.setParent(employelbV);
	//			 	 
	//				 
	//			 //cellule competence
	//			 Listcell cellulecompetence=new Listcell();
	//			 cellulecompetence.setLabel(libelleCompetence);
	//			 cellulecompetence.setTooltiptext(descriptionCompetence);
	//			 cellulecompetence.setParent(listItem);
	//				 
	//			 //cellule aptitude observable
	//			 Listcell celluleaptitude=new Listcell();
	//			 celluleaptitude.setLabel(aptitudeObservable);
	//			 celluleaptitude.setParent(listItem);
	//				 
	//				 
	//			 //cellule niveau de maitrise
	//			 Listcell cellulecotation=new Listcell();
	//			 Combobox cotation=new Combobox();
	//			 //cotation.setWidth("50%");
	//			 Iterator <CotationBean> iterator4=listCotation.iterator();
	//			 String valeur="";
	//			 while(iterator4.hasNext())
	//			 {
	//				 CotationBean cotationBean=(CotationBean)iterator4.next();
	//				 String vleurCotation=cotationBean.getValeur_cotation()+"";
	//				 if("0".equals(vleurCotation))
	//					 vleurCotation="N/A";
	//				 //si la famille a déjé été visitée et enregistrée entant que brouillon
	//				 if(familleVisite)
	//				 {
	//					 cotation.appendItem(vleurCotation);
	//					 //cotation.appendItem(cotationBean.getValeur_cotation()+"");
	//					 
	//					 valeur= ficheEvaluation.getId_repertoire_competence()+"#"+employerAEvaluerBean.getId_employe()+"#"+employerAEvaluerBean.getId_planning_evaluation();
	//					 cotation.setContext(valeur);
	//					 
	//					 cotation.setName("-1");
	//					 cotation.setReadonly(true);
	//					 
	//					 
	//					 if (statut_fiche.equalsIgnoreCase("0") || statut_fiche.equalsIgnoreCase("-1")){
	//						 String niveauMaitrise=ficheEvaluation.getNiveau_maitrise()+"";
	//						 if("0".equals(niveauMaitrise) )
	//							 niveauMaitrise="N/A";
	//	//					 cotation.setValue(ficheEvaluation.getNiveau_maitrise()+"");
	//	//					 cotation.setName(ficheEvaluation.getNiveau_maitrise()+"");
	//						 if(!"-1".equals(niveauMaitrise))
	//						 {
	//							 cotation.setValue(niveauMaitrise);
	//							 cotation.setName(niveauMaitrise);
	//						 }
	//						 cotation.setParent(cellulecotation);
	//						 cellulecotation.setParent(listItem);
	//					 }
	//				 }
	//				 else //si la famille n'a pas été enregistrée entant que brouillon
	//				 {
	//					 valeur= ficheEvaluation.getId_repertoire_competence()+"#"+employerAEvaluerBean.getId_employe()+"#"+employerAEvaluerBean.getId_planning_evaluation();
	//						//cotation.setStyle("background:red;");
	//						//cotation.setWidth("50%");
	//
	//						 //System.out.println("pas encore visite");
	//						 String itemCotation=cotationBean.getValeur_cotation()+"";
	//						 if("0".equals(itemCotation))
	//							 itemCotation="N/A";
	//						 cotation.appendItem(itemCotation);
	//						 //cotation.appendItem(cotationBean.getValeur_cotation()+"");
	//						 cotation.setContext(valeur);
	//						
	//						 cotation.setName("-1");
	//						 cotation.setParent(cellulecotation);
	//						 cellulecotation.setParent(listItem);
	//				 }
	//				 
	//				 
	//			 }
	//			 cotation.addForward("onSelect", comp1, "onSelectEvaluationV");
	//			 listeComboV.put(valeur,cotation );
	//			 liste.add(listItem);
	//				 
	//		 }
	//		 
	//		 mapFamilleComboV.put(selectedFamilleV, listeComboV);
	//		 mapItemsFamilleV.put(cles, liste);
	//		 currentListItemV=liste;
	//	 		 
	//	 }
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

		//recuperer les infos associé é l'employé selectionné
		EmployesAEvaluerBean employerAEvaluerBean=mapEmployeEvalueBean.getMapclesnomEmploye().get(selectedEmployeV);


		//recuperation du code_poste associé é l'intitule
		//String code_poste=mapintitule_codeposte.get(selectednomposteTravailV);

		//String cles=code_poste+"#"+selectedFamilleV;

		//afficher le contenu de mapfamilleFicheEvaluationV
		FicheEvaluationJsonModel ficheEvaluationModel=new FicheEvaluationJsonModel();

		mapfamilleFicheEvaluationV=ficheEvaluationModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe(), mapcodeCompetenceLibelleCompetence, mapidRepCompetence_ApptitudeObservable,String.valueOf(compagne_id));

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

			String sValCotation=valCotation+"";
			if(0==valCotation || 6==valCotation)
				sValCotation="N/A";
			cellulecotation.setLabel(sValCotation);

			cellulecotation.setParent(listItem);

			liste.add(listItem);
		}

		currentListItemV=liste;

	}

	/**
	 * evenement a gerer lors de la selection d'un employé de l'onglet employe evalué
	 * @throws ParseException 
	 */
	public void onSelect$employeV() throws ParseException
	{
		//remplir les champs evaluateur et structure
		//debut modif NB 18/02/16

		compagne_id= Integer.parseInt((String) map_compagne.get((String)compagneV.getSelectedItem().getLabel()));


		FicheEvaluationModel ficheEvaluationModelID=new FicheEvaluationModel();
		selectedEmployeV=employeV.getSelectedItem().getLabel();
		EmployesAEvaluerBean employerAEvaluerID=mapEmployeEvalueBean.getMapclesnomEmploye().get(selectedEmployeV);
		if (employerAEvaluerID!=null){
			String nomEvaluateur= ficheEvaluationModelID.getMonEvaluateur(employerAEvaluerID.getId_employe(),String.valueOf(compagne_id));
			evaluateurV.setText(nomEvaluateur);
			//récupération du nom de la personne connecté
			String retour=ficheEvaluationModelID.getNomUtilisateur(employerAEvaluerID.getId_employe());
			String[] val=retour.split("#");
			structureV.setVisible(true);
			String structuretxt=val[2];
			structureV.setText(structuretxt);
			evaluateurV.setVisible(true);
			structure_lblV.setVisible(true);
			evaluateur_lblV.setVisible(true);

		}else{
			String nomEvaluateur="";
			String structuretxt="";
			evaluateurV.setVisible(false);
			structure_lblV.setVisible(false);
			evaluateur_lblV.setVisible(false);
			FamilleV.getItems().clear();
		}




		//fin modif NB 18/02/16

		//vider le contenu de la grille associée é l'ancien employé selectionné
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
		//lors de la selection d'un employé, affichage de la fiche associé é cet employé si elle existe 
		//si la fiche n'existe pas , il faut la creer

		//1. affichage des informations relatifs é l'employé

		selectedEmployeV=employeV.getSelectedItem().getLabel();

		if(!selectedEmployeV.equals("sélectionner un employé"))
		{

			nomEmployeV.setText(selectedEmployeV);
			EmployesAEvaluerBean employerAEvaluerBean=mapEmployeEvalueBean.getMapclesnomEmploye().get(selectedEmployeV);
			//employerAEvaluerBean1=employerAEvaluerBean;
			selectednomposteTravailV=employerAEvaluerBean.getPoste_travail();
			posteTravailV.setText(selectednomposteTravailV);

			FicheEvaluationJsonModel ficheEvaluationJsonModel=new FicheEvaluationJsonModel();
			FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();

			mapfamilleFicheEvaluationV=ficheEvaluationJsonModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe(), mapcodeCompetenceLibelleCompetence, mapidRepCompetence_ApptitudeObservable,String.valueOf(compagne_id));

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

			//afficher toutes les données associées é ce poste de travail

			//recuperation du code_poste associé é l'intitule


			//String cles=code_poste+"#"+selectedFamilleV;

			//System.out.println(cles);



			mapfamilleFicheEvaluationV=ficheEvaluationJsonModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe(), mapcodeCompetenceLibelleCompetence, mapidRepCompetence_ApptitudeObservable,String.valueOf(compagne_id));

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

				String sValeurCotation=valCotation+"";
				if(0==valCotation || 6==valCotation)
					sValeurCotation="N/A";
				cellulecotation.setLabel(sValeurCotation);

				cellulecotation.setParent(listItem);

				liste.add(listItem);
			}

			currentListItemV=liste;
		}	


	}

	public void rafraichirAffichage() throws ParseException
	{
		//				super.doAfterCompose(comp);
		//				comp1=comp;
		//				comp.setVariable(comp.getId() + "Ctrl", this, true);
		//				

		valider.setDisabled(true); 
		confirmer.setDisabled(true);
		//recupération du profil de l'utilisateur

		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		//TODO ligne a supprimer ApplicationFacade

		//CompteBean compteUtilisateur=ApplicationFacade.getInstance().getCompteUtilisateur();
		CompteBean compteUtilisateur=applicationSession.getCompteUtilisateur();

		//récupération de l'id_employé associé é l'id_compte
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

		//récupération des informations associées é une fiche d'evaluation é remplir;

		mapPosteTravailFiche=ficheEvaluationModel.getInfosFicheEvaluationparPoste(compagne_id);
		FicheEvaluationJsonModel ficheEvaluationJsonModel=new FicheEvaluationJsonModel();		
		mapintitule_codeposte=ficheEvaluationJsonModel.getlistepostesCode_postes(compagne_id);
		mapcode_intituleposte=ficheEvaluationJsonModel.getlisteCode_postes_intituleposte(compagne_id);


		//onglet Ma Fiche d'évaluation

		//evaluations.setStyle("overflow:auto");
		//				posteTravail.setDisabled(true);
		//				nomEmploye.setDisabled(true);

		//remplissage de la combobox famille
		//si c'est un évaluateur alors on affiche la liste des fiches associés aux employés é évaluer
		boolean est_evaluateur=false;
		//if(compteUtilisateur.getId_profile()==3)
		//un administrateur peut étre un évaluateur aors on test l'attribut est_evauateur de la table employe
		est_evaluateur=ficheEvaluationModel.getEstEvauateur(id_employe);
		if(est_evaluateur)
		{				
			//
			//				//si c'est un évaluateur alors on affiche la liste des fiches associés aux employés é évaluer
			//				if(compteUtilisateur.getId_profile()==3)
			//				{


			//remplissage du contenu de la combo associée aux postes de travail
			mapEmployeAEvaluerBean=ficheEvaluationModel.getListEmployesAEvaluer(id_employe,compagne_id);
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

			poste_travail.appendItem("Tous postes de travail");
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
		//if((compteUtilisateur.getId_profile()==3)||(compteUtilisateur.getId_profile()==2))
		if((compteUtilisateur.getId_profile()==3)||(compteUtilisateur.getId_profile()==2) ||(compteUtilisateur.getId_profile()==1))
		{


			mapPosteTravailFicheV=ficheEvaluationModel.getInfosFicheEvaluationparPoste(compagne_id);



			//remplissage du contenu de la combo associée aux postes de travail
			//si c'est un evaluateur lancer cette methode
			if (compteUtilisateur.getId_profile()==3)
				mapEmployeEvalueBean=ficheEvaluationModel.getListEmployesvalue(id_employe);

			//sinon (administrateur ) lancer une autre méthode qui récupére tous ceux qui ont été évaluées
			if (compteUtilisateur.getId_profile()==2)
				mapEmployeEvalueBean=ficheEvaluationModel.getListTousEmployesvalue(compagne_id);
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
			poste_travailV.appendItem("Tous postes de travail");
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
		confirmer.setDisabled(false);



	}
	public int getValeurCotation(String idCotation)
	{

		//recherche de l'id_cotation
		Iterator <CotationBean> iterator4=listCotation.iterator();
		String valeur="";
		int id=0;
		if(idCotation.equals("N/A"))
			return 0;
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
	public boolean enregistrerIMIStatParCompetence(HashMap<String, HashMap<String , ArrayList<Double>>> mapFamilleCompetence,String id_planning_evaluation, String id_employe)
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
				int nbCompetencecpt=listeCompetence.size();
				int nbCompetence=0;//
				Double moyenne=new Double(0);
				for(int i=0;i<nbCompetencecpt;i++)
				{
					Double valeurCotation=listeCompetence.get(i);
					if(0!=valeurCotation)
					{
						moyenne=moyenne+valeurCotation;
						nbCompetence++;
					}
				}
				moyenne=moyenne/nbCompetence;
				requete=requete+"  delete from IMI_COMPETENCE_STAT where id_compagne=#id_compagne and id_employe=#id_employe and code_famille=#code_famille and code_competence=#code_competence ; ";
				requete=requete+"  INSERT INTO IMI_COMPETENCE_STAT  (id_compagne,id_employe,code_famille,code_competence,moy_competence) VALUES (#id_compagne,#id_employe,#code_famille,#code_competence,#moy_competence) ; ";

				requete = requete.replaceAll("#id_compagne", id_compagne);
				requete = requete.replaceAll("#id_employe", id_employe);
				requete = requete.replaceAll("#code_famille", "'"+code_famille+ "'");
				requete = requete.replaceAll("#code_competence", "'"+code_competence+ "'");
				requete = requete.replaceAll("#moy_competence", moyenne+"");
				// System.out.println("requets IMI_COMPETENCE_STAT>>> "+requete);
			}


		}


		return(ficheEvaluationModel.insertImiCompetenceStat(requete));
	}



	public boolean enregistrerIMIMoyenneFamilleutilisantCompetence(HashMap<String, HashMap<String , ArrayList<Double>>> mapFamilleCompetence,String id_planning_evaluation, String id_employe)
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

				int nbCompetencecpt=listeCompetence.size();
				int nbCompetence=0;
				Double moyenne=new Double(0);

				for(int i=0;i<nbCompetencecpt;i++)
				{
					Double valeurCotation=listeCompetence.get(i);
					if(0!=valeurCotation)
					{
						moyenne=moyenne+valeurCotation;
						nbCompetence++;
					}
				}
				moyenne=new Double(moyenne/nbCompetence);
				if(moyenne!=0) // au cas oé toute les famille est N/A
					valeurFamilleMoyenne.add(moyenne);

			}
			//calcul de la moyenne par famille
			Iterator<Double> iterator=valeurFamilleMoyenne.iterator();
			Double moyenne=new Double(0);
			int nbVal=0;
			while(iterator.hasNext())
			{
				Double var=iterator.next();
				if(!var.isNaN())
				{	 nbVal++;
				moyenne=moyenne+var;
				}
			}
			moyenne=moyenne/nbVal;

			moyIMI=moyIMI+moyenne;
			requete=requete+ " delete from imi_stats where id_compagne=#id_compagne and id_employe=#id_employe and code_famille=#code_famille ; ";
			requete=requete+ " INSERT INTO imi_stats (id_compagne,id_employe,moy_par_famille,code_famille, imi) VALUES (#id_compagne,#id_employe,#moy_par_famille,#code_famille, #imi) ; ";
			requete = requete.replaceAll("#id_compagne", id_compagne);
			requete = requete.replaceAll("#id_employe", id_employe);
			requete = requete.replaceAll("#moy_par_famille", moyenne+"");
			requete = requete.replaceAll("#code_famille", "'"+code_famille+"'");

			//System.out.println("requetes IMI>>>"+requete);


		}

		//mise a jour de la requete avec l'IMI;
		Double calculIMI=moyIMI/nbFamille;

		requete = requete.replaceAll("#imi", "'"+calculIMI+"'");

		return(ficheEvaluationModel.insertImiCompetenceStat(requete));

	}

	//validation de la fichier d'evaluation	 
	public void onClick$confirmer() throws InterruptedException, ParseException	 {
		int nombreAptitude=0;
		FicheEvaluationModel fiche_model=new FicheEvaluationModel();
		EmployesAEvaluerBean employerAEvaluerBean=mapEmployeAEvaluerBean.getMapclesnomEmploye().get(selectedEmploye);

		String statut_fiche=fiche_model.getStatutFicheEval(String.valueOf(employerAEvaluerBean.getId_planning_evaluation()), String.valueOf(employerAEvaluerBean.getId_employe())); 

		//if (statut_fiche.equalsIgnoreCase("0")){

		if ( Messagebox.show( "La fiche d'évaluation ne sera plus accessible en modification, voulez-vous valider la fiche  ?", "Prompt", Messagebox.YES | Messagebox.NO,
				Messagebox.QUESTION ) == Messagebox.YES )	 {
			/************************************************************/

			//verifier si toutes les données ont ete saisi
			//si toutes les fiches ont eté évaluées 
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
					confirmer.setVisible(true);

					valider.setDisabled(false);
					confirmer.setDisabled(false);
					autorise=true;
					try 
					{

						Messagebox.show("Vos modifications ne peuvent étre validées tant que vous n'avez pas évalué toutes les compétences de l'employé", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
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

					//						 	count.setVisible(false);
					//						 	start.setDisabled(true);
					//						 	timer.stop();

					//désactiver le bouton valider
					//						 valider.setDisabled(true);  grisé
					//confirmer.setDisabled(true);
					//récupérer les modifications et enregistrer dans la base de donnée
					//valider la fiche dans la table appropriée

					FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
					FicheEvaluationJsonModel ficheEvaluationJsonModel=new FicheEvaluationJsonModel();
					FicheEvaluationJsonBean ficheEvaluationJsonBean=new FicheEvaluationJsonBean();
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

							setId_evalue(id_employe);
							setId_planning_eval(id_planning_evaluation);

							int valeurCotation=getValeurCotation(id_cotation);
							//ne pas prendre en compte les cotations 0 car éa veut dire non attribué
							if(0!=valeurCotation)
							{
								statINIFamille=statINIFamille+valeurCotation;

								nbvaleur++;
								nbimi++;
								statIMI=statIMI+valeurCotation;
								nombreAptitude++;
							}
							//construction de la requete de la mise é jour de l afiche d'evaluation
							//requeteUpdateFicheEvalution=ficheEvaluationModel.updateFicheEvalutionConstructionRequete(id_repertoire_competence,id_employe,id_planning_evaluation,id_cotation,requeteUpdateFicheEvalution);
							//								 ficheEvaluationModel.updateMultiQuery(requeteUpdateFicheEvalution);
							//								 requeteUpdateFicheEvalution="";

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

							/***************************************/
							CotationEvaluationBean cotationEvaluationBean=new CotationEvaluationBean();
							cotationEvaluationBean.setCotation(valeurCotation+"");
							//String libelleCompetence=mapcodeCompetenceLibelleCompetence.get(code_competence);

							//cotationEvaluationBean.setLibelle_competence(libelleCompetence);
							cotationEvaluationBean.setId_repertoire_competence(new Integer(id_repertoire_competence));
							cotationEvaluationBean.setFamille(clles);
							cotationEvaluationBean.setCode_competence(code_competence);
							//String apptitudeObservable=mapidRepCompetence_ApptitudeObservable.get(id_repertoire_competence);
							// cotationEvaluationBean.setAptitude_observable(apptitudeObservable);
							ficheEvaluationJsonBean.AjouterCotation(cotationEvaluationBean);
						}

						String clesIMI =id_planning_evaluation+"#"+id_employe+"#"+clles;
						statINIFamille=statINIFamille/nbvaleur;
						FamilleIMI.put(clesIMI, statINIFamille);

						//validation de la fiche

					}
					ficheEvaluationJsonBean.setFicheValide(true);
					//ces deux variables doivent étre positionnées quand on gérera l'affichage de plusieurs compagnes pour un employe
					//						 ficheEvaluationJsonBean.setCompagne_type(compagne_type);
					//						 ficheEvaluationJsonBean.setDate_evaluation(date_evaluation);
					ficheEvaluationJsonBean.setId_employe(new Integer(id_employe));
					ficheEvaluationJsonBean.setId_planning_evaluation(new Integer(id_planning_eval));
					FicheEvaluationJsonModel ficheJsonModel=new  FicheEvaluationJsonModel();
					boolean result_fich_eval=ficheJsonModel.updateFicheEvalutionJson(id_employe, id_planning_evaluation, ficheEvaluationJsonBean);

					//mise é jour de la fiche d'evaluation (exécution de la requete)
					// boolean result_fich_eval=ficheEvaluationModel.updateMultiQuery(requeteUpdateFicheEvalution);


					statIMI=statIMI/nbimi;




					//a la fin de l'evaluation de toute la famille enregistrement de l'IMI par famille et de l'IMI total dans la table
					//cette fonction calcul l'IMi en utilisant les notations des apptitudes observales
					// enregistrerIMIStat(FamilleIMI,statIMI);

					////cette fonction calcul l'IMi en utilisant les calculs de smoyennes pér competence
					boolean result_imi_stat=enregistrerIMIMoyenneFamilleutilisantCompetence(mapFamilleCompetence,id_planning_evaluation, id_employe);

					//enregistrement dans la base les stats IMI par competence

					boolean result_imi_compstat=enregistrerIMIStatParCompetence(mapFamilleCompetence,id_planning_evaluation, id_employe);
					autorise=ficheJsonModel.verifierNbAptitudecote(nbimi,employerAEvaluerBean.getCode_poste());

					if (result_fich_eval && result_imi_stat && result_imi_compstat && autorise ){

						//validation de la fiche d'evaluation
						ficheEvaluationModel.validerFicheEvaluation(id_planning_evaluation, id_employe,"0");
						fiche_model.validerFicheEvaluation(String.valueOf(employerAEvaluerBean.getId_planning_evaluation()), String.valueOf(employerAEvaluerBean.getId_employe()),"1");
						rafraichirAffichage();


						//TODO ajouter hito sur la fiche logger nombreAptitude;

						ficheJsonModel.histoFicheEvaluation(nbimi,employerAEvaluerBean.getId_employe(),employerAEvaluerBean.getNom_employe(),employerAEvaluerBean.getPoste_travail());

					}

					else {
						if(!autorise)
							Messagebox.show("Vos modifications ne peuvent étre validées tant que vous n'avez pas évalué toutes les compétences de l'employé", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
						else
							Messagebox.show("La fiche n'a pas été correctement enregistrée", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
					}

					//mis eé jour calcul imi en prenant en compte les calculs des moyennes par competence

					//vider le contenu du tableau 
					//effacer le nom de l'evalué de la combo de cet onglet et le mettre dans l'onglet des personnes déja évaluées

					//rafraichirAffichage(); //cet appel a été mis en commentaire car le bouton valider ne cloture pas l'evaluation de la fiche 
				}

			}
			else
			{
				valider.setDisabled(false);
				confirmer.setDisabled(false);
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

					Messagebox.show("Vos modifications ne peuvent étre validées tant que vous n'avez pas évalué toutes les compétences de l'employé", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			/***********************************************************/


			return;
		}

		else
		{
			return;
		}

		//			 }
		//			 
		//			 else{
		//				 Messagebox.show(" La fiche d'évaluation n'a pas été enregistrée ou certaines  compétences n'ont pas été évaluées . Merci de completer la fiche !");
		//			 }




	}
	public void onClick$exporterJson() throws InterruptedException
	{
		//pour chaque fiche charger les données et les enregistrer au format Json
		FicheEvaluationJsonModel ficheEvaluationJsonModel=new FicheEvaluationJsonModel();
		FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
		ArrayList <FicheEvaluationBean> listEmployesEvalue=ficheEvaluationJsonModel.geListEmployeAExporter();

		Iterator<FicheEvaluationBean> iterateur=listEmployesEvalue.iterator();
		System.out.println("il y a "+listEmployesEvalue.size()+ " fiches d'évaluation a exporter" );
		while(iterateur.hasNext())
		{
			FicheEvaluationBean Employe=(FicheEvaluationBean)iterateur.next();
			int idEmploye=Employe.getId_employe();
			System.out.println("export du salarié "+idEmploye);
			//récupérer la fiche d'evaluation de l'employe via la table fiche_evaluation
			HashMap <String, ArrayList<FicheEvaluationBean>> ficheEmploye= ficheEvaluationModel.getMaFicheEvaluaton(idEmploye);
			//enregister les données de chaque fiche d'avaluation dans json
			FicheEvaluationJsonBean ficheEvaluationJsonBean=new FicheEvaluationJsonBean();
			//boucler sur les familles
			Set <String> famillesRemplies=ficheEmploye.keySet();
			Iterator<String> iterateurFamille=famillesRemplies.iterator();
			while(iterateurFamille.hasNext())
			{
				String famille=iterateurFamille.next();
				ArrayList<FicheEvaluationBean> listCotation=ficheEmploye.get(famille);
				Iterator<FicheEvaluationBean> iterateurCotation=listCotation.iterator();
				while(iterateurCotation.hasNext())
				{
					FicheEvaluationBean ficheEvaluation=iterateurCotation.next();


					//remplissage des cotationsJson
					/***************************************/
					CotationEvaluationBean cotationEvaluationBean=new CotationEvaluationBean();
					cotationEvaluationBean.setCotation(ficheEvaluation.getNiveau_maitrise()+"");
					//String libelleCompetence=mapcodeCompetenceLibelleCompetence.get(ficheEvaluation.getC);

					//cotationEvaluationBean.setLibelle_competence(libelleCompetence);
					cotationEvaluationBean.setId_repertoire_competence(ficheEvaluation.getId_repertoire_competence());
					cotationEvaluationBean.setFamille(famille);
					cotationEvaluationBean.setCode_competence(ficheEvaluation.getCode_competence());
					//String apptitudeObservable=mapidRepCompetence_ApptitudeObservable.get(id_repertoire_competence);
					// cotationEvaluationBean.setAptitude_observable(apptitudeObservable);
					ficheEvaluationJsonBean.AjouterCotation(cotationEvaluationBean);
				}
			}
			//enregistrement des autres info concernant la fiche d'evaluation
			ficheEvaluationJsonBean.setId_employe(new Integer(idEmploye));
			ficheEvaluationJsonBean.setId_planning_evaluation(Employe.getId_planning_evaluation());
			String id_planning_evaluations=Employe.getId_planning_evaluation()+"";
			FicheEvaluationJsonModel ficheJsonModel=new  FicheEvaluationJsonModel();
			boolean result_fich_eval=ficheJsonModel.updateFicheEvalutionJson(idEmploye+"", id_planning_evaluations, ficheEvaluationJsonBean);

		}
		System.out.println("fin de l'export");
	}

	public void onClick$rattrapageCalculIMI() throws InterruptedException, ParseException
	{
		System.out.println("debut rattrapage");
		//vider les table de validation de la compagne
		String id_compagne="2";
		FicheEvaluationJsonModel ficheJsonModel= new  FicheEvaluationJsonModel();
		HashMap<String,String> mapEmployes=ficheJsonModel.getListSalariesPourRattrapage("2");
		ficheJsonModel.viderTableValidationCompagne(id_compagne);
		//lister tous les salaries 

		// lire fiche evaluation d'un salarie
		int k=0;
		for(Iterator i=mapEmployes.keySet().iterator();i.hasNext();){
			String employe=(String) i.next();
			String id_planning_evaluation=mapEmployes.get(employe);
			k++;
			int id_employe=new Integer(employe);
			System.out.println("rattrapage du salarie="+employe +" numero="+k);


			HashMap<String, HashMap<String, ArrayList<Double>>> mapFamilleCompetence=new HashMap<String, HashMap<String, ArrayList<Double>>>();

			FicheEvaluationJsonModel ficheEvaluationJsonModel= new FicheEvaluationJsonModel();

			HashMap <String, ArrayList<FicheEvaluationBean>> mapfamilleFicheEvaluationRattrapage; 
			mapfamilleFicheEvaluationRattrapage=ficheEvaluationJsonModel.getMaFicheEvaluaton(id_employe, mapcodeCompetenceLibelleCompetence, mapidRepCompetence_ApptitudeObservable,String.valueOf(compagne_id));
			HashMap<String, String> listFamilleCodeFamille=ficheEvaluationJsonModel.getlistefamillesCode_famille();
			boolean result_imi_stat=rattrapageIMIMoyenneFamilleutilisantCompetence(mapFamilleCompetence,mapfamilleFicheEvaluationRattrapage,id_planning_evaluation, id_employe+"",id_compagne,listFamilleCodeFamille);
			//		 
			//		 //enregistrement dans la base les stats IMI par competence
			//		 	 
			boolean result_imi_compstat=rattrapageIMIStatParCompetence(mapFamilleCompetence,id_planning_evaluation, id_employe+"");
		}

		System.out.println("fin rattrapage");
		//si la fiche est déjé validée ==> appeler la methode suivante
		//executer les actions de validation
	}

	public boolean rattrapageIMIMoyenneFamilleutilisantCompetence(HashMap<String, HashMap<String, ArrayList<Double>>> mapFamilleCompetence,HashMap <String, ArrayList<FicheEvaluationBean>> mapfamilleFicheEvaluationRattrapage,String id_planning_evaluation, String id_employe, String id_compagne, HashMap <String, String> mapFamille) throws ParseException
	{

		Set <String >listFamille= mapfamilleFicheEvaluationRattrapage.keySet();


		Iterator<String> iteratorFamille=listFamille.iterator();
		int nbFamille=listFamille.size();
		FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
		String requete="";
		Double moyIMI=new Double(0);

		while (iteratorFamille.hasNext())
		{
			String famille=iteratorFamille.next();

			ArrayList<FicheEvaluationBean> listFicheEvaluation=mapfamilleFicheEvaluationRattrapage.get(famille);
			String code_famille=mapFamille.get(famille);
			int nbVal=0;
			Double moyenne=new Double(0);
			for (Iterator<FicheEvaluationBean> iterator = listFicheEvaluation.iterator(); iterator
					.hasNext();) {
				FicheEvaluationBean ficheEvaluationBean = (FicheEvaluationBean) iterator
						.next();

				Double valeur=(double) ficheEvaluationBean.getNiveau_maitrise();

				if(!valeur.isNaN())
				{	 nbVal++;
				moyenne=moyenne+valeur;
				}
				/**************************/
				HashMap<String, String> maprepCompComp= ficheEvaluationModel.getmaprepCompetenceCodeCompetenceRattrapage();
				String code_competence=maprepCompComp.get(ficheEvaluationBean.getId_repertoire_competence());
				if(mapFamilleCompetence.containsKey(famille))
				{


					if(mapFamilleCompetence.get(famille).containsKey(code_competence))
					{
						ArrayList<Double> liste=mapFamilleCompetence.get(famille).get(code_competence);
						liste.add(new Double(valeur));
						mapFamilleCompetence.get(famille).put(code_competence, liste);
					}
					else
					{
						ArrayList<Double> liste=new ArrayList<Double>(); 
						liste.add(new Double(valeur));
						mapFamilleCompetence.get(famille).put(code_competence, liste);
					}
				}
				else
				{

					ArrayList<Double> liste=new ArrayList<Double>(); 
					liste.add(new Double(valeur));
					HashMap<String, ArrayList<Double>> map=new HashMap<String, ArrayList<Double>>();
					map.put(code_competence, liste);
					mapFamilleCompetence.put(famille, map);
				}

				/***********************/


			}




			moyenne=moyenne/nbVal;

			moyIMI=moyIMI+moyenne;
			requete=requete+ " delete from imi_stats where id_compagne=#id_compagne and id_employe=#id_employe and code_famille=#code_famille ; ";
			requete=requete+ " INSERT INTO imi_stats (id_compagne,id_employe,moy_par_famille,code_famille, imi) VALUES (#id_compagne,#id_employe,#moy_par_famille,#code_famille, #imi) ; ";
			requete = requete.replaceAll("#id_compagne", id_compagne);
			requete = requete.replaceAll("#id_employe", id_employe);
			requete = requete.replaceAll("#moy_par_famille", moyenne+"");
			requete = requete.replaceAll("#code_famille", "'"+code_famille+"'");

			//System.out.println("requetes IMI>>>"+requete);


		}

		//mise a jour de la requete avec l'IMI;
		Double calculIMI=moyIMI/nbFamille;

		requete = requete.replaceAll("#imi", "'"+calculIMI+"'");

		return(ficheEvaluationModel.insertImiCompetenceStat(requete));

	}

	public boolean rattrapageIMIStatParCompetence(HashMap<String, HashMap<String , ArrayList<Double>>> mapFamilleCompetence,String id_planning_evaluation, String id_employe)
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
				int nbCompetencecpt=listeCompetence.size();
				int nbCompetence=0;//
				Double moyenne=new Double(0);
				for(int i=0;i<nbCompetencecpt;i++)
				{
					Double valeurCotation=listeCompetence.get(i);
					if(0!=valeurCotation)
					{
						moyenne=moyenne+valeurCotation;
						nbCompetence++;
					}
				}
				moyenne=moyenne/nbCompetence;
				requete=requete+"  delete from IMI_COMPETENCE_STAT where id_compagne=#id_compagne and id_employe=#id_employe and code_famille=#code_famille and code_competence=#code_competence ; ";
				requete=requete+"  INSERT INTO IMI_COMPETENCE_STAT  (id_compagne,id_employe,code_famille,code_competence,moy_competence) VALUES (#id_compagne,#id_employe,#code_famille,#code_competence,#moy_competence) ; ";

				requete = requete.replaceAll("#id_compagne", id_compagne);
				requete = requete.replaceAll("#id_employe", id_employe);
				requete = requete.replaceAll("#code_famille", "'"+code_famille+ "'");
				requete = requete.replaceAll("#code_competence", "'"+code_competence+ "'");
				requete = requete.replaceAll("#moy_competence", moyenne+"");
				// System.out.println("requets IMI_COMPETENCE_STAT>>> "+requete);
			}


		}


		return(ficheEvaluationModel.insertImiCompetenceStat(requete));
	}

	public String getId_planning_eval() {
		return id_planning_eval;
	}

	public void setId_planning_eval(String id_planning_eval) {
		this.id_planning_eval = id_planning_eval;
	}

	public String getId_evalue() {
		return id_evalue;
	}

	public void setId_evalue(String id_evalue) {
		this.id_evalue = id_evalue;
	}

	//modif v3.2 point 2 ajout de la combo direction
	public void onSelect$directionV() throws SQLException
	{

		
		
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		CompteBean compteUtilisateur=applicationSession.getCompteUtilisateur();
		String direction =(String)directionV.getSelectedItem().getLabel();
		if ((compteUtilisateur.getId_profile()==2)||(compteUtilisateur.getId_profile()==1)){


			employeV.getItems().clear();
			poste_travailV.getItems().clear();

			// debut modif point 2 v3.2 05/07/2018
			HashMap<String, HashMap<String,ArrayList<String>>> Mapdirection=mapEmployeEvalueBean.getMapclesdirection();
			 HashMap<String,ArrayList<String>> listedirection= (HashMap) sortByComparator(Mapdirection.get(direction));

			if (listedirection==null  || listedirection.size()==0  ){
				try {
					Messagebox.show("Merci de selectionner une  campagne et/ou une direction ", "Erreur", Messagebox.OK, Messagebox.ERROR);
					return;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			poste_travailV.appendItem("Tous postes de travail");
			for (Entry<String, ArrayList<String>> entry : listedirection.entrySet()) {
				String poste_travail = entry.getKey();
				poste_travailV.appendItem(poste_travail);
				//HashMap<String,EmployesAEvaluerBean>  mapEmp = entry.getValue();
				/* for (Map.Entry<String, EmployesAEvaluerBean> entryEmp : mapEmp.entrySet()) {
				    String nom_employe = entry.getKey();
				    EmployesAEvaluerBean employesAEvaluerBean=entryEmp.getValue();
				    //mapEmploye.put(nom_employe,employesAEvaluerBean);
			    }*/

			}


			//selection du premier item (tous direction)
			poste_travailV.setSelectedIndex(0);
			// fin modif point 2 v3.2 05/07/2018

		}




	}

	public void onSelect$compagneV() throws SQLException
	{

		structureV.setText("");
		FamilleV.getItems().clear();
		employelbV.getItems().clear();

		poste_travailV.getItems().clear();
		employeV.getItems().clear();

		employe.getItems().clear();
		poste_travail.getItems().clear();

		FamilleM.getItems().clear();
		directionV.getItems().clear();


		try{
			compagne_id= Integer.parseInt((String) map_compagne.get((String)compagneV.getSelectedItem().getLabel()));
			loadFicheEval(1,compagne_id);
			
		}catch (Exception e){

			compagne_id=-1;
		}
	}

	public void onSelect$compagneM() throws SQLException
	{

		poste_travailV.getItems().clear();
		employeV.getItems().clear();
		employe.getItems().clear();
		poste_travail.getItems().clear();
		FamilleM.getItems().clear();
		employelbM.getItems().clear();


		try{
			compagne_id= Integer.parseInt((String) map_compagne.get((String)compagneM.getSelectedItem().getLabel()));
			loadFicheEval(1,compagne_id);

		}catch (Exception e){

			compagne_id=-1;
		}
	}

	public void  onClick$AEvaluer() throws ParseException, SQLException, Exception{
		//alert("Ce login existe deja, merci de choisir un autre login");

		employe.getItems().clear();
		poste_travail.getItems().clear();


		FicheEvaluationJsonModel ficheJson=new FicheEvaluationJsonModel();
		compagne_id=ficheJson.getCompagneEnCours();
		loadFicheEval(1,compagne_id);

	}

	public void  onClick$FValide() throws InterruptedException{
		///alert("Ce login existe deja, merci de choisir un autre login");



		compagneV.setSelectedIndex(0);


		try {
			compagne_id= Integer.parseInt((String) map_compagne.get((String)compagneV.getSelectedItem().getLabel()));
		}catch (NumberFormatException e) {

			//Messagebox.show(" Merci de selectionner une campagne d'évaluation", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);

		}

		employelbV.getItems().clear();

	}
	public void  onClick$FicheEvaluation() throws ParseException, SQLException, Exception{

		//reinitialiser les  widget
		poste_travailV.getItems().clear();
		employeV.getItems().clear();
		//employe.getItems().clear();
		//poste_travail.getItems().clear();
		FamilleM.getItems().clear();

		//alert("Ce login existe deja, merci de choisir un autre login");
		if(compagneM.getItemCount()>0){
			compagneM.setSelectedIndex(0);
			compagne_id= Integer.parseInt((String) map_compagne.get((String)compagneM.getSelectedItem().getLabel()));
			loadFicheEval(1,compagne_id);
		}

		//employelbM.getItems().clear();



	}

	public void onClick$exp_word() throws Exception
	{

		//int id_employe=(Integer) employeV.getSelectedItem().getValue();

		EmployesAEvaluerBean employerAEvaluerID=mapEmployeEvalueBean.getMapclesnomEmploye().get(selectedEmployeV);
		int id_employe=employerAEvaluerID.getId_employe();
		ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
		int compagne_id= Integer.parseInt((String) map_compagne.get((String)compagneV.getSelectedItem().getLabel()));

		applicationSession.setId_employe(id_employe);
		applicationSession.setId_compagne(compagne_id);

		String url = "/run?__report=ficheIndividuelle1.rptdesign&formatRapport=doc";
		iframe.setSrc(url);
		iframe.invalidate();
	}

	private static Map sortByComparator(Map unsortMap) {

		List list = new LinkedList(unsortMap.entrySet());

		//sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getKey())
						.compareTo(((Map.Entry) (o2)).getKey());
			}
		});

		//put sorted list into map again
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}	


}