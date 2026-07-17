package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.form.HelpDocumentItem;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: Dilshod Madrahimov
 * Date: 2/27/13
 * Time: 4:52 PM
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "helpdocument")
public class EdsHelpDocument extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "title")
    @Type(type = "text")
    private String title;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "link")
    @Type(type = "text")
    private String link;

    @Column(name = "hostName")
    private String hostName;

    @Column(name = "section")
    private String section;

    @Column(name = "form")
    private String form;

    @Column(name = "block")
    private String block;

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

    public HelpDocumentItem getRPC() {
        HelpDocumentItem item = new HelpDocumentItem();
        item.setObjectID(getObjectID());
        item.setTitle(getTitle());
        item.setDescription(getDescription());
        item.setLink(getLink());
        item.setHostName(getHostName());
        item.setSection(getSection());
        item.setForm(getForm());
        item.setBlock(getBlock());
        return item;
    }
}
