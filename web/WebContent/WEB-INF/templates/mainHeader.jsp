<%@ page import="com.edatasite.workforce.gwt.core.client.ui.CompanyConstants" %>
<%@ page import="com.edatasite.workforce.gwt.core.client.ui.Constants" %>
<%@ page import="com.edatasite.workforce.gwt.core.client.ui.PermissionConstants" %>
<%@ page import="com.edatasite.workforce.gwt.core.server.app.AuthUtils" %>
<%@ page import="com.edatasite.workforce.gwt.core.server.app.ServerUtils" %>
<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%@ page import="java.util.HashSet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: tashkent
  Date: 22-Jun-2011
  Time: 19:36:40
  To change this template use File | Settings | File Templates.
--%>
<%
    HashSet<String> menuPermissions = ServerUtils.getMainMenuPermissions();
    boolean isClient = "true".equals(request.getAttribute(Constants.IS_CLIENT));
    String productName = request.getAttribute("productName").toString().toLowerCase();
    String productNameWithOutWhiteSpace = "kpi.com";
    productNameWithOutWhiteSpace = productName.replace(' ', '-');
    String pdfUserGuideLink = "http://helpuserguides.s3.amazonaws.com/" + productNameWithOutWhiteSpace;
    String pdfPathForAll = "http://helpuserguides.s3.amazonaws.com/quick/Reporting_User_Guide.pdf";
%>
<html>
<head>
    <title></title>
    <style type="text/css">
        #searchPanel {
            clear: both;
            min-height: 57px;
            background: #f5f5f5;
        }

        #logo-site {
            float: left;
            margin: 0 12px;
            max-height: 53px;
        }

        #search {
            float: right;
            clear: right;
            margin: 17px 7px 0;
        }

        #search input[type="text"] {
            float: left;
            width: 188px;
            height: 20px;
            line-height: 20px;
            border: 1px solid #b5b5b5;
            margin-right: 2px;
            font-size: 13px;
        }

        #search img {
            float: right;
            border: 1px solid #b5b5b5;
        }

    </style>
    <%--<script type="text/javascript">
        window.onload = function () {
            var theWidth = 0;
            if (window.innerHeight) {
                theWidth = window.innerWidth;
            } else if (document.documentElement && document.documentElement.clientWidth) {
                theWidth = document.documentElement.clientWidth;
            } else if (document.body) {
                theWidth = document.body.clientWidth;
            }
            if (document.getElementById('toolBar') != null) {
                if (theWidth <= 1320) {
                    document.getElementById('toolBar').style.fontSize = '11px';
                } else {
                    document.getElementById('toolBar').style.fontSize = '12px';
                }
            }
        };
    </script>--%>
</head>
<body>

