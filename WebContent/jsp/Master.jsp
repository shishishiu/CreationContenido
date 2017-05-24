<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> --%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>${param.title}</title>
				
	</head>
	<body>
		<div id="container">
		<jsp:include page="Header.jsp"/>
		<c:if test="${param.mostrarMenu != false}">
			<jsp:include page="Menu.jsp"/>
		</c:if>
		<div id="main">
		${param.content} 
		</div>
		<jsp:include page="Footer.jsp"/>
		</div>
	</body>
</html>