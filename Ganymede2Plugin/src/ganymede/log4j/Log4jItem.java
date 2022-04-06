package ganymede.log4j;

/**
 * @author Brandon
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface Log4jItem {

    static public final int LEVEL = 0;
    static public final int CATEGORY = 1;
    static public final int MESSAGE = 2;
    static public final int LINE_NUMBER = 3;
    static public final int DATE = 4;
    static public final int THROWABLE = 6;
    static public final int UNKNOWN = 99;

    static public final String LABEL_LEVEL = "Level";
    static public final String LABEL_CATEGORY = "Category";
    static public final String LABEL_MESSAGE = "Message";
    static public final String LABEL_LINE_NUMBER = "Line#";
    static public final String LABEL_DATE = "Date";
    static public final String LABEL_THROWABLE = "Throwable";
    static public final String LABEL_UNKNOWN = "Unknown";

    public String getText();

}
