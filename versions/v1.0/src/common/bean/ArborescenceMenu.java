package common.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ArborescenceMenu {
	
	private List<EcranBean> listeEcranBean=null;;
	private HashMap<String, List<EcranBean>> structureMenu=null;
	private List<String> listLibelleMenu=null;
	
	/**
	 * Ce constructeur est appelé lors du chargement initial de l'application
	 * @param listeEcranBean
	 */
	public ArborescenceMenu(List<EcranBean> listeEcranBean, List<String> listLibelleMenu)
	{
		this.listLibelleMenu=new ArrayList<String>();
		this.listLibelleMenu=listLibelleMenu;
		this.listeEcranBean=new ArrayList <EcranBean>();
		this.listeEcranBean=listeEcranBean;
		
	}

	public ArborescenceMenu()
	{
		structureMenu=new HashMap <String, List<EcranBean>>();

	}
	
	public HashMap<String, List<EcranBean>> getArborescenceMenu()
	{
		if(structureMenu==null)
			structureMenu=new HashMap <String, List<EcranBean>>();
		if(listLibelleMenu!=null)
		{
			Iterator<String> iterator= listLibelleMenu.iterator();
			while(iterator.hasNext())
			{
				String libelleMenu=(String)iterator.next();
				
				List <EcranBean> listSousMenu=new ArrayList<EcranBean>();
				Iterator<EcranBean> it= listeEcranBean.iterator();
				while(it.hasNext())
				{
					EcranBean ecranBean=(EcranBean)it.next();
					if(libelleMenu.equals(ecranBean.getLibele_menu()))
					{
						listSousMenu.add(ecranBean);
					}
				}
				structureMenu.put(libelleMenu, listSousMenu);
			}
		}
		
		
		return structureMenu;
	}
//	public HashMap getmenuArborescence()
//	{
//		List<String> listeAdministration=new ArrayList<String>();
//		listeAdministration.add(new String("Administration des bases de données"));
//		listeAdministration.add(new String("Croisement base de données-client"));
//		listeAdministration.add(new String("Gestion des profiles de l'application"));
//		listeAdministration.add(new String("Création des logins utilisateurs"));
//		listeAdministration.add(new String("Sélection de la base de données client"));
//		listeAdministration.add(new String("Gestion des droits d'accès à l'application"));
//		listeAdministration.add(new String("Création des structures d'une entreprise"));
//		listeAdministration.add(new String("Création des formations-diplomes"));
//		listeAdministration.add(new String("Création des employés"));
//		listeAdministration.add(new String("Création du répertoire de compétences"));
//		listeAdministration.add(new String("Fiche de poste de travail"));
//		listeAdministration.add(new String("Association compétence-poste de travail"));
//		listeAdministration.add(new String("Définition de la cotation"));
//		listeAdministration.add(new String("Définition IMi vs stratégie"));
//
//		
//		structureMenu.put("Administration",listeAdministration );
//		
//		List<String> listeEvaluation=new ArrayList<String>();
//		listeEvaluation.add(new String("Création des compagnes d'evaluation"));
//		listeEvaluation.add(new String("Fiche d'évaluation"));
//		listeEvaluation.add(new String("Suivi de la compagbe d'évaluation"));
//		listeEvaluation.add(new String("Afficage des résultats de la compagne d'évaluation"));
//		listeEvaluation.add(new String("Répartition de la population à évaluer"));
//		structureMenu.put("Evaluation", listeEvaluation);
//		
//		List<String> listeStatistique=new ArrayList<String>();
//		listeStatistique.add(new String("Statistiques sur la cotation"));
//		listeStatistique.add(new String("Statistiques sur l'IMG"));
//
//		structureMenu.put("Statistiques", listeStatistique);
//		
//		
//		return structureMenu;
//	}

}

