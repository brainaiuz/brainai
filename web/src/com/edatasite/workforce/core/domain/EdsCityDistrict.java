package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "city_district")
public class EdsCityDistrict extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "alias", length = 1000)
    private String alias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regionId")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsRegion region;

    @Column(name = "name")
    private String name;

    @Column(name = "ruName")
    private String ruName;

    @Column(name = "uzName")
    private String uzName;

    @Column(name = "enName")
    private String enName;

    @Override
    public Integer getObjectID() {
        return this.objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public EdsRegion getRegion() {
        return region;
    }

    public void setRegion(EdsRegion region) {
        this.region = region;
    }

    @Override
    public String getName() {
        String lang = ServerUtils.getUserLocale().getLanguage();
        String result = "";
        switch (lang) {
            case "en":
                result = getEnName();
            case "ru":
                result = getRuName();
            case "uz":
                result = getUzName();
        }
        return result == null || result.isEmpty() ? name : result;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

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


//    public static ArrayList<TreeSelectItem> getRegionAsSelectItems(ArrayList<EdsRegion> states) {
//        if (states != null && states.size() > 0) {
//            ArrayList<TreeSelectItem> result = new ArrayList<>();
//            for (EdsRegion state : states) {
//                result.add(state.getAsTreeSelectItem());
//            }
//            if (result.size() > 0) {
//                return result;
//            }
//        }
//        return null;
//
//    }
}
