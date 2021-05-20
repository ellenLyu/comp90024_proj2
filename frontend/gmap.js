const mapStyle = [
    {
        featureType: "landscape",
        elementType: "geometry",
        stylers: [{ visibility: "on" }, { color: "#fcfcfc" }],
    },
    {
        featureType: "water",
        elementType: "geometry",
        stylers: [{ visibility: "on" }, { color: "#bfd4ff" }],
    },
    {
        elementType: "labels.icon",
        stylers: [
            {
                "visibility": "off"
            }
        ]
    },
    {
        featureType: "administrative",
        elementType: "geometry",
        styler: [
            {
                color: "#757575"
            }
        ]
    },
    {
        featureType: "administrative.country",
        elementType: "labels.text.fill",
        stylers: [
            {
                color: "#9e9e9e"
            }
        ]
    },
    {
        featureType: "administrative.neighborhood",
        stylers: [
            {
                "visibility": "off"
            }
        ]
    },
    {
        featureType: "administrative.land_parcel",
        stylers: [
            {
                visibility: "off"
            }
        ]
    },
    {
        featureType: "poi",
        elementType: "labels.text",
        stylers: [
            {"visibility": "off"}
        ]
    },
    {
        featureType: "poi",
        elementType: "labels.text.fill",
        stylers: [
            {
                visibility: "off"
            }
        ]
    },
    {
        featureType: "road",
        elementType: "labels",
        stylers: [
            {
                visibility: "off"
            }
        ]
    },
    {
        featureType: "road",
        elementType: "labels.icon",
        stylers: [
            {
                visibility: "off"
            }
        ]
    },
    {
        featureType: "transit",
        stylers: [
            {
                visibility: "off"
            }
        ]
    },
];


let colorChoices = [
    ["#e3dbda", "#8f0000"], // red 
    ['#ded7d1', '#8f4506'], // orange
    ['#edece6', '#9e8803'], // yellow
    ['#e1e3dc', '#507001'], // light green
    ['#e1ede2', '#04630c'], // dark green
    ['#e6edf5', '#007a62'], // blue
    ['#eae1f5', '#38017a']  // purple
]

let colorsByYear = {}
for (let i = 2014; i < 2021; i++) {
    if (i == 2019)
        continue
    let pair = colorChoices[(i-2014)];
    let colors = generateColors(pair[0], pair[1], 7);
    colorsByYear[i.toString()] = colors;
}

var data = {
    'colors': colorsByYear,
    'suburbs': {},
    'tweetCounts': {},
    'sentiments': {},
};
var map;
var infoWindow;
var dataLayer;
var selectedYear = $("#year").val();

function initMap() {
    // The map, centered at Uluru
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 12,
        center: { lat: -37.840935, lng: 144.946457 },
        disableDefaultUI: true,
        styles: mapStyle,
    });
    infoWindow = new google.maps.InfoWindow({
        maxHeight: "1000px",
    });

    loadMapShapes();

    selectedYear = $("#year").val();
    console.log("select: " + selectedYear);
    if (typeof data.suburbs[selectedYear] === 'undefined') {
        getSuburbsByYear(selectedYear);
    }

    $("#year").change(refreshMap);
}

function loadMapShapes() {

    dataLayer = new google.maps.Data();
    dataLayer.loadGeoJson("melb_geo.json");
    // dataLayer.loadGeoJson("vic_geo.json");

    dataLayer.setStyle(
        function (feature) {
            let name = feature.getProperty('name');
            var color;
            if (typeof data.suburbs[selectedYear] !== 'undefined') {
                color = getColor(data.suburbs[selectedYear][name]);
            } else {
                color = getColor(feature.getProperty('cartodb_id'));
            }
            return {
                fillColor: color,
                fillOpacity: 0.8,
                strokeColor: '#b3b3b3',
                strokeWeight: 1,
                zIndex: 1
            };
        });

    dataLayer.addListener('mouseover', function (e) {
        dataLayer.overrideStyle(e.feature, {
            strokeColor: '#2a2a2a',
            strokeWeight: 2,
            zIndex: 2
        });
    });
    dataLayer.addListener('mouseover', mouseInToRegion);

    dataLayer.addListener('mouseout', function (e) {
        dataLayer.revertStyle();
    });
    dataLayer.addListener('mouseout', mouseOutOfRegion);

    dataLayer.addListener('click', clickOnRegion);

    dataLayer.setMap(map);

}

function getColor(total) {
    if (typeof total === 'undefined') {
        return '#f5f5f5';
    }
    let color = '#000000';
    let colors = data.colors[selectedYear];
    if (total > 10000) {
        color = colors[6]
    } else if (total > 3000) {
        color = colors[5]
    } else if (total > 1000) {
        color = colors[4]
    } else if (total > 5000) {
        color = colors[3]
    } else if (total > 300) {
        color = colors[2]
    } else if (total > 100) {
        color = colors[1]
    } else {
        color = colors[0]
    }
    return color;
}

function mouseInToRegion(e) {
    // set the hover state so the setStyle function can change the border
    e.feature.setProperty("state", "hover");
    
    // update the label
    let name = e.feature.getProperty("name");
    let count = data.suburbs[selectedYear][name];
    $("#data-label").text(name);
    $("#data-value").text(`${count} tweets`);
    document.getElementById("data-box").style.display = "block";
}

/**
 * Responds to the mouse-out event on a map shape (state).
 *
 */
function mouseOutOfRegion(e) {
    // reset the hover state, returning the border to normal
    e.feature.setProperty("state", "normal");
}

