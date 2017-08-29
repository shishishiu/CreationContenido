package servlet.usuario;

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
@WebServlet("/BusUsu")
public class BusUsu extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Lista **/
	private List<MstCode> listaPermiso;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "BusUsu.jsp";
	/** Nombre del form de materias **/
	private final String KEY_VARIABLE_EXIST_RESULTADO = "existResultado";
	/** Nombre del form de materias **/
	private final String KEY_VARIABLE_USUARIOS = "usuarios";
	/** Nombre del form del clave **/
	private final String KEY_VARIABLE_CLAVE = "cveUsu";
	/** Nombre del form del nombre **/
	private final String KEY_VARIABLE_NOMBRE = "nomUsu";
	/** Nombre del form del usuario **/
	private final String KEY_VARIABLE_USUARIO = "usuario";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PERMISOS = "permisos";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PUEDE_MOSTRAR = "puedeMostrar";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_NUMERO_TOTAL = "numtotal";
	/** Nombre del param de dar de baja **/
	private final String KEY_HIDDEN_TIPO = "hiddenTipo";
	/** Nombre del param de dar de baja **/
	private final String KEY_HIDDEN_CURRENT_PAGINA = "hiddenCurrentPagina";
	/** Nombre del param de dar de baja **/
	private final String KEY_REQUEST_PARAM_CVE_USU = "paramCveUsu";
	/** Nombre del param de dar de baja **/
	private final String KEY_TIPO_BUSCAR = "0";
	/** Nombre del param de dar de baja **/
	private final String KEY_TIPO_BAJA = "1";
	/** Nombre del param**/
	private final String KEY_TIPO_MOVER_PAGINA = "2";
	/** Nombre del form del permiso **/
	private final String KEY_FORM_CVE_USU = "cveUsu";
	/** Nombre del form del permiso **/
	private final String KEY_FORM_NOM_USU = "nomUsu";
	/** Nombre del form del permiso **/
	private final String KEY_FORM_PERMISO = "permiso";

	/** Usuario **/
	public Usuario usuario;
	
    /**
     * @see HttpServletHttpServlet()
     */
    public BusUsu() {
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
			request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, true);
			
    		if(usuario.IsAutorizado()){
    			if(usuario.isAdministrador() || usuario.isAdministradorGeneral()){
        			Iniciar();
        			SetForm(request);
    				//buscar usuario	
        			int count = BuscarUsuario(request,0);
        			
        			int currentPagina = GetCurrentPagina(request);
        			request.setAttribute("paginas", Common.CreateListNumeroPagina(request, count, currentPagina));	    

    			}else{
   					request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, false);
   					request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_AUTENTIFICA);
    			}

    			Configuracion config = new Configuracion();
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathUsuario() + NOMBRE_DE_PAGINA);
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
    				if(tipo != null){
						int count;
	        			int currentPagina;
    					
    					if(tipo.equals(KEY_TIPO_BUSCAR)){
							count = BuscarUsuario(request,0);
		        			currentPagina = GetCurrentPagina(request);
		        			request.setAttribute("paginas", Common.CreateListNumeroPagina(request, count, currentPagina));	    
    					
    					} else if(tipo.equals(KEY_TIPO_BAJA)){
							DarBaja(request);
    						
    					} else if(tipo.equals(KEY_TIPO_MOVER_PAGINA)){
		        			currentPagina = GetCurrentPagina(request);
							int numfrom = ((currentPagina-1)*Common.NUMERO_DE_DATOS_PARA_MOSTRAR);
							count = BuscarUsuario(request, numfrom);
		        			request.setAttribute("paginas", Common.CreateListNumeroPagina(request, count, currentPagina));	    
    						
    					}
    					
    				}

    				SetForm(request);
    				
    			}else{
   					request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, false);
   					request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_AUTENTIFICA);
    			}

								
    			Configuracion config = new Configuracion();
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathUsuario() + NOMBRE_DE_PAGINA);
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
		return currentPagina;
	}

	/**
	 * Iniciar
	 */
	private void Iniciar() {
		this.listaPermiso = null;
	}


	private void DarBaja(HttpServletRequest request) throws Exception {
		String cveUsu = request.getParameter(KEY_REQUEST_PARAM_CVE_USU);
		Connection con = MySqlConnector.getConnection();
		try {
			con.setAutoCommit(false);
			Usuario bean = Usuario.Buscar(cveUsu);
			String message = "";
			if(bean.getBanUsu() == 1){
				Usuario.DarBajaLogical(con, cveUsu, 0);
				message = Common.TEXTO_ACTION_LOG_USUARIO_DAR_BAJA;
			} else{
				Usuario.DarBajaLogical(con, cveUsu, 1);
				message = Common.TEXTO_ACTION_LOG_USUARIO_DAR_ALTA;
			}
			Common.InsertLogAct(request, con, usuario.getCveUsu(), MessageFormat.format(message,cveUsu));
			
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
	private void SetForm(HttpServletRequest request) throws Exception {
		try {
			request.setAttribute(KEY_VARIABLE_USUARIO, usuario);
			request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, true);

			String clave = Common.htmlEscape(request.getParameter(KEY_VARIABLE_CLAVE));
			request.setAttribute(KEY_VARIABLE_CLAVE, clave);
	
			String nombre = Common.htmlEscape(request.getParameter(KEY_VARIABLE_NOMBRE));
			request.setAttribute(KEY_VARIABLE_NOMBRE, nombre);
	
			if(this.listaPermiso == null){
				this.listaPermiso = CrearComboPermiso();
			}
			request.setAttribute(KEY_VARIABLE_PERMISOS, this.listaPermiso);
		
			String permiso = request.getParameter(KEY_FORM_PERMISO);
			SetSelected(permiso);
			
		
		} catch (Exception e) {
			throw e;
		}
		
	}

	
	/**
	 * Buscar materia con condicional
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	private int BuscarUsuario(HttpServletRequest request, int numfrom) throws Exception{
		List<Usuario> list  = new ArrayList<Usuario>();
		int cnt = 0;
		Usuario bean = new Usuario();	
		
		bean.setCveUsu(request.getParameter(KEY_FORM_CVE_USU));
		bean.setNomUsu(request.getParameter(KEY_FORM_NOM_USU));
		bean.setNomPatUsu(request.getParameter(KEY_FORM_NOM_USU));
		bean.setNomMatUsu(request.getParameter(KEY_FORM_NOM_USU));
		
		String permiso = request.getParameter(KEY_FORM_PERMISO);
		if(StringUtil.isNumber(permiso)){
			bean.setPerUsu(Integer.parseInt(permiso));		
		}
			
		try {
			list = bean.Buscar(numfrom,Common.NUMERO_DE_DATOS_PARA_MOSTRAR);
			
			cnt = bean.CountUsuario();
			
		} catch (Exception e) {
			throw e;
		}
		
		request.setAttribute(KEY_VARIABLE_USUARIOS, list);
		request.setAttribute(KEY_VARIABLE_EXIST_RESULTADO, list.size() > 0);
		request.setAttribute(KEY_VARIABLE_NUMERO_TOTAL, cnt);

		return cnt;
	}

	/**
	 * Crear Combo
	 * @return
	 * @throws Exception 
	 */
	private List<MstCode> CrearComboPermiso() throws Exception {
		try {
			this.listaPermiso = MstCode.Buscar(MstCode.CVE_CODE_PERMISO);
			return this.listaPermiso;
		} catch (Exception e) {
			throw e;
		}		
	}

	
	/**
	 * Colocar "selected" en Combo
	 * @param nivel
	 */
	private void SetSelected(String code){
		for(int i=0; i <= listaPermiso.size()-1; i++){
			if(((MstCode)listaPermiso.get(i)).getCode().equals(code)){
				((MstCode)listaPermiso.get(i)).setSelected("selected");
			} else {
				((MstCode)listaPermiso.get(i)).setSelected("");
			}
		}
		
	}
}
