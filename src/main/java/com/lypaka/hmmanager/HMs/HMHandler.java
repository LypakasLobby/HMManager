package com.lypaka.hmmanager.HMs;

import com.google.common.reflect.TypeToken;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.hmmanager.API.CutEvent;
import com.lypaka.hmmanager.API.DefogEvent;
import com.lypaka.hmmanager.API.DiveEvent;
import com.lypaka.hmmanager.API.HMQueueEvent;
import com.lypaka.hmmanager.ConfigGetters;
import com.lypaka.hmmanager.HMManager;
import com.lypaka.hmmanager.HMs.Cut.CutSettings;
import com.lypaka.hmmanager.HMs.Cut.CutTree;
import com.lypaka.hmmanager.HMs.Defog.DefogSettings;
import com.lypaka.hmmanager.HMs.Defog.DefogSpot;
import com.lypaka.hmmanager.HMs.Dive.DiveSettings;
import com.lypaka.hmmanager.HMs.Dive.DiveSpot;
import com.lypaka.lypakautils.ConfigurationLoaders.ComplexConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.lypaka.lypakautils.MiscHandlers.WorldHelpers;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HMHandler {

    public static Map<String, HMSettings> settingsMap;
    public static Map<String, List<CutTree>> cutTreesByRegion;
    public static Map<String, List<DefogSpot>> defogSpotsByRegion;
    public static Map<String, List<DiveSpot>> diveSpotsByRegion;
    public static List<UUID> playersAffectedByDefog = new ArrayList<>();
    public static Map<Area, List<UUID>> playersThatHaveClearedDefogInThisArea = new HashMap<>();
    public static Map<UUID, Area> activeDefogAreas = new HashMap<>();

    public static void loadHMs() throws ObjectMappingException {

        settingsMap = new HashMap<>();
        cutTreesByRegion = new HashMap<>();
        defogSpotsByRegion = new HashMap<>();

        CutSettings cutSettings = new CutSettings(ConfigGetters.cutBlockIDs, ConfigGetters.cutFiles, ConfigGetters.cutMessages.get("No-Permission"), ConfigGetters.cutMessages.get("Use"), ConfigGetters.cutPermission, ConfigGetters.cutMoveRequired);
        settingsMap.put("Cut", cutSettings);

        DefogSettings defogSettings = new DefogSettings(ConfigGetters.defogFiles, ConfigGetters.defogMessages.get("No-Permission"), ConfigGetters.defogMessages.get("Use"), ConfigGetters.defogPermission, ConfigGetters.defogMoveRequired);
        settingsMap.put("Defog", defogSettings);

        DiveSettings diveSettings = new DiveSettings(ConfigGetters.diveBlockIDs, ConfigGetters.diveFiles, ConfigGetters.diveMessages.get("No-Permission"), ConfigGetters.diveMessages.get("Use"), ConfigGetters.divePermission, ConfigGetters.diveMoveRequired, ConfigGetters.diveMountForced);
        settingsMap.put("Dive", diveSettings);

        for (String region : com.lypaka.areamanager.ConfigGetters.regionNames) {

            Path regionDir = ConfigUtils.checkDir(Paths.get("./config/hmmanager/" + region));

            /** CUT START */
            HMManager.logger.info("Loading Cut for region: " + region);
            ComplexConfigManager cutCCM = new ComplexConfigManager(ConfigGetters.cutFiles, "Cut", "cutTemplate.conf", regionDir, HMManager.class, HMManager.MOD_NAME, HMManager.MOD_ID, HMManager.logger);
            cutCCM.init();
            for (int i = 0; i < ConfigGetters.cutFiles.size(); i++) {

                List<String> cutTreeLocations = cutCCM.getConfigNode(i, "Location").getList(TypeToken.of(String.class));
                String cutMode = cutCCM.getConfigNode(i, "Mode").getString();
                int greaterThanOrEqualToCheckedValue = cutCCM.getConfigNode(i, "Options", ">=", "Checked-Value").getInt();
                String greaterThanOrEqualToTeleportLocation = cutCCM.getConfigNode(i, "Options", ">=", "Teleport-To").getString();
                int lessThanOrEqualToCheckedValue = cutCCM.getConfigNode(i, "Options", "<=", "Checked-Value").getInt();
                String lessThanOrEqualToTeleportLocation = cutCCM.getConfigNode(i, "Options", "<=", "Teleport-To").getString();
                CutTree cutTree = new CutTree(cutTreeLocations, cutMode, greaterThanOrEqualToCheckedValue, greaterThanOrEqualToTeleportLocation, lessThanOrEqualToCheckedValue, lessThanOrEqualToTeleportLocation);
                List<CutTree> cutTreeList = new ArrayList<>();
                if (cutTreesByRegion.containsKey(region)) {

                    cutTreeList = cutTreesByRegion.get(region);

                }
                cutTreeList.add(cutTree);
                cutTreesByRegion.put(region, cutTreeList);

            }

            HMManager.logger.info("Finished loading Cut for region: " + region);
            /** CUT END */

            /** DEFOG START */
            HMManager.logger.info("Loading Defog for region: " + region);
            ComplexConfigManager defogCCM = new ComplexConfigManager(ConfigGetters.defogFiles, "Defog", "defogTemplate.conf", regionDir, HMManager.class, HMManager.MOD_NAME, HMManager.MOD_ID, HMManager.logger);
            defogCCM.init();
            for (int i = 0 ; i < ConfigGetters.defogFiles.size(); i++) {

                String areaName = defogCCM.getConfigNode(i, "Area-Name").getString();
                int maxX = defogCCM.getConfigNode(i, "Location", "Max-X").getInt();
                int maxY = defogCCM.getConfigNode(i, "Location", "Max-Y").getInt();
                int maxZ = defogCCM.getConfigNode(i, "Location", "Max-Z").getInt();
                int minX = defogCCM.getConfigNode(i, "Location", "Min-X").getInt();
                int minY = defogCCM.getConfigNode(i, "Location", "Min-Y").getInt();
                int minZ = defogCCM.getConfigNode(i, "Location", "Min-Z").getInt();
                DefogSpot defogSpot = new DefogSpot(areaName, maxX, maxY, maxZ, minX, minY, minZ);
                List<DefogSpot> spots = new ArrayList<>();
                if (defogSpotsByRegion.containsKey(region)) {

                    spots = defogSpotsByRegion.get(region);

                }
                spots.add(defogSpot);
                defogSpotsByRegion.put(region, spots);

            }

            HMManager.logger.info("Finished loading Defog for region: " + region);
            /** DEFOG END */

            /** DIVE START */
            HMManager.logger.info("Loading Dive for region: " + region);
            ComplexConfigManager diveCCM = new ComplexConfigManager(ConfigGetters.diveFiles, "Dive", "diveTemplate.conf", regionDir, HMManager.class, HMManager.MOD_NAME, HMManager.MOD_ID, HMManager.logger);
            diveCCM.init();
            for (int i = 0; i < ConfigGetters.diveFiles.size(); i++) {

                String areaName = diveCCM.getConfigNode(i, "Area-Name").getString();
                int maxX = diveCCM.getConfigNode(i, "Location", "Max-X").getInt();
                int maxY = diveCCM.getConfigNode(i, "Location", "Max-Y").getInt();
                int maxZ = diveCCM.getConfigNode(i, "Location", "Max-Z").getInt();
                int minX = diveCCM.getConfigNode(i, "Location", "Min-X").getInt();
                int minY = diveCCM.getConfigNode(i, "Location", "Min-Y").getInt();
                int minZ = diveCCM.getConfigNode(i, "Location", "Min-Z").getInt();
                String[] teleportLocation = diveCCM.getConfigNode(i, "Teleport-Location").getString().split(",");
                DiveSpot diveSpot = new DiveSpot(areaName, maxX, maxY, maxZ, minX, minY, minZ, teleportLocation);
                List<DiveSpot> spots = new ArrayList<>();
                if (diveSpotsByRegion.containsKey(region)) {

                    spots = diveSpotsByRegion.get(region);

                }
                spots.add(diveSpot);
                diveSpotsByRegion.put(region, spots);

            }

            HMManager.logger.info("Finished loading Dive for region: " + region);
            /** DIVE END */

        }

    }

    public static boolean passesHMRequirements (ServerPlayerEntity player, String hm, String region) {

        boolean passesParty = true;
        HMSettings settings = HMHandler.settingsMap.get(hm);
        if (settings.doesRequireMove()) passesParty = false;

        if (!passesParty) {

            PlayerPartyStorage storage = StorageProxy.getParty(player);
            for (Pokemon pokemon : storage.getTeam()) {

                if (pokemon != null) {

                    for (Attack attack : pokemon.getMoveset().attacks) {

                        if (attack != null) {

                            if (attack.getMove().getAttackName().equalsIgnoreCase(hm)) {

                                passesParty = true;
                                break;

                            }

                        }

                    }

                }

                if (passesParty) break;

            }

        }
        boolean passesPermission = false;
        if (!settings.getUniversalPermission().equalsIgnoreCase("")) {

            if (PermissionHandler.hasPermission(player, settings.getUniversalPermission().replace("%region%", region.toLowerCase()))) {

                passesPermission = true;

            }

        }

        HMQueueEvent queueEvent = new HMQueueEvent(player, hm, passesParty, passesPermission);
        MinecraftForge.EVENT_BUS.post(queueEvent);
        boolean passes = true;
        if (!queueEvent.isCanceled()) {

            passes = queueEvent.doesPassPartyCheck() && queueEvent.doesPassPermissionCheck();

        }

        return passes;

    }

    public static void useDive (ServerPlayerEntity player, DiveSpot spot, String region, Area area) {

        boolean passes = passesHMRequirements(player, "Dive", region);
        if (passes) {

            DiveEvent event = new DiveEvent(player, spot, area);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {

                String world = spot.getTeleportWorldName();
                int x = spot.getTeleportX();
                int y = spot.getTeleportY();
                int z = spot.getTeleportZ();
                WorldHelpers.teleportPlayer(player, world, x, y, z, player.cameraYaw, player.rotationPitch);
                HMSettings settings = HMHandler.settingsMap.get("Dive");
                if (!settings.getUseMessage().equalsIgnoreCase("")) {

                    Pokemon pokemon = getPokemonUsingHM(player, "Dive");
                    String name = pokemon == null ? "N/A" : pokemon.getSpecies().getName();
                    player.sendMessage(FancyText.getFormattedText(settings.getUseMessage().replace("%pokemon%", name)), player.getUniqueID());
                    DiveSettings diveSettings = (DiveSettings) HMHandler.settingsMap.get("Dive");
                    if (diveSettings.doesForceMount()) {

                        performAutomaticRemount(player, pokemon);

                    }

                }

            }

        }

    }

    private static void performAutomaticRemount (ServerPlayerEntity player, Pokemon pokemon) {

        pokemon.setGrowth(EnumGrowth.Microscopic);
        pokemon.getPersistentData().putBoolean("Catchable", false);
        PixelmonEntity pixelmon = pokemon.getOrSpawnPixelmon(player.world, player.getPosX(), player.getPosY(), player.getPosZ());
        player.startRiding(pixelmon, true);

    }

    public static void useCut (ServerPlayerEntity player, CutTree tree, String region) {

        String teleportLocation = null;
        int playerCoord;
        if (tree.getMode().equalsIgnoreCase("x")) {

            playerCoord = player.getPosition().getX();

        } else {

            playerCoord = player.getPosition().getZ();

        }
        if (playerCoord >= tree.getGreaterThanOrEqualToCheckedValue()) {

            teleportLocation = tree.getGreaterThanOrEqualToTeleportLocation();

        } else if (playerCoord <= tree.getLessThanOrEqualToCheckedValue()) {

            teleportLocation = tree.getLessThanOrEqualToTeleportLocation();

        }

        if (teleportLocation == null) {

            HMManager.logger.error("Detected null teleport location for a Cut tree in the " + region + " region!");
            return;

        }


        HMSettings settings = HMHandler.settingsMap.get("Cut");
        if (passesHMRequirements(player, "Cut", region)) {

            CutEvent event = new CutEvent(player, tree, teleportLocation);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {

                String[] location = event.getTeleportLocation().split(",");
                int x = Integer.parseInt(location[1]);
                int y = Integer.parseInt(location[2]);
                int z = Integer.parseInt(location[3]);

                player.setPositionAndUpdate(x, y, z);
                if (!settings.getUseMessage().equalsIgnoreCase("")) {

                    Pokemon pokemon = getPokemonUsingHM(player, "Cut");
                    String name = pokemon == null ? "N/A" : pokemon.getSpecies().getName();
                    player.sendMessage(FancyText.getFormattedText(settings.getUseMessage().replace("%pokemon%", name)), player.getUniqueID());

                }

            }

        } else {

            if (!settings.getNoPermissionMessage().equalsIgnoreCase("")) {

                player.sendMessage(FancyText.getFormattedText(settings.getNoPermissionMessage()), player.getUniqueID());

            }

        }

    }

    private static Pokemon getPokemonUsingHM (ServerPlayerEntity player, String move) {

        Pokemon user = null;
        PlayerPartyStorage storage = StorageProxy.getParty(player);
        for (Pokemon pokemon : storage.getTeam()) {

            if (pokemon != null) {

                for (Attack attack : pokemon.getMoveset().attacks) {

                    if (attack != null) {

                        if (attack.getMove().getAttackName().equalsIgnoreCase(move)) {

                            user = pokemon;
                            break;

                        }

                    }

                }

            }

        }

        return user;

    }

    public static void applyDefogBlindness (ServerPlayerEntity player, Area area) {

        DefogEvent.Apply event = new DefogEvent.Apply(player, area);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {

            player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 9999, 5, true, false));

        }
        playersAffectedByDefog.add(player.getUniqueID());
        activeDefogAreas.put(player.getUniqueID(), area);

    }

    public static void removeDefogBlindness (ServerPlayerEntity player, Area area, String region, String cause) {

        HMSettings settings = HMHandler.settingsMap.get("Defog");
        playersAffectedByDefog.removeIf(entry -> {

            if (entry.toString().equalsIgnoreCase(player.getUniqueID().toString())) {

                boolean passes = passesHMRequirements(player, "Defog", region);
                if (cause.equalsIgnoreCase("area leave")) passes = true; // player is not using Defog, they are leaving the area so there's no need to keep blinding them
                if (passes) {

                    DefogEvent.Remove removeEvent = new DefogEvent.Remove(player, area, cause);
                    MinecraftForge.EVENT_BUS.post(removeEvent);
                    if (!removeEvent.isCanceled()) {

                        player.removePotionEffect(Effects.BLINDNESS);
                        if (!settings.getUseMessage().equalsIgnoreCase("")) {

                            Pokemon pokemon = getPokemonUsingHM(player, "Defog");
                            String name = pokemon == null ? "N/A" : pokemon.getSpecies().getName();
                            player.sendMessage(FancyText.getFormattedText(settings.getUseMessage().replace("%pokemon%", name)), player.getUniqueID());

                        }
                        return true;

                    } else {

                        if (!settings.getNoPermissionMessage().equalsIgnoreCase("")) {

                            player.sendMessage(FancyText.getFormattedText(settings.getNoPermissionMessage()), player.getUniqueID());

                        }
                        return false;

                    }

                } else {

                    if (!settings.getNoPermissionMessage().equalsIgnoreCase("")) {

                        player.sendMessage(FancyText.getFormattedText(settings.getNoPermissionMessage()), player.getUniqueID());

                    }
                    return false;

                }

            }

            return false;

        });

    }

}
