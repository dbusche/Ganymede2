package ganymede.log4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * Special {@link ObjectInputStream} that resolves a class using a given {@link ClassLoader}.
 */
public class ObjectInputStreamWithClassLoader extends ObjectInputStream {
	private final ClassLoader _classLoader;

	ObjectInputStreamWithClassLoader(InputStream in, ClassLoader classLoader) throws IOException {
		super(in);
		this._classLoader = classLoader;
	}

	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		String name = desc.getName();
		try {
			return Class.forName(name, false, _classLoader);
		} catch (ClassNotFoundException ex) {
			return super.resolveClass(desc);
		}
	}
}