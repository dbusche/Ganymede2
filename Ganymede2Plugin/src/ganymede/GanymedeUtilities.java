package ganymede;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.e4.ui.model.application.ui.menu.MItem;
import org.eclipse.e4.ui.workbench.renderers.swt.HandledContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IViewSite;

import ganymede.actions.ShowDetailAction;
import ganymede.api.Level;
import ganymede.api.LogEvent;
import ganymede.log4j.ColumnList;
import ganymede.log4j.Log4jCategory;
import ganymede.log4j.Log4jDate;
import ganymede.log4j.Log4jItem;
import ganymede.log4j.Log4jLevel;
import ganymede.log4j.Log4jLineNumber;
import ganymede.log4j.Log4jMessage;
import ganymede.log4j.Log4jServer;
import ganymede.log4j.Log4jThrowable;
import ganymede.log4j.LogSet;
import ganymede.preferences.ColorPreferencePage;
import ganymede.views.GanymedeView;

/**
 * @author Brandon
 */
public class GanymedeUtilities {

	private static Table table;

	private static GanymedeView mView;

	private static Log4jServer mSocketServer;

	private static ShowDetailAction mShowDetailAction;

	private static Hashtable<String, Color> colors = new Hashtable<>(5);

	private static MItem mStartAction;

	private static MItem mStopAction;

	private static IViewSite mSite;

	private static boolean mActionsInited = false;

	private static String sViewTitle;

	private static int mServerType = Ganymede.P_SERVER_TYPE_SOCKET_APPENDER;

	static public Iterator<String> getColumnLabels() {
		Vector<String> v = new Vector<>();
		v.add(Log4jItem.LABEL_LEVEL);
		v.add(Log4jItem.LABEL_CATEGORY);
		v.add(Log4jItem.LABEL_MESSAGE);
		v.add(Log4jItem.LABEL_LINE_NUMBER);
		v.add(Log4jItem.LABEL_DATE);
		v.add(Log4jItem.LABEL_THROWABLE);
		return v.iterator();
	}

	/**
	 * Get the string label
	 * 
	 * @param col
	 * @return String
	 */
	static public String getLabelText(int col) {
		switch (col) {
		case Log4jItem.LEVEL:
			return Log4jItem.LABEL_LEVEL;
		case Log4jItem.CATEGORY:
			return Log4jItem.LABEL_CATEGORY;
		case Log4jItem.MESSAGE:
			return Log4jItem.LABEL_MESSAGE;
		case Log4jItem.LINE_NUMBER:
			return Log4jItem.LABEL_LINE_NUMBER;
		case Log4jItem.DATE:
			return Log4jItem.LABEL_DATE;
		case Log4jItem.THROWABLE:
			return Log4jItem.LABEL_THROWABLE;
		default:
			return Log4jItem.LABEL_UNKNOWN;
		}
	}

	/**
	 * This is used to convert a string column label to it's int counterpart
	 * 
	 * @param colLabel
	 *            The column label
	 * @return int
	 */
	static public int convertColumnToInt(String colLabel) {
		if (colLabel.equals(Log4jItem.LABEL_LEVEL)) {
			return Log4jItem.LEVEL;
		} else if (colLabel.equals(Log4jItem.LABEL_CATEGORY)) {
			return Log4jItem.CATEGORY;
		} else if (colLabel.equals(Log4jItem.LABEL_MESSAGE)) {
			return Log4jItem.MESSAGE;
		} else if (colLabel.equals(Log4jItem.LABEL_LINE_NUMBER)) {
			return Log4jItem.LINE_NUMBER;
		} else if (colLabel.equals(Log4jItem.LABEL_DATE)) {
			return Log4jItem.DATE;
		} else if (colLabel.equals(Log4jItem.LABEL_THROWABLE)) {
			return Log4jItem.THROWABLE;
		} else {
			return Log4jItem.UNKNOWN;
		}
	}

