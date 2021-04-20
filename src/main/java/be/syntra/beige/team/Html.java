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
     * ----------------------------------
     * ***          Variables         ***
     * ----------------------------------
     */
    private String inputFileName; // just for console message output
    private String outputFileName;
    private ArrayList<String> htmlElements;

    /**
     * ----------------------------------
     * ***        Constructors        ***
     * ----------------------------------
     */
    public Html(String inputFileName, String outputFileName){
        this(inputFileName);
        this.outputFileName = outputFileName;
    }

    public Html(String inputFileName) {
        this.inputFileName = inputFileName;
        this.htmlElements = new ArrayList<>();
    }

    /**
     * ----------------------------------
     * ***         Functions          ***
     * ----------------------------------
     */
    public void addElement(String element) {
        htmlElements.add(element);
    }

    public void addToPrevious(String element) {
        int lastIndex = htmlElements.size() - 1;
        String lastElement = htmlElements.get(lastIndex);
        htmlElements.set(lastIndex, lastElement + element);
    }

    /**
     * toString
     */
    @Override
    public String toString() {
        String output = "";

        if (outputFileName != null && !outputFileName.equals("")) {
            output += "Html output for file: '" + outputFileName + "'\n";
        }
        else {
            output += "Html converted from file: '" + inputFileName + "'\n";
        }

        if (htmlElements.isEmpty()) {
            output += "Nothing is parsed yet, call 1-800-FIXIT!";
        }

        for(String s : htmlElements){
            output += s + "\n";
            //output += s;
        }

        return output;
    }

    /**
     * ----------------------------------
     * ***          Getters           ***
     * ----------------------------------
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
     * ----------------------------------
     * ***           Setters          ***
     * ----------------------------------
     */
    public void setHtmlElements(ArrayList<String> htmlElements) {
        this.htmlElements = htmlElements;
    }
}
