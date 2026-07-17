<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Victechweb Forget Password Page</title>
    <link href="/wfp/templates/victor/style.css" rel="stylesheet" type="text/css"/>
</head>
        <body>

            <!--begin #wrapper-->
            <div id="wrapper">

                <!--begin login-content and login-content-inner-->
                <div class="login-content">
                    <a href="#" class="logo"><img src="/wfp/templates/victor/images/logo.jpg" width="246" height="83" alt="" /></a>
                    <h2 class="login-title">Password Reminder</h2>
                    <form action="/forgot/forgotPassword.html" class="login-form" method="post">
                        <img src="/wfp/templates/victor/images/lock.png" alt="" class="lock left" />

                        <div class="overhide">
                                <p>Please enter your e-mail address which you have provided during sign up process and click on
                                <strong>Send Password</strong> button.</p>

                                <label type="text" name="email" value="" for="email" class="login left ">E-mail address:</label>
                                <input class="right" type="text" name="email" id="email" />
                                <h2 class="error" style="margin-left:5px;font-size:12px;"> ${message}</h2>
                        </div>
                        <div class="btn-submit right clear">
                                <input type="submit" value="Send" />
                        </div>
                    </form>

                    <img src="/wfp/templates/victor/images/bg-btm-login-form.png" alt="" class="clear" />
                    <img src="/wfp/templates/victor/images/bg-shadow.jpg" alt="" class="clear" />
                    <address>VicTech Integrated Technologies 409 Running Doe Ct., Suwanee, GA 30024<br />
                    Phone: 1-888-617-2829 Fax: 1-928-441-4056</address>
                </div>
                <br>

            </div>
            <!--END #wrapper-->
        </body>
</html>