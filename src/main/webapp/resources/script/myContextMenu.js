/**
 * 
 */
myContextMenu = {
		version : "0.1"
};

myContextMenu.menuVisible = false;
myContextMenu.element;
myContextMenu.options;

myContextMenu.create = function(options) {
	var contextMenu = document.createElement("div");
	myContextMenu.contextMenu = contextMenu;
	contextMenu.className = "contextMenu";
	if (options != null){
		myContextMenu.options = options;
		var ul = document.createElement("ul");
		ul.className = "contextMenu-options";
		contextMenu.appendChild(ul);
		for (var i = 0; i < options.length; i++){
			var li = document.createElement("li");
			li.className = "contextMenu-option";
			li.setAttribute("rowNumber", i);
			li.innerHTML = options[i].name;
			$(li).click(function() {
				myContextMenu.options[this.getAttribute("rowNumber")].action();
			});
			ul.append(li);
		}
	}
	
	document.getElementsByTagName('body')[0].appendChild(contextMenu);
	
	window.addEventListener("click", e => {
		if(myContextMenu.menuVisible) myContextMenu.toggle("hide");
	});
}

myContextMenu.toggle = function(command) {
	myContextMenu.contextMenu.style.display = command === "show" ? "block" : "none";
	myContextMenu.menuVisible = !myContextMenu.menuVisible;
}

myContextMenu.setPosition = function(top, left) {
	myContextMenu.contextMenu.style.left = left + "px";
	myContextMenu.contextMenu.style.top = top + "px";
	myContextMenu.toggle('show');
}