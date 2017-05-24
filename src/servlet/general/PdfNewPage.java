package servlet.general;

import java.io.File;
import java.io.FileNotFoundException;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
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
import util.conf.Configuracion;

public class PdfNewPage extends PdfPageEventHelper{

    PdfTemplate total;
    
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 16);
    }

    
	public void onEndPage(PdfWriter writer, Document document) {
		
		Configuracion config = new Configuracion();
       try {
        	File f = new File("images/pdf/");
        	String imgPath = f.getAbsolutePath();
			File fileGov = new File(imgPath + File.separator + "EstadoDeMexico.png");
			File fileUniv = new File(imgPath + File.separator + "UDEM.jpg");
			
			String absolutePathImages = config.getAbsolutePath() 
					+ File.separator + "images" + File.separator + "pdf"; 
			
			Image logoGov;
			Image logoUniv;
			
			try{
				logoGov = Image.getInstance(absolutePathImages + File.separator + "EstadoDeMexico.png");
				logoUniv = Image.getInstance(absolutePathImages + File.separator + "UDEM.jpg");

			} catch(FileNotFoundException e){
			
				absolutePathImages = config.getAbsolutePath() 
						+ File.separator + "WebContent" + File.separator + "images" + File.separator + "pdf"; 				
				logoGov = Image.getInstance(absolutePathImages + File.separator + "EstadoDeMexico.png");
				logoUniv = Image.getInstance(absolutePathImages + File.separator + "UDEM.jpg");
			}


			logoGov.setAbsolutePosition(document.left()-30, document.top()+50);
	        document.add(logoGov);
			logoUniv.setAbsolutePosition(document.right()-150, document.top()+50);
	        document.add(logoUniv);
		       	      		
            PdfPTable table = new PdfPTable(3);
            table.setWidths(new int[]{24, 24, 2});
            table.setTotalWidth(527);
            table.setLockedWidth(true);
            table.getDefaultCell().setFixedHeight(20);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.addCell("");
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(String.format("Page %d de", writer.getPageNumber()));
            PdfPCell cell = new PdfPCell(Image.getInstance(total));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            table.writeSelectedRows(0, -1, 34, document.bottom(), writer.getDirectContent());
        }
        catch(Exception de) {
			
        	Common.Error(de);
            throw new ExceptionConverter(de);
        
        }
	        
    }
	
	public void onCloseDocument(PdfWriter writer, Document document){
        ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
        		new Phrase(String.valueOf(writer.getPageNumber())),
                2, 2, 0);
    
	}
}
