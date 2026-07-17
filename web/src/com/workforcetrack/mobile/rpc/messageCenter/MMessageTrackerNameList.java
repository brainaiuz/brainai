package com.workforcetrack.mobile.rpc.messageCenter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 9/8/11
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MMessageTrackerNameList {

    ArrayList<MMessageWithTrackerName> messageTrackerName;

    public MMessageTrackerNameList() {
    }

    public ArrayList<MMessageWithTrackerName> getMessageTrackerName() {
        if (messageTrackerName == null) {
            messageTrackerName = new ArrayList<>();
        }
        return messageTrackerName;
    }

    public void setMessageTrackerName(ArrayList<MMessageWithTrackerName> messageTrackerName) {
        this.messageTrackerName = messageTrackerName;
    }
}
