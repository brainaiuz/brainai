package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Anvarbek
 * Date: May 13, 2010
 * Time: 3:15:13 PM
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bankStatement")
public class EdsBankStatement extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    //private Set<EdsTransaction> transactions;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankaccountid")
    private EdsBankAccount bankAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankaccountattachementid")
    private EdsBankAccountAttachment bankAccountAttachment;

    private Date importedDate;

    private Boolean uploadedFileDeleted = Boolean.FALSE;

    public EdsBankStatement() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsBankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(EdsBankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public EdsBankAccountAttachment getBankAccountAttachment() {
        return bankAccountAttachment;
    }

    public void setBankAccountAttachment(EdsBankAccountAttachment bankAccountAttachment) {
        this.bankAccountAttachment = bankAccountAttachment;
    }

    public Date getImportedDate() {
        return importedDate;
    }

    public void setImportedDate(Date importedDate) {
        this.importedDate = importedDate;
    }

    public Boolean getUploadedFileDeleted() {
        return uploadedFileDeleted;
    }

    public void setUploadedFileDeleted(Boolean uploadedFileDeleted) {
        this.uploadedFileDeleted = uploadedFileDeleted;
    }
}
