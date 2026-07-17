package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.link.LinkTO;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 3/30/2018.
 */
public class CreateEventTO extends ResponseData {

    private Integer id;
    private String what;
    private String where;
    private String description;
    private WhenTO when;
    private RecurrenceTO recurrence;
    private ArrayList<LinkTO> links;
    private ShareWithTO share_with;
    private ArrayList<TimeTO> reminders;
    private ArrayList<AttachmentTO> draft_attachments;
    private ArrayList<Object> custom_fields;

    public CreateEventTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WhenTO getWhen() {
        return when;
    }

    public void setWhen(WhenTO when) {
        this.when = when;
    }

    public RecurrenceTO getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(RecurrenceTO recurrence) {
        this.recurrence = recurrence;
    }

    public ArrayList<LinkTO> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<LinkTO> links) {
        this.links = links;
    }

    public ShareWithTO getShare_with() {
        return share_with;
    }

    public void setShare_with(ShareWithTO share_with) {
        this.share_with = share_with;
    }

    public ArrayList<TimeTO> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<TimeTO> reminders) {
        this.reminders = reminders;
    }

    public ArrayList<AttachmentTO> getDraft_attachments() {
        return draft_attachments;
    }

    public void setDraft_attachments(ArrayList<AttachmentTO> draft_attachments) {
        this.draft_attachments = draft_attachments;
    }

    public ArrayList<Object> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<Object> custom_fields) {
        this.custom_fields = custom_fields;
    }
}
