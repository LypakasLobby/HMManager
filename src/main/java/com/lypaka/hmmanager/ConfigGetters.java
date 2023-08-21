package com.lypaka.hmmanager;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.Map;

public class ConfigGetters {

    public static List<String> cutBlockIDs;
    public static List<String> cutFiles;
    public static Map<String, String> cutMessages;
    public static String cutPermission;
    public static boolean cutRequired;

    public static void load() throws ObjectMappingException {

        cutBlockIDs = HMManager.configManager.getConfigNode(0, "Cut", "Block-IDs").getList(TypeToken.of(String.class));
        cutFiles = HMManager.configManager.getConfigNode(0, "Cut", "Files").getList(TypeToken.of(String.class));
        cutMessages = HMManager.configManager.getConfigNode(0, "Cut", "Messages").getValue(new TypeToken<Map<String, String>>() {});
        cutPermission = HMManager.configManager.getConfigNode(0, "Cut", "Permission").getString();
        cutRequired = HMManager.configManager.getConfigNode(0, "Cut", "Require-Move").getBoolean();

    }

}
