package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20.11.2008
 * Time: 17:43:20
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "EmployeeWageClientRateHistory")
public class EmployeeWageClientRateHistory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    private EdsEmployee employee;


    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    //
    private Double wageRate;
    private Double clientChargeRate;
    private Date changeDate;

    public Double getWageRate() {
        return wageRate;
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public Double getClientChargeRate() {
        return clientChargeRate;
    }

    public void setClientChargeRate(Double clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public static BigDecimal calculateInHoursRate(List<EmployeeWageClientRateHistory> wagesHistory, Date date, Double timespent) {
        BigDecimal calculatedAmount = BigDecimal.ZERO;
        for (int j = 0; j < wagesHistory.size(); j++) {
            EmployeeWageClientRateHistory current = wagesHistory.get(j);
            //if date lays before first salary entry
            if ((date.before(current.getChangeDate()) || date.equals(current.getChangeDate())) && j == 0) {
                calculatedAmount = calculatedAmount.add(new BigDecimal(current.getWageRate() * (timespent / 60))); //should take the difference, correct
                break;
            }

            EmployeeWageClientRateHistory next = null;
            if (j != (wagesHistory.size() - 1)) {
                next = wagesHistory.get(j + 1);
            }

            //if date between salaries change time range, take the lowest salary change date
            if (date.after(current.getChangeDate()) && ((next == null || date.before(next.getChangeDate())))) {
                calculatedAmount = calculatedAmount.add(new BigDecimal(current.getWageRate() * (timespent / 60))); //should take the difference, correct
                break;
            }
        }
        return calculatedAmount;
    }

    public static BigDecimal calculateInHoursRateTimesheet(List<EdsProjectEmployeeWageClientRateHistory> wagesHistory, Date date, Integer _timespent) {
        BigDecimal calculatedAmount = BigDecimal.ZERO;
        BigDecimal timespent = new BigDecimal(_timespent);
        for (int j = 0; j < wagesHistory.size(); j++) {
            EdsProjectEmployeeWageClientRateHistory current = wagesHistory.get(j);
            //if date lays before first salary entry
            if ((date.before(current.getChangeDate()) || date.equals(current.getChangeDate())) && j == 0) {
                calculatedAmount = calculatedAmount.add(new BigDecimal(current.getWageRate() * (timespent.doubleValue() / 60))); //should take the difference, correct
                break;
            }
            EdsProjectEmployeeWageClientRateHistory next = null;
            if (j != (wagesHistory.size() - 1)) {
                next = wagesHistory.get(j + 1);
            }
            //if date between salaries change time range, take the lowest salary change date
            if (date.compareTo(current.getChangeDate()) >= 0 && ((next == null || date.compareTo(next.getChangeDate()) <= 0))) {
                if (next != null && date.compareTo(next.getChangeDate()) == 0) {
                    calculatedAmount = calculatedAmount.add(new BigDecimal(next.getWageRate() * (timespent.doubleValue() / 60))); //should take the difference, correct
                } else {
                    calculatedAmount = calculatedAmount.add(new BigDecimal(current.getWageRate() * (timespent.doubleValue() / 60))); //should take the difference, correct
                }
                break;
            }
        }
        System.out.println(date + " and timespent ->" + _timespent + " After calculate amount -> " + calculatedAmount);
        return calculatedAmount;
    }
}
