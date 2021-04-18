package be.syntra.beige.team;

import junit.framework.TestCase;

import java.util.ArrayList;

public class HtmlConverterTest extends TestCase {
    public void testConvertToHtml() {
        HamlData hd = new HamlData("testinput.haml", "testoutput.html");
        Html html = new Html(hd.getInputFileName(), hd.getOutputFileName());
        hd.hamlDataElements = getTestHamlElements();
        HtmlConverter.convertToHtml(hd.getHamlDataElements(), html);

        for (String line : html.getHtmlElements()) {
            System.out.println(line);
        }
    }

    private ArrayList<HamlDataElement> getTestHamlElements() {
        ArrayList<HamlDataElement> elements = new ArrayList<>();

        HamlDataElement html = new HamlDataElement(0,
                0,
                true,
                "html",
                false,
                null,
                false,
                null,
                null,
                false,
                null,
                false,
                null);
        HamlDataElement head = new HamlDataElement(1, 1, true, "head", false, null, false, null, null, false, null, false, null);
        HamlDataElement title = new HamlDataElement(2, 2, true, "title", true, "Page title", false, null, null, false, null, false, null);
        HamlDataElement body = new HamlDataElement(3, 1, true, "body", false, null, false, null, null, false, null, false, null);
        HamlDataElement h1 = new HamlDataElement(4, 2, true, "h1", true, "Page header", false, null, null, false, null, false, null);
        HamlDataElement commentBlock = new HamlDataElement(5, 3, false, null, false, null, true, "html", "An example:", false, null, false, null);
        HamlDataElement commentBlockHeader = new HamlDataElement(6, 4, true, "h1", true, "Example header", true, "html", null, false, null, false, null);
        HamlDataElement commentBlockParagraph = new HamlDataElement(7, 4, true, "p", true, "A nested button link below:", true, "html", null, false, null, false, null);
        HamlDataElement commentBlockAnchor = new HamlDataElement(8, 4, true, "a", false, null, true, "html", null, false, null, false, null);
        HamlDataElement commentBlockImg = new HamlDataElement(9, 5, true, "img", false, null, true, "html", null, false, null, false, null);
        HamlDataElement section = new HamlDataElement(10, 2, true, "section", false, null, false, null, null, false, null, false, null);

        for (int x = 0; x < 10; x++) {
            HamlDataElement article = new HamlDataElement(7 + ((x + 1) * 4), 3, true, "article", false, null, false, null, null, false, null, false, null);
            HamlDataElement articleTitle = new HamlDataElement(8 + ((x + 1) * 4), 4, true, "h2", true, "Article title " + x, false, null, null, false, null, false, null);
            HamlDataElement articleDescription = new HamlDataElement(9 + ((x + 1) * 4), 4, true, "p", true, "Article description " + x, false, null, null, false, null, false, null);
            HamlDataElement articleLink = new HamlDataElement(10 + ((x + 1) * 4), 4, true, "a", true, "Add item to basket", false, null, null, false, null, false, null);

            article.addChild(articleTitle);
            article.addChild(articleDescription);
            article.addChild(articleLink);
            section.addChild(article);

            elements.add(article);
            elements.add(articleTitle);
            elements.add(articleDescription);
            elements.add(articleLink);
        }

        commentBlock.addChild(commentBlockHeader);
        commentBlock.addChild(commentBlockParagraph);
        commentBlock.addChild(commentBlockAnchor);
        commentBlockAnchor.addChild(commentBlockImg);
        h1.addChild(commentBlock);
        body.addChild(h1);
        body.addChild(section);
        head.addChild(title);
        html.addChild(head);
        html.addChild(body);

        elements.add(html);
        elements.add(head);
        elements.add(title);
        elements.add(body);
        elements.add(h1);
        elements.add(commentBlock);
        elements.add(commentBlockHeader);
        elements.add(commentBlockParagraph);
        elements.add(commentBlockAnchor);
        elements.add(commentBlockImg);

        return elements;
    }
}