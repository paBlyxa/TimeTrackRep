myTable = {
	version : "0.1"
};

myTable.heightOffset = 163;
myTable.height = 584;
myTable.gridWidth = 200;
myTable.gridDataHeight = 0;
myTable.rowHeight = 35;
myTable.scaleHeight = 34;
myTable.cellWidth = 70;
myTable.numberColumns = 2;

const CONTAINER_CLASS = "myTableContainer";

// 1 - Create area
myTable.create = function(myTableHere, data){
	$(myTableHere).empty();
	var w = window.innerWidth;
	myTable.height = window.innerHeight - myTable.heightOffset;
	myTable.data = data;
	myTable.area = newDiv(CONTAINER_CLASS);
	myTable.scrollByWheel();
	// 1.1 - Create grid
	myTable.area.appendChild(myTable.createGrid());
	// 1.2 - Create first column field
	myTable.area.appendChild(myTable.createTaskField(w - parseInt(myTable.gridWidth),myTable.height));
	// 1.3 - Create vertical scroll
	myTable.area.appendChild(newDiv("myTableVerScroll", 0, myTable.height - myTable.scaleHeight));
	myTable.area.lastChild.style.display = "block";
	myTable.area.lastChild.style.top = myTable.scaleHeight + "px";myTable.area.lastChild.appendChild(newDiv());
	myTable.area.lastChild.addEventListener("scroll", function() {myTable.scroll($(this).scrollTop());});
	// 1.4 - Create horizontal scroll
	var scrollHorHeight = 18;
	var dataAreaWidth = myTable.numberColumns * myTable.cellWidth; 
	if (w > (dataAreaWidth  + myTable.gridWidth)) {scrollHorHeight = 0;}
	
	myTable.area.appendChild(newDiv("myTableHorScroll", w - 10, scrollHorHeight));
	myTable.area.lastChild.style.display = "block";
	myTable.area.lastChild.appendChild(newDiv());myTable.area.lastChild.lastChild.style.width = dataAreaWidth + myTable.gridWidth + "px";
	myTable.area.lastChild.addEventListener("scroll", function() {
		console.log("scroll " + $(this).scrollLeft());
		$(".myTableScaleLine")[0].style.left = -$(this).scrollLeft() + "px";
		$(".myTableDataArea")[0].style.left = -$(this).scrollLeft() + "px";
	});
	// 1.5 - Resize window
	$(window).resize(function() {myTable.resize();});
	
	// 2 - init and fill
	myTable.init();
	myTableHere.appendChild(myTable.area);
	myTable.resize();
}

// 1.1 - Create grid
myTable.createGrid = function() {
	var myTableGrid = newDiv("myTableGrid", myTable.gridWidth, myTable.height);
	myTableGrid.setAttribute("role", "treegrid");
	
	var gridScale = newDiv("myTableGridScale", myTable.gridWidth, myTable.scaleHeight);
	gridScale.style.lineHeight = myTable.scaleHeight - 1 + "px";
	gridScale.setAttribute("role", "row");
	gridScale.appendChild(newDiv("myTableGridHeadCell myTableGridHeadText",myTable.gridWidth));
	gridScale.childNodes[0].innerText = "Сотрудник";
	
	myTableGrid.appendChild(gridScale);

	var gridData = newDiv("myTableGridData", myTable.gridWidth, 0);
	gridData.setAttribute("role", "rowgroup");
	myTableGrid.appendChild(gridData);

	myTable.gridData = gridData;
	myTable.gridDataHeight = 0;
	
	return myTableGrid;
}

//1.2 - Create first column field
myTable.createTaskField = function(width, height) {
	var area = newDiv("myTableTask", width, height);
	// 1.2.1 - Create scale
	var myTableTaskScale = newDiv("myTableTaskScale", myTable.numberColumns * myTable.cellWidth, myTable.scaleHeight);

	var myTableScaleLine = myTable.createScaleLine();

	myTableTaskScale.appendChild(myTableScaleLine);
	
	$.each(myTable.data["columnNames"], function(index, value){
		if (index != 0){
			myTableScaleLine.appendChild(myTable.createScaleCell(value,
					(index - 1) * myTable.cellWidth, myTable.cellWidth));
		}
	});
	
	area.appendChild(myTableTaskScale);

	// 1.2.2 - Create data area
	var dataArea = newDiv("myTableDataArea", myTable.numberColumns * myTable.cellWidth);
	area.appendChild(dataArea);myTable.dataArea = dataArea;

	return area;
}

