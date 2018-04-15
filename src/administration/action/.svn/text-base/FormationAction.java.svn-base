/**
 * 
 */
package administration.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.lang.Strings;
import org.zkoss.util.media.Media;
import org.zkoss.zk.au.out.AuClearWrongValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import administration.bean.AdministrationLoginBean;
import administration.bean.Formation;
import administration.bean.StructureEntrepriseBean;
import administration.model.AdministrationLoginModel;
import administration.model.FormationModel;
import administration.model.StructureEntrepriseModel;

/**
 * @author FTERZI
 *
 */
public class FormationAction
    extends GenericForwardComposer
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Listbox formationlb;
    Listbox libelleFormationlb;
    Textbox codeFormationtb;
    Textbox libelleFormationtb;
    Textbox libelleDiplometb;
    AnnotateDataBinder binder;
    private String lbl_formation;
    
	Window win;
    Div divupdown;
    List<Formation> model = new ArrayList<Formation>();
    Formation selected;
    Button okAdd;
    Button effacer;
    Button add;
    Button update;
    Button delete;
    Map map_formation=null;

    public FormationAction()
    {
    }
    
    public String getLbl_formation() {
		return lbl_formation;
	}



	public void setLbl_formation(String lbl_formation) {
		this.lbl_formation = lbl_formation;
	}

    

    @SuppressWarnings("deprecation")
    public void doAfterCompose( Component comp )
        throws Exception
    {
        super.doAfterCompose( comp );

        // creation formation
        FormationModel formationModel = new FormationModel();
        model = formationModel.getAllFormations();

        comp.setVariable( comp.getId() + "Ctrl", this, true );
        okAdd.setVisible( false );
        effacer.setVisible( false );
        codeFormationtb.setDisabled(true);
        
        map_formation=formationModel.getListFormation();
		Set set = (map_formation).entrySet(); 
		Iterator i = set.iterator();
		
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			libelleFormationlb.appendItem((String) me.getKey(),(String) me.getKey());
			}

        binder = new AnnotateDataBinder( comp );

        if ( model.size() != 0 )
            selected = model.get( 0 );

        if ( formationlb.getItemCount() != 0 )
            formationlb.setSelectedIndex( 0 );

        binder.loadAll();

    }

    public List<Formation> getModel()
    {
        return model;
    }

    public Formation getSelected()
    {
        return selected;
    }

    public void setSelected( Formation selected )
    {
        this.selected = selected;
    }

    private String getSelectedcodeFormation()
        throws WrongValueException
    {
        String code = codeFormationtb.getValue();
        if ( Strings.isBlank( code ) )
        {
            throw new WrongValueException( codeFormationtb, "le Code Formation ne doit pas etre vide!" );
        }
        return code;
    }

    private String getSelectedlibelleDiplome()
        throws WrongValueException
    {
        String code = libelleDiplometb.getValue();
        if ( Strings.isBlank( code ) )
        {
            throw new WrongValueException( libelleDiplometb, "le libelle diplome ne doit pas etre vide!" );
        }
        return code;
    }

   /* private String getSelectedlibelleFormation()
        throws WrongValueException
    {
        String code = libelleFormationtb.getValue();
        if ( Strings.isBlank( code ) )
        {
            throw new WrongValueException( libelleFormationtb, "le libelle Formation ne doit pas etre vide!" );
        }
        return code;
    }*/
    
    private int getSelectedFormation() throws WrongValueException {
		
		int name=(Integer) map_formation.get((String)libelleFormationlb.getSelectedItem().getLabel());
		setLbl_formation((String)libelleFormationlb.getSelectedItem().getLabel());
		if (name== 0) {
			throw new WrongValueException(libelleFormationlb, "Merci de saisir une formation!");
		}
		return name;
	}
    

    public void onClick$delete()
        throws InterruptedException
    {

        if ( selected == null )
        {
            alert( "No row selected" );
            return;
        }
        FormationModel formationModel = new FormationModel();
        //suppression de la donnee supprimee de la base de donnee
        formationModel.delFormation( selected );

        if ( Messagebox.show( "Voulez vous supprimer cette formation?", "Prompt", Messagebox.YES | Messagebox.NO,
                              Messagebox.QUESTION ) == Messagebox.YES )
        {
            model.remove( selected );
            selected = null;
            binder.loadAll();
            return;
        }

        else
        {
            return;
        }

    }

   

    public void onClick$add()
        throws WrongValueException, ParseException, SQLException
    {

        clearFields();
        FormationModel admini_model=new FormationModel();

        okAdd.setVisible( true );
        effacer.setVisible( true );
        add.setVisible( false );
        update.setVisible( false );
        delete.setVisible( false );
        codeFormationtb.setValue(admini_model.getNextCode("F",admini_model.getMaxKeyCode()));
        codeFormationtb.setDisabled(true);

    }

    public void onClick$okAdd()
        throws WrongValueException, ParseException, InterruptedException
    {

        Formation addedData = new Formation();

        addedData.setCodeFormation( getSelectedcodeFormation() );
        addedData.setLibelleDiplome( getSelectedlibelleDiplome() );
        addedData.setNiv_for_id( getSelectedFormation() );
        addedData.setLibelleFormation(getLbl_formation());

        //controle d'integrite 
        FormationModel formationModel = new FormationModel();
        Boolean donneeValide = formationModel.controleIntegrite( addedData );
        if ( donneeValide )
        {
            //insertion de la donnee ajoutee dans la base de donnee
            boolean donneeAjoute = formationModel.addFormation( addedData );
            // raffrechissemet de l'affichage
            if ( donneeAjoute )
            {
                model.add( addedData );
                selected = addedData;
                binder.loadAll();
            }
        }

        okAdd.setVisible( false );
        effacer.setVisible( false );
        add.setVisible( true );
        update.setVisible( true );
        delete.setVisible( true );

    }

    public void onClick$update()
        throws WrongValueException, ParseException, InterruptedException
    {
        if ( selected == null )
        {
            alert( "Aucune ligne selectionnée" );
            return;
        }
        selected.setCodeFormation( getSelectedcodeFormation() );
        selected.setNiv_for_id( getSelectedFormation() );
        selected.setLibelleDiplome( getSelectedlibelleDiplome() );
        selected.setLibelleFormation(getLbl_formation());

        //controle d'integrite 
        FormationModel formationModel = new FormationModel();
        Boolean donneeValide = formationModel.controleIntegrite( selected );
        if ( donneeValide )
        {
            //insertion de la donnee ajoutee dans la base de donnee
            boolean donneeAjoute = formationModel.updFormation( selected );
            // raffrechissemet de l'affichage
            if ( donneeAjoute )
            {
                binder.loadAll();
            }
        }

    }

    public void onClick$effacer()
    {

        clearFields();
        okAdd.setVisible( false );
        add.setVisible( true );
        update.setVisible( true );
        delete.setVisible( true );
    	formationlb.setSelectedIndex(0);
		binder.loadAll();

    }

    public void clearFields()
    {
        codeFormationtb.setText( "" );
        //libelleFormationtb.setText( "" );
        libelleDiplometb.setText( "" );
    }
    
    public void onSelect$formationlb() {
		closeErrorBox(new Component[] { codeFormationtb, libelleFormationlb,libelleDiplometb});
	}
	
	
	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}
	

}
