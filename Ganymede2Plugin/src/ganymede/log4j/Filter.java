package ganymede.log4j;

import java.io.IOException;
import java.io.Serializable;
import java.util.StringTokenizer;

import ganymede.api.LogEvent;
import org.apache.regexp.RE;
import org.apache.regexp.RECompiler;
import org.apache.regexp.REProgram;
import org.apache.regexp.RESyntaxException;

import ganymede.GanymedeUtilities;

/**
 * @author Brandon
 */
public class Filter implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String ENCODING_SEPERATOR = ":::";

	private int type = 0;

	private String criteria;

	private boolean inclusive = true; // false = exclusive

	private RE mExpression;

	private boolean mRegExpValid;

	/**
	 * ALL TYPES [EVEN LEVEL]
	 * @param type
	 * @param criteria
	 */
	public Filter(int type, String criteria, boolean inclusive)
	{
		this.type = type;
		this.criteria = criteria;
		this.inclusive = inclusive;
		try
		{
			REProgram program = new RECompiler().compile(criteria);
			mExpression = new RE(program);
			mRegExpValid = true;
		}
		catch (RESyntaxException e)
		{
			System.out.println(
				"Could not compile regular expression, will use java contains");
			mRegExpValid = false;
		}

	}

	/**
	 * Get the value of the specified field in string form
	 * @param event
	 * @return String
	 */
	private String getInputValue(LogEvent event)
	{
		return getInputValue(event, type);
	}

	private String getInputValue(LogEvent event, int type)
	{
		return GanymedeUtilities.Log4jItemFactory(type, event).getText();
	}

	/**
	 * See if we are valid
	 * @param le
	 * @return boolean
	 */
	public boolean isValid(LogEvent le)
	{
		String fromMessage = getInputValue(le);
		String fromFilter = criteria;

		if (mRegExpValid)
		{
			if (mExpression.match(fromMessage))
			{
				return isInclusive();
			}
			else
			{
				return !isInclusive();
			}
		}
		else
		{
			int index = fromMessage.indexOf(fromFilter);
			if (index > -1 && isInclusive())
			{
				return true;
			}
			else if (index == -1 && !isInclusive())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	public boolean isValid(LogEvent le, boolean allFields)
	{
		if (allFields)
		{
			if (mExpression.match(getInputValue(le, Log4jItem.CATEGORY)))
			{
				return true;
			}
			if (mExpression.match(getInputValue(le, Log4jItem.DATE)))
			{
				return true;
			}

			if (mExpression.match(getInputValue(le, Log4jItem.LEVEL)))
			{
				return true;
			}
			if (mExpression.match(getInputValue(le, Log4jItem.LINE_NUMBER)))
			{
				return true;
			}
			if (mExpression.match(getInputValue(le, Log4jItem.MESSAGE)))
			{
				return true;
			}
            
			return false;
		}
		else
		{
			return isValid(le);
		}
	}

	/**
	 * String version
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(GanymedeUtilities.getLabelText(type));
		if (inclusive)
		{
			sb.append(" includes ");
		}
		else
		{
			sb.append(" excludes ");
		}
		sb.append(criteria);
		return sb.toString();
	}

	/**
	 * Serialize
	 * @param serializedFilter
	 * @return Filter
	 * @throws IOException
	 */
	static public String serialize(Filter filter)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf(filter.getType()));
		sb.append(ENCODING_SEPERATOR);
		sb.append(filter.getCriteria());
		sb.append(ENCODING_SEPERATOR);
		sb.append(new Boolean(filter.inclusive).toString());
		return sb.toString();
	}

	/**
	 * Deserialize
	 * @param serializedFilter
	 * @return Filter
	 * @throws IOException
	 */
	static public Filter deSerialize(String serializedFilter)
		throws IOException
	{
		try
		{
			StringTokenizer st =
				new StringTokenizer(serializedFilter, ENCODING_SEPERATOR);
			int tokenCount = st.countTokens();
			String[] fields = new String[tokenCount];
			for (int i = 0; i < tokenCount; i++)
			{
				fields[i] = st.nextToken();
			}
			return new Filter(
				Integer.parseInt(fields[0]),
				fields[1],
				Boolean.valueOf(fields[2]).booleanValue());
		}
		catch (NumberFormatException nfe)
		{
			throw new IOException("Could not deserialize filter");
		}
	}

	static public Filter composeFromForm(String input)
	{
		StringTokenizer st = new StringTokenizer(input, " ");

		String tmp;

		int type = 0;
		boolean inclusive = true;
		String criteria;

		// figure out type
		tmp = st.nextToken();
		type = GanymedeUtilities.convertColumnToInt(tmp);

		// figure out inclusive
		tmp = st.nextToken();
		if (tmp.equals("includes"))
		{
			inclusive = true;
		}
		else
		{
			inclusive = false;
		}

		// figure out criteria
		criteria = st.nextToken();

		return new Filter(type, criteria, inclusive);
	}

	/**
	                 * Returns the criteria.
	                 * @return String
	                 */
	public String getCriteria()
	{
		return criteria;
	}

	/**
	                         * Returns the inclusive.
	                         * @return boolean
	                         */
	public boolean isInclusive()
	{
		return inclusive;
	}

	/**
	                         * Returns the type.
	                         * @return int
	                         */
	public int getType()
	{
		return type;
	}

	/**
	     * @see java.lang.Object#equals(java.lang.Object)
	     */
	public boolean equals(Object obj)
	{
		if (obj instanceof Filter)
		{
			Filter filter = (Filter) obj;
			if ((filter.getType() == getType())
				&& filter.getCriteria().equalsIgnoreCase(getCriteria())
				&& (filter.isInclusive() == isInclusive()))
			{
				return true;
			}
		}

		return false;
	}

}