	static public Log4jItem Log4jItemFactory(int type, LogEvent e) {
		switch (type) {
		case Log4jItem.LEVEL:
			return new Log4jLevel(e);
		case Log4jItem.CATEGORY:
			return new Log4jCategory(e);
		case Log4jItem.MESSAGE:
			return new Log4jMessage(e);
		case Log4jItem.LINE_NUMBER:
			return new Log4jLineNumber(e);
		case Log4jItem.DATE:
			return new Log4jDate(e);
		case Log4jItem.THROWABLE:
			return new Log4jThrowable(e);
		default:
			return null;
		}
	}

	static public void setTable(Table table) {
		GanymedeUtilities.table = table;
	}

	static public Table getTable() {
		return GanymedeUtilities.table;
	}

	/**
	 * Sets the defaults. This needs to be called before you can update widths
	 * or save any
	 */
	static public void setTableColumnWidthDefaults() {
		IPreferenceStore store = Ganymede.getDefault().getPreferenceStore();
		store.setDefault("colWidth." + Log4jItem.LEVEL, 75);
		store.setDefault("colWidth." + Log4jItem.CATEGORY, 75);
		store.setDefault("colWidth." + Log4jItem.DATE, 75);
		store.setDefault("colWidth." + Log4jItem.LINE_NUMBER, 75);
		store.setDefault("colWidth." + Log4jItem.MESSAGE, 75);
		store.setDefault("colWidth." + Log4jItem.THROWABLE, 75);
		store.setDefault("colWidth." + Log4jItem.UNKNOWN, 75);
	}

	/**
	 * Saves column widths
	 */
	static public void saveTableColumnWidths() {
		if (isShowing()) {
			setTableColumnWidthDefaults();
			IPreferenceStore store = Ganymede.getDefault().getPreferenceStore();
			TableColumn[] tc = getTable().getColumns();
			ColumnList list = ColumnList.getInstance();
			Iterator<Integer> iter = list.getList();
			for (int i = 0; i < list.getColumnCount(); i++) {
				int val = iter.next().intValue();
				store.setValue("colWidth." + val, tc[i].getWidth());
			}
		}
	}

	/**
	 * Used to refresh their widths [on startup usually]
	 */
	static public void updateTableColumnWidths() {
		if (isShowing()) {
			setTableColumnWidthDefaults();
			IPreferenceStore store = Ganymede.getDefault().getPreferenceStore();
			TableColumn[] tc = getTable().getColumns();
			ColumnList list = ColumnList.getInstance();
			Iterator<Integer> iter = list.getList();
			for (int i = 0; i < list.getColumnCount(); i++) {
				int val = iter.next().intValue();
				int width = store.getInt("colWidth." + val);
				tc[i].setWidth(width);
			}
		}
	}

	static public void updateTableColumns() {
		if (isShowing()) {
			TableColumn[] tc = getTable().getColumns();
			ColumnList list = ColumnList.getInstance();
			Iterator<Integer> iter = list.getList();
			for (int i = 0; i < list.getColumnCount(); i++) {
				int val = iter.next().intValue();
				tc[i].setText(GanymedeUtilities.getLabelText(val));
				updateTableColumnWidths();
			}

			resetTableRows();
		}
	}

	static public String handleNull(String string) {
		if (string == null) {
			return "";
		} else {
			return string;
		}
	}

	public static String getColumnText(Object aLogEvent, int aColumnIndex) {
		if (aLogEvent instanceof LogEvent) {
			LogEvent le = (LogEvent) aLogEvent;
			return ColumnList.getInstance().getText(aColumnIndex, le);
		}

		return "Not a valid logging event";
	}

	/**
	 * The filter has been updated, so we need to refresh display
	 */
	public static void filterUpdated() {
		LogSet.getInstance().revalidateAll();
		resetTableRows();
	}

	/**
	 * @return
	 */
	public static GanymedeView getView() {
		return mView;
	}

	/**
	 * @param aView
	 */
	public static void setView(GanymedeView aView) {
		mView = aView;
	}

	/**
	 * @return
	 */
	public static Log4jServer getSocketServer() {
		return mSocketServer;
	}

