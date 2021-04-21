package be.syntra.beige.team;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <h1>HamlConverter</h1>
 * HamlConverter class contains all methods to convert a line in a .haml file to a HamlDataElement object.
 * It returns a HamlDataElement and adds it to HamlDataElements arraylist in the HamlData object.
 * <p>
 *
 * To be added:
 * list of all methods
 *
 * convertToElement(String input) --> return type: HamlDataElement
 * firstChar(String input) --> return type: char
 * returnCommentContent(String input) --> return type: String
 * returnCommentType(String input) --> return type: String
 * returnDepth(String input) --> return type: int
 * returnHasText(String input) --> return type: boolean
 * returnHasWSRM(String input) --> return type: boolean
 * returnIdName(String input) --> return type: String
 * returnIsComment(String input) --> return type: boolean
 * returnIsTag(String input) --> --> return type: boolean
 * returnLineNumber() --> return type: int
 * returnTagName(String input) --> return type: String
 * returnTextContent(String input) --> return type: String
 * returnWsrmType(String input) --> return type: String
 *
 *
 * @author  Team Beige
 * @version 1.0
 * @since   2021-03-24
 */

public class HamlConverter {
    private static int HamlLineCounter;

    //
    // Methods to compile HAML code to proper HamlDataElement object parameters
    //

    // Return the linenumber in the .haml file
    //
    public static int returnLineNumber() {
        return ++HamlLineCounter;
    }

    // Return the depth of the element based on the indentation
    //
    public static int returnDepth(String input) {
        int depth = 0;
        int pos = 0;
        if (input.startsWith(Config.INDENTATION)) {

            while (
                    (pos + Config.LENGTH_INDENTATION < input.length()) &&
                    input.substring(pos, pos + Config.LENGTH_INDENTATION).equals(Config.INDENTATION)
            ) {
                depth++;
                pos += Config.LENGTH_INDENTATION;
            }
        }
        return depth;
    }

    // Helper method to return a haml line, ignoring the indentation
    //
    public static String inputZeroDepthForm(String input){
        return input.substring(returnDepth(input) * Config.LENGTH_INDENTATION);
    }

    // Helper method to return first character of a haml line, ignoring the indentation
    //
    public static char firstChar(String input) {
        String normalizedInput = input.trim();
        char ch1 = normalizedInput.charAt(0);
        return ch1;
    }

    // Return true if the haml line contains a tag
    //
    public static boolean returnIsTag(String input) {
        char ch1 = firstChar(input);
        return (ch1 == '%' || ch1 == '#' || ch1 == '.');
    }

    // Return the tag name
    //
    public static String returnTagName(String input) {
        String tagName = null;

        // Check what kind of elementType
        char symbol = firstChar(input);
        if (symbol == '#' || symbol == '.') {
            tagName = "div";
        } else if (symbol == '%') {
            // check the tagname
            int symbolPos = input.indexOf('%');
            String[] arr = input.substring(symbolPos + 1).split("\\P{Alnum}+");
            tagName = arr[0].equals("") ? "div" : arr[0];
        }

        return tagName;
    }

    // Return the id name
    //
    public static String returnIdName(String input) {
        String id = null;
        int startPos;
        int endPos = -1;
        int currPos;
        String currChar = "";

        // If the haml line represents a tag and # occurs, get the first occurrence of # and retrieve the idName
        if (returnIsTag(input) && input.contains("#")) {
            // get start position of id name
            startPos = input.indexOf("#") + 1;
            // get end position of id name, can end by a ' ','.','(','<','>' or end of line (returns -1 in this case)
            for (int i = startPos; i < input.length(); i++) {
                currChar = input.substring(i, i + 1);
                if (currChar.equals(" ") || currChar.equals(".") || currChar.equals("(") || currChar.equals("<") || currChar.equals(">")) {
                    endPos = i;
                    break;
                }
            }
            id = (endPos != -1) ? input.substring(startPos, endPos) : input.substring(startPos);
        }
        return id;
    }

    // Return whether text was found in line
    //
    public static boolean returnHasText(String input) {
        boolean hasText = false;

        // If haml line is not a tag and not a comment => true
        if(!returnIsTag(input) && !returnIsComment(input)){
            hasText = true;
        }
        // If haml line is a tag or a comment, check if content after space
        else if(inputZeroDepthForm(input).split(" ").length > 1)
        {
            hasText = true;
        }

        return hasText;
    }

    // Return the actual text content found in the line
    //
    public static String returnTextContent(String input) {
        String textContent = "";

        if (returnHasText(input)){
            if(!returnIsTag(input) && !returnIsComment(input)){
                // If not a tag and not a comment, set textContent
                textContent = inputZeroDepthForm(input);
            }else{
                // If a tag, find first space and set textContent
                if(returnIsTag(input)){
                    int textStart = inputZeroDepthForm(input).indexOf(" ") + 1;
                    textContent = inputZeroDepthForm(input).substring(textStart);
                }
                // If a htmlComment, trim haml html comment symbols '/ '
                if(returnCommentType(input).equals("htmlComment")){
                    textContent = inputZeroDepthForm(input).substring(2);
                }
            }
        }

        return textContent;
    }

