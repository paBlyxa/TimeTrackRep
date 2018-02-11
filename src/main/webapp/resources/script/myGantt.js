/**
 * 
 */
gantt = {
	version : "0.1"
};

// Константы
const MONTHS_RU = [ "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь" ];

const GANTT_CONTAINER_CLASS = "gantt_container";

gantt.cellWidth = 18;
gantt.cellCountMax = 80;
gantt.gridWidth = 200;
gantt.height = 584;
gantt.heightOffset = 135;
gantt.scaleHeight = 34;
gantt.scrollDaysHeight = 75;
gantt.rowHeight = 35;
gantt.gridDataHeight = 0;
gantt.arrayid = [];
gantt.stackUndo = [];
gantt.stackActTypeDelete = 1;
gantt.stackActTypeChange = 2;
gantt.stackActTypeNew = 3;
gantt.employeeId;

gantt.startDate = new Date();

// 1 - Create area
gantt.create = function(ganttHere, startDate, employeeId) {
	if (startDate) {gantt.startDate = startDate;}
	gantt.employeeId = employeeId;
	var w = window.innerWidth;
	gantt.height = window.innerHeight - gantt.heightOffset;
	gantt.area = newDiv(GANTT_CONTAINER_CLASS);
	gantt.scroollByWheel();
	// 1.1 - Create grid
	gantt.area.appendChild(gantt.createGrid());
	// 1.2 - Create tasks field
	gantt.area.appendChild(gantt.createTaskField(w - parseInt(gantt.gridWidth),(gantt.height  - gantt.scrollDaysHeight)));
	// 1.3 - Create vertical scroll
	gantt.area.appendChild(newDiv("gantt_ver_scroll", 0, gantt.height - gantt.scrollDaysHeight - gantt.scaleHeight));
	gantt.area.lastChild.style.display = "block";gantt.area.lastChild.style.top = gantt.scaleHeight + "px";gantt.area.lastChild.appendChild(newDiv());
	gantt.area.lastChild.addEventListener("scroll", function() {gantt.scroll($(this).scrollTop());});
	// 1.4 - Create horizontal scroll
	var scrollHorHeight = 18;
	if (w > 1900) {scrollHorHeight = 0;}
	
	gantt.area.appendChild(newDiv("gantt_hor_scroll", w - 10, scrollHorHeight));
	gantt.area.lastChild.style.display = "block";
	gantt.area.lastChild.appendChild(newDiv());gantt.area.lastChild.lastChild.style.width = "1832px";
	gantt.area.lastChild.addEventListener("scroll", function() {
		console.log("scroll " + $(this).scrollLeft());
		$(".gantt_scale_line")[0].style.left = -$(this).scrollLeft() + "px";
		$(".gantt_scale_line")[1].style.left = -$(this).scrollLeft() + "px";
		$(".gantt_data_area")[0].style.left = -$(this).scrollLeft() + "px";
	});
	// 1.5 - Resize window
	$(window).resize(function() {gantt.resize();});
	// 1.6
	gantt.area.appendChild(gantt.createScrollDays());
	
	// 2 - init and fill
	gantt.init();
	ganttHere.appendChild(gantt.message.create("text"));
	ganttHere.appendChild(gantt.area);
}

// 1.1 - Create grid
gantt.createGrid = function() {
	var ganttGrid = newDiv("gantt_grid", gantt.gridWidth, (gantt.height-gantt.scrollDaysHeight));
	ganttGrid.setAttribute("role", "treegrid");

	var gridScale = newDiv("gantt_grid_scale", gantt.gridWidth, gantt.scaleHeight);
	gridScale.style.lineHeight = gantt.scaleHeight - 1 + "px";
	gridScale.setAttribute("role", "row");
	gridScale.appendChild(newDiv("gantt_grid_head_cell gantt_grid_head_text",gantt.gridWidth));
	gridScale.childNodes[0].innerText = "Фамилия";/*
													 * gridScale.appendChild(newDiv("gantt_grid_column_resize_wrap"));
													 * gridScale.childNodes[1].style.top =
													 * "0px";
													 * gridScale.childNodes[1].style.height =
													 * "35px";
													 * gridScale.childNodes[1].style.left =
													 * "156px";
													 * gridScale.childNodes[1].appendChild(newDiv("gantt_grid_column_resize"));
													 * 
													 * gridScale.appendChild(newDiv("gantt_grid_column_resize_wrap"));
													 * gridScale.childNodes[2].style.top =
													 * "0px";
													 * gridScale.childNodes[2].style.height =
													 * "35px";
													 * gridScale.childNodes[2].style.left =
													 * "246px";
													 * gridScale.childNodes[2].appendChild(newDiv("gantt_grid_column_resize"));
													 */

	ganttGrid.appendChild(gridScale);

	var gridData = newDiv("gantt_grid_data", gantt.gridWidth, 0);
	gridData.setAttribute("role", "rowgroup");
	ganttGrid.appendChild(gridData);

	gantt.gridData = gridData;
	gantt.gridDataHeight = 0;
	
	return ganttGrid;
}

