package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: izaynutdinov
 * Date: 28.04.2007
 * Time: 17:17:44
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "ProfileIm")
public class EdsProfileIm extends EdsObject {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profileId")
    private EdsEmployeeProfile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imId")
    private EdsIm im;

    @Column(name = "account")
    private String account;


    public EdsProfileIm() {
    }

    public EdsProfileIm(EdsEmployeeProfile profile, EdsIm im) {
        this.profile = profile;
        this.im = im;
    }

    public EdsEmployeeProfile getProfile() {
        return profile;
    }

    public void setProfile(EdsEmployeeProfile profile) {
        this.profile = profile;
    }

    public EdsIm getIm() {
        return im;
    }

    public void setIm(EdsIm im) {
        this.im = im;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

}
