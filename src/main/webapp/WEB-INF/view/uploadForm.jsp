<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>
<meta charset="utf-8">
<title>Welcome</title>
</head>
<body>
	<form:form method="post" enctype="multipart/form-data"
		modelAttribute="DbDocument" action="${pageContext.request.contextPath}/rs/findFiles">
		<table>
			<tr>
				<td>Upload File:</td>
				<td><input type="file" name="file" /></td>
				<td style="color: red; font-style: italic;"><form:errors
						path="file" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Upload" /></td>
				<td></td>
			</tr>
		</table>
	</form:form>
</html>
