package ganymede.api;

public abstract class AbstractThrowableProxy implements ThrowableProxy {

	@Override
	public abstract AbstractThrowableProxy getCauseProxy();

	/**
	 * Copied from {@link Throwable#toString()}.
	 */
	@Override
	public String toString() {
		String s = getClassName();
		String message = getLocalizedMessage();
		return (message != null) ? (s + ": " + message) : s;
	}

}
