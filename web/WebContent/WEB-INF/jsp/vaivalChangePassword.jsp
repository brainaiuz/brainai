<%--
  User: Faxriddin
  Date: 4/15/13
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title">
        Vaival Change Password Page
    </tiles:putAttribute>
    <link href="/wfp/templates/vaival/css/style.css" rel="stylesheet" type="text/css"/>
    <tiles:putAttribute name="style">
        <style>
            body{overflow: auto;}
            .error {color: #ff0000;font-weight: bold; }
        </style>
    </tiles:putAttribute>

    <tiles:putAttribute name="body">
        <img id="background" src="/wfp/templates/vaival/css/images/bg.jpg" alt="" title="" style="height:710px;">

        <div id="wrapper">
            <a class="logo" href="#">Vaival Chage Password Page</a>

            <div id="cover">

                <div id="content" class="full-main">

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
                                <fmt:message key="changepassword.your"/> ${productName}
                                <fmt:message key="changepassword.administrator"/> ${productName}
                                <fmt:message key="changepassword.accountForYour"/> <br/><br/>
                                <b><fmt:message key="changepassword.company"/> </b><font color="#950004"> ${compName}</font><br/>
                                <b><fmt:message key="changepassword.userRole"/> </b><font color="#950004"> ${roles} </font><br/>
                                <b><fmt:message key="changepassword.login"/> </b><font color="#950004"> ${email}</font><br/><br/>

                                <fmt:message key="changepassword.pleasePickApassword"/> <br/><br/>
                                <fmt:message key="changepassword.passwordShouldBeAtLeast"/>
                                <br/>
                                <br/>

                                <form action='<c:url value="/password/changePassword.html"/>' method="post">
                                    <input type="hidden" name="checkUser" value="<c:out value="${checkUser}"/>">

                                    <p style="margin-left:112px;"><fmt:message key="changepassword.yourNewPassword"/> <input
                                            id = 'pass1' type="password" name="password"
                                            value="" onkeyup="document.getElementById('error').innerHTML = '<fmt:message key="changepassword.passwordStrength"/>' + code2word(checkPassStrength(this.value))">
                        <span id="error"><font style="color:red;">
                            &nbsp; <c:out value="${message}"/></font>
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

                </div>

                <div class="footer">
                    <img src="/wfp/templates/vaival/css/images/footer-vaival-logo.png">
                    <span>Copyrights 2010 All Rights Reserved</span>
                    <div class="footer-icon">
                        <a href="#"><img src="/wfp/templates/vaival/css/images/su-icon.png"></a>
                        <a href="#"><img src="/wfp/templates/vaival/css/images/yt-icon.png"></a>
                        <a href="#"><img src="/wfp/templates/vaival/css/images/facebook-icon.png"></a>
                        <a href="#"><img src="/wfp/templates/vaival/css/images/rss-icon.png"></a>
                        <a href="#"><img src="/wfp/templates/vaival/css/images/twitter-icon.png"></a>
                    </div>
                </div>

            </div>

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
    </tiles:putAttribute>
</tiles:insertDefinition>