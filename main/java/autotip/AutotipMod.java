package autotip;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

import java.io.File;
import java.io.IOException;

//some mod shit idk
@Mod(modid = AutotipMod.MODID, version = AutotipMod.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "1.8.9")
public class AutotipMod {
    public static final String MODID = "mememod";
    public static final String VERSION = "1.2.5";
    public static boolean show = false;
    public static String BOOSTERS = "http://skywars.info/boosters";
    public static boolean showTips = true;
    public static boolean toggle = true;
    public static boolean anon = false;
    public static int totalTips;
    public static boolean onHypixel = false;
    public static boolean hasRun = false;
    public static boolean hasRunStats = false;
    public static boolean tracker = false;
    public static boolean runningThread = false;
    public static int nextRun = 5;
    long unixTime;

    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        // initialize some shit
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Listener());
        ClientCommandHandler.instance.registerCommand(new MainAutotipCommand());
        ClientCommandHandler.instance.registerCommand(new LastTipCommand());

        // get settings from file
        File f = new File("config/autotip.txt");
        if (f.exists() && !f.isDirectory()) { // see if the file exists
            try {
                FileUtil.getVars();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileUtil.writeVars(); // if the file doesn't exist, write the
                // default settings to the file
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // moved to game tick to conserve memory
    @SubscribeEvent
    public void gameTick(ClientTickEvent event) {
        if (onHypixel && toggle && (unixTime != System.currentTimeMillis() / 1000L)) { // check
            // if
            // on
            // hypixel,
            // autotip
            // is
            // on,
            // and
            // only
            // runs
            // every
            // second
            nextRun--;
            if (nextRun <= 0) {
                if (!runningThread) {
                    new Thread(new TipperRunnable()).start(); // create a new thread tip
                    // thread. Creating a
                    // new thread every time
                    // (and then letting it
                    // die) conserves memory
                    System.out.println("Running tipper thread");
                }
                nextRun = 340; // set back up to 5 2/3 minutes until run
            }
        }
        unixTime = System.currentTimeMillis() / 1000L; // some shit to make this
        // run every second
    }

    @SubscribeEvent
    public void playerLoggedIn(ClientConnectedToServerEvent event) {
        if (event.manager.getRemoteAddress().toString().toLowerCase().contains(".hypixel.net")) { // check
            // if
            // logging
            // into
            // hypixel
            onHypixel = true;
            System.out.println("Joined hypixel");
            nextRun = 5; // 5 seconds until the autotip starts
        } else {
            onHypixel = false; // if it's logging into a server thats not
            // hypixel
        }

    }

    @SubscribeEvent
    public void playerLoggedOut(ClientDisconnectionFromServerEvent event) {
        onHypixel = false; // logging out of server, so its always gonna be
        // false
    }
}
