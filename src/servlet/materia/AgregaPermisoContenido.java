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
import util.conf.Configuracion;
import util.db.MySqlConnector;
import util.string.StringUtil;

/**
 * Servlet implementation class BusMat
 */
@WebServlet("/AgregaPermisoContenido")
public class AgregaPermisoContenido extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "AgregaPermisoContenido.jsp";
	/** Lista del usuario **/
	private List<Usuario> listaUsuario;
	/** Lista del permiso **/
	private List<MstCode> listaPermiso;
	/** Lista del permiso del contenido **/
	private List<MstCode> listaPermisoCont;
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_CVE_MAT = "cveMat";
	/** Nombre del form de nivel **/
	private final String KEY_VARIABLE_EXIST_RESULTADO = "existResultado";
	/** Nombre del variable **/	
	private final String KEY_VARIABLE_CVE_MAT = "cveMat";
	/** Nombre del variable **/
	private final String KEY_VARIABLE_USUARIOS = "usuarios";
	/** Nombre del variable **/
	private final String KEY_VARIABLE_PERMISOS = "permisos";
	/** Nombre del variable **/
	private final String KEY_VARIABLE_PERMISOS_CONTENIDO = "permisosCont";
	/** Nombre del variable **/
	private final String KEY_VARIABLE_CVE_USU = "cveUsu";
	/** Nombre del variable **/
	private final String KEY_VARIABLE_NOM_USU = "nomUsu";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_MATERIA = "materia";
	/** Nombre del form **/	
	private final String KEY_VARIABLE_PARENT_SUBMIT = "parentSubmit";
	/** Nombre del form **/
	private final String KEY_FORM_CVE_USU = "cveUsu";
	/** Nombre del form **/
	private final String KEY_FORM_NOM_USU = "nomUsu";
	/** Nombre del form **/
	private final String KEY_FORM_PERMISO = "permiso";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_ACTION_TIPO = "hiddenActionTipo";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_MAT = "hiddenCveMat";
	/** Tipo del accion **/
	private final String ACTION_TIPO_BUSCAR = "1";
	/** Tipo del accion **/
	private final String ACTION_TIPO_AGREGAR = "2";
	/** Usuario **/
	public Usuario usuario;
	/** Materia **/
	public Materia materia;
	
    /**
     * @see HttpServletHttpServlet()
     */
    public AgregaPermisoContenido() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;
			
    	try {
    		
    		usuario = new Usuario(request, response, true);
    		if(usuario.IsAutorizado()){
    				
    			if(usuario.isAdministradorGeneral() || usuario.isAdministrador()){
    				
    				Inicializar();
	    			BuscarUsuario(request, request.getParameter(KEY_REQUEST_PARAM_CVE_MAT));    			
	    			
	    			SetForm(request);
	    			
	    			Configuracion config = new Configuracion();
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
		
		try {
    	
			usuario = new Usuario(request, response, true);
    		if(usuario.IsAutorizado()){
 
    			if(usuario.isAdministradorGeneral() || usuario.isAdministrador()){
				
    				if(request.getParameter(KEY_FORM_HIDDEN_ACTION_TIPO).equals(ACTION_TIPO_BUSCAR)){
						// Buscar usuarios
    					BuscarUsuario(request, request.getParameter(KEY_FORM_HIDDEN_CVE_MAT));

					}
    				if(request.getParameter(KEY_FORM_HIDDEN_ACTION_TIPO).equals(ACTION_TIPO_AGREGAR)){
    					// Agregar permiso
    					Agregar(request, request.getParameter(KEY_FORM_HIDDEN_CVE_MAT));
    					request.setAttribute(KEY_VARIABLE_PARENT_SUBMIT, 1);
    					BuscarUsuario(request, request.getParameter(KEY_FORM_HIDDEN_CVE_MAT));
    				}

    			}else{
					Common.Error(Common.MENSAJE_ERROR_AUTENTIFICA);
					response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
					return;
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
	 * Inicializar
	 */
	private void Inicializar() {
		this.usuario = null;
		this.materia = null;
		this.listaPermisoCont = null;
		this.listaUsuario = null;
	}

	/**
	 * Agregar permiso
	 * @param request
	 * @throws Exception
	 */
	private void Agregar(HttpServletRequest request, String cveMat) throws Exception {
		Connection con = null;			
		try {
			con = MySqlConnector.getConnection();			
			con.setAutoCommit(false);
		
			for(Usuario bean : this.listaUsuario){
				String val = request.getParameter("permisoCont_" + bean.getCveUsu());
				if(StringUtil.isNumber(val)){
					
					int permiso = Integer.parseInt(val);
					if(permiso != Common.PERMISO_CONTENIDO_NINGUNA){
	
						PermisoContenido beanPc = new PermisoContenido();
						beanPc.setPermiso(permiso);
						beanPc.setCveUsu(bean.getCveUsu());
						beanPc.setCveMat(cveMat);
				
						PermisoContenido.Insertar(con, beanPc);
						
						Common.InsertLogAct(request, con, usuario.getCveUsu(),MessageFormat.format(
								Common.TEXTO_ACTION_LOG_DAR_PERMISO_CONTENIDO_AGREGAR, cveMat, bean.getCveUsu()));
	
					}
				}

			}
						
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
	 * Buscar usuarios
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private void BuscarUsuario(HttpServletRequest request, String cveMat) throws Exception{
		
		Usuario u = new Usuario();
		u.setCveUsu(request.getParameter(KEY_FORM_CVE_USU));
		u.setNomUsu(request.getParameter(KEY_FORM_NOM_USU));

		String str = request.getParameter(KEY_FORM_PERMISO);
		if(StringUtil.isNumber(str)){
			u.setPerUsu(Integer.parseInt(str));
		}

		try {
			this.listaUsuario = u.BuscarUsuarioSinPermisoContenido(cveMat);

			request.setAttribute(KEY_VARIABLE_USUARIOS, this.listaUsuario);	
			request.setAttribute(KEY_VARIABLE_EXIST_RESULTADO, this.listaUsuario.size() > 0);

		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Crear Combo de permiso
	 * @return
	 * @throws Exception 
	 */
	private List<MstCode> CrearComboPermiso() throws Exception {
		try {
			List<MstCode> l = MstCode.Buscar(MstCode.CVE_CODE_PERMISO);
			this.listaPermiso = new ArrayList<MstCode>();
			
			for(MstCode bean : l){
				if(Integer.parseInt(bean.getCode()) != Common.PERMISO_ADMINISTRADOR &&
						Integer.parseInt(bean.getCode()) != Common.PERMISO_ADMINISTRADOR_GENERAL &&
						Integer.parseInt(bean.getCode()) != Common.PERMISO_USUARIO_NORMAL){
					this.listaPermiso.add(bean);
				}
			}
			
			return this.listaPermiso;
		} catch (Exception e) {
			throw e;
		}		
	}

	/**
	 * Crear radio butones de permiso de contenido
	 * @return
	 * @throws Exception
	 */
	private List<MstCode> CrearRadioPermisoCont() throws Exception {
		try {
			this.listaPermisoCont = MstCode.Buscar(MstCode.CVE_CODE_PERMISO_CONTENIDO);
			return this.listaPermisoCont;
		} catch (Exception e) {
			throw e;
		}		
	}
	
	/**
	 * Colocar "selected" en Combo de Nivel
	 * @param nivel
	 */
	private void SetSelected(String code){
		if(StringUtil.isNumber(code)){
			for(int i=0; i <= listaPermiso.size()-1; i++){
				if(((MstCode)listaPermiso.get(i)).getCode().equals(code)){
					((MstCode)listaPermiso.get(i)).setSelected("selected");
				} else {
					((MstCode)listaPermiso.get(i)).setSelected("");
				}
			}
		}
		
	}
	
	/**
	 * Set form de la pantalla
	 * @param request
	 * @throws Exception 
	 */
	private void SetForm(HttpServletRequest request) throws Exception {

		try {
			if(this.listaPermiso == null){
				this.listaPermiso = CrearComboPermiso();
			}
			request.setAttribute(KEY_VARIABLE_PERMISOS, this.listaPermiso);				
			SetSelected(request.getParameter(KEY_FORM_PERMISO));				
			
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

			request.setAttribute(KEY_VARIABLE_CVE_USU, request.getParameter(KEY_FORM_CVE_USU));
			request.setAttribute(KEY_VARIABLE_NOM_USU, request.getParameter(KEY_FORM_NOM_USU));


		} catch (Exception e) {
			throw e;
		}
	}
}