// 1.2 - Create tasks field
gantt.createTaskField = function(width, height) {
	var area = newDiv("gantt_task", width, height);
	// 1.2.1 - Create scale
	var ganttTaskScale = newDiv("gantt_task_scale", 1900 - gantt.gridWidth, gantt.scaleHeight);

	var ganttScaleLineMonth = gantt.createScaleLine(), ganttScaleLineDay = gantt.createScaleLine();

	ganttTaskScale.appendChild(ganttScaleLineMonth);ganttTaskScale.appendChild(ganttScaleLineDay);

	var dateCell = new Date(gantt.startDate);
	var countDayInMonth = 0;
	var prevMonth = gantt.startDate.getMonth(),prevYear = gantt.startDate.getFullYear();
	var offset = 0;
	var cells = (1900 - gantt.gridWidth) / gantt.cellWidth;

	for (var i = 0; i < cells; i++) {
		ganttScaleLineDay.appendChild(gantt.createScaleCell(dateCell.getDate(),
				i * gantt.cellWidth, gantt.cellWidth));
		if (dateCell.getMonth() == prevMonth) {
			countDayInMonth++;
		} else {
			ganttScaleLineMonth.appendChild(gantt.createScaleCell(
					MONTHS_RU[prevMonth] + " " + prevYear, offset,
					countDayInMonth * gantt.cellWidth));
			offset += countDayInMonth * gantt.cellWidth;
			countDayInMonth = 1;
			prevMonth = dateCell.getMonth();
			prevYear = dateCell.getFullYear();
		}
		dateCell.setDate(dateCell.getDate() + 1);
	}
	ganttScaleLineMonth.appendChild(gantt.createScaleCell(MONTHS_RU[prevMonth]
			+ " " + prevYear, offset, countDayInMonth * gantt.cellWidth));

	area.appendChild(ganttTaskScale);

	// 1.2.2 - Create data area
	var dataArea = newDiv("gantt_data_area", 1900 - gantt.gridWidth);
	area.appendChild(dataArea);gantt.dataArea = dataArea;

	area.addEventListener("mouseup", function(event) {gantt.dragg.stopMoving(area, event);});
	return area;
}

gantt.createScaleLine = function() {
	var ganttScaleLine = newDiv("gantt_scale_line");
	ganttScaleLine.style.height = "17px";ganttScaleLine.style.position = "relative";ganttScaleLine.style.lineHeight = "17px";return ganttScaleLine;}

gantt.createScaleCell = function(text, left, width) {
	var ganttScaleCell = newDiv("gantt_scale_cell", width);
	ganttScaleCell.style.height = "17px";ganttScaleCell.style.position = "absolute";ganttScaleCell.innerText = text;ganttScaleCell.style.left = left + "px";return ganttScaleCell;}

// 1.6 - Create scroll days
gantt.createScrollDays = function(){
	var scroll_days = newDiv("gantt_scroll_days");
	scroll_days.style.height = "43px";
	var list = document.createElement('ul');
	list.className = "gantt_menu";
	var items = [
			innerText = [ "Год", "Месяц", "Неделя", "Год", "Месяц", "Неделя" ],
			className = [ 'gantt_menuItem', 'gantt_menuItem', 'gantt_menuItem', 'gantt_menuItem', 'gantt_menuItem',	'gantt_menuItem' ],
			idName = [ 'gantt_menu_left', 'gantt_menu_left', 'gantt_menu_left',	'gantt_menu_right', 'gantt_menu_right', 'gantt_menu_right' ],
			countDays = [ '-year', '-month', '-week', '+year', '+month', '+week' ] ];
	fillList(list, items, gantt.scrollDays);
	scroll_days.appendChild(list);
	return scroll_days;
}

// 1.6.1 - Scroll days
gantt.scrollDays = function(event) {
	var offsetDays = getEventTarget(event).getAttribute("offsetDays");
	console.log(offsetDays);
	var newGantt = $("#gantt_here")[0];
	if (newGantt) {
		switch (offsetDays) {
		case "-year":
			gantt.startDate.setFullYear(gantt.startDate.getFullYear() - 1);
			break;
		case "-month":
			gantt.startDate.setMonth(gantt.startDate.getMonth() - 1);
			break;
		case "-week":
			gantt.startDate.setDate(gantt.startDate.getDate() - 7);
			break;
		case "+year":
			gantt.startDate.setFullYear(gantt.startDate.getFullYear() + 1);
			break;
		case "+month":
			gantt.startDate.setMonth(gantt.startDate.getMonth() + 1);
			break;
		case "+week":
			gantt.startDate.setDate(gantt.startDate.getDate() + 7);
			break;
		}

		$(gantt.area).remove(); 
		gantt.create(newGantt, gantt.startDate, gantt.employeeId);
	}
}

// 2 - Init and fill
gantt.init = function() {
	console.log("init");
	var endDate = new Date(gantt.startDate);
	endDate.setDate(endDate.getDate() + gantt.cellCountMax);
	// Get data by json
	var result = [];
	$.getJSON("vacation/getData.do", {
		// Дата начала
		period : dateToString(gantt.startDate) + " - " + dateToString(endDate),
	},
	// Разбор полученных данных
	function(data) {
		for (var i = 0; i < data.length; i++) {
			result.push({employeeId : data[i].employeeId, name : data[i].shortName, dates : data[i].vacationList});
		}
		console.log(result);
		gantt.data = result;
		// 2.1 - Fill data
		gantt.fillData();
		// 2.4 - Gantt resize
		gantt.resize();
	});
}

