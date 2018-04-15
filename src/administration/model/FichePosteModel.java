package administration.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import administration.action.StructureEntreprise;
import administration.bean.AdministrationLoginBean;
import administration.bean.FichePosteBean;
import administration.bean.RepCompetenceBean;
import administration.bean.StructureEntrepriseBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import common.Contantes;
import common.CreateDatabaseCon;
import common.PwdCrypt;
import common.StringUtils;

public class FichePosteModel {
	
private ArrayList<FichePosteBean>  listcomp =null; 


	
	
public List loadFichesPostes() throws SQLException{
		
		listcomp = new ArrayList<FichePosteBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();
		Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = (Statement) conn.createStatement();

			
			/*String sql_query=" select  p.code_poste ,p.code_poste_hierarchie,p.intitule_poste,trim(p.sommaire_poste) as sommaire_poste ,trim(p.tache_responsabilite) as tache_responsabilite ,trim(p.environement_perspectif) as environement_perspectif ,concat(d.niv_for_libelle,'-',f.libelle_diplome)  as formation_general," +
	         " p.formation_professionnelle ,p.experience,trim(p.profile_poste) as profile_poste,t.intitule_poste as libelle_poste,p.code_structure,p.date_maj_poste, g.gsp_libelle as is_cadre  , p.code_formation,d.niv_for_libelle" +
	         " from poste_travail_description p,structure_entreprise s ,poste_travail_description t, formation f,def_niv_formation d,def_gsp g" +
	         " where p.code_structure=s.code_structure  and t.code_poste=p.code_poste_hierarchie  and p.code_formation=f.code_formation" +
	         " and f.niv_for_id=d.niv_for_id and p.gsp_id=g.gsp_id" +
	         " Union" +
	         " select p.code_poste ,p.code_poste_hierarchie,p.intitule_poste,trim(p.sommaire_poste) as sommaire_poste ,trim(p.tache_responsabilite) as tache_responsabilite ,trim(p.environement_perspectif) as environement_perspectif ,concat(d.niv_for_libelle,'-',f.libelle_diplome) as formation_general," +
	         " p.formation_professionnelle ,p.experience,trim(p.profile_poste) as profile_poste,code_poste_hierarchie as libelle_poste,p.code_structure,p.date_maj_poste,g.gsp_libelle as is_cadre  , p.code_formation,d.niv_for_libelle" +
	         " from poste_travail_description p,structure_entreprise s , formation f,def_niv_formation d,def_gsp g" +
	         " where code_poste_hierarchie ='' and p.code_structure=s.code_structure and f.niv_for_id=d.niv_for_id" +
	         " and p.code_formation=f.code_formation and p.gsp_id=g.gsp_id order by 1";*/
			
			String sql_query=" select  p.code_poste ,p.code_poste_hierarchie,p.intitule_poste,trim(p.sommaire_poste) as sommaire_poste ,trim(p.tache_responsabilite) as tache_responsabilite ,trim(p.environement_perspectif) as environement_perspectif ,concat(d.niv_for_libelle,'-',f.libelle_diplome)  as formation_general,"
					+ " p.formation_professionnelle ,p.experience,trim(p.profile_poste) as profile_poste,t.intitule_poste as libelle_poste,s.structure_ent,p.date_maj_poste, g.gsp_libelle as is_cadre  , p.code_formation,d.niv_for_libelle"
					+ " from poste_travail_description p,poste_travail_description t, formation f,def_niv_formation d,def_gsp g ,"
					+ "      (select code_structure, structure_ent from ("
					+ "		                                            select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
					+ "     											 and  libelle_section !='null' and  libelle_section !=''"
					+ "        										    union"
					+ "  	 											 select code_structure,libelle_service structure_ent from structure_entreprise"
					+ "													 where libelle_service is  not null and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
					+ "			    				   		 		    union"
					+ "													 select code_structure,libelle_departement structure_ent from structure_entreprise"
					+ "      											 where libelle_departement is  not null and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
					+ "         										 union"
					+ " 											     select code_structure,libelle_sous_direction structure_ent from structure_entreprise"
					+ " 		  									    where libelle_sous_direction is  not null and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
					+ "         										 union"
					+ "   											  select code_structure,libelle_unite structure_ent from structure_entreprise"
					+ "  											 where libelle_unite is  not null and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "												  and length(libelle_service)=0 and  length(libelle_section) =0"
					+ "												 union"
					+ "		             							 select code_structure,libelle_direction structure_ent from structure_entreprise"
					+ "    											 where libelle_direction is  not null and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
					+ "   										  and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise ) s"
					+ " 	          where p.code_structure=s.code_structure  and t.code_poste=p.code_poste_hierarchie  and p.code_formation=f.code_formation"
					+ "		          and f.niv_for_id=d.niv_for_id and p.gsp_id=g.gsp_id"
					+" Union "
					+" select  p.code_poste ,p.code_poste_hierarchie,p.intitule_poste,trim(p.sommaire_poste) as sommaire_poste ,trim(p.tache_responsabilite) as tache_responsabilite ,trim(p.environement_perspectif) as environement_perspectif ,concat(d.niv_for_libelle,'-',f.libelle_diplome)  as formation_general,"
							+ " p.formation_professionnelle ,p.experience,trim(p.profile_poste) as profile_poste,t.intitule_poste as libelle_poste,s.structure_ent,p.date_maj_poste, g.gsp_libelle as is_cadre  , p.code_formation,d.niv_for_libelle"
							+ " from poste_travail_description p,poste_travail_description t, formation f,def_niv_formation d,def_gsp g ,"
							+ "      (select code_structure, structure_ent from ("
							+ "		                                            select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null"
							+ "     											 and  libelle_section !='null' and  libelle_section !=''"
							+ "        										    union"
							+ "  	 											 select code_structure,libelle_service structure_ent from structure_entreprise"
							+ "													 where libelle_service is  not null and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0"
							+ "			    				   		 		    union"
							+ "													 select code_structure,libelle_departement structure_ent from structure_entreprise"
							+ "      											 where libelle_departement is  not null and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0"
							+ "         										 union"
							+ " 											     select code_structure,libelle_sous_direction structure_ent from structure_entreprise"
							+ " 		  									    where libelle_sous_direction is  not null and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0"
							+ "         										 union"
							+ "   											  select code_structure,libelle_unite structure_ent from structure_entreprise"
							+ "  											 where libelle_unite is  not null and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
							+ "												  and length(libelle_service)=0 and  length(libelle_section) =0"
							+ "												 union"
							+ "		             							 select code_structure,libelle_direction structure_ent from structure_entreprise"
							+ "    											 where libelle_direction is  not null and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0"
							+ "   										  and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise ) s"
							+ " 	          where p.code_poste_hierarchie ='' and p.code_structure=s.code_structure  and t.code_poste=p.code_poste_hierarchie  and p.code_formation=f.code_formation"
							+ "		          and f.niv_for_id=d.niv_for_id and p.gsp_id=g.gsp_id order by 1";

			
			/****************************************/
			//System.out.println(sql_query);
			
			rs = (ResultSet) stmt.executeQuery(sql_query);
						
			while(rs.next()){
				
				FichePosteBean bean =new FichePosteBean();
				
				bean.setCode_poste(rs.getString("code_poste"));
				bean.setIntitule_poste(rs.getString("intitule_poste"));
				bean.setSommaire_poste(rs.getString("sommaire_poste"));
				bean.setTache_responsabilite(rs.getString("tache_responsabilite"));
				bean.setEnvironement_perspectif(rs.getString("environement_perspectif"));
				bean.setFormation_general(rs.getString("formation_general"));
				bean.setFormation_professionnelle(rs.getString("formation_professionnelle"));
				bean.setProfile_poste(rs.getString("profile_poste"));
				bean.setLibelle_poste(rs.getString("libelle_poste"));
				bean.setDate_maj_poste(rs.getDate("date_maj_poste"));
				bean.setIs_cadre(rs.getString("is_cadre"));
				bean.setExperience(rs.getString("experience"));
				bean.setCode_formation(rs.getString("code_formation"));
				bean.setNiv_formation(rs.getString("niv_for_libelle"));
				bean.setPoste_hierarchie(rs.getString("code_poste_hierarchie"));
				bean.setLibelle_structure(rs.getString("structure_ent"));

				bean.setCodeFormationNiv(bean.getCode_formation()+","+bean.getNiv_formation());
				bean.setHierarchie(rs.getString("code_poste_hierarchie"));
				    listcomp.add(bean);
				   
					
				}
//			stmt.close();
//			conn.close();
			
		} catch ( SQLException e ) {
			e.printStackTrace();

	    } finally {
	        
	        if ( rs != null ) {
	            try {
	                rs.close();
	            } catch ( SQLException ignore ) {
	            }
	        }
	       
	        if ( stmt != null ) {
	            try {
	            	stmt.close();
	            } catch ( SQLException ignore ) {
	            }
	        }
	        
	        if ( conn != null ) {
	            try {
	            	conn.close();
	            } catch ( SQLException ignore ) {
	            }
	        }
	    }
		return listcomp;
	
		
		
	}

