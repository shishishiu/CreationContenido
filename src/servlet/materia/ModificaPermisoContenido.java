package servlet.materia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.*;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;
import util.string.StringUtil;
/**
 * Servlet implementation class BusMat
 */
@WebServlet("/ModificaPermisoContenido")
public class ModificaPermisoContenido extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Lista del usuario que tiene permiso del contenido **/
	private List<PermisoContenido> listaUsuarioContenido;
	/** Lista del permiso del contenido **/
	private List<MstCode> listaPermisoCont;
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_CVE_MAT = "cveMat";
	/** Nombre del variable **/	
	private final String KEY_VARIABLE_CVE_MAT = "cveMat";
	/** Nombre del variable **/
	private final String KEY_VARIABLE_USUARIOS = "usuarios";
	/** Nombre del variable **/
	private final String KEY_VARIABLE_PERMISOS_CONTENIDO = "permisosCont";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_MATERIA = "materia";
	/** Nombre del form de nivel **/
	private final String KEY_VARIABLE_EXIST_RESULTADO = "existResultado";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_ACTION_TIPO = "hiddenActionTipo";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_MAT = "hiddenCveMat";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_NUMERO_TOTAL = "numtotal";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_CURRENT_PAGINA = "currentPagina";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PAGINAS = "paginas";
	/** Nombre del param de dar de baja **/
	private final String KEY_HIDDEN_CURRENT_PAGINA = "hiddenCurrentPagina";
	/** Usuario **/
	public Usuario usuario;
	/** Materia **/
	public Materia materia;
	
	
    /**
     * @see HttpServletHttpServlet()
     */
    public ModificaPermisoContenido() {
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
    			
    			if(usuario.isAdministradorGeneral() || usuario.isAdministrador()){
	    					
    				Inicializar();
    				
	    			SetForm(request);
	    			
	    			Configuracion config = new Configuracion();
					rd = getServletConfig().getServletContext().getRequestDispatcher(
							config.getPathMateria() + Common.NOMBRE_DE_PAGINA_MODIFICA_PERMISO_CONTENIDO 
							+ Common.EXTENSION_JSP);
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
		
		try {
    	
			usuario = new Usuario(request, response);
    		if(usuario.IsAutorizado()){

    			if(usuario.isAdministradorGeneral() || usuario.isAdministrador()){
				

    				String cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
    				
    				if(request.getParameter(KEY_FORM_HIDDEN_ACTION_TIPO).equals("1")){
    					Modificar(request, cveMat);						
    				}
			  				
    				
    				SetForm(request);
					
	    			Configuracion config = new Configuracion();
					rd = getServletConfig().getServletContext().getRequestDispatcher(
							config.getPathMateria() + Common.NOMBRE_DE_PAGINA_MODIFICA_PERMISO_CONTENIDO + Common.EXTENSION_JSP);
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
	 * Inicializar
	 */
	private void Inicializar() {
		this.usuario = null;
		this.materia = null;
		this.listaPermisoCont = null;
		this.listaUsuarioContenido = null;
	}
	/**
	 * Buscar usuarios que tienen permiso de modificar los contenidos de la materia
	 * @return
	 * @throws Exception
	 */
	private List<PermisoContenido> BuscarUsuarioContenido(String cveMat, int numfrom) throws Exception {
		PermisoContenido bean = new PermisoContenido();
		bean.setCveMat(cveMat);
		try {
			this.listaUsuarioContenido = bean.Buscar(numfrom, Common.KEY_NUMERO_MOSTRAR_PAGINA); 
			return listaUsuarioContenido;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Modificar los permisos de los usuarios
	 * @param request
	 * @throws Exception
	 */
	private void Modificar(HttpServletRequest request, String cveMat) throws Exception{
		Connection con = null;			
		try {
			con = MySqlConnector.getConnection();			
			con.setAutoCommit(false);
		
			for(PermisoContenido bean : this.listaUsuarioContenido){
				
				int permiso = Integer.parseInt(request.getParameter("permisoCont_" + bean.getCveUsu()));
				if(bean.getPermiso() != permiso){
					bean.setPermiso(permiso);
			
					PermisoContenido.Modificar(con, bean);
					
					Common.InsertLogAct(request, con, usuario.getCveUsu(), 
							MessageFormat.format(Common.TEXTO_ACTION_LOG_DAR_PERMISO_CONTENIDO_MODIFICAR, cveMat, bean.getCveUsu()));

				}
			}
						
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
	 * Crear radio butones de permiso de contenido
	 * @return
	 * @throws Exception
	 */
	private List<MstCode> CrearRadioPermisoCont() throws Exception {
		try {
			return MstCode.Buscar(MstCode.CVE_CODE_PERMISO_CONTENIDO);
		} catch (Exception e) {
			throw e;
		}		
	}
	
	/**
	 * Set form de la pantalla
	 * @param request
	 * @throws Exception 
	 */
	private void SetForm(HttpServletRequest request) throws Exception {
		try {
			if(this.listaPermisoCont == null){
				this.listaPermisoCont = CrearRadioPermisoCont();
			}
			request.setAttribute(KEY_VARIABLE_PERMISOS_CONTENIDO, this.listaPermisoCont);				

			String cveMat = request.getParameter(KEY_REQUEST_PARAM_CVE_MAT);
			if(cveMat == null){
				cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
			}
			request.setAttribute(KEY_VARIABLE_CVE_MAT, cveMat);
			
			if(this.materia == null){
				this.materia = Materia.Buscar(cveMat);
			}
			request.setAttribute(KEY_VARIABLE_MATERIA, this.materia);
			
			
			int currentPagina = GetCurrentPagina(request);
			int numfrom = ((currentPagina-1)*Common.KEY_NUMERO_MOSTRAR);
			PermisoContenido bean = new PermisoContenido();
			bean.setCveMat(cveMat);
			int count = bean.CountUsuariosContenidos();
			request.setAttribute(KEY_VARIABLE_PAGINAS, Common.CreateListNumeroPagina(request, count, currentPagina));	    

			this.listaUsuarioContenido = BuscarUsuarioContenido(cveMat, numfrom);
			int usuarioContenidoCount = this.listaUsuarioContenido.size();
			request.setAttribute(KEY_VARIABLE_USUARIOS, this.listaUsuarioContenido);
			request.setAttribute(KEY_VARIABLE_EXIST_RESULTADO, usuarioContenidoCount > 0);

			request.setAttribute(KEY_VARIABLE_NUMERO_TOTAL, count);
			

			request.setAttribute(KEY_VARIABLE_PAGINAS, Common.CreateListNumeroPagina(request, count, currentPagina));	    

			

		} catch (Exception e) {
			throw e;
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

}
