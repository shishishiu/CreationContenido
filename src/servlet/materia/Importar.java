package servlet.materia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.sql.Clob;
import java.sql.Connection;
import java.text.MessageFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Materia;
import beans.MateriaSolicitud;
import beans.Nivel;
import beans.NivelGrado;
import beans.NivelGradoCombos;
import beans.TrcnMat;
import beans.Usuario;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;
import util.string.StringUtil;

import org.apache.commons.io.*;

/**
 * Servlet implementation class Importar
 */
@WebServlet("/Importar")
public class Importar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "Importar.jsp";
	/** Nombre del form de niveles **/
	private final String KEY_VARIABLE_NIVELES = "niveles";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form del nombre **/
	private final String KEY_VARIABLE_NOMBRE = "nombre";
	/** Nombre del form de niveles **/
	private final String KEY_VARIABLE_NIVELES_GRADO = "nivelGrados";
	/** Nombre del form de nivel **/
	private final String KEY_VARIABLE_EXIST_RESULTADO = "existResultado";
	/** Nombre del form de materias **/
	private final String KEY_VARIABLE_MATERIAS = "materias";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_NUMERO_TOTAL = "numtotal";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PAGINAS = "paginas";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_CURRENT_PAGINA = "currentPagina";
	/** Nombre del form de nivel **/
	private final String KEY_FORM_NIVEL = "nivel";
	/** Nombre del form del nombre **/
	private final String KEY_FORM_NOMBRE = "cveMat";
	/** Nombre del form del nombre de la materia **/
	private final String KEY_FORM_NIVEL_GRADO = "nivelGrado";
	/** Nombre del variable **/	
	private final String KEY_FORM_HIDDEN_TIPO = "hiddenTipo";
	/** Nombre del variable **/	
	private final String KEY_FORM_HIDDEN_CVE_MAT = "hiddenCveMat";
	/** Nombre del variable **/	
	private final String KEY_FORM_HIDDEN_NIVEL = "hiddenNivel";
	/** Nombre del param de dar de baja **/
	private final String KEY_HIDDEN_CURRENT_PAGINA = "hiddenCurrentPagina";
	/** Nombre del variable **/	
	private final String KEY_TIPO_VALIDACION = "1";
	/** Nombre del variable **/	
	private final String KEY_TIPO_IMPORTAR = "2";
	/** Nombre del variable **/	
	private final String KEY_TIPO_BUSCAR = "3";

	/** Lista del nivel **/
	private List<Nivel> listaNivel;
	/** Lista del nivel grado **/
	private NivelGradoCombos ngc;

	/** Usuario **/
	public Usuario usuario;
	Configuracion config = new Configuracion();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Importar() {
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
		RequestDispatcher rd = null;	
		String tipo = request.getParameter(KEY_FORM_HIDDEN_TIPO);
		
		try {
    	
			usuario = new Usuario(request, response);
    		if(usuario.IsAutorizado()){
    			if(usuario.isAdministrador() || usuario.isAdministradorGeneral()){

					int currentPagina = GetCurrentPagina(request);
					int numfrom = ((currentPagina-1)*Common.NUMERO_DE_DATOS_PARA_MOSTRAR);

    				if(tipo.equals(KEY_TIPO_VALIDACION)){
    					Validacion(request, response);
   						return;
    					
    				}else if(tipo.equals(KEY_TIPO_IMPORTAR)){
        				ImportarMateria(request);
    					request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_TERMINAR_PROCESO);
    					
    				}else if(tipo.equals(KEY_TIPO_BUSCAR)){
						numfrom = 0;
    				}	
    				
    				
    				int count = BuscarMateria(request, numfrom);
    				request.setAttribute(KEY_VARIABLE_PAGINAS, Common.CreateListNumeroPagina(request, count, currentPagina));	    
    				
    				
    				
    				
    			}else{
 
					Common.Error(Common.MENSAJE_ERROR_AUTENTIFICA);

					if(Common.IsAjax(request)){
        				Common.MsgJson(Common.JSON_STATUS_NO_AUTORIZADO, Common.NOMBRE_DE_PAGINA_AUTENTIFICA_ERROR, response);
    					
    				} else{
    					response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
    					
    				}
    				
        			return;
        		}
    			
    			SetForm(request);

				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathMateria() + NOMBRE_DE_PAGINA);
			    rd.forward(request,response);		
    		
    		}
    	} catch (Exception e) {
			Common.Error(e);

			if(Common.IsAjax(request)){
				Common.MsgJson("error", Common.MENSAJE_ERROR, response);
				
			} else{
				response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
				
			}

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

	private int BuscarMateria(HttpServletRequest request, int numfrom) throws Exception{
		List<Materia> list = new ArrayList<Materia>();
		String strNivel = request.getParameter(KEY_FORM_NIVEL);
		String nivelGrado = request.getParameter(KEY_FORM_NIVEL_GRADO + strNivel);
		int count = 0;
		
		int nivel = Integer.parseInt(strNivel);
		Connection con = null;

		switch (nivel) {
			case Common.NIVEL_BACHILLERATO:
				con = MySqlConnector.getConnectionBac();
				list = Materia.BuscarDeExternoBac(con, numfrom, Common.NUMERO_DE_DATOS_PARA_MOSTRAR);
				count = Materia.CountMateriaDeExternoBac(con);
				break;
			case Common.NIVEL_LICENCIATURA:
				con = MySqlConnector.getConnectionLic();
				list = Materia.BuscarDeExternoLic(con, Integer.parseInt(nivelGrado), numfrom, Common.NUMERO_DE_DATOS_PARA_MOSTRAR);
				count = Materia.CountMateriaDeExternoLic(con, Integer.parseInt(nivelGrado));
				break;
			default:
				request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_NO_SE_ELIGE_NIVEL);
				return 0;
		}
		
		request.setAttribute(KEY_VARIABLE_MATERIAS, list);
		request.setAttribute(KEY_VARIABLE_EXIST_RESULTADO, list.size() > 0);
		request.setAttribute(KEY_VARIABLE_NUMERO_TOTAL, count);
		
		return count;
		
	}

	private boolean ImportarMateria(HttpServletRequest request) throws Exception{

		String cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
		String strNivel = request.getParameter(KEY_FORM_HIDDEN_NIVEL);

		
		Connection conExt = null;
		
		int nivel = Integer.parseInt(strNivel);

		switch (nivel) {
			case Common.NIVEL_BACHILLERATO:
				conExt = MySqlConnector.getConnectionBac();
				break;
			case Common.NIVEL_LICENCIATURA:
				conExt = MySqlConnector.getConnectionLic();
				break;
			default:
				break;
		}
		
		
		//importar los archivos de los contenidos
		CopiarArchivos(cveMat, nivel, request, conExt);
		
		//importar base de datos
		Insertar(cveMat, nivel, request, conExt);
		
		
		return true;
	}

	private void CopiarArchivos(String strCveMats, int nivel, HttpServletRequest request, Connection conExt) throws Exception {
		
//		String server = config.getFtpLicServer();
//		int port = config.getFtpLicPort();
//		String user = config.getFtpLicUser();
//		String password = config.getFtpLicPassword();
//		Sftp ftp = new Sftp(server, port, user, password, true, true, "ISO-8859-1");

//		int nivGrado = Integer.parseInt(request.getParameter(KEY_FORM_HIDDEN_NIVEL_GRADO));
//		NivelGrado beanNivGrado = NivelGrado.BuscarConCveNivGrado(nivGrado);

		
		String[] arrCveMat = strCveMats.split(",");
		for(String cveMat : arrCveMat){
		
			Materia bean = Materia.BuscarDeExterno(conExt, cveMat);
			String rootPath = "";
			switch (nivel) {
				case Common.NIVEL_BACHILLERATO:
					rootPath = config.getAbsolutePathBac();
					break;
				case Common.NIVEL_LICENCIATURA:
					rootPath = config.getAbsolutePathLic();
					break;
				default:
					break;
			}


			String pathMat = bean.GetPathMateria(rootPath);
			if(nivel == Common.NIVEL_LICENCIATURA){
				pathMat = pathMat.replace("/Lic/", "/lic/");
			}

			
			File srcDir = new File(pathMat);
			File destDir = new File(bean.GetPathMateriaAbsolute());
			
			FileUtils.copyDirectory(srcDir, destDir);
			
			
			
//		    try {
//
//		      ftp.Connect();
//	          
//	          ftp.Get(beanNivGrado.getNomGrado() + "/" + bean.getModulo() + "/" + cveMat , bean.GetPathMateriaAbsolute());
//	          
//		    } catch(Exception e) {
//		    	throw e;
//		    } finally {
//		    	if(ftp.getIsConnected()) {
//			        try {
//			        	ftp.Disconnect();
//			        } catch(IOException ioe) {
//			        	
//			        }
//		        }
//		    }		
		}
		
	}

	private void Insertar(String cveMat, int nivel, HttpServletRequest request, Connection conExt) throws Exception {
		Connection conConVirt = null;
		
		try{
			
			conConVirt = MySqlConnector.getConnection();
			conConVirt.setAutoCommit(false);

			String[] arrCveMat = cveMat.split(",");
			for(String materia : arrCveMat){
				
				//Si hay misma materia en este sistema, se la borra
				Materia beanExistMat = Materia.Buscar(materia);
				if(!beanExistMat.getCveMat().equals("")){
					Materia.Delete(conConVirt, materia);
					Common.InsertLogAct(request, conConVirt, usuario.getCveUsu(), 
							MessageFormat.format(Common.TEXTO_ACTION_LOG_MATERIA_BORRAR, materia));
				}
				
				//Si hay contenidos de la misma materia en este sistema, se los borra
				List<TrcnMat> listExistCont = TrcnMat.Buscar(materia, true);
				if(listExistCont.size()>0){
					TrcnMat.Delete(conConVirt, materia);
					Common.InsertLogAct(request, conConVirt, usuario.getCveUsu(), 
							MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_BORRAR, materia));
				}
				
				//Borra logicamente los solicitudes
				MateriaSolicitud.DarBajaLogical(conConVirt, materia);
				

				Nivel beanNiv = Nivel.Buscar(nivel);
				int nivGrado = Integer.parseInt(request.getParameter(KEY_FORM_NIVEL_GRADO + nivel));
				NivelGrado beanNivGrado = NivelGrado.BuscarConCveNivGrado(nivGrado);
				
				//Insertar materia
				Materia beanMat = Materia.BuscarDeExterno(conExt, materia);
				beanMat.setNivelGrado(nivGrado);
				beanMat.setNomNivelDir(beanNiv.getNomDir());
				beanMat.setNomGrado(beanNivGrado.getNomGrado());
				
				beanMat.Insertar(conConVirt);
				Common.InsertLogAct(request, conConVirt, usuario.getCveUsu(), 
						MessageFormat.format(Common.TEXTO_ACTION_LOG_MATERIA_IMPORTAR, materia));
				
				//Insertar contenidos
				List<TrcnMat> listCont = TrcnMat.BuscarDeExterno(conExt, materia);
				for(TrcnMat beanCont : listCont){
					String handler = beanCont.getHandler();
					
					if(handler != null){
						int apartado = 0;
						int unidad = 0;
						
						String[] arrStr = handler.split("_");
						if(arrStr.length > 1){
							
							unidad = Integer.parseInt(arrStr[1]);
							apartado = Integer.parseInt(arrStr[2]); 
							
							beanCont.setApartado(apartado);
							beanCont.setUnidad(unidad);
						}
						
					}else if(handler == null && beanCont.getCaption().indexOf(Common.CAPTION_CONTENIDO_UNIDAD)>=0){
						
						int unidad = 0;
						String str = beanCont.getCaption().replaceAll("[^0-9][^I II III IV V VI VII VIII IX X]","");
						
						if(str.equals("I")){str = "1";}
						else if(str.equals("II")){str = "2";}
						else if(str.equals("III")){str = "3";}
						else if(str.equals("IV")){str = "4";}
						else if(str.equals("V")){str = "5";}
						else if(str.equals("VI")){str = "6";}
						else if(str.equals("VII")){str = "7";}
						else if(str.equals("VIII")){str = "8";}
						else if(str.equals("IX")){str = "9";}
						else if(str.equals("X")){str = "10";}
						
						if(StringUtil.isNumber(str)){
							unidad = Integer.parseInt(str);
							beanCont.setUnidad(unidad);
							
						}						
					}
					
					
					beanCont.setContenido(GetHtml(beanMat, beanCont, conConVirt));
					int cveCont = beanCont.Insertar(conConVirt);
					Common.InsertLogAct(request, conConVirt, usuario.getCveUsu(), 
							MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_IMPORTAR, cveCont));
				}	
			}
			
			conConVirt.commit();

		} catch (Exception e) {
			if (conConVirt != null) {
				conConVirt.rollback();
			}			
			throw e;
		} finally{
			if (conExt != null) {
				conExt.close();
	        }			
			if (conConVirt != null) {
				conConVirt.setAutoCommit(true);
				conConVirt.close();
			}			
		}		
		
	}

	private Clob GetHtml(Materia materia, TrcnMat beanCont, Connection conConVirt) throws Exception {
		Clob myClob;

		try {

			if(beanCont.getHandler() != null && !beanCont.getHandler().equals("") && beanCont.getHandler().indexOf(Common.EXTENSION_JSP)>=0){
			
				String absolutePath = materia.GetPathMateriaAbsolute() + File.separator + beanCont.getHandler();

				
	        	FileReader fr = new FileReader(absolutePath);
	        	String PalRes="<!-- InstanceBeginEditable name=\"Trabajo\" -->";
	        	String PalRes2 = "<!-- InstanceEndEditable -->";
	        	
	            String Cadena = "";
	               
	            BufferedReader br = new BufferedReader(fr);
	         
	            String linea;
	            int Ban =0;
	            int resultado =  0;
	                 
	                
	            while((linea = br.readLine()) != null){
	            	// VERIFICA DONDE INICIA  EL TEXTO A TRABAJAR
	            	resultado = linea.indexOf(PalRes);  
				    if(resultado != -1)                  
				    {
				    	resultado = resultado + PalRes.length();
				        Ban =1;
				        linea = linea.substring(resultado, linea.length());
				    }
				          
				    if (Ban == 1 && linea.indexOf(PalRes2) != -1)
				    {
				    	linea = linea.substring(0, linea.indexOf(PalRes2));
			            Ban = 2;
				    }
				    if (Ban == 1 || Ban == 2)
				    {
				    	Cadena = Cadena + linea;
				        if ( Ban == 2)
				        {
				            Ban = 0;
				        }
				    }
	 
	            }
	         
	            fr.close();
	         	
				myClob = conConVirt.createClob();
								
				String pathMat = materia.GetPathMateriaRelative();
				if(beanCont.getCaption().equals(Common.CAPTION_CONTENIDO_INTRODUCCION)){
					
					Cadena = Cadena.replaceAll(Common.CARPETA_IMG + "/" + Common.CARPETA_IMG_INT + "/",
							Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_IMG + "/" + Common.CARPETA_IMG_INT + "/"));
				
				}else{

					Cadena = Cadena.replaceAll(Common.CARPETA_IMG + "/",
							Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_IMG + "/"));
					
				}

				Cadena = Cadena.replaceAll(Common.CARPETA_HTML5 + "/",
								Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_HTML5 + "/"));
				Cadena = Cadena.replaceAll(Common.CARPETA_SONIDO + "/",
								Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_SONIDO + "/"));
				Cadena = Cadena.replaceAll(Common.CARPETA_ACTIVIDADES + "/",
								Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_ACTIVIDADES + "/"));
				Cadena = Cadena.replaceAll(Common.CARPETA_ACTIVIDADES_COMPLEMENTARIAS + "/",
								Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_ACTIVIDADES_COMPLEMENTARIAS + "/"));
				Cadena = Cadena.replaceAll(Common.CARPETA_GNERALIDADES + "/",
								Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_GNERALIDADES + "/"));

						
		        myClob.setString(1, Cadena);			
				return myClob;
			}

		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private boolean Validacion(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String cveMat = request.getParameter(KEY_FORM_NOMBRE);
		String strNivel = request.getParameter(KEY_FORM_NIVEL);

		
		String message = "";
		int nivel = 0;
		Connection con = null;
		
		try {
			if(!StringUtil.isNumber(strNivel)){
				message += "Ingresa nivel, por favor.<br>";
			}
			if(cveMat == null || cveMat.equals("")){
				message += "Ingresa materia, por favor.<br>";
			}
			if(!message.equals("")){
				Common.MsgJson("error", message, response);
				return false;
			}
	
			String[] arrCveMat = cveMat.split(",");
			nivel = Integer.parseInt(strNivel);
			String nomSistema = "";
			
			// no hay materia en el sistema de bac o lic
			switch (nivel) {
			case Common.NIVEL_BACHILLERATO:
				con = MySqlConnector.getConnectionBac();
				nomSistema = Common.NIVEL_BACHILLERATO_STR;
				break;
			case Common.NIVEL_LICENCIATURA:
				con = MySqlConnector.getConnectionLic();
				nomSistema = Common.NIVEL_LICENCIATURA_STR;
				break;
			default:
				message = Common.MENSAJE_ERROR_IMPORTAR;
				Common.MsgJson("error", message, response);
				return false;
			}
		
			for(String materia : arrCveMat){
				boolean existMateria = Materia.ExistsMateria(con, materia);
				if(!existMateria){
					message += MessageFormat.format(Common.MENSAJE_ERROR_IMPORTAR_NO_HAY_MATERIA, materia, nomSistema);
				}
			}
			
			if(!message.equals("")){
				Common.MsgJson("error", message, response);
				return false;
			}

			con = MySqlConnector.getConnection();
			for(String materia : arrCveMat){
				boolean existMateria = Materia.ExistsMateria(con, cveMat);
				if(existMateria){
					message += MessageFormat.format(Common.MENSAJE_CONFIRMAR_IMPORTAR, materia);
				}
			}
			
			Common.MsgJson("success", message, response);
						
		} catch (Exception e) {
			Common.Error(e);
			Common.MsgJson("error", Common.MENSAJE_ERROR, response);
			return false;
		} finally{
			if (con != null) {
				con.close();
	        }
		}

		return true;
	}

	private void SetForm(HttpServletRequest request) throws Exception {
		try {

			if(this.listaNivel == null){
					this.listaNivel = CrearComboNivel();
			}
			request.setAttribute(KEY_VARIABLE_NIVELES, this.listaNivel);
			request.setAttribute(KEY_VARIABLE_NIVELES_GRADO, CrearComboNivelGrado());
			
			String cveMat = request.getParameter(KEY_FORM_NOMBRE);
			String nivel = request.getParameter(KEY_FORM_NIVEL);
			String nivelGrado = request.getParameter(KEY_FORM_NIVEL_GRADO + nivel);
			request.setAttribute(KEY_VARIABLE_NOMBRE, cveMat);
			SetSelected(nivel);
			if(StringUtil.isNumber(nivel) && StringUtil.isNumber(nivelGrado)){
	    		SetSelectedNivelGrado(Integer.parseInt(nivel), Integer.parseInt(nivelGrado));  
			}
		
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
