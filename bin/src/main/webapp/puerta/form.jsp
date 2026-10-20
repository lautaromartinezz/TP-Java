<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="entities.Puerta" %>
<%@ page import="util.HtmlUtil" %>
<%
	Puerta puerta = (Puerta) request.getAttribute("puerta");
	boolean edit = puerta != null && puerta.getId() > 0;
	String ctx = request.getContextPath();
	String numero = edit ? puerta.getNumero() : "";
%>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title"><%= edit ? "Editar puerta #" + puerta.getId() : "Nueva puerta" %></h1>
</div>

<div class="card card-form">
	<form id="form" class="form" method="post" action="<%= ctx %>/puerta">
		<div class="form-group">
			<label class="form-label" for="numero">Numero</label>
			<input class="form-input" type="text" id="numero" name="numero" value="<%= HtmlUtil.escapeHtml(numero) %>" required>
		</div>
		<div class="alert alert-error" id="error" hidden></div>
		<div class="form-actions">
			<button type="submit" class="btn btn-primary">
				<i data-lucide="save" class="icon-sm"></i>
				<%= edit ? "Guardar" : "Crear" %>
			</button>
			<a href="<%= ctx %>/puerta" class="btn btn-ghost">Volver</a>
		</div>
	</form>
</div>

<script>
	var ctx = '<%= ctx %>';
	var edit = <%= edit %>;
	var id = <%= edit ? puerta.getId() : 0 %>;

	document.getElementById('form').addEventListener('submit', function(e) {
		e.preventDefault();
		var numero = document.getElementById('numero').value;
		var url = ctx + '/puerta';
		if (edit) {
			url += '?id=' + id;
		}
		fetch(url, {
			method: edit ? 'PUT' : 'POST',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			body: 'numero=' + encodeURIComponent(numero)
		}).then(function(res) {
			if (res.ok || res.redirected) {
				location.href = ctx + '/puerta';
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
