package servlet.conf;

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

import beans.Nivel;
import beans.NivelGrado;
import beans.Usuario;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;
import util.string.StringUtil;

/**
 * Servlet implementation class ModificaPlantilla
 */
@WebServlet("/VerNivel")
public class VerNivel extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "VerNivel.jsp";
	/** Nombre del form **/
	private final String KEY_VARIABLE_NIVEL_LIST = "nivelList";
	/** Nombre del form **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form **/
	private final String KEY_VARIABLE_PUEDE_MOSTRAR = "puedeMostrar";
	/** Nombre del param **/
	private final String KEY_HIDDEN_NIVEL_GRADO = "hiddenNivelGrado";
	/** Nombre del param **/
	private final String KEY_HIDDEN_TIPO = "hiddenTipo";
	/** Nombre del param**/
	private final String KEY_TIPO_BAJAR = "1";
	/** Usuario **/
	public Usuario usuario;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerNivel() {
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

  					request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, true);
    				String tipo = request.getParameter(KEY_HIDDEN_TIPO);
    				if(tipo.equals(KEY_TIPO_BAJAR)){
    					
    					BajarNivelGrado(request);
 
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
	
		
	
	private void BajarNivelGrado(HttpServletRequest request) throws Exception {
		
		String strNivelGrado = request.getParameter(KEY_HIDDEN_NIVEL_GRADO);
		int nivelGrado = 0;
		
		if(!StringUtil.isNumber(strNivelGrado)){
			return;
		}
		
		nivelGrado = Integer.parseInt(strNivelGrado);
		
		Connection con = null;
		try {
		    con = MySqlConnector.getConnection();			
			con.setAutoCommit(false);
			
			NivelGrado bean = new NivelGrado();
			
			bean.setCveNivelGrado(nivelGrado);
			bean.DarBajaLogical(con);
			

			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_NIVEL_GRADO_BORRAR, nivelGrado));
						
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
	 * Iniciar
	 */
	private void Iniciar() {
	}

	private void SetForm(HttpServletRequest request) throws Exception {
		try {
			
			List<Nivel> listNivel = Nivel.BuscarNivelNivGrado();
			request.setAttribute(KEY_VARIABLE_NIVEL_LIST, listNivel);
			
		} catch (Exception e) {
			throw e;
		}
		
	}

}
