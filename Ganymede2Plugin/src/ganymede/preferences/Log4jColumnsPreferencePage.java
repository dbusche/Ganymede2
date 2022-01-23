package ganymede.preferences;

import ganymede.Ganymede;
import ganymede.GanymedeUtilities;
import ganymede.log4j.ColumnList;

import java.util.Iterator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class Log4jColumnsPreferencePage
	extends PreferencePage
	implements IWorkbenchPreferencePage
{

	public static final String P_COLUMNS = "P_COLUMNS";

	List columnList;
	Button btnRemove;
	Button btnMoveUp;
	Button btnAdd;
	Combo cmboAddList;
	Button btnMoveDown;
	Label label;

	public void init(IWorkbench workbench)
	{
		setPreferenceStore(Ganymede.getDefault().getPreferenceStore());
	}

	protected Control createContents(Composite parent)
	{

		Composite entryTable = new Composite(parent, SWT.NULL);

		//Create a data that takes up the extra space in the dialog .
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		entryTable.setLayoutData(data);

		GridLayout layout = new GridLayout(2, false);
		entryTable.setLayout(layout);

		GridData gData;

		label = new Label(entryTable, SWT.NONE);
		label.setText("Table Columns");
		gData = new GridData();
		gData.horizontalSpan = 2;
		label.setLayoutData(gData);

		// LIST
		columnList =
			new List(entryTable, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		gData = new GridData();
		gData.verticalSpan = 3;
		columnList.setLayoutData(gData);

		// MOVE UP
		btnMoveUp = new Button(entryTable, SWT.PUSH | SWT.CENTER);
		btnMoveUp.setText("Move Up"); //$NON-NLS-1$
		btnMoveUp.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				int index = columnList.getSelectionIndex();
				ColumnList.getInstance().moveUp(index);
				refreshList();
				columnList.select(index - 1);
				GanymedeUtilities.updateTableColumns();
			}
		});
		gData = new GridData();
		gData.horizontalSpan = 1;
		btnMoveUp.setLayoutData(gData);

		// MOVE DOWN
		btnMoveDown = new Button(entryTable, SWT.PUSH | SWT.CENTER);
		btnMoveDown.setText("Move Down"); //$NON-NLS-1$
		btnMoveDown.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				int index = columnList.getSelectionIndex();
				ColumnList.getInstance().moveDown(index);
				refreshList();
				columnList.select(index + 1);
				GanymedeUtilities.updateTableColumns();
			}
		});
		gData = new GridData();
		gData.horizontalSpan = 1;
		btnMoveDown.setLayoutData(gData);

		// REMOVE
		btnRemove = new Button(entryTable, SWT.PUSH | SWT.CENTER);
		btnRemove.setText("Remove"); //$NON-NLS-1$
		btnRemove.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				ColumnList.getInstance().remove(columnList.getSelectionIndex());
				refreshList();
				GanymedeUtilities.updateTableColumns();
			}
		});
		gData = new GridData();
		gData.horizontalSpan = 1;
		btnRemove.setLayoutData(gData);

		// ADD IT LIST
		cmboAddList = new Combo(entryTable, SWT.DROP_DOWN | SWT.READ_ONLY);
		Iterator i = GanymedeUtilities.getColumnLabels();
		while (i.hasNext())
		{
			cmboAddList.add((String) i.next());
		}
		cmboAddList.select(0);
		gData = new GridData();
		gData.horizontalSpan = 1;
		cmboAddList.setLayoutData(gData);

		// ADD IT BUTTON
		btnAdd = new Button(entryTable, SWT.PUSH | SWT.CENTER);
		btnAdd.setText("Add Column"); //$NON-NLS-1$
		btnAdd.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				int col =
					GanymedeUtilities.convertColumnToInt(cmboAddList.getText());
				ColumnList.getInstance().add(col);
				refreshList();
				GanymedeUtilities.updateTableColumns();
			}
		});
		gData = new GridData();
		gData.horizontalSpan = 1;
		btnAdd.setLayoutData(gData);

		refreshList(); // initial

		return null;
	}

	/**
	 * Refresh the main list
	 */
	protected void refreshList()
	{
		columnList.removeAll();
		Iterator i = ColumnList.getInstance().getList();
		while (i.hasNext())
		{
			Integer col = (Integer) i.next();
			columnList.add(GanymedeUtilities.getLabelText(col.intValue()));
		}
	}

//	/**
//	 * Sets the default values of the preferences.
//	 */
//	private void initializeDefaults()
//	{
//		IPreferenceStore store = Ganymede.getDefault().getPreferenceStore();
//
//		int[] defaultCols =
//			{ Log4jItem.LEVEL, Log4jItem.CATEGORY, Log4jItem.MESSAGE };
//		store.setDefault(P_COLUMNS, ColumnList.serialize(defaultCols));
//	}
//
//	private void loadValues()
//	{
//		IPreferenceStore store = Ganymede.getDefault().getPreferenceStore();
//		store.getString(P_COLUMNS);
//	}

	private void storeValues()
	{
		IPreferenceStore store = Ganymede.getDefault().getPreferenceStore();
		store.setValue(
			P_COLUMNS,
			ColumnList.serialize(ColumnList.getInstance().getCols()));
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#performApply()
	 */
	protected void performApply()
	{
		storeValues();
		super.performApply();
	}

	/**
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk()
	{
		storeValues();
		return super.performOk();
	}

}