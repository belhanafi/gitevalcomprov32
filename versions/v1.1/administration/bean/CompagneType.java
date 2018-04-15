/**
 * 
 */
package administration.bean;

import common.AbstractEnterpriseObject;

/**
 * @author FTERZI
 *
 */
public class CompagneType
    extends AbstractEnterpriseObject
{

    private static final long serialVersionUID = 1L;

    private int id;

    private String libelle;

    public CompagneType()
    {
        // TODO Auto-generated constructor stub
    }

    public CompagneType( int id, String libelle )
    {
        this.id = id;
        this.libelle = libelle;
    }

    public CompagneType( int id )
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getLibelle()
    {
        return libelle;
    }

    public void setLibelle( String libelle )
    {
        this.libelle = libelle;
    }

}
