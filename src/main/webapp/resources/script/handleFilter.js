/**
* Скрипт для обработки действий с фильтром статистики
*/
function handleFilter(id, text1, text2){
	$("#selectItems").SumoSelect({
		placeholder: 'Выберите из списка',
		search: true,
		searchText: 'Введите имя...'
	});
	//В зависимости от выбора checkbox-а (Все проекты/Все задачи)
	//скрываем или отображаем возможные варианты
	$("#typeAllItems").click(function() {
		if ($("#typeAllItems").prop("checked")) {
			$("#items").hide();
		}
		else {
			//Сброс вариантов
			$("#selectItems").empty();
			$(".optWrapper .options").empty();
			// Загружаем возможные варианты (проекты или задачи)
			$.getJSON("stat/getItems.do",
				{ id: id, statPeriod: $('#statPeriod').val(), type: $("#statType").val()},
				function(data){
					$.each(data, function(str, val){
						$("#selectItems")[0].sumo.add(val, str);
					});
				}
			);
			// Отображаем список
			$("#items").slideDown("slow");
		}
	});
	
	// Изменяем текст checkbox-а в зависимости от выбора типа фильтра
	$("#statType").change(function() {
		$("#typeAllItems").prop( "checked", true );
		$("#items").hide();
		if ($("#statType").val() == 1){
			$("#filterItems").slideDown("slow");
			$("#typeAllItemsText").text(text1);
		}
		else if ($("#statType").val() == 2){
			$("#filterItems").slideDown("slow");
			$("#typeAllItemsText").text(text2);
		}
		else {
			// Скрываем фильтр
			$("#filterItems").hide();
		}
	});
};