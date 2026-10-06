<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="entities.Monster" %>
<%
	Monster monster = (Monster) request.getAttribute("monster");
	boolean edit = monster != null && monster.getId() > 0;
	String ctx = request.getContextPath();
	String monsterName = edit ? monster.getName() : "";
	String safeHtmlName = monsterName
		.replace("&", "&amp;")
		.replace("<", "&lt;")
		.replace(">", "&gt;")
		.replace("\"", "&quot;")
		.replace("'", "&#39;");
	String safeJsName = monsterName
		.replace("\\", "\\\\")
		.replace("\"", "\\\"")
		.replace("'", "\\'")
		.replace("\r", "\\r")
		.replace("\n", "\\n");
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
		<label for="name">Nombre</label>
		<input type="text" id="name" name="name" value="<%= safeHtmlName %>" required>
		<br>
		<button type="submit"><%= edit ? "Guardar" : "Crear" %></button>
		<a href="<%= ctx %>/monster">Volver</a>
	</form>

	<script>
		var ctx = '<%= ctx %>';
		var edit = <%= edit %>;
		var id = <%= edit ? monster.getId() : 0 %>;
		var safeName = '<%= safeJsName %>';

		document.getElementById('form').addEventListener('submit', function(e) {
			e.preventDefault();
			var name = document.getElementById('name').value;
			var url = ctx + '/monster';
			if (edit) {
				url += '?id=' + id + '&name=' + encodeURIComponent(name);
			}
			fetch(url, {
				method: edit ? 'PUT' : 'POST',
				headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
				body: 'name=' + encodeURIComponent(name)
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
