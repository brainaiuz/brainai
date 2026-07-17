<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Vaival Forget Password Page</title>
    <link href="/wfp/templates/vaival/css/style.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/wfp/templates/vaival/js/jquery-1.6.1.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            $(".inputarea-input").addClass("idle");
            $(".inputarea-input").focus(function() {
                $(this).addClass("activeField").removeClass("idle");
            }).blur(function() {
                $(this).removeClass("activeField").addClass("idle");
            });
        });
    </script>
</head>
<body>
<img title="" alt="" src="/wfp/templates/vaival/css/images/bg.jpg" id="background">

<div id="wrapper">
    <a href="#" class="logo">Vaival Login Page</a>

    <div class="sign-in">
        <div class="top-sign-in"></div>
        <div class="center-forget-password">
            <img src="/wfp/templates/vaival/css/images/forget-password-icon.png" class="user-icon"/>
            <img src="/wfp/templates/vaival/css/images/password-reminder.png" class="sign-in-txt"/>

            <div class="sign-in-shadow"></div>

            <div id="inputarea">
                <center><i>
                    <h2 style="font-weight:bold;font-size:14px;">Your login details have been sent to your e-mail
                        address. You can access to
                        Vaival account with your login details and enjoy full features of the tool.</h2>
                    <br/></i>
                </center>
                <div><br></div>
                <br>
                <a href="http://projects.vaival.com">Go back to Sign In Page</a>
            </div>
        </div>
        <div class="btm-sign-in"></div>
        <div class="footer">
            <img src="/wfp/templates/vaival/css/images/footer-vaival-logo.png"/>
            <span>Copyrights 2010 All Rights Reserved</span>

            <div class="footer-icon">
                <a href="#"><img src="/wfp/templates/vaival/css/images/su-icon.png"/></a>
                <a href="#"><img src="/wfp/templates/vaival/css/images/yt-icon.png"/></a>
                <a href="#"><img src="/wfp/templates/vaival/css/images/facebook-icon.png"/></a>
                <a href="#"><img src="/wfp/templates/vaival/css/images/rss-icon.png"/></a>
                <a href="#"><img src="/wfp/templates/vaival/css/images/twitter-icon.png"/></a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
