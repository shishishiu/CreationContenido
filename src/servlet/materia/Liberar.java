package servlet.materia;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import beans.*;
import servlet.general.PdfNewPage;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;

/**
 * Servlet implementation class Validar
 */
@WebServlet("/Liberar")
public class Liberar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "Liberar.jsp";
	/** Nombre del form de materias **/
	private final String KEY_VARIABLE_MATERIA = "materia";
	/** Nombre del form de materias **/
	private final String KEY_VARIABLE_FECHA_SOLICITUD = "fechaSolicitud";
	/** Nombre del form del usuario **/
	private final String KEY_VARIABLE_USUARIO = "usuario";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_TIENE_AUTORIDAD = "tieneAutoridad";
	/** Nombre del form del cveMat **/
	private final String KEY_REQUEST_PARAM_CVE_MAT = "cveMat";
	/** Nombre del form **/
//	private final String KEY_FORM_NOTA = "nota";
	/** Nombre del form **/
	private final String KEY_FORM_NOTA2 = "nota2";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_MAT = "hiddenCveMat";
	/** Nombre del param **/
	private final String KEY_TIPO_VALIDAR = "1";
	/** Nombre del param **/
	private final String KEY_TIPO_IMPRIMIR = "2";
//	/** Nombre del param **/
//	private final String KEY_TIPO_PREVIEW1 = "3";
	/** Nombre del param **/
	private final String KEY_TIPO_PREVIEW2 = "4";

	/** Usuario **/
	public Usuario usuario;
	
    /**
     * @see HttpServletHttpServlet()
     */
    public Liberar() {
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
    		if(usuario.IsAutorizado()) {
				if(usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR ||
						usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR_GENERAL){
	    			Iniciar();
	    			SetForm(request);
		    		
		    		} else{
		
		    			request.setAttribute(KEY_VARIABLE_TIENE_AUTORIDAD, false);
		    			request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_AUTENTIFICA);
		    		}

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
	 * Iniciar
	 */
	private void Iniciar() {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;	
		
		try {
    	
			usuario = new Usuario(request, response);
    		if(usuario.IsAutorizado()){
				if(usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR || 
						usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR_GENERAL){
	 			
					String tipo = request.getParameter("hiddenTipo");

					if(tipo.equals(KEY_TIPO_VALIDAR)){
						LiberarMateria(request, response);
						return;
					}else if(tipo.equals(KEY_TIPO_IMPRIMIR)){
						Imprimir(request, response);
						return;
					}else if(tipo.equals(KEY_TIPO_PREVIEW2)){
						ImprimirPreview(2, request, response);
						return;
					}
					
//					switch(tipo){
//					case KEY_TIPO_VALIDAR:
//						LiberarMateria(request, response);
//						return;
//					case KEY_TIPO_IMPRIMIR:
//						Imprimir(request, response);
//						return;
//					case KEY_TIPO_PREVIEW2:
//						ImprimirPreview(2, request, response);
//						return;
//					default:
//						break;
//					}

					SetForm(request);
					
	    		} else{
	
	    			request.setAttribute(KEY_VARIABLE_TIENE_AUTORIDAD, false);
	    			request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_AUTENTIFICA);
	    		}

    		
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
	
	
	private void ImprimirPreview(int tipo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ByteArrayOutputStream baos = null;
		
		try{
		
			if(tipo == 1){
				baos = ImprimirLiberacion(request);
			}else{
				baos = ImprimirCambioInformacion(request);				
			}

	        response.setHeader("Expires", "0");
	        response.setHeader("Content-Type", "charset=UTF-8");
	        response.setHeader("Content-disposition", "inline; filename=\"oficio.pdf\"");
	        response.setContentType("application/pdf");
	        
	        OutputStream os = response.getOutputStream();
	        os.write(baos.toByteArray());
	        os.flush();
	        os.close();

	        
		}catch(Exception e){
			throw e;
			
		}finally{
			if(baos != null){
				baos.close();				
			}
			
		}
		
	}

	private void Imprimir(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ZipOutputStream zipOutStream = null;
		ByteArrayOutputStream baos = null;
		try{
		
	        response.setHeader("Expires", "0");
	        response.setHeader("Content-Type", "charset=UTF-8");
	        response.setHeader("Content-disposition", "attachment; filename=\"oficios.zip\"");
	        response.setContentType("application/zip");

			zipOutStream = new ZipOutputStream(response.getOutputStream());

	        baos = ImprimirLiberacion(request);
			try {
		        ZipEntry entry = new ZipEntry("Liberacion.pdf");
		        zipOutStream.putNextEntry(entry);
		        zipOutStream.write(baos.toByteArray());

		        zipOutStream.closeEntry();
		        
		        
			}catch(Exception e){
				  throw e;
			
			}finally{
				baos.close();
			}
	        
			baos = ImprimirCambioInformacion(request);
			try {
		        ZipEntry entry = new ZipEntry("CambioDeInformacion.pdf");
		        zipOutStream.putNextEntry(entry);
		        zipOutStream.write(baos.toByteArray());

		        zipOutStream.closeEntry();
		        
			}catch(Exception e){
				  
			}finally{
				if(baos != null){
					baos.close();
				}
			}

            zipOutStream.finish();

		}catch(Exception e){
			throw e;
	    } finally {
	    	if(baos != null){
	    		baos.close();
	    	}
	        zipOutStream.close();
	    }

	}
	private ByteArrayOutputStream ImprimirLiberacion(HttpServletRequest request) throws Exception {
		
		String cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
		Materia bean = Materia.Buscar(cveMat);

		Document document = new Document(PageSize.LETTER, 50,50,160,50);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try{
	       
            PdfWriter writer = PdfWriter.getInstance(document,baos );
	        PdfNewPage event = new PdfNewPage();
	        writer.setPageEvent(event);
	        
	        document.open();
	
	        //-----tabla de fecha 
	        PdfPTable table = new PdfPTable(2);
	        table.setTotalWidth(document.getPageSize().getWidth() - 300);
	        table.setLockedWidth(true);
	        table.setHorizontalAlignment(Rectangle.ALIGN_RIGHT);
	        PdfPCell cell = new PdfPCell(new Phrase("FECHA/HORA"));
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
	        table.addCell(cell);
	
			Date date = new Date();
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	        cell = new PdfPCell(new Phrase(sdf1.format(date) + " HRS."));
	        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
	        table.addCell(cell);
	        table.setSpacingAfter(30);
	
	        document.add(table);
	        
	        //-----tabla de fecha
	        //-----tabla de menu
	        Configuracion conf = new Configuracion();
	        String pathTemplate = conf.getAbsolutePath() + File.separator + conf.getPlantillas() + File.separator + "templateOficio1.txt";
	        
			File fileTmp = new File(pathTemplate);
	        FileReader filereader = new FileReader(fileTmp);
	        BufferedReader br = new BufferedReader(filereader);
	        String message = "";
	        String str;
	        while((str = br.readLine()) != null){
	          message += str + "\r\n";
	        }
	        br.close();
	        
	        String nombre = usuario.getNomCompletoUsu();
	        String materia = bean.getNomNivel() + " " + bean.getNomMat();
	        
	        String menu = bean.getCveMat() + "  " + bean.getNomMat() 
	        + "\r\n" + bean.getNomNivel() + " Modulo " + bean.getModulo()
	        + "\r\n" + TrcnMat.CreaMenuPDF(bean, 0, 0);
	
	        Paragraph prg = new Paragraph(MessageFormat.format(message,nombre,materia,menu,materia));
	        prg.setIndentationLeft(20);
	        prg.setAlignment(Rectangle.ALIGN_LEFT);
	
	        document.add(prg);
	        //-----tabla de menu        
	                
	        //-----tabla de firma
	        float[] columnWidths = {20, 1, 20};
	        PdfPTable tableFirma = new PdfPTable(columnWidths);
	        tableFirma.setTotalWidth(document.getPageSize().getWidth() - 50);
	        tableFirma.setLockedWidth(true);
	        tableFirma.setSpacingBefore(30);
	        
	        PdfPCell cellOne = new PdfPCell(new Phrase("Nombre y Firma"));
	        PdfPCell cellTwo = new PdfPCell(new Phrase("Nombre y Firma"));
	        PdfPCell cellBlank = new PdfPCell(new Phrase(""));
	        
	        cellOne.setBorder(Rectangle.NO_BORDER);
	        cellOne.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
	        cellOne.setFixedHeight(80);
	        cellTwo.setBorder(Rectangle.NO_BORDER);
	        cellTwo.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
	        cellBlank.setBorder(Rectangle.NO_BORDER);
	        
	        tableFirma.addCell(cellOne);
	        tableFirma.addCell(cellBlank);
	        tableFirma.addCell(cellTwo);
	
	        cellOne = new PdfPCell();
	        cellTwo = new PdfPCell();
	
	        cellOne.setBorder(Rectangle.NO_BORDER);
	        cellOne.setBorder(Rectangle.BOTTOM);
	        cellTwo.setBorder(Rectangle.NO_BORDER);
	        cellTwo.setBorder(Rectangle.BOTTOM);
	
	        tableFirma.addCell(cellOne);
	        tableFirma.addCell(cellBlank);
	        tableFirma.addCell(cellTwo);
	        
	        PdfPCell cellOne2 = new PdfPCell(new Phrase(usuario.getNomCompletoUsu() + "\r\n"
	        		+ "JEFA DEL DEPARTAMENTO DE DESARROLLO ACADÉMICO"));
	        cellOne2.setBorder(Rectangle.NO_BORDER);
	        cellOne2.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
	        tableFirma.addCell(cellOne2);
	
	        tableFirma.addCell(cellBlank);
	
	        PdfPCell cellTwo2 = new PdfPCell(new Phrase("ING.JORGE HERNÁNDEZ VALDÍN" + "\r\n"
	        		+ "JEFE DEL DEPARTAMENTO DE DESARROLLO Y PRODUCCIÓN DE CONTENIDOS VIRTUALES"));
	        cellTwo2.setBorder(Rectangle.NO_BORDER);
	        cellTwo2.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
	        tableFirma.addCell(cellTwo2);
	
	        
	        document.add(tableFirma);
	        //-----tabla de firma
	

		}catch(Exception e){
			throw e;
	    } finally {
	    	document.close();
	    }

	        return baos;
	}

	private ByteArrayOutputStream ImprimirCambioInformacion(HttpServletRequest request) throws Exception {

		String cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
		Materia bean = Materia.Buscar(cveMat);

      
		Document document = new Document(PageSize.LETTER, 50,50,160,50);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document,baos );

        PdfNewPage event = new PdfNewPage();
        writer.setPageEvent(event);

        document.open();	

        //-----tabla de fecha 
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(document.getPageSize().getWidth() - 300);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Rectangle.ALIGN_RIGHT);
        PdfPCell cell = new PdfPCell(new Phrase("FECHA/HORA"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FOLIO"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);        
        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.addCell(cell);

		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        cell = new PdfPCell(new Phrase(sdf1.format(date) + " HRS."));
        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(""));
        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.addCell(cell);

        document.add(table);
        //-----tabla de fecha

        //-----tabla de datos del area administrativa solicitante
        table = new PdfPTable(2);
        table.setTotalWidth(document.getPageSize().getWidth() - 80);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.setSpacingBefore(30);
        cell = new PdfPCell(new Phrase("DATOS DEL AREA ADMINISTRATIVA SOLICITANTE"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        cell.setColspan(2);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("SOLICITA"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("AUTORIZA"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nombre: " + usuario.getNomCompletoUsu()));
        cell.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Nombre: " + usuario.getNomCompletoUsu()));
        cell.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Cargo: "));
        cell.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Cargo: "));
        cell.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Área: "));
        cell.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Área: "));
        cell.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
        table.addCell(cell);
        
        document.add(table);
        //-----tabla de datos del area administrativa solicitante
        
        //-----tabla de tipo de consulta/cambio
        table = new PdfPTable(3);
        table.setTotalWidth(document.getPageSize().getWidth() - 80);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.setSpacingBefore(20);
        cell = new PdfPCell(new Phrase("TIPO DE CONSULTA/CAMBIO"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        cell.setColspan(3);
        table.addCell(cell);

        //innertable1
        PdfPTable innertable = CreateInnerTable();        
        innertable.addCell(" ");
        PdfPCell innercell = new PdfPCell(new Phrase("Configuración Hw/Sw"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);

        innercell = new PdfPCell(new Phrase("X"));
        innercell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        innertable.addCell(innercell);
        
        innercell = new PdfPCell(new Phrase("Applicaciones"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);
        
        innercell = new PdfPCell(new Phrase(" "));
        innercell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        innertable.addCell(innercell);

        innercell = new PdfPCell(new Phrase("Sitio WEB"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
        cell.addElement(innertable);
        table.addCell(cell);

        //innertable2
        innertable = CreateInnerTable();
        
        innertable.addCell(" ");
        innercell = new PdfPCell(new Phrase("Base de Datos"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);

        innertable.addCell(" ");
        innercell = new PdfPCell(new Phrase("Hardware"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);
        
        innercell = new PdfPCell(new Phrase("Especifique:"));
        innercell.setColspan(2);
        innercell.setBorder(Rectangle.NO_BORDER);
        innercell.setBorder(Rectangle.BOTTOM);
        innertable.addCell(innercell);

        cell = new PdfPCell();
        cell.addElement(innertable);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorder(Rectangle.BOTTOM);
        table.addCell(cell);
        
        //innertable3
        innertable = CreateInnerTable();
        
        innertable.addCell(" ");
        innercell = new PdfPCell(new Phrase("Telecomunicaciones"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);

        innertable.addCell(" ");
        innercell = new PdfPCell(new Phrase("Redes"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);
        
        cell = new PdfPCell();
        cell.addElement(innertable);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
        table.addCell(cell);

        document.add(table);

         //-----tabla de tipo de consulta/cambio

        
        //-----tabla de periodicidad
        table = new PdfPTable(3);
        table.setTotalWidth(document.getPageSize().getWidth() - 80);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.setSpacingBefore(20);
        cell = new PdfPCell(new Phrase("PERIODICIDAD"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        cell.setColspan(3);
        table.addCell(cell);

        //innertable1
        innertable = CreateInnerTable();        
        innertable.addCell(" ");
        innercell = new PdfPCell(new Phrase("Días"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);

        innertable.addCell(" ");
        innercell = new PdfPCell(new Phrase("Años"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);
        
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
        cell.addElement(innertable);
        table.addCell(cell);

        //innertable2
        innertable = CreateInnerTable();        
        innertable.addCell(" ");
        innercell = new PdfPCell(new Phrase("Semanas"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);

        innertable.addCell(" ");
        innercell = new PdfPCell(new Phrase("Indefinido"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorder(Rectangle.BOTTOM);
        cell.addElement(innertable);
        table.addCell(cell);

        //innertable3
        innertable = CreateInnerTable();        
        innertable.addCell(" ");
        innercell = new PdfPCell(new Phrase("Meses"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);

        innercell = new PdfPCell(new Phrase("X"));
        innercell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        innertable.addCell(innercell);

        innercell = new PdfPCell(new Phrase("N/A"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
        cell.addElement(innertable);
        table.addCell(cell);

        document.add(table);

         //-----tabla de periodicidad


        //-----tabla de origen de la solicitud
        table = new PdfPTable(3);
        table.setTotalWidth(document.getPageSize().getWidth() - 80);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.setSpacingBefore(20);
        cell = new PdfPCell(new Phrase("ORIGEN DE LA SOLICITUD DEK CONSULTA/CAMBIO"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        cell.setColspan(3);
        table.addCell(cell);

        //innertable1
        innertable = CreateInnerTable();        
        innercell = new PdfPCell(new Phrase("X"));
        innercell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        innertable.addCell(innercell);
        
        innercell = new PdfPCell(new Phrase("Mejora"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);

        innertable.addCell(" ");
        innercell = new PdfPCell(new Phrase("Otro"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);
        
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
        cell.addElement(innertable);
        table.addCell(cell);

        //innertable2
        innertable = CreateInnerTable();        
        innertable.addCell(" ");
        innercell = new PdfPCell(new Phrase("Actualización"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);

        innertable.addCell(" ");
        innercell = new PdfPCell(new Phrase("Especifique:"));
        innercell.setColspan(2);
        innercell.setBorder(Rectangle.NO_BORDER);
        innercell.setBorder(Rectangle.BOTTOM);
        innertable.addCell(innercell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorder(Rectangle.BOTTOM);
        cell.addElement(innertable);
        table.addCell(cell);

        //innertable3
        innertable = CreateInnerTable();        
        innertable.addCell(" ");
        innercell = new PdfPCell(new Phrase("Falla"));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
        cell.addElement(innertable);
        table.addCell(cell);

        document.add(table);
         //-----tabla de origen de la solicitud

        //-----tabla de menu
        
        table = new PdfPTable(1);
        table.setTotalWidth(document.getPageSize().getWidth() - 80);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.setSpacingBefore(20);
        cell = new PdfPCell(new Phrase("LOS CONTENIDOS"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(bean.getCveMat() + "  " + bean.getNomMat() 
        + "\r\n" + bean.getNomNivel() + " Modulo " + bean.getModulo()));
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        table.addCell(cell);

        TrcnMat.CreaMenuPDF(bean, 0, 0, table);
    
		PdfPRow row = table.getRow(table.size()-1);
		PdfPCell[] cellarray = row.getCells();
		cellarray[0].setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
		
//		row = table.getRow(table.size()-1);
//		cellarray = row.getCells();
//		cellarray[0].setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP);
//		
		ArrayList<PdfPRow> rowArray = table.getRows();
//		row = table.getRow(rowArray.size()-1);
//		cellarray = row.getCells();
//		cellarray[0].setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);        
		    
		document.add(table);
        
        //-----tabla de menu

        //-----tabla de descripcion de consulta
        
        table = new PdfPTable(1);
        table.setTotalWidth(document.getPageSize().getWidth() - 80);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.setSpacingBefore(20);
        cell = new PdfPCell(new Phrase("DESCRIPCIÓN DE CONSULTA/CAMBIO"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        table.addCell(cell);

        String nota2 = request.getParameter(KEY_FORM_NOTA2);
        
        while(nota2.indexOf("\r\n")>=0){

        	int i = nota2.indexOf("\r\n");
        	String str = nota2.substring(0,i);
        	nota2 = nota2.substring(i+1);
        	
        	cell = new PdfPCell(new Phrase(str));
        	cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
    		table.addCell(cell);

        }
    	cell = new PdfPCell(new Phrase(nota2));
    	cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		table.addCell(cell);

        
		rowArray = table.getRows();
		row = table.getRow(rowArray.size()-1);
		cellarray = row.getCells();
		cellarray[0].setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);        
        
		document.add(table);
        //-----tabla de descripcion de consulta

        
        //-----tabla de firma
        float[] columnWidths = {20, 1, 20};
        PdfPTable tableFirma = new PdfPTable(columnWidths);
        tableFirma.setTotalWidth(document.getPageSize().getWidth() - 50);
        tableFirma.setLockedWidth(true);
        tableFirma.setSpacingBefore(30);
        
        PdfPCell cellOne = new PdfPCell(new Phrase("Nombre y Firma"));
        PdfPCell cellTwo = new PdfPCell(new Phrase("Nombre y Firma"));
        PdfPCell cellBlank = new PdfPCell(new Phrase(""));
        
        cellOne.setBorder(Rectangle.NO_BORDER);
        cellOne.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        cellOne.setFixedHeight(80);
        cellTwo.setBorder(Rectangle.NO_BORDER);
        cellTwo.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        cellBlank.setBorder(Rectangle.NO_BORDER);
        
        tableFirma.addCell(cellOne);
        tableFirma.addCell(cellBlank);
        tableFirma.addCell(cellTwo);

        cellOne = new PdfPCell();
        cellTwo = new PdfPCell();

        cellOne.setBorder(Rectangle.NO_BORDER);
        cellOne.setBorder(Rectangle.BOTTOM);
        cellTwo.setBorder(Rectangle.NO_BORDER);
        cellTwo.setBorder(Rectangle.BOTTOM);

        tableFirma.addCell(cellOne);
        tableFirma.addCell(cellBlank);
        tableFirma.addCell(cellTwo);
        
        PdfPCell cellOne2 = new PdfPCell(new Phrase(usuario.getNomCompletoUsu() + "\r\n"
        		+ "JEFA DEL DEPARTAMENTO DE DESARROLLO ACADÉMICO"));
        cellOne2.setBorder(Rectangle.NO_BORDER);
        cellOne2.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        tableFirma.addCell(cellOne2);

        tableFirma.addCell(cellBlank);

        PdfPCell cellTwo2 = new PdfPCell(new Phrase("ING.JORGE HERNÁNDEZ VALDÍN" + "\r\n"
        		+ "JEFE DEL DEPARTAMENTO DE DESARROLLO Y PRODUCCIÓN DE CONTENIDOS VIRTUALES"));
        cellTwo2.setBorder(Rectangle.NO_BORDER);
        cellTwo2.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        tableFirma.addCell(cellTwo2);

        
        document.add(tableFirma);
        //-----tabla de firma
 
        document.close();		
        
        return baos;
        
	}

	private PdfPTable CreateInnerTable() throws DocumentException {
        PdfPTable innertable = new PdfPTable(2);
        innertable.setTotalWidth(150);
        innertable.setWidths(new float[]{1,9});
        innertable.setLockedWidth(true);
		return innertable;
	}

	private void LiberarMateria(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
		
		if(Validacion(request, cveMat, response)){

			InsertarSolicitud(request, cveMat, response);

		}
	}

	private boolean Validacion(HttpServletRequest request, String cveMat, HttpServletResponse response) throws Exception {
		List<MateriaSolicitud> list = null;
		
		try {
			
			MateriaSolicitud bean = new MateriaSolicitud();
			bean.setCveMat(cveMat);
			list = bean.Buscar();
			if(list.size()>0 && 
					list.get(0).getEstadoSolicitud() != Common.ESTADO_DE_SOLICITUD_VALIDADO){
				
				Common.MsgJson("error", Common.MENSAJE_SOLICITAR_HA_LIBERADO, response);
				return false;
			
			}
			
		} catch (Exception e) {
			throw e;
		}
		return true;
	}

	private void InsertarSolicitud(HttpServletRequest request, String cveMat, HttpServletResponse response) throws Exception {

		Connection con = MySqlConnector.getConnection();
		try {
			con.setAutoCommit(false);

			MateriaSolicitud bean = new MateriaSolicitud();
			bean.setCveMat(cveMat);
//			bean.setNota(request.getParameter(KEY_FORM_NOTA));
			bean.setEstadoSolicitud(Common.ESTADO_DE_SOLICITUD_LIBERADO);
			bean.setResultado(Common.ESTADO_DE_SOLICITUD_LIBERADO);
			bean.setUsuarioSolicitud(usuario.getCveUsu());
			
			bean.Insertar(con);
			
			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_LIBERAR,cveMat));
	
			
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
	 * Set form de la pantalla
	 * @param request
	 * @throws Exception 
	 */
	private void SetForm(HttpServletRequest request) throws Exception {
		try {
	
   			request.setAttribute(KEY_VARIABLE_USUARIO, usuario);

			String cveMat = request.getParameter(KEY_REQUEST_PARAM_CVE_MAT);
			if(cveMat == null || cveMat.equals("")){
				cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
			}
			
			Materia bean = Materia.Buscar(cveMat);
			
			request.setAttribute(KEY_VARIABLE_MATERIA, bean);
			
			Date date = new Date();
	        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

	        request.setAttribute(KEY_VARIABLE_FECHA_SOLICITUD, sdf1.format(date));
	
   			request.setAttribute(KEY_VARIABLE_TIENE_AUTORIDAD, true);

			
		} catch (Exception e) {
			throw e;
		}
		
	}
	

}
