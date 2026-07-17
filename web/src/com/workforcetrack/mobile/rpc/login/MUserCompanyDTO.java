package com.workforcetrack.mobile.rpc.login;

import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/9/11
 * Time: 10:09 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "userInfo")
public class MUserCompanyDTO {

    private Integer userID;
    private String userName;
    private Integer companyID;
    private String sessionID;
    private String companyName;
    private String companyDescription;
    private String logo;

    private UsagePlanItem usagePlanItem;
    @XmlElementWrapper(name = "roleItems")
    @XmlElement(name = "roleItem")
    List<MSelectItem> roleItems;

    @XmlElementWrapper(name = "keyValueStructList")
    @XmlElement(name = "keyValueStruct")
    List<KeyValueStruct> keyValueStructs;

    private Boolean active;

    public MUserCompanyDTO() {
    }

    public MUserCompanyDTO(UserCompanyDTO userCompanyDTO) {
        this.userID = userCompanyDTO.getUserID();
        this.userName = userCompanyDTO.getUserName();
        this.companyID = userCompanyDTO.getCompanyID();
        this.sessionID = userCompanyDTO.getSessionID();
        this.companyName = userCompanyDTO.getCompanyName();
        this.companyDescription = userCompanyDTO.getCompanyDescription();
        this.logo = userCompanyDTO.getLogo();
    }

    public static boolean convert(UserCompanyDTO userCompanyDTO, MUserCompanyDTO mUserCompanyDTO, boolean fromUserCompanyDTO) {
        if (userCompanyDTO == null || mUserCompanyDTO == null) {
            return false;
        }

        try {
            if (fromUserCompanyDTO) {
                mUserCompanyDTO.setUserID(userCompanyDTO.getUserID());
                mUserCompanyDTO.setUserName(userCompanyDTO.getUserName());
                mUserCompanyDTO.setCompanyID(userCompanyDTO.getCompanyID());
                mUserCompanyDTO.setSessionID(userCompanyDTO.getSessionID());
                mUserCompanyDTO.setCompanyName(userCompanyDTO.getCompanyName());
                mUserCompanyDTO.setCompanyDescription(userCompanyDTO.getCompanyDescription());
                mUserCompanyDTO.setLogo(userCompanyDTO.getLogo());
            } else {
                userCompanyDTO.setUserID(mUserCompanyDTO.getUserID());
                userCompanyDTO.setUserName(mUserCompanyDTO.getUserName());
                userCompanyDTO.setCompanyID(mUserCompanyDTO.getCompanyID());
                userCompanyDTO.setSessionID(mUserCompanyDTO.getSessionID());
                userCompanyDTO.setCompanyName(mUserCompanyDTO.getCompanyName());
                userCompanyDTO.setCompanyDescription(mUserCompanyDTO.getCompanyDescription());
                userCompanyDTO.setLogo(mUserCompanyDTO.getLogo());

            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<KeyValueStruct> getKeyValueStructs() {
        return keyValueStructs;
    }

    public void setKeyValueStructs(List<KeyValueStruct> keyValueStructs) {
        this.keyValueStructs = keyValueStructs;
    }

    public UsagePlanItem getUsagePlanItem() {
        return usagePlanItem;
    }

    public void setUsagePlanItem(UsagePlanItem usagePlanItem) {
        this.usagePlanItem = usagePlanItem;
    }

    public List<MSelectItem> getRoleItems() {
        if (roleItems == null) {
            roleItems = new ArrayList<>();
        }
        return roleItems;
    }

    public void setRoleItems(List<MSelectItem> roleItems) {
        this.roleItems = roleItems;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


}
