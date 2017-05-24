package servlet.materia;

import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Materia;
import beans.Nivel;
import beans.NivelGrado;
import beans.NivelGradoCombos;
import beans.TrcnMat;
import beans.Usuario;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;
import util.string.StringUtil;

/**
 * Servlet implementation class ModificaMat
 */
@WebServlet("/ModificaMat")
public class ModificaMat extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Lista del nivel **/
	private List<Nivel> listaNivel;
	/** Lista del nivel grado **/
	private NivelGradoCombos ngc;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "ModificaMat.jsp";
	/** Nombre del key **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del key **/
	private final String KEY_VARIABLE_TITLE = "title";
	/** Nombre del key **/
	private final String KEY_VARIABLE_BOTON = "boton";
	/** Nombre del form de niveles **/
	private final String KEY_VARIABLE_NIVELES = "niveles";
	/** Nombre del form de niveles **/
	private final String KEY_VARIABLE_NIVELES_GRADO = "nivelGrados";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_MATERIA = "materia";
	/** Nombre del form **/
	private final String KEY_VARIABLE_REGRESO = "hidBack";
	/** Nombre del form **/
	private final String KEY_VARIABLE_TIPO = "hidTipo";
	/** Nombre del form de cveMat **/
	private final String KEY_REQUEST_PARAM_CVEMAT = "cveMat";
	/** Nombre del key **/
	private final String KEY_REQUEST_PARAM_TIPO = "tipo";
	/** Nombre del key **/
	private final String KEY_REQUEST_PARAM_REGRESO = "back";
	
	/** Nombre del form del clave **/
	private final String KEY_FORM_CLAVE = "clave";
	/** Nombre del form del nombre de la materia **/
	private final String KEY_FORM_NOM_MAT = "nomMat";
	/** Nombre del form del nombre de la materia **/
	private final String KEY_FORM_NIVEL = "nivel";
	/** Nombre del form del nombre de la materia **/
	private final String KEY_FORM_NIVEL_GRADO = "nivelGrado";
	/** Nombre del form del semestre de la materia **/
	private final String KEY_FORM_SEMESTRE = "semestre";
	/** Nombre del form del unidad de la materia **/
	private final String KEY_FORM_UNIDAD = "unidad";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_REGRESO = "hidBback";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_TIPO = "hidTipo";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_MAT = "hidCveMat";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_FECHA_MODIF = "hidFechaModf";
	
	/** Value del title **/
	private final String VALUE_TITLE_AGREGAR = "Agregar Materia";
	/** Value del title **/
	private final String VALUE_TITLE_MODIFICAR = "Modificar Materia";
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
    public ModificaMat() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;
		
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
				
			Configuracion config = new Configuracion();
			rd = getServletConfig().getServletContext().getRequestDispatcher(
					config.getPathMateria() + NOMBRE_DE_PAGINA);
			    rd.forward(request,response);
		
			}	 else{
			
				Common.Error(Common.MENSAJE_ERROR_AUTENTIFICA + " " + NOMBRE_DE_PAGINA);
				response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
				return;
			}
		}		
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;	
		Configuracion config = new Configuracion();

		usuario = new Usuario(request, response);
		if(usuario.IsAutorizado()){
			if(usuario.isAdministrador() || usuario.isAdministradorGeneral()){

				try{
					
					String cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
					String hidTipo = request.getParameter(KEY_FORM_HIDDEN_TIPO);
					if(hidTipo.equals(VALUE_REQUEST_PARAM_TIPO_AGREGAR)){
						//Agregar la materia
						if(IsValidatedAgregar(request)){
							cveMat = AgregarMateria(request);
						}else{
							SetForm(request, true);
							rd = getServletConfig().getServletContext().getRequestDispatcher(
									config.getPathMateria() + NOMBRE_DE_PAGINA);
						    rd.forward(request,response);
						    return;
						}
						
					}else if(hidTipo.equals(VALUE_REQUEST_PARAM_TIPO_MODIFICAR)){
						//Modificar la materia
						if(IsValidated(request)){
							ModificarMateria(request, cveMat.replace(" ", ""));
						}else{
							SetForm(request, true);
							rd = getServletConfig().getServletContext().getRequestDispatcher(
									config.getPathMateria() + NOMBRE_DE_PAGINA);
						    rd.forward(request,response);
						    return;
						}
					}
				
					SetForm(request, false);
					
					response.sendRedirect(Common.NOMBRE_DE_PAGINA_MODIFICA_PERMISO_CONTENIDO 
							+ "?cveMat=" + cveMat);

		
				} catch (Exception e) {
					Common.Error(e);
					response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
					return;
				}
			} else{
				Common.Error(Common.MENSAJE_ERROR_AUTENTIFICA + " " + NOMBRE_DE_PAGINA);
				response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
				return;			
			}
		}
	}

	private boolean IsValidatedAgregar(HttpServletRequest request) throws Exception {

		String cveMat = request.getParameter(KEY_FORM_CLAVE);
		Materia bean = new Materia();
		
		try {
			bean = Materia.Buscar(cveMat);
		} catch (Exception e) {
			throw e;
		}
		
		if(bean.getCveMat() != null && bean.getCveMat() != ""){
			request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_MISMA_CLAVE_MATERIA);
			return false;
		}
		
		return true;
	}

	private boolean IsValidated(HttpServletRequest request) throws Exception {
		try {
			if(request.getParameter(KEY_FORM_NOM_MAT).length() > 50){
				request.setAttribute(KEY_VARIABLE_MESSAGE, 
						"Puede ingresar el nombre de la materia hasta 50 letras");
				return false;
			}
			Materia m;
				m = Materia.Buscar(request.getParameter(KEY_FORM_HIDDEN_CVE_MAT));
			
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = format.parse(request.getParameter(KEY_FORM_HIDDEN_FECHA_MODIF));
			Date fechaModf = format.parse(m.getFechaModf().toString());

			if(fechaModf.compareTo(date) > 0){
				request.setAttribute(KEY_VARIABLE_MESSAGE, 
						"Alguien ha cambiado la materia. Vuelve a cargar y modifica, por favor.");
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
	private void ModificarMateria(HttpServletRequest request, String cveMat) throws Exception {
		Connection con = MySqlConnector.getConnection();
		try {
			con.setAutoCommit(false);

			String nivelval = request.getParameter(KEY_FORM_NIVEL);
			
			Materia m = new Materia();
			m.setCveMat(cveMat);
			m.setNomMat(request.getParameter(KEY_FORM_NOM_MAT));
			
			int nivelgrado = 0;
			if(StringUtil.isNumber(request.getParameter(KEY_FORM_NIVEL_GRADO + nivelval))){
				nivelgrado = Integer.parseInt(request.getParameter(KEY_FORM_NIVEL_GRADO + nivelval));
			}
			
			m.setNivelGrado(nivelgrado);
			
			if(nivelgrado <= 0){
				m.setNivel(Integer.parseInt(nivelval));
			}
			
			m.setModulo(Integer.parseInt(request.getParameter(KEY_FORM_SEMESTRE)));
//			m.setUnidad(Integer.parseInt(request.getParameter(KEY_FORM_UNIDAD)));
			m.Modificar(con);
			
			Common.InsertLogAct(request, con, usuario.getCveUsu(), MessageFormat.format(Common.TEXTO_ACTION_LOG_MATERIA_MODIFICAR, cveMat));
			
			con.commit();
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
	 * Agregar la materia
	 * @param request
	 * @throws Exception
	 */
	private String AgregarMateria(HttpServletRequest request) throws Exception {
		Connection con = MySqlConnector.getConnection();
		String nivelval = request.getParameter(KEY_FORM_NIVEL);
		String cveMat = request.getParameter(KEY_FORM_CLAVE).replace(" ", "");

		try {
			con.setAutoCommit(false);

			int nivelgrado = 0;
			if(StringUtil.isNumber(request.getParameter(KEY_FORM_NIVEL_GRADO + nivelval))){
				nivelgrado = Integer.parseInt(request.getParameter(KEY_FORM_NIVEL_GRADO + nivelval));
			}
			
			Materia m = new Materia();
			
			m.setCveMat(cveMat);
			m.setNivelGrado(nivelgrado);
			m.setNivel(Integer.parseInt(nivelval));
			m.setNomMat(request.getParameter(KEY_FORM_NOM_MAT));
			m.setModulo(Integer.parseInt(request.getParameter(KEY_FORM_SEMESTRE)));
			m.setUnidad(Integer.parseInt(request.getParameter(KEY_FORM_UNIDAD)));
			m.Insertar(con);
			
			AgregarTrcnMateria(con, m);
			
			Common.InsertLogAct(request, con, usuario.getCveUsu(), MessageFormat.format(Common.TEXTO_ACTION_LOG_MATERIA_AGREGAR, m.getCveMat()));
			
			con.commit();
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
		return cveMat;
	}

	/**
	 * Agregar los contenidos
	 * @param con
	 * @param m
	 * @throws Exception
	 */
	private void AgregarTrcnMateria(Connection con, Materia m) throws Exception {
		
		try {
			if(m.getNivel() == Common.NIVEL_BACHILLERATO){
				InsartarIntroduccion(con, m.getCveMat());
				InsartarGeneraridades(con, m.getCveMat());
				int cve = InsartarContenidoMateria(con, m.getCveMat());
				InsartarUnidad(con, m, cve);
				
			} else if(m.getNivel() == Common.NIVEL_DOCTORADO){
				InsartarIntroduccion(con, m.getCveMat());
				int cve = InsartarContenidoMateria(con, m.getCveMat());
				InsartarUnidad(con, m, cve);

			} else if(m.getNivel() == Common.NIVEL_LICENCIATURA){
				InsartarIntroduccion(con, m.getCveMat());
				InsartarGeneraridades(con, m.getCveMat());
				int cve = InsartarContenidoMateria(con, m.getCveMat());
				InsartarUnidad(con, m, cve);
			
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Agregar las unidades
	 * @param con
	 * @param m
	 * @param cve
	 * @throws Exception
	 */
	private void InsartarUnidad(Connection con, Materia m, int cve) throws Exception {
		for(int i=1; i <= m.getUnidad(); i++){
			TrcnMat bean = new TrcnMat();
			bean = new TrcnMat();
			bean.setCaption(Common.CAPTION_CONTENIDO_UNIDAD + i);
			bean.setCveMat(m.getCveMat());
			bean.setHandler(null);
			bean.setMenuItemBan(1);
			bean.setMenuItemParentId(cve);
			bean.setUnidad(i);
			bean.setApartado(0);
			bean.setOrden(i);
			bean.Insertar(con);
		}
		
	}

	/**
	 * Agregar la contenido materia
	 * @param con
	 * @param cveMat
	 * @return
	 * @throws Exception
	 */
	private int InsartarContenidoMateria(Connection con, String cveMat) throws Exception {
		TrcnMat bean = new TrcnMat();
		bean.setCaption(Common.CAPTION_CONTENIDO_CONTENIDO_MATERIA);
		bean.setCveMat(cveMat);
		bean.setHandler(null);
		bean.setMenuItemBan(1);
		bean.setMenuItemParentId(0);
		return bean.Insertar(con);
		
	}

	/**
	 * Agregar generalidades
	 * @param con
	 * @param cveMat
	 * @throws Exception
	 */
	private void InsartarGeneraridades(Connection con, String cveMat) throws Exception {
		TrcnMat bean = new TrcnMat();
		bean.setCaption(Common.CAPTION_CONTENIDO_GENERALIDADES);
		bean.setCveMat(cveMat);
		bean.setHandler(null);
		bean.setMenuItemBan(1);
		bean.setMenuItemParentId(0);
		bean.Insertar(con);
		
	}

	/**
	 * Agregar la introducción
	 * @param con
	 * @param cveMat
	 * @throws Exception
	 */
	private void InsartarIntroduccion(Connection con, String cveMat) throws Exception {
		TrcnMat bean = new TrcnMat();
		bean.setCaption(Common.CAPTION_CONTENIDO_INTRODUCCION);
		bean.setCveMat(cveMat);
		bean.setHandler("Cont" + cveMat + Common.EXTENSION_JSP);
		bean.setMenuItemBan(1);
		bean.setMenuItemParentId(0);
		bean.Insertar(con);
	}

	/**
	 * Set form
	 * @param request
	 * @throws Exception
	 */
	private void SetForm(HttpServletRequest request, boolean hasError) throws Exception {
				
		String cveMat = request.getParameter(KEY_REQUEST_PARAM_CVEMAT);
		if(cveMat == null){
			cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
		}
		
		String hidRegreso = request.getParameter(KEY_REQUEST_PARAM_REGRESO);
		if(hidRegreso == null){
			hidRegreso = request.getParameter(KEY_FORM_HIDDEN_REGRESO); 
		}
		request.setAttribute(KEY_VARIABLE_REGRESO, hidRegreso);			
		
		request.setAttribute(KEY_VARIABLE_NIVELES, CrearComboNivel());			
		request.setAttribute(KEY_VARIABLE_NIVELES_GRADO, CrearComboNivelGrado());			


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
	    		Materia m = Materia.Buscar(cveMat);
	    		
	    		if(hasError){
	    			m.setCveMat(request.getParameter(KEY_FORM_HIDDEN_CVE_MAT));
	    			m.setNomMat(request.getParameter(KEY_FORM_NOM_MAT)); 
	    			String nivel = request.getParameter(KEY_FORM_NIVEL);
	    			String str = "";
	    			if(StringUtil.isNumber(nivel)){
	    				m.setNivel(Integer.parseInt(nivel));
	    			}
	    			
	    			str = request.getParameter(KEY_FORM_NIVEL_GRADO + nivel);
	    			if(StringUtil.isNumber(str)){
		    			m.setNivelGrado(Integer.parseInt(str)); 
	    			}
	    			str = request.getParameter(KEY_FORM_SEMESTRE);
	    			if(StringUtil.isNumber(str)){
		    			m.setModulo(Integer.parseInt(str)); 
	    			}
	    			str = request.getParameter(KEY_FORM_UNIDAD);
	    			if(StringUtil.isNumber(str)){
		    			m.setUnidad(Integer.parseInt(str)); 
	    			}
	    		}

	    		SetSelectedNivel(m.getNivel());    		
	    		SetSelectedNivelGrado(m.getNivel(), m.getNivelGrado());  
				request.setAttribute(KEY_VARIABLE_MATERIA, m);

			} catch (Exception e) {
				throw e;
			}		

		} else{
			//mostrar la página de agregar
			request.setAttribute(KEY_VARIABLE_TITLE, VALUE_TITLE_AGREGAR);
			request.setAttribute(KEY_VARIABLE_BOTON, VALUE_BOTON_AGREGAR);

			try {
	    		
	    		if(hasError){
		    		Materia m = new Materia();
	    			m.setCveMat(request.getParameter(KEY_FORM_CLAVE));
	    			m.setNomMat(request.getParameter(KEY_FORM_NOM_MAT)); 
	    			String nivel = request.getParameter(KEY_FORM_NIVEL);
	    			String str = "";
	    			if(StringUtil.isNumber(nivel)){
	    				m.setNivel(Integer.parseInt(nivel));
	    			}
	    			
	    			str = request.getParameter(KEY_FORM_NIVEL_GRADO + nivel);
	    			if(StringUtil.isNumber(str)){
		    			m.setNivelGrado(Integer.parseInt(str)); 
	    			}
	    			str = request.getParameter(KEY_FORM_SEMESTRE);
	    			if(StringUtil.isNumber(str)){
		    			m.setModulo(Integer.parseInt(str)); 
	    			}
	    			str = request.getParameter(KEY_FORM_UNIDAD);
	    			if(StringUtil.isNumber(str)){
		    			m.setUnidad(Integer.parseInt(str)); 
	    			}

	    			SetSelectedNivel(m.getNivel());    		
		    		SetSelectedNivelGrado(m.getNivel(), m.getNivelGrado());  
					request.setAttribute(KEY_VARIABLE_MATERIA, m);
	    		}

			} catch (Exception e) {
				throw e;
			}		

		
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
	 * Crear Combo de nivel grado
	 * @return
	 * @throws Exception 
	 */
	private NivelGradoCombos CrearComboNivelGrado() throws Exception {
		List<NivelGrado> listaNivelGrado;
		ngc = new NivelGradoCombos();

		try {
			
			for (Nivel nivel : listaNivel) {
				listaNivelGrado = NivelGrado.Buscar(nivel.getCveNivel());
				ngc.add(listaNivelGrado);
			}

			return ngc;

		} catch (Exception e) {
			throw e;
		}		
	}

	/**
	 * Colocar "selected" en Combo de Nivel
	 * @param nivel
	 */
	private void SetSelectedNivel(int nivel){
		for(int i=0; i <= listaNivel.size()-1; i++){
			if(((Nivel)listaNivel.get(i)).getCveNivel() == nivel){
				((Nivel)listaNivel.get(i)).setSelected("selected");
			} else {
				((Nivel)listaNivel.get(i)).setSelected("");
			}
		}
	}
	/**
	 * Colocar "selected" en Combo de Nivel Grado
	 * @param nivel
	 */
	private void SetSelectedNivelGrado(int nivel ,int nivelGrado){
		for(int i=0; i <= ngc.size()-1; i++){
			
			List<NivelGrado> comboNivelGrado = (List<NivelGrado>)ngc.get(i);
			
			if(comboNivelGrado.size() > 0 && 
					comboNivelGrado.get(0).getCveNivel() == nivel){
				
				for(int j=0; j<=comboNivelGrado.size()-1; j++ ){
					NivelGrado ng = comboNivelGrado.get(j);
					if(ng.getCveNivelGrado() == nivelGrado){
						ng.setSelected("selected");						

					} else{
						ng.setSelected("");
					}
				}
			}
		}
	}

}
