package com.lypaka.hmmanager.API;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.hmmanager.HMs.Dive.DiveSpot;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Called when a player runs the "/dive" command, before the server teleports the player
 * Canceling the event cancels the teleportation
 */
@Cancelable
public class DiveEvent extends Event {

    private final ServerPlayerEntity player;
    private final DiveSpot spot;
    private final Area area;

    public DiveEvent (ServerPlayerEntity player, DiveSpot spot, Area area) {

        this.player = player;
        this.spot = spot;
        this.area = area;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public DiveSpot getDiveSpot() {

        return this.spot;

    }

    public Area getArea() {

        return this.area;

    }

}
