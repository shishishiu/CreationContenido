package servlet.conf;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import beans.Nivel;
import beans.Usuario;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;
import util.string.StringUtil;

/**
 * Servlet implementation class ModificaPlantilla
 */
@WebServlet("/ModificaPlantilla")
public class ModificaPlantilla extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "ModificaPlantilla.jsp";
	/** Nombre del form de nivel **/
	private final String KEY_VARIABLE_NOMBRE_NIVEL = "nombreNivel";
	/** Nombre del param **/
	private final String KEY_VARIABLE_NIVEL = "nivel";
	/** Nombre del form de niveles **/
	private final String KEY_VARIABLE_TIPO_ARCHIVO = "tipoArchivo";
	/** Nombre del form **/
	private final String KEY_VARIABLE_PUEDE_MOSTRAR = "puedeMostrar";
	/** Nombre del form **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form **/
	private final String KEY_REQUEST_PARAM_NIVEL = "nivel";
	/** Nombre del form **/
	private final String KEY_REQUEST_PARAM_TIPO_ARCHIVO = "tipo";

	/** Nombre del param **/
	private final String KEY_HIDDEN_TIPO_ARCHIVO = "hiddenTipoArchivo";
	/** Nombre del param **/
	private final String KEY_HIDDEN_NIVEL = "hiddenNivel";
	/** Nombre del param**/
	private final String KEY_TIPO_ARCHIVO_ENCABEZADO = "1";
	/** Nombre del param**/
	private final String KEY_TIPO_ARCHIVO_PIE = "2";

	
	
	/** Usuario **/
	public Usuario usuario;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModificaPlantilla() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;
		
    	try {
    		
    		usuario = new Usuario(request, response);
    		if(usuario.IsAutorizado()){

    			if(usuario.isAdministrador() || usuario.isAdministradorGeneral()){
    				
  					request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, true);
        			Iniciar();
        			SetForm(request, true);

    			}else{
   					request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, false);
   					request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_AUTENTIFICA);
    			}

    			
    		    Configuracion config = new Configuracion();
     			rd = getServletConfig().getServletContext().getRequestDispatcher(
    					config.getPathConf() + NOMBRE_DE_PAGINA);
			    rd.forward(request,response);
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

		
		try{
				
			Modificar(request, response);
		
		}catch(Exception e){
			Common.Error(e);
			Common.MsgJson("error", Common.MENSAJE_ERROR, response);
		}

	}
	
	/**
	 * Iniciar
	 */
	private void Iniciar() {
	}

	private void Modificar(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		String strNivel = "";
		String tipoArchivo = "";
		int nivel = 0;
    	FileItem uploaditem = null;
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		List<FileItem> items = upload.parseRequest(request);
		
		Iterator<FileItem> iter = items.iterator();
		while (iter.hasNext()) {
		    FileItem item = iter.next();

		    if (item.isFormField()) {
		    	
		    	if(item.getFieldName().equals(KEY_HIDDEN_NIVEL)){
		    		strNivel = item.getString();
		    		if(StringUtil.isNumber(strNivel)){
		    			nivel = Integer.parseInt(strNivel);
		    		}else{
		    			Common.MsgJson("error", Common.MENSAJE_NO_SE_ELIGE_NIVEL, response);
		    			return;
		    		}
	    		
		    	}else if(item.getFieldName().equals(KEY_HIDDEN_TIPO_ARCHIVO)){
		    		tipoArchivo = item.getString();
		    		
		    	}
		    	
		    } else {
		    	uploaditem = item;
		    }
		}
		
		
		String filepath = "";
		if(tipoArchivo.equals(KEY_TIPO_ARCHIVO_ENCABEZADO)){
			filepath = Common.GetFilePathPlantillaEncabezado(nivel);
		}else if(tipoArchivo.equals(KEY_TIPO_ARCHIVO_PIE)){
			filepath = Common.GetFilePathPlantillaPie(nivel);
		}

		if(filepath.equals("")){
			Common.MsgJson("error", Common.MENSAJE_NO_SE_ENCUENTRA_ARCHIVO, response);
			return;
		}
		
		if(uploaditem != null){
			
			File uploadedFile = new File(filepath);
			
			if(uploadedFile.exists()){
				
				Calendar cal = Calendar.getInstance();
				
				
				File fileDes = new File(filepath + "_" 
				+ cal.get(Calendar.DATE) + (cal.get(Calendar.MONTH)+1) + cal.get(Calendar.YEAR) + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND));

				uploadedFile.renameTo(fileDes);
				
			}
			
			uploaditem.write(uploadedFile);
			
		}
		
		
		Connection con = null;			
		try {
		    con = MySqlConnector.getConnection();			
			con.setAutoCommit(false);

			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_PLANTILLA_MODIFICAR, filepath));
						
			con.commit();

			Common.MsgJson("success", Common.MENSAJE_TERMINAR_PROCESO, response);

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

	private void SetForm(HttpServletRequest request, boolean isGet) throws Exception {
		try {
			
			String strNivel = request.getParameter(KEY_REQUEST_PARAM_NIVEL);
			String strTipo = request.getParameter(KEY_REQUEST_PARAM_TIPO_ARCHIVO);
			int nivel;
			if(!StringUtil.isNumber(strNivel)){
				return;
			}
			
			nivel = Integer.parseInt(strNivel);
			Nivel beanNivel = Nivel.Buscar(nivel);
			request.setAttribute(KEY_VARIABLE_NOMBRE_NIVEL, beanNivel.getNomNivel());
			request.setAttribute(KEY_VARIABLE_TIPO_ARCHIVO, strTipo);
			request.setAttribute(KEY_VARIABLE_NIVEL, strNivel);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
}
