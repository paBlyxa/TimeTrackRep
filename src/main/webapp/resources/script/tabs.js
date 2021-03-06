/**
 * Tabs
 */
function openPage(pageName, elmnt) {
	  // Hide all elements with class="tabcontent" by default */
	  var i, tabcontent, tablinks;
	  tabcontent = elmnt.parentNode.getElementsByClassName("tabcontent");
	  for (i = 0; i < tabcontent.length; i++) {
	    tabcontent[i].style.display = "none";
	  }

	  // Remove the background color of all tablinks/buttons
	  tablinks = elmnt.parentNode.getElementsByClassName("tablink");
	  for (i = 0; i < tablinks.length; i++) {
	    tablinks[i].style.backgroundColor = "";
	    tablinks[i].style.color = "";
	    tablinks[i].style.fontWeight = "";
	  }

	  // Show the specific tab content
	  document.getElementById(pageName).style.display = "block";

	  // Add the specific color to the button used to open the tab content
	  elmnt.style.backgroundColor = "white";
	  elmnt.style.color = "black";
	  elmnt.style.fontWeight = "bold";
	}

$(document).ready(function() {
	// Get the element with id="defaultOpen" and click on it
	var tablinks = document.getElementsByClassName("defaultOpen");
	for (i = 0; i< tablinks.length; i++) {
		tablinks[i].click();
	}
});