package ganymede.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import ganymede.GanymedeUtilities;
import ganymede.log4j.Log4jServer;

/**
 * TODO: Provide description for "StartAction".
 * @see IViewActionDelegate
 */
public class StartAction extends AbstractHandler// implements IViewActionDelegate
{
	/**
	 * TODO: Implement the "StartAction" constructor.
	 */
	public StartAction()
	{
	}

	/**
	 * TODO: Implement "run".
	 * @see IViewActionDelegate#run
	 */
	public void run(IAction action)
	{
		boolean success = Log4jServer.startListener();
		if (success)
		{
			action.setEnabled(false);
		} else
		{
            GanymedeUtilities.showMessage("Could not start Log4j");
		}
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO dbu: 
		if (GanymedeUtilities.log4jServerUpAndRunning()) {
			GanymedeUtilities.showMessage("Log4j server already started");
			return null;
		}
		boolean success = Log4jServer.startListener();
		if (success)
		{
			
		} else
		{
			GanymedeUtilities.showMessage("Could not start Log4j");
		}
		return null;
	}

	/**
	 * TODO: Implement "selectionChanged".
	 * @see IViewActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
	}

	/**
	 * TODO: Implement "init".
	 * @see IViewActionDelegate#init
	 */
	public void init(IViewPart view)
	{
	}

}
