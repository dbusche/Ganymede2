package ganymede.preferences;

import ganymede.Ganymede;
import ganymede.GanymedeUtilities;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>,
 * we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class Log4jPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	/**
	 * Port log4j server listens on
	 */
	public static final String P_PORT = "log4j_socket_server_port";

	public static final String P_SERVER = "log4j_socket_server";

	public static final String P_TYPE = "log4j_server_type";

	public static final String P_TYPE_SOCKET_APPENDER_VAL = "socket_appender";

	public static final String P_TYPE_SOCKET_HUB_APPENDER_VAL = "socket_hub_appender";

	public static final String P_MAX_MESSAGES = "log4j_max_messages";
	
	public static final String P_AUTOMATIC = "log4j_automatic_start";

	public RadioGroupFieldEditor radioEditor = null;

	public StringFieldEditor serverEditor = null;

	public Log4jPreferencePage() {
		super(GRID);
		setPreferenceStore(Ganymede.getDefault().getPreferenceStore());
		setDescription("Log4j Viewer (Ganymede)");
		initializeDefaults();
	}

	/**
	 * Sets the default values of the preferences.
	 */
	static public void initializeDefaults() {
		IPreferenceStore store = Ganymede.getDefault().getPreferenceStore();
		store.setDefault(P_PORT, 4445);
		if (store.getString(P_TYPE).equals(P_TYPE_SOCKET_APPENDER_VAL)
				&& GanymedeUtilities.getServerType() != Ganymede.P_SERVER_TYPE_SOCKET_APPENDER) {
			GanymedeUtilities
					.setServerType(Ganymede.P_SERVER_TYPE_SOCKET_APPENDER);
		} else if (store.getString(P_TYPE).equals(
				P_TYPE_SOCKET_HUB_APPENDER_VAL)
				&& GanymedeUtilities.getServerType() != Ganymede.P_SERVER_TYPE_SOCKET_HUB_APPENDER) {
			GanymedeUtilities
					.setServerType(Ganymede.P_SERVER_TYPE_SOCKET_HUB_APPENDER);
		}
		store.setDefault(P_AUTOMATIC, false);
		// store.setDefault(P_MAX_MESSAGES, 50);
	}

	public void createFieldEditors() {
		RadioGroupFieldEditor radioEditor = new RadioGroupFieldEditor(P_TYPE,
				"&Type", 2, getServerTypeFieldAndValues(),
				getFieldEditorParent(), true);
		StringFieldEditor serverEditor = new StringFieldEditor(P_SERVER,
				"&Server", getFieldEditorParent());
		addField(radioEditor);
		addField(serverEditor);
		addField(new IntegerFieldEditor(P_PORT, "Server &Port",
				getFieldEditorParent()));

		setRadioEditor(radioEditor);
		setServerEditor(serverEditor);

		// initial active/inactive for server
		if (getPreferenceStore().getString(P_TYPE).equals(
				P_TYPE_SOCKET_APPENDER_VAL)) {
			getServerEditor().setEnabled(false, getFieldEditorParent());
		} else if (getPreferenceStore().getString(P_TYPE).equals(
				P_TYPE_SOCKET_HUB_APPENDER_VAL)) {
			getServerEditor().setEnabled(true, getFieldEditorParent());
		}
		
		addField(new BooleanFieldEditor(P_AUTOMATIC, "&Automatic start", getFieldEditorParent()));
	}

	/**
	 * I needed to change the systems server type here, so I had to override
	 * this.
	 */
	public boolean performOk() {
		getRadioEditor().store();

		if (getPreferenceStore().getString(P_TYPE).equals(
				P_TYPE_SOCKET_APPENDER_VAL)
				&& GanymedeUtilities.getServerType() != Ganymede.P_SERVER_TYPE_SOCKET_APPENDER) {
			GanymedeUtilities
					.setServerType(Ganymede.P_SERVER_TYPE_SOCKET_APPENDER);
		} else if (getPreferenceStore().getString(P_TYPE).equals(
				P_TYPE_SOCKET_HUB_APPENDER_VAL)
				&& GanymedeUtilities.getServerType() != Ganymede.P_SERVER_TYPE_SOCKET_HUB_APPENDER) {
			GanymedeUtilities
					.setServerType(Ganymede.P_SERVER_TYPE_SOCKET_HUB_APPENDER);
		}

		return super.performOk();
	}

	// A property was changed.
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		super.propertyChange(event);

		if (event.getSource() == getRadioEditor()
				&& !event.getNewValue().equals(
						getPreferenceStore().getString(P_TYPE))) // are they
																	// the same
		{
			MessageBox serverTypeWarningBox = new MessageBox(
					getFieldEditorParent().getShell().getParent().getShell(),
					SWT.OK | SWT.ICON_WARNING);
			serverTypeWarningBox
					.setMessage("You are attempting to switch server types.  This requires Ganymede to restart.");
			serverTypeWarningBox.open();
			if (event.getNewValue().equals(
					Log4jPreferencePage.P_TYPE_SOCKET_APPENDER_VAL)) {
				getServerEditor().setEnabled(false, getFieldEditorParent());
			} else if (event.getNewValue().equals(
					Log4jPreferencePage.P_TYPE_SOCKET_HUB_APPENDER_VAL)) {
				getServerEditor().setEnabled(true, getFieldEditorParent());
			}
		}
	}

	protected void performApply() {
		// TODO Auto-generated method stub
		super.performApply();
	}

	private String[][] getServerTypeFieldAndValues() {
		// TODO Auto-generated method stub
		String[][] rVal = new String[2][2];
		rVal[0][0] = "Socket Appender";
		rVal[0][1] = P_TYPE_SOCKET_APPENDER_VAL;
		rVal[1][0] = "Socket Hub Appender";
		rVal[1][1] = P_TYPE_SOCKET_HUB_APPENDER_VAL;
		return rVal;
	}

	public void init(IWorkbench workbench) {
	}

	public RadioGroupFieldEditor getRadioEditor() {
		return radioEditor;
	}

	public void setRadioEditor(RadioGroupFieldEditor radioEditor) {
		this.radioEditor = radioEditor;
	}

	public StringFieldEditor getServerEditor() {
		return serverEditor;
	}

	public void setServerEditor(StringFieldEditor serverEditor) {
		this.serverEditor = serverEditor;
	}
}