package be.syntra.beige.team;

import java.util.*;

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

// TODO: create escape function (check for "\\" at the start and escape into html entities)

public class HtmlConverter {
    // Small performance enhancement - keep the indent level in memory in case we need it again.
    // Default indentation level 0 = ""
    private static final ArrayList<String> INDENTS = new ArrayList<String>() {{ add(""); }};
    private static boolean triggerNextWSRM = false;

    /**
     * ----------------------------------
     * ***   Main convert functions   ***
     * ----------------------------------
     */

    /**
     * Recursive function to loop through each root element's Haml tree
     * @param el Current handled HamlDataElement in the working tree; container of parent, current and children element information
     * @param html HTML information container - hosts HTML element strings and file paths
     */
    public static void convertToHtml(HamlDataElement el, Html html) {
        // Skip haml comments, don't even parse its children since they are also haml comments.
        if (el.getCommentType() != null && el.getCommentType().toLowerCase().contains("haml")) {
            return;
        }
        if (!el.isTag() && el.isComment()) {
            addCommentContent(el, html);
            return;
        }

        addBeginTag(el, html);

        if (el.hasText() || (!el.isTag() && !el.isComment())) {
            addTagContent(el, html);
        }

        ArrayList<HamlDataElement> children = el.getChildren();

        if (children != null) {
            // Shouldn't be iterated if there are none (0 < 0 => false => skip)
            for (int x = 0; x < children.size(); x++) {
                convertToHtml(children.get(x), html);
            }
        }

        addEndTag(el, html);
    }

    /**
     * Starter function - will loop through every top level Haml element and parse it into its own tree
     * @param elements ArrayList of Haml Data Elements and their own hierarchy
     * @param html HTML information container - hosts HTML element strings and file paths
     */
    public static void convertToHtml(ArrayList<HamlDataElement> elements, Html html) {
        // Let's start parsing from the top - ie the html tag
        for (HamlDataElement el : elements) {
            if (el.getDepth() == 0) {
                convertToHtml(el, html);
            }
        }
    }

    /**
     * ----------------------------------
     * ***        Add functions       ***
     * ----------------------------------
     */

    private static void addCommentContent(HamlDataElement el, Html html) {
        String indents = createIndentation(el);

        if (el.getChildren().size() < 1) {
            // No children, so if there's no text then there's no comment
            html.addElement(indents + createCommentContent(el));
        }
        else {
            // TODO: if we replace commentContent for textContent, these will have to change here - or in getCommentContent()
            String commentContent = el.getCommentContent();
            html.addElement(indents + "<!--");

            if (commentContent != null && !commentContent.isEmpty()) {
                html.addElement(indents + Config.INDENTATION + commentContent);
            }

            // There are children to this element, which means we can parse with convertToHtml until the end of the list
            for (HamlDataElement child : el.getChildren()) {
                convertToHtml(child, html);
            }

            html.addElement(indents + "-->");
        }
    }

    private static void addBeginTag(HamlDataElement el, Html html) {
        String beginTag = createBeginTag(el);

        if (beginTag.equals("")) {
            return;
        }

        HamlDataElement parent = el.getParent();
        HamlDataElement previousSibling = null;

        if (parent != null) {
            ArrayList<HamlDataElement> siblings = el.getParent().getChildren();

            for (int x = 0; x < siblings.size(); x++) {
                if (x > 0 && siblings.get(x).equals(el)) {
                    previousSibling = siblings.get(x - 1);
                }
            }
        }

        // First line checks for parent WSRM
        //  Second line checks for previous sibling WSRM
        //  Third line is a special case - multiple depth 0 elements with WSRM
        if (parent != null && parent.hasWhiteSpaceRemoval() && parent.getWhiteSpaceRemovalType().contains("<") ||
                previousSibling != null && previousSibling.hasWhiteSpaceRemoval() && previousSibling.getWhiteSpaceRemovalType().contains(">") ||
                html.getHtmlElements().size() > 0 && el.hasWhiteSpaceRemoval() && el.getWhiteSpaceRemovalType().contains(">") || triggerNextWSRM) {
            html.addToPrevious(beginTag);

            if (html.getHtmlElements().size() > 0 && el.hasWhiteSpaceRemoval() && el.getWhiteSpaceRemovalType().contains(">")) {
                triggerNextWSRM = true;
            }
            else if (triggerNextWSRM) {
                triggerNextWSRM = false;
            }
        }
        else {
            html.addElement(createIndentation(el) + beginTag);
        }
    }

