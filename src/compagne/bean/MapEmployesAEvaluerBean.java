package compagne.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class MapEmployesAEvaluerBean {
	
	HashMap<String, EmployesAEvaluerBean> MapclesnomEmploye=new HashMap <String, EmployesAEvaluerBean>();
	HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=new HashMap<String, HashMap<String, EmployesAEvaluerBean>>();
	//modif point 2 v3.2 05/07/2018
	HashMap<String, ArrayList< HashMap<String,String>>> Mapclesdirection=new  HashMap<String, ArrayList< HashMap<String,String>>>();

	
	public HashMap<String, EmployesAEvaluerBean> getMapclesnomEmploye() {
		return MapclesnomEmploye;
	}
	public void setMapclesnomEmploye(
			HashMap<String, EmployesAEvaluerBean> mapclesnomEmploye) {
		MapclesnomEmploye = mapclesnomEmploye;
	}
	public HashMap<String, HashMap<String, EmployesAEvaluerBean>> getMapclesposte() {
		return Mapclesposte;
	}
	public void setMapclesposte(HashMap<String, HashMap<String, EmployesAEvaluerBean>> mapclesposte) {
		Mapclesposte = mapclesposte;
	}
	public HashMap<String, ArrayList< HashMap<String,String>>> getMapclesdirection() {
		return Mapclesdirection;
	}
	public void setMapclesdirection(
			HashMap<String, ArrayList< HashMap<String,String>>> mapclesdirection) {
		Mapclesdirection = mapclesdirection;
	}
	
	
	
	
}
