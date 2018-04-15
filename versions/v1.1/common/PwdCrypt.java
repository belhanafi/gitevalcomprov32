package common;

public class PwdCrypt {
	
	
private static final String CLE_PAR_DEFAUT = "Longtemps, je me suis couché de bonne heure.";
    
    private static String sCle = CLE_PAR_DEFAUT;
    
    
    public PwdCrypt(){
    	
    }
    
    /**
     * Permet de modifier la clé par défaut
     * @param pCle : nouvelle clé par défaut
     */
    public static void setCle (String pCle) {
    	if (pCle != null && pCle.length() > 0) {
    		sCle = pCle;
    	} else {
    		sCle = CLE_PAR_DEFAUT;
    	}
    }
    
    
	/**
     * Renvoie une chaine de caractères cryptée
     * @param pChaine : chaine à crypter
     * @return chaine cryptée
     */
    public static String crypter (String pChaine) {
        int indiceDeDebutVariable = (int) (sCle.length() * Math.random());

        String laChaineCryptee = Integer.toHexString(indiceDeDebutVariable);
        int longueurNouvelleChaine = Integer.toHexString(sCle.length()).length();
        int longueurChaineActuelle = laChaineCryptee.length();
        String prefixe = "";
        int longueurPrefixe = longueurNouvelleChaine - longueurChaineActuelle;
        for (int i = 0; i < longueurPrefixe; i++) {
        	prefixe += "0";
		}
        laChaineCryptee = prefixe + laChaineCryptee;

        for (int i = 0; i < pChaine.length(); i++) {
        	int codeAscii = pChaine.charAt(i);
        	int codeAsciiCle = sCle.charAt((i + indiceDeDebutVariable) % sCle.length());
			String filtre = Integer.toHexString(codeAscii ^ codeAsciiCle);
            laChaineCryptee += ("00"+filtre).substring(("00"+filtre).length() - 2);
		}

        return laChaineCryptee;
    }

    /**
     * Renvoie une chaine de caractères décryptée
     * @param pChaine : chaine à décrypter
     * @return chaine décryptée
     */
    public static String decrypter (String pChaine) {
        int indiceDeDebutVariable = Integer.parseInt(pChaine.substring(0, Integer.toHexString(sCle.length()).length()), 16) % sCle.length();
        String laChaineADecrypter = pChaine.substring(Integer.toHexString(sCle.length()).length());

        String laChaineDecryptee = "";

        try {
        	while (laChaineADecrypter.length() >= 2) {
        		int ascii = sCle.charAt(indiceDeDebutVariable % sCle.length());
        		char caractere = (char) (Integer.parseInt(laChaineADecrypter.substring(0, 2), 16) ^ ascii);
                laChaineDecryptee += caractere;
                laChaineADecrypter = laChaineADecrypter.substring(2);
                indiceDeDebutVariable = indiceDeDebutVariable + 1;
			}

        } catch (Exception ex) {
            laChaineDecryptee = "";
        }

        return laChaineDecryptee;
    }
    
    /**
     * 
     */
    
    /*public static void main(String[] args){
    	String motCrypte = crypter("12345678");
    	System.out.println(motCrypte);
    	System.out.println("mot decrypte ="+ decrypter(motCrypte));
    }
*/

}
