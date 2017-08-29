package util.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfTemplate;

import util.common.Common;

public class PdfLiberarNewPage extends PdfPageEventHelper{

	private Font fontNormal, fontSmall;
	private PdfTemplate t;
	private Image total;
	private final String LOGO_FILE_NAME_1 = "EstadoDeMexico.jpg";
	private final String LOGO_FILE_NAME_2 = "EstadoDeMexico2.jpg";
    
    public void onOpenDocument(PdfWriter writer, Document document) {
        t = writer.getDirectContent().createTemplate(30, 16);
        try {
        	fontNormal  = FontFactory.getFont("Century Gothic", 10);
        	fontSmall  = FontFactory.getFont("Century Gothic", 8);
            total = Image.getInstance(t);
        } catch (DocumentException de) {
			Common.Error(de);
           throw new ExceptionConverter(de);
        } catch (Exception ioe) {
			Common.Error(ioe);
           throw new ExceptionConverter(ioe);
        }
    }

    
	public void onEndPage(PdfWriter writer, Document document) {

		Configuracion config = new Configuracion();
      try {
			
			String absolutePathImages = config.getAbsolutePath() 
					+ File.separator + "images" + File.separator + "pdf"; 
			
			Image logoLeft;
			Image logoRight;
			
			try{
				logoLeft = Image.getInstance(absolutePathImages + File.separator + LOGO_FILE_NAME_1);
				logoRight = Image.getInstance(absolutePathImages + File.separator + LOGO_FILE_NAME_2);

			} catch(FileNotFoundException e){
			
				absolutePathImages = config.getAbsolutePath() 
						+ File.separator + "WebContent" + File.separator + "images" + File.separator + "pdf"; 				
				logoLeft = Image.getInstance(absolutePathImages + File.separator + LOGO_FILE_NAME_1);
				logoRight = Image.getInstance(absolutePathImages + File.separator + LOGO_FILE_NAME_2);
			}

			PdfPTable table = new PdfPTable(5);
            table.setWidths(new int[]{10,45,10,15,15});
            table.setTotalWidth(document.getPageSize().getWidth() - 50);
           
            PdfPCell cell = new PdfPCell(logoLeft, true);
            cell.setPadding(2);
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(30);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase("FORMATO" + Common.NEW_LINE + "DOCUMENTO DE ACEPTACIÓN", fontNormal));
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase("Código", fontSmall));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("F003", fontSmall));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(logoRight, true);
            cell.setPadding(2);
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(30);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Revisión", fontSmall));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("0", fontSmall));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Fecha", fontSmall));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
    		Date date = new Date();
    		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
            cell = new PdfPCell(new Phrase(sdf1.format(date), fontSmall));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Página", fontSmall));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            
            PdfPTable innertable = new PdfPTable(2);        
            innertable.setWidths(new int[]{1,1});
            innertable.setTotalWidth(2);
            PdfPCell innercell = new PdfPCell(new Phrase(String.format("%d de", writer.getPageNumber()), fontSmall));
            innercell.setBorder(Rectangle.NO_BORDER);
            innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            innercell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            innercell.setPadding(0);
            innertable.addCell(innercell);

            innercell = new PdfPCell(total, true);
            innercell.setBorder(Rectangle.NO_BORDER);
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innercell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            innercell.setPadding(0);
            innertable.addCell(innercell);
            cell = new PdfPCell();
            cell.addElement(innertable);
            cell.setPadding(0);
           
            table.addCell(cell);
          
            
            table.writeSelectedRows(0, -1, 25, 750, writer.getDirectContent());
            
        } catch (Exception e) {
			Common.Error(e);
            throw new ExceptionConverter(e);
		}

    }


	public void onCloseDocument(PdfWriter writer, Document document){
        ColumnText.showTextAligned(t, Element.ALIGN_CENTER,
        		new Phrase(String.valueOf(writer.getPageNumber()),fontSmall), 5, 4, 0);
    
	}
}
