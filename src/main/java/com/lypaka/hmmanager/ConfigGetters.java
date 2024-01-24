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
    public static boolean cutMoveRequired;

    public static List<String> defogFiles;
    public static Map<String, String> defogMessages;
    public static String defogPermission;
    public static boolean defogMoveRequired;

    public static List<String> diveBlockIDs;
    public static List<String> diveFiles;
    public static Map<String, String> diveMessages;
    public static String divePermission;
    public static boolean diveMoveRequired;
    public static boolean diveMountForced;

    public static void load() throws ObjectMappingException {

        cutBlockIDs = HMManager.configManager.getConfigNode(0, "Cut", "Block-IDs").getList(TypeToken.of(String.class));
        cutFiles = HMManager.configManager.getConfigNode(0, "Cut", "Files").getList(TypeToken.of(String.class));
        cutMessages = HMManager.configManager.getConfigNode(0, "Cut", "Messages").getValue(new TypeToken<Map<String, String>>() {});
        cutPermission = HMManager.configManager.getConfigNode(0, "Cut", "Permission").getString();
        cutMoveRequired = HMManager.configManager.getConfigNode(0, "Cut", "Require-Move").getBoolean();

        defogFiles = HMManager.configManager.getConfigNode(0, "Defog", "Files").getList(TypeToken.of(String.class));
        defogMessages = HMManager.configManager.getConfigNode(0, "Defog", "Messages").getValue(new TypeToken<Map<String, String>>() {});
        defogPermission = HMManager.configManager.getConfigNode(0, "Defog", "Permission").getString();
        defogMoveRequired = HMManager.configManager.getConfigNode(0, "Defog", "Require-Move").getBoolean();

        diveBlockIDs = HMManager.configManager.getConfigNode(0, "Dive", "Block-IDs").getList(TypeToken.of(String.class));
        diveFiles = HMManager.configManager.getConfigNode(0, "Dive", "Files").getList(TypeToken.of(String.class));
        diveMessages = HMManager.configManager.getConfigNode(0, "Dive", "Messages").getValue(new TypeToken<Map<String, String>>() {});
        divePermission = HMManager.configManager.getConfigNode(0, "Dive", "Permission").getString();
        diveMoveRequired = HMManager.configManager.getConfigNode(0, "Dive", "Require-Move").getBoolean();
        diveMountForced = HMManager.configManager.getConfigNode(0, "Dive", "Ride-Pokemon").getBoolean();

    }

}
