<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %><%--
  Created by IntelliJ IDEA.
  User: Sherali
  Date: Jan 29, 2009
  Time: 4:13:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>


<!doctype html>
<html class="no-js" lang="">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Account Details</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="apple-touch-icon" href="../../customisation/postroom/img/apple-touch-icon.png">
    <!-- Place favicon.ico in the root directory -->


    <!--<link rel="stylesheet" href="css/normalize.css">-->
    <link rel="stylesheet" href="../../customisation/postroom/css/app.css?2017">
    <link rel="stylesheet" href="../../customisation/postroom/css/foundation.css?2017">
    <link rel="stylesheet" href="../../customisation/postroom/css/pg-welcome.css?2017">
    <link href="/customisation/kpi.com/select2.css" rel="stylesheet">
    <script type=text/javascript src="/customisation/preprod.kpi.com/scripts/jquery-3.1.1.min.js"></script>
    <script type=text/javascript src="/customisation/preprod.kpi.com/scripts/jquery.select2.js"></script>

    <link rel="stylesheet" href="/customisation/preprod.kpi.com/materialSIgnup/css/flags.min.css?2017">

</head>
<body class="pg-welcome">
<span class="welcome-logo">
    <a href="#"><img src="../../customisation/postroom/img/logo_white-1.png" alt="kpi.com"/></a>
</span>
<form:form id="appTrialForm" method="post" modelAttribute="newCompany" action="/signup/customSignUp.html">
    <form:hidden path="companySignedUpFrom"/>

    <%--<div class="small-60 medium-40 large-30 columns">--%>
    <%--<c:set var="nameHasBindError">--%>
    <%--<form:errors path="adminFName"/>--%>
    <%--</c:set>--%>
    <%--<c:set var="phoneHasBindError">--%>
    <%--<form:errors path="phone"/>--%>
    <%--</c:set>--%>
    <%--<c:set var="emailHasBindError">--%>
    <%--<form:errors path="adminEmail"/>--%>
    <%--</c:set>--%>
    <%--</div>--%>

    <div class="welcome-panel welcome-panel--details">
        <div class="panel-heading">
            <h2 class="panel-title">
                <fmt:message
                        key="signup.accountDetails"/>
            </h2>
        </div>
        <input type="hidden" name="users" value="<c:out value="${users}"/>"/>
        <input type="hidden" name="registrationType" value="<c:out value="${registrationType}"/>"/>
        <input type="hidden" name="socialUserName" value="<c:out value="${socialUserName}"/>"/>
        <div class="panel-body">

            <c:set var="adminFNameHasBindError">
                <form:errors path="adminFName"/>
            </c:set>
            <div class="form-group ${not empty adminFNameHasBindError ? 'error' : ''}">
                <label class="field-mark--name">
                    <form:input path="adminFName" class="form-control" id="signUpFree_firstName"/>
                </label>
            </div>

            <c:set var="adminLNameHasBindError">
                <form:errors path="adminLName"/>
            </c:set>
            <div class="form-group ${not empty adminLNameHasBindError ? 'error' : ''}">
                <label class="field-mark--name">
                    <form:input path="adminLName" class="form-control" id="signUpFree_secondName"/>
                </label>
            </div>

            <c:set var="adminEmailHasBindError">
                <form:errors path="adminEmail"/>
            </c:set>
            <div class="form-group ${not empty adminEmailHasBindError ? 'error' : ''}">
                <label class="field-mark--email">
                    <form:input path="adminEmail" class="form-control" id="edit-submitted-e-mail" type="email" readonly="${fromFederatedLogin}"/>
                </label>
            </div>

            <c:set var="nameHasBindError">
                <form:errors path="name"/>
            </c:set>
            <div class="form-group ${not empty nameHasBindError ? 'error' : ''}">
                <label class="field-mark--company">
                    <form:input path="name" class="form-control" id="signUpFree_company"/>
                </label>
            </div>

            <div class="countryCode facebookLogin">
                <div class="input-group">
                    <span class="input-group-addon field-mark--phone"></span>
                    <span class="input-group-addon">

                            <form:select path="callCode" id="countryID">
                                <option></option>
                                <c:forEach items="${countryCallCodes}" var="ccc">
                                    <c:choose>
                                        <c:when test="${ccc.callCode == currentCountry}">
                                                            <option value="${ccc.callCode}" id="${ccc.countryCode}"
                                                                    selected="selected">
                                                                +${ccc.callCode}( ${ccc.name} )
                                                            </option>
                                        </c:when>
                                        <c:otherwise>
                                                            <option value="${ccc.callCode}" id="${ccc.countryCode}">
                                                                +${ccc.callCode}( ${ccc.name} )
                                                            </option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </form:select>
                        </span>

                    <c:set var="phoneHasBindError">
                        <form:errors path="phone"/>
                    </c:set>

                    <div style="width: 270px;margin-top: 0px;" class="form-group ${not empty phoneHasBindError ? 'error' : ''}">
                        <form:input path="phone" id="signUpFree_phone" class="form-control"
                                    aria-label="Text input with radio button"
                                    onkeypress="return customPhoneValidation(event)"/>
                    </div>
                </div>

                <div class="form-note">
                    <fmt:message key="signup.bySigningUpIAgreeToKpi"/> <a class="nobr"
                                                                          href="//${helpHost}/content/terms-of-use"
                                                                          target="_blank"><fmt:message
                        key="signup.termsAndConditions"/></a> and <a href="//${helpHost}/content/terms-of-use"
                                                                     target="_blank"><fmt:message
                        key="signup.privacyPolicy"/></a>.
                </div>
            </div>

        </div>

        <div class="panel-footer">
            <button class="button btn-block btn-success" onclick="statistics();">
                CONTINUE
            </button>

        </div>
    </div>
