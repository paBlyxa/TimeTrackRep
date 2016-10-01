<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%> --%>
<head>
	<script src="<c:url value="/resources/script/clickableRow.js" />">
	</script>
</head>
<div class="projectForm">
  <h1>Новый проект</h1>
  <form:form method="POST" modelAttribute="projectForm">
	<table class="newTable">
		<thead>
			<tr>
				<th class="colProjectName">Проект</th>
				<th class="colActive">Статус</th>
				<th class="colComment">Комментарий</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><input class="input" type="text" name="name" /></td>
				<td>Актив</td>
				<td><form:input class="input" type="text" path="comment" /></td>
			</tr>
		</tbody>
		<tfoot></tfoot>
	</table>
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input type="submit" value="Добавить" />
  </form:form>
</div>

<div class="listTitle">
	<c:url var="deleteUrl" value="/projects/delete"/>  
  <h1>Проекты</h1>
  	<table id="header-fixed">
  	</table>
  
	<table class="mainTable" id="table-1">
		<thead>
			<tr>
				<th class="colProjectName">Проект</th>
				<th class="colProjectActive">Статус</th>
				<th class="colProjectComment">Комментарий</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="project" items="${projectList}" varStatus="status">
				<tr class="clickable-row" data-url="/TimeTrack/projects/project?id=<c:url value="${project.projectId}" />">
					<td><c:out value="${project.name}" /></td>
					<td>Актив</td>
					<td><c:out value="${project.comment}" /></td>	
					<td id="colLast">				  
						<form action="${deleteUrl}" method="POST">
						      <input name="projectId" type="hidden" value="${project.projectId}"/>
						      <input type="submit" value="Удалить" onClick="return confirm('Удалить запись?')"/>
						      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						</form>				
					</td>		
				</tr>
			</c:forEach>
		</tbody>
		<tfoot></tfoot>
	</table>
	
	
<!-- 		var tableOffset = $("#table-1").offset().top;
		var $header = $("#table-1 > thead").clone();
		var $fixedHeader = $("#header-fixed").append($header);
		
		("#header-fixed td").each(function(index){
		    var index2 = index;
		    $(this).width(function(index2){
		        return $("#table-1 td").eq(index).width();
		    });
		});
		
		$(window).bind("scroll", function() {
		    var offset = $(this).scrollTop();

		    if (offset >= (tableOffset - 50) && $fixedHeader.is(":hidden")) {
		        $fixedHeader.show();
		    }
		    else if (offset < (tableOffset - 50)) {
		        $fixedHeader.hide();
		    }
		});  -->

	
</div>
