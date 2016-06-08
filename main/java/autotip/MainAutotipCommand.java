package autotip;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Set;

public class MainAutotipCommand extends CommandBase {
    public String getCommandName() {
        return "autotip";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public boolean canSenderUseCommand(ICommandSender sender) {
        return true;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "/autotip <stats|s, info|?, messages|m, toggle|t, anon|a>";
    }

    public void processCommand(ICommandSender sender, String[] args) {
        try {
            if (args.length == 1) {
                // basically how this works is that if they change a setting, it
                // will update the variables locally, and then all the variables
                // will be rewritten to the file, so everything is saved.
                if (args[0].equalsIgnoreCase("messages") || args[0].equalsIgnoreCase("m")) {
                    if (AutotipMod.showTips) {
                        AutotipMod.showTips = false;
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                                EnumChatFormatting.GREEN + "You will no longer see successful tip messages!"));
                        FileUtil.writeVars();

                    } else {
                        AutotipMod.showTips = true;
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                                EnumChatFormatting.GREEN + "You will now see successful tip messages!"));
                        FileUtil.writeVars();

                    }
                } else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("?")) {
                    if (AutotipMod.toggle)
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                                EnumChatFormatting.GREEN + "Autotipper is " + EnumChatFormatting.YELLOW + "Enabled"));
                     else
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                                EnumChatFormatting.GREEN + "Autotipper is " + EnumChatFormatting.YELLOW + "Disabled"));


                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                            + "Tips since login: " + EnumChatFormatting.YELLOW + TipTracker.localtips));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                            + "Lifetime tips: " + EnumChatFormatting.YELLOW + AutotipMod.totalTips));

                    if (AutotipMod.showTips)
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                                + "Successful Tip Messages: " + EnumChatFormatting.YELLOW + "Shown"));
                     else
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                                + "Successful Tip Messages: " + EnumChatFormatting.YELLOW + "Hidden"));

                    if (AutotipMod.anon)
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                                EnumChatFormatting.GREEN + "Anonymous Tips: " + EnumChatFormatting.YELLOW + "Enabled"));
                     else
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                                + "Anonymous Tips: " + EnumChatFormatting.YELLOW + "Disabled"));

                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                            EnumChatFormatting.GOLD + "Type /autotip stats to see what has been earned"));
                } else if (args[0].equalsIgnoreCase("stats") || args[0].equalsIgnoreCase("s")) {
                    Set<String> coinKeys = Listener.totalCoins.keySet();
                    for (String key : coinKeys)
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                                + key + ": " + EnumChatFormatting.YELLOW + Listener.totalCoins.get(key) + " coins"));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(
                            new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "Karma: " + Listener.totalKarma));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(
                            new ChatComponentText(EnumChatFormatting.BLUE + "XP: " + TipTracker.localtips * 100));
                } else if (args[0].equalsIgnoreCase("toggle") || args[0].equalsIgnoreCase("t")) {
                    if (AutotipMod.toggle) {
                        AutotipMod.toggle = false;
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                                + "Autotipper is disabled, all functions of autotipper are off."));
                        FileUtil.writeVars();

                    } else {
                        AutotipMod.toggle = true;
                        Minecraft.getMinecraft().thePlayer.addChatMessage(
                                new ChatComponentText(EnumChatFormatting.GREEN + "Autotipper is enabled."));
                        FileUtil.writeVars();
                        AutotipMod.nextRun = 5;

                    }

                } else if (args[0].equalsIgnoreCase("anon") || args[0].equalsIgnoreCase("a")) {
                    if (AutotipMod.anon) {
                        AutotipMod.anon = false;
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                                EnumChatFormatting.GREEN + "Tips will no longer be sent anonymously"));
                        FileUtil.writeVars();

                    } else {
                        AutotipMod.anon = true;
                        Minecraft.getMinecraft().thePlayer.addChatMessage(
                                new ChatComponentText(EnumChatFormatting.GREEN + "Tips will now be sent anonymously"));
                        FileUtil.writeVars();

                    }
                } else {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(
                            new ChatComponentText(EnumChatFormatting.RED + "Usage: " + getCommandUsage(sender)));
                }

            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                        new ChatComponentText(EnumChatFormatting.RED + "Usage: " + getCommandUsage(sender)));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
