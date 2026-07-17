package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Hayot on 2/15/2016.
 */
public abstract class HasApprovers implements IsSerializable {
    ArrayList<ApproverItemMini> approvers = new ArrayList<>();
    ApproverItemMini currentApprover;
    ApproverItemMini prevApprover;
    ReferenceItem overallStatus;

    public ArrayList<ApproverItemMini> getApprovers() {
        if (approvers == null) {
            approvers = new ArrayList<>();
        }
        return approvers;
    }

    public void setApprovers(ArrayList<ApproverItemMini> approvers) {
        this.approvers = approvers;
    }

    public void addApprover(ApproverItemMini item) {
        getApprovers().add(item);
    }

    public ApproverItemMini getFirstApprover() {
        return getApprovers().size() > 0 ? getApprovers().get(0) : null;
    }

    public ApproverItemMini getLastApprover() {
        return getApprovers().size() > 0 ? getApprovers().get(getApprovers().size() - 1) : null;
    }

    public ApproverItemMini getCurrentApprover() {
        return currentApprover;
    }

    public ReferenceItem getCurrentStatus() {
        if (getCurrentApprover() != null) {
            return getCurrentApprover().getStatus();
        }
        return null;
    }

    public void setCurrentApprover(ApproverItemMini currentApprover) {
        this.currentApprover = currentApprover;
    }

    public ApproverItemMini getPrevApprover() {
        return prevApprover;
    }

    public void setPrevApprover(ApproverItemMini prevApprover) {
        this.prevApprover = prevApprover;
    }

    public ReferenceItem getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(ReferenceItem overallStatus) {
        this.overallStatus = overallStatus;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (ApproverItemMini approver : approvers) {
            s.append(approver.toString()).append("\n");
        }
        return s.toString();
    }
}
