package be.syntra.beige.team;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <h1>Writer</h1>
 * Writer object receives input file name, output file name (optional), list of htmlElements.
 * Writer either parses Html to file or to console
 * <p>
 * Writer object contains:
 * input filename
 * output filename
 * htmlElements list
 *
 *
 * @author  Team Beige
 * @version 1.0
 * @since   2021-03-24
 */

public class Writer {
    String inputFileName;
    String outputFileName;
    ArrayList<String> htmlElements;

    // Temporary:
    // Hardcoded filepath
    String outputFilePath = System.getProperty("user.dir") + "/src/main/java/be/syntra/beige/team/";

    public Writer(String inputFileName, String outputFileName, ArrayList<String> htmlElements) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.htmlElements = htmlElements;
    }

    private void createFile(){
        // Create outputfile
        //
        try {
            File outputFile = new File(outputFilePath + outputFileName);
            if (outputFile.createNewFile()) {
                System.out.println("File created: " + outputFile.getName());
            } else {
                // Delete existing file & recreate it
                if (outputFile.delete()) {
                    createFile();
                } else {
                    System.out.println("Failed to delete & recreate the outputfile.");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void writeToOutputFile(){
        // Create file
        createFile();

        // Write to created file
        try {
            FileWriter fileWriter = new FileWriter(outputFilePath + outputFileName);

            for(String line : htmlElements){
                fileWriter.write(line + "\n");
            }

            fileWriter.close();

            System.out.println("Successfully wrote to the outputfile.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to outputfile.");
            e.printStackTrace();
        }
    }
}
