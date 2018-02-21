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