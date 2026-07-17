<%--
  Created by IntelliJ IDEA.
  User: Virus
  Date: 7/15/11
  Time: 6:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head><title>Restore your password</title>
    <link href="../../customisation/atm/algoSelect/style.css" type="text/css" rel="stylesheet" rev="stylesheet"/>
</head>
<body>
<div class="wrapper">
    <div id="cover">
        <div style="width:100%;height:300px;margin-top:5px" align="center">
            <h2 class="title">Password Reminder</h2>

            <p>Please enter your e-mail address which you have provided during sign up process and click on “Send
                Password”
                button.<br>
                Your login details will be sent to your e-mail address</p>

            <form action='/forgot/forgotPassword.html' method="post">
                <table style="margin-bottom:15px;">
                    <tr>
                        <td style="vertical-align:middle;">E-mail address:</td>
                        <td style="vertical-align:middle;"><input type="text" name="email" value=""
                                                                  style="margin-right:5px;margin-left:5px;"></td>
                        <td><span><input type="submit" value="Send Password"/></span></td>
                        <td style="vertical-align:middle;">
                            <h2 class="error" style="margin-left:5px;font-size:12px;">
                                ${message}</h2>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>

</body>
</html>