package corpus.sinhala.wildcard.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;

/**
 *
 * @author lahiru
 */
public class TestWildcardQuery {
    InputStream is;
    BufferedReader br;
    
    @Before
    public void before() {
        is = TestWordParser.class.getClassLoader().getResourceAsStream("wildcardQuery.txt");
        br = new BufferedReader(new InputStreamReader(is));
    }
    
    @org.junit.Test
    public void testWordTokenizer() throws IOException {
        String line;
        WildCardQuery wildcardQuery = new WildCardQuery();
        while((line = br.readLine()) != null) {
            String words[] = line.split(",");
            String searchingWord = words[0].trim();
            LinkedList<String> results = wildcardQuery.wildCardSearchEncoded(searchingWord);
            for(int i = 1; i < words.length; ++i) {
                if(!results.contains(words[i].trim())) {
                    assertEquals(true, false);
                }
            }
        }
    }
    
    @After
    public void after() throws IOException {
        br.close();
        is.close();
    }
}
