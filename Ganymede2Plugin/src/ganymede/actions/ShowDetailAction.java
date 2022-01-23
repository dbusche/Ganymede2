package ganymede.actions;

import org.apache.logging.log4j.core.LogEvent;
import org.eclipse.jface.action.Action;

import ganymede.GanymedeUtilities;
import ganymede.dialogs.Log4jEventDialog;
import ganymede.log4j.LogSet;

/**
 * @author Brandon
 */
public class ShowDetailAction extends Action
{
	public void run()
	{
		LogEvent le =
			LogSet.getInstance().getLogEventShowingAt(
				GanymedeUtilities.getTable().getSelectionIndex());

		// build up display
		//Shell shell = new Shell(Display.getDefault());
		//shell.setSize(new Point(400, 200));
		//shell.setText(Log4jEventDialog.getTitleText(le));
		Log4jEventDialog dialog =
			new Log4jEventDialog(
				GanymedeUtilities.getTable().getParent().getShell(), le);
		dialog.open();
	}

}
