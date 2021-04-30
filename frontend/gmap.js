const mapStyle = [
    {
        stylers: [{ visibility: "off" }],
    },
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
];

function initMap() {

    // The map, centered at Uluru
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 11,
        center: { lat: -37.840935, lng: 144.946457 },
        styles: mapStyle,
    });
    loadMapShapes();
}

function loadMapShapes() {

    var dataLayer = new google.maps.Data();
    dataLayer.loadGeoJson("./melb_geo.json")

    dataLayer.setStyle(
        function (feature) {
            let name = feature.getProperty('name');
            return {
                fillColor: getColor(feature.getProperty('cartodb_id')),
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

    dataLayer.setMap(map);
    // map.data.loadGeoJson("./melb.json");

}

function getColor(index) {
    switch (index % 5) {
        case 0:
            color = '#89a844';
            break;
        case 1:
            color = '#acd033';
            break;
        case 2:
            color = '#cbd97c';
            break;
        case 3:
            color = '#c2c083';
            break;
        case 4:
            color = '#d1ccad';
    }
    return color;
}

function mouseInToRegion(e) {
    // set the hover state so the setStyle function can change the border
    e.feature.setProperty("state", "hover");
    
    // update the label
    document.getElementById("data-label").textContent = e.feature.getProperty(
        "name"
    );
    document.getElementById("data-value").textContent = e.feature
        .getProperty("cartodb_id");
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

initMap();
