<%--
  Created by IntelliJ IDEA.
  User: Ilhombek
  Date: 19.01.2011
  Time: 22:37:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title" value="WorkforceTrack.com-Error page"/>
    <tiles:putAttribute name="style">
        <!--<link href="/errorpages/css.css" rel="stylesheet" type="text/css"/>-->
        <%--<script type="text/javascript">

            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', 'UA-355982-15']);
            _gaq.push(['_trackPageview']);

            (function() {
                var ga = document.createElement('script');
                ga.type = 'text/javascript';
                ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(ga, s);
            })();

        </script>--%>

        <script type="text/javascript">
            var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
            document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
        </script>
        <script type="text/javascript">
            try {
                var pageTracker = _gat._getTracker("UA-355982-15");
                pageTracker._trackPageview("/404.html?page=" + document.location.pathname + document.location.search + "&from=" + document.referrer);
            } catch(err) {
            }
        </script>

    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <div id="index-page" style="width:860px;">

            <h2 class="title">Error Page</h2>

            <p>Your session has expired. Please try to log in again</p>

            <p><strong>E-mail:</strong> <a href="mailto:support@workforcetrack.com"
                                           style="">support@workforcetrack.com</a></p>

            <p><strong>Skype:</strong> workforcetrack.team</p>

            <p><strong>Phone:</strong> + 44 (0) 207 096 1245</p>

            <p><strong>Live Help:</strong>&nbsp;<SPAN
                    style="BORDER-RIGHT: medium none; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: medium none;">
					<!--<script type="text/javascript"
						src="http://helpcenter2.edatasite.com/livehelp_js.php?department=5&amp;pingtimes=15"></script>-->
				   <!--<script type="text/javascript"
						src="http://helpcenter2.edatasite.com/livehelp_js.php?department=5&amp;pingtimes=15"></script>-->
			<!-- http://www.LiveZilla.net Chat Button Link Code -->
				   <a href="javascript:void(window.open('http://livehelp.workforcetracksupport.com/livezilla.php','','width=600,height=600,left=0,top=0,resizable=yes,menubar=no,location=yes,status=yes,scrollbars=yes'))">
                       <img src="//livehelp.workforcetracksupport.com/image.php?id=04" width="208" height="65"
                            border="0"
                            alt="LiveZilla Live Help"/>
                   </a><noscript>
                <div><a href="http://livehelp.workforcetracksupport.com/livezilla.php" target="_blank">Start Live Help
                    Chat</a></div>
            </noscript>
				   <!-- http://www.LiveZilla.net Chat Button Link Code --><!-- http://www.LiveZilla.net Tracking Code -->
				   <div id="livezilla_tracking" style="display:none"></div>
				   <script language="JavaScript" type="text/javascript"></script>
					   <!--DON'T REMOVE ANY LINE BREAKS-->
					  <!--var script = document.createElement("script");
					   script.type = "text/javascript";
					   var src = "http://livehelp.workforcetracksupport.com/server.php?request=track&output=jcrpt&nse=" + Math.random();
					   setTimeout("script.src=src;document.getElementById('livezilla_tracking').appendChild(script)", 1); -->
				   <!-- http://www.LiveZilla.net Tracking Code -->
					</SPAN></p>
            <p>We apologise for any inconvenience caused.</p>
        </div>


    </tiles:putAttribute>

</tiles:insertDefinition>