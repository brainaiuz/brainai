package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsTrainingContractCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TrainingContractItem;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Babayev xushnud
 * Date: 8/16/12
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "trainingcontract")
public class EdsTrainingContract extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "accountid")
    private EdsCrmAccount account;

    @Column(name = "enddate")
    private Date endDate;

    @Column(name = "startdate")
    private Date startDate;

    @Column(name = "updatedDate")
    private Date updatedDate;

    @Column(name = "name")
    private String name;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "trainingcontractcourse",
            joinColumns = {@JoinColumn(name = "trainingcontractid")},
            inverseJoinColumns = {@JoinColumn(name = "courseid")})
    private List<EdsCourse> courses = new ArrayList<>();

    @Column(name = "prepaid")
    private Boolean isPrePaid = Boolean.FALSE;

    @OneToOne
    @JoinColumn(name = "customfieldsid")
    private EdsTrainingContractCustomFields customFields;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCrmAccount getAccount() {
        return account;
    }

    public void setAccount(EdsCrmAccount accountId) {
        this.account = accountId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<EdsCourse> getCourses() {
        return courses;
    }

    public void setCourses(List<EdsCourse> courses) {
        this.courses = courses;
    }

    public Boolean getPrePaid() {
        return isPrePaid;
    }

    public void setPrePaid(Boolean prePaid) {
        isPrePaid = prePaid;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }


    public TrainingContractItem getRPC() {
        TrainingContractItem item = new TrainingContractItem();
        item.setObjectID(getObjectID());
        item.setName(getName());
        item.setDescription(getDescription());
        item.setStartDate(getStartDate());
        item.setEndDate(getEndDate());
        item.setPrepaid(getPrePaid() != null ? getPrePaid() : false);

        if (getAccount() != null) {
            item.setAccountItem(getAccount().getRPC(null, false));
            item.setAccountID(getAccount().getObjectID());
        }


        ArrayList<SelectItem> courseList = new ArrayList<>();
        ArrayList<Integer> courseIDs = new ArrayList<>();
        for (EdsCourse c : getCourses()) {
            courseList.add(new SelectItem(c.getObjectID(), c.getName()));
            courseIDs.add(c.getObjectID());
        }
        item.setCourses(courseList.toArray(new SelectItem[]{}));
        item.setCourseIDs(courseIDs);

        return item;
    }

    public EdsTrainingContractCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsTrainingContractCustomFields customFields) {
        this.customFields = customFields;
    }

}
