package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: Nov 18, 2009
 * Time: 4:59:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "locale")
public class EdsLocale extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    private String language;

    @Basic
    private String country;

    @Basic
    private String languageCode;

    @Basic
    private String countryCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getObjectID() {
        return id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return (languageCode != null ? languageCode : "")
                + (languageCode != null && countryCode != null && !languageCode.equals("") && !countryCode.equals("") ? "_" + countryCode : "");
    }
}
