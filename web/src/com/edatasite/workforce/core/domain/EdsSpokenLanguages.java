package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by Ilhom Lutfullaev on 03.11.2017.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "spokenlanguages")
public class EdsSpokenLanguages extends EdsObject {

    public static String LANGUAGE = "_LANGUAGES";
    public static String LANGUAGE_LEVELS = "_LANGUAGE_LEVELS";
    public static String TYPE_EMPLOYEE = "EMPLOYEE";
    public static String TYPE_CANDIDATE = "CANDIDATE";
    public static String TYPE_VACANCY = "VACANCY";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "languageId")
    private EdsReference language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "levelId")
    private EdsReference level;

    private Integer entityId;
    private String entityType;

    public EdsReference getLanguage() {
        return language;
    }

    public void setLanguage(EdsReference language) {
        this.language = language;
    }

    public EdsReference getLevel() {
        return level;
    }

    public void setLevel(EdsReference level) {
        this.level = level;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
