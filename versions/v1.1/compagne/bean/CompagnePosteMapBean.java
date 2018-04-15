package compagne.bean;

public class CompagnePosteMapBean {
	
	private int map_stat;
	private String code_poste;
	private String libelle_poste;
	
	
	
	
	public CompagnePosteMapBean() {
		super();
	}
	public CompagnePosteMapBean(int map_stat, String code_poste,
			String libelle_poste) {
		super();
		this.map_stat = map_stat;
		this.code_poste = code_poste;
		this.libelle_poste = libelle_poste;
	}
	public int getMap_stat() {
		return map_stat;
	}
	public void setMap_stat(int map_stat) {
		this.map_stat = map_stat;
	}
	public String getCode_poste() {
		return code_poste;
	}
	public void setCode_poste(String code_poste) {
		this.code_poste = code_poste;
	}
	public String getLibelle_poste() {
		return libelle_poste;
	}
	public void setLibelle_poste(String libelle_poste) {
		this.libelle_poste = libelle_poste;
	}
	
	
	
	
	
	
	
	
}
