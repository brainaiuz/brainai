package com.edatasite.workforce.core.domain.reporting;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.gwt.core.client.rpc.ReportingDBUrlListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 4/30/12
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "reportingDBUrl")
public class EdsReportingDBUrl extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    private String dbUrl;
    private String userName;
    private String password;
    @Column(name = "isEncrypted", columnDefinition = " boolean DEFAULT false")
    private Boolean isEncrypted = false;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PUBLIC_SCHEMA,
            name = "companyReportingDBUrl",
            joinColumns = {@JoinColumn(name = "reportingDBUrlID")},
            inverseJoinColumns = {@JoinColumn(name = "companyId")}
    )
    private Set<EdsCompany> companies = new HashSet<>();

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getDbUrl() {
        if (dbUrl != null && getEncrypt()) {
            return EncryptionHelper.decryptURL(EncryptionHelper.decrypt(dbUrl, Constants.PASS_PHRASE));
        }
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = EncryptionHelper.encrypt(EncryptionHelper.encryptURL(dbUrl), Constants.PASS_PHRASE);
    }

    public String getUserName() {
        if (userName != null && getEncrypt()) {
            return EncryptionHelper.decryptURL(EncryptionHelper.decrypt(userName, Constants.PASS_PHRASE));
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = EncryptionHelper.encrypt(EncryptionHelper.encryptURL(userName), Constants.PASS_PHRASE);
    }

    public String getPassword() {
        if (password != null && getEncrypt()) {
            return EncryptionHelper.decryptURL(EncryptionHelper.decrypt(password, Constants.PASS_PHRASE));
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = EncryptionHelper.encrypt(EncryptionHelper.encryptURL(password), Constants.PASS_PHRASE);
    }

    public Set<EdsCompany> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<EdsCompany> companies) {
        this.companies = companies;
    }

    public Boolean getEncrypt() {
        return isEncrypted;
    }

    public void setEncrypt(Boolean encrypt) {
        isEncrypted = encrypt;
    }

    public ReportingDBUrlListItem getRPC() {
        ReportingDBUrlListItem item = new ReportingDBUrlListItem();
        item.setDbUrl(getDbUrl());
        item.setId(getObjectID());
        item.setPassword(getPassword());
        item.setUserName(getUserName());
        item.setEncrypt(getEncrypt());
        for (EdsCompany company : getCompanies()) {
            item.getCompany().add(new SelectItem(company.getObjectID(), company.getName()));
        }
        return item;
    }
}
