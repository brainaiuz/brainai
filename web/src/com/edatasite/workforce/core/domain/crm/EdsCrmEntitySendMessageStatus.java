/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/17 8:33:26                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 30-Jan-2010
 * Time: 17:45:10
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "leadsendmessagestatus",
        indexes = {
                @Index(columnList = "id", name = "leadsendmessagestatus_id_idx")
        })
public class EdsCrmEntitySendMessageStatus extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id")
    private EdsCrmContact entity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageid")
    private EdsMailMessage mailmessage;

    @Enumerated(EnumType.STRING)
    private MessageStatusEnum status;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsCrmContact getEntity() {
        return entity;
    }

    public void setEntity(EdsCrmContact entity) {
        this.entity = entity;
    }

    public EdsMailMessage getMailmessage() {
        return mailmessage;
    }

    public void setMailmessage(EdsMailMessage mailmessage) {
        this.mailmessage = mailmessage;
    }

    public MessageStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MessageStatusEnum status) {
        this.status = status;
    }
}
