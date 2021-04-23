package be.syntra.beige.team;



import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private Path outputPathForDirectory;
    private static String error = "Something went wrong. Check \"--help\" for commands";

    public static void main(String[] args) throws IOException {
        CommandLineInterpreter interpreter = new CommandLineInterpreter();
        System.out.println("Arguments:");
        for (String s : args) {
            System.out.println("Argument: " + s);
        }

        interpreter.interpretCommand(args);

        try{
            for (int i = 0; i< fileNames.size();i++){
                System.out.println(fileNames.get(i));
            }

        }
         catch (NullPointerException e){
             System.out.println(error);
         }
    }

    /**
     *
     * @param args
     * Sets the 'filesNames' arraylist with the names of the inputfiles and the names of their respective outputfiles
     */
    public void interpretCommand(String[] args) {
        if (args.length == 0) {
            System.out.println("No arguments given. Use \"--help\" for commands.\n");
        }
        else if (args.length == 1) {
            String command = args[0];
            if (command.equals("--help")) {
                showCommands();
            }
            else if (command.equals("--update")) {
                addToFileNames(getFilesToUpdate());
            }
            else if (command.equals("--watch")) {
                addToFileNames(getFilesToUpdate());
                // same as update => so get outdated files + setup watch service in directory.
            }
            else if(command.contains(":")) {
                if(countDoublePoint(command)){
                    interpretDoublePoint(command);
                } else error = "Wrong command, contains multiple \":\". Use \"--help\" for commands.\n";
            }
            else {
                    fileNames.add(command);
                    fileNames.add(null); //write to console;
            }

        } else {
            System.out.println("args length > 1" );
        }
    }
   /*
   GETTER
    */
    public static ArrayList<String> getFileNames() {
        return fileNames;
    }

    /**
    Adds the files that need to be compiled to arrayList FileNames so that our app can get the list.
     **/
    public static void addToFileNames(String[] arr){
        for(String s : arr){
            fileNames.add(s);
            String split = s.split("\\.")[0] + ".html";
            fileNames.add(split);
        }
    }


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
    gives back a String[] with the haml files that need updating;
     **/
    public String[] getFilesToUpdate() {
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
     checks which files are outdated based on time
     of modification in correspondence with their respective outputfiles.
      When haml file has no corresponding .html file, its is included in outdatedlist and will be compiled.
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
     *
     * @param command
     * interprets to command with ":". Checks if command is about directory to directory or about file to file;
     * In the first case we put ToDirectory to true to signal app that he needs to use outputPathForDirectory as outputpath.
     */
    public void interpretDoublePoint(String command){
        String[] arr = command.split(":");
        File inputDirFile = new File(arr[0]);
        File outputDirFile = new File(arr[1]);


        if(inputDirFile.isDirectory()){
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
            } else
                addToFileNames(files);
            }
        else if(inputDirFile.isFile() && inputDirFile.toPath().endsWith(".haml")) {
            addToFileNames(arr);
        }
    }

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

