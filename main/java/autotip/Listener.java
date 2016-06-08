package autotip;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class Listener {
	public static boolean lastMsg = false;
	public static Map<String, Integer> totalCoins = new HashMap<String, Integer>();
	public static int totalKarma;

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		String msg = event.message.getUnformattedText();
		if (AutotipMod.toggle) {
			// All the ones below are hiding failed tip messages
			if (msg.startsWith("You can't tip the same person"))
				event.setCanceled(true);
			if (msg.equals("Still processing your most recent request!"))
				event.setCanceled(true);
			if (msg.startsWith("You've already tipped that person"))
				event.setCanceled(true);
			if (msg.equals("You cannot tip yourself!"))
				event.setCanceled(true);
			if (msg.startsWith("You can only use the /tip command"))
				event.setCanceled(true);
			if (msg.equals("You are not allowed to use commands as a spectator!"))
				event.setCanceled(true);
			if (msg.equals("Slow down! You can only use /tip every few seconds."))
				event.setCanceled(true);

			// Parse tip message
			if (msg.startsWith("You") && msg.contains("tip") && msg.contains(" coins to ") && msg.contains(" in ")) {
				// get name of the player
				int beforeName = msg.indexOf(" coins to ") + 10;
				int afterName = msg.indexOf(" in");
				String name = msg.substring(beforeName, afterName);

				TipTracker.addTip(name);
				// add their name to that little array thing

				AutotipMod.totalTips++; // +1 total tips, and then write that
										// the new total tips to file
				try {
					FileUtil.writeVars();
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Tipped " + name);
			}

			// parse coins gained
			if ((msg.startsWith("+")) && (msg.contains("coins for you in "))
					&& (msg.endsWith("for being generous :)"))) {
				int coinsEarned = Integer.parseInt(msg.substring(1, 3));
				// + amt of coins

				msg = msg.substring(21);
				String gametype = msg.replace(" for being generous :)", "");
				// Get the name of the game

				// put this shit in a map my dude (for /autotip stats)
				if (totalCoins.containsKey(gametype)) {
					coinsEarned = totalCoins.get(gametype) + coinsEarned;
					totalCoins.put(gametype, coinsEarned);
				} else totalCoins.put(gametype, coinsEarned);

				if (!AutotipMod.showTips)  // cancel the message if messages are turned off
					event.setCanceled(true);
			}

			// detect karma message
			if ((msg.startsWith("+")) && (msg.contains(" Karma!"))) {
				if (!Listener.lastMsg) { // see if they said "gg" previously
					msg = msg.replace("+", "");
					msg = msg.replace(" Karma!", "");
					totalKarma = totalKarma + Integer.parseInt(msg);
					// parse amount of karma

					if (!AutotipMod.showTips) {
						event.setCanceled(true); // cancel message if messages
													// turned off
					}
				}
			}
			if (!AutotipMod.showTips) {
				if ((msg.startsWith("+")) && (msg.contains("experience (Gave a player a /tip)")))
					event.setCanceled(true);
				if (msg.startsWith("You") && msg.contains("tip") && msg.contains(" coins to ")
						&& msg.contains(" in "))
					event.setCanceled(true);
				// see if they said "gg", in which case the next message will be
				// +karma and that message shouldn't be canceled by /autotip
				// messages
				if (msg.toLowerCase().endsWith("gg") && msg.contains(Minecraft.getMinecraft().thePlayer.getName()))
					Listener.lastMsg = true;
				 else Listener.lastMsg = false;


			} else { // some other cancelling messages if tips
										// aren't needed
			}
		} else {
		}
	}
}
