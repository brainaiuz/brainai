package com.edatasite.workforce.gwt.core.client.ui.components;

import com.google.gwt.core.client.ScriptInjector;

import static com.google.gwt.core.client.ScriptInjector.TOP_WINDOW;

public class TawkLiveChat {

    private final String tawkToSiteId;
    private final String company;
    private final String userFullName;
    private final String userEmail;

    private static final String TAWK_TO_LINK = "var Tawk_API = Tawk_API || {}, Tawk_LoadStart = new Date();\n" +
            " Tawk_API.embedded='tawk_5b2e498aeba8cd3125e31c0d'; \n" +
            " Tawk_API.visitor = {\n" +
            "    name  : '$fullname ($company)',\n" +
            "    email : '$useremail'\n" +
            " };" +
            " (function () {\n" +
            "    var s1 = document.createElement(\"script\"), s0 = document.getElementsByTagName(\"script\")[0];\n" +
            "    s1.async = true;\n" +
            "    s1.src = 'https://embed.tawk.to/$tawktositeid/1ijvrek7s';\n" +
            "    s1.charset = 'UTF-8';\n" +
            "    s1.setAttribute('crossorigin', '*');\n" +
            "    s0.parentNode.insertBefore(s1, s0);\n" +
            " })();";

    public TawkLiveChat(String tawkToSiteId, String company, String userFullName, String userEmail) {
        this.company = company;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.tawkToSiteId = tawkToSiteId;
        if (tawkToSiteId != null) {
            if (getTawkToSiteLink() != null) {
                ScriptInjector.fromString(getTawkToSiteLink())
                        .setRemoveTag(false)
                        .setWindow(TOP_WINDOW)
                        .inject();
            }
        }
    }

    private String getTawkToSiteLink() {
        if (tawkToSiteId != null) {
            return TAWK_TO_LINK
                    .replace("$tawktositeid", tawkToSiteId)
                    .replace("$company", company.replace("'", "&quot;"))
                    .replace("$fullname", userFullName.replace("'", "&quot;"))
                    .replace("$useremail", userEmail);
        }
        return null;
    }

    public static native boolean isLiveChatOpen() /*-{
        if ($wnd.Tawk_API) {
            return $wnd.Tawk_API.isChatMaximized && $wnd.Tawk_API.isChatMaximized();
        } else {
            console.log("Tawk_API is not available.");
        }
        return false;
    }-*/;

    public static native void toggleChat() /*-{
        if ($wnd.Tawk_API) {
            if ($wnd.Tawk_API.isChatMaximized && $wnd.Tawk_API.isChatMaximized()) {
                $wnd.Tawk_API.minimize();
            } else {
                $wnd.Tawk_API.maximize();
            }
        } else {
            console.log("Tawk_API is not available.");
        }
    }-*/;

    public static native void closeChat() /*-{
        if ($wnd.Tawk_API){
            $wnd.Tawk_API.minimize();
        }
    }-*/;

}
