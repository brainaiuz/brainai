<%@ page import="com.edatasite.workforce.gwt.core.client.ui.Constants" %>
<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%--
  Created by IntelliJ IDEA.
  User: Sherzod
  Date: Sep 30, 2009
  Time: 11:54:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:fb="http://www.facebook.com/2008/fbml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Kanokla Networks</title>
    <%--<link type="image/x-icon" href="/landing/images/favicon.ico" rel="shortcut icon">--%>
    <link rel="shortcut icon" href="/customisation/kpi/images/favicon.ico" type="image/x-icon"/>
    <link rel="apple-touch-icon" href="/mobile_sources/images/apple-touch-icon.png">
    <link rel="stylesheet" type="text/css" href="/customisation/kanokla/kanokla.css"/>

</head>
<body>
<%--<div id="outer">--%>
<!--wrapper-->
<div id="heder_free">
    <div id="header">
        <div id="logo_site-1">
            <img style="width:200px; height:45px; " src="/customisation/kanokla/images/logo.jpg"/>
            <br>
            <br>
        </div>

        <!-- Start #nav-menu -->
        <div id="block-nice_menus-1" class="clear-block block block-nice_menus">

            <ul class="nice-menu nice-menu-down" id="nice-menu-1">
                <li><a href="<%=Constants.DOMEN%>" title="" class="active">Home</a></li>

                <li class="menuparent"><a href="<%=Constants.DRUPAL_DOMEN%>content/product-tour" title="Product Tours">Tour</a>
                    <ul>
                        <li>
                            <a href="<%=Constants.DRUPAL_DOMEN%>content/take-project-management-software-screenshot-tour"
                               title="Project Management Tour">Project Management &amp; Time Keeping</a></li>
                        <li><a href="<%=Constants.DRUPAL_DOMEN%>content/crm-campaign-contact-and-notes"
                               title="CRM, Campaign, Contact and Notes">CRM, Campaign, Contact and Notes</a></li>
                        <li>
                            <a href="<%=Constants.DRUPAL_DOMEN%>content/hrms-goal-management-employee-profile-and-employee-self-service"
                               title="HRMS, Goal Management, Employee Profile and Employee Self Service">HRMS, Goal
                                Management, Employee Profile and Employee Self Service</a></li>
                        <li>
                            <a href="<%=Constants.DRUPAL_DOMEN%>content/take-finance-and-accounting-software-screenshot-tour"
                               title="Take a finance and accounting software screenshot tour">Sales Order, Invoicing and
                                Record Keeping</a></li>
                        <li>
                            <a href="<%=Constants.DRUPAL_DOMEN%>content/take-two-way-simple-appraisal-and-360%C2%B0-review-software-screenshot-tour"
                               title="Take a Two Way Simple Appraisal and 360° Review software screenshot tour">Two way
                                simple appraisal and 360° review software</a></li>
                        <li>
                            <a href="<%=Constants.DRUPAL_DOMEN%>content/staff-management-and-attendance-tracking-software-screenshots"
                               title="Staff management and Attendance tracking software screenshots">HR, Attendance
                                Tracking, Staff Management</a></li>
                    </ul>
                </li>

                <li><a href="<%=Constants.DOMEN%>Pricing.html" title="Pricing">Pricing</a></li>
                <li><a href="<%=Constants.DRUPAL_DOMEN%>content/workforcetrack-price-comparison"
                       title="WorkforceTrack comparison to other popular products">Compare Us</a></li>
                <li class="menuparent"><a href="<%=Constants.DRUPAL_DOMEN%>content/products" title="Our ERP Products">Products</a>
                    <ul>
                        <li><a href="http://www.workforcetrack.com/landings/landingPM2.html"
                               title="Online Project Management Software">Online Project Management Software</a></li>
                        <li><a href="<%=Constants.DRUPAL_DOMEN%>landings/landingPA2.html"
                               title="Two Way Simple Appraisal and 360° Review">Two Way Simple Appraisal and 360°
                            Review</a></li>
                        <li><a href="<%=Constants.DRUPAL_DOMEN%>landings/landingAva.html"
                               title="Staff Management and Attendance Tracking">Staff Management and Attendance
                            Tracking</a></li>
                        <li><a href="<%=Constants.DRUPAL_DOMEN%>FinanceLanding.html"
                               title="Bookkeeping and Finance Workflow">Bookkeeping and Finance Workflow</a></li>
                    </ul>
                </li>
                <li><a href="<%=Constants.DRUPAL_DOMEN%>content/news">News</a></li>

                <li><a href="http://www.workforcetrack.com/content/wft-demo-1" title="Product overview video">Demo</a>
                </li>
            </ul>
        </div>
        <!-- End #nav-menu -->
    </div>
