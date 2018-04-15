package kpi.bean;

public class TauxCouvertureCompagneBean {
	
	private int nbemploye;
	private int nbfichevalide;
	private Double pourcentage;
	private String nomvague;
	
	
	
	
	public TauxCouvertureCompagneBean() {
		super();
	}
	public TauxCouvertureCompagneBean(int nbemploye, int nbfichevalide,
			Double pourcentage, String nomvague) {
		super();
		this.nbemploye = nbemploye;
		this.nbfichevalide = nbfichevalide;
		this.pourcentage = pourcentage;
		this.nomvague = nomvague;
	}
	public int getNbemploye() {
		return nbemploye;
	}
	public void setNbemploye(int nbemploye) {
		this.nbemploye = nbemploye;
	}
	public int getNbfichevalide() {
		return nbfichevalide;
	}
	public void setNbfichevalide(int nbfichevalide) {
		this.nbfichevalide = nbfichevalide;
	}
	public Double getPourcentage() {
		return pourcentage;
	}
	public void setPourcentage(Double pourcentage) {
		this.pourcentage = pourcentage;
	}
	public String getNomvague() {
		return nomvague;
	}
	public void setNomvague(String nomvague) {
		this.nomvague = nomvague;
	}
	
	
	
	
	
	

}