</form:form>
</body>
</html>


<%--Insert Javascript--%>
<tiles:putAttribute name="script">
    <script type="text/javascript">
        var WRInitTime = (new Date()).getTime();
    </script>

    <div id="ClickTaleDiv" style="display: none;"></div>
    <script src="http://s.clicktale.net/WRb4.js" type="text/javascript"></script>
    <script type="text/javascript">
        if (typeof ClickTale == 'function') ClickTale(6057, 1, "www02");
    </script>

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

</tiles:putAttribute>

<script src="/customisation/preprod.kpi.com/materialSIgnup/js/vendor/what-input.js"></script>
<script src="/customisation/preprod.kpi.com/materialSIgnup/js/vendor/foundation.js"></script>
<script src="/customisation/preprod.kpi.com/materialSIgnup/js/app.js"></script>

<script type="text/javascript">

    function formatState(state) {
        if (!state.id) {
            return state.text;
        }
        var $state = $(
            '<span><img class="flag flag-' + state.element.id.toLowerCase() + '" /> ' + state.text + '</span>'
        );
        return $state;
    };


    $(document).ready(function () {
//        $('#signUpFree_country').select2({
//            placeholder: 'Select a country code'
//        }).on("select2:close", function (event) {
//            $('#signUpFree_country').get(0).focus();
//        });


        $("#countryID").select2({
            templateResult: formatState,
            templateSelection: formatState,
            placeholder: 'Select a country code'
        }).on("select2:close", function (event) {
            $('#countryID').get(0).focus();
        });
        ;

        $('#signUpFree_firstName').on('keyup keypress keydown', function (event) {
            if (event.keyCode === 9) {                      // 9 - tabkey when user blurs from name field he/she supposed to
                $('#countryID').select2('open');   // focus on country code field. so open event triggers
            }
        });

        $('#xField').keypress(function (event) {
            var check = false;
            if (!validateOnlyDigits(event)) {
                if (event.preventDefault) {
                    event.preventDefault();
                } else {
                    event.returnValue = false;
                }
            }
        });
    });


    function customPhoneValidation(e) {
        var keynum;
        var keychar;
        var num = '0123456789'
        if (e.keyCode) //IE
        {
            keynum = e.keyCode;
        } else if (e.which)// Netscape/Firefox/Opera
        {
            keynum = e.which;
        }
        keychar = String.fromCharCode(keynum);
        if ((num.indexOf(keychar) != -1) || keynum === 8 || keynum === 9) {
            return true;
        } else return false;
    }


    function validateOnlyDigits(e) {
        var keynum;
        var keychar;
        var num = '0123456789'
        if (e.keyCode) //IE
        {
            keynum = e.keyCode;
        } else if (e.which)// Netscape/Firefox/Opera
        {
            keynum = e.which;
        }
        keychar = String.fromCharCode(keynum);
        if ((num.indexOf(keychar) !== -1)
            || keynum === 8 || keynum === 9 || keynum === 46
            || keynum === 13 || keynum === 36 || keynum === 35
            || keynum === 37 || keynum === 38 || keynum === 39
            || keynum === 40) return true;
        else return false;
    }


    function statistics() {
        var cookiearray = document.cookie.split(';');
        var utm_source = "";
        var utm_medium = "";
        var utm_campaign = "";
        for (var i = 0; i < cookiearray.length; i++) {
            var name = cookiearray[i].split('=')[0];
            var value = cookiearray[i].split('=')[1];
            if (name === " utm_source") {
                utm_source = value;
            } else if (name === " utm_medium") {
                utm_medium = value;
            } else if (name === " utm_campaign") {
                utm_campaign = value;
            }
        }
        var firstName = jQuery('#signUpFree_firstName').val();
        var secondName = jQuery('#signUpFree_secondName').val();
        var phone = jQuery('#signUpFree_phone').val();
        var email = jQuery('#edit-submitted-e-mail').val();
        var company = jQuery('#signUpFree_company').val();
        var country = jQuery('#countryID').find('option:selected').text();
        var countryId = jQuery('#countryID').val();
        if (firstName !== "" && secondName !== "" && phone !== "" && email !== "" && company !== "" && countryId !== "") {
            if (utm_source !== "" && utm_medium !== "" && utm_campaign !== "") {
                ga('send', 'event', 'Signup from www.kpi.com ', 'Signup From Homepage from signup form iframe',
                    'utm_source=' + utm_source + ', utm_medium=' + utm_medium +
                    ', utm_campaign=' + utm_campaign + ', First Name=' + firstName + ', Last Name=' + secondName + ', Phone=' +
                    phone + ', E-mail=' + email + ', Company=' + company + ', Country=' + country);
                ga('finnetTracker.send', 'event', 'Signup from www.kpi.com ', 'Signup From Homepage from signup form iframe',
                    'utm_source=' + utm_source + ', utm_medium=' + utm_medium +
                    ', utm_campaign=' + utm_campaign + ', First Name=' + firstName + ', Last Name=' + secondName + ', Phone=' +
                    phone + ', E-mail=' + email + ', Company=' + company + ', Country=' + country);
            } else {
                ga('send', 'event', 'Signup from www.kpi.com ', 'Signup From Homepage from signup form iframe',
                    'First Name=' + firstName + ', Last Name=' + secondName + ', Phone=' +
                    phone + ', E-mail=' + email + ', Company=' + company + ', Country=' + country);
                ga('finnetTracker.send', 'event', 'Signup from www.kpi.com ', 'Signup From Homepage from signup form iframe',
                    'First Name=' + firstName + ', Last Name=' + secondName + ', Phone=' +
                    phone + ', E-mail=' + email + ', Company=' + company + ', Country=' + country);
            }
        }
    }
