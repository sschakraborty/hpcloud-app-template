/*
 *  Model creation logic using Vue and jQuery
 *  Version 1.0 - 16th Nov 2018
 */

var status = "";
$(document).prop("title", title);
$("#title_container").html(title);

var resetFunction = function() {
	for (field in model) {
		model[field] = "";
	}
}
var mapTypes = function(type) {
	if (type == "number")
		return type;
	else if (type == "string")
		return "text";
	else if (type == "boolean")
		return "checkbox";
	else
		return "unknown";
}
var html = "";
for (field in model) {
	html += "<tr>";
	html += "<th align='right'>" + fieldName[field] + "</th>";
	html += "<td align='left'>{{ model." + field + " }}</td>";
	html += "</tr>";
}
$("#form_data_display_id").html(html);
html = "";
for (field in model) {
	html += "<tr>";
	html += "<th align='right'>" + fieldName[field] + "</th>";
	html += "<td align='left'>";
	html += "<input type='" + mapTypes(typeof model[field]);
	html += "' v-model='model." + field + "' />";
	html += "</td>";
	html += "</tr>";
}
html += "<tr><th align='center' colspan='2'>";
html += "<button v-on:click='post'>Create</button>";
html += "<button v-on:click='reset'>Reset</button>";
html += "</th></tr>";
$("#form_data_construct_id").html(html);

var app = new Vue({
	el: '#app',
	data: {
		model,
		status
	},
	methods: {
		post: function() {
			var self = this;
			self.status = "Please wait...";
			$.post(postURL, JSON.stringify(model), function(msg) {
				if(msg.message == "success") {
					self.status = "Successful!";
					resetFunction();
				} else {
					self.status = "Operation failed!";
				}
			}, "json");
		},
		reset: function() {
			resetFunction();
		}
	}
});