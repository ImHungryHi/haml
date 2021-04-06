package be.syntra.beige.team;

/**
 * <h1>HamlValidation</h1>
 * HamlValidation class inherits from Exception,
 * checks the HamlData object on faulty input,
 * returns error message with affected haml input filename & lineNumber
 * On success, return true
 *
 *
 * @author  Team Beige
 * @version 1.0
 * @since   2021-03-24
 */

public class HamlValidation {
    private static boolean valid = true;

    public static boolean validateHaml(HamlData hamlData){

        // Write validation rules here
        //
        // -- code here --


        // Check valid var
        // Todo
        // Ideally HamlValidation also returns the lilne number(s) where the error occurs
        //
        if(valid){
            System.out.println("\nHaml file valid. \\m/ Converting to html.\n");
            return true;
        }else{
            // Todo
            // Implement error message and linenumber
            // Make sure reader is closed afterwards
            // System.out.println("\nHaml file invalid. Error on linenumber X\n");
            return false;
        }

    }
}
