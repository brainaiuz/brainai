package com.edatasite.workforce.gwt.importfile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12.02.2011
 * Time: 21:08:31
 * To change this template use File | Settings | File Templates.
 */
public class CustomisedImportData implements IsSerializable {
    private Integer csvColumnId;
    private Integer systemSelectedId;

    public CustomisedImportData() {
    }

    public Integer getCsvColumnId() {
        return csvColumnId;
    }

    public void setCsvColumnId(Integer csvColumnId) {
        this.csvColumnId = csvColumnId;
    }

    public Integer getSystemSelectedId() {
        return systemSelectedId;
    }

    public void setSystemSelectedId(Integer systemSelectedId) {
        this.systemSelectedId = systemSelectedId;
    }
}
