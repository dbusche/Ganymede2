package ganymede.actions;

import ganymede.log4j.Log4jServer;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * TODO: Provide description for "StopAction".
 * @see IViewActionDelegate
 */
public class StopAction implements IViewActionDelegate {
	/**
	 * TODO: Implement the "StopAction" constructor.
	 */
	public StopAction() {
	}

	/**
	 * TODO: Implement "run".
	 * @see IViewActionDelegate#run
	 */
	public void run(IAction action)  {
        Log4jServer.stopListener();
	}

	/**
	 * TODO: Implement "selectionChanged".
	 * @see IViewActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection)  {
	}

	/**
	 * TODO: Implement "init".
	 * @see IViewActionDelegate#init
	 */
	public void init(IViewPart view)  {
	}
}
