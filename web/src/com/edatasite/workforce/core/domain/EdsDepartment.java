package com.edatasite.workforce.core.domain;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsDepartmentCustomFields;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.enums.ChildOrientation;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrDepartmentRepresenter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.team.client.rpc.TeamSolrItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "team")
public class EdsDepartment extends EdsTraceable implements ObjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderId")
    private EdsEmployee leader;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "team_vacants",
            joinColumns = {@JoinColumn(name = "department_id")},
            inverseJoinColumns = {@JoinColumn(name = "vacant_id")})
    private List<EdsEmployee> vacants = new ArrayList<>();

    @Column(insertable = false, updatable = false)
    private Integer leaderId;

    @Column(insertable = false, updatable = false)
    private Integer leaderId2;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderId2")
    private EdsEmployee leader2;

    @Column(insertable = false, updatable = false)
    private Integer leaderId3;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderId3")
    private EdsEmployee leader3;

    @Column(insertable = false, updatable = false)
    private Integer leaderId4;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderId4")
    private EdsEmployee leader4;

    @Column(insertable = false, updatable = false)
    private Integer leaderId5;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderId5")
    private EdsEmployee leader5;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Set<EdsEmployeeDepartment> members = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsDepartmentCustomFields customFields;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentNameId")
    private EdsReference departmentName;

    private String email;

    @Column(name = "active", columnDefinition = " boolean default true")
    private Boolean active = true;

    @Column(name = "externalGUID")
    private String externalGUID;

    @Column(name = "intnumber")
    private Integer intNumber;

    @Column(name = "numberData")
    private String numberData;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "description_locale_id")
    private EdsReferenceLocale descriptionLocale;

    @Column(name = "short_description")
    private String shortDescription;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "short_description_locale_id")
    private EdsReferenceLocale shortDescriptionLocale;

    @Column(name = "child_orientation")
    @Enumerated(EnumType.STRING)
    private ChildOrientation childOrientation = ChildOrientation.HORIZONTAL;

    private String color;

    private Date startDate;

    private Date endDate;

    @Column(name = "isDeleted")
    private Boolean deleted = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localeId")
    private EdsReferenceLocale departmentNameLocale;


    private Boolean leaderIsVacant;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "locationId")
    private EdsLocation location;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @Column(name = "creationTime")
    private Date creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmployee getLeader() {
        return leader;
    }

    public void setLeader(EdsEmployee leader) {
        this.leader = leader;
    }

    public EdsEmployee getLeader2() {
        return leader2;
    }

    public void setLeader2(EdsEmployee leader2) {
        this.leader2 = leader2;
    }

    public EdsEmployee getLeader3() {
        return leader3;
    }

    public void setLeader3(EdsEmployee leader3) {
        this.leader3 = leader3;
    }

    public EdsEmployee getLeader4() {
        return leader4;
    }

    public void setLeader4(EdsEmployee leader4) {
        this.leader4 = leader4;
    }

    public EdsEmployee getLeader5() {
        return leader5;
    }

    public void setLeader5(EdsEmployee leader5) {
        this.leader5 = leader5;
    }

    public Set<EdsEmployeeDepartment> getMembers() {
        return members;
    }

    public void setMembers(Set<EdsEmployeeDepartment> members) {
        this.members = members;
    }

    public String getName() {
        if (getLocale() != null) {
            String lang = ServerUtils.getUserLocale().getLanguage();
            if (StringUtils.isNotBlank(getLocale().getLocaleByCode(lang))) {
                return getLocale().getLocaleByCode(lang);
            }
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EdsEmployee> getVacants() {
        return vacants;
    }

    public void setVacants(List<EdsEmployee> vacants) {
        this.vacants = vacants;
    }

    public String getRealDepartmentName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }


    public void setShortDescriptionLocale(EdsReferenceLocale shortDescriptionLocale) {
        this.shortDescriptionLocale = shortDescriptionLocale;
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

    public Boolean getDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsReference getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(EdsReference departmentName) {
        this.departmentName = departmentName;
    }

    public TeamSolrItem getSolrRPC() {
        TeamSolrItem result = new TeamSolrItem();

        result.setObjectId(getObjectID());
        result.setName(getName());
        result.setNumber(getNumberData());
        if (getDepartmentName() != null && getDepartmentName().getLocale() != null) {
            EdsReferenceLocale locale = getDepartmentName().getLocale();
            result.setNameEn(locale.getEnglish() != null ? locale.getEnglish() : getName());
            result.setNameRu(locale.getRussian() != null ? locale.getRussian() : getName());
            result.setNameAr(locale.getArabic() != null ? locale.getArabic() : getName());
            result.setNameUz(locale.getUzbek() != null ? locale.getUzbek() : getName());
        } else if (getLocale() != null) {
            EdsReferenceLocale locale = getLocale();
            result.setNameEn(locale.getEnglish() != null ? locale.getEnglish() : getName());
            result.setNameRu(locale.getRussian() != null ? locale.getRussian() : getName());
            result.setNameAr(locale.getArabic() != null ? locale.getArabic() : getName());
            result.setNameUz(locale.getUzbek() != null ? locale.getUzbek() : getName());
        }
        result.setStartDate(getStartDate());
        result.setStatusName(isActive());

        if (getLocation() != null) {
            EdsLocation location = getLocation();
            result.setLocation(location.getAsSelectItem());
        }

        if (getLeader() != null) {
            EdsEmployee leader = getLeader();
            result.setLeader(leader.getAsSelectItem());
        }
        result.setLeaderIsVacant(getLeaderIsVacant());

        if (getCreator() != null) {
            result.setCreatedDate(getCreationTime());
            result.setCreatedBy(getCreator().getAsSelectItem());
        }

        if (getUpdater() != null) {
            result.setModifiedDate(getLastUpdateTime());
            result.setModifiedBy(getUpdater().getAsSelectItem());
        }

        return result;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
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
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean hasAccess(EdsUser user) {
        return user.getCompany().getObjectID().equals(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public EdsReferenceLocale getLocale() {
        return getDepartmentName() != null ? getDepartmentName().getLocale() : departmentNameLocale;
    }

    public void setLocale(EdsReferenceLocale departmentNameLocale) {
        this.departmentNameLocale = departmentNameLocale;
    }

    public void setLeaderIsVacant(boolean leaderIsVacant) {
        this.leaderIsVacant = leaderIsVacant;
    }

    public Boolean getLeaderIsVacant() {
        return leaderIsVacant;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getNumberData() {
        return numberData;
    }

    public void setNumberData(String numberData) {
        this.numberData = numberData;
    }

    public EdsDepartmentCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsDepartmentCustomFields customFields) {
        this.customFields = customFields;
    }

    public String getExternalGUID() {
        return externalGUID;
    }

    public void setExternalGUID(String externalGUID) {
        this.externalGUID = externalGUID;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        this.location = location;
    }

    public ChildOrientation getChildOrientation() {
        return childOrientation;
    }

    public void setChildOrientation(ChildOrientation childOrientation) {
        this.childOrientation = childOrientation;
    }

    public EdsReferenceLocale getDescriptionLocale() {
        return descriptionLocale;
    }

    public EdsReferenceLocale getShortDescriptionLocale() {
        return shortDescriptionLocale;
    }

    public HashMap<String, String> getDescriptionLocaleMap() {
        return convertLocaleToMap(this.descriptionLocale);
    }

    public HashMap<String, String> getShortDescriptionLocaleMap() {
        return convertLocaleToMap(this.shortDescriptionLocale); // Now using the correct field
    }

    private HashMap<String, String> convertLocaleToMap(EdsReferenceLocale locale) {
        HashMap<String, String> map = new HashMap<>();

        if (locale == null) {
            return map;
        }

        map.put("uz", Objects.requireNonNullElse(locale.getUzbek(), ""));
        map.put("ru", Objects.requireNonNullElse(locale.getRussian(), ""));
        map.put("en", Objects.requireNonNullElse(locale.getEnglish(), ""));
        map.put("ar", Objects.requireNonNullElse(locale.getArabic(), ""));

        return map;
    }

    public void setDescriptionLocale(EdsReferenceLocale descriptionLocale) {
        this.descriptionLocale = descriptionLocale;
    }


    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID.equals(CustomFormConstants.DEPARTMENT_CREATED_BY)) {
            return getCreator().getFullName();
        } else if (fieldID.equals(CustomFormConstants.DEPARTMENT_DESCRIPTION)) {
            return getDescription();
        } else if (fieldID.equals(CustomFormConstants.DEPARTMENT_EMAIL)) {
            return getEmail();
        } else if (fieldID.equals(CustomFormConstants.DEPARTMENT_LEADER)) {
            return getLeader() != null ? getLeader().getFullName() : null;
        } else if (fieldID.equals(CustomFormConstants.DEPARTMENT_NAME)) {
            return getName();
        } else if (fieldID.equals(CustomFormConstants.DEPARTMENT_NUMBER)) {
            return getNumberData();
        } else if (fieldID.equals(CustomFormConstants.DEPARTMENT_START_DATE)) {
            return getStartDate();
        }
        return super.getRealValue(fieldID);
    }

    public SelectItem getAsSelectItem(String lang) {
        String name = switch (lang) {
            case "en" -> getLocale() != null ? getLocale().getEnglish() : getName();
            case "ru" -> getLocale() != null ? getLocale().getRussian() : getName();
            case "uz" -> getLocale() != null ? getLocale().getUzbek() : getName();
            case "ar" -> getLocale() != null ? getLocale().getArabic() : getName();
            default -> getName();
        };
        return new SelectItem(getObjectID(), getNumberData() + " -> " + name);
    }

    public SolrInputDocument wrapToSolrDocument(Integer companyID, SelectItem parentDepartment, EdsReferenceLocale locale, Long headCount) {
        String compositeId = companyID + "_" + getObjectID();
        SolrInputDocument doc = new SolrInputDocument();

        doc.addField(SolrDepartmentRepresenter.FIELD_COMPOSITE_ID, compositeId);
        doc.addField(SolrDepartmentRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrDepartmentRepresenter.FIELD_DEPARTMENT_ID, getObjectID());
        doc.addField(SolrDepartmentRepresenter.FIELD_NAME, getName());
        doc.addField(SolrDepartmentRepresenter.FIELD_NUMBER, getNumberData());
        doc.addField(SolrDepartmentRepresenter.FIELD_START_DATE, getStartDate());
        doc.addField(SolrDepartmentRepresenter.FIELD_STATUS_NAME, isActive());
        doc.addField(SolrDepartmentRepresenter.FIELD_ENCRYPTED_ID, EncryptionHelper.encryptURL("department/" + getObjectID().toString()));
        doc.addField(SolrDepartmentRepresenter.FIELD_HEAD_COUNT, headCount.toString());
        doc.addField(SolrDepartmentRepresenter.FIELD_CREATED_DATE, getCreationTime());
        doc.addField(SolrDepartmentRepresenter.FIELD_MODIFIED_DATE, getLastUpdateTime());

        String en = getName();
        String ru = getName();
        String ar = getName();
        String uz = getName();
        if (getDepartmentName() != null && getDepartmentName().getLocale() != null) {
            en = getDepartmentName().getLocale().getEnglish() != null ? getDepartmentName().getLocale().getEnglish() : getName();
            ru = getDepartmentName().getLocale().getRussian() != null ? getDepartmentName().getLocale().getRussian() : getName();
            ar = getDepartmentName().getLocale().getArabic() != null ? getDepartmentName().getLocale().getArabic() : getName();
            uz = getDepartmentName().getLocale().getUzbek() != null ? getDepartmentName().getLocale().getUzbek() : getName();
        } else if (getLocale() != null) {
            en = getLocale().getEnglish() != null ? getLocale().getEnglish() : getName();
            ru = getLocale().getRussian() != null ? getLocale().getRussian() : getName();
            ar = getLocale().getArabic() != null ? getLocale().getArabic() : getName();
            uz = getLocale().getUzbek() != null ? getLocale().getUzbek() : getName();
        }
        doc.addField(SolrDepartmentRepresenter.FIELD_NAME_EN, en);
        doc.addField(SolrDepartmentRepresenter.FIELD_NAME_RU, ru);
        doc.addField(SolrDepartmentRepresenter.FIELD_NAME_AR, ar);
        doc.addField(SolrDepartmentRepresenter.FIELD_NAME_UZ, uz);

        if (getLocation() != null) {
            doc.addField(SolrDepartmentRepresenter.FIELD_LOCATION_ID, getLocation().getObjectID());
            doc.addField(SolrDepartmentRepresenter.FIELD_LOCATION_NAME, (getLocation() != null && getLocation().getCode() != null) ? getLocation().getCode() + "->" + getLocation().getName() : getLocation().getName());
            doc.addField(SolrDepartmentRepresenter.FIELD_LOCATION_ID_NAME, getLocation().getObjectID() + SolrDepartmentRepresenter.SPLIT + (getLocation().getCode() != null ? getLocation().getCode() + "->" + getLocation().getName() : getLocation().getName()));
        }

        if (parentDepartment != null) {
            doc.addField(SolrDepartmentRepresenter.FIELD_DEPARTMENT_PARENT_ID, parentDepartment.getId());
            doc.addField(SolrDepartmentRepresenter.FIELD_DEPARTMENT_PARENT_NAME, parentDepartment.getCode() != null ? parentDepartment.getCode() + "->" + parentDepartment.getName() : parentDepartment.getName());
            doc.addField(SolrDepartmentRepresenter.FIELD_DEPARTMENT_PARENT_ID_NAME, (parentDepartment.getId() + SolrDepartmentRepresenter.SPLIT + (parentDepartment.getCode() + "->" + parentDepartment.getName())));
        }

        if (parentDepartment != null) {
            String parentDepartmentEn = parentDepartment.getName();
            String parentDepartmentRu = parentDepartment.getName();
            String parentDepartmentAr = parentDepartment.getName();
            String parentDepartmentUz = parentDepartment.getName();
            String parentCode = parentDepartment.getCode() + "->";

            if (locale != null) {
                parentDepartmentEn = locale.getEnglish() != null ? parentCode + locale.getEnglish() : parentDepartment.getName();

                parentDepartmentRu = locale.getRussian() != null ? parentCode + locale.getRussian() : parentDepartment.getName();

                parentDepartmentAr = locale.getArabic() != null ? parentCode + locale.getArabic() : parentDepartment.getName();

                parentDepartmentUz = locale.getUzbek() != null ? parentCode + locale.getUzbek() : parentDepartment.getName();

            }

            doc.addField(SolrDepartmentRepresenter.FIELD_DEPARTMENT_PARENT_NAME_EN, parentDepartmentEn);
            doc.addField(SolrDepartmentRepresenter.FIELD_DEPARTMENT_PARENT_NAME_RU, parentDepartmentRu);
            doc.addField(SolrDepartmentRepresenter.FIELD_DEPARTMENT_PARENT_NAME_AR, parentDepartmentAr);
            doc.addField(SolrDepartmentRepresenter.FIELD_DEPARTMENT_PARENT_NAME_UZ, parentDepartmentUz);

        }

        if (getCreator() != null) {
            doc.addField(SolrDepartmentRepresenter.FIELD_CREATED_BY_ID, getCreator().getObjectID());
            doc.addField(SolrDepartmentRepresenter.FIELD_CREATED_BY_NAME, getCreator().getFullName());
            doc.addField(SolrDepartmentRepresenter.FIELD_CREATED_BY_ID_NAME, getCreator().getObjectID() + SolrDepartmentRepresenter.SPLIT + getCreator().getFullName());
        }

        if (getLeader() != null) {
            doc.addField(SolrDepartmentRepresenter.FIELD_LEADER_ID, getLeader().getObjectID());
            doc.addField(SolrDepartmentRepresenter.FIELD_LEADER_NAME, getLeader().getName());
            doc.addField(SolrDepartmentRepresenter.FIELD_LEADER_ID_NAME, getLeader().getObjectID() + SolrDepartmentRepresenter.SPLIT + getLeader().getFullName());
        }

        doc.addField(SolrDepartmentRepresenter.FIELD_LEADER_IS_VACANT, getLeaderIsVacant());


        if (getUpdater() != null) {
            doc.addField(SolrDepartmentRepresenter.FIELD_MODIFIED_BY_ID, getUpdater().getObjectID());
            doc.addField(SolrDepartmentRepresenter.FIELD_MODIFIED_BY_NAME, getUpdater().getFullName());
            doc.addField(SolrDepartmentRepresenter.FIELD_MODIFIED_BY_ID_NAME, getUpdater().getObjectID() + SolrDepartmentRepresenter.SPLIT + getUpdater().getFullName());
        }

        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());
        return doc;
    }
}
