package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class RepRpc implements IsSerializable {
    private LinkedHashMap<Integer, FolderRpc> folders;
    public LinkedHashMap<Integer, FolderRpc> getFolders() {
        if (folders == null) {
            folders = new LinkedHashMap<>();
        }
        return folders;
    }

    public void setFolders(LinkedHashMap<Integer, FolderRpc> categories) {
        this.folders = categories;
    }

}
