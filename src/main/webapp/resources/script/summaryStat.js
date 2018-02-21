summary = {
	version : "0.1"
};

summary.heightOffset = 163;
summary.height = 584;
summary.gridWidth = 200;
summary.gridDataHeight = 0;
summary.rowHeight = 35;
summary.scaleHeight = 34;
summary.cellWidth = 70;

const CONTAINER_CLASS = "summaryContainer";

// 1 - Create area
summary.create = function(summaryHere, data){
	$(summaryHere).empty();
	var w = window.innerWidth;
	summary.numberColumns = getSize(data["&&&"]) - 1;
	summary.height = window.innerHeight - summary.heightOffset;
	summary.data = data;
	summary.area = newDiv(CONTAINER_CLASS);
	summary.scroollByWheel();
	// 1.1 - Create grid
	summary.area.appendChild(summary.createGrid());
	// 1.2 - Create tasks field
	summary.area.appendChild(summary.createTaskField(w - parseInt(summary.gridWidth),summary.height));
	// 1.3 - Create vertical scroll
	summary.area.appendChild(newDiv("summaryVerScroll", 0, summary.height - summary.scaleHeight));
	summary.area.lastChild.style.display = "block";summary.area.lastChild.style.top = summary.scaleHeight + "px";summary.area.lastChild.appendChild(newDiv());
	summary.area.lastChild.addEventListener("scroll", function() {summary.scroll($(this).scrollTop());});
	// 1.4 - Create horizontal scroll
	var scrollHorHeight = 18;
	var dataAreaWidth = summary.numberColumns * summary.cellWidth; 
	if (w > (dataAreaWidth  + summary.gridWidth)) {scrollHorHeight = 0;}
	
	summary.area.appendChild(newDiv("summaryHorScroll", w - 10, scrollHorHeight));
	summary.area.lastChild.style.display = "block";
	summary.area.lastChild.appendChild(newDiv());summary.area.lastChild.lastChild.style.width = dataAreaWidth + summary.gridWidth + "px";
	summary.area.lastChild.addEventListener("scroll", function() {
		console.log("scroll " + $(this).scrollLeft());
		$(".summaryScaleLine")[0].style.left = -$(this).scrollLeft() + "px";
		$(".summaryDataArea")[0].style.left = -$(this).scrollLeft() + "px";
	});
	// 1.5 - Resize window
	$(window).resize(function() {summary.resize();});
	
	// 2 - init and fill
	summary.init();
	summaryHere.appendChild(summary.area);
	summary.resize();
}

// 1.1 - Create grid
summary.createGrid = function() {
	var summaryGrid = newDiv("summaryGrid", summary.gridWidth, summary.height);
	summaryGrid.setAttribute("role", "treegrid");
	
	var gridScale = newDiv("summaryGridScale", summary.gridWidth, summary.scaleHeight);
	gridScale.style.lineHeight = summary.scaleHeight - 1 + "px";
	gridScale.setAttribute("role", "row");
	gridScale.appendChild(newDiv("summaryGridHeadCell summaryGridHeadText",summary.gridWidth));
	gridScale.childNodes[0].innerText = "Сотрудник";
	
	summaryGrid.appendChild(gridScale);

	var gridData = newDiv("summaryGridData", summary.gridWidth, 0);
	gridData.setAttribute("role", "rowgroup");
	summaryGrid.appendChild(gridData);

	summary.gridData = gridData;
	summary.gridDataHeight = 0;
	
	return summaryGrid;
}

//1.2 - Create tasks field
summary.createTaskField = function(width, height) {
	var area = newDiv("summaryTask", width, height);
	// 1.2.1 - Create scale
	var summaryTaskScale = newDiv("summaryTaskScale", summary.numberColumns * summary.cellWidth, summary.scaleHeight);

	var summaryScaleLine = summary.createScaleLine();

	summaryTaskScale.appendChild(summaryScaleLine);
	
	$.each(summary.data["&&&"], function(index, value){
		if (index != 0){
			summaryScaleLine.appendChild(summary.createScaleCell(value,
					(index - 1) * summary.cellWidth, summary.cellWidth));
		}
	});
	
	area.appendChild(summaryTaskScale);

	// 1.2.2 - Create data area
	var dataArea = newDiv("summaryDataArea", summary.numberColumns * summary.cellWidth);
	area.appendChild(dataArea);summary.dataArea = dataArea;

	return area;
}

summary.createScaleLine = function() {
	var scaleLine = newDiv("summaryScaleLine");
	scaleLine.style.height = "34px";scaleLine.style.position = "relative";
	scaleLine.style.lineHeight = "34px";
	return scaleLine;}

