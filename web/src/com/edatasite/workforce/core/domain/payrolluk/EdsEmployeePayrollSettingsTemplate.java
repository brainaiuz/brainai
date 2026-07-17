package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 11/27/15
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "ep_template")
public class EdsEmployeePayrollSettingsTemplate extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "dobDate")
    private Date dobDate;

    @Column(name = "resignationDate")
    private Date resignationDate;

    @Column(name = "employeeCode")
    private String employeeCode;

    @Column(name = "intnumber")
    private Integer intNumber;

    @Column(name = "gender")
    private String gender;

    @Column(name = "employee_id")
    private Integer employeeID;

    @Column(name = "payroll_settings")
    @Type(type = "text")
    private String payrollSettings;

    @Column(name = "rejection_note")
    @Type(type = "text")
    private String rejectionNote;

    @Column(name = "deletedCategories")
    private String deletedCategories;

    @Column(name = "inactiveCategories")
    private String inactiveCategories;

    @Column(name = "payment_method")
    private String paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @ForeignKey(name = "none")
    private EdsCurrency salaryCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizenship", foreignKey = @javax.persistence.ForeignKey(ConstraintMode.NO_CONSTRAINT))
    EdsCountry citizenship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approverId")
    private EdsEmployee approver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private EdsUser sender;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "employeeTemplate")
    @Where(clause = "(deleted = 'false' or deleted is null) and isRecurring is not null")
    private List<EdsPaymentDeduction> categories = new ArrayList<>();

    private Boolean deleted;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getDobDate() {
        return dobDate;
    }

    public void setDobDate(Date dobDate) {
        this.dobDate = dobDate;
    }

    public Date getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(Date resignationDate) {
        this.resignationDate = resignationDate;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getPayrollSettings() {
        return payrollSettings;
    }

    public void setPayrollSettings(String payrollSettings) {
        this.payrollSettings = payrollSettings;
    }

    public String getRejectionNote() {
        return rejectionNote;
    }

    public void setRejectionNote(String rejectionNote) {
        this.rejectionNote = rejectionNote;
    }

    public String getDeletedCategories() {
        return deletedCategories;
    }

    public void setDeletedCategories(String deletedCategories) {
        this.deletedCategories = deletedCategories;
    }

    public String getInactiveCategories() {
        return inactiveCategories;
    }

    public void setInactiveCategories(String inactiveCategories) {
        this.inactiveCategories = inactiveCategories;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public EdsEmployee getApprover() {
        return approver;
    }

    public void setApprover(EdsEmployee approver) {
        this.approver = approver;
    }

    public EdsUser getSender() {
        return sender;
    }

    public void setSender(EdsUser sender) {
        this.sender = sender;
    }

    public List<EdsPaymentDeduction> getCategories() {
        return categories;
    }

    public void setCategories(List<EdsPaymentDeduction> categories) {
        this.categories = categories;
    }

    public EdsCurrency getSalaryCurrency() {
        return salaryCurrency;
    }

    public void setSalaryCurrency(EdsCurrency salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsCountry getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(EdsCountry citizenship) {
        this.citizenship = citizenship;
    }
}
