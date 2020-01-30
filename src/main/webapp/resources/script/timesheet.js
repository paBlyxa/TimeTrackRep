/**
 * Functions for page 'Timesheet' 
 */
function nextWeek(){
	week--;
	if (!(typeof datepicker === "undefined")) 
		datepicker.clear();
	getDate(week);
}
function lastWeek(){
	week++;
	if (!(typeof datepicker === "undefined")) 
		datepicker.clear();
	getDate(week);
}
function deleteTimesheet(id) {
	if (confirm('Удалить запись?')){
		ajaxPost("timesheet/deleteTimesheet", "POST", JSON.stringify(id), function(){
			getDate(week);
		});
	}
}
function validate() {
	if (isNaN(document.getElementById("inputCountTime").value)) {
		document.getElementById("errorValidate").innerHTML = "Введите числовое значение в поле часы";
		return false;
	} else {
		if (document.getElementById("inputCountTime").value <= 0) {
			document.getElementById("errorValidate").innerHTML = "Введите положительное и ненулевое значение в поле часы";
			return false;
		}
	}
	var prjId = $("#selectProject option:selected").val();
	var taskId = $("#selectTask option:selected").val();
	var dates = $("#datepickerTS").val();
	var countTime = $("#inputCountTime").val();
	var comment = $("#inputComment").val();
	
	ajaxPost("timesheet/newTimesheet", "POST", JSON.stringify({
		projectId: prjId, 
		taskId : taskId,
		dates : dates,
		countTime : countTime,
		comment: comment }), function(){
		getDate(week);
	});
	return false;
};
function getDate(week) {
	// Запрос записей
	var url, edit;
	if (typeof employeeId === "undefined") {
		url = "timesheet/getTimesheets.do";
		edit = true;
	} else {
		url = "getTimesheets.do";
		edit = false;
	}
	$.getJSON(url, {week : week},
	// Разбор полученных данных
	function(data) {
		var tablehtml = '';
		var countTime = 0;
		var countOverTime = 0;
		startDate = new Date(Date.UTC(data[0].day.dateDay.year, data[0].day.dateDay.monthValue - 1, data[0].day.dateDay.dayOfMonth));
		endDate = new Date(Date.UTC(data[data.length - 1].day.dateDay.year, data[data.length - 1].day.dateDay.monthValue - 1, data[data.length - 1].day.dateDay.dayOfMonth));
		for (var i = 0; i < data.length; i++) {
			var t = data[i];
			var strDate = formatDate(t.day.dateDay);
			var classDay;
			if (t.day.status == 'Work'){
				classDay = "dateCell timesheet-work";
			} else if (t.day.status == 'Short'){
				classDay = "dateCell timesheet-short";
			} else if (t.day.status == 'Weekend'){
				classDay = "dateCell timesheet-weekend";
			}
			var classRow = "even";
			if (i % 2 == 0)	classRow = "odd";
			
			// переработки
			var overHours;
			if (t.day.status == 'Short'){
				overHours = t.hours - 7;
			} else if (t.day.status == 'Weekend') {
				overHours = t.hours;
			} else {
				overHours = t.hours - 8;
			}
			countTime += t.hours;
			countOverTime += overHours;
			
			var classOverHours = "timesheet-normcount";
					
			if (overHours > 0){
				classOverHours = "timesheet-overcount";
			} else if (overHours < 0) {
				classOverHours = "timesheet-abovecount";
			}
			
			if (t.timesheets.length > 0){
				for (var j = 0; j < t.timesheets.length; j++){
					var s = t.timesheets[j];
					tablehtml += "<tr class=\"" + classRow + "\">";
					if (j == 0) {
						// Столбец дата
						tablehtml += "<td rowspan=\"" + t.timesheets.length + "\" class=\""
							+ classDay + "\" data-value=\"" + strDate + "\">" + mapDayOfWeek.get(t.day.dateDay.dayOfWeek) 
							+ "<br/>"+ strDate + "</td>";
					}
					tablehtml += "<td class=\"projectCell\" data-value=\"" + s.projectId + "\">" + s.project + "</td>";
					tablehtml += "<td class=\"taskCell\" data-value=\"" + s.taskId + "\">" + s.task + "</td>";
					// Часы
					tablehtml += "<td>";
					if (edit)  tablehtml += "<form class=\"modifyTime\" method=\"POST\"><input name=\"timesheetId\" type=\"hidden\" value=\"" + s.id + "\"/>";
					tablehtml += "<input name=\"countTime\" class=\"input\" type=\"number\" value=\""
						+ s.countTime + "\" step=\"0.5\" min=\"0.5\" max=\"24\"";
					if (edit) tablehtml += "/></form>";
					else  tablehtml += " readonly=\"readonly\"/>";
					tablehtml += "</td>";
					if (j == 0) {
						// Общие часы
						tablehtml += "<td rowspan=\"" + t.timesheets.length + "\"><input class=\"input\" "
							+ "type=\"number\" readonly=\"readonly\" value=\"" + t.hours + "\"></td>";

							
						tablehtml += "<td rowspan=\"" + t.timesheets.length + "\" class=\""
							+ classOverHours + "\"><input class=\"input\" type=\"number\" readonly=\"readonly\" "
							+ "value=\"" + overHours + "\"></td>";
					}
					
					// Comment
					tablehtml += "<td>";
					if (edit)  tablehtml += "<form class=\"modifyComment\"><input name=\"timesheetId\" type=\"hidden\" value=\""+ s.id + "\"/>";
					tablehtml += "<input name=\"comment\" class=\"input\" type=\"text\" value=\"" + s.comment + "\"";
					if (edit) tablehtml += "/></form>";
					else  tablehtml += " readonly=\"readonly\" />";
					tablehtml += "</td>";
					if (edit) {
						// Last column - delete
						tablehtml += "<td id=\"colLast\">"
							+ "<button onClick=\"deleteTimesheet(" + s.id + ")\" "
							+ "class=\"btnWithIcon\"><i class=\"fa fa-trash-o fa-fw\"></i></button>"
							+ "</td>";
					}
					tablehtml += "</tr>";
				}
			} else {
				tablehtml += "<tr class=\"" + classRow + "\"><td class=\""
				+ classDay + "\" data-value=\"" + strDate + "\">" + mapDayOfWeek.get(t.day.dateDay.dayOfWeek) + "<br/>"+ strDate
				+ "</td><td></td><td></td><td></td><td></td><td></td><td></td></tr>";
			}
				
		}
		$('.timesheetTable tbody')[0].innerHTML = tablehtml;
		$("#countTime").val(countTime);
		$("#countOverTime").val(countOverTime);

		var str = startDate.getDate() + " ";
		if (startDate.getMonth() != endDate.getMonth()) {
			 str += MONTHS_RU2[startDate.getMonth()] + " ";
		}
		if (startDate.getFullYear() != endDate.getFullYear()){
			str += startDate.getFullYear() + " года ";
		}
		str += "- " + endDate.getDate() + " " + MONTHS_RU2[endDate.getMonth()] + " " + endDate.getFullYear() + " года";
		$("#dateRange")[0].innerHTML = str;
		
		if (edit){
	
			// Выбор даты записи
			datepicker = $('#datepickerTS').datepicker({
				startDate: startDate,
				minDate : startDate,
				maxDate : endDate,
				// Передаем функцию, которая добавляет класс 'my-class'
			    // и делает их невозможными к выбору.
			    onRenderCell: function(date, cellType) {
			        if (cellType == 'day' && dates.inRange(date, startDate, endDate)) {
			            return {classes: '-currentWeek-'}
			        }
			    }
			}).data('datepicker');
			dbClick(datepicker);
			
			$(".modifyComment").submit(function( event ) {
				event.preventDefault();
				var timesheetId = this.childNodes[0].value;
				var comment = this.childNodes[1].value;
				ajaxPost("timesheet/modifyTimesheetComment", "POST", JSON.stringify({ id: timesheetId, comment: comment }) , function(){
					getDate(week);
				});
			});
			$(".modifyTime").submit(function( event ) {
				event.preventDefault();
				var timesheetId = this.childNodes[0].value;
				var countTime = this.childNodes[1].value;
				ajaxPost("timesheet/modifyTime", "POST", JSON.stringify({ id: timesheetId, countTime: countTime }) , function(){
					getDate(week);
				});
			});
		}
	});
}