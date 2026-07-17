package com.workforcetrack.mobile.rpc.task;

import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/12/11
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MPositionList {

    private List<MPositonsSelectItem> positonListItem;


    public MPositionList(){}

    public MPositionList(PositionsSelectItem[] positionLists) {
        if (positionLists != null) {
            this.positonListItem = new ArrayList<>();
            for (PositionsSelectItem positionsSelectItem : positionLists) {
                this.positonListItem.add(new MPositonsSelectItem(positionsSelectItem));
            }
        }
    }


    public List<MPositonsSelectItem> getPositonListItem() {
        return positonListItem;
    }

    public void setPositonListItem(List<MPositonsSelectItem> positonListItem) {
        this.positonListItem = positonListItem;
    }
}
