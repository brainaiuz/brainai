<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 25.11.2010
  Time: 20:01:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>--%>


<tiles:insertDefinition name="frontEndLayoutNew">
<%--<tiles:putAttribute name="title" value="Sign in or sign up to WorkforceTrack"/>--%>
<tiles:putAttribute name="title">
    <fmt:message key="index.titleWorkforceTrack"/> ${productName}
</tiles:putAttribute>
<tiles:putAttribute name="style">

    <link href="/customisation/preprod.kpi.com/style.css" type="text/css" rel="stylesheet">
    <script src="/customisation/preprod.kpi.com/scripts/jquery.js?ver=1.7.2" type="text/javascript"></script>
    <%--<script src="//kpi.com/sites/all/themes/wft/jquery.cookie.js"></script>--%>
    <SCRIPT type=text/javascript src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></SCRIPT>
    <SCRIPT type=text/javascript src="/customisation/kpi.com/jquery.placeholder.min.js"></SCRIPT>

    <SCRIPT type=text/javascript>
        jQuery(function(){
            jQuery('input[placeholder], textarea[placeholder]').placeholder();
        });
    </SCRIPT>

    <meta name="google-site-verification" content="jjw1ZnV5AryaQZs-KB64fCH3EpvkT94GKJAoFvLFzbU"/>
    <meta name="msvalidate.01" content="358DD608AB06D6ED2E842AF53CAFFB9E"/>
    <META name="y_key" content="d766be7156d8deef">
    <%--End of Drupal Head Content--%>


