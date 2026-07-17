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
<link href="/customisation/kpi.com/webFormSignupAWS.css" rel="stylesheet" type="text/css"/>
<%
    //Recaptcha script
    Boolean enableCaptcha = request.getAttribute("captcha") != null ? ((Boolean) request.getAttribute("captcha")) : false;
//    boolean enableCaptcha = false;
    String captchaTheme = request.getAttribute("captchaTheme") != null ? ((String) request.getAttribute("captchaTheme")) : "white";
    if (enableCaptcha) {
%>
<script type="text/javascript" src="https://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
<% } %>

<meta name="msvalidate.01" content="358DD608AB06D6ED2E842AF53CAFFB9E"/>


<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>

<!-- Javascript -->
<script type="text/javascript">

    $(document).ready(function () {
//        $("#btn231").click(function(){
//            $("#test1").text(window.location.hostname);
//        });
        $("#parentIframeUrl").val(window.location.hostname);
    });
</script>

<!-- HTML -->


<form:form method="post" commandName="newCompany" cssClass="webform-client-form signUp-form">

    <div>

        <div id="edit-submitted-first-name-wrapper" class="form-item odd">
            <label for="edit-submitted-first-name"><fmt:message key="signup.firstName">:</fmt:message>
                <span title="This field is required." class="form-required">*</span>
            </label>
                <%--<input type="text" class="form-text required" value="" id="edit-submitted-first-name" name="submitted[first_name]" />--%>
            <form:input path="adminFName" cssClass="form-text required" id="edit-submitted-first-name"/>
            <span> <form:errors path="adminFName" cssClass="error"/> </span>
        </div>


        <div id="edit-submitted-last-name-wrapper" class="form-item even">
            <label for="edit-submitted-last-name"><fmt:message key="signup.lastName">:</fmt:message>
                <span title="This field is required." class="form-required">*</span>
            </label>
                <%--<input type="text" class="form-text required" value="" id="edit-submitted-last-name" name="submitted[last_name]" />--%>
            <form:input path="adminLName" cssClass="form-text required" id="edit-submitted-last-name"/>
            <span> <form:errors path="adminLName" cssClass="error"/> </span>

        </div>


        <div id="edit-submitted-phone-wrapper" class="form-item odd">
            <label for="edit-submitted-phone"><fmt:message key="signup.phone">:</fmt:message>
                <span title="This field is required." class="form-required">*</span>
            </label>
                <%--<input type="text" class="form-text required" value="" id="edit-submitted-phone" name="submitted[phone]" />--%>
            <form:input path="phone" cssClass="form-text required" id="edit-submitted-phone"
                        onkeypress="return phoneValidation(event)"/>
            <span> <form:errors path="phone" cssClass="error"/> </span>


        </div>


        <div id="edit-submitted-e-mail-wrapper" class="form-item even">
            <label for="edit-submitted-e-mail"> <fmt:message key="signup.email">:</fmt:message>
		  <span title="This field is required."
                class="form-required">*</span></label>
                <%--<input type="text" class="form-text required" value="" id="edit-submitted-e-mail" name="submitted[e_mail]" />--%>
            <form:input path="adminEmail" cssClass="form-text required" id="edit-submitted-e-mail"
                        readonly="${fromFederatedLogin}"
                        onblur="sendRequest('GET','/signup/handleAjaxRequest?adminEmail='+this.value,this.value)"/>
            <span id="emailValidate" class="error"><form:errors path="adminEmail" cssClass="error"/></span>
        </div>


        <div id="edit-submitted-company-wrapper" class="form-item odd">
            <label for="edit-submitted-company"> <fmt:message key="signup.company">:</fmt:message>
                <span title="This field is required." class="form-required">*</span>
            </label>
                <%--<input type="text" class="form-text required" value="" id="edit-submitted-company" name="submitted[company]" />--%>
            <form:input path="name" cssClass="form-text required" id="edit-submitted-company"/>
            <span> <form:errors path="name" cssClass="error"/> </span>

        </div>


        <div id="edit-submitted-country-wrapper" class="form-item even">
            <label for="edit-submitted-country"><fmt:message key="signup.country">:</fmt:message>
                <span title="This field is required." class="form-required">*</span>
            </label>

            <form:select path="countryID" id="edit-submitted-country" cssClass="form-select required">
                <form:option value=""> <fmt:message key="signup.pleaseSelect"> </fmt:message> </form:option>
                <form:options items="${countrys}" itemLabel="name" itemValue="objectID"/>
            </form:select>
            <form:errors path="countryID" cssClass="error"/>

        </div>

        <center style="float: left;">
            <div id="recaptcha"></div>
            <div style="text-transform:uppercase; height: 20px; ">${captchaError}</div>
        </center>

        <input type="hidden" name="value1" value="value1"/>
        <input type="hidden" name="value2" value="value2"/>
        <input type="hidden" name="value3" value="value3"/>


            <%--<input type="submit" name="op" id="edit-submit" value="Sign up for a Free Trial" class="form-submit" />--%>


        <input class="form-submit" type="submit" id="edit-submit"
               value='<fmt:message key="signup.demoVersion"> </fmt:message> ' onclick="this.form.submit();"/>

        <input type="hidden" name="agreeWithCondition" value="true"/>
        <input type="hidden" name="_agreeWithCondition" value="on"/>
        <input type="hidden" name="companySignedUpFrom" value="kpiarabia"/> <%--referral--%>
        <input type="hidden" name="action" value="signUp"/>
            <%--<input type="hidden" name="myCustomHost" id="myCustomHost" value=""/>--%>
        <form:hidden path="parentIframeUrl"/>


    </div>
</form:form>

<%
    if (enableCaptcha) {%>
<script type="text/javascript">
    Recaptcha.create("<%=EdsContextParams.getRecaptchaPublicKey()%>", "recaptcha", {
        theme: "<%=captchaTheme%>",
        callback: Recaptcha.focus_response_field});
</script>
<% } %>

