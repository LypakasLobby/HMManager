package com.lypaka.hmmanager.API;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.hmmanager.HMs.Dive.DiveSpot;
import com.lypaka.hmmanager.HMs.Flash.FlashSpot;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Called when a player runs the "/flash" command, before the server gives them the night vision
 * Canceling the event cancels the effect
 */
@Cancelable
public class FlashEvent extends Event {

    private final ServerPlayerEntity player;
    private final FlashSpot spot;
    private final Area area;

    public FlashEvent (ServerPlayerEntity player, FlashSpot spot, Area area) {

        this.player = player;
        this.spot = spot;
        this.area = area;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public FlashSpot getFlashSpot() {

        return this.spot;

    }

    public Area getArea() {

        return this.area;

    }

}
