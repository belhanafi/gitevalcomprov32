package compagne.bean;

import java.util.ArrayList;

public class FicheEvaluationJsonBean {
	
	private ArrayList <CotationEvaluationBean> listCotationEvaluationBean=null;
	private int id_employe;
	private boolean ficheValide=false;
	
	private int id_planning_evaluation;
	private String date_evaluation;
	private String compagne_type;
	
	public void AjouterCotation(CotationEvaluationBean cotationEvaluationBean)
	{
		if(listCotationEvaluationBean==null)
			listCotationEvaluationBean=new ArrayList <CotationEvaluationBean>();
		listCotationEvaluationBean.add(cotationEvaluationBean);
	}
	public ArrayList<CotationEvaluationBean> getListCotationEvaluationBean() {
		return listCotationEvaluationBean;
	}
	public void setListCotationEvaluationBean(
			ArrayList<CotationEvaluationBean> listCotationEvaluationBean) {
		this.listCotationEvaluationBean = listCotationEvaluationBean;
	}
	
	public int getId_employe() {
		return id_employe;
	}
	public void setId_employe(int id_employe) {
		this.id_employe = id_employe;
	}
	public boolean isFicheValide() {
		return ficheValide;
	}
	public void setFicheValide(boolean ficheValide) {
		this.ficheValide = ficheValide;
	}
	public int getId_planning_evaluation() {
		return id_planning_evaluation;
	}
	public void setId_planning_evaluation(int id_planning_evaluation) {
		this.id_planning_evaluation = id_planning_evaluation;
	}
	public String getDate_evaluation() {
		return date_evaluation;
	}
	public void setDate_evaluation(String date_evaluation) {
		this.date_evaluation = date_evaluation;
	}
	public String getCompagne_type() {
		return compagne_type;
	}
	public void setCompagne_type(String compagne_type) {
		this.compagne_type = compagne_type;
	}
	
	
}
