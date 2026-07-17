package com.edatasite.workforce.gwt.core.server;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;

import java.util.Map;
import java.util.Objects;

/**
 * Created by dilsh0d on 27.03.16.
 */
public class OrgChartUtils {

    public static String getTeamNodesRow(int colspan, Integer depth, Integer id, String name, String desc, Integer parentId, String parentName, String parentDepartmentStr, boolean isShowView, Integer levelOptionList, boolean isFromUI, Integer childrenSize, Integer totalChildSize, Integer employeeCount, Integer parentChildSize, Integer selectedId, Integer showAllId, String depthLevel, Integer locationId, boolean departmentsHasLocation, Map<Integer, Integer> locationSizeMap) {
        String departmentLabel = locationId != null ? "<h2 data-tooltip=\"" + name + "\"><a href=\"#department|summary/" + id + "\">" + (name != null ? name.toUpperCase() : name) + "</a></h2>" : "<h2 data-tooltip=\"" + name + "\"><a href=\"#department|summary/" + id + "\" onclick=\"window.open('" + "Settings.html#location|summary/" + id + "', '_blank')\">" + (name != null ? name.toUpperCase() : name) + "</a></h2>";
        ;
        String employeesDiv = locationId != null || !departmentsHasLocation ? "<div class=\"nodeCount-right arrow-toggle " + (selectedId != null && selectedId.equals(id) ? "has-data" : "") + "\" id=\"empShow-" + id + "\" title=\"span of control\">" +
                "<span>" + employeeCount + "</span>" + "<i></i>" +
                "</div>" : "<div class=\"nodeCount-right arrow-toggle has-data" + "\" id=\"empShow-" + id + "\" title=\"span of control\" onclick=\"openTeamOrgChart('" + id + "')\">" +
                "<span>" + locationSizeMap.get(id) + "</span>" + "<i></i>" +
                "</div>";
        if (locationId != null ? !ServerUtils.hasPermission(PermissionConstants.HRMS_DEPARTMENT_SUMMARY_VIEW) : !ServerUtils.hasPermission(PermissionConstants.HRMS_POSITION_SUMMARRY)) {
            departmentLabel = "<h2 data-tooltip=\"" + name + "\"><a>" + (name != null ? name.toUpperCase() : name) + "</a></h2>";
        }
        if (isFromUI) {
            if (isShowView) {
                if (depth >= parentChildSize) {
                    return "<tr class=\"nodes nodesView--v nodesMultiple " + depthLevel + " " + (depth.equals(parentChildSize) ? "nodesMultipleFirst " : "") + " " + (childrenSize == 1 ? "mainOfColl " : childrenSize > 1 ? "mainOfRow " : "") + " \">" +
                            "<td class=\"nodeCell parentLines\" colspan=\"" + colspan + "\">" +
                            "<div class=\"nextLevelWrapp parentChildSize\">" +
                            "<div class=\"nodeWrapp\">" +
                            "<div class=\"node hasChildren shownChildren childrenLines\" name=\"teamboardn-" + name + "\" id=\"teamboard-:-" + id + "-:-" + name + "\">" +
                            employeesDiv +
                            "<div class=\"nodeCount-left arrow-toggle " + (showAllId != null && showAllId.equals(id) ? "has-data" : "") + "\" id=\"allShow-" + id + "\" title=\"span of control\">" +
                            "<span>" + totalChildSize + "</span>" +
                            "</div>" +
                            "<div class=\"nodeHgroup\"> " +
                            departmentLabel +
                            "</div>" +
                            "<div class=\"nodeSetParent\" >" +
                            "<a href=\"javascript:;\" onclick=\"openPopupSetParentTeamFromTree('" + id + "','" + name + "','" +
                            (parentId != null ? parentId : "-1") + "','" + (parentName != null ? parentName : "") + "')\" class=\"btn--circle\"  title=\" " + parentDepartmentStr + " \" ><svg viewBox=\"0 0 12 12\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                            "<path d=\"M0.600047 11.2939C0.600047 11.2939 0.300049 11.2939 0.300049 10.9409C0.300049 10.588 0.600047 9.52916 2.10004 9.52916C3.60002 9.52916 3.90002 10.588 3.90002 10.9409C3.90002 11.2939 3.60002 11.2939 3.60002 11.2939H0.600047ZM2.10004 9.17623C2.33873 9.17623 2.56765 9.06467 2.73643 8.86611C2.90521 8.66754 3.00003 8.39823 3.00003 8.11741C3.00003 7.83659 2.90521 7.56728 2.73643 7.36871C2.56765 7.17015 2.33873 7.05859 2.10004 7.05859C1.86134 7.05859 1.63243 7.17015 1.46364 7.36871C1.29486 7.56728 1.20004 7.83659 1.20004 8.11741C1.20004 8.39823 1.29486 8.66754 1.46364 8.86611C1.63243 9.06467 1.86134 9.17623 2.10004 9.17623Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                            "<g clip-path=\"url(#clip0_119_10611)\">\n" +
                            "<path d=\"M8.09999 4.94132C8.09999 4.94132 7.79999 4.94132 7.79999 4.58838C7.79999 4.23544 8.09999 3.17663 9.59997 3.17663C11.1 3.17663 11.4 4.23544 11.4 4.58838C11.4 4.94132 11.1 4.94132 11.1 4.94132H8.09999ZM9.59997 2.82369C9.83867 2.82369 10.0676 2.71213 10.2364 2.51357C10.4051 2.315 10.5 2.04569 10.5 1.76487C10.5 1.48406 10.4051 1.21474 10.2364 1.01617C10.0676 0.817608 9.83867 0.706055 9.59997 0.706055C9.36128 0.706055 9.13236 0.817608 8.96358 1.01617C8.7948 1.21474 8.69998 1.48406 8.69998 1.76487C8.69998 2.04569 8.7948 2.315 8.96358 2.51357C9.13236 2.71213 9.36128 2.82369 9.59997 2.82369Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                            "</g>\n" +
                            "<path d=\"M4.80005 11.1173H7.1618C8.59123 11.1173 9.75001 9.95852 9.75001 8.52908V6.70557\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M7.95013 7.94116L9.75012 6L11.4001 7.94116\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M6.8999 1.05848H4.53815C3.10872 1.05848 1.94994 2.21727 1.94994 3.6467V5.47021\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M3.75 4.23511L1.95001 6.17627L0.300025 4.23511\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<defs>\n" +
                            "<clipPath id=\"clip0_119_10611\">\n" +
                            "<rect width=\"4.79997\" height=\"5.64702\" fill=\"white\" transform=\"translate(7.20013)\"/>\n" +
                            "</clipPath>\n" +
                            "</defs>\n" +
                            "</svg></a>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</td>" +
                            "</tr>";
                } else {
                    return "<tr class=\"nodes nodesView--v nodesSingle " + depthLevel + " " + (childrenSize == 1 ? "mainOfColl " : childrenSize > 1 ? "mainOfRow " : "") + " \">" +
                            "<td class=\"nodeCell\" colspan=\"" + colspan + "\">" +
                            "<div class=\"nextLevelWrapp parentChildSize\">" +
                            "<div class=\"nodeWrapp\">" +
                            "<div class=\"node hasChildren shownChildren\" name=\"teamboardn-" + name + "\" id=\"teamboard-:-" + id + "-:-" + name + "\">" +
                            employeesDiv +
                            "<div class=\"nodeCount-left arrow-toggle " + (showAllId != null && showAllId.equals(id) ? "has-data" : "") + "\" id=\"allShow-" + id + "\" title=\"span of control\">" +
                            "<span>" + totalChildSize + "</span>" +
                            "</div>" +
                            "<div class=\"nodeHgroup\"> " +
                            departmentLabel +
                            "</div>" +
                            "<div class=\"nodeSetParent\" >" +
                            "<a href=\"javascript:;\" onclick=\"openPopupSetParentTeamFromTree('" + id + "','" + name + "','" +
                            (parentId != null ? parentId : "-1") + "','" + (parentName != null ? parentName : "") + "')\" class=\"btn--circle\" title=\" " + parentDepartmentStr + " \" ><svg viewBox=\"0 0 12 12\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                            "<path d=\"M0.600047 11.2939C0.600047 11.2939 0.300049 11.2939 0.300049 10.9409C0.300049 10.588 0.600047 9.52916 2.10004 9.52916C3.60002 9.52916 3.90002 10.588 3.90002 10.9409C3.90002 11.2939 3.60002 11.2939 3.60002 11.2939H0.600047ZM2.10004 9.17623C2.33873 9.17623 2.56765 9.06467 2.73643 8.86611C2.90521 8.66754 3.00003 8.39823 3.00003 8.11741C3.00003 7.83659 2.90521 7.56728 2.73643 7.36871C2.56765 7.17015 2.33873 7.05859 2.10004 7.05859C1.86134 7.05859 1.63243 7.17015 1.46364 7.36871C1.29486 7.56728 1.20004 7.83659 1.20004 8.11741C1.20004 8.39823 1.29486 8.66754 1.46364 8.86611C1.63243 9.06467 1.86134 9.17623 2.10004 9.17623Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                            "<g clip-path=\"url(#clip0_119_10611)\">\n" +
                            "<path d=\"M8.09999 4.94132C8.09999 4.94132 7.79999 4.94132 7.79999 4.58838C7.79999 4.23544 8.09999 3.17663 9.59997 3.17663C11.1 3.17663 11.4 4.23544 11.4 4.58838C11.4 4.94132 11.1 4.94132 11.1 4.94132H8.09999ZM9.59997 2.82369C9.83867 2.82369 10.0676 2.71213 10.2364 2.51357C10.4051 2.315 10.5 2.04569 10.5 1.76487C10.5 1.48406 10.4051 1.21474 10.2364 1.01617C10.0676 0.817608 9.83867 0.706055 9.59997 0.706055C9.36128 0.706055 9.13236 0.817608 8.96358 1.01617C8.7948 1.21474 8.69998 1.48406 8.69998 1.76487C8.69998 2.04569 8.7948 2.315 8.96358 2.51357C9.13236 2.71213 9.36128 2.82369 9.59997 2.82369Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                            "</g>\n" +
                            "<path d=\"M4.80005 11.1173H7.1618C8.59123 11.1173 9.75001 9.95852 9.75001 8.52908V6.70557\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M7.95013 7.94116L9.75012 6L11.4001 7.94116\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M6.8999 1.05848H4.53815C3.10872 1.05848 1.94994 2.21727 1.94994 3.6467V5.47021\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M3.75 4.23511L1.95001 6.17627L0.300025 4.23511\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<defs>\n" +
                            "<clipPath id=\"clip0_119_10611\">\n" +
                            "<rect width=\"4.79997\" height=\"5.64702\" fill=\"white\" transform=\"translate(7.20013)\"/>\n" +
                            "</clipPath>\n" +
                            "</defs>\n" +
                            "</svg></a>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</td>" +
                            "</tr>";
                }
            } else {
                if (depth >= parentChildSize) {
                    return "<tr class=\"nodes nodesView--h nodesMultiple " + depthLevel + " " + (depth.equals(parentChildSize) ? "nodesMultipleFirst " : "") + " " + (childrenSize == 1 ? "mainOfColl " : childrenSize > 1 ? "mainOfRow " : "") + " \">" +
                            "<td class=\"nodeCell parentLines\" colspan=\"" + colspan + "\">" +
                            "<div class=\"nextLevelWrapp parentChildSize\">" +
                            "<div class=\"nodeWrapp\">" +
                            "<div class=\"node hasChildren shownChildren childrenLines\" name=\"teamboardn-" + name + "\" id=\"teamboard-:-" + id + "-:-" + name + "\">" +
                            employeesDiv +
                            "<div class=\"nodeCount-left arrow-toggle " + (showAllId != null && showAllId.equals(id) ? "has-data" : "") + "\" id=\"allShow-" + id + "\" title=\"span of control\">" +
                            "<span>" + totalChildSize + "</span>" +
                            "</div>" +
                            "<div class=\"nodeHgroup\"> " +
                            departmentLabel +
//                            "<em data-tooltip=\"" + desc + "\"><span>" + (desc != null ? desc.toUpperCase() : "") + "</span></em>" +
                            "</div>" +
                            "<div class=\"nodeSetParent\" >" +
                            "<a href=\"javascript:;\" onclick=\"openPopupSetParentTeamFromTree('" + id + "','" + name + "','" +
                            (parentId != null ? parentId : "-1") + "','" + (parentName != null ? parentName : "") + "')\" class=\"btn--circle\"  title=\" " + parentDepartmentStr + " \" ><svg viewBox=\"0 0 12 12\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                            "<path d=\"M0.600047 11.2939C0.600047 11.2939 0.300049 11.2939 0.300049 10.9409C0.300049 10.588 0.600047 9.52916 2.10004 9.52916C3.60002 9.52916 3.90002 10.588 3.90002 10.9409C3.90002 11.2939 3.60002 11.2939 3.60002 11.2939H0.600047ZM2.10004 9.17623C2.33873 9.17623 2.56765 9.06467 2.73643 8.86611C2.90521 8.66754 3.00003 8.39823 3.00003 8.11741C3.00003 7.83659 2.90521 7.56728 2.73643 7.36871C2.56765 7.17015 2.33873 7.05859 2.10004 7.05859C1.86134 7.05859 1.63243 7.17015 1.46364 7.36871C1.29486 7.56728 1.20004 7.83659 1.20004 8.11741C1.20004 8.39823 1.29486 8.66754 1.46364 8.86611C1.63243 9.06467 1.86134 9.17623 2.10004 9.17623Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                            "<g clip-path=\"url(#clip0_119_10611)\">\n" +
                            "<path d=\"M8.09999 4.94132C8.09999 4.94132 7.79999 4.94132 7.79999 4.58838C7.79999 4.23544 8.09999 3.17663 9.59997 3.17663C11.1 3.17663 11.4 4.23544 11.4 4.58838C11.4 4.94132 11.1 4.94132 11.1 4.94132H8.09999ZM9.59997 2.82369C9.83867 2.82369 10.0676 2.71213 10.2364 2.51357C10.4051 2.315 10.5 2.04569 10.5 1.76487C10.5 1.48406 10.4051 1.21474 10.2364 1.01617C10.0676 0.817608 9.83867 0.706055 9.59997 0.706055C9.36128 0.706055 9.13236 0.817608 8.96358 1.01617C8.7948 1.21474 8.69998 1.48406 8.69998 1.76487C8.69998 2.04569 8.7948 2.315 8.96358 2.51357C9.13236 2.71213 9.36128 2.82369 9.59997 2.82369Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                            "</g>\n" +
                            "<path d=\"M4.80005 11.1173H7.1618C8.59123 11.1173 9.75001 9.95852 9.75001 8.52908V6.70557\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M7.95013 7.94116L9.75012 6L11.4001 7.94116\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M6.8999 1.05848H4.53815C3.10872 1.05848 1.94994 2.21727 1.94994 3.6467V5.47021\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M3.75 4.23511L1.95001 6.17627L0.300025 4.23511\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<defs>\n" +
                            "<clipPath id=\"clip0_119_10611\">\n" +
                            "<rect width=\"4.79997\" height=\"5.64702\" fill=\"white\" transform=\"translate(7.20013)\"/>\n" +
                            "</clipPath>\n" +
                            "</defs>\n" +
                            "</svg></a>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</td>" +
                            "</tr>";
                } else {
                    return "<tr class=\"nodes nodesView--h nodesSingle " + depthLevel + " " + (childrenSize == 1 ? "mainOfColl " : childrenSize > 1 ? "mainOfRow " : "") + " \">" +
                            "<td class=\"nodeCell\" colspan=\"" + colspan + "\">" +
                            "<div class=\"nextLevelWrapp parentChildSize\">" +
                            "<div class=\"nodeWrapp\">" +
                            "<div class=\"node hasChildren shownChildren\" name=\"teamboardn-" + name + "\" id=\"teamboard-:-" + id + "-:-" + name + "\">" +
                            employeesDiv +
                            "<div class=\"nodeCount-left arrow-toggle " + (showAllId != null && showAllId.equals(id) ? "has-data" : "") + "\" id=\"allShow-" + id + "\" title=\"span of control\">" +
                            "<span>" + totalChildSize + "</span>" +
                            "</div>" +
                            "<div class=\"nodeHgroup\"> " +
                            departmentLabel +
//                            "<em data-tooltip=\"" + desc + "\"><span>" + (desc != null ? desc.toUpperCase() : "") + "</span></em>" +
                            "</div>" +
                            "<div class=\"nodeSetParent\" >" +
                            "<a href=\"javascript:;\" onclick=\"openPopupSetParentTeamFromTree('" + id + "','" + name + "','" +
                            (parentId != null ? parentId : "-1") + "','" + (parentName != null ? parentName : "") + "')\" class=\"btn--circle\" title=\" " + parentDepartmentStr + " \" ><svg viewBox=\"0 0 12 12\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                            "<path d=\"M0.600047 11.2939C0.600047 11.2939 0.300049 11.2939 0.300049 10.9409C0.300049 10.588 0.600047 9.52916 2.10004 9.52916C3.60002 9.52916 3.90002 10.588 3.90002 10.9409C3.90002 11.2939 3.60002 11.2939 3.60002 11.2939H0.600047ZM2.10004 9.17623C2.33873 9.17623 2.56765 9.06467 2.73643 8.86611C2.90521 8.66754 3.00003 8.39823 3.00003 8.11741C3.00003 7.83659 2.90521 7.56728 2.73643 7.36871C2.56765 7.17015 2.33873 7.05859 2.10004 7.05859C1.86134 7.05859 1.63243 7.17015 1.46364 7.36871C1.29486 7.56728 1.20004 7.83659 1.20004 8.11741C1.20004 8.39823 1.29486 8.66754 1.46364 8.86611C1.63243 9.06467 1.86134 9.17623 2.10004 9.17623Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                            "<g clip-path=\"url(#clip0_119_10611)\">\n" +
                            "<path d=\"M8.09999 4.94132C8.09999 4.94132 7.79999 4.94132 7.79999 4.58838C7.79999 4.23544 8.09999 3.17663 9.59997 3.17663C11.1 3.17663 11.4 4.23544 11.4 4.58838C11.4 4.94132 11.1 4.94132 11.1 4.94132H8.09999ZM9.59997 2.82369C9.83867 2.82369 10.0676 2.71213 10.2364 2.51357C10.4051 2.315 10.5 2.04569 10.5 1.76487C10.5 1.48406 10.4051 1.21474 10.2364 1.01617C10.0676 0.817608 9.83867 0.706055 9.59997 0.706055C9.36128 0.706055 9.13236 0.817608 8.96358 1.01617C8.7948 1.21474 8.69998 1.48406 8.69998 1.76487C8.69998 2.04569 8.7948 2.315 8.96358 2.51357C9.13236 2.71213 9.36128 2.82369 9.59997 2.82369Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                            "</g>\n" +
                            "<path d=\"M4.80005 11.1173H7.1618C8.59123 11.1173 9.75001 9.95852 9.75001 8.52908V6.70557\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M7.95013 7.94116L9.75012 6L11.4001 7.94116\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M6.8999 1.05848H4.53815C3.10872 1.05848 1.94994 2.21727 1.94994 3.6467V5.47021\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M3.75 4.23511L1.95001 6.17627L0.300025 4.23511\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<defs>\n" +
                            "<clipPath id=\"clip0_119_10611\">\n" +
                            "<rect width=\"4.79997\" height=\"5.64702\" fill=\"white\" transform=\"translate(7.20013)\"/>\n" +
                            "</clipPath>\n" +
                            "</defs>\n" +
                            "</svg></a>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</td>" +
                            "</tr>";
                }
            }
        } else {
            if (isShowView) {
                if (depth >= parentChildSize) {
                    return "<tr class=\"nodes\">" +
                            "<td class=\"nodeCell parentLines\" colspan=\"" + colspan + "\">" +
                            "<div class=\"nextLevelWrapp parentChildSize\">" +
                            "<div class=\"nodeWrapp\">" +
                            "<div class=\"node hasChildren shownChildren childrenLines\" name=\"teamboardn-" + name + "\" id=\"teamboard-:-" + id + "-:-" + name + "\">" +
                            "<div class=\"nodeCount-right arrow-toggle\"> " +
                            "<span>" + employeeCount + "</span>" + "<i></i>" +
                            "</div>" +
                            "<div class=\"nodeCount-left\"> " +
                            "<span>" + totalChildSize + "</span>" +
                            "</div>" +
                            "<div class=\"nodeHgroup\"> " +
                            "<h2><a href=\"#department|summary/" + id + "\">" + (name != null ? name.toUpperCase() : name) + "</a></h2>" +
//                            "<em>" + (desc != null ? desc.toUpperCase() : "") + "</em>" +
                            "</div>" +
                            "<div class=\"nodeSetParent\" >" +
                            "<a class=\"btn--circle\"  title=\"set parent department\"><svg viewBox=\"0 0 12 12\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                            "<path d=\"M0.600047 11.2939C0.600047 11.2939 0.300049 11.2939 0.300049 10.9409C0.300049 10.588 0.600047 9.52916 2.10004 9.52916C3.60002 9.52916 3.90002 10.588 3.90002 10.9409C3.90002 11.2939 3.60002 11.2939 3.60002 11.2939H0.600047ZM2.10004 9.17623C2.33873 9.17623 2.56765 9.06467 2.73643 8.86611C2.90521 8.66754 3.00003 8.39823 3.00003 8.11741C3.00003 7.83659 2.90521 7.56728 2.73643 7.36871C2.56765 7.17015 2.33873 7.05859 2.10004 7.05859C1.86134 7.05859 1.63243 7.17015 1.46364 7.36871C1.29486 7.56728 1.20004 7.83659 1.20004 8.11741C1.20004 8.39823 1.29486 8.66754 1.46364 8.86611C1.63243 9.06467 1.86134 9.17623 2.10004 9.17623Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                            "<g clip-path=\"url(#clip0_119_10611)\">\n" +
                            "<path d=\"M8.09999 4.94132C8.09999 4.94132 7.79999 4.94132 7.79999 4.58838C7.79999 4.23544 8.09999 3.17663 9.59997 3.17663C11.1 3.17663 11.4 4.23544 11.4 4.58838C11.4 4.94132 11.1 4.94132 11.1 4.94132H8.09999ZM9.59997 2.82369C9.83867 2.82369 10.0676 2.71213 10.2364 2.51357C10.4051 2.315 10.5 2.04569 10.5 1.76487C10.5 1.48406 10.4051 1.21474 10.2364 1.01617C10.0676 0.817608 9.83867 0.706055 9.59997 0.706055C9.36128 0.706055 9.13236 0.817608 8.96358 1.01617C8.7948 1.21474 8.69998 1.48406 8.69998 1.76487C8.69998 2.04569 8.7948 2.315 8.96358 2.51357C9.13236 2.71213 9.36128 2.82369 9.59997 2.82369Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                            "</g>\n" +
                            "<path d=\"M4.80005 11.1173H7.1618C8.59123 11.1173 9.75001 9.95852 9.75001 8.52908V6.70557\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M7.95013 7.94116L9.75012 6L11.4001 7.94116\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M6.8999 1.05848H4.53815C3.10872 1.05848 1.94994 2.21727 1.94994 3.6467V5.47021\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M3.75 4.23511L1.95001 6.17627L0.300025 4.23511\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<defs>\n" +
                            "<clipPath id=\"clip0_119_10611\">\n" +
                            "<rect width=\"4.79997\" height=\"5.64702\" fill=\"white\" transform=\"translate(7.20013)\"/>\n" +
                            "</clipPath>\n" +
                            "</defs>\n" +
                            "</svg></a>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</td>" +
                            "</tr>";
                } else {
                    return "<tr class=\"nodes\">" +
                            "<td class=\"nodeCell\" colspan=\"" + colspan + "\">" +
                            "<div class=\"nextLevelWrapp parentChildSize\">" +
                            "<div class=\"nodeWrapp\">" +
                            "<div class=\"node hasChildren shownChildren\" name=\"teamboardn-" + name + "\" id=\"teamboard-:-" + id + "-:-" + name + "\">" +
                            "<div class=\"nodeCount-right arrow-toggle\"> " +
                            "<span>" + employeeCount + "</span>" + "<i></i>" +
                            "</div>" +
                            "<div class=\"nodeCount-left\"> " +
                            "<span>" + totalChildSize + "</span>" +
                            "</div>" +
                            "<div class=\"nodeHgroup\"> " +
                            "<h2><a href=\"#department|summary/" + id + "\">" + (name != null ? name.toUpperCase() : name) + "</a></h2>" +
//                            "<em>" + (desc != null ? desc.toUpperCase() : "") + "</em>" +
                            "</div>" +
                            "<div class=\"nodeSetParent\" >" +
                            "<a class=\"btn--circle\"  title=\"set parent department\"><svg viewBox=\"0 0 12 12\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                            "<path d=\"M0.600047 11.2939C0.600047 11.2939 0.300049 11.2939 0.300049 10.9409C0.300049 10.588 0.600047 9.52916 2.10004 9.52916C3.60002 9.52916 3.90002 10.588 3.90002 10.9409C3.90002 11.2939 3.60002 11.2939 3.60002 11.2939H0.600047ZM2.10004 9.17623C2.33873 9.17623 2.56765 9.06467 2.73643 8.86611C2.90521 8.66754 3.00003 8.39823 3.00003 8.11741C3.00003 7.83659 2.90521 7.56728 2.73643 7.36871C2.56765 7.17015 2.33873 7.05859 2.10004 7.05859C1.86134 7.05859 1.63243 7.17015 1.46364 7.36871C1.29486 7.56728 1.20004 7.83659 1.20004 8.11741C1.20004 8.39823 1.29486 8.66754 1.46364 8.86611C1.63243 9.06467 1.86134 9.17623 2.10004 9.17623Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                            "<g clip-path=\"url(#clip0_119_10611)\">\n" +
                            "<path d=\"M8.09999 4.94132C8.09999 4.94132 7.79999 4.94132 7.79999 4.58838C7.79999 4.23544 8.09999 3.17663 9.59997 3.17663C11.1 3.17663 11.4 4.23544 11.4 4.58838C11.4 4.94132 11.1 4.94132 11.1 4.94132H8.09999ZM9.59997 2.82369C9.83867 2.82369 10.0676 2.71213 10.2364 2.51357C10.4051 2.315 10.5 2.04569 10.5 1.76487C10.5 1.48406 10.4051 1.21474 10.2364 1.01617C10.0676 0.817608 9.83867 0.706055 9.59997 0.706055C9.36128 0.706055 9.13236 0.817608 8.96358 1.01617C8.7948 1.21474 8.69998 1.48406 8.69998 1.76487C8.69998 2.04569 8.7948 2.315 8.96358 2.51357C9.13236 2.71213 9.36128 2.82369 9.59997 2.82369Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                            "</g>\n" +
                            "<path d=\"M4.80005 11.1173H7.1618C8.59123 11.1173 9.75001 9.95852 9.75001 8.52908V6.70557\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M7.95013 7.94116L9.75012 6L11.4001 7.94116\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M6.8999 1.05848H4.53815C3.10872 1.05848 1.94994 2.21727 1.94994 3.6467V5.47021\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<path d=\"M3.75 4.23511L1.95001 6.17627L0.300025 4.23511\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                            "<defs>\n" +
                            "<clipPath id=\"clip0_119_10611\">\n" +
                            "<rect width=\"4.79997\" height=\"5.64702\" fill=\"white\" transform=\"translate(7.20013)\"/>\n" +
                            "</clipPath>\n" +
                            "</defs>\n" +
                            "</svg></a>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</td>" +
                            "</tr>";
                }
            } else {
                return "<tr class=\"nodes\">" +
                        "<td class=\"nodeCell\" colspan=\"" + colspan + "\">" +
                        "<div class=\"nextLevelWrapp parentChildSize\">" +
                        "<div class=\"nodeWrapp\">" +
                        "<div class=\"node hasChildren shownChildren\" name=\"teamboardn-" + name + "\" id=\"teamboard-:-" + id + "-:-" + name + "\">" +
                        "<div class=\"nodeCount-right arrow-toggle\"> " +
                        "<span>" + employeeCount + "</span>" + "<i></i>" +
                        "</div>" +
                        "<div class=\"nodeCount-left\"> " +
                        "<span>" + totalChildSize + "</span>" +
                        "</div>" +
                        "<div class=\"nodeHgroup\">" +
                        "<h2><a href=\"#department|summary/" + id + "\">" + (name != null ? name.toUpperCase() : name) + "</a></h2>" +
//                        "<em>" + (desc != null ? desc.toUpperCase() : "") + "</em>" +
                        "</div>" +
                        "<div class=\"nodeSetParent\" >" +
                        "<a class=\"btn--circle\"  title=\"set parent department\"><svg viewBox=\"0 0 12 12\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                        "<path d=\"M0.600047 11.2939C0.600047 11.2939 0.300049 11.2939 0.300049 10.9409C0.300049 10.588 0.600047 9.52916 2.10004 9.52916C3.60002 9.52916 3.90002 10.588 3.90002 10.9409C3.90002 11.2939 3.60002 11.2939 3.60002 11.2939H0.600047ZM2.10004 9.17623C2.33873 9.17623 2.56765 9.06467 2.73643 8.86611C2.90521 8.66754 3.00003 8.39823 3.00003 8.11741C3.00003 7.83659 2.90521 7.56728 2.73643 7.36871C2.56765 7.17015 2.33873 7.05859 2.10004 7.05859C1.86134 7.05859 1.63243 7.17015 1.46364 7.36871C1.29486 7.56728 1.20004 7.83659 1.20004 8.11741C1.20004 8.39823 1.29486 8.66754 1.46364 8.86611C1.63243 9.06467 1.86134 9.17623 2.10004 9.17623Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                        "<g clip-path=\"url(#clip0_119_10611)\">\n" +
                        "<path d=\"M8.09999 4.94132C8.09999 4.94132 7.79999 4.94132 7.79999 4.58838C7.79999 4.23544 8.09999 3.17663 9.59997 3.17663C11.1 3.17663 11.4 4.23544 11.4 4.58838C11.4 4.94132 11.1 4.94132 11.1 4.94132H8.09999ZM9.59997 2.82369C9.83867 2.82369 10.0676 2.71213 10.2364 2.51357C10.4051 2.315 10.5 2.04569 10.5 1.76487C10.5 1.48406 10.4051 1.21474 10.2364 1.01617C10.0676 0.817608 9.83867 0.706055 9.59997 0.706055C9.36128 0.706055 9.13236 0.817608 8.96358 1.01617C8.7948 1.21474 8.69998 1.48406 8.69998 1.76487C8.69998 2.04569 8.7948 2.315 8.96358 2.51357C9.13236 2.71213 9.36128 2.82369 9.59997 2.82369Z\" fill=\"black\" fill-opacity=\"0.45\"/>\n" +
                        "</g>\n" +
                        "<path d=\"M4.80005 11.1173H7.1618C8.59123 11.1173 9.75001 9.95852 9.75001 8.52908V6.70557\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                        "<path d=\"M7.95013 7.94116L9.75012 6L11.4001 7.94116\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                        "<path d=\"M6.8999 1.05848H4.53815C3.10872 1.05848 1.94994 2.21727 1.94994 3.6467V5.47021\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                        "<path d=\"M3.75 4.23511L1.95001 6.17627L0.300025 4.23511\" stroke=\"black\" stroke-opacity=\"0.71\" stroke-width=\"0.235292\" stroke-linecap=\"round\"/>\n" +
                        "<defs>\n" +
                        "<clipPath id=\"clip0_119_10611\">\n" +
                        "<rect width=\"4.79997\" height=\"5.64702\" fill=\"white\" transform=\"translate(7.20013)\"/>\n" +
                        "</clipPath>\n" +
                        "</defs>\n" +
                        "</svg></a>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</td>" +
                        "</tr>";
            }
        }
    }

