package kpi.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.StringUtils;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.zkoss.zul.Filedownload;

public class WebReport extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor of the object.
	 */
	private IReportEngine birtReportEngine = null;
	protected static Logger logger = Logger.getLogger( "org.eclipse.birt" );
	
	public WebReport() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); 
		BirtEngine.destroyBirtEngine();
	}


	/**
	 * The doGet method of the servlet. <br>
	 *
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		traiterBirtRequest(req, resp);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		traiterBirtRequest(req, resp);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init(ServletConfig sc) throws ServletException {
		BirtEngine.initBirtConfig();
		this.birtReportEngine = BirtEngine.getBirtEngine(sc.getServletContext());
		
	}
	public void downloadDocFile(HttpServletRequest req,HttpServletResponse resp)throws ServletException, IOException {

			
			resp.setContentType( "application/msword" ); 
			resp.setHeader("Content-disposition","attachment; filename=\"" 
					+"Download_Report.doc" +"\"");
			
			String reportName = req.getParameter("__report");
			ServletContext sc = req.getSession().getServletContext();
			this.birtReportEngine = BirtEngine.getBirtEngine(sc);
					
			IReportRunnable design;
			try
			{
				//Open report design
				design = birtReportEngine.openReportDesign( sc.getRealPath("/Reports")+"/"+reportName );
				//create task to run and render report
				IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask( design );		
				task.getAppContext().put("BIRT_VIEWER_HTTPSERVLET_REQUEST", req );			
				
				
				//set output options DOC
				PDFRenderOption options = new PDFRenderOption();
				//options.setOutputFileName("C:/Temp/output_test.doc");
	            options.setOutputFormat("DOC");
	            options.setSupportedImageFormats( "JPG;PNG;BMP;SVG;GIF" );
	            
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            options.setOutputStream( out );
	            //options.setOutputStream( resp.getOutputStream() );
	            
	            task.setRenderOption(options);
							
	            
				//run report
				task.run();

				
				InputStream inputStream = new ByteArrayInputStream(( (ByteArrayOutputStream) out ).toByteArray( ) );
				
				Filedownload.save(inputStream,"application/msword", "Rapport.doc");
				
				
				task.close();
			}catch (Exception e){
				
				e.printStackTrace();
				throw new ServletException( e );
			}
	}
	
	public void displayHtmlFile(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//get report name and launch the engine
		resp.setContentType("text/html");
		
		resp.setHeader ("Content-Disposition","inline; filename=test.pdf");
		
		String reportName = req.getParameter("__report");
		ServletContext sc = req.getSession().getServletContext();
		this.birtReportEngine = BirtEngine.getBirtEngine(sc);
		
		
		
		IReportRunnable design;
		try
		{
			//Open report design
			design = birtReportEngine.openReportDesign( sc.getRealPath("/Reports")+"/"+reportName );
			//create task to run and render report
			IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask( design );		
			task.getAppContext().put("BIRT_VIEWER_HTTPSERVLET_REQUEST", req );			
			
			//set output options HTML
			HTMLRenderOption options = new HTMLRenderOption();
			options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
			options.setOutputStream(resp.getOutputStream());
			options.setImageHandler(new HTMLServerImageHandler());
			options.setBaseImageURL(req.getContextPath()+"/images");
			options.setImageDirectory(sc.getRealPath("/images"));
			

            task.setRenderOption(options);

						
            
			//run report
			task.run();
			OutputStream out=resp.getOutputStream();
			resp.getOutputStream().flush();
			task.close();
		}catch (Exception e){
			
			e.printStackTrace();
			throw new ServletException( e );
		}
		
	}
	
	public void traiterBirtRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String format=req.getParameter("formatRapport");
		
		if(StringUtils.equals(format, "html"))
				displayHtmlFile(req, resp);
		else
			downloadDocFile( req,resp);
	}

}

