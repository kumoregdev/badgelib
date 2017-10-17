package org.kumoricon.badgelib;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Arrays;

public class BadgeBase {

    public enum ALIGNMENT {LEFT, CENTER, RIGHT}
    public enum ROTATION {NONE, LEFT, RIGHT}

    /*
     * Draws multiline text scaled to fill the given bounding box and alignment
     * with an optional left or right rotation
     *
     * alignment = left:
     * +------------------------+
     * | Line 1                 |
     * +------------------------+
     *
     * alignment = center:
     * +------------------------+
     * |         Line 1         |
     * +------------------------+
     *
     * alignment = right:
     * +------------------------+
     * |                  Line 1|
     * +------------------------+
     *
     * @param document          Open PDDcument file to draw in to
     * @param page              Open PDPage (the current page to draw on)
     * @param box               The bounding box
     * @param font              The font to use
     * @param lines             The String array of text lines to draw
     * @param alignment         LEFT / CENTER / RIGHT
     * @param rotation          NONE / LEFT / RIGHT
     * @param fontLeading       The total height of a line of text, inclusive of whitespace, in point size
     * @param fontSize          Set the font size, the value of which doesn't matter if relying on autoScale
     * @param autoScale         Whether or not to automatically scale text to fit the bounding box
     * @param drawBoundingBox   Whether or not to make the bounding box visible
     * @throws IOException
     *
     */
    public static void drawText(PDDocument document, PDPage page, PDRectangle box, PDFont font, String[] lines, ALIGNMENT alignment, ROTATION rotation, float fontLeading, float fontSize, boolean autoScale, boolean drawBoundingBox) throws IOException {


        /* Prepare the content stream */
        PDPageContentStream stream = new PDPageContentStream(document, page, true, true);


        /* Define the working boundary for the text being inserted */
        float boundaryX = box.getLowerLeftX();
        float boundaryY = box.getUpperRightY();
        float boundaryWidth = box.getUpperRightX() - box.getLowerLeftX();
        float boundaryHeight = box.getUpperRightY() - box.getLowerLeftY();
        System.out.println("Boundary X: " + boundaryX);
        System.out.println("Boundary Y: " + boundaryY);
        System.out.println("Boundary Width: " + boundaryWidth);
        System.out.println("Boundary Height: " + boundaryHeight);


        /* Define font attributes */
        if (fontSize <= 0)
            fontSize = 12.0f;

        stream.setFont(font, fontSize);

        if (fontLeading <= 0)
            // Get the default leading for the font
            fontLeading = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
        stream.setLeading(fontLeading);

        /* Get the size of the page in printer DPI */
        PDRectangle pageSize = page.getMediaBox();
        float pageWidth = pageSize.getWidth();
        float pageHeight = pageSize.getHeight();
        System.out.println("Page Width: " + pageWidth);
        System.out.println("Page Height: " + pageHeight);


        /* Draw the outline of the bounding box */
        if (drawBoundingBox == true) {
            stream.setNonStrokingColor(Color.BLUE);
            stream.addRect(box.getLowerLeftX(), box.getLowerLeftY(), box.getWidth(), box.getHeight());
            stream.fill();
            stream.setNonStrokingColor(Color.WHITE);
            stream.addRect(box.getLowerLeftX() + 1, box.getLowerLeftY() + 1, box.getWidth() - 2, box.getHeight() - 2);
            stream.fill();
        }


        /* Enter into text drawing mode */
        stream.beginText();
        stream.setNonStrokingColor(Color.BLACK);


        /* Create a TextMatrix object */
        // The text matrix allows drawing text normally without consideration for where it is located,
        // how big it is, or what direction it is facing
        Matrix matrix = new Matrix();


        /* Determine the value to scale the text to fit the boundary box, taking into account optional rotation
           Once the value is determined, apply translation, rotation, and scaling to the text */

        // Get the widths of each line
        float[] lineWidths = new float[lines.length];
        for (int i = 0; i < (lines.length); i++) {
            lineWidths[i] = font.getStringWidth(lines[i])/1000f*fontSize;
            System.out.println("'" + lines[i] + "' Width: " + lineWidths[i]);
        }

        // Get the width of the longest line
        Arrays.sort(lineWidths);
        float maxlineWidth = lineWidths[(lines.length-1)];
        System.out.println("Max Line Width: " + maxlineWidth);

        float whitespaceGap = (fontLeading - fontSize);

        // Calculate autoScaleFactor based on the type of rotation
        float autoScaleFactor = 1.0f;
        if (rotation == ROTATION.RIGHT || rotation == ROTATION.LEFT) {
            //The boundaryWidth and boundaryHeight variables are swapped for the rotate right and left cases

            // Calculate the scale factor to fit the longest line in the bounding box
            float fitHeightScaleFactor = boundaryHeight / maxlineWidth;
            System.out.println("Fit Height Scale Factor: " + fitHeightScaleFactor);

            // Determine the value to scale the combined height of text to fit the boundary box
            float fitWidthScaleFactor = boundaryWidth / (lines.length*fontLeading);
            System.out.println("Fit Width Scale Factor: " + fitWidthScaleFactor);

            // Go with the smaller of the calculated width and height scale values
            if (fitWidthScaleFactor < fitHeightScaleFactor)
                autoScaleFactor = fitWidthScaleFactor;
            else
                autoScaleFactor = fitHeightScaleFactor;
            System.out.println("Auto Scale Factor: " + autoScaleFactor);

            if (rotation == ROTATION.RIGHT) {
                float xPositionRightSideOfBoundary = boundaryX + boundaryWidth + whitespaceGap * lines.length;
                float xOffset = (boundaryWidth-(lines.length)*fontSize)/2;
                matrix.translate(xPositionRightSideOfBoundary - xOffset, boundaryY);
                matrix.rotate(-Math.PI / 2);
            }
            else {
                float xPositionLeftSideOfBoundary = boundaryX - whitespaceGap * lines.length;
                float xOffset = (boundaryWidth-(lines.length)*fontSize)/2;
                matrix.translate(xPositionLeftSideOfBoundary + xOffset, boundaryY - boundaryHeight);
                matrix.rotate(Math.PI/2);
            }
        }
        else {
            // Calculate the scale factor to fit the longest line in the bounding box
            float fitWidthScaleFactor = boundaryWidth / maxlineWidth;
            System.out.println("Fit Width Scale Factor: " + fitWidthScaleFactor);

            // Determine the value to scale the combined height of text to fit the boundary box
            float fitHeightScaleFactor = boundaryHeight / (lines.length*fontLeading);
            System.out.println("Fit Height Scale Factor: " + fitHeightScaleFactor);

            // Go with the smaller of the calculated width and height scale values
            if (fitHeightScaleFactor < fitWidthScaleFactor)
                autoScaleFactor = fitHeightScaleFactor;
            else
                autoScaleFactor = fitWidthScaleFactor;
            System.out.println("Auto Scale Factor: " + autoScaleFactor);

            // Determine the Y offset for the starting point of the text
            float textYOffset = 0.0f;
            if (autoScaleFactor == fitWidthScaleFactor)
                textYOffset = (boundaryHeight-(lines.length)*fontSize)/2;
            else {
                if (autoScale == true)
                    textYOffset = whitespaceGap * lines.length / 2;
                else
                    textYOffset = 0.0f;
            }

            matrix.translate(boundaryX,boundaryY-textYOffset);
        }


        /* Scale the text if desired */
        if (autoScale == true)
            matrix.scale(autoScaleFactor,autoScaleFactor);
        else
            autoScaleFactor = 1.0f;


        /* Apply the text matrix to the content stream */
        stream.setTextMatrix(matrix);


        /* Draw the lines of text */
        for (int i = 0; i < lines.length; i++)
        {
            // Default the line offset to zero for ALIGNMENT.LEFT and adjust for other types of alignment
            float lineOffset = 0f;

            if (alignment == ALIGNMENT.CENTER) {
                if (rotation == ROTATION.RIGHT || rotation == ROTATION.LEFT)
                    lineOffset = (boundaryHeight / autoScaleFactor / 2) - (font.getStringWidth(lines[i]) / 1000f * fontSize / 2);
                if (rotation == ROTATION.NONE)
                    lineOffset = (boundaryWidth / autoScaleFactor / 2) - (font.getStringWidth(lines[i]) / 1000f * fontSize / 2);
            }

            if (alignment == ALIGNMENT.RIGHT) {
                if (rotation == ROTATION.RIGHT || rotation == ROTATION.LEFT)
                    lineOffset = (boundaryHeight / autoScaleFactor) - (font.getStringWidth(lines[i]) / 1000f * fontSize);
                if (rotation == ROTATION.NONE)
                    lineOffset = (boundaryWidth / autoScaleFactor) - (font.getStringWidth(lines[i]) / 1000f * fontSize);
            }

            System.out.println("Line Offset: " + lineOffset);

            // Move the cursor to the appropriate new location relative to its current old location
            stream.newLineAtOffset(lineOffset, -fontLeading);

            // Draw the text
            stream.showText(lines[i]);

            // Reset the cursor to a predictable state for the next loop iteration
            stream.moveTextPositionByAmount(-lineOffset,0);
        }

        stream.endText();
        stream.close();
    }
}