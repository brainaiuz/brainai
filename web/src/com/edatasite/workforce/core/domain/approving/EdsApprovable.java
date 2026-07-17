package com.edatasite.workforce.core.domain.approving;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by Hayot on 1/31/2016.
 */
@MappedSuperclass
public abstract class EdsApprovable extends EdsTraceable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overallStatus")
    private EdsReference overallStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prevApprover")
    private EdsApprover prevApprover;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currentApprover")
    private EdsApprover currentApprover;

    public abstract List<EdsApprover> getApprovers();

    public abstract void setApprovers(List<EdsApprover> approvers);

    public EdsReference getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(EdsReference overallStatus) {
        this.overallStatus = overallStatus;
    }

    public abstract void setEntityStatus(EdsReference overallStatus);

    public ArrayList<ApproverItemMini> getMiniApproversRPC() {
        ArrayList<ApproverItemMini> result = new ArrayList<>();
        for (EdsApprover approver : getApprovers()) {
            result.add(approver.getRPCMini());
        }
        return result;
    }

    public <T extends HasApprovers> void initApproverData(T rpc) {
        rpc.setApprovers(getMiniApproversRPC());
        if (isOk(getCurrentApprover())) {
            rpc.setCurrentApprover(getCurrentApprover().getRPC());
        }
        if (isOk(getPrevApprover())) {
            rpc.setPrevApprover(getPrevApprover().getRPC());
        }
        if (isOk(getOverallStatus())) {
            rpc.setOverallStatus(getOverallStatus().getRPC());
        }
    }

    public String getApproverName(EdsApprover... approvers) {
        if (isOk(approvers)) {
            for (EdsApprover approver : approvers) {
                if (isOk(approver) && isOk(approver.getExactEmployee())) {
                    return approver.getExactEmployee().getFullName();
                }
            }
        }
        return null;

    }

    public EdsApprover getFirstApprover() {
        for (EdsApprover approver : getApprovers()) {
            return approver;
        }
        return null;
    }

    public EdsApprover getLastApprover() {
        EdsApprover result = null;
        for (EdsApprover approver : getApprovers()) {
            result = approver;
        }
        return result;
    }

    public EdsApprover getNextApprover() {
        EdsApprover result = null;
        boolean foundCurrent = false;
        for (EdsApprover approver : getApprovers()) {
            if (foundCurrent) {
                result = approver;
                break;
            }
            if (isOk(getCurrentApprover()) && getCurrentApprover().getObjectID().equals(approver.getObjectID())) {
                foundCurrent = true;
            }
        }
        return result;
    }

    public EdsApprover getPrevApprover() {
        return prevApprover;
    }

    public void setPrevApprover(EdsApprover prevApprover) {
        this.prevApprover = prevApprover;
    }

    public EdsApprover getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(EdsApprover currentApprover) {
        this.currentApprover = currentApprover;
    }

    public void updateStatus(EdsReference status) {
        if (getCurrentApprover() != null) {
            getCurrentApprover().setStatus(status);
        }
        //We must set OverallStatus=REJECTED only if thats set in action
        if (isCurrentApproverRejected() && getCurrentApprover().getOnRejectedAction().equals(ApproverItem.MARK_AS_REJECTED)) {
            setEntityStatus(getCurrentApprover().getStatus());
        }
    }

    /*
     * as every entity has it's own StatusReference
     * we need to know if this status means approved
     * please override this method and check if currentApprovers status is equal to entity's approved status
     * */
    public abstract boolean isCurrentApproverApproved();

    /*
     * as every entity has it's own StatusReference
     * we need to know if this status means rejected
     * please override this method and check if currentApprovers status is equal to entity's rejected status
     * */
    public abstract boolean isCurrentApproverRejected();

    public void jumpToNextApprover() {
        setPrevApprover(getCurrentApprover());
        boolean hasCurrentApprover = false;
        boolean hasNextApprover = false;
        for (EdsApprover approver : getApprovers()) {
            // if there is no currentApprover yet, then put the first one as currentApprover
            if (!isOk(getCurrentApprover())) {
                setCurrentApprover(approver);
                break;
            }
            // nextApproverFound will be true if the found the approver in list. and the next approver in loop will be chosen as currentApprover
            if (hasCurrentApprover) {
                setCurrentApprover(approver);
                hasNextApprover = true;
                break;
            }
            if (getCurrentApprover().getObjectID().equals(approver.getObjectID())) {
                hasCurrentApprover = true;
            }
        }
        //if not nextApproverFound, then it means we are on last stage! we need to update overAllStatus
        if (!hasNextApprover) {
            // we can take status of last approver as overallstatus, as it will come here if that approver already approved.
            setEntityStatus(getCurrentApprover().getStatus());
        }
    }

    public void jumpToFirstApprover() {
        if (getApprovers() != null && getApprovers().size() > 0) {
            getApprovers().sort(Comparator.comparing(EdsApprover::getApproverOrder));
            setCurrentApprover(getApprovers().get(0));
        }
    }

    public void jumpToPreviousApprover() {
        EdsApprover prevPrevApprover = null;
        EdsApprover prevApprover = null;
        for (EdsApprover approver : getApprovers()) {
            if (isOk(prevPrevApprover)) {
                prevApprover = approver;
            } else {
                prevPrevApprover = approver;
            }
            if (getCurrentApprover().getObjectID().equals(approver.getObjectID())) {
                int currentIndex = getApprovers().indexOf(prevApprover);
                if (currentIndex > 0) {
                    EdsApprover prev = getApprovers().get(currentIndex - 1);
                    if (prev != null) {
                        setCurrentApprover(prev);
                    }
                } else {
                    setCurrentApprover(prevApprover);
                }
                if (currentIndex >= 2) {
                    EdsApprover prevPrev = getApprovers().get(currentIndex - 2);
                    if (prevPrev != null) {
                        setPrevApprover(prevPrev);
                    }
                } else {
                    setPrevApprover(null);
                }
                break;
            }
        }
       /*
       Talked with Faxridin and we agreed that below code is incorrect
       if (getCurrentApprover().getObjectID().equals(prevPrevApprover.getObjectID())) {
            setEntityStatus(getCurrentApprover().getStatus());
        }*/
    }

    public void updateOverAllStatusIfMarked() {
        ArrayList<Integer> actions = new ArrayList<>(2);
        actions.add(ApproverItem.MARK_AS_APPROVED);
        actions.add(ApproverItem.MARK_AS_REJECTED);

        Integer onRejectedAction = getCurrentApprover().getOnRejectedAction();
        Integer onApprovedAction = getCurrentApprover().getOnApprovedAction();
        if ((isCurrentApproverRejected() && isOk(onRejectedAction) && actions.contains(onRejectedAction)) || (isCurrentApproverApproved() && isOk(onApprovedAction) && actions.contains(onApprovedAction))) {
            setEntityStatus(getStatusByMarkedAction(isCurrentApproverRejected() ? onRejectedAction : onApprovedAction));
        }
    }

    public void updateRejectedStatus() {

    }

    protected abstract EdsReference getStatusByMarkedAction(Integer integer);

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.Approvers.PREVIOUS_APPROVER)) {
            return isOk(getPrevApprover()) && isOk(getPrevApprover().getExactEmployee()) ? getPrevApprover().getExactEmployee().getName() : "";
        } else if (fieldID.equals(CustomFormConstants.Approvers.PREVIOUS_APPROVER_EMAIL)) {
            return isOk(getPrevApprover()) && isOk(getPrevApprover().getExactEmployee()) ? getPrevApprover().getExactEmployee().getEmail() : "";
        } else if (fieldID.equals(CustomFormConstants.Approvers.PREVIOUS_APPROVER_STATUS)) {
            return isOk(getPrevApprover()) && isOk(getPrevApprover().getStatus()) ? getPrevApprover().getStatus().getName() : "";
        } else if (fieldID.equals(CustomFormConstants.Approvers.CURRENT_APPROVER)) {
            return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getExactEmployee()) ? getCurrentApprover().getExactEmployee().getName() : "";
        } else if (fieldID.equals(CustomFormConstants.Approvers.CURRENT_APPROVER_EMAIL)) {
            return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getExactEmployee()) ? getCurrentApprover().getExactEmployee().getEmail() : "";
        } else if (fieldID.equals(CustomFormConstants.Approvers.CURRENT_APPROVER_STATUS)) {
            return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) ? getCurrentApprover().getStatus().getName() : "";
        } else if (fieldID.equals(CustomFormConstants.Approvers.NEXT_APPROVER)) {
            return isOk(getNextApprover()) && isOk(getNextApprover().getExactEmployee()) ? getNextApprover().getExactEmployee().getName() : "";
        } else if (fieldID.equals(CustomFormConstants.Approvers.NEXT_APPROVER_EMAIL)) {
            return isOk(getNextApprover()) && isOk(getNextApprover().getExactEmployee()) ? getNextApprover().getExactEmployee().getEmail() : "";
        } else if (fieldID.equals(CustomFormConstants.Approvers.NEXT_APPROVER_STATUS)) {
            return isOk(getNextApprover()) && isOk(getNextApprover().getStatus()) ? getNextApprover().getStatus().getName() : "";
        } else if (fieldID.equals(CustomFormConstants.STATUS)) {
            return isOk(getOverallStatus()) ? getOverallStatus().getName() : "";
        } else if (fieldID.equals(CustomFormConstants.OVERALL_STATUS)) {
            return isOk(getOverallStatus()) ? getOverallStatus().getName() : "";
        }
        return super.getRealValue(fieldID);
    }

//    public Set<EdsApprovableHistory> getApproverHistory() {
//        if (approverHistory == null) {
//            approverHistory = new HashSet<>();
//        }
//        return approverHistory;
//    }
//
//    public void setApproverHistory(Set<EdsApprovableHistory> approverHistory) {
//        this.approverHistory = approverHistory;
//    }
}
