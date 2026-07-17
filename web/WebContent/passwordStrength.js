function checkPassStrength(t) {
    var r = !1, n = !1, a = !1, e = !1, o = !1;
    t.match(/[A-Z]/g) && (r = !0);
    t.match(/[a-z]/g) && (n = !0);
    t.match(/[0-9]/g) && (a = !0);
    if (t.match(/[!|@|#|$|%|^|&]/g) && (e = !0), 8 <= t.length && (o = !0), r && n && a && e && o) return "STRONG";
    var i = 0;
    return r && (i += 3), n && (i += 2), a && (i += 2), e && (i += 3), o && (i += 8), 18 <= i ? "STRONG" : 8 < i ? "MEDIUM" : "SHORT"
}

function passStrength(pass, minLength, maxLength, upperCaseVal, lowerCaseVal, numberVal, specialVal, minLengthVal) {
    var containsUpperCase, containsLowerCase, containsNumbers, containsSpecial, minLengthValid, maxLengthValid;
    var strength = 0;

    containsUpperCase = pass.match(/[A-Z]/g);
    containsLowerCase = pass.match(/[a-z]/g);
    containsNumbers = pass.match(/[0-9]/g);
    containsSpecial = pass.match(/[$|%|&|(|@|#|§|=|)|,|:|;|\-|_|+|^]/g);

    minLengthValid = minLength <= pass.length;
    maxLengthValid = maxLength <= pass.length;
    if (containsUpperCase) {
        strength += upperCaseVal;
    }
    if (containsLowerCase) {
        strength += lowerCaseVal;
    }
    if (containsNumbers) {
        strength += numberVal;
    }
    if (containsSpecial) {
        strength += specialVal;
    }
    if (minLengthValid) {
        strength += minLengthVal;
    }
    if (maxLengthValid && containsLowerCase && containsUpperCase && containsNumbers && containsSpecial) {
        strength += maxLength - (upperCaseVal + lowerCaseVal + numberVal + specialVal + minLengthVal);
    }
    return maxLength <= strength ? "STRONG" : minLength <= strength ? "MEDIUM" : "WEAK";
}

function scorePassword(t) {
    var r = 0;
    if (!t) return r;
    for (var n = new Object, a = 0; a < t.length; a++) n[t[a]] = (n[t[a]] || 0) + 1, r += 5 / n[t[a]];
    var e = {digits: /\d/.test(t), lower: /[a-z]/.test(t), upper: /[A-Z]/.test(t), nonWords: /\W/.test(t)};
    for (var o in variationCount = 0, e) variationCount += 1 == e[o] ? 1 : 0;
    return 1 == variationCount ? 0 : (r += 10 * variationCount, parseInt(r))
}

function passwordsMatch(t, r) {
    return t == r
}