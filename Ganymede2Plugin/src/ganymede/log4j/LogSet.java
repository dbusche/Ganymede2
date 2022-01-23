package ganymede.log4j;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.logging.log4j.core.LogEvent;

import ganymede.GanymedeUtilities;
import ganymede.actions.PauseAction;

/**
 * @author Brandon
 */
public class LogSet
{

	Vector mAllLogs = new Vector();
	Vector mShowingLogs = new Vector();
	Vector mHiddenLogs = new Vector();
	Vector mListeners = new Vector();
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
		return (LogEvent) mShowingLogs.get(idx);
	}

	public Collection getValidLogs()
	{
		Vector rSet = new Vector();
        
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
		Iterator allLogs = mAllLogs.iterator();
		LogEvent thisEvent;
		while (allLogs.hasNext())
		{
			thisEvent = (LogEvent) allLogs.next();
			if (getFilterset().isValidForShow(thisEvent))
			{
				mShowingLogs.insertElementAt(thisEvent, 0);
			}
		}
	}

}
