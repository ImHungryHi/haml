package be.syntra.beige.team;



import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;


/** interpreter of command lines:

 possible inputs:
 1) input.haml output.html (> inputfile and create outputfile)
 2) input.haml (convert hamlfile to console)
 3) index.haml:index.html (> inputfile and create outputfile)
 4) index.haml:index.html contact.haml:contact.html first.haml:first.html
 5) themes:public/site (all files in themes directory to public/site directory
 6) --update directory
 7) --update file (check .html file, if newer, update html file; not newer, "file is up-to-date"
 8)
 **/

public class CommandLineInterpreter {
    private static ArrayList<String> fileNames = new ArrayList<>();
    private static final File dirPath = new File(System.getProperty("user.dir"));
    private boolean watch = false;
    private static boolean toDirectory = false;
    private static Path outputPathForDirectory;
    private static String error = "Something went wrong. Check \"--help\" for commands";

    public static void main(String[] args) {
        CommandLineInterpreter interpreter = new CommandLineInterpreter();

        interpreter.interpretCommand(args);
        if(fileNames.size() == 0){
            System.out.println(error);
        } else
            System.out.println("Printing files:");

        for (int i = 0; i< fileNames.size();i++){
            System.out.println(fileNames.get(i));
        }
    }

    /**
     *
     * @param args
     * Sets the 'filesNames' arraylist with the names of the inputfiles and the names of their respective outputfiles
     */
    public void interpretCommand(String[] args) {
        if (args.length == 0) {
            error = "No arguments given. Use \"--help\" for commands.\n";
        }
        else if (args.length == 1) {
            String command = args[0];
            if (command.equals("--help")) {
                showCommands();
            }
            else if (command.equals("--update")) {
                addToFileNames(filesToUpdate());
            }
            else if (command.equals("--watch")) {
                addToFileNames(filesToUpdate());
                // same as update => so get outdated files + setup watch service in directory.
            }
            else if(command.contains(":")) {
                if(countDoublePoint(command)){
                    interpretDoublePoint(command);
                } else error = "Wrong command, contains multiple \":\". Use \"--help\" for commands.\n";
            }
            else if(checkHamlInput(command)){
                    addToFileNames(command, null);
            }
            else error = "Wrong command. Use \"--help\" for commands.\n";
        }
        else {
            System.out.println("args length > 1" );
        }
    }
   /*
   GETTERS
    */

    public static ArrayList<String> getFileNames() {
        return fileNames;
    }





    /*
    CLASS METHODS
     */

    /**
     * @method filesToUpdate
     * gives back a String[] with the haml files that need updating;
     **/
    public static boolean checkHamlInput(String input){
        return input.endsWith(".haml");
    }

    /**
     * @method addToFileNames
    Adds the files that need to be compiled to arrayList FileNames so that our app can get the list.
     **/
    public static void addToFileNames(String[] arr){
        for(String s : arr){
            fileNames.add(s);
            String split = s.split("\\.")[0] + ".html";
            fileNames.add(split);
        }
    }
    public static void addToFileNames(String input, String output){
        fileNames.add(input);
        fileNames.add(output);
    }

    /**
     * @method filesToUpdate
     * gives back a String[] with the haml files that need updating;
     **/
    public void showCommands(){
        System.out.println("# Compiles index.haml to index.html.");
        System.out.println("$ java -jar HamlIt.jar index.haml:index.html \n");

        System.out.println("# Compiles index.jhaml and contact.jhml to index.html and contact.html.");
        System.out.println("$ java -jar HamlIt.jar index.haml:index.html contact.haml:contact.html \n");

        System.out.println("# Compiles all haml files in themes/ to html files in public/site/.");
        System.out.println("$ java -jar HamlIt.jar themes:public/site \n");

        System.out.println("Options:");
        System.out.println("1) --update  (only compile files that have been modified more recently than the corresponding Html file was generated)");
        System.out.println("2) --watch (after the first round compilation is done HamlIt stays open and continues compiling files whenever they change.)");
    }


    /**
     * @method filesToUpdate
     * gives back a String[] with the haml files that need updating;
     **/
    public String[] filesToUpdate() {
        System.out.println("looking for outdated files");
        System.out.println("/n");

        File dir = new File(".");
        if (dir.isDirectory()) {
            String[] files = dir.list(new FilenameFilter() {
                  @Override
                  public boolean accept(File dir, String name) {
                      if (name.endsWith(".haml")) {
                          try {
                              return checkBasicAttributes(name);
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                      }
                      return false;
                  }
              }
            );
            return files;
        }
        System.out.println(error);
        return null;
    }


     /**
      * @method checkBasicAttributes
      * checks which files are outdated based on time
      * of modification in correspondence with their respective outputfiles.
      * When haml file has no corresponding .html file, its is included in outdatedlist and will be compiled.
     **/
    public boolean checkBasicAttributes(String nameHaml) throws IOException {
        Path fileHaml = Paths.get(nameHaml);
        BasicFileAttributes attrHaml = Files.readAttributes(fileHaml, BasicFileAttributes.class);

        String nameHtml = nameHaml.split("\\.")[0] + ".html";
        File fileCheck = new File(dirPath + "/" + nameHtml);

        if (fileCheck.isFile()) {
            System.out.println("output file exists");
            Path fileHtml = Paths.get(nameHtml);
            BasicFileAttributes attrHtml = Files.readAttributes(fileHtml, BasicFileAttributes.class);

            return attrHaml.lastModifiedTime().compareTo(attrHtml.lastModifiedTime()) == 1 ? true : false;
            } else {
            return true;
            }

        }


    /**
     * @method interpretDoublePoint
     * @param command
     * interprets to command with ":". Checks if command is about directory to directory or about file to file;
     * In the case of directory to directory, we check the existence of the input directory and check if the directory has .haml files.
     * If so we check the existence of the output Directory and try to make one when the directory does not exist.
     * Then we put ToDirectory to true to signal app that he needs to use outputPathForDirectory as outputpath.
     *
     * When the commands asks compiling file.haml to file.html, we check if the first part of the command (inputDirFile) is a file and if it ends with .haml.
     * We can then give the input- and outputfilename to the arraylist. Checks concerning the outputfile happen in Writer class.
     */
    public void interpretDoublePoint(String command) {
        String[] arr = command.split(":");
        File inputDirFile = new File(arr[0]);
        File outputDirFile = new File(arr[1]);


        if(inputDirFile.isFile() && inputDirFile.getPath().endsWith(".haml"))
        {
            addToFileNames(inputDirFile.toString(), outputDirFile.toString());
        }
        else if(inputDirFile.isDirectory()){
            String[] files = inputDirFile.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".haml");
                }
            });
            if(files == null){
                error = "No haml files detected.";
            }
            else if(outputDirFile.isDirectory()) {
                toDirectory = true;
                addToFileNames(files);
                outputPathForDirectory = Paths.get(String.valueOf(outputDirFile)).toAbsolutePath();
            } else {
                try{
                    outputDirFile.mkdirs();
                } catch (SecurityException e) {
                   error = "Could not created directory.";
                }
            }
        } else {
            error = "Something went wrong, the inputdirectory does not exist.";
        }
    }


    /**
     * @method countDoublePoint
     * counts the number of ":" in one elements of String[] args;
     **/
    public boolean countDoublePoint(String command){
        int count = 0;
        for(int i = 0; i<command.length(); i++){
            if(command.charAt(i) == ':'){
                count++;
            }
        }
        return count == 1 ? true : false;
    }
}

