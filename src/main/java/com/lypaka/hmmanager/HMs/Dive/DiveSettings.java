package com.lypaka.hmmanager.HMs.Dive;

import com.lypaka.hmmanager.HMs.HMSettings;

import java.util.List;

public class DiveSettings extends HMSettings {

    private final List<String> blockIDs;
    private final boolean forceMount;

    public DiveSettings (List<String> blockIDs, List<String> files, String noPermissionMessage, String useMessage, String universalPermission, boolean requireMove, boolean forceMount) {

        super(files, noPermissionMessage, useMessage, universalPermission, requireMove);
        this.blockIDs = blockIDs;
        this.forceMount = forceMount;

    }

    public List<String> getBlockIDs() {

        return this.blockIDs;

    }

    public boolean doesForceMount() {

        return this.forceMount;

    }

}
