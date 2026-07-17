package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public class OrgBoardDTO implements IsSerializable {
    private String companyName;
    private String description;
    private OrgBlockDTO founderBlock;
    private List<OrgBlockDTO> headerBlocks;
    private List<OrgBlockDTO> departmentBlocks;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrgBlockDTO getFounderBlock() {
        return founderBlock;
    }

    public void setFounderBlock(OrgBlockDTO founderBlock) {
        this.founderBlock = founderBlock;
    }

    public List<OrgBlockDTO> getHeaderBlocks() {
        return headerBlocks;
    }

    public void setHeaderBlocks(List<OrgBlockDTO> headerBlocks) {
        this.headerBlocks = headerBlocks;
    }

    public List<OrgBlockDTO> getDepartmentBlocks() {
        return departmentBlocks;
    }

    public void setDepartmentBlocks(List<OrgBlockDTO> departmentBlocks) {
        this.departmentBlocks = departmentBlocks;
    }
}