summary.createScaleCell = function(text, left, width) {
	var scaleCell = newDiv("summaryScaleCell", width);
	scaleCell.style.height = "34px";scaleCell.style.position = "absolute";scaleCell.innerText = text;scaleCell.style.left = left + "px";scaleCell.title = text;
	scaleCell.addEventListener("mousedown", function(event) {
		summary.selectColumn($(this).index());
	});
	return scaleCell;
}

//2 - Init and fill
summary.init = function() {
	console.log("Fill data: " + getSize(summary.data));
	// 2.2 - Fill grid
	$.each(summary.data, function(key, value){
		if (key != "&&&"){
			summary.gridAppend(summary.createGridRow(key));
		}
	});
	// 2.3 - Fill data
	// 2.3.1 Create gantt task bg
	var widthBg = summary.numberColumns * summary.cellWidth;
	var bg = newDiv("summaryTaskBg", widthBg);
	$.each(summary.data, function(key, value){
		// 2.3.1.1 - Creata data bg row
		if (key != "&&&"){
			bg.appendChild(summary.createTaskRow(value,widthBg));
		}
	});
	summary.dataAppend(bg);

}

//2.2.1 - Fill grid
summary.gridAppend = function(child) {
	summary.gridDataHeight += summary.rowHeight;
	summary.gridData.style.height = summary.gridDataHeight + "px";
	summary.gridData.appendChild(child);
}
//2.2.2 - Create grid row
summary.createGridRow = function(name) {
	var row = newDiv("summaryRow");
	row.setAttribute("role", "row");
	row.style.height = summary.rowHeight + "px";
	row.style.lineHeight = summary.rowHeight + "px";

	row.appendChild(newDiv("SummaryCell"));
	row.lastChild.setAttribute("role", "gridcell");
	row.lastChild.appendChild(newDiv("summaryTreeContent"));
	row.lastChild.lastChild.innerText = name;

	row.addEventListener("mousedown", function(event) {
		summary.selectRow($(this).index());
	});
	
	return row;
}

//2.3.1 - Fill data area
summary.dataAppend = function(child) {summary.dataArea.appendChild(child);}

//2.3.1.1 - Creata data bg row
summary.createTaskRow = function(data, width) {
	var row = newDiv("summaryTaskRow");
	var cellNumber = width / summary.cellWidth + 1;
	//row.setAttribute("rowNumber", rowNum);
	row.style.height = "35px";
	row.style.lineHeight = "35px";
	$.each(data, function(index, val){
		var cell = newDiv("summaryTaskCell", summary.cellWidth);
		cell.innerText = val;
		if (val == "0,0"){
			$(cell).addClass("summaryNullValue");
		}
		row.appendChild(cell);
		cell.addEventListener("mousedown", function(event) {
			summary.selectColumn($(this).index());
		});
	});
	row.lastChild.className += " summaryLastCell";
	row.addEventListener("mousedown", function(event) {
		summary.selectRow($(this).index());
	});
	return row;
}

function showLoadImage(here){
	$(here).empty();
	var w = window.innerWidth, h = window.innerHeight;
	var imageDiv = newDiv("loadImage", 250, 250);
	imageDiv.style.backgroundImage = 'url(/TimeTrack/resources/images/lg.triple-gears-loading-icon.gif';
	here.appendChild(newDiv("loadImageContainer", w, h));
	here.lastChild.appendChild(imageDiv);
}

/**
* Скрипт для запроса данных статистики
*/
function getStatistic(summaryHere){
	showLoadImage(summaryHere);
	
	// Запрос данных статистика
	$.getJSON("stat/getData.do",
		{
			// Диапазон дат для выборки данных,
			statPeriod: $("#statPeriod").val(),
		},
			// Разбор полученных данных
			function(data) {
				summary.create(summaryHere,data)
			}
	);
	$(window).resize(function() {summary.resize();});
}

