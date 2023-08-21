package com.lypaka.hmmanager.API;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Called when the server checks a player's party and/or permissions to use a designated HM.
 * Cancelling the event cancels the permission/party check.
 * Meaning if Surf requires a Pokemon in the player's party to know the move, canceling this event will allow the player to use Surf without having a Pokemon that knows the move
 * Likewise, if Surf requires a permission to use, canceling this event will bypass the permission check allowing the player to use Surf without having the permission
 */
@Cancelable
public class HMQueueEvent extends Event {

    private final ServerPlayerEntity player;
    private final String hmName;
    private boolean passesParty;
    private boolean passesPermission;

    public HMQueueEvent (ServerPlayerEntity player, String hmName, boolean passesParty, boolean passesPermission) {

        this.player = player;
        this.hmName = hmName;
        this.passesParty = passesParty;
        this.passesPermission = passesPermission;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public String getHMName() {

        return this.hmName;

    }

    public boolean doesPassPartyCheck() {

        return this.passesParty;

    }

    public void setPassesParty (boolean passes) {

        this.passesParty = passes;

    }

    public boolean doesPassPermissionCheck() {

        return this.passesPermission;

    }

    public void setPassesPermission (boolean passes) {

        this.passesPermission = passes;

    }

}
