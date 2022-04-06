package ganymede.api;

public interface ExtendedStackTraceElement {

	String getClassName();

	String getMethodName();

	String getFileName();

	int getLineNumber();

}
