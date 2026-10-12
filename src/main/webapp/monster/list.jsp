<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.Monster" %>
<%@ page import="util.HtmlUtil" %>
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
		<p class="error"><%= HtmlUtil.escapeHtml(error) %></p>
	<% } %>

	<a href="<%= request.getContextPath() %>/monster?new=true">+ Nuevo monster</a>
	<a href="<%= request.getContextPath() %>/monster?ranking=true">Ver ranking por energia</a>

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
				String safeNombre = HtmlUtil.escapeHtml(m.getNombre());
				String safeJsNombre = HtmlUtil.escapeJs(m.getNombre());
		%>
		<tr>
			<td><%= m.getId() %></td>
			<td><%= safeNombre %></td>
			<td>
				<a href="<%= request.getContextPath() %>/monster?id=<%= m.getId() %>">Editar</a>
				<button type="button" onclick="eliminar('<%= m.getId() %>', '<%= safeJsNombre %>')">Eliminar</button>
			</td>
		</tr>
		<%
				}
			}
		%>
	</table>

	<script>
		function eliminar(id, nombre) {
			if (!confirm('Eliminar ' + nombre + '?')) { return; }
			fetch('<%= request.getContextPath() %>/monster?id=' + id, { method: 'DELETE' })
				.then(function() { location.href = '<%= request.getContextPath() %>/monster'; });
		}
	</script>
</body>
</html>
