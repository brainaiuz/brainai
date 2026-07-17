package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Employee
 * Date: Nov 4, 2009
 * Time: 11:57:26 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "notecomment")
public class EdsNoteComment extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private EdsNoteHistory note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @Column(name = "comment", length = 10000)
    private String comment;

    private Date date;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsNoteHistory getNote() {
        return note;
    }

    public void setNote(EdsNoteHistory note) {
        this.note = note;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
