<%--
  Created by IntelliJ IDEA.
  User: Fatxulla Nigmatjonov
  Date: 17/11/14
  Time: 15:15:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title" value="Multiple Company Selection"/>
    <tiles:putAttribute name="style">

    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <script type="text/javascript" src="/jsscript/landing.js"></script>
        <script type="text/javascript">
            function setCheked() {
                form = document.getElementById('newCompany');
                form.submit();
            }
            function clickStopper(e) {
                e.preventDefault();
            }
            var isInIFrame = (window.location != window.parent.location) ? true : false;
            if (isInIFrame) {
                var query = "?adminFName=${newCompany.adminFName}&adminLName=${newCompany.adminLName}&adminEmail=${newCompany.adminEmail}"
                        +"&name=${newCompany.name}&adminActive=${newCompany.adminActive}&signedUpPage=${newCompany.signedUpPage}&countryID=${newCompany.countryID}"
                        + "&stateID=${newCompany.stateID}&workArea=${newCompany.workArea}&phone=${newCompany.phone}&setUp=${newCompany.setUp}"
                        + "&currencyID=${newCompany.currencyID}&active=${newCompany.active}&clientSingUpIPAddress=${newCompany.clientSingUpIPAddress}"
                        +"&agreeWithCondition=${newCompany.agreeWithCondition}&countryName=${newCompany.countryName}&googleAppsDomain=${newCompany.googleAppsDomain}"
                        +"&companySignedUpFrom=${newCompany.companySignedUpFrom}&fromFederatedLogin=${newCompany.fromFederatedLogin}&locale=${newCompany.locale}&iframe=1";
                parent.location = "http://${hostName}/signup/existingUser.html" +query;
            }

        </script>

        <div class="breadcrumb">
            <ol class="container">
                <li><a href="/">Home</a></li>
                <li class="active">Sign in</li>
            </ol>
        </div>

        <div class="container">
            <article class="post-9397 page type-page status-publish hentry" id="post-9397">
                <h1 class="page_title">Sign in</h1>

                <div class="row products_review bottomField">

                    <form:form method="post" commandName="newCompany" action="/signup/registerCompany">
                        <input type="hidden" name="adminFName" value="${newCompany.adminFName}"/>
                        <input type="hidden" name="adminLName" value="${newCompany.adminLName}"/>
                        <input type="hidden" name="adminEmail" value="${newCompany.adminEmail}"/>
                        <input type="hidden" name="name" value="${newCompany.name}"/>
                        <input type="hidden" name="adminActive" value="${newCompany.adminActive}"/>
                        <input type="hidden" name="signedUpPage" value="${newCompany.signedUpPage}"/>
                        <input type="hidden" name="countryID" value="${newCompany.countryID}"/>
                        <input type="hidden" name="stateID" value="${newCompany.stateID}"/>
                        <input type="hidden" name="workArea" value="${newCompany.workArea}"/>
                        <input type="hidden" name="phone" value="${newCompany.phone}"/>
                        <input type="hidden" name="setUp" value="${newCompany.setUp}"/>
                        <input type="hidden" name="currencyID" value="${newCompany.currencyID}"/>
                        <input type="hidden" name="active" value="${newCompany.active}"/>
                        <input type="hidden" name="clientSingUpIPAddress" value="${newCompany.clientSingUpIPAddress}"/>
                        <input type="hidden" name="agreeWithCondition" value="${newCompany.agreeWithCondition}"/>
                        <input type="hidden" name="countryName" value="${newCompany.countryName}"/>
                        <input type="hidden" name="googleAppsDomain" value="${newCompany.googleAppsDomain}"/>
                        <input type="hidden" name="companySignedUpFrom" value="${newCompany.companySignedUpFrom}"/>
                        <input type="hidden" name="fromFederatedLogin" value="${newCompany.fromFederatedLogin}"/>
                        <input type="hidden" name="locale" value="${newCompany.locale}"/>

                        <div class="col-md-12 main-content"> <!-- Begin of main content area -->


                            <p> <fmt:message key="existinguser.youAlready"/>
                                <a href="javascript:setCheked()" onclick="this.addEventListener('click', clickStopper, false)">
                                    <fmt:message key="existinguser.continue"/>
                                </a>
                                <fmt:message key="existinguser.inOrder"/>
                            </p>
                            <p>  <fmt:message key="existinguser.YouForgot"/>
                                <a href="/forgot/forgotPassword.html">
                                    <fmt:message key="existinguser.iForgot"/>
                                </a></p>
                            <p>   <fmt:message key="existinguser.clickHere"/>
                                <a href="/index.html">
                                    <fmt:message key="existinguser.cancel"/>
                                </a>
                                <fmt:message key="existinguser.andGoBack"/> </p>

                        </div><!-- End of main content area -->
                    </form:form>
                </div><!-- #row products_review -->

            </article><!-- #post-## -->
        </div> <!-- End of main container -->
        <!--New Google Analytics script-->
        <script type="text/javascript">
            var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
            document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
        </script>
        <script type="text/javascript">
            try {
                var pageTracker = _gat._getTracker("UA-355982-15");
                pageTracker._trackPageview();
            } catch(err) {
            }</script>
        <!--New Google Analytics script-->


    </tiles:putAttribute>
</tiles:insertDefinition>