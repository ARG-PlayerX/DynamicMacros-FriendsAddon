package ar.net.playerx.friendsaddon;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod("friendsaddon")
public class FriendsAddonMod {

    public static final String MODID = "friendsaddon";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FriendsAddonMod(IEventBus modEventBus) {
        modEventBus.addListener(this::onClientSetup);
        NeoForge.EVENT_BUS.register(this);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("[DynamicMacros] Initializing Friends Addon...");
        
        // Register API functions and events
        FriendsFunctions.registerAll();
        FriendsEventHandler.registerEventsAndListener();

        LOGGER.info("[DynamicMacros] Friends Addon initialized successfully.");
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        FriendsEventHandler.checkAndAttachListener();
    }
}