    public static String getTeamTLinesRow(int colspan, Integer depth) {
        return getTeamTLinesRow(colspan, depth, false, null, null);
    }

    public static String getTeamTLinesRow(int colspan, Integer depth, boolean isShowView, Integer childrenSize, Integer parentChildSize) {
        if (isShowView) {
            if (depth >= parentChildSize) {
                return "";
//                        "<tr class=\"tlines test--6 vertical-withChild\">" +
//                        "   <td class=\"tSame25\"></td>" +
//                        "   <td class=\"tSame25 tline tleft tbottom\"></td>" +
//                        "   <td class=\"tSame25 tline tright\"></td>" +
//                        "   <td class=\"tSame25\"></td>" +
//                        "</tr>";
            } else {
                //vertical view - after Single nodes only
                StringBuilder html = new StringBuilder("<tr class=\"tlines v tlines-single\">");
                for (int i = 1; i <= colspan; i++) {
                    if (i == 1) {
                        html.append("<td class=\"tline tleft\"></td>");
                    } else if (i == colspan) {
                        html.append("<td class=\"tline tright\"></td>");
                    } else {
                        html.append(i % 2 == 0 ? "<td class=\"tline trightTop\"></td>" : "<td class=\"tline tleftTop\"></td>");
                    }
                }
                html.append("</tr>");
//                return html.toString();
                return "";
            }
        } else {
//            Horizontal View after Every node with child
//            StringBuilder html = new StringBuilder("<tr class=\"tlines v test--8\">");
//            for (int i = 1; i <= colspan; i++) {
//                if (i == 1) {
//                    html.append("<td class=\"tline tleft\"></td>");
//                } else if (i == colspan) {
//                    html.append("<td class=\"tline tright\"></td>");
//                } else {
//                    html.append(i % 2 == 0 ? "<td class=\"tline trightTop\"></td>" : "<td class=\"tline tleftTop\"></td>");
//                }
//            }
//            html.append("</tr>");
//            return html.toString();
            return "";
        }
    }

