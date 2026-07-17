/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/3 5:27:14                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.hibernate.annotations.Where;

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
 * Created by IntelliJ IDEA.
 * User: izaynutdinov
 * Date: 28.04.2007
 * Time: 16:38:33
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "country")
public class EdsCountry extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "ruName")
    private String ruName;

    @Column(name = "uzName")
    private String uzName;

    @Column(name = "code")
    private String code;

    @Column(name = "alias", length = 10000)
    private String alias;

    @Column(name = "telcode")
    private String telCode;
//    private boolean additional = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyid")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCurrency currency;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @org.hibernate.annotations.ForeignKey(name = "none")
    List<EdsRegion> states = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @Where(clause = "deleted = 'false'")
    @OrderBy("sorder asc")
    List<EdsEmbassy> embassy = new ArrayList<>();

    @Column(name = "isActive", columnDefinition = " boolean DEFAULT true")
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsUser updater;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return "ru".equals(ServerUtils.getUserLocale().getLanguage()) && ruName != null ? ruName : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public String getTelCode() {
        return telCode;
    }

    public void setTelCode(String telCode) {
        this.telCode = telCode;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<EdsRegion> getStates() {
        return states;
    }

    public void setStates(List<EdsRegion> states) {
        this.states = states;
    }

    public List<EdsEmbassy> getEmbassy() {
        return embassy;
    }

    public void setEmbassy(List<EdsEmbassy> embassy) {
        this.embassy = embassy;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        if (lastUpdateTime == null) {
            lastUpdateTime = new Date();
        }
        this.lastUpdateTime = lastUpdateTime;
    }

    //    public boolean isAdditional() {
//        return additional;
//    }
//
//    public void setAdditional(boolean additional) {
//        this.additional = additional;
//    }

    public static TreeSelectItem[] asTreeSelectItemList(List<EdsCountry> countries) {
        if (countries != null && countries.size() > 0) {
            List<TreeSelectItem> result = new ArrayList<>();
            for (EdsCountry country : countries) {
                result.add(country.getRPC());
            }
            return result.toArray(new TreeSelectItem[]{});
        }
        return new TreeSelectItem[0];
    }

    private TreeSelectItem getRPC() {
        TreeSelectItem item = new TreeSelectItem(getObjectID(), getName(), getAlias());
        List<EdsRegion> regions = getStates();
        if (regions != null) {
            item.setChildren(EdsRegion.getRegionAsSelectItems(new ArrayList<>(regions)));
        }
        return item;
    }
}