// 2.1 - Fill data
gantt.fillData = function() {
	console.log("Fill data: " + gantt.data.length);
	// 2.2 - Fill grid
	for (var i = 0; i < gantt.data.length; i++) {
		gantt.gridAppend(gantt.createGridRow(gantt.data[i], i));
		gantt.arrayid[i] = gantt.data[i].employeeId;
	}
	// 2.3 - Fill data
	// 2.3.1 Create gantt task bg
	var widthBg = 1900 - gantt.gridWidth;
	var bg = newDiv("gantt_task_bg", widthBg);
	for (var i = 0; i < gantt.data.length; i++) {
		// 2.3.1.1 - Creata data bg row
		bg.appendChild(gantt.createTaskRow(i,widthBg));
	}
	gantt.dataAppend(bg);
	// 2.3.2 Create gantt bars area
	var barsArea = newDiv("gantt_bars_area", 1470);
	for (var i = 0; i < gantt.data.length; i++) {
		for (var j = 0; j < gantt.data[i].dates.length; j++) {
			var curDate = dateFromStr(gantt.data[i].dates[j].startDate);
			console.log("Create new line: " + curDate);
			if (dateDiffInDays(gantt.startDate, curDate) >= -gantt.data[i].dates[j].duration) {
				var left = posFromDate(curDate);

				// 2.3.2.1
				var line = gantt.createTaskLine(gantt.data[i], left, i, gantt.data[i].dates[j]);
				
				if (gantt.employeeId == gantt.data[i].employeeId){
					line.addEventListener("mousedown", function(event) {gantt.dragg.startMoving(this, barsArea, event, true);});
				}
				barsArea.appendChild(line);
			}
		}
	}
	gantt.dataAppend(barsArea);
}

// 2.2.1 - Fill grid
gantt.gridAppend = function(child) {gantt.gridDataHeight += gantt.rowHeight;gantt.gridData.style.height = gantt.gridDataHeight + "px";gantt.gridData.appendChild(child);}

// 2.2.2 - Create grid row
gantt.createGridRow = function(data, rowNumber) {
	var row = newDiv("gantt_row");
	row.setAttribute("role", "row");row.setAttribute("rowNumber", rowNumber);
	row.style.height = gantt.rowHeight + "px";row.style.lineHeight = gantt.rowHeight + "px";

	if (gantt.employeeId == data.employeeId){
		row.appendChild(newDiv("gantt_cell gantt_last_cell", 43));
		row.lastChild.setAttribute("role", "gridcell");
		row.lastChild.appendChild(newDiv());
		row.lastChild.lastChild.className = "gantt_add";
		row.lastChild.lastChild.setAttribute("role", "button");
		row.lastChild.lastChild.setAttribute("aria-label", "New");
		row.lastChild.lastChild.addEventListener("click", function() {
			gantt.dialog.newD(data.name, rowNumber);
		});
	}
	row.appendChild(newDiv("gantt_cell"));
	row.lastChild.setAttribute("role", "gridcell");
	// row.childNodes[0].appendChild(newDiv("gantt_tree_icon gantt_close"));
	// row.childNodes[0].appendChild(newDiv("gantt_tree_icon
	// gantt_folder_open"));
	row.lastChild.appendChild(newDiv("gantt_tree_content"));
	row.lastChild.lastChild.innerText = data.name;

	/*
	 * row.appendChild(newDiv("gantt_cell", 90));
	 * row.childNodes[1].style.textAlign = "center";
	 * row.childNodes[1].setAttribute("role", "gridcell");
	 * row.childNodes[1].appendChild(newDiv("gantt_tree_content"));
	 * row.childNodes[1].lastChild.innerText = data.start_date;
	 * 
	 * row.appendChild(newDiv("gantt_cell", 70));
	 * row.childNodes[2].style.textAlign = "center";
	 * row.childNodes[2].setAttribute("role", "gridcell");
	 * row.childNodes[2].appendChild(newDiv("gantt_tree_content"));
	 * row.childNodes[2].lastChild.innerText = data.duration;
	 */
	return row;
}

// 2.3.1 - Fill data area
gantt.dataAppend = function(child) {gantt.dataArea.appendChild(child);}

// 2.3.1.1 - Creata data bg row
gantt.createTaskRow = function(rowNum, width) {
	var row = newDiv("gantt_task_row");
	var cellNumber = width / gantt.cellWidth + 1;
	row.setAttribute("rowNumber", rowNum);
	row.style.height = "35px";
	for (var i = 0; i < cellNumber; i++) { row.appendChild(newDiv("gantt_task_cell", gantt.cellWidth));}
	row.lastChild.className += " gantt_last_cell";
	return row;
}
// 2.3.2.1
gantt.createTaskLine = function(data, left, rowNum, dates) {
	var width = dates.duration * gantt.cellWidth;
	var line = newDiv("gantt_task_line", width, 30);
	line.setAttribute("vacation_id", dates.id);
	line.setAttribute("startDate", dates.startDate);
	line.setAttribute("duration", dates.duration);
	line.setAttribute("rowNumber", rowNum);
	line.setAttribute("changeCount", 0);
	line.style.left = left + "px";
	line.style.top = (rowNum + 1) * 2 + rowNum * 33 + "px";
	line.style.lineHeight = "30px";
	line.appendChild(newDiv("gantt_task_content"));
	line.lastChild.innerText = data.name;
	if (gantt.employeeId == data.employeeId){		
		line.appendChild(newDiv("gantt_task_drag task_left"));
		line.lastChild.addEventListener("mousedown", function(event) {gantt.dragg.startMoving(this, line, event, true);});
		line.appendChild(newDiv("gantt_task_drag task_right"));
		line.lastChild.addEventListener("mousedown", function(event) {gantt.dragg.startMoving(this, line, event, true);});
		line.addEventListener("dblclick", function(event) {gantt.dialog.changeD(this, data, rowNum);});
	}
	return line;
}

