package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Jul 4, 2010
 * Time: 5:55:59 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "crmcontactitemparams",
        indexes = {
                @Index(columnList = "contactid", name = "crmcontactitemparams_contactid_idx")
        })
public class EdsCrmContactItemParams extends EdsObject implements Constants {
    //params
    public static final int EMAIL = CONTACT_EMAILS;
    public static final int PHONE = CONTACT_PHONES;
    public static final int WEBSITE = CONTACT_WEBSITES;
    public static final int IMADDRESS = CONTACT_IMADDRESSES;
    public static final int RELATIONSHIPS = CONTACT_RELATIONSHIPS;
    public static final int TELEGRAM_CHATS = CONTACT_TELEGRAMS;
    //relations
    public static final int HOME = G_HOME;
    public static final int WORK = G_WORK;
    public static final int MOBILE = G_MOBILE;
    public static final int FAX = G_FAX;
    public static final int WHATS_APP = G_WHATS_APP;
    public static final int TELEGRAM = G_TELEGRAM;
    public static final int VIBER = G_VIBER;
    public static final int HOME_FAX = G_HOME_FAX;
    public static final int WORK_FAX = G_WORK_FAX;
    public static final int PAGER = G_PAGER;
    public static final int OTHER = G_OTHER;
    public static final int EXTENSION = G_EXTENSION;

    public static final int HOME_PAGE = G_HOME_PAGE;
    public static final int FTP = G_FTP;
    public static final int BLOG = G_BLOG;
    public static final int PROFILE = G_PROFILE;
    public static final int LINKEDIN = G_LINKEDIN;
    public static final int FACEBOOK = G_FACEBOOK;
    public static final int TG_USERNAME = TG_USER;
    public static final int TWITTER = G_TWITTER;
    public static final int INSTAGRAM = G_INSTAGRAM;
    //imAddress begin
    public static final int GOOGLE_TALK = G_GOOGLE_TALK;
    public static final int AIM = G_AIM;
    public static final int YAHOO = G_YAHOO;
    public static final int SKYPE = G_SKYPE;
    public static final int QQ = G_QQ;
    public static final int MSN = G_MSN;
    public static final int ICQ = G_ICQ;
    public static final int JABBER = G_JABBER;
    //imAddress End
    public static final int SPOUSE = G_SPOUSE;
    public static final int CHILD = G_CHILD;
    public static final int MOTHER = G_MOTHER;
    public static final int FATHER = G_FATHER;
    public static final int PARENT = G_PARENT;
    public static final int BROTHER = G_BROTHER;
    public static final int SISTER = G_SISTER;
    public static final int FRIEND = G_FRIEND;
    public static final int RELATIVE = G_RELATIVE;
    public static final int DOMESTIC_PARTNER = G_DOMESTIC_PARTNER;

    public static final int[] EMAIL_PARAMS = new int[]{HOME, WORK, OTHER};
    public static final int[] PHONE_PARAMS = new int[]{HOME, WORK, MOBILE, HOME_FAX, WORK_FAX, PAGER, OTHER, EXTENSION};
    public static final int[] IM_PARAMS = new int[]{GOOGLE_TALK, AIM, YAHOO, SKYPE, QQ, MSN, ICQ, JABBER};
    public static final int[] WEB_PARAMS = new int[]{HOME, WORK, HOME_PAGE, FTP, BLOG, PROFILE, OTHER, LINKEDIN, FACEBOOK, TWITTER, INSTAGRAM};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contactid")
    private EdsCrmContact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private EdsCrmAccount account;

    @Column(name = "paramid")
    private Integer param;            // for parameters type : contact emails, IM addresses, web sites, ...

    @Column(name = "relationid")
    private Integer relation;         // for parameters relation : home, work, other, ... (or reference.id for relationships...)

    @Column(name = "value", length = 1000)
    private String value;

    @Column(name = "lastupdatetime")
    private Date lastUpdateTime = new Date();

    public EdsCrmContactItemParams(Integer param) {
        super();
        this.param = param;
    }

    public EdsCrmContactItemParams() {
        super();
    }

    @Override
    public String getName() {
        return getValue();
    }

    public static EdsCrmContactItemParams getPrimaryAsDomainObject(List<EdsCrmContactItemParams> itemParams) {
        return getFirstItemParam(itemParams, true, EdsCrmContactItemParams.HOME);
    }

    public static EdsCrmContactItemParams getFirstItemParam(List<EdsCrmContactItemParams> itemParams, boolean ifNotExistReturnFirst, int... relations) {
        if (itemParams != null && itemParams.size() > 0) {
            for (EdsCrmContactItemParams itemParam : itemParams) {
                if (relations != null && relations.length > 0) {
                    for (int relation : relations) {
                        if (itemParam != null && itemParam.getRelation() != null && relation == itemParam.getRelation()) {
                            return itemParam;
                        }
                    }
                } else {
                    if (itemParam != null && itemParam.getRelation() != null && HOME == itemParam.getRelation()) {
                        return itemParam;
                    }
                }
                if (ifNotExistReturnFirst) {
                    return itemParams.get(0);
                }
            }
            if (!ifNotExistReturnFirst) {
                return null;
            }
            return itemParams.get(0);
        }
        return null;
    }

    public static String getFirstItemParamValue(List<EdsCrmContactItemParams> itemParams, boolean ifNotExistReturnFirst, int... relations) {
        EdsCrmContactItemParams param = getFirstItemParam(itemParams, ifNotExistReturnFirst, relations);
        return param != null ? param.getValue() : null;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsCrmContact getContact() {
        return contact;
    }

    public void setContact(EdsCrmContact contact) {
        this.contact = contact;
    }

    public EdsCrmAccount getAccount() {
        return account;
    }

    public void setAccount(EdsCrmAccount account) {
        this.account = account;
    }

    public Integer getParam() {
        return param;
    }

    public void setParam(Integer param) {
        this.param = param;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        EdsCrmContactItemParams that = (EdsCrmContactItemParams) o;

        if (contact != null ? !contact.equals(that.contact) : that.contact != null) {
            return false;
        }
        if (lastUpdateTime != null ? !lastUpdateTime.equals(that.lastUpdateTime) : that.lastUpdateTime != null) {
            return false;
        }
        if (objectID != null ? !objectID.equals(that.objectID) : that.objectID != null) {
            return false;
        }
        if (param != null ? !param.equals(that.param) : that.param != null) {
            return false;
        }
        if (relation != null ? !relation.equals(that.relation) : that.relation != null) {
            return false;
        }
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (objectID != null ? objectID.hashCode() : 0);
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        result = 31 * result + (param != null ? param.hashCode() : 0);
        result = 31 * result + (relation != null ? relation.hashCode() : 0);
        result = 31 * result + value.hashCode();
        result = 31 * result + (lastUpdateTime != null ? lastUpdateTime.hashCode() : 0);
        return result;
    }
}
