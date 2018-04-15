/**
 * 
 */
package administration.action;

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

import administration.bean.Compagne;
import administration.bean.StructureEntrepriseBean;
import administration.model.CompagneModel;
import administration.model.FormationModel;

/**
 * @author FTERZI
 *
 */
public class CompagneAction
    extends GenericForwardComposer
{

    private static final long serialVersionUID = 1L;

    Listbox compagnelb;

    Textbox idCompagnetb;

    Textbox codeStructuretb;

    Textbox libelleCompagnetb;

    Textbox dateDebuttb;

    Textbox dateFintb;

    Textbox idEmployetb;

    Textbox idCompagneTypetb;

    AnnotateDataBinder binder;

    Window win;

    Div divupdown;

    List<Compagne> model = new ArrayList<Compagne>();

    Compagne selected;

    Button okAdd;

    Button effacer;

    Button add;

    Button update;

    Button delete;

    @SuppressWarnings("deprecation")
    public void doAfterCompose( Component comp )
        throws Exception
    {
        super.doAfterCompose( comp );

        // creation formation
        CompagneModel compagneModel = new CompagneModel();
        model = compagneModel.getAllCompagnes();
        
        comp.setVariable( comp.getId() + "Ctrl", this, true );
        okAdd.setVisible( false );
        effacer.setVisible( false );

        binder = new AnnotateDataBinder( comp );

        if ( model.size() != 0 )
            selected = model.get( 0 );

        if ( compagnelb.getItemCount() != 0 )
            compagnelb.setSelectedIndex( 0 );

        binder.loadAll();

    }

    public List<Compagne> getModel()
    {
        return model;
    }

    public Compagne getSelected()
    {
        return selected;
    }

    public void setSelected( Compagne selected )
    {
        this.selected = selected;
    }

    private int getSelectedIdCompagne()
        throws WrongValueException
    {
        return Integer.valueOf( idCompagnetb.getValue() );
    }

    private String getSelectedLibelleCompagne()
        throws WrongValueException
    {
        String code = libelleCompagnetb.getValue();
        if ( Strings.isBlank( code ) )
        {
            throw new WrongValueException( libelleCompagnetb, "le libelle compagne ne doit pas etre vide!" );
        }
        return code;
    }

    private String getSelectedCodeStructure()
        throws WrongValueException
    {
        String code = codeStructuretb.getValue();
        if ( Strings.isBlank( code ) )
        {
            throw new WrongValueException( codeStructuretb, "le code structure ne doit pas etre vide!" );
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
        CompagneModel compagneModel = new CompagneModel();
        //suppression de la donnee supprimee de la base de donnee
        compagneModel.delCompagne( selected );

        if ( Messagebox.show( "Voulez vous supprimer cette compagne?", "Prompt", Messagebox.YES | Messagebox.NO,
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
                CompagneModel compagneModel = new CompagneModel();
                if ( filename.endsWith( ".xls" ) )
                {
                    //lecture et upload de fichiers OLE2 Office Documents 
                    List<Compagne> liste = compagneModel.uploadXLSFile( med.getStreamData() );
                    List<Compagne> donneeRejetes;
                    try
                    {
                        HashMap<String, List<Compagne>> listeDonnees = compagneModel.ChargementDonneedansBdd( liste );
                        donneeRejetes = listeDonnees.get( "supprimer" );
                        liste = null;
                        liste = listeDonnees.get( "inserer" );;

                        //raffrechissement de l'affichage
                        Iterator<Compagne> index = liste.iterator();
                        while ( index.hasNext() )
                        {
                            Compagne donnee = index.next();
                            model.add( donnee );
                        }

                        binder.loadAll();
                        if ( donneeRejetes.size() != 0 )
                        {
                            String listeRejet = new String( "-->" );
                            //Afficharge de la liste des donn�es rejet�es
                            Iterator<Compagne> index1 = donneeRejetes.iterator();
                            while ( index1.hasNext() )
                            {
                                Compagne donnee = index1.next();
                                String donneeString = donnee.getIdCompagne() + ";" + donnee.getLibelleCompagne() + ";"
                                    + donnee.getStructure().getCodestructure();
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
                    List<Compagne> liste = compagneModel.uploadXLSXFile( med.getStreamData() );
                    List<Compagne> donneeRejetes;
                    try
                    {
                        HashMap<String, List<Compagne>> listeDonnees = compagneModel.ChargementDonneedansBdd( liste );
                        donneeRejetes = listeDonnees.get( "supprimer" );
                        liste = null;
                        liste = listeDonnees.get( "inserer" );;

                        //raffrechissement de l'affichage
                        Iterator<Compagne> index = liste.iterator();
                        while ( index.hasNext() )
                        {
                            Compagne donnee = index.next();
                            model.add( donnee );
                        }

                        binder.loadAll();
                        if ( donneeRejetes.size() != 0 )
                        {
                            String listeRejet = new String( "-->" );
                            //Afficharge de la liste des donn�es rejet�es
                            Iterator<Compagne> index1 = donneeRejetes.iterator();
                            while ( index1.hasNext() )
                            {
                                Compagne donnee = index1.next();
                                String donneeString = donnee.getIdCompagne() + ";" + donnee.getLibelleCompagne() + ";"
                                    + donnee.getStructure().getCodestructure();
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
        throws WrongValueException, ParseException
    {

        clearFields();

        okAdd.setVisible( true );
        effacer.setVisible( true );
        add.setVisible( false );
        update.setVisible( false );
        delete.setVisible( false );

    }

    public void onClick$okAdd()
        throws WrongValueException, ParseException, InterruptedException
    {

        Compagne addedData = new Compagne();

        addedData.setLibelleCompagne( getSelectedLibelleCompagne() );
        addedData.setStructure( new StructureEntrepriseBean( getSelectedCodeStructure() ) );

        //controle d'integrite 
        CompagneModel compagneModel = new CompagneModel();
        Boolean donneeValide = compagneModel.controleIntegrite( addedData );
        if ( donneeValide )
        {
            //insertion de la donnee ajoutee dans la base de donnee
            boolean donneeAjoute = compagneModel.addCompagne( addedData );
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

        selected.setIdCompagne( getSelectedIdCompagne() );
        selected.setLibelleCompagne( getSelectedLibelleCompagne() );
        selected.setStructure( new StructureEntrepriseBean( getSelectedCodeStructure() ) );

        //controle d'integrite 
        CompagneModel compagneModel = new CompagneModel();
        Boolean donneeValide = compagneModel.controleIntegrite( selected );
        if ( donneeValide )
        {
            //insertion de la donnee ajoutee dans la base de donnee
            boolean donneeAjoute = compagneModel.updCompagne( selected );
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
    	compagnelb.setSelectedIndex(0);
		binder.loadAll();

    }

    public void clearFields()
    {
        idCompagnetb.setText( "" );
        libelleCompagnetb.setText( "" );
        codeStructuretb.setText( "" );
    }

}
