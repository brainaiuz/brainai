


// loadFacebookSdk.js
window.fbAsyncInit = function() {
    // JavaScript SDK configuration and setup
    FB.init({
        appId: '544686667080872', // Facebook App ID
        cookie: true, // enable cookies
        xfbml: true, // parse social plugins on this page
        version: 'v18.0' //Graph API version
    });

    // Execute the launchWhatsAppSignup function after the SDK is initialized
    launchWhatsAppSignup();
};

// Load the JavaScript SDK asynchronously
(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s);
    js.id = id;
    js.src = "https://connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));