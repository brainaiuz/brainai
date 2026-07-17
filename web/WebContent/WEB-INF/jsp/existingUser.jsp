<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%--
  Created by IntelliJ IDEA.
  User: Aziz
  Date: Oct 02, 2009
  Time: 4:13:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!doctype html>
<html class="no-js" lang="${newCompany.locale}" dir="ltr">
<head>

    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Multiple Company Selection</title>
    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/slick/slick.css"/>
    <link rel="stylesheet"
          href="/mainStyles/new-ui/css/materialize.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>">
    <style>
        .progress-panel--remove-sample-data {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: white;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            z-index: 1000;
            width: 600px;
            height: 400px;
        }

        .progress-panel__header {
            font-size: 1.5em;
            margin-bottom: 10px;
        }

        .progress-panel__steps,
        .progress-panel__current-desc,
        .cs-loader {
            margin-top: 10px;
        }

        .modal-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 999;
        }
    </style>

    <script type="text/javascript">
        function setCheked() {
            form = document.getElementById('newCompany');
            form.submit();
        }
        function clickStopper(e) {
            e.preventDefault();
        }
        var isInIFrame = (window.location != window.parent.location) ? true : false;
        if (isInIFrame) {
            var query = "?adminFName=${newCompany.adminFName}&adminLName=${newCompany.adminLName}&adminEmail=${newCompany.adminEmail}"
                + "&name=${newCompany.name}&adminActive=${newCompany.adminActive}&signedUpPage=${newCompany.signedUpPage}&countryID=${newCompany.countryID}"
                + "&stateID=${newCompany.stateID}&workArea=${newCompany.workArea}&phone=${newCompany.phone}&setUp=${newCompany.setUp}"
                + "&currencyID=${newCompany.currencyID}&active=${newCompany.active}&clientSingUpIPAddress=${newCompany.clientSingUpIPAddress}"
                + "&agreeWithCondition=${newCompany.agreeWithCondition}&countryName=${newCompany.countryName}&googleAppsDomain=${newCompany.googleAppsDomain}"
                + "&utm_source=${newCompany.utm_source}&callCode=${newCompany.callCode}&utm_btn=${newCompany.utm_btn}&utm_keyword=${newCompany.utm_keyword}&redirected=${newCompany.redirected}"
                + "&utm_medium=${newCompany.utm_medium}&utm_campaign=${newCompany.utm_campaign}&gclid=${newCompany.gclid}"
                + "&companySignedUpFrom=${newCompany.companySignedUpFrom}&fromFederatedLogin=${newCompany.fromFederatedLogin}&locale=${newCompany.locale}&iframe=1";
            parent.location = "http://${hostName}/auth/existingUser.html" + query;
        }

    </script>

</head>
<body class="signUpStep signUpStep--3">
<script type="text/javascript" src="/mainStyles/js/jquery-3.2.1.min.js"></script>
<!--<script type="text/javascript" src="js/jquery.easing.1.3.js"></script>-->
<script type="text/javascript" src="/mainStyles/new-ui/js/materialize-0.97.5.min.js"></script>

