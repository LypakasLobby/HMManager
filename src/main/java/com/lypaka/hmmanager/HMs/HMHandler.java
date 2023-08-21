package com.lypaka.hmmanager.HMs;

import com.google.common.reflect.TypeToken;
import com.lypaka.hmmanager.API.CutEvent;
import com.lypaka.hmmanager.API.HMQueueEvent;
import com.lypaka.hmmanager.ConfigGetters;
import com.lypaka.hmmanager.HMManager;
import com.lypaka.hmmanager.HMs.Cut.CutSettings;
import com.lypaka.hmmanager.HMs.Cut.CutTree;
import com.lypaka.lypakautils.ConfigurationLoaders.ComplexConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HMHandler {

    public static Map<String, HMSettings> settingsMap;
    public static Map<String, List<CutTree>> cutTreesByRegion;

    public static void loadHMs() throws ObjectMappingException {

        settingsMap = new HashMap<>();
        cutTreesByRegion = new HashMap<>();

        CutSettings settings = new CutSettings(ConfigGetters.cutBlockIDs, ConfigGetters.cutFiles, ConfigGetters.cutMessages.get("No-Permission"), ConfigGetters.cutMessages.get("Use"), ConfigGetters.cutPermission, ConfigGetters.cutRequired);
        settingsMap.put("Cut", settings);

        for (String region : com.lypaka.areamanager.ConfigGetters.regionNames) {

            /** CUT START */
            HMManager.logger.info("Loading Cut for region: " + region);
            Path regionDir = ConfigUtils.checkDir(Paths.get("./config/hmmanager/" + region));
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

        }

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

        boolean passesParty = true;
        HMSettings settings = HMHandler.settingsMap.get("Cut");
        if (settings.doesRequireMove()) passesParty = false;
        String pokemonName = "N/A";

        if (!passesParty) {

            PlayerPartyStorage storage = StorageProxy.getParty(player);
            for (Pokemon pokemon : storage.getTeam()) {

                if (pokemon != null) {

                    for (Attack attack : pokemon.getMoveset().attacks) {

                        if (attack != null) {

                            if (attack.getMove().getAttackName().equalsIgnoreCase("Cut")) {

                                passesParty = true;
                                pokemonName = pokemon.getSpecies().getName();
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

        HMQueueEvent queueEvent = new HMQueueEvent(player, "Cut", passesParty, passesPermission);
        MinecraftForge.EVENT_BUS.post(queueEvent);
        boolean passes = true;
        if (!queueEvent.isCanceled()) {

            passes = queueEvent.doesPassPartyCheck() && queueEvent.doesPassPermissionCheck();

        }
        if (passes) {

            CutEvent event = new CutEvent(player, tree, teleportLocation);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {

                String[] location = event.getTeleportLocation().split(",");
                int x = Integer.parseInt(location[1]);
                int y = Integer.parseInt(location[2]);
                int z = Integer.parseInt(location[3]);

                player.setPositionAndUpdate(x, y, z);
                if (!settings.getUseMessage().equalsIgnoreCase("")) {

                    player.sendMessage(FancyText.getFormattedText(settings.getUseMessage().replace("%pokemon%", pokemonName)), player.getUniqueID());

                }

            }

        } else {

            if (!settings.getNoPermissionMessage().equalsIgnoreCase("")) {

                player.sendMessage(FancyText.getFormattedText(settings.getNoPermissionMessage()), player.getUniqueID());

            }

        }

    }

}
