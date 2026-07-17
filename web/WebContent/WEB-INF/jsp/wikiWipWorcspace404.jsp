<%--  User: Djuraev   Date: 4/1/14 --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title">
        <fmt:message key="pagenotfound.pageNotFound"/>
    </tiles:putAttribute>
    <tiles:putAttribute name="style">

        <style type="text/css">
            #goog-wm li.search-goog {
                display: block;
            }
        </style>


        <script type="text/javascript">
            var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
            document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
        </script>
        <script type="text/javascript">
            try {
                var pageTracker = _gat._getTracker("UA-355982-15");
                pageTracker._trackPageview("/404.html?page=" + document.location.pathname + document.location.search + "&from=" + document.referrer);
            } catch (err) {
            }
        </script>

    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <%--${productName}--%>
        <div id="index-page" style="width:860px; margin:10px auto; padding:10px;">
            <h2 class="title"><fmt:message key="pagenotfound.thePageYouRequestedCouldNotbeFound"/></h2>

            <p><fmt:message key="pagenotfound.pleaseСheckTheAddress"/>&nbsp;<a
                    href="http://vipworkspace.com">vipworkspace.com</a>,
                <fmt:message key="pagenotfound.tryVisitingThe"/>&nbsp;
                <a href="http://vipworkspace.com"> vipworkspace.com home page</a>.
                <fmt:message key="pagenotfound.alsoYouMayFind"/></p>
            <script type="text/javascript">
                var GOOG_FIXURL_LANG = 'en_GB';
                var GOOG_FIXURL_SITE = 'http://vipworkspace.com/';
            </script>
            <script type="text/javascript" src="https://linkhelp.clients.google.com/tbproxy/lh/wm/fixurl.js"></script>

            <p><strong><fmt:message key="pagenotfound.email"/></strong> <a href="mailto:support@vipworkspace.com">support@vipworkspace.com</a>
            </p>

            <p><strong><fmt:message key="pagenotfound.phone"/></strong> +407-965-1765</p>

        </div>


    </tiles:putAttribute>

</tiles:insertDefinition>