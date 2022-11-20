package ganymede;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ganymede.log4j.Log4jServer;
import ganymede.preferences.Log4jPreferencePage;

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
		Log4jPreferencePage.initializeDefaults();
		boolean automatic = getPreferenceStore().getBoolean(Log4jPreferencePage.P_AUTOMATIC);
        if (automatic)
        {
            Log4jServer.startListener();
        }
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		Log4jServer.stopListener();
	}

	/**
	 * Returns the shared instance.
	 */
	public static Ganymede getDefault()
	{
		return plugin;
	}
	
	public static IPreferenceStore preferences() {
		return getDefault().getPreferenceStore();
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace()
	{
		return ResourcesPlugin.getWorkspace();
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
