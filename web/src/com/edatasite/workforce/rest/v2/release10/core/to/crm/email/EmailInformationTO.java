package com.edatasite.workforce.rest.v2.release10.core.to.crm.email;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Farrukh Abdurakhmonov on 4/14/2018.
 */
public class EmailInformationTO extends ResponseData {
    private String subject;
    private String date;
    private EmailSenderTO sender;
    private EmailSenderTO reply_to;
    private ArrayList<EmailSenderTO> list_to;
    private ArrayList<EmailSenderTO> list_cc;
    private EmailContentTO email_content;
    private ArrayList<EmailAttachmentsTO> attachments;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public EmailSenderTO getSender() {
        return sender;
    }

    public void setSender(EmailSenderTO sender) {
        this.sender = sender;
    }

    public EmailSenderTO getReply_to() {
        return reply_to;
    }

    public void setReply_to(EmailSenderTO reply_to) {
        this.reply_to = reply_to;
    }

    public ArrayList<EmailSenderTO> getList_to() {
        return list_to;
    }

    public void setList_to(ArrayList<EmailSenderTO> list_to) {
        this.list_to = list_to;
    }

    public ArrayList<EmailSenderTO> getList_cc() {
        return list_cc;
    }

    public void setList_cc(ArrayList<EmailSenderTO> list_cc) {
        this.list_cc = list_cc;
    }

    public EmailContentTO getEmail_content() {
        return email_content;
    }

    public void setEmail_content(EmailContentTO email_content) {
        this.email_content = email_content;
    }

    public ArrayList<EmailAttachmentsTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<EmailAttachmentsTO> attachments) {
        this.attachments = attachments;
    }
}
