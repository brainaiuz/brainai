package com.edatasite.workforce.gwt.core.server;

public class SupervisorStructureUtils {

    public static String getTeamTLinesRowForSuperVsr(int colspan) {
        return "<tr class=\"tlines\">" +
                "<td colspan=\"" + colspan + "\">" +
                "   <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">" +
                "       <tbody>" +
                "           <tr class=\"tlines x\">" +
                "               <td class=\"tline tleft\"></td>" +
                "               <td class=\"tline tright\"></td>" +
                "           </tr>" +
                "       </tbody>" +
                "   </table>" +
                "</td>" +
                "</tr>";
    }

    public static String getTeamTLinesRowForSuperVsrVerticalView(int colspan) {
        return "<tr class=\"tlines\">" +
                "<td colspan=\"" + colspan + "\">" +
                "   <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">" +
                "       <tbody>" +
                "           <tr class=\"tlines x\">" +
                "               <td class=\"tline tleft\"></td>" +
                "               <td class=\"tline tright\"></td>" +
                "           </tr>" +
                "       </tbody>" +
                "   </table>" +
                "</td>" +
                "</tr>";
    }


    public static String getTeamVerticalLineRowForSuperVsr(int colspan) {
        StringBuilder html = new StringBuilder("<tr class=\"tlines v\">");
        if (colspan > 2) {
            for (int i = 1; i <= colspan; i++) {
                if (i == 1) {
                    html.append("<td class=\"tline tleft\"></td>");
                } else if (i == 2) {
                    html.append("<td class=\"tline tright ttop\"></td>");
                } else if (i == colspan - 1) {
                    html.append("<td class=\"tline tleft ttop\"></td>");
                } else if (i == colspan) {
                    html.append("<td class=\"tline tright\"></td>");
                } else {
                    html.append(i % 2 == 0 ? "<td class=\"tline tlinestraight ttop\"></td>" : "<td class=\"tline ttop\"></td>");
                }
            }
        } else {
            for (int i = 1; i <= colspan; i++) {
                if (i == 1) {
                    html.append("<td class=\"tline tleft\"></td>");
                } else if (i == colspan) {
                    html.append("<td class=\"tline tright\"></td>");
                } else {
                    html.append(i % 2 == 0 ? "<td class=\"tline tright ttop\"></td>" : "<td class=\"tline tleft ttop\"></td>");
                }
            }
        }
        html.append("</tr>");
        return html.toString();
    }

    public static String getTeamVerticalLineRowForSuperVsrVerticalView(int colspan) {
        StringBuilder html = new StringBuilder("<tr class=\"tlines v\">");
        if (colspan > 2) {
            for (int i = 1; i <= colspan; i++) {
                if (i == 1) {
                    html.append("<td class=\"tline tleft\"></td>");
                } else if (i == 2) {
                    html.append("<td class=\"tline tright ttop\"></td>");
                } else if (i == colspan - 1) {
                    html.append("<td class=\"tline tleft ttop\"></td>");
                } else if (i == colspan) {
                    html.append("<td class=\"tline tright\"></td>");
                } else {
                    html.append(i % 2 == 0 ? "<td class=\"tline tlinestraight ttop\"></td>" : "<td class=\"tline ttop\"></td>");
                }
            }
        } else {
            for (int i = 1; i <= colspan; i++) {
                if (i == 1) {
                    html.append("<td class=\"tline tleft\"></td>");
                } else if (i == colspan) {
                    html.append("<td class=\"tline tright\"></td>");
                } else {
                    html.append(i % 2 == 0 ? "<td class=\"tline tright ttop\"></td>" : "<td class=\"tline tleft ttop\"></td>");
                }
            }
        }
        html.append("</tr>");
        return html.toString();
    }

    public static String getEmployeeNodesRowForSuperVsr(int colspan, Integer id, String name, String position, String team, String img) {
        if (id != null && id > 0) {
            return "<tr class=\"nodes\">" +
                    "<td class=\"node\" colspan=\"" + colspan + "\">" +
                    "<div class=\"node hasChildren shownChildren\">" +
                    "<div class=\"circular-portrait\">" +
                    "<img onerror=\n" +
                    "\"this.style.display = 'none'\" src=\"" + img + "\"/>" +
                    "</div>" +
                    "<div class=\"overhide\">" +
                    "<h2 title='" + name + "'><a href=\"#employeeProfile|employeeProfileView/" + id + "\">" + name + "</a></h2>" +
                    "<h3 style=\"font-size:11px\"><i>" + position + "</i></h3>" +
//                    "<em>" + (team != null ? team : "&nbsp;") + "</em>" +
                    "</div>" +
                    "</div>" +
                    "</td>" +
                    "</tr>";
        } else {
            return "<tr class=\"nodes\">" +
                    "<td class=\"node\" colspan=\"" + colspan + "\">" +
                    "<div class=\"node hasChildren shownChildren\">" +
                    "<div class=\"circular-portrait\">" +
                    "<img onerror=\n" +
                    "\"this.style.display = 'none'\" src=\"" + img + "\"/>" +
                    "</div>" +
                    "<div class=\"overhide\">" +
                    "<h2 title='" + name + "'>" + name + "</h2>" +
                    "<h3 style=\"font-size:11px\"><i>" + position + "</i></h3>" +
//                    "<em>" + (team != null ? team : "&nbsp;") + "</em>" +
                    "</div>" +
                    "</div>" +
                    "</td>" +
                    "</tr>";
        }
    }

    public static String getEmployeeNodesRowForSuperVsrVerticalView(int colspan, Integer id, String name, String position, String team, String img) {
        if (id != null && id > 0) {
            return "<tr class=\"nodes\">" +
                    "<td class=\"node\" colspan=\"" + colspan + "\">" +
                    "<div class=\"node hasChildren shownChildren\">" +
                    "<div class=\"circular-portrait\">" +
                    "<img onerror=\n" +
                    "\"this.style.display = 'none'\" src=\"" + img + "\"/>" +
                    "</div>" +
                    "<div class=\"overhide\">" +
                    "<h2 title='" + name + "'><a href=\"#employeeProfile|employeeProfileView/" + id + "\">" + name + "</a></h2>" +
                    "<h3 style=\"font-size:11px\"><i>" + position + "</i></h3>" +
//                    "<em>" + (team != null ? team : "&nbsp;") + "</em>" +
                    "</div>" +
                    "</div>" +
                    "</td>" +
                    "</tr>";
        } else {
            return "<tr class=\"nodes\">" +
                    "<td class=\"node\" colspan=\"" + colspan + "\">" +
                    "<div class=\"node hasChildren shownChildren\">" +
                    "<div class=\"circular-portrait\">" +
                    "<img onerror=\n" +
                    "\"this.style.display = 'none'\" src=\"" + img + "\"/>" +
                    "</div>" +
                    "<div class=\"overhide\">" +
                    "<h2 title='" + name + "'>" + name + "</h2>" +
                    "<h3 style=\"font-size:11px\"><i>" + position + "</i></h3>" +
//                    "<em>" + (team != null ? team : "&nbsp;") + "</em>" +
                    "</div>" +
                    "</div>" +
                    "</td>" +
                    "</tr>";
        }
    }
}
