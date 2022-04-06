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
public class Log4jCategory implements Log4jItem {
	
	private LogEvent le;
	
	public Log4jCategory(LogEvent event) {
		le = event;
	}

    /**
     * @see ganymede.log4j.Log4jItem#getText()
     */
    public String getText() {
        return le.getCategory();
    }

}
