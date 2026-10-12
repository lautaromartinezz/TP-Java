<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="entities.Puesto" %>
<%@ page import="util.HtmlUtil" %>
<%
	Puesto puesto = (Puesto) request.getAttribute("puesto");
	boolean edit = puesto != null && puesto.getId() > 0;
	String ctx = request.getContextPath();
	String nombre = edit ? puesto.getNombre() : "";
	String descripcion = edit && puesto.getDescripcion() != null ? puesto.getDescripcion() : "";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= edit ? "Editar Puesto" : "Nuevo Puesto" %></title>
<style>
	body { font-family: sans-serif; margin: 2rem; }
	label { display: block; margin-bottom: 4px; font-weight: bold; }
	input, textarea { padding: 6px; width: 240px; }
	.error { color: #c00; font-weight: bold; }
	button { margin-top: 12px; padding: 6px 14px; }
</style>
</head>
<body>
	<h1><%= edit ? "Editar Puesto #" + puesto.getId() : "Nuevo Puesto" %></h1>

	<p id="error" class="error"></p>

	<form id="form" method="post" action="<%= ctx %>/puesto">
		<label for="nombre">Nombre</label>
		<input type="text" id="nombre" name="nombre" value="<%= HtmlUtil.escapeHtml(nombre) %>" required>
		<br>
		<label for="descripcion">Descripcion</label>
		<textarea id="descripcion" name="descripcion"><%= HtmlUtil.escapeHtml(descripcion) %></textarea>
		<br>
		<button type="submit"><%= edit ? "Guardar" : "Crear" %></button>
		<a href="<%= ctx %>/puesto">Volver</a>
	</form>

	<script>
		var ctx = '<%= ctx %>';
		var edit = <%= edit %>;
		var id = <%= edit ? puesto.getId() : 0 %>;

		document.getElementById('form').addEventListener('submit', function(e) {
			e.preventDefault();
			var nombre = document.getElementById('nombre').value;
			var descripcion = document.getElementById('descripcion').value;
			var url = ctx + '/puesto';
			if (edit) {
				url += '?id=' + id;
			}
			fetch(url, {
				method: edit ? 'PUT' : 'POST',
				headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
				body: 'nombre=' + encodeURIComponent(nombre)
					+ '&descripcion=' + encodeURIComponent(descripcion)
			}).then(function(res) {
				if (res.ok || res.redirected) {
					location.href = ctx + '/puesto';
				} else {
					return res.text().then(function(msg) {
						document.getElementById('error').textContent = msg;
					});
				}
			});
		});
	</script>
</body>
</html>
