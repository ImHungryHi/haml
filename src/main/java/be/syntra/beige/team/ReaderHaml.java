package be.syntra.beige.team;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

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
    public static String  inputFileName;
    public static String outputFileName;

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

        Path filePath = CommandLineInterpreter.isToDirectory() ? CommandLineInterpreter.getInputPathForDirectory() : CommandLineInterpreter.getDIRPATH();

        HamlData myHamlData = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath + "\\" + inputFileName));
            String line;


            // Create HamlData object
            myHamlData = new HamlData(inputFileName, outputFileName);

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
        } catch (Exception e){
            System.out.println(e +": your inputfile was not correct or does not exist");
        } finally {
            br.close();
        }

        return myHamlData;

    }

}