</div>


<!-- 	End #header -->

<!--start content-->
<div id="contbody">
    <div class="wrapper content">
        <div class="content_top"></div>
        <!--Start Sign In-->
        <%--<form method="post" id="log-box" action="/mainLogin">--%>

            <div class="sign_in">
                <div class="signin_top">
                    <h1 style="background-color:#30A1B3; width:80px; border-top:1px solid #278795; border-left:1px solid #278795; border-right:1px solid #278795; color:#FFFFFF;">
                        Sign In </h1>
                </div>
                <div class="signin_cont" style="background-color:#30A1B3;">
                    <div style="color:red; font-size:10px;margin-left:70px;height:12px;display:block;"><b>${error}</b>
                    </div>
                    <ul class="login_form">
                        <li>
                            <label for="login" style=" color:#FFFFFF;">Username:</label>
                        </li>
                        <li>
                            <input type="text" class="input" id="login" name="USER_NAME"/>
                        </li>
                        <li>
                            <label for="pass" style=" color:#FFFFFF;">Password:</label>
                        </li>

                        <li><input type="password" class="input" id="pass" name="USER_PASSWORD"/></li>
                        <li>
                            <input type="checkbox" class="input" id="rememberme" name="REMEMBER_ME"
                                   style="width:auto;height:auto;border:none;background:none;"/>
                            <label for="rememberme" style=" color:#FFFFFF;">Remember me</label>
                        </li>
                        <li>
                            <a href="/forgot/forgotPassword.html" class="checkbox" style=" color:#FFFFFF;">
                                Forgot password?
                            </a>


                            <input style="background:#196C79" type="image"
                                   src="/customisation/kanokla/images/login_bt_kanolka.gif" value=""/>

                        </li>
                    </ul>
                    <h1 style="color:#FFFFFF">Sign-In Using</h1>
                    <ul class="icon">
                        <li><a href="/enterGoogleDomain.html"><img src="/customisation/kanokla/images/google_apps.png"
                                                                   title="Click to login using your Google Apps account (custom domain)"/></a>
                        </li>
                        <li style="padding-left:30px"><a href="/check"><img src="/loginpage/images/google_icon.gif"
                                                                            title="Click to login using your Google account"/></a>
                        </li>
                        <li style="padding-left:1px"><a href="/check" rel="nofollow"
                                                        title="Click to login using your Google account"><span>Google</span></a>
                        </li>


                        <li style="padding-left:30px"><a href="/liveidauth"><img src="/loginpage/images/win_icon.gif"
                                                                                 title="Click to login using your Windows Live account"/></a>
                        </li>
                        <li style="padding-left:1px"><a href="/liveidauth" rel="nofollow"
                                                        title="Click to login using your Windows Live account"><span>Windows Live</span></a>
                        </li>

                    </ul>
                    <ul class="icon">
                        <li style="padding-left:45px"><a href="/check?ID_PROVIDER=https://me.yahoo.com"
                                                         title="Click to login using your Yahoo account"><img
                                src="/loginpage/images/yahoo-account.jpg"/></a></li>
                        <li><a href="/check?ID_PROVIDER=https://me.yahoo.com" rel="nofollow"
                               title="Click to login using your Yahoo account"><span>Yahoo</span></a></li>
                        <li style="padding-left:30px; padding-bottom:10px;">
                            <fb:login-button perms="email" onlogin="window.location = '/facebookLogin'" v="2"><fb:intl>
                                <span style="color:#FFFFFF; font-size:13px; padding-bottom:10px; "> Facebook </span>
                            </fb:intl></fb:login-button></li>
                    </ul>
                </div>
            </div>

        </form>
        <!--End Sign In-->
        <!--Start Sign Up-->
        <div class="sign_up">
            <div class="signup_top">
                <h1 style="color:#135D69; font-size:18px; font-weight:normal; ">Sign Up</h1>
            </div>
            <div class="signup_cont">
                <div class="signup_br">
                    <h1 style="color:#135D69;">Don't have an account? </h1>

                    <div class="free_but">
                        <a href="/signup/freeSignup.html"><img
                                src="/customisation/kanokla/images/sign_bt_bgfor_kanolka.png"/></a>
                    </div>
                    <div class="need_help">
                        <!-- http://www.LiveZilla.net Chat Button Link Code --><a
                            href="javascript:void(window.open('http://livehelp.workforcetracksupport.com/livezilla.php','','width=600,height=600,left=0,top=0,resizable=yes,menubar=no,location=yes,status=yes,scrollbars=yes'))"><img
                            src="https://livehelp.workforcetrack.com/image.php?id=03" width="237" height="78"
                            border="0" alt="LiveZilla Live Help"/></a>
                        <noscript>
                            <div><a href="http://livehelp.workforcetracksupport.com/livezilla.php" target="_blank">Start
                                Live Help Chat</a></div>
                        </noscript>
                        <!-- http://www.LiveZilla.net Chat Button Link Code -->
                        <!-- http://www.LiveZilla.net Tracking Code -->
                        <div id="livezilla_tracking" style="display:none"></div>
                        <script language="JavaScript" type="text/javascript"></script>
                        <!-- DON'T REMOVE ANY LINE BREAKS-->

                        <%--<script type="text/javascript"--%>
                        <%--src="http://helpcenter2.edatasite.com/livehelp_js.php?department=5&amp;pingtimes=15"></script>--%>
                    </div>
                </div>
            </div>
        </div>
        <!--End Sign Up-->

    </div>
    <!-- End wrapper content -->
