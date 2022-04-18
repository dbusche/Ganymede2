package ganymede.api;

public interface ThrowableProxy {

	String getLocalizedMessage();

	String getClassName();

	ThrowableProxy getCauseProxy();

	ExtendedStackTraceElement[] getExtendedStackTrace();

}
