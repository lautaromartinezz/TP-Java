<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.Puerta" %>
<%@ page import="util.HtmlUtil" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Puertas</title>
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
	<h1>Lista de Puertas</h1>

	<% String error = (String) request.getAttribute("error"); %>
	<% if (error != null) { %>
		<p class="error"><%= HtmlUtil.escapeHtml(error) %></p>
	<% } %>

	<a href="<%= request.getContextPath() %>/puerta?new=true">+ Nueva puerta</a>

	<table>
		<tr>
			<th>ID</th>
			<th>Numero</th>
			<th>Puesto</th>
			<th>Acciones</th>
		</tr>
		<%
			LinkedList<Puerta> puertas = (LinkedList<Puerta>) request.getAttribute("puertas");
			if (puertas != null) {
				for (Puerta p : puertas) {
		%>
		<tr>
			<td><%= p.getId() %></td>
			<td><%= HtmlUtil.escapeHtml(p.getNumero()) %></td>
			<td><%= HtmlUtil.escapeHtml(p.getPuesto() != null ? p.getPuesto().getNombre() : "") %></td>
			<td>
				<a href="<%= request.getContextPath() %>/puerta?id=<%= p.getId() %>">Editar</a>
				<button type="button" onclick="eliminar('<%= p.getId() %>', '<%= HtmlUtil.escapeJs(p.getNumero()) %>')">Eliminar</button>
			</td>
		</tr>
		<%
				}
			}
		%>
	</table>

	<script>
		function eliminar(id, numero) {
			if (!confirm('Eliminar puerta ' + numero + '?')) { return; }
			fetch('<%= request.getContextPath() %>/puerta?id=' + id, { method: 'DELETE' })
				.then(function() { location.href = '<%= request.getContextPath() %>/puerta'; });
		}
	</script>
</body>
</html>
