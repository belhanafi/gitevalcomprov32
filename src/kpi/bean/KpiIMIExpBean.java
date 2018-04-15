package kpi.bean;

public class KpiIMIExpBean {
	
		
		private String trancheExp;
		private String niveauMaitrise;
		private int pourcentage;
		
		
		public KpiIMIExpBean(String trancheExp, String niveauMaitrise,
				int pourcentage) {
			super();
			this.trancheExp = trancheExp;
			this.niveauMaitrise = niveauMaitrise;
			this.pourcentage = pourcentage;
		}
		public KpiIMIExpBean() {
			// TODO Auto-generated constructor stub
		}
		public String getTrancheExp() {
			return trancheExp;
		}
		public void setTrancheExp(String trancheExp) {
			this.trancheExp = trancheExp;
		}
		public String getNiveauMaitrise() {
			return niveauMaitrise;
		}
		public void setNiveauMaitrise(String niveauMaitrise) {
			this.niveauMaitrise = niveauMaitrise;
		}
		public int getPourcentage() {
			return pourcentage;
		}
		public void setPourcentage(int pourcentage) {
			this.pourcentage = pourcentage;
		}
		
	}



