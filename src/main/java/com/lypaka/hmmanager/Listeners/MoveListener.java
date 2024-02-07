package com.lypaka.hmmanager.Listeners;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.areamanager.Regions.Region;
import com.lypaka.areamanager.Regions.RegionHandler;
import com.lypaka.hmmanager.ConfigGetters;
import com.lypaka.hmmanager.HMManager;
import com.lypaka.hmmanager.HMs.Defog.DefogSpot;
import com.lypaka.hmmanager.HMs.Fly.FlyArea;
import com.lypaka.hmmanager.HMs.HMHandler;
import com.lypaka.lypakautils.API.PlayerMovementEvent;
import com.lypaka.lypakautils.LPPlayer;
import com.lypaka.lypakautils.LypakaUtils;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.lypaka.lypakautils.WorldStuff.WorldMap;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.UUID;

public class MoveListener {

    @SubscribeEvent
    public void onPlayerLandMovement (PlayerMovementEvent.Land event) {

        ServerPlayerEntity player = event.getPlayer();
        Region region = RegionHandler.getRegionAtPlayer(player);

        if (region == null) return;
        List<Area> areas = AreaHandler.getAreasAtPlayer(player);
        if (areas.size() == 0) return;
        if (HMHandler.defogSpotsByRegion.containsKey(region.getName())) {

            if (HMHandler.playersAffectedByDefog.contains(player.getUniqueID())) return;
            int x = player.getPosition().getX();
            int y = player.getPosition().getY();
            int z = player.getPosition().getZ();
            List<DefogSpot> defogSpots = HMHandler.defogSpotsByRegion.get(region.getName());
            for (Area area : areas) {

                if (HMHandler.playersThatHaveClearedDefogInThisArea.containsKey(area)) {

                    if (HMHandler.playersThatHaveClearedDefogInThisArea.get(area).contains(player.getUniqueID())) return;

                }
                for (DefogSpot spot : defogSpots) {

                    if (spot.getAreaName().equalsIgnoreCase(area.getName())) {

                        int dMaxX = spot.getMaxX();
                        int dMaxY = spot.getMaxY();
                        int dMaxZ = spot.getMaxZ();
                        int dMinX = spot.getMinX();
                        int dMinY = spot.getMinY();
                        int dMinZ = spot.getMinZ();

                        if (x >= dMinX && x <= dMaxX) {

                            if (y >= dMinY && y <= dMaxY) {

                                if (z >= dMinZ && z <= dMaxZ) {

                                    HMHandler.applyDefogBlindness(player, area);
                                    return;

                                }

                            }

                        }

                    }

                }

            }

        }
        if (HMHandler.flyAreasByRegion.containsKey(region.getName())) {

            String worldName = WorldMap.getWorldName(player);
            int x = player.getPosition().getX();
            int y = player.getPosition().getY();
            int z = player.getPosition().getZ();
            List<FlyArea> flyAreas = HMHandler.flyAreasByRegion.get(region.getName());
            for (Area area : areas) {

                for (FlyArea flyArea : flyAreas) {

                    if (flyArea.getUnlockLocationsByMovement().size() > 0) {

                        if (area.getName().equalsIgnoreCase(flyArea.getAreaName())) {

                            String permission = ConfigGetters.flyPermission.replace("%region%", region.getName().toLowerCase()) + "." + area.getName().toLowerCase();
                            if (!PermissionHandler.hasPermission(player, permission)) {

                                boolean atLocation = false;
                                for (String location : flyArea.getUnlockLocationsByMovement()) {

                                    String locationWorld = location.split(",")[0];
                                    int locationX = Integer.parseInt(location.split(",")[1]);
                                    int locationY = Integer.parseInt(location.split(",")[2]);
                                    int locationZ = Integer.parseInt(location.split(",")[3]);
                                    if (locationWorld.equalsIgnoreCase(worldName)) {

                                        if (locationX == x) {

                                            if (locationY == y) {

                                                if (locationZ == z) {

                                                    atLocation = true;
                                                    break;

                                                }

                                            }

                                        }

                                    }

                                }
                                if (atLocation) {

                                    LPPlayer lpPlayer = LypakaUtils.playerMap.get(player.getUniqueID());
                                    lpPlayer.addPermission(permission);
                                    lpPlayer.save(false);
                                    break;

                                }

                            }

                        }

                    }

                }

            }

        }

    }

    @SubscribeEvent
    public void onPlayerWaterMovement (PlayerMovementEvent.Swim event) {

        ServerPlayerEntity player = event.getPlayer();
        Region region = RegionHandler.getRegionAtPlayer(player);

        if (region == null) return;
        List<Area> areas = AreaHandler.getAreasAtPlayer(player);
        if (areas.size() == 0) return;
        if (HMHandler.defogSpotsByRegion.containsKey(region.getName())) {

            if (HMHandler.playersAffectedByDefog.contains(player.getUniqueID())) return;
            int x = player.getPosition().getX();
            int y = player.getPosition().getY();
            int z = player.getPosition().getZ();
            List<DefogSpot> defogSpots = HMHandler.defogSpotsByRegion.get(region.getName());
            for (Area area : areas) {

                if (HMHandler.playersThatHaveClearedDefogInThisArea.containsKey(area)) {

                    if (HMHandler.playersThatHaveClearedDefogInThisArea.get(area).contains(player.getUniqueID())) return;

                }
                for (DefogSpot spot : defogSpots) {

                    if (spot.getAreaName().equalsIgnoreCase(area.getName())) {

                        int dMaxX = spot.getMaxX();
                        int dMaxY = spot.getMaxY();
                        int dMaxZ = spot.getMaxZ();
                        int dMinX = spot.getMinX();
                        int dMinY = spot.getMinY();
                        int dMinZ = spot.getMinZ();

                        if (x >= dMinX && x <= dMaxX) {

                            if (y >= dMinY && y <= dMaxY) {

                                if (z >= dMinZ && z <= dMaxZ) {

                                    HMHandler.applyDefogBlindness(player, area);
                                    return;

                                }

                            }

                        }

                    }

                }

            }

        }

    }

}
