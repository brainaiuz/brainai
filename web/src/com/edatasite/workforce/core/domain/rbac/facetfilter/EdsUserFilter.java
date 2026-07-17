package com.edatasite.workforce.core.domain.rbac.facetfilter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 03.10.13
 * Time: 15:48
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "userfilter")
public class EdsUserFilter extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "userid")
    private EdsUser user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "filterid")
    private EdsFacetFilter filter;

    @Column(name = "isdefault")
    private Boolean isDefault = false;

    @Column(name = "isfavour")
    private Boolean isFavour = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public EdsFacetFilter getFilter() {
        return filter;
    }

    public void setFilter(EdsFacetFilter filter) {
        this.filter = filter;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getFavour() {
        return isFavour;
    }

    public void setFavour(Boolean favour) {
        isFavour = favour;
    }
}
