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
<%@ taglib prefix="title" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% String hostName = (request.getAttribute("hostName") != null) ? request.getAttribute("hostName").toString() : request.getServerName(); %>



<tiles:insertDefinition name="frontEndLayoutNew">

<tiles:putAttribute name="title">
    <fmt:message key="index.titleWorkforceTrack"/> ${helpHost}
</tiles:putAttribute>
<tiles:putAttribute name="style">

    <SCRIPT type=text/javascript src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></SCRIPT>

    <SCRIPT type=text/javascript>
        jQuery(function(){

            jQuery('input[placeholder], textarea[placeholder]').placeholder();


        });
    </SCRIPT>

    <meta name="google-site-verification" content="jjw1ZnV5AryaQZs-KB64fCH3EpvkT94GKJAoFvLFzbU"/>
    <meta name="msvalidate.01" content="358DD608AB06D6ED2E842AF53CAFFB9E"/>
    <META name="y_key" content="d766be7156d8deef">

</tiles:putAttribute>
<tiles:putAttribute name="body">

<%----------------------%>
<%--- BEGIN content page---%>
<div id="contentPlace" class="clear-block">

    <!-- Start MAIN -->
    <div id="main">
        <ul id = "breadCrump" class="breadCrump">
            <li><a href="http://${helpHost}/">${helpHost}</a></li>
            <li> › <fmt:message key="index.login"/></li>
        </ul>

        <!-- START LOGIN FORM -->

                <section>
                    <form action="/mainLogin" method="post">

                    <h1 class="title"><fmt:message key="index.login"/></h1>

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

                                <c:if test="${hostName != 'aws.alfursanrecruitment.ae' && hostName != 'login.alfursanrecruitment.ae'
                                && hostName != 'app.genesis-gifts.com' && hostName != 'aws.genesis-gifts.com'}">
                                    <div class="alterLogin"><fmt:message key="index.or"/></div>

                                    <center>
                                        <ul class="useSocialBookMarking">
                                            <li class="mini-icons"></li>

                                            <li>
                                                <div class="fb-login-button" data-max-rows="1"
                                                     data-label="Facebook" data-size="large"
                                                     data-show-faces="false" data-auto-logout-link="false"
                                                     onlogin="fbRedirect()"
                                                     data-scope="basic_info, read_stream, email"></div>
                                            </li>
                                        </ul>
                                            <%--<script type="IN/Login" data-label="LinkedIn"></script>--%>
                                    </center>

                                    <div class="c">
                                        <a id="forgot" href="/forgot/forgotPassword.html"> <fmt:message
                                                key="index.forgotPassword"/> </a>
                                                    <span id="signupLink">|
                                                    <a href="/signup/freeSignup.html"><fmt:message
                                                            key="index.signUp"/> </a>
                                                        </span>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    <div class="clearBox"></div>
            </form>
                </section>
        <!-- END LOGIN FORM -->
        <!--Begin Browser Type-->
        <div id="userAgentHelp" style="margin:0 0 30px; text-align:center;">

            <script type="text/javascript">
                document.getElementById("login").focus();//Initially focus on login textbox
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
                        text += "<div style='border:1px solid #5d5d5d;width:630px;margin:0 auto;padding:10px;'>";
                        text += "${productName} ";
                        text += "<fmt:message key="index.isBestSupportedInChromeBrowser"/> ";
                        text += "<fmt:message key="index.youCanDownloadLatestVersionFrom"/> ";
                        text += "<a href='http://www.google.com/chrome' target='_blank' style='color:#205FAB;'> <fmt:message key="index.clickHere"/></a>";
                        text += "</div>";
                    } else {
                        text += "<div style='border:1px solid #5d5d5d;width:600px;margin:10px auto;padding:10px;'>";
                        text += "<fmt:message key="index.youAreUsingAnOldVersionOfFirefoxWhichIsNoLongerSupportedBy"/> ";
                        text += " ${productName}. ";
                        text += "<fmt:message key="index.pleaseUpgradeToNewerVersionOrTry"/> ";
                        text += "<a href='http://www.google.com/chrome' target='_blank' style='color:#205FAB;'> <fmt:message key="index.googleChrome"/></a> ";
                        text += "<fmt:message key="index.forBetterExperience"/> ";
                        text += "</div>";
                    }
                } else {
                    text += "<div style='border:1px solid #5d5d5d;width:630px;margin:0 auto;padding:10px;'>";
                    text += "${productName} ";
                    text += "<fmt:message key="index.isBestSupportedInChromeBrowser"/> ";
                    text += "<fmt:message key="index.youCanDownloadLatestVersionFrom"/> ";
                    text += "<a href='http://www.google.com/chrome' target='_blank' style='color:#205FAB;'> <fmt:message key="index.clickHere"/></a>";
                    text += "</div>";
                }
                document.getElementById("userAgentHelp").innerHTML = text;
            </script>
        </div>

        <!--End Browser Type-->

    </div>
    <!--End #main -->


</div>
<%----END content page----%>
<%----------------------%>

</tiles:putAttribute>
<tiles:putAttribute name="script">


    <script type="text/javascript">

    function replaceT(obj){
    var newO=document.createElement('input');
    newO.setAttribute('type','password');
    newO.setAttribute('name',obj.getAttribute('name'));
    obj.parentNode.replaceChild(newO,obj);
    newO.focus();
    }

    function fbRedirect() {
        FB.getLoginStatus(function(response) {
            if (response.status === 'connected') {
                // the user is logged in and has authenticated your
                // app, and response.authResponse supplies
                // the user's ID, a valid access token, a signed
                // request, and the time the access token
                // and signed request each expire
                var uid = response.authResponse.userID;
                var accessToken = response.authResponse.accessToken;
                window.location = 'facebookLogin?access_token=' + accessToken + '&uid=' + uid;
            } else if (response.status === 'not_authorized') {
                // the user is logged in to Facebook,
                // but has not authenticated your app
            } else {
                // the user isn't logged in to Facebook.
            }
        });
    }
    </script>
    <%--End of Drupal Content--%>
</tiles:putAttribute>
</tiles:insertDefinition>