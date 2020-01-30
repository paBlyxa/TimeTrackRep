<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%-- <%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%> --%>
<head>
	<script src="<c:url value="/resources/script/clickableRow.js" />"></script>
	<script src="<c:url value="/resources/script/tabs.js" />"></script>
	<script src="<c:url value="/resources/script/FilterTable.js" />"></script>
<style type="text/css">
	.divWithBorder {min-width: 930px;}
</style>
</head>

<div class="divWithBorder">
	<button class="tablink" onclick="openPage('now', this)" id="defaultOpen">Текущие проекты</button>
	<button class="tablink" onclick="openPage('old', this)">Завершенные проекты</button>	
	<div class="tablink"></div>
	<div class="input-group">
		<i class="fa fa-search" aria-hidden="true"></i>
		<input type="search" class="light-table-filter" data-table="order-table" placeholder="Поиск">
	</div>
	<div id="now" class="tabcontent">
		<table class="order-table table">
			<thead>
				<tr>
					<th class="colProjectName">Проект</th>
					<th class="colProjectContract">Номер договора</th>
					<th class="colProjectDate">Дата заключения</th>
					<th class="colProjectDate">Дата окончания</th>
					<th class="colProjectLeaders">Ведущие сотрудники</th>
					<th class="colProjectComment">Комментарий</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="project" items="${projectList}" varStatus="status">
					<c:if test="${project.isActive()}">
						<tr class="clickable-d-row" data-url-d="<c:url value="/"/>projects/project?id=<c:out value="${project.projectId}" />">
							<td>${project.name}</td>
							<td>${project.contract}</td>
							<fmt:parseDate value="${project.startDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
							<fmt:formatDate pattern="dd.MM.yyyy" value="${parsedDate}" var="dayDate"/>
							<td data-value="${dayDate}">
								<c:out value="${dayDate}"/>
							</td>
							<fmt:parseDate value="${project.endDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
							<fmt:formatDate pattern="dd.MM.yyyy" value="${parsedDate}" var="dayDate"/>
							<td data-value="${dayDate}">
								<c:out value="${dayDate}"/>
							</td>
							<td><c:forEach var="leader" items="${project.managers}" varStatus="stat"><c:if test="${stat.index > 0}">, </c:if><c:out value="${leader.shortName}"/></c:forEach></td>
							<td><c:out value="${project.comment}" /></td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
			<tfoot></tfoot>
		</table>
	</div>
	<div id="old" class="tabcontent">
		<table class="order-table table">
			<thead>
				<tr>
					<th class="colProjectName">Проект</th>
					<th class="colProjectContract">Номер договора</th>
					<th class="colProjectDate">Дата заключения</th>
					<th class="colProjectDate">Дата окончания</th>
					<th class="colProjectLeaders">Ведущие сотрудники</th>
					<th class="colProjectComment">Комментарий</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="project" items="${projectList}" varStatus="status">
					<c:if test="${!project.isActive()}">
						<tr class="clickable-d-row" data-url-d="<c:url value="/"/>projects/project?id=<c:out value="${project.projectId}" />">
							<td>${project.name}</td>
							<td>${project.contract}</td>
							<fmt:parseDate value="${project.startDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
							<fmt:formatDate pattern="dd.MM.yyyy" value="${parsedDate}" var="dayDate"/>
							<td data-value="${dayDate}">
								<c:out value="${dayDate}"/>
							</td>
							<fmt:parseDate value="${project.endDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
							<fmt:formatDate pattern="dd.MM.yyyy" value="${parsedDate}" var="dayDate"/>
							<td data-value="${dayDate}">
								<c:out value="${dayDate}"/>
							</td>
							<td><c:forEach var="leader" items="${project.managers}" varStatus="stat"><c:if test="${stat.index > 0}">, </c:if><c:out value="${leader.shortName}"/></c:forEach></td>
							<td><c:out value="${project.comment}" /></td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
			<tfoot></tfoot>
		</table>	
	</div>
</div>
