package ganymede.log4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.ISafeRunnableWithResult;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import ganymede.Ganymede;
import ganymede.GanymedeUtilities;
import ganymede.api.LogEvent;
import ganymede.api.LogEventConverter;
import ganymede.preferences.Log4jPreferencePage;

/**
 * @author Brandon
 */
public class Log4jServer extends Thread
{
	
	/**
	 * java.net
	 */
	static ServerSocket mServerSocket;

	/**
	 * The GUI thread, used to look up display and tell
	 * it to fire off our updates
	 */
	static Thread mPrimary;

	/**
	 * The viewer
	 */
	static Table table;

	/**
	 * Again, communication across threads
	 */
	static boolean mServerUp = false;

	private static Log4jServer mLog4jServer;
	
	public Log4jServer(String name) {
		super(name);
	}

	static public void init()
	{
		setPrimary(Thread.currentThread());
	}

	/**
	 * Stop the Log4j Socket Server
	 * @return boolean
	 */
	static public boolean startListener()
	{
		//		System.out.println(
		//			"I am trying to start on port: "
		//				+ Ganymede.getDefault().getPreferenceStore().getInt(
		//					Log4jPreferencePage.P_PORT));
		try
		{
			int port =
					Ganymede.getDefault().getPreferenceStore().getInt(
						Log4jPreferencePage.P_PORT);
				setLog4jServer(new Log4jServer(threadName("log4jServer")));
				setServerSocket(new ServerSocket(port));
				getServerSocket().setReuseAddress(true);
				getLog4jServer().setServerUp(true);
			getLog4jServer().start();
		} catch (BindException be) {
			// Port already in use
			return false;
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return false;
		}
		
		GanymedeUtilities.updateStartStopActions();
		return true;
	}

	 static String threadName(String suffix) {
		return Ganymede.getDefault().getBundle().getSymbolicName() + "." + suffix;
	}

