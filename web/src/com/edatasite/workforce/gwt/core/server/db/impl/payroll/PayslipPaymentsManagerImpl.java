package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipPayments;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvancePayment;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipPaymentsManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BASIC_SALARY;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.HOUSING_ALLOWANCE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAYRUN_STATUS_APPROVED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAYRUN_STATUS_PAID;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAYRUN_STATUS_PARTIAL_PAID;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.TRANSPORTATION_ALLOWANCE;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12.06.14
 * Time: 0:27
 * To change this template use File | Settings | File Templates.
 */
@Repository("payslipPaymentsManager")
public class PayslipPaymentsManagerImpl extends BaseManager<EdsPayslipPayments> implements PayslipPaymentsManager {

    public PayslipPaymentsManagerImpl() {
        super(EdsPayslipPayments.class);
    }

    @Override
    public EdsPayslipPayments getPayslipPayment(Integer paymentDeductionID, Integer payslipItemID) {
        return (EdsPayslipPayments) findSingle("select pp from EdsPayslipPayments pp where pp.paymentDeductionID=? and pp.payslipItemID=?", paymentDeductionID, payslipItemID);
    }

    @Override
    public EdsPayslipPayments getOldPayslipPayment(Integer paymentDeductionID, Integer payslipID) {
        return (EdsPayslipPayments) findSingle("select pp from EdsPayslipPayments pp where pp.paymentDeductionID=? and pp.payslipID=?", paymentDeductionID, payslipID);
    }

    @Override
    public BigDecimal getPayedAmountByCategory(Integer paymentDeductionID) {
        return (BigDecimal) findSingle("select sum(coalesce(pp.paymentTotal, 0.00)) from EdsPayslipPayments pp where pp.paymentDeductionID=?", paymentDeductionID);
    }

    @Override
    public BigDecimal getPaymentAmount(Integer paymentDeductionID, Integer payslipItemID) {
        return (BigDecimal) findSingle("select coalesce(pp.paymentTotal, 0.00) from EdsPayslipPayments pp where pp.paymentDeductionID=? and pp.payslipItemID=?", paymentDeductionID, payslipItemID);
    }

    @Override
    public BigDecimal getPaymentAmountForOldPayslip(Integer paymentDeductionID, Integer payslipID) {
        return (BigDecimal) findSingle("select coalesce(pp.paymentTotal, 0.00) from EdsPayslipPayments pp where pp.paymentDeductionID=? and pp.payslipID=?", paymentDeductionID, payslipID);
    }

    @Override
    public List<EdsPaymentDeduction> getCategoriesForTransaction(Integer objectID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select pd.* from " + companyID + ".payslip_payments pp ");
        sql.append("left join " + companyID + ".PaymentDeduction pd on pd.id=pp.payment_deduction_id ");
        sql.append("where pp.payslip_item_id=" + objectID);
        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public List<Object[]> getEosDataFromOldPaylip(Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select p.id, p.date from " + companyID + ".payslip_payments pp ");
        sql.append("inner join " + companyID + ".payslip p on p.id=pp.payslip_id ");
        sql.append("where p.employeeid=" + employeeID);
        sql.append(" order by p.date limit 1");
        return findNative(sql.toString());
    }

    @Override
    public List<Object[]> getEosDataFromPaylipTableItem(Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select pi.id, pi.todate from " + companyID + ".payslip_payments pp ");
        sql.append("inner join " + companyID + ".paysliptableitem pi on pi.id=pp.payslip_item_id ");
        sql.append("left join  " + companyID + ".reference ref on ref.id = pi.status_id ");
        sql.append("where pi.employee_id=" + employeeID).append(" and ").append(ServerUtils.checkForDeleted("pi.deleted"));
        sql.append(" and ref.code in ('" + PAYRUN_STATUS_APPROVED + "', '" + PAYRUN_STATUS_PARTIAL_PAID + "', '" + PAYRUN_STATUS_PAID + "')");
        sql.append(" order by pi.todate desc limit 1");
        return findNative(sql.toString());
    }

    public List<EdsPayslipPayments> getByPayslipItemID(Integer payslipItemID) {
        return find("select pp from EdsPayslipPayments pp where pp.payslipItemID=?", payslipItemID);
    }

    @Override
    public List<Object[]> getEosDataFromPaylipTableItemByCategory(Integer employeeID, String categories) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select pi.id, pd.id " + companyID + ".payslip_payments pp ");
        sql.append("inner join " + companyID + ".paysliptableitem pi on pi.id=pp.payslip_item_id ");
        sql.append("left join  " + companyID + ".reference ref on ref.id = pi.status_id ");
        sql.append("left join  " + companyID + ".paymentdeduction pd on pd.id = pp.payment_deduction_id ");
        sql.append("left join  " + companyID + ".category c on c.id = pd.categoryid ");
        sql.append("where pi.employee_id=" + employeeID).append(" and ").append(ServerUtils.checkForDeleted("pi.deleted"));
        sql.append(" and ref.code in ('" + PAYRUN_STATUS_APPROVED + "', '" + PAYRUN_STATUS_PARTIAL_PAID + "', '" + PAYRUN_STATUS_PAID + "')");
        sql.append(" and c.code in(").append(categories).append(") ");
        sql.append(" order by pi.todate desc limit 1");
        return findNative(sql.toString());
    }

