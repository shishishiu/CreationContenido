package servlet.materia;

import java.io.*;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.TrcnMat;
import beans.Materia;
import beans.PermisoContenido;
import beans.Usuario;
import util.common.Common;
import util.conf.CkEditorPathBuilder;
import util.conf.Configuracion;
import util.db.MySqlConnector;
import util.string.StringUtil;

/**
 * Servlet implementation class BusContenido
 */
@WebServlet("/ModificaContenido")
public class ModificaContenido extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String NOMBRE_DE_PAGINA = "ModificaContenido.jsp";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_MATERIA = "materia";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_CONTENIDO = "contenido";
	/** Nombre del key **/
	private final String KEY_VARIABLE_TITLE = "title";
	/** Nombre del key **/
	private final String KEY_VARIABLE_CONTENIDO_HTML = "contenidoHtml";
	/** Nombre del key **/
	private final String KEY_VARIABLE_CAPTION_INTRODUCCION = "capIntroduccion";
	/** Nombre del key **/
	private final String KEY_VARIABLE_URL_IMG = "urlImg";
	/** Nombre del key **/
	private final String KEY_VARIABLE_URL_FLASH = "urlFlash";
	/** Nombre del form del usuario **/
	private final String KEY_VARIABLE_USUARIO = "usuario";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PUEDE_MOSTRAR = "puedeMostrar";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_MAT = "hiddenCveMat";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_CONTENIDO = "hiddenCveCont";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_TIPO = "hiddenTipo";
	/** Nombre del form **/
	private final String KEY_FORM_NOM_CONTENIDO = "nomContenido";
	/** Nombre del form **/
	private final String KEY_FORM_CONTENIDO = "contenido";
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_CVE_CONT = "cveCont";
	/** Value del title **/
	private final String VALUE_TITLE_MODIFICAR = "Modificar Contenido";
	/** Nombre del form **/
	private final String VALUE_TIPO_GUARDAR = "1";
	/** Nombre del form **/
	private final String KEY_VARIABLE_EDITOR_CONFIG_SOURCE = "editorConfigSource";

	
	/** Usuario **/
	public Usuario usuario;
	/** Materia **/
	public Materia materia;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModificaContenido() {
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
   				Inicializar();
   				
   				
   				request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, true);
   				if(!TieneAutoridad(request)){
   					request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, false);
   				}
   				   				
    			SetForm(request);
	
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
    	
			usuario = new Usuario(request, response);
    		if(usuario.IsAutorizado()){
    			request.setAttribute(KEY_VARIABLE_USUARIO, usuario);

   				request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, true);
   				if(!TieneAutoridad(request)){
   					request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, false);
   				
   				} else{

	    			String tipo = request.getParameter(KEY_FORM_HIDDEN_TIPO);
					if(tipo != null && tipo.equals(VALUE_TIPO_GUARDAR)){
						
//						String err = Validar(request);
//						if(err.length() <= 0){
							Guardar(request);
//						}else{
//							Common.MsgJson("error", err, response);
//						}
						
					}				
   				}
				
   				SetForm(request);
				
