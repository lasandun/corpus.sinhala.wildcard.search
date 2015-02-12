/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package corpus.sinhala.wildcard.search;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import org.apache.log4j.Logger;

public class SolrWildCardSearch {
    
    private PrintWriter writer;
    private final static Logger logger = Logger.getLogger(SolrWildCardSearch.class);
    
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
            logger.error(ex);
        }
        
        XMLUploader uploader = new XMLUploader();
        writeLine("\nuploading starts...\n");
        
        String summary = uploader.uploadXMLs(); // upload xml files
        
        writeLine(summary);
        writeLine("finished successfully\n\n\n");
        writer.close();
    }
    
    /**
     * Search the given wildcard word
     * @param word searching word (may include ? or * signs)
     * @param useEncoded use encoded search if the true. Else do search on sinhala word
     * @return list of matching words
     */
    public LinkedList<String> searchWord(String word, boolean useEncoded) {
        WildCardQuery query = new WildCardQuery();
        
        if(useEncoded) return query.wildCardSearchEncoded(word);
        else           return query.wildCardSearch(word);
    }
    
}