    @Override
    public void deleteByPayslipItemID(Integer payslipItemID) {
        update("delete from EdsPayslipPayments pp where pp.payslipItemID=?", payslipItemID);
    }

    @Override
    public void deleteByPayslipID(Integer payslipID) {
        update("delete from EdsPayslipPayments pp where pp.payslipID=?", payslipID);
    }

    @Override
    public Boolean checkPaymentDeductionForUsed(Integer paymentDeductionID) {
        return find("select pp from EdsPayslipPayments pp where pp.paymentDeductionID=?", paymentDeductionID).size() > 0;
    }

    @Override
    public LinkedHashMap<String, BigDecimal> getPayrollChartData(ListingFilterParameter fp) {
        LinkedHashMap<String, BigDecimal> resultMap = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select c.type, c.name, ")
                .append("CASE WHEN pi.exchangerate IS NOT NULL THEN pp.payment_total / pi.exchangerate ELSE pp.payment_total END, ")
                .append("pi.monthid, pi.year from " + companyID + ".payslip_payments pp \n");
        sql.append("left join " + companyID + ".paysliptableitem pi ON pp.payslip_item_id = pi.id \n");
        sql.append("left join " + companyID + ".paymentdeduction pd on pd.id = pp.payment_deduction_id \n");
        sql.append("left join " + companyID + ".category c on c.id = pd.categoryid \n");
        sql.append("where pi.deleted is not true \n");
        if (fp.getType() != null) {
            if (fp.getType() == Constants.PAYMENT) {
                sql.append("and c.type ='" + EdsPayrollCategory.PAYMENT + "'\n");
            } else {
                sql.append("and c.type ='" + EdsPayrollCategory.DEDUCTION + "'\n");
            }
        }
        if (fp.getSelectedMonth() != null && fp.getSelectedYear() != null) {
            sql.append("and pi.monthid =" + fp.getSelectedMonth() + "\n");
            sql.append("and pi.year =" + fp.getSelectedYear() + "\n");
        }

        List<Object[]> objects = (List<Object[]>) findNative(sql.toString());
        if (objects != null && objects.size() > 0) {
            for (Object[] object : objects) {
                if (resultMap.containsKey(object[1])) {
                    resultMap.put((String) object[1], resultMap.get(object[1]).add((BigDecimal) object[2]));
                } else {
                    resultMap.put((String) object[1], (BigDecimal) object[2]);
                }
            }
        }
        return resultMap;
    }

