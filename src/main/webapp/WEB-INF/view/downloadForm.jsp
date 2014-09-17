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
	<form:form method="post"
		modelAttribute="downloadDocument" action="fileDownload">
		<table>
			<tr>
				<td>Enter file name:</td>
				<td><input type="text" name="fileName" /></td>
				<td style="color: red; font-style: italic;"><form:errors
						path="file" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Download" /></td>
				<td></td>
			</tr>
		</table>
	</form:form>
</html>
