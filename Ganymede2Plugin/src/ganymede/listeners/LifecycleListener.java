package ganymede.listeners;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

import ganymede.Ganymede;
import ganymede.GanymedeUtilities;
import ganymede.log4j.Log4jServer;
import ganymede.preferences.Log4jPreferencePage;

/**
 * @author Brandon
 */
public class LifecycleListener implements IPartListener
{

	public void partActivated(IWorkbenchPart part)
	{
		boolean automatic = Ganymede.getDefault().getPreferenceStore().getBoolean(Log4jPreferencePage.P_AUTOMATIC);
        if (automatic)
        {
            Log4jServer.startListener();
        }
	}

	public void partBroughtToTop(IWorkbenchPart part)
	{
		// TODO Auto-generated method stub
	}

	public void partClosed(IWorkbenchPart part)
	{
		//		boolean shutdown =
		//			MessageDialog.openQuestion(
		//				GanymedeUtilities.getTable().getParent().getShell(),
		//				"Shut Down Log4j Server",
		//				"Do you want to stop listening for Log4j Messages?");
		//		if (shutdown)
		//		{
		//			Log4jServer.stopListener();
		//		}       
	}

	public void partDeactivated(IWorkbenchPart part)
	{
        GanymedeUtilities.saveTableColumnWidths();
	}

	public void partOpened(IWorkbenchPart part)
	{
		// TODO Auto-generated method stub
	}

}
