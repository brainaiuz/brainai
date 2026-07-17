function changeStatePaid(field) {
    if ((field.value == "46") || (field.value == "USA")) {
        document.getElementById('state').disabled = false;
    } else {
        document.getElementById('state').disabled = true;
    }
    document.signup.submit();
}

function changeState(field) {
    if ((field.value == "46") || (field.value == "USA")) {
        document.getElementById('state').disabled = false;
    } else {
        document.getElementById('state').disabled = true;
    }
}

function checkBox(field) {

    if (field.cheked == false) {
        document.getElementById('checkText').value = "false";
    } else {
        document.getElementById('checkText').value = "true";
    }
}

function onLoad() {
    var field = document.getElementById('countryID');
    if ((field.value == "46") || (field.value == "USA")) {
        document.getElementById('state').disabled = false;
    } else {
        document.getElementById('state').disabled = true;
    }
}

function phoneValidation(e) {
    var keynum;
    var keychar;
    var num = '0123456789'
    var sym = '()+- '
    if (e.keyCode) //IE
    {
        keynum = e.keyCode;
    } else if (e.which)// Netscape/Firefox/Opera
    {
        keynum = e.which;
    }
    keychar = String.fromCharCode(keynum);
    if (num.indexOf(keychar) != -1 || sym.indexOf(keychar) != -1
        || keynum == 8 || keynum == 9 || keynum == 46
        || keynum == 13 || keynum == 36 || keynum == 35
        || keynum == 37 || keynum == 38 || keynum == 39
        || keynum == 40) return true;
    else return false;
}