public void ConstructionRequeteAddPosteTravail(FichePosteBean addedData) throws SQLException 
{

//	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//	PwdCrypt pwdcrypt=new PwdCrypt();
	
	/*String insert_structure="INSERT INTO poste_travail_description(code_poste, intitule_poste, sommaire_poste, tache_responsabilite, environement_perspectif, code_formation, formation_professionnelle, experience, profile_poste, code_poste_hierarchie, code_structure, gsp_id)" +
     " VALUES(#code_poste,#intitule_poste,#sommaire_poste,#tache_responsabilite,#environement_perspectif,#code_formation,#formation_professionnelle,#experience,#profile_poste,#hierarchie,#code_structure,#gsp_id)";

	System.out.println("code_poste="+addedData.getCode_poste()+ "addedData.getSommaire_poste()"+addedData.getSommaire_poste());
	String[] code_hierarchie=addedData.getPoste_hierarchie().split(",");
	String[] gsp=addedData.getCode_gsp().split(",");
	insert_structure = insert_structure.replaceAll("#code_poste", "'"+addedData.getCode_poste()+"'");
	insert_structure = insert_structure.replaceAll("#intitule_poste", "'"+addedData.getIntitule_poste()+"'");
	insert_structure = insert_structure.replaceAll("#sommaire_poste", "'"+removeString(addedData.getSommaire_poste())+"'");
	insert_structure = insert_structure.replaceAll("#tache_responsabilite", "'"+removeString(addedData.getTache_responsabilite())+"'");
	insert_structure = insert_structure.replaceAll("#environement_perspectif", "'"+removeString(addedData.getEnvironement_perspectif())+"'");
	insert_structure = insert_structure.replaceAll("#code_formation", "'"+removeString(addedData.getLibelle_formation())+"'");
	insert_structure = insert_structure.replaceAll("#formation_professionnelle", "'"+removeString(addedData.getFormation_professionnelle())+"'");
	insert_structure= insert_structure.replaceAll("#experience", "'"+removeString(addedData.getExperience())+"'");
	insert_structure = insert_structure.replaceAll("#profile_poste", "'"+addedData.getProfile_poste()+"'");
	insert_structure = insert_structure.replaceAll("#hierarchie", "'"+code_hierarchie[0]+"'");
	insert_structure = insert_structure.replaceAll("#code_structure", "'"+addedData.getCode_structure()+"'");
	
	insert_structure = insert_structure.replaceAll("#gsp_id","'"+gsp[0]+"'");


	
	requete=requete+ insert_structure+ " ; ";
	
	

		return requete;*/
	
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToSecondairesDB();
	
	String[] code_hierarchie=addedData.getPoste_hierarchie().split(",");
	String[] gsp=addedData.getCode_gsp().split(",");
	
	PreparedStatement pstmt = null;
	try {
		pstmt = conn.prepareStatement("INSERT INTO poste_travail_description(code_poste, intitule_poste, sommaire_poste, tache_responsabilite, environement_perspectif, code_formation, formation_professionnelle, experience, profile_poste, code_poste_hierarchie, code_structure, gsp_id)" +
		 " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
		pstmt.setString(1, addedData.getCode_poste());
		pstmt.setString(2, addedData.getIntitule_poste());
		pstmt.setString(3, addedData.getSommaire_poste());
		pstmt.setString(4, addedData.getTache_responsabilite());
		pstmt.setString(5, addedData.getEnvironement_perspectif());
		pstmt.setString(6, addedData.getFormation_general());
		pstmt.setString(7, addedData.getLibelle_formation());
		pstmt.setString(8, addedData.getExperience());
		pstmt.setString(9, addedData.getProfile_poste());
		pstmt.setString(10, code_hierarchie[0]);
		pstmt.setString(11, addedData.getCode_structure());
		pstmt.setString(12, gsp[0]);

		
		pstmt.executeUpdate();
		//pstmt.close();conn.close();
		
	} catch ( SQLException e ) {
		e.printStackTrace();
		System.out.println("Exception ors de l'insertion du poste de travail avecla requete  "+pstmt);

    } finally {
       
        if ( pstmt != null ) {
            try {
            	pstmt.close();
            } catch ( SQLException ignore ) {
            }
        }
        
        if ( conn != null ) {
            try {
            	conn.close();
            } catch ( SQLException ignore ) {
            }
        }
    }
	
	
	

	

}
/**
 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
 * @param addedData
 * @return
 * @throws ParseException 
 */
public boolean addPosteTravail(FichePosteBean addedData) throws ParseException
{
	

	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToSecondairesDB();
	Statement stmt = null;
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 Boolean retour=false;
	try 
	{
		                                                
		stmt = (Statement) conn.createStatement();
		String select_structure="INSERT INTO poste_travail_description(code_poste, intitule_poste, sommaire_poste, tache_responsabilite, environement_perspectif, code_formation, formation_professionnelle, experience, profile_poste, code_poste_hierarchie, code_structure, date_maj_poste,gsp_id)" +
	 		          " VALUES(#code_poste,#intitule_poste,#sommaire_poste,#tache_responsabilite,#environement_perspectif,#code_formation,#formation_professionnelle,#experience,#profile_poste,#hierarchie,#code_structure,#date_maj_poste,#gsp_id)";
	 
		//StringUtils stringUtils=new StringUtils();
		
		select_structure = select_structure.replaceAll("#code_poste", "'"+addedData.getCode_poste()+"'");
		select_structure = select_structure.replaceAll("#intitule_poste", "'"+addedData.getIntitule_poste()+"'");
		select_structure = select_structure.replaceAll("#sommaire_poste", "'"+removeString(addedData.getSommaire_poste())+"'");
		select_structure = select_structure.replaceAll("#tache_responsabilite", "'"+removeString(addedData.getTache_responsabilite())+"'");
	    select_structure = select_structure.replaceAll("#environement_perspectif", "'"+removeString(addedData.getEnvironement_perspectif())+"'");
	    select_structure = select_structure.replaceAll("#code_formation", "'"+removeString(addedData.getLibelle_formation())+"'");
        select_structure = select_structure.replaceAll("#formation_professionnelle", "'"+removeString(addedData.getFormation_professionnelle())+"'");
		select_structure = select_structure.replaceAll("#experience", "'"+removeString(addedData.getExperience())+"'");
		select_structure = select_structure.replaceAll("#profile_poste", "'"+addedData.getProfile_poste()+"'");
		select_structure = select_structure.replaceAll("#hierarchie", "'"+addedData.getPoste_hierarchie()+"'");
		select_structure = select_structure.replaceAll("#code_structure", "'"+addedData.getCode_structure()+"'");
		select_structure = select_structure.replaceAll("#date_maj_poste", "'"+formatter.format(addedData.getDate_maj_poste())+"'");
		select_structure = select_structure.replaceAll("#gsp_id","'"+addedData.getCode_gsp()+"'");
		
		
						
	//System.out.println(select_structure);
		
		 stmt.execute(select_structure);
		 retour=true;
	} 

	catch ( SQLException e ) {
		retour=false;
		try 
		{
			
			Messagebox.show("La donnée n'a pas été insérée dans la base de données" +e, "Erreur",Messagebox.OK, Messagebox.ERROR);
		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    } finally {

        if ( stmt != null ) {
            try {
            	stmt.close();
            } catch ( SQLException ignore ) {
            }
        }
        
        if ( conn != null ) {
            try {
            	conn.close();
            } catch ( SQLException ignore ) {
            }
        }
    }
	return retour;
}

public Boolean majPosteTravail(FichePosteBean addedData)
{
	
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToSecondairesDB();
	Statement stmt=null;
	Boolean retour=false;
	try 
	{
		//StringUtils stringUtils=new StringUtils();
		
		stmt = (Statement) conn.createStatement();
		String select_structure="UPDATE poste_travail_description set intitule_poste=#intitule_poste, sommaire_poste=#sommaire_poste, tache_responsabilite=#tache_responsabilite, environement_perspectif=#environement_perspectif, code_formation=#code_formation, formation_professionnelle=#formation_professionnelle, experience=#experience, profile_poste=#profile_poste" +
        "  , code_poste_hierarchie=#hierarchie, code_structure=#code_structure, date_maj_poste=#date_maj_poste,gsp_id=#gsp_id where code_poste=#code_poste";

		select_structure = select_structure.replaceAll("#code_poste", "'"+addedData.getCode_poste()+"'");
		select_structure = select_structure.replaceAll("#intitule_poste", "'"+addedData.getIntitule_poste()+"'");
		select_structure = select_structure.replaceAll("#sommaire_poste", "'"+removeString(addedData.getSommaire_poste())+"'");
		select_structure = select_structure.replaceAll("#tache_responsabilite", "'"+removeString(addedData.getTache_responsabilite())+"'");
	    select_structure = select_structure.replaceAll("#environement_perspectif", "'"+removeString(addedData.getEnvironement_perspectif())+"'");
	    select_structure = select_structure.replaceAll("#code_formation", "'"+removeString(addedData.getCode_formation())+"'");
        select_structure = select_structure.replaceAll("#formation_professionnelle", "'"+removeString(addedData.getFormation_professionnelle())+"'");
		select_structure = select_structure.replaceAll("#experience", "'"+removeString(addedData.getExperience())+"'");
		select_structure = select_structure.replaceAll("#profile_poste", "'"+addedData.getProfile_poste()+"'");
		select_structure = select_structure.replaceAll("#hierarchie", "'"+addedData.getPoste_hierarchie()+"'");
		select_structure = select_structure.replaceAll("#code_structure", "'"+addedData.getCode_structure()+"'");
		select_structure = select_structure.replaceAll("#date_maj_poste", "'"+formatter.format(addedData.getDate_maj_poste())+"'");
		select_structure = select_structure.replaceAll("#gsp_id","'"+addedData.getCode_gsp()+"'");

	   //System.out.println(update_structure);
		
		 stmt.executeUpdate(select_structure);
		 retour=true;
	} 

	catch ( SQLException e ) {
		retour=false;
		try 
		{
			Messagebox.show("La modification n'a pas été prise en compte "+e, "Erreur",Messagebox.OK, Messagebox.ERROR);
		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    } finally {
     
        if ( stmt != null ) {
            try {
            	stmt.close();
            } catch ( SQLException ignore ) {
            }
        }
        
        if ( conn != null ) {
            try {
            	conn.close();
            } catch ( SQLException ignore ) {
            }
        }
    }
	return retour;
}

/**
 * cette classe permet de supprimer une donnée de la table structure_entreprise
 * @param codeStructure
 * @throws InterruptedException 
 */
public void supprimerFichePoste(FichePosteBean addedData) throws InterruptedException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToSecondairesDB();
	Statement stmt=null;
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String sql_query="DELETE FROM  poste_travail_description  where code_poste=#code_poste";

		sql_query = sql_query.replaceAll("#code_poste", "'"+addedData.getCode_poste()+"'");
	
		
		 stmt.executeUpdate(sql_query);
	} 

	catch ( SQLException e ) {
		Messagebox.show("La suppression de l'enregistrement a échoué "+e, "Erreur",Messagebox.OK, Messagebox.ERROR);

    } finally {
       
        if ( stmt != null ) {
            try {
            	stmt.close();
            } catch ( SQLException ignore ) {
            }
        }
        
        if ( conn != null ) {
            try {
            	conn.close();
            } catch ( SQLException ignore ) {
            }
        }
    }
}

public HashMap getListFormation() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToSecondairesDB();
	Statement stmt = null;
	HashMap map = new HashMap();
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String profile_list="select   f.code_formation ,concat(d.niv_for_libelle,'-',f.libelle_diplome)" +
				            " as libelle_formation  from formation f, def_niv_formation d" +
				            " where f.niv_for_id=d.niv_for_id"; 
		ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
		
		
		while(rs.next()){
			map.put(rs.getString("libelle_formation"), rs.getString("code_formation"));
			//list_profile.add(rs.getString("libelle_profile"));
        }
		//stmt.close();conn.close();
	} 
	catch ( SQLException e ) {

    } finally {
      if ( stmt != null ) {
            try {
            	stmt.close();
            } catch ( SQLException ignore ) {
            }
        }
        
        if ( conn != null ) {
            try {
            	conn.close();
            } catch ( SQLException ignore ) {
            }
        }
    }
	
	return map;
}	

