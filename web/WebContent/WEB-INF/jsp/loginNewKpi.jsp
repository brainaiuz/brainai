<%--
  Created by IntelliJ IDEA.
  User: Fatxulla Nigmatjonov
  Date: 14.11.2014
  Time: 12:32:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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

    <div class="breadcrumb">
        <ol class="container">
            <li><a href="http://${helpHost}/">${helpHost}</a></li>
            <li> › <fmt:message key="index.login"/></li>
        </ol>
    </div>


    <div class="container">
        <article class="post-9403 page type-page status-publish hentry" id="post-9403">
            <form role="form" action="/mainLogin" method="post">

                <h1 class="page_title"><fmt:message key="index.login"/></h1>

                <p class="c">${error}</p>

                <div class="row products_review bottomField">

                    <div class="col-md-4 col-md-offset-4">
                        <div class="main-content login_content">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <input id="login" type="text" name="USER_NAME" value="" placeholder="<fmt:message key="index.userName"/>" class="form-control" >
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <input id="pass" type="password" name="USER_PASSWORD" value="" placeholder="<fmt:message key="index.password"/>" class="form-control" >
                                        </div>
                                    </div>
                                </div>
                                <div class="row" id="recaptcha"></div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <button class="btn btn-primary btn-block btn-login-submit"><fmt:message key="index.login"/></button>
                                        </div>
                                    </div>
                                </div>
                            <div class="row">
                                <div class="aboveLine">
                                    <div><span><fmt:message key="index.or"/> </span></div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-10 col-md-offset-1">
                                    <div class="well">
                                        <div class="carousel slide" id="accounts-in-carousel">
                                            <!-- Carousel items -->
                                            <div class="carousel-inner">
                                                <div class="item active">
                                                    <div class="row">

                                                        <div class="col-sm-3"><a href="/check"><img width="100%" alt="Image" src="http://new.kpi.com/wp-content/themes/kpi-new/img/social/gmail-icon.png"></a>
                                                        </div>
                                                        <div class="col-sm-3"><a href="/enterGoogleDomain.html"><img width="100%" alt="Image" src="http://new.kpi.com/wp-content/themes/kpi-new/img/social/google_play.png"></a>
                                                        </div>
                                                        <div class="col-sm-3"><a href="#"><img width="100%" alt="Image" src="http://new.kpi.com/wp-content/themes/kpi-new/img/social/windows_phone.png"></a>
                                                        </div>
                                                        <div class="col-sm-3"><a href="/check?ID_PROVIDER=https://me.yahoo.com"><img width="100%" alt="Image" src="http://new.kpi.com/wp-content/themes/kpi-new/img/social/yahoo.png"></a>
                                                        </div>
                                                    </div>
                                                    <!--/row-->
                                                </div>
                                                <!--/item-->
                                                <div class="item">
                                                    <div class="row">
                                                        <div class="col-sm-3"><a href="#"><img width="100%" alt="Image" src="http://new.kpi.com/wp-content/themes/kpi-new/img/social/linkedin.png"></a>
                                                        </div>
                                                        <div class="col-sm-3"><a href="#"><img width="100%" alt="Image" src="http://new.kpi.com/wp-content/themes/kpi-new/img/social/facebook.png"></a>
                                                        </div>
                                                    </div>
                                                    <!--/row-->
                                                </div>
                                            </div>
                                            <a data-slide="prev" href="#accounts-in-carousel" class="left carousel-control">‹</a>
                                            <a data-slide="next" href="#accounts-in-carousel" class="right carousel-control">›</a>
                                        </div>
                                        <!--/myCarousel-->
                                    </div>
                                    <!--/well-->
                                </div>
                            </div>
                            <div class="row">
                                <div class="aboveLine">
                                    <div><span><a href="/forgot/forgotPassword.html"><fmt:message key="index.forgotPassword"/>?</a>&nbsp;|&nbsp;<a href="/signup/sign-up-for-free.html"><fmt:message key="index.signUp"/> </a></span></div>
                                </div>
                            </div>
                        </div>
                    </div> <!-- End of Right sidebar, sign up form -->

                </div><!-- #row products_review -->
           </form>
       </article>
    </div>






















            </form>
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


    <script type="text/javascript">
        <!--//--><![CDATA[//><!--
        var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
        document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
        //--><!] ]>

    </script>
    <script type="text/javascript">
        <!--//--><![CDATA[//><!--
        try {
            var pageTracker = _gat._getTracker("UA-355982-15");
            pageTracker._trackPageview();
        } catch(err) {
        }
        //--><!] ]>
    </script>

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

</tiles:putAttribute>
</tiles:insertDefinition>