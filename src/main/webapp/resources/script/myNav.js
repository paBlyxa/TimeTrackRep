/**
 * 
 */
navMenu = {
		version : "0.1"
};

navMenu.create = function(navMenuHere) {
	
	navMenuHere.className = "menuHelp-wrap";
	navMenuHere.setAttribute("id", "menuHelp-wrap");
	
	$(window).scroll(function() {
		if ($(this).scrollTop() > 60) {
			$("#menuHelp-wrap").addClass("menuHelp-wrap-fixed");
		} else {
			$("#menuHelp-wrap").removeClass("menuHelp-wrap-fixed");
		}
	});
	
	var nav = document.createElement("nav");
	nav.className = "menuHelp";
	nav.setAttribute("id", "menu");
	navMenuHere.appendChild(nav);
	
	//var main = document.getElementsByTagName("article")[0].parentNode;
	var childrens = document.getElementsByTagName("article");
	for (var i = 0; i < childrens.length; i++){
		var child = childrens[i];
		nav.appendChild(newDiv("nav--section"));
		nav.lastChild.innerHTML = "<h2 class=\"nav--section-title\"><a href=\"#" +
		child.getAttribute("id") + "\">" + child.getElementsByTagName("h2")[0].innerHTML + "</a></h2>";
		
		var subs = child.getElementsByTagName("h3"); 
		for (var j = 0; j < subs.length; j++){
			var element = subs[j];
			nav.lastChild.innerHTML += "<div class=\"nav--subsection\"><h3 class=\"nav--subsection-title\">" +
			"<a href=\"#" + element.getAttribute("id") + "\">" + element.innerHTML + "</a></h3></div>";
		}
	}
}