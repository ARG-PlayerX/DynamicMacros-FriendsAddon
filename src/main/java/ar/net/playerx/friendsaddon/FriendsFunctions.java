package ar.net.playerx.friendsaddon;

import ar.net.playerx.dynamicmacros.api.DynamicMacrosApi;
import ar.net.playerx.dynamicmacros.api.DynamicCallable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendsFunctions {

    public static void registerAll() {
        // 1. getFriends()
        DynamicMacrosApi.registerFunction("getFriends", new DynamicCallable() {
            @Override
            public int arity() { return 0; }
            @Override
            public Object call(Object context, List<Object> arguments) {
                PlayerSocialManager manager = Minecraft.getInstance().getPlayerSocialManager();
                List<String> list = new ArrayList<>();
                if (manager != null && manager.isFriendListEnabled()) {
                    for (PlayerSocialManager.PlayerData data : manager.getFriends()) {
                        list.add(data.name());
                    }
                }
                return list;
            }
        });

        // 2. isFriend("name")
        DynamicMacrosApi.registerFunction("isFriend", new DynamicCallable() {
            @Override
            public int arity() { return 1; }
            @Override
            public Object call(Object context, List<Object> arguments) {
                PlayerSocialManager manager = Minecraft.getInstance().getPlayerSocialManager();
                if (manager == null || !manager.isFriendListEnabled()) return false;
                String target = arguments.get(0) != null ? arguments.get(0).toString() : "";
                for (PlayerSocialManager.PlayerData data : manager.getFriends()) {
                    if (data.name().equalsIgnoreCase(target) || data.id().toString().equalsIgnoreCase(target)) {
                        return true;
                    }
                }
                return false;
            }
        });

        // 3. getFriendCount()
        DynamicMacrosApi.registerFunction("getFriendCount", new DynamicCallable() {
            @Override
            public int arity() { return 0; }
            @Override
            public Object call(Object context, List<Object> arguments) {
                PlayerSocialManager manager = Minecraft.getInstance().getPlayerSocialManager();
                if (manager != null && manager.isFriendListEnabled()) {
                    return (double) manager.getFriends().size();
                }
                return 0.0;
            }
        });

        // 4. getIncomingRequests()
        DynamicMacrosApi.registerFunction("getIncomingRequests", new DynamicCallable() {
            @Override
            public int arity() { return 0; }
            @Override
            public Object call(Object context, List<Object> arguments) {
                PlayerSocialManager manager = Minecraft.getInstance().getPlayerSocialManager();
                List<String> list = new ArrayList<>();
                if (manager != null && manager.isFriendListEnabled()) {
                    for (PlayerSocialManager.PlayerData data : manager.getIncomingRequests()) {
                        list.add(data.name());
                    }
                }
                return list;
            }
        });

        // 5. getOutgoingRequests()
        DynamicMacrosApi.registerFunction("getOutgoingRequests", new DynamicCallable() {
            @Override
            public int arity() { return 0; }
            @Override
            public Object call(Object context, List<Object> arguments) {
                PlayerSocialManager manager = Minecraft.getInstance().getPlayerSocialManager();
                List<String> list = new ArrayList<>();
                if (manager != null && manager.isFriendListEnabled()) {
                    for (PlayerSocialManager.PlayerData data : manager.getOutgoingRequests()) {
                        list.add(data.name());
                    }
                }
                return list;
            }
        });

        // 6. sendFriendRequest("name")
        DynamicMacrosApi.registerFunction("sendFriendRequest", new DynamicCallable() {
            @Override
            public int arity() { return 1; }
            @Override
            public Object call(Object context, List<Object> arguments) {
                try {
                    PlayerSocialManager manager = Minecraft.getInstance().getPlayerSocialManager();
                    if (manager != null && manager.isFriendListEnabled() && arguments.get(0) != null) {
                        String target = arguments.get(0).toString();
                        manager.sendFriendRequest(target);
                    }
                } catch (Exception e) {
                    System.err.println("[FriendsAddon] Exception sending friend request: " + e.getMessage());
                }
                return null;
            }
        });

        // 7. acceptFriendRequest("name")
        DynamicMacrosApi.registerFunction("acceptFriendRequest", new DynamicCallable() {
            @Override
            public int arity() { return 1; }
            @Override
            public Object call(Object context, List<Object> arguments) {
                try {
                    PlayerSocialManager manager = Minecraft.getInstance().getPlayerSocialManager();
                    if (manager != null && manager.isFriendListEnabled() && arguments.get(0) != null) {
                        String target = arguments.get(0).toString();
                        for (PlayerSocialManager.PlayerData data : manager.getIncomingRequests()) {
                            if (data.name().equalsIgnoreCase(target) || data.id().toString().equalsIgnoreCase(target)) {
                                manager.acceptIncomingFriendRequest(data.id());
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[FriendsAddon] Exception accepting friend request: " + e.getMessage());
                }
                return null;
            }
        });

        // 8. declineFriendRequest("name")
        DynamicMacrosApi.registerFunction("declineFriendRequest", new DynamicCallable() {
            @Override
            public int arity() { return 1; }
            @Override
            public Object call(Object context, List<Object> arguments) {
                try {
                    PlayerSocialManager manager = Minecraft.getInstance().getPlayerSocialManager();
                    if (manager != null && manager.isFriendListEnabled() && arguments.get(0) != null) {
                        String target = arguments.get(0).toString();
                        for (PlayerSocialManager.PlayerData data : manager.getIncomingRequests()) {
                            if (data.name().equalsIgnoreCase(target) || data.id().toString().equalsIgnoreCase(target)) {
                                manager.declineIncomingFriendRequest(data.id());
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[FriendsAddon] Exception declining friend request: " + e.getMessage());
                }
                return null;
            }
        });

        // 9. removeFriend("name")
        DynamicMacrosApi.registerFunction("removeFriend", new DynamicCallable() {
            @Override
            public int arity() { return 1; }
            @Override
            public Object call(Object context, List<Object> arguments) {
                try {
                    PlayerSocialManager manager = Minecraft.getInstance().getPlayerSocialManager();
                    if (manager != null && manager.isFriendListEnabled() && arguments.get(0) != null) {
                        String target = arguments.get(0).toString();
                        for (PlayerSocialManager.PlayerData data : manager.getFriends()) {
                            if (data.name().equalsIgnoreCase(target) || data.id().toString().equalsIgnoreCase(target)) {
                                manager.removeFriend(data.id());
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[FriendsAddon] Exception removing friend: " + e.getMessage());
                }
                return null;
            }
        });

        // 10. isHidden("name")
        DynamicMacrosApi.registerFunction("isHidden", new DynamicCallable() {
            @Override
            public int arity() { return 1; }
            @Override
            public Object call(Object context, List<Object> arguments) {
                PlayerSocialManager manager = Minecraft.getInstance().getPlayerSocialManager();
                if (manager != null && arguments.get(0) != null) {
                    String target = arguments.get(0).toString();
                    UUID uuid = manager.getDiscoveredUUID(target);
                    if (uuid != null) return manager.isHidden(uuid);
                }
                return false;
            }
        });

        // 11. hidePlayer("name")
        DynamicMacrosApi.registerFunction("hidePlayer", new DynamicCallable() {
            @Override
            public int arity() { return 1; }
            @Override
            public Object call(Object context, List<Object> arguments) {
                PlayerSocialManager manager = Minecraft.getInstance().getPlayerSocialManager();
                if (manager != null && arguments.get(0) != null) {
                    String target = arguments.get(0).toString();
                    UUID uuid = manager.getDiscoveredUUID(target);
                    if (uuid != null) manager.hidePlayer(uuid);
                }
                return null;
            }
        });

        // 12. showPlayer("name")
        DynamicMacrosApi.registerFunction("showPlayer", new DynamicCallable() {
            @Override
            public int arity() { return 1; }
            @Override
            public Object call(Object context, List<Object> arguments) {
                PlayerSocialManager manager = Minecraft.getInstance().getPlayerSocialManager();
                if (manager != null && arguments.get(0) != null) {
                    String target = arguments.get(0).toString();
                    UUID uuid = manager.getDiscoveredUUID(target);
                    if (uuid != null) manager.showPlayer(uuid);
                }
                return null;
            }
        });

        // 13. isPremium()
        DynamicMacrosApi.registerFunction("isPremium", new DynamicCallable() {
            @Override
            public int arity() { return 0; }
            @Override
            public Object call(Object context, List<Object> arguments) {
                Minecraft mc = Minecraft.getInstance();
                if (mc != null && mc.getUser() != null) {
                    User user = mc.getUser();
                    UUID currentUuid = user.getProfileId();
                    if (currentUuid != null) {
                        UUID offlineUuid = UUID.nameUUIDFromBytes(
                            ("OfflinePlayer:" + user.getName()).getBytes(StandardCharsets.UTF_8)
                        );
                        return !currentUuid.equals(offlineUuid);
                    }
                }
                return false;
            }
        });
    }
}
