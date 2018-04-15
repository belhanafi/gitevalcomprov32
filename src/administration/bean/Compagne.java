/**
 * 
 */
package administration.bean;

import java.sql.Date;

import common.AbstractEnterpriseObject;

/**
 * @author nbehlouli
 *
 */
public class Compagne
extends AbstractEnterpriseObject
{

	private static final long serialVersionUID = 1L;

	private int idCompagne;
	private String dateDebut;
	private String dateFin;
	private String libelleCompagne;
	private String compagneType;
	private int id_compagneType;
	private String causeRejet;





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

	
	public String getDateDebut()
	{
		return dateDebut;
	}

	public void setDateDebut( String dateDebut )
	{
		this.dateDebut = dateDebut;
	}

	public String getDateFin()
	{
		return dateFin;
	}

	public void setDateFin( String dateFin )
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




	public String getCompagneType()
	{
		return compagneType;
	}

	public void setCompagneType( String compagneType )
	{
		this.compagneType = compagneType;
	}

	public int getId_compagneType() {
		return id_compagneType;
	}

	public void setId_compagneType(int id_compagneType) {
		this.id_compagneType = id_compagneType;
	}

	public String getCauseRejet() {
		return causeRejet;
	}
	public void setCauseRejet(String causeRejet) {
		this.causeRejet = causeRejet;
	}

}
