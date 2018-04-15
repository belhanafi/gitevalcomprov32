package common;

public class StringUtils {
	
	public String removeString(String chaine){
		String chaine_trt="";
		if(chaine==null)
			return "";
		else		
			if(!chaine.equals(""))
			{
				chaine_trt=chaine;
	           chaine_trt=chaine_trt.replaceAll("’"," ");
	           chaine_trt=chaine_trt.replaceAll("'"," ");
			}
		return chaine_trt;
	}


}
