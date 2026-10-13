<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.Monster" %>
<%@ page import="entities.Puesto" %>
<%@ page import="entities.PuestoAsignado" %>
<%@ page import="util.HtmlUtil" %>
<%
	PuestoAsignado asignacion = (PuestoAsignado) request.getAttribute("asignacion");
	boolean edit = asignacion != null && asignacion.getId() > 0;
	String ctx = request.getContextPath();
	String fechaValor = edit ? asignacion.getFechaInput() : "";
	int idMonsterActual = edit && asignacion.getMonster() != null ? asignacion.getMonster().getId() : 0;
	int idPuestoActual = edit && asignacion.getPuesto() != null ? asignacion.getPuesto().getId() : 0;
	LinkedList<Monster> monsters = (LinkedList<Monster>) request.getAttribute("monsters");
	LinkedList<Puesto> puestos = (LinkedList<Puesto>) request.getAttribute("puestos");
%>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title"><%= edit ? "Editar asignacion #" + asignacion.getId() : "Nueva asignacion" %></h1>
</div>

<div class="card card-form">
	<form id="form" class="form" method="post" action="<%= ctx %>/puesto-asignado">
		<div class="form-group">
			<label class="form-label" for="fecha">Fecha</label>
			<input class="form-input" type="date" id="fecha" name="fecha" value="<%= fechaValor %>" required>
		</div>
		<div class="form-group">
			<label class="form-label" for="id_monster">Monstruo</label>
			<select class="form-select" id="id_monster" name="id_monster" required>
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
		</div>
		<div class="form-group">
			<label class="form-label" for="id_puesto">Puesto</label>
			<select class="form-select" id="id_puesto" name="id_puesto" required>
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
		</div>
		<div class="alert alert-error" id="error" hidden></div>
		<div class="form-actions">
			<button type="submit" class="btn btn-primary">
				<i data-lucide="save" class="icon-sm"></i>
				<%= edit ? "Guardar" : "Crear" %>
			</button>
			<a href="<%= ctx %>/puesto-asignado" class="btn btn-ghost">Volver</a>
		</div>
	</form>
</div>

<script>
	var ctx = '<%= ctx %>';
	var edit = <%= edit %>;
	var id = <%= edit ? asignacion.getId() : 0 %>;

	document.getElementById('form').addEventListener('submit', function(e) {
		e.preventDefault();
		var fecha = document.getElementById('fecha').value;
		var idMonster = document.getElementById('id_monster').value;
		var idPuesto = document.getElementById('id_puesto').value;
		if (!idMonster || !idPuesto) {
			var error = document.getElementById('error');
			error.textContent = 'Seleccione monstruo y puesto';
			error.hidden = false;
			return;
		}
		var body = 'fecha=' + encodeURIComponent(fecha)
			+ '&id_monster=' + encodeURIComponent(idMonster)
			+ '&id_puesto=' + encodeURIComponent(idPuesto);
		var url = ctx + '/puesto-asignado';
		if (edit) {
			url += '?id=' + id;
		}
		fetch(url, {
			method: edit ? 'PUT' : 'POST',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			body: body
		}).then(function(res) {
			if (res.ok || res.redirected) {
				location.href = ctx + '/puesto-asignado';
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
