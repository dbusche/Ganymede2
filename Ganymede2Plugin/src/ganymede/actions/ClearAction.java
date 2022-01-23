package ganymede.actions;

import ganymede.GanymedeUtilities;
import ganymede.log4j.LogSet;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * TODO: Provide description for "ClearAction".
 * @see IViewActionDelegate
 */
public class ClearAction extends AbstractHandler implements IViewActionDelegate
{
	/**
	 * TODO: Implement the "ClearAction" constructor.
	 */
	public ClearAction()
	{
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		LogSet.getInstance().clear();
		int item_count = GanymedeUtilities.getTable().getItemCount();
		if (item_count > 0)
		{
			GanymedeUtilities.getTable().remove(
				0,
				GanymedeUtilities.getTable().getItemCount()-1);
		}
		return null;
	}

	/**
	 * TODO: Implement "run".
	 * @see IViewActionDelegate#run
	 */
	public void run(IAction action)
	{
		LogSet.getInstance().clear();
		int item_count = GanymedeUtilities.getTable().getItemCount();
		if (item_count > 0)
		{
			GanymedeUtilities.getTable().remove(
				0,
				GanymedeUtilities.getTable().getItemCount()-1);
		}
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
