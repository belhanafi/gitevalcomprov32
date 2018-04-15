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

import org.zkoss.lang.Strings;
import org.zkoss.util.media.Media;
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

    Textbox codeFormationtb;

    Textbox libelleFormationtb;

    Textbox libelleDiplometb;

    AnnotateDataBinder binder;

    Window win;

    Div divupdown;

    List<Formation> model = new ArrayList<Formation>();

    Formation selected;

    Button okAdd;

    Button effacer;

    Button add;

    Button update;

    Button delete;

    public FormationAction()
    {
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

    private String getSelectedlibelleFormation()
        throws WrongValueException
    {
        String code = libelleFormationtb.getValue();
        if ( Strings.isBlank( code ) )
        {
            throw new WrongValueException( libelleFormationtb, "le libelle Formation ne doit pas etre vide!" );
        }
        return code;
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

    public void processMedia( Media med )
    {

        if ( ( med != null ) && ( med.getName() != null ) )
        {
            String filename = med.getName();

            if ( filename.indexOf( ".xls" ) == -1 )
            {
                alert( filename + " n'est pas un fichier excel" );
            }
            else
            {

                // process the file...
                FormationModel formationModel = new FormationModel();
                if ( filename.endsWith( ".xls" ) )
                {
                    //lecture et upload de fichiers OLE2 Office Documents 
                    List<Formation> liste = formationModel.uploadXLSFile( med.getStreamData() );
                    List<Formation> donneeRejetes;
                    try
                    {
                        HashMap<String, List<Formation>> listeDonnees = formationModel.ChargementDonneedansBdd( liste );
                        donneeRejetes = listeDonnees.get( "supprimer" );
                        liste = null;
                        liste = listeDonnees.get( "inserer" );;

                        //raffrechissement de l'affichage
                        Iterator<Formation> index = liste.iterator();
                        while ( index.hasNext() )
                        {
                            Formation donnee = index.next();
                            model.add( donnee );
                        }

                        binder.loadAll();
                        if ( donneeRejetes.size() != 0 )
                        {
                            String listeRejet = new String( "-->" );
                            //Afficharge de la liste des donn�es rejet�es
                            Iterator<Formation> index1 = donneeRejetes.iterator();
                            while ( index1.hasNext() )
                            {
                                Formation donnee = index1.next();
                                String donneeString = donnee.getCodeFormation() + ";" + donnee.getLibelleFormation()
                                    + ";" + donnee.getLibelleDiplome();
                                listeRejet = listeRejet + System.getProperty( "line.separator" ) + donneeString;//saut de ligne

                            }
                            AfficherFenetreRejet( listeRejet );

                        }
                    }
                    catch ( Exception e )
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else if ( filename.endsWith( ".xlsx" ) )
                {

                    // lecture de fichiers Office 2007+ XML
                    List<Formation> liste = formationModel.uploadXLSXFile( med.getStreamData() );
                    List<Formation> donneeRejetes;
                    try
                    {
                        HashMap<String, List<Formation>> listeDonnees = formationModel.ChargementDonneedansBdd( liste );
                        donneeRejetes = listeDonnees.get( "supprimer" );
                        liste = null;
                        liste = listeDonnees.get( "inserer" );;

                        //raffrechissement de l'affichage
                        Iterator<Formation> index = liste.iterator();
                        while ( index.hasNext() )
                        {
                            Formation donnee = index.next();
                            model.add( donnee );
                        }

                        binder.loadAll();
                        if ( donneeRejetes.size() != 0 )
                        {
                            String listeRejet = new String( "-->" );
                            //Afficharge de la liste des donn�es rejet�es
                            Iterator<Formation> index1 = donneeRejetes.iterator();
                            while ( index1.hasNext() )
                            {
                                Formation donnee = index1.next();
                                String donneeString = donnee.getCodeFormation() + ";" + donnee.getLibelleFormation()
                                    + ";" + donnee.getLibelleDiplome();
                                listeRejet = listeRejet + System.getProperty( "line.separator" ) + donneeString;//saut de ligne

                            }
                            AfficherFenetreRejet( listeRejet );

                        }
                    }
                    catch ( Exception e )
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        }
    }

    public void onClick$upload()
    {
        Executions.getCurrent().getDesktop().setAttribute( "org.zkoss.zul.Fileupload.target", divupdown );

        try
        {
            Fileupload fichierupload = new Fileupload();

            Media me = fichierupload.get( "Merci de selectionner le fichier qui doit etre charge",
                                          "Chargement de fichier", true );

            processMedia( me );
        }
        catch ( InterruptedException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void onClick$download()
    {
        //chargement du contenu de la table formation et creation du fichier excel
        FormationModel formationModel = new FormationModel();

        formationModel.downloadFormationDataToXls();

    }

    public void AfficherFenetreRejet( String listeRejet )
    {
        Map<String, String> listDonne = new HashMap<String, String>();
        listDonne.put( "rejet", listeRejet );

        final Window win = (Window) Executions.createComponents( "../pages/REJDATA.zul", self, listDonne );
        // We send a message to the Controller of the popup that it works in popup-mode.
        win.setAttribute( "popup", true );

        //decoratePopup(win);
        try
        {
            win.doModal();

        }
        catch ( InterruptedException ex )
        {
            ex.printStackTrace();
        }
        catch ( SuspendNotAllowedException ex )
        {
            ex.printStackTrace();
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
        addedData.setLibelleFormation( getSelectedlibelleFormation() );

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
            alert( "No row selected" );
            return;
        }
        selected.setCodeFormation( getSelectedcodeFormation() );
        selected.setLibelleFormation( getSelectedlibelleFormation() );
        selected.setLibelleDiplome( getSelectedlibelleDiplome() );

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
        libelleFormationtb.setText( "" );
        libelleDiplometb.setText( "" );
    }

}
