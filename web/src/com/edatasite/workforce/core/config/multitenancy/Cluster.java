package com.edatasite.workforce.core.config.multitenancy;

import java.io.Serializable;

public class Cluster implements Serializable {
    private String code;
    private ClusterConnectionConfig master;
    private ClusterConnectionConfig slave;

    public Cluster() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ClusterConnectionConfig getMaster() {
        return master;
    }

    public void setMaster(ClusterConnectionConfig master) {
        this.master = master;
    }

    public ClusterConnectionConfig getSlave() {
        return slave;
    }

    public void setSlave(ClusterConnectionConfig slave) {
        this.slave = slave;
    }
}
