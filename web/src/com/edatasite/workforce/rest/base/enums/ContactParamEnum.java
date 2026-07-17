package com.edatasite.workforce.rest.base.enums;

import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 4/21/15 12:02 PM
 */
public enum ContactParamEnum implements IsSerializable {

    HOME(1, "HOME", "Home"),
    WORK(2, "WORK", "Work"),
    MOBILE(3, "MOBILE", "Mobile"),
    HOME_FAX(4, "HOME_FAX", "Home Fax"),
    WORK_FAX(5, "WORK_FAX", "Work Fax"),
    PAGER(6, "PAGER", "Pager"),
    OTHER(7, "OTHER", "Other"),
    HOME_PAGE(8, "HOME_PAGE", "Home Page"),
    FTP(9, "FTP", "FTP"),
    BLOG(10, "BLOG", "Blog"),
    PROFILE(11, "PROFILE", "Profile"),
    GOOGLE_TALK(12, "GOOGLE_TALK", "Google Talk"),
    AIM(13, "AIM", "AIM"),
    YAHOO(14, "YAHOO", "Yahoo"),
    SKYPE(15, "SKYPE", "Skype"),
    QQ(16, "QQ", "QQ"),
    MSN(17, "MSN", "MSN"),
    ICQ(18, "ICQ", "ICQ"),
    JABBER(19, "JABBER", "Jabber"),
    SPOUSE(20, "SPOUSE", "Spouse"),
    CHILD(21, "CHILD", "Child"),
    MOTHER(22, "MOTHER", "Mother"),
    FATHER(23, "FATHER", "Father"),
    PARENT(24, "PARENT", "Parent"),
    BROTHER(25, "BROTHER", "Brother"),
    SISTER(26, "SISTER", "Sister"),
    FRIEND(27, "FRIEND", "Friend"),
    RELATIVE(28, "RELATIVE", "Relative"),
    DOMESTIC_PARTNER(29, "DOMESTIC_PARTNER", "Domestic Partner"),
    EXTENSION(30, "EXTENSION", "Extension"),
    FAX(31, "FAX", "Fax"),
    TELEGRAM(37, "TELEGRAM", "Telegram"),
    VIBER(33, "VIBER", "Viber"),
    WHATSAPP(36, "WHATSAPP", "WhatsApp"),

    PHONES(1, "PHONES", "Phone Numbers"),//parent
    EMAILS(2, "EMAILS", "E-mails"),//parent e.g. includes : HOME.WORK,OTHER
    WEB_ADDRESSES(4, "WEB_ADDRESSES", "Web Addresses"),//parent
    IM_ADDRESSES(5, "IM_ADDRESSES", "IM Addresses"),//parent
    RELATIONSHIPS(6, "RELATIONSHIPS", "Relationships"),//parent
    ADDRESS_TYPES(7, "ADDRESS_TYPES", "Address Types");//parent

    Integer id;
    String code;
    String name;


