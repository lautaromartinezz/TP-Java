<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.Monster" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Monsters</title>
<style>
	body { font-family: sans-serif; margin: 2rem; }
	table { border-collapse: collapse; margin-top: 1rem; }
	th, td { border: 1px solid #ccc; padding: 6px 12px; text-align: left; }
	th { background: #f0f0f0; }
	.error { color: #c00; font-weight: bold; }
	a, button { margin-right: 6px; }
</style>
</head>
<body>
	<h1>Lista de Monsters</h1>

	<% String error = (String) request.getAttribute("error"); %>
	<% if (error != null) { %>
		<p class="error"><%= error %></p>
	<% } %>

	<a href="<%= request.getContextPath() %>/monster?new=true">+ Nuevo monster</a>

	<table>
		<tr>
			<th>ID</th>
			<th>Nombre</th>
			<th>Acciones</th>
		</tr>
		<%
			LinkedList<Monster> monsters = (LinkedList<Monster>) request.getAttribute("monsters");
			if (monsters != null) {
				for (Monster m : monsters) {
				String safeMonsterName = m.getName()
					.replace("&", "&amp;")
					.replace("<", "&lt;")
					.replace(">", "&gt;")
					.replace("\"", "&quot;")
					.replace("'", "&#39;");
				String safeJsMonsterName = m.getName()
					.replace("\\", "\\\\")
					.replace("\"", "\\\"")
					.replace("'", "\\'")
					.replace("\r", "\\r")
					.replace("\n", "\\n");
		%>
		<tr>
			<td><%= m.getId() %></td>
			<td><%= safeMonsterName %></td>
			<td>
				<a href="<%= request.getContextPath() %>/monster?id=<%= m.getId() %>">Editar</a>
				<button type="button" onclick="eliminar('<%= m.getId() %>', '<%= safeJsMonsterName %>')">Eliminar</button>
			</td>
		</tr>
		<%
				}
			}
		%>
	</table>

	<script>
		function eliminar(id, name) {
			if (!confirm('Eliminar ' + name + '?')) { return; }
			fetch('<%= request.getContextPath() %>/monster?id=' + id, { method: 'DELETE' })
				.then(function() { location.href = '<%= request.getContextPath() %>/monster'; });
		}
	</script>
</body>
</html>
