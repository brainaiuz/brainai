package com.finnetlimited.reportservice.core.client.ui.table;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.FolderType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.ArrayList;

/**
 * User: ${Dilsh0d}
 * Date: 11-Mar-2010
 * Time: 21:29:12
 * <p/>
 * <br/> Folder Table by category
 */
public class FolderTable extends HTMLPanel {

    // Click button name
    private static final String RUN = "runn";
    private static final String MODIFY = "modifyy";
    private static final String DELETE = "deletee";
    private static final String EXPORT = "exportt";
    private static final String CLONE = "clonee";
    private static final String LINE = "-";

    private static int number = 0;
    private static final String id = IdType.FOLDER_TABLE.getName();

    private StringBuilder tableText;
    private FolderType style;
    private String org_id;
    private int odd = 0;


    public FolderTable(String folderName, FolderType style) {
        super("<h2 class='" + style.getType() + "'>" +
                "<span>" + folderName + " (" + style.name() + ")</span>" +
                "</h2>" +
                "<div id='" + (id + number) + "'>" +

                "</div>");
        this.style = style;
        this.org_id = id + number;
        number++;
        sinkEvents(Event.ONCLICK);
    }

    private void addRow(Integer id, SelectListRpc args, String folderType, String companyId) {
        if (odd % 2 != 0) {
            tableText.append("<tr><td class='first'><ul class='row-box'>");
        } else {
            tableText.append("<tr class='stronger'><td class='first'><ul class='row-box'>");
        }
        odd++;

        tableText.append("<li><a href='javascript:;' id='" + (org_id + LINE + RUN + LINE + args.getType() + LINE + id + LINE + folderType) + "' >Run</a></li>");

        if ((!FolderType.System.equals(style) || "1".equals(companyId)) && !Utils.hasRole(Constants.CLIENT)) {
            tableText.append("<li><a href='javascript:;' id='" + (org_id + LINE + MODIFY + LINE + args.getType() + LINE + id) + "'>Modify</a></li>");
        }

        if ((!FolderType.System.equals(style) || "1".equals(companyId)) && !Utils.hasRole(Constants.CLIENT)) {
            tableText.append("<li><a href='javascript:;' id='" + (org_id + LINE + DELETE + LINE + args.getType() + LINE + id) + "'>Delete</a></li>");
        }

        /* tableText.append("<li><a href='javascript:;' id='" + (org_id + LINE + EXPORT + LINE + id) + "'>Export</a></li>");*/
        if ((!FolderType.System.equals(style) || "1".equals(companyId)) && !Utils.hasRole(Constants.CLIENT)) {
            tableText.append("<li><a href='javascript:;' id='" + (org_id + LINE + CLONE + LINE + args.getType() + LINE + id) + "'>Clone</a></li></ul></td>");
        }
        tableText.append("<td>" + args.getName() + "</td>");
        tableText.append("<td class='last'>" + args.getDescription() + "</td></tr>");

    }
    // String[] array one row

    public void drawTable(ArrayList<SelectListRpc> rows, String folderType, String companyId) {
        tableText = new StringBuilder("<table class='zebra' style='border-collapse:separate'>" +
                "<tr> " +
                "<th class='act first'>Action</th>" +
                "<th class='act first'>Report Name</th>" +
                "<th class='act first'>Description</th>" +
                "</tr>");

        for (SelectListRpc row : rows) {
            addRow(row.getId(), row, folderType, companyId);
        }

        tableText.append("</table>");
        add(new HTML(tableText.toString()), org_id);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        com.google.gwt.dom.client.Element e = com.google.gwt.dom.client.Element.as(event.getEventTarget());
        String attValue = e.getAttribute("id");
        if (attValue != null) {
            String[] arrayValue = attValue.split(LINE);
            if (arrayValue != null) {
                if (RUN.equals(arrayValue[1])) {
                    runReport(arrayValue[2], arrayValue[3], arrayValue[4]);
                } else if (MODIFY.equals(arrayValue[1])) {
                    modifyReport(arrayValue[2], arrayValue[3]);
                } else if (DELETE.equals(arrayValue[1])) {
                    deleteReport(arrayValue[3]);
//                } else if (EXPORT.equals(arrayValue[1])) {
//                    exportReport(arrayValue[3]);
                } else if (CLONE.equals(arrayValue[1])) {
                    cloneReport(arrayValue[3]);
                }
            }
        }
    }

    private void cloneReport(String s) {
        tableAction.clone(Integer.valueOf(s));
    }

//    private void exportReport(String s) {
//        tableAction.export(Integer.valueOf(s));
//    }

    private void deleteReport(String s) {
        tableAction.delete(Integer.valueOf(s));
    }

    private void modifyReport(String type, String id) {
        tableAction.modify(type, Integer.valueOf(id));
    }

    private void runReport(String type, String id, String folderType) {
        tableAction.run(type, Integer.valueOf(id), folderType);
    }

    public interface FolderTableAction {
        void delete(Integer id);

        void modify(String type, Integer id);

        //void export(Integer id);

        void clone(Integer id);

        void run(String type, Integer id, String folderType);
    }

    private FolderTableAction tableAction;

    public void addTableAction(FolderTableAction tableAction) {
        this.tableAction = tableAction;
    }
}
