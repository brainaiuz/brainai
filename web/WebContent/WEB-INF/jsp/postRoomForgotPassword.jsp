<%--
  User: Ilhom
  Date: 31.10.13
  Time: 19:56
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../../customisation/postroom/css/normalize.css"/>
    <link rel="stylesheet" href="../../customisation/postroom/css/style.css"/>
</head>
<body>
<div id="pageTop"></div>
<div id="header" class="clearfix">
    <a href="#" id="logo"><img src="../../customisation/postroom/img/logo.png" alt="logo"/></a>

    <div id="welcome">Welcome to your Post Room</div>
</div>
<div id="loginWrap">
    <fieldset>
        <legend>Forgot password</legend>
        <form action="/forgot/forgotPassword.html" method="post" id="loginBox">
            ${message}
            <input type="text" name="email" class="login-text"/>
            <input type="submit" value="Send to email" class="login-button"/>

            <div class="layer"></div>
            <div class="layer layer2"></div>
        </form>

    </fieldset>
    <div class="info">Access to this system is strickly for postroom customers</div>
</div>
<div id="footer"></div>

</body>
</html>