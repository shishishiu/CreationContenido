package servlet.materia;

import java.io.*;
import java.sql.Connection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import beans.*;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;

/**
 * Servlet implementation class AgregaFile
 */
@WebServlet("/AgregaArchivo")
public class AgregaArchivo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "AgregaArchivo.jsp";
	/** Nombre del variable **/	
	private final String KEY_HIDDEN_CVE_MATERIA = "hiddenCveMatAgregaArchivo";
	/** Nombre del form del permiso **/
	private final String KEY_HIDDEN_PATH_DIR = "hiddenPathDirAgregaArchivo";
	/** Usuario **/
	public Usuario usuario;

    /**
     * @see HttpServletHttpServlet()
     */
    public AgregaArchivo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;
			
    	try {
    		
    		Configuracion config = new Configuracion();
    		this.usuario = new Usuario(request, response, true);
    		if(this.usuario.IsAutorizado()){
    				    			
    			if(!this.usuario.isUsuarioNormal()){
    				
					rd = getServletConfig().getServletContext().getRequestDispatcher(
							config.getPathMateria() + NOMBRE_DE_PAGINA);
				    rd.forward(request,response);

				}else{
					Common.Error(Common.MENSAJE_ERROR_AUTENTIFICA);
					response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
					return;
				}
    		}

    	} catch (Exception e) {
			Common.Error(e);
			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
    	
			usuario = new Usuario(request, response, true);
    		if(usuario.IsAutorizado()){
 
    			if(!usuario.isUsuarioNormal()){
    				
    				AgregarArchivo(request, response);    				
    		    	

    			}else{

    				Common.Error(Common.MENSAJE_ERROR_AUTENTIFICA);
					response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
					return;
				}
					
    		}
    		
    	} catch (Exception e) {
			Common.Error(e);
			Common.MsgJson("error", Common.MENSAJE_ERROR, response);
		}
		
	}	
	

	private void AgregarArchivo(HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String cveMat = "";
		String pathDirectory = "";

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		List<FileItem> items = upload.parseRequest(request);
		
		Iterator<FileItem> iter = items.iterator();
		List<FileItem> fileItems = new ArrayList<FileItem>();
		while (iter.hasNext()) {
		    FileItem item = iter.next();

		    if (item.isFormField()) {
		    	
		    	if(item.getFieldName().equals(KEY_HIDDEN_CVE_MATERIA)){
		    		cveMat = item.getString();
	    		
		    	}else if(item.getFieldName().equals(KEY_HIDDEN_PATH_DIR)){
		    		pathDirectory = item.getString();
		    	}
		    	
		    } else {
            	fileItems.add(item);
		    }
		}
		
		Materia beanMat = Materia.Buscar(cveMat);
		String pathMat = beanMat.GetPathMateriaAbsolute();
		
		String dirName = pathMat + File.separator + pathDirectory;
				
		if(fileItems != null){

			Connection con = null;			
			try {
			    con = MySqlConnector.getConnection();			
				con.setAutoCommit(false);
			
			
				for(FileItem item : fileItems){
					
					File uploadedFile = new File(dirName + File.separator + item.getName());
					
					item.write(uploadedFile);
	
					Common.InsertLogAct(request, con, usuario.getCveUsu(), 
							MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_ARCHIVO_AGREGAR, dirName + File.separator + item.getName()));
					
				}
			
			con.commit();

			Common.MsgJson("success", Common.MENSAJE_TERMINAR_PROCESO, response);

			} catch (Exception e) {
				if (con != null) {
					con.rollback();
				}			
				throw e;
			} finally{
				if (con != null) {
					con.setAutoCommit(true);
					con.close();
		        }			
			}		

						
		}
				
	}

}


