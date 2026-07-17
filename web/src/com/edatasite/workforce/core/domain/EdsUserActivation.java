package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sherzodmuratov
 * Date: 24.02.2009
 * Time: 19:29:58
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "useractivation")
public class EdsUserActivation extends EdsObject {

    public static final String _SENT_TYPE = "_ALERT_TYPE";
    public static final String ACTIVATION = "ACTIVATION";

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @Column(name = "sentCount")
    private Integer sentCount = 0;

    @Column(name = "lastSentDate")
    private Date lastSentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sentTypeid")
    private EdsReference type;

    public Integer getObjectID() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public Integer getSentCount() {
        return sentCount;
    }

    public void setSentCount(Integer sentCount) {
        this.sentCount = sentCount;
    }

    public Date getLastSentDate() {
        return lastSentDate;
    }

    public void setLastSentDate(Date lastSentDate) {
        this.lastSentDate = lastSentDate;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }
}
