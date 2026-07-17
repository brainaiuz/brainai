package com.edatasite.workforce.rest.v2.release10.core.to.crm.email;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Farrukh Abdurakhmonov on 4/24/2018.
 */
public class DraftEmailInfoTO extends ResponseData {
    private EmailSenderTO from;
    private ArrayList<EmailSenderTO> to;
    private EmailSenderTO reply_to;
    private ArrayList<EmailSenderTO> list_cc;
    private ArrayList<EmailSenderTO> list_bcc;
    private String subject;
    private String content;
    private String html_data;
    private ArrayList<EmailAttachmentsTO> draft_attachments;

    public EmailSenderTO getFrom() {
        return from;
    }

    public void setFrom(EmailSenderTO from) {
        this.from = from;
    }

    public ArrayList<EmailSenderTO> getTo() {
        return to;
    }

    public void setTo(ArrayList<EmailSenderTO> to) {
        this.to = to;
    }

    public EmailSenderTO getReply_to() {
        return reply_to;
    }

    public void setReply_to(EmailSenderTO reply_to) {
        this.reply_to = reply_to;
    }

    public ArrayList<EmailSenderTO> getList_cc() {
        return list_cc;
    }

    public void setList_cc(ArrayList<EmailSenderTO> list_cc) {
        this.list_cc = list_cc;
    }

    public ArrayList<EmailSenderTO> getList_bcc() {
        return list_bcc;
    }

    public void setList_bcc(ArrayList<EmailSenderTO> list_bcc) {
        this.list_bcc = list_bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHtml_data() {
        return html_data;
    }

    public void setHtml_data(String html_data) {
        this.html_data = html_data;
    }

    public ArrayList<EmailAttachmentsTO> getDraft_attachments() {
        return draft_attachments;
    }

    public void setDraft_attachments(ArrayList<EmailAttachmentsTO> draft_attachments) {
        this.draft_attachments = draft_attachments;
    }
}
