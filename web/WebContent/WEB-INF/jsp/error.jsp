<%--
  Created by IntelliJ IDEA.
  User: Ilhombek
  Date: 19.01.2011
  Time: 22:48:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title" value="${hostName}-Error page"/>
    <tiles:putAttribute name="style">

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

        <!-- Yandex.Metrika counter -->
        <%--<script type="text/javascript"> (function (d, w, c) {
            (w[c] = w[c] || []).push(function () {
                try {
                    w.yaCounter45824055 = new Ya.Metrika2({id: 45824055, clickmap: true, trackLinks: true, accurateTrackBounce: true, webvisor: true, trackHash: true});
                } catch (e) {
                }
            });
            var n = d.getElementsByTagName("script")[0], s = d.createElement("script"), f = function () {
                n.parentNode.insertBefore(s, n);
            };
            s.type = "text/javascript";
            s.async = true;
            s.src = "https://mc.yandex.ru/metrika/tag.js";
            if (w.opera == "[object Opera]") {
                d.addEventListener("DOMContentLoaded", f, false);
            } else {
                f();
            }
        })(document, window, "yandex_metrika_callbacks2"); </script>
        <noscript>
            <div><img src="https://mc.yandex.ru/watch/45824055" style="position:absolute; left:-9999px;" alt=""/></div>
        </noscript>--%>
        <!-- /Yandex.Metrika counter -->


    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <div id="index-page" style="width:860px;">
            <h2 class="title">Error Page</h2>

            <p>Sorry unexpected error occurred. Please try to refresh your browser with <strong>"Ctrl + F5"</strong> and
                try
                again.</p>

            <p>If the problem persists please contact our support team.</p>

            <p><strong>E-mail:</strong> <a href="mailto:${email}">${email}</a></p>

            <p><strong>Skype:</strong> ${skype}</p>

            <p><strong>Phone:</strong> ${supportPhone}</p>

        </div>

        <%--<div style="padding:3px;margin: -10px 215px 10px 10px; background: #fcefc8; border:1px solid #edd28a;width:290px;float:right;">
           <img border="0" style="margin:4px;float: left;" alt="Google Account" title="Login using your Google Account"
                src="/i/google.jpg"/>
           <a href="/check?service=pm" style="font-size:12px; color:#205476">No need to create a new account. You may
               use your Google Account to Sign In</a>
       </div>--%>

    </tiles:putAttribute>

</tiles:insertDefinition>