    private static void addTagContent(HamlDataElement el, Html html) {
        String textContent = createTextContent(el);

        // What do we say to the god of empty lines? Not today!
        if (textContent.equals("")) {
            return;
        }

        ArrayList<HamlDataElement> siblings = null;
        HamlDataElement previousSibling = null;
        boolean isText = false;

        if (!el.isTag() && !el.isComment()) {
            isText = true;

            if (el.getParent() != null) {
                // Check for previous siblings
                siblings = el.getParent().getChildren();

                for (int x = 0; x < siblings.size(); x++) {
                    if (x > 0 && siblings.get(x).equals(el)) {
                        previousSibling = siblings.get(x - 1);
                    }
                }
            }
        }

        boolean hasChildren = el.hasChildren(),
                hasNewLine = textContent.contains("\n"),
                hasInnerWSRM = el.hasWhiteSpaceRemoval() && el.getWhiteSpaceRemovalType().contains("<"),
                siblingHasOuterWSRM = previousSibling != null && previousSibling.hasWhiteSpaceRemoval() && previousSibling.getWhiteSpaceRemovalType().contains(">");

        if ((el.hasText() || isText) && textContent.contains("header")) {
            boolean stophere = true;
        }

        // Normal situation for single line text. WSRM doesn't do anything different in this case.
        if (!hasChildren && el.isTag() && !hasNewLine ||
            isText && siblings != null && siblings.size() > 1 && previousSibling == null) {
            html.addToPrevious(textContent);
        }
        // First line is no children + multi-line text + inner WSRM
        //  Second is children + both single-/multi-line text + inner WSRM
        //  Third is the previous sibling - if found - with outer WSRM
        else if ((!hasChildren && hasNewLine && hasInnerWSRM) ||
                (el.hasChildren() && hasInnerWSRM) ||
                siblingHasOuterWSRM) {
            html.addToPrevious(textContent.replace("\n", "\n" + createIndentation(el)));
        }
        else {
            String indents = createIndentation(el) + (!isText ? Config.INDENTATION : "");
            html.addElement(indents + textContent.replace("\n", "\n" + indents));
        }
    }

    private static void addEndTag(HamlDataElement el, Html html) {
        String endTag = createEndTag(el);

        // Prevent the code from adding empty lines
        if (endTag.equals("")) {
            return;
        }

        ArrayList<HamlDataElement> children = el.getChildren();
        HamlDataElement lastChild = null;

        if (el.hasChildren()) {
            lastChild = children.get(children.size() - 1);
        }

        boolean hasChildren = el.hasChildren(),
                childHasWSRM = lastChild != null && lastChild.hasWhiteSpaceRemoval(),
                childHasOuterWSRM = childHasWSRM && lastChild.getWhiteSpaceRemovalType().contains(">"),
                hasInnerWSRM = el.hasWhiteSpaceRemoval() && el.getWhiteSpaceRemovalType().contains("<");

        // First line checks for content within the tag that indicates a multi-line text
        //  The second line checks for WSRM in the last child that forces the closing tag upwards (">")
        //  The third line checks for WSRM inside the tag itself ("<")
        if ((!hasChildren && el.isTag() && el.hasText() && !el.getTextContent().contains("\n")) ||
                (hasChildren && childHasWSRM && childHasOuterWSRM) ||
                hasInnerWSRM) {
            html.addToPrevious(endTag);
            return;
        }

        html.addElement(createIndentation(el) + endTag);
    }

    /**
     * ----------------------------------
     * ***   Parse/create functions   ***
     * ----------------------------------
     */