    ContactParamEnum(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static SelectItemTO getParamAsSelectItemTO(Integer relationType) {
        return switch (relationType) {
            case 1 -> new SelectItemTO(ContactParamEnum.HOME.getId(), ContactParamEnum.HOME.getName(), ContactParamEnum.HOME.getCode(), null);
            case 2 -> new SelectItemTO(ContactParamEnum.WORK.getId(), ContactParamEnum.WORK.getName(), ContactParamEnum.WORK.getCode(), null);
            case 3 -> new SelectItemTO(ContactParamEnum.MOBILE.getId(), ContactParamEnum.MOBILE.getName(), ContactParamEnum.MOBILE.getCode(), null);
            case 4 -> new SelectItemTO(ContactParamEnum.HOME_FAX.getId(), ContactParamEnum.HOME_FAX.getName(), ContactParamEnum.HOME_FAX.getCode(), null);
            case 5 -> new SelectItemTO(ContactParamEnum.WORK_FAX.getId(), ContactParamEnum.WORK_FAX.getName(), ContactParamEnum.WORK_FAX.getCode(), null);
            case 6 -> new SelectItemTO(ContactParamEnum.PAGER.getId(), ContactParamEnum.PAGER.getName(), ContactParamEnum.PAGER.getCode(), null);
            case 7 -> new SelectItemTO(ContactParamEnum.OTHER.getId(), ContactParamEnum.OTHER.getName(), ContactParamEnum.OTHER.getCode(), null);
            case 8 -> new SelectItemTO(ContactParamEnum.HOME_PAGE.getId(), ContactParamEnum.HOME_PAGE.getName(), ContactParamEnum.HOME_PAGE.getCode(), null);
            case 9 -> new SelectItemTO(ContactParamEnum.FTP.getId(), ContactParamEnum.FTP.getName(), ContactParamEnum.FTP.getCode(), null);
            case 10 -> new SelectItemTO(ContactParamEnum.BLOG.getId(), ContactParamEnum.BLOG.getName(), ContactParamEnum.BLOG.getCode(), null);
            case 11 -> new SelectItemTO(ContactParamEnum.PROFILE.getId(), ContactParamEnum.PROFILE.getName(), ContactParamEnum.PROFILE.getCode(), null);
            case 12 -> new SelectItemTO(ContactParamEnum.GOOGLE_TALK.getId(), ContactParamEnum.GOOGLE_TALK.getName(), ContactParamEnum.GOOGLE_TALK.getCode(), null);
            case 13 -> new SelectItemTO(ContactParamEnum.AIM.getId(), ContactParamEnum.AIM.getName(), ContactParamEnum.AIM.getCode(), null);
            case 14 -> new SelectItemTO(ContactParamEnum.YAHOO.getId(), ContactParamEnum.YAHOO.getName(), ContactParamEnum.YAHOO.getCode(), null);
            case 15 -> new SelectItemTO(ContactParamEnum.SKYPE.getId(), ContactParamEnum.SKYPE.getName(), ContactParamEnum.SKYPE.getCode(), null);
            case 16 -> new SelectItemTO(ContactParamEnum.QQ.getId(), ContactParamEnum.QQ.getName(), ContactParamEnum.QQ.getCode(), null);
            case 17 -> new SelectItemTO(ContactParamEnum.MSN.getId(), ContactParamEnum.MSN.getName(), ContactParamEnum.MSN.getCode(), null);
            case 18 -> new SelectItemTO(ContactParamEnum.ICQ.getId(), ContactParamEnum.ICQ.getName(), ContactParamEnum.ICQ.getCode(), null);
            case 19 -> new SelectItemTO(ContactParamEnum.JABBER.getId(), ContactParamEnum.JABBER.getName(), ContactParamEnum.JABBER.getCode(), null);
            case 20 -> new SelectItemTO(ContactParamEnum.SPOUSE.getId(), ContactParamEnum.SPOUSE.getName(), ContactParamEnum.SPOUSE.getCode(), null);
            case 21 -> new SelectItemTO(ContactParamEnum.CHILD.getId(), ContactParamEnum.CHILD.getName(), ContactParamEnum.CHILD.getCode(), null);
            case 22 -> new SelectItemTO(ContactParamEnum.MOTHER.getId(), ContactParamEnum.MOTHER.getName(), ContactParamEnum.MOTHER.getCode(), null);
            case 23 -> new SelectItemTO(ContactParamEnum.FATHER.getId(), ContactParamEnum.FATHER.getName(), ContactParamEnum.FATHER.getCode(), null);
            case 24 -> new SelectItemTO(ContactParamEnum.PARENT.getId(), ContactParamEnum.PARENT.getName(), ContactParamEnum.PARENT.getCode(), null);
            case 25 -> new SelectItemTO(ContactParamEnum.BROTHER.getId(), ContactParamEnum.BROTHER.getName(), ContactParamEnum.BROTHER.getCode(), null);
            case 26 -> new SelectItemTO(ContactParamEnum.SISTER.getId(), ContactParamEnum.SISTER.getName(), ContactParamEnum.SISTER.getCode(), null);
            case 27 -> new SelectItemTO(ContactParamEnum.FRIEND.getId(), ContactParamEnum.FRIEND.getName(), ContactParamEnum.FRIEND.getCode(), null);
            case 28 -> new SelectItemTO(ContactParamEnum.RELATIVE.getId(), ContactParamEnum.RELATIVE.getName(), ContactParamEnum.RELATIVE.getCode(), null);
            case 29 -> new SelectItemTO(ContactParamEnum.DOMESTIC_PARTNER.getId(), ContactParamEnum.DOMESTIC_PARTNER.getName(), ContactParamEnum.DOMESTIC_PARTNER.getCode(), null);
            case 30 -> new SelectItemTO(ContactParamEnum.EXTENSION.getId(), ContactParamEnum.EXTENSION.getName(), ContactParamEnum.EXTENSION.getCode(), null);
            case 36 -> new SelectItemTO(ContactParamEnum.WHATSAPP.getId(), ContactParamEnum.WHATSAPP.getName(), ContactParamEnum.WHATSAPP.getCode(), null);
            case 37 -> new SelectItemTO(ContactParamEnum.TELEGRAM.getId(), ContactParamEnum.TELEGRAM.getName(), ContactParamEnum.TELEGRAM.getCode(), null);
            default -> null;
        };
    }

    public static Integer getParamIdByCode(String code) {
        if (code == null || "".equals(code)) {
            return null;
        }
        if (ContactParamEnum.HOME.getCode().equalsIgnoreCase(code)) {
            return ContactParamEnum.HOME.getId();
        }
        if (ContactParamEnum.WORK.getCode().equalsIgnoreCase(code)) {
            return ContactParamEnum.WORK.getId();
        }
        if (ContactParamEnum.OTHER.getCode().equalsIgnoreCase(code)) {
            return ContactParamEnum.OTHER.getId();
        }
        //todo set others
        return null;
    }
}