    @Override
    public LinkedHashMap<String, BigDecimal> getTotalSalaryChartData(ListingFilterParameter fp) {
        LinkedHashMap<String, BigDecimal> resultMap = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select sum(CASE WHEN pi.exchangerate IS NOT NULL THEN pp.payment_total / pi.exchangerate ELSE pp.payment_total END)\n")
                .append("from " + companyID + ".payslip_payments pp\n")
                .append("left join " + companyID + ".paysliptableitem pi ON pp.payslip_item_id = pi.id\n")
                .append("left join " + companyID + ".reference ref on ref.id = pi.status_id\n")
                .append("left join " + companyID + ".paymentdeduction pd on pd.id = pp.payment_deduction_id\n")
                .append("left join " + companyID + ".category c on c.id = pd.categoryid\n")
                .append("left join " + companyID + ".employee e on e.id = pi.employee_id\n")
                .append("left join " + companyID + ".myuser mu on mu.id = e.id\n")
                .append("left join " + companyID + ".teamemployee te on te.id = e.employeedepartmentid\n")
                .append("where pi.deleted is not true ")
                .append("and c.code in ('" + BASIC_SALARY + "', '" + TRANSPORTATION_ALLOWANCE + "', '" + HOUSING_ALLOWANCE + "')\n")
                .append("and ref.code in ('" + PAYRUN_STATUS_APPROVED + "', '" + PAYRUN_STATUS_PARTIAL_PAID + "', '" + PAYRUN_STATUS_PAID + "')\n");

        if (fp.getSelectedMonth() != null && fp.getSelectedYear() != null) {
            sql.append("and pi.monthid <=" + fp.getSelectedMonth() + "\n")
                    .append("and pi.year =" + fp.getSelectedYear() + "\n");
        }
        if (fp.getLocationId() != null) {
            sql.append("and mu.locationid is not null and mu.locationid =" + fp.getLocationId() + "\n");
        }
        if (fp.getDepartmentId() != null) {
            sql.append("and te.teamid is not null and te.teamid =" + fp.getDepartmentId() + "\n");
        }
        String sql1 = sql.toString();
        if (fp.getSelectedYear() != null) {
            sql.append("and (e.startdate is null or extract(year from e.startdate) < " + fp.getSelectedYear() + ")\n");
            sql1 = sql1 + "and e.startdate is not null and extract(year from e.startdate) = " + fp.getSelectedYear() + "\n";
        }

        List<Object> objects = (List<Object>) findNative(sql.toString());
        if (objects != null && objects.size() > 0) {
            for (Object object : objects) {
                if (object != null) {
                    resultMap.put("Old Employees", (BigDecimal) object);
                    break;
                }
            }
        }
        objects = (List<Object>) findNative(sql1);
        if (objects != null && objects.size() > 0) {
            for (Object object : objects) {
                if (object != null) {
                    resultMap.put("New Employees", (BigDecimal) object);
                    break;
                }
            }
        }
        return resultMap;
    }

    @Override
    public LinkedHashMap<String, BigDecimal> getTotalIncetivesByDepartmentChartData(ListingFilterParameter fp) {
        LinkedHashMap<String, BigDecimal> resultMap = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select * from\n")
                .append("(select t.name as dept, sum(CASE WHEN pi.exchangerate IS NOT NULL THEN pp.payment_total / pi.exchangerate ELSE pp.payment_total END) as tot\n")
                .append("from " + companyID + ".payslip_payments pp \n")
                .append("left join " + companyID + ".paysliptableitem pi ON pp.payslip_item_id = pi.id\n")
                .append("left join " + companyID + ".reference ref on ref.id = pi.status_id\n")
                .append("left join " + companyID + ".paymentdeduction pd on pd.id = pp.payment_deduction_id\n")
                .append("left join " + companyID + ".category c on c.id = pd.categoryid\n")
                .append("left join " + companyID + ".employee e on e.id = pi.employee_id\n")
                .append("left join " + companyID + ".myuser mu on mu.id = e.id\n")
                .append("left join " + companyID + ".teamemployee te on te.id = e.employeedepartmentid\n")
                .append("left join " + companyID + ".team t on t.id = te.teamid\n")
                .append("where pi.deleted is not true \n")
                .append(companyID.equals("\"57824\""/*NAOS*/) ? "and c.code in ('BONUS', 'Quarter Incentive', 'Bonus', 'Bio-Commissions', 'bonus')" :
                        "and c.code in ('BONUS')\n")
                .append("and ref.code in ('" + PAYRUN_STATUS_APPROVED + "', '" + PAYRUN_STATUS_PARTIAL_PAID + "', '" + PAYRUN_STATUS_PAID + "')\n");

        if (fp.getSelectedMonth() != null && fp.getSelectedYear() != null) {
            sql.append("and pi.monthid <=" + fp.getSelectedMonth() + "\n")
                    .append("and pi.year =" + fp.getSelectedYear() + "\n");
        }
        if (fp.getLocationId() != null) {
            sql.append("and mu.locationid is not null and mu.locationid =" + fp.getLocationId() + "\n");
        }

        sql.append("group by t.name having sum(pp.payment_total) > 0) as inc order by tot desc limit 10");

        List<Object[]> objects = (List<Object[]>) findNative(sql.toString());
        if (objects != null && objects.size() > 0) {
            for (Object[] object : objects) {
                if (resultMap.containsKey(object[0])) {
                    resultMap.put((String) object[0], resultMap.get(object[0]).add((BigDecimal) object[1]));
                } else {
                    resultMap.put((String) object[0], (BigDecimal) object[1]);
                }
            }
        }
        return resultMap;
    }

