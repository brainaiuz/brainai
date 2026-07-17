package com.edatasite.workforce.core.domain.settings.mobile;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.core.domain.EdsUser;

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
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "user_modules")
public class EdsUserModule extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private EdsUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private EdsModule module;

    @Column(name = "order_number")
    private Integer order;

    @Column(columnDefinition = "boolean default false")
    private boolean selected = false;

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public EdsModule getModule() {
        return module;
    }

    public void setModule(EdsModule module) {
        this.module = module;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
