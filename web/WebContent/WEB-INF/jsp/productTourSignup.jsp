<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script type="text/javascript" src="/jsscript/landing.js"></script>
<script type="text/javascript" src="/jsscript/ajax.js"></script>
<link href="/i/signup/ifrmaeSignup/style.css" rel="stylesheet" type="text/css"/>
<%--<link href="http://www.workforcetrack.ru/sites/default/themes/wft_ru/style.css?C" rel="stylesheet" type="text/css"/>--%>
<style type="text/css">
    .pre-title {
        color: #007DC3;
        font-family: Trebuchet MS;
        font-size: 13px;
        font-weight: bold;
    }

    h2 {
        margin: -4px 0 2px;
    }
<%----%>
    /*.error {*/
        /*color: red;*/
        /*font-family: Trebuchet MS;*/
        /*font-size: 11px;*/
        /*margin-left: 1px*/
    /*}*/
<%----%>
    /*.signup_box td h2 {*/
        /*margin: -12px 0 5px;*/
    /*}*/
<%----%>
    /*.pre-title {*/
        /*color: #007DC3;*/
        /*font-family: Trebuchet MS;*/
        /*font-size: 13px;*/
        /*font-weight: bold;*/
    /*}*/

</style>

<meta name="msvalidate.01" content="358DD608AB06D6ED2E842AF53CAFFB9E"/>

<div class="signup_box">
    <form:form method="post" commandName="newCompany" cssClass="webform-client-form signUp-form sidebarForm">
            <div>
                 <h2 class="pre-title"><fmt:message key="iframeSignupTitle"> </fmt:message> </h2>
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
                    <label for="edit-submitted-country"> <fmt:message key="signup.country">:</fmt:message>
                        <span title="This field is required." class="form-required">*</span>
                    </label>

                    <form:select path="countryID" id="edit-submitted-country" cssClass="form-select required">
                        <form:option value=""> <fmt:message key="signup.pleaseSelect"> </fmt:message> </form:option>
                        <form:options items="${countrys}" itemLabel="name" itemValue="objectID"/>
                    </form:select>
                    <form:errors path="countryID" cssClass="error"/>

                </div>

                    <%--<input type="submit" name="op" id="edit-submit" value="Sign up for a Free Trial" class="form-submit" />--%>


                <input class="form-submit" type="submit" id="edit-submit"
                       value='<fmt:message key="signup.demoVersion"> </fmt:message> ' onclick="this.form.submit();"/>

                <input type="hidden" name="agreeWithCondition" value="true"/>
                <input type="hidden" name="_agreeWithCondition" value="on"/>
                <input type="hidden" name="companySignedUpFrom" value="PRODUCT_TOUR_SIGNUP_jsp"/> <%--referral--%>
                <input type="hidden" name="action" value="signUp"/>

            </div>
    </form:form>
</div>
