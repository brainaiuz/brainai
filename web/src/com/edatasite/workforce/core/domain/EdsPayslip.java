package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.util.Date;
//import com.edatasite.workforce.gwt.payroll.client.rpc.Payslip;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payslip_old")
public class EdsPayslip extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    private EdsEmployee employee;

    private String name;

    private Date datefrom;

    private Date dateto;

    private Double grossfortax;

    private Float taxpercent;

    private Double taxpay;

    private Double total;

    private Double netpay;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    public Integer getObjectID() {
        return objectID;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public Date getDatefrom() {
        return datefrom;
    }

    public void setDatefrom(Date datefrom) {
        this.datefrom = datefrom;
    }

    public Date getDateto() {
        return dateto;
    }

    public void setDateto(Date dateto) {
        this.dateto = dateto;
    }

    public Double getGrossfortax() {
        return grossfortax;
    }

    public void setGrossfortax(Double grossfortax) {
        this.grossfortax = grossfortax;
    }

    public Float getTaxpercent() {
        return taxpercent;
    }

    public void setTaxpercent(Float taxpercent) {
        this.taxpercent = taxpercent;
    }

    public Double getTaxpay() {
        return taxpay;
    }

    public void setTaxpay(Double taxpay) {
        this.taxpay = taxpay;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getNetpay() {
        return netpay;
    }

    public void setNetpay(Double netpay) {
        this.netpay = netpay;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    /*public Payslip createPayslipItem(){
         Payslip result = new Payslip();

         result.setId(getObjectID());
         result.setName(getName());
         if (getEmployee() != null) {
             result.setEmployeeId(getEmployee().getObjectID());
             result.setEmployee(getEmployee().getFullName());
         }
         result.setDatefrom(getDatefrom()!=null ? new Date(getDatefrom().getTime()) : null);
         result.setDateto(getDateto()!=null ? new Date(getDateto().getTime()) : null);
         result.setCurrencyId(getCurrency().getObjectID());
         result.setCurrencyName(getCurrency().getName());
         result.setGrossfortax(getGrossfortax());
         result.setNetpay(getNetpay());
         result.setTaxpay(getTaxpay());
         result.setTaxpercent(getTaxpercent());
         result.setTotal(getTotal());

         return result;
     }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
