<%@ page import="com.edatasite.workforce.gwt.core.server.app.BrowserSupportUtils" %>
<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%@ page import="org.springframework.web.servlet.support.RequestContext" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%  String hostName = (request.getAttribute("hostName") != null)? request.getAttribute("hostName").toString():request.getServerName();
    RequestContext rc = new RequestContext(request);
    boolean isRussian = rc != null && rc.getLocale() != null && "ru".equals(rc.getLocale().getLanguage());
    boolean isRussianHost = request.getHeader("host") != null && request.getHeader("host").toLowerCase().contains(".ru") || request.getParameterMap().get("locale") != null && request.getParameter("locale").toLowerCase().contains("ru");
    boolean isSupportedIE8 = BrowserSupportUtils.isSupportedIE8(request.getHeader("user-agent"));
    String facebookAppID = EdsContextParams.getFacebookAppID(request.getServerName());
    boolean isCompanyDomain = EdsContextParams.getCompanyDomain(request.getServerName());
%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>
    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>

    <!-- Yandex.Metrika counter -->
    <%--<script type="text/javascript"> (function (d, w, c) {
        (w[c] = w[c] || []).push(function () {
            try {
                w.yaCounter45824055 = new Ya.Metrika2({id: 45824055, clickmap: true, trackLinks: true, accurateTrackBounce: true, webvisor: true, trackHash: true});
            } catch (e) {
            }
        });
        var n = d.getElementsByTagName("script")[0], s = d.createElement("script"), f = function () {
            n.parentNode.insertBefore(s, n);
        };
        s.type = "text/javascript";
        s.async = true;
        s.src = "https://mc.yandex.ru/metrika/tag.js";
        if (w.opera == "[object Opera]") {
            d.addEventListener("DOMContentLoaded", f, false);
        } else {
            f();
        }
    })(document, window, "yandex_metrika_callbacks2"); </script>
    <noscript>
        <div><img src="https://mc.yandex.ru/watch/45824055" style="position:absolute; left:-9999px;" alt=""/></div>
    </noscript>--%>
    <!-- /Yandex.Metrika counter -->

    <link rel="stylesheet" href="/customisation/preprod.kpi.com/style.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/master.css"/>
    <link href="//app.kpi.com/customisation/kpi.com/images/favicon.ico" rel="shortcut icon">
    <!--[if lt IE 9]>
    <script type="text/javascript" src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <!-- <meta http-equiv="X-UA-Compatible" content="chrome=1" /> -->
    <!-- for ie -->

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/fix-ie.css"/>
    <![endif]-->

    <script type="text/javascript" src="/customisation/preprod.kpi.com/scripts/jquery.js"></script>
    <script type="text/javascript" src="/customisation/preprod.kpi.com/scripts/common.js"></script>

    <tiles:insertAttribute name="style" ignore="true"/>

</head>

<body>

<!-- begin wrapper -->
<div id="wrapper" class="land-CRM page-thankYou">

<!--begin header-->
    <header class="extra-margin">
        <div class="ZoneIn">

            <!-- begin logo site -->
            <a id="logo" href="/">
                <%--<img title="KPI.com" alt="KPI.com" src="/customisation/${productNameLower}/images/logo-big.png">--%>
                <img title="${productName}" alt="${productName}" src="${logoImage}">
            </a>
            <!-- end logo site -->
            <div class="clearBox"></div>
        </div>
    </header>
<!--END header-->

<!--begin #cover-->
<div id="cover">

    <!--begin #content-->
    <div id="content" class="full-main">


        <tiles:insertAttribute name="body" ignore="false"/>

        <!--begin footer-->
        <footer class="welcomePage-footer">
            <table class="footer-nav">
                <tbody>
                <tr>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-1 footNavIcon">
                                <% if (hostName != null && hostName.contains("activira")) { %>
                                <fmt:message key="template.projectResourceManagement"/>
                                <%}else{
                                %>
                                <a href="//${helpHost}/project-management">
                                    <fmt:message key="template.projectResourceManagement"/>
                                </a>
                                <%
                                }%>

                            </h2>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-2 footNavIcon">
                                <% if (hostName != null && hostName.contains("activira")) { %>
                                <fmt:message key="template.crmAndSales"/>
                                <%}else{
                                %>
                                <a href="//${helpHost}/crm">
                                    <fmt:message key="template.crmAndSales"/>
                                </a>
                                <%
                                    }%>

                            </h2>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-3 footNavIcon">
                                <% if (hostName != null && hostName.contains("activira")) { %>
                                <fmt:message key="template.hrAndPayroll"/>
                                <%}else{
                                %>
                                <a href="//${helpHost}/hrms">
                                    <fmt:message key="template.hrAndPayroll"/>
                                </a>
                                <%
                                    }%>

                            </h2>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-4 footNavIcon">

                                <% if (hostName != null && hostName.contains("activira")) { %>
                                <fmt:message key="template.financialsAndInventory"/>
                                <%}else{
                                %>
                                <a href="//${helpHost}/accounting">
                                    <fmt:message key="template.financialsAndInventory"/>
                                </a>
                                <%
                                    }%>
                            </h2>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-6 footNavIcon">
                                <% if (hostName != null && hostName.contains("activira")) { %>
                                <fmt:message key="template.cmsAndAddOns"/>
                                <%}else{
                                %>
                                <a href="//${helpHost}/add-on">
                                    <fmt:message key="template.cmsAndAddOns"/>
                                </a>
                                <%
                                    }%>

                            </h2>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-5 footNavIcon">
                                <% if (hostName != null && hostName.contains("activira")) { %>
                                <fmt:message key="template.reportingAndDashboard"/>
                                <%}else{
                                %>
                                <a href="//${helpHost}/reporting">
                                    <fmt:message key="template.reportingAndDashboard"/>
                                </a>
                                <%
                                    }%>

                            </h2>
                        </nav>
                    </td>
                </tr>
                </tbody>
            </table>





            <div class="clearBox"></div>
        </footer>

        <!--END footer-->

    </div>
    <!--END #content-->

</div>
<!--END #cover-->


</div>
<!-- END #wrapper -->

<tiles:insertAttribute name="script" ignore="true"/>

</body>
</html>

