package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate.CertificateTypeData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "certificatetype")
public class EdsCertificateType extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    private String frontImageURL;
    private String backImageURL;

    private Integer fieldCount;

    private Integer expiryDateSorder; //certificate item qo'shilganda expiryDate sorderni bu fieldga ham qo'shib ketish kerak

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrontImageURL() {
        return frontImageURL;
    }

    public void setFrontImageURL(String frontImageURL) {
        this.frontImageURL = frontImageURL;
    }

    public String getBackImageURL() {
        return backImageURL;
    }

    public void setBackImageURL(String backImageURL) {
        this.backImageURL = backImageURL;
    }

    public Integer getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(Integer fieldCount) {
        this.fieldCount = fieldCount;
    }

    public Integer getExpiryDateSorder() {
        return expiryDateSorder;
    }

    public void setExpiryDateSorder(Integer expiryDateSorder) {
        this.expiryDateSorder = expiryDateSorder;
    }

    public CertificateTypeData createCertificateTypeData() {
        CertificateTypeData certificateTypeData = new CertificateTypeData();
        certificateTypeData.setObjectID(getObjectID());
        certificateTypeData.setName(getName());
        certificateTypeData.setImageURL(getBackImageURL());
        certificateTypeData.setFieldsCount(getFieldCount());
        return certificateTypeData;
    }
}
