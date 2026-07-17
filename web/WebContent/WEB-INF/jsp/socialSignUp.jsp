<%--
  Created by IntelliJ IDEA.
  User: Sherali
  Date: Jan 29, 2009
  Time: 4:13:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html class="no-js" lang="">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Account Details</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <script type=text/javascript src="/customisation/preprod.kpi.com/scripts/jquery-3.1.1.min.js"></script>
    <script type=text/javascript src="/customisation/preprod.kpi.com/scripts/jquery.select2.js"></script>
    <script type="text/javascript" src="/mainStyles/js/jquery-3.2.1.min.js"></script>
    <script type=text/javascript src="/customisation/preprod.kpi.com/scripts/jquery.select2.js"></script>
    <!--CSS-->
    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/css/main.css">
    <link href="/customisation/kpi.com/select2.css" rel="stylesheet">
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/materialSIgnup/css/flags.min.css">

    <style type="text/css"></style>
</head>
<body>


<div class="pg_landing pg_sign-up">
    <div class="pg_landing__container">
        <figure class="pg_landing__header">
            <div class="pg_landing__logo">
                <a href="#">
                    <img src="../../mainStyles/new-ui/images/new-kpi-logo.svg" alt="logo">
                </a>
            </div>
            <figcaption>Sign up now and try the trial <br> period for 14 days.</figcaption>
        </figure>
        <div class="pg_landing__main">
            <div class="pg_landing__main-innerbox">

                <div class="cp_login">
                    <div class="cp_login__content">
                        <div class="cp_signup__caption">&nbsp;</div>
                        <form:form id="appTrialForm" method="post" modelAttribute="newCompany" cssClass="cp_login__main" action="/signup/customSignUp.html">
                            <form:hidden path="companySignedUpFrom"/>
                            <input type="hidden" name="users" value="<c:out value="${users}"/>"/>
                            <input type="hidden" name="registrationType" value="<c:out value="${registrationType}"/>"/>
                            <input type="hidden" name="socialUserName" value="<c:out value="${socialUserName}"/>"/>

                            <c:set var="adminFNameHasBindError">
                                <form:errors path="adminFName"/>
                            </c:set>
                            <c:set var="adminLNameHasBindError">
                                <form:errors path="adminLName"/>
                            </c:set>
                            <c:set var="phoneHasBindError">
                                <form:errors path="phone"/>
                            </c:set>
                            <c:set var="adminEmailHasBindError">
                                <form:errors path="adminEmail"/>
                            </c:set>

                            <div class="cp_login__field">
                                <%--<input id="login" name="USER_NAME" value="" type="text" autofocus="" placeholder="Name">--%>
                                <form:input path="adminFName" id="signUpFree_firstName" cssClass="${not empty adminFNameHasBindError ? 'cp_login__field-invalid' : ''}" required="required"/>
                                <div class="cp_login__field-underline"></div>
                            </div>
                            <div class="cp_login__field">
                                <%--<input id="login" name="USER_NAME" value="" type="text" autofocus="" placeholder="Name">--%>
                                    <form:input path="adminLName" id="signUpFree_secondName" cssClass="${not empty adminLNameHasBindError ? 'cp_login__field-invalid' : ''}" required="required"/>
                                <div class="cp_login__field-underline"></div>
                            </div>
                            <div class="cp_login__field">
                                <%--<input id="email" name="USER_EMAIL" value="" type="text" autofocus="" placeholder="Email">--%>
                                <form:input path="adminEmail" id="edit-submitted-e-mail" type="email" readonly="${fromFederatedLogin}" cssClass="${not empty adminEmailHasBindError ? 'cp_login__field-invalid' : ''}"  required="required"/>
                                <div class="cp_login__field-underline"></div>
                            </div>

                            <div class="cp_login__field cp_login__field--password">
                                    <%--<input type="text" id="promo" name="USER_PASSWORD" placeholder="Promo code">--%>
                                <form:input path="name" id="signUpFree_company" placeholder="Company name" required="required"/>
                                <div class="cp_login__field-underline"></div>
                            </div>

                            <div class="cp_login__field signUp-phone">
                                <div class="signUp-phone__prefix">
                                    <form:select path="callCode" id="signUpFree_country">
                                        <option></option>
                                        <c:forEach items="${countryCallCodes}" var="ccc">
                                            <c:choose>
                                                <c:when test="${ccc.countryCode == currentCountry}">
                                                    <option value="${ccc.callCode}" title="${ccc.callCode}" id="${ccc.countryCode}"
                                                            selected="selected">
                                                            ${ccc.name}
                                                    </option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${ccc.callCode}"  title="${ccc.callCode}" id="${ccc.countryCode}">
                                                            ${ccc.name}
                                                    </option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </form:select>
                                </div>

                                <div class="signUp-phone__phone">
                                    <form:input path="phone" id="signUpFree_phone" placeholder="Phone" value="${currentCallCode}"
                                                onkeypress="return customPhoneValidation(event)"
                                                onpaste="return customPasteValidator(event)"
                                                cssClass="${not empty phoneHasBindError ? 'cp_login__field-invalid' : ''}"
                                    />
                                </div>
                                <div class="cp_login__field-underline">
                                </div>
                            </div>

                            <%--<div class="cp_login__field">
                                &lt;%&ndash;<input type="tel" id="phone" />&ndash;%&gt;
                                    <form:input path="phone" id="signUpFree_phone" class="form-control"
                                                aria-label="Text input with radio button"
                                                onkeypress="return customPhoneValidation(event)"/>
                            </div>--%>



                            <div class="cp_login__field controls-stack">
                                <label class="control control--checkbox">
                                    <input type="checkbox" name="agreeWithTerms" id="agreeWithTerms" required="">
                                    <span class="control__indicator"></span>
                                    <span class="control__description" for="agreeWithTerms">I agree with <a href="https://www.kpi.com/company/terms/">Terms and Conditions</a></span>
                                </label>
                                <label class="control control--checkbox">
                                    <input type="checkbox" name="consentToPrivacy" id="consentToPrivacy" required="">
                                    <span class="control__indicator"></span>
                                    <span class="control__description" for="consentToPrivacy">I give consent to <a href="https://www.kpi.com/company/privacy/">Privacy Policy</a></span>
                                </label>
                            </div>

                            <div class="cp_login__form-item">
                                <button class="cp_login__submit elm_btn--green" onclick="statistics();">
                                    CONTINUE
                                </button>
                            </div>
                        </form:form>
                    </div>
                    <div class="cp_login__footer">
                        <dl>
                            <dt>I already have an account</dt>
                            <dd>
                                <span class="pg_landing__start-btn">
                                    <%--<a class="btn-login-sign-up" href="https://www.kpi.com/">Login</a>--%>
                                    <a class="btn-login-sign-up" href="/">Login</a>
                                </span>
                            </dd>
                        </dl>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

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
<script src="/mainStyles/new-ui/js/frame_affix.js"></script>


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
    function selectedFormatState(state) {
        if (!state.id) return state.text;
        return $('<span><img class="flag flag-' + state.element.id.toLowerCase() + '" /></span>');
    };
    $("#signUpFree_country").select2({
        templateResult: formatState,
        templateSelection: selectedFormatState,
        placeholder: 'Country'
    }).on("select2:close", function (event) {
        $('#signUpFree_country').get(0).focus();
    }).on("change", function(changeEvent) {
        var title = $(this).children(":selected").attr("title");
        $('#signUpFree_phone').val('+' + title);
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
    function customPasteValidator(e) {
        // Get the pasted text
        const pastedData = event.clipboardData.getData('text');
        // You can modify the pasted text if needed
        const sanitizedData = pastedData.replace(/[^0-9]/g, ''); // Only allow numbers
        event.target.value = "+" + sanitizedData; // Set the sanitized value back to the input
        // Prevent default paste behavior
        event.preventDefault();
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
        initializeProgressBar("/mainStyles/new-ui/css/materialize.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>");
        removeProgressPanel();
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
        /*var country = jQuery('#countryID').find('option:selected').text();
        var countryId = jQuery('#countryID').val();*/
        if (firstName !== "" && secondName !== "" && phone !== "" && email !== "" && company !== ""/* && countryId !== ""*/) {
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