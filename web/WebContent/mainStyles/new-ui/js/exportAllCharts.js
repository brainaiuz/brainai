function printAllChartsToPdf() {
    var charts = Highcharts.charts;
    var keys = [];
    var values = [];
    var arr = [];
    for (var i = 0; i < charts.length; i++) {
        if (typeof charts[i] !== 'undefined') {
            keys.push(charts[i].options.objectId + "@" + charts[i].subtitle.textStr);
            var c = charts[i].getSVG();
            var f = c.substr(0, c.indexOf("width"));
            var m = 'width="100%" height="100%"';
            var l = c.substr(c.indexOf("height") + 12);
            values.push(f + m + l);
        }
    }
    arr.push(keys);
    arr.push(values);
    return arr;
}