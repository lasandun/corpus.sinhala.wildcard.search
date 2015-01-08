package corpus.sinhala.wildcard.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;

/**
 *
 * @author lahiru
 */
public class WildCardQuery {
    
    String serverUrl;

    public WildCardQuery() {
        serverUrl = SysProperty.getProperty("solrServerURL");
    }
    
    // do searching using the encoded stirng. Vowel sign problems won't occur
    public LinkedList<String> wildCardSearchEncoded(String word) {
        String encoded = new WordParser().encode(word);
        String query = "select?q=encoded:" + encoded + "&fl=content&rows=1400000";
        LinkedList<String> wordList = execQuery(query);
        return wordList;
    }
    
    // simple wildcard search using solr
    public LinkedList<String> wildCardSearch(String word) {
        try {
            word = URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(WildCardQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        String query = "select?q=content:" + word + "&fl=content&rows=1400000";
        LinkedList<String> wordList = execQuery(query);
        return wordList;
    }
    
    // execute given query and return result word list
    private LinkedList<String> execQuery(String q) {
        LinkedList<String> matchingList = new LinkedList<String>();
        try {
            // create connection and query to Solr Server
            URL query = new URL(serverUrl + "solr/wildcard/" + q);
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
                OMElement word = docDoc.getFirstChildWithName(new QName("str"));
                String w = word.getText();
                matchingList.addLast(w);
            }
        } catch (XMLStreamException ex) {
            Logger.getLogger(WildCardQuery.class.getName()).log(Level.SEVERE, null, ex);
        } catch(MalformedURLException ex) {
            Logger.getLogger(WildCardQuery.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IOException ex) {
            Logger.getLogger(WildCardQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return matchingList;
    }
    
}

