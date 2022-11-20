package ganymede.log4j;

import java.util.Iterator;
import java.util.Vector;

import ganymede.api.LogEvent;

/**
 * @author Brandon
 */
public class FilterSet
{

	private Vector<Filter> filters = new Vector<>(1);

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
		Iterator<Filter> i = filters.iterator();
		while (i.hasNext())
		{
			Filter f = i.next();
			if (filter.equals(f))
			{
				filters.remove(f);
				return;
			}
		}
	}

	public Filter[] getFilters()
	{
		return filters.toArray(new Filter[filters.size()]);
	}

	public Iterator<Filter> iterator()
	{
		return filters.iterator();
	}

	public boolean isValidForShow(LogEvent le)
	{
		Iterator<Filter> i = iterator();
		while (i.hasNext())
		{
			Filter filter = i.next();
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