myTable.createScaleLine = function() {
	var scaleLine = newDiv("myTableScaleLine");
	scaleLine.style.height = "34px";scaleLine.style.position = "relative";
	scaleLine.style.lineHeight = "34px";
	return scaleLine;}

myTable.createScaleCell = function(text, left, width) {
	var scaleCell = newDiv("myTableScaleCell", width);
	scaleCell.style.height = "34px";scaleCell.style.position = "absolute";scaleCell.innerText = text;scaleCell.style.left = left + "px";scaleCell.title = text;
	scaleCell.addEventListener("mousedown", function(event) {
		myTable.selectColumn($(this).index());
	});
	return scaleCell;
}

//2 - Init and fill
myTable.init = function() {
	console.log("Fill data: " + getSize(myTable.data));
	// 2.2 - Fill grid
	$.each(myTable.data, function(key, value){
		if (key != "columnNames"){
			myTable.gridAppend(myTable.createGridRow(key));
		}
	});
	// 2.3 - Fill data
	// 2.3.1 Create gantt task bg
	var widthBg = myTable.numberColumns * myTable.cellWidth;
	var bg = newDiv("myTableTaskBg", widthBg);
	$.each(myTable.data, function(key, value){
		// 2.3.1.1 - Creata data bg row
		if (key != "columnNames"){
			bg.appendChild(myTable.createTaskRow(value,widthBg));
		}
	});
	myTable.dataAppend(bg);

}

//2.2.1 - Fill grid
myTable.gridAppend = function(child) {
	myTable.gridDataHeight += myTable.rowHeight;
	myTable.gridData.style.height = myTable.gridDataHeight + "px";
	myTable.gridData.appendChild(child);
}
//2.2.2 - Create grid row
myTable.createGridRow = function(name) {
	var row = newDiv("myTableRow");
	row.setAttribute("role", "row");
	row.style.height = myTable.rowHeight + "px";
	row.style.lineHeight = myTable.rowHeight + "px";

	row.appendChild(newDiv("myTableCell"));
	row.lastChild.setAttribute("role", "gridcell");
	row.lastChild.appendChild(newDiv("myTableTreeContent"));
	row.lastChild.lastChild.innerText = name;

	row.addEventListener("mousedown", function(event) {
		myTable.selectRow($(this).index());
	});
	
	return row;
}

//2.3.1 - Fill data area
myTable.dataAppend = function(child) {myTable.dataArea.appendChild(child);}

//2.3.1.1 - Creata data bg row
myTable.createTaskRow = function(data, width) {
	var row = newDiv("myTableTaskRow");
	var cellNumber = width / myTable.cellWidth + 1;
	//row.setAttribute("rowNumber", rowNum);
	row.style.height = "35px";
	row.style.lineHeight = "35px";
	$.each(data, function(index, val){
		var cell = newDiv("myTableTaskCell", myTable.cellWidth);
		cell.innerText = val;
		if (val == "0,0"){
			$(cell).addClass("myTableNullValue");
		}
		row.appendChild(cell);
		cell.addEventListener("mousedown", function(event) {
			myTable.selectColumn($(this).index());
		});
	});
	row.lastChild.className += " myTableLastCell";
	row.addEventListener("mousedown", function(event) {
		myTable.selectRow($(this).index());
	});
	return row;
}

function showLoadImage(here){
	$(here).empty();
	var w = window.innerWidth, h = window.innerHeight;
	var imageDiv = newDiv("loadImage", 250, 250);
	imageDiv.style.backgroundImage = 'url(/resources/images/lg.triple-gears-loading-icon.gif';
	here.appendChild(newDiv("loadImageContainer", w, h));
	here.lastChild.appendChild(imageDiv);
}

