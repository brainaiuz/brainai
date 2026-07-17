package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowEmployee;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashMap;
/**
 * Created by Azazello on 4/26/16.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workflowEmployee")
public class EdsWorkflowEmployee extends EdsObject {
    private static final String DELIMETR = ";;";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "workflowID")
    private Integer workflowID;

    @Column(name = "values")
    @Type(type = "text")
    private String values;
    @Column(name = "isworkflowactionTimeBased", columnDefinition = "boolean default false")
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(Integer workflowID) {
        this.workflowID = workflowID;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public boolean isWorkflowActionTimeBased() {
        return workflowActionTimeBased;
    }

    public void setWorkflowActionTimeBased(boolean workflowActionTimeBased) {
        this.workflowActionTimeBased = workflowActionTimeBased;
    }

    public String getWorkflowActionStartTime() {
        return workflowActionStartTime;
    }

    public void setWorkflowActionStartTime(String workflowActionStartTime) {
        this.workflowActionStartTime = workflowActionStartTime;
    }

    public Integer getWorkflowActionStartTimeUnit() {
        return workflowActionStartTimeUnit;
    }

    public void setWorkflowActionStartTimeUnit(Integer workflowActionStartTimeUnit) {
        this.workflowActionStartTimeUnit = workflowActionStartTimeUnit;
    }

    public String getWorkflowActionStartTimeGranularity() {
        return workflowActionStartTimeGranularity;
    }

    public void setWorkflowActionStartTimeGranularity(String workflowActionStartTimeGranularity) {
        this.workflowActionStartTimeGranularity = workflowActionStartTimeGranularity;
    }

    public WorkflowEmployee getRPC(WorkflowEmployee item){
        item = item == null ? new WorkflowEmployee() : item;
        item.setObjectID(getObjectID());
        item.setWorkflowID(getWorkflowID());
        item.setValuesAsString(getValues());
        item.setValues(getValuesAsMap());
        item.setWorkflowActionTimeBased(isWorkflowActionTimeBased());
        item.setWorkflowActionStartTime(getWorkflowActionStartTime());
        item.setWorkflowActionStartTimeUnit(getWorkflowActionStartTimeUnit());
        item.setWorkflowActionStartTimeGranularity(getWorkflowActionStartTimeGranularity());
        return item;
    }

    public HashMap<String, String> getValuesAsMap() {
        HashMap<String,String> map = new HashMap<>();
        if(getValues() != null && getValues().length() > 0){
            String[] values = getValues().split(DELIMETR);
            int i = 0;
            while (values.length > i+1){
                map.put(values[i], values[i + 1]);
                i += 2;
            }
        }
        return map;
    }
}
