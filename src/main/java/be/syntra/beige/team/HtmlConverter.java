package be.syntra.beige.team;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <h1>HtmlConverter</h1>
 * HtmlConverter class contains all methods to convert a HamlDataElement to a htmlElement list item.
 * HtmlConverter receives HamlDataElements list in App. Converts them to a htmlElement list item
 * and adds them to the Html object.
 * <p>
 * To be added:
 * list of all methods
 *
 *
 * @author  Team Beige
 * @version 1.0
 * @since   2021-03-24
 */

public class HtmlConverter {

    // To html methods
    //
    private static boolean isEmptyTag(HamlDataElement el){
        return Config.EMPTY_TAGS.contains(el.getTagName());
    }

    private static String createIndentation(HamlDataElement el){
        String indent = "";
        int depth = el.getDepth();
        if(depth != 0){
            for(int i = 1; i <= el.getDepth(); i++){
                indent += Config.INDENTATION;
            }
        }
        return indent;
    }

    private static String createBeginTag(HamlDataElement el){
        String beginTag = "";
        if(el.isTag()){
            beginTag = "<" + el.getTagName();
            if(el.getId() != null){
                beginTag += " id='" + el.getId() + "'";
            }
            beginTag += ">";
        }
        return beginTag;
    }

    private static String createEndTag(HamlDataElement el){
        String endTag = "";
        if(el.isTag() && !isEmptyTag(el)){
            endTag = "</" + el.getTagName() + ">";
        }
        return endTag;
    }

    public static void convertToHtml(ArrayList<HamlDataElement> elements, Html html){
        String htmlElement = "";
        ArrayList<String> htmlElements = new ArrayList<String>();

        HamlDataElement currEl = null;
        HamlDataElement prevEl = null;
        HamlDataElement nextEl = null;
        ArrayList<HamlDataElement> parents = new ArrayList<>();

        HashMap<Integer,Boolean> prevOutwardWsrm = new HashMap<Integer,Boolean>();

        for(int i = 0; i < elements.size(); i++){

            // Set previous, current, next & parent HamlDataElements
            //
            currEl = elements.get(i);
            prevEl = i != 0 ? elements.get(i-1) : null;
            nextEl = i != elements.size() - 1 ? elements.get(i+1) : null;
            // When going a level deeper, store the parent
            if(prevEl != null && currEl.getDepth() > prevEl.getDepth()){
                parents.add(prevEl);
            }


            // Create indentation + beginTag
            // Create newline (\n), taken whitespaceremoval symbols into account
            if(i != 0 &&
                    !(prevEl.getWhiteSpaceRemovalType().contains("<") || prevEl.getWhiteSpaceRemovalType().contains(">")) &&
                    !currEl.getWhiteSpaceRemovalType().contains(">")
                // Todo
                // Still needs an extra check here:
                // the first preceding element of the same depth as currEl, cannot have a WhiteSpaceRemovalType '>'
                // see last whitespaceremoval case in example.haml file
            )
            {
                htmlElement = "\n" + createIndentation(currEl) + createBeginTag(currEl);
            }else{
                htmlElement = createBeginTag(currEl);
            }


            // Closing tag logic
            //
            if(nextEl != null && nextEl.getDepth() <= currEl.getDepth()){
                // Close current element when next element is same level or higher
                htmlElement += createEndTag(currEl);

                // When there are parent tags, close those that are deeper then next element
                for(int j = parents.size(); j > nextEl.getDepth(); j--){
                    htmlElement += "\n";
                    htmlElement += createIndentation(parents.get(j-1)) + createEndTag(parents.get(j-1));
                    parents.remove(j-1);
                }
            }

            // Close remaining tags for the last element
            if(i == elements.size() - 1){
                for(int k = parents.size()-1; k >= 0; k--){
                    htmlElement += "\n" + createIndentation(parents.get(k)) + createEndTag(parents.get(k));
                    parents.remove(k);
                }
            }



            // Add html line to html output array
            htmlElements.add(htmlElement);
        }

        html.setHtmlElements(htmlElements);

    }

}
