package com.lypaka.hmmanager.HMs.Fly;

import com.lypaka.lypakautils.WorldStuff.WorldMap;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlyArea {

    private final String areaName;
    private final Map<String, String> unlockLocations;
    private final String[] teleportLocation;

    public FlyArea (String areaName, Map<String, String> unlockLocations, String[] teleportLocation) {

        this.areaName = areaName;
        this.unlockLocations = unlockLocations;
        this.teleportLocation = teleportLocation;

    }

    public String getAreaName() {

        return this.areaName;

    }

    public List<String> getUnlockLocationsByHealer() {

        List<String> locations = new ArrayList<>();
        for (Map.Entry<String, String> map : this.unlockLocations.entrySet()) {

            if (map.getValue().equalsIgnoreCase("healer")) {

                locations.add(map.getKey());

            }

        }

        return locations;

    }

    public List<String> getUnlockLocationsByNPC() {

        List<String> locations = new ArrayList<>();
        for (Map.Entry<String, String> map : this.unlockLocations.entrySet()) {

            if (map.getValue().equalsIgnoreCase("npc")) {

                locations.add(map.getKey());

            }

        }

        return locations;

    }

    public List<String> getUnlockLocationsByMovement() {

        List<String> locations = new ArrayList<>();
        for (Map.Entry<String, String> map : this.unlockLocations.entrySet()) {

            if (map.getValue().equalsIgnoreCase("movement")) {

                locations.add(map.getKey());

            }

        }

        return locations;

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
