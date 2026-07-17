package com.edatasite.workforce.gwt.core.client.rpc.form;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * User: Fatkhulla Nigmatjonov
 * Date: 2/27/13
 * Time: 3:30 PM
 */
public class LocalizationItem implements IsSerializable {

    private Integer objectID;
    private String code;
    private String defaultText;
    private String en;
    private String ru;
    private String arabic;
    private String turkish;
    private String ger;
    private String spa;
    private String fr;
    private String por;
    private String neder;
    private String ita;
    private String thai;
    private String propertyCode;
    private String propertyPath;
    private String description;

    private Date defaultLastUpdate;
    private Date enLastUpdate;
    private Date ruLastUpdate;
    private Date arLastUpdate;
    private Date turLastUpdate;
    private Date spaLastUpdate;
    private Date frLastUpdate;
    private Date porLastUpdate;
    private Date nederLastUpdate;
    private Date itaLastUpdate;
    private Date thaiLastUpdate;

    private String defaultLastChanger;
    private String enLastChanger;
    private String ruLastChanger;
    private String arLastChanger;
    private String turLastChanger;
    private String spaLastChanger;
    private String frLastChanger;
    private String porLastChanger;
    private String nederLastChanger;
    private String itaLastChanger;
    private String thaiLastChanger;
    private LocalizationPermissionItem localizationPermission;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public String getArabic() {
        return arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    public String getTurkish() {
        return turkish;
    }

    public void setTurkish(String turkish) {
        this.turkish = turkish;
    }

    public String getGer() {
        return ger;
    }

    public void setGer(String ger) {
        this.ger = ger;
    }

    public String getSpa() {
        return spa;
    }

    public void setSpa(String spa) {
        this.spa = spa;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getPor() {
        return por;
    }

    public void setPor(String por) {
        this.por = por;
    }

    public String getNeder() {
        return neder;
    }

    public void setNeder(String neder) {
        this.neder = neder;
    }

    public String getIta() {
        return ita;
    }

    public void setIta(String ita) {
        this.ita = ita;
    }

    public String getThai() {
        return thai;
    }

    public void setThai(String thai) {
        this.thai = thai;
    }

    public String getPropertyCode() {
        return propertyCode;
    }

    public void setPropertyCode(String propertyCode) {
        this.propertyCode = propertyCode;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    public LocalizationPermissionItem getLocalizationPermission() {
        return localizationPermission;
    }

    public void setLocalizationPermission(LocalizationPermissionItem localizationPermission) {
        this.localizationPermission = localizationPermission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDefaultLastUpdate() {
        return defaultLastUpdate;
    }

    public void setDefaultLastUpdate(Date defaultLastUpdate) {
        this.defaultLastUpdate = defaultLastUpdate;
    }

    public Date getEnLastUpdate() {
        return enLastUpdate;
    }

    public void setEnLastUpdate(Date enLastUpdate) {
        this.enLastUpdate = enLastUpdate;
    }

    public Date getRuLastUpdate() {
        return ruLastUpdate;
    }

    public void setRuLastUpdate(Date ruLastUpdate) {
        this.ruLastUpdate = ruLastUpdate;
    }

    public Date getArLastUpdate() {
        return arLastUpdate;
    }

    public void setArLastUpdate(Date arLastUpdate) {
        this.arLastUpdate = arLastUpdate;
    }

    public Date getTurLastUpdate() {
        return turLastUpdate;
    }

    public void setTurLastUpdate(Date turLastUpdate) {
        this.turLastUpdate = turLastUpdate;
    }

    public Date getSpaLastUpdate() {
        return spaLastUpdate;
    }

    public void setSpaLastUpdate(Date spaLastUpdate) {
        this.spaLastUpdate = spaLastUpdate;
    }

    public Date getFrLastUpdate() {
        return frLastUpdate;
    }

    public void setFrLastUpdate(Date frLastUpdate) {
        this.frLastUpdate = frLastUpdate;
    }

    public Date getPorLastUpdate() {
        return porLastUpdate;
    }

    public void setPorLastUpdate(Date porLastUpdate) {
        this.porLastUpdate = porLastUpdate;
    }

    public Date getNederLastUpdate() {
        return nederLastUpdate;
    }

    public void setNederLastUpdate(Date nederLastUpdate) {
        this.nederLastUpdate = nederLastUpdate;
    }

    public Date getItaLastUpdate() {
        return itaLastUpdate;
    }

    public void setItaLastUpdate(Date itaLastUpdate) {
        this.itaLastUpdate = itaLastUpdate;
    }

    public Date getThaiLastUpdate() {
        return thaiLastUpdate;
    }

    public void setThaiLastUpdate(Date thaiLastUpdate) {
        this.thaiLastUpdate = thaiLastUpdate;
    }

    public String getDefaultLastChanger() {
        return defaultLastChanger;
    }

    public void setDefaultLastChanger(String defaultLastChanger) {
        this.defaultLastChanger = defaultLastChanger;
    }

    public String getEnLastChanger() {
        return enLastChanger;
    }

    public void setEnLastChanger(String enLastChanger) {
        this.enLastChanger = enLastChanger;
    }

    public String getRuLastChanger() {
        return ruLastChanger;
    }

    public void setRuLastChanger(String ruLastChanger) {
        this.ruLastChanger = ruLastChanger;
    }

    public String getArLastChanger() {
        return arLastChanger;
    }

    public void setArLastChanger(String arLastChanger) {
        this.arLastChanger = arLastChanger;
    }

    public String getTurLastChanger() {
        return turLastChanger;
    }

    public void setTurLastChanger(String turLastChanger) {
        this.turLastChanger = turLastChanger;
    }

    public String getSpaLastChanger() {
        return spaLastChanger;
    }

    public void setSpaLastChanger(String spaLastChanger) {
        this.spaLastChanger = spaLastChanger;
    }

    public String getFrLastChanger() {
        return frLastChanger;
    }

    public void setFrLastChanger(String frLastChanger) {
        this.frLastChanger = frLastChanger;
    }

    public String getPorLastChanger() {
        return porLastChanger;
    }

    public void setPorLastChanger(String porLastChanger) {
        this.porLastChanger = porLastChanger;
    }

    public String getNederLastChanger() {
        return nederLastChanger;
    }

    public void setNederLastChanger(String nederLastChanger) {
        this.nederLastChanger = nederLastChanger;
    }

    public String getItaLastChanger() {
        return itaLastChanger;
    }

    public void setItaLastChanger(String itaLastChanger) {
        this.itaLastChanger = itaLastChanger;
    }

    public String getThaiLastChanger() {
        return thaiLastChanger;
    }

    public void setThaiLastChanger(String thaiLastChanger) {
        this.thaiLastChanger = thaiLastChanger;
    }
}
