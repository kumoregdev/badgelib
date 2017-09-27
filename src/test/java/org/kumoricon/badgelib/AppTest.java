package org.kumoricon.badgelib;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        /*
            REQUIREMENTS:
            Function 1:
                -Pass an arbitrary number of strings which must automatically fit in the PDF
            Function 2:
                -Pass text to be printed vertically on the left or right on the PDF
                -The text should align perfectly
         */
        App a = new App();
        String[] s = new String[]{""};
        a.main(s);
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec("xdg-open /tmp/test.pdf");
        }
        catch (java.io.IOException e) {
            assertTrue( false );
        }
        assertTrue( true );
    }
}
