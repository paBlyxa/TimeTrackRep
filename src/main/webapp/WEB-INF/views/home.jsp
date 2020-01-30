<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>

<div class="login-container">
	<div class="login-card">
		<div class="login-form">


			<form name='login' action="<c:url value='/home' />" method='POST'
				class="form-horizontal">
				<c:if test="${param.error != null}">
					<div class="alert alert-danger">
						<c:choose>
						    <c:when test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
						       <p>${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
						    </c:when>    
						    <c:otherwise>
						       <p>Invalid username or password.</p>
						    </c:otherwise>
						</c:choose>
					</div>
				</c:if>
				<c:if test="${param.logout != null}">       
					<div class="alert alert-danger">
						<p>You have been logged out.</p>
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
				<c:if test="${empty loginUpdate}">
					<div class="input-group input-sm">
						<label class="input-group-addon" for="remember me" id="input-anchor"><i
						class="fa fa-anchor"></i></label>
						<label id="rememberMe-label" >Remember Me:</label>
						<span class="input-group-addon">
        					<input type="checkbox" name="remember-me" >
						</span>
					</div>
				</c:if>
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