package servlet.usuario;

import java.io.IOException;
import java.sql.Connection;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.MstCode;
import beans.Usuario;
import util.common.Common;
import util.common.Crypto;
import util.conf.Configuracion;
import util.db.MySqlConnector;

/**
 * Servlet implementation class ModificaMat
 */
@WebServlet("/ModificaUsu")
public class ModificaUsu extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Lista **/
	private List<MstCode> listaPermiso;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "ModificaUsu.jsp";
	/** Nombre del key **/
	private final String KEY_VARIABLE_TITLE = "title";
	/** Nombre del key **/
	private final String KEY_VARIABLE_BOTON = "boton";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PERMISOS = "permisos";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_USU = "usu";
	/** Nombre del form **/
	private final String KEY_VARIABLE_TIPO = "hidTipo";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PUEDE_MOSTRAR = "puedeMostrar";
	/** Nombre del form de cveMat **/
	private final String KEY_REQUEST_PARAM_CVE_USU = "cveUsu";
	/** Nombre del key **/
	private final String KEY_REQUEST_PARAM_TIPO = "tipo";
	
	/** Nombre del form del clave **/
	private final String KEY_FORM_CLAVE = "clave";
	/** Nombre del form del nombre **/
	private final String KEY_FORM_NOM = "nomUsu";
	/** Nombre del form del nombre **/
	private final String KEY_FORM_NOM_PAT = "nomPatUsu";
	/** Nombre del form del nombre **/
	private final String KEY_FORM_NOM_MAT = "nomMatUsu";
	/** Nombre del form del nombre **/
	private final String KEY_FORM_PERMISO = "permiso";
	/** Nombre del form del nombre **/
	private final String KEY_FORM_CONTRASENA = "pwsUsu";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_TIPO = "hidTipo";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_USU = "hidCveUsu";
	
	/** Value del title **/
	private final String VALUE_TITLE_AGREGAR = "Agregar Usuario";
	/** Value del title **/
	private final String VALUE_TITLE_MODIFICAR = "Modificar Usuario";
	/** Value del boton **/
	private final String VALUE_BOTON_AGREGAR = "Agregar";
	/** Value del boton **/
	private final String VALUE_BOTON_MODIFICAR = "Modificar";
	/** Value del  tipo **/
	private final String VALUE_REQUEST_PARAM_TIPO_MODIFICAR = "1";
	private final String VALUE_REQUEST_PARAM_TIPO_AGREGAR = "2";
       
	/** Usuario **/
	public Usuario usuario;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModificaUsu() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;
		
		request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, true);
		usuario = new Usuario(request, response);
		if(usuario.IsAutorizado()){
			if(usuario.isAdministrador() || usuario.isAdministradorGeneral()){
				
				try {
			    		
					SetForm(request, false);

				} catch (Exception e) {
					Common.Error(e);
					response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
					return;
				}
				
		
			}else{
				request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, false);
				request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_AUTENTIFICA);
			}

			Configuracion config = new Configuracion();
			rd = getServletConfig().getServletContext().getRequestDispatcher(
					config.getPathUsuario() + NOMBRE_DE_PAGINA);
		    rd.forward(request,response);
		}		
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;	

		request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, true);
		usuario = new Usuario(request, response);
		Configuracion config = new Configuracion();
		if(usuario.IsAutorizado()){
			if(usuario.isAdministrador() || usuario.isAdministradorGeneral()){

				try{
					
					String cveUsu = request.getParameter(KEY_FORM_HIDDEN_CVE_USU);
					String hidTipo = request.getParameter(KEY_FORM_HIDDEN_TIPO);
					if(hidTipo.equals(VALUE_REQUEST_PARAM_TIPO_AGREGAR)){
						//Agregar
						if(IsValidated(request)){
							Agregar(request);
						}else{
							SetForm(request, true);
							rd = getServletConfig().getServletContext().getRequestDispatcher(
									config.getPathUsuario() + NOMBRE_DE_PAGINA);
						    rd.forward(request,response);
						    return;
						}

					}else if(hidTipo.equals(VALUE_REQUEST_PARAM_TIPO_MODIFICAR)){
						//Modificar
						if(IsValidated(request)){
							Modificar(request, cveUsu);
						}else{
							SetForm(request, true);
							rd = getServletConfig().getServletContext().getRequestDispatcher(
									config.getPathUsuario() + NOMBRE_DE_PAGINA);
						    rd.forward(request,response);
						    return;
						}
					}
				
					SetForm(request, false);
		
				} catch (Exception e) {
					Common.Error(e);
					response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
					return;
				}
			}else{
				request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, false);
				request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_AUTENTIFICA);
			}
			
			rd = getServletConfig().getServletContext().getRequestDispatcher(
					config.getPathUsuario() + NOMBRE_DE_PAGINA);
		    rd.forward(request,response);

		}
	}

	private boolean IsValidated(HttpServletRequest request) throws Exception {
		try {
			String message = "";
			String hidTipo = request.getParameter(KEY_FORM_HIDDEN_TIPO);
			if(hidTipo.equals(VALUE_REQUEST_PARAM_TIPO_AGREGAR)){
				String cve = request.getParameter(KEY_FORM_CLAVE);
				if(cve == null || cve.equals("")){
					message += "Ingresa la clave, por favor <br>";
				} else if(cve.length() > 21){
					message += "Ingresa menos de 21 letras en clave, por favor<br>";
				}
				
			}
			
			if(request.getParameter(KEY_FORM_NOM) == null || request.getParameter(KEY_FORM_NOM).equals("")){
				message += "Ingresa el nombre, por favor <br>";
			} else if(request.getParameter(KEY_FORM_NOM).length() > 50){
				message += "Ingresa menos de 50 letras en el nombre, por favor<br>";
			}
			
			if(request.getParameter(KEY_FORM_NOM_PAT) == null || request.getParameter(KEY_FORM_NOM_PAT).equals("")){
				message += "Ingresa el nombre padrino, por favor <br>";
			} else if(request.getParameter(KEY_FORM_NOM_PAT).length() > 50){
				message += "Ingresa menos de 50 letras en el nombre padrino, por favor<br>";
			}

			if(request.getParameter(KEY_FORM_NOM_MAT) == null || request.getParameter(KEY_FORM_NOM_MAT).equals("")){
				message += "Ingresa el nombre madrino, por favor <br>";
			} else if(request.getParameter(KEY_FORM_NOM_MAT).length() > 50){
				message += "Ingresa menos de 50 letras en el nombre madrino, por favor<br>";
			}

			if(request.getParameter(KEY_FORM_PERMISO) == null || request.getParameter(KEY_FORM_PERMISO).equals("-1")){
				message += "Ingresa el permiso, por favor <br>";
			}

			if(request.getParameter(KEY_FORM_CONTRASENA) != null || !request.getParameter(KEY_FORM_CONTRASENA).equals("")){
				if(request.getParameter(KEY_FORM_CONTRASENA).length() > 21){
					message += "Ingresa menos de 21 letras en la contraseña, por favor<br>";
				}
			}


			if(message.length() > 0){
				request.setAttribute(KEY_VARIABLE_MESSAGE, message);
				return false;
			}
			
		} catch (Exception e) {
			throw e;
		}
		return true;
	}

	/**
	 * Modificar materia
	 * @param request
	 * @param cveMat
	 * @throws Exception
	 */
	private void Modificar(HttpServletRequest request, String cveUsu) throws Exception {
		Connection con = MySqlConnector.getConnection();
		try {

			String password = request.getParameter(KEY_FORM_CONTRASENA);
			String passwordCrypto = "";
			
			con.setAutoCommit(false);

			Usuario bean = Usuario.Buscar(cveUsu);
			bean.setNomUsu(request.getParameter(KEY_FORM_NOM));
			bean.setNomPatUsu(request.getParameter(KEY_FORM_NOM_PAT));
			bean.setNomMatUsu(request.getParameter(KEY_FORM_NOM_MAT));
			bean.setPerUsu(Integer.parseInt(request.getParameter(KEY_FORM_PERMISO)));
			
			if(password != null && !password.equals("")){
				passwordCrypto = Crypto.getStretchedPassword(password, cveUsu);
				bean.setPwsUsu(passwordCrypto);
			}


			bean.Modificar(con);
			
			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_USUARIO_MODIFICAR, cveUsu));
			
			con.commit();

			request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_TERMINAR_PROCESO);
			
		} catch (Exception e) {
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
	 * Agregar
	 * @param request
	 * @throws Exception
	 */
	private void Agregar(HttpServletRequest request) throws Exception {
		Connection con = MySqlConnector.getConnection();

		try {
			
			String password = request.getParameter(KEY_FORM_CONTRASENA);
			String userId = request.getParameter(KEY_FORM_CLAVE);
			String passwordCrypto = Crypto.getStretchedPassword(password, userId);
			
			
			con.setAutoCommit(false);

			Usuario bean = new Usuario();
			bean.setCveUsu(request.getParameter(KEY_FORM_CLAVE));
			bean.setNomUsu(request.getParameter(KEY_FORM_NOM));
			bean.setNomPatUsu(request.getParameter(KEY_FORM_NOM_PAT));
			bean.setNomMatUsu(request.getParameter(KEY_FORM_NOM_MAT));
			bean.setPerUsu(Integer.parseInt(request.getParameter(KEY_FORM_PERMISO)));
			bean.setPwsUsu(passwordCrypto);
			
			bean.Insertar(con);
			
			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_USUARIO_AGREGAR, bean.getCveUsu()));
			
			con.commit();

			request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_TERMINAR_PROCESO);
			
		} catch (Exception e) {
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
	 * Set form
	 * @param request
	 * @throws Exception
	 */
	private void SetForm(HttpServletRequest request, boolean hasError) throws Exception {
				
		String cveUsu = request.getParameter(KEY_REQUEST_PARAM_CVE_USU);
		if(cveUsu == null){
			cveUsu = request.getParameter(KEY_FORM_HIDDEN_CVE_USU);
		}

		request.setAttribute(KEY_VARIABLE_PERMISOS, CrearComboPermiso());			

		String hidTipo;
		hidTipo = request.getParameter(KEY_REQUEST_PARAM_TIPO);
		if(hidTipo == null){
			hidTipo = request.getParameter(KEY_FORM_HIDDEN_TIPO);
		}
		request.setAttribute(KEY_VARIABLE_TIPO, hidTipo);			

		if(hidTipo.equals(VALUE_REQUEST_PARAM_TIPO_MODIFICAR)){
			//mostrar la página de modificar
			request.setAttribute(KEY_VARIABLE_TITLE, VALUE_TITLE_MODIFICAR);
			request.setAttribute(KEY_VARIABLE_BOTON, VALUE_BOTON_MODIFICAR);		

			try {

				Usuario bean = Usuario.Buscar(cveUsu);
				if(hasError){
					bean.setNomUsu(request.getParameter(KEY_FORM_NOM));
					bean.setNomPatUsu(request.getParameter(KEY_FORM_NOM_PAT));
					bean.setNomMatUsu(request.getParameter(KEY_FORM_NOM_MAT));
					bean.setPwsUsu(request.getParameter(KEY_FORM_CONTRASENA));
					bean.setPerUsu(Integer.parseInt(request.getParameter(KEY_FORM_PERMISO)));
				}
				
				request.setAttribute(KEY_VARIABLE_USU, bean);			
				SetSelected(bean.getPerUsu());    		

			} catch (Exception e) {
				throw e;
			}		

		} else{
			//mostrar la página de agregar
			request.setAttribute(KEY_VARIABLE_TITLE, VALUE_TITLE_AGREGAR);
			request.setAttribute(KEY_VARIABLE_BOTON, VALUE_BOTON_AGREGAR);
			
			if(hasError){
				Usuario bean = new Usuario();
				bean.setCveUsu(request.getParameter(KEY_FORM_CLAVE));
				bean.setNomUsu(request.getParameter(KEY_FORM_NOM));
				bean.setNomPatUsu(request.getParameter(KEY_FORM_NOM_PAT));
				bean.setNomMatUsu(request.getParameter(KEY_FORM_NOM_MAT));
				bean.setPwsUsu(request.getParameter(KEY_FORM_CONTRASENA));
				bean.setPerUsu(Integer.parseInt(request.getParameter(KEY_FORM_PERMISO)));
				request.setAttribute(KEY_VARIABLE_USU, bean);			
				SetSelected(bean.getPerUsu());    		
			} else{
				SetSelected(Common.PERMISO_USUARIO_NORMAL);
			}
			

		}

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
	private void SetSelected(int code){
		
		String str = Integer.toString(code);
		
		for(int i=0; i <= listaPermiso.size()-1; i++){
			if(((MstCode)listaPermiso.get(i)).getCode().equals(str)){
				((MstCode)listaPermiso.get(i)).setSelected("selected");
			} else {
				((MstCode)listaPermiso.get(i)).setSelected("");
			}
		}
		
	}

}
