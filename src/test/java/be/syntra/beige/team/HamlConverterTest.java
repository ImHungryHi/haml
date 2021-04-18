package be.syntra.beige.team;

import org.junit.Test;
import junit.framework.TestCase;

import java.util.HashMap;

public class HamlConverterTest extends TestCase {
    @Test
    public void testReturnAttributes() throws Exception {
        String input1 = "%img(src=\"/images/loading.gif\" alt=\"Image is loading\")",
                input2 = ".main.column",
                input3 = "%foo",
                input4 = "{:id => \"orangeDiv\", :alt => \"This div will be orange\"}";
        HashMap<String, String> attMap1Check = new HashMap<>(),
                                attMap2_3Check = new HashMap<>(),
                                attMap4Check = new HashMap<>(),
                                attMap1 = HamlConverter.returnAttributes(input1),
                                attMap2 = HamlConverter.returnAttributes(input2),
                                attMap3 = HamlConverter.returnAttributes(input3),
                                attMap4 = HamlConverter.returnAttributes(input4);

        attMap1Check.put("src", "/images/loading.gif");
        attMap1Check.put("alt", "Image is loading");
        attMap4Check.put("id", "orangeDiv");
        attMap4Check.put("alt", "This div will be orange");

        // All of these should work, otherwise the function failed
        assertEquals(attMap1, attMap1Check);
        assertEquals(attMap2, attMap2_3Check);
        assertEquals(attMap3, attMap2_3Check);
        assertEquals(attMap4, attMap4Check);

        //getAttributeString(attributes).replace(":", "");
    }
}