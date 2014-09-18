<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>Welcome</title>
	</head> 
	<body>
		<p><c:url value="/ui/uploadForm" var="uploadUrl" />
		<a href="${uploadUrl}">Upload File</a></p>
		<p><c:url value="/ui/downloadForm" var="downloadUrl" />
		<a href="${downloadUrl}">Download File</a></p>
		
	</body>

</html>
