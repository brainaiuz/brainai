<%--
  Created by IntelliJ IDEA.
  User: Sherali
  Date: 15.10.2008
  Time: 13:24:18
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
        <style>
            .error {
                color: #ff0000;
                font-weight: bold;
            }
        </style>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.2">

        <link rel="stylesheet" href="/customisation/stefano/custom.css" type="text/css" />

        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
        <link rel="stylesheet" href="/customisation/stefano/ie8-prev.css" type="text/css" />
        <script src="/customisation/stefano/scripts/html5shiv.js"></script>
        <![endif]-->

        <!--[if gte IE 9]>
        <style type="text/css">
        </style>
        <![endif]-->

        <!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="/customisation/stefano/scripts/jquery.js"><\/script>')</script>
        -->

    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <div id="index-page" style="width:860px; margin: 20px auto;">
            <%--<h2 class="title"><fmt:message key="passwordReminder.title"/></h2>--%>

            <%--<p><fmt:message key="forgotpassword.txt"/> </p>--%>

            <form class="login" action='<c:url value="/forgot/forgotPassword.html"/>' method="post">
                <div class="form-group">
                    <input  class="form-control" type="text" name="email" value="" placeholder="e-mail address">
                    <em class="icon-email"></em>

                    <label> <h2 class="error" style="margin-left:5px;font-size:12px;"><c:out
                            value="${message}"/></h2>
                    </label>
                </div>
                <input class="btn btn_signIn" type="submit" value="Send">
            </form>


        </div>
    </tiles:putAttribute>

    <tiles:putAttribute name="script">

    </tiles:putAttribute>

</tiles:insertDefinition>
