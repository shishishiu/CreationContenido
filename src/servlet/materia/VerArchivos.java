package servlet.materia;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.*;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;
import util.string.StringUtil;

/**
 * Servlet implementation class AgregaFile
 */
@WebServlet("/VerArchivos")
public class VerArchivos extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "VerArchivos.jsp";
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
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PATH_DIR = "pathDirectory";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_ROOT_DIR = "rootDir";
	/** Nombre del form **/
	private final String VALUE_TIPO_CARGAR = "1";
	/** Nombre del form **/
	private final String VALUE_TIPO_DELETE = "2";
	/** Nombre del form **/
	private final String VALUE_TIPO_CAMBIAR_TIPO_ARCHIVO = "3";
	/** Nombre del form **/
	private final String KEY_POST_PARAM_TIPO = "tipo";
	/** Nombre del variable **/	
	private final String KEY_POST_PARAM_CVE_MATERIA = "cveMat";
	/** Nombre del variable **/	
	private final String KEY_POST_PARAM_SELECTED_NOMBRE_ARCHIVO_CARPETA = "selectedName";
	/** Usuario **/
	public Usuario usuario;

    /**
     * @see HttpServletHttpServlet()
     */
    public VerArchivos() {
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

		try {
    	
			usuario = new Usuario(request, response, true);
    		if(usuario.IsAutorizado()){
 
    			if(!usuario.isUsuarioNormal()){
    				
    				String tipo = request.getParameter(KEY_POST_PARAM_TIPO);
    				if(tipo != null && tipo.equals(VALUE_TIPO_CARGAR)){
    					
        				CargarFileList(request, response);
        				
    				} else if(tipo != null && tipo.equals(VALUE_TIPO_DELETE)){
    					
    					Delete(request, response);
    					
    				} else if(tipo != null && tipo.equals(VALUE_TIPO_CAMBIAR_TIPO_ARCHIVO)){
					
    					CambiarTipoArchivo(request, response);
					
    				}

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
	
	private void CambiarTipoArchivo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String cveMat = request.getParameter(KEY_POST_PARAM_CVE_MATERIA);
		String tipoArchivo = request.getParameter("tipoArchivo");
		String rootDir = "";

		
		if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_ANIMACION)){
			rootDir = Common.CARPETA_HTML5;
		
		}else if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_ACTIVIDADES_COMPLEMENTARIAS)){
			rootDir = Common.CARPETA_ACTIVIDADES_COMPLEMENTARIAS;
		
		}else if(tipoArchivo.equals(Common.VALUE_TIPO_ARCHIVO_HTML5)){
			rootDir = Common.CARPETA_HTML5;
		}

		Materia materia = Materia.Buscar(cveMat);
		String pathMat = materia.GetPathMateriaAbsolute();
		List<File> list = CreateFileList(pathMat + File.separator + rootDir);

		String html = CreateHtmlFileList(list, rootDir);
		Common.MsgJson("success", html, response);
		
	}

	private void Delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cveMat = request.getParameter(KEY_POST_PARAM_CVE_MATERIA);
		String borrarNombre = request.getParameter(KEY_POST_PARAM_SELECTED_NOMBRE_ARCHIVO_CARPETA);
		String pathDirectory = request.getParameter("pathDirectory");

		Materia materia = Materia.Buscar(cveMat);
		
		String dirName = materia.GetPathMateriaAbsolute() + File.separator + pathDirectory + File.separator + borrarNombre;
		
		File dir = new File(dirName);
		
		if(!dir.exists()){
	    	Common.MsgJson("error", Common.MENSAJE_ERROR_FILE_NO_EXIST, response);
		    return;			
		}

		recursiveDelete(dir, response);

    	Common.MsgJson("success", "", response);
		
		Connection con = null;			
		try {
		    con = MySqlConnector.getConnection();			
			con.setAutoCommit(false);

			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_CARPETA_ARCHIVO_BORRAR
							, dirName));
						
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
	
	private static void recursiveDelete(final File file, HttpServletResponse response) throws Exception {
	    if (!file.exists()) {
	        return;
	    }
	    if (file.isDirectory()) {
	        for (File child : file.listFiles()) {
	            recursiveDelete(child, response);
	        }
	    }
	    
	    if(!file.delete()){
	    	Common.MsgJson("error", Common.MENSAJE_ERROR_FILE_NO_PUEDE_BORRAR, response);
	    	return;
	    }
	}
	

	private void CargarFileList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String cveMat = request.getParameter(KEY_POST_PARAM_CVE_MATERIA);
		Materia materia = Materia.Buscar(cveMat);
		
		String pathDirectory = request.getParameter("pathDirectory");
		String dirName = materia.GetPathMateriaAbsolute() + File.separator + pathDirectory;
		
		List<File> list = CreateFileList(dirName);
		String html = CreateHtmlFileList(list, pathDirectory);
				
		Common.MsgJson("success", html, response);

	}
	
	
	private String CreatePathDirHtml(String pathDirectory) {
	
		String[] arrStr = pathDirectory.split(Pattern.quote(File.separator));
		String html = "";
		int idx = 0;
		
		for(String dir : arrStr){
			
			if(!html.equals("")){
				html += " \\ ";
			}
			
			html += "<a href='javascript:void(0)' onclick='funcAbrirCarpeta(" + idx + ", \"\")'>" + dir + "</a>";
			
			idx++;
		}
		
		return "<div id='divPathDir'>" + html + "<input type='hidden' id='pathDirectory' value='" + pathDirectory + "'/></div>";

	}

	private String CreateHtmlFileList(List<File> list, String pathDirectory){
		
		String html = "";
		
		html = CreatePathDirHtml(pathDirectory);
		
		html += "<div id='filelist'>";
		
		for(File file : list){
			
    		if(file.isFile()){
    			html += "<div class='line'><div class='filename'>" + file.getName() + "</div></div>";
    		}
    		
    		else if(file.isDirectory()){
    			html += "<div class='line folder'><a><i class='material-icons'>folder</i><div class='foldername'>" + file.getName() + "</div></a></div>";
    			
    		}
    		
    		html += "<br>";
		
		}
		
		html += "<script>$('.folder').dblclick(function() {funcAbrirCarpeta(" + pathDirectory.split(Pattern.quote(File.separator)).length + ",$('.foldername', this).html());});</script></div>";

		
		return html;
	}


	/**
	 * Set form de la pantalla
	 * @param request
	 * @throws Exception 
	 */
	private void SetForm(HttpServletRequest request, MultiPartFormData data) throws Exception {

		try {
			String cveMat = request.getParameter(KEY_REQUEST_PARAM_CVE_MATERIA);
			String strcveCont = request.getParameter(KEY_REQUEST_PARAM_CVE_CONTENIDO);

			Materia materia = Materia.Buscar(cveMat, this.usuario.getCveUsu());
			request.setAttribute(KEY_VARIABLE_MATERIA, materia);
			
			if(StringUtil.isNumber(strcveCont)){
				int cveCont = Integer.parseInt(strcveCont);
				TrcnMat contenido = TrcnMat.Buscar(cveCont);
				request.setAttribute(KEY_VARIABLE_CONTENIDO, contenido);
			} else{
				return;
			}
			
			String rootDir = Common.CARPETA_HTML5;

			request.setAttribute(KEY_VARIABLE_ROOT_DIR, rootDir);
			
			String pathMat = materia.GetPathMateriaAbsolute();
			List<File> list = CreateFileList(pathMat + File.separator + rootDir);
			request.setAttribute("fileList", list);
			
			request.setAttribute(KEY_VARIABLE_PATH_DIR, rootDir);
			
			
		} catch (Exception e) {
			throw e;
		}
	}

	private List<File> CreateFileList(String dirPath) throws Exception {		
		File dir = new File(dirPath);
		
		File[] filelist = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String name) {
				if(name.lastIndexOf('.')>0)
	              {
	                    return true;
	              }
	              return false;
	        }
		});
		File[] folderlist = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String name) {
				if(name.lastIndexOf('.')<=0)
	              {
	                    return true;
	              }
	              return false;
	        }
		});
				
		List<File> list = new ArrayList<File>();
		
		if(folderlist != null){
			Arrays.sort(folderlist);
			for(File f : folderlist){
				list.add(f);
			}
		}
		if(filelist != null){
			Arrays.sort(filelist);
			for(File f : filelist){
				list.add(f);
			}
		}

		return list;
	}

}


