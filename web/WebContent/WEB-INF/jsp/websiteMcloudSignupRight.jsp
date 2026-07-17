<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<script type="text/javascript" src="/jsscript/landing.js"></script>
<script type="text/javascript" src="/jsscript/ajax.js"></script>
<%--<link href="/i/signup/ifrmaeSignup/style.css" rel="stylesheet" type="text/css"/>--%>
<link href="/customisation/kpi.com/webFormSignupMcloudRight.css" rel="stylesheet" type="text/css"/>
<%
    //Recaptcha script
//    Boolean enableCaptcha = request.getAttribute("captcha") != null ? ((Boolean) request.getAttribute("captcha")) : false;
    boolean enableCaptcha = false;
    String captchaTheme = request.getAttribute("captchaTheme") != null ? ((String) request.getAttribute("captchaTheme")) : "white";
    if (enableCaptcha) {
%>
<script type="text/javascript" src="https://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
<% } %>

<meta name="msvalidate.01" content="358DD608AB06D6ED2E842AF53CAFFB9E"/>
<div class="signup-form-sidebar">
    <form:form method="post" commandName="newCompany" id="signUpFree" class="plate form_signUp">
                <h1 class="plateTitle_big text-center">Sign up for 14 Day Free Trial</h1>
                <hr>
                <fieldset class="">
                    <div class="form-group clearfix">
                        <div class="half">
                            <label for="signUpFree_firstName"><fmt:message key="signup.firstName"></fmt:message></label>
                            <form:input path="adminFName" cssClass="form-control input-sm" id="signUpFree_firstName"/>
                            <span> <form:errors path="adminFName" cssClass="error"/> </span>
                        </div>

                        <div class="half">
                            <label for="signUpFree_secondName"><fmt:message key="signup.lastName"></fmt:message></label>
                            <form:input path="adminLName" cssClass="form-control input-sm" id="signUpFree_secondName"/>
                            <span> <form:errors path="adminLName" cssClass="error"/> </span>
                        </div>
                    </div>

                    <div class="form-group clearfix">
                        <div class="half">
                            <label for="signUpFree_phone"><fmt:message key="signup.phone"></fmt:message></label>
                            <form:input path="phone" cssClass="form-control input-sm" id="signUpFree_phone"
                                        onkeypress="return phoneValidation(event)"/>
                            <span> <form:errors path="phone" cssClass="error"/> </span>
                        </div>

                        <div class="half">
                            <label for="signUpFree_email"> <fmt:message key="signup.email"></fmt:message> </label>
                            <form:input path="adminEmail" cssClass="form-control input-sm" id="signUpFree_email"
                                        readonly="${fromFederatedLogin}"
                                        onblur="sendRequest('GET','/signup/handleAjaxRequest?adminEmail='+this.value,this.value)"/>
                            <span id="emailValidate" class="error"><form:errors path="adminEmail" cssClass="error"/></span>
                        </div>
                    </div>

                    <div class="form-group clearfix">
                        <div class="half">
                            <label for="signUpFree_company"> <fmt:message key="signup.company"></fmt:message></label>
                            <form:input path="name" cssClass="form-control input-sm" id="signUpFree_company"/>
                            <span> <form:errors path="name" cssClass="error"/> </span>
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

                <%--<center>
                    <div id="recaptcha"></div>
                    <div style="text-transform:uppercase; height: 20px; ">${captchaError}</div>
                </center>--%>
                <hr>
                <footer>
                    <button class="btn btn-primary btn-lg fullWidth" type="submit"  onclick="this.form.submit();">Submit</button>
                </footer>

                <input type="hidden" name="value1" value="value1"/>
                <input type="hidden" name="value2" value="value2"/>
                <input type="hidden" name="value3" value="value3"/>

                <input type="hidden" name="agreeWithCondition" value="true"/>
                <input type="hidden" name="_agreeWithCondition" value="on"/>
                <input type="hidden" name="companySignedUpFrom" value="IFRAME_SIGNUP_jsp"/> <%--referral--%>
                <input type="hidden" name="action" value="signUp"/>


            </form:form>
</div>
<%
    if (enableCaptcha) {%>
<script type="text/javascript">
    Recaptcha.create("<%=EdsContextParams.getRecaptchaPublicKey()%>", "recaptcha", {
        theme: "<%=captchaTheme%>",
        callback: Recaptcha.focus_response_field});
</script>
<% } %>



