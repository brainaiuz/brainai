<%@ tag import="com.edatasite.workforce.gwt.core.client.ui.Constants" %>
<%@ tag body-content="tagdependent" isELIgnored="false" %>
<%@ attribute name="serviceType" required="true" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script type="text/javascript" src="/jsscript/ajax.js"></script>

<form:form method="post" commandName="newCompany">
    <table cellpadding="0" cellspacing="0">
        <tr>
            <td colspan="2">
                <div style="padding:3px;margin:10px 0 10px 0; background: #fcefc8; border:1px solid #edd28a;">
                    <img border="0" style="margin:4px;float: left;" alt="Google Account"
                         title="Login using your Google Account" src="/i/google.jpg"/>
                    <a href="/check?service=${serviceType}" style="font-size:12px; color:#205476">
                        No need to create a new account. You may use your Google Account to Sign In
                    </a>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="2"><h1>Sign Up Now. <span>7 days FREE Trial</span></h1></td>
        </tr>
        <tr>
            <td height="5" colspan="2"/>
        </tr>
        <tr>
            <td>First Name:<span>*</span></td>
            <td>
                <form:input path="adminFName"/>
            </td>
        </tr>
        <tr>
            <td/>

            <td>
                 <span>
                    <form:errors path="adminFName"/>
               </span>
            </td>
        </tr>
        <tr>
            <td><label for="lastname">Last Name:<span>*</span></label></td>
            <td>
                <form:input path="adminLName"/>
            </td>
        </tr>
        <tr>
            <td/>
            <td><span>
                    <form:errors path="adminLName"/>
              </span>
            </td>
        </tr>
        <tr>
            <td><label for="mail">E-mail:<span>*</span></label></td>

            <td>
                <form:input path="adminEmail" onblur="sendRequest('GET','/signup/freeTrial.html?action=handleAjaxRequest&adminEmail='+this.value,this.value)"/>
            </td>
        </tr>
        <tr>
            <td/>
            <td><p>Please enter an existing e-mail so<br/>
                activation link will be sent to it.</p></td>
        </tr>
        <tr>
            <td/>
            <td><span>
               <span id="emailValidate" class="error"><form:errors path="adminEmail" cssClass="error" htmlEscape="false"/></span>
                </span></td>
        </tr>
        <tr>
            <td><label for="company">Company Name:<span>*</span></label></td>

            <td>
                <form:input path="name"/>

            </td>
        </tr>
        <tr>
            <td/>
            <td><span>
                    <form:errors path="name"/>
                </span>
            </td>
        </tr>

        <tr>
            <td>
                <label for="Phone">Phone:<span>*</span></label></td>
            <td>
                <form:input path="phone" onkeypress="return phoneValidation(event)" id="Phone"/>
            </td>
        </tr>

        <tr>
            <td/>
            <td><span>
                    <form:errors path="phone"/>
                </span>
            </td>
        </tr>
        <tr>
            <td>Country:<span>*</span></td>
            <td>
                <form:select path="countryID"
                             cssClass="formsize">
                    <form:option value="">Please select</form:option>
                    <form:options items="${countrys}" itemLabel="name" itemValue="objectID"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td/>
            <td><span>
                    <form:errors path="countryID"/>
                </span>
            </td>
        </tr>

        <tr>
            <td colspan="2">
                <form:checkbox path="agreeWithCondition" id="terms"/>
                <label for="terms" id="agree">
                    I agree with the
                    <a href="<%=Constants.DRUPAL_DOMEN%>content/terms-of-use" target="_blank">
                        Terms and Conditions
                    </a>
                </label>
                &nbsp;
                <button type="submit" id="submitButton">Sign Up</button>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <span>
                <form:errors path="agreeWithCondition"/>
              </span>
            </td>
        </tr>
        <tr>
            <td height="10" colspan="2"/>
        </tr>
        <tr>
            <td colspan="2">
                <p><b>Note:</b> Please be advised that we use your personal
                    information to correspond with you, and to process
                    your requests for our services. We do not sell, trade
                    or rent your personal information to the third parties.
                </p></td>
        </tr>
    </table>
    <input type="hidden" name="service" value="${serviceType}">
    <input type="hidden" name="action" value="signUp"/>
</form:form>