/**
* Скрипт для отображения статистики
*/
function showStatistic(data) {
	// Очистка содержимого таблица
	$("#tableSummaryStat thead").empty();
	$("#tableSummaryStat tbody").empty();
	
	//Перебираем полученные данные, и отображаем в таблице
	$.each(data, function(key, value){
		if (key != "&&&"){
			// Формируем тело таблицы
			$("#tableSummaryStat tbody").append('<tr>');
			$("#tableSummaryStat tbody").append('<td class="colSumEmployee">' + key + '</td>');
			$.each(value, function(index, val){
				$("#tableSummaryStat tbody").append('<td class="colSumValue">' + val + '</td>');
			});
			$("#tableSummaryStat tbody").append('</tr>');
		} else {
			// Формируем заголовки таблицы
			$("#tableSummaryStat thead").append('<tr>');
			$.each(value, function(index, val){
				if (index == 0){
					$("#tableSummaryStat thead").append('<th scope="col" class="colSumEmployee">' + val + '</th>');
				} else {
					$("#tableSummaryStat thead").append('<th scope="col"><div class="colProjName" title="' + val + '">' + val + '</div></th>');
				}
			});
			$("#tableSummaryStat thead").append('</tr>');
		}
	});
	
	$("#tableSummaryStat").append('</tbody>');
	summary.resize();
}

function getSize(data){
	var count = 0;
	$.each(data, function(index, value){
		count++;
	});
	return count;
}

summary.selectRow = function(index){
	$(".summaryRowSelected").removeClass("summaryRowSelected");
	$($(".summaryTaskBg")[0].childNodes[index]).addClass("summaryRowSelected");
	$($(".summaryGridData")[0].childNodes[index]).addClass("summaryRowSelected");
}

summary.selectColumn = function(index){
	$(".summaryColumnSelected").removeClass("summaryColumnSelected");
	$(".summaryTaskRow").each(function(){
		$(this.childNodes[index]).addClass("summaryColumnSelected");
	});
	$($(".summaryScaleLine")[0].childNodes[index]).addClass("summaryColumnSelected");
}

summary.resize = function() {
	var w = window.innerWidth, h = window.innerHeight;
	summary.height = h - summary.heightOffset;
	
	console.log("Resize w: " + w + ", h: " + h, " summary.height:" + summary.height, "summary.dataAreaWidth: " + (summary.numberColumns * summary.cellWidth));
	
	$(".summaryTask")[0].style.width = w - summary.gridWidth + "px";
	$(".summaryHorScroll")[0].style.width = w - 10 + "px";
	
	$(".summaryContainer")[0].style.height = summary.height + "px";
	
	
	var horScrollVisible = false;
	if (w > (summary.numberColumns * summary.cellWidth + summary.gridWidth)) {
		$(".summaryHorScroll")[0].style.height = "0px";
		$(".summaryGrid")[0].style.height = summary.height + "px";
		$(".summaryVerScroll")[0].style.height = summary.height - summary.scaleHeight + "px";
		$(".summaryTask")[0].style.height = summary.height + "px";
	} else {
		$(".summaryHorScroll")[0].style.height = "18px";
		$(".summaryGrid")[0].style.height = (summary.height - 18) + "px";
		$(".summaryVerScroll")[0].style.height = summary.height - summary.scaleHeight - 18 + "px";
		$(".summaryTask")[0].style.height = summary.height - 18 + "px";
		horScrollVisible = true;
	}
	
	if ((summary.height - summary.scaleHeight) > (summary.gridDataHeight)) {
		$(".summaryVerScroll")[0].style.width = "0px";
	} else {
		$(".summaryVerScroll")[0].style.width = "18px";
		$(".summaryVerScroll")[0].lastChild.style.height = summary.gridDataHeight + "px";
	}
};

summary.scroollByWheel = function(){
	var elem = summary.area;
	if (elem.addEventListener) {
		  if ('onwheel' in document) {
		    // IE9+, FF17+, Ch31+
		    elem.addEventListener("wheel", summary.onWheel);
		  } else if ('onmousewheel' in document) {
		    // устаревший вариант события
		    elem.addEventListener("mousewheel", summary.onWheel);
		  } else {
		    // Firefox < 17
		    elem.addEventListener("MozMousePixelScroll", summary.onWheel);
		  }
		} else { // IE8-
		  elem.attachEvent("onmousewheel", summary.onWheel);
		}
}

summary.onWheel = function(e) {
	  e = e || window.event;

	  // wheelDelta не дает возможность узнать количество пикселей
	  var delta = e.deltaY || e.detail || e.wheelDelta;
	  delta = delta / 2;
	  var ver_scroll = $(".summaryVerScroll")[0];
	  console.log(ver_scroll.scrollTop);
	  var top = parseInt(ver_scroll.scrollTop);
	  top += delta;
	  ver_scroll.scrollTop =  top;
	  
	  summary.scroll(ver_scroll.scrollTop);
	  e.preventDefault ? e.preventDefault() : (e.returnValue = false);
}

summary.scroll = function(delta){
	console.log(delta);
	$(".summaryGridData")[0].style.top = -delta + "px";
	$(".summaryDataArea")[0].style.top = -delta + "px";
}