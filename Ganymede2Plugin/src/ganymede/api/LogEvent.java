package ganymede.api;

import java.util.Date;

public interface LogEvent {
	
	Level getLevel();
	
	/**
	 * 
	 * @return -1 for no line number information.
	 */
	default int getLineNumber() {
		ExtendedStackTraceElement source = getSource();
		if (source == null) {
			return -1;
		}
		return source.getLineNumber();
	}
	
	String getCategory();

	Date getDate();
	
	String getMessage();
	
    /**
     * Gets the source of logging request.
     *
     * @return source of logging request, may be null.
     */
    ExtendedStackTraceElement getSource();
    
    ThrowableProxy getThrownProxy();

}