</tiles:putAttribute>
<tiles:putAttribute name="body">

    <div id="contentPlace" class="clear-block">
    <!-- Start MAIN -->
    <div id="main">
        <ul class="breadCrump">
            <li><a href="http://${helpHost}/">${helpHost}</a></li>
            <li> › <fmt:message key="index.login"/></li>
        </ul>
        <!-- START LOGIN FORM -->
        <section>
            <h1 class="title"><span><fmt:message key="index.login"/></span></h1>

            <form action="/mainLogin" method="post">
                <p class="c req">${error}</p>

                <div class="loginBox">
                    <div class="boxIn">
                        <label for="login">
                            <input id="login" type="text" name="USER_NAME" value=""
                                   placeholder="<fmt:message key="index.userName"/>" class="txt" >
                        </label>

                        <label for="pass">
                            <input id="pass" type="password" name="USER_PASSWORD" value="" placeholder="<fmt:message key="index.password"/>" class="txt" >
                        </label>
                        <div id="recaptcha"></div>

                        <input style="text-indent: 1px;" type="submit" value="<fmt:message key="index.login"/>" class="btnLogIn btn">

                        <div class="alterLogin"> <fmt:message key="index.or"/> </div>

                        <ul class="useSocialBookMarking">
                            <li><a id = "liveid" href="/liveidauth" class="mini-icons mini-livemessager" style="display:none">Live Messanger</a></li>
                            <li><a href="/check" class="mini-icons mini-google">Google</a></li>
                            <li><a href="/check?ID_PROVIDER=https://me.yahoo.com" class="mini-icons mini-yahoo">Yahoo</a></li>
                            <li><a href="/enterGoogleDomain.html" class="mini-icons mini-google-apps">Google Apps</a></li>
                        </ul>

                        <div class="c">
                            <a href="/forgot/forgotPassword.html"> <fmt:message key="index.forgotPassword"/>  </a>
                            |
                            <a href="/signup/freeSignup.html"><fmt:message key="index.signUp"/> </a>
                        </div>
                    </div>
                </div>
                <div class="clearBox"></div>

            </form>

            <!--[if lte IE 8]>
            <span class="bgAngle topLeft"></span>
            <span class="bgAngle topRight"></span>
            <span class="bgAngle bottomLeft"></span>
            <span class="bgAngle bottomRight">&nbsp;</span>
            <![endif]-->
        </section>
        <div class="shadowLoginForm"></div>
        <!-- END FREE LOGIN FORM -->

    </div>

    <div id="userAgentHelp" style="text-align:center;">

        <script type="text/javascript">
            uAgent = navigator.userAgent.toLowerCase();
            var chromeName = "chrome/";
            var chromeVersion = 5.0;
            var firefoxName = "firefox/";
            var firefoxVersion = 4.0;
            text = "";
            if (uAgent.indexOf("chrome") != -1) {
                startL = uAgent.indexOf("chrome/") + chromeName.length;
                uAgent += " ";
                endL = uAgent.indexOf(" ", startL);
                versionBrowser = uAgent.substring(startL, endL);
                versionBrowser = versionBrowser.substring(0, versionBrowser.indexOf(".") + 2);
                if (Number(versionBrowser) >= Number(chromeVersion)) {
                    text += "<div style='height: 35px;'>";
                } else {
                    text += "<div style='border:1px solid #5d5d5d;width:650px;margin:10px auto;padding:10px;'>";
                    text += "<fmt:message key="index.notePleaseUseGoogleChromeVersion"/> ";
                    text += "<fmt:message key="index.youCanDownloadLatestVersionFrom"/> ";
                    text += "<a href='http://www.google.com/chrome' target='_blank' style='color:#205FAB;'> <fmt:message key="index.clickHere"/></a>";
                    text += "</div>";
                }
            } else if (uAgent.indexOf("firefox") != -1) {
                startL = uAgent.indexOf("firefox/") + firefoxName.length;
                uAgent += " ";
                endL = uAgent.indexOf(" ", startL);
                versionBrowser = uAgent.substring(startL, endL);
                versionBrowser = versionBrowser.substring(0, versionBrowser.indexOf(".") + 2);
                if (Number(versionBrowser) >= Number(firefoxVersion)) {
                    text += "<div style='border:1px solid #5d5d5d;width:600px;margin:10px auto;padding:10px;'>";
                    text += "${productName} ";
                    text += "<fmt:message key="index.isBestSupportedInChromeBrowser"/> ";
                    text += "<fmt:message key="index.youCanDownloadLatestVersionFrom"/> ";
                    text += "<a href='http://www.google.com/chrome' target='_blank' style='color:#205FAB;'> <fmt:message key="index.clickHere"/></a>";
                    text += "</div>";
                } else {
                    text += "<div style='border:1px solid #5d5d5d;width:650px;margin:10px auto;padding:10px;'>";
                    text += "<fmt:message key="index.youAreUsingAnOldVersionOfFirefoxWhichIsNoLongerSupportedBy"/> ";
                    text += " ${productName}. ";
                    text += "<fmt:message key="index.pleaseUpgradeToNewerVersionOrTry"/> ";
                    text += "<a href='http://www.google.com/chrome' target='_blank' style='color:#205FAB;'> <fmt:message key="index.googleChrome"/></a> ";
                    text += "<fmt:message key="index.forBetterExperience"/> ";
                    text += "</div>";
                }
            } else {
                text += "<div style='border:1px solid #5d5d5d;width:600px;margin:10px auto;padding:10px;'>";
                text += "${productName} ";
                text += "<fmt:message key="index.isBestSupportedInChromeBrowser"/> ";
                text += "<fmt:message key="index.youCanDownloadLatestVersionFrom"/> ";
                text += "<a href='http://www.google.com/chrome' target='_blank' style='color:#205FAB;'> <fmt:message key="index.clickHere"/></a>";
                text += "</div>";
            }
            document.getElementById("userAgentHelp").innerHTML = text;
        </script>
    </div>



    <div style="height:20px; position:center "></div>
    </div>


</tiles:putAttribute>

<tiles:putAttribute name="script">





    <%--End of Drupal Content--%>
</tiles:putAttribute>
</tiles:insertDefinition>