	/**
	 * @param aServer
	 */
	public static void setSocketServer(Log4jServer aServer) {
		mSocketServer = aServer;
	}

	/**
	 * Show a message box
	 * 
	 * @param message
	 */
	static public void showMessage(String message) {
		MessageDialog.openInformation(getTable().getParent().getShell(),
				"Ganymede Log4j View", message + "...");
	}

	/**
	 * @return
	 */
	public static ShowDetailAction getShowDetailAction() {
		return mShowDetailAction;
	}

	/**
	 * @param aAction
	 */
	public static void setShowDetailAction(ShowDetailAction aAction) {
		mShowDetailAction = aAction;
	}

	static public void updateColors() {
		IPreferenceStore store = Ganymede.getDefault().getPreferenceStore();

		colors.clear();
		colors.put(Level.DEBUG.toString(), new Color(Display.getCurrent(),
				PreferenceConverter.getColor(store,
						ColorPreferencePage.DEBUG_COLOR_KEY)));
		colors.put(Level.INFO.toString(), new Color(Display.getCurrent(),
				PreferenceConverter.getColor(store,
						ColorPreferencePage.INFO_COLOR_KEY)));
		colors.put(Level.WARN.toString(), new Color(Display.getCurrent(),
				PreferenceConverter.getColor(store,
						ColorPreferencePage.WARN_COLOR_KEY)));
		colors.put(Level.ERROR.toString(), new Color(Display.getCurrent(),
				PreferenceConverter.getColor(store,
						ColorPreferencePage.ERROR_COLOR_KEY)));
		colors.put(Level.FATAL.toString(), new Color(Display.getCurrent(),
				PreferenceConverter.getColor(store,
						ColorPreferencePage.FATAL_COLOR_KEY)));

		// reset display
		if (isShowing()) {
			LogSet logSet = LogSet.getInstance();
			Collection<LogEvent> validLogs = logSet.getValidLogs();
			TableItem[] items = getTable().getItems();
			int idx = 0;
			for (Iterator<LogEvent> logIter = validLogs.iterator(); logIter.hasNext(); idx++) {
				LogEvent le = logIter.next();
				items[idx].setForeground(GanymedeUtilities.getColor(le
						.getLevel()));
			}
		}
	}

	static public Color getColor(Level level) {
		return colors.get(level.toString());
	}

	static public void initColorDefaults() {
		IPreferenceStore store = Ganymede.getDefault().getPreferenceStore();
		PreferenceConverter.setDefault(store,
				ColorPreferencePage.DEBUG_COLOR_KEY, new RGB(0, 0, 0));
		PreferenceConverter.setDefault(store,
				ColorPreferencePage.INFO_COLOR_KEY, new RGB(0, 255, 0));
		PreferenceConverter.setDefault(store,
				ColorPreferencePage.WARN_COLOR_KEY, new RGB(255, 128, 0));
		PreferenceConverter.setDefault(store,
				ColorPreferencePage.ERROR_COLOR_KEY, new RGB(255, 0, 0));
		PreferenceConverter.setDefault(store,
				ColorPreferencePage.FATAL_COLOR_KEY, new RGB(255, 0, 0));
	}

	/**
	 * Is the view active? Use this in preference pages to see if you need to
	 * call back to update view.
	 * 
	 * @return
	 */
	static public boolean isShowing() {
		Table table = GanymedeUtilities.getTable();
		return (table != null && !table.isDisposed());
	}

