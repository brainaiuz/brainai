package com.edatasite.workforce.core.domain.network;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsUser;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 7, 2010
 * Time: 4:00:35 PM
 * To change this template use File | Settings | File Templates.
 */

@MappedSuperclass
public class EdsCooContact extends EdsObject {

    @Column(name = "confirmed")
    private boolean confirmed;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    @Column(name = "pending")
    private boolean pending;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private EdsUser user;

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }
}
