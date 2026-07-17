package com.edatasite.workforce.gwt.assessment.client.ui;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Class intended for rendering comments given by commenter it will show comment in disclosure panel if comment very long
 */
public class SkillComment extends Composite {
    private final String commenter;
    private final String comment;
    private final String commenterTeam;
    private DisclosurePanel panel;
    private Hyperlink more;
    private VerticalPanel textPanel;
    private boolean visibleFullComment = false;
    private int width = 500;
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    /**
     * @param commenter     commenter name
     * @param commenterTeam comenter department
     * @param comment       given comment
     */
    public SkillComment(String commenter, String commenterTeam, String comment) {
        this.commenter = commenter;
        this.comment = comment;
        this.commenterTeam = commenterTeam;
        init();
    }

    /**
     * @param commenter     commenter name
     * @param commenterTeam commenter department
     * @param comment       givent comment
     * @param width         width of widget
     */
    public SkillComment(String commenter, String commenterTeam, String comment, int width) {
        this.commenter = commenter;
        this.comment = comment;
        this.commenterTeam = commenterTeam;
        this.width = width;
        init();
    }

    private void init() {
        textPanel = new VerticalPanel();
        more = new Hyperlink(wfmStrings.more(), false, "");
        more.addClickHandler(sender -> {
            panel.setOpen(!visibleFullComment);
            visibleFullComment = !visibleFullComment;
        });

        panel = new DisclosurePanel();
        panel.setOpen(visibleFullComment);
        HTML fullHTML = new HTML();

        String name = "";
        int used = 0;
        if (commenter != null) {
            name = "<b>" + commenter + "</b>";
            used += commenter.length();
        }
        if (commenterTeam != null) {
            name += "<b>(" + commenterTeam + ")</b>";
            used += commenterTeam.length();
        }
        int end = 74;
        int limit = 0;
        if (comment != null) {
            if ((used + comment.length()) / 2 > end) {
                HorizontalPanel hp = new HorizontalPanel();
                if (used < end) {
                    limit = end - used;
                    String str = null;
                    //int i =0;
                    while (limit != 0) {
                        str = comment.substring(limit - 1, limit);
                        if (str.equals(" ") || str.equals(".") || str.equals(",")) {
                            str = comment.substring(0, limit);
                            break;
                        }
                        limit--;
                    }
                    hp.add(new HTML(name + "<b>:</b>&nbsp;" + str + "..."));
                } else {
                    hp.add(new HTML(name));
                }
                hp.add(more);
                textPanel.add(hp);
                textPanel.add(panel);
                panel.setWidth(width + "px");
                if (limit != 0) {
                    panel.setContent(new HTML(formatString(comment.substring(limit))));
                } else {
                    panel.setContent(new HTML(formatString(comment)));
                }
                fullHTML = null;
            } else {
                fullHTML.setHTML(name + "<b>:</b>&nbsp;" + formatString(comment));
            }
        } else {
            fullHTML.setHTML(name + "<b>:</b>&nbsp;");
        }
        if (fullHTML == null) {

            initWidget(textPanel);
        } else {
            initWidget(fullHTML);
        }
    }

    private String formatString(String text) {
        StringBuilder sb = new StringBuilder();
        String[] html = text.split("\r\n");
        for (String aHtml : html) {
            sb.append(aHtml);
            sb.append("<br>");
        }
        return sb.toString();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}