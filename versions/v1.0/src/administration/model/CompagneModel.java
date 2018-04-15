package administration.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import administration.bean.Compagne;
import administration.bean.CompagneType;
import administration.bean.Employe;
import administration.bean.StructureEntrepriseBean;

import java.sql.Connection;
import java.sql.Statement;
import common.CreateDatabaseCon;

/**
 * @author FTERZI
 *
 */

public class CompagneModel
{

    /*
     * Table compagne_evaluation
       =========================

    id_compagne, code_direction, code_service, id_employe, date_debut, date_fin, libelle_compagne, code_structure, id_compagne_type
    -------------------------------------------------------------------------------------------------------------------------------

    id_compagne      mediumint(9) PK
    id_employe       int(11)
    date_debut       date
    date_fin         date
    libelle_compagne varchar(50)
    code_structure   varchar(5)
    id_compagne_type int(11)
     */

    private List<Compagne> compagnes = null;

    public List<Compagne> getAllCompagnes()
        throws SQLException
    {

        compagnes = new ArrayList<Compagne>();

        CreateDatabaseCon dbcon = new CreateDatabaseCon();
        Connection conn = (Connection) dbcon.connectToEntrepriseDB();
        Statement stmt;

        try
        {
            stmt = (Statement) conn.createStatement();
            String select_all_compagne = ConstantsModel.SEL_COMPAGNE;

            ResultSet rs = (ResultSet) stmt.executeQuery( select_all_compagne );

            while ( rs.next() )
            {
                if ( rs.getRow() >= 1 )
                {
                    // id_compagne, id_employe, date_debut, date_fin, libelle_compagne, code_structure, id_compagne_type
                    Compagne compagne = new Compagne();

                    compagne.setIdCompagne( rs.getInt( "id_compagne" ) );
                    compagne.setEmploye( new Employe( rs.getInt( "id_employe" ) ) );
                    compagne.setDateDebut( rs.getDate( "date_debut" ) );
                    compagne.setDateFin( rs.getDate( "date_fin" ) );
                    compagne.setLibelleCompagne( rs.getString( "libelle_compagne" ) );
                    compagne.setStructure( new StructureEntrepriseBean( rs.getString( "code_structure" ) ) );
                    compagne.setCompagneType( new CompagneType( rs.getInt( "id_compagne_type" ), null ) );

                    compagnes.add( compagne );

                }
                else
                {
                    return compagnes;
                }

            }
            conn.close();
        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            //((java.sql.Connection) dbcon).close();
            e.printStackTrace();
            conn.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            conn.close();
        }

        return compagnes;

    }

    public void setCompagnes( List<Compagne> compagnes )
    {
        this.compagnes = compagnes;
    }

    public List<Compagne> getCompagnes()
    {
        return compagnes;
    }

    public boolean controleIntegrite( Compagne addedData )
    {
        try
        {
            if ( addedData.getIdCompagne() > 16777215 )
            {
                Messagebox.show( "L'id comagne est trop grand", "Erreur", Messagebox.OK, Messagebox.ERROR );
                return false;
            }
        }
        catch ( InterruptedException e1 )
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return true;
    }

    public boolean addCompagne( Compagne addedData )
    {

        CreateDatabaseCon dbcon = new CreateDatabaseCon();
        Connection conn = (Connection) dbcon.connectToEntrepriseDB();
        Statement stmt;

        try
        {
            // id_compagne, code_direction, code_service, id_employe, date_debut, date_fin, libelle_compagne, code_structure, id_compagne_type
            stmt = (Statement) conn.createStatement();
            String ins_compagne = ConstantsModel.INS_COMPAGNE;
            ins_compagne = ins_compagne.replaceAll( "#id_employe", String.valueOf( addedData.getEmploye().getId() ) );
            ins_compagne = ins_compagne.replaceAll( "#id_compagne_type",
                                                    String.valueOf( addedData.getCompagneType().getId() ) );
            ins_compagne = ins_compagne.replaceAll( "#date_debut", "'" + addedData.getDateDebut() + "'" );
            ins_compagne = ins_compagne.replaceAll( "#date_fin", "'" + addedData.getDateFin() + "'" );
            ins_compagne = ins_compagne.replaceAll( "#libelle_compagne", "'" + addedData.getLibelleCompagne() + "'" );
            ins_compagne = ins_compagne.replaceAll( "#code_structure", "'"
                + addedData.getStructure().getCodestructure() + "'" );

            stmt.execute( ins_compagne );
            conn.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();

            // TODO Auto-generated catch block
            try
            {
                conn.close();
            }
            catch ( SQLException e1 )
            {
                // TODO Auto-generated catch block

                e1.printStackTrace();
                //return false;
            }

            return false;
        }

        return true;
    }

