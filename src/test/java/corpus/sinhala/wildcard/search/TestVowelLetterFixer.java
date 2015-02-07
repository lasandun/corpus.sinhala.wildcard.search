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
public class TestVowelLetterFixer {
    
    InputStream is;
    BufferedReader br;
    
    @Before
    public void before() {
        is = TestVowelLetterFixer.class.getClassLoader().getResourceAsStream("vowelFixerWordList.txt");
        br = new BufferedReader(new InputStreamReader(is));
    }
    
    @org.junit.Test
    public void testWordTokenizer() throws IOException {
        SinhalaVowelLetterFixer fixer = new SinhalaVowelLetterFixer();
        String line;
        while((line = br.readLine()) != null) {
            String parts[] = line.split(",");
            String original = parts[0].trim();
            String correct = parts[1].trim();
            String fixed = fixer.fixText(original, true);
            assertEquals(correct, fixed);
        }
    }
    
    @After
    public void after() throws IOException {
        br.close();
        is.close();
    }
    
}
