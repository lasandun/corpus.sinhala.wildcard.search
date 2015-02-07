package corpus.sinhala.wildcard.search;

import java.io.BufferedReader;
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
public class TestWordParser {
    InputStream is;
    BufferedReader br;
    
    @Before
    public void before() {
        is = TestWordParser.class.getClassLoader().getResourceAsStream("sinhalaWordList.txt");
        br = new BufferedReader(new InputStreamReader(is));
    }
    
    @org.junit.Test
    public void testWordTokenizer() throws IOException {
        String word;
        String encoded, decoded;
        WordParser parser = new WordParser();
        while((word = br.readLine()) != null) {
            encoded = parser.encode(word);
            decoded = parser.decode(encoded);
            assertEquals(word, decoded);
        }
    }
    
    @After
    public void after() throws IOException {
        br.close();
        is.close();
    }
}
