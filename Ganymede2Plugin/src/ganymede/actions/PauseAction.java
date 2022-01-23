package ganymede.actions;

import ganymede.GanymedeUtilities;
import ganymede.log4j.LogSet;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewActionDelegate;

/**
 * TODO: Provide description for "PauseAction".
 * @see IViewActionDelegate
 */
public class PauseAction extends AbstractHandler implements IViewActionDelegate
{

	public static boolean sPaused;
	/**
	 * TODO: Implement the "PauseAction" constructor.
	 */
	public PauseAction()
	{
	}

	/**
	 * TODO: Implement "run".
	 * @see IViewActionDelegate#run
	 */
	public void run(IAction action)
	{
		setPaused(!isPaused());
		if (!isPaused())
		{
			LogSet.getInstance().revalidateAll();
			GanymedeUtilities.resetTableRows();
		}
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		setPaused(!isPaused());
		if (!isPaused())
		{
			LogSet.getInstance().revalidateAll();
			GanymedeUtilities.resetTableRows();
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
	/**
	 * @return
	 */
	public static boolean isPaused()
	{
		return sPaused;
	}

	/**
	 * @param aB
	 */
	public static void setPaused(boolean aB)
	{
		sPaused = aB;
	}

}
