package corpus.sinhala.wildcard.search;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lahiru
 */
public class SysProperty {
    public static String getProperty(String key) {
        InputStream fis = null;
        try {
            fis = SysProperty.class.getClassLoader().getResourceAsStream( "solr.properties" );
            Properties p = new Properties();
            p.load(fis);
            return p.getProperty(key);
        } catch (IOException ex) {
            System.out.println();
            System.out.println("undefined property : "+ key);
            System.out.println();
            Logger.getLogger(SysProperty.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(SysProperty.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}