public HashMap getListPostes() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToSecondairesDB();
	Statement stmt = null;
	HashMap map = new HashMap();
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String profile_list="select code_poste,intitule_poste from poste_travail_description"; 
		ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
		
		
		while(rs.next()){
			map.put(rs.getString("intitule_poste"), rs.getString("code_poste"));
			//list_profile.add(rs.getString("libelle_profile"));
        }
		//stmt.close();conn.close();
	} 
	catch ( SQLException e ) {

    } finally {
        if ( stmt != null ) {
            try {
            	stmt.close();
            } catch ( SQLException ignore ) {
            }
        }
        
        if ( conn != null ) {
            try {
            	conn.close();
            } catch ( SQLException ignore ) {
            }
        }
    }
	
	return map;
}	

public String getMaxKeyCode() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToSecondairesDB();
	Statement stmt = null;
	String result ="";
	ResultSet rs=null;
	try 
	{
		stmt = (Statement) conn.createStatement();
		String profile_list="select max(code_poste) as max_code from poste_travail_description"; 
		rs = (ResultSet) stmt.executeQuery(profile_list);
		
		
		while(rs.next()){
			result=rs.getString("max_code");
			//list_profile.add(rs.getString("libelle_profile"));
        }
		//stmt.close();conn.close();
	} 
	catch ( SQLException e ) {

    } finally {
        
        if ( rs != null ) {
            try {
                rs.close();
            } catch ( SQLException ignore ) {
            }
        }
       
        if ( stmt != null ) {
            try {
            	stmt.close();
            } catch ( SQLException ignore ) {
            }
        }
        
        if ( conn != null ) {
            try {
            	conn.close();
            } catch ( SQLException ignore ) {
            }
        }
    }
	
	return result;
}	




