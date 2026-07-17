package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Feb 19, 2011
 * Time: 4:42:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportDirectoryPathRpc implements IsSerializable {

    private String directoryName;
    private ArrayList<SelectItem> files;
    private Integer id;

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public ArrayList<SelectItem> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<SelectItem> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ReportDirectoryPathRpc) {
            return ((ReportDirectoryPathRpc) obj).getDirectoryName().equals(getDirectoryName());
        }
        return super.equals(obj);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
