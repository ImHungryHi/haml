package be.syntra.beige.team;

import java.io.*;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;
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

        CommandLineInterpreter interpreter = new CommandLineInterpreter();
        interpreter.interpretCommand(args);
        try {
            if((!interpreter.isIsError() && interpreter.getFileNames().size()>0) || interpreter.isWatch()) {
                for (int i = 0; i < interpreter.getFileNames().size(); i += 2) {
                    if (interpreter.getFileNames().get(i + 1) == null) {
                        compileFile(interpreter.getFileNames().get(i));
                    } else if (interpreter.getFileNames().get(i + 1) != null) {
                        compileFile(interpreter.getFileNames().get(i), interpreter.getFileNames().get(i + 1));
                    }
                }
                System.out.println(interpreter.isUpdate() ? "Updating is done!" : "Compiling is done.");
                if(interpreter.isWatch()){
                    Path path = CommandLineInterpreter.getInputPathDirectoryToWatch();
                    try{
                        WatchService watcher = path.getFileSystem().newWatchService();
                        path.register(watcher, ENTRY_MODIFY);
                        System.out.println("Watching directory...");
                        WatchKey key;
                        while((key = watcher.take()) != null){

                            for(WatchEvent<?> event : key.pollEvents()){
                                WatchEvent.Kind<?> kind = event.kind();

                                WatchEvent<Path> ev = (WatchEvent<Path>)event;
                                Path fileName = ev.context();

                                if(kind == ENTRY_MODIFY && fileName.toString().endsWith(".haml")){
                                    String[] arr = fileName.toString().split("\\.");
                                    System.out.println(fileName);
                                    compileFile(fileName.toString(),arr[0] + ".html");
                                }
                            }
                            boolean valid = key.reset();
                            if(!valid){
                                break;
                            }
                        }
                    } catch(IOException | InterruptedException e){
                        System.out.println(e + ":" + "watching failed");
                    }
                }
            } else if(!interpreter.isHelp()) System.out.println(interpreter.getError());
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     *  First the method will set up a reader for the .haml inputfile. The reader will read the file and pushes every line to
     *  the haml converter so we can create a hamlDataElement for each line of the inputfile. The htmlconverter parses every hamlDataElement to a line of usable HTML code.
     *  When no outputfile was given, the writer prints the result to console.
     * @param input filename of inputfile
     * @throws IOException
     */
    public static void compileFile(String input) throws IOException {
        ReaderHaml rh = new ReaderHaml(input);
        HamlData hd = rh.read();
        HamlValidation validator = HamlValidation.validateHaml(hd);
        if(validator.isValid()){

            // Create Html object
            Html html = new Html(hd.getInputFileName(), hd.getOutputFileName());
            // Pass HamlDataElements to HtmlConverter
            HtmlConverter.convertToHtml(hd.getHamlDataElements(), html);
            // Pass Html object to Writer
            Writer writer = new Writer(html.getInputFileName(), html.getOutputFileName(), html.getHtmlElements());
            writer.writeToOutputFile();

        }else{

            // Parse the hamlErrors arraylist of the validator object
            //
            System.out.println("\nErrors found in " + hd.getInputFileName() + ":");
            for(String s : validator.getHamlErrors()){
                System.out.println(s);
            }
            System.out.println("=> " + hd.getInputFileName() + " not parsed!");

        }
    }

    /**
     * First the method will set up a reader for the .haml inputfile. The reader will read the file and pushes every line to
     * the haml converter so we can create a hamlDataElement for each line of the inputfile. The htmlconverter parses every hamlDataElement to a line of usable HTML code.
     * The writer creates (when non-existing) or modifies the given outputfile.
     * @param input filename of inputfile
     * @param output filename of outputfile
     * @throws IOException
     */
    public static void compileFile(String input, String output) throws IOException {
        ReaderHaml rh = new ReaderHaml(input, output);
        HamlData hd = rh.read();
        HamlValidation validator = HamlValidation.validateHaml(hd);
        if(validator.isValid()){

            // Create Html object
            Html html = new Html(hd.getInputFileName(), hd.getOutputFileName());
            // Pass HamlDataElements to HtmlConverter
            HtmlConverter.convertToHtml(hd.getHamlDataElements(), html);
            // Pass Html object to Writer
            //
            Writer writer = new Writer(html.getInputFileName(), html.getOutputFileName(), html.getHtmlElements());
            writer.writeToOutputFile();

        }else{

            // Parse the hamlErrors arraylist of the validator object
            //
            System.out.println("\nErrors found in " + hd.getInputFileName() + ":");
            for(String s : validator.getHamlErrors()){
                System.out.println(s);
            }
            System.out.println("=> " + hd.getInputFileName() + " not parsed!");

        }
    }
}
