<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.Monster" %>
<%@ page import="util.HtmlUtil" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ranking de Monsters</title>
<style>
	body { font-family: sans-serif; margin: 2rem; }
	table { border-collapse: collapse; margin-top: 1rem; }
	th, td { border: 1px solid #ccc; padding: 6px 12px; text-align: left; }
	th { background: #f0f0f0; }
	.rank { font-weight: bold; }
</style>
</head>
<body>
	<h1>Lista de monstruos por energia generada</h1>

	<a href="<%= request.getContextPath() %>/monster">Volver al listado</a>

	<table>
		<tr>
			<th>#</th>
			<th>ID</th>
			<th>Nombre</th>
			<th>Energia total</th>
		</tr>
		<%
			LinkedList<Monster> monsters = (LinkedList<Monster>) request.getAttribute("monsters");
			if (monsters != null) {
				int pos = 1;
				for (Monster m : monsters) {
		%>
		<tr>
			<td class="rank"><%= pos++ %></td>
			<td><%= m.getId() %></td>
			<td><%= HtmlUtil.escapeHtml(m.getNombre()) %></td>
			<td><%= m.getEnergiaTotal() %></td>
		</tr>
		<%
				}
			}
		%>
	</table>
</body>
</html>
