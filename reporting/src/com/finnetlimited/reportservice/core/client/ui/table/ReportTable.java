package com.finnetlimited.reportservice.core.client.ui.table;

import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.module.ReportingModuleSettings;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

import java.util.HashMap;

/**
 * User: ${Dilsh0d}
 * Date: 20-Mar-2010
 * Time: 16:31:19
 */
public class ReportTable extends ComplexPanel {

    private static final String _id = IdType.REPORT_TABLE.getName();
    private static final String LINE = "-";
    private static int num = 0;

    private String id;
    private StringBuilder htmlText;
    private ReportingModuleSettings settings;

    private int nowPosition;
    private int lastPosition;
    private int allCount;
    private int step;
    private Command command;
    private String sortByColumn;
    private Integer index;

    public ReportTable(ReportingModuleSettings settings) {
		setElement(DOM.createDiv());
//		super("");
        DOM.setElementAttribute(getElement(), "id", (_id + num));
        DOM.setElementAttribute(getElement(), "align", "center");
        //  DOM.setElementAttribute(getElement(), "style", "margin:0 20px");
        setStyleName("clear");
        id = _id + num;
        num++;
        this.settings = settings;
        init();
    }

    private void init() {
    }

    public void drawReportTable(ReportRpc reportRpc) {
        step = reportRpc.getLimit();
        nowPosition = reportRpc.getNowPosition();
        lastPosition = reportRpc.getNowLastPosition();
        allCount = reportRpc.getAllCount();
        loadReportTable(getSerializedRpc(reportRpc));
    }

    private String getSerializedRpc(ReportRpc reportRpc) {
        SerializationStreamFactory factory = GWT.create(CoreService.class);
        SerializationStreamWriter writer = factory.createStreamWriter();
        try {
            writer.writeObject(reportRpc);
            return URL.encode(writer.toString().replace("+", "%2B").replace("&", "%26").replace("?", "%3F"));
        } catch (SerializationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "";
        }
    }