function clickOnRegion(e) {
    let feature = e.feature;
    infoWindow.setPosition(e.latLng);
    let name = feature.getProperty("name");
    let number = typeof data.suburbs[selectedYear][name] === 'undefined' ? 0 : data.suburbs[selectedYear][name];
    console.log(`${name}: ${number}`);
    // getTweetCounts(name);, year
    infoWindow.setContent(`${name}: ${number}`);
    infoWindow.open(map);

    // let container = `<div id="chart1" style="width:600px; height:400px;"></div>`;

    // var nodeFrame = document.createElement("div");
    // nodeFrame.style.cssText  = "width:400px; height:600px;";

    drawTweetCountChart(name);

}

function make_base_auth(user, password) {
    var tok = user + ':' + password;
    var hash = btoa(tok);
    return 'Basic ' + hash;
}

function getSuburbsByYear(year) {
    $.ajax({
        url: "http://172.26.128.60:8080/search/large_by_suburbs",
        async: false,
        type: "POST",
        dataType: "json",
        headers: {'Content-Type':'application/json'},
        data: JSON.stringify({"year": year}),
        success: function(res) {
            let suburbs = {};
            for (let [key, value] of Object.entries(res.data)) {
                suburbs[upcaseSentence(key)] = value;
            }
            data.suburbs[year] = suburbs;
        },
        error: function() {
            console.log("error");
        }
    })
}

function getSuburbs() {
    $.ajax({
        url: "http://172.26.128.60:8080/search/by_suburbs",
        async: false,
        type: "POST",
        dataType: "json",
        headers: {'Content-Type':'application/json'},
        data: JSON.stringify({}),
        success: function(res) {
            let suburbs = {};
            for (let [key, value] of Object.entries(res.data)) {
                suburbs[upcaseSentence(key)] = value;
            }
        },
        error: function() {
            console.log("error");
        }
    })
}

function upcaseSentence(text) {
    let words = text.split(" ");
    for (let i = 0; i < words.length; i++) {
        words[i] = words[i][0].toUpperCase() + words[i].substr(1);
    }
    return words.join(" ");
}

function drawTweetCountChart(suburb) {
    var node = document.createElement('div');
    node.setAttribute("id", "chart1");
    node.style.cssText = "width:300px; height:200px;";

    if (typeof data.tweetCounts[suburb] === 'undefined') {
        getTweetCounts(suburb);
    }
    let yearCounts = data.tweetCounts[suburb];

    var myChart = echarts.init(
        node, null, 
        // {height: parseInt(node.style.height) * 0.40 + "px"}
    );
    var option = {
        grid: {
            top: "10%",
        },
        title: {
            text: 'Past years tweets count',
            textStyle: {
                fontSize: 10,
            }
        },
        tooltip: {},
        legend: {
            data:['Count']
        },
        xAxis: {
            data: Object.keys(yearCounts),
        },
        yAxis: {
            show: false,
        },
        series: [{
            name: 'Year',
            type: 'bar',
            data: Object.values(yearCounts),
        }],
    };
    myChart.setOption(option);
    infoWindow.setContent(node);
    infoWindow.open(map);
}

function getTweetCounts(suburb) {
    $.ajax({
        url: "http://172.26.128.60:8080/search/large_by_suburbs",
        async: false,
        type: "POST",
        dataType: "json",
        headers: {'Content-Type':'application/json'},
        data: JSON.stringify({"suburb": suburb.toLowerCase()}),
        success: function(res) {
            // suburbs = {};
            // for (let [key, value] of Object.entries(res.data)) {
            //     suburbs[upcaseSentence(key)] = value;
            // }
            console.log(res.data);
            data.tweetCounts[suburb] = res.data;
        },
        error: function() {
            console.log("error");
        }
    })
}

function hex(c) {
    var s = "0123456789abcdef";
    var i = parseInt(c);
    if (i == 0 || isNaN(c)) {
        return "00";
    }
    i = Math.round (Math.min (Math.max (0, i), 255));
    return s.charAt((i - i % 16) / 16) + s.charAt(i % 16);
}

/* Convert an RGB triplet to a hex string */
function rgb2Hex(rgb) {
    return "#" + hex(rgb[0]) + hex(rgb[1]) + hex(rgb[2]);
}

/* Remove '#' in color hex string */
function trim(s) { return (s.charAt(0) == '#') ? s.substring(1, 7) : s }

/* Convert a hex string to an RGB triplet */
function hex2Rgb(hex) {
    var color = [];
    color[0] = parseInt((trim(hex)).substring(0, 2), 16);
    color[1] = parseInt((trim(hex)).substring(2, 4), 16);
    color[2] = parseInt((trim(hex)).substring(4, 6), 16);
    return color;
}

function generateColors(startColor, endColor, step) {
    var start = hex2Rgb(startColor),
        end = hex2Rgb(endColor);

    var rStep = (end[0] - start[0]) / step,
        gStep = (end[1] - start[1]) / step,
        bStep = (end[2] - start[2]) / step;
    var colors = [];
    for (var i = 0; i < step; i++){
        colors.push(rgb2Hex([
            parseInt(rStep*i + start[0]),
            parseInt(gStep*i + start[1]),
            parseInt(bStep*i + start[2])
        ]));
    }
    return colors;
}

// function drawTweetCountChart

function refreshMap() {
    selectedYear = $("#year").val();
    console.log("select: " + selectedYear);
    if (typeof data.suburbs[selectedYear] === 'undefined') {
        getSuburbsByYear(selectedYear);
    }
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 12,
        center: { lat: -37.840935, lng: 144.946457 },
        disableDefaultUI: true,
        styles: mapStyle,
    });
    infoWindow = new google.maps.InfoWindow({
        maxHeight: "1000px",
    });
    dataLayer.setMap(map);
}

initMap();
