package autotip;

import java.util.Map;
import java.util.TreeMap;

public class TipTracker {
	// some shit for /lasttip
	public static Map<String, Long> tipTime = new TreeMap<String, Long>(String.CASE_INSENSITIVE_ORDER);
	public static int localtips = 0;

	public static void addTip(String username) {
		localtips++;
		tipTime.put(username, System.currentTimeMillis());

	}
}