package ganymede;

import org.eclipse.ui.IStartup;

import ganymede.log4j.Log4jServer;

/**
 * {@link IStartup} triggering starting of {@link Log4jServer} when workbench
 * started.
 */
public class StartPluginOnWorkbechStartup implements IStartup {

	/**
	 * This {@link IStartup} actually do nothing. It is included to force to be
	 * loaded on workbench startup. The {@link Ganymede} plugin is configured to be
	 * started when one of of its classes is loaded. When the plugin is loaded, the
	 * {@link Log4jServer} is started.
	 *
	 * @see Ganymede#start(org.osgi.framework.BundleContext))
	 */
	@Override
	public void earlyStartup() {
		// Just a dummy class to force starting of Plugin.
	}

}
