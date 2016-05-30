package autotip;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class Tipper implements Runnable {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Boolean version = Boolean.parseBoolean(get);
			System.out.println("Autotip up to date:" + version);
			if (!version) // if it's not up to date spam to update
			{
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
		for (int i = 0; i < boosters.length; i++) {
			String user = boosters[i];
			if (AutotipMod.toggle) {
				// tip em
				System.out.println("Attempting to tip " + user);
				if (AutotipMod.anon) {
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/tip -a " + user);
				} else {
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/tip " + user);
				}

				// wait 4.5 seconds
				try {
					Thread.sleep(4500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		AutotipMod.runningThread = false;
	}

	// simple get request, probably breaks sometimes idk
	public static String get(String url) throws Exception {
		URL website = new URL(url);
		URLConnection connection = website.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		StringBuilder response = new StringBuilder();
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);

		in.close();

		return response.toString();
	}
}
