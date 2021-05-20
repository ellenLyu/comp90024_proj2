// var mymap = L.map('mapid').setView([39, 20], 13);
var mymap = L.map('mapid').setView([-37.840935,  144.946457], 13);
var mapboxAccessToken = "pk.eyJ1IjoieXVmZW5nOTciLCJhIjoiY2tvMHJreTdjMGZubTJvdGRtd2Z1eTRsYSJ9.XIBr_3Zx-jdco9UcudpcWA";
L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox/streets-v11',
    tileSize: 512,
    zoomOffset: -1,
    accessToken: mapboxAccessToken,
}).addTo(mymap);

// var geojsonFeature = {
//     "type": "Feature",
//     "properties": {
//         "name": "Coors Field",
//         "amenity": "Baseball Stadium",
//         "popupContent": "This is where the Rockies play!"
//     },
//     "geometry": {
//         "type": "Point",
//         "coordinates": [40, 116.30226]
//     }
// };

// var myLines = [{
//     "type": "LineString",
//     "coordinates": [[-100, 40], [-105, 45], [-110, 55]]
// }, {
//     "type": "LineString",
//     "coordinates": [[-105, 40], [-110, 45], [-115, 55]]
// }];

// L.geoJSON(geojsonFeature).addTo(mymap);

// var myLayer = L.geoJSON().addTo(mymap);
// myLayer.addData(geojsonFeature);


var geojsonLayer = new L.GeoJSON.AJAX("suburb-2-vic.geojson");       
geojsonLayer.addTo(mymap);


function getColor(d) {
    return d > 1000 ? '#800026' :
           d > 500  ? '#BD0026' :
           d > 200  ? '#E31A1C' :
           d > 100  ? '#FC4E2A' :
           d > 50   ? '#FD8D3C' :
           d > 20   ? '#FEB24C' :
           d > 10   ? '#FED976' :
                      '#FFEDA0';
}

function style(feature) {
    return {
        fillColor: getColor(feature.properties.density),
        weight: 2,
        opacity: 1,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7
    };
}

L.geoJson(statesData, {style: style}).addTo(map);

