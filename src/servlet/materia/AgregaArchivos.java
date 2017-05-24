package servlet.materia;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
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
import util.string.StringUtil;

/**
 * Servlet implementation class AgregaFile
 */
@WebServlet("/AgregaArchivos")
public class AgregaArchivos extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "AgregaArchivos.jsp";
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_CVE_MATERIA = "cveMat";
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_CVE_CONTENIDO = "cveCont";
	/** Nombre del variable **/	
	private final String KEY_VARIABLE_MATERIA = "materia";
	/** Nombre del variable **/
	private final String KEY_VARIABLE_CONTENIDO = "contenido";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_TIPO = "hiddenTipo";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_MATERIA = "hiddenCveMat";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_CONT = "hiddenCveCont";
	/** Nombre del form **/
	private final String VALUE_TIPO_AGREGAR = "1";
	/** Usuario **/
	public Usuario usuario;
	/** Materia **/
	public Materia materia;
	/** Contenido **/
	public TrcnMat contenido;

    /**
     * @see HttpServletHttpServlet()
     */
    public AgregaArchivos() {
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
    				
        			String cveMat = request.getParameter(KEY_REQUEST_PARAM_CVE_MATERIA);
        			this.materia = Materia.Buscar(cveMat, this.usuario.getCveUsu());
        			if(this.materia == null || this.materia.getCveMat().equals("")){
        				return;
        			}
    				
    				Inicializar();
    				SetForm(request, null);
	    			
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
		RequestDispatcher rd = null;	
		MultiPartFormData data = new MultiPartFormData();

		try {
    	
			usuario = new Usuario(request, response, true);
    		if(usuario.IsAutorizado()){
 
    			if(!usuario.isUsuarioNormal()){
    				
    				List<FileItem> items = ParseFormData(request, data);
    				
    				String tipo = data.getParameter(KEY_FORM_HIDDEN_TIPO);
    				if(tipo != null && tipo.equals(VALUE_TIPO_AGREGAR)){
        				Agregar(request, response, items, data);
    				}

    			}else{
					Common.Error(Common.MENSAJE_ERROR_AUTENTIFICA);
					response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
					return;
				}
					
				SetForm(request, data);
				
	    		Configuracion config = new Configuracion();
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathMateria() + NOMBRE_DE_PAGINA);
			    rd.forward(request,response);		
    		}
    		
    	} catch (Exception e) {
			Common.Error(e);
			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}
		
	}	
	
	private List<FileItem> ParseFormData(HttpServletRequest request, MultiPartFormData data) throws FileUploadException {
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Configure a repository (to ensure a secure temp location is used)
		ServletContext servletContext = this.getServletConfig().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		try {
			List<FileItem> items = upload.parseRequest(request);
			List<FileItem> fileItems = new ArrayList<FileItem>();
            // Parse the request  
        	Iterator<FileItem> iterator = items.iterator();
            while (iterator.hasNext()) 
            {  
            	FileItem item = (FileItem) iterator.next();  
                if (!item.isFormField())  
                {  
                	fileItems.add(item);
                }else{
                	data.getParameters().put(item.getFieldName(), item.getString());
                }
            }
            return fileItems;
		} catch (FileUploadException e) {
			throw e;
		}
	}

	/**
	 * Inicializar
	 */
	private void Inicializar() {
		this.materia = null;
		this.contenido = null;
	}

	/**
	 * Agregar permiso
	 * @param request
	 * @throws Exception
	 */
	private void Agregar(HttpServletRequest request, HttpServletResponse response, 
		List<FileItem> items, MultiPartFormData data) throws Exception {
		if(!UploadFile(request, response, items, data)){
			return;
		}

		Connection con = null;			
		try {
		    con = MySqlConnector.getConnection();			
			con.setAutoCommit(false);

			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_ARCHIVO_AGREGAR
							, materia.getCveMat() + " " + contenido.getMenuItem()));
						
			con.commit();

			request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_TERMINAR_PROCESO);

		} catch (SQLException e) {
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

	/**
	 * Copiar archivos al servidor
	 * @param request
	 * @param response
	 * @param items
	 * @param data
	 * @throws Exception
	 */
	private boolean UploadFile(HttpServletRequest request, HttpServletResponse response, List<FileItem> items, MultiPartFormData data) throws Exception {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);  
		response.setContentType("text/html;charset=UTF-8");

	    // Create path components to save the file
		String cveMat = materia.getCveMat();
		String cveContenido = data.getParameter(KEY_FORM_HIDDEN_CVE_CONT);

		if (isMultipart) 
        {
            try 
            {  
                // Parse the request  
            	Iterator<FileItem> iterator = items.iterator();  
                while (iterator.hasNext()) 
                {  
                	String path = materia.GetPathMateriaAbsolute();
                	
                	FileItem item = (FileItem) iterator.next();  
                    if (!item.isFormField())  
                    {  
                        if(item.getSize() > 600000){
                        	request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_FILE_GRANDE);
                        	return false;
                        }
                    	String fileName = item.getName();      
            		    String extension = "";
            		    int i = fileName.lastIndexOf(".");
            		    if (i >= 0) {
            		        extension = fileName.substring(i).toLowerCase();
            		    }
            		    
            		    String tipoArchivo = data.getParameter("tipoArchivo");
            		    path += File.separator + 
            		    		Common.GetNombreCarpeta(tipoArchivo, this.contenido,File.separator);

                        File pathDir = new File(path);  
                        if (!pathDir.exists())  
                        {  
                            pathDir.mkdirs();  
                        }  
            		    
                        fileName = GetFileName(cveMat, cveContenido, tipoArchivo, path) + extension;
                       
                        File uploadedFile = new File(pathDir + File.separator + fileName);  
                        
	                    if(fileName!="") {
	                    	item.write(uploadedFile);  
	                    }
	                }
                }  
            }catch (Exception e){  
            	throw e;
            } 
            
        }
	    return true;
	    			
	}

	/**
	 * Obtener sequencia del archivo
	 * @param path
	 * @return
	 */
	private int GetSequencia(String path) {
		File f = new File(path);
		
        // create new filename filter
        FilenameFilter filter = new FilenameFilter() {
           @Override
           public boolean accept(File dir, String name) {
              if(name.lastIndexOf('.')>0)
              {
                    return true;
              }
              return false;
           }
        };
		
		return f.listFiles(filter).length + 1;
	}


	/**
	 * Obtener el nombre del archivo
	 * @param cveMat
	 * @param cveContenido
	 * @param tipoArchivo
	 * @param path
	 * @return
	 * @throws Exception
	 */
	private String GetFileName(String cveMat, String cveContenido, String tipoArchivo, String path) throws Exception{
		String filename = "";
		String parentContName = this.contenido.getParentContName();
		String prefix = "";
		int unidad = this.contenido.getUnidad();
		int apartado = this.contenido.getApartado();

		int sequencia = 0;

		if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_IMAGEN)){
			sequencia = GetSequencia(path);
    		if(this.contenido.getCaption().equals(Common.CAPTION_CONTENIDO_INTRODUCCION)){
    			return Common.PREFIX_ARCHIVO_CONTENIDO + cveMat + Common.GUION_BAJO + sequencia;

    		}else if(parentContName.indexOf(Common.CAPTION_CONTENIDO_GENERALIDADES)>=0){
    			return Common.PREFIX_GENERALIDADES 
    					+ this.contenido.getCaption().substring(0, 1).toUpperCase()
    					+ this.contenido.getCaption().substring(1, 3).toLowerCase()
    					+ cveMat + Common.GUION_BAJO + sequencia;
    		
    		}else if(unidad > 0){
    			return Common.PREFIX_ARCHIVO_CONTENIDO + cveMat + Common.GUION_BAJO + unidad
    			+ Common.GUION_BAJO + apartado + Common.GUION_BAJO + sequencia;
    		}
	    }
	    else if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_ANIMACION)){
    		prefix = Common.PREFIX_ARCHIVO_CONTENIDO;
	    }
	    else if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_SONIDO)){
    		prefix = Common.PREFIX_ARCHIVO_SONIDO;
	    }
	    else if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_ACTIVIDADES)){
    		prefix = Common.PREFIX_ARCHIVO_ACTIVIDADES;
	    }
	    else if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_ACTIVIDADES_COMPLEMENTARIAS)){
    		prefix = Common.PREFIX_ARCHIVO_ACTIVIDADES_COMPLEMENTARIAS;
	    }
		
	    else if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_CONTENIDO_IMPRIMIBLE)){
    		return Common.PREFIX_ARCHIVO_CONTENIDO_IMPRIMIBLE + cveMat;
	    }	  
	    else if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_PROGRAMA_IMPRIMIBLE)){
	    	return Common.PREFIX_ARCHIVO_PROGRAMA_IMPRIMIBLE + cveMat;
	    	
	    }else if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_FORMULARIO)){
	    	return Common.PREFIX_ARCHIVO_FORMULARIO + cveMat;
	    }

		sequencia = GetSequencia(path);
		filename = prefix + cveMat + Common.GUION_BAJO + unidad
		+ Common.GUION_BAJO + apartado + Common.GUION_BAJO + sequencia;
		
		return filename;
	}

	/**
	 * Set form de la pantalla
	 * @param request
	 * @throws Exception 
	 */
	private void SetForm(HttpServletRequest request, MultiPartFormData data) throws Exception {

		try {
			String cveMat = null;
			String cveCont = null;
			if(data != null){
				cveMat = data.getParameter(KEY_FORM_HIDDEN_CVE_MATERIA);
				cveCont = data.getParameter(KEY_FORM_HIDDEN_CVE_CONT);
			}
			if(cveMat == null){
				cveMat = request.getParameter(KEY_REQUEST_PARAM_CVE_MATERIA);
			}
			this.materia = Materia.Buscar(cveMat, this.usuario.getCveUsu());
			request.setAttribute(KEY_VARIABLE_MATERIA, this.materia);
			
			if(cveCont == null){
				cveCont = request.getParameter(KEY_REQUEST_PARAM_CVE_CONTENIDO);
			}
			if(StringUtil.isNumber(cveCont)){
				this.contenido = TrcnMat.Buscar(Integer.parseInt(cveCont));
				request.setAttribute(KEY_VARIABLE_CONTENIDO, contenido);
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
