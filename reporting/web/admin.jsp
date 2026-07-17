<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%--
  User: Dilsh0d
  Date: 23-Apr-2010
  Time: 11:44:40
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <META Http-Equiv="Pragma" Content="no-cache">
    <link rel="shortcut icon" href="images/reporting.png" type="image/x-icon">
    <title>Dynamic Reporting System Admin Page</title>
    <link href="<%=request.getContextPath()%>/style/main.css" rel="stylesheet" type="text/css">
</head>
<body>
<h2 align="center">
    <br/>
    Dynamic Reporting System Admin Login Page
    <br/><br/>
</h2>
<hr/>
<br/>
<br/>
<br/>
<div width="400px" align="center">

    <form action="<%=request.getContextPath()%>/admin" method="post" class="main-form">
        <fieldset class="form-legend">
            <legend><b style="color:#7f9db9">Admin Login</b></legend>
            <table border="0" cellpadding="5" cellspacing="10">
                <tr>
                    <td>
                        <b style="font-size:12px;text-align:right;">Login:</b>
                    </td>
                    <td>
                        <input type="text" name="adminLogin" style="width:170px" value=""/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <b style="font-size:12px;text-align:right;">Password:</b>
                    </td>
                    <td>
                        <input type="password" name="adminPassword" style="width:170px" value=""/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="right">
                        <input type="submit" name="adminSubmit" value="submit"/>
                    </td>
                </tr>
            </table>
        </fieldset>
    </form>
</div>
</body>
</html>