// 2.4 - Resize
gantt.resize = function() {
	var w = window.innerWidth, h = window.innerHeight;
	gantt.height = h - gantt.heightOffset;
	
	console.log("Resize w: " + w + ", h: " + h, " gantt.height:" + gantt.height);
	
	$(".gantt_task")[0].style.width = w - gantt.gridWidth + "px";
	$(".gantt_hor_scroll")[0].style.width = w - 10 + "px";
	
	$(".gantt_task")[0].style.height = gantt.height - gantt.scrollDaysHeight + "px";
	$(".gantt_grid")[0].style.height = gantt.height - gantt.scrollDaysHeight + "px";
	$(".gantt_ver_scroll")[0].style.height = gantt.height - gantt.scrollDaysHeight - gantt.scaleHeight + "px";
	
	var horScrollVisible = false;
	if (w > 1900) {
		$(".gantt_hor_scroll")[0].style.height = "0px";
	} else {
		$(".gantt_hor_scroll")[0].style.height = "18px";
		horScrollVisible = true;
	}
	
	if ((gantt.height - gantt.scrollDaysHeight - gantt.scaleHeight) > (gantt.gridDataHeight)) {
		$(".gantt_ver_scroll")[0].style.width = "0px";
	} else {
		$(".gantt_ver_scroll")[0].style.width = "18px";
		$(".gantt_ver_scroll")[0].lastChild.style.height = gantt.gridDataHeight + "px";
	}
};


gantt.dragg = function() {
	var divV,eWi, resizeAndMove = 0;// 0 - onlymove, 1 - moveAndResize(leftDrag), 2 -
							// resize(rightDrag)

	return {
		move : function(divid, xpos, ypos, onlyX, width) {
			if (resizeAndMove < 2) {
				divid.style.left = xpos + 'px';
				if (!onlyX) {divid.style.top = ypos + 'px';}
			}
			if (resizeAndMove == 1) {
				console.log("resize: " + (width + eWi));
				divid.style.width = width + eWi + 'px';
			}
			if (resizeAndMove == 2) {
				console.log("resize: " + (-width + eWi));
				divid.style.width = -width + eWi + 'px';
			}
		},
		startMoving : function(divid, container, evt, onlyX) {
			divV = divid;
			if (divid.className == "gantt_task_drag task_left") {resizeAndMove = 1;}
			else if (divid.className == "gantt_task_drag task_right") { resizeAndMove = 2;}
			else {
				evt = evt || window.event;
				var posX = evt.clientX, posY = evt.clientY, divTop = divid.style.top, divLeft = divid.style.left, eHe = parseInt(divid.style.height), cWi = parseInt(container.style.width), cHe = parseInt(container.style.height);
				eWi = parseInt(divid.style.width);
				container.style.cursor = 'move';
				divTop = divTop.replace('px', '');
				divLeft = divLeft.replace('px', '');
				var diffX = posX - divLeft, diffY = posY - divTop;
				document.onmousemove = function(evt) {
					evt = evt || window.event;
					var posX = evt.clientX, posY = evt.clientY, aX = posX - diffX, aY = posY - diffY;
					if (aX < 0) aX = 0;
					if (aY < 0)	aY = 0;
					if (aX + eWi > cWi)	aX = cWi - eWi;
					if (aY + eHe > cHe)	aY = cHe - eHe;
					gantt.dragg.move(divid, aX, aY, onlyX, (divLeft - aX));
				}
			}
		},
		stopMoving : function(container, event) {
			var isChanged = false;
			if (divV && divV.getAttribute("vacation_id")) {
				var divLeft = parseInt(divV.style.left), divWidth = parseInt(divV.style.width),offset = divLeft % gantt.cellWidth,oldDuration = divV.getAttribute("duration");
				if (offset >= (gantt.cellWidth / 2)) { offset = gantt.cellWidth - offset;}
				else { offset = -offset;}
				divLeft += offset;
				if (resizeAndMove < 2) {divV.style.left = divLeft + 'px';}
				if (resizeAndMove == 1) {
					divWidth -= offset;
					divV.style.width = divWidth + 'px';
					if (divWidth/gantt.cellWidth != oldDuration){
						divV.setAttribute("duration", divWidth / gantt.cellWidth);
						isChanged = true;
					}
				}
				if (resizeAndMove == 2) {
					offset = divWidth % gantt.cellWidth;
					if (offset >= (gantt.cellWidth / 2)) {offset = gantt.cellWidth - offset;}
					else {offset = -offset;}
					divWidth += offset;
					divV.style.width = divWidth + 'px';
					if (divWidth/gantt.cellWidth != oldDuration){
						divV.setAttribute("duration", divWidth / gantt.cellWidth);
						isChanged = true;
					}
				}
				// Изменение даты в поле после сдвига даты
				var newDateStr = dateToString(dateFromPos(divLeft)), oldDateStr = divV.getAttribute("startDate");
				if (newDateStr != oldDateStr){
					divV.setAttribute("startDate", newDateStr);
					isChanged = true;
				}
				
				if (isChanged){gantt.modifyDiv(gantt.stackActTypeChange, divV, oldDateStr, oldDuration);}
				
				container.style.cursor = 'default';				
			}
			resizeAndMove = 0;
			document.onmousemove = function() {}
		},
	}
}();

