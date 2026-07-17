package com.edatasite.workforce.gwt.core.client.rpc.form;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Dilshod Madrahimov
 * Date: 2/27/13
 * Time: 3:30 PM
 */
public class HelpDocumentItem implements IsSerializable {

    public static final String LEFT_BLOCK = "LEFT_BLOCK";
    public static final String RIGHT_TOP = "RIGHT_TOP";
    private Integer objectID;
    private String title;
    private String description;
    private String link;
    private String hostName;
    private String block;
    private String section;
    private String form;
    private String view;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public static SelectItem[] getPosition() {
        return new SelectItem[]{
                new SelectItem(1, LEFT_BLOCK),
                new SelectItem(2, RIGHT_TOP)
        };
    }


}
