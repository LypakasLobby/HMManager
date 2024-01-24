package com.lypaka.hmmanager.Listeners;

import com.lypaka.areamanager.API.AreaEvents.AreaLeaveEvent;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Regions.RegionHandler;
import com.lypaka.hmmanager.HMs.HMHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AreaLeaveListener {

    @SubscribeEvent
    public void onAreaLeave (AreaLeaveEvent event) {

        ServerPlayerEntity player = event.getPlayer();
        Area area = event.getArea();

        HMHandler.removeDefogBlindness(player, area, RegionHandler.getRegionAtPlayer(player).getName(), "area leave");
        if (HMHandler.playersThatHaveClearedDefogInThisArea.containsKey(area)) {

            HMHandler.playersThatHaveClearedDefogInThisArea.get(area).removeIf(entry -> entry.toString().equalsIgnoreCase(player.getUniqueID().toString()));

        }

    }

}
