package com.edatasite.workforce.gwt.core.client.rpc.historyNote;

import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;

public class UpdateTypeStyle {

    public static String getStyleByUpdateSubType(String subType) {
        switch (subType) {
            case MyUpdateItem.ADD:
                return "updates-row updates-cat--added";
            case MyUpdateItem.EDIT:
                return "updates-row updates-cat--edited";
            case MyUpdateItem.DELETE:
                return "updates-row updates-cat--deleted";
            case MyUpdateItem.STATUS_CHANGE:
                return "updates-row updates-cat--edit";
            case MyUpdateItem.STATUS_PAID:
            case MyUpdateItem.SUCCESS:
                return "updates-row updates-cat--paid";
            case MyUpdateItem.STATUS_APPROVED:
            case MyUpdateItem.STATUS_COMPELETED:
                return "updates-row updates-cat--approved";
            case MyUpdateItem.STATUS_WAITING:
                return "updates-row updates-cat--waiting";
            case MyUpdateItem.STATUS_REJECT:
            case MyUpdateItem.FAIL:
                return "updates-row updates-cat--rejected";
            case MyUpdateItem.ASSIGN:
                return "updates-row updates-cat--assigned";
            case MyUpdateItem.IMPORTED:
                return "updates-row updates-cat--imported";
            case MyUpdateItem.STATUS_RECEIVED:
                return "updates-row updates-cat--received";
            case MyUpdateItem.CONVERTED:
                return "updates-row updates-cat--converted";
            case MyUpdateItem.FILE_UPLOAD:
                return "updates-row updates-cat--uploaded";
            case MyUpdateItem.STATUS_REFUNDED:
                return "updates-row updates-cat--refunded";
            case MyUpdateItem.STATUS_SUBMITED:
                return "updates-row updates-cat--submited";
            case MyUpdateItem.STATUS_TERMINATED:
                return "updates-row updates-cat--terminated";
            case MyUpdateItem.STATUS_SENT:
                return "updates-row updates-cat--sent";
            case MyUpdateItem.STATUS_CANCELLED:
            case MyUpdateItem.STATUS_CLOSED:
                return "updates-row updates-cat--cancel";
            default:
                return "updates-row updates-cat--edited";
        }
    }
}
