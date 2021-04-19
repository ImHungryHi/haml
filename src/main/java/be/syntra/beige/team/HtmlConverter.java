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

public class HtmlConverter {
    // Small performance enhancement - keep the indent level in memory in case we need it again.
    // Default indentation level 0 = ""
    private static final ArrayList<String> INDENTS = new ArrayList<String>() {{ add(""); }};

    // To html methods
    //
    private static boolean isEmptyTag(HamlDataElement el) {
        return Config.EMPTY_TAGS.contains(el.getTagName());
    }

    private static String createIndentation(HamlDataElement el) {
        int depth = el.getDepth();

        if (depth > 0) {
            // No need to recreate existing indents
            if (INDENTS.size() > depth) {
                return INDENTS.get(depth);
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

        return INDENTS.get(depth);
    }

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
            String textContent = el.getTextContent();

            if (textContent.contains("\n")) {
                String indents = createIndentation(el);
                textContent = textContent.replace("\n", indents + Config.INDENTATION + "\n");   // Fixes indentation for new lines
                output += "\n" +
                        indents + Config.INDENTATION + textContent + "\n" +
                        indents;    // Fixes indentation for closure after text
            }
            else {
                output += textContent;
            }
        }

        return output;
    }

    private static String createCommentContent(HamlDataElement el) {
        // No haml comments allowed
        if (el.getCommentType().toLowerCase().contains("haml")) {
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

    private static void addCommentContent(HamlDataElement el, Html html) {
        String indents = createIndentation(el);

        if (el.getChildren().size() < 1) {
            // No children, so if there's no text then there's no comment
            html.addElement(createCommentContent(el));
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

    public static void convertToHtml(HamlDataElement el, Html html) {
        if (!el.isTag()) {
            addCommentContent(el, html);
            return;
        }

        String indents = createIndentation(el);
        String output = indents + createBeginTag(el);
        ArrayList<HamlDataElement> children = el.getChildren();

        if (children == null || children.size() < 1) {
            output += createTextContent(el);
            output += createEndTag(el);
            html.addElement(output);
            return;
        }

        // We'll get here only if there are child elements that require a new line
        if (el.hasText()) {
            String elText = createTextContent(el);

            if (!elText.contains("\n")) {
                output += "\n" + createIndentation(el) + Config.INDENTATION;
            }

            output += elText;
        }

        // Finally, write the begin tag and text to the html list
        html.addElement(output);

        // Start parsing children
        for (HamlDataElement child : children) {
            convertToHtml(child, html);
        }

        // Close off this element's tag
        html.addElement(((el.getChildren().size() > 0 || output.contains("\n")) ? indents : "") + createEndTag(el));
    }

    public static void convertToHtml(ArrayList<HamlDataElement> elements, Html html) {
        // Let's start parsing from the top - ie the html tag
        for (HamlDataElement el : elements) {
            if (el.getDepth() == 0) {
                convertToHtml(el, html);
            }
        }
    }
}