    public boolean updCompagne( Compagne data )
    {

        CreateDatabaseCon dbcon = new CreateDatabaseCon();
        Connection conn = (Connection) dbcon.connectToEntrepriseDB();
        Statement stmt;

        try
        {
            stmt = (Statement) conn.createStatement();
            String upd_compagne = ConstantsModel.UPD_COMPAGNE;
            upd_compagne = upd_compagne.replaceAll( "#id_compagne", String.valueOf( data.getIdCompagne() ) );
            upd_compagne = upd_compagne.replaceAll( "#id_employe", String.valueOf( data.getEmploye().getId() ) );
            upd_compagne = upd_compagne.replaceAll( "#id_compagne_type",
                                                    String.valueOf( data.getCompagneType().getId() ) );
            upd_compagne = upd_compagne.replaceAll( "#date_debut", "'" + data.getDateDebut() + "'" );
            upd_compagne = upd_compagne.replaceAll( "#date_fin", "'" + data.getDateFin() + "'" );
            upd_compagne = upd_compagne.replaceAll( "#libelle_compagne", "'" + data.getLibelleCompagne() + "'" );
            upd_compagne = upd_compagne.replaceAll( "#code_structure", "'" + data.getStructure().getCodestructure()
                + "'" );

            stmt.execute( upd_compagne );
            conn.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();

            // TODO Auto-generated catch block
            try
            {
                conn.close();
            }
            catch ( SQLException e1 )
            {
                // TODO Auto-generated catch block

                e1.printStackTrace();
                //return false;
            }

            return false;
        }

        return true;
    }

