package corpus.sinhala.wildcard.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;

/**
 *
 * @author lahiru
 */
public class TestXMLCreator {
    InputStream is;
    BufferedReader br;
    String idealResultOfParsedXML;
    
    @Before
    public void before() throws IOException {
        is = TestWordParser.class.getClassLoader().getResourceAsStream("parsed.xml");
        br = new BufferedReader(new InputStreamReader(is));
        String line;
        idealResultOfParsedXML = "";
        while((line = br.readLine()) != null) {
            idealResultOfParsedXML += line;
        }
    }
    
    @org.junit.Test
    public void testWordTokenizer() throws IOException {
        XMLCreator xmlCreator = new XMLCreator();
        xmlCreator.setTestCSVPath();
        xmlCreator.parseToXMLs();
        String outputXMLPath = SysProperty.getProperty("solrWildcardXMLPath");
        BufferedReader br2 = new BufferedReader(new FileReader(outputXMLPath + "0.xml"));
        String line;
        String contentOfParsedXML = "";
        while((line = br2.readLine()) != null) {
            contentOfParsedXML += line;
        }
        assertEquals(idealResultOfParsedXML, contentOfParsedXML);
    }
    
    @After
    public void after() throws IOException {
        br.close();
        is.close();
    }
}
