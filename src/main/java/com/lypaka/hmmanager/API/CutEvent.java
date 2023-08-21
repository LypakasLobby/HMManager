package com.lypaka.hmmanager.API;

import com.lypaka.hmmanager.HMs.Cut.CutTree;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Called when the server detects a player using Cut.
 * Cancelling the event will stop the player from using the HM, which also stops the teleportation
 */
@Cancelable
public class CutEvent extends Event {

    private final ServerPlayerEntity player;
    private final CutTree cutTree;
    private String teleportLocation;

    public CutEvent (ServerPlayerEntity player, CutTree tree, String teleportLocation) {

        this.player = player;
        this.cutTree = tree;
        this.teleportLocation = teleportLocation;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public CutTree getCutTree() {

        return this.cutTree;

    }

    public String getTeleportLocation() {

        return this.teleportLocation;

    }

    public void setTeleportLocation (String location) {

        this.teleportLocation = location;

    }

}