    private static String createBeginTag(HamlDataElement el) {
        String beginTag = "";

        if (el.isTag()) {
            beginTag = "<" + el.getTagName();

            if (el.getId() != null) {
                beginTag += " id='" + el.getId() + "'";
            }

            if (el.getClassName() != null) {
                beginTag += " class='" + el.getClassName() + "'";
            }

            HashMap<String, String> attributes = el.getAttributes();

            if (attributes != null && !attributes.isEmpty()) {
                for (Map.Entry<String, String> entry : el.getAttributes().entrySet()) {
                    beginTag += " " + entry.getKey() + "='" + entry.getValue() + "'";
                }
            }

            beginTag += ">";
        }

        return beginTag;
    }

    private static String createEndTag(HamlDataElement el) {
        String endTag = "";

        if (el.isTag() && !isEmptyTag(el)) {
            endTag = "</" + el.getTagName() + ">";
        }

        return endTag;
    }

    private static String createTextContent(HamlDataElement el) {
        String output = "";

        if (!isEmptyTag(el) && el.hasText()) {
            String textContent = el.getTextContent().replace("\"", "").replace("\\n", "\n");  // TODO: this might need to be checked inside hamlconverter

            if (textContent.contains("\n")) {
                String indents = createIndentation(el);

                if (el.getWhiteSpaceRemovalType() != null && el.getWhiteSpaceRemovalType().equals("<")) {
                    textContent = textContent.replace("\n", "\n" + indents);   // Fixes indentation for new lines
                    output += textContent;
                }
                else {
                    textContent = textContent.replace("\n", "\n" + indents + Config.INDENTATION);   // Fixes indentation for new lines
                    output += "\n" +
                            indents + Config.INDENTATION + textContent + "\n" +
                            indents;    // Fixes indentation for closure after text
                }
            }
            else {
                output += textContent;
            }
        }

        return output;
    }

    private static String createCommentContent(HamlDataElement el) {
        // No haml comments allowed
        if (el.getCommentType() != null && el.getCommentType().toLowerCase().contains("haml")) {
            return null;
        }

        String indents = createIndentation(el);
        String commentContent = el.getCommentContent();
        // TODO: if we replace commentContent for textContent, these will have to change here - or in getCommentContent()

        if (commentContent != null && !commentContent.isEmpty()) {
            if (commentContent.contains("\n")){
                return "<!--\n" +
                        indents + Config.INDENTATION + commentContent.replace("\n", indents + Config.INDENTATION + "\n") + "\n" +
                        "-->";
            }
            else{
                return "<!-- " + commentContent + " -->";
            }
        }

        return null;
    }

    private static String createIndentation(HamlDataElement el) {
        int depth = el.getDepth();
        int countWSRM = countAncestralWSRM(el);

        if (countWSRM == 1 && el.hasWhiteSpaceRemoval()) {
            countWSRM--;
        }

        if (depth > 0) {
            // No need to recreate existing indents
            if (INDENTS.size() > depth) {
                return INDENTS.get(depth - Math.min(countWSRM, depth));
            }

            //  Size will always be 1 increment larger than the last index (which is equal to depth)
            //  If the difference in requested depth oversteps this gap, add additional indents inbetween
            //    eg: 2 indents of 2 spaces -> INDENTS = { "", "  ", "    " }
            //        add depth 5: we will also add 3 and 4, followed by 5 indents
            for (int x = INDENTS.size(); x <= depth; x++) {
                // x - 1 will never be below 0 since we checked in the top "if"
                INDENTS.add(INDENTS.get(x - 1) + Config.INDENTATION);
            }
        }

        return INDENTS.get(depth - Math.min(countWSRM, depth));
    }

    /**
     * ----------------------------------
     * ***      Helper functions      ***
     * ----------------------------------
     */

    private static int countAncestralWSRM(HamlDataElement el) {
        if (el.getParent() == null) {
            return el.hasWhiteSpaceRemoval() ? 1 : 0;
        }

        return countAncestralWSRM(el.getParent()) + (el.hasWhiteSpaceRemoval() ? 1 : 0);
    }

    private static boolean isEmptyTag(HamlDataElement el) {
        return Config.EMPTY_TAGS.contains(el.getTagName());
    }
}