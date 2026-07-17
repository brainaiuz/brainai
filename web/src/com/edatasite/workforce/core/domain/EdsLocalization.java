package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.form.LocalizationItem;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla Nigmatjonov
 * Date: 27.03.2012
 * Time: 22:02:08
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "localization")
public class EdsLocalization extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "defaultText", length = 5000)
    private String defaultText;
    @Column(name = "en", length = 5000)
    private String en;
    @Column(name = "ru", length = 5000)
    private String ru;
    @Column(name = "arabic", length = 5000)
    private String ar;
    @Column(name = "turkish", length = 5000)
    private String tr;
    @Column(name = "ger", length = 5000)
    private String ger;
    @Column(name = "spa", length = 5000)
    private String spa;
    @Column(name = "fr", length = 5000)
    private String fr;
    @Column(name = "por", length = 5000)
    private String pt;
    @Column(name = "neder", length = 5000)
    private String nl;
    @Column(name = "ita", length = 5000)
    private String it;
    @Column(name = "thai", length = 5000)
    private String th;
    private String propertyCode;
    private String propertyPath;
    private Date lastUpdate;
    private Boolean isActive = true;
    @Column(name = "description")
    @Type(type = "text")
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


    public EdsLocalization() {

    }

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

    public String getAr() {
        return ar;
    }

    public void setAr(String arabic) {
        this.ar = arabic;
    }

    public String getTr() {
        return tr;
    }

    public void setTr(String turkish) {
        this.tr = turkish;
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

    public String getPt() {
        return pt;
    }

    public void setPt(String por) {
        this.pt = por;
    }

    public String getNl() {
        return nl;
    }

    public void setNl(String neder) {
        this.nl = neder;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String ita) {
        this.it = ita;
    }

    public String getTh() {
        return th;
    }

    public void setTh(String thai) {
        this.th = thai;
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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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

    public LocalizationItem loadLocalization() {
        LocalizationItem property = new LocalizationItem();
        property.setObjectID(getObjectID());
        property.setCode(getCode() == null ? " " : this.getCode());
        property.setDefaultText(this.getDefaultText() == null ? "" : this.getDefaultText());
        property.setEn(this.getEn() == null ? "" : this.getEn());
        property.setRu(this.getRu() == null ? "" : this.getRu());
        property.setArabic(this.getAr() == null ? "" : this.getAr());
        property.setTurkish(this.getTr() == null ? "" : this.getTr());
        property.setGer(this.getGer() == null ? "" : this.getGer());
        property.setSpa(this.getSpa() == null ? "" : this.getSpa());
        property.setFr(this.getFr() == null ? "" : this.getFr());
        property.setPor(this.getPt() == null ? "" : this.getPt());
        property.setNeder(this.getNl() == null ? "" : this.getNl());
        property.setIta(this.getIt() == null ? "" : this.getIt());
        property.setThai(this.getTh() == null ? "" : this.getTh());
        property.setPropertyCode(this.getPropertyCode() == null ? "" : this.getPropertyCode());
        property.setPropertyPath(this.getPropertyPath() == null ? "" : this.getPropertyPath());
        property.setDescription(this.getDescription() == null ? "" : this.getDescription());

        if (this != null) {
            property.setDefaultLastChanger(this.getDefaultLastChanger());
            property.setEnLastChanger(this.getEnLastChanger());
            property.setItaLastChanger(this.getItaLastChanger());
            property.setRuLastChanger(this.getRuLastChanger());
            property.setArLastChanger(this.getArLastChanger());
            property.setPorLastChanger(this.getPorLastChanger());
            property.setFrLastChanger(this.getFrLastChanger());
            property.setSpaLastChanger(this.getSpaLastChanger());
            property.setTurLastChanger(this.getTurLastChanger());
            property.setThaiLastChanger(this.getThaiLastChanger());
            property.setNederLastChanger(this.getNederLastChanger());

            property.setDefaultLastUpdate(this.getDefaultLastUpdate());
            property.setEnLastUpdate(this.getEnLastUpdate());
            property.setItaLastUpdate(this.getItaLastUpdate());
            property.setRuLastUpdate(this.getRuLastUpdate());
            property.setArLastUpdate(this.getArLastUpdate());
            property.setPorLastUpdate(this.getPorLastUpdate());
            property.setFrLastUpdate(this.getFrLastUpdate());
            property.setSpaLastUpdate(this.getSpaLastUpdate());
            property.setTurLastUpdate(this.getTurLastUpdate());
            property.setThaiLastUpdate(this.getThaiLastUpdate());
            property.setNederLastUpdate(this.getNederLastUpdate());
        }
        return property;
    }

}
