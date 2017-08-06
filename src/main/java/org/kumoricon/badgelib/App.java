package org.kumoricon.badgelib;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // Just test harness setup
        final PDDocument document;
        FileOutputStream fos = null;
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Define what to draw
            PDFont font = PDType1Font.HELVETICA;
            PDRectangle boundingBox = new PDRectangle(100, 600, 200, 50);
            String[] text = {"Line 1", "Line 2"};

            BadgeBase.drawTextInBox(document, page, boundingBox, font, text, BadgeBase.ALIGNMENT.CENTER);

            PDFont font2 = PDType1Font.COURIER;
            PDRectangle boundingBox2 = new PDRectangle(400, 600, 200, 50);
            String[] text2 = {"Line 3", "Line 4"};

            BadgeBase.drawTextInBox(document, page, boundingBox2, font2, text2, BadgeBase.ALIGNMENT.LEFT);


            // Save the PDF
            document.save(os);
            document.close();
            fos = new FileOutputStream (new File("/tmp/test.pdf"));
            os.writeTo(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                os.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        System.out.println( "PDF saved to /tmp/test.pdf" );
    }
}
