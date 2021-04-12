package be.syntra.beige.team;

import java.io.*;

/**
 * <h1>App</h1>
 * Read command from console.
 * Create ReaderHaml
 * Create HamlData
 * Validate HamlData
 * Create Html
 * Write Html to file
 *
 *
 * @author  Team Beige
 * @version 1.0
 * @since   2021-03-24
 */

public class App {
    public static void main(String[] args) throws IOException {

        // Read command from console
        //
        // -- code here --


        // Construct ReaderHaml with parameters from console
        // Params: .haml input filename ; .html output filename (optional)
        // Temporary:
        // the example.haml input file & example.html output file are hardcoded in ReaderHaml
        //
        ReaderHaml rh = new ReaderHaml();
        HamlData hd = rh.read();

        // Temporary:
        // Print hamlData to console
        System.out.println(hd);


        // Validate HamlData object
        //
        if(HamlValidation.validateHaml(hd)){

            // Create Html object
            Html html = new Html(hd.getInputFileName(), hd.getOutputFileName());
            // Pass HamlDataElements to HtmlConverter
            HtmlConverter.convertToHtml(hd.getHamlDataElements().get(0), html);

            // Temporary:
            // print Html to console
            System.out.println(html);


            // Pass Html object to Writer
            //
            Writer writer = new Writer(html.getInputFileName(), html.getOutputFileName(), html.getHtmlElements());
            writer.writeToOutputFile();

        }else{
            // Temporary:
            // Figure out error handling and error messaging
            System.out.println("\nHaml file invalid. Error on linenumber Y\n");
        }

    }
}
