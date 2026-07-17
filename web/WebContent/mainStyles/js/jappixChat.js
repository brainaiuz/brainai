/**
 * Created with IntelliJ IDEA.
 * User: Fathulla
 * Date: 29.03.13
 * Time: 16:50
 * To change this template use File | Settings | File Templates.
 */
function runChat(login, pasw) {
    jQuery.ajaxSetup({cache:true});
    jQuery.getScript('https://static.jappix.com/php/get.php?l=en&t=js&g=mini.xml', function () {
        MINI_ANIMATE = true;
        MINI_ERROR_LINK = '/info.html';
        // Connect the user (autoconnect, show_pane, domain, username, password)
        launchMini(true, true, "gmail.com", login, pasw);
    });
}