<div class="toolBar-panel dd-menu" id="toolBar">
    <ul class="addNewBar">
        <%
            if (!Constants.SETTINGS_PAGE.equals(request.getAttribute(Constants.ACTIVE_MENU)) &&
                    !Constants.DASHBOARD_PAGE.equals(request.getAttribute(Constants.ACTIVE_MENU)) &&
                    !Constants.DOC_MY_FOLDER.equals(request.getAttribute(Constants.ACTIVE_MENU))) {
        %>
        <li class="addNew hasSub">
            <% if (EdsContextParams.getHost().contains("telemanaged.com")) { %>
            QUICK ADD
            <%} else {%>
            <a href="/"><fmt:message key="add.new"/></a>
            <%}%>
        </li>
        <%}%>
    </ul>
    <ul class="toolBar">
        <% if (menuPermissions.contains(PermissionConstants.ACCOUNTING_MAIN_MENU)) { %>
        <% if (AuthUtils.marketplaceShowSection(request, Constants.ACCOUNTING_PAGE)) {%>
        <li class="hasSub <%=Constants.ACCOUNTING_PAGE.equals(request.getAttribute(Constants.ACTIVE_MENU)) ?
                                                                       "act" : "" %>"><a
                href="Accounting.html">
            <% if (EdsContextParams.getHost().contains("telemanaged.com")) { %>
            MONEY
            <%} else {%>
            <fmt:message key="main.accountingFinance"/>
            <%}%>
        </a>
        </li>
        <% } %>
        <% } %>
        <% if (menuPermissions.contains(PermissionConstants.HRMS_MAIN_MENU)) { %>
        <% if (AuthUtils.marketplaceShowSection(request, Constants.HRMS_PAGE)) {%>
        <li class="hasSub <%=Constants.HRMS_PAGE.equals(request.getAttribute(Constants.ACTIVE_MENU)) ? "act":""%>">
            <a href="Hrms.html">
                <% if (EdsContextParams.getHost().contains("ezyadmin.com")) { /*Temporary hack for ezyadmin ProjectManagement -> Edition Management*/ %>
                EMPLOYEES
                <%} else {%>
                <fmt:message key="main.HRMS"/>
                <%}%>
            </a>

        </li>
        <% } %>
        <% } %>

        <% if (menuPermissions.contains(PermissionConstants.CRM_MAIN_MENU)) { %>
        <% if (AuthUtils.marketplaceShowSection(request, Constants.CRM_PAGE)) {%>
        <li class="hasSub <%=Constants.CRM_PAGE.equals(request.getAttribute(Constants.ACTIVE_MENU)) ?
                                                                       "act" : "" %>"><a href="Crm.html"><fmt:message
                key="main.CRM"/></a>
        </li>
        <% } %>
        <% } %>

        <% if (menuPermissions.contains(PermissionConstants.PM_MAIN_MENU)) {%>
        <% if (AuthUtils.marketplaceShowSection(request, Constants.PROJECT_MANAGEMENT_PAGE)) {%>
        <li class="hasSub <%=Constants.PROJECT_MANAGEMENT_PAGE.equals(request.getAttribute(Constants.ACTIVE_MENU)) ? "act":""%>">
            <a href="ProjectManagement.html">
                <% if (EdsContextParams.getHost().contains("ezyadmin.com")) { /*Temporary hack for ezyadmin ProjectManagement -> Edition Management*/ %>
                Edition Management
                <%} else if (EdsContextParams.getHost().contains("telemanaged.com")) {%>
                PROJECTS
                <%} else { %>
                <fmt:message key="main.projectManagment"/></a>
            <%}%>
        </li>
        <% } %>
        <% } %>

        <% if (menuPermissions.contains(PermissionConstants.REPORTING_MAIN_MENU)) { %>
        <% if (AuthUtils.marketplaceShowSection(request, Constants.REPORTING_PAGE)) {%>
        <li class="hasSub <%=Constants.REPORTING_PAGE.equals(request.getAttribute(Constants.ACTIVE_MENU)) ?
                                                                       "act" : "" %>"><a
                href="Reporting.html"><fmt:message key="main.reporting"/></a>
        </li>

        <% } %>
        <% } %>

        <% if (menuPermissions.contains(PermissionConstants.LOGISTICS_MAIN_MENU)) { %>
        <% if (AuthUtils.marketplaceShowSection(request, Constants.LOGISTICS_PAGE)) {%>
        <li class="hasSub <%=Constants.LOGISTICS_PAGE.equals(request.getAttribute(Constants.ACTIVE_MENU)) ? "act" : "" %>">
            <a href="Logistics.html"><fmt:message key="main.logistics"/></a>
        </li>
        <% } %>
        <% } %>


        <% if (menuPermissions.contains(PermissionConstants.PAYROLL_MAIN_MENU)) { %>
        <% if (!(
                CompanyConstants.C25608.equals(String.valueOf(request.getAttribute(Constants.COMPANY_ID)))
               // ||  CompanyConstants.C8687.equals(String.valueOf(request.getAttribute(Constants.COMPANY_ID)))
        )) { %>


        <% if (AuthUtils.marketplaceShowSection(request, Constants.PAYROLL_PAGE) && !EdsContextParams.getHost().contains("app.tjilo.com")) {%>
        <li class="hasSub <%=Constants.PAYROLL_PAGE.equals(request.getAttribute(Constants.ACTIVE_MENU)) ?
                                                                       "act" : "" %>"><a
                href="Payroll.html"><fmt:message key="main.payroll"/></a>
        </li>
        <% } %>
        <% } %>
        <% } %>

        <% if (menuPermissions.contains(PermissionConstants.DOCUMENTS_MAIN_MENU)) { %>
        <% if (AuthUtils.marketplaceShowSection(request, Constants.DOC_MY_FOLDER)) {%>
        <li class="hasSub  <%=(Constants.DOC_MY_FOLDER.equals(request.getAttribute(Constants.ACTIVE_MENU)) ||
                                                                                   Constants.DOC_INTRANET.equals(request.getAttribute(Constants.ACTIVE_MENU))) ?
                                                                       "act" : "" %>">
            <a href="Documents.html">
                <% if (
                        ( //CompanyConstants.C8687.equals(String.valueOf(request.getAttribute(Constants.COMPANY_ID))) ||

                        CompanyConstants.C25608.equals(String.valueOf(request.getAttribute(Constants.COMPANY_ID))))) {%>
                Intranet
                <%} else if (EdsContextParams.getHost().contains("telemanaged.com")) { %>
                DOCS REPORTS
                <%} else {%>
                <span class="wfm-my-toolitem-text"> <fmt:message key="main.documents"/></span>
                <%} %></a>

        </li>
        <% } %>
        <% } %>


        <% String trainingCenterEnabled = (String) request.getAttribute(Constants.TRAINING_CENTER_ENABLED);%>
        <% if ("true".equals(trainingCenterEnabled)) { %>
        <% if (menuPermissions.contains(PermissionConstants.TC_MAIN_MENU)) { %>
        <% if (AuthUtils.marketplaceShowSection(request, Constants.TRAINING_CENTER_PAGE)) {%>
        <li class="hasSub <%=Constants.TRAINING_CENTER_PAGE.equals(request.getAttribute(Constants.ACTIVE_MENU)) ?
                                                                       "act" : "" %>"><a
                href="TrainingCenter.html"><fmt:message key="main.trainingCenter"/></a>
        </li>

        <% } %>
        <% } %>
        <% } %>
        <% if (!isClient) { %>
        <li class="hasSub"><a href=""><fmt:message key="frontendmain.help"/> </a></a>
            <ul>
                <li><a href="<%=pdfUserGuideLink%>/ProjectManagement.pdf"> <fmt:message
                        key="main.projectManagementGuide"/> </a></li>
                <li><a href="<%=pdfUserGuideLink%>/HRMS.pdf"> <fmt:message key="main.hRMSGuide"/> </a></li>
                <li><a href="<%=pdfUserGuideLink%>/CRM.pdf"> <fmt:message key="main.cRMGuide"/> </a></li>
                <li><a href="<%=pdfUserGuideLink%>/Accounting.pdf"> <fmt:message
                        key="main.accountingFinanceGuide"/> </a></li>
                <li><a href="<%=pdfUserGuideLink%>/Payroll.pdf"> <fmt:message key="main.payrollGuide"/> </a></li>
                <li><a href="<%=pdfPathForAll%>"> <fmt:message key="main.reportingGuide"/></a></li>
            </ul>
        </li>
        <%}%>
    </ul>
    <ul class="userMenu">
        <li class="accountSettings">
            <a href=""><%=request.getAttribute(Constants.USER_FULLNAME)%>
            </a>
            <ul>
                <% if (menuPermissions.contains(PermissionConstants.SETTINGS_MAIN_MENU)) { %>
                <li><a href="/Settings.html"><fmt:message key="main.settings"/></a></li>
                <%}%>
                <%--<% if (ServerUtils.isUserHasRole(request, Constants.ADMIN)) { %>--%>
                <% if (menuPermissions.contains(PermissionConstants.MYACCOUNT_MAIN_MENU)) { %>
                <li class="hasSub <%=Constants.MYACCOUNT_PAGE.equals(request.getAttribute(Constants.ACTIVE_MENU)) ?
                                                                       "act" : "" %>"><a
                        href="Myaccount.html"><fmt:message key="main.myAccount"/></a>
                </li>
                <%}%>
                <li><a class="gwt-Anchor" href="logout.html"><fmt:message key="main.logOut"/></a></li>
            </ul>
        </li>
    </ul>
</div>
<%--<div id="searchPanel">--%>
    <%--<a id="logo-site" href="#"><%String logo = (String) request.getAttribute(Constants.LOGO_URL);%>--%>
        <%--<% if (logo != null && !"".equals(logo)) {%>--%>
        <%--<img src="<%=logo%>" alt="<%=request.getAttribute(Constants.COMPANY_NAME)%>"/>--%>
        <%--<%} else {%>--%>
        <%--<img src="${logoImage}" alt="<%=request.getAttribute(Constants.COMPANY_NAME)%>"/>--%>
        <%--<%}%>--%>
        <%--&lt;%&ndash;<img src="/customisation/kpi/images/kpilogo.png">&ndash;%&gt;--%>

    <%--</a>--%>
<%--</div>--%>

</body>
</html>