    public static String getTeamTLinesRowEmployee(int colspan, Integer depth, boolean isShowView, Integer childrenSize, Integer parentChildSize) {
        if (isShowView) {
            if (depth >= parentChildSize) {
                return "";
//                        "<tr class=\"tlines tlinesY test--3 withChild-after-opened-subNodes\">" +
//                        "   <td class=\"tSame25\"></td>" +
//                        "   <td class=\"tSame25 tline tleft\"></td>" +
//                        "   <td class=\"tSame25 tline tright\"></td>" +
//                        "   <td class=\"tSame25\"></td>" +
//                        "</tr>";
            } else {
                return "";
//                        "<tr class=\"tlines tlinesY test--4 \">" +
//                        "   <td class=\"tSame25\"></td>" +
//                        "   <td class=\"tSame25 tline tleft\"></td>" +
//                        "   <td class=\"tSame25 tline tright\"></td>" +
//                        "   <td class=\"tSame25\"></td>" +
//                        "</tr>";
            }
        } else {
            return "";
//                        "<tr class=\"tlines tlinesY test--5  withChild-after-opened-subNodes\">" +
//                        "   <td class=\"tSame25\"></td>" +
//                        "   <td class=\"tSame25 tline tleft\"></td>" +
//                        "   <td class=\"tSame25 tline tright\"></td>" +
//                        "   <td class=\"tSame25\"></td>" +
//                        "</tr>";
        }
    }

