package be.syntra.beige.team;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * <h1>ReaderHaml</h1>
 * ReaderHaml stores input & output filenames
 * ReaderHaml read method creates a HamlData object.
 * ReaderHaml read method reads each line of the input haml file
 * and eventually returns a HamlData object.
 * <p>
 * ReaderHaml object contains:
 * input haml filename,
 * output html filename (optional)
 *
 *
 * @author  Team Beige
 * @version 1.0
 * @since   2021-03-24
 */

public class ReaderHaml {
    String inputFileName;
    String outputFileName;

    /**
     * Constructors:
     * Temporary empty constructor
     * Constructor with only input file
     * Constructor with inputfile & outputfile
     */
    // Temporary: empty constructor to allow hardcoded example.haml input file & hardcoded outputfileName
    public ReaderHaml(){

    }

    // Only inputfile
    public ReaderHaml(String inputFileName){
        this.inputFileName = inputFileName;
    }

    // Input & outputfile
    public ReaderHaml(String inputFileName, String outputFileName){
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }


    /**
     * Read method for reading input file
     * Creating HamlData object
     *
     * Temporary:
     * hardcoded input & output file
     *
     * @throws IOException
     */

    public static HamlData read() throws IOException{
        // TODO
        // Retrieve input & output file from ReaderHaml object
        // Create path + filenames to read from
        // -- code here --

        // Temporary:
        // Hardcoded read from example.haml file
        // Construct relative path + filename
        String filePath = new File("").getAbsolutePath();
        filePath += "\\src\\main\\java\\be\\syntra\\beige\\team\\";
        String inputFileName = "example.haml";

        // Temporary:
        // Hardcoded defining of html outputfile from inputFileName
        // Eventually this will need to come from console command read.
        String[] outputFile = inputFileName.split("\\.");
        String outputFileName = outputFile[0] + ".html";

        // TODO
        // Handle exceptions (try, catch)
        //
        // Create bufferedreader
        BufferedReader br = new BufferedReader(new FileReader(filePath + inputFileName));
        String line;


        // Create HamlData object
        HamlData myHamlData = new HamlData(inputFileName, outputFileName);

        // Read Haml inputfile & add to HamlData object
        while ((line = br.readLine()) != null) {
            // For now, simply add each line to an HamlDataElement
            // -------
            // Store Haml data in the HamlData object
            // -------
            myHamlData.hamlDataElements.add(HamlConverter.convertToElement(line));
        }

        // Optimize hamlDataElements array in the Haml object for the Htmlconverter
        HamlConverter.nestHamlDataElements(myHamlData);

        // Todo
        // Close the reader

        return myHamlData;

    }

}
