package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

/**
 * User: Sherali
 * Date: 01.12.2008
 * Time: 13:53:36
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "activation_link")
public class EdsActivationLink extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private Date createdDate;
    private Date updatedDate;
    private Boolean deleted;

    @NotNull
    @Type(type = "text")
    @Column(unique = true)
    private String key;
    private Integer userId;
    private Integer companyId;
    @Enumerated(EnumType.STRING)
    private ActivationLinkType linkType;

    @Override
    public Integer getObjectID() {
        return this.id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isDeleted() {
        return Optional.ofNullable(this.deleted).orElse(false);
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public ActivationLinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(ActivationLinkType linkType) {
        this.linkType = linkType;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}