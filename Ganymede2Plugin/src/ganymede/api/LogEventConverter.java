package ganymede.api;

public interface LogEventConverter {
	
	LogEvent getLogEvent(Object serialized) ;
	
	Class<?> getEventClass();
	
	
}
