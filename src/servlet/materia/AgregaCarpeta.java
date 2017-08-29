package servlet.materia;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import beans.*;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;

/**
 * Servlet implementation class AgregaFile
 */
@WebServlet("/AgregaCarpeta")
public class AgregaCarpeta extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "AgregaCarpeta.jsp";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_NOMBRE_CARPETA = "valNombreCarpeta";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_DISABLED = "disabled";
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_TIPO_ARCHIVO = "tipoArchivo";
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_TIPO_AGREGAR = "tipoAgregar";
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_CVE_MAT = "cveMat";
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_CVE_CONT = "cveCont";
	/** Nombre del variable **/	
	private final String KEY_POST_PARAM_NOMBRE_CARPETA = "nombreCarpeta";
	/** Nombre del variable **/	
	private final String KEY_POST_PARAM_CVE_MAT = "cveMat";
	/** Nombre del variable **/	
	private final String KEY_POST_PARAM_PATH_DIR = "pathDirectory";

	/** Usuario **/
	private Usuario usuario;
	private Configuracion config = new Configuracion();

    /**
     * @see HttpServletHttpServlet()
     */
    public AgregaCarpeta() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;
			
    	try {
    		
    		this.usuario = new Usuario(request, response, true);
    		if(this.usuario.IsAutorizado()){
    				    			
    			if(!this.usuario.isUsuarioNormal()){
    				
    				SetForm(request, null);
	    			
					rd = getServletConfig().getServletContext().getRequestDispatcher(
							this.config.getPathMateria() + NOMBRE_DE_PAGINA);
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
    				
    				AgregarCarpeta(request, response);
    				

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
	

	/**
	 * Agregar permiso
	 * @param request
	 * @throws Exception
	 */
	private void AgregarCarpeta(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String nombreNewCarpeta = request.getParameter(KEY_POST_PARAM_NOMBRE_CARPETA);
		String cveMat = request.getParameter(KEY_POST_PARAM_CVE_MAT);
		String pathDirectory = request.getParameter(KEY_POST_PARAM_PATH_DIR);
		Materia beanMat = Materia.Buscar(cveMat);
		String pathMat = beanMat.GetPathMateriaAbsolute();
		
		String dirName = pathMat + File.separator + pathDirectory;
		File dir = new File(dirName);
		if(!dir.exists()){
			
			FileUtils.forceMkdir(dir);
			
		}
		
		File newfile = new File(dirName + File.separator + nombreNewCarpeta);

		if(newfile.exists()){
	    	Common.MsgJson("error", Common.MENSAJE_ERROR_FILE_EXIST, response);
	    	return;		
		}
		
	    if (newfile.mkdir()){
	    }else{
	    	Common.MsgJson("error", Common.MENSAJE_ERROR_FILE_NO_CREAR, response);
	    	return;
	    }
		
    	Common.MsgJson("success", "", response);
		
		Connection con = null;			
		try {
		    con = MySqlConnector.getConnection();			
			con.setAutoCommit(false);

			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_CARPETA_AGREGAR
							, cveMat + " " + nombreNewCarpeta));
						
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
	 * Set form de la pantalla
	 * @param request
	 * @throws Exception 
	 */
	private void SetForm(HttpServletRequest request, MultiPartFormData data) throws Exception {

		try {
			
			String tipoAgregar = request.getParameter(KEY_REQUEST_PARAM_TIPO_AGREGAR);
			if(tipoAgregar.equals("1")){
				
				request.setAttribute(KEY_VARIABLE_DISABLED, "disabled=disabled");
				request.setAttribute(KEY_VARIABLE_NOMBRE_CARPETA, CreateNombreCarpeta(request));
			
			} else{
				request.setAttribute(KEY_VARIABLE_DISABLED, "");
				
			}
			
			
		} catch (Exception e) {
			throw e;
		}
	}

	private String CreateNombreCarpeta(HttpServletRequest request) throws Exception {
		
		String nombreCarpeta  ="";
		String tipoArchivo = request.getParameter(KEY_REQUEST_PARAM_TIPO_ARCHIVO);
		String currentDirPrefix = "";
		String rootDir = "";
		String cveMat = request.getParameter(KEY_REQUEST_PARAM_CVE_MAT);
		String cveCont = request.getParameter(KEY_REQUEST_PARAM_CVE_CONT);
		
		if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_ANIMACION)){
			rootDir = Common.CARPETA_HTML5;
			currentDirPrefix = Common.PREFIX_ARCHIVO_CONTENIDO;
		
		}else if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_ACTIVIDADES_COMPLEMENTARIAS)){
			rootDir = Common.CARPETA_ACTIVIDADES_COMPLEMENTARIAS;
			currentDirPrefix = Common.PREFIX_ARCHIVO_ACTIVIDADES_COMPLEMENTARIAS;
		
		}else if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_HTML5)){
			rootDir = Common.CARPETA_HTML5;
			currentDirPrefix = Common.PREFIX_ARCHIVO_CONTENIDO;
		}

		
		Materia materia = Materia.Buscar(cveMat);
		TrcnMat contenido = TrcnMat.Buscar(Integer.parseInt(cveCont));
		String pathMat = materia.GetPathMateriaAbsolute();
		int sequencia = GetSequencia(pathMat + File.separator + rootDir);
		nombreCarpeta = currentDirPrefix + materia.getCveMat() + Common.GUION_BAJO + contenido.getUnidad()
				+ Common.GUION_BAJO + contenido.getApartado() + Common.GUION_BAJO + sequencia;	
		
		return nombreCarpeta;
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
                    return false;
              }
              return true;
           }
        };
		
        if(f.listFiles(filter) == null){
        	return 1;
        }
               
		return f.listFiles(filter).length + 1;
	}


}
