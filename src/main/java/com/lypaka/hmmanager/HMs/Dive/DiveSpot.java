package com.lypaka.hmmanager.HMs.Dive;

import com.lypaka.lypakautils.WorldStuff.WorldMap;
import net.minecraft.world.server.ServerWorld;

public class DiveSpot {

    private final String areaName;
    private final int maxX;
    private final int maxY;
    private final int maxZ;
    private final int minX;
    private final int minY;
    private final int minZ;
    private final String[] teleportLocation;

    public DiveSpot (String areaName, int maxX, int maxY, int maxZ, int minX, int minY, int minZ, String[] teleportLocation) {

        this.areaName = areaName;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.teleportLocation = teleportLocation;

    }

    public String getAreaName() {

        return this.areaName;

    }

    public int getMaxX() {

        return this.maxX;

    }

    public int getMaxY() {

        return this.maxY;

    }

    public int getMaxZ() {

        return this.maxZ;

    }

    public int getMinX() {

        return this.minX;

    }

    public int getMinY() {

        return this.minY;

    }

    public int getMinZ() {

        return this.minZ;

    }

    public String getTeleportWorldName() {

        return this.teleportLocation[0];

    }

    public ServerWorld getTeleportWorld() {

        return WorldMap.worldMap.get(this.teleportLocation[0]);

    }

    public int getTeleportX() {

        return Integer.parseInt(this.teleportLocation[1]);

    }

    public int getTeleportY() {

        return Integer.parseInt(this.teleportLocation[2]);

    }

    public int getTeleportZ() {

        return Integer.parseInt(this.teleportLocation[3]);

    }

}
