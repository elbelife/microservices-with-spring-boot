Highcharts.setOptions({
	global : {
		useUTC : false
	}
});

$().ready(
		function() {
			$("#container").css("height", $(window).height());
			var url = "microservicesInfo";
			var options = {
				chart : {
					renderTo : "container",
					events : {
						load : function() {
							var series = this.series;
							setInterval(function() {
								$.getJSON(url, null, function(ds) {
									var now = new Date();
									series[0].addPoint([ now.getTime(),
											ds.messagesEngueued ], true, true);
									series[1].addPoint([ now.getTime(),
											ds.numberOfPendingMessage ], true,
											true);
									series[2].addPoint([ now.getTime(),
											ds.messagesDequeued ], true, true);
								})
							}, 2000);
						}
					}
				},
				title : {
					text : 'Spring Boot monitor Active MQ'
				},
				subtitle : {
					text : 'Message queue statics'
				},
				xAxis : {
					type : 'datetime',
					text : "Time"
				},
				yAxis : {
					title : {
						text : 'Numbers'
					}
				},
				tooltip : {
					shared : true
				},
				plotOptions : {
					area : {
						stacking : 'normal',
						lineColor : '#666666',
						lineWidth : 1,
						marker : {
							lineWidth : 1,
							lineColor : '#666666'
						}
					}
				},
				series : [ {
					name : 'Messages Enqueued',
					data : generateSeries()
				}, {
					name : 'Pending Messages',
					data : generateSeries()
				}, {
					name : 'Messages Dequeued',
					data : generateSeries()
				} ]
			}
			new Highcharts.Chart(options);
		})

function generateSeries() {
	// generate an array of random data
	var data = [], time = (new Date()).getTime();

	for (var i = -19; i <= 0; i += 1) {
		data.push({
			x : time + i * 1000,
			y : null
		});
	}
	return data;
}