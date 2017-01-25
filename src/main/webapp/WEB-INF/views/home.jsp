<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ page session="false" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<!DOCTYPE HTML>

<div class="login-container">
	<div class="login-card">
		<div class="login-form">


			<form name='login' action="<c:url value='/home' />" method='POST'
				class="form-horizontal">
				<c:if test="${param.error != null}">
					<div class="alert alert-danger">
						<p>Invalid username and password.</p>
					</div>
				</c:if>
				<c:if test="${not empty error}">
					<div>${error}</div>
				</c:if>
				<c:if test="${not empty message}">
					<div>${message}</div>
				</c:if>
				<div class="input-group input-sm">
					<label class="input-group-addon" for="username"><i
						class="fa fa-user"></i></label> <input type="text" class="form-control"
						id="username" name="username" placeholder="Enter Username"
						required>
				</div>
				<div class="input-group input-sm">
					<label class="input-group-addon" for="password"><i
						class="fa fa-lock"></i></label> <input type="password"
						class="form-control" id="password" name="password"
						placeholder="Enter Password" required>
				</div>

				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
				<div class="form-actions">
					<input type="submit" class="btn btn-block btn-primary btn-default"
						value="Log in">
				</div>
			</form>
		</div>
	</div>
</div>