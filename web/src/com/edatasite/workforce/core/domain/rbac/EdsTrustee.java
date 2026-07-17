package com.edatasite.workforce.core.domain.rbac;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

/**
 * User: Abdulaziz
 * Date: Jan 6, 2010
 * Time: 7:05:57 PM
 * Specifies a user account, group account, or logon session to which an access control entry applies.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "trustee", uniqueConstraints = @UniqueConstraint(columnNames = {"trusteetype", "trusteeID"}))
public class EdsTrustee extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trusteetype")
    @ForeignKey(name = "none")
    private EdsTrusteeType type;

    private Integer trusteeID;// trustee id it may be user id or specific group id

    public boolean validate(Integer trusteeType) {
        return trusteeType.equals(type.getObjectID());
    }

    public EdsTrusteeType getType() {
        return type;
    }

    public void setType(EdsTrusteeType type) {
        this.type = type;
    }

    public Integer getTrusteeID() {
        return trusteeID;
    }

    public void setTrusteeID(Integer trusteeID) {
        this.trusteeID = trusteeID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
}
