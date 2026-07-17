function createRequestObject() {
    var req;
    if (window.XMLHttpRequest) {
        //For Firefox, Safari, Opera
        req = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        //For IE 5+
        req = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        //Error for an old browser
        alert('Your browser is not IE 5 or higher, or Firefox or Safari or Opera');
    }

    return req;
}

//Make the XMLHttpRequest Object
var http = createRequestObject();

function sendRequest(method, url, value) {
    if (value != null && value != "" && value != " " && (method == 'get' || method == 'GET')) {
        http.open(method, url);
        http.onreadystatechange = handleResponse;
        http.send(null);
    }
}

function handleResponse() {
    document.getElementById("emailValidate").innerHTML = "<img src='/loading-animation.gif'> checking..";
    if (http.readyState == 4 && http.status == 200) {
        var response = http.responseText;
        if (response) {
            document.getElementById("emailValidate").innerHTML = response;
        }
    }
}