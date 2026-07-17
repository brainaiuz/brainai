package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate.CertificateItemData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "certificateitem")
public class EdsCertificateItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_id")
    private EdsCertificate certificate;

    private Integer sorder;

    private String values;

    private Integer color;

//    private String htmlStyle;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(EdsCertificate certificate) {
        this.certificate = certificate;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    //    public String getHtmlStyle() {
//        return htmlStyle;
//    }
//
//    public void setHtmlStyle(String htmlStyle) {
//        this.htmlStyle = htmlStyle;
//    }

    public CertificateItemData createItemData() {
        CertificateItemData itemData = new CertificateItemData();
        itemData.setObjectID(getObjectID());
        itemData.setValues(getValues());
        itemData.setSorder(getSorder());
        itemData.setColor(getColor());
        return itemData;
    }
}
