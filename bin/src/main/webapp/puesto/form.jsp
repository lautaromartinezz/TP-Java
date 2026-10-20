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
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title"><%= edit ? "Editar puesto #" + puesto.getId() : "Nuevo puesto" %></h1>
</div>

<div class="card card-form">
	<form id="form" class="form" method="post" action="<%= ctx %>/puesto">
		<div class="form-group">
			<label class="form-label" for="nombre">Nombre</label>
			<input class="form-input" type="text" id="nombre" name="nombre" value="<%= HtmlUtil.escapeHtml(nombre) %>" required>
		</div>
		<div class="form-group">
			<label class="form-label" for="descripcion">Descripcion</label>
			<textarea class="form-textarea" id="descripcion" name="descripcion"><%= HtmlUtil.escapeHtml(descripcion) %></textarea>
		</div>
		<div class="alert alert-error" id="error" hidden></div>
		<div class="form-actions">
			<button type="submit" class="btn btn-primary">
				<i data-lucide="save" class="icon-sm"></i>
				<%= edit ? "Guardar" : "Crear" %>
			</button>
			<a href="<%= ctx %>/puesto" class="btn btn-ghost">Volver</a>
		</div>
	</form>
</div>

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
					var error = document.getElementById('error');
					error.textContent = msg;
					error.hidden = false;
				});
			}
		});
	});
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
