package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/21/15
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payroll_batch")
public class EdsPayrollBatch extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;
    @Type(type = "text")
    private String description;

    @Column(name = "type")
    private Integer type; //0-by Department, 1-by Position

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "group_managers",
            joinColumns = {@JoinColumn(name = "groupid")},
            inverseJoinColumns = {@JoinColumn(name = "managerid")})
    private Set<EdsEmployee> managers = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "emp_batch",
            joinColumns = {@JoinColumn(name = "batch_id")},
            inverseJoinColumns = {@JoinColumn(name = "emp_id")})
    private List<EdsEmployee> employees;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsCrmAccount client;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsProject project;

    private Boolean deleted;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public Boolean isDeleted() {
        return deleted != null ? deleted : Boolean.FALSE;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<EdsEmployee> getManagers() {
        return managers;
    }

    public void setManagers(Set<EdsEmployee> managers) {
        this.managers = managers;
    }

    public List<EdsEmployee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EdsEmployee> employees) {
        this.employees = employees;
    }

    public EdsCrmAccount getClient() {
        return client;
    }

    public void setClient(EdsCrmAccount cliet) {
        this.client = cliet;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public SelectItem asSelectItem() {
        return new SelectItem(objectID, name);
    }
}
