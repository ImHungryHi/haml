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
    // To html methods
    //
    private static boolean isEmptyTag(HamlDataElement el) {
        return Config.EMPTY_TAGS.contains(el.getTagName());
    }

    private static String createIndentation(HamlDataElement el) {
        String indent = "";
        int depth = el.getDepth();

        if (depth != 0) {
            for (int i = 1; i <= el.getDepth(); i++) {
                indent += Config.INDENTATION;
            }
        }

        return indent;
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
        String indents = createIndentation(el);
        String commentContent = el.getCommentContent();

        if (commentContent != null) {
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

    public static void convertToHtml(HamlDataElement el, Html html) {
        if (!el.isTag()) {
            String commentContent = createCommentContent(el);

            if (commentContent != null) {
                html.addElement(commentContent);
            }

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
        html.addElement(createEndTag(el));
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
