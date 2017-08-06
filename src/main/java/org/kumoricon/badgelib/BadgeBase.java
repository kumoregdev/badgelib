package org.kumoricon.badgelib;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.io.IOException;
import java.util.Enumeration;

public class BadgeBase {

    public enum ALIGNMENT {LEFT, CENTER, RIGHT}

    /**
     * Draws the given multiline text scaled to fill given bounding box and alignment.
     * Ex:
     * alignment = left:
     *     +------------------------+
     *     | Line 1                 |
     *     | Line 2                 |
     *     +------------------------+
     *
     * alignment = center:
     *     +------------------------+
     *     |         Line 1         |
     *     |         Line 2         |
     *     +------------------------+
     * @param document Open PDDcument file to draw in to
     * @param page Open PDPage (current page to draw on)
     * @param box Bounding box
     * @param font Font to use
     * @param text Array of text lines to draw
     * @param alignment LEFT / CENTER / Right
     * @throws IOException
     */
    public static void drawTextInBox(PDDocument document, PDPage page, PDRectangle box, PDFont font, String[] text, ALIGNMENT alignment) throws IOException {

        // Create new stream to draw to:
        PDPageContentStream stream = new PDPageContentStream(document, page, true, true, false);

        // Draw bounding box, for testing only:
        stream.setNonStrokingColor(Color.red);
        stream.fillRect(box.getLowerLeftX(), box.getLowerLeftY(), box.getWidth(), box.getHeight());

        stream.setLineWidth(0.5f);
        stream.beginText();
        stream.setStrokingColor(Color.white);
        stream.setNonStrokingColor(Color.black);
        stream.setRenderingMode(RenderingMode.FILL_STROKE);

        // Todo: Scale the text so that it fills (but does not overlap) the bounding box
        // Todo: Align text properly per alignment
        // Todo: Center text vertically in the bounding box
        int fontSize = 24;
        stream.setFont(font, fontSize);
        for (int i = 0; i < text.length; i++) {
            Matrix offset = Matrix.getTranslateInstance(box.getLowerLeftX(),
                    box.getUpperRightY()-(fontSize*(i+1)));
            stream.setTextMatrix(offset);
            stream.showText(text[i]);
        }

        stream.close();
    }

    public static void drawTextCenteredInBoxRotatedLeft(PDPage page, PDRectangle boundingBox, PDFont font, String[] text, ALIGNMENT alignment) throws IOException {

        // Todo: Draw text rotated 90 degrees to the left, filling the given bounding box as above

        // This will probably be useful:
        //        Matrix offset = Matrix.getRotateInstance(-90 * Math.PI * 0.25, x, y);

    }

    public static void drawTextInBoxRotatedRight(PDPage page, PDRectangle boundingBox, PDFont font, String[] text, ALIGNMENT alignment) throws IOException {
        // Todo: Draw text rotated 90 degrees to the right, filling the given bounding box as above

        // This will probably be useful:
//        Matrix offset = Matrix.getRotateInstance(90 * Math.PI * 0.25, x, y);

    }

}
