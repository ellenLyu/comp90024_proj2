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


var data = {
    'income': {},
    'language': {},
    'citizen': {},
    'year12': {},
};
var map;
var infoWindow;
var dataLayer;
var languageChart;
var citizenChart;
var year12Chart;
var selectedArea;
var lastFeature;

function initMap() {
    // The map, centered at Uluru
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 10,
        center: { lat: -37.840935, lng: 144.946457 },
        disableDefaultUI: true,
        styles: mapStyle,
    });
    infoWindow = new google.maps.InfoWindow({
        maxHeight: "1000px",
    });

    loadMapShapes();
}

function loadMapShapes() {

    dataLayer = new google.maps.Data();
    dataLayer.loadGeoJson("../assets/LGA.geojson");

    dataLayer.setStyle(
        function (feature) {
            // let suburb = feature.getProperty('name');
            var color = '#f5f5f5';
            if (feature.getProperty("selected")) {
                color = "red";
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

    dataLayer.addListener('click', function(e) {
        if (lastFeature == undefined) {
            e.feature.setProperty("selected", true);
            lastFeature = e.feature;
            return;
        }
        if (e.feature.getProperty("ABB_NAME") == lastFeature.getProperty("ABB_NAME")) {
            if (e.feature.getProperty("selected")) {
                e.feature.setProperty("selected", false);
            } else {
                e.feature.setProperty("selected", true)
            }
        } else {
            e.feature.setProperty("selected", true);
            lastFeature.setProperty("selected", false);
        }
        lastFeature = e.feature;
        // dataLayer.overrideStyle(e.feature, {
        //     fillColor: "#941603",
        //     fillOpacity: 0.8,
        // });
    })

    dataLayer.addListener('click', clickOnRegion);

    google.maps.event.addListener(infoWindow, "closeclick", function() {
        dataLayer.revertStyle();
    })

    dataLayer.setMap(map);

}


function mouseInToRegion(e) {
    // set the hover state so the setStyle function can change the border
    e.feature.setProperty("state", "hover");
    // update the label
    let area = e.feature.getProperty("SH_NAME");
    area = extractCityName(area)
    $("#data-value").text(`Area ${area}`);
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
    let area = feature.getProperty("SH_NAME");
    area = extractCityName(area);
    // let count = typeof data.suburbs[selectedYear][suburb] === 'undefined' ? 0 : data.suburbs[selectedYear][suburb];
    // console.log(`${suburb}: ${count}`);

    // set selected suburb
    selectedArea = area;

    if (Object.keys(data.income).length == 0) {
        getAbs();
        console.log("start request");
    }
    showInfoWindow(area);
}

function extractCityName(text) {
    let token = text.split("OF", 2);
    var city;
    if (token.length == 1) {
        city = token[0];
    } else {
        city = token[1];
    }
    return upcaseSentence(city.trim());
}


function upcaseSentence(text) {
    let words = text.toLowerCase().split(" ");
    for (let i = 0; i < words.length; i++) {
        words[i] = words[i][0].toUpperCase() + words[i].substr(1);
    }
    return words.join(" ");
}

function showInfoWindow(area) {
    var nodeFrame = document.createElement('div');
    nodeFrame.style.cssText = "width:300px; height: 600px;";

    var label = document.createElement('h3');
    label.setAttribute("id", "income-label")
    if (data.income[area] != undefined) {
        let income = data.income[area]["Median"];
        label.innerText = `2016 ${area} income: ${income}$`;
    } else {
        label.innerText = `2016 ${area} income: `;
    }
    label.style.cssText = "text-align:center;";
    nodeFrame.appendChild(label);

    let languageChartNode = drawLanguageChart(area);
    nodeFrame.appendChild(languageChartNode);

    let citizenChartNode = drawCitizenChart(area);
    nodeFrame.appendChild(citizenChartNode);

    let year12ChartNode = drawYear12Chart(area);
    nodeFrame.appendChild(year12ChartNode)

    infoWindow.setContent(nodeFrame);
    infoWindow.open(map);
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


function drawLanguageChart(area) {
    var node = document.createElement('div');
    node.setAttribute("id", "chart1");
    node.style.cssText = "width:300px; height:200px;";
    
    languageChart = echarts.init(node);

    if (Object.keys(data.language).length == 0) {
        languageChart.showLoading();
    } else {
        setLanguageChart(area, languageChart);
    }
    return node;
}

function drawCitizenChart(area) {
    var node = document.createElement('div');
    node.setAttribute("id", "chart2");
    node.style.cssText = "width:300px; height:200px;";
    
    citizenChart = echarts.init(node);

    if (Object.keys(data.citizen).length == 0) {
        citizenChart.showLoading();
    } else {
        setCitizenChart(area, citizenChart);
    }
    return node;
}

function drawYear12Chart(area) {
    var node = document.createElement('div');
    node.setAttribute("id", "chart3");
    node.style.cssText = "width:300px; height:200px;";
    
    year12Chart = echarts.init(node);

    if (Object.keys(data.year12).length == 0) {
        year12Chart.showLoading();
    } else {
        setYear12Chart(area, year12Chart);
    }
    return node;
}




function getAbs() {
    $.ajax({
        url: "http://172.26.128.60:8080/search/get_abs",
        type: "POST",
        dataType: "json",
        headers: {'Content-Type':'application/json'},
        data: JSON.stringify({"year": 2016}),
    }).done(function(res) {
        console.log("request abs data finished");
        data.income = res.data["Median employee income ($)"];
        data.language = res.data["Speaks a Language Other Than English at Home (%)"];
        data.citizen = res.data["Australian citizen (%)"];
        data.year12 = res.data["Completed Year 12 or equivalent (%)"];
        let temp = $("#income-label").text();
        $("#income-label").text(temp + data.income[selectedArea]["Median"] + "$");
        if (languageChart != undefined) {
            languageChart.hideLoading();
            setLanguageChart(selectedArea, languageChart);
        }
        if (citizenChart != undefined) {
            citizenChart.hideLoading();
            setCitizenChart(selectedArea, citizenChart);
        }
        if (year12Chart != undefined) {
            year12Chart.hideLoading();
            setYear12Chart(selectedArea, year12Chart);
        }
    }).fail(function() {
        console.log("request ABS error");
    })
}


function setLanguageChart(area, chart) {
    console.log("area", area);
    console.log(data.language);
    var values = data.language[area];
    console.log(values);
    var option = {
        title: {
            text: `Speaks Language distribution`,
            // subtext: '纯属虚构',
            left: 'center',
            textStyle: {
                fontSize: 12,
            }
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            top: "30%",
        },
        series: [
            {
                name: 'Language',
                type: 'pie',
                radius: '65%',
                label: {
                    normal: {
                        show: true,
                        position: 'inside',
                        formatter: '{c}%', //模板变量有 {a}、{b}、{c}、{d}，分别表示系列名，数据名，数据值，百分比。{d}数据会根据value值计算百分比
                        textStyle : {
                            fontSize : 10,
                            align: 'center',
                            baseline: 'middle',
                            fontWeight : 'bolder'
                        }
                    },
                },
                data: [
                    {value: values['English'], name: 'English'},
                    {value: values['International'], name: 'International'},
                ],
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    }
    chart.setOption(option);
}

function setCitizenChart(area, chart) {
    var values = data.citizen[area];
    var option = {
        title: {
            text: `Australian citizen distribution`,
            // subtext: '纯属虚构',
            left: 'center',
            textStyle: {
                fontSize: 12,
            }
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            top: "30%",
        },
        series: [
            {
                name: 'Language',
                type: 'pie',
                radius: '65%',
                label: {
                    normal: {
                        show: true,
                        position: 'inside',
                        formatter: '{c}%', //模板变量有 {a}、{b}、{c}、{d}，分别表示系列名，数据名，数据值，百分比。{d}数据会根据value值计算百分比
                        textStyle : {
                            fontSize : 10,
                            align: 'center',
                            baseline: 'middle',
                            fontWeight : 'bolder'
                        }
                    },
                },
                data: [
                    {value: values['Australian'], name: 'Australian'},
                    {value: values['International'], name: 'International'},
                ],
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    }
    chart.setOption(option);
}

function setYear12Chart(area, chart) {
    var values = data.year12[area];
    var option = {
        title: {
            text: `Completed Year 12 distribution`,
            // subtext: '纯属虚构',
            left: 'center',
            textStyle: {
                fontSize: 12,
            }
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            top: "30%",
        },
        series: [
            {
                // name: '',
                type: 'pie',
                radius: '65%',
                label: {
                    normal: {
                        show: true,
                        position: 'inside',
                        formatter: '{c}%', //模板变量有 {a}、{b}、{c}、{d}，分别表示系列名，数据名，数据值，百分比。{d}数据会根据value值计算百分比
                        textStyle : {
                            fontSize : 10,
                            align: 'center',
                            baseline: 'middle',
                            fontWeight : 'bolder'
                        }
                    },
                },
                data: [
                    {value: values['Incomplete'], name: 'Incomplete'},
                    {value: values['Completed'], name: 'Completed'},
                ],
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    }
    chart.setOption(option);
}

// initMap();
