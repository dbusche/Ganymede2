package ganymede.api;

public class SimpleExtendedStackElement implements ExtendedStackTraceElement {

	private String _className;
	private String _methodName;
	private String _fileName;
	private int _line;

	public SimpleExtendedStackElement(String className, String methodName, int line, String fileName) {
		this._className = className;
		this._methodName = methodName;
		this._line = line;
		this._fileName = fileName;
	}

	public SimpleExtendedStackElement(StackTraceElement base) {
		this(base.getClassName(), base.getMethodName(), base.getLineNumber(), base.getFileName());
	}

	@Override
	public String getClassName() {
		return _className;
	}

	@Override
	public String getMethodName() {
		return _methodName;
	}

	@Override
	public String getFileName() {
		return _fileName;
	}

	@Override
	public int getLineNumber() {
		return _line;
	}

}
