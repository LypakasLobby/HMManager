package com.lypaka.hmmanager.HMs;

import java.util.List;

public abstract class HMSettings {

    private final List<String> files;
    private final String noPermissionMessage;
    private final String useMessage;
    private final String universalPermission;
    private final boolean requireMove;

    public HMSettings (List<String> files, String noPermissionMessage, String useMessage, String universalPermission, boolean requireMove) {

        this.files = files;
        this.noPermissionMessage = noPermissionMessage;
        this.useMessage = useMessage;
        this.universalPermission = universalPermission;
        this.requireMove = requireMove;

    }

    public List<String> getFiles() {

        return this.files;

    }

    public String getNoPermissionMessage() {

        return this.noPermissionMessage;

    }

    public String getUseMessage() {

        return this.useMessage;

    }

    public String getUniversalPermission() {

        return this.universalPermission;

    }

    public boolean doesRequireMove() {

        return this.requireMove;

    }

}
