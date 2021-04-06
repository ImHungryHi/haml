package be.syntra.beige.team;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Simple Haml to Html compile test
     */
    @Test
    public void compileSimpleTags() {
        // Desired output
        // Create desired list of HtmlElements
        ArrayList<String> desiredHtmlElements = new ArrayList<String>(Arrays.asList("<br>","<br>","<br>"));

        // Output to test
        // Create list of hamlFileLines
        ArrayList<String> hamlFileLines = new ArrayList<String>(Arrays.asList("%br","%br","%br"));
        // Create HamlData object
        HamlData hd = new HamlData("test.haml","test.html");
        for(String hamlLine : hamlFileLines){
            hd.hamlDataElements.add(HamlConverter.convertToElement(hamlLine));
        }
        // Create Html object
        Html h = new Html(hd.getInputFileName(),hd.getOutputFileName());
        HtmlConverter.convertToHtml(hd.getHamlDataElements(), h);
        // Create list of HtmlElements to test
        ArrayList<String> realHtmlElements = h.getHtmlElements();

        // Compare 2 lists
        assertEquals(desiredHtmlElements,realHtmlElements);
    }
}
