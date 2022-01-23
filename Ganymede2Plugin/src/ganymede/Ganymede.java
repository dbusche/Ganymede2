package ganymede;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class Ganymede extends AbstractUIPlugin 
{
	//The shared instance.
	private static Ganymede plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	public static final int P_SERVER_TYPE_SOCKET_APPENDER = 1;
	
	public static final int P_SERVER_TYPE_SOCKET_HUB_APPENDER = 2;

	/**
	 * The constructor.
	 */
//	public Ganymede(IPluginDescriptor desc)
	public Ganymede()
	{
		super();
		plugin = this;
		try
		{
			resourceBundle =
				ResourceBundle.getBundle("ganymede.GanymedeResources");
		}
		catch (MissingResourceException x)
		{
			resourceBundle = null;
		}
	}
	
	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static Ganymede getDefault()
	{
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace()
	{
		return ResourcesPlugin.getWorkspace();
	}
	
	public static void markServerStarted(boolean b) {
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key)
	{
		ResourceBundle bundle = Ganymede.getDefault().getResourceBundle();
		try
		{
			return bundle.getString(key);
		}
		catch (MissingResourceException e)
		{
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle()
	{
		return resourceBundle;
	}

}
