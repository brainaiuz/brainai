package com.edatasite.workforce.core.domain.emailfetching.mongo;

import com.edatasite.workforce.core.domain.settings.EdsEmailFilter;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.profile.client.rpc.EmailFilter;
import jakarta.mail.MessageRemovedException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Document(collection = "emails")
@CompoundIndexes({
        @CompoundIndex(name = "messageId_companyId_emailSettingId_folderId_trackerId_messageUID", def = "{ 'messageId': 1, 'companyId' : 1, 'emailSettingId': 1, 'folderId': 1, 'trackerId': 1, 'messageUID': 1 }"),
})
public class EdsEmail {
    public static final String FLAG_DELETED = "FLAG_DELETED";
    public static final String FLAG_READ = "FLAG_READ";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String companyId;

    private Long messageUID;
    private String messageId;

    private String subject;
    private String description;

    private String from;
    private String to;
    private String replyTo;

    private String toBCC;
    private String toCC;

    private Date createdDate;
    private Date fetchedDate;

    private boolean deleted = false;
    private boolean fetched = false;
    private boolean read = false;

    private boolean hasAttachment = false;

    private Integer emailSettingId;
    private Integer emailTemplateId;
    private Integer emailContentId;
    private Integer trackerId;
    private String trackerType;

    private Integer folderId;
    private String folderType;

//    private long googleID = -1L;
    private long generatedGoogleID = 0L;

    private int attempts = 0;//max = 2
    private String clusterType;

    public Boolean isNew() {
        return getId() == null;
    }

    public Email getRPC() {
        Email email = new Email();
        email.setObjectID(getId());
        email.setMessageUID(getMessageUID());
        email.setMessageId(getMessageId());
        email.setSubject(getSubject());
        email.setContent(getDescription());
        email.setFromEmail(getFrom());
        email.setToEmail(getTo());
        email.setToEmails(getTo());
        email.setReplyTo(getReplyTo());
        email.setBcc(getToBCC());
        email.setCc(getToCC());
        email.setDate(getCreatedDate());
        email.setReceivedDate(getFetchedDate());
        email.setDeleted(isDeleted());
        email.setSeen(isRead());
        email.setAttachment(isHasAttachment());
        email.setSettingID(getEmailSettingId());
        email.setTemplateId(getEmailTemplateId());
        email.setTrackerID(getTrackerId());
        email.setFolderID(getFolderId());
        email.setGeneratedGoogleID(String.valueOf(getGeneratedGoogleID()));
        email.setClusterType(getClusterType());
        return email;
    }

    public Email getListRPC() {
        Email email = new Email();
        email.setObjectID(getId());
        email.setSubject(getSubject());
        email.setFromEmail(getFrom());
        email.setToEmail(getTo());
        email.setToEmails(getTo());
        email.setSeen(isRead());
        email.setDate(getCreatedDate());
        email.setReceivedDate(getFetchedDate());
        return email;
    }

    @Transient
    public static final ConcurrentHashMap<Integer, Map<String, Object>> messageContents = new ConcurrentHashMap<>();
    @Transient
    Map<String, Map<EdsEmailFilter, EdsEmailFilter>> filtersTransient = new HashMap<>();
    @Transient
    private boolean ignoreFilters = false;

    public boolean hasCaseFilter() {
        return ignoreFilters || filtersTransient.containsKey(EmailFilter.CREATE_CASE);
    }

    public void filter(List<EdsEmailFilter> filters, MimeMessage message) {
        if (filters != null && filters.size() > 0) {
            try {
                parents:
                for (EdsEmailFilter parentFilter : filters) {
                    if (parentFilter.getSubFilters() != null && parentFilter.getSubFilters().size() > 0) {
                        if (parentFilter.asSearchTerm() == null || "".equals(parentFilter.asSearchTerm()) || message.match(parentFilter.asSearchTerm())) {
                            childs:
                            for (EdsEmailFilter childFilter : parentFilter.getSubFilters()) {
                                if (!filtersTransient.containsKey(childFilter.getType())) {
                                    filtersTransient.put(childFilter.getType(), new HashMap<>());
                                    EdsEmailFilter rule = null;
                                    if (childFilter.getSubFilters() != null && childFilter.getSubFilters().size() > 0) {
                                        for (EdsEmailFilter rule_ : childFilter.getSubFilters()) {
                                            if (rule_ != null && rule_.asSearchTerm() != null && message.match(rule_.asSearchTerm())) {
                                                rule = rule_;
                                                break;
                                            }
                                        }
                                    }
                                    filtersTransient.get(childFilter.getType()).put(childFilter, rule);
                                }
                            }
                        }
                    }
                }
            } catch (MessageRemovedException e) {
                System.err.println("Message had been Removed : Message Number is " + message.getMessageNumber());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    public EdsEmailFilter getFilter(String type) {
        if (filtersTransient.get(type) != null && filtersTransient.get(type).size() > 0) {
            for (Map.Entry<EdsEmailFilter, EdsEmailFilter> entry : filtersTransient.get(type).entrySet()) {
                if (entry.getValue() != null) {
                    return entry.getValue();
                } else if (entry.getKey() != null) {
                    return entry.getKey();
                }
            }
        }
        return new EdsEmailFilter();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Long getMessageUID() {
        return messageUID;
    }

    public void setMessageUID(Long messageUID) {
        this.messageUID = messageUID;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getToBCC() {
        return toBCC;
    }

    public void setToBCC(String toBCC) {
        this.toBCC = toBCC;
    }

    public String getToCC() {
        return toCC;
    }

    public void setToCC(String toCC) {
        this.toCC = toCC;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getFetchedDate() {
        return fetchedDate;
    }

    public void setFetchedDate(Date fetchedDate) {
        this.fetchedDate = fetchedDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isFetched() {
        return fetched;
    }

    public void setFetched(boolean fetched) {
        this.fetched = fetched;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public Integer getEmailSettingId() {
        return emailSettingId;
    }

    public void setEmailSettingId(Integer emailSettingId) {
        this.emailSettingId = emailSettingId;
    }

    public Integer getEmailTemplateId() {
        return emailTemplateId;
    }

    public void setEmailTemplateId(Integer emailTemplateId) {
        this.emailTemplateId = emailTemplateId;
    }

    public Integer getEmailContentId() {
        return emailContentId;
    }

    public void setEmailContentId(Integer emailContentId) {
        this.emailContentId = emailContentId;
    }

    public Integer getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(Integer trackerId) {
        this.trackerId = trackerId;
    }

    public String getTrackerType() {
        return trackerType;
    }

    public void setTrackerType(String trackerType) {
        this.trackerType = trackerType;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getFolderType() {
        return folderType;
    }

    public void setFolderType(String folderType) {
        this.folderType = folderType;
    }

    public long getGeneratedGoogleID() {
        return generatedGoogleID;
    }

    public void setGeneratedGoogleID(long generatedGoogleID) {
        this.generatedGoogleID = generatedGoogleID;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public Map<String, Map<EdsEmailFilter, EdsEmailFilter>> getFiltersTransient() {
        return filtersTransient;
    }

    public void setFiltersTransient(Map<String, Map<EdsEmailFilter, EdsEmailFilter>> filtersTransient) {
        this.filtersTransient = filtersTransient;
    }

    public boolean isIgnoreFilters() {
        return ignoreFilters;
    }

    public void setIgnoreFilters(boolean ignoreFilters) {
        this.ignoreFilters = ignoreFilters;
    }

    public String getClusterType() {
        return clusterType;
    }

    public void setClusterType(String clusterType) {
        this.clusterType = clusterType;
    }
}