    @Override
    public LinkedHashMap<String, BigDecimal> getTotalSalaryRatioChartData(ListingFilterParameter fp) {
        LinkedHashMap<String, BigDecimal> resultMap = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder();
        String incentives, comission;
        String companyID = getCompanyId();
        sql.append("select sum(CASE WHEN pi.exchangerate IS NOT NULL THEN pp.payment_total / pi.exchangerate ELSE pp.payment_total END)\n")
                .append("from " + companyID + ".payslip_payments pp \n")
                .append("left join " + companyID + ".paysliptableitem pi ON pp.payslip_item_id = pi.id \n")
                .append("left join " + companyID + ".reference ref on ref.id = pi.status_id\n")
                .append("left join " + companyID + ".paymentdeduction pd on pd.id = pp.payment_deduction_id \n")
                .append("left join " + companyID + ".category c on c.id = pd.categoryid \n")
                .append("left join " + companyID + ".employee e on e.id = pi.employee_id\n")
                .append("left join " + companyID + ".myuser mu on mu.id = e.id\n")
                .append("left join " + companyID + ".teamemployee te on te.id = e.employeedepartmentid\n")
                .append("where pi.deleted is not true \n")
                .append("and ref.code in ('" + PAYRUN_STATUS_APPROVED + "', '" + PAYRUN_STATUS_PARTIAL_PAID + "', '" + PAYRUN_STATUS_PAID + "')\n");

        if (fp.getSelectedMonth() != null && fp.getSelectedYear() != null) {
            sql.append("and pi.monthid <=" + fp.getSelectedMonth() + "\n")
                    .append("and pi.year =" + fp.getSelectedYear() + "\n");
        }
        if (fp.getEmployeeId() != null) {
            sql.append("and e.id =" + fp.getEmployeeId() + "\n");
        }
        if (fp.getLocationId() != null) {
            sql.append("and mu.locationid is not null and mu.locationid =" + fp.getLocationId() + "\n");
        }
        if (fp.getDepartmentId() != null) {
            sql.append("and te.teamid is not null and te.teamid =" + fp.getDepartmentId() + "\n");
        }
        incentives = sql.toString();
        comission = sql.toString();
        sql.append("and c.code in ('" + BASIC_SALARY + "', '" + TRANSPORTATION_ALLOWANCE + "', '" + HOUSING_ALLOWANCE + "')\n");//Salary
        incentives = incentives + (companyID.equals("\"57824\""/*NAOS*/) ? "and c.code in ('BONUS', 'Quarter Incentive', 'Bonus', 'Bio-Commissions', 'bonus')\n" :
                "and c.code in ('BONUS')\n");
        comission = comission + "and c.code in ('COMMISSION')\n";

        List<Object> objects = (List<Object>) findNative(sql.toString());
        if (objects != null && objects.size() > 0) {
            for (Object object : objects) {
                if (object != null) {
                    resultMap.put("Salary", (BigDecimal) object);
                    break;
                }
            }
        }
        objects = (List<Object>) findNative(incentives);
        if (objects != null && objects.size() > 0) {
            for (Object object : objects) {
                if (object != null) {
                    resultMap.put("Incentive", (BigDecimal) object);
                    break;
                }
            }
        }
        objects = (List<Object>) findNative(comission);
        if (objects != null && objects.size() > 0) {
            for (Object object : objects) {
                if (object != null) {
                    resultMap.put("Comission", (BigDecimal) object);
                    break;
                }
            }
        }
        return resultMap;
    }

