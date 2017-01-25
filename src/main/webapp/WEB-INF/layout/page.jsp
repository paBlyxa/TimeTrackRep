<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>TimeX</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css"
	href="<s:url value="/resources/style.css" />">
<script src="<c:url value="/resources/script/jquery-3.0.0.min.js" />"></script>

</head>
<body>
	<div class="header">
		<t:insertAttribute name="header" />
	</div>
	<div class="menuForm">
		<t:insertAttribute name="menu" />
	</div>
	<div class="pageBody">
		<t:insertAttribute name="body" />
	</div>
	<div>
		<t:insertAttribute name="footer" />
	</div>
</body>
</html>