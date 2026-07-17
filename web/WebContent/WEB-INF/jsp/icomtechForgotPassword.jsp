<%--
  Created by IntelliJ IDEA.
  User: developer
  Date: 4/7/12
  Time: 1:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>
        I-ComTech Interactive Login page
    </title>
    <link href="/wfp/templates/comtech/style.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<!--begin #wrapper-->
<div id="wrapper">

    <!--begin login-content and login-content-inner-->
    <div class="login-content">
        <a href="#" class="logo"><img src="/wfp/templates/comtech/images/logo.jpg" width="189" height="76" alt=""/></a>

        <h2 class="login-title">Password Reminder</h2>

        <form action="/forgot/forgotPassword.html" class="login-form" method="post">
            <img src="/wfp/templates/comtech/images/lock.png" alt="" class="lock left"/>

            <div class="overhide">
                <p>Please enter your e-mail address which you have provided during sign up process and click on <strong>
                    Send Password</strong> button.
                </p>
                <label for="email" class="login left ">E-mail address:</label>
                <input class="right" type="text" name="email" id="email"/>
            </div>
            <div class="btn-submit right clear">
                <input type="submit" value="Send"/></div>
        </form>
        <img src="/wfp/templates/comtech/images/bg-btm-login-form.png" alt="" class="clear"/>
        <img src="/wfp/templates/comtech/images/bg-shadow.jpg" alt="" class="clear"/>
        <address>80, Seletar Aerospace View, #01-01<br/>
            Singapore ( 797563 )
        </address>
    </div>
    <!--END login-content-->
</div>
<!--END #wrapper-->

</body>
</html>