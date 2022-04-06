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
public class Log4jThrowable implements Log4jItem {
	
    private LogEvent le;
    
	public Log4jThrowable(LogEvent event) {
		le = event;
	}

    /**
     * @see ganymede.log4j.Log4jItem#getText()
     */
    public String getText() {
    	if ( le.getThrownProxy() != null ) {
    		return "X"; // TODO: use image instead
    	} else {
    		return "";
    	}
    }

}
