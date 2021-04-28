package be.syntra.beige.team;

import java.util.ArrayList;

/**
 * <h1>HamlValidation</h1>
 * HamlValidation object contains validation data from 1 haml file:
 * - boolean valid
 * - arraylist of hamlErrors
 *
 *
 * @author  Team Beige
 * @version 1.0
 * @since   2021-03-24
 */

public class HamlValidation {
    private boolean valid;
    private ArrayList<String> hamlErrors = new ArrayList<>();

    /**
     * Constructor
     */
    private HamlValidation(boolean valid) {
        this.valid = valid;
    }

    /**
     * Getters
     */
    public boolean isValid() {
        return valid;
    }

    public ArrayList<String> getHamlErrors() {
        return hamlErrors;
    }

    /**
     * Methods
     */
    // Recursive method to loop through the children of a HamlDataElement
    // Method is used to build an easier arraylist to apply validation on
    //
    public static void buildObjToValidate(HamlDataElement el, ArrayList<HamlDataElement> array){
        if(el.getChildren() != null || el.getChildren().size() > 0){
            for (HamlDataElement child : el.getChildren()) {
                array.add(child);
                buildObjToValidate(child,array);
            }
        }
    }

    // Method that validates a HamlData object
    // returns a HamlValidation object with validation data
    //
    public static HamlValidation validateHaml(HamlData hamlData){

        // Initialize HamlValidation object, setting valid to true
        HamlValidation validatedHaml = new HamlValidation(true);

        // Declare original HamlDataElements arraylist
        ArrayList<HamlDataElement> elements = hamlData.getHamlDataElements();

        // Transform original arraylist to easier arraylist to apply validation on
        //
        ArrayList<HamlDataElement> objToValidate = new ArrayList<HamlDataElement>();
        for(HamlDataElement el : elements){
            objToValidate.add(el);
            buildObjToValidate(el,objToValidate);
        }

        // Validate nesting of HamlDataElements
        //
        HamlDataElement currEl;
        HamlDataElement nextEl;

        //  HamlData shouldn't be empty
        //  Probable cause, first line has no zero depth
        //
        if(objToValidate.size() < 1){
            validatedHaml.valid = false;
            validatedHaml.hamlErrors.add("First line should start at zero depth.");
        }

        for(int i = 0 ; i < objToValidate.size(); i++){
            currEl = objToValidate.get(i);
            nextEl = i < objToValidate.size() - 1 ? objToValidate.get(i+1) : null;

            // A HamlDataElement of depth x containing a tag,
            // cannot be followed by a HamlDataElement of the same depth x, which is not a tag or comment
            // if error is found, change valid property to false
            // gather linenumber and error message and store it in hamlErrors arrayList
            if(
                    nextEl != null && currEl.isTag() &&
                            (currEl.getDepth() == nextEl.getDepth() && !nextEl.isTag() && !nextEl.isComment())
            ){
                validatedHaml.valid = false;
                validatedHaml.hamlErrors.add("Nesting error on line " + nextEl.getLineNumber());
            }

        }

        // Return HamlValidation object
        //
        return validatedHaml;

    }
}
