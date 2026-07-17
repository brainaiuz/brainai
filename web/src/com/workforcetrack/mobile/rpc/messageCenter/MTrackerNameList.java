package com.workforcetrack.mobile.rpc.messageCenter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 12.09.11
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MTrackerNameList {

    private ArrayList<String> trackerName;

    public MTrackerNameList() {

    }

    public ArrayList<String> getTrackerName() {
        if (trackerName == null) {
            trackerName = new ArrayList<>();
        }
        return trackerName;
    }

    public void setTrackerName(ArrayList<String> trackerName) {
        this.trackerName = trackerName;
    }
}
