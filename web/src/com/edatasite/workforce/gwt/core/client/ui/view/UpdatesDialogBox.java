package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.google.gwt.user.client.ui.*;

import java.util.List;

/**
 * Created by Shohruh on 10-Oct-15.
 */
public class UpdatesDialogBox extends KpiModal {
    // Event color name
    private static final String ADD_COLOR = "#13649b";
    private static final String EDIT_COLOR = "black";
    private static final String DELETE_COLOR = " #931212";

    private static final String APPROVED_COLOR = "#0e681b";
    private static final String WATING_COLOR = "#994a03";
    private static final String REJECTED_COLOR = "#931212";

    private static final String COMPLETED_COLOR = "#0e681b";
    private static final String CANCELLED_COLOR = "#931212";
    private static final String CLOSED_COLOR = "#e27000";
    private static final String STATUS_CHANGE_COLOR = "#218721";

    // Event icon
    private static final String ADD_ICON = "myupdate-add-icon";
    private static final String EDIT_ICON = "myupdate-edit-icon";
    private static final String DELETE_ICON = "myupdate-delete-icon";

    private static final String WAITING_ICON = "myupdate-waiting-icon";
    private static final String APPROVED_ICON = "myupdate-approved-icon";
    private static final String REJECTED_ICON = "myupdate-rejected-icon";

    private static final String COMPLETED_ICON = "myupdate-completed-icon";
    private static final String CANCELLED_ICON = "myupdate-canceled-icon";
    private static final String CLOSED_ICON = "myupdate-closed-icon";
    private static final String STATUS_CHANGE_ICON = "myupdate-status-change-icon";

    /*Event status*/
    public static final String ADD = "ADD";
    public static final String EDIT = "EDIT";
    public static final String DELETE = "DELETE";
    public static final String ALERT = "ALERT";
    public static final String NOTE = "NOTE";
    public static final String MESSAGE = "MESSAGE";
    public static final String STATUS_CHANGE = "STATUS_CHANGE";

    List<MyUpdateItem> myUpdateItems;
    String title;

    public UpdatesDialogBox(String title, List<MyUpdateItem> myUpdateItems) {
        super();
        setCloseButton(true);
        this.title = title;
        this.myUpdateItems = myUpdateItems;
        setWidth("350px");
        setScrollable(true);
        FlexTable table = new FlexTable();
        table.setWidth("100%");
        MyUpdateItem myUpdateItem;
        String color = "", icon = "";
        for (int i = 0; i < myUpdateItems.size(); i++) {
            myUpdateItem = myUpdateItems.get(i);
            switch (myUpdateItem.getType()) {
                case ADD:
                    color = ADD_COLOR;
                    icon = ADD_ICON;
                    break;
                case EDIT:
                    color = EDIT_COLOR;
                    icon = EDIT_ICON;
                    break;
                case DELETE:
                    color = DELETE_COLOR;
                    icon = DELETE_ICON;
                    break;
                case STATUS_CHANGE:
                    if (myUpdateItem.getSubType() != null && myUpdateItem.getSubType().endsWith("SUBMIT")) {
                        color = WATING_COLOR;
                        icon = WAITING_ICON;
                    } else if (myUpdateItem.getSubType() != null && myUpdateItem.getSubType().endsWith("APPROVE")) {
                        color = APPROVED_COLOR;
                        icon = APPROVED_ICON;
                    } else if (myUpdateItem.getSubType() != null && myUpdateItem.getSubType().endsWith("REJECT")) {
                        color = REJECTED_COLOR;
                        icon = REJECTED_ICON;
                    } else {
                        color = STATUS_CHANGE_COLOR;
                        icon = STATUS_CHANGE_ICON;
                    }
                    break;
            }
            Grid linkMessage = new Grid(2, 1);
            linkMessage.setWidget(0, 0, new HTML("<span style='color:" + color + ";font-size:13px;width:100%;'>" +
                    myUpdateItem.getMessage() + "</span>"));
            linkMessage.setWidget(1, 0, new HTML("<span style='color:gray;height:16px;'>" + DateUtils.formatInternal(myUpdateItem.getEventDate()) + "</span>"));
            Grid grid = new Grid(1, 1);
            grid.setHTML(0, 0, "<span style='height:16px;width:16px;display:block;'>&nbsp;</span>");
            grid.getCellFormatter().setStyleName(0, 0, icon);
            FlexTable tabe = new FlexTable();
            tabe.setWidget(0, 0, grid);
            tabe.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
            tabe.getFlexCellFormatter().setStyleName(0, 0, "paddingTop3");
            tabe.setStyleName("dotted-line");
            tabe.setWidget(0, 1, linkMessage);
            tabe.getFlexCellFormatter().setWidth(0, 0, "3%");
            tabe.getFlexCellFormatter().setWidth(0, 1, "97%");
            table.getFlexCellFormatter().setVerticalAlignment(i, 0, VerticalPanel.ALIGN_TOP);
            table.setWidget(i, 0, tabe);
        }
        add(table);
        setTitle(title);
    }
}
