package ganymede2.log4j1;

import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import ganymede.api.AbstractThrowableProxy;
import ganymede.api.ExtendedStackTraceElement;
import ganymede.api.Level;
import ganymede.api.LogEvent;
import ganymede.api.SimpleExtendedStackElement;
import ganymede.api.ThrowableProxy;

public class Log4jLogEvent implements LogEvent {

	private LoggingEvent _evt;
	private ThrowableProxy _throwableProxy;

	public Log4jLogEvent(LoggingEvent evt) {
		_evt = evt;
		ThrowableInformation throwableInfo = _evt.getThrowableInformation();
		if (throwableInfo != null) {
			_throwableProxy = Log4jThrowableProxy.toThrowableProxy(throwableInfo.getThrowable());
		}
	}

	@Override
	public Level getLevel() {
		org.apache.log4j.Level l4jLevel = _evt.getLevel();
		if (org.apache.log4j.Level.FATAL.equals(l4jLevel)) {
			return Level.FATAL;
		}
		if (org.apache.log4j.Level.ERROR.equals(l4jLevel)) {
			return Level.ERROR;
		}
		if (org.apache.log4j.Level.WARN.equals(l4jLevel)) {
			return Level.WARN;
		}
		if (org.apache.log4j.Level.INFO.equals(l4jLevel)) {
			return Level.INFO;
		}
		if (org.apache.log4j.Level.DEBUG.equals(l4jLevel)) {
			return Level.DEBUG;
		}
		if (org.apache.log4j.Level.TRACE.equals(l4jLevel)) {
			return Level.TRACE;
		}
		if (org.apache.log4j.Level.ALL.equals(l4jLevel)) {
			return Level.ALL;
		}
		return Level.UNKNOWN;
	}

	@Override
	public ExtendedStackTraceElement getSource() {
		LocationInfo locationInformation = _evt.getLocationInformation();
		if (locationInformation == null) {
			return null;
		}
		return toExtendedStackTraceElement(locationInformation);
	}

	public static ExtendedStackTraceElement toExtendedStackTraceElement(LocationInfo element) {
		String lineNumber = element.getLineNumber();
		int line;
		if (lineNumber == null) {
			line = -1;
		} else if (LocationInfo.NA.equals(lineNumber)) {
			line = -1;
		} else {
			line = Integer.parseInt(lineNumber);
		}
		return new SimpleExtendedStackElement(element.getClassName(), element.getMethodName(), line,
				element.getFileName());
	}

	@Override
	public String getCategory() {
		return _evt.getLoggerName();
	}

	@Override
	public Date getDate() {
		return new Date(_evt.getTimeStamp());
	}

	@Override
	public String getMessage() {
		return _evt.getRenderedMessage();
	}

	@Override
	public ThrowableProxy getThrownProxy() {
		return _throwableProxy;
	}

	static class Log4jThrowableProxy extends AbstractThrowableProxy {

		private Throwable _proxy;

		public Log4jThrowableProxy(Throwable proxy) {
			this._proxy = proxy;
		}

		public static Log4jThrowableProxy toThrowableProxy(Throwable proxy) {
			if (proxy == null) {
				return null;
			}
			return new Log4jThrowableProxy(proxy);
		}

		@Override
		public String getClassName() {
			return _proxy.getClass().getName();
		}

		@Override
		public String getLocalizedMessage() {
			return _proxy.getLocalizedMessage();
		}

		@Override
		public Log4jThrowableProxy getCauseProxy() {
			return toThrowableProxy(_proxy.getCause());
		}

		@Override
		public ExtendedStackTraceElement[] getExtendedStackTrace() {
			return Log4jLogEvent.toExtendedStackTraceElement(_proxy.getStackTrace());
		}

	}

	public static ExtendedStackTraceElement toExtendedStackTraceElement(StackTraceElement element) {
		return new SimpleExtendedStackElement(element.getClassName(), element.getMethodName(), element.getLineNumber(),
				element.getFileName());
	}

	public static ExtendedStackTraceElement[] toExtendedStackTraceElement(StackTraceElement[] elements) {
		return Arrays.stream(elements).map(Log4jLogEvent::toExtendedStackTraceElement)
				.toArray(ExtendedStackTraceElement[]::new);
	}

}
