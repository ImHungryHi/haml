package be.syntra.beige.team;

import java.util.ArrayList;

public class HConverterWork {
    private static String createBeginTag(HamlDataElement el) { return ""; }
    private static String createTextContent(HamlDataElement el) { return ""; }
    private static void addCommentContent(HamlDataElement el, Html html) { }
    private static String createEndTag(HamlDataElement el) { return ""; }
    // Here endeth the dummy content

    private static void addBeginTag(HamlDataElement el, Html html) {
        String beginTag = createBeginTag(el);

        /*
        TODO:
         whitespace check
            -> parent has inner / previous sibling has outer wsrm
                => add to previous element
         add as per usual
         */
    }

    private static void addTagContent(HamlDataElement el, Html html) {
        String textContent = createTextContent(el);

        /*
        TODO:
         whitespace check
            -> parent has inner / previous sibling has outer wsrm
                => add to previous element
         single line check
                => add to previous element
         add as per usual
         */
    }

    private static void addEndTag(HamlDataElement el, Html html) {
        String endTag = createEndTag(el);

        /*
        TODO:
         whitespace check
            -> last child has outer wsrm
                => add to previous element
         single line check
                => add to previous element
         add as per usual
         */
    }

    public static void convertToHtml(HamlDataElement el, Html html) {
        // Skip haml comments, don't even parse its children since they are also haml comments.
        if (el.getCommentType() != null && el.getCommentType().toLowerCase().contains("haml")) {
            return;
        }
        if (!el.isTag() && el.isComment()) {
            addCommentContent(el, html);
            return;
        }

        if (el.isTag()) {
            addBeginTag(el, html);
        }

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

        if (el.isTag()) {
            addEndTag(el, html);
        }
    }

    /*
    public static void convertToHtml(HamlDataElement el, Html html) {
        // Skip haml comments, don't even parse its children since they are also haml comments.
        if (el.getCommentType() != null && el.getCommentType().toLowerCase().contains("haml")) {
            return;
        }

        if (!el.isTag() && el.isComment()) {
            addCommentContent(el, html);
            return;
        }
        else if (!el.isTag() && !el.isComment()) {
            // isText = true
            String textIndents = createIndentation(el);
            String textElement = el.getTextContent().replace("\n", "\n" + textIndents);
            html.addElement(textIndents + textElement);

            // technically this is not allowed to have children - but we could implement setting the children back a level
            //  maybe something we could do in the future; for now, let's return
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
    }*/
}
