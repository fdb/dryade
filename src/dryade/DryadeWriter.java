package dryade;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;

public class DryadeWriter {
    File file;
    Dryade dryade;

    float width, height;

    public static void main(String[] args) {

        try {
            DryadeWriter writer = new DryadeWriter(new File("out/dryade.pdf"));
            writer.write();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
    }

    public DryadeWriter(File file) {
        File dir = file.getParentFile();
        dir.mkdirs();
        this.file = file;
        this.dryade = new Dryade();
    }

    public void write() throws Exception {
        Rectangle format = PageSize.A4.rotate();
        width = format.getWidth();
        height = format.getHeight();
        Document document = new Document(format, 0F, 0F, 0F, 0F);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        PdfTemplate tp = cb.createTemplate(width, height);
        Graphics2D g2 = tp.createGraphics(width, height);
        //tp.moveTo(0,0);
        g2.setStroke(new BasicStroke(0.1f));
//    tp.setWidth(width);
//    tp.setHeight(height);
        dryade.setup();
        dryade.draw(g2);
        System.out.println(dryade.lines + " lines written.");
        g2.dispose();
        cb.addTemplate(tp, 0, 0);
        document.close();
    }

}