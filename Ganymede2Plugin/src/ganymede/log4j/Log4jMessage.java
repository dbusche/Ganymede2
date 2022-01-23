package ganymede.log4j;

import org.apache.logging.log4j.core.LogEvent;

/**
 * @author Brandon
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Log4jMessage implements Log4jItem {

    LogEvent le;

    public Log4jMessage(LogEvent event) {
        le = event;
    }

    /**
     * @see ganymede.log4j.Log4jItem#getText()
     */
    public String getText() {
        return le.getMessage().getFormattedMessage();
    }

}
