package corpus.sinhala.wildcard.search;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author lahiru
 */
public class SysProperty {
    
    final static Logger logger = Logger.getLogger(SysProperty.class);
    
    public static String getProperty(String key) {
        InputStream fis = null;
        try {
            fis = SysProperty.class.getClassLoader().getResourceAsStream( "solr.properties" );
            Properties p = new Properties();
            p.load(fis);
            return p.getProperty(key);
        } catch (IOException ex) {
            logger.info("\n " + "Undefined key: " + key + "\n");
            logger.error(ex);
        }finally {
            try {
                fis.close();
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
        return null;
    }
}