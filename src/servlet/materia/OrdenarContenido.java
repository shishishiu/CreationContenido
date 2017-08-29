package servlet.materia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Clob;
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

import beans.TrcnMat;
import beans.Materia;
import beans.Usuario;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;

/**
 * Servlet implementation class BusContenido
 */
@WebServlet("/OrdenarContenido")
public class OrdenarContenido extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "OrdenarContenido.jsp";
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_MENU_ID = "padreId";
	/** Nombre del variable **/	
	private final String KEY_VALIABLE_MENU_ID = "menuId";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MENU_HTML = "menuHtml";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	private final String FILE_SUFFIX = "_ordenar";

	
	/** Usuario **/
	public Usuario usuario;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrdenarContenido() {
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
			Error(response, Common.MENSAJE_ERROR);
		}	
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
    	
			usuario = new Usuario(request, response);
    		if(usuario.IsAutorizado()){
    			Common.LogInfo("start Ordenar");
    			Ordenar(request);
    			Common.LogInfo("end Ordenar");
    		}
    	} catch (Exception e) {
			Common.Error(e);
			Error(response, Common.MENSAJE_ERROR);
		}
	}

	private void Ordenar(HttpServletRequest request) throws Exception {
		
		String menuId = request.getParameter(KEY_VALIABLE_MENU_ID);
		List<int[]> listModificarArchivoCarpeta = new ArrayList<int[]>();
		List<TrcnMat> listModificarCont = new ArrayList<TrcnMat>();
		TrcnMat beancont = TrcnMat.Buscar(Integer.parseInt(menuId));
		String cveMat = beancont.getCveMat();
		StringBuilder strMessage = new StringBuilder();
		
		OrdenarContenidos(menuId,request, listModificarCont);

		Connection con;
		con = MySqlConnector.getConnection();

		try{
	
			
			Common.LogInfo("start ModificarDatos");
			ModificarDatos(listModificarCont, con, cveMat, request, listModificarArchivoCarpeta);
			Common.LogInfo("end ModificarDatos");
			
			con.commit();

			Common.LogInfo("start ModificarArchivoCarpeta");
			ModificarArchivoCarpeta(cveMat, listModificarArchivoCarpeta, strMessage);
			Common.LogInfo("end ModificarArchivoCarpeta");
			
			if(strMessage.length() > 0){
				
				Common.Error(Common.MENSAJE_ERROR_NO_CAMBIAR_NOMBRE_FILE_FOLDER + Common.NEW_LINE + strMessage.toString());
				
			}
			
			
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


	private void ModificarDatos(List<TrcnMat> listModificarCont, Connection con, String cveMat, 
			HttpServletRequest request, List<int[]> listModificarArchivoCarpeta) throws Exception {
		
		
		con.setAutoCommit(false);

		//Modificar
		for(TrcnMat bean : listModificarCont){
			
			//handler
			TrcnMat beanActual = TrcnMat.Buscar(bean.getMenuItem());
			String caption = bean.getCaption();
			int apartado = bean.getApartado();
			int unidad = bean.getUnidad();
			if(caption == null || caption.equals("")){
				caption = beanActual.getCaption();
			}
			if(apartado <= 0){
				apartado = beanActual.getApartado();
			}
			if(unidad <= 0){
				unidad = beanActual.getUnidad();
			}				
			if(unidad > 0 && apartado > 0 && beanActual.getHandler() != null && !beanActual.getHandler().equals("")){
				
				TrcnMat beanHandler = new TrcnMat();
				beanHandler.setApartado(apartado);
				beanHandler.setCveMat(cveMat);
				beanHandler.setCaption(caption);
				beanHandler.setUnidad(unidad);
				beanHandler.setMenuItemParentId(beanActual.getMenuItemParentId());
				String handler = Common.GetHandler(beanHandler);

				bean.setHandler(handler.substring(0,handler.lastIndexOf(Common.EXTENSION_JSP)-1) 
						+ beanActual.getHandler().substring(handler.lastIndexOf(Common.EXTENSION_JSP)-1));
				
				if(bean.getUnidad() > 0 || bean.getApartado() > 0){
					//Si se cambian unidad o apartado, se va a cambiar html
					String contenido = beanActual.getContenido();
					
					if(bean.getUnidad() != beanActual.getUnidad()){
						contenido = contenido.replaceAll(cveMat + "/" + Common.CARPETA_IMG + "/" + beanActual.getUnidad(),
								cveMat + "/" + Common.CARPETA_IMG + "/" + unidad);
					}
					
					contenido = contenido.replaceAll(cveMat + "_" + beanActual.getUnidad() + "_" + beanActual.getApartado(), 
							cveMat + "_" + unidad + "_" + apartado);
					
					Clob myClob = con.createClob();
			        myClob.setString(1, contenido);			

					bean.setContenido(myClob);
					
					int arr[] = new int[4];
					arr[0]= beanActual.getUnidad();
					arr[1]= beanActual.getApartado();
					arr[2]= unidad;
					arr[3]= apartado;
					
					if(!listModificarArchivoCarpeta.contains(arr)){
						listModificarArchivoCarpeta.add(arr);
					}
					
				}
			}
			

			bean.Modificar(con);
			
			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_MODIFICAR,bean.getMenuItem()));
			
		}
		
	}

	private void OrdenarContenidos(String menuId, HttpServletRequest request, List<TrcnMat> listModificarCont) throws Exception {
		String[] arrstr = request.getParameterValues(menuId + "[]");

		if(arrstr != null){

			try {

				int unidad = 0;
				for(int i=0; i<arrstr.length; i++){
				
					String str = arrstr[i];
					String strSubMenuId = str.substring(str.indexOf("_")+1);
					int subMenuId = Integer.parseInt(strSubMenuId);
					
					OrdenarContenidos(strSubMenuId,request, listModificarCont);
					
					TrcnMat bean = new TrcnMat();
					bean.setMenuItem(subMenuId);
					bean.setOrden(i+1);
					
					TrcnMat beanActual = TrcnMat.Buscar(subMenuId);
					if(beanActual.getUnidad()>0 && beanActual.getApartado()<=0){
						//Unidades
						unidad++;
						
						bean.setUnidad(unidad);
						bean.setCaption(Common.CAPTION_CONTENIDO_UNIDAD+unidad);

						ModificarChildUnidad(unidad,beanActual, listModificarCont);
						
						ModificarApartado(beanActual.getMenuItem(), request, listModificarCont);						

					}

					boolean exists = false;
					for(TrcnMat beanModificar : listModificarCont){
						if(beanModificar.getMenuItem() == subMenuId){
							
							beanModificar.setOrden(i+1);
							if(bean.getUnidad()!=0){
								beanModificar.setUnidad(unidad);
								beanModificar.setCaption(Common.CAPTION_CONTENIDO_UNIDAD+unidad);
							}
							
							exists = true;
						}
					}
					if(!exists){
						listModificarCont.add(bean);
					}
						
				}
			

			} catch (Exception e) {
				throw e;
			}		

		}

	}


	private boolean ModificarArchivoCarpeta(String cveMat, List<int[]> listModificarArchivoCarpeta, StringBuilder strMessage) throws Exception {
		//Modificar nombre de la carpeta de la unidad, modificar nombre del archivo de la unidad

		Materia mat = Materia.Buscar(cveMat);
		String pathUserFiles = mat.GetPathMateriaAbsolute();
		String pathImg = pathUserFiles + File.separator + Common.CARPETA_IMG;
		String pathHtml5 = pathUserFiles + File.separator + Common.CARPETA_HTML5;
		String pathSnd = pathUserFiles + File.separator + Common.CARPETA_SONIDO;
		String pathAct = pathUserFiles + File.separator + Common.CARPETA_ACTIVIDADES;
		String pathActCmp = pathUserFiles + File.separator + Common.CARPETA_ACTIVIDADES_COMPLEMENTARIAS;
		String pathGnr = pathUserFiles + File.separator + Common.CARPETA_GNERALIDADES;

		for(int[] arr : listModificarArchivoCarpeta){
			int actualUnidad = arr[0];
			int newUnidad = arr[2];
			
			File folderActual = new File(pathImg + File.separator + actualUnidad);
			File folderNew = new File(pathImg + File.separator + newUnidad + FILE_SUFFIX);
			
			//modificar nombre de la carpeta de las imagenes
			if(folderActual.exists()){
				if(!folderActual.renameTo(folderNew)){
					AddMessage(strMessage, folderActual.getAbsolutePath(), pathImg + File.separator + newUnidad);
				}
			}
			
			//modificar nombre del archivo de las imagenes
			if(folderNew.exists()){
				ModificarArchivo(folderNew, pathImg + File.separator + newUnidad + FILE_SUFFIX, arr, strMessage);
								
			}
			
			//modificar nombre del archivo
			File folder = new File(pathHtml5);
			ModificarArchivo(folder, pathHtml5, arr, strMessage);

			folder = new File(pathSnd);
			ModificarArchivo(folder, pathSnd, arr, strMessage);
			
			folder = new File(pathAct);
			ModificarArchivo(folder, pathAct, arr, strMessage);

			folder = new File(pathActCmp);
			ModificarArchivo(folder, pathActCmp, arr, strMessage);
			
			folder = new File(pathGnr);
			ModificarArchivo(folder, pathGnr, arr, strMessage);

				
		}
		
		QuitarSuffix(pathImg, strMessage);
		QuitarSuffix(pathHtml5, strMessage);
		QuitarSuffix(pathSnd, strMessage);
		QuitarSuffix(pathAct, strMessage);
		QuitarSuffix(pathActCmp, strMessage);
		QuitarSuffix(pathGnr, strMessage);
			
		return true;
		
		
	}

	
	private void AddMessage(StringBuilder strMessage, String pathActual, String pathNew) {
		if(strMessage.length()>0){
			strMessage.append(Common.NEW_LINE);
		}
		strMessage.append(" Antes:" + pathActual + " Después:" + pathNew);

	}

	private void ModificarArchivo(File folder, String path, int arr[], StringBuilder strMessage){

		int actualUnidad = arr[0];
		int actualApartado = arr[1];
		
		int newUnidad = arr[2];
		int newApartado = arr[3];

		
		File files[] = folder.listFiles();
		
		if(files != null){

			for (int i=0; i<files.length; i++) {
				
				String filename = files[i].getName();
				
				if(filename.indexOf(FILE_SUFFIX) >= 0 || filename.indexOf("_") < 0){
					continue;
				}
				
				String filenameAntesUnidad = filename.split("_")[0];
				String filenameUnidad = filename.split("_")[1];
				String filenameApartado = filename.split("_")[2];
				String filenameDespuesApartado= filename.split("_")[3];
				
				
				if(filenameUnidad.equals(Integer.toString(actualUnidad)) 
						&& filenameApartado.equals(Integer.toString(actualApartado))){
					
					if(!filenameUnidad.equals(Integer.toString(newUnidad)) || !filenameApartado.equals(Integer.toString(newApartado))){
						
						File fileDes = new File(path + File.separator 
		            			+ filenameAntesUnidad + "_" + newUnidad + "_" + newApartado + "_" + filenameDespuesApartado + FILE_SUFFIX);

						if(files[i].exists()){
							if(!files[i].renameTo(fileDes)){
								AddMessage(strMessage, files[i].getAbsolutePath(), path + File.separator 
				            			+ filenameAntesUnidad + "_" + newUnidad + "_" + newApartado + "_" + filenameDespuesApartado);
							}
						}
					}
				}
	        }
		}
	}
	
	private void QuitarSuffix(String path, StringBuilder strMessage){
		
		File folder = new File(path);
		File folderes[] = folder.listFiles();
		
		if(folderes != null){
				
			for (int i=0; i<folderes.length; i++) {
	
				String folderName = folderes[i].getName();
				
				File files[] = folderes[i].listFiles();
				if(files != null){
					for (int j=0; j<files.length; j++) { 
		
						String fileName = files[j].getName();
			            if(fileName.indexOf(FILE_SUFFIX)>0){
			            	
			            	File fileDes = new File(path + File.separator + folderName + File.separator + 
			            			fileName.substring(0,fileName.indexOf(FILE_SUFFIX)));
			            	
			            	if(!files[j].renameTo(fileDes)){
			            		AddMessage(strMessage, files[j].getAbsolutePath(), fileDes.getAbsolutePath());
							}
			            }
					}
				}
	
				if(folderName.indexOf(FILE_SUFFIX)>0){
	            	
	            	File folderDes = new File(path + File.separator + folderName.substring(0,folderName.indexOf(FILE_SUFFIX)));
	            	if(!folderes[i].renameTo(folderDes)){
	            		AddMessage(strMessage, folderes[i].getAbsolutePath(), folderDes.getAbsolutePath());
	            	}
	            }
	        }
		}
	}
	
	private void ModificarApartado(int unidadMenuId, HttpServletRequest request, List<TrcnMat> listModificarCont) throws Exception {
				
		int apartado = 0;
		String[] arrStrApartadosMenus = request.getParameterValues(unidadMenuId + "[]");
		
		if(arrStrApartadosMenus == null){
			return;
		}
		
		for(int i=0; i<arrStrApartadosMenus.length; i++){
			apartado++;
			String str = arrStrApartadosMenus[i];
			String strMenuId = str.substring(str.indexOf("_")+1);
			int apartadoMenuId = Integer.parseInt(strMenuId);
			
			List<Integer> idList = new ArrayList<Integer>();
			HacerChildLista(apartadoMenuId,idList,request);
			
			idList.add(apartadoMenuId);
			
			for(int menuid : idList){

				TrcnMat beanActual = TrcnMat.Buscar(menuid);
				if(beanActual.getApartado() != apartado){

					
					boolean exists = false;
					for(TrcnMat beanModificar : listModificarCont){
						if(beanModificar.getMenuItem() == menuid){
							beanModificar.setApartado(apartado);
							exists = true;
						}
					}
					if(!exists){
						
						TrcnMat bean = new TrcnMat();
						bean.setMenuItem(menuid);
						bean.setApartado(apartado);
						listModificarCont.add(bean);
					}

				}
				

			}		

		}
				
	}

	private List<Integer> HacerChildLista(int menuId, List<Integer> idList, HttpServletRequest request) {
		
		String[] arrStrChildMenu = request.getParameterValues(menuId + "[]");
		
		if(arrStrChildMenu == null){
			return null;
		}

		for(int i=0; i<arrStrChildMenu.length; i++){
			
			String str = arrStrChildMenu[i];
			String strMenuId = str.substring(str.indexOf("_")+1);
			int childMenuId = Integer.parseInt(strMenuId);

			HacerChildLista(childMenuId, idList, request);
			
			
			idList.add(childMenuId);
		}
		
		return idList;
	}

	private void ModificarChildUnidad(int unidad, TrcnMat beanActual, List<TrcnMat> listModificarCont) throws Exception {
		
		List<TrcnMat> childList = TrcnMat.BuscarChild(beanActual.getCveMat(), beanActual.getMenuItem());
		
		for(TrcnMat child : childList){
			
			ModificarChildUnidad(unidad, child, listModificarCont);
			
			if(child.getUnidad() != unidad){
				boolean exists = false;
				for(TrcnMat beanModificar : listModificarCont){
					if(beanModificar.getMenuItem() == child.getMenuItem()){
						beanModificar.setUnidad(unidad);
						exists = true;
					}
				}
				if(!exists){
					
					TrcnMat bean = new TrcnMat();
					bean.setMenuItem(child.getMenuItem());
					bean.setUnidad(unidad);
					listModificarCont.add(bean);
				}
				
			}
		}
		
	}

	/**
	 * Regresar a la pantalla con error
	 * @param response
	 * @param msg
	 * @throws IOException
	 */
	private void Error(HttpServletResponse response, String msg) throws IOException {
		String json = "{\"status\":\"error\",\"message\": \"" + msg + "\"}";
		response.setContentType("application/json;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();
		out.print(json);
		
	}


	private void SetForm(HttpServletRequest request) throws Exception {
		try {

			String strContentId = request.getParameter(KEY_REQUEST_PARAM_MENU_ID);
			int contentId = Integer.parseInt(strContentId);
			
			TrcnMat contenido = TrcnMat.Buscar(contentId);
			String cveMat = contenido.getCveMat();
			Materia bean = Materia.Buscar(cveMat);
			request.setAttribute(KEY_VALIABLE_MENU_ID, contentId);
			
			List<TrcnMat> trcnList = TrcnMat.BuscarChild(cveMat,contentId);
			if(trcnList.size()<=0){
				request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_NO_HAY_DATOS);
			}else{
				String s = TrcnMat.CreaMenu(bean, contentId, false, false, usuario, true);			
				request.setAttribute(KEY_VARIABLE_MENU_HTML, s);
			}
			
			
		} catch (Exception e) {
			throw e;
		}
		
	}

}
