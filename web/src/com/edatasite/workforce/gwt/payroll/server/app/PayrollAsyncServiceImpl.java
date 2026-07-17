package com.edatasite.workforce.gwt.payroll.server.app;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.solr.component.GroupPayrunSolrComponent;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.PaymentMethodManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollBatchManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.payroll.SinglePayrunEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollAmountsTO;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollTotalTO;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * User: Murad Satimov
 * Date: 3/29/18 3:49 AM
 */
@Service
public class PayrollAsyncServiceImpl implements PayrollAsyncService {

    private final static Logger log = LoggerFactory.getLogger(PayrollAsyncServiceImpl.class);

    @Autowired
    private SolrManager solrManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private PayrollCategoryManager categoryManager;
    @Autowired
    private PayslipTableManager payslipTableManager;
    @Autowired
    private PayrollBatchManager payrollBatchManager;
    @Autowired
    private PaymentMethodManager paymentMethodManager;
    @Autowired
    private PayslipTableItemManager payslipTableItemManager;
    @Autowired
    private BaseEventsPostProcessor baseEventsPostProcessor;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private EmployeePayrollSettingsManager employeePayrollSettingsManager;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private PayrollServiceLocal payrollServiceLocal;
    @Autowired
    private GroupPayrunSolrComponent groupPayrunSolrComponent;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T getInNewTransaction(Supplier<T> supplier) {
        return supplier.get();
    }

    @Override
    @Transactional
    public Integer saveSinglePayrunItem(SinglePayrunItem item, BiConsumer<SinglePayrunItem, EdsPayslipTableItem> expenseAndPaymentFunction) {
        final EdsPayslipTable payslipTable = payslipTableManager.get(item.getGroupPayrunID());

        if (payslipTable == null) {
            return null;
        }
        EdsPayslipTableItem payslipTableItem;
        EdsPayrollCategory basicSalayCategory;
        if (item.getObjectID() != null) {
            payslipTableItem = payslipTableItemManager.get(item.getObjectID());
        } else {
            payslipTableItem = new EdsPayslipTableItem();
        }
        payslipTableItem.setPayslipTable(payslipTable);
        payslipTableItem.setEmployee(employeeManager.get(item.getEmployeeID()));
        payslipTableItem.setDriverID(item.getDriverID());
        payslipTableItem.setFromDate(item.getFromDate() != null ? item.getFromDate().getNonConvertedDate() : null);
        payslipTableItem.setToDate(item.getToDate() != null ? item.getToDate().getNonConvertedDate() : null);
        payslipTableItem.setProcessDate(item.getProcessDate() != null ? item.getProcessDate().getNonConvertedDate() : null);
        payslipTableItem.setDaysWorked(item.getDaysWorked());
        payslipTableItem.setCollection(item.getMonthlyCollection());
        payslipTableItem.setCommision(item.getComission());
        payslipTableItem.setUsedPetrol(item.getSpentFlueAmount());
        payslipTableItem.setMonthlySalik(item.getMonthlySalik());
        payslipTableItem.setBasicSalary(item.getBasicSalary());
        payslipTableItem.setDailyRate(item.getDailyRate());
        payslipTableItem.setActualMonthPay(item.getActualMonthPay());
        payslipTableItem.setAllowance(item.getAllowance());
        payslipTableItem.setAdditionalPay(item.getAdditionalPay());
        payslipTableItem.setDeduction(item.getDeduction());
        payslipTableItem.setTax(item.getTax());
        payslipTableItem.setEmployerContribution(item.getEmployerContribution());
        payslipTableItem.setExpense(item.getEmployeeExpenses() != null ? item.getEmployeeExpenses().getPaymentAmount() : BigDecimal.ZERO);
        payslipTableItem.setDescription(item.getDescription());
        payslipTableItem.setTotal(item.getTotal());
        payslipTableItem.setTotalInBase(item.getTotal().divide(Optional.ofNullable(payslipTable.getExchangeRate()).orElse(BigDecimal.ONE), RoundingMode.CEILING));
        payslipTableItem.setCurrency(payslipTable.getCurrency());
        payslipTableItem.setExchangeRate(payslipTable.getExchangeRate());
        payslipTableItem.setPensionRate(item.getPensionRate());
        payslipTableItem.setPensionValueType(item.getPensionValueType());
        payslipTableItem.setPensionAmount(item.getPensionAmount());
        payslipTableItem.setCompanyPensionAmount(item.getCompanyPensionAmount());
        payslipTableItem.setCompanyPensionRate(item.getCompanyPensionRate());
        payslipTableItem.setCompanyNonLocalPensionRate(item.getCompanyNonLocalPensionRate());
        payslipTableItem.setCompanyPensionType(item.getCompanyPensionType());
        payslipTableItem.setNonLocalPensionRate(item.getNonLocalPensionRate());
        EdsReference itemStatus = null;
        boolean valid = item.getTotal().compareTo(BigDecimal.ZERO) > -1;

        if (!Constants.PAYRUN_STATUS_DRAFT.equals(item.getStatus()) && !valid) {
            itemStatus = referenceManager.findReference(Constants.PAYRUN_STATUS, Constants.PAYRUN_STATUS_PENDING);
        } else if (Constants.PAYRUN_STATUS_APPROVED.equals(item.getStatus()) && item.getTotal().compareTo(BigDecimal.ZERO) == 0) {
            itemStatus = referenceManager.findReference(Constants.PAYRUN_STATUS, Constants.PAYRUN_STATUS_PAID);
        } else {
            itemStatus = referenceManager.findReference(Constants.PAYRUN_STATUS, item.getStatus());
        }
        if (item.getProjectItem() != null && item.getProjectItem().getId() != null) {
            payslipTableItem.setProject(projectManager.get(item.getProjectItem().getId()));
        }
        payslipTableItem.setStatus(itemStatus);
        payslipTableItem.setStatus2(payslipTable.getStatus2());
        payslipTableItem.setApprover(payslipTable.getApprover());
        payslipTableItem.setApprover2(payslipTable.getApprover2());
        payslipTableItem.setPreparer(payslipTable.getPreparer());
        payslipTableItem.setMonthID(item.getMonthID() != null ? item.getMonthID() : payslipTable.getMonthID());
        payslipTableItem.setMonth(item.getMonth() != null ? item.getMonth() : payslipTable.getMonth());
        payslipTableItem.setYear(item.getYear() != null ? item.getYear() : payslipTable.getYear());
        payslipTableItem.setFrequency(payslipTable.getFrequency());
        payslipTableItem.setCreationDate(payslipTable.getCreationDate());
        payslipTableItem.setApprovedDate(payslipTable.getApprovedDate());
        payslipTableItem.setGrossPay(item.getGross());
        payslipTableItem.setLastUpdateTime(new Date());
        payslipTableItemManager.createOrUpdate(payslipTableItem);

        if (item.getObjectID() == null) {
            baseEventsPostProcessor.registerEvent(SinglePayrunEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, payslipTableItem, userManager.getUser());
        } else {
            baseEventsPostProcessor.registerEvent(SinglePayrunEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, payslipTableItem, userManager.getUser());
        }

        String salarySettings = getEmployeeSettingValue(item.getEmployeeID(), Constants.SALARY_CATEGORY, "");
        if (salarySettings != null && !salarySettings.isEmpty()) {
            basicSalayCategory = categoryManager.get(Integer.parseInt(salarySettings));
        } else {
            basicSalayCategory = categoryManager.getCategoryByCode(Constants.BASIC_SALARY);
        }
        item.getPaymentCategories().removeIf(PaymentDeductionObject::isSalaryObject);

        final PaymentDeductionObject basicSalary = new PaymentDeductionObject();
        basicSalary.setPaymentAmount(item.getBasicSalary());
        basicSalary.setCategoryItem(basicSalayCategory.createPaymentDeductionSelectItem());
        basicSalary.setSalaryObject(true);
        item.getPaymentCategories().add(basicSalary);
        if (expenseAndPaymentFunction != null) {
            expenseAndPaymentFunction.accept(item, payslipTableItem);
        }
        return payslipTableItem.getObjectID();
    }

