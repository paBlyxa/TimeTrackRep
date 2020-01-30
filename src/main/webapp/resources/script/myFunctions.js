/**
 * Functions
 */
function newDiv() {
	return document.createElement("div");
}
function newDiv(className) {
	var div = document.createElement("div");
	div.className = className;
	return div;
}
function newDiv(className, width) {
	var div = document.createElement("div");
	div.className = className;
	div.style.width = width + "px";
	return div;
}
function newDiv(className, width, height) {
	var div = document.createElement("div");
	div.className = className;
	div.style.width = width + "px";
	div.style.height = height + "px";
	return div;
}
function formatDate(localDate) {
	var result = "";
	var value = localDate.dayOfMonth.toString();
	if (value.length < 2){
		result += "0";
	}
	result += value + ".";
	value = localDate.monthValue.toString();
	if (value.length < 2){
		result += "0";
	}
	result += value + "." + localDate.year;
	return result;
}


function ajaxPost(url, type, value, success) {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
	    beforeSend: function(xhr) {
	        xhr.setRequestHeader('X-CSRF-TOKEN', token);
	    }
	});
	$.ajax({ 
	    url:url,
	    type:type, 
	    contentType: "application/json; charset=utf-8",
	    dataType : "json",
	    data: value,
	    async: true,    // Cross-domain requests and dataType: "jsonp"
						// requests
						// do not support synchronous operation
	    cache: false,    // This will force requested pages not to be
							// cached
							// by the browser
	    processData:false, // To avoid making query String instead of JSON
	    success: function(responseJsonObject){success(responseJsonObject);},
	    error: function(jqXhr, textStatus, errorMessage){
	        console.log("Error: ", errorMessage);
	     }
	});
}
var mapDayOfWeek = new Map();
mapDayOfWeek.set("MONDAY", "ПОНЕДЕЛЬНИК");
mapDayOfWeek.set("TUESDAY", "ВТОРНИК");
mapDayOfWeek.set("WEDNESDAY", "СРЕДА");
mapDayOfWeek.set("THURSDAY", "ЧЕТВЕРГ");
mapDayOfWeek.set("FRIDAY", "ПЯТНИЦА");
mapDayOfWeek.set("SATURDAY", "СУББОТА");
mapDayOfWeek.set("SUNDAY", "ВОСКРЕСЕНИЕ");
const MONTHS_RU2 = [ "января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря" ];