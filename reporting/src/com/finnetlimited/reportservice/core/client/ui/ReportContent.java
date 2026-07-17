package com.finnetlimited.reportservice.core.client.ui;

import com.finnetlimited.reportservice.core.client.ui.body.AbstractReportBody;
import com.finnetlimited.reportservice.core.client.ui.body.ReportBody;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.HashMap;


/**
 * User: ${Dilsh0d}
 * Date: 10-Mar-2010
 * Time: 17:29:43
 */
public class ReportContent extends Composite {
    interface ContentBinder extends UiBinder<HTMLPanel, ReportContent> {
    }

    public static final ContentBinder header = GWT.create(ContentBinder.class);

    @UiField
    ReportBody reportBody;

    private Integer id;

    private String param;

    private String type;

    private String dataType;

    private String folderType;

    private String folderName;

    private HashMap<String, AbstractReportBody> mapBody;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setMapBody(HashMap<String, AbstractReportBody> mapBody) {
        this.mapBody = mapBody;
    }

    public ReportContent() {
        initWidget(header.createAndBindUi(this));
    }


    public void setContentAndReplace(AbstractReportBody content) {
        if (content == null) {
            reportBody.setWidgetAnReplace(new HTML("&nbsp;"));
        } else {
            History.newItem(content.getName());
            content.setReportContent(this);
            //Window.alert(id.toString()+"|"+type);
            if (content.getReportingModuleSettings() == null
                    || (!content.getReportingModuleSettings().getCustomise() && !content.getReportingModuleSettings().getRunFromUrl())) {
                content.setParams(id != null ? id.intValue() : null,
                        type != null ? type + "" : null,
                        param != null ? param + "" : null,
                        folderType != null ? folderType + "" : null);
            }

            //content.setParams(id, type, param, folderType);
            //Window.alert(param+"|"+folderType);

            reportBody.setWidgetAnReplace(content);
            DeferredCommand.addCommand(() -> {
                id = null;
                type = null;
                param = null;
                folderType = null;
                folderName = null;
            });

        }
    }

    private String linkFilter(String name) {
        String[] historyLink = name.split("[/]");
        if (historyLink.length == 3) {
            dataType = historyLink[2];
//            String[] params = historyLink[1].split("[|]");
//            if (params.length == 2) {
//                param = params[0];
//                String[] arr = params[1].split("[&]");
//                if (arr.length == 2) {
//                    id = Integer.valueOf(arr[0]);
//                    type = arr[1];
//                } else {
//                    id = Integer.valueOf(arr[0]);
//                }
//            }
        } else if (historyLink.length == 2) {
            String[] params = historyLink[1].split("[|]");
            if (params.length == 2) {
                param = params[0];
                String[] arr = params[1].split("[&]");
                if (arr.length == 3) {
                    id = Integer.valueOf(arr[0]);
                    type = arr[1];
                    folderType = arr[2];
                } else if (arr.length == 2) {
                    id = Integer.valueOf(arr[0]);
                    type = arr[1];
                } else {
                    id = Integer.valueOf(arr[0]);
                }
            }
        } else {
            historyLink = name.split("[|]");
            if (historyLink.length == 2) {
                String[] arr = historyLink[1].split("[&]");
                if (arr.length == 3) {
                    id = Integer.valueOf(arr[0]);
                    type = arr[1];
                    folderType = arr[2];
                } else if (arr.length == 2) {
                    id = Integer.valueOf(arr[0]);
                    type = arr[1];
                } else {
                    id = Integer.valueOf(arr[0]);
                }
            }
        }
        return historyLink[0];
    }

    public void setContent(AbstractReportBody content) {
        if (content == null) {
            reportBody.setWidget(new HTML("&nbsp;"));
        } else {
            History.newItem(content.getName());
            content.setReportContent(this);
            reportBody.setWidget(content);
        }
    }

    public void goToContent(String historyName) {
        setContentAndReplace(mapBody.get(historyName));
    }

    public void refreshContent(String historyType) {
        mapBody.get(historyType).refresh();
        goToContent(historyType);
    }

    public void goToClearAndCreateContent(String historyName) {
        AbstractReportBody body = mapBody.get(linkFilter(historyName));
        body.refreshContent();
        setContentAndReplace(body);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
