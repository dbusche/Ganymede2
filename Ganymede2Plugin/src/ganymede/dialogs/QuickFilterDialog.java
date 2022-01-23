/*
 * Created on Aug 2, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ganymede.dialogs;

import ganymede.GanymedeUtilities;
import ganymede.log4j.Filter;
import ganymede.log4j.LogSet;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Brandon
 */
public class QuickFilterDialog extends Dialog
{

	private Text mInput;

	/**
	 * @param parentShell
	 */
	public QuickFilterDialog(Shell parentShell)
	{
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
	}

	public Control createDialogArea(Composite parent)
	{
		Composite composite = (Composite) super.createDialogArea(parent);

		// Create a data that takes up the extra space in the dialog .
		GridData data = new GridData(GridData.FILL_BOTH);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);

		GridData gData;
		Label label;

		label = new Label(composite, SWT.NONE);
		label.setText("Quick Filter");
		gData = new GridData();
		gData.horizontalSpan = 1;
		label.setLayoutData(gData);

		mInput = new Text(composite, SWT.BORDER);
		if (LogSet.getInstance().getFilterset().getQuickFilter() != null)
		{
			mInput.setText(
				LogSet
					.getInstance()
					.getFilterset()
					.getQuickFilter()
					.getCriteria());
		}
		gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.horizontalSpan = 1;
		mInput.setLayoutData(gData);

		return composite;
	}

	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("Quick Filter");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed()
	{
		if (getInput().getText() != null && !getInput().getText().equals(""))
		{
			Filter filter = new Filter(0, getInput().getText(), true);
			LogSet.getInstance().getFilterset().setQuickFilter(filter);
		}
		else
		{
			LogSet.getInstance().getFilterset().setQuickFilter(null);
		}
		GanymedeUtilities.filterUpdated();
		GanymedeUtilities.updateTitleBar(getInput().getText());
		super.okPressed();
	}

	/**
	 * @return
	 */
	public Text getInput()
	{
		return mInput;
	}

	/**
	 * @param aText
	 */
	public void setInput(Text aText)
	{
		mInput = aText;
	}

}
