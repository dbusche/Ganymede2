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
public class Log4jLineNumber implements Log4jItem {
	
	private LogEvent le;
	
	public Log4jLineNumber(LogEvent event) {
		le = event;
	}

    /**
     * @see ganymede.log4j.Log4jItem#getText()
     */
    public String getText() {
    	int lineNumber = le.getLineNumber();
    	if (lineNumber == -1) {
    		return "";
    	} else {
    		return Integer.toString(lineNumber);
    	}
    }

}
