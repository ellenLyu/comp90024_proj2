$.ajax({
    url: 'http://172.26.128.60:8080/search/get_all_count',
    type: "POST",
    dataType: "json",
    headers: {'Content-Type':'application/json'},
    data: JSON.stringify({}),
    
}).done(function (res) {
    console.log("query finished");
    console.log(res.data["abs"]);
    $("#abs_title").html(res.data["abs"]);
    $("#large_title").html(res.data["large"]);
    $("#tweets_title").html(res.data["tweets"]);
    $("#covidcases_title").html(res.data["covidcases"]);
    
})

