function openWindow(url, title) {
    window.open(url, title, 'width=600,height=600');
}

function deleteObject(objectDeletingUrl, objectID, objectType) {
    var sessionID = getCookie("SESSION_ID");
    sessionID = sessionID.replace("%24", "$");

    var params = 'SESSION_ID=' + sessionID + '&entityid=' + objectID + '&entitytype=' + objectType;
    xmlhttpPost(objectDeletingUrl, params);
}

function xmlhttpPost(strURL, params) {
    var xmlHttpReq = false;
    var self = this;
    // Mozilla/Safari
    if (window.XMLHttpRequest) {
        self.xmlHttpReq = new XMLHttpRequest();
    }
    // IE
    else if (window.ActiveXObject) {
        self.xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
    }
    self.xmlHttpReq.open('POST', strURL, true);
    self.xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    self.xmlHttpReq.onreadystatechange = function () {
        if (self.xmlHttpReq.readyState == 4) {
            callback(self.xmlHttpReq.responseText);
        }
    }
    self.xmlHttpReq.send(params);
}

function callback(str) {
    alert("In order to see latest changes click on Run Report button");
}

function getCookie(c_name) {
    var i, x, y, ARRcookies = document.cookie.split(";");

    for (i = 0; i < ARRcookies.length; i++) {
        x = ARRcookies[i].substr(0, ARRcookies[i].indexOf("="));
        y = ARRcookies[i].substr(ARRcookies[i].indexOf("=") + 1);
        x = x.replace(/^\s+|\s+$/g, "");

        if (x == c_name) {
            return unescape(y);
        }
    }
}