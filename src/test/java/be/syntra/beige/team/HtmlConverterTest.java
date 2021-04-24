package be.syntra.beige.team;

import org.junit.Test;
import junit.framework.TestCase;

import java.util.ArrayList;

public class HtmlConverterTest extends TestCase {
    @Test
    public void testConvertToHtml() {
        HamlData hd = new HamlData("testinput.haml", "testoutput.html");
        Html html = new Html(hd.getInputFileName(), hd.getOutputFileName());
        hd.hamlDataElements = getTestHamlElements();
        HtmlConverter.convertToHtml(hd.getHamlDataElements(), html);

        assertEquals(getHtmlTestOutput(), html.getHtmlElements());
    }

    @Test
    public void testEscaping() {
        String knownEscapes = "\\ &€\"' -<img>";
        final HamlDataElement el = new HamlDataElement(1, 0, true, "p", true, knownEscapes, false, null, null, false, null, false, null);
        final HamlDataElement normal = new HamlDataElement(1, 0, true, "p", true, "This is just some text, nothing special here", false, null, null, false, null, false, null);
        ArrayList<HamlDataElement> list = new ArrayList<HamlDataElement>() {{
            add(el);
            add(normal);
        }};
        ArrayList<String> output = new ArrayList<String>() {{
            add("<p>&amp;&euro;&quot;&apos;&nbsp;&ndash;&lt;img&gt;</p>");
            add("<p>This is just some text, nothing special here</p>");
        }};

        HamlData hd = new HamlData("testinput.haml", "testoutput.html");
        Html html = new Html(hd.getInputFileName(), hd.getOutputFileName());
        hd.hamlDataElements = list;
        HtmlConverter.convertToHtml(hd.getHamlDataElements(), html);

        assertEquals(output, html.getHtmlElements());
    }

