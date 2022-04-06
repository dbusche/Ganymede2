package ganymede2.log4j1;

import org.apache.log4j.spi.LoggingEvent;

import ganymede.api.AbstractLogEventConverter;
import ganymede.api.LogEvent;

public class Log4jLogEventConverter extends AbstractLogEventConverter<LoggingEvent> {

	public Log4jLogEventConverter() {
		super(LoggingEvent.class);
	}

	@Override
	protected LogEvent findLogEvent(LoggingEvent deserialized) {
		return new Log4jLogEvent(deserialized);
	}
	
}
