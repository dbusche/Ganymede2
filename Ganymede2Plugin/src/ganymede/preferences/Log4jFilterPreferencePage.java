package ganymede.preferences;

import java.util.Iterator;
import java.util.StringTokenizer;

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import ganymede.Ganymede;
import ganymede.GanymedeUtilities;
import ganymede.log4j.Filter;
import ganymede.log4j.LogSet;

public class Log4jFilterPreferencePage
    extends PreferencePage
    implements IWorkbenchPreferencePage {

    /**
     * List is stored as one string, need a seperator for storing
     */
    private static final String ENCODING_SEPERATOR = ";;;";

    public static final String P_FILTERS = "filters";

    private List filterList;

    private Text criteriaText;

    private Combo combo1, combo2;

    /**
     * Sets the default values of the preferences.
     */
    private void initializeDefaults() {
    	Iterator<Filter> i = LogSet.getInstance().getFilterset().iterator();
    	while ( i.hasNext() ) {
    		Filter filter = i.next();
    		filterList.add(filter.toString());
    	}
    }

    /*
     * @see PreferencePage#createContents(Composite)
     */
    protected Control createContents(Composite parent) {
        Composite entryTable = new Composite(parent, SWT.NULL);

        //Create a data that takes up the extra space in the dialog .
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        entryTable.setLayoutData(data);

        GridLayout layout = new GridLayout();
        entryTable.setLayout(layout);

        //Add in a dummy label for spacing
        new Label(entryTable, SWT.NONE);

        filterList = new List(entryTable, SWT.BORDER);
        filterList.setItems(
            convert(
                Ganymede.getDefault().getPreferenceStore().getString(
                    P_FILTERS)));

        //Create a data that takes up the extra space in the dialog and spans both columns.
        data = new GridData(GridData.FILL_BOTH);
        filterList.setLayoutData(data);
        // Type
        data = new GridData(GridData.FILL_HORIZONTAL);
        combo1 = new Combo(entryTable, SWT.DROP_DOWN | SWT.READ_ONLY);
        
        Iterator<String> i = GanymedeUtilities.getColumnLabels();
        
        while ( i.hasNext() ) {
        	combo1.add(i.next());
        }
        combo1.select(0);
        combo1.setLayoutData(data);

        // Inclusive/Exclusive
        data = new GridData(GridData.FILL_HORIZONTAL);
        combo2 = new Combo(entryTable, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo2.add("includes");
        combo2.add("excludes");
        combo2.select(0);
        combo2.setLayoutData(data);

        // Criteria
        data = new GridData(GridData.FILL_HORIZONTAL);
        criteriaText = new Text(entryTable, SWT.BORDER);
        criteriaText.setLayoutData(data);

        // Add Button
        Button addButton = new Button(entryTable, SWT.PUSH | SWT.CENTER);
        addButton.setText("Add to List"); //$NON-NLS-1$
        addButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                String filter =
                    combo1.getText()
                        + " "
                        + combo2.getText()
                        + " "
                        + criteriaText.getText();
                filterList.add(filter, filterList.getItemCount());
                Filter filtere = Filter.composeFromForm(filter);
                LogSet.getInstance().getFilterset().addFilter(filtere);
                GanymedeUtilities.filterUpdated();
            }
        });

        // Remove Button
        Button removeButton = new Button(entryTable, SWT.PUSH | SWT.CENTER);
        removeButton.setText("Remove From List"); //$NON-NLS-1$
        removeButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
            	String selection = filterList.getSelection()[0];
            	Filter filter = Filter.composeFromForm(selection);
            	LogSet.getInstance().getFilterset().removeFilter(filter);
            	filterList.remove(filterList.getSelectionIndex());
                GanymedeUtilities.filterUpdated();
            }
        });
        
        initializeDefaults();

        return entryTable;
    }

    /**
     * Creates the field editors. Field editors are abstractions of
     * the common GUI blocks needed to manipulate various types
     * of preferences. Each field editor knows how to save and
     * restore itself.
     */
    public void createFieldEditors() {
    }

    public void init(IWorkbench workbench) {
        setPreferenceStore(Ganymede.getDefault().getPreferenceStore());
    }

    public String[] getDefaultFilters() {
        return convert(getPreferenceStore().getDefaultString(P_FILTERS));
    }

    /**
     * Convert the serialized string into the list of filters
     * @param input
     * @return String[]
     */
    public String[] convert(String input) {
        StringTokenizer st = new StringTokenizer(input, ENCODING_SEPERATOR);
        int tokenCount = st.countTokens();
        String[] rval = new String[tokenCount];
        for (int i = 0; i < tokenCount; i++) {
            rval[i] = st.nextToken();
        }

        return rval;
    }
}