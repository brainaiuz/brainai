package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class CopyMoveFolderTO extends ResponseData {
    private Integer selected_folder;
    private Integer destination_folder;

    public Integer getSelected_folder() {
        return selected_folder;
    }

    public void setSelected_folder(Integer selected_folder) {
        this.selected_folder = selected_folder;
    }

    public Integer getDestination_folder() {
        return destination_folder;
    }

    public void setDestination_folder(Integer destination_folder) {
        this.destination_folder = destination_folder;
    }
}
