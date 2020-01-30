/**
 * Скрипт для перехода на страницу по "клику" на строке таблицы
 */
$(document).ready(function() {
	$(".clickable-row").click(function() {
		window.document.location = $(this).data("url");
	});
});

$(document).ready(function() {
	$(".clickable-r-row").contextmenu(function() {
		window.document.location = $(this).data("url-r");
	});
});

$(document).ready(function() {
	$(".clickable-d-row").dblclick(function() {
		window.document.location = $(this).data("url-d");
	});
});


