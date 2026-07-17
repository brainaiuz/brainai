package com.edatasite.workforce.core.domain;

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

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla Nigmatjonov
 * Date: 27.03.2012
 * Time: 22:02:08
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "localizationpermissions")
public class EdsLocalizationPermissions extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCompany company;
    private Boolean code;
    private Boolean defaultText;
    private Boolean en;
    private Boolean ru;
    private Boolean arabic;
    private Boolean turkish;
    private Boolean ger;
    private Boolean spa;
    private Boolean fr;
    private Boolean por;
    private Boolean neder;
    private Boolean ita;
    private Boolean thai;


    public EdsLocalizationPermissions() {

    }


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCompany getCompany() {
        return company;
    }

    public void setCompany(EdsCompany company) {
        this.company = company;
    }

    public Boolean getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(Boolean defaultText) {
        this.defaultText = defaultText;
    }

    public Boolean getEn() {
        return en;
    }

    public void setEn(Boolean en) {
        this.en = en;
    }

    public Boolean getRu() {
        return ru;
    }

    public void setRu(Boolean ru) {
        this.ru = ru;
    }

    public Boolean getArabic() {
        return arabic;
    }

    public void setArabic(Boolean arabic) {
        this.arabic = arabic;
    }

    public Boolean getTurkish() {
        return turkish;
    }

    public void setTurkish(Boolean turkish) {
        this.turkish = turkish;
    }

    public Boolean getGer() {
        return ger;
    }

    public void setGer(Boolean ger) {
        this.ger = ger;
    }

    public Boolean getSpa() {
        return spa;
    }

    public void setSpa(Boolean spa) {
        this.spa = spa;
    }

    public Boolean getFr() {
        return fr;
    }

    public void setFr(Boolean fr) {
        this.fr = fr;
    }

    public Boolean getPor() {
        return por;
    }

    public void setPor(Boolean por) {
        this.por = por;
    }

    public Boolean getNeder() {
        return neder;
    }

    public void setNeder(Boolean neder) {
        this.neder = neder;
    }

    public Boolean getIta() {
        return ita;
    }

    public void setIta(Boolean ita) {
        this.ita = ita;
    }

    public Boolean getThai() {
        return thai;
    }

    public void setThai(Boolean thai) {
        this.thai = thai;
    }

    public Boolean getCode() {
        return code;
    }

    public void setCode(Boolean code) {
        this.code = code;
    }
}
