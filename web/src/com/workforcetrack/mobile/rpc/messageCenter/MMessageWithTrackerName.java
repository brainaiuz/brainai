package com.workforcetrack.mobile.rpc.messageCenter;

import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 08.09.11
 * Time: 19:18
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MMessageWithTrackerName {

    private String messageID;
    private String trackerName;

    private List<MSelectItem> relation;

    public MMessageWithTrackerName() {

    }

    public MMessageWithTrackerName(String messageID, String trackerName) {
        this.messageID = messageID;
        this.trackerName = trackerName;
    }

    public MMessageWithTrackerName(String messageID, String trackerName, List<MSelectItem> relations) {
        this.messageID = messageID;
        this.trackerName = trackerName;
        this.relation = relations;
    }

    public List<MSelectItem> getRelation() {
        return relation;
    }

    public void setRelation(List<MSelectItem> relation) {
        this.relation = relation;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getTrackerName() {
        return trackerName;
    }

    public void setTrackerName(String trackerName) {
        this.trackerName = trackerName;
    }
}
