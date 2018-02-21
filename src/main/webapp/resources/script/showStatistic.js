var chart1;
var chart2;

/**
* Скрипт для запроса данных статистики
*/
function getStatistic(refresh, id, ctx1, ctx2, text1, text2){
	
	var items = []; // Массив выбранных элементов в списке "Фильтр"
	
	// Если галочка "все" снята, то сохраняем в массив выбранные элементы
	if (! $("#typeAllItems").prop("checked")) {
		$("#selectItems option:selected").each(function() {
			items.push($(this).val());
		})
	}
	
	// Запрос данных статистика
	$.getJSON("stat/getData.do",
		{
			// Id сотрудника (или проекта)
			id: id,
			// Диапазон дат для выборки данных,
			statPeriod: $("#statPeriod").val(),
			//Тип выборки (1 - по проектам, 2 - по задачам, 3 - по времени)
			type: $("#statType").val(),
			//Выборка по всем элементам ( или выбранным
			itemsAll: $("#typeAllItems").prop("checked"), items: items
		},
			
			// Разбор полученных данных
			function(data) {
				showStatistic(data, refresh, ctx1, ctx2, text1, text2);
			}
	);
}

/**
* Скрипт для отображения диаграмм и графиков статистики
*/
function showStatistic(data, refresh, ctx1, ctx2, text1, text2) {
	
	var labels = []; // Массив подписей к трендам или точки по оси Х для графиков
	var counts = []; // Массив значений или точки по оси Y для графиков
	
	// Очистка содержимого таблица
	$("#tableStat tbody").empty();
	
	// Если обновление данных, то предыдущие диаграмма уничтожаем
	if (refresh & (chart1 != null) & (chart2 != null)){
		chart1.destroy();
		chart2.destroy();
	}
	// Подсчитываем общие часы
	var allHours = 0;
	$.each(data, function(str, val){
		allHours += val;
	});
	// Сортировка
	var sortedData = sortProperties(data);
	//Перебираем полученные данные, и отображаем в таблице для типов 1 и 2, сохраняем данные в массивы
	$.each(sortedData, function(index, value){
		if ($("#statType").val() != 3){
			$("#tableStat tbody").append('<tr><td>' + value[0] + '</td><td>' + value[1] + '</td><td>' + (value[1] / allHours * 100).toFixed(1) + '</td></tr>');
			if ($("#statType").val() == 1){
				$("#colName").text(text1);
			} else {
				$("#colName").text(text2);
			}
		}
		labels.push(value[0]);
		counts.push(value[1]);
	});
	
	//Для типов 1 и 2 (1 - по проектам, 2 - по задачам)
	if ($("#statType").val() != 3){
		// Отображаем таблицу
		$("#tableStat").slideDown("slow");
		// Задаем размеры для диаграмм
		$(".leftChart").width("29%");
		$(".rightChart").width("70%");
		// Формируем данные для диаграмм
		dataBy = {
				labels: labels,
				datasets: [
				{
					data: counts,
					backgroundColor: ["#FF6384","#36A2EB","#FFCE56","#42FE38","#FF24F5", "#FF0404", "#FFFF00", "#0009FF", "#00FFFC", "#9500FF"],
					hoverBackgroundColor: ["#E6486A","#238ACF","#D5A735","#2AD521","#D111C7", "#CF0F0F", "#D1D101", "#040BD7", "#06DCD8", "#8206DA"]
				}]
		};
		
		chart1 = new Chart(ctx1,{
			type: 'pie',
			data: dataBy,
			options: {
				common: {
			   	responsive: false
			    }
			}
		});
				
		chart2 = new Chart(ctx2, {
		    type: 'bar',
		    data: dataBy,
		    options: {
			   	scales : {
			  		yAxes: [{
			   			ticks: {
			   				beginAtZero: true
			   			}
			   		}]
			   	},
			   	legend: {
		            display: false
			   	}
			}
		});
	} else {
		// Скрываем таблицу с данными
		$("#tableStat").hide();
		// Устанавливаем ширину для вывода одного графика
		$(".leftChart").width("1%");
		$(".rightChart").width("99%");
		// Формируем данные для графика
		dataBy = {
			    labels: labels,
			    datasets: [
			        {
			            label: "Часы по дням",
			            fill: false,
			            lineTension: 0.1,
			            backgroundColor: "rgba(75,192,192,0.4)",
			            borderColor: "rgba(75,192,192,1)",
			            borderCapStyle: 'butt',
			            borderDash: [],
			            borderDashOffset: 0.0,
			            borderJoinStyle: 'miter',
			            pointBorderColor: "rgba(75,192,192,1)",
			            pointBackgroundColor: "#fff",
			            pointBorderWidth: 1,
			            pointHoverRadius: 5,
			            pointHoverBackgroundColor: "rgba(75,192,192,1)",
			            pointHoverBorderColor: "rgba(220,220,220,1)",
			            pointHoverBorderWidth: 2,
			            pointRadius: 1,
			            pointHitRadius: 10,
			            data: counts,
			            spanGaps: false,
			        }
			    ]
		};
		chart2 = new Chart(ctx2, {
		    type: 'line',
		    data: dataBy,
		    options: {
		        scales: {
		            xAxes: [{
		                type: 'time',
		                time: {
		                    displayFormats: {
		                        quarter: 'MMM YYYY'
		                    }
		                }
		            }]
		        },
			   	legend: {
		            display: false
			   	}
			}	
		});
	}
}

function sortProperties(obj)
{
  // convert object into array
	var sortable=[];
	for(var key in obj)
		if(obj.hasOwnProperty(key))
			sortable.push([key, obj[key]]); // each item is an array in format [key, value]
	
	// sort items by value
	sortable.sort(function(a, b)
	{
	  return a[1]-b[1]; // compare numbers
	});
	return sortable; // array in format [ [ key1, val1 ], [ key2, val2 ], ... ]
}