</script>


<script type="text/javascript">
    setTimeout(function () {
        var a = document.createElement("script");
        var b = document.getElementsByTagName("script")[0];
        a.src = document.location.protocol + "//script.crazyegg.com/pages/scripts/0043/1807.js?" + Math.floor(new Date().getTime() / 3600000);
        a.async = true;
        a.type = "text/javascript";
        b.parentNode.insertBefore(a, b)
    }, 1);
</script>
<%
    //Recaptcha script
    Boolean enableCaptcha = request.getAttribute("captcha") != null ? ((Boolean) request.getAttribute("captcha")) : false;
    String captchaTheme = request.getAttribute("captchaTheme") != null ? ((String) request.getAttribute("captchaTheme")) : "white";

    if (enableCaptcha) {%>
<script type="text/javascript">
    var recaptchaCallback = function () {
        //console.log('recaptcha is ready'); // not showing
        grecaptcha.render("recaptcha", {
            sitekey: '<%=EdsContextParams.getRecaptchaPublicKey()%>',
            'type': 'image',
            'theme': 'custom',
            callback: function () {
                console.log('recaptcha callback');
            }
        });
    }
</script>
<script src="https://www.google.com/recaptcha/api.js?onload=recaptchaCallback&render=explicit" async defer></script>

<% } %>
<script type="text/javascript">
    (function (d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) return;
        js = d.createElement(s);
        js.id = id;
        js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=<%=EdsContextParams.getFacebookAppID(request.getServerName())%>";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));</script>

<script type="text/javascript">
    function google_login(event) {
        if (event) {
            event.preventDefault();
        }
        window.location = '/check';

    }
    function fb_login(event) {
        if (event) {
            event.preventDefault();
        }
        FB.login(function (response) {
            console.log(response);

            if (response.authResponse) {
                console.log('Welcome!  Fetching your information.... ');
                var uid = response.authResponse.userID;
                var accessToken = response.authResponse.accessToken;
                window.location = 'facebookLogin?access_token=' + accessToken + '&uid=' + uid;
            } else {
                console.log('User cancelled login or did not fully authorize.');

            }
        }, {
            scope: 'email, public_profile',
            return_scopes: false
        });
    }
</script>


