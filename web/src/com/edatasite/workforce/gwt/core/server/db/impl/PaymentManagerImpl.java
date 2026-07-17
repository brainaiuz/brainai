package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsPayment;
import com.edatasite.workforce.gwt.core.server.db.PaymentManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("paymentManager")
public class PaymentManagerImpl extends BaseManager<EdsPayment> implements PaymentManager {

    public PaymentManagerImpl() {
        super(EdsPayment.class);
    }

    public List<EdsPayment> getPayments() {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select py from EdsPayment py");
            return find(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EdsPayment> getPayments(Date from, Date to, Integer employeeID) {
        Integer paymentTypeID = 1;
        Map<String, Object> map = new HashMap<>();
        map.put("dateto", to);
        map.put("paymentTypeID", paymentTypeID);
        map.put("employeeID", employeeID);
        map.put("datefrom", from);
        return findByNamedParams(
                "SELECT p FROM EdsPayment p WHERE p.paymentdate BETWEEN :datefrom AND :dateto AND " +
                        "p.type.objectID=:paymentTypeID AND p.employee.objectID =:employeeID AND p.payslip IS NULL", map);

    }

    public List<EdsPayment> getDeductions(Date from, Date to, Integer employeeID) {
        Integer paymentTypeID = 2;
        Map<String, Object> map = new HashMap<>();
        map.put("datefrom", from);
        map.put("dateto", to);
        map.put("paymentTypeID", paymentTypeID);
        map.put("employeeID", employeeID);
        return findByNamedParams(
                "SELECT p FROM EdsPayment p WHERE p.paymentdate BETWEEN :datefrom AND :dateto AND " +
                        "p.type.objectID=:paymentTypeID AND p.employee.objectID =:employeeID AND p.payslip IS NULL", map);
    }

    public List<EdsPayment> getNotAssignedPayments(Integer employeeID) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("paymentTypeID", 1);
            map.put("employeeID", employeeID);
            StringBuilder sql = new StringBuilder();
            sql.append("select py from EdsPayment py\n");
            sql.append("where py.type.objectID=:paymentTypeID AND py.payslip IS NULL AND py.employee.objectID=:employeeID");
            return findByNamedParams(sql.toString(), map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EdsPayment> getNotAssignedDeductions(Integer employeeID) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("paymentTypeID", 2);
            map.put("employeeID", employeeID);
            StringBuilder sql = new StringBuilder();
            sql.append("select py from EdsPayment py\n");
            sql.append("where py.type.objectID=:paymentTypeID AND py.payslip IS NULL AND py.employee.objectID=:employeeID");
            return findByNamedParams(sql.toString(), map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EdsPayment> getPaymentsByPayslip(Integer payslipID) {
        Integer paymentTypeID = 1;
        Map<String, Object> map = new HashMap<>();
        map.put("paymentTypeID", paymentTypeID);
        map.put("payslipID", payslipID);
        return findByNamedParams(
                "SELECT p FROM EdsPayment p WHERE p.type.objectID=:paymentTypeID AND p.payslip.objectID =:payslipID", map);
    }

    public List<EdsPayment> getDeductionsByPayslip(Integer payslipID) {
        Integer paymentTypeID = 2;
        Map<String, Object> map = new HashMap<>();
        map.put("paymentTypeID", paymentTypeID);
        map.put("payslipID", payslipID);
        return findByNamedParams(
                "SELECT p FROM EdsPayment p WHERE p.type.objectID=:paymentTypeID AND p.payslip.objectID =:payslipID", map);
    }

    public List<EdsEmployee> getEmployeesWithExpenses() {
        Integer expenseTypeID = 3;
//		EdsPayment payment = new EdsPayment();
        return find("select distinct p.employee from EdsPayment p where p.type.objectID=?", expenseTypeID);
    }

    public EdsPayment getPayment(Integer paymentId) {
        return (EdsPayment) findSingle("from EdsPayment where objectID = ?", paymentId);
    }

    public List<EdsPayment> getExpenses(Integer employeeID) {
        Integer paymentTypeID = 3;
        if (employeeID != null) {
            return find(
                    "SELECT p FROM EdsPayment p WHERE  p.type.objectID=? AND p.employee.objectID =?", paymentTypeID, employeeID);
        } else {
            return find(
                    "SELECT p FROM EdsPayment p WHERE  p.type.objectID=?", paymentTypeID);
        }
    }

    public List<EdsPayment> getNotAssignedExpenses(Integer employeeID) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select py from EdsPayment py\n");
            sql.append("where py.type.objectID=? AND py.payslip IS NULL AND py.employee.objectID=?", 3, employeeID);
            return find(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
