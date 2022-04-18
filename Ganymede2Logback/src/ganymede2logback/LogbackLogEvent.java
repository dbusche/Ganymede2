package ganymede2logback;

import java.util.Arrays;
import java.util.Date;

import ganymede.api.AbstractThrowableProxy;
import ganymede.api.ExtendedStackTraceElement;
import ganymede.api.Level;
import ganymede.api.LogEvent;
import ganymede.api.SimpleExtendedStackElement;
import ganymede.api.ThrowableProxy;

public class LogbackLogEvent implements LogEvent {

	private ch.qos.logback.classic.spi.ILoggingEvent _evt;
	private ThrowableProxy _throwableProxy;

	public LogbackLogEvent(ch.qos.logback.classic.spi.ILoggingEvent evt) {
		_evt = evt;
		_throwableProxy = LogbackThrowableProxy.toThrowableProxy(_evt.getThrowableProxy());
	}

	@Override
	public Level getLevel() {
		ch.qos.logback.classic.Level logLevel = _evt.getLevel();
		if (ch.qos.logback.classic.Level.ERROR.equals(logLevel)) {
			return Level.ERROR;
		}
		if (ch.qos.logback.classic.Level.WARN.equals(logLevel)) {
			return Level.WARN;
		}
		if (ch.qos.logback.classic.Level.INFO.equals(logLevel)) {
			return Level.INFO;
		}
		if (ch.qos.logback.classic.Level.DEBUG.equals(logLevel)) {
			return Level.DEBUG;
		}
		if (ch.qos.logback.classic.Level.TRACE.equals(logLevel)) {
			return Level.TRACE;
		}
		if (ch.qos.logback.classic.Level.ALL.equals(logLevel)) {
			return Level.ALL;
		}
		return Level.UNKNOWN;
	}

	@Override
	public ExtendedStackTraceElement getSource() {
		if (!_evt.hasCallerData()) {
			return null;
		}
		StackTraceElement[] callerData = _evt. getCallerData();
		if (callerData.length == 0) {
			return null;
		}
		return new SimpleExtendedStackElement(callerData[0]);
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
		return _evt.getFormattedMessage();
	}

	@Override
	public ThrowableProxy getThrownProxy() {
		return _throwableProxy;
	}

	static class LogbackThrowableProxy extends AbstractThrowableProxy {

		private ch.qos.logback.classic.spi.IThrowableProxy proxy;

		public LogbackThrowableProxy(ch.qos.logback.classic.spi.IThrowableProxy proxy) {
			this.proxy = proxy;
		}

		public static LogbackThrowableProxy toThrowableProxy(ch.qos.logback.classic.spi.IThrowableProxy proxy) {
			if (proxy == null) {
				return null;
			}
			return new LogbackThrowableProxy(proxy);
		}
		
		@Override
		public String getClassName() {
			return proxy.getClassName();
		}
		
		@Override
		public String getLocalizedMessage() {
			return proxy.getMessage();
		}

		@Override
		public LogbackThrowableProxy getCauseProxy() {
			return toThrowableProxy(proxy.getCause());
		}

		@Override
		public ExtendedStackTraceElement[] getExtendedStackTrace() {
			return LogbackLogEvent.toExtendedStackTraceElement(proxy.getStackTraceElementProxyArray());
		}

	}

	public static ExtendedStackTraceElement toExtendedStackTraceElement(
			ch.qos.logback.classic.spi.StackTraceElementProxy element) {
		return new SimpleExtendedStackElement(element.getStackTraceElement());
	}

	public static ExtendedStackTraceElement[] toExtendedStackTraceElement(
			ch.qos.logback.classic.spi.StackTraceElementProxy[] elements) {
		return Arrays.stream(elements).map(LogbackLogEvent::toExtendedStackTraceElement)
				.toArray(ExtendedStackTraceElement[]::new);
	}

}