//				rd = getServletConfig().getServletContext().getRequestDispatcher(
//						Configuracion.getPathMateria() + NOMBRE_DE_PAGINA);
//			    rd.forward(request,response);		
    		}
    		
    	} catch (Exception e) {

    		Common.Error(e);
    		
    		if(Common.IsAjax(request)){
    			Common.MsgJson("error", Common.MENSAJE_ERROR, response);
    		}else{
    			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
    		}
    		
		}
	}

	private String Validar(HttpServletRequest request) throws Exception {

		String strCveContenido = request.getParameter(KEY_FORM_HIDDEN_CVE_CONTENIDO);
		if(!StringUtil.isNumber(strCveContenido)){
			return Common.MENSAJE_NO_MODIFICAR;
		}
		
		String err = "";
		
		int cveCont = Integer.parseInt(strCveContenido);
		
		try {
			
			TrcnMat bean = TrcnMat.Buscar(cveCont);
			int unidad = bean.getUnidad();
			int apartado = bean.getApartado();
	        String strContenido = request.getParameter(KEY_FORM_CONTENIDO);

	        String unidadApartado = "([^" + unidad + "]" + "_" + "[^" + apartado + "]|" 
	        + unidad + "_" + "[^" + apartado + "]|"
	        + "[^" + unidad + "]" + "_" + apartado + ")";
	        
			String cveMat = bean.getCveMat();
			String[] arrStrRegex = new String[5];
			arrStrRegex[0] = "(" + cveMat + File.separator + Common.CARPETA_IMG + File.separator + "[^" + unidad + "]" + File.separator 
					+  Common.PREFIX_ARCHIVO_CONTENIDO + cveMat + "_" + unidadApartado + "_[0-9]+" + "(.bmp|.gif|.jpeg|.jpg|.png)" + ")";
			
			arrStrRegex[1] = "(" + cveMat + File.separator + Common.CARPETA_HTML5 + File.separator 
        			+ ".*" + "_" + unidadApartado + ")";
			
			arrStrRegex[2] = "(" + cveMat + File.separator + Common.CARPETA_SONIDO + File.separator 
        			+ Common.PREFIX_ARCHIVO_SONIDO + cveMat + "_" + unidadApartado + "_[0-9]+" + "(.mp3|.wav|.aiff|.wma|.m4a)" + ")";
			
			arrStrRegex[3] = "(" + cveMat + File.separator + Common.CARPETA_ACTIVIDADES + File.separator 
					+ Common.PREFIX_ARCHIVO_ACTIVIDADES + cveMat + "_" + unidadApartado + "_[0-9]+" + ".doc)";
			
			arrStrRegex[4] = "(" + cveMat + File.separator + Common.CARPETA_ACTIVIDADES_COMPLEMENTARIAS + File.separator 
        			+ Common.PREFIX_ARCHIVO_ACTIVIDADES_COMPLEMENTARIAS + cveMat + "_" + unidadApartado + "_[0-9]+" + ".doc" + ")";

			for(String regex : arrStrRegex){
				
				regex = regex.replaceAll(Matcher.quoteReplacement(File.separator), "/");
		        Pattern p = Pattern.compile(regex);
		        Matcher m = p.matcher(strContenido);
		        int end = 0;
		        int lengthContenido = strContenido.length();
		        
		        do{
		        	
			        if (m.find(end)){
			        	String s = m.group (1);
			        	err += s + Common.NEW_LINE; 
//			        	Common.LogInfo(s);
			        	
			        	end = m.end();
			        	
			        } else{
			        	break;
			        }
		        	
		        } while(lengthContenido > end);
				
			}
	        			
		} catch (Exception e) {
			throw e;
		}
			
		if(err.length() > 0){
			
			err = Common.MENSAJE_NO_MODIFICAR + Common.NEW_LINE + Common.MENSAJE_NO_MODIFICAR_CONTENIDO_POR_UNIDAD_APARTADO 
					+ Common.NEW_LINE + err;
			
		}
		
		return err;
	}

	/**
	 * Guardar el contenido
	 * @param request
	 * @throws Exception
	 */
	private void Guardar(HttpServletRequest request) throws Exception {
		String cveContenido = request.getParameter(KEY_FORM_HIDDEN_CVE_CONTENIDO);
		try {
			Modificar(request, cveContenido);
		} catch (Exception e) {
			throw e;
		}				
	}

	/**
	 * Actualizar base de datos
	 * @param request
	 * @param cveContenido
	 * @throws Exception
	 */
	private void Modificar(HttpServletRequest request, String cveContenido) throws Exception{
		Connection con;
		con = MySqlConnector.getConnection();
		try {
			con.setAutoCommit(false);
			TrcnMat bean = new TrcnMat();
			bean.setCveMat(request.getParameter(KEY_FORM_HIDDEN_CVE_MAT));
			bean.setMenuItem(Integer.parseInt(cveContenido));
			bean.setCaption(request.getParameter(KEY_FORM_NOM_CONTENIDO));

			Clob myClob = con.createClob();
	        String strContenido = request.getParameter(KEY_FORM_CONTENIDO);
	        myClob.setString(1, strContenido);			
			bean.setContenido(myClob);

			bean.Modificar(con);

			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_MODIFICAR,cveContenido));
			
			
