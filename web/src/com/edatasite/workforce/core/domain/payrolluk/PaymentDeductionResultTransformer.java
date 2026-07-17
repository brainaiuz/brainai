package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PaymentDeductionResultTransformer extends AbstractResultTransformer {

    private final Map<Integer, PaymentDeductionObject> result = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        initAliasMap(aliases);

        Integer id = getInteger(tuple, "id");
        Integer employeeId = getInteger(tuple, "employeeid");

        BigDecimal paymentAmount = getBigDecimal(tuple, "paymentamount");
        BigDecimal percentage = getBigDecimal(tuple, "percentage");
        BigDecimal deduction = getBigDecimal(tuple, "deduction");
        BigDecimal tax = getBigDecimal(tuple, "tax");
        BigDecimal totalAmount = getBigDecimal(tuple, "totalamount");

        Integer payType = getInteger(tuple, "pay_type");

        Date startDate = getDate(tuple, "startdate");
        Date endDate = getDate(tuple, "enddate");

        DateNonConvertable startDateNC = startDate != null ? new DateNonConvertable(startDate) : null;
        DateNonConvertable endDateNC = endDate != null ? new DateNonConvertable(endDate) : null;

        Integer categoryId = getInteger(tuple, "categoryid");
        String categoryName = getString(tuple, "category_name");
        String categoryCode = getString(tuple, "category_code");
        String categoryType = getString(tuple, "category_type");

        Integer linkedCategoryId = getInteger(tuple, "linked_category_id");
        String linkedCategoryName = getString(tuple, "linked_category_name");
        String linkedCategoryCode = getString(tuple, "linked_category_code");
        String linkedCategoryType = getString(tuple, "linked_category_type");

        String systemCode = getString(tuple, "system_code");
        Boolean taxable = getBoolean(tuple, "taxable");
        Boolean excludeInCustomDeductions = getBoolean(tuple, "exclude_in_custom_deductions");
        Boolean nonMoneyType = getBoolean(tuple, "nonmoney");
        Boolean fromAllAllowances = getBoolean(tuple, "fromallallowances");
        fromAllAllowances = fromAllAllowances != null && fromAllAllowances;

        paymentAmount = BigDecimal.ZERO.compareTo(paymentAmount) == 0 ? null : paymentAmount;
        totalAmount = BigDecimal.ZERO.compareTo(totalAmount) == 0 ? null : totalAmount;
        percentage = BigDecimal.ZERO.compareTo(percentage) == 0 ? null : percentage;
        tax = BigDecimal.ZERO.compareTo(tax) == 0 ? null : tax;

        PaymentDeductionSelectItem categoryItem = new PaymentDeductionSelectItem(categoryId, categoryName, categoryCode, categoryType);
        categoryItem.setSystemCode(systemCode);
        categoryItem.setTaxable(taxable);
        categoryItem.setExcludeInCustomDeductions(excludeInCustomDeductions);
        categoryItem.setNonMoneyType(nonMoneyType);
        PaymentDeductionObject object = result.getOrDefault(id, new PaymentDeductionObject(id, employeeId, paymentAmount, payType, percentage, totalAmount, startDateNC, endDateNC, categoryItem));
        object.setAmount(paymentAmount);
        object.setFromAllAllowances(fromAllAllowances);
        object.setDeduction(deduction);
        object.setTax(tax);
        result.putIfAbsent(id, object);

        if (linkedCategoryId != null) {
            PaymentDeductionSelectItem linkedCategoryItem = new PaymentDeductionSelectItem(linkedCategoryId, linkedCategoryName, linkedCategoryCode, linkedCategoryType);
            object.getLinkedCategories().add(new PaymentDeductionObject(linkedCategoryItem));
        }
        return object;
    }

    @Override
    public List transformList(List list) {
        return new ArrayList<>(result.values());
    }
}
