package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.ui.FlexTable;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 20.11.2008
 * Time: 15:14:47
 * To change this template use File | Settings | File Templates.
 */

/**
 * Displays tips, helps background will be gray
 */
public class HelpTextPanel extends FlexTable {
    private String text;
    private int width = 250;
    private boolean newWidth = false;

    public HelpTextPanel(String text) {
        super();
        this.text = text;
        init();
    }

    public HelpTextPanel(String text, int width) {
        super();
        this.text = text;
        this.width = width;
        newWidth = true;
        init();
    }

    private void init() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table cellpadding=\"0\" cellspacing=\"0\" class=\"hp\" bgcolor=\"#e8e8e8\">");
        sb.append("<tr>");
        sb.append("<td class=\"hp_l_t\" height=\"10\" width=\"9\">");
        sb.append("</td>");
        sb.append("<td>");
        sb.append("</td>");
        sb.append("<td class=\"hp_r_t\" height=\"10\" width=\"9\">");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");
        sb.append("</td>");
        sb.append("<td " + (newWidth ? "style =\"width:" + width + "px;\"" : "") + ">");
        sb.append(text);
        sb.append("</td>");
        sb.append("<td>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td class=\"hp_l_b\" height=\"10\" width=\"9\">");
        sb.append("</td>");
        sb.append("<td>");
        sb.append("</td>");
        sb.append("<td class=\"hp_r_b\" height=\"10\" width=\"9\">");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("</table>");
        setHTML(0, 0, sb.toString());
    }

}
