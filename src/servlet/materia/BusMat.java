package servlet.materia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.*;
import util.common.Common;
import util.common.CustomException;
import util.conf.Configuracion;
import util.db.MySqlConnector;
import util.string.StringUtil;

import org.apache.commons.lang.StringEscapeUtils;
/**
 * Servlet implementation class BusMat
 */
@WebServlet("/BusMat")
public class BusMat extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Lista del nivel **/
	private List<Nivel> listaNivel;
	/** Lista del solicitud **/
	private List<MstCode> listaSolicitud;
	/** Nombre del form de nivel **/
	private final String KEY_FORM_NIVEL = "nivel";
	/** Nombre del form de solicitudd **/
	private final String KEY_FORM_SOLICITUD = "solicitud";
	/** Nombre del form de nivel **/
	private final String KEY_VARIABLE_EXIST_RESULTADO = "existResultado";
	/** Nombre del form de niveles **/
	private final String KEY_VARIABLE_NIVELES = "niveles";
	/** Nombre del form de materias **/
	private final String KEY_VARIABLE_MATERIAS = "materias";
	/** Nombre del form del clave **/
	private final String KEY_VARIABLE_CLAVE = "clave";
	/** Nombre del form del nombre **/
	private final String KEY_VARIABLE_NOMBRE = "nombre";
	/** Nombre del form del clave **/
	private final String KEY_VARIABLE_MODULO = "modulo";
	/** Nombre del form del nombre **/
