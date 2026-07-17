package com.edatasite.workforce.gwt.payroll.server.app;

import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvancePayment;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance.MultiCashAdvanceItem;
import com.edatasite.workforce.gwt.core.server.app.RejectedImportRecord;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.CategoryObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Normurod on 11/5/2016.
 */
public interface PayrollServiceLocal {
    String generatePermissionQuery(String view);

    CashAdvanceItem getCashAdvancedItem(ListingFilterParameter fp);

    ListResult<CashAdvanceItem> getCashAdvanceList(ListingFilterParameter fp);

    TestRPC saveCashAdvance(CashAdvanceItem cashAdvanceItem);

    SelectItem[] getDriversForLookUp(ListingFilterParameter filterParameter);

    TestRPC saveCashAdvancePayment(CashAdvancePayment payment);

    TestRPC deleteCashAdvancePayment(Integer cashAdvanceid, Integer paymentId);

    SinglePayrunItem getSinglePayrunData(PayslipItemFilter filter);

    void approveSinglePayrun(SinglePayrunItem data);

    Integer saveSinglePayrun(SinglePayrunItem data);

    ListResult<SinglePayrunItem> getSinglePayrunList(ListingFilterParameter filterParameter);

    PaymentDeductionSelectItem[] getCategoriesForLookUp(ListingFilterParameter filterParameter);
    PaymentDeductionSelectItem getCategoryById(Integer categoryId);

    ListResult<AdditionalPayment> getAdditionalPaymentList(ListingFilterParameter filterParameter);

    BankTransferNumberData generateCashAdvanceNumberFormat();

    AdditionalPayment getAdditionalPaymentData(ListingFilterParameter fp);

    void saveAdditionalPayment(AdditionalPayment data);

    Integer saveAdditionalPayment(AdditionalPayment data, boolean forImport);

    void addAdditionalPaymentToSolr(Integer objectID);

    Integer createCategory(CategoryObject category);

    BigDecimal getPredefinedValueOfCategory(Integer employeeId, Integer categoryId);

    ArrayList<RejectedImportRecord[]> importGroupPayrun(ImportFile importFile, List<String[]> data);

    ArrayList<RejectedImportRecord[]> importPaymentDeduction(ImportFile importFile, List<String[]> data, String type);

    ArrayList<PaymentDeductionObject> getEmployeesForMultiCashAdvance(ListingFilterParameter filterParametrs);

    MultiCashAdvanceItem getMultiCashAdvanceData(Integer objectdID);

    TestRPC saveMultiCashAdvance(MultiCashAdvanceItem multiCashAdvanceItem, boolean fromView);

    ListResult<MultiCashAdvanceItem> getMultiCashAdvanceList(ListingFilterParameter filterParametrs);

    Boolean deleteMultiCashAdvance(Integer objectID);

    Integer createCashAdvance(CashAdvanceItem cashAdvanceItem);

    Integer createPaymentDeduction(PaymentDeductionObject paymentDeductionObject);

    void additionalPaymentTransaction(EdsAdditionalPayment edsAdditionalPayment);

    void approvedActionForRecurringPayDeduction(Integer objectID);

    Integer createCashAdvanceByLeaveRequest(Integer objectID);

    void createAdditionalPaymentForLeaveRequestBackups(Integer sickRequestId);

    void createAdditionalPaymentByLeaveRequest(Integer objectID);

    Integer deleteAdditionalPaymentForBackupEmployee(Integer backupEmployeeId);

    void createSinglePayrun(PayslipItemFilter itemFilter);
}
