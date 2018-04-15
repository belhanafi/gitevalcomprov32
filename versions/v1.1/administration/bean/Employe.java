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
public class Employe
    extends AbstractEnterpriseObject
{

    private static final long serialVersionUID = 1L;

    private int id;

    private String nom;

    private String prenom;

    private Date dateNaissance;

    private boolean rattacheDG;

    private Date dateRecrutement;

    private Formation formation;

    private String codePoste;

    private String email;

    private boolean evaluateur;

    private boolean responsableService;

    private boolean responsableDirection;

    private boolean responsableDivision;

    private boolean responsableDepartement;

    private boolean responsableUnite;

    private boolean responsableSection;

    private boolean responsableRH;

    private StructureEntrepriseBean structure;

    public Employe()
    {
        // TODO Auto-generated constructor stub
    }

    public Employe( int id )
    {
        // TODO Auto-generated constructor stub
        this.id = id;
    }

    public Employe( String nom, String prenom )
    {
        // TODO Auto-generated constructor stub
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getNom()
    {
        return nom;
    }

    public void setNom( String nom )
    {
        this.nom = nom;
    }

    public String getPrenom()
    {
        return prenom;
    }

    public void setPrenom( String prenom )
    {
        this.prenom = prenom;
    }

    public Date getDateNaissance()
    {
        return dateNaissance;
    }

    public void setDateNaissance( Date dateNaissance )
    {
        this.dateNaissance = dateNaissance;
    }

    public boolean isRattacheDG()
    {
        return rattacheDG;
    }

    public void setRattacheDG( boolean rattacheDG )
    {
        this.rattacheDG = rattacheDG;
    }

    public Date getDateRecrutement()
    {
        return dateRecrutement;
    }

    public void setDateRecrutement( Date dateRecrutement )
    {
        this.dateRecrutement = dateRecrutement;
    }

    public Formation getFormation()
    {
        return formation;
    }

    public void setFormation( Formation formation )
    {
        this.formation = formation;
    }

    public String getCodePoste()
    {
        return codePoste;
    }

    public void setCodePoste( String codePoste )
    {
        this.codePoste = codePoste;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public boolean isEvaluateur()
    {
        return evaluateur;
    }

    public void setEvaluateur( boolean evaluateur )
    {
        this.evaluateur = evaluateur;
    }

    public boolean isResponsableService()
    {
        return responsableService;
    }

    public void setResponsableService( boolean responsableService )
    {
        this.responsableService = responsableService;
    }

    public boolean isResponsableDirection()
    {
        return responsableDirection;
    }

    public void setResponsableDirection( boolean responsableDirection )
    {
        this.responsableDirection = responsableDirection;
    }

    public boolean isResponsableDivision()
    {
        return responsableDivision;
    }

    public void setResponsableDivision( boolean responsableDivision )
    {
        this.responsableDivision = responsableDivision;
    }

    public boolean isResponsableDepartement()
    {
        return responsableDepartement;
    }

    public void setResponsableDepartement( boolean responsableDepartement )
    {
        this.responsableDepartement = responsableDepartement;
    }

    public boolean isResponsableUnite()
    {
        return responsableUnite;
    }

    public void setResponsableUnite( boolean responsableUnite )
    {
        this.responsableUnite = responsableUnite;
    }

    public boolean isResponsableSection()
    {
        return responsableSection;
    }

    public void setResponsableSection( boolean responsableSection )
    {
        this.responsableSection = responsableSection;
    }

    public boolean isResponsableRH()
    {
        return responsableRH;
    }

    public void setResponsableRH( boolean responsableRH )
    {
        this.responsableRH = responsableRH;
    }

    public StructureEntrepriseBean getStructure()
    {
        return structure;
    }

    public void setStructure( StructureEntrepriseBean structure )
    {
        this.structure = structure;
    }

}
