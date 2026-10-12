<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.Resultado" %>
<%@ page import="util.HtmlUtil" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Resultados</title>
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
	<h1>Lista de Resultados</h1>

	<% String error = (String) request.getAttribute("error"); %>
	<% if (error != null) { %>
		<p class="error"><%= HtmlUtil.escapeHtml(error) %></p>
	<% } %>

	<a href="<%= request.getContextPath() %>/resultado?new=true">+ Nuevo resultado</a>
	<a href="<%= request.getContextPath() %>/resultado?asustar=true">Asustar a un nino</a>

	<table>
		<tr>
			<th>ID</th>
			<th>Fecha</th>
			<th>Nino</th>
			<th>Monstruo</th>
			<th>Puerta</th>
			<th>Puesto</th>
			<th>Energia</th>
			<th>Acciones</th>
		</tr>
		<%
			LinkedList<Resultado> resultados = (LinkedList<Resultado>) request.getAttribute("resultados");
			if (resultados != null) {
				for (Resultado r : resultados) {
		%>
		<tr>
			<td><%= r.getId() %></td>
			<td><%= r.getFechaFormateada() %></td>
			<td><%= HtmlUtil.escapeHtml(r.getNombreNino()) %></td>
			<td><%= HtmlUtil.escapeHtml(r.getMonster() != null ? r.getMonster().getNombre() : "") %></td>
			<td><%= HtmlUtil.escapeHtml(r.getPuerta() != null ? r.getPuerta().getNumero() : "") %></td>
			<td><%= HtmlUtil.escapeHtml(r.getPuerta() != null && r.getPuerta().getPuesto() != null ? r.getPuerta().getPuesto().getNombre() : "") %></td>
			<td><%= r.getEnergiaGenerada() %></td>
			<td>
				<a href="<%= request.getContextPath() %>/resultado?id=<%= r.getId() %>">Editar</a>
				<button type="button" onclick="eliminar('<%= r.getId() %>')">Eliminar</button>
			</td>
		</tr>
		<%
				}
			}
		%>
	</table>

	<script>
		function eliminar(id) {
			if (!confirm('Eliminar resultado #' + id + '?')) { return; }
			fetch('<%= request.getContextPath() %>/resultado?id=' + id, { method: 'DELETE' })
				.then(function() { location.href = '<%= request.getContextPath() %>/resultado'; });
		}
	</script>
</body>
</html>
