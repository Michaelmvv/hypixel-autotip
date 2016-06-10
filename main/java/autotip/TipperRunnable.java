package autotip;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

class TipperRunnable implements Runnable {
	public void run() {
		// check for new update if it hasn't already checked
		if (!AutotipMod.tracker) {
			System.out.println("Autotip Version: " + AutotipMod.VERSION);
			String get = null;
			// get true or false from my little server thingy
			try {
				get = get("http://skywars.info/test/newupdate.php?u=" + Minecraft.getMinecraft().thePlayer.getUniqueID()
						+ "&v=" + AutotipMod.VERSION);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Boolean version = Boolean.parseBoolean(get);
			System.out.println("Autotip up to date:" + version);
			if (!version) { // if it's not up to date spam to update
				ClickEvent versionCheckChatClickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL,
						"http://www.skywars.info/autotip");
				ChatStyle clickableChatStyle = new ChatStyle().setChatClickEvent(versionCheckChatClickEvent);
				ChatComponentText versionWarningChatComponent = new ChatComponentText(
						EnumChatFormatting.RED + "Autotip is out of date! Click here to update.");
				versionWarningChatComponent.setChatStyle(clickableChatStyle);
				for (int i = 0; i < 10; i++)
					Minecraft.getMinecraft().thePlayer.addChatMessage(versionWarningChatComponent);
				// ^ some fancy shit i copy and pasted
			}

			AutotipMod.tracker = true; // don't run that again ok
		}
		AutotipMod.runningThread = true;
		String[] boosters = null;
		try {
			boosters = get(AutotipMod.BOOSTERS).split("\\s+");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// get the current boosters active and put em in a string array
		System.out.println("Boosters: " + boosters);
		if(boosters == null) boosters = new String[] {};
		for (String user : boosters) {
			if (AutotipMod.toggle) {
				// tip em
				System.out.println("Attempting to tip " + user);
				if (AutotipMod.anon)
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/tip -a " + user);
				else Minecraft.getMinecraft().thePlayer.sendChatMessage("/tip " + user);
				// wait 4.5 seconds
				try {
					Thread.sleep(4500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		AutotipMod.runningThread = false;
	}

	// credits to eladkay for the url to string shit that doesn't actually break!!
	public static String get(String url) throws IOException {
		URL url2 = new URL(url);
		URLConnection urlConnection = url2.openConnection();
		urlConnection.setConnectTimeout(5000);
		urlConnection.setReadTimeout(5000);
		BufferedReader breader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

		StringBuilder stringBuilder = new StringBuilder();

		String line;
		while ((line = breader.readLine()) != null) {
			stringBuilder.append(line);
		}

		return stringBuilder.toString();
}
