package ganymede.log4j;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import ganymede.api.LogEvent;

import ganymede.GanymedeUtilities;
import ganymede.actions.PauseAction;

/**
 * @author Brandon
 */
public class LogSet
{

	Vector<LogEvent> mAllLogs = new Vector<>();
	Vector<LogEvent> mShowingLogs = new Vector<>();
	Vector<LogEvent> mHiddenLogs = new Vector<>();
	Vector<LogEvent> mListeners = new Vector<>();
	FilterSet mFilterset = new FilterSet();

	static private LogSet mInstance = null;

	private LogSet()
	{
	}

	static public LogSet getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new LogSet();
		}
		return mInstance;
	}

	/**
	 * Add a log4j event to the set and alert listeners
	 * @param le
	 */
	public void addLogEvent(LogEvent le)
	{
		mAllLogs.add(le); // add to entire set regardless of filters
		if (getFilterset().isValidForShow(le))
		{
			if (GanymedeUtilities.isShowing() && !PauseAction.isPaused())
			{
				GanymedeUtilities.addTableItem(le);
				mShowingLogs.insertElementAt(le, 0);
			}
			else if (PauseAction.isPaused())
			{
				mHiddenLogs.insertElementAt(le, 0);
			}
		}
	}

	/**
	 * Returns the filterset.
	 * @return FilterSet
	 */
	public FilterSet getFilterset()
	{
		return mFilterset;
	}

	/**
	 * Sets the filterset.
	 * @param filterset The filterset to set
	 */
	public void setFilterset(FilterSet filterset)
	{
		this.mFilterset = filterset;
	}

	public void clear()
	{
		// TODO: HOW DO WE CLEAR THE NEW TABLES
		mAllLogs.clear();
		mShowingLogs.clear();
        mHiddenLogs.clear();
	}

	public LogEvent getLogEventShowingAt(int idx)
	{
		return mShowingLogs.get(idx);
	}

	public Collection<LogEvent> getValidLogs()
	{
		Vector<LogEvent> rSet = new Vector<>();
        
		if (!PauseAction.isPaused())
		{
			rSet.addAll(mHiddenLogs);
		}
		rSet.addAll(mShowingLogs);
        
		return rSet;
	}

	public int getValidLogCount()
	{
		return mShowingLogs.size();
	}

	public void revalidateAll()
	{
		mShowingLogs.clear();
        mHiddenLogs.clear();
		Iterator<LogEvent> allLogs = mAllLogs.iterator();
		LogEvent thisEvent;
		while (allLogs.hasNext())
		{
			thisEvent = allLogs.next();
			if (getFilterset().isValidForShow(thisEvent))
			{
				mShowingLogs.insertElementAt(thisEvent, 0);
			}
		}
	}

}
