package be.syntra.beige.team;

import java.util.ArrayList;

public class HConverterWork {
    private static String createBeginTag(HamlDataElement el) { return ""; }
    private static String createTextContent(HamlDataElement el) { return ""; }
    private static void addCommentContent(HamlDataElement el, Html html) { }
    private static String createEndTag(HamlDataElement el) { return ""; }
    private static String createIndentation(HamlDataElement el) { return ""; }
    // Here endeth the dummy content

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
        if (parent != null && parent.hasWhiteSpaceRemoval() && parent.getWhiteSpaceRemovalType().contains("<") ||
            previousSibling != null && previousSibling.hasWhiteSpaceRemoval() && previousSibling.getWhiteSpaceRemovalType().contains(">")) {
            html.addToPrevious(beginTag);
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

        HamlDataElement previousSibling = null;
        boolean isText = false;

        if (!el.isTag() && !el.isComment()) {
            isText = true;

            if (el.getParent() != null) {
                // Check for previous siblings
                ArrayList<HamlDataElement> siblings = el.getParent().getChildren();

                for (int x = 0; x < siblings.size(); x++) {
                    if (x > 0 && siblings.get(x).equals(el)) {
                        previousSibling = siblings.get(x - 1);
                    }
                }
            }
        }

        // Normal situation for single line text. WSRM doesn't do anything different in this case.
        if (!el.hasChildren() && !textContent.contains("\n")) {
            html.addToPrevious(textContent);
        }
        // First line is no children + multi-line text + inner WSRM
        //  Second is children + both single-/multi-line text + inner WSRM
        //  Third is the previous sibling - if found - with outer WSRM
        else if ((!el.hasChildren() && textContent.contains("\n") && (el.hasWhiteSpaceRemoval() && el.getWhiteSpaceRemovalType().contains("<"))) ||
                    (el.hasChildren() && (el.hasWhiteSpaceRemoval() && el.getWhiteSpaceRemovalType().contains("<"))) ||
                    (previousSibling != null && previousSibling.hasWhiteSpaceRemoval() && previousSibling.getWhiteSpaceRemovalType().contains(">"))) {
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
        HamlDataElement lastChild = children.get(children.size() - 1);

        // First line checks for content within the tag that indicates a multi-line tag
        //  The second line checks for WSRM in the last child that forces the closing tag upwards (">")
        //  The third line checks for WSRM inside the tag itself ("<")
        if ((!el.hasChildren() && !el.hasText() && !el.getTextContent().contains("\n")) ||
                (el.hasChildren() && lastChild.hasWhiteSpaceRemoval() && lastChild.getWhiteSpaceRemovalType().contains(">")) ||
                (el.hasWhiteSpaceRemoval() && el.getWhiteSpaceRemovalType().contains("<"))) {
            html.addToPrevious(endTag);
            return;
        }

        html.addElement(createIndentation(el) + endTag);
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
