package ganymede.actions;

import org.eclipse.core.expressions.PropertyTester;

import ganymede.GanymedeUtilities;

public class Log4JServerTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (property.equals("serverStarted")) {
			return GanymedeUtilities.log4jServerUpAndRunning();
		}
		return false;
	}

}