<div class="modal-holder active">
    <div class="modal modal--md sign-up-step sign-up-step--3">
        <div class="modal-wrapper">
            <div class="modal-header">
                <div class="hgroup">
                    <h1 class="fs-4">
                        Existing Account!
                    </h1>
                </div>
            </div>
            <div class="modal-content">
                <div class="form-group" style="font-size: 14px">
                    <form:form id="newCompany" method="post" commandName="newCompany" action="/signup/registerCompany">
                        <input type="hidden" name="adminFName" value="${newCompany.adminFName}"/>
                        <input type="hidden" name="adminLName" value="${newCompany.adminLName}"/>
                        <input type="hidden" name="adminEmail" value="${newCompany.adminEmail}"/>
                        <input type="hidden" name="name" value="${newCompany.name}"/>
                        <input type="hidden" name="adminActive" value="${newCompany.adminActive}"/>
                        <input type="hidden" name="signedUpPage" value="${newCompany.signedUpPage}"/>
                        <input type="hidden" name="countryID" value="${newCompany.countryID}"/>
                        <input type="hidden" name="stateID" value="${newCompany.stateID}"/>
                        <input type="hidden" name="workArea" value="${newCompany.workArea}"/>
                        <input type="hidden" name="phone" value="${newCompany.phone}"/>
                        <input type="hidden" name="callCode" value="${newCompany.callCode}"/>
                        <input type="hidden" name="setUp" value="${newCompany.setUp}"/>
                        <input type="hidden" name="currencyID" value="${newCompany.currencyID}"/>
                        <input type="hidden" name="active" value="${newCompany.active}"/>
                        <input type="hidden" name="clientSingUpIPAddress"
                               value="${newCompany.clientSingUpIPAddress}"/>
                        <input type="hidden" name="agreeWithCondition"
                               value="${newCompany.agreeWithCondition}"/>
                        <input type="hidden" name="countryName" value="${newCompany.countryName}"/>
                        <input type="hidden" name="googleAppsDomain" value="${newCompany.googleAppsDomain}"/>
                        <input type="hidden" name="companySignedUpFrom"
                               value="${newCompany.companySignedUpFrom}"/>
                        <input type="hidden" name="fromFederatedLogin"
                               value="${newCompany.fromFederatedLogin}"/>
                        <input type="hidden" name="utm_campaign" value="${newCompany.utm_campaign}"/>
                        <input type="hidden" name="utm_source" value="${newCompany.utm_source}"/>
                        <input type="hidden" name="utm_medium" value="${newCompany.utm_medium}"/>
                        <input type="hidden" name="utm_keyword" value="${newCompany.utm_keyword}"/>
                        <input type="hidden" name="utm_btn" value="${newCompany.utm_btn}"/>
                        <input type="hidden" name="utm_content" value="${newCompany.utm_content}"/>
                        <input type="hidden" name="redirected" value="${newCompany.redirected}"/>
                        <input type="hidden" name="referrer" value="${newCompany.referrer}"/>
                        <input type="hidden" name="gclid" value="${newCompany.gclid}"/>
                        <input type="hidden" name="promoCode" value="${newCompany.promoCode}"/>
                        <input type="hidden" name="redirectToSettings" value="${newCompany.redirectToSettings}"/>
                        <p>
                            <fmt:message key="existinguser.youAlready"/>
                            <a href="javascript:setCheked()" style="color:#0070f1;"
                               onclick="this.addEventListener('click', clickStopper, false)">
                                <fmt:message key="existinguser.continue"/>
                            </a>
                            <fmt:message key="existinguser.inOrder"/>
                        </p>
                        <p><fmt:message key="existinguser.YouForgot"/>
                            <a href="/auth/forgotPassword.html" style="color:#0070f1;">
                                <fmt:message key="existinguser.iForgot"/>
                            </a>
                        </p>
                        <p>
                            <fmt:message key="existinguser.clickHere"/>
                            <a href="/index.html" style="color:#0070f1;">
                                <fmt:message key="existinguser.cancel"/>
                            </a>
                            <fmt:message key="existinguser.andGoBack"/>
                        </p>
                    </form:form>
                </div>

                <a href="/index.html" class="btn btn--primary">Cancel</a>
                <a href="javascript:setCheked()"
                   onclick="initializeProgressBar('/mainStyles/new-ui/css/materialize.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>'); this.addEventListener('click', clickStopper, false); removeProgressPanel();"
                   class="btn btn--primary">Continue To Sign Up
                </a>

            </div>
        </div>
    </div>
    <div class="lean-overlay file--existingUser" id="materialize-lean-overlay-1" style="z-index: 1002; display: block; opacity: 0.5;"></div>
</div>

<!--SCRIPTS-->
<script>
    jQuery(document).ready(function () {
        $('select').material_select();
    });
</script>
<script src="/mainStyles/new-ui/login/js/jquery.min.js"></script>
<script src="/mainStyles/new-ui/login/js/slick.min.js"></script>
<script src="/mainStyles/new-ui/login/js/main.js"></script>
<script src="/mainStyles/new-ui/js/frame_affix.js"></script>
<!--New Google Analytics script-->
<script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
    try {
        var pageTracker = _gat._getTracker("UA-355982-15");
        pageTracker._trackPageview();
    } catch (err) {
    }</script>
<!--New Google Analytics script-->
</body>
</html>