package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 12/02/2018.
 */
public class StorageInfoTO extends ResponseData {
    private Double used;
    private Double free;

    public StorageInfoTO() {
    }

    public Double getUsed() {
        return used;
    }

    public void setUsed(Double used) {
        this.used = used;
    }

    public Double getFree() {
        return free;
    }

    public void setFree(Double free) {
        this.free = free;
    }
}
