package be.syntra.beige.team;



import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;

/** interpreter of command lines:
 commands:

 one to one mode:

 (One-to-one mode compiles a single input file (input.haml)
 to a single output location (output.html). If no output location is passed,
 the compiled CSS is printed to the terminal.
 The input file is parsed as HAML if its extension is .haml.)

 java -jar HamlIt.jar <input.haml> [output.html]

 many to many mode (compiles one or more input files into one or more output files.
 It can also comile all HAML files in a directory to
 HTML files with the same name in another directory):

 # Compiles index.haml to index.html.
 $ java -jar HamlIt.jar index.haml:index.html

 # Compiles index.jhaml and contact.jhml to index.html and contact.html.
 $ java -jar HamlIt.jar index.haml:index.html contact.haml:contact.html

 # Compiles all haml files in themes/ to html files in public/site/.
 $ java -jar HamlIt.jar themes:public/site

 When compiling whole directories, HamlIt will ignore files whose names begin with _.

 --update If the --update flag is passed, HamlIt will only compile
 files that have been modified more recently than the corresponding Html file was generated.
 It will also print status messages when updating files.

 -watch This flag acts like the --update flag,
 but after the first round compilation is done
 HamlIt stays open and continues compiling files whenever they change.



 possible inputs:
 1) input.haml output.html (> inputfile and create outputfile)
 2) input.haml (convert hamlfile to console)
 3) index.haml:index.html (> inputfile and create outputfile)
 4) index.haml:index.html contact.haml:contact.html first.haml:first.html
 5) themes:public/site (all files in themes directory to public/site directory
 6) --update directory
 7) --update file (check .html file, if newer, update html file; not newer, "file is up-to-date"
 8)
 */

public class CommandLineInterpreter {
    private String[] args;

    private static String filenameInput;
    private static String filenameOutput;
    private ArrayList<String> fileNames;
    private static File dirPath = new File(System.getProperty("user.dir"));
    private boolean update = false;
    private boolean watch = false;

    public static void main(String[] args) {
        CommandLineInterpreter interpreter = new CommandLineInterpreter();
        for (String s : args) {
            System.out.println(s);

        }
        System.out.println(dirPath);

        interpreter.interpretCommand(args);
    }


    public void interpretCommand(String[] args) {
        System.out.println(args.length);

        if (args.length == 0) {
            System.out.println("No arguments given. Use \"--help\" for commands.");
        } else if (args.length == 1) {
            String command = args[0];
            if (command.equals("--help")) {
                System.out.println("help commands: ");//showCommands()
            } else if (command.equals("--update")) {
                getFilesToUpdate(); //check html files older than haml files
            } else if (command.equals("--watch")) {
                watch = true;//initiate watch service
            } else {
                if (command.matches(".:.")) {
                    fileNames.add(splitCommand(args[0])[0]);
                    fileNames.add(splitCommand(args[0])[1]);

                } else {
                    fileNames.add(command);
                    fileNames.add(null); //write to console;
                }
            }
        } else {
            System.out.println("nothing happened");
        }

    }


    public String[] splitCommand(String s) {
        return s.split(":");
    }


    //gives back a String[] with the haml files that need updating;
    public void getFilesToUpdate() {
        System.out.println("looking for outdated files");
        File dir = new File(".");
        if (dir.isDirectory()) {
            String[] files = dir.list(new FilenameFilter() {
                                          @Override
                                          public boolean accept(File dir, String name) {
                                              if (name.endsWith(".haml")) {
                                                  try {
                                                      checkBasicAttributes(name);
                                                  } catch (IOException e) {
                                                      e.printStackTrace();
                                                  }
                                              }
                                              return false;
                                          }
                                      }
            );
            System.out.println("Files collected");
            for (String s : files) {
                System.out.println(s);
            }
        } else System.out.println("error no directory");
    }

    public boolean checkBasicAttributes(String nameHaml) throws IOException {
        Path fileHaml = Paths.get(nameHaml);
        BasicFileAttributes attrHaml = Files.readAttributes(fileHaml, BasicFileAttributes.class);
        System.out.println("modified time: " + attrHaml.lastModifiedTime());
        System.out.println(nameHaml);
        String nameHtml = nameHaml.split(".")[0] + ".html";
        File test = new File(dirPath + "/" + nameHtml);
        if (test.isFile()) {
            System.out.println("output file exists");
            Path fileHtml = Paths.get(nameHtml);
            BasicFileAttributes attrHtml = Files.readAttributes(fileHtml, BasicFileAttributes.class);
            System.out.println(attrHtml.lastModifiedTime());
            return attrHaml.lastModifiedTime().compareTo(attrHtml.lastModifiedTime()) == 1 ? true : false;
            } else {
            System.out.println(nameHaml + "no output file");
            return true;
            }

        }
}

