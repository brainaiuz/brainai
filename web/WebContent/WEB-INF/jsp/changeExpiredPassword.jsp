<%--
  Created by IntelliJ IDEA.
  User: Sherali
  Date: 12.12.2012
  Time: 18:00:43
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title">
        <fmt:message key="changepassword.createPassword"/>
    </tiles:putAttribute>
    <tiles:putAttribute name="style">
        <style>
            .error {
                color: #ff0000;
                font-weight: bold;
            }
        </style>
    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <div id="index-page" style="width:850px;  margin:19px auto 6px;">
            <b class="spiffy">
                <b class="spiffy1"><b></b></b>
                <b class="spiffy2"><b></b></b>
                <b class="spiffy3"></b>
                <b class="spiffy4"></b>
                <b class="spiffy5"></b></b>

            <div class="spiffyfg"><br/>
                <font style="color:#1f4f8f; font-size:20px; margin-right:530px;"><fmt:message
                        key="changepassword.welcomeTo"/> ${productName}</font>

                <div style="width:780px; height:1px; background:#c0c0c0;"></div>
                <div style="width:780px;" align="left"><br/><br/>
                    <b><fmt:message key="changepassword.dear"/> ${fullName}</b><br/><br/>
                    <fmt:message key="changepassword.welcomeTo"/> ${productName}
                    <fmt:message key="changepassword.web-based"/>
                    <br/><br/>
                    <b><fmt:message key="changepassword.login"/> </b><font color="#950004"> ${userName}</font><br/>
                    <br/>
                        <fmt:message key="changepassword.validityPeriod" >
                            <fmt:param value="${passwordPeriod}"/>
                        </fmt:message> <br/><br/>
                         <fmt:message key="changepassword.pleasePickANewPassword"/> <br/><br/>
                    <fmt:message key="changepassword.passwordShouldBeAtLeast"/>
                    <br/>
                    <br/>

                    <form action='<c:url value="/auth/changeExpiredPassword.html"/>' method="post">
                        <input type="hidden" name="authid" value="<c:out value="${authid}"/>">
                        <input type="hidden" name="userName" value="<c:out value="${userName}"/>">
                        <input type="hidden" name="fullName" value="<c:out value="${fullName}"/>">
                        <input type="hidden" name="companyId" value="<c:out value="${COMPANY_ID}"/>">
                        <input type="hidden" name="userId" value="<c:out value="${userId}"/>">
                        <input type="hidden" name="passwordPeriod" value="<c:out value="${passwordPeriod}"/>">

                        <p style="margin-left:112px;"><fmt:message key="changepassword.yourNewPassword"/>
                            <input id = 'pass1' type="password" name="password" value="" onkeyup="document.getElementById('error').innerHTML = '<fmt:message key="changepassword.passwordStrength"/>' + code2word(checkPassStrength(this.value))">
                        <span id="error">
                            <font style="color:red;">
                                &nbsp; <c:out value="${message}"/>
                            </font>
                        </span>
                        </p>

                        <p style="margin-left:113px;"><fmt:message key="changepassword.confirmPassword"/>&nbsp;
                            &nbsp;<input type="password"
                                         name="cPassword"
                                         value="" onblur="document.getElementById('error2').style.display = passwordsMatch(this.value,document.getElementById('pass1').value)?'none':''">
                        <span id="error2" style="display:none"><font style="color:red;">
                            &nbsp;<fmt:message key="changepassword.match"/></font>
                        </span>
                        </p>
                        <input type='hidden' id='strength' name='strength' value='b'>
                        <div align="center"><br/>
                            <input type="submit" onclick = "document.getElementById('strength').value=checkPassStrength(document.getElementById('pass1').value)" value="<fmt:message key="existinguser.continue"/>"/></div>
                    </form>
                    <br/><br/>
                </div>

            </div>

            <b class="spiffy">
                <b class="spiffy5"></b>
                <b class="spiffy4"></b>
                <b class="spiffy3"></b>
                <b class="spiffy2"><b></b></b>
                <b class="spiffy1"><b></b></b></b>
        </div>

    </tiles:putAttribute>
    <tiles:putAttribute name="script">
        <script type=text/javascript src="/passwordStrength.js"></script>
        <script type="text/javascript">
            function code2word(code) {
                if (code == 'SHORT') {
                    return "<font color='red'><b><fmt:message key="changepassword.short"/></b></font>"
                }
                if (code == 'STRONG') {
                    return "<font color='green'><b><fmt:message key="changepassword.strong"/></b></font>";
                }
                if (code == 'MEDIUM') {
                    return "<font color='blue'><b><fmt:message key="changepassword.medium"/></b></font>";
                }
                return "<font color='red'><b><fmt:message key="changepassword.weak"/></b></font>";
            }
        </script>
    </tiles:putAttribute>
</tiles:insertDefinition>