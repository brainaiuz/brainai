<%--
  Created by IntelliJ IDEA.
  User: babayev.xushnud@gmail.com
  Date: 3/2/12
  Time: 4:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<script type="text/javascript" src="/jsscript/landing.js"></script>
<script type="text/javascript" src="/jsscript/ajax.js"></script>
<link href="/i/signup/marketingSignup.css" rel="stylesheet" type="text/css"/>

<meta name="msvalidate.01" content="358DD608AB06D6ED2E842AF53CAFFB9E"/>

<form:form method="post" commandName="newCompany" cssClass="webform-client-form strongBox trialFormFildset">

    <div class="marketing">

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

            <%--<input type="submit" name="op" id="edit-submit" value="Sign up for a Free Trial" class="form-submit" />--%>

            <input class="form-submit2" type="submit" id="edit-submit"
                   value='<fmt:message key="signup.demoVersion"> </fmt:message> ' onclick="this.form.submit();"/>


        <input type="hidden" name="companySignedUpFrom" value="SAASMARKETS"/>
        <input type="hidden" name="agreeWithCondition" value="true"/>
        <input type="hidden" name="_agreeWithCondition" value="on"/>
        <input type="hidden" name="action" value="signUp"/>

    </div>
</form:form>
<div class="shadowTrialFieldset" id=""></div>

<!-- END FREE TRIAL FORM -->

