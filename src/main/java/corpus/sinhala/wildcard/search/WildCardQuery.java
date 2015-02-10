package corpus.sinhala.wildcard.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.log4j.Logger;

/**
 *
 * @author lahiru
 */
public class WildCardQuery {
    
    String serverUrl;
    final static Logger logger = Logger.getLogger(WildCardQuery.class);

    public WildCardQuery() {
        serverUrl = "http://sinhala-corpus.projects.uom.lk/solr/";///SysProperty.getProperty("solrServerURL");
    }
    
    // do searching using the encoded stirng. Vowel sign problems won't occur
    public LinkedList<String> wildCardSearchEncoded(String word) {
        String encoded = new WordParser().encode(word);
        String query = "select?q=encoded:" + encoded + "&fl=content,frequency&rows=1400000";
        LinkedList<String> wordList = execQuery(query);
        return wordList;
    }
    
    // simple wildcard search using solr
    public LinkedList<String> wildCardSearch(String word) {
        try {
            word = URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
        }
        String query = "select?q=content:" + word + "&fl=content,frequency&rows=1400000";
        LinkedList<String> wordList = execQuery(query);
        return wordList;
    }
    
    // execute given query and return result word list
    private LinkedList<String> execQuery(String q) {
        class WordFreq implements Comparable<WordFreq> {
            String word;
            int freq;

            public WordFreq(String word, int freq) {
                this.word = word;
                this.freq = freq;
            }
            
            @Override
            public int compareTo(WordFreq o) {
                return (freq - o.freq);
            }
            
        }
        
        LinkedList<WordFreq> resultList = new LinkedList<WordFreq>();
        try {
            // create connection and query to Solr Server
            URL query = new URL(serverUrl + q);
            URLConnection connection = query.openConnection();
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            String content = "";
            // read the result to a string
            while ((line = inputStream.readLine()) != null) {
                content += line;
            }
            inputStream.close();
            // read the query time from the xml file
            OMElement documentElement = AXIOMUtil.stringToOM(content);
            OMElement resultDoc = documentElement.getFirstChildWithName(new QName("result"));
            Iterator docDocIter = resultDoc.getChildElements();
            while(docDocIter.hasNext()) {
                OMElement docDoc = (OMElement) docDocIter.next();
                Iterator strIterator = docDoc.getChildElements();
                OMElement word = (OMElement) strIterator.next();
                OMElement freq = (OMElement) strIterator.next();
                resultList.addLast(new WordFreq(word.getText(), Integer.parseInt(freq.getText())));
            }
        } catch (XMLStreamException ex) {
            logger.error(ex);
        } catch(MalformedURLException ex) {
            logger.error(ex);
        } catch(IOException ex) {
            logger.error(ex);
        }
        
        Collections.sort(resultList, Collections.reverseOrder()); // sort by frequency
        LinkedList<String> sortedMatchingList = new LinkedList<>();
        for(WordFreq wordFreqObj : resultList) {
            sortedMatchingList.addLast(wordFreqObj.word);
        }
        
        return sortedMatchingList;
    }
    
}

