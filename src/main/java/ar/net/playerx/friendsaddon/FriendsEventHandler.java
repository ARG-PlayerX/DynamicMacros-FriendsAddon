package ar.net.playerx.friendsaddon;

import ar.net.playerx.dynamicmacros.api.DynamicMacrosApi;
import ar.net.playerx.dynamicmacros.api.EventDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FriendsEventHandler {

    private static boolean listenerRegistered = false;
    private static Set<String> knownIncomingRequests = new HashSet<>();

    public static void registerEventsAndListener() {
        // Register Event Definitions for DynamicMacros Editor
        EventDefinition listChangeDef = new EventDefinition("onFriendListChange", "Fires when the Minecraft friends list or requests change.", "Friends Addon")
                .addField("count", "number", "Total number of confirmed friends")
                .addField("friends", "array", "List of friend usernames")
                .addField("incomingCount", "number", "Number of incoming requests")
                .addField("outgoingCount", "number", "Number of outgoing requests")
                .addField("incoming", "array", "List of usernames requesting friendship")
                .addField("outgoing", "array", "List of usernames you sent requests to")
                .addField("state", "string", "Synchronization state with Mojang services");
        DynamicMacrosApi.registerEvent(listChangeDef);

        EventDefinition requestDef = new EventDefinition("onFriendRequest", "Fires when a new incoming friend request is received.", "Friends Addon")
                .addField("sender", "string", "Username of the player who sent the request")
                .addField("uuid", "string", "UUID of the player who sent the request");
        DynamicMacrosApi.registerEvent(requestDef);

        EventDefinition statusDef = new EventDefinition("onFriendStatusChange", "Fires when a friend connects or changes online status.", "Friends Addon")
                .addField("player", "string", "Friend's username")
                .addField("isOnline", "boolean", "True if online, false if offline");
        DynamicMacrosApi.registerEvent(statusDef);
    }

    public static void checkAndAttachListener() {
        if (listenerRegistered) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return;
        PlayerSocialManager manager = mc.getPlayerSocialManager();
        if (manager == null) return;

        manager.addFriendListUpdateListener(() -> {
            onFriendListUpdated(manager);
        });
        listenerRegistered = true;
    }

    private static void onFriendListUpdated(PlayerSocialManager manager) {
        if (!manager.isFriendListEnabled()) return;

        List<String> friendNames = new ArrayList<>();
        for (PlayerSocialManager.PlayerData data : manager.getFriends()) {
            friendNames.add(data.name());
        }

        List<String> incomingNames = new ArrayList<>();
        Set<String> currentIncomingUuids = new HashSet<>();
        for (PlayerSocialManager.PlayerData data : manager.getIncomingRequests()) {
            incomingNames.add(data.name());
            currentIncomingUuids.add(data.id().toString());

            // Check if this is a newly received request
            if (!knownIncomingRequests.contains(data.id().toString())) {
                Map<String, Object> reqData = new HashMap<>();
                reqData.put("sender", data.name());
                reqData.put("uuid", data.id().toString());
                DynamicMacrosApi.triggerEvent("onFriendRequest", reqData, false);
            }
        }
        knownIncomingRequests = currentIncomingUuids;

        List<String> outgoingNames = new ArrayList<>();
        for (PlayerSocialManager.PlayerData data : manager.getOutgoingRequests()) {
            outgoingNames.add(data.name());
        }

        Map<String, Object> listData = new HashMap<>();
        listData.put("count", (double) friendNames.size());
        listData.put("friends", friendNames);
        listData.put("incomingCount", (double) incomingNames.size());
        listData.put("outgoingCount", (double) outgoingNames.size());
        listData.put("incoming", incomingNames);
        listData.put("outgoing", outgoingNames);
        listData.put("state", manager.getFriendListState() != null ? manager.getFriendListState().name() : "UNKNOWN");

        // Fire onFriendListChange event in DynamicMacros
        DynamicMacrosApi.triggerEvent("onFriendListChange", listData, false);
    }
}
