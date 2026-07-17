<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<script type="text/javascript" src="/jsscript/landing.js"></script>
<script type="text/javascript" src="/jsscript/ajax.js"></script>
<SCRIPT type=text/javascript src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></SCRIPT>
<%--<link href="/i/signup/ifrmaeSignup/style.css" rel="stylesheet" type="text/css"/>--%>
<link href="/customisation/kpi.com/webFormSignupNewKpi.css" rel="stylesheet" type="text/css"/>

<meta name="msvalidate.01" content="358DD608AB06D6ED2E842AF53CAFFB9E"/>
<div id="containerNew" class="form_signUp_slim">
    <form:form method="post" commandName="newCompany" id="signUpFree" cssClass="form_signUp clearfix">
        <h1 class="plateTitle_big text-center" style="font-weight: bold;">Sign up for 14 Day Free Trial</h1>
        <fieldset class="">
            <div class="form-group clearfix">
                <div class="half">
                    <label for="signUpFree_firstName"><fmt:message key="signup.firstName"></fmt:message></label>
                    <form:input path="adminFName" cssClass="form-control input-sm" id="signUpFree_firstName"/>
                    <form:errors path="adminFName" cssClass="error"/>
                </div>

                <div class="half">
                    <label for="signUpFree_secondName"><fmt:message key="signup.lastName"></fmt:message></label>
                    <form:input path="adminLName" cssClass="form-control input-sm" id="signUpFree_secondName"/>
                    <form:errors path="adminLName" cssClass="error"/>
                </div>
            </div>

            <div class="form-group clearfix">
                <div class="half">
                    <label for="signUpFree_phone"><fmt:message key="signup.phone"></fmt:message></label>
                    <form:input path="phone" cssClass="form-control input-sm" id="signUpFree_phone"
                                onkeypress="return phoneValidation(event)"/>
                    <form:errors path="phone" cssClass="error"/>
                </div>

                <div class="half">
                    <label for="signUpFree_email"> <fmt:message key="signup.email"></fmt:message> </label>
                    <form:input path="adminEmail" cssClass="form-control input-sm" id="signUpFree_email"
                                readonly="${fromFederatedLogin}"
                                onblur="sendRequest('GET','/signup/handleAjaxRequest?adminEmail='+this.value,this.value)"/>
                    <form:errors path="adminEmail" cssClass="error"/>
                </div>
            </div>

            <div class="form-group clearfix">
                <div class="half">
                    <label for="signUpFree_company"> <fmt:message key="signup.company"></fmt:message></label>
                    <form:input path="name" cssClass="form-control input-sm" id="signUpFree_company"/>
                    <form:errors path="name" cssClass="error"/>
                </div>

                <div class="half">
                    <label for="signUpFree_country"><fmt:message key="signup.country"></fmt:message></label>
                    <form:select path="countryID" id="signUpFree_country" cssClass="form-control input-sm">
                        <form:option value=""> <fmt:message key="signup.pleaseSelect"> </fmt:message> </form:option>
                        <form:options items="${countrys}" itemLabel="name" itemValue="objectID"/>
                    </form:select>
                    <form:errors path="countryID" cssClass="error"/>
                </div>
            </div>

        </fieldset>

        <center>
            <div id="recaptcha"></div>
            <div style="text-transform:uppercase; height: 20px; ">${captchaError}</div>
        </center>

        <footer>
            <input class="btn btn-primary btn-lg fullWidth" type="submit" id="edit-submit"
                   value='<fmt:message key="signup.demoVersion"> </fmt:message> ' onclick="this.form.submit();"/>
        </footer>

        <input type="hidden" name="value1" value=""/>
        <input type="hidden" name="value2" value=""/>
        <input type="hidden" name="value3" value=""/>

        <input type="hidden" name="agreeWithCondition" value="true"/>
        <input type="hidden" name="_agreeWithCondition" value="on"/>
        <input type="hidden" name="companySignedUpFrom" value="IFRAME_SIGNUP_jsp"/> <%--referral--%>
        <input type="hidden" name="action" value="signUp"/>


    </form:form>
    <script type="text/javascript">
        jQuery("#signUpFree").submit(function (event) {
            var firstName = jQuery('#signUpFree_firstName').val();
            var secondName = jQuery('#signUpFree_secondName').val();
            var phone = jQuery('#signUpFree_phone').val();
            var email = jQuery('#signUpFree_email').val();
            var company = jQuery('#signUpFree_company').val();
            var country = jQuery('#signUpFree_country option:selected').text();
            var countryId = jQuery('#signUpFree_country').val();
            if (firstName != "" && secondName != "" && phone != ""
                    && email != "" && company != "" && countryId != "") {
                ga('send', 'event', 'Signup from www.kpi.com ', 'Signup From Homepage from signup form iframe',
                                'First Name=' + firstName + ', Last Name=' + secondName + ', Phone=' +
                                phone + ', E-mail=' + email + ', Company=' + company + ', Country=' + country);
                ga('finnetTracker.send', 'event', 'Signup from www.kpi.com ', 'Signup From Homepage from signup form iframe',
                                'First Name=' + firstName + ', Last Name=' + secondName + ', Phone=' +
                                phone + ', E-mail=' + email + ', Company=' + company + ', Country=' + country);
            }
        });
    </script>
</div>
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
            callback: function () {
                console.log('recaptcha callback');
            }
        });
    }
</script>
<script src="https://www.google.com/recaptcha/api.js?onload=recaptchaCallback&render=explicit" async defer></script>

<% } %>





