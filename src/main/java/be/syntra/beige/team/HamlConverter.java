package be.syntra.beige.team;

import java.util.HashMap;

/**
 * <h1>HamlConverter</h1>
 * HamlConverter class contains all methods to convert a line in a .haml file to a HamlDataElement object.
 * It returns a HamlDataElement and adds it to HamlDataElements arraylist in the HamlData object.
 * <p>
 * To be added:
 * list of all methods
 *
 *
 * @author  Team Beige
 * @version 1.0
 * @since   2021-03-24
 */

public class HamlConverter {
    private static int HamlLineCounter;

    // Methods to compile HAML code to proper HamlDataElement object parameters
    //
    public static int returnLineNumber(){
        return ++HamlLineCounter;
    }

    public static int returnDepth(String input){
        int depth = 0;
        int pos = 0;
        if(input.startsWith(Config.INDENTATION)){
            while((pos + 2 < input.length()) && input.substring(pos,pos+2).equals(Config.INDENTATION)){
                depth++;
                pos += 2;
            }
        }
        return depth;
    }

    public static char firstChar(String input){
        String normalizedInput = input.trim();
        char ch1 = normalizedInput.charAt(0);
        return ch1;
    }

    public static boolean returnIsTag(String input){
        char ch1 = firstChar(input);
        return (ch1 == '%' || ch1 == '#' || ch1 == '.');
    }

    public static String returnTagName(String input){
        String tagName = null;

        // Check what kind of elementType
        char symbol = firstChar(input);
        if(symbol == '#' || symbol == '.'){
            tagName = "div";
        }else if(symbol == '%'){
            // check the tagname
            int symbolPos = input.indexOf('%');
            String[] arr = input.substring(symbolPos + 1).split("\\P{Alnum}+");
            tagName = arr[0].equals("") ? "div" : arr[0];
        }

        return tagName;
    }

    public static String returnIdName(String input){
        String id = null;
        int startPos;
        int endPos = -1;
        int currPos;
        String currChar = "";

        // If the haml line represents a tag and # occurs, get the first occurrence of # and retrieve the idName
        if(returnIsTag(input) && input.contains("#")){
            // get start position of id name
            startPos = input.indexOf("#") + 1;
            // get end position of id name, can end by a ' ','.','(','<','>' or end of line (returns -1 in this case)
            for(int i = startPos; i < input.length();i++){
                currChar = input.substring(i,i+1);
                if(currChar.equals(" ") || currChar.equals(".") || currChar.equals("(") || currChar.equals("<") || currChar.equals(">")){
                    endPos = i;
                    break;
                }
            }
            id = (endPos != -1) ? input.substring(startPos, endPos) : input.substring(startPos);
        }
        return id;
    }

    public static boolean returnHasWSRM(String input){
        boolean wsrm = false;
        // Todo
        // Still needs improving, because '<' or '>' might occur in the text content.
        // Then it cannot be considered as a white space removal symbol.
        // Therefore we need to check if '>' or '<' is set directly after the tag definition.
        if(returnIsTag(input) && (input.contains(">") || input.contains("<"))){
            wsrm = true;
        }
        return wsrm;
    }

    public static String returnWsrmType(String input){
        String wsrmType = "";
        if(returnHasWSRM(input)){
            wsrmType += input.contains(">") ? ">" : "";
            wsrmType += input.contains("<") ? "<" : "";
        }
        return wsrmType;
    }

    // Todo: returnHasText method here (boolean)
    // Return whether text was found in line
    //

    // Todo: returnTextContent method here (String)
    // Return the actual text content found in the line
    //

    // Todo: returnIsComment method here (boolean)
    // Return whether a comment was found in line
    //

    // Todo: returnCommentType method here (String, either 'htmlComment' or 'hamlComment')
    // Return the commenttype found in line (htmlComment or hamlComment)
    //

    // Todo: returnCommentContent method here (String)
    // Return the comment content
    //

    // Todo: returnHasEscaping method here (boolean)
    // Return if escape symbol was found in line
    //

    // Todo: returnEscapedContent method here (String)
    // Return escaped content found in line
    //

    // Todo: returnClassNames method here (String)
    // Return classNames
    //

    // Todo: returnAttributes method here (HashMap<String,String>)
    // Return attributes
    //


    // Convert a String to a HamlDataElement object
    //
    public static HamlDataElement convertToElement(String input){
        //
        // Retrieve data from input string and convert to HamlDataElement object parameters
        //


        // Retrieve the obligatory parameters for the HamlDataElement object
        //
        // Return lineNumber
        int lineNumber = returnLineNumber();
        // Return depth
        int depth = returnDepth(input);
        // Return whether line contains a tag or not
        boolean isTag = returnIsTag(input);
        // Return elementType, if a tag was found
        String tagName = isTag ? returnTagName(input) : null;
        // Return whether text was found in line
        boolean hasText = false; // Todo: call returnHasText method here
        // Return the actual text content found in the line
        String textContent = ""; // Todo: call returnTextContent method here
        // Return whether comment was found in line
        boolean isComment = false; // Todo: call returnIsComment method here
        // Return the commenttype found in line (htmlComment or hamlComment)
        String commentType = ""; // Todo: call returnCommentType method here
        // Return the comment content
        String commentContent = ""; // Todo: call returnCommentContent method here
        // Return if escape symbol was found in line
        boolean hasEscaping = false; // Todo: call returnHasEscaping method here
        // Return escaped content found in line
        String escapedContent = ""; // Todo: call returnEscapedContent method here
        // Return if element has a white-space-removal symbol
        boolean hasWhiteSpaceRemoval = returnHasWSRM(input);
        // Return what white-space-removal symbols
        String whiteSpaceRemovalType = returnWsrmType(input);


        // Retrieve the optional parameters for the HamlDataElement object
        //
        // Return idName
        String id = returnIdName(input);
        // Return classNames
        String className = ""; // Todo: call returnClassNames method here
        // Return attributes
        HashMap<String,String> attributes = null; // Todo: call returnAttributes method here



        // Create minimal HamlDataElement via constructor
        //
        HamlDataElement el = new HamlDataElement(
                lineNumber,
                depth,
                isTag,
                tagName,
                hasText,
                textContent,
                isComment,
                commentType,
                commentContent,
                hasEscaping,
                escapedContent,
                hasWhiteSpaceRemoval,
                whiteSpaceRemovalType
        );


        // Get optional data if available and add them with setters
        //
        // Set id
        if(id != null){ el.setId(id); }
        // Set classes
        // Todo: use setter method 'setClassName'
        // Set attributes
        // Todo: use setter method 'setAttributes'



        return el;
    }

}