gantt.message = function() {
	var innerDiv;
	var buttonSave;
	return {
		create : function(message){
			var divMessage;
			if (!$(".gantt_message").length){
				divMessage = newDiv("gantt_message");
				innerDiv = newDiv("gantt_message_inner");
				innerDiv.style.display = "none";
				innerDiv.appendChild(newDiv("gantt_message_text"));
				innerDiv.lastChild.innerHTML = "<h3>" + message + "</h3>";
				buttonSave = newDiv("gantt_message_button");
				innerDiv.appendChild(buttonSave);
				innerDiv.lastChild.innerHTML = '<input type="button" class="gantt_message_btn" value="Сохранить">';
				innerDiv.lastChild.lastChild.addEventListener("click", function(event){
					console.log("Сохранить");
					gantt.save();
				});
				innerDiv.appendChild(newDiv("gantt_message_button"));
				innerDiv.lastChild.innerHTML = '<input type="button" class="gantt_message_btn" value="Отменить">';
				innerDiv.lastChild.lastChild.addEventListener("click", function(event){
					if (gantt.undo()){
						$(innerDiv).slideUp("slow");
					}
				});
				divMessage.appendChild(innerDiv);
			} else {
				divMessage = $(".gantt_message")[0];
			}
			return divMessage;
		},
		show : function(text){
			innerDiv.firstChild.innerHTML = "<h3>" + text + "</h3>";
			$(buttonSave).show();
			$(innerDiv).slideDown("slow");
		},
		error : function(text){
			innerDiv.firstChild.innerHTML = "<h3>" + text + "</h3>";
			$(buttonSave).hide();
			$(innerDiv).slideDown("slow");
		},
		myHide : function(){
			$(innerDiv).slideUp("slow");
		}
	
	}
}();

gantt.save = function(){
	var data = [];
	$(".gantt_changed").each(function(){
		data.push({
				employeeId : gantt.arrayid[this.getAttribute("rowNumber")],
				id : this.getAttribute("vacation_id"),
				startDate : this.getAttribute("startDate"),
				duration : this.getAttribute("duration")})
	});
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
	    beforeSend: function(xhr) {
	        xhr.setRequestHeader('X-CSRF-TOKEN', token);
	    }
	});
	if (data.length > 0){
		$.ajax({ 
		    url:"vacation/saveData",
		    type:"POST", 
		    contentType: "application/json; charset=utf-8",
		    dataType : "json",
		    data: JSON.stringify(data), // Stringified Json Object
		    async: true,    // Cross-domain requests and dataType: "jsonp"
							// requests
							// do not support synchronous operation
		    cache: false,    // This will force requested pages not to be
								// cached
								// by the browser
		     processData:false, // To avoid making query String instead of JSON
		     success: function(data){
		        // Success Action
		    	 console.log(data);
		    	 $(".gantt_changed").each(function(){
		    		 if (this.getAttribute("vacation_id") == "null"){
		    			 for (var i = 0; i < data.length; i++) {
		    				if (data[i].employeeId == gantt.arrayid[this.getAttribute("rowNumber")]) {
		    					for (var j = 0; j < data[i].vacationList.length; j++){
		    						if (this.getAttribute("startDate") == data[i].vacationList[j].startDate){
		    							this.setAttribute("vacation_id", data[i].vacationList[j].id);
		    							console.log("Get new vacation_id: " + data[i].vacationList[j].id);
		    							break;
		    						}
		    					}
		    					break;
		    				}
		 					
		 				}
		    		 }
		    	 });
		 		
		    	 $(".gantt_changed").removeClass("gantt_changed");
		    	 gantt.message.myHide();
		    }
		});
	}
	var dataToDelete = [];
	$(".gantt_deleted").each(function(){
		dataToDelete.push({
				employeeId : gantt.arrayid[this.getAttribute("rowNumber")],
				id : this.getAttribute("vacation_id"),
				startDate : this.getAttribute("startDate"),
				duration : this.getAttribute("duration")})
	});
	if (dataToDelete.length > 0){
		$.ajax({ 
		    url:"vacation/deleteData",
		    type:"POST", 
		    contentType: "application/json; charset=utf-8",
		    data: JSON.stringify(dataToDelete), // Stringified Json Object
		    async: true,    // Cross-domain requests and dataType: "jsonp"
							// requests
							// do not support synchronous operation
		    cache: false,    // This will force requested pages not to be
								// cached
								// by the browser
		     processData:false, // To avoid making query String instead of JSON
		     success: function(resposeJsonObject){
		        // Success Action
		    	 console.log("Delete: " + resposeJsonObject);
		    	 $(".gantt_deleted").remove();
		    	 gantt.message.myHide();
		    }
		});
	}
	gantt.stackUndo = [];
}

