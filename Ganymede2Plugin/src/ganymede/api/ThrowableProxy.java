package ganymede.api;

public interface ThrowableProxy {

	ThrowableProxy getCauseProxy();

	ExtendedStackTraceElement[] getExtendedStackTrace();

}