    // Return whether a comment was found in line
    //
    public static boolean returnIsComment(String input) {
        boolean isComment = false;
        String input_Z_D_F = inputZeroDepthForm(input);

        if (input_Z_D_F.startsWith("/") || input_Z_D_F.startsWith("-#")) {
            isComment = true;
        }

        return isComment;
    }

    // Return the comment type found in line (htmlComment or hamlComment)
    //
    public static String returnCommentType(String input) {
       if (returnIsComment(input)) {
           input = inputZeroDepthForm(input);
           if (input.startsWith("/")){
               return "htmlComment";
           } else {
               return "hamlComment";
           }
       }
       return "";
    }

    // Return the comment content
    //
    public static String returnCommentContent(String input) {
        // MARKED AS OBSOLETE, returnTextContent method covers this functionality
        //

        return null;
    }

    // Return true if Whitespace removal symbols were found
    //
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

    // Return the type(s) of Whitespace removal symbols found
    //
    public static String returnWsrmType(String input){
        String wsrmType = "";
        if(returnHasWSRM(input)){
            wsrmType += input.contains(">") ? ">" : "";
            wsrmType += input.contains("<") ? "<" : "";
        }
        return wsrmType;
    }



    // OBSOLETE returnHasEscaping method here (boolean)
    // Return if escape symbol was found in line
    //

    // OBSOLETE returnEscapedContent method here (String)
    // Return escaped content found in line
    //

    // Return classNames
    //
    public static String returnClassName(String input) {
        String className = null;
        int startPos;
        int endPos = -1;
        int currPos;
        String currChar = "";

        // If the haml line represents a tag and . occurs, get the first occurrence of . and retrieve the className
        if ((returnIsTag(input) && input.contains(".")) && !extractAttributeContent(input).contains(".") ){
            // get start position of class name
            startPos = input.indexOf(".") + 1;
            // get end position of class name, can end by a ' ','#','(','<','>' or end of line (returns -1 in this case)
            for (int i = startPos; i < input.length(); i++) {
                currChar = input.substring(i, i + 1);

                if (currChar.equals(" ") || currChar.equals("#") || currChar.equals("(") || currChar.equals("<") || currChar.equals(">")) {
                    endPos = i;
                    break;
                }
                if (currChar.equals(".")){
                    input= input.replace(currChar," ");
                }
            }
            className = (endPos != -1) ? input.substring(startPos, endPos) : input.substring(startPos);
        }
        return className;
    }


    /**
     * Takes haml input and parses accolade- or parenthesis-type attribute information into a key-value HashMap.
     * @param input haml input line
     * @return a set of key-value attributes
     */
    public static HashMap<String, String> returnAttributes(String input) {
        HashMap<String, String> attributes = new HashMap<>();
        String strAttributes = extractAttributeContent(input);

        if (!strAttributes.equals("")) {
            strAttributes = deflateAttributeString(strAttributes);  // This decreases the checks to split from below (ie uniform input)
            String[] arrAttributes = splitAttributeString(strAttributes);

            // If we've found attributes, we now expect the array to contain values like:
            //  "key=\"value"   OR   "key=>\"value"
            if (arrAttributes.length > 0) {
                int idxEqualSign = -1;

                for (String oneAttribute : arrAttributes) {
                    idxEqualSign = oneAttribute.indexOf("=");

                    // When the quotes are BEFORE an equals sign, something's off - which invalidates this attribute
                    if (idxEqualSign >= 0 && idxEqualSign < oneAttribute.indexOf("\"")) {
                        String[] keySet = new String[0];    // Can't complain about uninitialized arrays now. Can you, compiler?

                        if (oneAttribute.charAt(idxEqualSign + 1) == '>') {
                            keySet = oneAttribute.split("=>");
                        }
                        else {
                            keySet = oneAttribute.split("=");
                        }

                        // Trim space at end of key and start of value (in case of " => " or " = ")
                        if (keySet[0].charAt(keySet[0].length() - 1) == ' ') {
                            keySet[0] = keySet[0].substring(0, keySet[0].length() - 1);
                        }
                        if (keySet[1].charAt(0) == ' ') {
                            keySet[1] = keySet[1].substring(1);
                        }

                        // Finally, add the attribute to the collection - without leading or trailing spaces
                        attributes.put(keySet[0].replace(":", "").trim(), keySet[1].replace("\"", "").trim());
                    }
                }
            }else{
                // Retrieve Strings key & value for a single attribute and put in HashMap attributes
                //
                String[] attr = strAttributes.split("=");
                String key = attr[0];
                String value = attr[1];
                attributes.put(key,value.replace("\"",""));
            }
        }

        /*
        System.out.println("----");
        for (HashMap.Entry<String, String> entry : attributes.entrySet()) {
            System.out.println(entry.getKey() + "|" + entry.getValue());
        }
         */

        return attributes;
    }

