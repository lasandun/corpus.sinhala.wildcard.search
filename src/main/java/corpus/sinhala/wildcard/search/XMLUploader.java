package corpus.sinhala.wildcard.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author lahiru
 */
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
    
    /*
     * This method uploads xml files to Solr.
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
