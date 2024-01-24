package com.lypaka.hmmanager.Listeners;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.areamanager.Regions.Region;
import com.lypaka.areamanager.Regions.RegionHandler;
import com.lypaka.hmmanager.HMManager;
import com.lypaka.hmmanager.HMs.Defog.DefogSpot;
import com.lypaka.hmmanager.HMs.HMHandler;
import com.lypaka.lypakautils.API.PlayerMovementEvent;
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
