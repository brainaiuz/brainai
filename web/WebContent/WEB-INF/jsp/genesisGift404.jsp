<%--  User: dilshod madrahimov --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title">
        <fmt:message key="pagenotfound.pageNotFound"/>
    </tiles:putAttribute>
    <tiles:putAttribute name="style">

        <style type="text/css">
            #goog-wm {
            }

            #goog-wm h3.closest-match {
            }

            #goog-wm h3.closest-match a {
            }

            #goog-wm h3.other-things {
            }

            #goog-wm ul li {
            }

            #goog-wm li.search-goog {
                display: block;
            }
        </style>

    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <%--${productName}--%>
        <div id="index-page" style="width:860px; margin:10px auto; padding:10px;">
            <h2 class="title"><fmt:message key="pagenotfound.thePageYouRequestedCouldNotbeFound"/></h2>

            <p><fmt:message key="pagenotfound.pleaseСheckTheAddress"/>&nbsp;<a href="http://app.genesis-gifts.com/">genesis-gifts.com</a>,
                <fmt:message key="pagenotfound.tryVisitingThe"/>&nbsp;
                <a href="http://app.genesis-gifts.com/">
                    genesis-gifts.com home page
                </a>.
                <fmt:message key="pagenotfound.alsoYouMayFind"/></p>
            <script type="text/javascript">
                var GOOG_FIXURL_LANG = 'en_GB';
                var GOOG_FIXURL_SITE = 'http://app.genesis-gifts.com/';
            </script>
            <script type="text/javascript" src="https://linkhelp.clients.google.com/tbproxy/lh/wm/fixurl.js"></script>

            <p><strong><fmt:message key="pagenotfound.email"/></strong>
                <a href="mailto:dxbsales@genesis-gifts.com">dxbsales@genesis-gifts.com (Dubai)</a><br>
                <a href="mailto:dxbsales@genesis-gifts.com">auhsales@genesis-gifts.com (Abu Dhabi)</a><br>
                <a href="mailto:dxbsales@genesis-gifts.com">usasales@genesis-gifts.com (USA)</a><br>
                <a href="mailto:dxbsales@genesis-gifts.com">mumsales@genesis-gifts.com (India)</a><br>
            </p>

            <p><strong><fmt:message key="pagenotfound.phone"/></strong>
                +971 4 431 1432 (Dubai)<br>
                +971 2 665 6420 (Abu Dhabi)<br>
                +1 281 265 2756 (USA)<br>
                +91 22 2432 4427 (India)<br>
            </p>

        </div>


    </tiles:putAttribute>

</tiles:insertDefinition>