    @Override
    @Transactional
    public void applyGroupPayrunTotal(Integer groupPayrunId) {
        final EdsPayslipTable payslipTable = payslipTableManager.get(groupPayrunId);

        final PayrollTotalTO totalTO = payslipTableItemManager.getTotalAmountGroupId(payslipTable.getObjectID());

        if (totalTO != null && totalTO.getTotalAmount() != null) {
            payslipTable.setTotalAmount(totalTO.getTotalAmount());
            payslipTable.setTotalInBase(totalTO.getTotalAmount().divide(Optional.ofNullable(payslipTable.getExchangeRate()).orElse(BigDecimal.ONE), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        }

        PayrollAmountsTO amountsTO = payslipTableItemManager.getTotalsByGroupId(payslipTable.getObjectID());
        if (amountsTO != null && amountsTO.getBasicSalary() != null) {
            payslipTable.setBasicSalary(amountsTO.getBasicSalary());
            payslipTable.setAllowance(amountsTO.getAllowance());
            payslipTable.setPension(amountsTO.getPension());
            payslipTable.setDeduction(amountsTO.getDeduction());
            payslipTable.setTax(amountsTO.getTax());
            payslipTable.setEmployerContribution(amountsTO.getEmployerContribution());
            payslipTable.setExpense(amountsTO.getExpense());
        }

        payslipTableManager.update(payslipTable);
        addGroupPayrunToSolr(payslipTable);

        WebSocketServerObject pushMessage = new WebSocketServerObject();
        pushMessage.setEventType(WfmUiEventType.ON_PAYSLIP_SAVED);
        try {
            Integer userId = ((EdsUser) SecurityContext.getInstance().getUser()).getObjectID();
            pushMessage.setUserId(userId);
            rabbitMQService.sendWebPushNotification(pushMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public Integer createCashAdvance(CashAdvanceItem cashAdvanceItem) {
        return payrollServiceLocal.createCashAdvance(cashAdvanceItem);
    }

    @Override
    @Transactional
    public Integer createPaymentDeduction(PaymentDeductionObject paymentDeductionObject) {
        return payrollServiceLocal.createPaymentDeduction(paymentDeductionObject);
    }

    private void addGroupPayrunToSolr(EdsPayslipTable payslipTable) {
        try {
            groupPayrunSolrComponent.index(payslipTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getEmployeeSettingValue(Integer empId, String key, String defaultVal) {
        final EdsEmployeePayrollSettings settings = employeePayrollSettingsManager.getEmployeeSettingValue(empId, key);

        if (settings == null || StringUtil.isEmpty(settings.getValue())) {
            return !StringUtil.isEmpty(defaultVal) ? defaultVal : null;
        }
        return settings.getValue();
    }
}
