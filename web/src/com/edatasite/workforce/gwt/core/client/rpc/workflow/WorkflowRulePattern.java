package com.edatasite.workforce.gwt.core.client.rpc.workflow;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Hayot on 3/16/14.
 */
public class WorkflowRulePattern implements IsSerializable {
    private WorkflowRulePattern parent;
    private ArrayList<WorkflowCondition> conditions;
    private ArrayList<WorkflowRulePattern> children;

    public WorkflowRulePattern() {

    }

    public WorkflowRulePattern getParent() {
        return parent;
    }

    public void setParent(WorkflowRulePattern parent) {
        this.parent = parent;
    }
}
