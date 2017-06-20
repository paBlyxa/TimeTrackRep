<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
	
<head>
<link href="<c:url value="/resources/calendar.css"/>" rel="stylesheet"
	type="text/css">
</head>

<div id="calendar-year-header">
	<span class="calendar-left" id="calendar-prev"> &lang;</span> <span
		class="calendar-year" id="calendar-year-label"> Рабочий календарь на
		2017 год </span> <span class="calendar-right" id="calendar-next">
		&rang;</span>
</div>
<div id="calendar-here"></div>

<sec:authorize access="hasAuthority('Timex руководители')">
<div class="calendar-update">
	<h1>Обновление календаря</h1>
	<c:if test="${message != null}" ><h2>Message : ${message}</h2></c:if>

	<c:url var="uploadUrl" value="/calendar/uploadFile" />
	<form:form method="POST" action="${uploadUrl}"
		enctype="multipart/form-data">
		<input type="file" name="file" />
		<br/>
		<input type="submit" value="Обновить" />
	</form:form>
</div>
</sec:authorize>

<script>
	var currentYear = (new Date).getFullYear();
	
	var CALENDAR = function() {
		var wrap, label, months = [ "Январь", "Февраль", "Март", "Апрель",
				"Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь",
				"Ноябрь", "Декабрь" ], dayOfWeek = [ "Пн", "Вт", "Ср", "Чт",
				"Пт", "Сб", "Вс" ];

		function init(newWrap, month, year) {
			//initialize the calendar widget
			wrap = $(newWrap || "#calendar");
			var content = "<div class='calendar-header'><span class='calendar-month-year' id='calendar-label'>June </span></div>";
			content += "<table id='calendar-days'><tr>";
			var i;
			for (i = 0; i < 7; i++) {
				content += "<td>" + dayOfWeek[i] + "</td>";
			}
			content += "</tr></table><div id='calendar-frame'></div>";
			$(wrap).append(content);
			label = wrap.find("#calendar-label");
			switchMonth(null, month, year);

		}

		function switchMonth(next, month, year) {
			/* var curr = label.text().trim().split(" "), calendar, tempYear = parseInt(
					curr[1], 10); */
			//month = month || ((next) ? ( (curr[0] === "Декабрь") ? 0 : months.indexOf(curr[0]) + 1 ) : ( (curr[0] === "Январь") ? 11 : months.indexOf(curr[0]) - 1 )); 
			//year = year || ((next && month === 0) ? tempYear + 1 : (!next && month === 11) ? tempYear - 1 : tempYear);

			calendar = createCal(year, month);
			$("#calendar-frame", wrap).find(".calendar-curr").removeClass(
					"calendar-curr").addClass("calendar-temp").end().prepend(
					calendar.calendar()).find(".calendar-temp").fadeOut("slow",
					function() {
						$(this).remove();
					});

			label.text(calendar.label);
		}

		function createCal(year, month) {
			var day = 1, i, j, haveDays = true, startDay = new Date(year,
					month, day).getDay() - 1, daysInMonths = [
					31,
					(((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) ? 29
							: 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ], calendar = [];

			if (startDay < 0) {
				startDay = 6;
			}
			var startDayOfMonth = startDay;

			if (createCal.cache[year]) {
				if (createCal.cache[year][month]) {
					return createCal.cache[year][month];
				}
			} else {
				createCal.cache[year] = {};
			}

			i = 0;
			while (haveDays) {
				calendar[i] = [];
				for (j = 0; j < 7; j++) {
					if (i == 0) {
						if (j == startDay) {
							calendar[i][j] = day++;
							startDay++;
						}
					} else if (day <= daysInMonths[month]) {
						calendar[i][j] = day++;
					} else {
						calendar[i][j] = "";
						haveDays = false;
					}
					if (day > daysInMonths[month]) {
						haveDays = false;
					}
				}
				i++;
			}
			/* 			if (calendar[5]) {
			 for (i = 0; i < calendar[5].length; i++) {
			 if (calendar[5][i] !== "") {
			 calendar[4][i] = "<span>" + calendar[4][i]
			 + "</span><span>" + calendar[5][i] + "</span>";
			 }
			 }
			 calendar = calendar.slice(0, 5);
			 } */

			for (i = 0; i < calendar.length; i++) {
				calendar[i] = "<tr><td>" + calendar[i].join("</td><td>")
						+ "</td></tr>";
			}
			calendar = $("<table>" + calendar.join("") + "</table>").addClass(
					"calendar-curr");

			/* 		 	var days = getDays(year, month + 1);

			 console.log(days);
			 $.each(days, function(value, status) {
			 console.log("Put a message here.");
			 if (status == 1){
			 calendar[(value + startDayOfMonth - 1) / 7][(value + startDayOfMonth - 1) % 7].addClass(
			 "calendar-short");
			 console.log("Put a message here. 1");
			 } else if (status == 2){
			 calendar[(value + startDayOfMonth - 1) / 7][(value + startDayOfMonth - 1) % 7].addClass(
			 "calendar-weekend");
			 console.log("Put a message here. 2");
			 }
			 });  */

			$("td:empty", calendar).addClass("calendar-nil");
			if (month === new Date().getMonth()) {
				$('td', calendar).filter(function() {
					return $(this).text() === new Date().getDate().toString();
				}).addClass("calendar-today");
			}
			createCal.cache[year][month] = {
				calendar : function() {
					return calendar.clone()
				},
				label : months[month]
			};

			return createCal.cache[year][month];
		}

		function addWeekend(dayOfMonth) {
			$('td', wrap).filter(function() {
				return $(this).text() == dayOfMonth;
			}).addClass("calendar-weekend");
		}

		function addShort(dayOfMonth) {
			$('td', wrap).filter(function() {
				return $(this).text() == dayOfMonth;
			}).addClass("calendar-short");
		}

		createCal.cache = {};
		return {
			init : init,
			switchMonth : switchMonth,
			createCal : createCal,
			addWeekend : addWeekend,
			addShort : addShort
		};

	};

	function createCalendar(year) {

		var i;
		if (year == null){
			year = currentYear;
		} else {
			currentYear = year;
		}
		$("#calendar-here").empty();
		$("#calendar-year-label").text("Рабочий календарь на " + year + " год");
		
		var content = "<ul>";
		for (i = 0; i < 12; i++) {
			content += "<li><div id='calendar" + i + "' class='calendar'></div></li>";
		}

		content += "</ul>";
		$("#calendar-here").append(content);

		var cal = [];

		for (i = 0; i < 12; i++) {
			cal[i] = CALENDAR();
			cal[i].init("#calendar" + i, i, year);
		}

		// Запрос данных статистика
		$.getJSON("calendar/getDays.do", {
			// Год
			year : year
		},
		// Разбор полученных данных
		function(data) {
			$.each(data, function(value, days) {
				$.each(days, function(index, day) {
					// 					console.log(value + " " + index + " "
					// 							+ day.dateDay.dayOfMonth + " " + day.status);
					if (day.status == "Weekend") {
						cal[value].addWeekend(day.dateDay.dayOfMonth);
					} else {
						cal[value].addShort(day.dateDay.dayOfMonth);
					}
	});

			});
		});
	};

	$(document).ready(function() {
		createCalendar(null);
		$("#calendar-prev").bind("click", function() { createCalendar(currentYear - 1); });
		$("#calendar-next").bind("click", function() { createCalendar(currentYear + 1); });
	});
</script>