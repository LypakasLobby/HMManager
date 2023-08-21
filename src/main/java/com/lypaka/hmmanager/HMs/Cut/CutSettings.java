package com.lypaka.hmmanager.HMs.Cut;

import com.lypaka.hmmanager.HMs.HMSettings;

import java.util.List;

public class CutSettings extends HMSettings {

    private final List<String> blockIDs;

    public CutSettings (List<String> blockIDs, List<String> files, String noPermissionMessage, String useMessage, String universalPermission, boolean requireMove) {

        super(files, noPermissionMessage, useMessage, universalPermission, requireMove);
        this.blockIDs = blockIDs;

    }

    public List<String> getBlockIDs() {

        return this.blockIDs;

    }

}
