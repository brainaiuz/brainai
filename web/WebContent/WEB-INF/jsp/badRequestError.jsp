<%--
  Created by IntelliJ IDEA.
  User: Ilhombek
  Date: 19.01.2011
  Time: 22:53:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title" value="Error page"/>
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

    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <div id="index-page" style="width:860px; margin: 15px auto;">
            <h2 class="title">Error Page</h2>

            <p>Bad request</p>

            <p><strong>E-mail:</strong> <a href="mailto:email">${email}</a></p>

            <p><strong>Skype:</strong> ${skype}</p>

            <p><strong>Phone:</strong> ${supportPhone}</p>

        </div>


    </tiles:putAttribute>
</tiles:insertDefinition>