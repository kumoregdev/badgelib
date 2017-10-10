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
        /* Define the page size */
        float widthInInches = 6.0f;
        float heightInInches = 4.0f;
        float dotsPerInch = 72;
        double pi = Math.PI;
        PDRectangle pageSize = new PDRectangle(widthInInches * dotsPerInch, heightInInches * dotsPerInch);

        /* Create a document object */
        final PDDocument document;
        document = new PDDocument();

        FileOutputStream fos = null;
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            PDPage page = new PDPage(pageSize);
            document.addPage(page);

            /* Draw text in the document */
            // NOTE 1: bounding box units are in printer DPI which is 1/72" by default
            // NOTE 2: coordinates originate from the lower-left

            PDFont font1 = PDType1Font.HELVETICA;
            PDRectangle boundingBox1 = new PDRectangle(140, 30, 200, 220);
            String[] text1 = {"The first line", "The second line which is so very long indeed", "Third line", "Fourth"};
            BadgeBase.drawText(document, page, boundingBox1, font1, text1, BadgeBase.ALIGNMENT.CENTER, BadgeBase.ROTATION.NONE, 14.0f, 12.0f, true, true);

            PDFont font2 = PDType1Font.COURIER;
            PDRectangle boundingBox2 = new PDRectangle(360, 40, 50, 170);
            String[] text2 = {"Right Rotation", "another right rotated line"};
            BadgeBase.drawText(document, page, boundingBox2, font2, text2, BadgeBase.ALIGNMENT.LEFT, BadgeBase.ROTATION.RIGHT, 14.0f, 12.0f, true, true);

            PDFont font3 = PDType1Font.HELVETICA;
            PDRectangle boundingBox3 = new PDRectangle(65, 55, 50, 150);
            String[] text3 = {"Left Rotation", "another left rotated line"};
            BadgeBase.drawText(document, page, boundingBox3, font3, text3, BadgeBase.ALIGNMENT.RIGHT, BadgeBase.ROTATION.LEFT, 14.0f, 12.0f, true, true);

            PDFont font4 = PDType1Font.COURIER;
            PDRectangle boundingBox4 = new PDRectangle(0, 40, 50, 180);
            String[] text4 = {"A","D","U","L","T"};
            BadgeBase.drawText(document, page, boundingBox4, font4, text4, BadgeBase.ALIGNMENT.CENTER, BadgeBase.ROTATION.NONE, 10.0f, 12.0f, true, true);

            // Save the PDF
            document.save(os);
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

            try {
                document.close();
            }
            catch (IOException e) {

            }
        }

        System.out.println( "PDF saved to /tmp/test.pdf" );
        //try {
        //    BadgeBase.doIt("This is a test message!", "/tmp/test.pdf");
        //} catch (Exception e){}
    }
}
