<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.Puesto" %>
<%@ page import="entities.Puerta" %>
<%@ page import="util.HtmlUtil" %>
<%
	Puerta puerta = (Puerta) request.getAttribute("puerta");
	boolean edit = puerta != null && puerta.getId() > 0;
	String ctx = request.getContextPath();
	String numero = edit ? puerta.getNumero() : "";
	int idPuestoActual = edit && puerta.getPuesto() != null ? puerta.getPuesto().getId() : 0;
	LinkedList<Puesto> puestos = (LinkedList<Puesto>) request.getAttribute("puestos");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= edit ? "Editar Puerta" : "Nueva Puerta" %></title>
<style>
	body { font-family: sans-serif; margin: 2rem; }
	label { display: block; margin-bottom: 4px; font-weight: bold; }
	input, select { padding: 6px; width: 240px; }
	.error { color: #c00; font-weight: bold; }
	button { margin-top: 12px; padding: 6px 14px; }
</style>
</head>
<body>
	<h1><%= edit ? "Editar Puerta #" + puerta.getId() : "Nueva Puerta" %></h1>

	<p id="error" class="error"></p>

	<form id="form" method="post" action="<%= ctx %>/puerta">
		<label for="numero">Numero</label>
		<input type="text" id="numero" name="numero" value="<%= HtmlUtil.escapeHtml(numero) %>" required>
		<br>
		<label for="id_puesto">Puesto</label>
		<select id="id_puesto" name="id_puesto" required>
			<option value="">-- Seleccione --</option>
			<%
				if (puestos != null) {
					for (Puesto p : puestos) {
			%>
			<option value="<%= p.getId() %>" <%= (p.getId() == idPuestoActual ? "selected" : "") %>><%= HtmlUtil.escapeHtml(p.getNombre()) %></option>
			<%
					}
				}
			%>
		</select>
		<br>
		<button type="submit"><%= edit ? "Guardar" : "Crear" %></button>
		<a href="<%= ctx %>/puerta">Volver</a>
	</form>

	<script>
		var ctx = '<%= ctx %>';
		var edit = <%= edit %>;
		var id = <%= edit ? puerta.getId() : 0 %>;

		document.getElementById('form').addEventListener('submit', function(e) {
			e.preventDefault();
			var numero = document.getElementById('numero').value;
			var idPuesto = document.getElementById('id_puesto').value;
			if (!idPuesto) {
				document.getElementById('error').textContent = 'Seleccione un puesto';
				return;
			}
			var url = ctx + '/puerta';
			if (edit) {
				url += '?id=' + id;
			}
			fetch(url, {
				method: edit ? 'PUT' : 'POST',
				headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
				body: 'numero=' + encodeURIComponent(numero)
					+ '&id_puesto=' + encodeURIComponent(idPuesto)
			}).then(function(res) {
				if (res.ok || res.redirected) {
					location.href = ctx + '/puerta';
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
