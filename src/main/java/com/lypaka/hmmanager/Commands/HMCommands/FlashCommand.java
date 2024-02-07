package com.lypaka.hmmanager.Commands.HMCommands;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.areamanager.Regions.Region;
import com.lypaka.areamanager.Regions.RegionHandler;
import com.lypaka.hmmanager.ConfigGetters;
import com.lypaka.hmmanager.HMs.Flash.FlashSpot;
import com.lypaka.hmmanager.HMs.HMHandler;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;

public class FlashCommand {

    public FlashCommand (CommandDispatcher<CommandSource> dispatcher) {

        dispatcher.register(
                Commands.literal("flash")
                        .executes(c -> {

                            if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                Region region = RegionHandler.getRegionAtPlayer(player);
                                if (region == null) return 1;

                                if (!PermissionHandler.hasPermission(player, ConfigGetters.flashPermission.replace("%region%", region.getName().toLowerCase()))) {

                                    player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this HM!"), player.getUniqueID());
                                    return 0;

                                }
                                List<Area> areas = AreaHandler.getAreasAtPlayer(player);
                                if (areas.size() == 0) return 1;
                                int x = player.getPosition().getX();
                                int y = player.getPosition().getY();
                                int z = player.getPosition().getZ();
                                if (HMHandler.flashSpotsByRegion.containsKey(region.getName())) {

                                    List<FlashSpot> flashSpots = HMHandler.flashSpotsByRegion.get(region.getName());
                                    for (Area a : areas) {

                                        for (FlashSpot spot : flashSpots) {

                                            if (spot.getAreaName().equalsIgnoreCase(a.getName())) {

                                                int dMaxX = spot.getMaxX();
                                                int dMaxY = spot.getMaxY();
                                                int dMaxZ = spot.getMaxZ();
                                                int dMinX = spot.getMinX();
                                                int dMinY = spot.getMinY();
                                                int dMinZ = spot.getMinZ();

                                                if (x >= dMinX && x <= dMaxX) {

                                                    if (y >= dMinY && y <= dMaxY) {

                                                        if (z >= dMinZ && z <= dMaxZ) {

                                                            HMHandler.useFlash(player, spot, region.getName(), a);
                                                            return 0;

                                                        }

                                                    }

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                            return 1;

                        })
        );

    }

}
