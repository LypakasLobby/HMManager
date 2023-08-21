package com.lypaka.hmmanager.HMs.Cut;

import java.util.List;

public class CutTree {

    private final List<String> locations;
    private final String mode;
    private final int greaterThanOrEqualToCheckedValue;
    private final String greaterThanOrEqualToTeleportLocation;
    private final int lessThanOrEqualToCheckedValue;
    private final String lessThanOrEqualToTeleportLocation;

    public CutTree (List<String> locations, String mode, int greaterThanOrEqualToCheckedValue, String greaterThanOrEqualToTeleportLocation, int lessThanOrEqualToCheckedValue, String lessThanOrEqualToTeleportLocation) {

        this.locations = locations;
        this.mode = mode;
        this.greaterThanOrEqualToCheckedValue = greaterThanOrEqualToCheckedValue;
        this.greaterThanOrEqualToTeleportLocation = greaterThanOrEqualToTeleportLocation;
        this.lessThanOrEqualToCheckedValue = lessThanOrEqualToCheckedValue;
        this.lessThanOrEqualToTeleportLocation = lessThanOrEqualToTeleportLocation;

    }

    public List<String> getLocations() {

        return this.locations;

    }

    public String getMode() {

        return this.mode;

    }

    public int getGreaterThanOrEqualToCheckedValue() {

        return this.greaterThanOrEqualToCheckedValue;

    }

    public String getGreaterThanOrEqualToTeleportLocation() {

        return this.greaterThanOrEqualToTeleportLocation;

    }

    public int getLessThanOrEqualToCheckedValue() {

        return this.lessThanOrEqualToCheckedValue;

    }

    public String getLessThanOrEqualToTeleportLocation() {

        return this.lessThanOrEqualToTeleportLocation;

    }

}
