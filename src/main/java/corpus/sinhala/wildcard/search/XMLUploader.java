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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class XMLUploader {
    
    private final String java;
    private final String solrPostJarPath;
    private final String xmlDir;
    
    private final boolean debug;
    
    public XMLUploader() {
        java            = SysProperty.getProperty("java");
        solrPostJarPath = SysProperty.getProperty("solrPostJarPath");
        xmlDir          = SysProperty.getProperty("parsedXMLPath");
        debug = SysProperty.getProperty("debug").equals("yes");
    }
    
    /**
     * This method uploads xml files to Solr.
     * @return OutputStream of the Solr post.jar
     * @throws IOException 
     */
    public String uploadXMLs() throws IOException {
        String serverURL = SysProperty.getProperty("solrServerURL");
        String sysVariable = " -Durl=" + serverURL + "update "; // check -h of post.jar
        String command = java + sysVariable + " -jar " + solrPostJarPath + " " + Util.refactorDirPath(xmlDir) + "*.xml";
        if(debug) System.out.println("command: " + command);
        Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});
        InputStream solrInputStream = p.getInputStream();
        BufferedReader solrStreamReader = new BufferedReader(new InputStreamReader(solrInputStream));
        String line;
        String outputString = "";
        // reading output from post.jar to find the status of operation
        while ((line = solrStreamReader.readLine ()) != null) {
            outputString += line + "\n";
            if(line.startsWith("No files or directories matching")) {
                if(debug) System.out.println("Error while uploading.");
            }
        }
        return outputString;
    }
    
}