public HashMap getListStructure() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToSecondairesDB();
	Statement stmt = null;
	HashMap map = new HashMap();
	ResultSet rs=null;	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String profile_list="select code_structure  from structure_entreprise"; 
		rs = (ResultSet) stmt.executeQuery(profile_list);
		
		
		while(rs.next()){
			map.put(rs.getString("code_structure"), rs.getString("code_structure"));
			//list_profile.add(rs.getString("libelle_profile"));
        }
		//stmt.close();conn.close();
	} 
	catch ( SQLException e ) {

    } finally {
        
        if ( rs != null ) {
            try {
                rs.close();
            } catch ( SQLException ignore ) {
            }
        }
       
        if ( stmt != null ) {
            try {
            	stmt.close();
            } catch ( SQLException ignore ) {
            }
        }
        
        if ( conn != null ) {
            try {
            	conn.close();
            } catch ( SQLException ignore ) {
            }
        }
    }
	
	return map;
}	

public String removeString(String chaine){
	String chaine_trt="";
	if(chaine==null)
		return "";
	else		
		if(!chaine.equals(""))
		{
			chaine_trt=chaine;
			chaine_trt=chaine_trt.replaceAll("’"," ");
	        chaine_trt=chaine_trt.replaceAll("'"," ");
		}
	return chaine_trt;
}


public HashMap setlectedStructure(String code_structure) throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToSecondairesDB();
	Statement stmt = null;
	HashMap map = new HashMap();
	ResultSet rs=null;
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String sql_query="select concat_ws('-->',libelle_division,libelle_direction,libelle_unite,libelle_departement,libelle_service,libelle_section) as structure from structure_entreprise where code_structure=#code_structure";
		sql_query = sql_query.replaceAll("#code_structure", "'"+code_structure+"'");
		rs = (ResultSet) stmt.executeQuery(sql_query);
		
		
		while(rs.next()){
			map.put( rs.getString("structure"),rs.getString("structure"));
        }
		//stmt.close();conn.close();
	} 
	catch ( SQLException e ) {

    } finally {
        
        if ( rs != null ) {
            try {
                rs.close();
            } catch ( SQLException ignore ) {
            }
        }
       
        if ( stmt != null ) {
            try {
            	stmt.close();
            } catch ( SQLException ignore ) {
            }
        }
        
        if ( conn != null ) {
            try {
            	conn.close();
            } catch ( SQLException ignore ) {
            }
        }
    }
	
	return map;
	
}

public static String getNextCode(String charto,String code) {

	String nextvalue=
	"XXXX";

	if (code==null || code.length()==0){

	nextvalue=charto+
	"001";

	return nextvalue;

	}

	String[]list=code.split(charto);

	String chaine=String.valueOf(Integer.parseInt(list[1])+1);

	if (chaine.length() <4){

	if (chaine.length()==1){

	chaine=charto+"00"+chaine;

	}

	if (chaine.length()==2){

	chaine=charto+"0"+chaine;

	}

	if (chaine.length()==3){

	chaine=charto+chaine;

	}

	nextvalue=chaine;

	}

	return nextvalue;

	}

