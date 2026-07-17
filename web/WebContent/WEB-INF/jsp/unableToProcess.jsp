<%@ page import="java.util.Date" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!doctype html>
<html class="no-js" lang="en" dir="ltr">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Invalid IP Address</title>

    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/slick/slick.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/css/main.css?v=<%=new Date().getTime()%>"/>

</head>
<body>

<div class="pg_landing">

    <div class="pg_landing__container">
        <div class="pg_landing__header">
            <div class="pg_landing__logo">
                <a href="#">
                    <img src="../../mainStyles/new-ui/images/new-kpi-logo.svg?v=2" alt="logo"/>
                </a>
            </div>
        </div>
        <div class="pg_landing__main">
            <div class="pg_landing__main-innerbox">
                <div class="pg_landing__content">
                </div>
                <div class="pg_landing__sidebar">
                    <div class="cp_login" style="border: none">
                        <div class="cp_login__content">
                            <div class="cp_login__title">Unable to Process</div>
                            <div class="cp_login__subtitle" style="color: #be6d00">We are unable to process your request now. Please try again after sometime or contact support@kpi.com</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/mainStyles/new-ui/login/js/jquery.min.js"></script>
<script src="/mainStyles/new-ui/login/js/slick.min.js"></script>
<script src="/mainStyles/new-ui/login/js/main.js"></script>
</body>
</html>