    @Override
    public List<EdsPayslipPayments> getCashAdvancePayments(ListingFilterParameter filter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select pp.* ")
                .append(" from ").append(getCompanyId()).append(".payslip_payments pp ")
                .append(" join ").append(getCompanyId()).append(".paymentdeduction pd on pd.id=pp.payment_deduction_id ")
                .append(" where pd.cashadvanceid = ").append(filter.getObjectId());
        if (filter.getSortField() != null) {
            sql.append(" ORDER BY ");
            if (CashAdvancePayment.AMOUNT.equals(filter.getSortField())) {
                sql.append(" pp.payment_total ");
            } else if (CashAdvancePayment.DATE.equals(filter.getSortField())) {
                sql.append(" pp.paymentdate ");
            } else if (CashAdvancePayment.REFERENCE.equals(filter.getSortField())) {
                sql.append(" pp.reference ");
            }
            if (filter.getSortDir() != null && filter.getSortDir().equals(1)) {
                sql.append(" asc ");
            }
        }
        return (List<EdsPayslipPayments>)findNative(sql.toString(), EdsPayslipPayments.class);
    }

    @Override
    public Integer getCashAdvancePaymentAmount(ListingFilterParameter filter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(pp.id) ")
                .append(" from ").append(getCompanyId()).append(".payslip_payments pp ")
                .append(" join ").append(getCompanyId()).append(".paymentdeduction pd on pd.id=pp.payment_deduction_id ")
                .append(" where pd.cashadvanceid = ").append(filter.getObjectId());
        return ((BigInteger) findNativeSingle(sql.toString())).intValue();
    }

    public LinkedHashMap<Integer, BigDecimal> getPayrollYTDChartData(ListingFilterParameter fp, boolean isPayment) {
        LinkedHashMap<Integer, BigDecimal> resultMap = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();

        sql.append("select pti.monthid, pti.year, sum(CASE WHEN pti.exchangerate IS NOT NULL THEN pp.payment_total / pti.exchangerate ELSE pp.payment_total END) \n")
                .append("from " + companyID + ".payslipTableItem pti \n")
                .append("left join " + companyID + ".payslip_payments pp on  pp.payslip_item_id=pti.id \n")
                .append("left join " + companyID + ".paymentDeduction pd on pp.payment_deduction_id=pd.id \n")
                .append("left join " + companyID + ".category cat on pd.categoryID=cat.id \n")
                .append("left join " + companyID + ".reference payst on  payst.id=pti.status_id \n");
        sql.append("where pti.deleted is not true and payst.code = 'PY_APPROVED' \n");

        if (isPayment) {
            sql.append("and cat.type ='" + EdsPayrollCategory.PAYMENT + "'\n");
        } else {
            sql.append("and cat.type ='" + EdsPayrollCategory.DEDUCTION + "'\n");
        }

        if (fp.getSelectedMonth() != null && fp.getSelectedYear() != null) {
            sql.append("and pti.monthid >=" + fp.getSelectedMonth() + "\n");
            sql.append("and pti.year >=" + fp.getSelectedYear() + "\n");
        }

        sql.append("group by pti.monthid, pti.year \n");
        sql.append("order by pti.year, pti.monthid ");

        List<Object[]> list = (List<Object[]>) findNative(sql.toString());

        for (Object[] obj : list) {
            resultMap.put((Integer) obj[0], (BigDecimal) obj[2]);
        }

        return  resultMap;
    }

