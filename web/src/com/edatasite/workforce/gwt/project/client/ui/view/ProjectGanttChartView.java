package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.security.ClientSecurityContext;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ProjectGanttChartView extends View /*implements Colapse*/ {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private Integer projectID = 0;

    public ProjectGanttChartView(Integer id) {
        super("ganttchart", wfmStrings.ganttChart());
        this.projectID = id;
    }

    public String getIconStyle() {
        return "bgMark project-gant-chart";
    }

    /*public AbstractImagePrototype getIconImage() {
        return ProjectViewImageBundles.App.get().projectGanttChart();
    }*/

    protected Widget onInitialize() {
        boolean isClient = Utils.hasRole(Utils.CLIENT);
        int screenWidth = 0;
        int screenHeight = 0;
        if(Utils.isIE()) {
            screenWidth = getIEScreenWidth();
            screenHeight = getIEScreenHeight();
        } else {
            screenWidth = getScreenWidth();
            screenHeight = getScreenHeight();
        }
        screenWidth -= 198;  // for adjusting left panel
        String sessionId = ClientSecurityContext.get().getSessionId();
//        Integer userId = Utils.getUserID();
        String shortDateFormat = Utils.getShortDateFormat();
        String htmlText = "<embed src=\"ganttchart/ganttChart.swf?projectId=" + projectID + "&contextPath=" + GWT.getHostPageBaseURL() +
                "&sessionId="+sessionId+"&userId="+"&dateFormat="+shortDateFormat+"&chartWidth="+screenWidth+"&chartHeight="+screenHeight+"&isClient="+isClient+"\" quality=\"high\" bgcolor=\"#FFFFFF\" width=\""+(screenWidth-15)+
                "\" height=\""+(screenHeight-118)+"\" name=\"ganttChart\" align=\"middle\" wmode=\"opaque\" allowScriptAccess=\"sameDomain\" " +
                "allowFullScreen=\"true\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />\n" +
                "</object>";

        add(new HTML(htmlText));
        return null;
    }

    public native int getIEScreenHeight() /*-{
        return $doc.body.offsetHeight;
    }-*/;

    public native int getIEScreenWidth() /*-{
        return $doc.body.offsetWidth;
    }-*/;

    public native int getScreenHeight() /*-{

        return $wnd.innerHeight;
    }-*/;

    public native int getScreenWidth() /*-{
        return $wnd.innerWidth;
    }-*/;

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

}
