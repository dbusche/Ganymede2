package ganymede2.log4j2;

import java.util.Arrays;
import java.util.Date;

import ganymede.api.AbstractThrowableProxy;
import ganymede.api.ExtendedStackTraceElement;
import ganymede.api.Level;
import ganymede.api.LogEvent;
import ganymede.api.SimpleExtendedStackElement;
import ganymede.api.ThrowableProxy;

public class Log4jLogEvent implements LogEvent {

	private org.apache.logging.log4j.core.LogEvent _evt;
	private ThrowableProxy _throwableProxy;

	public Log4jLogEvent(org.apache.logging.log4j.core.LogEvent evt) {
		_evt = evt;
		_throwableProxy = Log4jThrowableProxy.toThrowableProxy(_evt.getThrownProxy());
	}

	@Override
	public Level getLevel() {
		org.apache.logging.log4j.Level l4jLevel = _evt.getLevel();
		if (org.apache.logging.log4j.Level.FATAL.equals(l4jLevel)) {
			return Level.FATAL;
		}
		if (org.apache.logging.log4j.Level.ERROR.equals(l4jLevel)) {
			return Level.ERROR;
		}
		if (org.apache.logging.log4j.Level.WARN.equals(l4jLevel)) {
			return Level.WARN;
		}
		if (org.apache.logging.log4j.Level.INFO.equals(l4jLevel)) {
			return Level.INFO;
		}
		if (org.apache.logging.log4j.Level.DEBUG.equals(l4jLevel)) {
			return Level.DEBUG;
		}
		if (org.apache.logging.log4j.Level.TRACE.equals(l4jLevel)) {
			return Level.TRACE;
		}
		if (org.apache.logging.log4j.Level.ALL.equals(l4jLevel)) {
			return Level.ALL;
		}
		return Level.UNKNOWN;
	}

	@Override
	public int getLineNumber() {
		StackTraceElement source = _evt.getSource();
		if (source == null) {
			return -1;
		} else {
			return source.getLineNumber();
		}
	}

	@Override
	public ExtendedStackTraceElement getSource() {
		StackTraceElement source = _evt.getSource();
		if (source == null) {
			return null;
		}
		return new SimpleExtendedStackElement(source.getClassName(), source.getMethodName(), source.getLineNumber(),
				source.getFileName());
	}

	@Override
	public String getCategory() {
		return _evt.getLoggerName();
	}

	@Override
	public Date getDate() {
		return new Date(_evt.getTimeMillis());
	}

	@Override
	public String getMessage() {
		return _evt.getMessage().getFormattedMessage();
	}

	@Override
	public ThrowableProxy getThrownProxy() {
		return _throwableProxy;
	}

	static class Log4jThrowableProxy extends AbstractThrowableProxy {

		private org.apache.logging.log4j.core.impl.ThrowableProxy proxy;

		public Log4jThrowableProxy(org.apache.logging.log4j.core.impl.ThrowableProxy proxy) {
			this.proxy = proxy;
		}

		public static Log4jThrowableProxy toThrowableProxy(org.apache.logging.log4j.core.impl.ThrowableProxy proxy) {
			if (proxy == null) {
				return null;
			}
			return new Log4jThrowableProxy(proxy);
		}

		@Override
		public String getClassName() {
			return proxy.getName();
		}

		@Override
		public String getLocalizedMessage() {
			return proxy.getLocalizedMessage();
		}

		@Override
		public Log4jThrowableProxy getCauseProxy() {
			return toThrowableProxy(proxy.getCauseProxy());
		}

		@Override
		public ExtendedStackTraceElement[] getExtendedStackTrace() {
			return Log4jLogEvent.toExtendedStackTraceElement(proxy.getExtendedStackTrace());
		}

	}

	public static ExtendedStackTraceElement toExtendedStackTraceElement(
			org.apache.logging.log4j.core.impl.ExtendedStackTraceElement element) {
		return new SimpleExtendedStackElement(element.getClassName(), element.getMethodName(), element.getLineNumber(),
				element.getFileName());
	}

	public static ExtendedStackTraceElement[] toExtendedStackTraceElement(
			org.apache.logging.log4j.core.impl.ExtendedStackTraceElement[] elements) {
		return Arrays.stream(elements).map(Log4jLogEvent::toExtendedStackTraceElement)
				.toArray(ExtendedStackTraceElement[]::new);
	}

}
