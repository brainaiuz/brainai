if (typeof (urchinTracker) != 'function')
    document.write('<sc' + 'ript src="' +
        'http' + (document.location.protocol == 'https:' ? 's://ssl' : '://www') +
        '.google-analytics.com/urchin.js' + '"></sc' + 'ript>')

try {
    _uacct = 'UA-355982-27';
    urchinTracker("/1964370141/test");
} catch (err) {
}

function utmx_section() {
}

function utmx() {
}

(function () {
    var k = '1964370141', d = document, l = d.location, c = d.cookie;

    function f(n) {
        if (c) {
            var i = c.indexOf(n + '=');
            if (i > -1) {
                var j = c.indexOf(';', i);
                return c.substring(i + n.length + 1, j < 0 ? c.length : j)
            }
        }
    }

    var x = f('__utmx'), xx = f('__utmxx'), h = l.hash;
    d.write('<sc' + 'ript src="' +
        'http' + (l.protocol == 'https:' ? 's://ssl' : '://www') + '.google-analytics.com'
        + '/siteopt.js?v=1&utmxkey=' + k + '&utmx=' + (x ? x : '') + '&utmxx=' + (xx ? xx : '') + '&utmxtime='
        + new Date().valueOf() + (h ? '&utmxhash=' + escape(h.substr(1)) : '') +
        '" type="text/javascript" charset="utf-8"></sc' + 'ript>')
})();

utmx("url", 'A/B');