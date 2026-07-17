package com.edatasite.workforce.gwt.core.server.servlets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WfmCommand {

    protected List<WfmMultipartFile> files = new ArrayList<>();
    private static final WfmMultipartFile[] EMPTY = new WfmMultipartFile[0];
	private HashMap parameters = new HashMap();
    private String companyId;

    public void addFile(WfmMultipartFile file) {
        files.add(file);
    }

    public WfmMultipartFile[] getFiles() {
        return files.toArray(EMPTY);
    }

	public HashMap getParameters() {
		return parameters;
	}

	public void setParameters(HashMap parameters) {
		this.parameters = parameters;
	}

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
