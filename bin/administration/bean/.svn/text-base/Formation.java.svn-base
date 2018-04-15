/**
 * 
 */
package administration.bean;

import common.AbstractEnterpriseObject;

/**
 * @author FTERZI
 *
 */
public class Formation
    extends AbstractEnterpriseObject
    implements Comparable<Object>
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String codeFormation;

    private String libelleFormation;

    private String libelleDiplome;
    
	private int niv_for_id;

    /**
     * 
     */
    public Formation()
    {
        // TODO Auto-generated constructor stub
    }

    public Formation( String codeFormation, String libelleFormation, String libelleDiplome )
    {
        super();
        this.codeFormation = codeFormation;
        this.libelleFormation = libelleFormation;
        this.libelleDiplome = libelleDiplome;
    }

    public String getCodeFormation()
    {
        return codeFormation;
    }

    public void setCodeFormation( String codeFormation )
    {
        this.codeFormation = codeFormation;
    }

    public String getLibelleFormation()
    {
        return libelleFormation;
    }

    public void setLibelleFormation( String libelleFormation )
    {
        this.libelleFormation = libelleFormation;
    }

    public String getLibelleDiplome()
    {
        return libelleDiplome;
    }

    public void setLibelleDiplome( String libelleDiplome )
    {
        this.libelleDiplome = libelleDiplome;
    }
    
    public int getNiv_for_id() {
		return niv_for_id;
	}

	public void setNiv_for_id(int niv_for_id) {
		this.niv_for_id = niv_for_id;
	}

    @Override
    public int compareTo( Object o )
    {
        Formation f = (Formation) o;

        if ( codeFormation.equals( f.codeFormation ) )
        {
            if ( libelleFormation.equals( f.libelleFormation ) )
            {
                return libelleDiplome.compareTo( f.libelleDiplome );
            }
            else
            {
                return libelleFormation.compareTo( f.libelleFormation );
            }

        }

        return codeFormation.compareTo( f.codeFormation );
    }
}
