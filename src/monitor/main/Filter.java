package monitor.main;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;

public class Filter {
	private static boolean enabled = true;
	private static ArrayList<String> namesToExclude = new ArrayList<String>();

	public static boolean accepts(NetworkInterface current) throws SocketException{
		if (!enabled) return true;
		boolean rejectedByName = false;
		String displayedName = current.getDisplayName();
		for(String startsWith : namesToExclude){
			if (displayedName.startsWith(startsWith)) {
				rejectedByName = true;
				break;
			}
		}
		return current.isUp() || !current.isLoopback() || !current.isVirtual() || !rejectedByName; 
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static void setEnabled(boolean enabled) {
		Filter.enabled = enabled;
	}
	
	public static void addNameToExclude(String startsWith){
		namesToExclude.add(startsWith);
	}
	
	public static void clearNamesToExclude(){
		namesToExclude.clear();
	}
}
