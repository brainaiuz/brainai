package com.edatasite.workforce.core.domain.network;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 7, 2010
 * Time: 4:15:01 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "peertopeercontact")
public class EdsPeerToPeerContact extends EdsCooContact {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peer_id")
    private EdsUser peer;

    @Column(name = "personalnote", length = 1000)
    private String personalNote;

    @Column(name = "relationship")
    private String relationship;

    public EdsUser getPeer() {
        return peer;
    }

    public void setPeer(EdsUser peer) {
        this.peer = peer;
    }

    public String getPersonalNote() {
        return personalNote;
    }

    public void setPersonalNote(String personalNote) {
        this.personalNote = personalNote;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