    public boolean delCompagne( Compagne data )
    {

        CreateDatabaseCon dbcon = new CreateDatabaseCon();
        Connection conn = (Connection) dbcon.connectToEntrepriseDB();
        Statement stmt;

        try
        {
            stmt = (Statement) conn.createStatement();
            String del_compagne = ConstantsModel.DEL_COMPAGNE;
            del_compagne = del_compagne.replaceAll( "#id_compagne", String.valueOf( data.getIdCompagne() ) );

            stmt.execute( del_compagne );
            conn.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();

            // TODO Auto-generated catch block
            try
            {
                conn.close();
            }
            catch ( SQLException e1 )
            {
                // TODO Auto-generated catch block

                e1.printStackTrace();
                //return false;
            }

            return false;
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    public List<Compagne> uploadXLSFile( InputStream inputStream )
    {
        compagnes = new ArrayList<Compagne>();

        // Creer l'objet representant le fichier Excel
        try
        {
            HSSFWorkbook fichierExcel = new HSSFWorkbook( inputStream );
            // creer l'objet representant les lignes Excel
            HSSFRow ligne;

            // creer l'objet representant les cellules Excel
            HSSFCell cellule;

            //lecture de la premi�re feuille excel
            HSSFSheet feuilleExcel = fichierExcel.getSheetAt( 0 );

            // lecture du contenu de la feuille excel
            int nombreLigne = feuilleExcel.getLastRowNum() - feuilleExcel.getFirstRowNum();

            for ( int numLigne = 1; numLigne <= nombreLigne; numLigne++ )
            {

                ligne = feuilleExcel.getRow( numLigne );
                int nombreColonne = ligne.getLastCellNum() - ligne.getFirstCellNum();
                Compagne compagne = new Compagne();
                CompagneType compagneType = new CompagneType();
                compagne.setCompagneType( compagneType );
                // parcours des colonnes de la ligne en cours
                for ( short numColonne = 0; numColonne < nombreColonne; numColonne++ )
                {

                    cellule = ligne.getCell( numColonne );

                    String valeur = cellule.getStringCellValue();

                    if ( numColonne == 0 )
                    {
                        compagne.setIdCompagne( Integer.valueOf( valeur ) );
                    }
                    else if ( numColonne == 1 )
                    {
                        compagne.setEmploye( new Employe( Integer.valueOf( valeur ) ) );
                    }
                    else if ( numColonne == 2 )
                    {
                        compagne.setDateDebut( Date.valueOf( valeur ) );
                    }
                    else if ( numColonne == 3 )
                    {
                        compagne.setDateFin( Date.valueOf( valeur ) );
                    }
                    else if ( numColonne == 4 )
                    {
                        compagne.setLibelleCompagne( valeur );
                    }
                    else if ( numColonne == 5 )
                    {
                        compagne.setStructure( new StructureEntrepriseBean( valeur ) );
                    }
                    else if ( numColonne == 6 )
                    {
                        compagne.setCompagneType( new CompagneType( Integer.valueOf( valeur ) ) );
                    }
                }//fin for colonne
                compagnes.add( compagne );
            }//fin for ligne

        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return compagnes;
    }

    public HashMap<String, List<Compagne>> ChargementDonneedansBdd( List<Compagne> liste )
        throws Exception
    {
        //Verification de l'integrit� des donn�es � inserer doublon dans le fichier
        List<Compagne> listeAInserer = new ArrayList<Compagne>();
        List<Compagne> listeDonneesRejetes = new ArrayList<Compagne>();

        for ( int i = 0; i < liste.size(); i++ )
        {
            Compagne donnee = liste.get( i );
            boolean donneerejete = false;
            for ( int j = i + 1; j < liste.size(); j++ )
            {
                Compagne donnee2 = liste.get( j );
                if ( donnee.getIdCompagne() == donnee2.getIdCompagne() )
                {
                    listeDonneesRejetes.add( donnee );
                    donneerejete = true;

                }
            }
            if ( ( i == liste.size() - 1 ) || ( i == 0 ) || ( donneerejete == false ) )
                listeAInserer.add( donnee );

        }

        //Verification de l'integrit� des donn�es � inserer doublon avec les donn�es de la base

        List<Compagne> listeAInsererFinal = new ArrayList<Compagne>();
        List<Compagne> compagnebdd = getAllCompagnes();
        Iterator<Compagne> iterator = listeAInserer.iterator();

        while ( iterator.hasNext() )
        {

            Compagne bean = (Compagne) iterator.next();

            Iterator<Compagne> index = compagnebdd.iterator();
            boolean donneerejete = false;
            while ( index.hasNext() )
            {

                Compagne bean2 = (Compagne) index.next();
                if ( bean.getIdCompagne() == bean2.getIdCompagne() )
                {

                    listeDonneesRejetes.add( bean );
                    donneerejete = true;
                    continue;
                }
            }
            if ( !donneerejete )
            {

                listeAInsererFinal.add( bean );
            }

        }

        //Insertion des donn�es dans la table Structure_entreprise
        Iterator<Compagne> index = listeAInsererFinal.iterator();
        while ( index.hasNext() )
        {
            Compagne donneeBean = (Compagne) index.next();

            addCompagne( donneeBean );
        }

        HashMap<String, List<Compagne>> donneeMap = new HashMap<String, List<Compagne>>();
        donneeMap.put( "inserer", listeAInsererFinal );
        donneeMap.put( "supprimer", listeDonneesRejetes );

        return donneeMap;
    }

    public List<Compagne> uploadXLSXFile( InputStream inputStream )
    {
        compagnes = new ArrayList<Compagne>();

        // Creer l'objet representant le fichier Excel
        try
        {
            XSSFWorkbook fichierExcel = new XSSFWorkbook( inputStream );
            // creer l'objet representant les lignes Excel
            XSSFRow ligne;

            // creer l'objet representant les cellules Excel
            XSSFCell cellule;

            //lecture de la premi�re feuille excel
            XSSFSheet feuilleExcel = fichierExcel.getSheetAt( 0 );

            // lecture du contenu de la feuille excel
            int nombreLigne = feuilleExcel.getLastRowNum() - feuilleExcel.getFirstRowNum();

            for ( int numLigne = 1; numLigne <= nombreLigne; numLigne++ )
            {

                ligne = feuilleExcel.getRow( numLigne );
                int nombreColonne = ligne.getLastCellNum() - ligne.getFirstCellNum();
                Compagne compagne = new Compagne();
                // parcours des colonnes de la ligne en cours
                for ( short numColonne = 0; numColonne < nombreColonne; numColonne++ )
                {
                    try
                    {

                        cellule = ligne.getCell( numColonne );

                        String valeur = cellule.getStringCellValue();

                        if ( numColonne == 0 )
                        {
                            compagne.setIdCompagne( Integer.valueOf( valeur ) );
                        }
                        else if ( numColonne == 1 )
                        {
                            compagne.setEmploye( new Employe( Integer.valueOf( valeur ) ) );
                        }

                        else if ( numColonne == 2 )
                        {
                            compagne.setDateDebut( Date.valueOf( valeur ) );
                        }

                        else if ( numColonne == 3 )
                        {
                            compagne.setDateFin( Date.valueOf( valeur ) );
                        }

                        else if ( numColonne == 4 )
                        {
                            compagne.setLibelleCompagne( valeur );
                        }

                        else if ( numColonne == 5 )
                        {
                            compagne.setStructure( new StructureEntrepriseBean( valeur ) );
                        }

                        else if ( numColonne == 6 )
                        {
                            compagne.setCompagneType( new CompagneType( Integer.valueOf( valeur ) ) );
                        }

                    }
                    catch ( Exception e )
                    {

                    }

                }//fin for colonne
                compagnes.add( compagne );
            }//fin for ligne

        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return compagnes;
    }

    public void downloadCompagneDataToXls()
    {

        //recup�ration du contenu de la table compagne_evaluation
        try
        {
            List<Compagne> compagnes = getAllCompagnes();

            //creation du fichier xls

            Iterator<Compagne> index = compagnes.iterator();
            //creation d'un document excel 
            HSSFWorkbook workBook = new HSSFWorkbook();

            //creation d'une feuille excel
            HSSFSheet sheet = workBook.createSheet( "Compagne" );

            //creation de l'ent�te du document excel
            HSSFCellStyle cellStyle = null;
            cellStyle = workBook.createCellStyle();
            cellStyle.setFillForegroundColor( HSSFColor.RED.index );
            cellStyle.setFillPattern( HSSFCellStyle.SOLID_FOREGROUND );

            HSSFRow row = sheet.createRow( 0 );

            HSSFCell cell = row.createCell( (short) 0 );
            cell.setCellValue( "Id Compagne" );
            cell.setCellStyle( cellStyle );

            HSSFCell cell3 = row.createCell( (short) 1 );
            cell3.setCellValue( "Id Employe" );
            cell3.setCellStyle( cellStyle );

            HSSFCell cell4 = row.createCell( (short) 2 );
            cell4.setCellValue( "Date Debut" );
            cell4.setCellStyle( cellStyle );

            HSSFCell cell5 = row.createCell( (short) 3 );
            cell5.setCellValue( "Date Fin" );
            cell5.setCellStyle( cellStyle );

            HSSFCell cell6 = row.createCell( (short) 4 );
            cell6.setCellValue( "Libelle Compagne" );
            cell6.setCellStyle( cellStyle );

            HSSFCell cell7 = row.createCell( (short) 5 );
            cell7.setCellValue( "Code Structure" );
            cell7.setCellStyle( cellStyle );

            HSSFCell cell8 = row.createCell( (short) 6 );
            cell8.setCellValue( "Id Compagne Type" );
            cell8.setCellStyle( cellStyle );

            int i = 1;
            while ( index.hasNext() )
            {

                Compagne donnee = (Compagne) index.next();

                HSSFRow row1 = sheet.createRow( i );
                HSSFCell cel = row1.createCell( (short) 0 );
                cel.setCellValue( donnee.getIdCompagne() );

                cel = row1.createCell( (short) 1 );
                cel.setCellValue( donnee.getEmploye().getId() );

                cel = row1.createCell( (short) 2 );
                cel.setCellValue( donnee.getDateDebut() );

                cel = row1.createCell( (short) 3 );
                cel.setCellValue( donnee.getDateFin() );

                cel = row1.createCell( (short) 4 );
                cel.setCellValue( donnee.getLibelleCompagne() );

                cel = row1.createCell( (short) 5 );
                cel.setCellValue( donnee.getStructure().getCodestructure() );

                cel = row1.createCell( (short) 6 );
                cel.setCellValue( donnee.getCompagneType().getId() );

                i++;
            }

            FileOutputStream fOut;
            try
            {
                fOut = new FileOutputStream( "Compagne.xls" );
                workBook.write( fOut );
                fOut.flush();
                fOut.close();

                File file = new File( "Compagne.xls" );
                Filedownload.save( file, "XLS" );
            }
            catch ( FileNotFoundException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch ( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
