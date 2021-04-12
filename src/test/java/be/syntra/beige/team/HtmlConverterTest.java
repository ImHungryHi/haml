package be.syntra.beige.team;

import junit.framework.TestCase;

import java.util.ArrayList;

public class HtmlConverterTest extends TestCase {
    public void testConvertToHtml() {
        HamlData hd = new HamlData("testinput.haml", "testoutput.html");
        Html html = new Html(hd.getInputFileName(), hd.getOutputFileName());
        hd.hamlDataElements = getTestHamlElements();
        HtmlConverter.convertToHtml(hd.getHamlDataElements().get(0), html);

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

        head.addChild(title);
        html.addChild(head);
        html.addChild(body);

        elements.add(html);
        elements.add(head);
        elements.add(title);
        elements.add(body);

        return elements;
    }
}