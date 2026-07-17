package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.enums.Office365ItemBodyType;
import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import org.json.simple.JSONObject;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365OutlookItemBody extends Office365BaseItem {
    private String content;
    private Office365ItemBodyType contentType;

    public Office365OutlookItemBody() {
    }

    /**
     * @param data
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#ItemBody
     */
    public Office365OutlookItemBody(JSONObject data) {
        this.content = this.getString(data, "Content");
        this.contentType = Office365ItemBodyType.valueOf(this.getString(data, "ContentType"));
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("Content", this.getContent());
        json.put("ContentType", this.getContentType().name());

        return json;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Office365ItemBodyType getContentType() {
        return contentType;
    }

    public void setContentType(Office365ItemBodyType contentType) {
        this.contentType = contentType;
    }
}
