package ganymede.api;

public abstract class AbstractLogEventConverter<T> implements LogEventConverter {

	private final Class<T> _eventType;

	public AbstractLogEventConverter(Class<T> eventType) {
		_eventType = eventType;
	}
	
	@Override
	public Class<T> getEventClass() {
		return _eventType;
	}
	
	@Override
	public LogEvent getLogEvent(Object serialized) {
		if (!getEventClass().isInstance(serialized)) {
			return null;
		}
		return findLogEvent(getEventClass().cast(serialized));
	}
	
	
	protected abstract LogEvent findLogEvent(T deserialized);

}
