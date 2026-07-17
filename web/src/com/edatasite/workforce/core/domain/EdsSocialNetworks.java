package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: 21.12.2009
 * Time: 11:09:28
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "socialnetworks")
public class EdsSocialNetworks extends EdsObject {
    public static final int TWITTER = 1;
    public static final int FACEBOOK = 2;
    public static final int LINKEDIN = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "middlename")
    private String middleName;

    @Column(name = "createdDate")
    private Date createdDate;

    @Column(name = "type")
    private int type;

    // only for facebook
    @Column(name = "session")
    private String session;

    // only for facebook
    @Column(name = "sessionsecret")
    private String sessionSecret;

    // only for facebook
    @Column(name = "cookie", length = 1000)
    private String cookie;

    // only for LINKEDIN
    @Column(name = "token")
    private String token;

    // only for LINKEDIN
    @Column(name = "secrettoken")
    private String secretToken;

    @Column(name = "apikey" + LINKEDIN)
    private String apiKeyLinkedin;

    @Column(name = "apisecret" + LINKEDIN)
    private String apiSecretLinkedin;

    @Column(name = "deleted")
    private boolean deleted;


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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getSessionSecret() {
        return sessionSecret;
    }

    public void setSessionSecret(String sessionSecret) {
        this.sessionSecret = sessionSecret;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    public String getApiKeyLinkedin() {
        return apiKeyLinkedin;
    }

    public void setApiKeyLinkedin(String apiKeyLinkedin) {
        this.apiKeyLinkedin = apiKeyLinkedin;
    }

    public String getApiSecretLinkedin() {
        return apiSecretLinkedin;
    }

    public void setApiSecretLinkedin(String apiSecretLinkedin) {
        this.apiSecretLinkedin = apiSecretLinkedin;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
