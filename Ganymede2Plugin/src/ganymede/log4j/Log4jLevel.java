package ganymede.log4j;

import ganymede.api.LogEvent;

/**
 * @author Brandon
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Log4jLevel implements Log4jItem {
	LogEvent le;
	
	public Log4jLevel(LogEvent event) {
		le = event;
	}

    /**
     * @see ganymede.log4j.LogItem#getText()
     */
    public String getText() {
        return le.getLevel().toString();
    }

}
