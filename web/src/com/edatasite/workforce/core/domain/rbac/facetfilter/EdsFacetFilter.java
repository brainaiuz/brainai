package com.edatasite.workforce.core.domain.rbac.facetfilter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.enums.DateTermsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import org.hibernate.annotations.Type;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 18-Jun-2011
 * Time: 17:32:51
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "facetfilter")
public class EdsFacetFilter extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    /**
     * Save facet filter name
     */
    @Column(name = "name")
    private String name;
    /**
     * This is type properties ListPanelType enum types,
     * if isSystemFilter equals true that ListPanelType + " Default System" text add
     * and not show in users facet filter user interfaces this is facet filter setting.
     */
    @Column(name = "facettype")
    private String type;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "filter")
    private List<EdsUserFilter> userFilters = new ArrayList<>();


    @Basic
    private Date startDate;

    @Basic
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private DateTermsEnum dateTerm;

    private Integer less;
    private Integer more;

    /**
     * If isSystemFilter equals true that this is facet setting
     * system setting and this facet filter not show in user facet
     * filter user interfacet
     */
    @Basic
    private Boolean isSystemFilter = false;

    /**
     * Facet Filter settings saved repository
     */
    @Column(name = "facetFilterJsonData")
    @Type(type = "text")
    private String facetFilterJsonData;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId")
//    private EdsUser user;

    @Override
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean isSystemFilter() {
        return isSystemFilter;
    }

    public void setSystemFilter(Boolean systemFilter) {
        isSystemFilter = systemFilter;
    }

    public FacetFilterRpc getFacetFilter(Set<String> keySet) {
        FacetFilterRpc facetFilterRpc = WfmJsonUtils.jsonConvertToFacetFilterRpc(facetFilterJsonData, keySet);
        if (facetFilterRpc != null) {
            facetFilterRpc.setName(getName());
            facetFilterRpc.setObjectID(getObjectID());
            if (getDateTerm() != null && !getDateTerm().equals(DateTermsEnum.CUSTOM)) {
                facetFilterRpc.setStartDate(DateTermsEnum.getStartDateByTerm(getDateTerm()));
                facetFilterRpc.setEndDate(DateTermsEnum.getEndDateByTerm(getDateTerm()));
            } else {
                facetFilterRpc.setStartDate(getStartDate());
                facetFilterRpc.setEndDate(getEndDate());
            }
            facetFilterRpc.setLess(getLess());
            facetFilterRpc.setMore(getMore());
//            facetFilterRpc.setDefaultFilter(isDefaultFilter());
        }
        return facetFilterRpc;
    }

    public void setFacetFilter(FacetFilterRpc facetFilter) {
        this.facetFilterJsonData = WfmJsonUtils.facetFilrerConvertToJsonData(facetFilter);
    }

    public void setFacetFilterJsonData(String facetFilterJsonData) {
        this.facetFilterJsonData = facetFilterJsonData;
    }

//    public EdsUser getUser() {
//        return user;
//    }
//
//    public void setUser(EdsUser user) {
//        this.user = user;
//    }

    public List<EdsUserFilter> getUserFilters() {
        return userFilters;
    }

    public void setUserFilters(List<EdsUserFilter> userFilters) {
        this.userFilters = userFilters;
    }

    public Integer getMore() {
        return more;
    }

    public void setMore(Integer more) {
        this.more = more;
    }

    public Integer getLess() {
        return less;
    }

    public void setLess(Integer less) {
        this.less = less;
    }

    public DateTermsEnum getDateTerm() {
        return dateTerm;
    }

    public void setDateTerm(DateTermsEnum dateTerm) {
        this.dateTerm = dateTerm;
    }
}