    @Test
    public void testWhitespaces() {
        ArrayList<String> compA = new ArrayList<String>() {{
            add("<img><img><img>");
        }};
        ArrayList<String> compB = new ArrayList<String>() {{
            add("<div><p>");
            add("  Some text");
            add("</p></div>");
        }};
        ArrayList<String> compC = new ArrayList<String>() {{
            add("<p>Foo\nBar</p>");
        }};
        ArrayList<String> compD = new ArrayList<String>() {{
            add("<img><pre>foo");
            add("bar</pre><img>");
        }};

        final HamlDataElement imgA1 = new HamlDataElement(1, 0, true, "img", false, null, false, null, null, false, null, false, null);
        final HamlDataElement imgA2 = new HamlDataElement(2, 0, true, "img", false, null, false, null, null, false, null, true, ">");
        final HamlDataElement imgA3 = new HamlDataElement(3, 0, true, "img", false, null, false, null, null, false, null, false, null);

        final HamlDataElement divB = new HamlDataElement(1, 0, true, "div", false, null, false, null, null, false, null, true, "<");
        final HamlDataElement pB = new HamlDataElement(2, 1, true, "p", false, null, false, null, null, false, null, false, null);
        final HamlDataElement txtB = new HamlDataElement(3, 2, false, null, true, "Some text", false, null, null, false, null, false, null);
        pB.addChild(txtB);
        divB.addChild(pB);

        final HamlDataElement pC = new HamlDataElement(1, 0, true, "p", true, "Foo\nBar", false, null, null, false, null, true, "<");

        final HamlDataElement imgD1 = new HamlDataElement(1, 0, true, "img", false, null, false, null, null, false, null, false, null);
        final HamlDataElement preD = new HamlDataElement(2, 0, true, "pre", false, null, false, null, null, false, null, true, "><");
        final HamlDataElement txtD1 = new HamlDataElement(3, 1, false, null, true, "foo", false, null, null, false, null, false, null);
        final HamlDataElement txtD2 = new HamlDataElement(4, 1, false, null, true, "bar", false, null, null, false, null, false, null);
        final HamlDataElement imgD2 = new HamlDataElement(5, 0, true, "img", false, null, false, null, null, false, null, false, null);
        preD.addChild(txtD1);
        preD.addChild(txtD2);

        ArrayList<HamlDataElement> elementsA = new ArrayList<HamlDataElement>() {{
            add(imgA1);
            add(imgA2);
            add(imgA3);
        }};
        ArrayList<HamlDataElement> elementsB = new ArrayList<HamlDataElement>() {{
            add(divB);
            add(pB);
            add(txtB);
        }};
        ArrayList<HamlDataElement> elementsC = new ArrayList<HamlDataElement>() {{
            add(pC);
        }};
        ArrayList<HamlDataElement> elementsD = new ArrayList<HamlDataElement>() {{
            add(imgD1);
            add(preD);
            add(txtD1);
            add(txtD2);
            add(imgD2);
        }};

        HamlData hd = new HamlData("testinput.haml", "testoutput.html");
        Html html = new Html(hd.getInputFileName(), hd.getOutputFileName());
        hd.hamlDataElements = elementsA;
        HtmlConverter.convertToHtml(hd.getHamlDataElements(), html);
        assertEquals(compA, html.getHtmlElements());

        html = new Html(hd.getInputFileName(), hd.getOutputFileName());
        hd.hamlDataElements = elementsB;
        HtmlConverter.convertToHtml(hd.getHamlDataElements(), html);
        assertEquals(compB, html.getHtmlElements());

        html = new Html(hd.getInputFileName(), hd.getOutputFileName());
        hd.hamlDataElements = elementsC;
        HtmlConverter.convertToHtml(hd.getHamlDataElements(), html);
        assertEquals(compC, html.getHtmlElements());

        html = new Html(hd.getInputFileName(), hd.getOutputFileName());
        hd.hamlDataElements = elementsD;
        HtmlConverter.convertToHtml(hd.getHamlDataElements(), html);
        assertEquals(compD, html.getHtmlElements());
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

    private ArrayList<String> getHtmlTestOutput() {
        final ArrayList<String> i = new ArrayList<String>() {{
            add("");
            add("  ");
            add("    ");
            add("      ");
            add("        ");
            add("          ");
            add("            ");
            add("              ");
            add("                ");
            add("                  ");
            add("                    ");
            add("                      ");
            add("                        ");
            add("                          ");
        }};
        return new ArrayList<String>() {{
            add("<html>");
            add(i.get(1) + "<head>");
            add(i.get(2) + "<title>Page title</title>");
            add(i.get(1) + "</head>");
            add(i.get(1) + "<body>");
            add(i.get(2) + "<h1>");
            add(i.get(3) + "Page header");
            add(i.get(3) + "<!--");
            add(i.get(4) + "An example:");
            add(i.get(4) + "<h1>Example header</h1>");
            add(i.get(4) + "<p>A nested button link below:</p>");
            add(i.get(4) + "<a>");
            add(i.get(5) + "<img>");
            add(i.get(4) + "</a>");
            add(i.get(3) + "-->");
            add(i.get(2) + "</h1>");
            add(i.get(2) + "<section>");
            add(i.get(3) + "<article>");
            add(i.get(4) + "<h2>Article title 0</h2>");
            add(i.get(4) + "<p>Article description 0</p>");
            add(i.get(4) + "<a>Add item to basket</a>");
            add(i.get(3) + "</article>");
            add(i.get(3) + "<article>");
            add(i.get(4) + "<h2>Article title 1</h2>");
            add(i.get(4) + "<p>Article description 1</p>");
            add(i.get(4) + "<a>Add item to basket</a>");
            add(i.get(3) + "</article>");
            add(i.get(3) + "<article>");
            add(i.get(4) + "<h2>Article title 2</h2>");
            add(i.get(4) + "<p>Article description 2</p>");
            add(i.get(4) + "<a>Add item to basket</a>");
            add(i.get(3) + "</article>");
            add(i.get(3) + "<article>");
            add(i.get(4) + "<h2>Article title 3</h2>");
            add(i.get(4) + "<p>Article description 3</p>");
            add(i.get(4) + "<a>Add item to basket</a>");
            add(i.get(3) + "</article>");
            add(i.get(3) + "<article>");
            add(i.get(4) + "<h2>Article title 4</h2>");
            add(i.get(4) + "<p>Article description 4</p>");
            add(i.get(4) + "<a>Add item to basket</a>");
            add(i.get(3) + "</article>");
            add(i.get(3) + "<article>");
            add(i.get(4) + "<h2>Article title 5</h2>");
            add(i.get(4) + "<p>Article description 5</p>");
            add(i.get(4) + "<a>Add item to basket</a>");
            add(i.get(3) + "</article>");
            add(i.get(3) + "<article>");
            add(i.get(4) + "<h2>Article title 6</h2>");
            add(i.get(4) + "<p>Article description 6</p>");
            add(i.get(4) + "<a>Add item to basket</a>");
            add(i.get(3) + "</article>");
            add(i.get(3) + "<article>");
            add(i.get(4) + "<h2>Article title 7</h2>");
            add(i.get(4) + "<p>Article description 7</p>");
            add(i.get(4) + "<a>Add item to basket</a>");
            add(i.get(3) + "</article>");
            add(i.get(3) + "<article>");
            add(i.get(4) + "<h2>Article title 8</h2>");
            add(i.get(4) + "<p>Article description 8</p>");
            add(i.get(4) + "<a>Add item to basket</a>");
            add(i.get(3) + "</article>");
            add(i.get(3) + "<article>");
            add(i.get(4) + "<h2>Article title 9</h2>");
            add(i.get(4) + "<p>Article description 9</p>");
            add(i.get(4) + "<a>Add item to basket</a>");
            add(i.get(3) + "</article>");
            add(i.get(2) + "</section>");
            add(i.get(1) + "</body>");
            add("</html>");
        }};
    }
}