    private static String extractAttributeContent(String input) {
        int idxOpenAccolade = input.indexOf("{"),
                idxCloseAccolade = input.indexOf("}"),
                idxOpenParenthesis = input.indexOf("("),
                idxCloseParenthesis = input.indexOf(")");
        String strAttributes = "";

        if (idxOpenAccolade >= 0 && idxCloseAccolade > idxOpenAccolade) {
            strAttributes = input.substring(++idxOpenAccolade, idxCloseAccolade);
        }
        else if (idxOpenParenthesis >= 0 && idxCloseParenthesis > idxOpenParenthesis) {
            strAttributes = input.substring(++idxOpenParenthesis, idxCloseParenthesis);
        }
        // else - We didn't find any, leave the attributes empty

        return strAttributes;
    }

    private static String[] splitAttributeString(String input) {
        // We'll assume 3 possible formats in typing from this deflation:
        //  key="value",key="value"...
        //  :key="value",:key="value"...
        //  key="value" key="value"...
        if (input.contains("\",")) {
            return input.split("\",");
        }
        else if (input.contains(",:")) {
            return input.split(",:");
        }
        else if (input.contains("\" ")) {
            return input.split("\" ");
        }

        return new String[0];   // Skip null check/error later on
    }

    private static String deflateAttributeString(String input) {
        input = input.replace("\" , ", "\",");
        input = input.replace("\" ,", "\",");
        input = input.replace("\", ", "\",");
        input = input.replace("\" :", "\",:");
        input = input.replace(" , :", ",:");
        input = input.replace(", :", ",:");

        return input;
    }

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
        boolean hasText = returnHasText(input);
        // Return the actual text content found in the line
        String textContent = returnTextContent(input);
        // Return whether comment was found in line
        boolean isComment = returnIsComment(input);
        // Return the commenttype found in line (htmlComment or hamlComment)
        String commentType = returnCommentType(input);
        // Return the comment content
        String commentContent = returnCommentContent(input); //""; // OBSOLETE
        // Return if escape symbol was found in line
        boolean hasEscaping = false; // OBSOLETE call returnHasEscaping method here
        // Return escaped content found in line
        String escapedContent = "";  // OBSOLETE call returnEscapedContent method here
        // Return if element has a white-space-removal symbol
        boolean hasWhiteSpaceRemoval = returnHasWSRM(input);
        // Return what white-space-removal symbols
        String whiteSpaceRemovalType = returnWsrmType(input);


        // Retrieve the optional parameters for the HamlDataElement object
        //
        // Return idName
        String id = returnIdName(input);
        // Return classNames
        String className = returnClassName(input);
        // Return attributes
        HashMap<String,String> attributes = returnAttributes(input);



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
        if(className != null){ el.setClassName(className); }
        // Set attributes
        el.setAttributes(attributes);


        return el;
    }

    // Converts the hamlDataElements array of a Haml object to
    // a nested array of HamlDataElements usable for the HtmlConverter
    //
    static HamlData nestHamlDataElements(HamlData hamlData){
        ArrayList<HamlDataElement> originalElements = hamlData.getHamlDataElements();
        ArrayList<HamlDataElement> nestedElements = new ArrayList<>();

        ArrayList<HamlDataElement> parents = new ArrayList<>();
        HamlDataElement parentEl = null;
        HamlDataElement currEl = null;
        int currDepth = 0;
        int depthChange = 0;

        for(int i = 0; i < originalElements.size(); i++){
            currEl = originalElements.get(i);

            if (currEl.getDepth() == currDepth) {

                // If no parent element existed, the current one is the parent, otherwise the parent remains
                parentEl = parentEl == null ? currEl : parentEl;
                // Add the parent to the parents list
                if(parentEl == null){ parents.add(currEl); }

                // If currEl.getDepth() == 0 => Add element to root level of nestedElements
                // Else => It needs to be added to its parent
                if(currEl.getDepth() == 0){
                    nestedElements.add(currEl);
                }else{
                    parents.get(parents.size()-1).addChild(currEl);
                }

            }

            if (currEl.getDepth() == currDepth + 1) {

                // The parent element is the previous hamlDataElement
                parentEl = originalElements.get(i-1);
                // Add it to the parents list
                parents.add(parentEl);

                // Add current element as a child to its parent
                parentEl.addChild(currEl);

                // Raise the current depth level
                currDepth++;
            }

            if (currEl.getDepth() < currDepth) {
                // check how many levels the current element went up
                depthChange = parents.size() - currEl.getDepth();

                // Remove the deeper elements from parents list
                for(int j = parents.size(); j > currEl.getDepth(); j--){
                    parents.remove(j-1);
                }

                // Add the current element to its parent
                parents.get(parents.size()-1).addChild(currEl);

                // Change the current depth with the amount of levels it went up
                currDepth = currDepth - depthChange;
            }

        }

        hamlData.hamlDataElements = nestedElements;
        return hamlData;
    }

}
