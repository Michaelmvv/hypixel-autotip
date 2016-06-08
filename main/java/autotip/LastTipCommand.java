package autotip;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Set;

public class LastTipCommand extends CommandBase {

	public String getCommandName() {
		return "lasttip";
	}

	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return index == 0;
	}

	public void processCommand(ICommandSender sender, String[] args) {
		long currentTime = System.currentTimeMillis();
		if (args.length == 1) {
			if (TipTracker.tipTime.containsKey(args[0])) {
				// fancy shit that doesn't really matter
				long timeSinceL = currentTime - TipTracker.tipTime.get(args[0]);
				int timeSince = (int) timeSinceL / 1000 / 60;
				if (timeSince > 30)
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/tip " + args[0]);

				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
						+ "Last tipped " + args[0] + " " + EnumChatFormatting.YELLOW + timeSince + " minutes ago."));
			}
		} else {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
					EnumChatFormatting.BOLD + "" + EnumChatFormatting.GREEN + "Tipped in last hour"));
			Set<String> usernames = TipTracker.tipTime.keySet();
			for (String key : usernames) {
				long timeSinceL = currentTime - TipTracker.tipTime.get(key);
				int timeSince = (int) timeSinceL / 1000 / 60;
				if (timeSince < 60)
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
							EnumChatFormatting.GREEN + key + ": " + EnumChatFormatting.YELLOW + timeSince + "m ago"));

			}
		}

	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/lasttip <username>";
	}
}
