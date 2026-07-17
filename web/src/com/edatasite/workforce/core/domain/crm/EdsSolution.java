package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCaseSolution;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.crm.client.rpc.SolutionItem;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 21-Jul-2009
 * Time: 13:32:41
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "solution")
public class EdsSolution extends EdsObject {

    public static final String _SOLUTION_STATUS = "_SOLUTION_STATUS";
    public static final String DRAFT = "DRAFT";
    public static final String REVIEWED = "REVIEWED";
    public static final String DUPLICATE = "DUPLICATE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private EdsEmployee assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    private EdsReference status;

    @Column(name = "question")
    @Type(type = "text")
    private String question;

    @Column(name = "answer")
    @Type(type = "text")
    private String answer;

    @Column(name = "title")
    private String title;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "details")
    private String details;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "caseSolutionId")
    private EdsCaseSolution caseSolution;

    public void setCase(EdsCase crmCase) {
        caseSolution = new EdsCaseSolution(crmCase, this);
    }

    public EdsCase getCase() {
        return caseSolution == null ? null : caseSolution.getCrmCase();
    }

    public SolutionItem getRPC(SolutionItem item) {
        if (item == null) {
            item = new SolutionItem();
        }
        item.setObjectId(getObjectID());
        item.setTitle(getTitle());
        item.setAnswer(getAnswer());
        item.setQuestion(getQuestion());
        item.setDetails(getDetails());
        if (getAssignee() != null) {
            item.setAssigneeId(getAssignee().getObjectID());
            item.setAssignee(getAssignee().getFullName());
        }
        if (getStatus() != null) {
            item.setStatusId(getStatus().getObjectID());
            item.setStatus(getStatus().getName());
        }
        return item;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsEmployee getAssignee() {
        return assignee;
    }

    public void setAssignee(EdsEmployee assignee) {
        this.assignee = assignee;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public EdsCaseSolution getCaseSolution() {
        return caseSolution;
    }

    public void setCaseSolution(EdsCaseSolution caseSolution) {
        this.caseSolution = caseSolution;
    }
}
