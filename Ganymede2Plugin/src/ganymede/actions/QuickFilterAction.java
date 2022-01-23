package ganymede.actions;

import ganymede.GanymedeUtilities;
import ganymede.dialogs.QuickFilterDialog;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewActionDelegate;

/**
 * TODO: Provide description for "QuickFilterAction".
 * @see IViewActionDelegate
 */
public class QuickFilterAction extends AbstractHandler implements IViewActionDelegate
{
	/**
	 * TODO: Implement the "QuickFilterAction" constructor.
	 */
	public QuickFilterAction()
	{
	}

	/**
	 * TODO: Implement "run".
	 * @see IViewActionDelegate#run
	 */
	public void run(IAction action)
	{
		QuickFilterDialog dialog =
			new QuickFilterDialog(GanymedeUtilities.getTable().getShell());
		dialog.open();
        //System.out.println(""+dialog.getReturnCode());
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		QuickFilterDialog dialog =
				new QuickFilterDialog(GanymedeUtilities.getTable().getShell());
		dialog.open();
		//System.out.println(""+dialog.getReturnCode());
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