</div>
<!--end #contbody-->

<!--start footer-->
<div id="footer">
    <ul>
        <%--<li class="nobg"><a href="http://www.workforcetrack.com/content/sitemap">Site Map</a></li>--%>
        <li><a href="http://www.workforcetrack.com/content/privacy">Privacy Policy</a></li>
        <li><a href="http://www.workforcetrack.com/content/terms-of-use">Terms of Use</a></li>
        <li><a href="http://www.workforcetrack.com/content/about">About Us</a></li>

        <li><a href="http://www.workforcetrack.com/content/contact">Contact us</a></li>
    </ul>
    <p>Copyright &copy; 2007-
        <script type="text/javascript">
            <!--
            var currentTime = new Date()
            var year = currentTime.getFullYear()
            document.write(year)
            //-->
        </script>
        Kanokla
    </p>
</div>
<!--end footer-->
<%--</div>--%>
<!--wrapper-->

<!--New Google Analytics script-->

<script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
    try {
        var pageTracker = _gat._getTracker("UA-355982-15");
        pageTracker._trackPageview();
    } catch(err) {
    }</script>
<!--New Google Analytics script-->

<%--Facebook Script Begin--%>
<%
    String facebookAppID = EdsContextParams.getFacebookAppID(request.getServerName());
%>
<%--<div id="fb-root">--%>
<%--as--%>
<%--</div>--%>
<script src="http://connect.facebook.net/en_US/all.js"></script>
<script>
    FB.init({appId: '<%=facebookAppID%>', status: true, cookie: true, xfbml: true});

    //      FB.Event.subscribe('auth.statusChange', function(response) {
    //          alert(response.toString());
    //        if (response.session){
    //            window.location = '/facebookLogin'
    //        } else {
    //        }
    //      });
</script>


</body>
</html>
