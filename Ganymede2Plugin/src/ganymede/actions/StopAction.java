package ganymede.actions;

import ganymede.GanymedeUtilities;
import ganymede.log4j.Log4jServer;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * TODO: Provide description for "StopAction".
 * @see IViewActionDelegate
 */
public class StopAction extends AbstractHandler {
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
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO dbu: 
		if (!GanymedeUtilities.log4jServerUpAndRunning()) {
			GanymedeUtilities.showMessage("Log4j server not started");
			return null;
		}
		Log4jServer.stopListener();
		
//		GanymedeUtilities.getStartAction().setEnabled(true);
//		GanymedeUtilities.getStopAction().setEnabled(false);
		return null;
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