	/**
	 * 
	 */
	public static void resetTableRows() {
		// refresh display
		if (isShowing()) {
			boolean visible = getTable().getVisible();
			getTable().setVisible(false);
			LogSet logSet = LogSet.getInstance();
			Collection<LogEvent> validLogs = logSet.getValidLogs();
			int itemCount = getTable().getItemCount();
			TableItem[] items = getTable().getItems();
			TableItem thisItem;
			int idx = 0;
			for (Iterator<LogEvent> logIter = validLogs.iterator(); logIter.hasNext(); idx++) {
				LogEvent le = logIter.next();
				if (idx < itemCount) // reuse if possible
				{
					thisItem = items[idx];
				} else {
					thisItem = createTableItem(idx);
				}

				int colCount = getTable().getColumnCount();
				for (int colIdx = 0; colIdx < colCount; colIdx++) {
					String text = GanymedeUtilities.getColumnText(le, colIdx);
					thisItem.setText(colIdx, text);
					thisItem.setForeground(GanymedeUtilities.getColor(le
							.getLevel()));
				}
			}

			// get rid of any now open spaces
			if (idx < itemCount) {
				getTable().remove(idx, itemCount - 1);
			}

			getTable().setVisible(visible);
		}
	}

	/**
	 * @return
	 */
	private static TableItem createTableItem(int index) {
		return new TableItem(getTable(), SWT.NONE, index);
	}

	/**
	 * @return
	 */
	public static MItem getStartAction() {
		if (!isActionsInited()) {
			initActions();
		}
		return mStartAction;
	}

	/**
	 * @return
	 */
	public static MItem getStopAction() {
		if (!isActionsInited()) {
			initActions();
		}
		return mStopAction;
	}

	/**
	 * @param aAction
	 */
	public static void setStartAction(MItem aAction) {
		mStartAction = aAction;
	}

	/**
	 * @param aAction
	 */
	public static void setStopAction(MItem aAction) {
		mStopAction = aAction;
	}

	/**
	 * 
	 */
	private static void initActions() {
		setActionsInited(true);
		GanymedeUtilities.setStartAction(((HandledContributionItem)startToolbarItem()).getModel());
		GanymedeUtilities.setStopAction(((HandledContributionItem)stopToolBarItem()).getModel());

		
		if (log4jServerUpAndRunning()) {
			getStartAction().setEnabled(false);
			getStopAction().setEnabled(true);
		} else {
			getStartAction().setEnabled(true);
			getStopAction().setEnabled(false);
		}
	}

	public static IContributionItem stopToolBarItem() {
		return getSite()
				.getActionBars().getToolBarManager()
				.find("Ganymede.StopAction.toolbarItem");
	}

	public static IContributionItem startToolbarItem() {
		return getSite()
				.getActionBars().getToolBarManager().find(
						"Ganymede.StartAction.toolbarItem");
	}
	
	public static boolean log4jServerUpAndRunning() {
		Log4jServer instance = Log4jServer.getLog4jServer();
		return instance != null && instance.isServerUp();
	}

	/**
	 * @return
	 */
	static public boolean isActionsInited() {
		return mActionsInited;
	}

	/**
	 * @param aB
	 */
	static public void setActionsInited(boolean aB) {
		mActionsInited = aB;
	}

	/**
	 * @return
	 */
	public static IViewSite getSite() {
		return mSite;
	}

	/**
	 * @param aSite
	 */
	public static void setSite(IViewSite aSite) {
		mSite = aSite;
	}

	/**
	 * @return
	 */
	public static String getViewTitle() {
		return sViewTitle;
	}

	/**
	 * @param aString
	 */
	public static void setViewTitle(String aString) {
		sViewTitle = aString;
	}

	/**
	 * @param le
	 */
	public static void addTableItem(LogEvent le) {
		TableItem tableItem = createTableItem(0);

		for (int i = 0; i < getTable().getColumnCount(); i++) {
			tableItem.setText(i, getColumnText(le, i));
		}

		tableItem.setForeground(GanymedeUtilities.getColor(le.getLevel()));

		table.setSelection(0);
	}

	public static void updateTitleBar(String filterText) {
		if (filterText == null || filterText.equals("")) {
			filterText = "No Filter";
		}
		GanymedeUtilities.getView().getFilterLabel().setText(filterText);
	}

	public static int getServerType() {
		return mServerType;
	}

	public static void setServerType(int serverType) {
		
		System.out.println("Changing server type [" + serverType + "]");
		mServerType = serverType;
		
		// restart
		// TODO -Check to see if it's running first.
		Log4jServer.stopListener();
		Log4jServer.startListener();
	}

}
