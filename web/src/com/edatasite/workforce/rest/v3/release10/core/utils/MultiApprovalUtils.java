package com.edatasite.workforce.rest.v3.release10.core.utils;

import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MultiApprovalUtils {
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;

    /**
     * Retrieves all approval schemes based on type which was configured for this company
     *
     * @param type
     * @return
     */
    public ArrayList<SelectItem> getApprovalSchemes(String type) {
        ApprovalListResult approvalListResult = allInOneServiceLocal.getApprovers(type, null, false, null, false);
        ArrayList<SelectItem> itemTOs = new ArrayList<>();
        if (approvalListResult.getList() != null) {
            int order = 0;
            for (ApproverItem item : approvalListResult.getList()) {
                SelectItem itemTO = new SelectItem();
                itemTO.setId(item.getObjectID());
                itemTO.setName("Approver" + order);
                itemTO.setDescription("" + order);
                itemTOs.add(itemTO);
                order++;
            }
        }
        return itemTOs;
    }

    public List<ApproverItem> getApprovalSchemes2(String type) {
        ApprovalListResult approvalListResult = allInOneServiceLocal.getApprovers(type, null, false, null, false, true);
        return approvalListResult.getList() != null ? approvalListResult.getList() : new ArrayList<>();
    }

    /**
     * Generates ApproverItem list based on approverIds and its schemes
     *
     * @param approverIds
     * @param approvalSchemes
     * @return
     */
    public ArrayList<ApproverItemMini> getSelectedApprovers(List<Integer> approverIds, ArrayList<SelectItem> approvalSchemes) {
        ArrayList<ApproverItemMini> result = new ArrayList<>();
        int order = 0;
        for (Integer approverid : approverIds) {
            ApproverItemMini item = new ApproverItem();
            item.setApproverOrder(order);
            item.setClonedFrom(approvalSchemes.get(order).getId());

            SelectItem exactEmployee = new SelectItem();
            exactEmployee.setId(approverid);
            item.setExactEmployee(exactEmployee);
            result.add(item);
            order++;
        }
        return result;
    }
}
