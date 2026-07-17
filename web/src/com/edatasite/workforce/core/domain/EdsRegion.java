package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: izaynutdinov
 * Date: 28.04.2007
 * Time: 16:50:36
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "Region")
public class EdsRegion extends EdsObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "alias", length = 1000)
    private String alias;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    @ForeignKey(name = "none")
    private EdsCountry country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeZoneId")
    @ForeignKey(name = "none")
    private EdsTimeZone timeZone;

    @Column(name = "name")
    private String name;

    @Column(name = "ruName")
    private String ruName;

    @Column(name = "uzName")
    private String uzName;

    private String code;

    public String getRuName() {
        return ruName;
    }

    public void setRuName(String ruName) {
        this.ruName = ruName;
    }

    public String getUzName() {
        return uzName;
    }

    public void setUzName(String uzName) {
        this.uzName = uzName;
    }

    public EdsCountry getCountry() {
        return country;
    }

    public void setCountry(EdsCountry country) {
        this.country = country;
    }

    public String getName() {
        return "ru".equals(ServerUtils.getUserLocale().getLanguage()) && ruName != null ? ruName : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EdsTimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(EdsTimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public static ArrayList<TreeSelectItem> getRegionAsSelectItems(ArrayList<EdsRegion> states) {
        if (states != null && states.size() > 0) {
            ArrayList<TreeSelectItem> result = new ArrayList<>();
            for (EdsRegion state : states) {
                result.add(state.getAsTreeSelectItem());
            }
            if (result.size() > 0) {
                return result;
            }
        }
        return null;

    }
}
