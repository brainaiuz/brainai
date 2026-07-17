package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gson.Gson;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdditionalPaymentAddEditExcelHandler extends AdditionalPaymentViewExcelHandler {
    private final String GROUP_TYPE = "group";
    private final String EMPLOYEE_TYPE = "employee";
    private final String DEPARTMENT_TYPE = "department";
    private final String LOCATION_TYPE = "location";
    private final String SUPERVISOR_TYPE = "supervisor";

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        Map<String, String[]> filterMap = request.getParameterMap();
        AdditionalPayment additionalPayment = new AdditionalPayment();
        HashMap<String, String> valueMap = new HashMap<>();
        filterMap.forEach((key, val) -> {
            valueMap.put(key, (val != null && val.length > 0) ? val[0] : null);
        });
        additionalPayment.setMapValuesToFields(valueMap);
        DateFormat formatFull = new SimpleDateFormat("dd-MM-yyyy");
        if (!ServerUtils.isNullOrEmpty(valueMap.get(AdditionalPayment.DEFAULT_DATE))) {
            try {
                additionalPayment.setDefaultDate(new DateNonConvertable(formatFull.parse(valueMap.get(AdditionalPayment.DEFAULT_DATE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(additionalPayment.getObjectID());
        filterParameter.setYear(additionalPayment.getYear());
        filterParameter.setMonthId(additionalPayment.getMonthID());
        filterParameter.setBasicPlusAllowancePaymentType(additionalPayment.isBasicPlusAllowance());
        if (additionalPayment.isBasicPlusAllowance() && !ServerUtils.isNullOrEmpty(additionalPayment.getValueMap().get(AdditionalPayment.ALLOWANCE_PAYMENT_CATEGORY_CODES))) {
            filterParameter.setPaymentCategories(payrollCategoryManager.getCategoriesByCodes(additionalPayment.getValueMap().get(AdditionalPayment.ALLOWANCE_PAYMENT_CATEGORY_CODES).split(",")).stream().map(EdsPayrollCategory::createPaymentDeductionSelectItem).collect(Collectors.toCollection(ArrayList::new)));
        }
        String entityType = valueMap.get(AdditionalPayment.ENTITY_TYPE);
        if (EMPLOYEE_TYPE.equals(entityType)) {
            filterParameter.setEmployeeIDs(additionalPayment.getEmployeeIds());
        }
        if (DEPARTMENT_TYPE.equals(entityType)) {
            filterParameter.setDepartmentId(valueMap.get(AdditionalPayment.DEPARTMENT_ID) != null ? Integer.valueOf(valueMap.get(AdditionalPayment.DEPARTMENT_ID)) : null);
        }
        if (LOCATION_TYPE.equals(entityType)) {
            filterParameter.setLocationId(valueMap.get(AdditionalPayment.LOCATION_ID) != null ? Integer.valueOf(valueMap.get(AdditionalPayment.LOCATION_ID)) : null);
        }
        if (GROUP_TYPE.equals(entityType)) {
            filterParameter.setObjectId(valueMap.get(AdditionalPayment.GROUP_TYPE_ID) != null ? Integer.valueOf(valueMap.get(AdditionalPayment.GROUP_TYPE_ID)) : null);
        }
        if (SUPERVISOR_TYPE.equals(entityType)) {
            filterParameter.setSupervisorId(valueMap.get(AdditionalPayment.SUPERVISOR_ID) != null ? Integer.valueOf(valueMap.get(AdditionalPayment.SUPERVISOR_ID)) : null);
        }
        filterParameter.setResignedEmployeesIncluded(false);
        filterParameter.setCalculateByLastMonth(Boolean.valueOf(valueMap.get(AdditionalPayment.CALCULATE_BY_LAST_MONT)));
        JSONObject jsonObject = new Gson().fromJson(valueMap.get(AdditionalPayment.ADDITIONAL_PAYMENT_ITEMTABLE_DATA), JSONObject.class);
        additionalPayment.setItems(getUpdatedDeductionItems(new Gson().toJson(jsonObject.get(AdditionalPayment.UPDATED_TABLE_ITEMS)), filterParameter, jsonObject.get(AdditionalPayment.DELETED_TABLE_ITEMS).toString(), additionalPayment, Boolean.valueOf(valueMap.get(AdditionalPayment.IS_BASIC_SALARY))));
        return additionalPayment;
    }

    private List<PaymentDeductionObject> getUpdatedDeductionItems(String jsonValue, ListingFilterParameter filterParameter, String deletedItemIDs, AdditionalPayment additionalPaymentItem, Boolean isBasicSalary) {
        Gson gson = new Gson();
        HashMap<String, String>[] map = gson.fromJson(jsonValue, HashMap[].class);
        System.out.println(map.toString());
        List<PaymentDeductionObject> deductionObjects = new ArrayList<>();
        if (additionalPaymentItem.getObjectID() != null) {
            EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(additionalPaymentItem.getObjectID());
            if (additionalPayment != null) {
                deductionObjects = additionalPayment.getRPC().getItems();
            }
        } else {
            deductionObjects = payrollService.getEmployeesForAdditionalPayment(filterParameter, null).getItems();
        }
        HashMap<Integer, PaymentDeductionObject> updatedEmployees = new HashMap<>();
        for (HashMap<String, String> m : map) {
            PaymentDeductionObject deductionObject = new PaymentDeductionObject();
            deductionObject.setMapValuesToObject(m);
            if (deductionObject.getEmployee() != null && deductionObject.getEmployee().getId() != null) {
                updatedEmployees.put(deductionObject.getEmployee().getId(), deductionObject);
            }
        }
        List<PaymentDeductionObject> result = new ArrayList<>();
        for (PaymentDeductionObject object : deductionObjects) {
            PaymentDeductionObject deductionObject = object;
            boolean isUpdatedItem = false;
            if (!ServerUtils.isNullOrEmpty(deletedItemIDs) || updatedEmployees.size() > 0) {
                if (!deletedItemIDs.contains(String.valueOf(object.getEmployee().getId()))) {
                    if (updatedEmployees.get(object.getEmployee().getId()) != null) {
                        isUpdatedItem = true;
                        deductionObject = updatedEmployees.get(object.getEmployee().getId());
                        if (additionalPaymentItem.getObjectID() == null) {
                            deductionObject.setEmployerContributionCategories(object.getEmployerContributionCategories());
                            deductionObject.setAllEmployerContributionCategories(object.getAllEmployerContributionCategories());
                            deductionObject.setAllDeductionCategories(object.getAllDeductionCategories());
                            deductionObject.setLgotaBalanceMap(object.getLgotaBalanceMap());
                            deductionObject.setAllTaxCategories(object.getAllTaxCategories());
                            deductionObject.setBasicPlusAllowance(object.getBasicPlusAllowance());
                            deductionObject.setEmployeeBasicSalary(object.getEmployeeBasicSalary());
                        }
                    }
                } else {
                    deductionObject = null;
                }
            }
            if (deductionObject != null) {
                if (additionalPaymentItem.getObjectID() == null) {
                    BigDecimal reqAmount = (object.getAmount() != null && !BigDecimal.ZERO.equals(object.getAmount())) ? object.getAmount() : additionalPaymentItem.getFixedAmount();
                    BigDecimal percentage = (object.getPercentage() != null && !BigDecimal.ZERO.equals(object.getPercentage())) ? object.getPercentage() : additionalPaymentItem.getPercentage();
                    if (additionalPaymentItem.isBasicPlusAllowance() && deductionObject.getBasicPlusAllowance() != null) {
                        reqAmount = isUpdatedItem ? deductionObject.getAmount() : deductionObject.getBasicPlusAllowance().multiply(percentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    } else if (isBasicSalary) {
                        reqAmount = isUpdatedItem ? deductionObject.getAmount() : deductionObject.getEmployeeBasicSalary().multiply(percentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }
                    deductionObject.setPercentage(percentage);
                    calculationTaxAndemployerContribution(deductionObject, filterParameter, additionalPaymentItem, reqAmount);
                }
                result.add(deductionObject);
//                System.out.println(deductionObject.getEmployeeBasicSalary() + " basicSalary " + deductionObject.getPercentage() + " percentage " + deductionObject.getAmount() + " amount " + " fixed amouyn " + additionalPaymentItem.getFixedAmount());
            }
        }
        return result;
    }

    @Override
    protected AdditionalPayment getAdditionalPayment(Object object) {
        AdditionalPayment additionalPayment = (AdditionalPayment) object;
        EdsAdditionalPayment payment = additionalPaymentManager.get(additionalPayment.getObjectID());
        if (payment != null && payment.getCreator() != null && !payment.getCreator().getFullName().equals(additionalPayment.getCreator().getName())) {
            additionalPayment.setCreator(payment.getCreator().getAsSelectItem());
        }
        return additionalPayment;
    }

    private void calculationTaxAndemployerContribution(PaymentDeductionObject object, ListingFilterParameter filterParameter, AdditionalPayment additionalPayment, BigDecimal reqAmount) {
        reqAmount = reqAmount != null ? reqAmount : BigDecimal.ZERO;
        BigDecimal taxableAmount = reqAmount;
        BigDecimal taxTotal = BigDecimal.ZERO;
        BigDecimal employerContributionTotal = BigDecimal.ZERO;
        List<PaymentDeductionObject> taxCategories = new ArrayList<>();
        List<PaymentDeductionObject> employeeContributionCategories = new ArrayList<>();
        EdsPayrollCategory payrollCategory = payrollCategoryManager.get(additionalPayment.getDefaultPayrollCategoryId());
        PaymentDeductionSelectItem categoryItem = payrollCategory != null ? payrollCategory.createPaymentDeductionSelectItem() : null;
        if (!additionalPayment.isShowInPayslip() && object.getEmployee() != null && additionalPayment.getDefaultPayrollCategoryId() != null) {
            if (categoryItem != null) {
                if (PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS.equals(categoryItem.getSystemCode()) ||
                        PayrollConstants.MATERIAL_AID_TYPE_FUNERAL.equals(categoryItem.getSystemCode()) ||
                        PayrollConstants.MATERIAL_AID_TYPE_GIFT.equals(categoryItem.getSystemCode())) {
                    BigDecimal balance = object.getLgotaBalanceMap().getOrDefault(categoryItem.getSystemCode(), BigDecimal.ZERO);
                    taxableAmount = taxableAmount.subtract(balance);
                    taxableAmount = taxableAmount.compareTo(BigDecimal.ZERO) > 0 ? taxableAmount : BigDecimal.ZERO;
                }
            }
            if (object.getAllTaxCategories() != null && object.getAllTaxCategories().size() > 0 && taxableAmount.compareTo(BigDecimal.ZERO) > 0) {
                for (PaymentDeductionObject taxCategory : object.getAllTaxCategories()) {
                    boolean findCategory = false;
                    if (taxCategory.isFromAllAllowances()) {
                        findCategory = true;
                    } else if (taxCategory != null && !taxCategory.isSalaryObject() && taxCategory.getType() != null &&
                            PayrollConstants.LINKED_TYPE_PERCENTAGE_OF_BASIC_AND_ALLOWANCE.equals(taxCategory.getType()) && taxCategory.getLinkedCategories() != null && taxCategory.getLinkedCategories().size() > 0) {
                        for (PaymentDeductionObject taxAllowanceCategory : taxCategory.getLinkedCategories()) {
                            if (taxAllowanceCategory != null && taxAllowanceCategory.getCategoryItem() != null && categoryItem.getId().equals(taxAllowanceCategory.getCategoryItem().getId())) {
                                findCategory = true;
                                break;
                            }
                        }
                    }
                    if (findCategory) {
                        taxCategories.add(taxCategory);
                        BigDecimal taxAmount = taxableAmount.multiply(taxCategory.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                        taxTotal = taxTotal.add(taxAmount);
                    }
                }
            }
            if (object.getAllEmployerContributionCategories() != null && object.getAllEmployerContributionCategories().size() > 0 && taxableAmount.compareTo(BigDecimal.ZERO) > 0) {
                for (PaymentDeductionObject empContCategory : object.getAllEmployerContributionCategories()) {
                    boolean findCategory = false;
                    if (empContCategory.isFromAllAllowances()) {
                        findCategory = true;
                    } else if (empContCategory != null && !empContCategory.isSalaryObject() && empContCategory.getType() != null &&
                            PayrollConstants.LINKED_TYPE_PERCENTAGE_OF_BASIC_AND_ALLOWANCE.equals(empContCategory.getType()) && empContCategory.getLinkedCategories() != null && empContCategory.getLinkedCategories().size() > 0) {
                        for (PaymentDeductionObject empContAllowanceCategory : empContCategory.getLinkedCategories()) {
                            if (empContAllowanceCategory != null && empContAllowanceCategory.getCategoryItem() != null && additionalPayment.getDefaultPayrollCategoryId().equals(empContAllowanceCategory.getCategoryItem().getId())) {
                                findCategory = true;
                            }
                        }
                    }
                    if (findCategory) {
                        employeeContributionCategories.add(empContCategory);
                        BigDecimal empContrAmount = taxableAmount.multiply(empContCategory.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                        employerContributionTotal = employerContributionTotal.add(empContrAmount);
                    }
                }
            }
        }

        object.setAmount(reqAmount);
        object.setTax(taxTotal);
        object.setEmployerContribution(employerContributionTotal);
        object.setCategoryItem(categoryItem);
        if (additionalPayment.getDate() != null) {
            object.setPaymentDate(additionalPayment.getDate().getNonConvertedDate());
        }
        BigDecimal totalValue = reqAmount.subtract(taxTotal).setScale(2, RoundingMode.HALF_UP);
        if (object.getAdditionalPaymentDate() == null && additionalPayment.getDefaultDate() != null) {
            object.setAdditionalPaymentDate(additionalPayment.getDefaultDate());
        }

        if (!additionalPayment.isShowInPayslip() && categoryItem != null && categoryItem.isNonMoneyType()) {
            totalValue = BigDecimal.ZERO;
        }
        object.setTotalAmount(totalValue);
    }

}
