package ganymede.dialogs;

import ganymede.GanymedeUtilities;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
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
public class Log4jEventDialog extends Dialog
{
	private LoggingEvent mLoggingEvent;
	
	private Composite topComposite;

	/**
	 * @param parentShell
	 */
	public Log4jEventDialog(Shell parentShell, LoggingEvent le)
	{
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
		setLoggingEvent(le);
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	public Control createDialogArea(Composite parent)
	{
		GridData gData;
		Label label;
		Text text;
		
		Composite primary = (Composite) super.createDialogArea(parent);
		FillLayout primaryLayout = new FillLayout(SWT.VERTICAL);
		primary.setLayout(primaryLayout);
		
		SashForm sashForm = new SashForm(primary, SWT.VERTICAL | SWT.BORDER);
		
		Composite topDataComposite = new Composite(sashForm, SWT.BORDER);
		GridLayout topDataCompLayout = new GridLayout();
		topDataCompLayout.numColumns = 3;
		topDataComposite.setLayout(topDataCompLayout);

		label = new Label(topDataComposite, SWT.NONE);
		label.setText("Severity: ");
		label.setForeground(
			new Color(
				GanymedeUtilities.getTable().getDisplay(),
				new RGB(0, 0, 255)));
		gData = new GridData();
		gData.horizontalSpan = 1;
		label.setLayoutData(gData);

		label = new Label(topDataComposite, SWT.NONE);
		label.setText(getLoggingEvent().getLevel().toString());
		gData = new GridData();
		gData.horizontalSpan = 2;
		label.setLayoutData(gData);

		label = new Label(topDataComposite, SWT.NONE);
		label.setText("Category: ");
		label.setForeground(
			new Color(
				GanymedeUtilities.getTable().getDisplay(),
				new RGB(0, 0, 255)));
		gData = new GridData();
		gData.horizontalSpan = 1;
		label.setLayoutData(gData);

		label = new Label(topDataComposite, SWT.NONE);
		label.setText(getLoggingEvent().getLoggerName());
		gData = new GridData();
		gData.horizontalSpan = 2;
		label.setLayoutData(gData);

		if (getLoggingEvent().getLocationInformation() != null)
		{
			label = new Label(topDataComposite, SWT.NONE);
			label.setText("Location Information");
			label.setForeground(
				new Color(
					GanymedeUtilities.getTable().getDisplay(),
					new RGB(255, 0, 0)));
			gData = new GridData();
			gData.horizontalSpan = 3;
			label.setLayoutData(gData);

			//          empty space
			label = new Label(topDataComposite, SWT.NONE);
			label.setText("      ");
			gData = new GridData();
			gData.horizontalSpan = 1;
			label.setLayoutData(gData);

			label = new Label(topDataComposite, SWT.NONE);
			label.setText("File Name: ");
			label.setForeground(
				new Color(
					GanymedeUtilities.getTable().getDisplay(),
					new RGB(0, 0, 255)));
			gData = new GridData();
			gData.horizontalSpan = 1;
			label.setLayoutData(gData);

			label = new Label(topDataComposite, SWT.NONE);
			label.setText(
				getLoggingEvent().getLocationInformation().getFileName());
			gData = new GridData();
			gData.horizontalSpan = 1;
			label.setLayoutData(gData);

			// empty space
			label = new Label(topDataComposite, SWT.NONE);
			label.setText("      ");
			gData = new GridData();
			gData.horizontalSpan = 1;
			label.setLayoutData(gData);

			label = new Label(topDataComposite, SWT.NONE);
			label.setText("Class Name: ");
			label.setForeground(
				new Color(
					GanymedeUtilities.getTable().getDisplay(),
					new RGB(0, 0, 255)));
			gData = new GridData();
			gData.horizontalSpan = 1;
			label.setLayoutData(gData);

			label = new Label(topDataComposite, SWT.NONE);
			label.setText(
				getLoggingEvent().getLocationInformation().getClassName());
			gData = new GridData();
			gData.horizontalSpan = 1;
			label.setLayoutData(gData);

			// empty space
			label = new Label(topDataComposite, SWT.NONE);
			label.setText("      ");
			gData = new GridData();
			gData.horizontalSpan = 1;
			label.setLayoutData(gData);

			label = new Label(topDataComposite, SWT.NONE);
			label.setText("Method: ");
			label.setForeground(
				new Color(
					GanymedeUtilities.getTable().getDisplay(),
					new RGB(0, 0, 255)));
			gData = new GridData();
			gData.horizontalSpan = 1;
			label.setLayoutData(gData);

			label = new Label(topDataComposite, SWT.NONE);
			label.setText(
				getLoggingEvent().getLocationInformation().getMethodName()
					+ "()");
			gData = new GridData();
			gData.horizontalSpan = 1;
			label.setLayoutData(gData);

			// empty space
			label = new Label(topDataComposite, SWT.NONE);
			label.setText("      ");
			gData = new GridData();
			gData.horizontalSpan = 1;
			label.setLayoutData(gData);

			label = new Label(topDataComposite, SWT.NONE);
			label.setText("Line Number: ");
			label.setForeground(
				new Color(
					GanymedeUtilities.getTable().getDisplay(),
					new RGB(0, 0, 255)));
			gData = new GridData();
			gData.horizontalSpan = 1;
			label.setLayoutData(gData);

			label = new Label(topDataComposite, SWT.NONE);
			label.setText(
				getLoggingEvent().getLocationInformation().getLineNumber());
			gData = new GridData();
			gData.horizontalSpan = 1;
			label.setLayoutData(gData);

		}
		
		Composite renderedMessageComposite = new Composite(sashForm, SWT.BORDER);
		GridLayout renderedMessageCompositeLayout = new GridLayout();
		renderedMessageCompositeLayout.numColumns = 3;
		renderedMessageComposite.setLayout(renderedMessageCompositeLayout);

		label = new Label(renderedMessageComposite, SWT.NONE);
		label.setText("Rendered Message");
		label.setForeground(
			new Color(
				GanymedeUtilities.getTable().getDisplay(),
				new RGB(255, 0, 0)));
		gData = new GridData();
		gData.horizontalSpan = 3;
		label.setLayoutData(gData);

		text =
			new Text(renderedMessageComposite,
				SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		text.setText(getLoggingEvent().getRenderedMessage());
		text.setEditable(false);
		gData = new GridData(GridData.FILL_BOTH);
		gData.horizontalSpan = 3;
		text.setLayoutData(gData);

		if (getLoggingEvent().getThrowableInformation() != null)
		{
			
			Composite throwableMessageComposite = new Composite(sashForm, SWT.BORDER);
			GridLayout throwableMessageCompositeLayout = new GridLayout();
			throwableMessageCompositeLayout.numColumns = 3;
			throwableMessageComposite.setLayout(renderedMessageCompositeLayout);

			label = new Label(throwableMessageComposite, SWT.NONE);
			label.setText("Exception");
			label.setForeground(
				new Color(
					GanymedeUtilities.getTable().getDisplay(),
					new RGB(255, 0, 0)));
			gData = new GridData();
			gData.horizontalSpan = 3;
			label.setLayoutData(gData);

			text =
				new Text(
						throwableMessageComposite,
					SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
			String[] stack =
				getLoggingEvent()
					.getThrowableInformation()
					.getThrowableStrRep();
			StringBuffer stackString = new StringBuffer();
			for (int i = 0; i < stack.length; i++)
			{
				stackString.append(stack[i]);
				stackString.append("\n");
			}
			text.setText(stackString.toString());
			text.setEditable(false);
			gData = new GridData(GridData.FILL_BOTH);
			gData.horizontalSpan = 3;
			text.setLayoutData(gData);
		}

		return primary;
	}

	/**
	 * Get the title for the log4j detail screen
	 * @param le
	 * @return String
	 */
	static public String getTitleText(LoggingEvent le)
	{
		if (le.getLocationInformation() != null)
		{
			return "["
				+ le.getLevel()
				+ "] "
				+ le.getLoggerName()
				+ "(line #: "
				+ le.getLocationInformation().getLineNumber()
				+ ")";
		}
		else
		{
			return "[" + le.getLevel() + "] " + le.getLoggerName();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#getInitialSize()
	 */
	protected Point getInitialSize() {
		return new Point(600, 400);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText(getTitleText(getLoggingEvent()));
	}

	/**
	 * @return
	 */
	public LoggingEvent getLoggingEvent()
	{
		return mLoggingEvent;
	}

	/**
	 * @param aEvent
	 */
	public void setLoggingEvent(LoggingEvent aEvent)
	{
		mLoggingEvent = aEvent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	protected void createButtonsForButtonBar(Composite parent)
	{
		createButton(
			parent,
			IDialogConstants.OK_ID,
			IDialogConstants.OK_LABEL,
			true);
	}
	/**
	 * @return Returns the topComposite.
	 */
	public Composite getTopComposite() {
		return topComposite;
	}
	/**
	 * @param topComposite The topComposite to set.
	 */
	public void setTopComposite(Composite topComposite) {
		this.topComposite = topComposite;
	}
}
