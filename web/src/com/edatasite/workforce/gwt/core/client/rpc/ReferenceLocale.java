package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class ReferenceLocale implements Serializable, IsSerializable {
    private Integer objectId;
    private String english;
    private String russian;
    private String arabic;
    private String uzbek;

    public ReferenceLocale() {
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getRussian() {
        return russian;
    }

    public void setRussian(String russian) {
        this.russian = russian;
    }

    public String getArabic() {
        return arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    public String getUzbek() {
        return uzbek;
    }

    public void setUzbek(String uzbek) {
        this.uzbek = uzbek;
    }
}
