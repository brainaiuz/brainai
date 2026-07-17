package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryDetailedReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryDetailedReportItem;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SalaryReportResultTransformer extends AbstractResultTransformer {

    private final Map<Integer, SalaryDetailedReportData> result = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        initAliasMap(aliases);

        Integer employeeId = getInteger(tuple, "employeeid");
        String employeeCode = getString(tuple, "employeecode");
        String employeeName = getString(tuple, "employeename");

//        Integer monthid = getInteger(tuple, "monthid");
//        String month = getString(tuple, "month");
//        Integer year = getInteger(tuple, "year");

        Integer categoryId = getInteger(tuple, "categoryid");
        String categoryName = getString(tuple, "categoryname");
        String categoryCode = getString(tuple, "categorycode");
        String categoryType = getString(tuple, "categorytype");

        BigDecimal total = getBigDecimal(tuple, "total");

        SalaryDetailedReportData object = result.getOrDefault(employeeId, new SalaryDetailedReportData(employeeId, employeeCode, employeeName));

        SalaryDetailedReportItem item = SalaryDetailedReportItem.create()
                .setCategoryId(categoryId)
                .setCategoryName(categoryName)
                .setCategoryCode(categoryCode)
                .setCategoryType(categoryType)
                .setTotal(total);
        if (PayrollConstants.CATEGORY_PAYMENT.equals(categoryType) || PayrollConstants.CATEGORY_MATERIAL_AID.equals(categoryType)) {
            ArrayList<SalaryDetailedReportItem> list = object.getPayments().getOrDefault(PayrollConstants.CATEGORY_PAYMENT, new ArrayList<>());
            list.add(item);
            object.getPayments().putIfAbsent(PayrollConstants.CATEGORY_PAYMENT, list);
        } else if (PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION.equals(categoryType)) {
            ArrayList<SalaryDetailedReportItem> list = object.getEmployerContribution().getOrDefault(PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION, new ArrayList<>());
            list.add(item);
            object.getEmployerContribution().putIfAbsent(PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION, list);
        }  else if (categoryType != null) {
            ArrayList<SalaryDetailedReportItem> list = object.getDeductions().getOrDefault(categoryType, new ArrayList<>());
            list.add(item);
            object.getDeductions().putIfAbsent(categoryType, list);
        }

        result.putIfAbsent(employeeId, object);

        return null;
    }

    @Override
    public List transformList(List list) {
        return new ArrayList<>(result.values());
    }
}
