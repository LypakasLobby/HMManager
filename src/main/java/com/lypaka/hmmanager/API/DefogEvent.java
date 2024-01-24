package com.lypaka.hmmanager.API;

import com.lypaka.areamanager.Areas.Area;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public abstract class DefogEvent extends Event {

    private final ServerPlayerEntity player;
    private final Area area;

    public DefogEvent(ServerPlayerEntity player, Area area) {

        this.player = player;
        this.area = area;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public Area getArea() {

        return this.area;

    }

    /**
     * Called when the server attempts to blind the player with Defog
     * Cancelling the event will cancel the actual blindness, but the server will still think they have it (so it doesn't keep trying to give it to them endlessly)
     */
    @Cancelable
    public static class Apply extends DefogEvent {

        public Apply (ServerPlayerEntity player, Area area) {

            super(player, area);

        }

    }

    /**
     * Called when the server attempts to remove the Defog blindness
     * Cancelling the event will cancel the removal of the blindness. Defog will fail basically
     */
    @Cancelable
    public static class Remove extends DefogEvent {

        private final String cause;
        public Remove (ServerPlayerEntity player, Area area, String cause) {

            super(player, area);
            this.cause = cause;

        }

        public String getCause() {

            return this.cause;

        }

    }

}
