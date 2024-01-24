package com.lypaka.hmmanager.Commands.HMCommands;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.areamanager.Regions.Region;
import com.lypaka.areamanager.Regions.RegionHandler;
import com.lypaka.hmmanager.HMs.HMHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DefogCommand {

    private static final List<String> ALIASES = Arrays.asList("defog", "dfg");

    public DefogCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .executes(c -> {

                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                    if (HMHandler.playersAffectedByDefog.contains(player.getUniqueID())) {

                                        Region region = RegionHandler.getRegionAtPlayer(player);
                                        if (region == null) return 1;
                                        List<Area> areas = AreaHandler.getAreasAtPlayer(player);
                                        if (areas.size() == 0) return 1;

                                        for (Area area : areas) {

                                            if (HMHandler.playersThatHaveClearedDefogInThisArea.containsKey(area)) {

                                                List<UUID> uuids = HMHandler.playersThatHaveClearedDefogInThisArea.get(area);
                                                if (uuids.contains(player.getUniqueID())) {

                                                    HMHandler.removeDefogBlindness(player, area, region.getName(), "command");
                                                    break;

                                                }

                                            }

                                        }

                                    }

                                }

                                return 0;

                            })
            );

        }

    }

}
