package administration.bean;

public class SelCliDBNameBean {
	
	private int datbase_id;
	private String nombase;
	
	public SelCliDBNameBean(){
		
	}
	
	
	public SelCliDBNameBean(int database_id,String nombase){
		this.datbase_id=database_id;
		this.nombase=nombase;
		
	}
	
	public int getDatbase_id() {
		return datbase_id;
	}
	public void setDatbase_id(int datbase_id) {
		this.datbase_id = datbase_id;
	}
	public String getNombase() {
		return nombase;
	}
	public void setNombase(String nombase) {
		this.nombase = nombase;
	}
	
	
	

}
