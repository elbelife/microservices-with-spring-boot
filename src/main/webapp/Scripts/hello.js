$(document).ready(function() {

	setInterval(function() {

		$.ajax({
			url : "http://localhost:8080/microservicesInfo"
		}).then(function(data) {
			$('.greeting-id').html(data.numberOfPendingMessage);
			$('.greeting-content').html(data.numberOfConsumers);
		});

	}, 5000)
});

$().ready(
		function() {
			var url = "microservicesInfo";
			var options = {
				chart : {
					renderTo : "container",
					type : 'area',
					events : {
						load : function() {
							var series = this.series;
							setInterval(function() {
								$.getJSON(url, null, function(ds) {
									series[0].data.push(ds.MessagesEngueued);
									series[1].data.push(ds.numberOfPendingMessage);
									series[2].data.push(ds.messagesDequeued);
								})
							}, 5000);
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
					categories : [ '1750', '1800', '1850', '1900', '1950',
							'1999', '2050' ],
					tickmarkPlacement : 'on',
					title : {
						enabled : false
					}
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
					data : [ 502, 635, 809, 947, 1402, 3634, 5268 ]
				}, {
					name : 'Pending Messages',
					data : [ 106, 107, 111, 133, 221, 767, 1766 ]
				}, {
					name : 'Messages Dequeued',
					data : [ 163, 203, 276, 408, 547, 729, 628 ]
				} ]
			}
			new Highcharts.Chart(options);
		})