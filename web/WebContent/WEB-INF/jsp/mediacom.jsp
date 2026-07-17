<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 28.11.2010
  Time: 18:27:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MediaCom</title>
    <meta name="vs_showGrid" content="False">
    <meta name="vs_defaultClientScript" content="JavaScript">
    <meta name="vs_targetSchema" content="http://schemas.microsoft.com/intellisense/ie5">

    <link rel="stylesheet" type="text/css" href="/customisation/mediacom/Style.css"/>
    <script src="/customisation/mediacom/AC_RunActiveContent.js" type="text/javascript"></script>


</head>
<body>
<form method="post" action="/mainLogin" id="log-box">
    <input name="__VIEWSTATE" id="__VIEWSTATE"
           value="/wEPDwUKLTkwODI3NzI3NmQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFCGNtZExvZ2ludQPuMe4uLAY5dkO8dDGZXEtiTFs="
           type="hidden">

    <input name="__EVENTVALIDATION" id="__EVENTVALIDATION"
           value="/wEWBALw+begCAKl1bK4CQK1qbSRCwKFoZPNA88j4NR/Olwt4SgRWLGK0hq20W/5" type="hidden">
    <table id="Table1" align="center" border="0" cellpadding="0" cellspacing="0" width="960">
        <tbody>
        <tr>
            <td>
                <table id="Table3" align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
                    <tbody>
                    <tr>
                        <td class="logo">
                            <img src="/customisation/mediacom/images/media_logo.jpg"></td>
                    </tr>
                    <tr>
                        <td>
                            <table id="Table2" background="/customisation/mediacom/images/menu_pt.gif" border="0"
                                   cellpadding="0"
                                   cellspacing="0" width="100%">
                                <tbody>
                                <tr>
                                    <td>
                                        <img src="/customisation/mediacom/images/menu_left.jpg"></td>
                                    <td style="width: 100%;" align="right">
                                        <img src="/customisation/mediacom/images/menu_right.jpg"></td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding-left: 11px;">
                            <object width="938" height="173">
                                <param name="movie" value="/customisation/mediacom/images/sub_flash.swf">
                                <embed src="/customisation/mediacom/images/sub_flash.swf" width="938" height="173">
                                </embed>
                            </object>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <img src="/customisation/mediacom/images/spacer.gif"
                                 width="1" height="15"></td>
                    </tr>
                    <tr>
                        <td align="center">
                            <table border="0" cellpadding="0"
                                   cellspacing="0" width="938">
                                <tbody>
                                <tr>
                                    <td style="width: 344px;"
                                        class="gridRightList" valign="top">
                                        Welcome to MediaComWorld.
                                        Please enter you username and password to log in or register if you are a
                                        new user.<br>
                                        <br>

                                        <br>
                                        <br>
                                        <a
                                                href="http://d10115156.u73.c7.ixwebhosting.com/register.aspx"
                                                class="gridList" style="text-decoration: none;">Register</a><br>
                                        <a
                                                href="http://d10115156.u73.c7.ixwebhosting.com/forgot.aspx"
                                                class="gridList" style="text-decoration: none;">Forgot Password</a></td>
                                    <td>
                                        <img
                                                src="/customisation/mediacom/images/spacer.gif" width="45" height="1">
                                    </td>
                                    <td>
                                        <table id="Table4"
                                               border="0" cellpadding="0" cellspacing="0" width="356">
                                            <tbody>
                                            <tr>
                                                <td colspan="3">
                                                    <img
                                                            src="/customisation/mediacom/images/log_top.jpg"></td>
                                            </tr>
                                            <tr>
                                                <td
                                                        style="padding-left: 18px; width: 147px;"
                                                        background="/customisation/mediacom/images/log_user_bg.jpg">
                                                    <input type="text" class="input" id="login" name="USER_NAME" style="color: rgb(229, 21, 101);
border-width: 0; width: 133px;" type="text"></td>
                                                <td>
                                                    <img
                                                            src="/customisation/mediacom/images/log_sep.jpg"></td>
                                                <td
                                                        style="padding-left: 8px; width: 163px;"
                                                        background="/customisation/mediacom/images/log_pass_bg.jpg">
                                                    <input
                                                            class="input" id="pass" name="USER_PASSWORD" style="color: rgb(229, 21, 101);
border-width: 0; width: 130px;" type="password"></td>
                                            </tr>
                                            <tr>
                                                <td colspan="3">
                                                    <img
                                                            src="/customisation/mediacom/images/log_mid.jpg"></td>
                                            </tr>
                                            <tr>
                                                <td colspan="3">
                                                    <table
                                                            border="0" cellpadding="0" cellspacing="0">
                                                        <tbody>
                                                        <tr>
                                                            <td style="color: rgb(229, 21, 101); border-width: 0; width: 133px;">
                                                                <img src="/customisation/mediacom/images/log_help.jpg">
                                                                <%--<b style="white-space:nowrap;font-size:9px;">${error}</b>--%>
                                                                <%--${error}--%>
                                                            </td>
                                                            <td>
                                                                <input type="image"
                                                                       src="/customisation/mediacom/images/cmd_login.jpg"
                                                                       value=""/>
                                                            </td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="3">
                                                    <img
                                                            src="/customisation/mediacom/images/log_bottom.jpg"></td>
                                            </tr>
                                            <tr>
                                                <td colspan="3"
                                                    class="gridList">
                                                    <img
                                                            src="/customisation/mediacom/images/spacer.gif" width="1"
                                                            height="25">
                                                    <b style="font-size: 9px;">${error}</b>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">

                            <table id="Table1" border="0" cellpadding="0" cellspacing="0"
                                   width="960">
                                <tbody>
                                <tr>
                                    <td><img alt="" src="/customisation/mediacom/images/spacer.gif" width="1"
                                             height="15"></td>
                                </tr>
                                <tr>
                                    <td style="height: 1px; background-color: rgb(237, 236, 236);"><img
                                            src="/customisation/mediacom/images/spacer.gif" width="1" height="1"></td>
                                </tr>
                                <tr>
                                    <td class="footer">Copyright © 2010 <span style="color: rgb(229, 21,
101);">Mediacom</span>.
                                        All rights reserved.
                                    </td>
                                </tr>
                                <tr>
                                    <td style="height: 1px; background-color: rgb(237, 236, 236);"><img
                                            src="/customisation/mediacom/images/spacer.gif" width="1" height="1"></td>
                                </tr>
                                </tbody>
                            </table>

                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        </tbody>
    </table>
</form>
</body>
</html>