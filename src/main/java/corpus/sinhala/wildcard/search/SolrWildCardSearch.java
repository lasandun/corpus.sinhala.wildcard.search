package corpus.sinhala.wildcard.search;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lahiru
 */
public class SolrWildCardSearch {
    
    private PrintWriter writer;
    
    private void writeLine(String s) {
        writer.write(s + "\n");
        writer.flush();
    }
    
    /**
     * This mehod updates the given solr core with new data
     * Creates solr syntaxed xml files from given words.csv file.
     * Output XML file directory path - solrWildcardXMLPath 
     * Input CSV file directory - solrWildcardWordListPath
     * Input CSV file should be in the same name of the updating core
     * Uploads xml files at directory - parsedXMLPath
     * Uses support of solr/example/post.jar - solrPostJarPath
     * Summary of post.jar written to summary file - solrWildcardUploadSummaryFile
     * @param solrCore core the data to be sent
     * @throws IOException 
     */
    public void updateSolrCore() throws IOException {
        // creates summary file for appending
        File summaryFile = new File(SysProperty.getProperty("solrWildcardUploadSummaryFile"));
        writer = new PrintWriter(new FileOutputStream(summaryFile, true));
        writeLine("------------------------" + new Date().toString() + "----------------------");
        writeLine("start updating solr core: " + "wildcard");
        
        // delete all xml files from xml directory before start creating xml files
        Util.deleteAllXMLs(SysProperty.getProperty("solrWildcardXMLPath"));
        writeLine("directory cleared: " + SysProperty.getProperty("solrWildcardXMLPath"));
        
        // create xml files
        writeLine("creating xml files...");
        XMLCreator x = new XMLCreator();
        LinkedList<String> rejectedWords = x.parseToXMLs();
        writeLine("\n\nrejected words:");
        for(String word : rejectedWords) {
            writeLine(word);
        }
        writeLine("");
        
        // clear the given core before uploading new data
        try {
            Util.clearSolrDataAndIndexes("wildcard");
            writeLine("solr core cleared.\n");
        } catch (Exception ex) {
            Logger.getLogger(SolrWildCardSearch.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        XMLUploader uploader = new XMLUploader();
        writeLine("\nuploading starts...\n");
        
        String summary = uploader.uploadXMLs(); // upload xml files
        
        writeLine(summary);
        writeLine("finished successfully\n\n\n");
        writer.close();
    }
    
    /**
     * 
     * @param word searching word (may include ? or * signs)
     * @param useEncoded use encoded search if the true. Else do search on sinhala word
     * @return list of matching words
     */
    public LinkedList<String> searchWord(String word, boolean useEncoded) {
        WildCardQuery query = new WildCardQuery();
        
        if(useEncoded) return query.wildCardSearchEncoded(word);
        else           return query.wildCardSearch(word);
    }
    
    public static void main(String[] args) throws IOException, Exception {
        SolrWildCardSearch x = new SolrWildCardSearch();
        
        
        x.updateSolrCore();
        
        
//        LinkedList<String> list = x.searchWord("විසි*", true);
//        for(String s : list) {
//            System.out.println(s);
//        }
    }
    
}
