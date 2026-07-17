<%--
  Created by IntelliJ IDEA.
  User: Babayev Xushnud
  Date: 28.11.2010
  Time: 18:27:44
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
    <link rel="shortcut icon" href="/customisation/lenzoarabia.net/images/favicon.ico" type="image/x-icon"/>

    <link rel="stylesheet" href="/customisation/lenzoarabia.net/style.css" type="text/css" />

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/customisation/lenzoarabia.net/ie8-andLeft.css" type="text/css" />
    <![endif]-->


</head>
<body>
<div class="loginPage">

    <a href="/">
        <img id="logo" src="/customisation/lenzoarabia.net/images/logo_lenzo-1.png" alt="image"/>
    </a>

    <form action="/mainLogin" method="post" id="loginForm">
        <h2> Login </h2>
        <div>
            <div style="color:#ffffff; font-size:10px; margin-left:10%; margin-bottom:3px; margin-top: -20px; display:block;">
                <b>${error}</b>
            </div>
            <label for="login"> Username:
                <input id="login" class="txt" type="text" value="" name="USER_NAME"/>
            </label>

            <label for="pass"> Password:
                <input id="pass" class="txt" type="password" value="" name="USER_PASSWORD"/>
            </label>

            <div class="submitGroup submitGroup-1 ">
                <em>Sign in using:</em>
                <a href="/facebookLogin"><img src="/customisation/lenzoarabia.net/images/icon_fb.png" alt="image" /></a>
                <a href="/check"><img src="/customisation/lenzoarabia.net/images/icon_gPlus.png" alt="image" /></a>
                <a href="/liveidauth"> <img src="/customisation/lenzoarabia.net/images/icon_win.png" alt="image" /></a>
                <a href="/check?ID_PROVIDER=https://me.yahoo.com"> <img src="/customisation/lenzoarabia.net/images/icon_yahoo.png" alt="image" /></a>
            </div>

            <div class="submitGroup submitGroup-2 ">
                <a href="/forgot/forgotPassword.html">Forgot password</a>
                <input class="btnLogIn btn" type="submit" value="Login" />
            </div>
        </div>
    </form>

    <div class="copy">
        2012 &copy; <strong>Lenzo</strong> All Rights Reserved
    </div>

</div>

</body>
</html>