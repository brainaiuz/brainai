package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.type.MessageType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA,name = "whatsapp_message")
public class EdsWhatsAppMessage extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType messageType;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "receiver_phone_number")
    private String receiverPhoneNumber;

    @Column(name = "receiver_phone_number_id")
    private String receiverPhoneNumberId;

    @Column(name = "sender_phone_number")
    private String senderPhoneNumber;

    @Column(name = "sender_phone_number_id")
    private String senderPhoneNumberId;

    @Column(name = "text")
    private String text;

    @Column(name = "received_date")
    private Date receivedDate;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "media_id")
    private String mediaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crm_contact_id")
    private EdsCrmContact crmContact;

    @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "crm_account_id")
    private EdsCrmAccount crmAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EdsEmployee employee;

    @Column(name = "message_date")
    private Date messageDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public String getReceiverPhoneNumberId() {
        return receiverPhoneNumberId;
    }

    public void setReceiverPhoneNumberId(String receiverPhoneNumberId) {
        this.receiverPhoneNumberId = receiverPhoneNumberId;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public String getSenderPhoneNumberId() {
        return senderPhoneNumberId;
    }

    public void setSenderPhoneNumberId(String senderPhoneNumberId) {
        this.senderPhoneNumberId = senderPhoneNumberId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public EdsCrmContact getCrmContact() {
        return crmContact;
    }

    public void setCrmContact(EdsCrmContact crmContact) {
        this.crmContact = crmContact;
    }

    public EdsCrmAccount getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(EdsCrmAccount crmAccount) {
        this.crmAccount = crmAccount;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    @Override
    public Integer getObjectID() {
        return null;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }


    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}
