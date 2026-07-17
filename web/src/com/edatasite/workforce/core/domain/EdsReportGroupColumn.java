package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "group_column")
public class EdsReportGroupColumn extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "report_code")
    private String reportCode;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_columns")
    private String groupOfColumns;

    public EdsReportGroupColumn(Integer objectID, String reportCode, String groupName, String groupOfColumns) {
        this.objectID = objectID;
        this.reportCode = reportCode;
        this.groupName = groupName;
        this.groupOfColumns = groupOfColumns;
    }

    public EdsReportGroupColumn() {
    }


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupOfColumns() {
        return groupOfColumns;
    }

    public void setGroupOfColumns(String groupOfColumns) {
        this.groupOfColumns = groupOfColumns;
    }
}