	/**
	 * Stop the Log4j Socket Server
	 */
	static public void stopListener()
	{
		if (getLog4jServer() != null)
		{
			getLog4jServer().setServerUp(false);
			try
			{
				// close socket if it's open
				if (getServerSocket() != null && !getServerSocket().isClosed())
				{
					getServerSocket().close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			//System.out.println("Nothing to stop");
		}

		GanymedeUtilities.updateStartStopActions();

	}

	/**
	 * Add a new message to the table - this is the 
	 * real connection between our model and firing
	 * off updates to the GUI.
	 * @param le The logging event to add
	 */
	static synchronized public void newMessage(LogEvent le)
	{
		Display display = Display.findDisplay(getPrimary());
		if (display == null) {
			LogSet.getInstance().addLogEvent(le);
		} else  {
			display.asyncExec(() -> LogSet.getInstance().addLogEvent(le));
		} 
	}

	/**
	 * Kick off the runner thread
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		Socket s = null;
		ClientConn client_conn = null;
		try
		{
			while (isServerUp())
			{
				s = mServerSocket.accept();
				s.setSoLinger(false, -1);
				s.setReuseAddress(false);
				client_conn = new ClientConn(threadName("clientConnection"), s);
				client_conn.start();
			}
		}
		catch (java.io.EOFException e)
		{
		}
		catch (IOException e)
		{
			// closed from above (ServerSocket closed, so it is thrown here)
		}
		catch (Exception e)
		{
		}
		finally
		{
			try
			{
				// shut down client correctly
				if (client_conn != null)
				{
					client_conn.setActive(false);
				}

				// close down socket if it's open
				if (s != null && !s.isClosed())
				{
					s.shutdownOutput();
					s.shutdownInput();
					s.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			//System.out.println("Server exited");
		}
	}

	/**
	 * Quick Encapsulation of the client runner
	 * - made it seperate so many servers/conns could be active
	 *   at once, each in their own thread
	 * @author Brandon
	 */
	class ClientConn extends Thread
	{

		public ClientConn(String name, Socket s)
		{
			super(name);
			registeredConverters = new ArrayList<>();
			for (IConfigurationElement e : Platform.getExtensionRegistry()
					.getConfigurationElementsFor("ganymede.extensionpoint.logevent.api")) {
				addConverterSafe(registeredConverters, e);
			}
			setSocket(s);
		}

		private Socket socket;

		private boolean mActive = false;

		private List<LogEventConverter> registeredConverters;

		private void addConverterSafe(List<LogEventConverter> converters, IConfigurationElement converterConfig) {
			ISafeRunnable runnable = new ISafeRunnable() {
				@Override
				public void handleException(Throwable e) {
					System.out.println("Exception in client: " + e.getMessage());
					e.printStackTrace();
				}

				@Override
				public void run() throws Exception {
					Object extension = converterConfig.createExecutableExtension("class");
					if (extension instanceof LogEventConverter) {
						registeredConverters.add((LogEventConverter) extension);
					}
				}
			};
			SafeRunner.run(runnable);
		}
		

		public Socket getSocket()
		{
			return socket;
		}

		public void setSocket(Socket s)
		{
			socket = s;
		}

		/**
		 * Pawn me off so I can handle some requests
		 * @see java.lang.Runnable#run()
		 */
		public void run()
		{
			setActive(true);
			try (	
					InputStream socketStream = getSocket().getInputStream();
					InputStream buffered = new BufferedInputStream(socketStream);
					CachingInputStream cachingStream = new CachingInputStream(socketStream);
					ObjectInputStream ois = new ObjectInputStream(cachingStream);
			)
			{
				List<ObjectInputStream> streams = fetchDerivedStreams(cachingStream);
				while (mServerUp && isActive())
				{

					Object serializedObject;
					try {
						serializedObject = ois.readObject();
					} catch (ClassNotFoundException e) {
						// expected, this call just loads the derived streams
					}
					
					for (int i = 0 ; i < streams.size(); i++) {
						LogEventConverter correspondingConverter = registeredConverters.get(i);
						ObjectInputStream stream = streams.get(i);
						LogEvent logEvent = createEventSafe(correspondingConverter, stream);
						if (logEvent != null) {
							newMessage(logEvent);
						}
					}
					
					cachingStream.resetCaches();
				}
			}
			catch (java.io.IOException io)
			{
				// Connection Dropped
			}
			finally
			{
				try
				{
					if (!getSocket().isClosed())
					{
						getSocket().shutdownOutput();
						getSocket().shutdownInput();
						getSocket().close();
					}
					setSocket(null); // GC
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

				//System.out.println("Client exited");
			}
		}

		private List<ObjectInputStream> fetchDerivedStreams(CachingInputStream cachingStream) throws IOException {
			List<ObjectInputStream> streams = new ArrayList<>(registeredConverters.size());
			for (LogEventConverter converter : registeredConverters) {
				ClassLoader classLoader = converter.getClass().getClassLoader();
				streams.add(new ObjectInputStreamWithClassLoader(
						cachingStream.newDerivedStream(), 
						classLoader));
			}
			return streams;
		}

		private LogEvent createEventSafe(LogEventConverter converter, ObjectInputStream serializedObject) {
			ISafeRunnableWithResult<LogEvent> runnable = new ISafeRunnableWithResult<>() {
				@Override
				public void handleException(Throwable e) {
					System.out.println("Exception in client: " + e.getMessage());
					e.printStackTrace();
				}

				@Override
				public LogEvent runWithResult() throws Exception {
					Object deserialized;
					try {
						deserialized = serializedObject.readObject();
					} catch (ClassNotFoundException e) {
						// can not be loaded, does not belong to plugin
						return null;
					}
					if (!converter.getEventClass().isInstance(deserialized)) {
						return null;
					}
					return converter.getLogEvent(deserialized);
				}
			};
			return SafeRunner.run(runnable);
		}

		/**
		 * @return
		 */
		public boolean isActive()
		{
			return mActive;
		}

		/**
		 * @param aB
		 */
		public void setActive(boolean aB)
		{
			mActive = aB;
		}
		
		class CachingInputStream extends InputStream {

			private static final int DEFAULT_BUFFER_SIZE = 8192;

			private final InputStream _base;

			int[] _cache = new int[DEFAULT_BUFFER_SIZE];

			int _length;

			List<DerivedStream> _derivedStreams = new ArrayList<>();

			CachingInputStream(InputStream base) {
				_base = base;
			}

			public DerivedStream newDerivedStream() {
				DerivedStream stream = new DerivedStream();
				_derivedStreams.add(stream);
				return stream;
			}

			@Override
			public int read() throws IOException {
				int val = _base.read();
				if (val == -1) {
					return -1;
				}
				enlargeCache();
				_cache[_length++] =  val;
				return val;
			}

			private void enlargeCache() {
				if (_length == _cache.length) {
					int newLength = _length * 2;
					_cache = Arrays.copyOf(_cache, newLength);
				}

			}

			@Override
			public void close() throws IOException {
				super.close();
				_base.close();
				for (InputStream derived : _derivedStreams) {
					derived.close();
				}
			}

			@Override
			public int available() throws IOException {
				return _base.available();
			}

			@Override
			public long skip(long n) throws IOException {
				return _base.skip(n);
			}

			void resetCaches() {
				_length = 0;
				_derivedStreams.forEach(DerivedStream::clear);
			}

			int[] readBytes() {
				return Arrays.copyOf(_cache, _length);
			}

			public class DerivedStream extends InputStream {

				private int _next = 0;

				@Override
				public int read() throws IOException {
					if (_next >= _length) {
						return -1;
					}
					return _cache[_next++];
				}

				void clear() {
					_next = 0;
				}
			}

		}
		
		

	}

	/**
	 * @return
	 */
	public static Table getTable()
	{
		return table;
	}

	/**
	 * @param aTable
	 */
	public static void setTable(Table aTable)
	{
		table = aTable;
	}

	/**
	 * @return
	 */
	public boolean isServerUp()
	{
		return mServerUp;
	}

	/**
	 * @param aB
	 */
	public void setServerUp(boolean aB)
	{
		mServerUp = aB;
	}

	/**
	 * @return
	 */
	public static Log4jServer getLog4jServer()
	{
		return mLog4jServer;
	}

	/**
	 * @param aServer
	 */
	static void setLog4jServer(Log4jServer aServer)
	{
		mLog4jServer = aServer;
	}

	/**
	 * @return
	 */
	public static ServerSocket getServerSocket()
	{
		return mServerSocket;
	}

	/**
	 * @param aSocket
	 */
	public static void setServerSocket(ServerSocket aSocket)
	{
		mServerSocket = aSocket;
	}

	/**
	 * @return
	 */
	public static Thread getPrimary()
	{
		return mPrimary;
	}

	/**
	 * @param aThread
	 */
	public static void setPrimary(Thread aThread)
	{
		mPrimary = aThread;
	}

}
