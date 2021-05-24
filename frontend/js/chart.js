var covidDom = $("#covid-chart").get(0);
var covidChart = echarts.init(covidDom);
var covidOption;
$.ajax({
    url: 'http://172.26.128.60:8080/search/daily_new',
    type: "POST",
    dataType: "json",
    headers: {'Content-Type':'application/json'},
    data: JSON.stringify({}),
}).done(function (res) {
        console.log("query finished");
        console.log(res.data);
        covidChart.setOption(option = {
            title: {
                text: 'Daily increase in the number of coronaviruses in Australia from January 2020 to May 2021',
                // left: '1%'
                left: 'center',
                textStyle: {
                    fontSize: 14,
                }
            },
            tooltip: {
                trigger: 'axis'
            },
            // grid: {
            //     left: '5%',
            //     right: '15%',
            //     bottom: '10%'
            // },
            xAxis: {
                data: res.data.categoryData.map(function (item) {
                    return item;
                })
            },
            yAxis: {},
            toolbox: {
                right: 10,
                feature: {
                    dataZoom: {
                        yAxisIndex: 'none'
                    },
                    restore: {},
                    saveAsImage: {}
                }
            },
            dataZoom: [{
                startValue: '2020-01-25'
            }, {
                type: 'inside'
            }],
            visualMap: {
                top: 50,
                right: 10,
                pieces: [{
                    gt: 0,
                    lte: 50,
                    color: '#93CE07'
                }, {
                    gt: 50,
                    lte: 100,
                    color: '#FBDB0F'
                }, {
                    gt: 100,
                    lte: 150,
                    color: '#FC7D02'
                }, {
                    gt: 150,
                    lte: 200,
                    color: '#FD0100'
                }, {
                    gt: 200,
                    lte: 300,
                    color: '#AA069F'
                }, {
                    gt: 300,
                    color: '#AC3B2A'
                }],
                outOfRange: {
                    color: '#999'
                }
            },
            series: {
                name: 'COVID-19 cases',
                type: 'line',
                data: res.data.valueData.map(function (item) {
                    return item;
                }),
                markLine: {
                    silent: true,
                    lineStyle: {
                        color: '#333'
                    },
                    data: [{
                        yAxis: 50
                    }, {
                        yAxis: 100
                    }, {
                        yAxis: 150
                    }, {
                        yAxis: 200
                    }, {
                        yAxis: 300
                    }]
                }
            }
        });
    });

covidOption && covidChart.setOption(covidOption);



var populationDom = $("#population-chart").get(0);
var populationChart = echarts.init(populationDom);
var populationOption;

$.ajax({
    url: "http://172.26.128.60:8080/search/covid_scatter",
    type: "POST",
    dataType: "json",
    headers: {'Content-Type':'application/json'},
    data: JSON.stringify({}),
}).done(function(res) {
    console.log("request covid scatter finished");
    let option = {
        color: ['rgba(223, 83, 83, .5)'],
        title: {
            text: "Australian population and the number of confiremed coronavirus cases distribution map",
            left: 'center',
            textStyle: {
                fontSize: 14,
            }
        },
        xAxis: {
            type: 'value',
            name: "population"
        },
        yAxis: {
            type: 'value',
            name: "the number of covid-19 cases"
        },
        tooltip: {
            position: 'top'
        },
    dataZoom: [
        {
            type: 'slider',
            xAxisIndex: 0,
            // start: 10,
            // end: 60
        },
        {
            type: 'inside',
            xAxisIndex: 0,
            // start: 10,
            // end: 60
        },
    ],
    series: [{
        encode: { tooltip: [0, 1, 2] },
        symbolSize: 10,
        data: res.data.map(function(item) {
            let value = item.data;
            return [value[0], value[1], item.name];
        }),
        type: 'scatter',
    }]
    }
    populationChart.setOption(option);
}).fail(function() {
    console.log("request covid scatter failed");
});
