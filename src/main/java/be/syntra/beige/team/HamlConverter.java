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

///s
    private static boolean flagComment;
    private static int depthVanComment;
///s

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

            //zijn deze goed (kijk 66. line)
            while ((pos + Config.LENGTH_INDENTATION < input.length()) && input.substring(pos, pos + Config.LENGTH_INDENTATION).equals(Config.INDENTATION)) {
                depth++;
                pos += Config.LENGTH_INDENTATION;
            }
            //ipv beneden
            /*while ((pos + 2 < input.length()) && input.substring(pos, pos + 2).equals(Config.INDENTATION)) {
                depth++;
                pos += 2;
            }
             */
        }
        return depth;
    }

///a
    // Helper method to return a haml line, ignoring the indentation
    //
    public static String inputZeroDepthForm(String input){
        return input.substring(returnDepth(input)*2);
    }

    //helper method for comment blok finished or not yet..
    public static void flag(String input){
        flagComment=returnIsComment(input);
    }
///a

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
        if (!flagComment) {
            if ((!returnIsTag(input) && (!returnIsComment(input)) || (returnIsTag(input) && inputZeroDepthForm(input).split(" ").length > 1))) {
                hasText = true;
            }
            //return hasText;
        }
        return hasText;
    }

    // Return the actual text content found in the line
    //
    public static String returnTextContent(String input) {
        String textContent = "";
        if (returnHasText(input) && !returnIsTag(input)) {

            //input (zonder ruimtes van depth) is text.
            textContent += inputZeroDepthForm(input);

        } else if (returnHasText(input) && returnIsTag(input)) {

            //in dit geval input heeft ruimtes;
            //                                  - in depth
            //                                  - in text
            //                                  - net voor hele text
            // text[] heeft het stukje die is tot "text" in index 0 en split form van text( word op word) in de volgende indexes.
            String[] text = inputZeroDepthForm(input).split(" ");

            textContent += input.substring((returnDepth(input) * 2) + (text[0].length()) + 1);
        }
        return textContent;
    }

    // Return whether a comment was found in line
    //
    public static boolean returnIsComment(String input) {
        boolean isComment = false;
        //input = inputZeroDepthForm(input);
        String input_Z_D_F = inputZeroDepthForm(input);

        //if (input.startsWith("/") || input.startsWith("-#")) {
        if (input_Z_D_F.startsWith("/") || input_Z_D_F.startsWith("-#")) {
            isComment = true;
            depthVanComment=returnDepth(input);
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
       /* String result="";
        if (returnIsComment(input)) {
            input=inputZeroDepthForm(input);
            result="b";
           // String naCommentCharacterEnSpace=input.substring(input.indexOf(" ",0));
            if (input.contains("\\p{print}")) {
                result= input.substring(input.indexOf(' ')+1);
            }
        }
        return result;
        */
        String commentContent="";
        /*if (flagComment){ //onceki satir halihazirda coklu yorum satiri miydi?
            if (depthVanComment>=returnDepth(input)){
                return "";
            }
        } else {*/
            flag(input);
            if ((flagComment && returnCommentType(input).equals("htmlComment")) && !inputZeroDepthForm(input).equals("/")){ //bu satirda yalniz "/" karakteri yoksa
                                                                                                                            //yani tek satirlik yorum ise
                flagComment=false;
                String[] comment = inputZeroDepthForm(input).split(" ");
                commentContent += inputZeroDepthForm(input).substring(comment[0].length()+1);
                return commentContent;
            } else if ((flagComment && returnCommentType(input).equals("htmlComment")) && inputZeroDepthForm(input).equals("/")){//çok satirli yorum basliyor
                if (depthVanComment<returnDepth(input)){
                    return input; //OLMAZ!!!! Normal islem yapmali. baska bir flag gerekebilir.
                } else{

                }
                return "";
            }
        //}  -->else' in kapanisi
        return "";
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



    // Todo: returnHasEscaping method here (boolean)
    // Return if escape symbol was found in line
    //

    // Todo: returnEscapedContent method here (String)
    // Return escaped content found in line
    //

    // Todo: returnClassNames method here (String)
    // Return classNames
    //
    // Still needs improving: 1.probleem: ".class1.class2"; 2. probleem:"a.jpg" .....
    public static String returnClassName(String input) {
        String className = null;
        int startPos;
        int endPos = -1;
        int currPos;
        String currChar = "";

        // If the haml line represents a tag and . occurs, get the first occurrence of . and retrieve the className
        if (returnIsTag(input) && input.contains(".")) {
            // get start position of class name
            startPos = input.indexOf(".") + 1;
            // get end position of class name, can end by a ' ','#','(','<','>' or end of line (returns -1 in this case)
            for (int i = startPos; i < input.length(); i++) {
                currChar = input.substring(i, i + 1);
                if (currChar.equals(" ") || currChar.equals("#") || currChar.equals("(") || currChar.equals("<") || currChar.equals(">")) {
                    endPos = i;
                    break;
                }
            }
            className = (endPos != -1) ? input.substring(startPos, endPos) : input.substring(startPos);
        }
        return className;
    }


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
        boolean hasText = returnHasText(input);//false; // Todo: call returnHasText method here     OK
        // Return the actual text content found in the line
        String textContent = returnTextContent(input);//""; // Todo: call returnTextContent method here     OK
        // Return whether comment was found in line
        boolean isComment = returnIsComment(input);//false; // Todo: call returnIsComment method here       OK
        // Return the commenttype found in line (htmlComment or hamlComment)
        String commentType = returnCommentType(input); // Todo: call returnCommentType method here      OK
        // Return the comment content
        String commentContent = returnCommentContent(input); //""; // Todo: call returnCommentContent method here       OK
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
        String className = returnClassName(input); //""; // Todo: call returnClassNames method here
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
        if(className != null){ el.setClassName(className); }
        // Set attributes
        // Todo: use setter method 'setAttributes'



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

                    // System.out.print(currEl.getLineNumber() + " " + currEl.getDepth() + " I'm same level");

                    /*if(currEl.getDepth() != 0){
                        System.out.println("=> my parent is on line " + parents.get(parents.size()-1).getLineNumber());
                    }else{
                        System.out.print("\n");
                    }*/

                // If currEl.getDepth() == 0 => Add element to root level of nestedElements
                // Else => It needs to be added to its parent
                if(currEl.getDepth() == 0){
                    nestedElements.add(currEl);
                }else{
                    parents.get(parents.size()-1).addChild(currEl);
                }



            }

            if (currEl.getDepth() == currDepth + 1) {
                    // System.out.print(currEl.getLineNumber() + " " + currEl.getDepth() + " I'm level deeper");

                // The parent element is the previous hamlDataElement
                parentEl = originalElements.get(i-1);
                // Add it to the parents list
                parents.add(parentEl);
                    //System.out.println("=> my parent is on line " + parentEl.getLineNumber());

                // Add current element as a child to its parent
                parentEl.addChild(currEl);

                // Raise the current depth level
                currDepth++;
            }

            if (currEl.getDepth() < currDepth) {
                // check how many levels the current element went up
                depthChange = parents.size() - currEl.getDepth();
                    //System.out.println(depthChange);

                // Remove the deeper elements from parents list
                for(int j = parents.size(); j > currEl.getDepth(); j--){
                    parents.remove(j-1);
                }

                    // System.out.print(currEl.getLineNumber() + " " + currEl.getDepth() + " I'm level higher");
                    /* System.out.println("=> my parent is on line " +
                            parents.get(parents.size()-1).getLineNumber()
                    ); */

                // Add the current element to its parent
                parents.get(parents.size()-1).addChild(currEl);

                // Change the current depth with the amount of levels it went up
                currDepth = currDepth - depthChange;
            }

        }

        hamlData.hamlDataElements = nestedElements; //null;
        return hamlData;
    }

}
