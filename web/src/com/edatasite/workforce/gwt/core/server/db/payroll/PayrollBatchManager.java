package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollBatch;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/22/15
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
public interface PayrollBatchManager extends Manager<EdsPayrollBatch> {

    void removeEmployeesReferencebyBatch(Integer objectID);

    Integer getTotalCount();

    List<EdsPayrollBatch> getPayrollBatchList(ListingFilterParameter lfp);

    Map<Integer, Integer> getPayrollBatchEmployeeAmount();

    ArrayList<SelectItem> getPayrollBatchesForLookUp(ListingFilterParameter lfp);

    List<EdsPayrollBatch> getManagerPayrollGroups(Integer managerID);

    void removeEmployeeFromGroups(Integer employeeID);

    void removeEmployeesFromGroup(Integer objectID, Set<Integer> members);

    void addEmployeePayrolBatch(Integer objectID, Set<Integer> members);
}