    public LinkedHashMap<String, BigDecimal> getEmployeePayrollYTDChartData(ListingFilterParameter fp, boolean isPayment) {
        LinkedHashMap<String, BigDecimal> resultMap = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();

        sql.append("select cat.type, cat.code, cat.name, sum(CASE WHEN pti.exchangerate IS NOT NULL THEN pp.payment_total / pti.exchangerate ELSE pp.payment_total END), pti.monthid, pti.year \n")
            .append("from " + companyID + ".payslipTableItem pti \n")
            .append("left join " + companyID + ".payslip_payments pp on  pp.payslip_item_id=pti.id \n")
            .append("left join " + companyID + ".paymentDeduction pd on pp.payment_deduction_id=pd.id \n")
            .append("left join " + companyID + ".category cat on pd.categoryID=cat.id \n")
            .append("left join " + companyID + ".reference payst on  payst.id=pti.status_id \n");
        sql.append("where pti.deleted is not true and payst.code = 'PY_APPROVED' \n");

        if (isPayment) {
            sql.append("and cat.type ='" + EdsPayrollCategory.PAYMENT + "'\n");
        } else {
            sql.append("and cat.type ='" + EdsPayrollCategory.DEDUCTION + "'\n");
        }

        if (fp.getSelectedMonth() != null && fp.getSelectedYear() != null) {
            sql.append("and pti.monthid >=" + fp.getSelectedMonth() + "\n");
            sql.append("and pti.year >=" + fp.getSelectedYear() + "\n");
        }
        if (fp.getEmployeeId() != null) {
            sql.append("and pti.employee_id = " + fp.getEmployeeId() + "\n");
        }

        sql.append("group by cat.type, cat.code, cat.name, pti.monthid, pti.year \n");
        sql.append("order by pti.year, pti.monthid ");

        List<Object[]> list = (List<Object[]>) findNative(sql.toString());

        for (Object[] obj : list) {
            String categoryAndMonth = (String) obj[1] + obj[4]; //like: BASIC_SALARY4
            resultMap.put(categoryAndMonth, (BigDecimal) obj[3]);
        }

        return  resultMap;
    }

    public LinkedHashMap<String, ArrayList<String>> getPayrollYTDChartDataCategories(ListingFilterParameter fp) {
        LinkedHashMap<String, ArrayList<String>> resultMap = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();

        sql.append("select cat.type, cat.code \n")
                .append("from " + companyID + ".payslipTableItem pti \n")
                .append("left join " + companyID + ".payslip_payments pp on  pp.payslip_item_id=pti.id \n")
                .append("left join " + companyID + ".paymentDeduction pd on pp.payment_deduction_id=pd.id \n")
                .append("left join " + companyID + ".category cat on pd.categoryID=cat.id \n")
                .append("left join " + companyID + ".reference payst on  payst.id=pti.status_id \n");
        sql.append("where pti.deleted is not true and payst.code = 'PY_APPROVED' \n");

        if (fp.getSelectedMonth() != null && fp.getSelectedYear() != null) {
            sql.append("and pti.monthid >=" + fp.getSelectedMonth() + "\n");
            sql.append("and pti.year >=" + fp.getSelectedYear() + "\n");
        }
        if (fp.getEmployeeId() != null) {
            sql.append("and pti.employee_id = " + fp.getEmployeeId() + "\n");
        }

        sql.append("group by cat.code, cat.type \n");
        sql.append("order by cat.code ");

        List<Object[]> list = (List<Object[]>) findNative(sql.toString());

        for (Object[] obj : list) {
            String type = (String) obj[0];
            String code = (String) obj[1];
            ArrayList<String> data = resultMap.getOrDefault(type, new ArrayList<>());
            data.add((String) code);

            resultMap.put(type, data);
        }

        return  resultMap;
    }

    public LinkedHashMap<Integer, BigDecimal> getPayrollYTDChartDataExpenses(ListingFilterParameter fp) {
        LinkedHashMap<Integer, BigDecimal> resultMap = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();

        sql.append("select pti.monthid, pti.year, sum(CASE WHEN ep.exchangerate IS NOT NULL THEN ep.amount / pti.exchangerate ELSE ep.amount END) \n")
            .append("from " + companyID + ".expensepayments ep \n")
            .append("left join " + companyID + ".expenseReport er on er.id = ep.expensereportid \n")
            .append("inner join " + companyID + ".paysliptableitem pti on ep.paysliptableitem_id = pti.id \n")
            .append("where ep.deleted is not true \n");

        if (fp.getSelectedMonth() != null && fp.getSelectedYear() != null) {
            sql.append("and pti.monthid >=" + fp.getSelectedMonth() + "\n");
            sql.append("and pti.year >=" + fp.getSelectedYear() + "\n");
        }

        sql.append("group by pti.monthid, pti.year \n");
        sql.append("order by pti.year, pti.monthid ");

        List<Object[]> list = (List<Object[]>) findNative(sql.toString());

        for (Object[] obj : list) {
            resultMap.put((Integer) obj[0], (BigDecimal) obj[2]);
        }

        return  resultMap;
    }

