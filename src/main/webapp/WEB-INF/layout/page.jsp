<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>TimeX</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css"	href="<s:url value="/resources/style.css" />">
<script src="<c:url value="/resources/script/jquery-3.0.0.min.js" />"></script>
<link rel="shortcut icon" href="<c:url value="/resources/" />images/favicon.ico" />
<link rel="stylesheet" href="<c:url value="/resources/fonts/font-awesome-4.7.0/css/font-awesome.min.css"/>">
<meta http-equiv="refresh" CONTENT="<%= session.getMaxInactiveInterval() %>;">
 
</head>
<body>
	<div class="header">
		<t:insertAttribute name="header" />
	</div>
	<div class="menuForm">
		<t:insertAttribute name="menu" />
	</div>
	<div id="myModal" class="modal">
	  <!-- Modal content -->
		  <div class="modal-content">
			  <div class="modal-header">
			    <span class="close">&times;</span>
				<c:if test="${not empty message}">
			    	<h2 id="header" ><c:out value = "${message.name}"/></h2>
				</c:if>
			  </div>
			  <div class="modal-body">
				<c:if test="${not empty message}">
				    <p id="messageText"><c:out value = "${message.text}"/></p>
				    <button type="button" class="collapsible">Подробнее:</button>
				    <div class="content">
				    	<p><c:out value = "${message.info}"/></p>
				    </div>
				    
			    </c:if>
			  </div>	  
		  </div>
	</div>
	<div class="pageBody">
		<t:insertAttribute name="body" />
	</div>
	<div>
		<t:insertAttribute name="footer" />
	</div>
	<script type="text/javascript">
		var messageType = null;
		<c:if test="${not empty message}">
			messageType = "${message.type}";
		</c:if>
		// Get the modal
		var modal = document.getElementById("myModal");
		
		$(document).ready(function() {
			if (messageType != null && (messageType.length > 0)){
				if (messageType === "OK"){
					document.getElementsByClassName("modal-header")[0].style.backgroundColor = "green";
				} else {
					document.getElementsByClassName("modal-header")[0].style.backgroundColor = "red";
				}
				modal.style.display = "block";
				
				document.getElementsByClassName("collapsible")[0].onclick = function() {
					this.classList.toggle("activeAdd");
					var content = this.nextElementSibling;
				    if (content.style.display === "block") {
				      content.style.display = "none";
				    } else {
				      content.style.display = "block";
				    }
				}
			}
			document.getElementsByClassName("close")[0].onclick = function(){
				modal.style.display = "none";
			}
			window.onclick = function(event) {
				if (event.target == modal) {
				    modal.style.display = "none";
				}
			}
		});
	</script>
</body>
</html>