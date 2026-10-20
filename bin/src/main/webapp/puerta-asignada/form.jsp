<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.PuestoAsignado" %>
<%@ page import="entities.Puerta" %>
<%@ page import="entities.PuertaAsignada" %>
<%@ page import="util.HtmlUtil" %>
<%
	PuertaAsignada puertaAsignada = (PuertaAsignada) request.getAttribute("puertaAsignada");
	boolean edit = puertaAsignada != null && puertaAsignada.getId() > 0;
	String ctx = request.getContextPath();
	int idAsignacionActual = edit && puertaAsignada.getPuestoAsignado() != null ? puertaAsignada.getPuestoAsignado().getId() : 0;
	int idPuertaActual = edit && puertaAsignada.getPuerta() != null ? puertaAsignada.getPuerta().getId() : 0;
	LinkedList<PuestoAsignado> asignaciones = (LinkedList<PuestoAsignado>) request.getAttribute("asignaciones");
	LinkedList<Puerta> puertas = (LinkedList<Puerta>) request.getAttribute("puertas");
%>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title"><%= edit ? "Editar puerta asignada #" + puertaAsignada.getId() : "Nueva puerta asignada" %></h1>
</div>

<div class="card card-form">
	<form id="form" class="form" method="post" action="<%= ctx %>/puerta-asignada">
		<div class="form-group">
			<label class="form-label" for="id_puesto_asignado">Asignacion (monstruo - puesto - fecha)</label>
			<select class="form-select" id="id_puesto_asignado" name="id_puesto_asignado" required>
				<option value="">-- Seleccione --</option>
				<%
					if (asignaciones != null) {
						for (PuestoAsignado pa : asignaciones) {
							String etiqueta = "";
							if (pa.getMonster() != null) { etiqueta += pa.getMonster().getNombre(); }
							if (pa.getPuesto() != null) { etiqueta += " - " + pa.getPuesto().getNombre(); }
							etiqueta += " - " + pa.getFechaFormateada();
				%>
				<option value="<%= pa.getId() %>" <%= (pa.getId() == idAsignacionActual ? "selected" : "") %>><%= HtmlUtil.escapeHtml(etiqueta) %></option>
				<%
						}
					}
				%>
			</select>
		</div>
		<div class="form-group">
			<label class="form-label" for="id_puerta">Puerta</label>
			<select class="form-select" id="id_puerta" name="id_puerta" required>
				<option value="">-- Seleccione --</option>
				<%
					if (puertas != null) {
						for (Puerta p : puertas) {
				%>
				<option value="<%= p.getId() %>" <%= (p.getId() == idPuertaActual ? "selected" : "") %>><%= HtmlUtil.escapeHtml(p.getNumero()) %></option>
				<%
						}
					}
				%>
			</select>
		</div>
		<div class="alert alert-error" id="error" hidden></div>
		<div class="form-actions">
			<button type="submit" class="btn btn-primary">
				<i data-lucide="save" class="icon-sm"></i>
				<%= edit ? "Guardar" : "Crear" %>
			</button>
			<a href="<%= ctx %>/puerta-asignada" class="btn btn-ghost">Volver</a>
		</div>
	</form>
</div>

<script>
	var ctx = '<%= ctx %>';
	var edit = <%= edit %>;
	var id = <%= edit ? puertaAsignada.getId() : 0 %>;

	document.getElementById('form').addEventListener('submit', function(e) {
		e.preventDefault();
		var idAsignacion = document.getElementById('id_puesto_asignado').value;
		var idPuerta = document.getElementById('id_puerta').value;
		if (!idAsignacion || !idPuerta) {
			var error = document.getElementById('error');
			error.textContent = 'Seleccione asignacion y puerta';
			error.hidden = false;
			return;
		}
		var body = 'id_puesto_asignado=' + encodeURIComponent(idAsignacion)
			+ '&id_puerta=' + encodeURIComponent(idPuerta);
		var url = ctx + '/puerta-asignada';
		if (edit) {
			url += '?id=' + id;
		}
		fetch(url, {
			method: edit ? 'PUT' : 'POST',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			body: body
		}).then(function(res) {
			if (res.ok || res.redirected) {
				location.href = ctx + '/puerta-asignada';
			} else {
				return res.text().then(function(msg) {
					var error = document.getElementById('error');
					error.textContent = msg;
					error.hidden = false;
				});
			}
		});
	});
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