    public LinkedHashMap<Integer, BigDecimal> getEmployeePayrollYTDChartDataExpenses(ListingFilterParameter fp) {
        LinkedHashMap<Integer, BigDecimal> resultMap = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();

        sql.append("select pti.monthid, pti.year, sum(CASE WHEN ep.exchangerate IS NOT NULL THEN ep.amount / pti.exchangerate ELSE ep.amount END) \n")
            .append("from " + companyID + ".expensepayments ep \n")
            .append("left join " + companyID + ".expenseReport er on er.id = ep.expensereportid \n")
            .append("inner join " + companyID + ".paysliptableitem pti on ep.paysliptableitem_id = pti.id \n")
            .append("where ep.deleted is not true \n");

        if (fp.getSelectedMonth() != null && fp.getSelectedYear() != null) {
            sql.append("and pti.monthid >=" + fp.getSelectedMonth() + "\n");
            sql.append("and pti.year >=" + fp.getSelectedYear() + "\n");
        }

        if (fp.getEmployeeId() != null) {
            sql.append("and pti.employee_id = " + fp.getEmployeeId() + "\n");
        }

        sql.append("group by pti.monthid, pti.year \n");
        sql.append("order by pti.year, pti.monthid ");

        List<Object[]> list = (List<Object[]>) findNative(sql.toString());

        for (Object[] obj : list) {
            resultMap.put((Integer) obj[0], (BigDecimal) obj[2]);
        }

        return  resultMap;
    }

    @Override
    public HashMap<Integer, BigDecimal> getEmployeeSalaryForPeriod(ListingFilterParameter fp) {
        HashMap<Integer, BigDecimal> result = new HashMap<>();
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder("select pi.employee_id, \n")
                .append("sum(CASE WHEN pi.exchangerate IS NOT NULL THEN pp.payment_total / pi.exchangerate ELSE pp.payment_total END) \n")
                .append("from " + companyID + ".payslip_payments pp \n")
                .append("join " + companyID + ".paysliptableitem pi on pi.id = pp.payslip_item_id \n")
                .append("left join " + companyID + ".reference ref on ref.id = pi.status_id \n")
                .append("left join " + companyID + ".paymentdeduction pd on pd.id = pp.payment_deduction_id \n")
                .append("left join " + companyID + ".category c on c.id = pd.categoryid \n")
                .append("where ").append(ServerUtils.checkForDeleted("pi.deleted")).append(" \n")
                .append("and pi.employee_id in (").append(fp.getEmployeeIDs()).append(") \n")
                .append("and ref.code in ('" + PAYRUN_STATUS_APPROVED + "', '" + PAYRUN_STATUS_PARTIAL_PAID + "', '" + PAYRUN_STATUS_PAID + "') \n")
                .append("and (c.type = '" + EdsPayrollCategory.PAYMENT + "' \n")
                .append("and c.id in (").append(ServerUtils.getAsCommoDelimited(fp.getObjectIDs(), "0")).append(") \n")
                .append("or pd.issalaryobject = true) \n");
        if (fp.getMonthId() != null && fp.getYear() != null) {
            sql.append("and pi.monthid =" + fp.getMonthId() + "\n");
            sql.append("and pi.year =" + fp.getYear() + "\n");
        }
        sql.append("group by pi.employee_id");

        List<Object[]> sqlResult = findNative(sql.toString());
        for (Object[] obj : sqlResult) {
            result.put((Integer) obj[0], (BigDecimal) obj[1]);
        }
        return result;
    }

    @Override
    public Map<Integer, BigDecimal> getPaymentAmounts(String paymentDeductionIds, Integer payslipItemID) {
        List<Object[]> objects = findNative("select pp.payment_deduction_id, coalesce(pp.payment_total, 0.00) from " + getCompanyId() + ".payslip_payments pp where pp.payment_deduction_id in (" + paymentDeductionIds + ") and pp.payslip_item_id=" + payslipItemID);
        if (objects == null || objects.isEmpty()) {
            return null;
        }
        return objects.stream().collect(Collectors.toMap(o -> (Integer) o[0], o -> (BigDecimal) o[1]));
    }
}
