package ganymede.log4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.core.LogEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import ganymede.Ganymede;
import ganymede.GanymedeUtilities;
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
				setLog4jServer(new Log4jServer());
				getLog4jServer().setServerUp(true);
				setServerSocket(new ServerSocket(port));
				getServerSocket().setReuseAddress(true);
			getLog4jServer().start();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return false;
		}
		
		GanymedeUtilities.getStartAction().setEnabled(false);
		GanymedeUtilities.getStopAction().setEnabled(true);
		return true;
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

		GanymedeUtilities.getStartAction().setEnabled(true);
		GanymedeUtilities.getStopAction().setEnabled(false);
	}

	/**
	 * Add a new message to the table - this is the 
	 * real connection between our model and firing
	 * off updates to the GUI.
	 * @param le The logging event to add
	 */
	static synchronized public void newMessage(LogEvent le)
	{
		final LogEvent thisEvent = le;
		Display.findDisplay(getPrimary()).asyncExec(new Runnable()
		{
			public void run()
			{
				LogSet.getInstance().addLogEvent(thisEvent);
			}
		});
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
				client_conn = new ClientConn(s);
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

		public ClientConn(Socket s)
		{
			setSocket(s);
		}

		private Socket socket;

		private boolean mActive = false;

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
			ObjectInputStream ois = null;
			setActive(true);
			try
			{
				ois =
					new ObjectInputStream(
						new BufferedInputStream(getSocket().getInputStream()));
				while (mServerUp && isActive())
				{

					LogEvent event = (LogEvent) ois.readObject();
					newMessage(event);
				}
			}
			catch (java.io.IOException ioe)
			{
				// Connection Dropped
			}
			catch (ClassNotFoundException cnf)
			{
				cnf.printStackTrace();
			}
			finally
			{
				try
				{
					ois.close();
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