    public static String getTeamVerticalLineRow(int colspan, Integer depth) {
        return getTeamVerticalLineRow(colspan, depth, false, null, null);
    }

    public static String getTeamVerticalLineRow(int colspan, Integer depth, boolean isShowView, Integer childrenSize, Integer parentChildSize) {
        if (isShowView) {
//          in vertical view before NodesMultipleFirst only
            if (depth.equals(parentChildSize - 1)) {
                StringBuilder html = new StringBuilder("<tr class=\"tlines v tlines-multipleFirst \">");
                for (int i = 1; i <= colspan; i++) {
                    if (i == 1) {
                        html.append("<td class=\"tline tleft\"></td>");
                    } else if (i == colspan) {
                        html.append("<td class=\"tline tright\"></td>");
                    } else {
                        html.append(i % 2 == 0 ? "<td class=\"tline trightTop\"></td>" : "<td class=\"tline tleftTop\"></td>");
                    }
                }
                html.append("</tr>");
//                return html.toString();
                return "";

            } else {
//            StringBuilder html = new StringBuilder("<tr class=\"tlines test--0 vertical-with-child\">");
//            for (int i = 1; i <= colspan; i++) {
//            html.append("<td class=\"tSame25\"></td>");
//            html.append("<td class=\"tSame25 tline trightTop bls2\"></td>");
//            html.append("<td class=\"tSame25 tline\"></td>");
//            html.append("<td class=\"tSame25\"></td>");
//                if (i == 1) {
//                    html.append("<td class=\"tline tleft\"></td>");
//                } else if (i == colspan) {
//                    html.append("<td class=\"tline tright\"></td>");
//                } else {
//                    html.append(i % 2 == 0 ? "<td class=\"tline trightTop\"></td>" : "<td class=\"tline tleftTop\"></td>");
//                }
//            }
//            html.append("</tr>");
//            return html.toString();
                return "";
            }
        } else {
//          Horizontal view after Every node with child
            StringBuilder html = new StringBuilder("<tr class=\"tlines v test--2\">");
            for (int i = 1; i <= colspan; i++) {
                if (i == 1) {
                    html.append("<td class=\"tline tleft\"></td>");
                } else if (i == colspan) {
                    html.append("<td class=\"tline tright\"></td>");
                } else {
                    html.append(i % 2 == 0 ? "<td class=\"tline trightTop\"></td>" : "<td class=\"tline tleftTop\"></td>");
                }
            }
            html.append("</tr>");
//                return html.toString();
            return "";
        }
    }

