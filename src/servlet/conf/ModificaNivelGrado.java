package servlet.conf;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

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
@WebServlet("/ModificaNivelGrado")
public class ModificaNivelGrado extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "ModificaNivelGrado.jsp";
	/** Nombre del form de nivel **/
	private final String KEY_VARIABLE_NOMBRE_NIVEL = "nombreNivel";
	/** Nombre del form de nivel **/
	private final String KEY_VARIABLE_NOMBRE_GRADO = "nombreGrado";
	/** Nombre del param **/
	private final String KEY_VARIABLE_NOMBRE_NIVEL_GRADO = "nombreNivelGrado";
	/** Nombre del form de nivel **/
	private final String KEY_VARIABLE_CVE_NIVEL = "cveNivel";
	/** Nombre del param **/
	private final String KEY_VARIABLE_CVE_NIVEL_GRADO = "cveNivelGrado";
	/** Nombre del param **/
	private final String KEY_VARIABLE_TIPO = "tipo";
	/** Nombre del form **/
	private final String KEY_VARIABLE_PUEDE_MOSTRAR = "puedeMostrar";
	/** Nombre del form **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form **/
	private final String KEY_VARIABLE_TIENE_MATERIA = "tieneMat";
	/** Nombre del form **/
	private final String KEY_REQUEST_PARAM_NIVEL = "nivel";
	/** Nombre del form **/
	private final String KEY_REQUEST_PARAM_NIVEL_GRADO = "nivgrado";
	/** Nombre del form **/
	private final String KEY_REQUEST_PARAM_TIPO = "tipo";
	/** Nombre del form **/
	private final String KEY_REQUEST_PARAM_TIENE_MATERIA = "tieneMat";

	/** Nombre del form **/
	private final String KEY_POST_PARAM_NIVEL = "nivel";
	/** Nombre del form **/
	private final String KEY_POST_PARAM_NIVEL_GRADO = "nivgrado";
	/** Nombre del form **/
	private final String KEY_POST_PARAM_TIPO = "tipo";
	/** Nombre del form **/
	private final String KEY_POST_PARAM_NOM_NIVEL_GRADO = "nomNivGrado";
	/** Nombre del form **/
	private final String KEY_POST_PARAM_NOM_GRADO = "nomGrado";
	/** Nombre del form **/
	private final String KEY_POST_PARAM_TIENE_MATERIA = "tieneMat";

	
	/** Nombre del param**/
	private final String KEY_TIPO_AGREGAR = "1";
	/** Nombre del param**/
	private final String KEY_TIPO_MODIFICAR = "2";

	
	
	/** Usuario **/
	public Usuario usuario;
	public Configuracion config = new Configuracion();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModificaNivelGrado() {
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

			String tipo = request.getParameter(KEY_POST_PARAM_TIPO);
			if(tipo.equals(KEY_TIPO_AGREGAR)){

				Agregar(request, response);

			}else if(tipo.equals(KEY_TIPO_MODIFICAR)){

				Modificar(request, response);

			}else{
				
			}

		
		}catch(Exception e){
			Common.Error(e);
			Common.MsgJson("error", Common.MENSAJE_ERROR, response);
		}

	}
	
	private void Agregar(HttpServletRequest request, HttpServletResponse response)throws Exception {
		int nivel =0;
		String strCveNivel = request.getParameter(KEY_POST_PARAM_NIVEL);
		String strNomNivelGrado= request.getParameter(KEY_POST_PARAM_NOM_NIVEL_GRADO);
		String strNomGrado = request.getParameter(KEY_POST_PARAM_NOM_GRADO);
		
		if(strNomNivelGrado.length() > 255){
			Common.MsgJson("error", MessageFormat.format(Common.MENSAJE_LIMIT_DE_NUMERO_DE_CARACTER, 255), response);
			return;
		}
		if(strNomGrado.length() > 50){
			Common.MsgJson("error", MessageFormat.format(Common.MENSAJE_LIMIT_DE_NUMERO_DE_CARACTER, 50), response);
			return;
		}

		if(!StringUtil.isNumber(strCveNivel)){
			Common.MsgJson("error", Common.MENSAJE_ERROR, response);
			return;
		}
		
		nivel = Integer.parseInt(strCveNivel);

		if(NivelGrado.TieneMismoNomGrado(0, nivel, strNomGrado)){
			Common.MsgJson("error", Common.MENSAJE_ERROR_MISMO_NOM_GRADO, response);
			return;
			
		}


		Connection con = null;
		try {
		    con = MySqlConnector.getConnection();			
			con.setAutoCommit(false);
			
			NivelGrado bean = new NivelGrado();
			
			bean.setCveNivel(nivel);
			bean.setNomNivelGrado(strNomNivelGrado);
			bean.setNomGrado(strNomGrado);
			int nivelgrado = bean.Insertar(con);
			

			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_NIVEL_GRADO_AGREGAR, nivelgrado));
						
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

	/**
	 * Iniciar
	 */
	private void Iniciar() {
	}

	private void Modificar(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		int nivelgrado = 0;
		int nivel = 0;
		String strCveNivel = request.getParameter(KEY_POST_PARAM_NIVEL);
		String strCveNivelGrado = request.getParameter(KEY_POST_PARAM_NIVEL_GRADO);
		String strNomNivelGrado= request.getParameter(KEY_POST_PARAM_NOM_NIVEL_GRADO);
		String strNomGrado = request.getParameter(KEY_POST_PARAM_NOM_GRADO);
		String tieneMat = request.getParameter(KEY_POST_PARAM_TIENE_MATERIA);
		
		if(!StringUtil.isNumber(strCveNivel)){
			Common.MsgJson("error", Common.MENSAJE_ERROR, response);
			return;
		}
		if(!StringUtil.isNumber(strCveNivelGrado)){
			Common.MsgJson("error", Common.MENSAJE_ERROR, response);
			return;
		}
		
		nivelgrado = Integer.parseInt(strCveNivelGrado);
		nivel = Integer.parseInt(strCveNivel);

		NivelGrado nivelgradoBean = NivelGrado.BuscarConCveNivGrado(nivelgrado);

		if(nivelgradoBean.getNomNivelGrado().equals(strNomNivelGrado) && nivelgradoBean.getNomGrado().equals(strNomGrado)){
			Common.MsgJson("error", Common.MENSAJE_NO_HAY_CAMBIO, response);
			return;
		}
		
		if(strNomNivelGrado.length() > 255){
			Common.MsgJson("error", MessageFormat.format(Common.MENSAJE_LIMIT_DE_NUMERO_DE_CARACTER, 255), response);
			return;
		}

		if(strNomGrado.length() > 50){
			Common.MsgJson("error", MessageFormat.format(Common.MENSAJE_LIMIT_DE_NUMERO_DE_CARACTER, 50), response);
			return;
		}

		
		if(NivelGrado.TieneMismoNomGrado(nivelgrado, nivel, strNomGrado)){
			Common.MsgJson("error", Common.MENSAJE_ERROR_MISMO_NOM_GRADO, response);
			return;
			
		}
		
		
		Connection con = null;
		try {
		    con = MySqlConnector.getConnection();			
			con.setAutoCommit(false);
			
			NivelGrado bean = new NivelGrado();
			
			bean.setCveNivelGrado(nivelgrado);
			bean.setNomNivelGrado(strNomNivelGrado);
			bean.setNomGrado(strNomGrado);
			bean.Modificar(con);
			 	

			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_NIVEL_GRADO_MODIFICAR, nivelgrado));
						
			
			if(tieneMat != null && tieneMat.equals("true")){
				
				if(!ModificaDirectorio(strNomGrado, nivel, nivelgradoBean, response)){
					if (con != null) {
						con.rollback();
					}			
					return;
				}
			}
			
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

	
	private boolean ModificaDirectorio(String strNomGrado, int nivel, NivelGrado nivelgradoBean, HttpServletResponse response) throws Exception {
		
		Nivel nivelBean = Nivel.Buscar(nivel);
	    String userfilePath = config.getUserfilesAbsolutePath();
	    
		File folderActual = new File(userfilePath + File.separator + nivelBean.getNomDir() + File.separator + nivelgradoBean.getNomGrado());
		File folderNew = new File(userfilePath + File.separator + nivelBean.getNomDir() + File.separator + strNomGrado);
		
		if(folderActual.exists()){
			if(!folderActual.renameTo(folderNew)){
				Common.MsgJson("error", Common.MENSAJE_ERROR_NO_CAMBIAR_NOMBRE_FOLDER, response);
				return false;
			}
		}

		return true;
		
	}

	private void SetForm(HttpServletRequest request, boolean isGet) throws Exception {
		try {
			
			String strNivel = request.getParameter(KEY_REQUEST_PARAM_NIVEL);
			String strNivelGrado = request.getParameter(KEY_REQUEST_PARAM_NIVEL_GRADO);
			int nivel;
			int nivelGrado = 0;
			if(!StringUtil.isNumber(strNivel)){
				return;
			}
			if(StringUtil.isNumber(strNivelGrado)){
				nivelGrado = Integer.parseInt(strNivelGrado);
			}
			
			nivel = Integer.parseInt(strNivel);
			Nivel beanNivel = Nivel.Buscar(nivel);
			NivelGrado beanNivGrado = NivelGrado.BuscarConCveNivGrado(nivelGrado);
			
			request.setAttribute(KEY_VARIABLE_CVE_NIVEL, nivel);
			request.setAttribute(KEY_VARIABLE_NOMBRE_GRADO, beanNivGrado.getNomGrado());
			request.setAttribute(KEY_VARIABLE_CVE_NIVEL_GRADO, nivelGrado);
			request.setAttribute(KEY_VARIABLE_NOMBRE_NIVEL, beanNivel.getNomNivel());
			request.setAttribute(KEY_VARIABLE_NOMBRE_NIVEL_GRADO, beanNivGrado.getNomNivelGrado());
			request.setAttribute(KEY_VARIABLE_TIPO, request.getParameter(KEY_REQUEST_PARAM_TIPO));
			
			String strTieneMat = request.getParameter(KEY_REQUEST_PARAM_TIENE_MATERIA);
			request.setAttribute(KEY_VARIABLE_TIENE_MATERIA, strTieneMat);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
}
