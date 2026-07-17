<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="mainLayout">
    <tiles:putAttribute name="loading_message">
        <fmt:message key="main.loading"/>
    </tiles:putAttribute>
    <tiles:putAttribute name="script">
        <script language="javascript" src="/passwordStrength.js"></script>
        <script language="javascript" src="settings/settings.nocache.js"></script>
        <script>
            window.fbAsyncInit = function () {
                // JavaScript SDK configuration and setup
                FB.init({
                    appId:    '544686667080872', // Facebook App ID
                    cookie:   true, // enable cookies
                    xfbml:    true, // parse social plugins on this page
                    version:  'v18.0' //Graph API version
                });
            };

            // Load the JavaScript SDK asynchronously
            (function (d, s, id) {
                var js, fjs = d.getElementsByTagName(s)[0];
                if (d.getElementById(id)) return;
                js = d.createElement(s); js.id = id;
                js.src = "https://connect.facebook.net/en_US/sdk.js";
                fjs.parentNode.insertBefore(js, fjs);
            }(document, 'script', 'facebook-jssdk'));

            // Facebook Login with JavaScript SDK
            function launchWhatsAppSignup() {
                // Conversion tracking code

                // Launch Facebook login
                FB.login(function (response) {
                    if (response.authResponse) {
                        const code = response.authResponse.code;
                        // The returned code must be transmitted to your backend,
                        // which will perform a server-to-server call from there to our servers for an access token
                    } else {
                        console.log('User cancelled login or did not fully authorize.');
                    }
                }, {
                    config_id: '765570601736088', // configuration ID goes here
                    response_type: 'code',    // must be set to 'code' for System User access token
                    override_default_response_type: true, // when true, any response types passed in the "response_type" will take precedence over the default types
                    extras: {
                        setup: {
                            solutionID: '544686667080872'
                        }
                    }
                });
            }
        </script>
    </tiles:putAttribute>
</tiles:insertDefinition>