public HashMap isCadre() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToSecondairesDB();
	Statement stmt = null;
	HashMap map = new HashMap();
	ResultSet rs=null;
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String sql_query="select gsp_id,gsp_libelle from def_gsp";
		rs = (ResultSet) stmt.executeQuery(sql_query);
		
		
		while(rs.next()){
			map.put( rs.getString("gsp_libelle"),rs.getString("gsp_id"));
        }
		//stmt.close();conn.close();
	} 
	catch ( SQLException e ) {

    } finally {
        
        if ( rs != null ) {
            try {
                rs.close();
            } catch ( SQLException ignore ) {
            }
        }
       
        if ( stmt != null ) {
            try {
            	stmt.close();
            } catch ( SQLException ignore ) {
            }
        }
        
        if ( conn != null ) {
            try {
            	conn.close();
            } catch ( SQLException ignore ) {
            }
        }
    }
	
	return map;
	
}
public List <FichePosteBean> uploadXLSFile(InputStream inputStream)
{
	
	ArrayList <FichePosteBean> listFichePostebean=new ArrayList<FichePosteBean>();
	
	 // Creer l'objet representant le fichier Excel
    try 
    {
		HSSFWorkbook fichierExcel = new HSSFWorkbook(inputStream);
		// creer l'objet representant les lignes Excel
        HSSFRow ligne;

        // creer l'objet representant les cellules Excel
        HSSFCell cellule;

        //lecture de la première feuille excel
        HSSFSheet feuilleExcel=fichierExcel.getSheet(Contantes.onglet_fiche_postes);
        
        // lecture du contenu de la feuille excel
        int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();
        
        
       
        for(int numLigne =1;numLigne<=nombreLigne; numLigne++)
        {
        	
        	
        	ligne = feuilleExcel.getRow(numLigne);
        	if(ligne!=null)
        	{
        		
            int nombreColonne = ligne.getLastCellNum()
                    - ligne.getFirstCellNum();
            FichePosteBean fichePoste=new FichePosteBean();
            // parcours des colonnes de la ligne en cours
            short numColonne=-1;
            boolean inserer=true;
            while( (numColonne < nombreColonne)&&(inserer) )
            {
            	numColonne++;
            	
            	
            	cellule = ligne.getCell(numColonne);
            	Double v;
            	String valeur="";
            	if(cellule!=null)
            	{
            		inserer=true;
            		
            	if(numColonne==7)
            	{
            		try
            		{
	            		v=cellule.getNumericCellValue();
	            		valeur=v.toString();
            		}
            		catch(Exception e)
            		{
            			valeur=cellule.getStringCellValue();
            		}
            	}
            	else
            		valeur= cellule.getStringCellValue();
            	
            	
            	if(numColonne==0)
            	{
            		if(valeur==null)
            		{
            			inserer=false;
            		}
            		else
            			if(valeur.equals("")||valeur.equals(" "))
            				inserer=false;
            		fichePoste.setCode_poste(valeur);
            	}
            	else
            		if(numColonne==1)
	            	{
            			fichePoste.setIntitule_poste(valeur);
	            	}
	            	else
	            		if(numColonne==2)
		            	{
	            			fichePoste.setSommaire_poste(valeur);
		            	}
		            	else
		            		if(numColonne==3)
			            	{
		            			fichePoste.setTache_responsabilite(valeur);
			            	}
			            	else
			            		if(numColonne==4)
				            	{
			            			fichePoste.setEnvironement_perspectif(valeur);
				            	}
				            	else
				            		if(numColonne==5)
					            	{
				            			String[] val=valeur.split(",");
				            			fichePoste.setFormation_general(val[0]);
				            			if(val.length>1)
				            				fichePoste.setLibelle_formation(val[1]);
					            	}
					            	else
					            		if(numColonne==6)
						            	{
					            			fichePoste.setFormation_professionnelle(valeur);
						            	}
						            	else
						            		if(numColonne==7)
							            	{
						            			fichePoste.setExperience(valeur);
							            	}
							            	else
							            		if(numColonne==8)
								            	{
							            			fichePoste.setProfile_poste(valeur);
								            	}
								            	else
								            		if(numColonne==9)
									            	{
								            			fichePoste.setPoste_hierarchie(valeur);
									            	}
									            	else
									            		if(numColonne==10)
										            	{
									            			String[] val=valeur.split(",");
									            			fichePoste.setCode_structure(val[0]);
										            	}
									            		else
									            			if(numColonne==11)
									            			{
									            				fichePoste.setCode_gsp(valeur);
									            			}
									            			
            	
            	}
            	else
            		if(numColonne==0)
            			inserer=false;
            }//fin for colonne
            if(inserer)
            	listFichePostebean.add(fichePoste);
        	}
        }//fin for ligne

	} 
    catch (IOException e) 
    {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	
	
	
	return listFichePostebean;
}

public List <FichePosteBean> uploadXLSXFile(InputStream inputStream)
{
	
	ArrayList <FichePosteBean> listFichePostebean=new ArrayList<FichePosteBean>();
	
	 // Creer l'objet representant le fichier Excel
    try 
    {
		XSSFWorkbook fichierExcel = new XSSFWorkbook(inputStream);
		// creer l'objet representant les lignes Excel
        XSSFRow ligne;

        // creer l'objet representant les cellules Excel
        XSSFCell cellule;

        //lecture de la première feuille excel
        XSSFSheet feuilleExcel=fichierExcel.getSheet(Contantes.onglet_fiche_postes);
        
        // lecture du contenu de la feuille excel
        int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();
        
        
       
        for(int numLigne =1;numLigne<=nombreLigne; numLigne++)
        {
        	
        	ligne = feuilleExcel.getRow(numLigne);
        	if(ligne!=null)
        	{
            int nombreColonne = ligne.getLastCellNum()
                    - ligne.getFirstCellNum();
            FichePosteBean fichePoste=new FichePosteBean();
            // parcours des colonnes de la ligne en cours
            short numColonne=-1;
            boolean inserer=true;
            while( (numColonne < nombreColonne)&&(inserer) )
            {
            	numColonne++;
            	
            	
            	cellule = ligne.getCell(numColonne);
            	Double v;
            	String valeur="";
            	if(cellule!=null)
            	{
            		inserer=true;
            		
            	if(numColonne==7)
            	{
            		try
            		{
	            		v=cellule.getNumericCellValue();
	            		valeur=v.toString();
            		}
            		catch(Exception e)
            		{
            			valeur=cellule.getStringCellValue();
            		}
            	}
            	else
            		valeur= cellule.getStringCellValue();
            	
            	
            	if(numColonne==0)
            	{
            		if(valeur==null)
            		{
            			inserer=false;
            		}
            		else
            			if(valeur.equals("")||valeur.equals(" "))
            				inserer=false;
            		fichePoste.setCode_poste(valeur);
            	}
            	else
            		if(numColonne==1)
	            	{
            			fichePoste.setIntitule_poste(valeur);
	            	}
	            	else
	            		if(numColonne==2)
		            	{
	            			fichePoste.setSommaire_poste(valeur);
		            	}
		            	else
		            		if(numColonne==3)
			            	{
		            			fichePoste.setTache_responsabilite(valeur);
			            	}
			            	else
			            		if(numColonne==4)
				            	{
			            			fichePoste.setEnvironement_perspectif(valeur);
				            	}
				            	else
				            		if(numColonne==5)
					            	{
				            			String[] val=valeur.split(",");
				            			fichePoste.setFormation_general(val[0]);
				            			if(val.length>1)
				            				fichePoste.setLibelle_formation(val[1]);
					            	}
					            	else
					            		if(numColonne==6)
						            	{
					            			fichePoste.setFormation_professionnelle(valeur);
						            	}
						            	else
						            		if(numColonne==7)
							            	{
						            			fichePoste.setExperience(valeur);
							            	}
							            	else
							            		if(numColonne==8)
								            	{
							            			fichePoste.setProfile_poste(valeur);
								            	}
								            	else
								            		if(numColonne==9)
									            	{
								            			fichePoste.setPoste_hierarchie(valeur);
									            	}
									            	else
									            		if(numColonne==10)
										            	{
									            			String[] val=valeur.split(",");
									            			fichePoste.setCode_structure(val[0]);
										            	}
									            		else
									            			if(numColonne==11)
									            			{
									            				fichePoste.setCode_gsp(valeur);
									            			}
									            			
            	
            	}
            	else
            		if(numColonne==0)
            			inserer=false;
            }//fin for colonne
            if(inserer)
            	listFichePostebean.add(fichePoste);
        }//fin for ligne
        }
	} 
    catch (IOException e) 
    {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	
	
	
	return listFichePostebean;
}

/**
 * cette méthode permet de vérifier l'integrite des données et retourne les données rejetés
 * @return
 */
public HashMap <String,List<FichePosteBean>> ChargementDonneedansBdd(List <FichePosteBean> liste)throws Exception
{
	//Verification de l'integrité des données à inserer doublon dans le fichier
	List <FichePosteBean> listeAInserer=new ArrayList <FichePosteBean>();
	List <FichePosteBean> listeDonneesRejetes=new ArrayList <FichePosteBean>();
	
	
	

	for(int i=0; i<liste.size();i++)
	{
		FichePosteBean donnee=liste.get(i);
		boolean donneerejete=false;
		for(int j=i+1;j<liste.size();j++)
		{
			FichePosteBean donnee2=liste.get(j);
			if(donnee.getCode_poste().equals(donnee2.getCode_poste()))
			{
				donnee.setCauseRejet("le code "+ donnee.getCode_poste()+" existe déja dans le fichier à inserer");
				listeDonneesRejetes.add(donnee);
				donneerejete=true;
				
			}
		}
		if((i==liste.size()-1)||(i==0)||(donneerejete==false))
			listeAInserer.add(donnee);
		
	}
	
	
	//Verification de l'integrité des données à inserer doublon avec les données de la base
	
	List <FichePosteBean> listeAInsererFinal=new ArrayList <FichePosteBean>();
	@SuppressWarnings("unchecked")
	List<FichePosteBean>fichePostebdd =loadFichesPostes();
	Iterator <FichePosteBean>iterator=listeAInserer.iterator();
	
	while(iterator.hasNext())
	{
		
		FichePosteBean bean=(FichePosteBean)iterator.next();
		
		Iterator<FichePosteBean> index=fichePostebdd.iterator();
		boolean donneerejete=false;
		while(index.hasNext())
		{
			
			FichePosteBean bean2=(FichePosteBean)index.next();
			if(bean.getCode_poste().equals(bean2.getCode_poste()))
			{
				bean.setCauseRejet("le code "+ bean.getCode_poste()+" existe déja dans la base de données");
				listeDonneesRejetes.add(bean);
				donneerejete=true;
				continue;
			}
		}
		if(!donneerejete)
		{
			
			listeAInsererFinal.add(bean);
		}
		
	}
	
	//Insertion des données dans la table Structure_entreprise
	Iterator<FichePosteBean> index =listeAInsererFinal.iterator();
	String requete="";
	while(index.hasNext())
	{
		FichePosteBean donneeBean=(FichePosteBean)index.next();
		ConstructionRequeteAddPosteTravail(donneeBean);
		//addPosteTravail(donneeBean);			
	}
	//execution de la requete
	/*try
	{
		//System.out.println(requete);
		if(requete!="")
			updateMultiQuery(requete);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}*/
	HashMap <String,List<FichePosteBean>> donneeMap=new HashMap<String,List<FichePosteBean>>();
	donneeMap.put("inserer", listeAInsererFinal);
	donneeMap.put("supprimer", listeDonneesRejetes);
	return donneeMap;
}


/**
 * Cette méthode permet de charger le contenu de la table Piche_Poste et de créer un fichier excel avec ces données
 */
public void downloadFichePosteDataToXls(HSSFWorkbook workBook)
{
	//recupération du contenu de la table Structure_entreprise
	try 
	{
		@SuppressWarnings("unchecked")
		List<FichePosteBean> listeFichePosteBean=loadFichesPostes();
		HashMap<String,StructureEntrepriseBean> mapStructureEntreprise= getStructureEntreprise();
		//creation du fichier xls
		
		Iterator <FichePosteBean>index=listeFichePosteBean.iterator();
		//creation d'un document excel 
		//HSSFWorkbook workBook = new HSSFWorkbook();
		
		//creation d'une feuille excel
		 HSSFSheet sheet = workBook.createSheet(Contantes.onglet_fiche_postes);
		 
		 //creation de l'entête du document excel
		 HSSFRow row = sheet.createRow(0);
		 HSSFCell cell = row.createCell((short)0);
		 
		 HSSFCellStyle cellStyle = null;
		 cellStyle = workBook.createCellStyle();
		 
		 HSSFFont font1 = workBook.createFont();
		 font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		 font1.setFontHeightInPoints((short)8);
		 font1.setFontName("Arial");
		 
	     cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	     cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
	     cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	     cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
	     cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	     cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
	     cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	     cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
	     cellStyle.setFont(font1);
	        
		 cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		 cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		 cell.setCellValue("code poste");
		 			 cell.setCellStyle(cellStyle);
		 HSSFCell cell1 = row.createCell((short)1);
		 cell1.setCellValue("intitule poste");
		 			 cell1.setCellStyle(cellStyle);
		 
		 HSSFCell cell2 = row.createCell((short)2);
		 cell2.setCellValue("sommaire poste");
		 			 cell2.setCellStyle(cellStyle);
		 
		 HSSFCell cell3 = row.createCell((short)3);
		 cell3.setCellValue("tache responsabilite");
		 			 cell3.setCellStyle(cellStyle);
		 
		 HSSFCell cell4 = row.createCell((short)4);
		 cell4.setCellValue("environement_perspectif");
		 			 cell4.setCellStyle(cellStyle);
		 
		 HSSFCell cell5 = row.createCell((short)5);
		 cell5.setCellValue("code formation");
		 			 cell5.setCellStyle(cellStyle);
		 
		 HSSFCell cell6 = row.createCell((short)6);
		 cell6.setCellValue("formation professionnelle");
		 			 cell6.setCellStyle(cellStyle);
		 
		 HSSFCell cell7 = row.createCell((short)7);
		 cell7.setCellValue("experience");
		 			 cell7.setCellStyle(cellStyle);
		 
		 HSSFCell cell8 = row.createCell((short)8);
		 cell8.setCellValue("profile poste");
		 			 cell8.setCellStyle(cellStyle);
		 
		 HSSFCell cell9 = row.createCell((short)9);
		 cell9.setCellValue("code poste hierarchie");
		 			 cell9.setCellStyle(cellStyle);
		 
		 HSSFCell cell10 = row.createCell((short)10);
		 cell10.setCellValue("code structure");
		 			 cell10.setCellStyle(cellStyle);
		 
		 HSSFCell cell11 = row.createCell((short)11);
		 cell11.setCellValue("GSP");
		 			 cell11.setCellStyle(cellStyle);
		 			 
		 HSSFCell cell12 = row.createCell((short)12);
		 cell12.setCellValue("code+libelle");
		 cell12.setCellStyle(cellStyle);		 			 
		 
		 
		 
		 int i=1;
		while (index.hasNext())
		{
			
			
			FichePosteBean donnee=(FichePosteBean)index.next();
			
			 HSSFRow row1 = sheet.createRow(i);
			 HSSFCell cel = row1.createCell((short)0);
			 cel.setCellValue(donnee.getCode_poste());
			 
			 cel = row1.createCell((short)1);
			 cel.setCellValue(donnee.getIntitule_poste());
			 cel = row1.createCell((short)2);
			 cel.setCellValue(donnee.getSommaire_poste());
			 cel = row1.createCell((short)3);
			 cel.setCellValue(donnee.getTache_responsabilite());
			 cel = row1.createCell((short)4);
			 cel.setCellValue(donnee.getEnvironement_perspectif());
			 cel = row1.createCell((short)5);
			 
			 cel.setCellValue(donnee.getCode_formation()+","+donnee.getNiv_formation());
			 cel = row1.createCell((short)6);
			 cel.setCellValue(donnee.getFormation_professionnelle());
			 cel = row1.createCell((short)7);
			 
			 cel.setCellValue(donnee.getExperience());
			 cel = row1.createCell((short)8);
			 cel.setCellValue(donnee.getProfile_poste());
			 cel = row1.createCell((short)9);
			 cel.setCellValue(donnee.getPoste_hierarchie());
			 cel = row1.createCell((short)10);
			 
			 StructureEntrepriseBean structure=mapStructureEntreprise.get(donnee.getCode_structure());
			 String libell_division=structure.getLibelleDivision();
			 String libelleDirection=structure.getLibelleDirection();
			 String libelleUnite=structure.getLibelleUnite();
			 String libelleDepartement=structure.getLibelleDepartement();
			 String libelleService=structure.getLibelleService();
			 String libelleSection=structure.getLibelleSection();
			 if(libell_division==null) libell_division="";
			 if(libelleDirection==null)libelleDirection="";
			 if(libelleUnite==null)libelleUnite="";
			 if(libelleDepartement==null)libelleDepartement="";
			 if(libelleService==null)libelleService="";
			 if(libelleSection==null)libelleSection="";
			 String valeurCode_Structure=donnee.getCode_structure()+","+libell_division+","+libelleDirection+","+libelleUnite+","+libelleDepartement+","+ libelleService+","+libelleSection;
			 cel.setCellValue(valeurCode_Structure);
			 cel = row1.createCell((short)11);
			 cel.setCellValue(donnee.getIs_cadre());
			 
			 cel = row1.createCell((short)12);		 
			 i++;
			 cel.setCellFormula("A"+i+"& \",\"&B"+i);
		}

		//autosize des colonnes
		for (int j=0;j<=12;j++)
	 	{
	 		sheet.autoSizeColumn(j);
	 		
	 	}	
		
//		FileOutputStream fOut;
//		try 
//		{
//			fOut = new FileOutputStream("Donnees_Evalcom.xls");
//			workBook.write(fOut);
//			fOut.flush();
//			fOut.close();
//			
//			File file = new File("Donnees_Evalcom.xls");
//			Filedownload.save(file, "XLS");
//		} 
//		catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
				
	}
	catch (SQLException e) 
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}


public void downloadFichePosteRejectedDataToXls(HSSFWorkbook workBook,List<FichePosteBean> listeFichePosteBean)
{
	//recupération du contenu de la table Structure_entreprise
	try 
	{
		@SuppressWarnings("unchecked")
		
		HashMap<String,StructureEntrepriseBean> mapStructureEntreprise= getStructureEntreprise();
		//creation du fichier xls
		
		Iterator <FichePosteBean>index=listeFichePosteBean.iterator();
		//creation d'un document excel 
		//HSSFWorkbook workBook = new HSSFWorkbook();
		
		//creation d'une feuille excel
		 HSSFSheet sheet = workBook.createSheet(Contantes.onglet_fiche_postes);
		 
		 //creation de l'entête du document excel
		 HSSFRow row = sheet.createRow(0);
		 HSSFCell cell = row.createCell((short)0);
		 
		 HSSFCellStyle cellStyle = null;
		 cellStyle = workBook.createCellStyle();
		 
		 HSSFFont font1 = workBook.createFont();
		 font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		 font1.setFontHeightInPoints((short)8);
		 font1.setFontName("Arial");
		 
	     cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	     cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
	     cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	     cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
	     cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	     cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
	     cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	     cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
	     cellStyle.setFont(font1);
	        
		 cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		 cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		 cell.setCellValue("code poste");
		 			 cell.setCellStyle(cellStyle);
		 HSSFCell cell1 = row.createCell((short)1);
		 cell1.setCellValue("intitule poste");
		 			 cell1.setCellStyle(cellStyle);
		 
		 HSSFCell cell2 = row.createCell((short)2);
		 cell2.setCellValue("sommaire poste");
		 			 cell2.setCellStyle(cellStyle);
		 
		 HSSFCell cell3 = row.createCell((short)3);
		 cell3.setCellValue("tache responsabilite");
		 			 cell3.setCellStyle(cellStyle);
		 
		 HSSFCell cell4 = row.createCell((short)4);
		 cell4.setCellValue("environement_perspectif");
		 			 cell4.setCellStyle(cellStyle);
		 
		 HSSFCell cell5 = row.createCell((short)5);
		 cell5.setCellValue("code formation");
		 			 cell5.setCellStyle(cellStyle);
		 
		 HSSFCell cell6 = row.createCell((short)6);
		 cell6.setCellValue("formation professionnelle");
		 			 cell6.setCellStyle(cellStyle);
		 
		 HSSFCell cell7 = row.createCell((short)7);
		 cell7.setCellValue("experience");
		 			 cell7.setCellStyle(cellStyle);
		 
		 HSSFCell cell8 = row.createCell((short)8);
		 cell8.setCellValue("profile poste");
		 			 cell8.setCellStyle(cellStyle);
		 
		 HSSFCell cell9 = row.createCell((short)9);
		 cell9.setCellValue("code poste hierarchie");
		 			 cell9.setCellStyle(cellStyle);
		 
		 HSSFCell cell10 = row.createCell((short)10);
		 cell10.setCellValue("code structure");
		 			 cell10.setCellStyle(cellStyle);
		 
		 HSSFCell cell11 = row.createCell((short)11);
		 cell11.setCellValue("GSP");
		 			 cell11.setCellStyle(cellStyle);
		 			 
		 HSSFCell cell12 = row.createCell((short)12);
		 cell12.setCellValue("code+libelle");
		 cell12.setCellStyle(cellStyle);
		 
		 HSSFCell cell13 = row.createCell((short)13);
		 cell13.setCellValue("Cause du rejet");
		 cell13.setCellStyle(cellStyle);
		 
		 
		 
		 int i=1;
		while (index.hasNext())
		{
			
			
			FichePosteBean donnee=(FichePosteBean)index.next();
			
			 HSSFRow row1 = sheet.createRow(i);
			 HSSFCell cel = row1.createCell((short)0);
			 cel.setCellValue(donnee.getCode_poste());
			 
			 cel = row1.createCell((short)1);
			 cel.setCellValue(donnee.getIntitule_poste());
			 cel = row1.createCell((short)2);
			 cel.setCellValue(donnee.getSommaire_poste());
			 cel = row1.createCell((short)3);
			 cel.setCellValue(donnee.getTache_responsabilite());
			 cel = row1.createCell((short)4);
			 cel.setCellValue(donnee.getEnvironement_perspectif());
			 cel = row1.createCell((short)5);
			 
			 cel.setCellValue(donnee.getCode_formation()+","+donnee.getNiv_formation());
			 cel = row1.createCell((short)6);
			 cel.setCellValue(donnee.getFormation_professionnelle());
			 cel = row1.createCell((short)7);
			 
			 cel.setCellValue(donnee.getExperience());
			 cel = row1.createCell((short)8);
			 cel.setCellValue(donnee.getProfile_poste());
			 cel = row1.createCell((short)9);
			 cel.setCellValue(donnee.getPoste_hierarchie());
			 cel = row1.createCell((short)10);
			 
			 StructureEntrepriseBean structure=mapStructureEntreprise.get(donnee.getCode_structure());
			 String libell_division=structure.getLibelleDivision();
			 String libelleDirection=structure.getLibelleDirection();
			 String libelleUnite=structure.getLibelleUnite();
			 String libelleDepartement=structure.getLibelleDepartement();
			 String libelleService=structure.getLibelleService();
			 String libelleSection=structure.getLibelleSection();
			 if(libell_division==null) libell_division="";
			 if(libelleDirection==null)libelleDirection="";
			 if(libelleUnite==null)libelleUnite="";
			 if(libelleDepartement==null)libelleDepartement="";
			 if(libelleService==null)libelleService="";
			 if(libelleSection==null)libelleSection="";
			 String valeurCode_Structure=donnee.getCode_structure()+","+libell_division+","+libelleDirection+","+libelleUnite+","+libelleDepartement+","+ libelleService+","+libelleSection;
			 cel.setCellValue(valeurCode_Structure);
			 cel = row1.createCell((short)11);
			 cel.setCellValue(donnee.getIs_cadre());
			 
			 cel = row1.createCell((short)12);		 
			 i++;
			 cel.setCellFormula("A"+i+"& \",\"&B"+i);
			 
			 cel = row1.createCell((short)13);
			 cel.setCellValue(donnee.getCauseRejet());
		}

		//autosize des colonnes
		for (int j=0;j<=13;j++)
	 	{
	 		sheet.autoSizeColumn(j);
	 		
	 	}	
		

		
				
	}
	catch (SQLException e) 
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}


public HashMap<String,StructureEntrepriseBean> getStructureEntreprise() throws SQLException{
	
	HashMap<String,StructureEntrepriseBean> mapStructureEntreprise = new HashMap<String,StructureEntrepriseBean>();
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToSecondairesDB();
	Statement stmt=null;
	ResultSet rs=null;
	try {
		stmt = (Statement) conn.createStatement();
		String select_structure="select code_structure, libelle_division, libelle_direction, libelle_unite, libelle_departement, libelle_service, libelle_section  from  structure_entreprise";
		
		rs = (ResultSet) stmt.executeQuery(select_structure);
		
		
		while(rs.next()){
			if (rs.getRow()>=1) {
				StructureEntrepriseBean structureEntreprise=new StructureEntrepriseBean();
				String code_structure=rs.getString("code_structure");

				structureEntreprise.setLibelleDivision(rs.getString("libelle_division"));
				
				structureEntreprise.setLibelleDirection(rs.getString("libelle_direction"));
				
				structureEntreprise.setLibelleUnite(rs.getString("libelle_unite"));
				
				structureEntreprise.setLibelleDepartement(rs.getString("libelle_departement"));
				
				structureEntreprise.setLibelleService(rs.getString("libelle_service"));
				
				structureEntreprise.setLibelleSection(rs.getString("libelle_section"));
				  
				mapStructureEntreprise.put(code_structure,structureEntreprise);
			   
				
			}else {
				return mapStructureEntreprise;
			}
			
		}
		//conn.close();
	}catch ( SQLException e ) {

    } finally {
        
        if ( rs != null ) {
            try {
                rs.close();
            } catch ( SQLException ignore ) {
            }
        }
       
        if ( stmt != null ) {
            try {
            	stmt.close();
            } catch ( SQLException ignore ) {
            }
        }
        
        if ( conn != null ) {
            try {
            	conn.close();
            } catch ( SQLException ignore ) {
            }
        }
    }
	
		
	return mapStructureEntreprise;	

	
	
}

public void updateMultiQuery(String requete)
{
	if(requete!="")
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToSecondairesDB();//connectToEntrepriseDBMulti();
		Statement stmt=null;
	
		try 
		{
			stmt = (Statement) conn.createStatement();
			//System.out.println(requete);
			stmt.execute(requete);
			
			//conn.close();
		} 
		catch ( SQLException e ) {

	    } finally {
	        	       
	        if ( stmt != null ) {
	            try {
	            	stmt.close();
	            } catch ( SQLException ignore ) {
	            }
	        }
	        
	        if ( conn != null ) {
	            try {
	            	conn.close();
	            } catch ( SQLException ignore ) {
	            }
	        }
	    }
		
	}


}




/*
 * remplissage de la list box structure entreprise
 * @return
 * @throws SQLException
 */

public Map getStructEntList() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToSecondairesDB();
	Statement stmt = null;
	HashMap map = new HashMap();
	ResultSet rs=null;
	try 
	{ 
		stmt = (Statement) conn.createStatement();
		String db_list=		
				"select code_structure, structure_ent from (" +
						" select code_structure,libelle_section structure_ent  from structure_entreprise  where libelle_section is  not null "+
						" and  libelle_section !='null' and  libelle_section !='' " +
						" union " +
						" select code_structure,libelle_service structure_ent from structure_entreprise  " +
						" where libelle_service is  not null" +
						"  and libelle_service !='null' and libelle_service !=''  and  length(libelle_section) =0   " +
						" union " +
						" select code_structure,libelle_departement structure_ent from structure_entreprise  " + 
						" where libelle_departement is  not null " +
						"  and libelle_departement !='null' and libelle_departement !='' and length(libelle_service)=0   and  length(libelle_section) =0  " +
						" union " +
						" select code_structure,libelle_sous_direction structure_ent from structure_entreprise   " +
						" where libelle_sous_direction is  not null " +
						"  and libelle_sous_direction !='null' and libelle_sous_direction !=''  and length(libelle_departement)=0 and length(libelle_service)=0  and  length(libelle_section) =0 " +   
						" union  " +
						" select code_structure,libelle_unite structure_ent from structure_entreprise   " +
						" where libelle_unite is  not null " +
						"  and libelle_unite !='null' and libelle_unite !=''  and length(libelle_sous_direction)=0 and length(libelle_departement)=0  " +
						"  and length(libelle_service)=0 and  length(libelle_section) =0    " +
						" union  " +
						" select code_structure,libelle_direction structure_ent from structure_entreprise   " +
						" where libelle_direction is  not null " +
						"  and libelle_direction !='null' and libelle_direction !=''  and length(libelle_unite)=0 and length(libelle_sous_direction)=0 and length(libelle_departement)=0 " + 
						"  and length(libelle_service)=0 and  length(libelle_section) =0 ) tmp_structure_entreprise  order by structure_ent ";
		//System.out.println(db_list);
		rs = (ResultSet) stmt.executeQuery(db_list);


		while(rs.next()){
			map.put( rs.getString("structure_ent"),rs.getString("code_structure"));
		}
		//map.put("Tous",-1);
		//stmt.close();conn.close();
	} 
	catch ( SQLException e ) {

	} finally {

		if ( rs != null ) {
			try {
				rs.close();
			} catch ( SQLException ignore ) {
			}
		}

		if ( stmt != null ) {
			try {
				stmt.close();
			} catch ( SQLException ignore ) {
			}
		}

		if ( conn != null ) {
			try {
				conn.close();
			} catch ( SQLException ignore ) {
			}
		}
	}

	Map<String, String> treeMap = new TreeMap<String, String>(map);
	return  treeMap;
}
}
