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
            throw new WrongValueException( libelleCompagnetb, "le libelle campagne ne doit pas etre vide!" );
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

  



    

}
