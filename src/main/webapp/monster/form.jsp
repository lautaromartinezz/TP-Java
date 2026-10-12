<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="entities.Monster" %>
<%@ page import="util.HtmlUtil" %>
<%
	Monster monster = (Monster) request.getAttribute("monster");
	boolean edit = monster != null && monster.getId() > 0;
	String ctx = request.getContextPath();
	String monsterNombre = edit ? monster.getNombre() : "";
	String safeHtmlNombre = HtmlUtil.escapeHtml(monsterNombre);
	String safeJsNombre = HtmlUtil.escapeJs(monsterNombre);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= edit ? "Editar Monster" : "Nuevo Monster" %></title>
<style>
	body { font-family: sans-serif; margin: 2rem; }
	label { display: block; margin-bottom: 4px; font-weight: bold; }
	input { padding: 6px; width: 240px; }
	.error { color: #c00; font-weight: bold; }
	button { margin-top: 12px; padding: 6px 14px; }
</style>
</head>
<body>
	<h1><%= edit ? "Editar Monster #" + monster.getId() : "Nuevo Monster" %></h1>

	<p id="error" class="error"></p>

	<form id="form" method="post" action="<%= ctx %>/monster">
		<label for="nombre">Nombre</label>
		<input type="text" id="nombre" name="nombre" value="<%= safeHtmlNombre %>" required>
		<br>
		<button type="submit"><%= edit ? "Guardar" : "Crear" %></button>
		<a href="<%= ctx %>/monster">Volver</a>
	</form>

	<script>
		var ctx = '<%= ctx %>';
		var edit = <%= edit %>;
		var id = <%= edit ? monster.getId() : 0 %>;

		document.getElementById('form').addEventListener('submit', function(e) {
			e.preventDefault();
			var nombre = document.getElementById('nombre').value;
			var url = ctx + '/monster';
			if (edit) {
				url += '?id=' + id;
			}
			fetch(url, {
				method: edit ? 'PUT' : 'POST',
				headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
				body: 'nombre=' + encodeURIComponent(nombre)
			}).then(function(res) {
				if (res.ok || res.redirected) {
					location.href = ctx + '/monster';
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
