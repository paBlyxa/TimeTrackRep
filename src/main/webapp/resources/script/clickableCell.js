/**
 * 
 */
$(document).ready(function() {
		$('.dateCell').dblclick(function() {
			var dataValue = dateFromStr($(this).attr("data-value"));
			var notInArray = true;
			datepicker.selectedDates.forEach(function(item, i, arr) {
				if (item.getDate() == dataValue.getDate()){
					notInArray = false;
				}
			});
			if (notInArray){
				datepicker.selectDate(dataValue);
			} else {
				datepicker.removeDate(dataValue);
			}
		});
		$('.projectCell').dblclick(function() {
			$('#selectProject')[0].sumo.selectItem($(this).attr("data-value"));
		});
		$('.taskCell').dblclick(function() {
			$('#selectTask')[0].sumo.selectItem($(this).attr("data-value"));
		});
	});

function dateFromStr(str){
	var parts = str.split('.');
	return new Date(parts[2], (parts[1]-1), parts[0]);
};