package servlet.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
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
@WebServlet("/VerPlantilla")
public class VerPlantilla extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "VerPlantilla.jsp";
	/** Nombre del form **/
	private final String KEY_FORM_NIVEL = "nivel";
	/** Nombre del form **/
	private final String KEY_VARIABLE_NIVELES = "niveles";
	/** Nombre del form **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form **/
	private final String KEY_VARIABLE_PUEDE_MOSTRAR = "puedeMostrar";
	/** Nombre del param **/
	private final String KEY_HIDDEN_TIPO_ARCHIVO = "hiddenTipoArchivo";
	/** Nombre del param **/
	private final String KEY_HIDDEN_TIPO = "hiddenTipo";
	/** Nombre del param**/
	private final String KEY_TIPO_ARCHIVO_ENCABEZADO = "1";
	/** Nombre del param**/
	private final String KEY_TIPO_ARCHIVO_PIE = "2";
	/** Nombre del param**/
	private final String KEY_TIPO_DOWNLOAD = "1";
	/** Usuario **/
	public Usuario usuario;
	/** Lista del nivel **/
	private List<Nivel> listaNivel;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerPlantilla() {
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
	    			SetForm(request);

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
		RequestDispatcher rd = null;	
		
		try {
    	
			usuario = new Usuario(request, response);
    		if(usuario.IsAutorizado()){
    			if(usuario.isAdministrador() || usuario.isAdministradorGeneral()){

    				String tipo = request.getParameter(KEY_HIDDEN_TIPO);
    				if(tipo.equals(KEY_TIPO_DOWNLOAD)){
    					
    					Download(request, response);

    				}
    				
    				SetForm(request);

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
	
		
	private void Download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String strNivel = request.getParameter(KEY_FORM_NIVEL);
		String tipo = request.getParameter(KEY_HIDDEN_TIPO_ARCHIVO);
		
		int nivel = 0;
		if(util.string.StringUtil.isNumber(strNivel)){
			nivel = Integer.parseInt(strNivel);
		}else{
			request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_NO_SE_ELIGE_NIVEL);
			return;
		}
		
		String filepath = "";
//		String pathWebInf = this.getServletContext().getRealPath(Common.PATH_WEB_INF);
		Configuracion conf = new Configuracion();
		String pathWebInf = conf.getAbsolutePath() + File.separator + conf.getPlantillas();
		if(tipo.equals(KEY_TIPO_ARCHIVO_ENCABEZADO)){
			filepath = Common.GetFilePathPlantillaEncabezado(nivel);
		}else if(tipo.equals(KEY_TIPO_ARCHIVO_PIE)){
			filepath = Common.GetFilePathPlantillaPie(nivel);
		}

		try {
			
			ExecDownload(request, response, filepath);
			
		} catch (Exception e) {
			throw e;
		}
		
	}

	
	private void ExecDownload(HttpServletRequest request, HttpServletResponse response, String filepath) throws Exception{
		
		FileInputStream fi = null;
		InputStreamReader isr = null;
		
		OutputStream ops = null;
		OutputStreamWriter osw = null;
		
		try{
			
			File file = new File(filepath);
			if(!file.exists()){
				
				request.setAttribute(KEY_VARIABLE_MESSAGE, MessageFormat.format(Common.MENSAJE_NO_SE_ENCUENTRA_ARCHIVO, filepath));
				return;
			}
			
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		 
			
			fi = new FileInputStream(file);
			isr = new InputStreamReader(fi, "ISO-8859-1");
		 
			ops = response.getOutputStream();
			osw = new OutputStreamWriter(ops, "ISO-8859-1");
		 
			
			int i;
			while ((i = isr.read()) != -1) {
				osw.write(i);
			}
		} catch (FileNotFoundException e) {
			
		} catch (UnsupportedEncodingException e) {
			
		} catch (IOException e) {
			
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
				if (ops != null) {
					ops.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (fi != null) {
					fi.close();
				}
			} catch (IOException e) {
			}
		}
		
	}
	
	/**
	 * Iniciar
	 */
	private void Iniciar() {
		this.listaNivel = null;
	}

	private void SetForm(HttpServletRequest request) throws Exception {
		try {
			
			if(this.listaNivel == null){
				this.listaNivel = CrearComboNivel();
			}
			request.setAttribute(KEY_VARIABLE_NIVELES, this.listaNivel);
			String nivel = "";
			nivel = request.getParameter(KEY_FORM_NIVEL);
			SetSelected(nivel);

		} catch (Exception e) {
			throw e;
		}
		
	}

	/**
	 * Crear Combo de nivel
	 * @return
	 * @throws Exception 
	 */
	private List<Nivel> CrearComboNivel() throws Exception {
		try {
			this.listaNivel = Nivel.BuscarTodo();
			return this.listaNivel;
		} catch (Exception e) {
			throw e;
		}		
	}

	/**
	 * Colocar "selected" en Combo de Nivel
	 * @param nivel
	 */
	private void SetSelected(String nivel){
		if(StringUtil.isNumber(nivel)){
			for(int i=0; i <= listaNivel.size()-1; i++){
				if(((Nivel)listaNivel.get(i)).getCveNivel() == Integer.parseInt(nivel)){
					((Nivel)listaNivel.get(i)).setSelected("selected");
				} else {
					((Nivel)listaNivel.get(i)).setSelected("");
				}
			}
		}
	}


}
