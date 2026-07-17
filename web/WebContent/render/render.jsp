<!doctype html>
<%@ page import="com.edatasite.workforce.gwt.website.client.rpc.pages.settings.PageSettings" %>
<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%--
  Created by IntelliJ IDEA.
  User: Ulugbek Normatov
  Date: Nov 9, 2011
  Time: 5:44:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="wfp" uri="/wfp-custom-tags" %>
<% PageSettings pageSettings = (PageSettings) request.getAttribute("pageSettings"); %>
<% String productName = pageSettings.getProductName(); %>
<%= pageSettings.getSiteLayout().getDocType() != null ? pageSettings.getSiteLayout().getDocType() : "" %>
<%--<% if ("ar".equals(pageSettings.getLanguage()) && pageSettings.getCompanyDefaultWebSite()) {%>--%>
<%--<html dir="rtl">--%>
<%--<%} else {%>--%>
<html>
<%--<%}%>--%>
<head>

    <title>
        <% if (EdsContextParams.getHostname() != null && EdsContextParams.getHostname().contains("telemanaged.com")) {%>
        TELEHOME - <%=productName%>
        <%} else if (productName != null && !"".equals(productName.trim())) {%>
        <%= pageSettings.getTitle() + " - " + productName%>
        <%} else {%>
        <%= pageSettings.getTitle() %>
        <%}%>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=8"/>

    <%if (pageSettings.getMetaRobots() != null && !pageSettings.getMetaRobots().equals("")) {%>
    <meta name="robots" content="<%= pageSettings.getMetaRobots() %>"/>
    <%}%>
    <%if (pageSettings.getMetaAuthor() != null && !pageSettings.getMetaAuthor().equals("")) {%>
    <meta name="author" content="<%= pageSettings.getMetaAuthor() %>"/>
    <%}%>
    <%if (pageSettings.getMetaContentRights() != null && !pageSettings.getMetaContentRights().equals("")) {%>
    <meta name="copyright" content="<%= pageSettings.getMetaContentRights() %>"/>
    <%}%>
    <%if (pageSettings.getMetaDescription() != null && !pageSettings.getMetaDescription().equals("")) {%>
    <meta name="description" content="<%= pageSettings.getMetaDescription() %>"/>
    <%}%>
    <%if (pageSettings.getMetaKeyword() != null && !pageSettings.getMetaKeyword().equals("")) {%>
    <meta name="keywords" content="<%= pageSettings.getMetaKeyword() %>"/>
    <%}%>

    <% if (pageSettings.getCompanyDefaultWebSite()) {%>
    <link rel="shortcut icon" href="/customisation/<%= productName.toLowerCase() %>/images/favicon.ico"
          type="image/x-icon"/>
    <%} else {%>
    <link rel="shortcut icon" href="/wfp/templates/<%= productName.toLowerCase() %>/images/favicon.ico"
          type="image/x-icon"/>
    <%}%>


    <%= pageSettings.getSiteLayout().getHead() != null ? pageSettings.getSiteLayout().getHead() : ""%>

    <% if (pageSettings.getSiteLayout().getCssPath() != null) {%>
    <link rel="stylesheet" href="<%= pageSettings.getSiteLayout().getCssPath()%>" type="text/css"/>
    <%} %>
    <%if (pageSettings.getSiteLayout().isUseSystemTheme()) {%>
    <link rel="stylesheet" href="<%= pageSettings.getSystemTheme()%>" type="text/css"/>
    <%}%>


    <script type="text/javascript">
        var CKEDITOR_BASEPATH = '/wfp/templates/cooconnect/images';
    </script>

    <script type="text/javascript">

        var injectorsHolder = new Object();

        function executeInjections() {
            for (var i in injectorsHolder) {
                injectorsHolder[i]();
            }
        }
    </script>
</head>
<%if (pageSettings.getSiteLayout().isUseSystemTheme()) {%>
<body class="<%=pageSettings.getSystemThemeName()%>">
    <%} else {%>
<body>
<%}%>

<%=pageSettings.getSiteLayout().hasExternalModuleSettings() ? "<div id=\"wfm-module-settings\" style='display:none;'>" + pageSettings.getSiteLayout().getExternalModuleSettings() + "</div>" : ""%>
<div id="temp_unvisible_div" style='display:none;'></div>
<wfp:PagaManagerTag pageSettings="<%= pageSettings%>"></wfp:PagaManagerTag>

</body>


</html>