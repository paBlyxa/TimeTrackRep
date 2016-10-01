<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

 <hr />
<p>Текущий пользователь: <sec:authentication property="principal.employee.name"/> <sec:authentication property="principal.employee.surname"/></p>