/**
 * Скрипт для перехода на страницу по "клику" на строке таблицы
 */
$(document).ready(function() {
	$(".clickable-row").click(function() {
		window.document.location = $(this).data("url");
	});
});