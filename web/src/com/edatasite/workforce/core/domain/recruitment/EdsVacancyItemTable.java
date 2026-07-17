package com.edatasite.workforce.core.domain.recruitment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "vacancyItemTable")
public class EdsVacancyItemTable extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    @Column(name = "name", length = 1000)
    @Type(type = "text")
    private String name;

    @Type(type = "text")
    private String description;

    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_id")
    private EdsVacancy vacancy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsVacancyItemTableCF customFields;

    public CustomTableRpc getRpc() {
        CustomTableRpc rpc = new CustomTableRpc();
        rpc.setId(getObjectID());
        rpc.setUuid(getUuid());
        rpc.setItemName(getName());
        rpc.setDescription(getDescription());
        return rpc;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Override
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public EdsVacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(EdsVacancy vacancy) {
        this.vacancy = vacancy;
    }

    public EdsVacancyItemTableCF getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsVacancyItemTableCF customFields) {
        this.customFields = customFields;
    }
}