    private void loadReportTable(String data) {
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, "/reportingrenderer");
        requestBuilder.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {
            DRSLoadingPanel.show();
            requestBuilder.sendRequest(URL.encode("reportrpc") + "=" + data, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {

                        if (response.getStatusCode() == 200) {
//                            clear();
//                            add(new HTML("<div class='boder'>" + response.getText() + "</div>"));
							getElement().setInnerHTML("<div class='boder'>" + response.getText() + "</div>");
							RangePanel rangePanelBottom = new RangePanel();
                            rangePanelBottom.setStyleName("rangeBottom");
                            if (step != -1) {
                                if (settings != null && settings.getActivePagers() != null && settings.getActivePagers().contains("bottom")) {
									add(rangePanelBottom, getElement());
								}
                            } else {
								add(new HTML("<br/>"), getElement());
							}
                            registerEventHandler();
                        }
                    } catch (Exception e) {

                    }
					DRSLoadingPanel.hide();
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    DRSLoadingPanel.hide();
                }
            });
        } catch (RequestException e) {
            DRSLoadingPanel.hide();
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    /*public void drawTabularReport(ReportGenerateTableRpc tableData, int stage) {
        this.stage = stage;
        this.nowPosition = tableData.getNowPosition();
        this.lastPosition = tableData.getLastPosition();
        this.allCount = tableData.getRowCount();
        RangePanel rangePanelTop = new RangePanel();
        drawReportColumnTitle(tableData.getTitleRows(), tableData.getReport());
        index = -1;
        if (stage != -1) {
            for (ReportTreeItem item : tableData.getTreeItems()) {
                if (nowPosition + index == lastPosition) {
                    break;
                }
                index++;
                drawTableRowsByReportTree(item, tableData.getReport());
            }
        } else {
            for (ReportTreeItem item : tableData.getTreeItems()) {
                index++;
                drawTableRowsByReportTree(item, tableData.getReport());
            }
        }
        if (!settings.getCustomise()) {
            drawFooterRow(tableData.getTreeItems().get(tableData.getTreeItems().size() - 1), tableData.getReport());
        }

        RangePanel rangePanelBottom = new RangePanel();
        rangePanelBottom.setStyleName("rangeBottom");
        if (tableData.getLimit() != -1) {
            if (settings != null && settings.getActivePagers() != null && settings.getActivePagers().contains("top")) {
                add(rangePanelTop);
            }
        } else {
            add(new HTML("<br/>"));
        }

		getElement().setInnerHTML("<div class='border' >" + htmlText.toString() + "</div>");
//        add(new HTML("<div class='border' >" + htmlText.toString() + "</div>"));

        if (tableData.getLimit() != -1) {
            if (settings != null && settings.getActivePagers() != null && settings.getActivePagers().contains("bottom")) {
                add(rangePanelBottom);
            }
        } else {
            add(new HTML("<br/>"));
        }

        registerEventHandler();
    }*/

  /*  public void drawSummariesReport(ReportGenerateTableRpc tableData) {
        this.nowPosition = tableData.getNowPosition();
        drawReportColumnTitle(tableData.getTitleRows(), tableData.getReport());
        index = -1;
        for (int i = 0; i < tableData.getTreeItems().size() - 1; i++) {
            index++;
            drawTableRowsByReportTree(tableData.getTreeItems().get(i), tableData.getReport());
        }

        if (!settings.getCustomise()) {
            drawFooterRow(tableData.getTreeItems().get(tableData.getTreeItems().size() - 1), tableData.getReport());
        }
        htmlText.append("</table>");
        // add(new HTML("<br/><br/>"), id);
//        add(new HTML("<div class='border' style='margin:0 -20px;'>" + htmlText.toString() + "</div>"));
		getElement().setInnerHTML("<div class='border' style='margin:0 -20px;'>" + htmlText.toString() + "</div>");
    }*/

    /*private void drawTableRowsByReportTree(ReportTreeItem treeItem, ReportRpc report) {

        String rowClass = (1 == index % 2) ? "even" : "odd";
        htmlText.append("<tr class='" + rowClass + "'>");

// if (!settings.getCustomise()) {
// htmlText.append("<td>" + (index + nowPosition) + "</td>");
// }

        Integer depth = treeItem.getDepth();
        Boolean firstCell = false;
        for (int i = depth - 1; i < treeItem.getCells().size(); i++) {
            String column = treeItem.getCells().get(i);
            if (depth > 1 && !firstCell) {
                htmlText.append("<td colspan='" + (depth - 1) + "'></td>");
                firstCell = true;
            }
            if (treeItem.getSummaryRow()) {
                String cellText = "";
                if (column.contains("<-->")) {
                    String[] tokens = column.split("<-->");
                    cellText = "<b>" + tokens[0] + "</b></br><b>" + tokens[1] + "</b>";
                } else {
                    cellText = "<b>" + column + "</b>";
                }

                if (report.isShowRowCount()) {
                    cellText += "<br>rows:" + treeItem.getCountOfAllChilds();
                }
                htmlText.append("<td class='mark'>" + cellText + "</td>");
            } else {
                if (IsNumiric(column)) {
                    htmlText.append("<td> " + column + "</td>");
                } else {
                    htmlText.append("<td> " + column + "</td>");
                }
            }
        }
        htmlText.append("</tr>");
        if (treeItem.getChilds() != null && treeItem.getChilds().size() > 0) {
            for (int i = 0; i < treeItem.getChilds().size(); i++) {
                index++;
                drawTableRowsByReportTree(treeItem.getChilds().get(i), report);
            }
        }
    }*/

   /* private void drawFooterRow(ReportTreeItem lastItem, ReportRpc report) {
        if (lastItem != null) {
            htmlText.append("<tr>");
            for (int i = 0; i < report.getSelectedColumns().size(); i++) {
                String cell = lastItem.getCells().get(i);
                if (i == 0) {
                    htmlText.append("<td class='thead'>");
                    htmlText.append("<b>Grand total: " + lastItem.getCountOfAllChilds() + "</b>");
                    if (cell != null && !cell.equals("")) {
                        htmlText.append("</br><b>" + cell + "</b>");
                    }
                    htmlText.append("</td>");
                } else {
                    htmlText.append("<td class='thead'><b>" + cell + "</b></td>");
                }
            }
            htmlText.append("</tr>");
        }
    }*/

    private void registerEventHandler() {
        DOM.addEventPreview(event -> {
            Element element = DOM.eventGetTarget(event);
            if (DOM.eventGetType(event) == Event.ONCLICK && element.getId().endsWith("sort")) {
                sortByColumn = element.getId().split(LINE)[0];
                if (command != null) {
                    command.execute();
                }
            }
            return true; //To change body of implemented methods use File | Settings | File Templates.
        });
    }

   /* private void drawReportColumnTitle(LinkedList<ColumnRpc> titleColumns, ReportRpc report) {
        htmlText = new StringBuilder();
        if (report.enableAddNewAction() && report.getAddNewAction() != null) {
            htmlText.append("<div class=\"add-new-action\">");
            htmlText.append(generateReportActionLink(null, report.getAddNewAction(), null));
            htmlText.append("</div>");
        }
        htmlText.append("<table border='1' class='table-List'>");
        htmlText.append("<tr>");

// if (!settings.getCustomise()) {
// htmlText.append("<td class='thead'>#</td>");
// }

        for (ColumnRpc column : titleColumns) {
            htmlText.append("<td class='thead'>");
            htmlText.append("<a href='javascript:void();' id='" + (id + LINE + column.getName()) + "-sort'>" + column.getTitle() + "</a>");
//            htmlText.append("" + column.getTitle());
            htmlText.append("</td>");
        }
        if (report.enableViewAction() || report.enableEditAction() || report.enableDeleteAction()) {
            report.getShowActions();
            if (!settings.getCustomise()) {
                htmlText.append("<td class='thead'>Actions</td>");
            } else {
                htmlText.append("<td class='thead'>Tools</td>");
            }
        }
        htmlText.append("</tr>");
    }*/

    private class RangePanel extends HorizontalPanel {

        private ActionButton next;
        private ActionButton prev;
        private ActionButton first;
        private ActionButton last;
        private HTMLPanel range;
        private HTMLPanel displaiItems;


        public RangePanel() {
            setStyleName("range");
            init();
            add(first);
            add(prev);
            add(range);
            add(next);
            add(last);
            add(displaiItems);
        }

        private void init() {
            first = new ActionButton("&nbsp;", "minimize key-toStart", ActionButton.Type.BUTTON);
            last = new ActionButton("&nbsp;", "minimize key-toEnd", ActionButton.Type.BUTTON);
            next = new ActionButton("&nbsp;", "minimize key-Next", ActionButton.Type.BUTTON);
            prev = new ActionButton("&nbsp;", "minimize key-Prev", ActionButton.Type.BUTTON);

            first.addClickHandler(clickEvent -> {
                if (nowPosition > 1) {
                    pagingEvent.clickPagingEvent(1, step);
                }
            });

            last.addClickHandler(clickEvent -> {
                if (nowPosition / step != allCount / step) {
                    if (allCount % step == 0) {
                        pagingEvent.clickPagingEvent(allCount - step + 1, step);
                    } else {
                        pagingEvent.clickPagingEvent(allCount - allCount % step + 1, step);
                    }
                }
            });

            next.addClickHandler(clickEvent -> {
                if ((allCount / step + 1) != (nowPosition / step + 1)) {
                    pagingEvent.clickPagingEvent(nowPosition + step, step);
                }
            });

            prev.addClickHandler(clickEvent -> {
                if (1 != nowPosition) {
                    pagingEvent.clickPagingEvent(nowPosition - step, step);
                }
            });

            range = new HTMLPanel("&nbsp;&nbsp;" + (nowPosition / step + 1) + "&nbsp;of&nbsp;" + (allCount / step + 1) + "&nbsp;&nbsp;");
            displaiItems = new HTMLPanel("Displaying&nbsp;items:<br/>" + nowPosition + "&nbsp;-&nbsp" + (((nowPosition + step) > allCount) ? allCount : (nowPosition + step)) + "&nbsp;&nbsp;of&nbsp;" + allCount + "&nbsp;&nbsp;");
            range.setStyleName("optBtn currLoc");
            displaiItems.setStyleName("displayItems currLoc");
        }
    }

    public interface TablePagingEvent {
        void clickPagingEvent(int beganPositon, int step);
    }

    private TablePagingEvent pagingEvent;

    public TablePagingEvent getPagingEvent() {
        return pagingEvent;
    }

    public void addPagingEvent(TablePagingEvent pagingEvent) {
        this.pagingEvent = pagingEvent;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getSortByColumn() {
        return sortByColumn;
    }

    private String getReportActionColumnValue(HashMap<String, String> map, String url) {
        String temp = url;

        if (map != null) {
            while (temp.contains("%")) {
                temp = temp.substring(temp.indexOf("%") + 1);
                String column = temp.substring(0, temp.indexOf("%"));
                url = url.replace("%" + column + "%", map.get(column));
                temp = temp.substring(temp.indexOf("%") + 1);
            }
        }

        return url;
    }

    /*private String generateReportActionLink(HashMap<String, String> actionColumnsData, ReportAction action, String content) {
        StringBuilder link = new StringBuilder();
        link.append("<a href=\"" + getReportActionColumnValue(actionColumnsData, action.getUrl()) + "\" style=\"margin-left:2px;\" ");

        if (action.getOnClick() != null && !action.getOnClick().isEmpty()) {
            link.append("onClick=\"" + getReportActionColumnValue(actionColumnsData, action.getOnClick()) + "\" ");
        }
        if (action.getTarget() != null && !action.getTarget().isEmpty()) {
            link.append("target=\"" + action.getTarget() + "\" ");
        }

        if (content != null && !content.isEmpty()) {
            link.append(">" + content + "</a>");
        } else {
            link.append(">" + action.getTitle() + "</a>");
        }

        return link.toString();
    }*/

   /* private Boolean IsNumiric(String text) {

        try {
            Integer result = Integer.parseInt(text);
            return true;
        } catch (Exception ex) {

        }
        return false;
    }*/
}
