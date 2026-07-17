<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 25.11.2010
  Time: 20:01:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>




<tiles:insertDefinition name="frontEndLayoutNew">

<tiles:putAttribute name="title">
    <fmt:message key="index.titleWorkforceTrack"/> ${helpHost}
</tiles:putAttribute>
<tiles:putAttribute name="style">

    <SCRIPT type=text/javascript src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></SCRIPT>

    <SCRIPT type=text/javascript>
        jQuery(function(){

            jQuery('input[placeholder], textarea[placeholder]').placeholder();


        });
    </SCRIPT>

    <meta name="google-site-verification" content="jjw1ZnV5AryaQZs-KB64fCH3EpvkT94GKJAoFvLFzbU"/>
    <meta name="msvalidate.01" content="358DD608AB06D6ED2E842AF53CAFFB9E"/>
    <META name="y_key" content="d766be7156d8deef">

</tiles:putAttribute>
<tiles:putAttribute name="body">



                    <form action="/mainLogin" method="post" class="login">

                    <h1 class="title"><fmt:message key="index.login"/></h1>

                        <p class="c req">${error}</p>

                        <div class="loginBox">
                            <div class="boxIn">
                                <div class="form-group">
                                    <input class="form-control" id="login" type="text" name="USER_NAME" value="" placeholder="<fmt:message key="index.userName"/>" class="txt" >
                                    <em class="icon-user"></em>
                                </div>

                                <div class="form-group">
                                    <input  class="form-control" id="pass" type="password" name="USER_PASSWORD" value="" placeholder="<fmt:message key="index.password"/>" class="txt" >
                                    <em class="icon-pass"></em>
                                </div>


                            <div id="recaptcha"></div>

                            <input class="btn btn_signIn"  style="text-indent: 1px;" type="submit" value="<fmt:message key="index.login"/>" class="btnLogIn btn">

                            <footer>
                                    <a href="/forgot/forgotPassword.html">Forgot password?</a>
                            </footer>

                        </div>
                        </div>
                    <div class="clearBox"></div>
            </form>
                </section>

<%----------------------%>


</tiles:putAttribute>
<tiles:putAttribute name="script">

    <script type="text/javascript">

    function replaceT(obj){
    var newO=document.createElement('input');
    newO.setAttribute('type','password');
    newO.setAttribute('name',obj.getAttribute('name'));
    obj.parentNode.replaceChild(newO,obj);
    newO.focus();
    }
    </script>
    <%--End of Drupal Content--%>
</tiles:putAttribute>
</tiles:insertDefinition>