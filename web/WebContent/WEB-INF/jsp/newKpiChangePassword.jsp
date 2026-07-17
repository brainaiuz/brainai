<%--
  Created by IntelliJ IDEA.
  User: Fatxulla Nigmatjonov
  Date: 15.11.2014
  Time: 19:51:43
  To change this template use File | Settings | File Templates.
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
        <div class="breadcrumb">
            <ol class="container">
                <li><a href="/">Home</a></li>
                <li class="active"><fmt:message key="changepassword.welcomeTo"/> ${productName}</li>
            </ol>
        </div>

        <div class="container">
            <article class="post-9541 page type-page status-publish hentry" id="post-9541">
                <h1 class="page_title"><fmt:message key="changepassword.welcomeTo"/> ${productName}</h1>

                <div class="row products_review bottomField">


                    <div class="col-md-12 main-content page-activation">
                        <h1><fmt:message key="changepassword.dear"/> <span>${fullName}</span></h1><hr>
                        <p><fmt:message key="changepassword.welcomeTo"/> ${productName} <fmt:message key="changepassword.web-based"/></p>
                        <p><fmt:message key="changepassword.your"/> ${productName}  <fmt:message key="changepassword.administrator"/> ${productName}
                            <fmt:message key="changepassword.accountForYour"/></p>
                        <dl class="dl-horizontal">
                            <dt><fmt:message key="changepassword.company"/></dt>
                            <dd>${compName}</dd>
                            <dt><fmt:message key="changepassword.userRole"/></dt>
                            <dd>${roles}</dd>
                            <dt><fmt:message key="changepassword.login"/></dt>
                            <dd>${email}</dd>
                        </dl>
                        <hr>
                        <p> <fmt:message key="changepassword.pleasePickApassword"/></p>
                        <p><fmt:message key="changepassword.passwordShouldBeAtLeast"/></p>
                        <br>
                        <div class="row">
                            <div class="col-md-5">
                               <form role="form" action='<c:url value="/password/changePassword.html"/>' method="post">
                                   <input type="hidden" name="checkUser" value="<c:out value="${checkUser}"/>">
                                    <label class="col-md-4 control-label" for="inputPassword2"><fmt:message key="changepassword.yourNewPassword"/></label>
                                    <div class="col-md-8">
                                        <input id = 'pass1' type="password" name="password"  class="form-control"
                                                value="" onkeyup="document.getElementById('error').innerHTML = '<fmt:message key="changepassword.passwordStrength"/>' + code2word(checkPassStrength(this.value))">
                                        <span id="error"><font style="color:red;">
                                            &nbsp; <c:out value="${message}"/></font>
                                        </span>
                                    </div>
                                    <label class="col-md-4 control-label" for="inputPassword3"><fmt:message key="changepassword.confirmPassword"/></label>
                                    <div class="col-md-8">
                                        <input type="password" name="cPassword" class="form-control"
                                               value="" onblur="document.getElementById('error2').style.display = passwordsMatch(this.value,document.getElementById('pass1').value)?'none':''">
                                        <span id="error2" style="display:none"><font style="color:red;">
                                            &nbsp;<fmt:message key="changepassword.match"/></font>
                                        </span>
                                    </div>
                                   <input type='hidden' id='strength' name='strength' value='b'>
                                    <div class="col-md-offset-4 col-md-8">
                                        <button class="btn btn-default btn-block" type="submit" onclick = "document.getElementById('strength').value=checkPassStrength(document.getElementById('pass1').value)" ><fmt:message key="existinguser.continue"/></button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div><!-- End of main content area -->
                </div><!-- #row products_review -->

            </article><!-- #post-## -->


        </div> <!-- End of main container -->

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