gantt.undo = function() {
	if (gantt.stackUndo.length > 0){
		var act = gantt.stackUndo.pop();
		console.log("Undo: " + act.type);
		switch(act.type){
			case gantt.stackActTypeDelete:
				if (act.line.getAttribute("vacation_id") != "null"){
					$(act.line).removeClass("gantt_deleted");
				} else {
					$(".gantt_bars_area")[0].appendChild(act.line);
				}
				
			break;
			case gantt.stackActTypeChange:
				act.line.setAttribute("startDate", act.oldDate);
				act.line.setAttribute("duration", act.oldDuration);
				$(act.line).animate({
					left: posFromDate(dateFromStr(act.oldDate)) + "px",
					width : act.oldDuration * gantt.cellWidth + "px"
				}, {
					done: function(){
						if (!gantt.checkCollision(act.line)){
							if (gantt.stackUndo.length > 0){	
								gantt.message.show("Изменения не сохранены. Сохранить изменения?");
							}
						} else {
							gantt.message.error("Ошибка. Невозможно сохранить изменения!");
						}
					}
				});
				act.line.setAttribute("changeCount", (act.line.getAttribute("changeCount")-1));
				if (act.line.getAttribute("changeCount") < 1)
					$(act.line).removeClass("gantt_changed");	
			break;
			case gantt.stackActTypeNew:
				$(act.line).remove();	
			break;
		}
	}
	if (act.type != gantt.stackActTypeChange){
		if (!gantt.checkCollision(act.line)){
			gantt.message.show("Изменения не сохранены. Сохранить изменения?");
		} else {
			gantt.message.error("Ошибка. Невозможно сохранить изменения!");
		}	
	}
	return (gantt.stackUndo.length == 0);
}

