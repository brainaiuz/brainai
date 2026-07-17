<%@ page import="com.edatasite.workforce.gwt.core.client.ui.Constants" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--
  Created by IntelliJ IDEA.
  User: Ilhombek
  Date: 5/13/11
  Time: 7:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<tiles:insertDefinition name="welcomePageLayout">
<tiles:putAttribute name="title">
    <fmt:message key="changepassword.welcomeTo"/> &nbsp; ${productName}
</tiles:putAttribute>
<tiles:putAttribute name="style">
    <link href="/landing/css/welcomepage.css" rel="stylesheet" type="text/css">
    <link rel="shortcut icon" href="/customisation/${productNameLower}/images/favicon.ico" type="image/x-icon"/>

</tiles:putAttribute>
<tiles:putAttribute name="body">
    <script type="text/javascript">

        setTimeout("redirect()", 10000);

        function getCookie(c_name) {
            var i, x, y, ARRcookies = document.cookie.split(";");
            for (i = 0; i < ARRcookies.length; i++) {
                x = ARRcookies[i].substr(0, ARRcookies[i].indexOf("="));
                y = ARRcookies[i].substr(ARRcookies[i].indexOf("=") + 1);
                x = x.replace(/^\s+|\s+$/g, "");
                if (x == c_name) {
                    return unescape(y);
                }
            }
        }

        function redirect() {
            var section = getCookie('SECTION_HTML');
            var path = "<%=Constants.DEFAULT_SECTION%>.html";
            //alert(section);
            if (section != null && section.match("html") != null) {
                path = section;
            }
            window.location = '/' + path;
        }
        var isInIFrame = (window.location != window.parent.location) ? true : false;
        if (isInIFrame) {
            parent.location = document.location;
        }
    </script>

    <script type="text/javascript">
        if (!window.mstag) mstag = {loadTag:function () {
        }, time:(new Date()).getTime()};

    </script>


    <div id="main">
<div class="extra-padding">

    <h1 class="title"><img alt="" class="icon" src="/customisation/${productNameLower}/images/tick.png">

        Welcome to ${helpHost}</h1>
    <div class="spec-bot"></div>
    <ul class="image-list">
        <li>  ${productName} &nbsp;<fmt:message key="welcomepage.accountIn10SecondsOrPleaseClick"/>
            <a href="javascript:redirect()" class="aclass"> <fmt:message key="welcome.here"/> </a> <fmt:message key="welcome.youWillBeRedirected"/>

        </li>

    </ul>

    <div class="overhide">
        <div class="half left">
            <h2 class="title-4"> <fmt:message key="welcome.questions"/> </h2>

            <dl class="addressInfo overhide">
                <dt class="left"> <fmt:message key="pagenotfound.email"/> </dt>
                <dd> <a href="mailto:${email}"> ${email} </a> </dd>

                <dt class="left"> <fmt:message key="pagenotfound.skype"/> </dt>
                <dd> ${skype} </dd>

                <dt class="left"><fmt:message key="pagenotfound.phone"/></dt>
                <dd> ${supportPhone} </dd>
            </dl>

            <p><fmt:message key="welcome.kindRegards"/> <br> ${productName} <fmt:message key="welcomePage.supportTeam"/> </p>

        </div>

        <div class="half right">
            <h2 class="title-4"><img alt="" class="icon" src="/customisation/${productNameLower}/images/info.jpg">Info</h2>
            <p>
                <fmt:message key="welcomepage.pleaseFeelFree"/> ${productName} <fmt:message key="welcomepage.thatEnablesYou"/>

            </p>

        </div>
    </div>


</div>
</div>

</tiles:putAttribute>

<tiles:putAttribute name="script">

</tiles:putAttribute>


</tiles:insertDefinition>