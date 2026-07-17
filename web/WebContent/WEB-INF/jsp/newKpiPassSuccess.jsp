<%--
  Created by IntelliJ IDEA.
  User: Fatxulla Nigmatjonov
  Date: 15/11/14
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
                <li class="active">Reset Password</li>
            </ol>
        </div>

        <div class="container">
            <article class="post-9449 page type-page status-publish hentry" id="post-9449">
                <h1 class="page_title">Reset Password</h1>
                <div class="row products_review bottomField">
                    <div class="col-md-12 main-content"> <!-- Begin of main content area -->
                        <p>A link has been sent to the registered email address. You can set up a new password using that link.</p>
                    </div><!-- End of main content area -->
                </div><!-- #row products_review -->
            </article><!-- #post-## -->
        </div>

    </tiles:putAttribute>
</tiles:insertDefinition>