    public static String getTeamEmployeeNodes(int colspan, Integer depth, String team, Integer leaderId, String vacant, Map<Integer, SelectItem> resultMap, String employeesOrgChart, boolean isShowView, Integer levelOptionList, Integer childrenSize, Integer parentChildSize) {
        boolean hasFullListAccess = ServerUtils.hasPermission(PermissionConstants.SHOW_ALL_EMPLOYEE_LIST);
        EdsUser edsUser = ((EdsUser) ServerSecurityContext.getInstance().getUser());
        boolean showTeamEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_DEPARTMENT_EMPLOYEE_LIST) && resultMap.containsKey(edsUser.getObjectID());
        if (isShowView) {
            if (depth >= parentChildSize) {

                StringBuilder html = new StringBuilder("<tr class=\"subNodes\"><td class=\"subNodeCell\" colspan=\"4\">" +
                        "<div class=\"subNodeWrapp\">" +
                        "<div class=\"subNode hasChildren shownChildren\">");
                html.append("<h2>").append("  </h2>");
                html.append("<ul class=\"emplTree\">");
                boolean isHasLeader = false;
                boolean isCurrentUser = Objects.equals(leaderId, edsUser.getObjectID());
                String genderClass;
                if (resultMap.containsKey(leaderId)) {
                    SelectItem leaderItem = resultMap.get(leaderId);
                    genderClass = leaderItem != null && leaderItem.getCategory() != null ? Constants.MALE.equals(leaderItem.getCategory()) ? "genderIs--male" : "genderIs--female" : "genderIs--no";
                    StringBuilder noBrokenImage = new StringBuilder();
                    if (leaderItem.getNumber() != null) {
                        noBrokenImage.append("<img src=").append(leaderItem.getNumber()).append(" />");
                    } else {
                    }
                    if (hasFullListAccess || isCurrentUser) {
                        html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\">").append(noBrokenImage).append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5><a href=\"#employeeProfile|employeeProfileView/").append(leaderItem.getId()).append("\">").append(leaderItem.getName()).append("</a></h5>").append("<em class=\"emplPost\">").append(leaderItem.getDescription() != null ? leaderItem.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>");
                    } else {
                        html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\">").append(noBrokenImage).append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5>").append(leaderItem.getName()).append("</h5>").append("<em class=\"emplPost\">").append(leaderItem.getDescription() != null ? leaderItem.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>");
                    }
                    if (resultMap.size() > 1) {
                        html.append("<ul>");
                    }
                    isHasLeader = true;
                } else if (leaderId != null && leaderId == -1) {
                    genderClass = "genderIs--hire";
                    html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\">").append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5>").append(vacant).append("</h5>").append("<em class=\"emplPost\">").append("&nbsp;").append("</em>").append("</div>").append("</div>");
                    if (resultMap.size() > 1) {
                        html.append("<ul>");
                    }
                }
                for (SelectItem employee : resultMap.values()) {
                    genderClass = employee != null && employee.getCategory() != null ? Constants.MALE.equals(employee.getCategory()) ? "genderIs--male" : "genderIs--female" : "genderIs--no";
                    StringBuilder noBrokenImage = new StringBuilder();
                    if (employee.getNumber() != null) {
                        noBrokenImage.append("<img src=").append(employee.getNumber()).append(" />");
                    } else {
                    }
                    if (!employee.getId().equals(leaderId) && employee.getId() < 0) {
                        genderClass = "genderIs--hire";
                        html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\">").append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5>").append(employee.getName()).append("</h5>").append("<em class=\"emplPost\">").append(employee.getDescription() != null ? employee.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>");
                    } else if (!employee.getId().equals(leaderId)) {
                        isCurrentUser = Objects.equals(employee.getId(), edsUser.getObjectID());
                        if (hasFullListAccess || isCurrentUser || showTeamEmployees) {
                            html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\" >").append(noBrokenImage).append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5><a href=\"#employeeProfile|employeeProfileView/").append(employee.getId()).append("\">").append(employee.getName()).append("</a></h5>").append("<em class=\"emplPost\">").append(employee.getDescription() != null ? employee.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>").append("</li>");
                        } else {
                            html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\" >").append(noBrokenImage).append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5>").append(employee.getName()).append("</h5>").append("<em class=\"emplPost\">").append(employee.getDescription() != null ? employee.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>").append("</li>");
                        }
                    }
                }
                if (isHasLeader) {
                    if (resultMap.size() > 1) {
                        html.append("</ul>");
                    }
                    html.append("</li>");
                }
                html.append("</ul></div></div></td></tr>");
                return html.toString();
            } else {
                StringBuilder html = new StringBuilder("<tr class=\"subNodes test--secondRow\"><td class=\"subNodeCell\" colspan=\"" + colspan + "\">" +
                        "<div class=\"subNodeWrapp\">" +
                        "<div class=\"subNode hasChildren shownChildren\">");
                html.append("<h2>").append("  </h2>");
                html.append("<ul class=\"emplTree\">");
                boolean isHasLeader = false;
                boolean isCurrentUser = Objects.equals(leaderId, edsUser.getObjectID());
                String genderClass;
                if (resultMap.containsKey(leaderId)) {
                    SelectItem leaderItem = resultMap.get(leaderId);
                    StringBuilder noBrokenImage = new StringBuilder();
                    if (leaderItem.getNumber() != null) {
                        noBrokenImage.append("<img src=").append(leaderItem.getNumber()).append(" />");
                    } else {
                    }
                    if (leaderId == -1) {
                        genderClass = "genderIs--hire";
                    } else {
                        genderClass = leaderItem != null && leaderItem.getCategory() != null ? Constants.MALE.equals(leaderItem.getCategory()) ? "genderIs--male" : "genderIs--female" : "genderIs--no";
                    }
                    if (hasFullListAccess || isCurrentUser) {
                        html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\" >").append(noBrokenImage).append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5><a href=\"#employeeProfile|employeeProfileView/").append(leaderItem.getId()).append("\">").append(leaderItem.getName()).append("</a></h5>").append("<em class=\"emplPost\">").append(leaderItem.getDescription() != null ? leaderItem.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>");
                    } else {
                        html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\" >").append(noBrokenImage).append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5>").append(leaderItem.getName()).append("</h5>").append("<em class=\"emplPost\">").append(leaderItem.getDescription() != null ? leaderItem.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>");
                    }
                    if (resultMap.size() > 1) {
                        html.append("<ul>");
                    }
                    isHasLeader = true;
                }
                for (SelectItem employee : resultMap.values()) {
                    genderClass = employee != null && employee.getCategory() != null ? Constants.MALE.equals(employee.getCategory()) ? "genderIs--male" : "genderIs--female" : "genderIs--no";
                    StringBuilder noBrokenImage = new StringBuilder();
                    if (employee.getNumber() != null) {
                        noBrokenImage.append("<img src=").append(employee.getNumber()).append(" />");
                    } else {
                    }
                    isCurrentUser = Objects.equals(employee.getId(), edsUser.getObjectID());
                    if (!employee.getId().equals(leaderId) && employee.getId() < 0) {
                        genderClass = "genderIs--hire";
                        html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\">").append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5>").append(employee.getName()).append("</h5>").append("<em class=\"emplPost\">").append(employee.getDescription() != null ? employee.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>");
                    } else if (!employee.getId().equals(leaderId)) {
                        if (hasFullListAccess || isCurrentUser || showTeamEmployees) {
                            html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\" >").append(noBrokenImage).append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5><a href=\"#employeeProfile|employeeProfileView/").append(employee.getId()).append("\">").append(employee.getName()).append("</a></h5>").append("<em class=\"emplPost\">").append(employee.getDescription() != null ? employee.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>").append("</li>");
                        } else {
                            html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\" >").append(noBrokenImage).append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5>").append(employee.getName()).append("</h5>").append("<em class=\"emplPost\">").append(employee.getDescription() != null ? employee.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>").append("</li>");
                        }
                    }
                }
                if (isHasLeader) {
                    if (resultMap.size() > 1) {
                        html.append("</ul>");
                    }
                    html.append("</li>");
                }
                html.append("</ul></div></div></td></tr>");
                return html.toString();
            }
        } else {
            StringBuilder html = new StringBuilder("<tr class=\"subNodes\"><td class=\"subNodeCell\" colspan=\"" + colspan + "\">" +
                    "<div class=\"subNodeWrapp\">" +
                    "<div class=\"subNode hasChildren shownChildren\">");
            html.append("<h2>").append("  </h2>");
            html.append("<ul class=\"emplTree\">");
            boolean isHasLeader = false;
            boolean isCurrentUser = Objects.equals(leaderId, edsUser.getObjectID());
            String genderClass;
            if (resultMap.containsKey(leaderId)) {
                SelectItem leaderItem = resultMap.get(leaderId);
                StringBuilder noBrokenImage = new StringBuilder();
                if (leaderItem.getNumber() != null) {
                    noBrokenImage.append("<img src=").append(leaderItem.getNumber()).append(" />");
                } else {
                }
                if (leaderId == -1) {
                    genderClass = "genderIs--hire";
                } else {
                    genderClass = leaderItem != null && leaderItem.getCategory() != null ? Constants.MALE.equals(leaderItem.getCategory()) ? "genderIs--male" : "genderIs--female" : "genderIs--no";
                }
                if (hasFullListAccess || isCurrentUser) {
                    html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\">").append(noBrokenImage).append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5><a href=\"#employeeProfile|employeeProfileView/").append(leaderItem.getId()).append("\">").append(leaderItem.getName()).append("</a></h5>").append("<em class=\"emplPost\">").append(leaderItem.getDescription() != null ? leaderItem.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>");
                } else {
                    html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\">").append(noBrokenImage).append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5>").append(leaderItem.getName()).append("</h5>").append("<em class=\"emplPost\">").append(leaderItem.getDescription() != null ? leaderItem.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>");
                }
                if (resultMap.size() > 1) {
                    html.append("<ul>");
                }
                isHasLeader = true;
            }
            for (SelectItem employee : resultMap.values()) {
                StringBuilder noBrokenImage = new StringBuilder();
                if (employee.getNumber() != null) {
                    noBrokenImage.append("<img src=").append(employee.getNumber()).append(" />");
                } else {
                }
                genderClass = employee != null && employee.getCategory() != null ? Constants.MALE.equals(employee.getCategory()) ? "genderIs--male" : "genderIs--female" : "genderIs--no";
                if (!employee.getId().equals(leaderId)) {
                    isCurrentUser = Objects.equals(employee.getId(), edsUser.getObjectID());
                    if (hasFullListAccess || isCurrentUser || showTeamEmployees) {
                        html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\" >").append(noBrokenImage).append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5><a href=\"#employeeProfile|employeeProfileView/").append(employee.getId()).append("\">").append(employee.getName()).append("</a></h5>").append("<em class=\"emplPost\">").append(employee.getDescription() != null ? employee.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>").append("</li>");
                    } else {
                        html.append("<li class = ").append(">" + "<div class=\"subNodeItem ").append(genderClass).append("\">").append("<div class=\"figImage\" >").append("<div class=\"circular-portrait\" >").append(noBrokenImage).append("</div>").append("</div>").append("<div class=\"figCaption\">").append("<h5>").append(employee.getName()).append("</h5>").append("<em class=\"emplPost\">").append(employee.getDescription() != null ? employee.getDescription() : "&nbsp;").append("</em>").append("</div>").append("</div>").append("</li>");
                    }
                }
            }
            if (isHasLeader) {
                if (resultMap.size() > 1) {
                    html.append("</ul>");
                }
                html.append("</li>");
            }
            html.append("</ul></div></div></td></tr>");
            return html.toString();
        }
    }

    public static String getEmployeeNodesRow(int colspan, Integer id, String name, String position, String team, String img) {
        return getEmployeeNodesRow(colspan, id, name, position, team, img, false);
    }

    public static String getEmployeeNodesRow(int colspan, Integer id, String name, String position, String team, String img, boolean isShowView) {
        if (isShowView) {
            if (id != null && id > 0) {
                return " ";
            } else {
                return "<tr class=\"subNodes\">" +
                        "<td class=\"subNodeCell\" colspan=\"" + colspan + "\">" +
                        "<div class=\"subNodeWrapp\">" +
                        "<div class=\"subNode hasChildren shownChildren\">" +
                        "<div class=\"figImage\">" +
                        "<div class=\"circular-portrait\">" +
                        "<img src=\"" + img + "\"/>" +
                        "</div>" +
                        "</div>" +
                        "<div class=\"figCaption\">" +
                        "<h2>" + name + "</h2>" +
                        "<h3 style=\"font-size:11px\"><i>" + position + "</i></h3>" +
                        "<em>" + (team != null ? team : "&nbsp;") + "</em>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</td>" +
                        "</tr>";
            }
        } else {
            if (id != null && id > 0) {
                return "<tr class=\"subNodes spvrStructure--\">" +
                        "<td class=\"subNodeCell\" colspan=\"" + colspan + "\">" +
                        "<div class=\"subNodeWrapp\">" +
                        "<div class=\"subNode hasChildren shownChildren\">" +
                        "<div class=\"figImage\">" +
                        "<div class=\"circular-portrait\">" +
                        "<img src=\"" + img + "\"/>" +
                        "</div>" +
                        "</div>" +
                        "<div class=\"figCaption\">" +
                        "<h2><a href=\"#employeeProfile|employeeProfileView/" + id + "\">" + name + "</a></h2>" +
                        "<h3 style=\"font-size:11px\"><i>" + position + "</i></h3>" +
                        "<em>" + (team != null ? team : "&nbsp;") + "</em>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</td>" +
                        "</tr>";
            } else {
                return "<tr class=\"subNodes\">" +
                        "<td class=\"subNodeCell\" colspan=\"" + colspan + "\">" +
                        "<div class=\"subNodeWrapp\">" +
                        "<div class=\"subNode hasChildren shownChildren\">" +
                        "<div class=\"figImage\">" +
                        "<div class=\"circular-portrait\">" +
                        "<img src=\"" + img + "\"/>" +
                        "</div>" +
                        "</div>" +
                        "<div class=\"figCaption\">" +
                        "<h2>" + name + "</h2>" +
                        "<h3 style=\"font-size:11px\"><i>" + position + "</i></h3>" +
                        "<em>" + (team != null ? team : "&nbsp;") + "</em>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</td>" +
                        "</tr>";
            }
        }
    }
}
