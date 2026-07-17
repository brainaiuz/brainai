package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by KHasan on 12.11.15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "selectedEmployeeFromCalendar")
public class EdsSelectedEmployeeFromCalendar extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private EdsUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selectedUserId")
    private EdsUser selectedUser;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public EdsUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(EdsUser selectedUser) {
        this.selectedUser = selectedUser;
    }
}
