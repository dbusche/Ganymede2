package ganymede.log4j;

import java.util.Iterator;
import java.util.Vector;

import org.apache.logging.log4j.core.LogEvent;

/**
 * @author Brandon
 */
public class FilterSet
{

	private Vector filters = new Vector(1);

	private Filter mQuickFilter;

	public FilterSet()
	{
	}

	public void addFilter(Filter filter)
	{
		filters.add(filter);
	}

	public void removeFilter(Filter filter)
	{
		Iterator i = filters.iterator();
		while (i.hasNext())
		{
			Filter f = (Filter) i.next();
			if (filter.equals(f))
			{
				filters.remove(f);
				return;
			}
		}
	}

	public Filter[] getFilters()
	{
		return (Filter[]) filters.toArray(new Filter[0]);
	}

	public Iterator iterator()
	{
		return filters.iterator();
	}

	public boolean isValidForShow(LogEvent le)
	{
		Iterator i = iterator();
		while (i.hasNext())
		{
			Filter filter = (Filter) i.next();
			if (!filter.isValid(le))
			{
				return false;
			}
		}

		// check quick filter
		if (getQuickFilter() != null)
		{
			if (!getQuickFilter().isValid(le, true))
			{
				return false;
			}
		}

		return true; // must be ok :)
	}

	/**
	 * How many filters do we have, useful for filling in
	 * arrays of values elsewhere
	 * @return int
	 */
	public int getCount()
	{
		return filters.size();
	}

	/**
	 * @return
	 */
	public Filter getQuickFilter()
	{
		return mQuickFilter;
	}

	/**
	 * @param aFilter
	 */
	public void setQuickFilter(Filter aFilter)
	{
		mQuickFilter = aFilter;
	}

}
