<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="entities.Monster" %>
<%@ page import="util.HtmlUtil" %>
<%
	Monster monster = (Monster) request.getAttribute("monster");
	boolean edit = monster != null && monster.getId() > 0;
	String ctx = request.getContextPath();
	String nombre = edit ? monster.getNombre() : "";
%>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title"><%= edit ? "Editar monstruo #" + monster.getId() : "Nuevo monstruo" %></h1>
</div>

<div class="card card-form">
	<form id="form" class="form" method="post" action="<%= ctx %>/monster">
		<div class="form-group">
			<label class="form-label" for="nombre">Nombre</label>
			<input class="form-input" type="text" id="nombre" name="nombre" value="<%= HtmlUtil.escapeHtml(nombre) %>" required>
		</div>
		<div class="alert alert-error" id="error" hidden></div>
		<div class="form-actions">
			<button type="submit" class="btn btn-primary">
				<i data-lucide="save" class="icon-sm"></i>
				<%= edit ? "Guardar" : "Crear" %>
			</button>
			<a href="<%= ctx %>/monster" class="btn btn-ghost">Volver</a>
		</div>
	</form>
</div>

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
					var error = document.getElementById('error');
					error.textContent = msg;
					error.hidden = false;
				});
			}
		});
	});
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