/**
* Скрипт для запроса данных
*/
function getStatistic(myTableHere){
	showLoadImage(myTableHere);
	
	// Запрос данных статистика
	$.getJSON("properties/getData.do",
		{
			// Параметры
		},
			// Разбор полученных данных
			function(data) {
				myTable.create(myTableHere,data)
			}
	);
	$(window).resize(function() {myTable.resize();});
}

function getSize(data){
	var count = 0;
	$.each(data, function(index, value){
		count++;
	});
	return count;
}

myTable.selectRow = function(index){
	$(".myTableRowSelected").removeClass("myTableRowSelected");
	$($(".myTableTaskBg")[0].childNodes[index]).addClass("myTableRowSelected");
	$($(".myTableGridData")[0].childNodes[index]).addClass("myTableRowSelected");
}

myTable.selectColumn = function(index){
	$(".myTableColumnSelected").removeClass("myTableColumnSelected");
	$(".myTableTaskRow").each(function(){
		$(this.childNodes[index]).addClass("myTableColumnSelected");
	});
	$($(".myTableScaleLine")[0].childNodes[index]).addClass("myTableColumnSelected");
}

myTable.resize = function() {
	var w = window.innerWidth, h = window.innerHeight;
	myTable.height = h - myTable.heightOffset;
	
	console.log("Resize w: " + w + ", h: " + h, " myTable.height:" + myTable.height, "myTable.dataAreaWidth: " + (myTable.numberColumns * myTable.cellWidth));
	
	$(".myTableTask")[0].style.width = w - myTable.gridWidth + "px";
	$(".myTableHorScroll")[0].style.width = w - 10 + "px";
	
	$(".myTableContainer")[0].style.height = myTable.height + "px";
	
	
	var horScrollVisible = false;
	if (w > (myTable.numberColumns * myTable.cellWidth + myTable.gridWidth)) {
		$(".myTableHorScroll")[0].style.height = "0px";
		$(".myTableGrid")[0].style.height = myTable.height + "px";
		$(".myTableVerScroll")[0].style.height = myTable.height - myTable.scaleHeight + "px";
		$(".myTableTask")[0].style.height = myTable.height + "px";
	} else {
		$(".myTableHorScroll")[0].style.height = "18px";
		$(".myTableGrid")[0].style.height = (myTable.height - 18) + "px";
		$(".myTableVerScroll")[0].style.height = myTable.height - myTable.scaleHeight - 18 + "px";
		$(".myTableTask")[0].style.height = myTable.height - 18 + "px";
		horScrollVisible = true;
	}
	
	if ((myTable.height - myTable.scaleHeight) > (myTable.gridDataHeight)) {
		$(".myTableVerScroll")[0].style.width = "0px";
	} else {
		$(".myTableVerScroll")[0].style.width = "18px";
		$(".myTableVerScroll")[0].lastChild.style.height = myTable.gridDataHeight + "px";
	}
};

myTable.scrollByWheel = function(){
	var elem = myTable.area;
	if (elem.addEventListener) {
		  if ('onwheel' in document) {
		    // IE9+, FF17+, Ch31+
		    elem.addEventListener("wheel", myTable.onWheel);
		  } else if ('onmousewheel' in document) {
		    // устаревший вариант события
		    elem.addEventListener("mousewheel", myTable.onWheel);
		  } else {
		    // Firefox < 17
		    elem.addEventListener("MozMousePixelScroll", myTable.onWheel);
		  }
		} else { // IE8-
		  elem.attachEvent("onmousewheel", myTable.onWheel);
		}
}

myTable.onWheel = function(e) {
	  e = e || window.event;

	  // wheelDelta не дает возможность узнать количество пикселей
	  var delta = e.deltaY || e.detail || e.wheelDelta;
	  delta = delta / 2;
	  var ver_scroll = $(".myTableVerScroll")[0];
	  console.log(ver_scroll.scrollTop);
	  var top = parseInt(ver_scroll.scrollTop);
	  top += delta;
	  ver_scroll.scrollTop =  top;
	  
	  myTable.scroll(ver_scroll.scrollTop);
	  e.preventDefault ? e.preventDefault() : (e.returnValue = false);
}

myTable.scroll = function(delta){
	console.log(delta);
	$(".myTableGridData")[0].style.top = -delta + "px";
	$(".myTableDataArea")[0].style.top = -delta + "px";
}