//	private final String KEY_VARIABLE_UNIDAD = "unidad";
	/** Nombre del form de nombre **/
	private final String KEY_VARIABLE_SOLICITUDES = "solicitudes";
	/** Nombre del form del usuario **/
	private final String KEY_VARIABLE_USUARIO = "usuario";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PERMISO_ADMINISTRADOR_GENERAL = "perAdminGeneral";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PERMISO_ADMINISTRADOR = "perAdmin";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PERMISO_ASESOR = "perAsesor";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PERMISO_REVISOR = "perRevisor";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PERMISO_USUARIO_NORMAL = "perUsuario";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_ESTADO_DE_SOLICITUD_PENDIENTE = "pendiente";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_ESTADO_DE_SOLICITUD_SOLICITADO = "solicitado";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_ESTADO_DE_SOLICITUD_REVISADO = "revisado";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_ESTADO_DE_SOLICITUD_VALIDADO = "validado";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_ESTADO_DE_SOLICITUD_LIBERADO = "liberado";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_ESTADO_DE_SOLICITUD_PRODUCCION = "produccion";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_NUMERO_TOTAL = "numtotal";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PAGINAS = "paginas";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_CURRENT_PAGINA = "currentPagina";
	/** Nombre del form del cveMat **/
	private final String KEY_REQUEST_PARAM_CVE_MAT = "cveMat";
	/** Nombre del param de dar de baja **/
	private final String KEY_HIDDEN_TIPO = "hiddenTipo";
	/** Nombre del param de dar de baja **/
	private final String KEY_TIPO_BAJA = "1";
	/** Nombre del param de crear jsp **/
	private final String KEY_TIPO_CREAR_JSP = "2";
	/** Nombre del param de dar de baja **/
	private final String KEY_TIPO_EXPORTAR_VALIDACION = "3";
	/** Nombre del param de crear jsp **/
	private final String KEY_TIPO_EXPORTAR = "4";
	/** Nombre del param**/
	private final String KEY_TIPO_BUSCAR = "5";
	/** Nombre del param**/
	private final String KEY_TIPO_PRODUCCION = "6";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_MAT = "hiddenCveMat";
	/** Nombre del param de dar de baja **/
	private final String KEY_HIDDEN_CURRENT_PAGINA = "hiddenCurrentPagina";

	/** Usuario **/
	public Usuario usuario;
	
	private ModuloCombos mc;
	
    /**
     * @see HttpServletHttpServlet()
     */
    public BusMat() {
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
    			Iniciar();
    			SetForm(request, true);
				//buscar materia	
    			int count = BuscarMateria(request, true, 0);
    			
    			int currentPagina = GetCurrentPagina(request);
    			request.setAttribute(KEY_VARIABLE_PAGINAS, Common.CreateListNumeroPagina(request, count, currentPagina));	    
	
    			Configuracion config = new Configuracion();
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathMateria() + Common.NOMBRE_DE_PAGINA_BUS_MATERIA + Common.EXTENSION_JSP);
			    rd.forward(request,response);
    		}

    	} catch (Exception e) {
			Common.Error(e);
			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}
	}

	private int GetCurrentPagina(HttpServletRequest request) {
		int currentPagina = 1;
		if(StringUtil.isNumber(request.getParameter(KEY_HIDDEN_CURRENT_PAGINA))){
			currentPagina = Integer.parseInt(request.getParameter(KEY_HIDDEN_CURRENT_PAGINA));
		}
		request.setAttribute(KEY_VARIABLE_CURRENT_PAGINA, currentPagina);
		return currentPagina;
	}

	/**
	 * Iniciar
	 */
	private void Iniciar() {
		this.listaNivel = null;
		this.listaSolicitud = null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;	
		
		Configuracion config = new Configuracion();
		try {
    	
			usuario = new Usuario(request, response);
    		if(usuario.IsAutorizado()){
    			request.setAttribute(KEY_VARIABLE_USUARIO, usuario);
			
    			int currentPagina = GetCurrentPagina(request);
				int numfrom = ((currentPagina-1)*Common.KEY_NUMERO_MOSTRAR);

				String tipo = request.getParameter(KEY_HIDDEN_TIPO);
				if(tipo != null){
					
					if(tipo.equals(KEY_TIPO_BAJA)){
						DarBajaMateria(request);

					} else if(tipo.equals(KEY_TIPO_CREAR_JSP)){
						String msg = Common.CrearJSP(request, usuario, 
								request.getParameter(KEY_REQUEST_PARAM_CVE_MAT));
    					request.setAttribute(KEY_VARIABLE_MESSAGE, msg);
					
					} else if(tipo.equals(KEY_TIPO_EXPORTAR_VALIDACION)){
						if(!Common.Validacion(request.getParameter(KEY_FORM_HIDDEN_CVE_MAT), response)){
    						return;
    					}
						return;
					
					} else if(tipo.equals(KEY_TIPO_EXPORTAR)){
						String cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
    					Common.Exportar(request,cveMat,usuario );

    					Materia mat = Materia.Buscar(cveMat);
    					
    					if(Common.FTPCopiar(mat)){
	    					request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_TERMINAR_PROCESO + 
	    							" URL de prueba es <a target='_blank' href='"+ config.getPruebaUrl() + "'>" + config.getPruebaUrl() + "</a>");

    					}else{
    						request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR + " cunado se copia por FTP");
    					}

					} else if(tipo.equals(KEY_TIPO_BUSCAR)){
						numfrom = 0;

					} else if(tipo.equals(KEY_TIPO_PRODUCCION)){
						EstaProduccion(request);
					}
					
					
				}
				
				//buscar materia	    
				int count = BuscarMateria(request,false,numfrom);
    			request.setAttribute(KEY_VARIABLE_PAGINAS, Common.CreateListNumeroPagina(request, count, currentPagina));	    
				
				SetForm(request, false);
				
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathMateria() + Common.NOMBRE_DE_PAGINA_BUS_MATERIA + Common.EXTENSION_JSP);
			    rd.forward(request,response);		
    		}
    		
    	} catch (Exception e) {
			Common.Error(e);
			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}
		
	}

	private void EstaProduccion(HttpServletRequest request) throws Exception {
		String cveMat = request.getParameter(KEY_REQUEST_PARAM_CVE_MAT);
		
		if(Validacion(request, cveMat)){

			InsertarSolicitud(request, cveMat);
			
		}		
	}

	private boolean Validacion(HttpServletRequest request, String cveMat) throws Exception {
		List<MateriaSolicitud> list = null;
		
		try {
			
			MateriaSolicitud bean = new MateriaSolicitud();
			bean.setCveMat(cveMat);
			list = bean.Buscar();
			if(list.size()>0 && 
					list.get(0).getEstadoSolicitud() != Common.ESTADO_DE_SOLICITUD_LIBERADO){
				
				request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_SOLICITAR_HA_PRODUCCION);
				return false;
			
			}
			
		} catch (Exception e) {
			throw e;
		}
		return true;
	}

	private void InsertarSolicitud(HttpServletRequest request, String cveMat) throws Exception {

		Connection con = MySqlConnector.getConnection();
		try {
			con.setAutoCommit(false);
			
			MateriaSolicitud bean = new MateriaSolicitud();
			bean.setCveMat(cveMat);
			bean.setResultado(Common.ESTADO_DE_SOLICITUD_PRODUCCION);
			bean.setEstadoSolicitud(Common.ESTADO_DE_SOLICITUD_PRODUCCION);
			bean.setUsuarioSolicitud(usuario.getCveUsu());
			
			bean.Insertar(con);
			
			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_PRODUCCION,cveMat));
	
			
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
	 * Dar baja la materia
	 * @param cveMat
	 * @param request
	 * @throws Exception
	 */
	private void DarBajaMateria(HttpServletRequest request) throws Exception {
		if(usuario.isAdministradorGeneral() || usuario.isAdministrador()){
			String cveMat = request.getParameter(KEY_REQUEST_PARAM_CVE_MAT);
			Connection con = MySqlConnector.getConnection();
			try {
				con.setAutoCommit(false);
				Materia mat = Materia.Buscar(cveMat);
				String message = "";
				if(mat.getBanMat() == 1){
					Materia.DarBajaLogical(con, cveMat, 0);
					TrcnMat.DarBajaConMateria(con, cveMat, 0);
					message = Common.TEXTO_ACTION_LOG_MATERIA_DAR_BAJA;
				} else{
					Materia.DarBajaLogical(con, cveMat, 1);
					TrcnMat.DarBajaConMateria(con, cveMat, 1);
					message = Common.TEXTO_ACTION_LOG_MATERIA_DAR_ALTA;
				}
				Common.InsertLogAct(request, con, usuario.getCveUsu(), MessageFormat.format(message,cveMat));
				
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
		}else{
			throw new CustomException(0, Common.MENSAJE_ERROR_AUTENTIFICA);
		}
		
	}

	/**
	 * Set form de la pantalla
	 * @param request
	 * @throws Exception 
	 */
	private void SetForm(HttpServletRequest request, boolean isGet) throws Exception {
		try {
			request.setAttribute(KEY_VARIABLE_USUARIO, usuario);
			request.setAttribute(KEY_VARIABLE_PERMISO_ADMINISTRADOR_GENERAL, Common.PERMISO_ADMINISTRADOR_GENERAL);
			request.setAttribute(KEY_VARIABLE_PERMISO_ADMINISTRADOR, Common.PERMISO_ADMINISTRADOR);
			request.setAttribute(KEY_VARIABLE_PERMISO_ASESOR, Common.PERMISO_ASESOR);
			request.setAttribute(KEY_VARIABLE_PERMISO_REVISOR, Common.PERMISO_REVISOR);
			request.setAttribute(KEY_VARIABLE_PERMISO_USUARIO_NORMAL, Common.PERMISO_USUARIO_NORMAL);
			request.setAttribute(KEY_VARIABLE_ESTADO_DE_SOLICITUD_PENDIENTE, Common.ESTADO_DE_SOLICITUD_PENDIENTE);
			request.setAttribute(KEY_VARIABLE_ESTADO_DE_SOLICITUD_SOLICITADO, Common.ESTADO_DE_SOLICITUD_SOLICITADO);
			request.setAttribute(KEY_VARIABLE_ESTADO_DE_SOLICITUD_REVISADO, Common.ESTADO_DE_SOLICITUD_REVISADO);
			request.setAttribute(KEY_VARIABLE_ESTADO_DE_SOLICITUD_VALIDADO, Common.ESTADO_DE_SOLICITUD_VALIDADO);
			request.setAttribute(KEY_VARIABLE_ESTADO_DE_SOLICITUD_LIBERADO, Common.ESTADO_DE_SOLICITUD_LIBERADO);
			request.setAttribute(KEY_VARIABLE_ESTADO_DE_SOLICITUD_PRODUCCION, Common.ESTADO_DE_SOLICITUD_PRODUCCION);
	
			String clave = Common.htmlEscape(request.getParameter(KEY_VARIABLE_CLAVE));
			request.setAttribute(KEY_VARIABLE_CLAVE, clave);
	
			String nombre = Common.htmlEscape(request.getParameter(KEY_VARIABLE_NOMBRE));
			request.setAttribute(KEY_VARIABLE_NOMBRE, nombre);
	
//			String unidad = StringEscapeUtils.escapeHtml(request.getParameter(KEY_VARIABLE_UNIDAD));
//			request.setAttribute(KEY_VARIABLE_UNIDAD, unidad);

			if(this.listaNivel == null){
					this.listaNivel = CrearComboNivel();
			}
			request.setAttribute(KEY_VARIABLE_NIVELES, this.listaNivel);
			String nivel = "";
			if(!isGet){
				nivel = request.getParameter(KEY_FORM_NIVEL);
			}
			SetSelected(nivel);

			String modulo = request.getParameter(KEY_VARIABLE_MODULO + nivel);
			request.setAttribute(KEY_VARIABLE_MODULO, modulo);
			
			if(this.listaSolicitud == null){
				this.listaSolicitud = CrearComboSolicitud();
			}
			request.setAttribute(KEY_VARIABLE_SOLICITUDES, this.listaSolicitud);
			String solicitud = "";
			if(isGet){
				
				solicitud = GetSolicitud();
				
			}else{
				solicitud = request.getParameter(KEY_FORM_SOLICITUD);
			}
			SetSelectedSolicitud(solicitud);
			
			request.setAttribute("modulos", CrearComboModulo());			
			SetSelectedModulo(nivel, modulo);
		
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	private String GetSolicitud() {
		if(usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR){
			return Integer.toString(Common.ESTADO_DE_SOLICITUD_REVISADO);
		}else if(usuario.getPerUsu() == Common.PERMISO_REVISOR){
			return Integer.toString(Common.ESTADO_DE_SOLICITUD_SOLICITADO);
		}else if(usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR_GENERAL){
			return Integer.toString(Common.ESTADO_DE_SOLICITUD_LIBERADO);
		}else{
			return Integer.toString(Common.ESTADO_DE_SOLICITUD_PENDIENTE);
		}
	}

	/**
	 * Crear combo de modulo
	 * @return
	 * @throws Exception
	 */
	private ModuloCombos CrearComboModulo() throws Exception {
		try {
			List<Materia> list = Materia.BuscaModulos();
			this.mc = new ModuloCombos();
			for(Nivel n : this.listaNivel){
				List<Materia> listm = new ArrayList<Materia>();
				for(Materia m : list){
					if(n.getCveNivel() == m.getNivel()){
						listm.add(m);
					}
				}
				this.mc.add(listm);
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		return this.mc;
	}

	/**
	 * Colocar "selected" en Combo de Modulo
	 * @param nivel
	 */
	private void SetSelectedModulo(String strnivel ,String strmodulo){
		if(StringUtil.isNumber(strnivel) && StringUtil.isNumber(strmodulo)){
			int nivel = Integer.parseInt(strnivel);
			int modulo = Integer.parseInt(strmodulo);
			for(int i=0; i <= mc.size()-1; i++){
				
				List<Materia> comboModulo = (List<Materia>)mc.get(i);
				
				if(comboModulo.size() > 0 && 
						comboModulo.get(0).getNivel() == nivel){
					
					for(int j=0; j<=comboModulo.size()-1; j++ ){
						Materia m = comboModulo.get(j);
						if(m.getModulo() == modulo){
							m.setSelected("selected");						

						} else{
							m.setSelected("");
						}
					}
				}
			}
		}
	}
	/**
	 * Buscar materia con condicional
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	private int BuscarMateria(HttpServletRequest request, boolean isGet, int numfrom) throws Exception{
		List<Materia> list = new ArrayList<Materia>();
		int count = 0;
		Materia mat = new Materia();		
		String str;
		int nivel = 0;
		str = request.getParameter(KEY_FORM_NIVEL);
		if(StringUtil.isNumber(str)){
			nivel = Integer.parseInt(str);
		}

		mat.setNivel(nivel);
		mat.setCveMat(request.getParameter(KEY_VARIABLE_CLAVE));
		mat.setNomMat(request.getParameter(KEY_VARIABLE_NOMBRE));

		if(isGet){
			
			str = GetSolicitud();
			
		}else{
			str = request.getParameter(KEY_FORM_SOLICITUD);
		}
		if(StringUtil.isNumber(str)){
			mat.setEstadoSolicitud(Integer.parseInt(str));			
		}		
		
		int modulo = 0;
		if(StringUtil.isNumber(request.getParameter(KEY_VARIABLE_MODULO + nivel))){
			modulo = Integer.parseInt(request.getParameter(KEY_VARIABLE_MODULO + nivel));
		}		
		mat.setModulo(modulo);

//		str = request.getParameter(KEY_VARIABLE_UNIDAD);
//		if(StringUtil.isNumber(str)){
//			mat.setUnidad(Integer.parseInt(str));
//		}
			
		try {
			
			list = mat.Buscar(usuario, numfrom,Common.KEY_NUMERO_MOSTRAR);
			count = mat.CountMateria(usuario);
			
		} catch (Exception e) {
			throw e;
		}
		
		request.setAttribute(KEY_VARIABLE_MATERIAS, list);
		request.setAttribute(KEY_VARIABLE_EXIST_RESULTADO, list.size() > 0);
		request.setAttribute(KEY_VARIABLE_NUMERO_TOTAL, count);

		return count;
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
	 * Crear Combo de solicitud
	 * @return
	 * @throws Exception 
	 */
	private List<MstCode> CrearComboSolicitud() throws Exception {
		try {
			this.listaSolicitud = MstCode.Buscar(MstCode.CVE_CODE_SOLICITUD);
			return this.listaSolicitud;
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
	/**
	 * Colocar "selected" en Combo de Solicitud
	 * @param nivel
	 */
	private void SetSelectedSolicitud(String val){
		for(int i=0; i <= listaSolicitud.size()-1; i++){
			if(((MstCode)listaSolicitud.get(i)).getCode().equals(val)){
				((MstCode)listaSolicitud.get(i)).setSelected("selected");
			} else {
				((MstCode)listaSolicitud.get(i)).setSelected("");
			}
		}
	}

}
