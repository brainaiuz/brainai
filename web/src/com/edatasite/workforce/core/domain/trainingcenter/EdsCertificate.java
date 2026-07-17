package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate.CertificateData;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "certificate")
public class EdsCertificate extends EdsTraceable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer intNumber;
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentid")
    private EdsStudent student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificatetypeid")
    private EdsCertificateType certificateType;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_id")
    @OrderBy("sorder")
    private List<EdsCertificateItem> items = new ArrayList<>();

    private Date creationDate;

    private Date expiryDate;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public EdsStudent getStudent() {
        return student;
    }

    public void setStudent(EdsStudent student) {
        this.student = student;
    }

    public EdsCertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(EdsCertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        if (!ServerUtils.equalsDate(this.expiryDate, expiryDate)) {
            addChange(CustomFormConstants.EXPIRY_DATE);
        }
        this.expiryDate = expiryDate;
    }

    public List<EdsCertificateItem> getItems() {
        return items;
    }

    public void setItems(List<EdsCertificateItem> items) {
        this.items = items;
    }

    public CertificateData createCertificateData() {
        CertificateData certificateData = new CertificateData();
        certificateData.setObjectID(getObjectID());
        if (getStudent() != null) {
            certificateData.setStudentID(getStudent().getObjectID());
            certificateData.setStudent(getStudent().getAsSelectItem().getName());
        }

        certificateData.setCertificateTypeData(getCertificateType().createCertificateTypeData());
        certificateData.getCertificateTypeData().setNumberData(new NumberData(getNumber(), getIntNumber()));
        certificateData.getCertificateTypeData().getNumberData().setNumberFormat("CERT_0001");

        certificateData.setCreationDate(getCreationDate());

        return certificateData;
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.EXPIRY_DATE)) {
            return getExpiryDate();
        } else if (fieldID.equals(CustomFormConstants.CERTIFICATE.CERTIFICATE_TYPE)) {
            return getCertificateType();
        } else if (fieldID.equals(CustomFormConstants.CERTIFICATE.NUMBER)) {
            return getNumber();
        }
        return super.getRealValue(fieldID);
    }
}