gantt.dialog = function() {
	var dialog;
	var rowNumber;
	var lineDiv;
	var newVacation;
	return {
		newD : function(employee, rowNum) {
			newVacation = true;
			rowNumber = rowNum;
			if (!$(".gantt_cal_light").length) {
				dialog = gantt.dialog.create(employee + " - Новый отпуск");
				$('body').prepend(dialog);
				$("#selectMonth").val(gantt.startDate.getMonth()+1).change();
				$("#selectDay").val(gantt.startDate.getDate()).change();
			} else {
				dialog.firstChild.lastChild.innerText =  employee + " - Новый отпуск";
				dialog.style.display = 'block';
			}
			$('body').append('<div class="gantt_cal_cover"></div>');
		},
		changeD : function(line, data, rowNum){
			newVacation = false;
			rowNumber = rowNum;
			lineDiv = line;
			if (!$(".gantt_cal_light").length) {
				dialog = gantt.dialog.create(data.name + " - Изменить отпуск");
				$('body').prepend(dialog);
			} else {
				dialog.firstChild.lastChild.innerText =  data.name + " - Изменить отпуск";
				dialog.style.display = 'block';
			}
			var parts = lineDiv.getAttribute("startDate").split(".");
			$("#selectYear").val(parseInt(parts[2])).change();
			$("#selectMonth").val(parseInt(parts[1])).change();
			$("#selectDay").val(parseInt(parts[0])).change();
			$(".gantt_duration_value").val(lineDiv.getAttribute("duration"));
			$(".gantt_delete_btn_set")[0].style.display = "block";
			$('body').append('<div class="gantt_cal_cover"></div>');
		},
		create : function(text) {
			var dialog = newDiv("gantt_cal_light");
			dialog.style.left = "141px";
			dialog.style.top = "201px";

			dialog.appendChild(newDiv("gantt_cal_ltitle"));
			dialog.lastChild.style.cursor = "pointer";
			dialog.lastChild.appendChild(document.createElement("span"))
			dialog.lastChild.lastChild.innerText = text;
			dialog.lastChild.lastChild.className = "gantt_title";
			dialog.lastChild.addEventListener("mousedown", function(event) {
				gantt.dragg.startMoving(dialog, $(".gantt_cal_cover")[0], event, false);
			});
			dialog.lastChild.addEventListener("mouseup", function(event) {
				gantt.dragg.stopMoving($(".gantt_cal_cover")[0], event);
			});
			
			dialog.appendChild(newDiv("gantt_cal_larea"));
			dialog.lastChild.style.height = "75px";
			dialog.lastChild.appendChild(newDiv("gantt_cal_lsection"));
			dialog.lastChild.lastChild.appendChild(document
					.createElement("label"));
			dialog.lastChild.lastChild.lastChild.innerText = "Период";

			dialog.lastChild.appendChild(newDiv("gantt_section_time"));
			dialog.lastChild.lastChild.style.height = "30px";
			var timeSelects = newDiv("gantt_time_selects");
			
			var selectDayList = '<select id="selectDay" aria-label="День">';
			for (var i = 1; i < 32; i++){
				selectDayList += '<option value="' + i + '">' + i
				+ '</option>';
			}
			selectDayList += "</select>";
			timeSelects.innerHTML = selectDayList;
			var selectList = '<select id="selectMonth" aria-label="Месяц">';
			for (var x = 1; x <= 12; x++) {
				selectList += '<option value="' + x + '">' + MONTHS_RU[x-1]
						+ '</option>';
			}
			selectList += "</select>";
			timeSelects.innerHTML += selectList;
			var selectYearList = '<select id="selectYear" aria-label="Год"/>';
			var yearNow = (new Date()).getFullYear();
			for (var i = 0; i < 5; i++) {
				selectYearList += '<option value="' + yearNow + '">' + yearNow
						+ '</option>';
				yearNow++;
			}
			selectYearList += "</select>";
			timeSelects.innerHTML += selectYearList;
			dialog.lastChild.lastChild.appendChild(timeSelects);

			var divDuration = newDiv("gantt_duration");
			divDuration.innerHTML = '<input type="button" class="gantt_duration_dec" value="-">';
			divDuration.innerHTML += '<input  type="number" class="gantt_duration_value" value="7" aria-label="Продолжительность" step="1" min="1" max="28">';
			divDuration.innerHTML += '<input type="button" class="gantt_duration_inc" value="+">';
			divDuration.innerHTML += '" Дней"';
			divDuration.innerHTML += '<span></span>';
			divDuration.childNodes[0].addEventListener("click", function(){
				var value = $(divDuration.childNodes[1]).val(); 
				if (value > 1)
					$(divDuration.childNodes[1]).val(value - 1);
			});
			divDuration.childNodes[2].addEventListener("click", function(){
				var value = parseInt($(divDuration.childNodes[1]).val());
				if (value < 28)
					$(divDuration.childNodes[1]).val(value + 1);
			});
			dialog.lastChild.lastChild.appendChild(divDuration);

			dialog
					.appendChild(newDiv("gantt_btn_set gantt_left_btn_set gantt_save_btn_set"));
			dialog.lastChild.appendChild(newDiv("gantt_save_btn"));
			dialog.lastChild.appendChild(newDiv());
			dialog.lastChild.lastChild.innerHTML = "Save";
			dialog.lastChild.lastChild.addEventListener("click", function() {
				gantt.dialog.save();
				gantt.dialog.close();
			});
			
			dialog
					.appendChild(newDiv("gantt_btn_set gantt_left_btn_set gantt_cancel_btn_set"));
			dialog.lastChild.appendChild(newDiv("gantt_cancel_btn"));
			dialog.lastChild.appendChild(newDiv());
			dialog.lastChild.lastChild.innerHTML = "Cancel";
			dialog.lastChild.lastChild.addEventListener("click", function() {
				gantt.dialog.close();
			});
			
			dialog
					.appendChild(newDiv("gantt_btn_set gantt_right_btn_set gantt_delete_btn_set"));
			dialog.lastChild.appendChild(newDiv("gantt_delete_btn"));
			dialog.lastChild.appendChild(newDiv());
			dialog.lastChild.lastChild.innerHTML = "Delete";
			dialog.lastChild.style.display = "none";
			dialog.lastChild.style.float = "right";
			dialog.lastChild.lastChild.addEventListener("click", function() {
				gantt.dialog.deleteD();
			});
		
			return dialog;
		},
		close : function() {
			dialog.style.display = 'none';
			$(".gantt_delete_btn_set")[0].style.display = "none";
			$('.gantt_cal_cover').remove();
		},
		save : function() {
			var curDate = new Date($("#selectYear").val(), $("#selectMonth").val()-1, $("#selectDay").val());
			var dates = {startDate: dateToString(curDate), duration:$(".gantt_duration_value").val(), id: null};
			var isChanged = false;
			if (newVacation){
				lineDiv = gantt.createTaskLine(gantt.data[rowNumber], posFromDate(curDate), rowNumber,
					dates);
				lineDiv.addEventListener("mousedown", function(event) {
					gantt.dragg.startMoving(this, $(".gantt_bars_area")[0], event, true);
				});
				$(".gantt_bars_area")[0].appendChild(lineDiv);
				gantt.modifyDiv(gantt.stackActTypeNew, lineDiv, null, null);
			} else {
				if ((lineDiv.getAttribute("startDate") != dates.startDate) ||
						(lineDiv.getAttribute("duration") != dates.duration)){

					var oldDate = lineDiv.getAttribute("startDate");
					var oldDuration = lineDiv.getAttribute("duration");
					lineDiv.setAttribute("startDate", dates.startDate);
					lineDiv.setAttribute("duration", dates.duration);
					$(lineDiv).animate({
						left: posFromDate(curDate) + "px",
						width : $(".gantt_duration_value").val() * gantt.cellWidth + "px"
					});
					gantt.modifyDiv(gantt.stackActTypeChange, lineDiv, oldDate, oldDuration);
				}
			}
		},
		deleteD : function() {
			if (!newVacation){
				if (lineDiv.getAttribute("vacation_id") != "null"){
					$(lineDiv).addClass("gantt_deleted");
				} else {
					$(lineDiv).remove();
				}
			}
			gantt.dialog.close();
			gantt.pushInStack(gantt.stackActTypeDelete, lineDiv, null, null);
			if (!gantt.checkCollision(lineDiv)){
				gantt.message.show("Изменения не сохранены. Сохранить изменения?");
			} else {
				gantt.message.show("Ошибка. Невозможно сохранить изменения!");
			}	
		}
	}
}();

