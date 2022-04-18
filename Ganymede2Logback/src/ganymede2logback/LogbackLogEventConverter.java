package ganymede2logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ganymede.api.AbstractLogEventConverter;
import ganymede.api.LogEvent;

public class LogbackLogEventConverter extends AbstractLogEventConverter<ILoggingEvent> {

	public LogbackLogEventConverter() {
		super(ILoggingEvent.class);
	}

	@Override
	protected LogEvent findLogEvent(ILoggingEvent deserialized) {
		return new LogbackLogEvent(deserialized);
	}

}