//			if(!CrearArchivoJSP(Integer.parseInt(cveContenido))){
//				con.rollback();
//				return;
//			}
			
			con.commit();
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
	 * Inicializar
	 */
	private void Inicializar() {
		this.materia = null;
	}

	/**
	 * Confirmar si tiene acceso
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private boolean TieneAutoridad(HttpServletRequest request) throws Exception {

		String cveContenido = request.getParameter(KEY_REQUEST_PARAM_CVE_CONT);
		if(cveContenido == null){
			cveContenido = request.getParameter(KEY_FORM_HIDDEN_CVE_CONTENIDO);
		}
		
		TrcnMat trcnBean = new TrcnMat();
		if(StringUtil.isNumber(cveContenido)){
			trcnBean = TrcnMat.Buscar(Integer.parseInt(cveContenido));
		}			
		
		String cveMat = trcnBean.getCveMat();
		if(cveMat == null || cveMat.equals("")){
			return false;
		}
		
		try {
			if(usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR 
					|| usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR_GENERAL){

				return true;
			}
			
			PermisoContenido bean = new PermisoContenido();
			bean.setCveUsu(usuario.getCveUsu());
			bean.setCveMat(cveMat);
			List<PermisoContenido> list = bean.Buscar();
			
			Materia matBean = Materia.Buscar(cveMat);
			
			if(list.size() <= 0){
				request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_AUTENTIFICA);
				return false;
			}else if(list.get(0).getPermiso() == Common.PERMISO_CONTENIDO_MODIFICAR){

				int estadoSolicitud = matBean.getMatSol().getEstadoSolicitud();
				switch (usuario.getPerUsu()) {
				case Common.PERMISO_ASESOR:
					if(estadoSolicitud != Common.ESTADO_DE_SOLICITUD_PENDIENTE){
						request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_NO_MODIFICAR);
						return false;
					}
					break;
				case Common.PERMISO_REVISOR:
					if(estadoSolicitud != Common.ESTADO_DE_SOLICITUD_SOLICITADO){
						request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_NO_MODIFICAR);
						return false;
					}					
					break;
				case Common.PERMISO_ADMINISTRADOR:
					if(estadoSolicitud != Common.ESTADO_DE_SOLICITUD_REVISADO){
						request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_NO_MODIFICAR);
						return false;
					}					
					break;
				default:
					break;
				}
				
				return true;
				
			}else if(list.get(0).getPermiso() == Common.PERMISO_CONTENIDO_VER){
				request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_NO_MODIFICAR);
				return false;
				
			}else{
				request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_AUTENTIFICA);
				return false;
			}
		} catch (Exception e) {
			throw e;
		}
		
	}

	private void SetForm(HttpServletRequest request) throws Exception {
		try {

			String cveContenido = request.getParameter(KEY_REQUEST_PARAM_CVE_CONT);
			if(cveContenido == null){
				cveContenido = request.getParameter(KEY_FORM_HIDDEN_CVE_CONTENIDO);
			}
			
			TrcnMat bean = TrcnMat.Buscar(Integer.parseInt(cveContenido));
			
			request.setAttribute(KEY_VARIABLE_CONTENIDO, bean);
			request.setAttribute(KEY_VARIABLE_CONTENIDO_HTML, 
					java.net.URLEncoder.encode(bean.getContenido(), "ISO-8859-1"));
			request.setAttribute(KEY_VARIABLE_TITLE, VALUE_TITLE_MODIFICAR);
	
			if(this.materia == null){
				this.materia = Materia.Buscar(bean.getCveMat());
			}
			request.setAttribute(KEY_VARIABLE_MATERIA, this.materia);
			
			request.setAttribute(KEY_VARIABLE_CAPTION_INTRODUCCION, Common.CAPTION_CONTENIDO_INTRODUCCION);
			
			String imgurl = Common.SLASH + 
					Common.GetNombreCarpeta(Common.VALUE_TIPO_ARCHIVO_IMAGEN, bean, Common.SLASH) + 
					Common.SLASH;
			
			request.setAttribute(KEY_VARIABLE_URL_IMG, imgurl);
			request.setAttribute(KEY_VARIABLE_URL_FLASH, Common.SLASH + Common.CARPETA_HTML5 + Common.SLASH);
			
			String editorConfigSource = ",Source";
			String allowedContent = "";
			if(usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR_GENERAL){
				editorConfigSource = "";
				allowedContent = "extraAllowedContent:'script', allowedContent:true,";
			}
			request.setAttribute(KEY_VARIABLE_EDITOR_CONFIG_SOURCE, editorConfigSource);
			request.setAttribute("ac", allowedContent);

			
			HttpSession sesion = request.getSession(true);			
 			sesion.setAttribute(CkEditorPathBuilder.SESSION_CKEDITOR_CONT_ABSOLUTE_PATH,materia.GetPathMateriaAbsolute());
 			sesion.setAttribute(CkEditorPathBuilder.SESSION_CKEDITOR_CONT_URL,materia.GetPathMateriaRelative());

			
		} catch (Exception e) {
			throw e;
		}
		
	}

}
