<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title" value="Authorization with google"/>
    <tiles:putAttribute name="style">
        <script type="text/javascript">

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

        </script>
    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <div id="index-page" style="width:860px; margin: 15px auto;">

            <h2 class="title">${message}</h2>

            <p>&nbsp;</p>

            <p><strong>E-mail:</strong> <a href="mailto:${email}">${email}</a></p>

            <p><strong>Skype:</strong> ${skype}</p>

            <p><strong>Phone:</strong> ${supportPhone}</p>


        </div>

    </tiles:putAttribute>

</tiles:insertDefinition>