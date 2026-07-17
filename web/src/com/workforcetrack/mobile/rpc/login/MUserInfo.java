package com.workforcetrack.mobile.rpc.login;

import com.edatasite.workforce.gwt.core.server.rpc.UserSignUPSessionID;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/25/11
 * Time: 10:50 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "userInfo")
public class MUserInfo {

    private Integer objectID;
    private String sessionID;

    private String name;
    private String fullName;
    private String companyName;
    private String imageURL;

    private boolean hasAccess;
    private boolean companyActive;

    private MCompanyList mCompanyList;

    private String roles;

    public MUserInfo() {
    }

    public MUserInfo(UserSignUPSessionID userSignUPSessionID) {
        if (userSignUPSessionID != null) {
            this.objectID = userSignUPSessionID.getUserId();
            this.sessionID = userSignUPSessionID.getSessionID();
            this.name = userSignUPSessionID.getUserName();
            this.fullName = userSignUPSessionID.getFullName();
            this.companyName = userSignUPSessionID.getCompanyName();
            this.imageURL = userSignUPSessionID.getImageUrl();
            this.hasAccess = userSignUPSessionID.isHasAccess();
            this.companyActive = userSignUPSessionID.getCompanyActive();
            this.roles = userSignUPSessionID.getRoles();
        }
    }

    /*public static boolean convert(UserSignUPSessionID userSignUPSessionID, MUserInfo mUserInfo, boolean fromMUserInfo) {

        if (userSignUPSessionID == null || mUserInfo == null)
            return false;

        try{
            if (fromMUserInfo) {
                userSignUPSessionID.setUserID(mUserInfo.objectID);
                userSignUPSessionID.set
            }
        }
    }
*/

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public boolean isCompanyActive() {
        return companyActive;
    }

    public void setCompanyActive(boolean companyActive) {
        this.companyActive = companyActive;
    }

    public MCompanyList getmCompanyList() {
        return mCompanyList;
    }

    public void setmCompanyList(MCompanyList mCompanyList) {
        this.mCompanyList = mCompanyList;
    }
}