gantt.modifyDiv = function(type, line, oldDate, oldDuration){
	$(line).addClass("gantt_changed");
	line.setAttribute("changeCount", (parseInt(line.getAttribute("changeCount")) + 1));
	gantt.pushInStack(type, line, oldDate, oldDuration);
	if (!gantt.checkCollision(line)){
		gantt.message.show("Изменения не сохранены. Сохранить изменения?");
	} else {
		gantt.message.error("Ошибка. Невозможно сохранить изменения!");
	}	
	console.log(gantt.checkCollision(line));
}

gantt.pushInStack = function(type, line, oldDate, oldDuration){
	console.log("Push in stack: type=" + type + ", line=" + line + ", oldDate=" + oldDate + ", oldDuration=" + oldDuration);
	gantt.stackUndo.push({type: type, line: line, oldDate: oldDate, oldDuration: oldDuration});
}

gantt.scroollByWheel = function(){
	var elem = gantt.area;
	if (elem.addEventListener) {
		  if ('onwheel' in document) {
		    // IE9+, FF17+, Ch31+
		    elem.addEventListener("wheel", gantt.onWheel);
		  } else if ('onmousewheel' in document) {
		    // устаревший вариант события
		    elem.addEventListener("mousewheel", gantt.onWheel);
		  } else {
		    // Firefox < 17
		    elem.addEventListener("MozMousePixelScroll", gantt.onWheel);
		  }
		} else { // IE8-
		  elem.attachEvent("onmousewheel", gantt.onWheel);
		}
}

gantt.onWheel = function(e) {
	  e = e || window.event;

	  // wheelDelta не дает возможность узнать количество пикселей
	  var delta = e.deltaY || e.detail || e.wheelDelta;
	  var ver_scroll = $(".gantt_ver_scroll")[0];
	  console.log(ver_scroll.scrollTop);
	  var top = parseInt(ver_scroll.scrollTop);
	  top += delta;
	  ver_scroll.scrollTop =  top;
	  
	  gantt.scroll(ver_scroll.scrollTop);
	  e.preventDefault ? e.preventDefault() : (e.returnValue = false);
	}

gantt.scroll = function(delta){
	console.log(delta);
	$(".gantt_grid_data")[0].style.top = -delta + "px";
	$(".gantt_data_area")[0].style.top = -delta + "px";
}

gantt.checkCollision = function(elem){
	var result = false;
	$(".gantt_task_line").each(function(){
		if (!(this == elem)){
			if (this.getAttribute("rownumber") == elem.getAttribute("rownumber")) {
				var ax = parseInt(this.style.left);
				var aw = parseInt(this.style.width);
				var bx = parseInt(elem.style.left);
				var bw = parseInt(elem.style.width);
				if (!(((ax + aw) <= bx) ||
						((bx + bw) <= ax))) {
					console.log("Collision elements: " + this + ", " + elem);
					result = true;
				}
			}
		}
	});
	return result;
}

/* Functions */
function newDiv() {
	return document.createElement("div");
}
function newDiv(className) {
	var div = document.createElement("div");
	div.className = className;
	return div;
}
function newDiv(className, width) {
	var div = document.createElement("div");
	div.className = className;
	div.style.width = width + "px";
	return div;
}
function newDiv(className, width, height) {
	var div = document.createElement("div");
	div.className = className;
	div.style.width = width + "px";
	div.style.height = height + "px";
	return div;
}
function dateToString(date) {
	if (date instanceof Date){
		return (date.getDate() > 9 ? "" + date.getDate() : "0" + date.getDate())
			+ "."
			+ (date.getMonth() > 8 ? "" + (date.getMonth() + 1) : "0"
					+ (date.getMonth() + 1)) + "." + (date.getFullYear());
	} else return localDateToString(date);
}
function dateFromStr(str){
	var parts = str.split('.');
	return new Date(parts[2], (parts[1]-1), parts[0]);
};
function localDateToString(localDate) {
	return (localDate.dayOfMonth > 9 ? "" + localDate.dayOfMonth : "0" + localDate.dayOfMonth)
			+ "."
			+ (localDate.monthValue > 9 ? "" + (localDate.monthValue) : "0"
					+ (localDate.monthValue)) + "." + (localDate.year);
}
function fillList(list, items, dynamicEvent) {
	for (var i = 0; i < items[0].length; i++) {
		var li = document.createElement('li');
		li.innerHTML = items[0][i];
		li.className = items[1][i];
		li.id = items[2][i];
		li.setAttribute("offsetDays", items[3][i]);
		li.onclick = dynamicEvent;
		list.appendChild(li);
	}
}
function dateFromLocalDate(localDate){
	return new Date(localDate.year, localDate.monthValue -1, localDate.dayOfMonth);
}
function posFromDate(curDate) {
	return (dateDiffInDays(gantt.startDate, curDate) * gantt.cellWidth);
}
function dateFromPos(pos) {
	var newDate = new Date(gantt.startDate);
	newDate.setDate(newDate.getDate() + (pos - pos % gantt.cellWidth)
			/ gantt.cellWidth);
	return newDate;
}
function dateDiffInDays(a, b) {
	var _MS_PER_DAY = 1000 * 60 * 60 * 24;
	// Discard the time and time-zone information.
	var utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
	var utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());

	return Math.floor((utc2 - utc1) / _MS_PER_DAY);
}
// This function will get the event target in a browser-compatible way
function getEventTarget(e) {
	e = e || window.event;
	return e.target || e.srcElement;
}