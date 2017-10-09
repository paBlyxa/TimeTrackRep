<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<head>

	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	
	<meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<title>Basic initialization</title>

<script
	src="<c:url value="/resources/script/myGantt.js" />"></script>
<link href="<c:url value="/resources/dhtmlxgantt.css"/>"
		rel="stylesheet" type="text/css">

<style type="text/css">
		html, body{ height:100%; padding:0px; margin:0px; overflow: hidden;}
		    
	.complex_gantt_bar{
		background: transparent;
		border:none;
	}
	.complex_gantt_bar .gantt_task_progress{
		display:none;
	}
	.pageBody {
		padding:0px;
		margin:0px;
	}
	</style></head>
	
<body class="">

	<div id="gantt_here" style="width:100%; height:100%;padding:0px; margin:0px;">

	</div>
	
	<sec:authentication var="employee" property="principal" />
	
<script>
var startDate = new Date();
var employeeId = "<c:out value="${employee.employeeId}"/>";

	$(document).ready(function() {
		
		console.log(employeeId);
		gantt.create($("#gantt_here")[0], startDate, employeeId);
	});


</script>
