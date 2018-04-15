/**
 * 
 */
package administration.bean;

import java.util.Date;

import common.AbstractEnterpriseObject;

/**
 * @author FTERZI
 *
 */
public class Compagne
    extends AbstractEnterpriseObject
{

    private static final long serialVersionUID = 1L;

    private int idCompagne;
    private Employe employe;
    private Date dateDebut;
    private Date dateFin;
    private String libelleCompagne;
    private StructureEntrepriseBean structure;
    private CompagneType compagneType;

    public Compagne()
    {
        // TODO Auto-generated constructor stub
    }

    public int getIdCompagne()
    {
        return idCompagne;
    }

    public void setIdCompagne( int idCompagne )
    {
        this.idCompagne = idCompagne;
    }

    public Employe getEmploye()
    {
        return employe;
    }

    public void setEmploye( Employe employe )
    {
        this.employe = employe;
    }

    public Date getDateDebut()
    {
        return dateDebut;
    }

    public void setDateDebut( Date dateDebut )
    {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin()
    {
        return dateFin;
    }

    public void setDateFin( Date dateFin )
    {
        this.dateFin = dateFin;
    }

    public String getLibelleCompagne()
    {
        return libelleCompagne;
    }

    public void setLibelleCompagne( String libelleCompagne )
    {
        this.libelleCompagne = libelleCompagne;
    }

    public StructureEntrepriseBean getStructure()
    {
        return structure;
    }

    public void setStructure( StructureEntrepriseBean structure )
    {
        this.structure = structure;
    }

    public CompagneType getCompagneType()
    {
        return compagneType;
    }

    public void setCompagneType( CompagneType compagneType )
    {
        this.compagneType = compagneType;
    }

}
