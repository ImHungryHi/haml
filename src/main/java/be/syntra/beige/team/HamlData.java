package be.syntra.beige.team;

import java.util.ArrayList;

/**
 * <h1>HamlData</h1>
 * HamlData object contains structured data of .haml file that should be converted to html.
 * The object gets created by ReaderHaml read method
 * The HamlDataElements variable in HamlData object gets populated by HamlConverter.
 * <p>
 * HamlData object contains:
 * input haml filename,
 * output html filename (optional),
 * arraylist of HamlDataElements
 *
 *
 * @author  Team Beige
 * @version 1.0
 * @since   2021-03-24
 */

public class HamlData {
    /**
     * Variables
     */
    private String inputFileName;
    private String outputFileName;
    ArrayList<HamlDataElement> hamlDataElements = new ArrayList<HamlDataElement>();

    /**
     * Constructors
     */
    public HamlData(String inputFileName){
        this.inputFileName = inputFileName;
    }

    public HamlData(String inputFileName, String outputFileName){
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    /**
     *
     * ToString
     */
    @Override
    public String toString() {
        String s = "HamlData object for file: '" + inputFileName + "'\n";
        for(HamlDataElement el : hamlDataElements){
            s += el + "\n";
        }
        return s;
    }

    /**
     *
     * Getters
     */

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public ArrayList<HamlDataElement> getHamlDataElements() {
        return hamlDataElements;
    }
}
