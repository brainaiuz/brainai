package com.edatasite.workforce.core.domain.rbac;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * User: Abdulaziz
 * Date: Jan 23, 2010
 * Time: 2:04:59 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "relationship")
public class EdsRelationship extends EdsObject {
    ////  RELATIONSHIP TYPE ////////////
    public static final String DIRECT = "DIRECT";
    public static final String INDIRECT = "INDIRECT";

    ////////////// Project Related relationships/////////
    public static final String PROJECT = "PROJECT";
    public static final String PROJECT_MEMBER = "PROJECT_MEMBER";
    public static final String PROJECT_MANAGER = "PROJECT_MANAGER";
    public static final String PROJECT_BACKUP_MANAGER = "PROJECT_BACKUP_MANAGER";

    ////////////// Task related relationships///////////
    public static final String TASK = "TASK";
    public static final String TASK_ASSIGNEE = "TASK_ASSIGNEE";
    public static final String TASK_NOT_ASSIGNEE = "TASK_NOT_ASSIGNEE";
    public static final String TASK_PROJECT_MANAGER = "TASK_PROJECT_MANAGER";
    public static final String TASK_PROJECT_BACKUP_MANAGER = "TASK_PROJECT_BACKUP_MANAGER";
    public static final String TASK_CLIENT = "TASK_CLIENT";
    public static final String TASK_DIRECTOR = "TASK_DIRECTOR";
    public static final String TASK_ADMINISTRATOR = "TASK_ADMINISTRATOR";
    public static final String TASK_REVIEWER = "TASK_REVIEWER";

    ////////////// Department related relationships///////////
    public static final String DEPARTMENT = "DEPARTMENT";
    public static final String DEPARTMENT_LEADER = "DEPARTMENT_LEADER";
    public static final String DEPARTMENT_MEMEBER = "DEPARTMENT_MEMEBER";

    ////////////// Assessment related relationships ////////////
    public static final String ASSESSMENT = "ASSESSMENT";
    public static final String ASSESSMENT_INITIATOR = "ASSESSMENT_INITIATOR";
    public static final String ASSESSMENT_REVIEWER = "ASSESSMENT_REVIEWER";
    public static final String ASSESSMENT_APPRAISEE = "ASSESSMENT_APPRAISEE";
    public static final String ASSESSMENT_COLLABORATOR = "ASSESSMENT_COLLABORATOR";

    ////////////// Document related relationships///////////
    public static final String DOCUMENT = "DOCUMENT";
    public static final String DOC_DIRECTOR = "DOC_DIRECTOR";
    public static final String DOC_ADMINISTRATOR = "DOC_ADMINISTRATOR";
    public static final String DOC_CREATOR = "DOC_CREATOR";
    public static final String DOC_OWNER = "DOC_OWNER";
    public static final String DOC_READER = "DOC_READER";
    public static final String DOC_VIEWER = "DOC_VIEWER";//CUSTOM

    public static final String EMAIL_OWNER = "EMAIL_OWNER";
    public static final String EMAIL_VIEWER = "EMAIL_VIEWER";

    public static final String CONTACT_OWNER = "CONTACT_OWNER";
    public static final String CONTACT_CO_OWNERS = "CONTACT_CO_OWNERS";
    public static final String CONTACT_EDITOR = "CONTACT_EDITOR";
    public static final String CONTACT_VIEWER = "CONTACT_VIEWER";

    public static final String PAGE_DIRECTOR = "PAGE_DIRECTOR";
    public static final String PAGE_ADMINISTRATOR = "PAGE_ADMINISTRATOR";
    public static final String PAGE_CREATOR = "PAGE_CREATOR";
    public static final String PAGE_OWNER = "PAGE_OWNER";
    public static final String PAGE_READER = "PAGE_READER";
    public static final String PAGE_VIEWER = "PAGE_VIEWER";//CUSTOM

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Column(unique = true)
    private String code;

    private String name;

    private Integer rank;

    private int entryType = BUILT_IN;

    private String description;

    private String relationType;// it may be Direct relation or Indirect

    private String entityName; // PROJECT, DEPARTMENT, TASK, ASSESSMENT

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public int getEntryType() {
        return entryType;
    }

    public void setEntryType(int entryType) {
        this.entryType = entryType;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
