<%--
  Created by IntelliJ IDEA.
  User: Fatxulla Nigmatjonov
  Date: 14/11/14
  Time: 7:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title">
        <fmt:message key="passwordReminder.title"/> ${helpHost}
    </tiles:putAttribute>
    <tiles:putAttribute name="style">

    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <div class="breadcrumb">
            <ol class="container">
                <li><a href="http://${helpHost}/">${helpHost}</a></li>
                <li class="active"><fmt:message key="passwordReminder.title"/></li>
            </ol>
        </div>

        <div class="container">

            <article class="post-9446 page type-page status-publish hentry" id="post-9446">
                <h1 class="page_title"><fmt:message key="passwordReminder.title"/></h1>
                <p class="c"><c:out value="${message}"/></p>

                <div class="row products_review bottomField">

                    <div class="col-md-4 col-md-offset-4">
                        <div class="main-content forgot_content">
                            <div class="row">
                                <div class="col-md-12">
                                    <p class="text-center"><fmt:message key="forgotpassword.txt"/></p>
                                    <hr>
                                </div>
                            </div>
                            <form action='<c:url value="/forgot/forgotPassword.html"/>' method="post">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <label><fmt:message key="emailAddres"/>:</label>
                                            <input type="text" name="email" value="" id="email" class="form-control">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <button type="submit" class="btn btn-primary btn-block btn-login-submit"><fmt:message key="sendPassword"/></button>
                                        </div>
                                    </div>
                                </div>

                            </form>
                        </div>
                    </div> <!-- End of Right sidebar, sign up form -->

                </div><!-- #row products_review -->

            </article><!-- #post-## -->
        </div>

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
