package ganymede2.log4j2;

import ganymede.api.AbstractLogEventConverter;
import ganymede.api.LogEvent;

public class Log4jLogEventConverter extends AbstractLogEventConverter<org.apache.logging.log4j.core.impl.Log4jLogEvent> {

	public Log4jLogEventConverter() {
		super(org.apache.logging.log4j.core.impl.Log4jLogEvent.class);
	}

	@Override
	protected LogEvent findLogEvent(org.apache.logging.log4j.core.impl.Log4jLogEvent deserialized) {
		return new Log4jLogEvent(deserialized);
	}
	
}
