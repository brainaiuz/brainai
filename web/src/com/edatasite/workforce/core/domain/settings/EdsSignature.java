package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 09.02.13
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "signature")
public class EdsSignature extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID")
    private EdsUser user;

    @Column(name = "signature")
    @Type(type = "text")
    private String signature;

    private Boolean showSignatureOnTop;

    private Boolean deleted = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isShowSignatureOnTop() {
        return showSignatureOnTop;
    }

    public void setShowSignatureOnTop(Boolean showSignatureOnTop) {
        this.showSignatureOnTop = showSignatureOnTop;
    }
}
