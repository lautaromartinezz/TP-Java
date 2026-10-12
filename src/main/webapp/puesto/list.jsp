<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.Puesto" %>
<%@ page import="util.HtmlUtil" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Puestos</title>
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
	<h1>Lista de Puestos</h1>

	<% String error = (String) request.getAttribute("error"); %>
	<% if (error != null) { %>
		<p class="error"><%= HtmlUtil.escapeHtml(error) %></p>
	<% } %>

	<a href="<%= request.getContextPath() %>/puesto?new=true">+ Nuevo puesto</a>

	<table>
		<tr>
			<th>ID</th>
			<th>Nombre</th>
			<th>Descripcion</th>
			<th>Acciones</th>
		</tr>
		<%
			LinkedList<Puesto> puestos = (LinkedList<Puesto>) request.getAttribute("puestos");
			if (puestos != null) {
				for (Puesto p : puestos) {
		%>
		<tr>
			<td><%= p.getId() %></td>
			<td><%= HtmlUtil.escapeHtml(p.getNombre()) %></td>
			<td><%= HtmlUtil.escapeHtml(p.getDescripcion()) %></td>
			<td>
				<a href="<%= request.getContextPath() %>/puesto?id=<%= p.getId() %>">Editar</a>
				<button type="button" onclick="eliminar('<%= p.getId() %>', '<%= HtmlUtil.escapeJs(p.getNombre()) %>')">Eliminar</button>
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
			fetch('<%= request.getContextPath() %>/puesto?id=' + id, { method: 'DELETE' })
				.then(function() { location.href = '<%= request.getContextPath() %>/puesto'; });
		}
	</script>
</body>
</html>
