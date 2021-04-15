package be.syntra.beige.team;

import java.util.ArrayList;

/**
 * <h1>Html</h1>
 * Html object contains structured data to write a html.
 * The object gets populated by HtmlConverter.
 * <p>
 * Html object contains:
 * input haml filename,
 * output html filename (optional),
 * arraylist of htmlElements
 *
 *
 * @author  Team Beige
 * @version 1.0
 * @since   2021-03-24
 */

public class Html {
    /**
     * Variables
     */
    private String inputFileName; // just for console message output
    private String outputFileName;
    private ArrayList<String> htmlElements;


    /**
     * Constructor
     */
    public Html(String inputFileName, String outputFileName){
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    /**
     * toString
     */
    @Override
    public String toString() {
        String output = "Html output for file: '" + outputFileName + "'\n";
        for(String s : htmlElements){
            //output += s + "\n";
            output += s;
        }
        return output;
    }

    /**
     * Getters
     */

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public ArrayList<String> getHtmlElements() {
        return htmlElements;
    }

    /**
     * Setter
     */
    public void setHtmlElements(ArrayList<String> htmlElements) {
        this.htmlElements = htmlElements;
    }
}