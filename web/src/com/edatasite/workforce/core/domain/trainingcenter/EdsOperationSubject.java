package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/16/12
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "operation_subject")
public class EdsOperationSubject extends EdsObject {

    private static final String ENQUIRY = "ENQUIRY";
    private static final String QUOTATION = "QUOTATION";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private EdsProductCategory subject;

    private Integer relationID; //for example: relationID will be quotationID/enquiryID

    private String relationType; //for example: relationType will be QUOTATION/ENQUIRY

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsProductCategory getSubject() {
        return subject;
    }

    public void setSubject(EdsProductCategory subject) {
        this.subject = subject;
    }

    public Integer getRelationID() {
        return relationID;
    }

    public void setRelationID(Integer relationID) {
        this.relationID = relationID;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
}
