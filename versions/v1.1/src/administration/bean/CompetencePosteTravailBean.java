package administration.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class CompetencePosteTravailBean {
	
	private HashMap <String,HashMap<String, HashMap<String, ArrayList<String>> >> listefamilles;

	private ArrayList <String >posteTravail;

	
	private HashMap <String, String > mapCodeCompetence;
	private HashMap <String, String > mapCodePoste;
	
	public HashMap<String, String> getMapCodePoste() {
		return mapCodePoste;
	}

	public void setMapCodePoste(HashMap<String, String> mapCodePoste) {
		this.mapCodePoste = mapCodePoste;
	}

	public HashMap<String, String> getMapCodeCompetence() {
		return mapCodeCompetence;
	}

	public void setMapCodeCompetence(HashMap<String, String> mapCodeCompetence) {
		this.mapCodeCompetence = mapCodeCompetence;
	}

	public HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> getListefamilles() {
		return listefamilles;
	}

	public void setListefamilles(
			HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> listefamilles) {
		this.listefamilles = listefamilles;
	}

	public ArrayList<String> getPosteTravail() {
		return posteTravail;
	}

	public void setPosteTravail(ArrayList<String> posteTravail) {
		this.posteTravail = posteTravail;
	}
	

}
