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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import javax.xml.stream.XMLStreamWriter;
import org.codehaus.stax2.XMLOutputFactory2;
import org.apache.log4j.Logger;

public class XMLCreator {

    private final int MAX_WORD_OF_FILE = 100000;
    private OMFactory factory;
    private OMElement root;
    private OMElement add;
    private int fileCount;
    public int rejectedWordsCount;
    public int acceptedWordCount;
    private WordParser parser;
    private final String outputXMLDirPath;
    private final String inputCVSFilePath;
    private InputStream inputStream;
    
    private final boolean debug;
    private final static Logger logger = Logger.getLogger(XMLCreator.class);
    
    public XMLCreator() {
        parser = new WordParser();
        outputXMLDirPath = SysProperty.getProperty("solrWildcardXMLPath");
        inputCVSFilePath = SysProperty.getProperty("solrWildcardWordListPath");
        debug = SysProperty.getProperty("debug").equals("yes");
    }
    
    private void initDoc() {
        factory = OMAbstractFactory.getOMFactory();
        root    = factory.createOMElement(new QName("root"));
        add     = factory.createOMElement(new QName("add"));
    }
    
    /*
     * create solr supported xml files which consist of words which were extracted
     * from a given text file.
     */
    public LinkedList<String> parseToXMLs() throws IOException {
        
        initDoc(); //  reset the XML dom
        fileCount          = 0;
        rejectedWordsCount = 0;
        acceptedWordCount  = 0;
        
        int count = 0;
        BufferedReader br;
        
        if(inputStream == null) {
            br = new BufferedReader(new FileReader(inputCVSFilePath));
        }
        else { // for testing purposes
            br = new BufferedReader(new InputStreamReader(inputStream));
        }
        
        String line;
        LinkedList<String> rejectedWords = new LinkedList<String>();
        
        while((line = br.readLine()) != null) {
            try{
                // tokenize and read data from file
                line = line.replaceAll(" ", "");
                line = line.replaceAll("\"", "");
                String parts[] = line.split(",");
                String id   = parts[0];
                String word = parts[1];
                String freq = parts[2];
                addWord(id, word, freq);
                ++count;
                acceptedWordCount++;

                // create doc of 'MAX_WORD_OF_FILE' or less words
                if(count > MAX_WORD_OF_FILE) {
                    try {
                        if(debug) System.out.println("wrote" + fileCount);
                        writeToFile(outputXMLDirPath + fileCount + ".xml");
                    } catch (FileNotFoundException ex) {
                        logger.error(ex);
                    } catch (XMLStreamException ex) {
                        logger.error(ex);
                    }
                    fileCount++;
                    count = 0;
                    initDoc();
                }
            } catch(Exception e) {
                if(debug) System.out.println("exception at line:" + line);
                rejectedWordsCount++;
                rejectedWords.addLast(line);
            }
        }
        rejectedWords.addLast("hello");
        rejectedWords.addLast("bello");
        
        // emptying the buffered data by writing them to a file
        if(count != 0) {
            try {
                if(debug) System.out.println("exception at line:" + line);
                writeToFile(outputXMLDirPath + fileCount + ".xml");
            } catch (FileNotFoundException ex) {
                logger.error(ex);
            } catch (XMLStreamException ex) {
                logger.error(ex);
            }
        }
        br.close();
        if(debug) {
            System.out.println("total accepted words: " + acceptedWordCount);
            System.out.println("total rejected words: " + rejectedWordsCount);
        }
        return rejectedWords;
    }
    
    // add a word to current XML doc
    private void addWord(String id, String word, String freq) {
        String encoded = parser.encode(word);
        OMElement doc = factory.createOMElement(new QName("doc"));
        OMElement idField = factory.createOMElement(new QName("field"));
        idField.addAttribute("name", "id", null);
        idField.setText(id);

        OMElement contentField = factory.createOMElement(new QName("field"));
        contentField.addAttribute("name", "content", null);
        contentField.setText(new QName(word));
        
        OMElement encodedField = factory.createOMElement(new QName("field"));
        encodedField.addAttribute("name", "encoded", null);
        encodedField.setText(new QName(encoded));
        
        OMElement frequencyField = factory.createOMElement(new QName("field"));
        frequencyField.addAttribute("name", "frequency", null);
        frequencyField.setText(new QName(freq));
        
        doc.addChild(idField);
        doc.addChild(contentField);
        doc.addChild(encodedField);
        doc.addChild(frequencyField);
        add.addChild(doc);
    }
    
    // write the current root doc to XML file
    private void writeToFile(String fileName) throws FileNotFoundException, XMLStreamException {
        root.addChild(add);
        OutputStream out = new FileOutputStream(fileName);
        XMLStreamWriter writer = XMLOutputFactory2.newInstance().createXMLStreamWriter(out);
        root.serialize(writer);
        writer.flush();
    }
    
    // for testing purposes. set the path of CSV file to test/resources/words.csv
    public void setTestCSVPath() {
        inputStream = XMLCreator.class.getClassLoader().getResourceAsStream("words.csv");
    }
    
}
