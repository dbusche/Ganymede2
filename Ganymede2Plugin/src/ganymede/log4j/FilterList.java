package ganymede.log4j;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

/**
 * @author Brandon
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class FilterList extends List {
	
	public FilterList (Composite parent, int style) {
		super(parent, style);
	}

    /**
     * @see org.eclipse.swt.widgets.List#setItems(java.lang.String)
     */
    public void setItems(Filter[] items) {
    	String[] rval = new String[items.length];
    	for (int i=0; i<items.length; i++) {
    		rval[i] = items[i].toString();
    	}
        super.setItems(rval);
    }

    /**
     * @see org.eclipse.swt.widgets.List#setItem(int, java.lang.String)
     */
    public void setItem(int index, Filter filter) {
        super.setItem(index, filter.toString());
    }
    
    /**
     * @see org.eclipse.swt.widgets.List#indexOf(java.lang.String, int)
     */
    public int indexOf(Filter filter, int start) {
        return super.indexOf(filter.toString(), start);
    }

    /**
     * @see org.eclipse.swt.widgets.List#indexOf(java.lang.String)
     */
    public int indexOf(Filter filter) {
        return super.indexOf(filter.toString());
    }

}
