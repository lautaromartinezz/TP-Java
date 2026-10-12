<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.Monster" %>
<%@ page import="entities.Puerta" %>
<%@ page import="entities.Resultado" %>
<%@ page import="util.HtmlUtil" %>
<%
	Resultado resultado = (Resultado) request.getAttribute("resultado");
	boolean edit = resultado != null && resultado.getId() > 0;
	boolean asustar = Boolean.TRUE.equals(request.getAttribute("asustar")) && !edit;
	String ctx = request.getContextPath();

	String fechaValor;
	if (edit) {
		fechaValor = resultado.getFechaInput();
	} else {
		fechaValor = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date());
	}
	String nombreNino = edit ? resultado.getNombreNino() : "";
	int idMonsterActual = edit && resultado.getMonster() != null ? resultado.getMonster().getId() : 0;
	int idPuertaActual = edit && resultado.getPuerta() != null ? resultado.getPuerta().getId() : 0;
	int energiaActual = edit ? resultado.getEnergiaGenerada() : 0;

	LinkedList<Monster> monsters = (LinkedList<Monster>) request.getAttribute("monsters");
	LinkedList<Puerta> puertas = (LinkedList<Puerta>) request.getAttribute("puertas");

	String titulo;
	if (edit) { titulo = "Editar Resultado #" + resultado.getId(); }
	else if (asustar) { titulo = "Asustar a un nino"; }
	else { titulo = "Nuevo Resultado"; }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= titulo %></title>
<style>
	body { font-family: sans-serif; margin: 2rem; }
	label { display: block; margin-bottom: 4px; font-weight: bold; }
	input, select { padding: 6px; width: 260px; }
	.info { color: #077; font-weight: bold; }
	.error { color: #c00; font-weight: bold; }
	button { margin-top: 12px; padding: 6px 14px; }
</style>
</head>
<body>
	<h1><%= titulo %></h1>

	<p id="error" class="error"></p>
	<% if (asustar) { %>
		<p class="info">La energia generada se calculara automaticamente al asustar.</p>
	<% } %>

	<form id="form" method="post" action="<%= ctx %>/resultado">
		<label for="fecha">Fecha</label>
		<input type="datetime-local" id="fecha" name="fecha" value="<%= fechaValor %>" required>
		<br>
		<label for="nombre_nino">Nombre del nino</label>
		<input type="text" id="nombre_nino" name="nombre_nino" value="<%= HtmlUtil.escapeHtml(nombreNino) %>" required>
		<br>
		<label for="id_monster">Monstruo</label>
		<select id="id_monster" name="id_monster" required>
			<option value="">-- Seleccione --</option>
			<%
				if (monsters != null) {
					for (Monster m : monsters) {
			%>
			<option value="<%= m.getId() %>" <%= (m.getId() == idMonsterActual ? "selected" : "") %>><%= HtmlUtil.escapeHtml(m.getNombre()) %></option>
			<%
					}
				}
			%>
		</select>
		<br>
		<label for="id_puerta">Puerta</label>
		<select id="id_puerta" name="id_puerta" required>
			<option value="">-- Seleccione --</option>
			<%
				if (puertas != null) {
					for (Puerta p : puertas) {
						String etiqueta = p.getNumero();
						if (p.getPuesto() != null) { etiqueta += " (" + p.getPuesto().getNombre() + ")"; }
			%>
			<option value="<%= p.getId() %>" <%= (p.getId() == idPuertaActual ? "selected" : "") %>><%= HtmlUtil.escapeHtml(etiqueta) %></option>
			<%
					}
				}
			%>
		</select>
		<br>
		<% if (!asustar) { %>
		<label for="energia_generada">Energia generada</label>
		<input type="number" id="energia_generada" name="energia_generada" min="0" value="<%= energiaActual %>">
		<br>
		<% } %>
		<button type="submit"><%= edit ? "Guardar" : (asustar ? "Asustar" : "Crear") %></button>
		<a href="<%= ctx %>/resultado">Volver</a>
	</form>

	<script>
		var ctx = '<%= ctx %>';
		var edit = <%= edit %>;
		var asustar = <%= asustar %>;
		var id = <%= edit ? resultado.getId() : 0 %>;

		document.getElementById('form').addEventListener('submit', function(e) {
			e.preventDefault();
			var fecha = document.getElementById('fecha').value;
			var nombreNino = document.getElementById('nombre_nino').value;
			var idMonster = document.getElementById('id_monster').value;
			var idPuerta = document.getElementById('id_puerta').value;
			if (!idMonster || !idPuerta) {
				document.getElementById('error').textContent = 'Seleccione monstruo y puerta';
				return;
			}
			var body = 'fecha=' + encodeURIComponent(fecha)
				+ '&nombre_nino=' + encodeURIComponent(nombreNino)
				+ '&id_monster=' + encodeURIComponent(idMonster)
				+ '&id_puerta=' + encodeURIComponent(idPuerta);
			if (!asustar) {
				body += '&energia_generada=' + encodeURIComponent(document.getElementById('energia_generada').value);
			}
			var url = ctx + '/resultado';
			if (edit) {
				url += '?id=' + id;
			}
			fetch(url, {
				method: edit ? 'PUT' : 'POST',
				headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
				body: body
			}).then(function(res) {
				if (res.ok || res.redirected) {
					location.href = ctx + '/resultado';
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
